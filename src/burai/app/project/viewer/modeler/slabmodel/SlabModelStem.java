/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.modeler.slabmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import burai.app.project.viewer.modeler.ModelerBase;
import burai.atoms.model.Atom;
import burai.atoms.model.Cell;
import burai.atoms.model.exception.ZeroVolumCellException;
import burai.com.math.Lattice;
import burai.com.math.Matrix3D;

public class SlabModelStem extends SlabModel {

    private static final double DET_THR = 1.0e-8;
    private static final double PACK_THR = 1.0e-6; // internal coordinate
    private static final double CART_THR = 1.0e-4; // angstrom

    private static final double DEFAULT_VACUUM = 10.0; // angstrom

    private static final double STEP_FOR_GENOMS = 0.5; // angstrom

    private int miller1;
    private int miller2;
    private int miller3;

    private int intercept1;
    private int intercept2;
    private int intercept3;
    private int numIntercept;
    private boolean hasIntercept1;
    private boolean hasIntercept2;
    private boolean hasIntercept3;

    private int[] vector1;
    private int[] vector2;
    private int[] vector3;

    private int[][] boundBox;

    private double[] lattConst;
    private double[][] lattCart;
    private double[][] lattSlab;

    private List<AtomEntry> entryUnit;
    private List<AtomEntry> entryAll;

    /**
     * cell is not changed.
     */
    public SlabModelStem(Cell cell, int h, int k, int l) throws MillerIndexException {
        super(0.0, DEFAULT_VACUUM);

        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        if (!this.setupMillers(cell, h, k, l)) {
            throw new MillerIndexException();
        }

        if (!this.setupUnitAtoms(cell)) {
            throw new MillerIndexException();
        }

        if (!this.setupAllAtoms()) {
            throw new MillerIndexException();
        }
    }

    @Override
    public SlabModel[] getSlabModels() {
        if (this.lattCart == null || this.lattCart.length < 3) {
            return new SlabModel[] { new SlabModelLeaf(this, this.offset, this.vacuum) };
        }
        if (this.lattCart[2] == null || this.lattCart[2].length < 3) {
            return new SlabModel[] { new SlabModelLeaf(this, this.offset, this.vacuum) };
        }

        int nstep = (int) (this.lattCart[2][2] / STEP_FOR_GENOMS);
        if (nstep < 2) {
            return new SlabModel[] { new SlabModelLeaf(this, this.offset, this.vacuum) };
        }

        Map<SlabGenom, Double> slabGenoms = new LinkedHashMap<SlabGenom, Double>();

        for (int i = 0; i < nstep; i++) {
            double offset = ((double) i) / ((double) nstep);
            SlabGenom slabGenom = this.getSlabGenom(offset);
            if (slabGenom != null && !(slabGenoms.containsKey(slabGenom))) {
                slabGenoms.put(slabGenom, offset);
            }
        }

        if (slabGenoms.isEmpty()) {
            return new SlabModel[] { new SlabModelLeaf(this, this.offset, this.vacuum) };
        }

        int index = 0;
        SlabModel[] slabModels = new SlabModel[slabGenoms.size()];
        for (double offset : slabGenoms.values()) {
            slabModels[index] = new SlabModelLeaf(this, offset, this.vacuum);
            index++;
        }

        return slabModels;
    }

    private SlabGenom getSlabGenom(double offset) {
        if (this.lattCart == null || this.lattCart.length < 3) {
            return null;
        }
        if (this.lattCart[2] == null || this.lattCart[2].length < 3) {
            return null;
        }

        if (this.entryAll == null || this.entryAll.isEmpty()) {
            return null;
        }

        int natom = this.entryAll.size();
        int iatom = natom;

        String[] names = new String[natom];
        double[] coords = new double[natom];

        for (int i = 0; i < natom; i++) {
            AtomEntry entry = this.entryAll.get(i);
            if (entry == null) {
                return null;
            }

            double c1 = entry.c + offset;
            double c2 = c1;
            c2 -= Math.floor(c2);

            double dc = Math.abs(c2 - 1.0);
            double dz = dc * this.lattCart[2][2];
            if (dz < CART_THR) {
                c2 -= 1.0;
            }

            dz = Math.abs(c1 - c2) * this.lattCart[2][2];
            if (iatom >= natom && dz > CART_THR) {
                iatom = i;
            }

            names[i] = entry.name;
            coords[i] = c2 * this.lattCart[2][2];
        }

        String[] names2 = new String[natom];
        double[] coords2 = new double[natom];

        int index = 0;
        for (int i = iatom; i < natom; i++) {
            names2[index] = names[i];
            coords2[index] = coords[i];
            index++;
        }

        for (int i = 0; i < iatom; i++) {
            names2[index] = names[i];
            coords2[index] = coords[i];
            index++;
        }

        return new SlabGenom(names2, coords2);
    }

