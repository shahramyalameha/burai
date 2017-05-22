/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.modeler;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;

public class ModelerIcon extends Group {

    private static final double INSETS_SIZE = 6.0;
    private static final double GRAPHIC_SIZE = 72.0;
    private static final String GRAPHIC_CLASS = "icon-modeler";

    public ModelerIcon(String text) {
        super();

        String text2 = "";
        if (text != null) {
            text2 = text;
        }

        Node figure = SVGLibrary.getGraphic(SVGData.TOOL, GRAPHIC_SIZE, null, GRAPHIC_CLASS);
        StackPane.setMargin(figure, new Insets(INSETS_SIZE));

        Label label = new Label(text2);
        label.setWrapText(true);
        label.getStyleClass().add(GRAPHIC_CLASS);

        StackPane pane = new StackPane();
        pane.getChildren().add(figure);
        pane.getChildren().add(label);

        this.getChildren().add(pane);
        StackPane.setAlignment(this, Pos.BOTTOM_LEFT);
    }
}
