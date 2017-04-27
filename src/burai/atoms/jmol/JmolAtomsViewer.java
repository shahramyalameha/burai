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

import burai.atoms.model.Atom;
import burai.atoms.model.Cell;
import burai.atoms.model.event.AtomEvent;
import burai.atoms.model.event.AtomEventListener;
import burai.atoms.model.event.CellEvent;
import burai.atoms.model.event.CellEventListener;
import burai.atoms.model.event.ModelEvent;
import burai.atoms.viewer.AtomsViewerBase;

public class JmolAtomsViewer extends AtomsViewerBase<BorderPane> implements AtomEventListener, CellEventListener {

    private JmolBase jmolBase;

    private SwingNode jmolNode;

    private JmolQueue jmolQueue;

    private boolean toBeFlushed;

    public JmolAtomsViewer(Cell cell, double size) {
        this(cell, size, size);
    }

    public JmolAtomsViewer(Cell cell, double width, double height) {
        super(cell, width, height);

        this.jmolBase = null;
        this.jmolNode = null;
        this.jmolQueue = null;

        this.toBeFlushed = false;

        this.createJmolBase();
        this.createJmolNode();
        this.createJmolQueue();
        this.setOneselfAsListener();
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

        if (this.jmolQueue != null && this.cell != null) {
            this.jmolQueue.addAction(new JmolCIFAction(this.cell));
        }
    }

    private void setOneselfAsListener() {
        this.cell.addListener(this);

        Atom[] atoms = this.cell.listAtoms();
        if (atoms != null) {
            for (Atom atom : atoms) {
                if (atom != null) {
                    atom.addListener(this);
                }
            }
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

        this.toBeFlushed = true;
        if (this.cell != null) {
            this.cell.flushListeners();
        }
    }

    @Override
    public boolean isToBeFlushed() {
        return this.toBeFlushed;
    }

    @Override
    public void onModelDisplayed(ModelEvent event) {
        // NOP
    }

    @Override
    public void onModelNotDisplayed(ModelEvent event) {
        // NOP
    }

    @Override
    public void onLatticeMoved(CellEvent event) {
        if (event == null || this.cell != event.getSource()) {
            return;
        }

        if (this.jmolQueue != null && this.cell != null) {
            this.jmolQueue.addAction(new JmolCIFAction(this.cell));
        }
    }

    @Override
    public void onAtomAdded(CellEvent event) {
        if (event == null || this.cell != event.getSource()) {
            return;
        }

        Atom atom = event.getAtom();
        if (atom == null) {
            return;
        }

        atom.addListener(this);

        // TODO 自動生成されたメソッド・スタブ
    }

    @Override
    public void onAtomRemoved(CellEvent event) {
        if (event == null || this.cell != event.getSource()) {
            return;
        }

        // TODO 自動生成されたメソッド・スタブ
    }

    @Override
    public void onBondAdded(CellEvent event) {
        // NOP
    }

    @Override
    public void onBondRemoved(CellEvent event) {
        // NOP
    }

    @Override
    public void onAtomRenamed(AtomEvent event) {
        Object obj = event == null ? null : event.getSource();
        if (obj == null || !(obj instanceof Atom)) {
            return;
        }

        Atom atom = (Atom) obj;

        // TODO 自動生成されたメソッド・スタブ
    }

    @Override
    public void onAtomMoved(AtomEvent event) {
        Object obj = event == null ? null : event.getSource();
        if (obj == null || !(obj instanceof Atom)) {
            return;
        }

        Atom atom = (Atom) obj;

        // TODO 自動生成されたメソッド・スタブ
    }
}
