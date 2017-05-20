/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.modeler;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import burai.app.QEFXAppController;
import burai.app.QEFXMain;
import burai.app.project.QEFXProjectController;
import burai.app.project.viewer.modeler.Modeler;
import burai.atoms.viewer.AtomsViewer;
import burai.atoms.viewer.AtomsViewerInterface;
import burai.com.consts.ConstantStyles;
import burai.com.fx.FXBufferedThread;
import burai.com.graphic.svg.SVGLibrary;
import burai.com.graphic.svg.SVGLibrary.SVGData;

public class QEFXModelerEditorController extends QEFXAppController {

    private static final long SLEEP_OF_FXBUFFER = 300L;

    private static final double CTRL_GRAPHIC_SIZE = 20.0;
    private static final String CTRL_GRAPHIC_CLASS = "piclight-button";

    private static final double BUILD_GRAPHIC_SIZE = 20.0;
    private static final String BUILD_GRAPHIC_CLASS = "piclight-button";

    private static final String ERROR_STYLE = ConstantStyles.ERROR_COLOR;

    private QEFXProjectController projectController;

    private Modeler modeler;

    private FXBufferedThread bufferedThread;

    @FXML
    private Button screenButton;

    @FXML
    private Button reflectButton;

    @FXML
    private Button initButton;

    @FXML
    private Button undoButton;

    @FXML
    private Button redoButton;

    @FXML
    private Button centerButton;

    private boolean transBusy;

    @FXML
    private Slider transSlider1;

    @FXML
    private Slider transSlider2;

    @FXML
    private Slider transSlider3;

    @FXML
    private Button superButton;

    @FXML
    private TextField scaleField1;

    @FXML
    private TextField scaleField2;

    @FXML
    private TextField scaleField3;

    @FXML
    private Button slabButton;

    @FXML
    private TextField millerField1;

    @FXML
    private TextField millerField2;

    @FXML
    private TextField millerField3;

    @FXML
    private ToggleButton symbolToggle;

    @FXML
    private ComboBox<Double> widthCombo;

    @FXML
    private ComboBox<Integer> typeCombo;

    public QEFXModelerEditorController(QEFXProjectController projectController, Modeler modeler) {
        super(projectController == null ? null : projectController.getMainController());

        if (modeler == null) {
            throw new IllegalArgumentException("modeler is null.");
        }

        this.projectController = projectController;

        this.modeler = modeler;
        this.initializeModeler();

        this.bufferedThread = new FXBufferedThread(SLEEP_OF_FXBUFFER, true);
    }