    @Override
    public boolean updateCell(Cell cell) {
        return this.updateCell(cell, this.offset, this.vacuum);
    }

    protected boolean updateCell(Cell cell, double offset, double vacuum) {
        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        if (!this.setupSlabModel(offset, vacuum)) {
            return false;
        }

        if (this.lattSlab == null || this.lattSlab.length < 3) {
            return false;
        }

        if (this.entryAll == null || this.entryAll.isEmpty()) {
            return false;
        }

        cell.stopResolving();

        try {
            cell.moveLattice(this.lattSlab);
        } catch (ZeroVolumCellException e) {
            cell.restartResolving();
            return false;
        }

        int natom = this.entryAll.size();
        int natom2 = cell.numAtoms(true);

        Atom[] refAtoms = null;
        if (natom == natom2) {
            refAtoms = cell.listAtoms(true);
        }

        if (refAtoms != null && refAtoms.length >= natom) {
            for (int i = 0; i < natom; i++) {
                AtomEntry entry = this.entryAll.get(i);
                if (entry == null) {
                    continue;
                }

                String name = entry.name;
                if (name == null || name.isEmpty()) {
                    continue;
                }

                Atom atom = refAtoms[i];
                if (atom == null) {
                    cell.addAtom(new Atom(name, entry.x, entry.y, entry.z));

                } else {
                    String name2 = atom.getName();
                    if (!name.equals(name2)) {
                        atom.setName(name);
                    }

                    double x2 = atom.getX();
                    double y2 = atom.getY();
                    double z2 = atom.getZ();
                    if (Math.abs(entry.x - x2) > 0.0 || Math.abs(entry.y - y2) > 0.0 || Math.abs(entry.z - z2) > 0.0) {
                        atom.moveTo(entry.x, entry.y, entry.z);
                    }
                }
            }

        } else {
            cell.removeAllAtoms();

            for (AtomEntry entry : this.entryAll) {
                if (entry == null) {
                    continue;
                }

                String name = entry.name;
                if (name == null || name.isEmpty()) {
                    continue;
                }

                cell.addAtom(new Atom(name, entry.x, entry.y, entry.z));
            }
        }

        cell.restartResolving();
        return true;
    }

    private static class AtomEntry implements Comparable<AtomEntry> {
        private double[][] lattice;

        public String name;
        public double a;
        public double b;
        public double c;
        public double x;
        public double y;
        public double z;

        public AtomEntry(double[][] lattice) {
            if (lattice == null) {
                throw new IllegalArgumentException("lattice is null.");
            }

            this.lattice = lattice;
            this.name = null;
            this.a = 0.0;
            this.b = 0.0;
            this.c = 0.0;
            this.x = 0.0;
            this.y = 0.0;
            this.z = 0.0;
        }

        @Override
        public int compareTo(AtomEntry other) {
            if (other == null) {
                return -1;
            }

            double dx;
            double dy;
            double dz;
            double rr;

            double dc = this.c - other.c;
            dx = dc * this.lattice[2][0];
            dy = dc * this.lattice[2][1];
            dz = dc * this.lattice[2][2];
            rr = dx * dx + dy * dy + dz * dz;
            if (rr > CART_THR * CART_THR) {
                if (dc > 0.0) {
                    return 1;
                } else {
                    return -1;
                }
            }

            double db = this.b - other.b;
            dx = db * this.lattice[1][0];
            dy = db * this.lattice[1][1];
            dz = db * this.lattice[1][2];
            rr = dx * dx + dy * dy + dz * dz;
            if (rr > CART_THR * CART_THR) {
                if (db > 0.0) {
                    return 1;
                } else {
                    return -1;
                }
            }

            double da = this.a - other.a;
            dx = da * this.lattice[0][0];
            dy = da * this.lattice[0][1];
            dz = da * this.lattice[0][2];
            rr = dx * dx + dy * dy + dz * dz;
            if (rr > CART_THR * CART_THR) {
                if (da > 0.0) {
                    return 1;
                } else {
                    return -1;
                }
            }

            if (this.name == null) {
                if (other.name == null) {
                    return 0;
                } else {
                    return 1;
                }
            }

            return this.name.compareTo(other.name);
        }

