/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.atoms.jmol;

import java.awt.Graphics;

import javax.swing.JPanel;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolViewer;

public class JmolBase extends JPanel {

    private JmolAdapter adapter;
    private JmolViewer viewer;

    public JmolBase() {
        super();

        this.adapter = new SmarterJmolAdapter();
        this.viewer = JmolViewer.allocateViewer(this, this.adapter);
        this.viewer.setJmolDefaults();
    }

    public JmolViewer getJmolViewer() {
        return this.viewer;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int width = this.getWidth();
        int height = this.getHeight();

        if (g != null && width > 0 && height > 0) {
            this.viewer.renderScreenImage(g, width, height);
        }
    }

}
