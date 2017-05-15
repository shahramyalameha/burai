/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.result.movie;

import javafx.scene.layout.BorderPane;
import burai.app.project.QEFXProjectController;
import burai.app.project.viewer.atoms.AtomsAction;
import burai.app.project.viewer.result.QEFXResultViewer;
import burai.atoms.model.Cell;
import burai.atoms.viewer.AtomsViewer;
import burai.atoms.viewer.AtomsViewerInterface;
import burai.project.property.ProjectProperty;

public class QEFXMovieViewer extends QEFXResultViewer<QEFXMovieViewerController> {

    public QEFXMovieViewer(QEFXProjectController projectController, ProjectProperty projectProperty, Cell cell, boolean mdMode) {

        super(cell == null ? null : new AtomsViewer(cell, AtomsAction.getAtomsViewerSize(), true),
                new QEFXMovieViewerController(projectController, projectProperty, cell, mdMode));

        if (this.node != null && (this.node instanceof AtomsViewerInterface)) {
            this.setupAtomsViewer((AtomsViewerInterface) this.node, projectController);
        }
    }

    private void setupAtomsViewer(AtomsViewerInterface atomsViewer, QEFXProjectController projectController) {
        if (atomsViewer == null) {
            return;
        }

        final BorderPane projectPane;
        if (projectController != null) {
            projectPane = projectController.getProjectPane();
        } else {
            projectPane = null;
        }

        if (projectPane != null) {
            atomsViewer.addExclusiveNode(() -> {
                return projectPane.getRight();
            });
            atomsViewer.addExclusiveNode(() -> {
                return projectPane.getBottom();
            });
        }
    }
}
