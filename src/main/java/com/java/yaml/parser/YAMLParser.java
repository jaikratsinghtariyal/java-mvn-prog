package com.java.yaml.parser;

import com.java.yaml.GenerateSampleProject;
import com.java.yaml.config.RamlConfigurator;
import com.java.yaml.convertor.ApplicationUtility;
import com.java.yaml.generator.CodeGenerator;
import com.java.yaml.model.RAML;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YAMLParser {

    public static void main(String[] args) throws IOException {
        Map<String, String> commonAttributes = prepareCommonAttributes();

        GenerateSampleProject obj = new GenerateSampleProject();
        String path = obj.createSpringBootSampleApp(commonAttributes);

        RamlConfigurator ramlConfigurator = new RamlConfigurator();
        ramlConfigurator.setBaseRepoPath(path);
        ramlConfigurator.setCommonAttributes(commonAttributes);

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
        CodeGenerator obj = new CodeGenerator(raml, ramlConfigurator.getProperties(),
                map, ramlConfigurator.getBaseRepoPath(), ramlConfigurator.getCommonAttributes());

        generateModels(files, obj);
        generateController(files, obj);
        generateService(files, obj);
        generateClient(files, obj);
        //generateSupportingFiles(obj);
    }

    private void generateSupportingFiles(CodeGenerator obj) {
        Map<String, String> pomAttributes = prepareCommonAttributes();
        obj.generateSupportingFiles(pomAttributes);
    }

    private void generateClient(List<File> files, CodeGenerator obj) {
    }

    private void generateModels(List<File> files, CodeGenerator obj) throws IOException {
        obj.generateModels(files);
    }

    private void generateService(List<File> files, CodeGenerator obj) throws IOException {
        obj.generateService(files);
    }

    private void generateController(List<File> files, CodeGenerator obj) throws IOException {
        obj.generateController(files);
    }


    private static Map<String, String> prepareCommonAttributes() {
        Map<String, String> map = new HashMap<>();
        //String url = "https://start.spring.io/starter.zip?type=maven-project&
        // language={java}&
        // bootVersion={bootVersion}&
        // baseDir={baseDir}&
        // groupId={groupId}&
        // artifactId={artifactId}&
        // name={name}&
        // description={description}&
        // packageName={packageName}&
        // packaging=jar&
        // javaVersion=1.8&
        // dependencies=web";

        String url = "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=2.5.3.RELEASE&baseDir=demo&groupId=com.example&artifactId=demo&name=demo&description=Demo%20project%20for%20Spring%20Boot&packageName=$packageName&packaging=jar&javaVersion=1.8&dependencies=web";

        map.put("fileName", "sping-mule-hello.zip");
        map.put("projectName", "sping-mule-hello");
        map.put("mvnFoldeStruc", "/src/main/java");

        map.put("$language", "java");
        map.put("$bootVersion", "2.5.3");
        map.put("$baseDir", "sping-mule-hello");
        map.put("$groupId", "com.example.demo");
        map.put("$artifactId", "sping-mule-hello");
        map.put("$name", "sping-mule-hello");
        map.put("$description", "Sping Mule Hello World Program");
        map.put("$packageName", "com.example.demo");
        map.put("$packaging", "jar");
        map.put("$javaVersion", "1.8");
        map.put("$dependencies", "web");    // dependencies can be comma seperate.

        map.put("modelPackage", "com.example.demo.model");
        map.put("controllerPackage", "com.example.demo.controller");
        map.put("servicePackage", "com.example.demo.service");
        map.put("srcMainJava", "/src/main/java/");

        map.put("groupId", "com.test.spring");
        map.put("artifactId", "spring-boot-hello");
        map.put("artifactVersion", "0.0.1");
        map.put("java.version", "1.8");

        return map;
    }

}
