/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.result.movie;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Label;
import burai.app.project.QEFXProjectController;
import burai.app.project.viewer.result.QEFXResultViewerController;
import burai.atoms.model.Atom;
import burai.atoms.model.Cell;
import burai.atoms.model.exception.ZeroVolumCellException;
import burai.com.consts.Constants;
import burai.com.math.Matrix3D;
import burai.project.property.ProjectGeometry;
import burai.project.property.ProjectGeometryList;
import burai.project.property.ProjectProperty;

public class QEFXMovieViewerController extends QEFXResultViewerController {

    private static final double RMIN = 1.0e-3;
    private static final double RRMIN = RMIN * RMIN;

    private Cell cell;

    private int currentIndex;

    private GeometryShown onGeometryShown;

    private ProjectGeometryList projectGeometryList;

    public QEFXMovieViewerController(QEFXProjectController
            projectController, ProjectProperty projectProperty, Cell cell, boolean mdMode) {

        super(projectController);

        if (projectProperty == null) {
            throw new IllegalArgumentException("projectProperty is null.");
        }

        if (cell == null) {
            throw new IllegalArgumentException("cell is null.");
        }

        if (mdMode) {
            this.projectGeometryList = projectProperty.getMdList();
        } else {
            this.projectGeometryList = projectProperty.getOptList();
        }

        this.cell = cell;
        this.currentIndex = 0;
        this.onGeometryShown = null;
    }

    public void setOnGeometryShown(GeometryShown onGeometryShown) {
        this.onGeometryShown = onGeometryShown;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // NOP
    }

    @Override
    public void reload() {
        if (this.projectController != null) {
            this.projectController.clearStackedsOnViewerPane();
            this.projectController.stackOnViewerPane(new Label("test"));
        }

        this.showCurrentGeometry();
    }

    public boolean showCurrentGeometry() {
        return this.showGeometry(this.currentIndex);
    }

    public boolean showNextGeometry() {
        return this.showGeometry(this.currentIndex + 1);
    }

    public boolean showPreviousGeometry() {
        return this.showGeometry(this.currentIndex - 1);
    }

    public boolean showFirstGeometry() {
        return this.showGeometry(0);
    }

    public boolean showLastGeometry() {
        if (this.projectGeometryList == null) {
            return false;
        }

        return this.showGeometry(this.projectGeometryList.numGeometries() - 1);
    }

    public boolean showGeometry(int index) {
        if (this.projectGeometryList == null) {
            return false;
        }

        if (index < 0 || this.projectGeometryList.numGeometries() <= index) {
            return false;
        }

        ProjectGeometry projectGeometry = null;

        try {
            projectGeometry = this.projectGeometryList.getGeometry(index);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

        double[][] lattice = projectGeometry.getCell();
        if (lattice == null || lattice.length < 3) {
            return false;
        }

        lattice = Matrix3D.mult(Constants.BOHR_RADIUS_ANGS, lattice);
        if (lattice == null || lattice.length < 3) {
            return false;
        }

        this.cell.stopResolving();

        try {
            this.cell.moveLattice(lattice);
        } catch (ZeroVolumCellException e) {
            e.printStackTrace();
            this.cell.restartResolving();
            return false;
        }

        int natom = projectGeometry.numAtoms();
        int natom2 = this.cell.numAtoms(true);

        Atom[] refAtoms = null;
        if (natom == natom2) {
            refAtoms = this.cell.listAtoms(true);
        }

        if (refAtoms != null && refAtoms.length >= natom) {
            for (int i = 0; i < natom; i++) {
                String name = projectGeometry.getName(i);
                if (name == null || name.trim().isEmpty()) {
                    continue;
                }

                double x = projectGeometry.getX(i) * Constants.BOHR_RADIUS_ANGS;
                double y = projectGeometry.getY(i) * Constants.BOHR_RADIUS_ANGS;
                double z = projectGeometry.getZ(i) * Constants.BOHR_RADIUS_ANGS;

                Atom atom = refAtoms[i];
                if (atom == null) {
                    this.cell.addAtom(new Atom(name, x, y, z));

                } else {
                    String name2 = atom.getName();
                    if (!name.equals(name2)) {
                        atom.setName(name);
                    }

                    double x2 = atom.getX();
                    double y2 = atom.getY();
                    double z2 = atom.getZ();
                    double dx = x - x2;
                    double dy = y - y2;
                    double dz = z - z2;
                    double rr = dx * dx + dy * dy + dz * dz;
                    if (rr > RRMIN) {
                        atom.moveTo(x, y, z);
                    }
                }
            }

        } else {
            this.cell.removeAllAtoms();

            for (int i = 0; i < natom; i++) {
                String name = projectGeometry.getName(i);
                if (name == null || name.trim().isEmpty()) {
                    continue;
                }

                double x = projectGeometry.getX(i) * Constants.BOHR_RADIUS_ANGS;
                double y = projectGeometry.getY(i) * Constants.BOHR_RADIUS_ANGS;
                double z = projectGeometry.getZ(i) * Constants.BOHR_RADIUS_ANGS;

                this.cell.addAtom(new Atom(name, x, y, z));
            }
        }

        this.cell.restartResolving();

        this.currentIndex = index;

        if (this.onGeometryShown != null) {
            this.onGeometryShown.onGeometryShown(index, projectGeometry);
        }

        return true;
    }
}