    private void initializeModeler() {
        this.transBusy = false;

        this.modeler.setOnCellOffsetChanged((a, b, c) -> {
            if (this.transBusy) {
                return;
            }

            if (this.transSlider1 != null) {
                this.transSlider1.setValue(Math.min(Math.max(0.0, a), 1.0));
            }

            if (this.transSlider2 != null) {
                this.transSlider2.setValue(Math.min(Math.max(0.0, b), 1.0));
            }

            if (this.transSlider3 != null) {
                this.transSlider3.setValue(Math.min(Math.max(0.0, c), 1.0));
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupScreenButton();
        this.setupReflectButton();
        this.setupInitButton();
        this.setupUndoButton();
        this.setupRedoButton();
        this.setupCenterButton();

        this.setupTransSlider(this.transSlider1);
        this.setupTransSlider(this.transSlider2);
        this.setupTransSlider(this.transSlider3);

        this.setupSuperButton();
        this.setupScaleField(this.scaleField1);
        this.setupScaleField(this.scaleField2);
        this.setupScaleField(this.scaleField3);

        this.setupSlabButton();
        this.setupMillerField(this.millerField1);
        this.setupMillerField(this.millerField2);
        this.setupMillerField(this.millerField3);
    }

    private void setupScreenButton() {
        if (this.screenButton == null) {
            return;
        }

        this.screenButton.setText("");
        this.screenButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.CAMERA, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.screenButton.setOnAction(event -> {
            if (this.projectController != null) {
                this.projectController.sceenShot();
            }
        });
    }

    private void setupReflectButton() {
        if (this.reflectButton == null) {
            return;
        }

        this.reflectButton.setText("");
        this.reflectButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.OUT, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.reflectButton.setOnAction(event -> {
            if (this.modeler != null) {
                this.modeler.reflect();
            }

            Platform.runLater(() -> {
                AtomsViewerInterface atomsViewer = null;
                if (this.projectController != null) {
                    atomsViewer = this.projectController.getAtomsViewer();
                }
                if (atomsViewer != null && atomsViewer instanceof AtomsViewer) {
                    ((AtomsViewer) atomsViewer).setCellToCenter();
                }
            });

            if (this.projectController != null) {
                this.projectController.setNormalMode();
            }
        });
    }

    private void setupInitButton() {
        if (this.initButton == null) {
            return;
        }

        this.initButton.setText("");
        this.initButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.INTO, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.initButton.setOnAction(event -> {
            if (this.modeler != null) {
                this.modeler.initialize();
            }
        });
    }

    private void setupUndoButton() {
        if (this.undoButton == null) {
            return;
        }

        this.undoButton.setText("");
        this.undoButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.UNDO, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.undoButton.setOnAction(event -> {
            if (this.modeler != null) {
                this.modeler.undo();
            }
        });
    }

    private void setupRedoButton() {
        if (this.redoButton == null) {
            return;
        }

        this.redoButton.setText("");
        this.redoButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.REDO, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.redoButton.setOnAction(event -> {
            if (this.modeler != null) {
                this.modeler.redo();
            }
        });
    }

    private void setupCenterButton() {
        if (this.centerButton == null) {
            return;
        }

        this.centerButton.setText("");
        this.centerButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.CENTER, CTRL_GRAPHIC_SIZE, null, CTRL_GRAPHIC_CLASS));

        this.centerButton.setOnAction(event -> {
            if (this.modeler != null) {
                this.modeler.center();
            }
        });
    }

    private void setupTransSlider(Slider slider) {
        if (slider == null) {
            return;
        }

        slider.valueProperty().addListener(o -> {
            if (this.modeler != null) {
                double a = this.transSlider1 == null ? 0.0 : this.transSlider1.getValue();
                double b = this.transSlider2 == null ? 0.0 : this.transSlider2.getValue();
                double c = this.transSlider3 == null ? 0.0 : this.transSlider3.getValue();
                this.bufferedThread.runLater(() -> {
                    this.transBusy = true;
                    this.modeler.translateCell(a, b, c);
                    this.transBusy = false;
                });
            }
        });
    }

    private void setupSuperButton() {
        if (this.superButton == null) {
            return;
        }

        this.superButton.setDisable(true);
        this.superButton.getStyleClass().add(BUILD_GRAPHIC_CLASS);
        this.superButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.GEAR, BUILD_GRAPHIC_SIZE, null, BUILD_GRAPHIC_CLASS));

        String text = this.superButton.getText();
        if (text != null) {
            this.superButton.setText(text + " ");
        }

