package com.java.yaml.convertors;

import java.util.Map;

public interface SpringBootConvertor {

    Map<String, String> convertIntoMap(Map<String, Object> muleXMLMap);
}
