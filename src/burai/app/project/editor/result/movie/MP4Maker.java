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
import javafx.scene.image.Image;

import org.jcodec.api.awt.AWTSequenceEncoder8Bit;
import org.jcodec.codecs.h264.H264Encoder;

import burai.app.project.QEFXProjectController;
import burai.app.project.viewer.result.movie.QEFXMovieViewerController;

public class MP4Maker {

    private QEFXProjectController projectController;

    private QEFXMovieViewerController viewerController;

    private boolean movieMaking;

    private boolean movieResult;

    private MovieProgress movieProgress;

    protected MP4Maker(QEFXProjectController projectController, QEFXMovieViewerController viewerController) {
        if (projectController == null) {
            throw new IllegalArgumentException("projectController is null.");
        }

        if (viewerController == null) {
            throw new IllegalArgumentException("viewerController is null.");
        }

        this.projectController = projectController;
        this.viewerController = viewerController;

        this.movieMaking = false;
        this.movieResult = false;
        this.movieProgress = null;
    }

    protected boolean makeMP4(File file) {
        if (file == null) {
            return false;
        }

        this.movieProgress = new MovieProgress(file);
        this.movieProgress.showProgress();

        this.movieMaking = true;

        Thread thread = new Thread(() -> {
            this.movieResult = this.makeMP4Kernel(file);

            synchronized (this) {
                this.movieMaking = false;
                this.notifyAll();
            }
        });

        thread.start();

        synchronized (this) {
            while (this.movieMaking) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        this.movieProgress.hideProgress();

        return this.movieResult;
    }

    private boolean makeMP4Kernel(File file) {
        if (file == null) {
            return false;
        }

        int numGeoms = this.viewerController.numGeometries();
        if (numGeoms < 1) {
            return false;
        }

        AWTSequenceEncoder8Bit encoder = null;

        try {
            encoder = AWTSequenceEncoder8Bit.create30Fps(file);
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
}
