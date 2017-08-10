/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.visible;

import burai.atoms.design.AtomDesign;
import burai.atoms.design.AtomDesignAdaptor;
import burai.atoms.design.AtomDesignListener;
import burai.atoms.design.AtomsStyle;
import burai.atoms.design.Design;
import burai.atoms.element.ElementUtil;
import burai.atoms.model.Atom;
import burai.atoms.model.AtomProperty;
import burai.atoms.model.event.AtomEvent;
import burai.atoms.model.event.AtomEventListener;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;

public class VisibleAtom extends Visible<Atom> implements AtomEventListener, AtomDesignListener {

    private static final String KEY_SELECTED = AtomProperty.SELECTED;

    private static final double RADIUS_SCALE_NORM = 0.5;
    private static final double RADIUS_SCALE_BOLD = 0.7;

    private static final double RMIN = 5.0e-3;
    private static final double RRMIN = RMIN * RMIN;

    private boolean boldMode;

    private AtomicSphere atomSphere;

    private boolean disableToSelect;

    private double currentRadius;
    private Color currentColor;
    private AtomsStyle currentStyle;

    private AtomDesign atomDesign;
    private AtomDesignAdaptor atomDesignAdaptor;

    public VisibleAtom(Atom atom, Design design) {
        this(atom, design, false);
    }

    public VisibleAtom(Atom atom, Design design, boolean disableToSelect) {
        this(atom, design, disableToSelect, false);
    }

    public VisibleAtom(Atom atom, Design design, boolean disableToSelect, boolean boldMode) {
        super(atom, design);

        this.model.addListener(this);
        this.model.addPropertyListener(KEY_SELECTED, o -> this.updateSelected());

        this.boldMode = boldMode;
        this.atomSphere = new AtomicSphere(this, !this.boldMode);
        this.disableToSelect = disableToSelect;

        this.currentRadius = -1.0;
        this.currentColor = null;
        this.currentStyle = null;

        this.atomDesign = null;
        this.atomDesignAdaptor = null;

        this.updateAtomDesign();
        this.updateRadiusOfSphere();
        this.updateXYZOfSphere();
        this.updateColorOfSphere();
        this.updateSelected();
        this.getChildren().add(this.atomSphere);
    }

    private void updateAtomDesign() {
        this.atomDesign = null;
        if (this.design != null) {
            this.atomDesign = this.design.getAtomDesign(this.model.getName());
        }

        if (this.atomDesignAdaptor != null) {
            this.atomDesignAdaptor.detach();
        }

        if (this.atomDesign != null) {
            this.atomDesignAdaptor = new AtomDesignAdaptor(this);
            this.atomDesign.addAdaptor(this.atomDesignAdaptor);
        }
    }

    private void updateRadiusOfSphere() {
        double scale = this.boldMode ? RADIUS_SCALE_BOLD : RADIUS_SCALE_NORM;

        double radius = -1.0;
        if (this.atomDesign != null) {
            radius = this.atomDesign.getRadius();
        }

        if (radius <= 0.0) {
            radius = this.model.getRadius();
        }

        this.currentRadius = radius;

        radius *= scale;

        if (this.isSelected()) {
            double radiusMin = scale * 0.80;
            radius = Math.max(1.05 * radius, radiusMin);
        }

        this.atomSphere.setRadius(radius);
    }

    private void updateXYZOfSphere() {
        this.atomSphere.setTranslateX(this.model.getX());
        this.atomSphere.setTranslateY(this.model.getY());
        this.atomSphere.setTranslateZ(this.model.getZ());
    }

    private void updateColorOfSphere() {
        PhongMaterial material = new PhongMaterial();

        Color diffuseColor = null;
        if (this.atomDesign != null) {
            diffuseColor = this.atomDesign.getColor();
        }

        if (diffuseColor == null) {
            diffuseColor = ElementUtil.getColor(this.model.getName());
        }

        this.currentColor = diffuseColor;

        material.setDiffuseColor(diffuseColor);
        material.setSpecularColor(Color.SILVER);
        this.atomSphere.setMaterial(material);
    }

    private void updateSelected() {
        if (this.disableToSelect) {
            return;
        }

        if (!this.isSelected()) {
            this.updateRadiusOfSphere();
            this.atomSphere.setDrawMode(DrawMode.FILL);

        } else {
            this.updateRadiusOfSphere();
            this.atomSphere.setDrawMode(DrawMode.LINE);
        }
    }

    public void setSelected(boolean selected) {
        Atom masterAtom = this.model.getMasterAtom();
        if (masterAtom == null) {
            return;
        }

        masterAtom.setProperty(KEY_SELECTED, selected);
    }

    public boolean isSelected() {
        if (!this.model.hasProperty(KEY_SELECTED)) {
            return this.initializeSelected();
        }

        return this.model.booleanProperty(KEY_SELECTED);
    }

    private boolean initializeSelected() {
        Atom masterAtom = this.model.getMasterAtom();
        if (masterAtom == null) {
            this.model.setProperty(KEY_SELECTED, false);
            return false;
        }

        boolean selected = masterAtom.booleanProperty(KEY_SELECTED);
        this.model.setProperty(KEY_SELECTED, selected);
        return selected;
    }

    public double getRadius() {
        return this.atomSphere.getRadius();
    }

    public double getX() {
        return this.atomSphere.getTranslateX();
    }

    public double getY() {
        return this.atomSphere.getTranslateY();
    }

    public double getZ() {
        return this.atomSphere.getTranslateZ();
    }

    @Override
    public void onAtomRenamed(AtomEvent event) {
        if (event == null) {
            return;
        }

        String name1 = event.getOldName();
        String name2 = event.getName();
        if (name1 != null && name1.equals(name2)) {
            return;
        }

        this.updateAtomDesign();
        this.updateRadiusOfSphere();
        this.updateColorOfSphere();
    }

    @Override
    public void onAtomMoved(AtomEvent event) {
        if (event == null) {
            return;
        }

        double dx = event.getDeltaX();
        double dy = event.getDeltaY();
        double dz = event.getDeltaZ();
        double rr = dx * dx + dy * dy + dz * dz;
        if (rr < RRMIN) {
            return;
        }

        this.updateXYZOfSphere();
    }

    @Override
    public void onAtomicRadiusChanged(AtomDesign atomDesign, double radius) {
        if (atomDesign != this.atomDesign || radius <= 0.0) {
            return;
        }
        if (Math.abs(radius - this.currentRadius) < RMIN) {
            return;
        }

        this.updateRadiusOfSphere();
    }

    @Override
    public void onAtomicColorChanged(AtomDesign atomDesign, Color color) {
        if (atomDesign != this.atomDesign || color == null) {
            return;
        }
        if (color.equals(this.currentColor)) {
            return;
        }

        this.updateColorOfSphere();
    }

    @Override
    public void onAtomsStyleChanged(AtomDesign atomDesign, AtomsStyle atomsStyle) {
        if (atomDesign != this.atomDesign || atomsStyle == null) {
            return;
        }
        if (atomsStyle == this.currentStyle) {
            return;
        }

        // TODO
    }
}
