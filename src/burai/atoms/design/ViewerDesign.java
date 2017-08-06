/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.design;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class ViewerDesign {

    private AtomsStyle atomsStyle;

    private Color backColor;

    private Color fontColor;

    private Color cellColor;

    private boolean showingLegend;

    private boolean showingAxis;

    private boolean showingCell;

    private double bondWidth;

    private double cellWidth;

    private Map<String, AtomDesign> atomDesigns;

    public ViewerDesign() {
        this.atomsStyle = AtomsStyle.BALL_STICK;
        this.backColor = null;
        this.fontColor = null;
        this.cellColor = null;
        this.showingLegend = true;
        this.showingAxis = true;
        this.showingCell = true;
        this.bondWidth = 1.0;
        this.cellWidth = 1.0;
        this.atomDesigns = null;
    }

    public AtomsStyle getAtomsStyle() {
        return this.atomsStyle;
    }

    public Color getBackColor() {
        return this.backColor;
    }

    public Color getFontColor() {
        return this.fontColor;
    }

    public Color getCellColor() {
        return this.cellColor;
    }

    public boolean isShowingLegend() {
        return this.showingLegend;
    }

    public boolean isShowingAxis() {
        return this.showingAxis;
    }

    public boolean isShowingCell() {
        return this.showingCell;
    }

    public double getBondWidth() {
        return this.bondWidth;
    }

    public double getCellWidth() {
        return this.cellWidth;
    }

    public AtomDesign getAtomDesign(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        if (this.atomDesigns == null) {
            this.atomDesigns = new HashMap<>();
        }

        if (!this.atomDesigns.containsKey(name)) {
            this.atomDesigns.put(name, new AtomDesign(name));
        }

        return this.atomDesigns.get(name);
    }

}
