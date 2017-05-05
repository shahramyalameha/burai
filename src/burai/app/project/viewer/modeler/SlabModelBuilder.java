/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.modeler;

import burai.atoms.model.Cell;
import burai.com.math.Matrix3D;

public class SlabModelBuilder {

    private static final double MIN_VOLUME = 1.0e-6;

    private Cell cell;

    protected SlabModelBuilder(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        this.cell = cell;
    }

    protected boolean build(int i, int j, int k) {
        double[][] lattice = this.cell.copyLattice();
        if (lattice == null || lattice.length < 3) {
            return false;
        }

        double volume = Matrix3D.determinant(lattice);
        if (volume < MIN_VOLUME) {
            return false;
        }

        double[][] invLattice = Matrix3D.inverse(lattice);
        if (invLattice == null || invLattice.length < 3) {
            return false;
        }

        double[] center = this.createCenter(lattice);
        if (center == null || center.length < 3) {
            return false;
        }

        double ri = (double) i;
        double rj = (double) j;
        double rk = (double) k;

        double[][] plane = new double[6][4];

        // button-plane
        plane[0][0] = ri;
        plane[0][1] = rj;
        plane[0][2] = rk;
        plane[0][3] = 0.0;
        this.convertCartesianPlane(plane[0], invLattice, center);

        // top-plane
        plane[0][0] = ri;
        plane[0][1] = rj;
        plane[0][2] = rk;
        plane[0][3] = ri + rj + rk;
        this.convertCartesianPlane(plane[1], invLattice, center);

        // TODO
        return false;
    }

    private double[] createCenter(double[][] lattice) {
        double[] center = { 0.0, 0.0, 0.0 };
        for (int i = 0; i < 3; i++) {
            center[0] += lattice[i][0];
            center[1] += lattice[i][1];
            center[2] += lattice[i][2];
        }

        center[0] *= 0.5;
        center[1] *= 0.5;
        center[2] *= 0.5;
        return center;
    }

    private void convertCartesianPlane(double[] plane, double[][] invLattice, double[] center) {
        // crystal coordinate -> cartesian coordinate
        double[] plane2 = Matrix3D.mult(invLattice, plane);
        if (plane2 == null || plane2.length < 3) {
            return;
        }

        plane[0] = plane2[0];
        plane[1] = plane2[1];
        plane[2] = plane2[2];

        // correct direction
        double value = Matrix3D.mult(plane, center);

        if (value > plane[3]) {
            plane[0] *= -1.0;
            plane[1] *= -1.0;
            plane[2] *= -1.0;
        }
    }
}
