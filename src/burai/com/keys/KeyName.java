/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.com.keys;

import javafx.scene.input.KeyCode;
import burai.com.env.Environments;

public final class KeyName {

    private KeyName() {
        // NOP
    }

    public static String getShortcut() {
        return getShortcut(null);
    }

    public static String getShortcut(KeyCode keyCode) {
        String comb = Environments.isMac() ? "Command" : "Ctrl";
        String name = keyCode == null ? null : keyCode.getName();
        if (name == null) {
            return comb;
        } else {
            return comb + "+" + name;
        }
    }

    public static String getShortcutShift() {
        return getShortcutShift(null);
    }

    public static String getShortcutShift(KeyCode keyCode) {
        String comb = Environments.isMac() ? "Command+Shift" : "Ctrl+Shift";
        String name = keyCode == null ? null : keyCode.getName();
        if (name == null) {
            return comb;
        } else {
            return comb + "+" + name;
        }
    }

    public static String getAlt() {
        return getAlt(null);
    }

    public static String getAlt(KeyCode keyCode) {
        String comb = Environments.isMac() ? "Option" : "Alt";
        String name = keyCode == null ? null : keyCode.getName();
        if (name == null) {
            return comb;
        } else {
            return comb + "+" + name;
        }
    }
}
