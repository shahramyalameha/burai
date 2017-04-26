/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.viewer;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import burai.atoms.model.Atom;
import burai.atoms.model.Cell;
import burai.atoms.viewer.logger.AtomsLogger;
import burai.atoms.viewer.operation.ViewerEventManager;
import burai.atoms.visible.VisibleAtom;
import burai.atoms.visible.VisibleCell;

public class AtomsViewer extends AtomsViewerBase<Group> {

    private boolean compassMode;

    private ViewerCell viewerCell;
    private ViewerSample viewerSample;
    private ViewerXYZAxis viewerXYZAxis;
    private ViewerCompass viewerCompass;

    private AtomsLogger logger;

    public AtomsViewer(Cell cell, double size) {
        this(cell, size, size);
    }

    public AtomsViewer(Cell cell, double width, double height) {
        super(cell, width, height);

        this.compassMode = false;

        this.viewerCell = new ViewerCell(this, this.cell);
        this.viewerSample = new ViewerSample(this, this.cell);
        this.viewerXYZAxis = new ViewerXYZAxis(this);
        this.viewerCompass = new ViewerCompass(this.viewerCell);

        this.logger = new AtomsLogger(this.cell);

        this.sceneRoot.getChildren().add(this.viewerCell.getNode());
        this.sceneRoot.getChildren().add(this.viewerSample.getNode());
        this.sceneRoot.getChildren().add(this.viewerXYZAxis.getNode());
        this.sceneRoot.getChildren().add(this.viewerCompass.getNode());

        ViewerEventManager viewerEventManager = new ViewerEventManager(this);
        this.subScene.setOnMousePressed(viewerEventManager.getMousePressedHandler());
        this.subScene.setOnMouseDragged(viewerEventManager.getMouseDraggedHandler());
        this.subScene.setOnMouseReleased(viewerEventManager.getMouseReleasedHandler());
        this.subScene.setOnKeyPressed(viewerEventManager.getKeyPressedHandler());
        this.subScene.setOnScroll(viewerEventManager.getScrollHandler());
    }

    @Override
    protected Group newSceneRoot() {
        return new Group();
    }

    @Override
    protected void onSceneResized() {
        if (this.viewerCell != null) {
            this.viewerCell.initialize(true);
        }

        if (this.viewerSample != null) {
            this.viewerSample.initialize();
        }

        if (this.viewerXYZAxis != null) {
            this.viewerXYZAxis.initialize(true);
        }

        if (this.viewerCompass != null) {
            this.viewerCompass.initialize(true);
        }
    }

    public boolean isCompassMode() {
        return this.compassMode;
    }

    public void setCompassMode(VisibleAtom targetAtom) {
        this.compassMode = (targetAtom != null);
        this.viewerCompass.setTargetAtom(targetAtom);

        if (this.compassMode) {
            this.viewerCompass.initialize();
            this.viewerCompass.getNode().setVisible(true);
            this.stopExclusiveNodes();

        } else {
            this.viewerCompass.getNode().setVisible(false);
            this.restartExclusiveNodes();
        }
    }

    public void appendCellScale(double scale) {
        if (this.compassMode) {
            return;
        }

        if (this.viewerCell != null) {
            this.viewerCell.appendScale(scale);
        }
    }

    public void appendCellRotation(double angle, double axisX, double axisY, double axisZ) {
        if (this.compassMode) {
            return;
        }

        if (this.viewerCell != null) {
            this.viewerCell.appendRotation(angle, axisX, axisY, axisZ);
        }

        if (this.viewerXYZAxis != null) {
            this.viewerXYZAxis.appendRotation(angle, axisX, axisY, axisZ);
        }
    }

    public void appendCellTranslation(double x, double y, double z) {
        if (this.compassMode) {
            return;
        }

        if (this.viewerCell != null) {
            this.viewerCell.appendTranslation(x, y, z);
        }
    }

    public void appendCompassRotation(double angle, double axisX, double axisY, double axisZ) {
        if (!this.compassMode) {
            return;
        }

        if (this.viewerCompass != null) {
            this.viewerCompass.appendRotation(angle, axisX, axisY, axisZ);
        }
    }

