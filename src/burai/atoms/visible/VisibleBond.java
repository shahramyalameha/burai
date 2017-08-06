/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.visible;

import burai.atoms.design.ViewerDesign;
import burai.atoms.element.ElementUtil;
import burai.atoms.model.Atom;
import burai.atoms.model.Bond;
import burai.atoms.model.event.AtomEvent;
import burai.atoms.model.event.BondEvent;
import burai.atoms.model.event.BondEventListener;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

public class VisibleBond extends Visible<Bond> implements BondEventListener {

    private static final double CYLINDER_RADIUS_NORM = 0.10;
    private static final double CYLINDER_RADIUS_BOLD = 0.12;
    private static final int CYLINDER_DIV = 12;

    private static final double RMIN = 5.0e-3;
    private static final double RRMIN = RMIN * RMIN;

    private boolean boldMode;

    private Cylinder bondCylinder1;
    private Cylinder bondCylinder2;

    public VisibleBond(Bond bond, ViewerDesign viewerDesign) {
        this(bond, viewerDesign, false);
    }

    public VisibleBond(Bond bond, ViewerDesign viewerDesign, boolean boldMode) {
        super(bond, viewerDesign);

        this.model.addListener(this);

        this.boldMode = boldMode;

        double radius = this.boldMode ? CYLINDER_RADIUS_BOLD : CYLINDER_RADIUS_NORM;
        this.bondCylinder1 = new Cylinder(radius, 1.0, CYLINDER_DIV);
        this.bondCylinder2 = new Cylinder(radius, 1.0, CYLINDER_DIV);

        this.updateXYZOfCylinder();
        this.updateColorOfCylinder();
        this.getChildren().add(this.bondCylinder1);
        this.getChildren().add(this.bondCylinder2);
    }

    private void updateXYZOfCylinder() {
        Atom atom1 = this.model.getAtom1();
        double x1 = atom1.getX();
        double y1 = atom1.getY();
        double z1 = atom1.getZ();
        double rad1 = Math.sqrt(atom1.getRadius());
        int anum1 = atom1.getAtomNum();

        Atom atom2 = this.model.getAtom2();
        double x2 = atom2.getX();
        double y2 = atom2.getY();
        double z2 = atom2.getZ();
        double rad2 = Math.sqrt(atom2.getRadius());
        int anum2 = atom2.getAtomNum();

        double dx = x2 - x1;
        double dy = y2 - y1;
        double dz = z2 - z1;
        double rr = dx * dx + dy * dy + dz * dz;
        double r = Math.sqrt(rr);

        double rate1 = rad1 / (rad1 + rad2);
        double rate2 = 1.0 - rate1;
        if (anum1 == anum2) {
            rate1 = 0.999;
            rate2 = 0.001;
        }

        Point3D ax1 = new Point3D(dz, 0.0, -dx);
        Point3D ax2 = new Point3D(-dz, 0.0, dx);
        double theta1 = Math.acos(Math.min(Math.max(-1.0, dy / r), 1.0));
        double theta2 = Math.PI - theta1;

        this.bondCylinder1.setHeight(rate1 * r);
        this.bondCylinder1.setTranslateX(x1 + 0.5 * rate1 * dx);
        this.bondCylinder1.setTranslateY(y1 + 0.5 * rate1 * dy);
        this.bondCylinder1.setTranslateZ(z1 + 0.5 * rate1 * dz);
        this.bondCylinder1.setRotationAxis(ax1);
        this.bondCylinder1.setRotate((180.0 / Math.PI) * theta1);

        this.bondCylinder2.setHeight(rate2 * r);
        this.bondCylinder2.setTranslateX(x2 - 0.5 * rate2 * dx);
        this.bondCylinder2.setTranslateY(y2 - 0.5 * rate2 * dy);
        this.bondCylinder2.setTranslateZ(z2 - 0.5 * rate2 * dz);
        this.bondCylinder2.setRotationAxis(ax2);
        this.bondCylinder2.setRotate((180.0 / Math.PI) * theta2);
    }

    private void updateColorOfCylinder() {
        Atom atom1 = this.model.getAtom1();
        PhongMaterial material1 = new PhongMaterial();
        material1.setDiffuseColor(ElementUtil.getColor(atom1.getName()));
        material1.setSpecularColor(Color.SILVER);
        this.bondCylinder1.setMaterial(material1);

        Atom atom2 = this.model.getAtom2();
        PhongMaterial material2 = new PhongMaterial();
        material2.setDiffuseColor(ElementUtil.getColor(atom2.getName()));
        material2.setSpecularColor(Color.SILVER);
        this.bondCylinder2.setMaterial(material2);
    }

    @Override
    public void onLinkedAtomRenamed(BondEvent event) {
        AtomEvent atomEvent = event == null ? null : event.getAtomEvent();
        if (atomEvent == null) {
            return;
        }

        String name1 = atomEvent.getOldName();
        String name2 = atomEvent.getName();
        if (name1 != null && name1.equals(name2)) {
            return;
        }

        this.updateXYZOfCylinder();
        this.updateColorOfCylinder();
    }

    @Override
    public void onLinkedAtomMoved(BondEvent event) {
        AtomEvent atomEvent = event == null ? null : event.getAtomEvent();
        if (atomEvent == null) {
            return;
        }

        double dx = atomEvent.getDeltaX();
        double dy = atomEvent.getDeltaY();
        double dz = atomEvent.getDeltaZ();
        double rr = dx * dx + dy * dy + dz * dz;
        if (rr < RRMIN) {
            return;
        }

        this.updateXYZOfCylinder();
    }

}
