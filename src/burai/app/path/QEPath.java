/*
 * Copyright (C) 2016 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.path;

import java.io.File;

import burai.com.env.Environments;

public final class QEPath {

    private static final String PROP_QE_PATH = "espresso_path";
    private static final String PROP_MPI_PATH = "mpi_path";

    private QEPath() {
        // NOP
    }

    public static String getPath() {
        return Environments.getProperty(PROP_QE_PATH);
    }

    public static String getMPIPath() {
        return Environments.getProperty(PROP_MPI_PATH);
    }

    public static void setPath(File file) {
        setPath(file == null ? null : file.getAbsolutePath());
    }

    public static void setPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            Environments.removeProperty(PROP_QE_PATH);
        } else {
            Environments.setProperty(PROP_QE_PATH, path);
        }
    }

    public static void setMPIPath(File file) {
        setMPIPath(file == null ? null : file.getAbsolutePath());
    }

    public static void setMPIPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            Environments.removeProperty(PROP_MPI_PATH);
        } else {
            Environments.setProperty(PROP_MPI_PATH, path);
        }
    }

}
