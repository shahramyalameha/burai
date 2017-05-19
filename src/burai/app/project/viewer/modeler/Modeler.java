/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.modeler;

import burai.atoms.model.Atom;
import burai.atoms.model.AtomProperty;
import burai.atoms.model.Cell;
import burai.atoms.model.exception.ZeroVolumCellException;
import burai.atoms.viewer.AtomsViewer;
import burai.com.consts.ConstantAtoms;
import burai.com.math.Matrix3D;

public class Modeler {

    protected static final int MAX_NUM_ATOMS = ConstantAtoms.MAX_NUM_ATOMS;

    private static final double RMIN = 1.0e-4;
    private static final double RRMIN = RMIN * RMIN;

    private Cell srcCell;
    private Cell dstCell;

    private AtomsViewer atomsViewer;

    private double aOffset;
    private double bOffset;
    private double cOffset;
    private CellOffsetChanged onCellOffsetChanged;

    public Modeler(Cell srcCell) {
        if (srcCell == null) {
            throw new IllegalArgumentException("srcCell is null.");
        }

        this.srcCell = srcCell;
        this.dstCell = null;

        this.atomsViewer = null;

        this.aOffset = 0.0;
        this.bOffset = 0.0;
        this.cOffset = 0.0;
        this.onCellOffsetChanged = null;

        this.copyCellForward();
    }

    public Cell getCell() {
        return this.dstCell;
    }

    protected void setAtomsViewer(AtomsViewer atomsViewer) {
        this.atomsViewer = atomsViewer;
    }

    public void initialize() {
        if (this.atomsViewer != null) {
            this.atomsViewer.storeCell();
        }

        this.copyCellForward();

        if (this.atomsViewer != null) {
            this.atomsViewer.setCellToCenter();
        }

        this.setCellOffset(0.0, 0.0, 0.0);
    }

    public void reflect() {
        this.copyCellBackward();
    }

    public void undo() {
        if (this.atomsViewer != null) {
            this.atomsViewer.restoreCell();
        }
    }

    public void redo() {
        if (this.atomsViewer != null) {
            this.atomsViewer.subRestoreCell();
        }
    }

    public void setOnCellOffsetChanged(CellOffsetChanged onCellOffsetChanged) {
        this.onCellOffsetChanged = onCellOffsetChanged;
    }

    private void setCellOffset(double a, double b, double c) {
        this.aOffset = a;
        this.bOffset = b;
        this.cOffset = c;

        if (this.onCellOffsetChanged != null) {
            this.onCellOffsetChanged.onCellOffsetChanged(a, b, c);
        }
    }

    public void translateCell(double a, double b, double c) {
        if (this.dstCell == null) {
            return;
        }

        double da = a - this.aOffset;
        double db = b - this.bOffset;
        double dc = c - this.cOffset;
        if (Math.abs(da) <= 0.0 && Math.abs(db) <= 0.0 && Math.abs(dc) <= 0.0) {
            return;
        }

        double[] trans = this.dstCell.convertToCartesianPosition(da, db, dc);
        if (trans == null || trans.length < 3) {
            return;
        }

        double dx = trans[0];
        double dy = trans[1];
        double dz = trans[2];
        double rr = dx * dx + dy * dy + dz * dz;
        if (rr <= RRMIN) {
            return;
        }

        Atom[] atoms = this.dstCell.listAtoms(true);
        if (atoms != null) {
            //this.dstCell.stopResolving();
            for (Atom atom : atoms) {
                if (atom != null) {
                    atom.moveBy(dx, dy, dz);
                }
            }
            //this.dstCell.restartResolving();
        }

        this.setCellOffset(a, b, c);
    }

    public boolean buildSuperCell(int na, int nb, int nc) {
        SuperCellBuilder builder = this.dstCell == null ? null : new SuperCellBuilder(this.dstCell);
        if (builder == null) {
            return false;
        }

        if (this.atomsViewer != null) {
            this.atomsViewer.storeCell();
        }

        boolean status = builder.build(na, nb, nc);

        if (status && this.atomsViewer != null) {
            this.atomsViewer.setCellToCenter();
        }

        if (status) {
            this.setCellOffset(0.0, 0.0, 0.0);
        }

        return status;
    }

    public boolean buildSlabModel(int i, int j, int k) {
        SlabModelBuilder builder = this.dstCell == null ? null : new SlabModelBuilder(this.dstCell);
        if (builder == null) {
            return false;
        }

        if (this.atomsViewer != null) {
            this.atomsViewer.storeCell();
        }

        boolean status = builder.build(i, j, k);

        if (status && this.atomsViewer != null) {
            this.atomsViewer.setCellToCenter();
        }

        if (status) {
            this.setCellOffset(0.0, 0.0, 0.0);
        }

        return status;
    }

