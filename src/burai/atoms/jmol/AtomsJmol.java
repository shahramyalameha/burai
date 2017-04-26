/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.jmol;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.BorderPane;
import burai.atoms.model.Cell;
import burai.atoms.viewer.AtomsViewerBase;

public class AtomsJmol extends AtomsViewerBase<BorderPane> {

    private JmolBase jmolBase;
    private SwingNode jmolNode;

    public AtomsJmol(Cell cell, double size) {
        this(cell, size, size);
    }

    public AtomsJmol(Cell cell, double width, double height) {
        super(cell, width, height);

        this.jmolBase = null;
        this.jmolNode = null;

        this.createJmolBase();
        this.createJmolNode();
        this.sceneRoot.setCenter(this.jmolNode);
    }

    private void createJmolBase() {
        this.jmolBase = new JmolBase();
    }

    private void createJmolNode() {
        this.jmolNode = new SwingNode();
        this.jmolNode.setContent(this.jmolBase);
    }

    @Override
    protected BorderPane newSceneRoot() {
        return new BorderPane();
    }

    @Override
    protected void onSceneResized() {
        Platform.runLater(() -> {
            this.jmolBase.repaint();
        });
    }

}
