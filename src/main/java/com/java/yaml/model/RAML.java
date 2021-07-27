package com.java.yaml.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties
public class RAML {
    private String[] mediaType;
    private String[] protocols;
    private String title;
    private String version;
    private String baseUri;
    private Map<String, Map> types;

    public String[] getMediaType() {
        return mediaType;
    }

    public void setMediaType(String[] mediaType) {
        this.mediaType = mediaType;
    }

    public String[] getProtocols() {
        return protocols;
    }

    public void setProtocols(String[] protocols) {
        this.protocols = protocols;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public Map<String, Map> getTypes() {
        return types;
    }

    public void setTypes(Map<String, Map> types) {
        this.types = types;
    }

    public Map<String, Map> getResources() {
        return resources;
    }

    public void setResources(Map<String, Map> resources) {
        this.resources = resources;
    }

    private Map<String, Map> resources;



}
