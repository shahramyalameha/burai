/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.ssh;

public class SSHServer {

    private static final String WORD_JOB_SCRIPT = "JOB_SCRIPT";

    private static final String WORD_QE_COMMAND = "QUANTUM_ESPRESSO_COMMAND";

    private static final int DEFAULT_PORT = 22;

    private String title;

    private String host;

    private String port;

    private String user;

    private String password;

    private String keyPath;

    private String jobCommand;

    private String jobScript;

    public SSHServer(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("title is empty.");
        }

        this.title = title;
        this.host = null;
        this.port = Integer.toString(DEFAULT_PORT);
        this.user = null;
        this.password = null;
        this.keyPath = null;
        this.initializeJobCommand();
        this.initializeJobScript();
    }

    public String getTitle() {
        return this.title;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return this.port;
    }

    public int intPort() {
        int iport = 0;

        try {
            iport = this.port == null ? DEFAULT_PORT : Integer.parseInt(this.port);

        } catch (NumberFormatException e) {
            iport = DEFAULT_PORT;
        }

        return iport;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeyPath() {
        return this.keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getJobCommand() {
        return this.jobCommand;
    }

    public void setJobCommand(String jobCommand) {
        this.jobCommand = jobCommand;
    }

    private void initializeJobCommand() {
        this.jobCommand = "qsub ${" + WORD_JOB_SCRIPT + "}";
    }

    public String getJobScript() {
        return this.jobScript;
    }

    public void setJobScript(String jobScript) {
        this.jobScript = jobScript;
    }

    private void initializeJobScript() {
        this.jobScript = "#!/bin/sh";
        // TODO
    }

    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public int hashCode() {
        return this.title.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }

        SSHServer other = (SSHServer) obj;

        return this.title.equals(other.title);
    }
}
