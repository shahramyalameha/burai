/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.app.project.viewer.run;

import burai.run.RunningNode;
import burai.ssh.SSHJob;

public class RunEvent {

    private RunningNode runningNode;

    private SSHJob sshJob;

    private RunEvent() {
        this.runningNode = null;
        this.sshJob = null;
    }

    public RunEvent(RunningNode runningNode) {
        this();
        this.runningNode = runningNode;
    }

    public RunEvent(SSHJob sshJob) {
        this();
        this.sshJob = sshJob;
    }

    public RunningNode getRunningNode() {
        return this.runningNode;
    }

    public SSHJob getSSHJob() {
        return this.sshJob;
    }
}
