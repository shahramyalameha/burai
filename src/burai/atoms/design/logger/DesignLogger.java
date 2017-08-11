/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.design.logger;

import java.util.Deque;
import java.util.LinkedList;

import burai.atoms.design.Design;
import burai.atoms.design.property.DesignProperty;

public class DesignLogger {

    private static final int DEFAULT_MAX_STORED = 32;

    private int maxStored;

    private Design design;

    private Deque<DesignProperty> props;

    private Deque<DesignProperty> subProps;

    public DesignLogger(Design design) {
        this(design, DEFAULT_MAX_STORED);
    }

    public DesignLogger(Design design, int maxStored) {
        if (design == null) {
            throw new IllegalArgumentException("design is null.");
        }

        this.maxStored = Math.max(maxStored, 0);

        this.design = design;

        this.props = new LinkedList<DesignProperty>();
        this.subProps = new LinkedList<DesignProperty>();
    }

    public void clearProperties() {
        this.props.clear();
        this.subProps.clear();
    }

    public void storeProperty() {
        this.subProps.clear();

        this.props.push(new DesignProperty(this.design));

        if (this.props.size() > this.maxStored) {
            this.props.removeLast();
        }
    }

    public boolean canRestoreProperty() {
        if (this.props == null || this.props.isEmpty()) {
            return false;
        }

        return true;
    }

    public boolean canSubRestoreProperty() {
        if (this.subProps == null || this.subProps.isEmpty()) {
            return false;
        }

        return true;
    }

    public void restoreProperty() {
        if (this.props == null || this.props.isEmpty()) {
            return;
        }

        this.subProps.push(new DesignProperty(this.design));

        boolean status = this.restoreProperty(this.props);

        if (!status) {
            this.subProps.poll();
        }
    }

    public void subRestoreProperty() {
        if (this.subProps == null || this.subProps.isEmpty()) {
            return;
        }

        this.props.push(new DesignProperty(this.design));

        boolean status = this.restoreProperty(this.subProps);

        if (!status) {
            this.props.poll();
        }
    }

    private boolean restoreProperty(Deque<DesignProperty> props) {
        if (props == null || props.isEmpty()) {
            return false;
        }

        DesignProperty prop = props.poll();
        if (prop == null) {
            return false;
        }

        prop.restoreDesign(this.design);

        return true;
    }
}
