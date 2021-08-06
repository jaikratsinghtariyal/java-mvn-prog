package com.java.yaml.convertors;

import java.util.HashMap;
import java.util.Map;

public class MQConnectorConvertor implements SpringBootConvertor {

    @Override
    public Map<String, String> convertIntoMap(Map<String, Object> muleXMLMap) {
        String localPort = (String)((Map)((Map)muleXMLMap.get("http:listener-config")).get("http:listener-connection")).get("port");

        String host = (String) ((Map)((Map)((Map)((Map)muleXMLMap.get("ibm-mq:config")).get("ibm-mq:connection")).get("ibm-mq:connection-mode")).get("ibm-mq:client")).get("host");
        String port = (String) ((Map)((Map)((Map)((Map)muleXMLMap.get("ibm-mq:config")).get("ibm-mq:connection")).get("ibm-mq:connection-mode")).get("ibm-mq:client")).get("port");
        String queueManager = (String) ((Map)((Map)((Map)((Map)muleXMLMap.get("ibm-mq:config")).get("ibm-mq:connection")).get("ibm-mq:connection-mode")).get("ibm-mq:client")).get("queueManager");
        String channel = (String) ((Map)((Map)((Map)((Map)muleXMLMap.get("ibm-mq:config")).get("ibm-mq:connection")).get("ibm-mq:connection-mode")).get("ibm-mq:client")).get("channel");
        String destination = (String) (((Map)((Map)muleXMLMap.get("flow")).get("ibm-mq:publish")).get("destination"));
        String username = (String) (((Map)((Map)muleXMLMap.get("ibm-mq:config")).get("ibm-mq:connection")).get("username"));
        String password = (String) (((Map)((Map)muleXMLMap.get("ibm-mq:config")).get("ibm-mq:connection")).get("password"));

        Map<String, String> muleValuedMap = new HashMap<>();
        muleValuedMap.put("localPort",  localPort);
        muleValuedMap.put("connName",  host.concat("(").concat(port).concat(")"));
        muleValuedMap.put("queueManager",  queueManager);
        muleValuedMap.put("channel",  channel);
        muleValuedMap.put("destination",  destination);
        muleValuedMap.put("user",  username);
        muleValuedMap.put("password",  password);

        return muleValuedMap;
    }
}
