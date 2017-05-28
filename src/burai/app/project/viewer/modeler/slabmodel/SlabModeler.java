/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.modeler.slabmodel;

import burai.app.project.viewer.modeler.ModelerBase;
import burai.atoms.model.Cell;

public class SlabModeler extends ModelerBase {

    private SlabModel slabModel;

    public SlabModeler(Cell srcCell) {
        super(srcCell);

        this.slabModel = null;
    }

    @Override
    public void initialize() {
        if (this.slabModel != null) {
            this.slabModel.setThickness(SlabModel.defaultThickness());
            this.slabModel.setVacuum(SlabModel.defaultVacuum());
            this.slabModel.setScaleA(SlabModel.defaultScale());
            this.slabModel.setScaleB(SlabModel.defaultScale());
        }

        if (this.atomsViewer != null) {
            this.atomsViewer.setCellToCenter();
        }
    }

    public double getOffset() {
        return this.slabModel == null ? SlabModel.defaultOffset() : this.slabModel.getOffset();
    }

    public double getThickness() {
        return this.slabModel == null ? SlabModel.defaultThickness() : this.slabModel.getThickness();
    }

    public double getVacuum() {
        return this.slabModel == null ? SlabModel.defaultVacuum() : this.slabModel.getVacuum();
    }

    public int getScaleA() {
        return this.slabModel == null ? SlabModel.defaultScale() : this.slabModel.getScaleA();
    }

    public int getScaleB() {
        return this.slabModel == null ? SlabModel.defaultScale() : this.slabModel.getScaleB();
    }

    private boolean update() {
        boolean status = false;
        if (this.dstCell != null) {
            status = this.slabModel.putOnCell(this.dstCell);
        }

        if (!status) {
            this.slabModel.putOnLastCell(this.dstCell);
        }

        return status;
    }

    public boolean setSlabModel(SlabModel slabModel) {
        if (slabModel != null) {
            slabModel.setThickness(this.getThickness());
            slabModel.setVacuum(this.getVacuum());
            slabModel.setScaleA(this.getScaleA());
            slabModel.setScaleB(this.getScaleB());
        }

        this.slabModel = slabModel;
        if (this.slabModel == null) {
            return false;
        }

        boolean status = false;
        if (this.dstCell != null) {
            status = this.slabModel.putOnCell(this.dstCell);
        }

        if (!status) {
            this.slabModel.setThickness(SlabModel.defaultThickness());
            this.slabModel.setVacuum(SlabModel.defaultVacuum());
            this.slabModel.setScaleA(SlabModel.defaultScale());
            this.slabModel.setScaleB(SlabModel.defaultScale());
            this.update();
        }

        return status;
    }

    public boolean changeThickness(double thickness) {
        if (thickness < 0.0) {
            return false;
        }

        if (this.slabModel == null) {
            return false;
        }

        this.slabModel.setThickness(thickness);

        return this.update();
    }

    public boolean changeVacuum(double vacuum) {
        if (vacuum < 0.0) {
            return false;
        }

        if (this.slabModel == null) {
            return false;
        }

        this.slabModel.setVacuum(vacuum);

        return this.update();
    }

    public boolean changeArea(int na, int nb) {
        if (na < 1 || nb < 1) {
            return false;
        }

        if (this.slabModel == null) {
            return false;
        }

        this.slabModel.setScaleA(na);
        this.slabModel.setScaleB(nb);

        boolean status = this.update();

        if (status) {
            if (this.atomsViewer != null) {
                this.atomsViewer.setCellToCenter();
            }
        }

        return status;
    }
}
