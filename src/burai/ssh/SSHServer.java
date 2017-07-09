/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.ssh;

import java.util.List;

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
        return this.getJobCommand(null);
    }

    public String getJobCommand(String scriptName) {
        String scriptName_ = scriptName == null ? null : scriptName.trim();
        if (scriptName_ == null || scriptName_.isEmpty()) {
            return this.jobCommand;
        }

        String jobCommand_ = this.jobCommand;
        jobCommand_.replaceAll("\\$" + WORD_JOB_SCRIPT, scriptName_);
        jobCommand_.replaceAll("\\$\\(" + WORD_JOB_SCRIPT + "\\)", scriptName_);
        jobCommand_.replaceAll("\\$\\{" + WORD_JOB_SCRIPT + "\\}", scriptName_);
        return jobCommand_;
    }

    public void setJobCommand(String jobCommand) {
        this.jobCommand = jobCommand;
    }

    private void initializeJobCommand() {
        this.jobCommand = "qsub ${" + WORD_JOB_SCRIPT + "}";
    }

    public String getJobScript() {
        return this.getJobScript((String) null);
    }

    public String getJobScript(String qeCommand) {
        String qeCommand_ = qeCommand == null ? null : qeCommand.trim();
        if (qeCommand_ == null || qeCommand_.isEmpty()) {
            return this.jobScript;
        }

        String jobScript_ = this.jobScript;
        jobScript_.replaceAll("\\$" + WORD_QE_COMMAND, qeCommand_);
        jobScript_.replaceAll("\\$\\(" + WORD_QE_COMMAND + "\\)", qeCommand_);
        jobScript_.replaceAll("\\$\\{" + WORD_QE_COMMAND + "\\}", qeCommand_);
        return jobScript_;
    }

    public String getJobScript(List<String> qeCommands) {
        if (qeCommands == null) {
            return this.getJobScript((String) null);
        }

        StringBuilder strBuilder = new StringBuilder();
        for (String qeCommand : qeCommands) {
            if (qeCommand == null) {
                continue;
            }
            strBuilder.append(qeCommand);
            strBuilder.append(System.lineSeparator());
        }

        return this.getJobScript(strBuilder.toString());
    }

    public void setJobScript(String jobScript) {
        this.jobScript = jobScript;
    }

    private void initializeJobScript() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("#!/bin/sh");
        strBuilder.append(System.lineSeparator());
        strBuilder.append("#PBS -q QUEUE");
        strBuilder.append(System.lineSeparator());
        strBuilder.append("#PBS -l select=1:ncpus=8:mpiprocs=4:ompthreads=2");
        strBuilder.append(System.lineSeparator());
        strBuilder.append("#PBS -l walltime=0:30:00");
        strBuilder.append(System.lineSeparator());
        strBuilder.append("#PBS -W group_list=GROUP");
        strBuilder.append(System.lineSeparator());
        strBuilder.append(System.lineSeparator());

        strBuilder.append("if [ -z \"${PBS_O_WORKDIR}\" ]; then");
        strBuilder.append(System.lineSeparator());
        strBuilder.append("cd ${PBS_O_WORKDIR}");
        strBuilder.append(System.lineSeparator());
        strBuilder.append("fi");
        strBuilder.append(System.lineSeparator());
        strBuilder.append(System.lineSeparator());

        strBuilder.append("${" + WORD_QE_COMMAND + "}");
        strBuilder.append(System.lineSeparator());
        strBuilder.append(System.lineSeparator());

        this.jobScript = strBuilder.toString();
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
