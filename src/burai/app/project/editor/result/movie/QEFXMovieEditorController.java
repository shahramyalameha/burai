/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.result.movie;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import burai.app.QEFXMain;
import burai.app.project.QEFXProjectController;
import burai.app.project.editor.result.QEFXResultEditorController;
import burai.app.project.viewer.result.movie.QEFXMovieViewerController;
import burai.atoms.model.Atom;
import burai.atoms.model.Cell;
import burai.atoms.model.exception.ZeroVolumCellException;
import burai.com.consts.Constants;
import burai.com.env.Environments;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;
import burai.com.math.Matrix3D;
import burai.project.Project;
import burai.project.property.ProjectGeometry;

public class QEFXMovieEditorController extends QEFXResultEditorController<QEFXMovieViewerController> {

    private static final double GRAPHIC_SIZE = 20.0;
    private static final String GRAPHIC_CLASS = "piclight-button";

    private Project project;

    @FXML
    private Button movieButton;

    @FXML
    private TextField numberField;

    private String numberText;

    @FXML
    private Label totalLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Button exportButton;

    @FXML
    private TextArea atomArea;

    public QEFXMovieEditorController(QEFXProjectController projectController, QEFXMovieViewerController viewerController, Project project) {
        super(projectController, viewerController);

        if (project == null) {
            throw new IllegalArgumentException("project is null.");
        }

        this.project = project;

        this.numberText = null;
    }

    @Override
    protected void setupFXComponents() {
        this.setupMovieButton();
        this.setupNumberField();
        this.setupExportButton();
        this.setupAtomArea();
    }

