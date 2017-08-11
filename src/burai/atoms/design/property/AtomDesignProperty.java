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

    private int atomsStyle;

    private double bondWidth;

    protected AtomDesignProperty() {
        this.radius = 0.0;
        this.color = null;
        this.atomsStyle = 0;
        this.bondWidth = 0.0;
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

    protected int getAtomsStyle() {
        return this.atomsStyle;
    }

    protected void setAtomsStyle(int atomsStyle) {
        this.atomsStyle = atomsStyle;
    }

    protected double getBondWidth() {
        return this.bondWidth;
    }

    protected void setBondWidth(double bondWidth) {
        this.bondWidth = bondWidth;
    }
}