    public boolean addChild(Node node) {
        if (this.compassMode) {
            return false;
        }

        if (this.sceneRoot != null) {
            return this.sceneRoot.getChildren().add(node);
        }

        return false;
    }

    public boolean removeChild(Node node) {
        if (this.compassMode) {
            return false;
        }

        if (this.sceneRoot != null) {
            return this.sceneRoot.getChildren().remove(node);
        }

        return false;
    }

    public boolean hasChild(Node node) {
        if (this.sceneRoot != null) {
            return this.sceneRoot.getChildren().contains(node);
        }

        return false;
    }

    public List<VisibleAtom> getVisibleAtoms() {
        List<VisibleAtom> visibleAtoms = new ArrayList<VisibleAtom>();

        VisibleCell visibleCell = null;
        if (this.viewerCell != null) {
            visibleCell = this.viewerCell.getNode();
        }

        if (visibleCell != null) {
            List<Node> children = visibleCell.getChildren();
            for (Node child : children) {
                if (child instanceof VisibleAtom) {
                    visibleAtoms.add((VisibleAtom) child);
                }
            }
        }

        return visibleAtoms;
    }

    public boolean isInCell(double sceneX, double sceneY, double sceneZ) {
        if (this.viewerCell == null) {
            return false;
        }

        return this.viewerCell.isInCell(sceneX, sceneY, sceneZ);
    }

    public Point3D sceneToCell(double sceneX, double sceneY, double sceneZ) {
        if (this.viewerCell == null) {
            return null;
        }

        VisibleCell visibleCell = this.viewerCell.getNode();
        if (visibleCell == null) {
            return null;
        }

        return visibleCell.sceneToLocal(sceneX, sceneY, sceneZ);
    }

    public double getSceneZOnCompass(double sceneX, double sceneY) {
        if (!this.compassMode) {
            return 0.0;
        }

        if (this.viewerCompass == null) {
            return 0.0;
        }

        return this.viewerCompass.getSceneZ(sceneX, sceneY);
    }

    public void putAtom(String name, double sceneX, double sceneY, double sceneZ) {
        if (this.compassMode) {
            return;
        }

        if (this.viewerCell == null) {
            return;
        }

        VisibleCell visibleCell = this.viewerCell.getNode();
        if (visibleCell == null) {
            return;
        }

        Point3D point3d = visibleCell.sceneToLocal(sceneX, sceneY, sceneZ);
        double x = point3d.getX();
        double y = point3d.getY();
        double z = point3d.getZ();

        if (name != null && !name.isEmpty()) {
            visibleCell.getModel().addAtom(new Atom(name, x, y, z));
        }
    }

    public void setCellToCenter() {
        if (this.compassMode) {
            return;
        }

        if (this.viewerCell != null) {
            this.viewerCell.initialize();
        }

        if (this.viewerXYZAxis != null) {
            this.viewerXYZAxis.initialize();
        }
    }

    public void setCompassToCenter() {
        if (!this.compassMode) {
            return;
        }

        if (this.viewerCompass != null) {
            this.viewerCompass.initialize();
        }
    }

    public void storeCell() {
        if (this.logger != null) {
            this.logger.storeConfiguration();
        }
    }

    public boolean canRestoreCell() {
        if (this.compassMode) {
            return false;
        }

        if (this.logger != null) {
            return this.logger.canRestoreConfiguration();
        }

        return false;
    }

    public boolean canSubRestoreCell() {
        if (this.compassMode) {
            return false;
        }

        if (this.logger != null) {
            return this.logger.canSubRestoreConfiguration();
        }

        return false;
    }

    public void restoreCell() {
        if (this.compassMode) {
            return;
        }

        if (this.logger != null) {
            this.logger.restoreConfiguration();
        }
    }

    public void subRestoreCell() {
        if (this.compassMode) {
            return;
        }

        if (this.logger != null) {
            this.logger.subRestoreConfiguration();
        }
    }
}
