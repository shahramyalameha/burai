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
import burai.atoms.model.exception.ZeroVolumCellException;
import burai.com.math.Matrix3D;

public class SuperCellBuilder {

    private Cell cell;

    protected SuperCellBuilder(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        this.cell = cell;
    }

    protected boolean build(int na, int nb, int nc) {
        if (na < 1 || nb < 1 || nc < 1) {
            return false;
        }

        int natom = this.cell.numAtoms(true);
        natom *= (na * nb * nc);
        if (natom >= Modeler.MAX_NUM_ATOMS) {
            return false;
        }

        Atom[] atoms = this.cell.listAtoms(true);
        this.cell.removeAllAtoms();
        this.cell.stopResolving();

        try {

            // expand lattice
            double[][] lattice = this.cell.copyLattice();
            if (lattice == null || lattice.length < 3) {
                return false;
            }

            double[][] lattice_ = Matrix3D.copy(lattice);
            if (lattice_ == null || lattice_.length < 3) {
                return false;
            }

            for (int i = 0; i < 3; i++) {
                lattice_[0][i] *= (double) na;
                lattice_[1][i] *= (double) nb;
                lattice_[2][i] *= (double) nc;
            }

            try {
                this.cell.moveLattice(lattice_);
            } catch (ZeroVolumCellException e) {
                e.printStackTrace();
                return false;
            }

            // fill with atoms
            if (atoms == null || atoms.length < 1) {
                return true;
            }

            for (int ia = 0; ia < na; ia++) {
                double ra = (double) ia;
                for (int ib = 0; ib < nb; ib++) {
                    double rb = (double) ib;
                    for (int ic = 0; ic < nc; ic++) {
                        double rc = (double) ic;

                        double tx = ra * lattice[0][0] + rb * lattice[1][0] + rc * lattice[2][0];
                        double ty = ra * lattice[0][1] + rb * lattice[1][1] + rc * lattice[2][1];
                        double tz = ra * lattice[0][2] + rb * lattice[1][2] + rc * lattice[2][2];

                        for (Atom atom : atoms) {
                            if (atom == null) {
                                continue;
                            }
                            String name = atom.getName();
                            double x = atom.getX() + tx;
                            double y = atom.getY() + ty;
                            double z = atom.getZ() + tz;
                            this.cell.addAtom(new Atom(name, x, y, z));
                        }
                    }
                }
            }

            return true;

        } finally {
            this.cell.restartResolving();
        }
    }
}
