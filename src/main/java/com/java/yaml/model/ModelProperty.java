package com.java.yaml.model;

import java.util.*;

public class ModelProperty {
    public String baseName, complexType, getter, setter, description, datatype,
            datatypeWithEnum, dataFormat, name, min, max, defaultValue, defaultValueWithParam,
            baseType, containerType, title;

    /** The 'description' string without escape charcters needed by some programming languages/targets */
    public String unescapedDescription;

    /**
     * maxLength validation for strings, see http://json-schema.org/latest/json-schema-validation.html#rfc.section.5.2.1
     */
    public Integer maxLength;
    /**
     * minLength validation for strings, see http://json-schema.org/latest/json-schema-validation.html#rfc.section.5.2.2
     */
    public Integer minLength;
    /**
     * pattern validation for strings, see http://json-schema.org/latest/json-schema-validation.html#rfc.section.5.2.3
     */
    public String pattern;
    /**
     * A free-form property to include an example of an instance for this schema.
     */
    public String example;

    public String jsonSchema;
    public String minimum;
    public String maximum;
    public boolean exclusiveMinimum;
    public boolean exclusiveMaximum;
    public boolean hasMore, required, secondaryParam;
    public boolean hasMoreNonReadOnly; // for model constructor, true if next properyt is not readonly
    public boolean isPrimitiveType, isContainer, isNotContainer;
    public boolean isString, isNumeric, isInteger, isLong, isNumber, isFloat, isDouble, isByteArray, isBinary, isFile, isBoolean, isDate, isDateTime, isUuid;
    public boolean isListContainer, isMapContainer;
    public boolean isEnum;
    public boolean isReadOnly = false;
    public List<String> _enum;
    public Map<String, Object> allowableValues;
    public ModelProperty items;
    public Integer itemsDepth;
    public Map<String, Object> vendorExtensions;
    public boolean hasValidation; // true if pattern, maximum, etc are set (only used in the mustache template)
    public boolean isInherited;
    public String discriminatorValue;
    public String nameInCamelCase; // property name in camel case
    // enum name based on the property name, usually use as a prefix (e.g. VAR_NAME) for enum name (e.g. VAR_NAME_VALUE1)
    public String enumName;
    public Integer maxItems;
    public Integer minItems;
    public boolean uniqueItems;

    // XML
    public boolean isXmlAttribute = false;
    public String xmlPrefix;
    public String xmlName;
    public String xmlNamespace;
    public boolean isXmlWrapped = false;
}
