package com.java.yaml.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class RamlConfigurator {
    private Properties properties;
    private String templateDir;
    private String auth;
    private String apiPackage;
    private String modelPackage;
    private String invokerPackage;
    private String modelNamePrefix;
    private String modelNameSuffix;
    private String groupId;
    private String artifactId;
    private String artifactVersion;
    private String library;
    private String ignoreFileOverride;
    private String baseRepoPath;
    private Map<String, String> commonAttributes;

    public RamlConfigurator() {
        InputStream iStream = null;
        try {
            // Loading properties file from the path (relative path given here)
            iStream = this.getClass().getClassLoader().getResourceAsStream("config/class-locations.properties");
            properties = new Properties();
            properties.load(iStream);

            iStream = this.getClass().getClassLoader().getResourceAsStream("config/reserverd-word-mapping.properties");
            properties.load(iStream);

            iStream = this.getClass().getClassLoader().getResourceAsStream("config/template-mapping.properties");
            properties.load(iStream);

            iStream = this.getClass().getClassLoader().getResourceAsStream("config/method-type-mapping.properties");
            properties.load(iStream);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (iStream != null) {
                    iStream.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String getTemplateDir() {
        return templateDir;
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir = templateDir;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getApiPackage() {
        return apiPackage;
    }

    public void setApiPackage(String apiPackage) {
        this.apiPackage = apiPackage;
    }

    public String getModelPackage() {
        return modelPackage;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public String getInvokerPackage() {
        return invokerPackage;
    }

    public void setInvokerPackage(String invokerPackage) {
        this.invokerPackage = invokerPackage;
    }

    public String getModelNamePrefix() {
        return modelNamePrefix;
    }

    public void setModelNamePrefix(String modelNamePrefix) {
        this.modelNamePrefix = modelNamePrefix;
    }

    public String getModelNameSuffix() {
        return modelNameSuffix;
    }

    public void setModelNameSuffix(String modelNameSuffix) {
        this.modelNameSuffix = modelNameSuffix;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getArtifactVersion() {
        return artifactVersion;
    }

    public void setArtifactVersion(String artifactVersion) {
        this.artifactVersion = artifactVersion;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public String getIgnoreFileOverride() {
        return ignoreFileOverride;
    }

    public void setIgnoreFileOverride(String ignoreFileOverride) {
        this.ignoreFileOverride = ignoreFileOverride;
    }

    public String getBaseRepoPath() {
        return baseRepoPath;
    }

    public void setBaseRepoPath(String baseRepoPath) {
        this.baseRepoPath = baseRepoPath;
    }

    public Map<String, String> getCommonAttributes() {
        return commonAttributes;
    }

    public void setCommonAttributes(Map<String, String> commonAttributes) {
        this.commonAttributes = commonAttributes;
    }
}
