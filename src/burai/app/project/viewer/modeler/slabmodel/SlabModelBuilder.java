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

public class SlabModelBuilder {

    private Cell cell;

    public SlabModelBuilder(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        this.cell = cell;
    }

    public SlabModel[] build(int h, int k, int l) {
        SlabModel slabModel = null;
        try {
            slabModel = new SlabModelStem(this.cell, h, k, l);
        } catch (MillerIndexException e) {
            //e.printStackTrace();
            return null;
        }

        SlabModel[] slabModels = slabModel.getSlabModels();
        if (slabModels == null || slabModels.length < 1) {
            return null;
        }

        return slabModels;
    }
}
