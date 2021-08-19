package com.java.yaml.convertors;

import java.util.HashMap;
import java.util.Map;

public class RestConnectorConvertor implements SpringBootConvertor {

    @Override
    public Map<String, String> convertIntoMap(Map<String, Object> muleXMLMap) {
        String localPort = (String)((Map)((Map)muleXMLMap.get("http:listener-config")).get("http:listener-connection")).get("port");
        String host = (String)((Map)((Map)muleXMLMap.get("http:listener-config")).get("http:listener-connection")).get("host");
        String port = (String)((Map)((Map)muleXMLMap.get("http:listener-config")).get("http:listener-connection")).get("port");

        String callingHost = (String)((Map)((Map)muleXMLMap.get("http:request-config")).get("http:request-connection")).get("host");
        String callingPort = (String)((Map)((Map)muleXMLMap.get("http:request-config")).get("http:request-connection")).get("port");
        String protocol = (String)((Map)((Map)muleXMLMap.get("http:request-config")).get("http:request-connection")).get("protocol");
        String endpoint = (String)((Map)muleXMLMap.get("http:request-config")).get("basePath");

        Map<String, String> muleValuedMap = new HashMap<>();
        muleValuedMap.put("localPort", localPort);
        muleValuedMap.put("host", host);
        muleValuedMap.put("port", port);
        muleValuedMap.put("callingHost", callingHost);
        muleValuedMap.put("callingPort", callingPort);
        muleValuedMap.put("endpoint", endpoint);
        muleValuedMap.put("protocol", protocol.toLowerCase());

        return muleValuedMap;
    }
}
