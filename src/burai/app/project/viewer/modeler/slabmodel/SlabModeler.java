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
import burai.app.project.viewer.modeler.supercell.SuperCellBuilder;
import burai.atoms.model.Cell;

public class SlabModeler extends ModelerBase {

    public SlabModeler(Cell srcCell) {
        super(srcCell);
    }

    public boolean changeSlabWidth(double rate) {
        if (rate <= 0.0) {
            return false;
        }

        // TODO

        return false;
    }

    public boolean scaleSlabArea(int na, int nb) {
        SuperCellBuilder builder = this.dstCell == null ? null : new SuperCellBuilder(this.dstCell);
        if (builder == null) {
            return false;
        }

        boolean status = builder.build(na, nb, 1);

        if (this.atomsViewer != null) {
            if (status) {
                this.atomsViewer.setCellToCenter();
            } else {
                this.atomsViewer.restoreCell();
            }
        }

        return status;
    }

    public boolean changeVacuumWidth(double vacuum) {
        if (vacuum <= 0.0) {
            return false;
        }

        // TODO

        return false;
    }
}
