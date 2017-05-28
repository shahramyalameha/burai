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

    private static final double DEFAULT_THICKNESS = 1.0;

    private static final double DEFAULT_VACUUM = 10.0; // angstrom

    public static double defaultThickness() {
        return DEFAULT_THICKNESS;
    }

    public static double defaultVacuum() {
        return DEFAULT_VACUUM;
    }

    protected double offset;
    protected double thickness;
    protected double vacuum;
    protected int scaleA;
    protected int scaleB;

    private double lastOffset;
    private double lastThickness;
    private double lastVacuum;
    private int lastScaleA;
    private int lastScaleB;

    protected SlabModel() {
        this.offset = 0.0;
        this.thickness = DEFAULT_THICKNESS;
        this.vacuum = DEFAULT_VACUUM;
        this.scaleA = 1;
        this.scaleB = 1;

        this.lastOffset = this.offset;
        this.lastThickness = this.thickness;
        this.lastVacuum = this.vacuum;
        this.lastScaleA = this.scaleA;
        this.lastScaleB = this.scaleB;
    }

    public final void setOffset(double offset) {
        this.offset = offset;
    }

    public final void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public final void setVacuum(double vacuum) {
        this.vacuum = vacuum;
    }

    public final void setScaleA(int scaleA) {
        this.scaleA = scaleA;
    }

    public final void setScaleB(int scaleB) {
        this.scaleB = scaleB;
    }

    public abstract SlabModel[] getSlabModels();

    protected abstract boolean updateCell(Cell cell);

    public final boolean putOnCell(Cell cell) {

        boolean status = this.updateCell(cell);

        if (status) {
            this.lastOffset = this.offset;
            this.lastThickness = this.thickness;
            this.lastVacuum = this.vacuum;
            this.lastScaleA = this.scaleA;
            this.lastScaleB = this.scaleB;
        }

        return status;
    }

    public final boolean putOnLastCell(Cell cell) {
        this.offset = this.lastOffset;
        this.thickness = this.lastThickness;
        this.vacuum = this.lastVacuum;
        this.scaleA = this.lastScaleA;
        this.scaleB = this.lastScaleB;

        return this.updateCell(cell);
    }
}
