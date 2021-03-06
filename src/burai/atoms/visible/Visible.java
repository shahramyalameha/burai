/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.visible;

import burai.atoms.design.Design;
import burai.atoms.model.Model;
import burai.atoms.model.event.ModelEvent;
import burai.atoms.model.event.ModelEventListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;

public abstract class Visible<M extends Model<? extends ModelEvent, ? extends ModelEventListener>> extends Group
        implements ModelEventListener {

    protected M model;

    protected Design design;

    private BooleanProperty toBeFlushed;

    protected Visible(M model, Design design) {
        super();

        if (model == null) {
            throw new IllegalArgumentException("model is null.");
        }

        this.model = model;
        this.design = design;
        this.toBeFlushed = null;
    }

    public M getModel() {
        return this.model;
    }

    public BooleanProperty toBeFlushedProperty() {
        if (this.toBeFlushed == null) {
            this.toBeFlushed = new SimpleBooleanProperty(false);
        }

        return this.toBeFlushed;
    }

    public void setToBeFlushed(boolean toBeFlushed) {
        this.toBeFlushedProperty().set(toBeFlushed);
    }

    @Override
    public boolean isToBeFlushed() {
        return this.toBeFlushedProperty().get();
    }

    @Override
    public void onModelDisplayed(ModelEvent event) {
        this.setVisible(true);
    }

    @Override
    public void onModelNotDisplayed(ModelEvent event) {
        this.setVisible(false);
    }
}
