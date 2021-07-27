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
        generateModels(map, raml, ramlConfigurator.getProperties());
        //generateApis(map, raml);
        //generateSupportingFiles(map, raml);
    }

    private void generateModels(Map<String, ? extends Object> map, RAML raml, Properties properties) throws IOException {
        Map modelMap = (Map) map.get("types");
        raml.setTypes(modelMap);

        CodeGenerator obj = new CodeGenerator(raml, properties, map);
        List<File> files = new ArrayList<File>();

        obj.generateModels(files);
        obj.generateController(files);
    }

    private void generateSupportingFiles(Map<String, ? extends Object> map, RAML raml) {

    }

    private void generateApis(Map<String, ? extends Object> map, RAML raml) {

    }
}
