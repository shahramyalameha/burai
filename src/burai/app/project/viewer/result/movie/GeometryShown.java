/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.result.movie;

import burai.project.property.ProjectGeometry;

@FunctionalInterface
public interface GeometryShown {

    public abstract void onGeometryShown(int index, ProjectGeometry geometry);

}
