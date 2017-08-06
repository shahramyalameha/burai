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
import java.util.ArrayList;
import java.util.List;

import burai.atoms.element.ElementUtil;
import javafx.scene.paint.Color;

public class AtomDesign {

    private static final int MAX_COUNT_LISTENERS = 16;

    private int countListeners;

    private List<WeakReference<AtomDesignListener>> listeners;

    private double radius;

    private Color color;

    public AtomDesign(double radius, Color color) {
        if (radius <= 0.0) {
            throw new IllegalArgumentException("radius is not positive.");
        }

        if (color == null) {
            throw new IllegalArgumentException("color is null.");
        }

        this.countListeners = 0;
        this.listeners = null;

        this.radius = radius;
        this.color = color;
    }

    public AtomDesign(double radius, double red, double green, double blue) {
        this(radius, Color.color(red, green, blue));
    }

    public AtomDesign(String name) {
        this(ElementUtil.getCovalentRadius(name), ElementUtil.getColor(name));
    }

    public void addListener(AtomDesignListener listener) {
        if (listener == null) {
            return;
        }

        if (this.listeners == null) {
            this.listeners = new ArrayList<>();
        }

        this.refreshListeners();

        this.listeners.add(new WeakReference<AtomDesignListener>(listener));
    }

    private void refreshListeners() {
        if (this.listeners == null || this.listeners.isEmpty()) {
            return;
        }

        if (this.countListeners < MAX_COUNT_LISTENERS) {
            this.countListeners++;
            return;
        }

        this.countListeners = 0;

        int numListeners = this.listeners.size();
        for (int i = (numListeners - 1); i >= 0; i--) {
            WeakReference<AtomDesignListener> weakListener = this.listeners.get(i);
            AtomDesignListener listener = weakListener == null ? null : weakListener.get();
            if (listener == null) {
                this.listeners.remove(i);
            }
        }
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        if (this.radius <= 0.0) {
            return;
        }

        this.radius = radius;

        if (this.listeners != null && !this.listeners.isEmpty()) {
            for (WeakReference<AtomDesignListener> weakListener : this.listeners) {
                AtomDesignListener listener = weakListener == null ? null : weakListener.get();
                if (listener != null) {
                    listener.onRadiusChanged(radius);
                }
            }
        }
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        if (this.color == null) {
            return;
        }

        this.color = color;

        if (this.listeners != null && !this.listeners.isEmpty()) {
            for (WeakReference<AtomDesignListener> weakListener : this.listeners) {
                AtomDesignListener listener = weakListener == null ? null : weakListener.get();
                if (listener != null) {
                    listener.onColorChanged(color);
                }
            }
        }
    }

}
