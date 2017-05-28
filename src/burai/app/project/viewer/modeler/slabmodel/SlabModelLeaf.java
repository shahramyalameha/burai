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

public class SlabModelLeaf extends SlabModel {

    private SlabModelStem stem;

    public SlabModelLeaf(SlabModelStem stem, double offset) {
        super();

        if (stem == null) {
            throw new IllegalArgumentException("stem is null.");
        }

        this.stem = stem;
        this.offset = offset;
    }

    @Override
    public SlabModel[] getSlabModels() {
        return new SlabModel[] { this };
    }

    @Override
    protected boolean updateCell(Cell cell) {
        return this.stem.updateCell(cell, this);
    }

}
