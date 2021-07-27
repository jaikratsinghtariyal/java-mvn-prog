package com.java.yaml.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationUtility {
    public static void main(String[] args) throws IOException {
        ApplicationUtility obj = new ApplicationUtility();
        String templateText = obj.getResourceText("src/main/resources/raml/RAML.yaml");
        String json = obj.ramlToJSON(templateText);
        Map<String,String> map = obj.jsonToMap(json);
        System.out.println(json);
    }

    public static String ramlToJSON(String templateText) throws IOException {
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj = yamlReader.readValue(templateText, Object.class);

        ObjectMapper jsonWriter = new ObjectMapper();
        return jsonWriter.writeValueAsString(obj);
    }

    public static Map<String,String> jsonToMap(String json) throws JsonProcessingException {
        Map<String,String> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<HashMap>(){});
    }

    //Read a file from the resource directory
    public static String getResourceText(String path) {
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
