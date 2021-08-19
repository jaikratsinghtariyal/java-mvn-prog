package com.java.yaml.parser;

import com.java.yaml.convertor.ApplicationUtility;
import com.java.yaml.convertors.MuleXMLConverter;
import com.thoughtworks.xstream.XStream;

import java.io.IOException;
import java.util.Map;

public class MuleXMLParser {

    public Map<String, Object> parseMuleXML(Map<String, String> commonAttributes) throws IOException {
        String xml = ApplicationUtility.readFromInputStream(commonAttributes);
        XStream xStream = new XStream();
        xStream.registerConverter(new MuleXMLConverter());
        xStream.alias("mule", Map.class);
        return (Map<String, Object>) xStream.fromXML(xml);
    }
}
