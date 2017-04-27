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

import org.jmol.api.JmolViewer;

import burai.atoms.model.Cell;
import burai.atoms.viewer.AtomsViewerBase;

public class JmolAtomsViewer extends AtomsViewerBase<BorderPane> {

    private JmolBase jmolBase;

    private SwingNode jmolNode;

    private JmolQueue jmolQueue;

    public JmolAtomsViewer(Cell cell, double size) {
        this(cell, size, size);
    }

    public JmolAtomsViewer(Cell cell, double width, double height) {
        super(cell, width, height);

        this.jmolBase = null;
        this.jmolNode = null;
        this.jmolQueue = null;

        this.createJmolBase();
        this.createJmolNode();
        this.createJmolQueue();

        this.sceneRoot.setCenter(this.jmolNode);
    }

    private void createJmolBase() {
        this.jmolBase = new JmolBase();
    }

    private void createJmolNode() {
        this.jmolNode = new SwingNode();

        if (this.jmolBase != null) {
            this.jmolNode.setContent(this.jmolBase);
        }
    }

    private void createJmolQueue() {
        JmolViewer jmolViewer = this.jmolBase == null ? null : this.jmolBase.getJmolViewer();
        this.jmolQueue = jmolViewer == null ? null : new JmolQueue(jmolViewer);

        if (this.jmolQueue != null) {
            this.jmolQueue.addAction(new JmolCIFAction(this.cell));
        }
    }

    @Override
    protected BorderPane newSceneRoot() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: transparent");
        return pane;
    }

    @Override
    protected void onSceneResized() {
        Platform.runLater(() -> {
            if (this.jmolBase != null) {
                this.jmolBase.repaint();
            }
        });
    }

    public void stopJmol() {
        if (this.jmolBase != null) {
            this.jmolBase.stopJmolViewer();
        }

        if (this.jmolQueue != null) {
            this.jmolQueue.stopActions();
        }
    }
}
