/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.com.fx;

import javafx.application.Platform;

public class FXThread<R> {

    private boolean inFX;

    private boolean doneFX;

    private R result;

    private FXRunnable<? extends R> runnable;

    public FXThread(FXRunnable<? extends R> runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("runnable is null.");
        }

        this.inFX = false;
        this.doneFX = false;
        this.result = null;
        this.runnable = runnable;
    }

    public R getResult() {
        this.runAndWait();
        return this.result;
    }

    public void runAndWait() {
        if (this.doneFX) {
            return;
        }

        this.inFX = true;

        Platform.runLater(() -> {
            this.result = this.runnable.run();

            synchronized (this) {
                this.inFX = false;
                this.notifyAll();
            }
        });

        synchronized (this) {
            while (this.inFX) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        this.doneFX = true;
    }
}
