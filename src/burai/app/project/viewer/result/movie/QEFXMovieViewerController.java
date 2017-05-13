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

import burai.app.project.QEFXProjectController;
import burai.app.project.viewer.result.QEFXResultViewerController;
import burai.atoms.model.Cell;
import burai.project.property.ProjectGeometryList;
import burai.project.property.ProjectProperty;

public class QEFXMovieViewerController extends QEFXResultViewerController {

    private Cell cell;

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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // NOP
    }

    @Override
    public void reload() {
        System.out.println("movie reload");
        // TODO
    }

}
