/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.editor.result.movie;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

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

    private MovieProgress movieProgress;

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
        this.movieProgress = null;
    }

    protected void makeMovie() {
        boolean status = false;
        this.mp4File = this.selectMP4File();
        if (this.mp4File != null) {
            this.movieProgress = new MovieProgress(this.mp4File);
            this.movieProgress.showProgress();
            status = this.makeMP4();
            this.movieProgress.hideProgress();
        }

        if (!status) {
            Alert alert = new Alert(AlertType.ERROR);
            QEFXMain.initializeDialogOwner(alert);
            if (this.mp4File != null) {
                alert.setHeaderText("ERROR in creating movie file: " + this.mp4File.getName());
            } else {
                alert.setHeaderText("ERROR in creating movie file.");
            }

            alert.showAndWait();
        }
    }

    private boolean makeMP4() {
        if (this.mp4File == null) {
            return false;
        }

        int numGeoms = this.viewerController.numGeometries();
        if (numGeoms < 1) {
            return false;
        }

        AWTSequenceEncoder8Bit encoder = null;

        try {
            encoder = AWTSequenceEncoder8Bit.create30Fps(this.mp4File);
            if (encoder == null) {
                return false;
            }

            H264Encoder h264Encoder = encoder.getEncoder();
            if (h264Encoder != null) {
                h264Encoder.setKeyInterval(30);
            }

            for (int i = 0; i < numGeoms; i++) {
                if (!this.viewerController.showGeometry(i)) {
                    return false;
                }

                Node subject = this.projectController.getViewerPane();
                Image image = subject == null ? null : subject.snapshot(null, null);
                BufferedImage swingImage = image == null ? null : SwingFXUtils.fromFXImage(image, null);
                swingImage = this.resizeSwingImage(swingImage);
                if (swingImage == null) {
                    return false;
                }

                encoder.encodeImage(swingImage);

                if (this.movieProgress != null) {
                    double valueDone = (double) (i + 1);
                    double valueTotal = (double) numGeoms;
                    this.movieProgress.setProgress(valueDone / valueTotal);
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            return false;

        } finally {
            if (encoder != null) {
                try {
                    encoder.finish();

                } catch (Exception e2) {
                    e2.printStackTrace();
                    return false;
                }
            }
        }

        return true;
    }

    private BufferedImage resizeSwingImage(BufferedImage swingImage1) {
        if (swingImage1 == null) {
            return null;
        }

        int width1 = swingImage1.getWidth();
        int height1 = swingImage1.getHeight();
        int width2 = 8 * ((int) (Math.rint(((double) width1) / 8.0) + 0.1));
        int height2 = 8 * ((int) (Math.rint(((double) height1) / 8.0) + 0.1));
        if (width2 <= 0 || height2 <= 0) {
            return null;
        }

        BufferedImage swingImage2 = new BufferedImage(width2, height2, swingImage1.getType());

        Graphics graphics1 = swingImage1.getGraphics();
        Graphics graphics2 = swingImage2.getGraphics();
        if (graphics1 == null || graphics2 == null) {
            return null;
        }

        java.awt.Image image1 = swingImage1.getScaledInstance(width2, height2, java.awt.Image.SCALE_DEFAULT);
        if (image1 == null) {
            return null;
        }

        graphics2.drawImage(image1, 0, 0, null);

        return swingImage2;
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
