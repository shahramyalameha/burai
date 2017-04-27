/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.viewer;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import burai.atoms.model.Cell;

public abstract class AtomsViewerInterface extends Group {

    protected AtomsViewerInterface() {
        super();
    }

    public abstract Cell getCell();

    public abstract double getSceneWidth();

    public abstract double getSceneHeight();

    public abstract void addExclusiveNode(Node node);

    public abstract void addExclusiveNode(NodeWrapper nodeWrapper);

    public abstract void bindSceneTo(Pane pane);

    public abstract void unbindScene();

}