        @Override
        public int hashCode() {
            return this.name == null ? 0 : this.name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }

            AtomEntry other = (AtomEntry) obj;
            if (this.name == null) {
                if (this.name != other.name) {
                    return false;
                }
            } else {
                if (!this.name.equals(other.name)) {
                    return false;
                }
            }

            double da = Math.abs(this.a - other.a);
            da = Math.min(da, Math.abs(this.a - other.a + Math.signum(0.5 - this.a)));

            double db = Math.abs(this.b - other.b);
            db = Math.min(db, Math.abs(this.b - other.b + Math.signum(0.5 - this.b)));

            double dc = Math.abs(this.c - other.c);
            dc = Math.min(dc, Math.abs(this.c - other.c + Math.signum(0.5 - this.c)));

            double dx = da * this.lattice[0][0] + db * this.lattice[1][0] + dc * this.lattice[2][0];
            double dy = da * this.lattice[0][1] + db * this.lattice[1][1] + dc * this.lattice[2][1];
            double dz = da * this.lattice[0][2] + db * this.lattice[1][2] + dc * this.lattice[2][2];
            double rr = dx * dx + dy * dy + dz * dz;
            if (rr > CART_THR * CART_THR) {
                return false;
            }

            return true;
        }
    }

    private boolean setupSlabModel(double offset, double vacuum) {
        // lattice
        if (this.lattCart == null) {
            return false;
        }

        this.lattSlab = Matrix3D.copy(this.lattCart);
        if (this.lattSlab == null || this.lattSlab.length < 3) {
            return false;
        }
        if (this.lattSlab[0] == null || this.lattSlab[0].length < 3) {
            return false;
        }
        if (this.lattSlab[1] == null || this.lattSlab[1].length < 3) {
            return false;
        }
        if (this.lattSlab[2] == null || this.lattSlab[2].length < 3) {
            return false;
        }

        double zSlab = this.lattSlab[2][2];
        double zTotal = zSlab + 2.0 * vacuum;
        double zScale = zSlab == 0.0 ? 1.0 : (zTotal / zSlab);
        this.lattSlab[2] = Matrix3D.mult(zScale, this.lattSlab[2]);
        if (this.lattSlab == null || lattSlab.length < 3) {
            return false;
        }

        // atoms
        if (this.entryAll == null || this.entryAll.isEmpty()) {
            return false;
        }

        for (AtomEntry entry : this.entryAll) {
            if (entry == null) {
                continue;
            }

            double a = entry.a;
            double b = entry.b;
            double c = entry.c + offset;
            c -= Math.floor(c);

            double dc = Math.abs(c - 1.0);
            double dz = dc * this.lattCart[2][2];
            if (dz < CART_THR) {
                c -= 1.0;
            }

            entry.x = a * this.lattCart[0][0] + b * this.lattCart[1][0] + c * this.lattCart[2][0];
            entry.y = a * this.lattCart[0][1] + b * this.lattCart[1][1] + c * this.lattCart[2][1];
            entry.z = a * this.lattCart[0][2] + b * this.lattCart[1][2] + c * this.lattCart[2][2];
        }

        double zMax = 0.0;
        double zMin = 0.0;
        boolean zFirst = true;
        for (AtomEntry entry : this.entryAll) {
            if (entry == null) {
                continue;
            }

            if (zFirst) {
                zFirst = false;
                zMax = entry.z;
                zMin = entry.z;
            } else {
                zMax = Math.max(zMax, entry.z);
                zMin = Math.min(zMin, entry.z);
            }
        }

        double zScale2 = zSlab == 0.0 ? 1.0 : (0.5 * (zMax + zMin) / zSlab);
        double tx = 0.5 * this.lattSlab[2][0] - zScale2 * this.lattCart[2][0];
        double ty = 0.5 * this.lattSlab[2][1] - zScale2 * this.lattCart[2][1];
        double tz = 0.5 * this.lattSlab[2][2] - zScale2 * this.lattCart[2][2];

        for (AtomEntry entry : this.entryAll) {
            if (entry == null) {
                continue;
            }

            entry.x += tx;
            entry.y += ty;
            entry.z += tz;
        }

        return true;
    }

    private boolean setupUnitAtoms(Cell cell) {
        if (cell == null) {
            return false;
        }

        Atom[] atoms = cell.listAtoms(true);
        if (atoms == null || atoms.length < 1) {
            return false;
        }

        this.entryUnit = new ArrayList<AtomEntry>();

        for (Atom atom : atoms) {
            if (atom == null) {
                return false;
            }

            String name = atom.getName();
            if (name == null || name.isEmpty()) {
                return false;
            }

            double x = atom.getX();
            double y = atom.getY();
            double z = atom.getZ();
            double[] coord = cell.convertToLatticePosition(x, y, z);
            if (coord == null || coord.length < 3) {
                return false;
            }

            double[][] lattice = cell.copyLattice();
            if (lattice != null) {
                AtomEntry entry = new AtomEntry(lattice);
                entry.name = name;
                entry.a = coord[0];
                entry.b = coord[1];
                entry.c = coord[2];
                this.entryUnit.add(entry);
            }
        }

        return true;
    }

    private boolean setupAllAtoms() {
        int natom = this.entryUnit.size();
        if (natom < 1) {
            return false;
        }

        double[][] lattInt = new double[3][];
        lattInt[0] = new double[] { (double) this.vector1[0], (double) this.vector1[1], (double) this.vector1[2] };
        lattInt[1] = new double[] { (double) this.vector2[0], (double) this.vector2[1], (double) this.vector2[2] };
        lattInt[2] = new double[] { (double) this.vector3[0], (double) this.vector3[1], (double) this.vector3[2] };

        double detLatt = Matrix3D.determinant(lattInt);
        if (Math.abs(detLatt) < DET_THR) {
            System.err.println("volume is zero.");
            return false;
        }
        if (detLatt < 0.0) {
            System.err.println("parity is incorrect.");
            return false;
        }

        double detLatt2 = Math.rint(detLatt);
        if (Math.abs(detLatt - detLatt2) >= DET_THR) {
            System.err.println("not integer volume of lattice.");
            return false;
        }

        int nsize = (int) (detLatt2 + 0.1);
        if ((nsize * natom) >= ModelerBase.maxNumAtoms()) {
            return false;
        }

        double[][] invLatt = Matrix3D.inverse(lattInt);
        if (invLatt == null || invLatt.length < 3) {
            return false;
        }
        if (invLatt[0] == null || invLatt[0].length < 3) {
            return false;
        }
        if (invLatt[1] == null || invLatt[1].length < 3) {
            return false;
        }
        if (invLatt[2] == null || invLatt[2].length < 3) {
            return false;
        }

        this.entryAll = new ArrayList<AtomEntry>();

        for (int ia = this.boundBox[0][0]; ia <= this.boundBox[0][1]; ia++) {
            double ta = (double) ia;
            for (int ib = this.boundBox[1][0]; ib <= this.boundBox[1][1]; ib++) {
                double tb = (double) ib;
                for (int ic = this.boundBox[2][0]; ic <= this.boundBox[2][1]; ic++) {
                    double tc = (double) ic;

                    for (AtomEntry entry : this.entryUnit) {
                        double a = entry.a + ta;
                        double b = entry.b + tb;
                        double c = entry.c + tc;
                        double a2 = a * invLatt[0][0] + b * invLatt[1][0] + c * invLatt[2][0];
                        double b2 = a * invLatt[0][1] + b * invLatt[1][1] + c * invLatt[2][1];
                        double c2 = a * invLatt[0][2] + b * invLatt[1][2] + c * invLatt[2][2];

                        if (-PACK_THR <= a2 && a2 < (1.0 + PACK_THR) &&
                                -PACK_THR <= b2 && b2 < (1.0 + PACK_THR) &&
                                -PACK_THR <= c2 && c2 < (1.0 + PACK_THR)) {

                            AtomEntry entry2 = new AtomEntry(this.lattCart);
                            entry2.name = entry.name;
                            entry2.a = a2;
                            entry2.b = b2;
                            entry2.c = c2;
                            if (!this.entryAll.contains(entry2)) {
                                this.entryAll.add(entry2);
                            }
                        }
                    }
                }
            }
        }

        if (this.entryAll.isEmpty()) {
            return false;
        }

        for (AtomEntry entry : this.entryAll) {
            entry.a -= Math.floor(entry.a);
            entry.b -= Math.floor(entry.b);
            entry.c -= Math.floor(entry.c);

            double dc = Math.abs(entry.c - 1.0);
            double dz = dc * this.lattCart[2][2];
            if (dz < CART_THR) {
                entry.c -= 1.0;
            }
        }

        Collections.sort(this.entryAll);

        return true;
    }

    private boolean setupMillers(Cell cell, int h, int k, int l) {
        if (cell == null) {
            return false;
        }

        if (h == 0 && k == 0 && l == 0) {
            return false;
        }

        this.miller1 = h;
        this.miller2 = k;
        this.miller3 = l;

        if (!this.setupIntercepts()) {
            return false;
        }

        if (!this.setupVectors()) {
            return false;
        }

        if (!this.setupBoundaryBox()) {
            return false;
        }

        if (!this.setupLattice(cell)) {
            return false;
        }

        return true;
    }

    private boolean setupIntercepts() {
        int scaleMin = 1;
        int scaleMax = 1;
        this.numIntercept = 0;

        if (this.miller1 != 0) {
            scaleMin = Math.max(scaleMin, Math.abs(this.miller1));
            scaleMax *= Math.abs(this.miller1);
            this.numIntercept++;
            this.hasIntercept1 = true;
        } else {
            this.hasIntercept1 = false;
        }

        if (this.miller2 != 0) {
            scaleMin = Math.max(scaleMin, Math.abs(this.miller2));
            scaleMax *= Math.abs(this.miller2);
            this.numIntercept++;
            this.hasIntercept2 = true;
        } else {
            this.hasIntercept2 = false;
        }

        if (this.miller3 != 0) {
            scaleMin = Math.max(scaleMin, Math.abs(this.miller3));
            scaleMax *= Math.abs(this.miller3);
            this.numIntercept++;
            this.hasIntercept3 = true;
        } else {
            this.hasIntercept3 = false;
        }

        if (scaleMin < 1) {
            System.err.println("scaleMin is not positive.");
            return false;
        }

        if (scaleMax < scaleMin) {
            System.err.println("scaleMax < scaleMin.");
            return false;
        }

        if (this.numIntercept < 1) {
            System.err.println("there are no intercepts.");
            return false;
        }

        int scale = 0;
        for (int i = scaleMin; i <= scaleMax; i++) {
            if (this.hasIntercept1 && (i % this.miller1) != 0) {
                continue;
            }
            if (this.hasIntercept2 && (i % this.miller2) != 0) {
                continue;
            }
            if (this.hasIntercept3 && (i % this.miller3) != 0) {
                continue;
            }

            scale = i;
            break;
        }

        if (scale < 1) {
            System.err.println("cannot detect scale.");
            return false;
        }

        this.intercept1 = this.hasIntercept1 ? (scale / this.miller1) : 0;
        this.intercept2 = this.hasIntercept2 ? (scale / this.miller2) : 0;
        this.intercept3 = this.hasIntercept3 ? (scale / this.miller3) : 0;

        return true;
    }

    private boolean setupVectors() {
        this.vector1 = new int[] { 0, 0, 0 };
        this.vector2 = new int[] { 0, 0, 0 };
        this.vector3 = new int[] { 0, 0, 0 };

        if (this.numIntercept <= 1) {
            this.setupVectors1();
        } else if (this.numIntercept <= 2) {
            this.setupVectors2();
        } else {
            this.setupVectors3();
        }

        return true;
    }

    private void setupVectors1() {
        if (this.hasIntercept1) {
            if (this.intercept1 > 0) {
                this.vector1[1] = 1;
                this.vector2[2] = 1;
                this.vector3[0] = 1;
            } else {
                this.vector1[2] = 1;
                this.vector2[1] = 1;
                this.vector3[0] = -1;
            }

        } else if (this.hasIntercept2) {
            if (this.intercept2 > 0) {
                this.vector1[2] = 1;
                this.vector2[0] = 1;
                this.vector3[1] = 1;
            } else {
                this.vector1[0] = 1;
                this.vector2[2] = 1;
                this.vector3[1] = -1;
            }

        } else if (this.hasIntercept3) {
            if (this.intercept3 > 0) {
                this.vector1[0] = 1;
                this.vector2[1] = 1;
                this.vector3[2] = 1;
            } else {
                this.vector1[1] = 1;
                this.vector2[0] = 1;
                this.vector3[2] = -1;
            }
        }
    }

    private void setupVectors2() {
        if (!this.hasIntercept3) { // cat in A-B plane
            int sign1 = Integer.signum(this.intercept1);
            int sign2 = Integer.signum(this.intercept2);
            this.vector1[2] = sign1 * sign2;
            this.vector2[0] = +this.intercept1;
            this.vector2[1] = -this.intercept2;
            this.vector3[0] = sign1;
            this.vector3[1] = sign2;

        } else if (!this.hasIntercept2) { // cat in A-C plane
            int sign1 = Integer.signum(this.intercept1);
            int sign3 = Integer.signum(this.intercept3);
            this.vector1[1] = sign1 * sign3;
            this.vector2[0] = -this.intercept1;
            this.vector2[2] = +this.intercept3;
            this.vector3[0] = sign1;
            this.vector3[2] = sign3;

        } else if (!this.hasIntercept1) { // cat in B-C plane
            int sign2 = Integer.signum(this.intercept2);
            int sign3 = Integer.signum(this.intercept3);
            this.vector1[0] = sign2 * sign3;
            this.vector2[1] = +this.intercept2;
            this.vector2[2] = -this.intercept3;
            this.vector3[1] = sign2;
            this.vector3[2] = sign3;
        }
    }

    private void setupVectors3() {
        int sign1 = Integer.signum(this.intercept1);
        int sign2 = Integer.signum(this.intercept2);
        int sign3 = Integer.signum(this.intercept3);
        if (sign3 > 0) {
            this.vector1[1] = +sign1 * this.intercept2;
            this.vector1[2] = -sign1 * this.intercept3;
            this.vector2[0] = -sign2 * this.intercept1;
            this.vector2[2] = +sign2 * this.intercept3;
        } else {
            this.vector1[0] = -sign1 * this.intercept1;
            this.vector1[2] = +sign1 * this.intercept3;
            this.vector2[1] = +sign2 * this.intercept2;
            this.vector2[2] = -sign2 * this.intercept3;
        }
        this.vector3[0] = sign1;
        this.vector3[1] = sign2;
        this.vector3[2] = sign3;
    }

    private boolean setupBoundaryBox() {
        this.boundBox = new int[3][2];

        for (int i = 0; i < 3; i++) {
            this.boundBox[i][0] = 0;
            this.boundBox[i][1] = 0;

            if (this.vector1[i] < 0) {
                this.boundBox[i][0] += this.vector1[i];
            } else {
                this.boundBox[i][1] += this.vector1[i];
            }

            if (this.vector2[i] < 0) {
                this.boundBox[i][0] += this.vector2[i];
            } else {
                this.boundBox[i][1] += this.vector2[i];
            }

            if (this.vector3[i] < 0) {
                this.boundBox[i][0] += this.vector3[i];
            } else {
                this.boundBox[i][1] += this.vector3[i];
            }
        }

        return true;
    }

    private boolean setupLattice(Cell cell) {
        if (cell == null) {
            return false;
        }

        double[][] lattInt = new double[3][];
        lattInt[0] = new double[] { (double) this.vector1[0], (double) this.vector1[1], (double) this.vector1[2] };
        lattInt[1] = new double[] { (double) this.vector2[0], (double) this.vector2[1], (double) this.vector2[2] };
        lattInt[2] = new double[] { (double) this.vector3[0], (double) this.vector3[1], (double) this.vector3[2] };

        double[][] lattCart0 = Matrix3D.mult(lattInt, cell.copyLattice());
        this.lattConst = lattCart0 == null ? null : Lattice.getCellDm(14, lattCart0);
        if (this.lattConst == null || this.lattConst.length < 6) {
            return false;
        }

        this.lattCart = Lattice.getCell(14, this.lattConst);
        if (this.lattCart == null || this.lattCart.length < 3) {
            return false;
        }
        if (this.lattCart[0] == null || this.lattCart[0].length < 3) {
            return false;
        }
        if (this.lattCart[1] == null || this.lattCart[1].length < 3) {
            return false;
        }
        if (this.lattCart[2] == null || this.lattCart[2].length < 3) {
            return false;
        }

        return true;
    }
}