        this.superButton.setOnAction(event -> {
            if (this.modeler == null) {
                return;
            }

            int n1 = this.getScaleValue(this.scaleField1);
            int n2 = this.getScaleValue(this.scaleField2);
            int n3 = this.getScaleValue(this.scaleField3);
            boolean status = this.modeler.buildSuperCell(n1, n2, n3);

            if (!status) {
                this.showErrorDialog();
            }
        });
    }

    private boolean isAvailSuper() {
        int n1 = this.getScaleValue(this.scaleField1);
        if (n1 < 1) {
            return false;
        }

        int n2 = this.getScaleValue(this.scaleField2);
        if (n2 < 1) {
            return false;
        }

        int n3 = this.getScaleValue(this.scaleField3);
        if (n3 < 1) {
            return false;
        }

        return (n1 * n2 * n3) > 1;
    }

    private void setupScaleField(TextField textField) {
        if (textField == null) {
            return;
        }

        textField.setText("");
        textField.setStyle("");

        textField.textProperty().addListener(o -> {
            int value = this.getScaleValue(textField);
            if (value > 0) {
                textField.setStyle("");
            } else {
                textField.setStyle(ERROR_STYLE);
            }

            if (this.superButton != null) {
                this.superButton.setDisable(!this.isAvailSuper());
            }
        });

        textField.setOnAction(event -> {
            if (this.superButton != null && !(this.superButton.isDisable())) {
                EventHandler<ActionEvent> handler = this.superButton.getOnAction();
                if (handler != null) {
                    handler.handle(event);
                }
            }
        });
    }

    private int getScaleValue(TextField textField) {
        if (textField == null) {
            return 0;
        }

        String text = textField.getText();
        text = text == null ? null : text.trim();
        if (text == null || text.isEmpty()) {
            return 1;
        }

        int value = 0;

        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }

        return value;
    }

    private void setupSlabButton() {
        if (this.slabButton == null) {
            return;
        }

        this.slabButton.setDisable(true);
        this.slabButton.getStyleClass().add(BUILD_GRAPHIC_CLASS);
        this.slabButton.setGraphic(
                SVGLibrary.getGraphic(SVGData.GEAR, BUILD_GRAPHIC_SIZE, null, BUILD_GRAPHIC_CLASS));

        String text = this.slabButton.getText();
        if (text != null) {
            this.slabButton.setText(text + " ");
        }

        this.slabButton.setOnAction(event -> {
            if (this.modeler == null) {
                return;
            }

            Integer M1 = this.getMillerValue(this.millerField1);
            Integer M2 = this.getMillerValue(this.millerField2);
            Integer M3 = this.getMillerValue(this.millerField3);
            int m1 = M1 == null ? 0 : M1.intValue();
            int m2 = M2 == null ? 0 : M2.intValue();
            int m3 = M3 == null ? 0 : M3.intValue();
            boolean status = this.modeler.buildSlabModel(m1, m2, m3);

            if (!status) {
                this.showErrorDialog();
            }
        });
    }

    private boolean isAvailSlab() {
        Integer M1 = this.getMillerValue(this.millerField1);
        Integer M2 = this.getMillerValue(this.millerField2);
        Integer M3 = this.getMillerValue(this.millerField3);
        if (M1 != null && M2 != null && M3 != null) {
            int m1 = M1.intValue();
            int m2 = M2.intValue();
            int m3 = M3.intValue();
            return (m1 != 0 || m2 != 0 || m3 != 0);
        }

        return false;
    }

    private void setupMillerField(TextField textField) {
        if (textField == null) {
            return;
        }

        textField.setText("");
        textField.setStyle("");

        textField.textProperty().addListener(o -> {
            if (textField != null) {
                if (this.checkMillerValue(textField)) {
                    textField.setStyle("");
                } else {
                    textField.setStyle(ERROR_STYLE);
                }
            }

            if (this.slabButton != null) {
                this.slabButton.setDisable(!this.isAvailSlab());
            }
        });

        textField.setOnAction(event -> {
            if (this.slabButton != null && !(this.slabButton.isDisable())) {
                EventHandler<ActionEvent> handler = this.slabButton.getOnAction();
                if (handler != null) {
                    handler.handle(event);
                }
            }
        });
    }

    private Integer getMillerValue(TextField textField) {
        if (textField == null) {
            return null;
        }

        String text = textField.getText();
        text = text == null ? null : text.trim();
        if (text == null || text.isEmpty()) {
            return null;
        }

        int value = 0;

        try {
            value = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }

        return value;
    }

    private boolean checkMillerValue(TextField textField) {
        if (textField == null) {
            return false;
        }

        String text = textField.getText();
        text = text == null ? null : text.trim();
        if (text == null || text.isEmpty()) {
            return true;
        }

        try {
            Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    private void showErrorDialog() {
        Alert alert = new Alert(AlertType.ERROR);
        QEFXMain.initializeDialogOwner(alert);
        alert.setHeaderText("Error has occurred in modering.");
        alert.setContentText("Atoms are too much.");
        alert.showAndWait();
    }
}
