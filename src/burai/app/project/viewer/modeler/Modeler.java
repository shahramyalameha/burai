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
import burai.com.math.Matrix3D;

public class Modeler {

    private Cell srcCell;
    private Cell dstCell;

    public Modeler(Cell srcCell) {
        if (srcCell == null) {
            throw new IllegalArgumentException("srcCell is null.");
        }

        this.srcCell = srcCell;
        this.dstCell = null;
        this.initializeDstCell();
    }

    public Cell getCell() {
        return this.dstCell;
    }

    private void initializeDstCell() {
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
        if (atoms == null) {
            return;
        }

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

        this.dstCell.restartResolving();
    }

}
