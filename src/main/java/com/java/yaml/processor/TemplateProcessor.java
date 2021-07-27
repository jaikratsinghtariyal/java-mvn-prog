package com.java.yaml.processor;

import java.io.File;
import java.util.Map;

public class TemplateProcessor {

    protected File processTemplateToFile(Map<String, Object> templateData, String templateName,
                                         String outputFilename) {
        String adjustedOutputFilename = outputFilename.replaceAll("//", "/").replace('/',
                File.separatorChar);

        return null;
    }
}
