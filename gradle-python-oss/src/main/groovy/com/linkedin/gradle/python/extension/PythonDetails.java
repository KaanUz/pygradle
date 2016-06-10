package com.linkedin.gradle.python.extension;

import aQute.bnd.build.Run;
import com.linkedin.gradle.python.util.internal.ExecutablePathUtils;
import org.gradle.api.Project;
import org.gradle.internal.os.OperatingSystem;

import java.io.File;


public class PythonDetails {

    private final Project project;

    private final File virtualEnv;
    private File activateLink;
    private File pythonInterpreter;
    private String virtualEnvPrompt;
    private PythonVersion pythonVersion;

    public PythonDetails(Project project, File virtualenvLocation) {
        this.project = project;
        pythonInterpreter = new File("/usr/bin/python");
        updateFromPythonInterpreter();

        virtualEnv = virtualenvLocation;
        activateLink = new File(project.getProjectDir(), "activate");
        virtualEnvPrompt = String.format("(%s)", project.getName());
    }

    private void updateFromPythonInterpreter() {
        if(pythonInterpreter == null || !pythonInterpreter.exists()) {
            throw new RuntimeException("Unable to find or execute python");
        }
        pythonVersion = new PythonVersion(PythonVersionParser.parsePythonVersion(project, pythonInterpreter));
    }

    public String getVirtualEnvPrompt() {
        return virtualEnvPrompt;
    }

    public void setVirtualEnvPrompt(String virtualEnvPrompt) {
        this.virtualEnvPrompt = virtualEnvPrompt;
    }

    public File getVirtualEnv() {
        return virtualEnv;
    }

    public File getVirtualEnvInterpreter() {
        return new File(virtualEnv, "bin/python");
    }

    public File getSystemPythonInterpreter() {
        return pythonInterpreter;
    }

    public File getActivateLink() {
        return activateLink;
    }

    public void setActivateLink(File activateLink) {
        this.activateLink = activateLink;
    }

    public void setPythonVersion(String pythonVersion) {
        pythonInterpreter = ExecutablePathUtils.getExecutable(String.format("python%s", pythonVersion));
        if(null == pythonInterpreter) {
            pythonInterpreter = new File(String.format("/export/apps/python/%s/bin/python%s", pythonVersion, pythonVersion));
        }
        updateFromPythonInterpreter();
    }

    public void setSystemPythonInterpreter(String path) {
        pythonInterpreter = new File(path);
        if (!pythonInterpreter.exists()) {
            throw new RuntimeException("Unable to find " + path);
        }

        updateFromPythonInterpreter();
    }

    public PythonVersion getPythonVersion() {
        return pythonVersion;
    }
}