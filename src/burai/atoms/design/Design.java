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
import java.util.Set;

import javafx.scene.paint.Color;

public class Design {

    private AtomsStyle atomsStyle;
    private AtomsStyleChanged onAtomsStyleChanged;

    private Color backColor;
    private ColorChanged onBackColorChanged;

    private Color fontColor;
    private ColorChanged onFontColorChanged;

    private Color cellColor;
    private ColorChanged onCellColorChanged;

    private boolean showingLegend;
    private ShowingChanged onShowingLegendChanged;

    private boolean showingAxis;
    private ShowingChanged onShowingAxisChanged;

    private boolean showingCell;
    private ShowingChanged onShowingCellChanged;

    private double bondWidth;
    private ValueChanged onBondWidthChanged;

    private double cellWidth;
    private ValueChanged onCellWidthChanged;

    private Map<String, AtomDesign> atomDesigns;

    public Design() {
        this.atomsStyle = AtomsStyle.BALL_STICK;
        this.onAtomsStyleChanged = null;

        this.backColor = Color.TRANSPARENT;
        this.onBackColorChanged = null;

        this.fontColor = Color.BLACK;
        this.onFontColorChanged = null;

        this.cellColor = Color.BLACK;
        this.onCellColorChanged = null;

        this.showingLegend = true;
        this.onShowingLegendChanged = null;

        this.showingAxis = true;
        this.onShowingAxisChanged = null;

        this.showingCell = true;
        this.onShowingCellChanged = null;

        this.bondWidth = 1.0;
        this.onBondWidthChanged = null;

        this.cellWidth = 1.0;
        this.onCellWidthChanged = null;

        this.atomDesigns = null;
    }

    public AtomsStyle getAtomsStyle() {
        return this.atomsStyle;
    }

    public void setAtomsStyle(AtomsStyle atomsStyle) {
        this.atomsStyle = atomsStyle;

        if (this.onAtomsStyleChanged != null) {
            this.onAtomsStyleChanged.onAtomsStyleChanged(this.atomsStyle);
        }
    }

    public void setOnAtomsStyleChanged(AtomsStyleChanged onAtomsStyleChanged) {
        this.onAtomsStyleChanged = onAtomsStyleChanged;
    }

    public Color getBackColor() {
        return this.backColor;
    }

    public void setBackColor(Color backColor) {
        this.backColor = backColor;

        if (this.onBackColorChanged != null) {
            this.onBackColorChanged.onColorChanged(this.backColor);
        }
    }

    public void setOnBackColorChanged(ColorChanged onBackColorChanged) {
        this.onBackColorChanged = onBackColorChanged;
    }

    public Color getFontColor() {
        return this.fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;

        if (this.onFontColorChanged != null) {
            this.onFontColorChanged.onColorChanged(this.fontColor);
        }
    }

    public void setOnFontColorChanged(ColorChanged onFontColorChanged) {
        this.onFontColorChanged = onFontColorChanged;
    }

    public Color getCellColor() {
        return this.cellColor;
    }

    public void setCellColor(Color cellColor) {
        this.cellColor = cellColor;

        if (this.onCellColorChanged != null) {
            this.onCellColorChanged.onColorChanged(this.cellColor);
        }
    }

    public void setOnCellColorChanged(ColorChanged onCellColorChanged) {
        this.onCellColorChanged = onCellColorChanged;
    }

    public boolean isShowingLegend() {
        return this.showingLegend;
    }

    public void setShowingLegend(boolean showingLegend) {
        this.showingLegend = showingLegend;

        if (this.onShowingLegendChanged != null) {
            this.onShowingLegendChanged.onShowingChanged(this.showingLegend);
        }
    }

    public void setOnShowingLegendChanged(ShowingChanged onShowingLegendChanged) {
        this.onShowingLegendChanged = onShowingLegendChanged;
    }

    public boolean isShowingAxis() {
        return this.showingAxis;
    }

    public void setShowingAxis(boolean showingAxis) {
        this.showingAxis = showingAxis;

        if (this.onShowingAxisChanged != null) {
            this.onShowingAxisChanged.onShowingChanged(this.showingAxis);
        }
    }

    public void setOnShowingAxisChanged(ShowingChanged onShowingAxisChanged) {
        this.onShowingAxisChanged = onShowingAxisChanged;
    }

    public boolean isShowingCell() {
        return this.showingCell;
    }

    public void setShowingCell(boolean showingCell) {
        this.showingCell = showingCell;

        if (this.onShowingCellChanged != null) {
            this.onShowingCellChanged.onShowingChanged(this.showingCell);
        }
    }

    public void setOnShowingCellChanged(ShowingChanged onShowingCellChanged) {
        this.onShowingCellChanged = onShowingCellChanged;
    }

    public double getBondWidth() {
        return this.bondWidth;
    }

    public void setBondWidth(double bondWidth) {
        this.bondWidth = bondWidth;

        if (this.onBondWidthChanged != null) {
            this.onBondWidthChanged.onValueChanged(this.bondWidth);
        }
    }

    public void setOnBondWidthChanged(ValueChanged onBondWidthChanged) {
        this.onBondWidthChanged = onBondWidthChanged;
    }

    public double getCellWidth() {
        return this.cellWidth;
    }

    public void setCellWidth(double cellWidth) {
        this.cellWidth = cellWidth;

        if (this.onCellWidthChanged != null) {
            this.onCellWidthChanged.onValueChanged(this.cellWidth);
        }
    }

    public void setOnCellWidthChanged(ValueChanged onCellWidthChanged) {
        this.onCellWidthChanged = onCellWidthChanged;
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

    public String[] namesOfAtoms() {
        if (this.atomDesigns == null || this.atomDesigns.isEmpty()) {
            return null;
        }

        Set<String> nameSet = this.atomDesigns.keySet();
        if (nameSet == null || nameSet.isEmpty()) {
            return null;
        }

        String[] names = new String[nameSet.size()];
        return nameSet.toArray(names);
    }
}
