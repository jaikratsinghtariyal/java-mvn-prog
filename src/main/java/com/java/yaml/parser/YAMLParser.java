package com.java.yaml.parser;

import com.java.yaml.generator.CodeGenerator;
import com.java.yaml.config.RamlConfigurator;
import com.java.yaml.convertor.ApplicationUtility;
import com.java.yaml.model.RAML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class YAMLParser {

    public static void main(String[] args) throws IOException {
        RamlConfigurator ramlConfigurator = new RamlConfigurator();
        new YAMLParser().invoke(ramlConfigurator);
    }

    private void invoke(RamlConfigurator ramlConfigurator) throws IOException {
        String templateText = ApplicationUtility.getResourceText("src/main/resources/raml/RAML.yaml");
        String json = ApplicationUtility.ramlToJSON(templateText);
       // System.out.println(json);
        Map<String, ? extends Object> map = ApplicationUtility.jsonToMap(json);
        RAML raml = new RAML();
        raml.setTypes((Map) map.get("types"));

        List<File> files = new ArrayList<File>();
        CodeGenerator obj = new CodeGenerator(raml, ramlConfigurator.getProperties(), map);

        generateModels(files, obj);
        generateApis(files, obj);
        //generateSupportingFiles(map, raml);
    }

    private void generateModels(List<File> files, CodeGenerator obj) throws IOException {
        obj.generateModels(files);
    }

    private void generateSupportingFiles(List<File> files, CodeGenerator obj) throws IOException {
        //obj.generateController(files);
    }

    private void generateApis(List<File> files, CodeGenerator obj) throws IOException {
        obj.generateController(files);
    }
}
