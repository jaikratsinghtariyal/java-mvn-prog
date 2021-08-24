package com.java.yaml.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.*;

public class RamlModel {

    public String parent, parentSchema;
    public List<String> interfaces;

    // References to parent and interface RamlModels. Only set when code generator supports inheritance.
    public RamlModel parentModel;
    public List<RamlModel> interfaceModels;
    public List<RamlModel> children;

    public String name, classname, title, description, classVarName, modelJson, dataType, xmlPrefix, xmlNamespace, xmlName;
    public String classFilename; // store the class file name, mainly used for import
    public String unescapedDescription;
    public String discriminator, discriminatorClassVarName;
    public String defaultValue;
    public String arrayModelType;
    public boolean isAlias; // Is this effectively an alias of another simple type
    public List<ModelProperty> vars = new ArrayList<ModelProperty>();
    public List<ModelProperty> requiredVars = new ArrayList<ModelProperty>(); // a list of required properties
    public List<ModelProperty> optionalVars = new ArrayList<ModelProperty>(); // a list of optional properties
    public List<ModelProperty> readOnlyVars = new ArrayList<ModelProperty>(); // a list of read-only properties
    public List<ModelProperty> readWriteVars = new ArrayList<ModelProperty>(); // a list of properties for read, write
    public List<ModelProperty> allVars;
    public List<ModelProperty> parentVars = new ArrayList<>();
    public Map<String, Object> allowableValues;

    // Sorted sets of required parameters.
    public Set<String> mandatory = new TreeSet<String>();
    public Set<String> allMandatory;

    public Set<String> imports = new TreeSet<String>();
    public boolean hasVars, emptyVars, hasMoreModels, hasEnums, isEnum, hasRequired, hasOptional, isArrayModel, hasChildren;
    public ModelProperty parentContainer;
    public boolean hasOnlyReadOnly = true; // true if all properties are read-only

    public Map<String, Object> vendorExtensions;

    //The type of the value from additional properties. Used in map like objects.
    public String additionalPropertiesType;

    {
        // By default these are the same collections. Where the code generator supports inheritance, composed models
        // store the complete closure of owned and inherited properties in allVars and allMandatory.
        allVars = vars;
        allMandatory = mandatory;
    }

    public boolean getIsInteger() {
        return "Integer".equalsIgnoreCase(this.dataType);
    }

    public boolean getIsNumber() {
        return "BigDecimal".equalsIgnoreCase(this.dataType);
    }

   /* private String reference;
    private String title;
    private Map<String, Object> vendorExtensions = new LinkedHashMap();
    private BigDecimal minimum;
    private BigDecimal maximum;
    private BigDecimal multipleOf;
    private Boolean exclusiveMinimum;
    private Boolean exclusiveMaximum;
    private Integer minLength;
    private Integer maxLength;
    private String pattern;
    protected Map<String, ModelProperty> properties;
    protected List<String> required;
    public static final String OBJECT = "object";

    private String type;
    private String format;
    private String name;
    private Boolean allowEmptyValue;
    private Boolean uniqueItems;
    private boolean isSimple = false;
    private String description;
    private Object example;
    private ModelProperty additionalProperties;
    private String discriminator;

    @JsonProperty("default")
    private String defaultValue;
    private List<String> _enum;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getVendorExtensions() {
        return vendorExtensions;
    }

    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        this.vendorExtensions = vendorExtensions;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    public BigDecimal getMultipleOf() {
        return multipleOf;
    }

    public void setMultipleOf(BigDecimal multipleOf) {
        this.multipleOf = multipleOf;
    }

    public Boolean getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Map<String, ModelProperty> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, ModelProperty> properties) {
        this.properties = properties;
    }

    public List<String> getRequired() {
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }

    public static String getOBJECT() {
        return OBJECT;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }

    public Boolean getUniqueItems() {
        return uniqueItems;
    }

    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

    public boolean isSimple() {
        return isSimple;
    }

    public void setSimple(boolean simple) {
        isSimple = simple;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getExample() {
        return example;
    }

    public void setExample(Object example) {
        this.example = example;
    }

    public ModelProperty getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(ModelProperty additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<String> get_enum() {
        return _enum;
    }

    public void set_enum(List<String> _enum) {
        this._enum = _enum;
    }*/
}
