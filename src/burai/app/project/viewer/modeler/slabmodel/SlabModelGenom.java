/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.modeler.slabmodel;

public class SlabModelGenom {

    private String[] genom;

    public SlabModelGenom(String[] nameList, double[] zList) {
        if (nameList == null || nameList.length < 1) {
            throw new IllegalArgumentException("nameList is empty.");
        }

        if (zList == null || zList.length < 1) {
            throw new IllegalArgumentException("zList is empty.");
        }

        if (nameList.length != zList.length) {
            throw new IllegalArgumentException("nameList.length != zList.length.");
        }

        this.genom = new String[nameList.length];
        this.setupGeom(nameList, zList);
    }

    private void setupGeom(String[] nameList, double[] zList) {
        // TODO
    }
}
