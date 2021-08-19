package com.java.yaml.parser;

import com.java.yaml.model.Input;
import com.java.yaml.model.RAML;
import com.java.yaml.utility.GenerateSampleProject;
import com.java.yaml.config.RamlConfigurator;
import com.java.yaml.generator.CodeGenerator;
import com.java.yaml.utility.ApplicationUtility;
import com.java.yaml.utility.GITOpsUtility;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YAMLParserService {

    public static void main(String[] args)  { }

    //private void invoke(RamlConfigurator ramlConfigurator) throws IOException, GitAPIException, URISyntaxException {
    public void invoke(List<Input> inputs) throws IOException, GitAPIException, URISyntaxException {

        for(Input input : inputs) {
            Map<String, String> commonAttributes = prepareCommonAttributes(input);

            GenerateSampleProject generateSampleProject = new GenerateSampleProject();
            String path = generateSampleProject.createSpringBootSampleApp(commonAttributes);

            RamlConfigurator ramlConfigurator = new RamlConfigurator();
            ramlConfigurator.setBaseRepoPath(path);
            ramlConfigurator.setCommonAttributes(commonAttributes);

            String gitLink = input.getMuleAPIGITRepoName();
            String apiName = gitLink.substring(gitLink.lastIndexOf("/") + 1, gitLink.lastIndexOf("."));
            String repoClonePath = YAMLParserService.class.getResource("/temp").getPath();

            GITOpsUtility gitOpsUtility = new GITOpsUtility();
            gitOpsUtility.cloneRepo(gitLink, apiName, repoClonePath);

            String ramlPath = repoClonePath.concat("/").concat(apiName).concat("/src/main/api/");
            String templateText = ApplicationUtility.getResourceText(ramlPath.concat("Raml.yaml"));

            String json = ApplicationUtility.ramlToJSON(templateText);
            Map<String, ? extends Object> map = ApplicationUtility.jsonToMap(json);
            RAML raml = new RAML();
            raml.setTypes((Map) map.get("types"));

            List<File> files = new ArrayList<>();
            CodeGenerator obj = new CodeGenerator(raml, ramlConfigurator.getProperties(),
                    map, ramlConfigurator.getBaseRepoPath(), ramlConfigurator.getCommonAttributes());

            generateModels(files, obj);
            Map<String, Object> operation = generateControllerService(files, obj);
            //generateService(files, obj);
            generateSupportingFiles(files, obj, operation);

            //Git git = gitOpsUtility.gitInit("/Users/ja20105259/projects/mule-to-spring-boot/spring-mule-hello");
            //gitOpsUtility.pushBranch(git);
        }

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

    private static Map<String, String> prepareCommonAttributes(Input input) {
        Map<String, String> map = new HashMap<>();
        String artifactNAme = input.getNewSBArtifactName();

        map.put("fileName", artifactNAme.concat(".zip"));
        map.put("projectName", artifactNAme);
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
        map.put("$baseDir", artifactNAme);
        map.put("$groupId", "com.example.java");
        map.put("$artifactId", artifactNAme);
        map.put("$name", artifactNAme);
        map.put("$description", "Spring Mule Hello World Program");
        map.put("$packageName", "com.example.java");
        map.put("$packaging", "jar");
        map.put("$javaVersion", "1.8");
        map.put("$dependencies", "web");    // dependencies can be comma separate.

        map.put("modelPackage", map.get("$packageName").concat(".model"));
        map.put("controllerPackage", map.get("$packageName").concat(".controller"));
        map.put("servicePackage", map.get("$packageName").concat(".service"));
        map.put("clientPackage", map.get("$packageName").concat(".client"));
        map.put("dbClientPackage", map.get("$packageName").concat(".client"));
        map.put("mqClientPackage", map.get("$packageName").concat(".client"));
        map.put("configPackage", map.get("$packageName").concat(".config"));
        map.put("srcMainJava", "/src/main/java/");
        map.put("resources", "/src/main/resources/");

        map.put("artifactVersion", "0.0.1");
        map.put("java.version", "1.8");

        /**
         * Only one of the below entry will be TRUE.
         */
        map.put("restClient", input.getMuleAPIFlavour().equals("Rest") ? "true" : "false");
        map.put("my-sql-database-call", input.getMuleAPIFlavour().equals("Database") ? "true" : "false");
        if (Boolean.parseBoolean(map.get("my-sql-database-call"))) {
            map.put("$dependencies", map.get("$dependencies").concat(",data-jpa,mysql"));
        }
        map.put("mq-client", input.getMuleAPIFlavour().equals("MQ") ? "true" : "false");

        return map;
    }
}