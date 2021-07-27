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

    @Override
    public String toString() {
        return String.format("%s(%s)", name, classname);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RamlModel that = (RamlModel) o;

        if (parent != null ? !parent.equals(that.parent) : that.parent != null)
            return false;
        if (parentSchema != null ? !parentSchema.equals(that.parentSchema) : that.parentSchema != null)
            return false;
        if (interfaces != null ? !interfaces.equals(that.interfaces) : that.interfaces != null)
            return false;
        if (parentModel != null ? !parentModel.equals(that.parentModel) : that.parentModel != null)
            return false;
        if (interfaceModels != null ? !interfaceModels.equals(that.interfaceModels) : that.interfaceModels != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (classname != null ? !classname.equals(that.classname) : that.classname != null)
            return false;
        if (title != null ? !title.equals(that.title) : that.title != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (classVarName != null ? !classVarName.equals(that.classVarName) : that.classVarName != null)
            return false;
        if (modelJson != null ? !modelJson.equals(that.modelJson) : that.modelJson != null)
            return false;
        if (dataType != null ? !dataType.equals(that.dataType) : that.dataType != null)
            return false;
        if (xmlPrefix != null ? !xmlPrefix.equals(that.xmlPrefix) : that.xmlPrefix != null)
            return false;
        if (xmlNamespace != null ? !xmlNamespace.equals(that.xmlNamespace) : that.xmlNamespace != null)
            return false;
        if (xmlName != null ? !xmlName.equals(that.xmlName) : that.xmlName != null)
            return false;
        if (classFilename != null ? !classFilename.equals(that.classFilename) : that.classFilename != null)
            return false;
        if (unescapedDescription != null ? !unescapedDescription.equals(that.unescapedDescription) : that.unescapedDescription != null)
            return false;
        if (discriminator != null ? !discriminator.equals(that.discriminator) : that.discriminator != null)
            return false;
        if (defaultValue != null ? !defaultValue.equals(that.defaultValue) : that.defaultValue != null)
            return false;
        if (vars != null ? !vars.equals(that.vars) : that.vars != null)
            return false;
        if (requiredVars != null ? !requiredVars.equals(that.requiredVars) : that.requiredVars != null)
            return false;
        if (optionalVars != null ? !optionalVars.equals(that.optionalVars) : that.optionalVars != null)
            return false;
        if (allVars != null ? !allVars.equals(that.allVars) : that.allVars != null)
            return false;
        if (allowableValues != null ? !allowableValues.equals(that.allowableValues) : that.allowableValues != null)
            return false;
        if (mandatory != null ? !mandatory.equals(that.mandatory) : that.mandatory != null)
            return false;
        if (allMandatory != null ? !allMandatory.equals(that.allMandatory) : that.allMandatory != null)
            return false;
        if (imports != null ? !imports.equals(that.imports) : that.imports != null)
            return false;
        if (hasVars != that.hasVars)
            return false;
        if (emptyVars != that.emptyVars)
            return false;
        if (hasMoreModels != that.hasMoreModels)
            return false;
        if (hasEnums != that.hasEnums)
            return false;
        if (isEnum != that.isEnum)
            return false;
        if (!Objects.equals(hasOnlyReadOnly, that.hasOnlyReadOnly))
            return false;
        if (!Objects.equals(hasChildren, that.hasChildren))
            return false;
        if (!Objects.equals(parentVars, that.parentVars))
            return false;
        return vendorExtensions != null ? vendorExtensions.equals(that.vendorExtensions) : that.vendorExtensions == null;

    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (parentSchema != null ? parentSchema.hashCode() : 0);
        result = 31 * result + (interfaces != null ? interfaces.hashCode() : 0);
        result = 31 * result + (parentModel != null ? parentModel.hashCode() : 0);
        result = 31 * result + (interfaceModels != null ? interfaceModels.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (classname != null ? classname.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (classVarName != null ? classVarName.hashCode() : 0);
        result = 31 * result + (modelJson != null ? modelJson.hashCode() : 0);
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        result = 31 * result + (xmlPrefix != null ? xmlPrefix.hashCode() : 0);
        result = 31 * result + (xmlNamespace != null ? xmlNamespace.hashCode() : 0);
        result = 31 * result + (xmlName != null ? xmlName.hashCode() : 0);
        result = 31 * result + (classFilename != null ? classFilename.hashCode() : 0);
        result = 31 * result + (unescapedDescription != null ? unescapedDescription.hashCode() : 0);
        result = 31 * result + (discriminator != null ? discriminator.hashCode() : 0);
        result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
        result = 31 * result + (vars != null ? vars.hashCode() : 0);
        result = 31 * result + (requiredVars != null ? requiredVars.hashCode() : 0);
        result = 31 * result + (optionalVars != null ? optionalVars.hashCode() : 0);
        result = 31 * result + (allVars != null ? allVars.hashCode() : 0);
        result = 31 * result + (allowableValues != null ? allowableValues.hashCode() : 0);
        result = 31 * result + (mandatory != null ? mandatory.hashCode() : 0);
        result = 31 * result + (allMandatory != null ? allMandatory.hashCode() : 0);
        result = 31 * result + (imports != null ? imports.hashCode() : 0);
        result = 31 * result + (hasVars ? 13:31);
        result = 31 * result + (emptyVars ? 13:31);
        result = 31 * result + (hasMoreModels ? 13:31);
        result = 31 * result + (hasEnums ? 13:31);
        result = 31 * result + (isEnum ? 13:31);
        result = 31 * result + (vendorExtensions != null ? vendorExtensions.hashCode() : 0);
        result = 31 * result + Objects.hash(hasOnlyReadOnly);
        result = 31 * result + Objects.hash(hasChildren);
        result = 31 * result + Objects.hash(parentVars);
        return result;
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
