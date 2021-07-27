package com.java.mustache.examples;

import com.samskivert.mustache.Mustache;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JavaFileCreator {
    public static void main(String[] args) throws IOException {

        new JavaFileCreator().invoke();
    }

    private void invoke() throws IOException {
        Map map = new HashMap<>();
        map.put("appVersion", "1.0.5");
        map.put("generatorClass", "io.swagger.codegen.languages.JavaClientCodegen");
        map.put("supportJava6", false);
        map.put("groupId", "io.swagger");
        map.put("invokerPackage", "io.swagger.client");
        map.put("developerEmail", "apiteam@swagger.io");
        map.put("generateModelDocs", true);
        map.put("generateModelTests", true);
        map.put("generateApiTests", true);
        map.put("usePlayWS", false);
        map.put("generateModels", true);
        map.put("serializableModel", false);
        map.put("playVersion", "play25");
        map.put("inputSpec", "https://petstore.swagger.io/v2/swagger.json");
        map.put("artifactUrl", "https://github.com/swagger-api/swagger-codegen");
        map.put("developerOrganization", "Swagger");
        List list1 = new ArrayList<>();
        Map map1 = new HashMap();
        map1.put("importPath", "io.swagger.client.model.Category");
        map1.put("model", "io.swagger.client.model.Category");
        list1.add(map1);
        map.put("models", list1);
        map.put("package", "io.swagger.client.model");
        List list2 = new ArrayList<>();
        Map map2 = new HashMap();
        map2.put("import", "com.google.gson.TypeAdapter");

        Map map3 = new HashMap();
        map2.put("import", "com.google.gson.annotations.JsonAdapter");

        Map map4 = new HashMap();
        map2.put("import", "com.google.gson.annotations.SerializedName");

        list2.add(map2);
        map.put("imports", list2);
        map.put("fullJavaUtil", false);
        map.put("appDescription", "This is a sample server Petstore server.  You can find out more about Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).  For this sample, you can use the api key `special-key` to test the authorization filters.");
        map.put("javaUtilPrefix", "");
        map.put("licenseName", "Unlicense");
        map.put("releaseNote", "Minor update");
        map.put("version", "1.0.5");
        map.put("modelDocPath", "docs/");
        map.put("withXml", false);
        map.put("scmDeveloperConnection", "scm:git:git@github.com:swagger-api/swagger-codegen.git");
        map.put("licenseUrl", "http://www.apache.org/licenses/LICENSE-2.0.html");
        map.put("jsr310", "true");
        map.put("generatedYear", "2021");
        map.put("modelPackage", "io.swagger.client.model");
        map.put("licenseInfo", "Apache 2.0");
        map.put("apiDocPath", "docs/");

        map.put("generateApis", true);
        map.put("parcelableModel", false);
        map.put("developerOrganizationUrl", "http://swagger.io");
        map.put("threetenbp", true);
        map.put("artifactId", "swagger-java-client");
        map.put("artifactDescription", "Swagger Java");
        map.put("hideGenerationTimestamp", false);
        map.put("disableHtmlEscaping", false);
        map.put("developerName", "Swagger");
        map.put("scmConnection", "scm:git:git@github.com:swagger-api/swagger-codegen.git");
        map.put("artifactVersion", "1.0.0");
        map.put("doNotUseRx", true);
        map.put("appName", "Swagger Petstore");
        map.put("generateApiDocs", true);
        map.put("termsOfService", "http://swagger.io/terms/");
        map.put("generatorVersion", "unset");
        map.put("apiPackage", "io.swagger.client.api");
        map.put("scmUrl", "https://github.com/swagger-api/swagger-codegen");
        map.put("classname", "Category");
        map.put("gitRepoId", "GIT_REPO_ID");
        map.put("generatedDate", "2021-07-05T17:44:45.794+05:30");
        map.put("infoEmail", "apiteam@swagger.io");
        map.put("gson", "true");
        map.put("gitUserId", "GIT_USER_ID");
        map.put("vars", true);
        map.put("baseName", "id");
        map.put("getter", "getId");
        map.put("setter", "setId");
        map.put("isContainer", false);
        map.put("datatypeWithEnum", "Long");
        map.put("name", "id");
        map.put("defaultValue", "null");
        map.put("description", false);

        String templateText = getResourceText("src/main/resources/template/model.mustache");
        //Template template = Mustache.compiler().compile(templateText);
        //String out = template.execute(map);

        Mustache.Compiler c = Mustache.compiler().withLoader(new Mustache.TemplateLoader() {
            public Reader getTemplate (String name) {
                return getTemplateReader(name);
            }
        });

        String out = c.compile(templateText).execute(map);

        //Output the result
        System.out.println(out);
    }

    public Reader getTemplateReader(String name) {
        try {
            name = "template/" + name + ".mustache";
            System.out.println(name);
            //InputStream is = this.getClass().getClassLoader().getResourceAsStream(getCPResourcePath(name));
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(name);
            if (is == null) {
                is = new FileInputStream(new File(name)); // May throw but never return a null value
            }
            return new InputStreamReader(is, StandardCharsets.UTF_8);
        } catch (Exception ignored) {
        }
        throw new RuntimeException("can't load template " + name);
    }

    public String getCPResourcePath(String name) {
        if (!"/".equals(File.separator)) {
            return name.replaceAll(Pattern.quote(File.separator), "/");
        }
        return name;
    }

    //Read a file from the resource directory
    public String getResourceText(String path) throws IOException {
        String content = null;
        try {
            content = Files.lines(Paths.get(path))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }
}
