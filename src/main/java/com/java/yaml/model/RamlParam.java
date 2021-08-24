package com.java.yaml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RamlParam {
    public boolean isFormParam, isQueryParam, isPathParam, isHeaderParam,
            isCookieParam, isBodyParam, hasMore, isContainer,
            secondaryParam, isCollectionFormatMulti, isPrimitiveType;
    public String baseName, paramName, dataType, datatypeWithEnum, dataFormat,
            collectionFormat, description, unescapedDescription, baseType, defaultValue, enumName;

    public String example; // example value (x-example)
    public String testExample;
    public String jsonSchema;
    public boolean isString, isNumeric, isInteger, isLong, isNumber, isFloat, isDouble, isByteArray, isBinary, isBoolean, isDate, isDateTime, isUuid;
    public boolean isListContainer, isMapContainer;
    public boolean isFile, notFile;
    public boolean isEnum, isConstEnumParam;
    public List<String> _enum;
    public Map<String, Object> allowableValues;
    public ModelProperty items;
    public Map<String, Object> vendorExtensions;
    public boolean hasValidation;

    /**
     * Determines whether this parameter is mandatory. If the parameter is in "path",
     * this property is required and its value MUST be true. Otherwise, the property
     * MAY be included and its default value is false.
     */
    public boolean required;

    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor17.
     */
    public String maximum;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor17
     */
    public boolean exclusiveMaximum;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor21
     */
    public String minimum;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor21
     */
    public boolean exclusiveMinimum;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor26
     */
    public Integer maxLength;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor29
     */
    public Integer minLength;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor33
     */
    public String pattern;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor42
     */
    public Integer maxItems;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor45
     */
    public Integer minItems;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor49
     */
    public boolean uniqueItems;
    /**
     * See http://json-schema.org/latest/json-schema-validation.html#anchor14
     */
    public Number multipleOf;
}
