package com.java.yaml.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RamlResponse {

    public final List<ModelProperty> headers = new ArrayList<ModelProperty>();
    public String code, message;
    public boolean hasMore;
    public List<Map<String, Object>> examples;
    public String dataType, baseType, containerType;
    public boolean hasHeaders;
    public boolean isString, isNumeric, isInteger, isLong, isNumber, isFloat, isDouble, isByteArray, isBoolean, isDate, isDateTime, isUuid;
    public boolean isDefault;
    public boolean simpleType;
    public boolean primitiveType;
    public boolean isMapContainer;
    public boolean isListContainer;
    public boolean isBinary = false;
    public boolean isFile = false;
    public Object schema;
    public String jsonSchema;
    public Map<String, Object> vendorExtensions;

    public boolean isWildcard() {
        return "0".equals(code) || "default".equals(code);
    }
}
