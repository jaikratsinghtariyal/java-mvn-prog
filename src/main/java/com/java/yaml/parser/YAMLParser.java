package com.java.yaml.parser;

import com.java.yaml.GenerateSampleProject;
import com.java.yaml.config.RamlConfigurator;
import com.java.yaml.convertor.ApplicationUtility;
import com.java.yaml.generator.CodeGenerator;
import com.java.yaml.model.RAML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String templateText = ApplicationUtility.getResourceText("src/main/resources/raml/Rest_RAML.yaml");
        String json = ApplicationUtility.ramlToJSON(templateText);
        // System.out.println(json);
        Map<String, ? extends Object> map = ApplicationUtility.jsonToMap(json);
        RAML raml = new RAML();
        raml.setTypes((Map) map.get("types"));

        List<File> files = new ArrayList<File>();
        CodeGenerator obj = new CodeGenerator(raml, ramlConfigurator.getProperties(),
                map, ramlConfigurator.getBaseRepoPath(), ramlConfigurator.getCommonAttributes());

        generateModels(files, obj);
        Map<String, Object> operation = generateControllerService(files, obj);
        //generateService(files, obj);
        generateSupportingFiles(files, obj, operation);
    }

    private void generateSupportingFiles(List<File> files, CodeGenerator obj, Map<String, Object> operation) throws IOException {
        obj.generateSupportingFiles(files, operation);
    }

    private void generateModels(List<File> files, CodeGenerator obj) throws IOException {
        obj.generateModels(files);
    }

    private void generateService(List<File> files, CodeGenerator obj) throws IOException {
        obj.generateService(files);
    }

    private Map<String, Object> generateControllerService(List<File> files, CodeGenerator obj) throws IOException {
        return obj.generateControllerService(files);
    }

    private static Map<String, String> prepareCommonAttributes() {
        Map<String, String> map = new HashMap<>();

        map.put("fileName", "spring-mule-hello.zip");
        map.put("projectName", "spring-mule-hello");
        map.put("mvnFoldeStruc", "/src/main/java");

        //"https://start.spring.io/starter.zip?type=maven-project&
        // language=$language&
        // bootVersion=$bootVersion&
        // baseDir=$baseDir&
        // groupId=$groupId&
        // artifactId=$artifactId&
        // name=$name&
        // description=$description&
        // packageName=$packageName&
        // packaging=$packaging&
        // javaVersion=$javaVersion&
        // dependencies=$dependencies";

        //https://start.spring.io/starter.zip?type=maven-project&
        // language=java&
        // bootVersion=2.5.3&
        // baseDir=spring-mule-hello&
        // com.test.spring=$com.test.spring&
        // spring-boot-hello=$spring-boot-hello&
        // name=spring-mule-hello&
        // description=Spring Mule Hello World Program&
        // packageName=com.example.java&
        // packaging=jar&
        // javaVersion=1.8&
        // dependencies=web

        map.put("$language", "java");
        map.put("$bootVersion", "2.5.3");
        map.put("$baseDir", "spring-mule-hello");
        map.put("$groupId", "com.example.java");
        map.put("$artifactId", "spring-mule-hello");
        map.put("$name", "spring-mule-hello");
        map.put("$description", "Spring Mule Hello World Program");
        map.put("$packageName", "com.example.java");
        map.put("$packaging", "jar");
        map.put("$javaVersion", "1.8");
        map.put("$dependencies", "web");    // dependencies can be comma seperate.

        map.put("modelPackage", map.get("$packageName").concat(".model"));
        map.put("controllerPackage", map.get("$packageName").concat(".controller"));
        map.put("servicePackage", map.get("$packageName").concat(".service"));
        map.put("clientPackage", map.get("$packageName").concat(".client"));
        map.put("dbClientPackage", map.get("$packageName").concat(".client"));
        map.put("configPackage", map.get("$packageName").concat(".config"));
        map.put("srcMainJava", "/src/main/java/");
        map.put("resources", "/src/main/resources/");

        map.put("artifactVersion", "0.0.1");
        map.put("java.version", "1.8");

        /**
         * Only one of the below entry will be TRUE.
         */
        map.put("restClient", "true");
        map.put("my-sql-database-call", "false");
        if(Boolean.parseBoolean(map.get("my-sql-database-call"))) {
            map.put("$dependencies", map.get("$dependencies").concat(",data-jpa,mysql"));
        }
        return map;
    }

}
