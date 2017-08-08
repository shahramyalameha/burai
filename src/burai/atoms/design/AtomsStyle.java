/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.design;

public enum AtomsStyle {

    BALL(0),

    STICK(1),

    BALL_STICK(2);

    private int id;

    private AtomsStyle(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static AtomsStyle getInstance(int id) {
        AtomsStyle[] atomsStyles = values();
        if (atomsStyles == null || atomsStyles.length < 1) {
            return null;
        }

        for (AtomsStyle atomsStyle : atomsStyles) {
            if (atomsStyle != null && id == atomsStyle.getId()) {
                return atomsStyle;
            }
        }

        return null;
    }
}
