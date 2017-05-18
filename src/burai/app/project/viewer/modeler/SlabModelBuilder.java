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
import burai.atoms.model.Cell;

public class SlabModelBuilder {

    private Cell cell;

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

    private String[] names;
    private double[][] coord;

    protected SlabModelBuilder(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        this.cell = cell;
    }

    protected boolean build(int h, int k, int l) {
        if (!this.setupMillers(h, k, l)) {
            return false;
        }

        if (!this.setupAtoms()) {
            return false;
        }

        // TODO

        return true;
    }

    private boolean setupAtoms() {
        Atom[] atoms = this.cell.listAtoms(true);
        if (atoms == null || atoms.length < 1) {
            return false;
        }

        this.names = new String[atoms.length];
        this.coord = new double[atoms.length][];

        for (int i = 0; i < atoms.length; i++) {
            Atom atom = atoms[i];
            if (atom == null) {
                return false;
            }
            this.names[i] = atom.getName();
            this.coord[i] = this.cell.convertToLatticePosition(atom.getX(), atom.getY(), atom.getZ());
        }

        return true;
    }

    private boolean setupMillers(int h, int k, int l) {
        if (h == 0 && k == 0 && l == 0) {
            return false;
        }

        this.miller1 = h;
        this.miller2 = k;
        this.miller3 = l;

        try {
            this.setupIntercepts();
        } catch (RuntimeException e) {
            return false;
        }

        this.setupVectors();

        return true;
    }

    private void setupIntercepts() throws RuntimeException {
        int scaleMin = 0;
        int scaleMax = 0;
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
            throw new RuntimeException("scaleMin is not positive.");
        }

        if (scaleMax < scaleMin) {
            throw new RuntimeException("scaleMax < scaleMin.");
        }

        if (this.numIntercept < 1) {
            throw new RuntimeException("there are no intercepts.");
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
            throw new RuntimeException("cannot detect scale.");
        }

        this.intercept1 = scale / this.miller1;
        this.intercept2 = scale / this.miller2;
        this.intercept3 = scale / this.miller3;
    }

    private void setupVectors() {
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
        this.vector1[1] = +this.intercept2;
        this.vector1[2] = -this.intercept3;
        this.vector2[0] = -this.intercept1;
        this.vector2[2] = +this.intercept3;
        this.vector3[0] = sign1;
        this.vector3[1] = sign2;
        this.vector3[2] = sign3;
    }
}
