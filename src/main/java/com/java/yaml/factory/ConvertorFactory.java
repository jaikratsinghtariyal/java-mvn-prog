package com.java.yaml.factory;

import com.java.yaml.convertors.JDBCConnectorConvertor;
import com.java.yaml.convertors.MQConnectorConvertor;
import com.java.yaml.convertors.RestConnectorConvertor;
import com.java.yaml.convertors.SpringBootConvertor;

public class ConvertorFactory {

    public SpringBootConvertor getConvertor(String input){
        switch (input) {
            case "REST":
                return new RestConnectorConvertor();
            case "DATABASE":
                return new JDBCConnectorConvertor();
            case "MQCLIENT":
                return new MQConnectorConvertor();
            default:
                return null;
        }
    }
}
