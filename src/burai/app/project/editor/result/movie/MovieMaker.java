/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.result.movie;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import org.jcodec.api.awt.AWTSequenceEncoder8Bit;
import org.jcodec.codecs.h264.H264Encoder;

import burai.app.QEFXMain;
import burai.app.project.QEFXProjectController;
import burai.app.project.viewer.result.movie.QEFXMovieViewerController;
import burai.project.Project;

public class MovieMaker {

    private QEFXProjectController projectController;

    private QEFXMovieViewerController viewerController;

    private Project project;

    private File mp4File;

    protected MovieMaker(QEFXProjectController projectController, QEFXMovieViewerController viewerController, Project project) {
        if (projectController == null) {
            throw new IllegalArgumentException("projectController is null.");
        }

        if (viewerController == null) {
            throw new IllegalArgumentException("viewerController is null.");
        }

        if (project == null) {
            throw new IllegalArgumentException("project is null.");
        }

        this.projectController = projectController;
        this.viewerController = viewerController;
        this.project = project;

        this.mp4File = null;
    }

    protected void makeMovie() {
        boolean status = this.makeMP4();

        if (!status) {
            Alert alert = new Alert(AlertType.ERROR);
            QEFXMain.initializeDialogOwner(alert);
            if (this.mp4File != null) {
                alert.setHeaderText("ERROR in making movie file: " + this.mp4File.getName());
            } else {
                alert.setHeaderText("ERROR in making movie file.");
            }

            alert.showAndWait();
        }
    }

    private boolean makeMP4() {
        this.mp4File = this.selectMP4File();
        if (this.mp4File == null) {
            return false;
        }

        int numGeoms = this.viewerController.numGeometries();
        if (numGeoms < 1) {
            return false;
        }

        AWTSequenceEncoder8Bit encoder = null;

        try {
            encoder = AWTSequenceEncoder8Bit.create25Fps(this.mp4File);
            if (encoder == null) {
                return false;
            }

            H264Encoder h264Encoder = encoder.getEncoder();
            if (h264Encoder != null) {
                h264Encoder.setKeyInterval(25);
            }

            for (int i = 0; i < numGeoms; i++) {
                if (!this.viewerController.showGeometry(i)) {
                    return false;
                }

                Node subject = this.projectController.getViewerPane();
                Image image = subject == null ? null : subject.snapshot(null, null);
                BufferedImage swingImage = image == null ? null : SwingFXUtils.fromFXImage(image, null);
                if (swingImage == null) {
                    return false;
                }

                // width and height must be 8*N
                encoder.encodeImage(new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR));
            }

        } catch (IOException e1) {
            e1.printStackTrace();
            return false;

        } finally {
            if (encoder != null) {
                try {
                    encoder.finish();

                } catch (IOException e2) {
                    e2.printStackTrace();
                    return false;
                }
            }
        }

        return true;
    }

    private File selectMP4File() {
        File selectedFile = null;
        Stage stage = this.projectController.getStage();
        FileChooser fileChooser = this.createFileChooser();
        if (stage != null && fileChooser != null) {
            selectedFile = fileChooser.showSaveDialog(stage);
        }

        return selectedFile;
    }

    private FileChooser createFileChooser() {
        File dirFile = this.project.getDirectory();
        String fileName = dirFile == null ? null : dirFile.getName();
        fileName = fileName == null ? null : (fileName.trim() + ".mp4");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save movie");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Movie Files (*.mp4)", "*.mp4"));

        try {
            if (dirFile != null && dirFile.isDirectory()) {
                fileChooser.setInitialDirectory(dirFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fileName != null && !(fileName.isEmpty())) {
            fileChooser.setInitialFileName(fileName);
        }

        return fileChooser;
    }
}
