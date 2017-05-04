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
import burai.com.math.Matrix3D;

public class Modeler {

    protected static final int MAX_NUM_ATOMS = 1024;

    private Cell srcCell;
    private Cell dstCell;

    private AtomsViewer atomsViewer;

    public Modeler(Cell srcCell) {
        if (srcCell == null) {
            throw new IllegalArgumentException("srcCell is null.");
        }

        this.srcCell = srcCell;
        this.dstCell = null;

        this.atomsViewer = null;

        this.copyCellForward();
    }

    public Cell getCell() {
        return this.dstCell;
    }

    protected void setAtomsViewer(AtomsViewer atomsViewer) {
        this.atomsViewer = atomsViewer;
    }

    public void initialize() {
        this.copyCellForward();

        if (this.atomsViewer != null) {
            this.atomsViewer.setCellToCenter();
        }
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

        return status;
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