    public boolean isToReflect() {
        if (this.srcCell == null || this.dstCell == null) {
            return false;
        }

        // if srcCell is same as dstCell -> not to be reflected
        // if srcCell is different from dstCell -> to be reflected

        double[][] srcLattice = this.srcCell.copyLattice();
        double[][] dstLattice = this.srcCell.copyLattice();
        if (!Matrix3D.equals(srcLattice, dstLattice)) {
            return true;
        }

        int srcNAtom = this.srcCell.numAtoms(true);
        int dstNAtom = this.dstCell.numAtoms(true);
        if (srcNAtom != dstNAtom) {
            return true;
        }

        Atom[] srcAtoms = this.srcCell.listAtoms(true);
        Atom[] dstAtoms = this.dstCell.listAtoms(true);
        if (srcAtoms == null || dstAtoms == null) {
            return false;
        }

        int natom = srcAtoms.length;
        for (int i = 0; i < natom; i++) {
            Atom atom1 = srcAtoms[i];
            Atom atom2 = dstAtoms[i];
            if (atom1 == null || atom2 == null) {
                return false;
            }

            String name1 = atom1.getName();
            String name2 = atom2.getName();
            if (name1 == null || name2 == null) {
                return false;
            }
            if (!name1.equals(name2)) {
                return true;
            }

            double x1 = atom1.getX();
            double y1 = atom1.getY();
            double z1 = atom1.getZ();
            double x2 = atom2.getX();
            double y2 = atom2.getY();
            double z2 = atom2.getZ();
            double dx = x1 - x2;
            double dy = y1 - y2;
            double dz = z1 - z2;
            double rr = dx * dx + dy * dy + dz * dz;
            if (rr > RRMIN) {
                return true;
            }
        }

        return false;
    }

    private void copyCellForward() {
        if (this.dstCell != null) {
            this.dstCell.removeAllAtoms();
            this.dstCell.stopResolving();
        }

        // setup lattice
        double[][] lattice = this.srcCell.copyLattice();
        if (lattice == null || lattice.length < 3) {
            lattice = Matrix3D.unit();
        }

        try {
            if (this.dstCell == null) {
                this.dstCell = new Cell(lattice);
                this.dstCell.stopResolving();
            } else {
                this.dstCell.moveLattice(lattice);
            }

        } catch (ZeroVolumCellException e1) {
            e1.printStackTrace();

            try {
                if (this.dstCell == null) {
                    this.dstCell = new Cell(Matrix3D.unit());
                    this.dstCell.stopResolving();
                } else {
                    this.dstCell.moveLattice(Matrix3D.unit());
                }

            } catch (ZeroVolumCellException e2) {
                e2.printStackTrace();
            }
        }

        // setup atoms
        Atom[] atoms = this.srcCell.listAtoms(true);
        if (atoms != null) {
            for (Atom atom : atoms) {
                if (atom == null) {
                    continue;
                }

                String name = atom.getName();
                double x = atom.getX();
                double y = atom.getY();
                double z = atom.getZ();
                boolean xFixed = atom.booleanProperty(AtomProperty.FIXED_X);
                boolean yFixed = atom.booleanProperty(AtomProperty.FIXED_Y);
                boolean zFixed = atom.booleanProperty(AtomProperty.FIXED_Z);

                if (name != null && !name.isEmpty()) {
                    Atom atom_ = new Atom(name, x, y, z);
                    atom_.setProperty(AtomProperty.FIXED_X, xFixed);
                    atom_.setProperty(AtomProperty.FIXED_Y, yFixed);
                    atom_.setProperty(AtomProperty.FIXED_Z, zFixed);
                    this.dstCell.addAtom(atom_);
                }
            }
        }

        this.dstCell.restartResolving();
    }

    private void copyCellBackward() {
        if (this.dstCell == null) {
            return;
        }

        this.srcCell.removeAllAtoms();
        this.srcCell.stopResolving();

        // setup lattice
        double[][] lattice = this.dstCell.copyLattice();
        if (lattice == null || lattice.length < 3) {
            lattice = Matrix3D.unit();
        }

        try {
            this.srcCell.moveLattice(lattice);

        } catch (ZeroVolumCellException e) {
            e.printStackTrace();
            this.srcCell.restartResolving();
            return;
        }

        // setup atoms
        Atom[] atoms = this.dstCell.listAtoms(true);
        if (atoms != null) {
            for (Atom atom : atoms) {
                if (atom == null) {
                    continue;
                }

                String name = atom.getName();
                double x = atom.getX();
                double y = atom.getY();
                double z = atom.getZ();
                boolean xFixed = atom.booleanProperty(AtomProperty.FIXED_X);
                boolean yFixed = atom.booleanProperty(AtomProperty.FIXED_Y);
                boolean zFixed = atom.booleanProperty(AtomProperty.FIXED_Z);

                if (name != null && !name.isEmpty()) {
                    Atom atom_ = new Atom(name, x, y, z);
                    atom_.setProperty(AtomProperty.FIXED_X, xFixed);
                    atom_.setProperty(AtomProperty.FIXED_Y, yFixed);
                    atom_.setProperty(AtomProperty.FIXED_Z, zFixed);
                    this.srcCell.addAtom(atom_);
                }
            }
        }

        this.srcCell.restartResolving();
    }
}