    private void setupMovieButton() {
        if (this.movieButton == null) {
            return;
        }

        this.movieButton.setText("");
        this.movieButton.setGraphic(SVGLibrary.getGraphic(SVGData.MOVIE, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        this.movieButton.setOnAction(event -> {
            // TODO
            });
    }

    private void setupNumberField() {
        if (this.numberField == null) {
            return;
        }

        this.numberField.focusedProperty().addListener(o -> {
            if (this.numberField.isFocused()) {
                this.numberText = this.numberField.getText();
            } else {
                if (this.numberText != null) {
                    this.numberField.setText(this.numberText);
                }
            }
        });

        this.numberField.setOnAction(event -> {
            String value = this.numberField.getText();
            value = value == null ? null : value.trim();
            if (value == null || value.isEmpty()) {
                return;
            }

            int index = -1;
            try {
                index = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                index = -1;
            }

            boolean status = false;
            if (this.viewerController != null && index > 0) {
                status = this.viewerController.showGeometry(index - 1);
            }

            if (!status) {
                Alert alert = new Alert(AlertType.ERROR);
                QEFXMain.initializeDialogOwner(alert);
                alert.setHeaderText("Incorrect number of atomic configuration.");
                alert.showAndWait();
                return;
            }

            this.numberText = Integer.toString(index);
        });
    }

    private void setupExportButton() {
        if (this.exportButton == null) {
            return;
        }

        this.exportButton.getStyleClass().add(GRAPHIC_CLASS);
        this.exportButton.setGraphic(SVGLibrary.getGraphic(SVGData.EXPORT, GRAPHIC_SIZE, null, GRAPHIC_CLASS));

        this.exportButton.setOnAction(event -> {
            Project project = this.saveNewProject();
            if (project == null) {
                return;
            }

            boolean status = this.editProject(project);
            if (!status) {
                System.err.println("cannot edit project.");
            }

            if (this.mainController != null) {
                this.mainController.showProject(project);
            }
        });
    }

    public Project saveNewProject() {
        File directory = null;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("New project");

        String projPath = this.project == null ? null : this.project.getDirectoryPath();
        projPath = projPath == null ? null : projPath.trim();

        File projDir = null;
        if (projPath != null && !(projPath.isEmpty())) {
            projDir = new File(projPath);
        }

        File initDir = projDir == null ? null : projDir.getParentFile();
        String initPath = initDir == null ? null : initDir.getPath();
        if (initDir == null || initPath == null || initPath.trim().isEmpty()) {
            initPath = Environments.getProjectsPath();
            initDir = new File(initPath);
        }

        if (initDir != null) {
            try {
                if (initDir.isDirectory()) {
                    fileChooser.setInitialDirectory(initDir);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Stage stage = this.getStage();
        if (stage != null) {
            directory = fileChooser.showSaveDialog(stage);
        }
        if (directory == null) {
            return null;
        }

        try {
            if (directory.exists()) {
                Alert alert = new Alert(AlertType.ERROR);
                QEFXMain.initializeDialogOwner(alert);
                alert.setHeaderText(directory.getName() + " already exists.");
                alert.showAndWait();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Project project = null;
        if (this.project != null) {
            project = this.project.cloneProject(directory);
        }

        if (project == null) {
            Alert alert = new Alert(AlertType.ERROR);
            QEFXMain.initializeDialogOwner(alert);
            alert.setHeaderText("Cannot create project: " + directory.getPath());
            alert.showAndWait();
            return null;
        }

        return project;
    }

    private boolean editProject(Project project) {
        Cell cell = project == null ? null : project.getCell();
        if (cell == null) {
            return false;
        }

        ProjectGeometry geometry = null;
        if (this.viewerController != null) {
            geometry = this.viewerController.getGeometry();
        }

        if (geometry == null) {
            return false;
        }

        double[][] lattice = geometry.getCell();
        lattice = Matrix3D.mult(Constants.BOHR_RADIUS_ANGS, lattice);
        if (lattice == null || lattice.length < 3) {
            return false;
        }
        if (lattice[0] == null || lattice[0].length < 3) {
            return false;
        }
        if (lattice[1] == null || lattice[1].length < 3) {
            return false;
        }
        if (lattice[2] == null || lattice[2].length < 3) {
            return false;
        }

        try {
            cell.moveLattice(lattice);
        } catch (ZeroVolumCellException e) {
            e.printStackTrace();
            return false;
        }

        int natom = geometry.numAtoms();
        int natom_ = cell.numAtoms(true);

        Atom[] refAtoms = null;
        if (natom == natom_) {
            refAtoms = cell.listAtoms(true);
        }

        if (refAtoms != null && refAtoms.length >= natom) {
            for (int i = 0; i < natom; i++) {
                String name = geometry.getName(i);
                if (name == null || name.trim().isEmpty()) {
                    continue;
                }

                double x = geometry.getX(i) * Constants.BOHR_RADIUS_ANGS;
                double y = geometry.getY(i) * Constants.BOHR_RADIUS_ANGS;
                double z = geometry.getZ(i) * Constants.BOHR_RADIUS_ANGS;

                Atom atom = refAtoms[i];
                if (atom == null) {
                    cell.addAtom(new Atom(name, x, y, z));
                } else {
                    atom.setName(name);
                    atom.moveTo(x, y, z);
                }
            }

        } else {
            cell.removeAllAtoms();

            for (int i = 0; i < natom; i++) {
                String name = geometry.getName(i);
                if (name == null || name.trim().isEmpty()) {
                    continue;
                }

                double x = geometry.getX(i) * Constants.BOHR_RADIUS_ANGS;
                double y = geometry.getY(i) * Constants.BOHR_RADIUS_ANGS;
                double z = geometry.getZ(i) * Constants.BOHR_RADIUS_ANGS;
                cell.addAtom(new Atom(name, x, y, z));
            }
        }

        return true;
    }

    private void setupAtomArea() {
        if (this.atomArea == null) {
            return;
        }

        if (this.viewerController == null) {
            return;
        }

        this.viewerController.setOnGeometryShown((i, n, geometry) -> {
            if (0 <= i && i < n) {
                if (this.numberField != null) {
                    this.numberField.setText(Integer.toString(i + 1));
                }
            } else {
                if (this.numberField != null) {
                    this.numberField.setText("#");
                }
                return;
            }

            if (n > 0) {
                if (this.totalLabel != null) {
                    this.totalLabel.setText(Integer.toString(n));
                }
            } else {
                if (this.totalLabel != null) {
                    this.totalLabel.setText("#");
                }
                return;
            }

            double time = geometry == null ? -1.0 : geometry.getTime();
            if (time >= 0.0) {
                if (this.timeLabel != null) {
                    this.timeLabel.setText("( t = " + String.format("%10.4f", time).trim() + "ps )");
                }
            } else {
                if (this.timeLabel != null) {
                    this.timeLabel.setText("");
                }
            }

            String strGeometry = this.geometryToString(geometry);
            if (strGeometry != null && !(strGeometry.isEmpty())) {
                if (this.atomArea != null) {
                    this.atomArea.setText(strGeometry);
                }
            } else {
                if (this.atomArea != null) {
                    this.atomArea.setText("");
                }
                return;
            }
        });
    }

    private String geometryToString(ProjectGeometry geometry) {
        if (geometry == null) {
            return null;
        }

        String str = "";

        // cell
        double[][] lattice = geometry.getCell();
        lattice = Matrix3D.mult(Constants.BOHR_RADIUS_ANGS, lattice);
        if (lattice == null || lattice.length < 3) {
            return null;
        }
        if (lattice[0] == null || lattice[0].length < 3) {
            return null;
        }
        if (lattice[1] == null || lattice[1].length < 3) {
            return null;
        }
        if (lattice[2] == null || lattice[2].length < 3) {
            return null;
        }

        String strFormat = "%10.6f %10.6f %10.6f%n";
        str = str + "CELL_PARAMETERS {angstrom}" + System.lineSeparator();
        str = str + String.format(strFormat, lattice[0][0], lattice[0][1], lattice[0][2]);
        str = str + String.format(strFormat, lattice[1][0], lattice[1][1], lattice[1][2]);
        str = str + String.format(strFormat, lattice[2][0], lattice[2][1], lattice[2][2]);

        // atoms
        int natom = geometry.numAtoms();
        if (natom > 0) {
            str = str + System.lineSeparator();
            str = str + "ATOMIC_POSITIONS {angstrom}" + System.lineSeparator();
        }

        for (int i = 0; i < natom; i++) {
            String name = geometry.getName(i);
            if (name == null || name.isEmpty()) {
                continue;
            }
            double x = geometry.getX(i) * Constants.BOHR_RADIUS_ANGS;
            double y = geometry.getY(i) * Constants.BOHR_RADIUS_ANGS;
            double z = geometry.getZ(i) * Constants.BOHR_RADIUS_ANGS;
            str = str + String.format("%-5s %10.6f %10.6f %10.6f%n", name, x, y, z);
        }

        return str;
    }
}
