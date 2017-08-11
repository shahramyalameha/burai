/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.design.property;

public class AtomDesignProperty {

    private double radius;

    private double[] color;

    protected AtomDesignProperty() {
        this.radius = 0.0;
        this.color = null;
    }

    protected double getRadius() {
        return this.radius;
    }

    protected void setRadius(double radius) {
        this.radius = radius;
    }

    protected double[] getColor() {
        return this.color;
    }

    protected void setColor(double[] color) {
        this.color = color;
    }
}
