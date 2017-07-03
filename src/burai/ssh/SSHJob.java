/*
 * Copyright (C) 2017 Satomichi Nishihara
 *
 * This file is distributed under the terms of the
 * GNU General Public License. See the file `LICENSE'
 * in the root directory of the present distribution,
 * or http://www.gnu.org/copyleft/gpl.txt .
 */

package burai.ssh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import burai.com.file.FileTools;
import burai.input.QEInput;
import burai.project.Project;
import burai.run.InputEditor;
import burai.run.RunningCondition;
import burai.run.RunningType;

public class SSHJob {

    private static final String DUMMY_INP_NAME = "__INP_NAME__";

    private Project project;

    private SSHServer sshServer;

    private RunningType type;

    private int numProcesses;

    private int numThreads;

    private List<File> inpFiles;

    private List<String> commands;

    public SSHJob(Project project, SSHServer sshServer) {
        if (project == null) {
            throw new IllegalArgumentException("project is null.");
        }

        if (sshServer == null) {
            throw new IllegalArgumentException("sshServer is null.");
        }

        this.project = project;

        this.sshServer = sshServer;

        this.type = RunningType.SCF;
        this.numProcesses = 1;
        this.numThreads = 1;

        this.inpFiles = null;
        this.commands = null;
    }

    public Project getProject() {
        return this.project;
    }

    public SSHServer getSSHServer() {
        return this.sshServer;
    }

    public RunningType getType() {
        return this.type;
    }

    public void setType(RunningType type) {
        if (type != null) {
            this.type = type;
        }
    }

    public int getNumProcesses() {
        return this.numProcesses;
    }

    public void setNumProcesses(int numProcesses) {
        if (numProcesses > 0) {
            this.numProcesses = numProcesses;
        }
    }

    public int getNumThreads() {
        return this.numThreads;
    }

    public void setNumThreads(int numThreads) {
        if (numThreads > 0) {
            this.numThreads = numThreads;
        }
    }

    public void postJobToServer() {
        this.setupCommands();

        if (this.inpFiles == null || this.inpFiles.isEmpty()) {
            return;
        }

        if (this.commands == null || this.commands.isEmpty()) {
            return;
        }

        System.out.println("post -> " + sshServer);
        for (String command : this.commands) {
            System.out.println(command);
        }

        // TODO
    }

    private void setupCommands() {

        if (this.inpFiles == null) {
            this.inpFiles = new ArrayList<File>();
        } else {
            this.inpFiles.clear();
        }

        if (this.commands == null) {
            this.commands = new ArrayList<String>();
        } else {
            this.commands.clear();
        }

        File directory = this.getDirectory();
        if (directory == null) {
            return;
        }

        this.project.resolveQEInputs();
        QEInput input = this.type.getQEInput(this.project);
        if (input == null) {
            return;
        }

        List<String[]> commandList = this.type.getCommandList(DUMMY_INP_NAME, this.numProcesses);
        if (commandList == null || commandList.isEmpty()) {
            return;
        }

        List<RunningCondition> conditionList = this.type.getConditionList();
        if (conditionList == null || conditionList.size() < commandList.size()) {
            return;
        }

        List<InputEditor> inputEditorList = this.type.getInputEditorList(this.project);
        if (inputEditorList == null || inputEditorList.size() < commandList.size()) {
            return;
        }

        List<String> inpNameList = this.type.getInpNameList(this.project);
        if (inpNameList == null || inpNameList.size() < commandList.size()) {
            return;
        }

        List<String> logNameList = this.type.getLogNameList(this.project);
        if (logNameList == null || logNameList.size() < commandList.size()) {
            return;
        }

        List<String> errNameList = this.type.getErrNameList(this.project);
        if (errNameList == null || errNameList.size() < commandList.size()) {
            return;
        }

        this.deleteExitFile(directory);

        for (int i = 0; i < commandList.size(); i++) {
            String[] command = commandList.get(i);
            if (command == null || command.length < 1) {
                continue;
            }

            RunningCondition condition = conditionList.get(i);
            if (condition == null) {
                continue;
            }

            InputEditor inputEditor = inputEditorList.get(i);
            if (inputEditor == null) {
                continue;
            }

            String inpName = inpNameList.get(i);
            inpName = inpName == null ? null : inpName.trim();
            if (inpName == null || inpName.isEmpty()) {
                continue;
            }

            String logName = logNameList.get(i);
            logName = logName == null ? null : logName.trim();
            if (logName == null || logName.isEmpty()) {
                continue;
            }

            String errName = errNameList.get(i);
            errName = errName == null ? null : errName.trim();
            if (errName == null || errName.isEmpty()) {
                continue;
            }

            QEInput input2 = inputEditor.editInput(input);
            if (input2 == null) {
                continue;
            }

            if (!condition.toRun(this.project, input2)) {
                continue;
            }

            String command0 = null;
            for (String token : command) {
                token = token == null ? null : token.trim();
                if (token == null || token.isEmpty()) {
                    continue;
                }
                if (DUMMY_INP_NAME.equals(token)) {
                    token = inpName;
                }
                if (command0 == null) {
                    command0 = token;
                } else {
                    command0 = command0 + " " + token;
                }
            }

            command0 = command0 == null ? null : command0.trim();
            if (command0 == null || command0.isEmpty()) {
                continue;
            }

            File inpFile = new File(directory, inpName);
            boolean inpStatus = this.writeQEInput(input2, inpFile);
            if (!inpStatus) {
                continue;
            }

            File logFile = new File(directory, logName);
            File errFile = new File(directory, errName);
            this.deleteLogFiles(logFile, errFile);

            this.inpFiles.add(inpFile);
            this.commands.add(command0);
        }
    }

    private File getDirectory() {
        String dirPath = this.project.getDirectoryPath();
        if (dirPath == null) {
            return null;
        }

        File dirFile = new File(dirPath);
        try {
            if (!dirFile.isDirectory()) {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return dirFile;
    }

    private boolean writeQEInput(QEInput input, File file) {
        if (input == null) {
            return false;
        }

        if (file == null) {
            return false;
        }

        String strInput = input.toString();
        if (strInput == null) {
            return false;
        }

        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            writer.println(strInput);

        } catch (IOException e) {
            e.printStackTrace();
            return false;

        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        return true;
    }

    private void deleteExitFile(File directory) {
        if (directory == null) {
            return;
        }

        String exitName = this.project.getExitFileName();
        exitName = exitName == null ? null : exitName.trim();
        if (exitName != null && (!exitName.isEmpty())) {
            try {
                File exitFile = new File(directory, exitName);
                if (exitFile.exists()) {
                    FileTools.deleteAllFiles(exitFile, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteLogFiles(File logFile, File errFile) {
        try {
            if (logFile != null && logFile.exists()) {
                FileTools.deleteAllFiles(logFile, false);
            }

            if (errFile != null && errFile.exists()) {
                FileTools.deleteAllFiles(errFile, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
