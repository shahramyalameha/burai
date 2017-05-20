/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.modeler.slabmodel;

import burai.atoms.model.Cell;

public abstract class SlabModel {

    protected double offset;
    protected double vacuum;

    protected SlabModel(double offset, double vacuum) {
        this.offset = offset;
        this.vacuum = vacuum;
    }

    public final void setOffset(double offset) {
        this.offset = offset;
    }

    public final void setVacuum(double vacuum) {
        this.vacuum = vacuum;
    }

    public abstract boolean updateCell(Cell cell);

}
