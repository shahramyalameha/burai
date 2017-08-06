/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.design;

import java.lang.ref.WeakReference;

public class AtomDesignAdaptor {

    private boolean alive;

    private WeakReference<AtomDesignListener> weakListener;

    public AtomDesignAdaptor(AtomDesignListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener is null.");
        }

        this.alive = true;

        this.weakListener = new WeakReference<AtomDesignListener>(listener);
    }

    protected boolean isAlive() {
        return this.alive && (this.weakListener.get() != null);
    }

    public void detach() {
        this.alive = false;
    }

    protected AtomDesignListener getListener() {
        return this.weakListener.get();
    }

}
