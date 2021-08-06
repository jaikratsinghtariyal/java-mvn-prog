package com.java.yaml.convertors;

import java.util.HashMap;
import java.util.Map;

public class JDBCConnectorConvertor implements SpringBootConvertor {

    @Override
    public Map<String, String> convertIntoMap(Map<String, Object> muleXMLMap) {
        String localPort = (String) ((Map) ((Map) muleXMLMap.get("http:listener-config")).get("http:listener-connection")).get("port");
        String host = (String) ((Map) ((Map) muleXMLMap.get("db:config")).get("db:my-sql-connection")).get("host");
        String port = (String) ((Map) ((Map) muleXMLMap.get("db:config")).get("db:my-sql-connection")).get("port");
        String user = (String) ((Map) ((Map) muleXMLMap.get("db:config")).get("db:my-sql-connection")).get("user");
        String password = (String) ((Map) ((Map) muleXMLMap.get("db:config")).get("db:my-sql-connection")).get("password");
        String database = (String) ((Map) ((Map) muleXMLMap.get("db:config")).get("db:my-sql-connection")).get("database");

        String sql = (String) ((Map) ((Map) muleXMLMap.get("flow")).get("db:select")).get("db:sql");

        Map<String, String> muleValuedMap = new HashMap<>();
        muleValuedMap.put("localPort", localPort);
        muleValuedMap.put("host", host);
        muleValuedMap.put("dbport", port);
        muleValuedMap.put("user", user);
        muleValuedMap.put("password", password);
        muleValuedMap.put("database", database);
        //muleValuedMap.put("sql", sql.substring(sql.indexOf("from")));
        muleValuedMap.put("sql", sql);

        return muleValuedMap;
    }
}
