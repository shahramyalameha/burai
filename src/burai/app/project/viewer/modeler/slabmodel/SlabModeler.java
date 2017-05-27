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

    public void setSlabModel(SlabModel slabModel) {
        this.slabModel = slabModel;
    }

    public boolean changeSlabWidth(double rate) {
        if (rate <= 0.0) {
            return false;
        }

        if (this.slabModel == null) {
            return false;
        }

        this.slabModel.setThickness(rate);

        boolean status = false;
        if (this.dstCell != null) {
            status = this.slabModel.updateCell(this.dstCell);
        }

        if (status) {
            //if (this.atomsViewer != null) {
            //    this.atomsViewer.setCellToCenter();
            //}
        }

        return status;
    }

    public boolean scaleSlabArea(int na, int nb) {
        if (na < 1 || nb < 1 || (na * nb) < 2) {
            return false;
        }

        if (this.slabModel == null) {
            return false;
        }

        this.slabModel.setScaleA(na);
        this.slabModel.setScaleB(nb);

        boolean status = false;
        if (this.dstCell != null) {
            status = this.slabModel.updateCell(this.dstCell);
        }

        if (status) {
            if (this.atomsViewer != null) {
                this.atomsViewer.setCellToCenter();
            }
        }

        return status;
    }

    public boolean changeVacuumWidth(double vacuum) {
        if (vacuum <= 0.0) {
            return false;
        }

        if (this.slabModel == null) {
            return false;
        }

        this.slabModel.setVacuum(vacuum);

        boolean status = false;
        if (this.dstCell != null) {
            status = this.slabModel.updateCell(this.dstCell);
        }

        if (status) {
            //if (this.atomsViewer != null) {
            //    this.atomsViewer.setCellToCenter();
            //}
        }

        return status;
    }
}
