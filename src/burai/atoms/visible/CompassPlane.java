/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.visible;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class CompassPlane extends Group {

    private static final double CYLINDER_RADIUS = 0.02;
    private static final double CYLINDER_HEIGHT = 7.5;
    private static final double SPHERE_RADIUS = 0.10;

    public CompassPlane() {
        super();
        this.creatCompassPlane();
    }

    public static double getHeight() {
        return CYLINDER_HEIGHT;
    }

    private void creatCompassPlane() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.BLACK);
        material.setSpecularColor(Color.GHOSTWHITE);

        double height = CYLINDER_HEIGHT;
        Cylinder cylinder = new Cylinder(CYLINDER_RADIUS, height);
        cylinder.setMaterial(material);
        cylinder.setRotationAxis(Rotate.X_AXIS);
        cylinder.setRotate(90.0);

        Sphere sphere1 = new Sphere(SPHERE_RADIUS);
        sphere1.setMaterial(material);

        Sphere sphere2 = new Sphere(SPHERE_RADIUS);
        sphere2.setMaterial(material);
        sphere2.setTranslateZ(+0.5 * height);

        Sphere sphere3 = new Sphere(SPHERE_RADIUS);
        sphere3.setMaterial(material);
        sphere3.setTranslateZ(-0.5 * height);

        Rectangle rectangle = new Rectangle(-0.5 * height, -0.5 * height, height, height);
        rectangle.setFill(Color.rgb(0, 255, 0, 0.2));

        Group group = new Group();
        group.getChildren().add(cylinder);
        group.getChildren().add(sphere1);
        group.getChildren().add(sphere2);
        group.getChildren().add(sphere3);
        group.getChildren().add(rectangle);

        this.getChildren().add(group);
    }
}
