/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.jmol;

import java.util.LinkedList;

import org.jmol.api.JmolViewer;

public class JmolQueue implements Runnable {

    private static final long POST_CIF_TIME = 500L;

    private JmolViewer viewer;

    private boolean alive;

    private LinkedList<JmolAction> actions;

    public JmolQueue(JmolViewer viewer) {
        if (viewer == null) {
            throw new IllegalArgumentException("viewer is null.");
        }

        this.viewer = viewer;

        this.alive = true;
        this.actions = new LinkedList<JmolAction>();

        Thread thread = new Thread(this);
        thread.start();
    }

    private synchronized boolean isAlive() {
        return this.alive;
    }

    public synchronized void stopActions() {
        this.alive = false;
        this.actions.clear();
        this.notifyAll();
    }

    public synchronized boolean addAction(JmolAction action) {
        if (action == null || (!action.isAvailable())) {
            return false;
        }

        boolean status = this.actions.offer(action);
        if (status) {
            this.notifyAll();
        }

        return status;
    }

    @Override
    public void run() {
        while (this.isAlive()) {

            JmolAction action = null;

            synchronized (this) {
                while (this.alive) {
                    int indexAction = 0;
                    int numActions = this.actions.size();
                    for (int i = (numActions - 1); i >= 0; i--) {
                        JmolAction action2 = this.actions.get(i);
                        if (action2 != null && (action2 instanceof JmolCIFAction)) {
                            indexAction = i;
                            break;
                        }
                    }

                    for (int i = 0; i <= indexAction; i++) {
                        action = this.actions.poll();
                    }
                    if (action != null) {
                        break;
                    }

                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (action != null && this.isAlive()) {
                action.actionOnJmol(this.viewer);
            }

            if (action != null && (action instanceof JmolCIFAction)) {
                synchronized (this) {
                    if (this.alive) {
                        try {
                            this.wait(POST_CIF_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
