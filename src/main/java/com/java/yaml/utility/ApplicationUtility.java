package com.java.yaml.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.java.yaml.parser.YAMLParserService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationUtility {

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


    public static String readFromInputStream(String repoClonePath)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        String filePath = repoClonePath.concat("/src/main/mule/mule.xml");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    public static void deleteDirectory(File file) {
        if (file.listFiles() == null){
            return;
        }
        for (File subfile : file.listFiles()) {
            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
            subfile.delete();
        }
    }

    public static void deleteFile(File file) {
        file.delete();
    }

    public static List<String> processInputFile() throws IOException {
        String inputFilePath = YAMLParserService.class.getResource("/input.txt").getPath();
        File file = new File(inputFilePath);

        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();

        String st;
        List<String> inputList = new ArrayList<>();
        while ((st = br.readLine()) != null) {
            inputList.add(st);
        }

        return inputList;
    }
}
