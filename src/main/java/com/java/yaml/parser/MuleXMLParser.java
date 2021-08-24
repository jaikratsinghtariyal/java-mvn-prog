package com.java.yaml.parser;

import com.java.yaml.utility.ApplicationUtility;
import com.java.yaml.convertors.MuleXMLConverter;
import com.thoughtworks.xstream.XStream;

import java.io.IOException;
import java.util.Map;

public class MuleXMLParser {

    public Map<String, Object> parseMuleXML(String repoClonePath) throws IOException {
        String xml = ApplicationUtility.readFromInputStream(repoClonePath);
        XStream xStream = new XStream();
        xStream.registerConverter(new MuleXMLConverter());
        xStream.alias("mule", Map.class);
        return (Map<String, Object>) xStream.fromXML(xml);
    }
}
