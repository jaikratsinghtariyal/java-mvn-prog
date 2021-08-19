package com.java.yaml.model;

public class Input {

    private String muleAPIGITRepoName;
    private String newSBArtifactName;
    private String muleAPIFlavour;

    public String getMuleAPIGITRepoName() {
        return muleAPIGITRepoName;
    }

    public void setMuleAPIGITRepoName(String muleAPIGITRepoName) {
        this.muleAPIGITRepoName = muleAPIGITRepoName;
    }

    public String getNewSBArtifactName() {
        return newSBArtifactName;
    }

    public void setNewSBArtifactName(String newSBArtifactName) {
        this.newSBArtifactName = newSBArtifactName;
    }

    public String getMuleAPIFlavour() {
        return muleAPIFlavour;
    }

    public void setMuleAPIFlavour(String muleAPIFlavour) {
        this.muleAPIFlavour = muleAPIFlavour;
    }
}
