package com.java.yaml.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.yaml.convertors.SpringBootConvertor;
import com.java.yaml.factory.ConvertorFactory;
import com.java.yaml.model.*;
import com.java.yaml.parser.MuleXMLParser;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;


import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeGenerator {

    private final RAML raml;
    private final Properties properties;
    private final Map ramlMap;
    private final Map<String, String> importMap = new HashMap<>();
    private final String basePath;
    private final Map<String, String> commonAttributes;

    public CodeGenerator(RAML raml, Properties properties, Map ramlMap, String basePath, Map<String, String> commonAttributes) {
        this.raml = raml;
        this.properties = properties;
        this.ramlMap = ramlMap;
        this.basePath = basePath;
        this.commonAttributes = commonAttributes;
    }

    public void generateModels(List<File> files) throws IOException {
        Map<String, Map> types = raml.getTypes();
        if (types == null) {
            return;
        }
        Map<String, Object> modelMap = processModel(types);

        for (String modelName : modelMap.keySet()) {
            //Map<String, Object> models = (Map<String, Object>) modelMap.get(modelName);
            RamlModel model = (RamlModel) modelMap.get(modelName);
            ObjectMapper m = new ObjectMapper();
            Map<String, Object> models = m.convertValue(model, Map.class);
            models.put("package", commonAttributes.get("modelPackage"));
            //models.put("modelPackage", properties.getProperty("model"));
            models.putAll(getClientMapInfo());
            String filename = javaClassFilename(modelName, "modelPackage");
            importMap.put(modelName, commonAttributes.get("modelPackage").concat(".").concat(modelName));
            File written = processTemplateToFile(models, "model.mustache", filename);
            files.add(written);
        }
    }

    private File processTemplateToFile(Map<String, Object> contentMap, String templateName, String outputFilename) throws IOException {
        String adjustedOutputFilename = outputFilename.replaceAll("//", "/").replace('/', File.separatorChar);
        String templateFile = getFullTemplateFile(templateName);
        String template = readTemplate(templateFile);
        Mustache.Compiler compiler = Mustache.compiler();
        Template tmpl = compiler
                .withLoader(new Mustache.TemplateLoader() {
                    @Override
                    public Reader getTemplate(String name) {
                        return getTemplateReader(getFullTemplateFile(name + ".mustache"));
                    }
                })
                .defaultValue("")
                .compile(template);

        writeToFile(adjustedOutputFilename, tmpl.execute(contentMap));
        return new File(adjustedOutputFilename);
    }

    public void writeToFile(String filename, String contents) throws IOException {
        contents = handleSpecialChars(contents);
        File output = new File(filename);

        if (output.getParent() != null && !new File(output.getParent()).exists()) {
            File parent = new File(output.getParent());
            parent.mkdirs();
        }
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(output), "UTF-8"));

        out.write(contents);
        out.close();
    }

    private String handleSpecialChars(String contents) {
        return contents.replaceAll("&#x3D;", "=");
    }

    public String getFullTemplateFile(String templateFile) {
        return "template/" + templateFile;
    }

    public String readTemplate(String name) {
        try {
            Reader reader = getTemplateReader(name);
            if (reader == null) {
                throw new RuntimeException("no file found");
            }
            Scanner s = new Scanner(reader).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (Exception ignored) {
        }
        throw new RuntimeException("can't load template " + name);
    }

    public Reader getTemplateReader(String name) {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(getCPResourcePath(name));
            if (is == null) {
                is = new FileInputStream(new File(name)); // May throw but never return a null value
            }
            return new InputStreamReader(is, "UTF-8");
        } catch (Exception ignored) {
        }
        throw new RuntimeException("can't load template " + name);
    }

    public String getCPResourcePath(String name) {
        if (!"/".equals(File.separator)) {
            return name.replaceAll(Pattern.quote(File.separator), "/");
        }
        return name;
    }

    public String javaClassFilename(String modelName, String packageName) {
        return getFileFolder(packageName) + File.separator + initialCaps(modelName) + ".java";
    }

    public String appPropFilename(String fileName) {
        return resourcesFileFolder() + fileName + ".properties";
    }

    public String resourcesFileFolder() {
        return this.basePath
                + commonAttributes.get("resources");
    }

    public String getFileFolder(String packageName) {
        return this.basePath
                + commonAttributes.get("srcMainJava")
                + commonAttributes.get(packageName).replaceAll("[.]", "/");
    }

    public String initialCaps(String name) {
        return StringUtils.capitalize(name);
    }

    private Map<String, Object> processModel(Map types) {
        Map<String, Object> objs = new HashMap<>();
        Set<String> modelKeys = types.keySet();
        for (String name : modelKeys) {
            Map<String, Object> map = (Map) types.get(name);
            RamlModel m = new RamlModel();

            if (Arrays.stream(properties.getProperty("reservedKeywords").split(",")).findAny().equals(name)) {
                m.name = escapeReservedWord(name);
            } else {
                m.name = name;
            }
            m.classname = name;
            if (map.get("type").equals("object")) {
                m.parent = null;
                m.parentSchema = null;
                m.parentModel = null;
                m.parentVars = null;
            } else if (properties.getProperty("datatypes").contains((String) map.get("type"))) {
                System.out.println("Check and Handle this");
            } else {
                m.parent = (String) map.get("type");
                m.parentSchema = null;
                m.parentModel = null;
                m.parentVars = null;
            }

            List<ModelProperty> modelProperties = populateProperties(name, types);
            Set<String> imports = populateImports(name, types);
            m.vars.addAll(modelProperties);
            //m.imports.addAll(imports);
            objs.put(name, m);
        }

        return objs;
    }

    private Set<String> populateImports(String name, Map types) {

        return null;
    }

    private List<ModelProperty> populateProperties(String modelName, Map<String, Object> actualMap) {
        Map<String, Object> map = (Map) actualMap.get(modelName);
        List<ModelProperty> vars = new ArrayList<>();
        //Check if Field is composite or predefined datatype.
        if (map.get("type").equals("object")) {
            Map<String, Object> modelProperties = (Map<String, Object>) map.get("properties");
            Set<String> modelPropertiesKeys = modelProperties.keySet();
            for (String name : modelPropertiesKeys) {
                ModelProperty property = new ModelProperty();
                Object value = (Object) modelProperties.get(name);
                if (value instanceof Map) {
                    Map valueMap = (Map) value;
                    property.datatype = camelize((String) valueMap.get("type"), false);
                    //Write code for remaining fields
                } else {
                    property.datatype = camelize((String) value, false);
                }
                property.datatypeWithEnum = property.datatype;
                property.name = toVarName(name);
                property.baseName = name;
                property.getter = toGetter(name);
                property.setter = toSetter(name);
                vars.add(property);
            }
        } else if (properties.getProperty("datatypes").contains((String) map.get("type"))) {
            System.out.println("Handle this");
        } else {
            Map<String, Object> modelProperties = (Map<String, Object>) map.get("properties");
            Set<String> modelPropertiesKeys = modelProperties.keySet();
            for (String name : modelPropertiesKeys) {
                ModelProperty property = new ModelProperty();
                Object value = (Object) modelProperties.get(name);

                property.name = toVarName(name);
                property.baseName = name;
                property.getter = toGetter(name);
                property.setter = toSetter(name);

                if (value instanceof Map) {
                    Map valueMap = (Map) value;
                    if (true) {
                        System.out.println("Fix for types like ENUM etc");
                    }
                    if (valueMap.get("type") != null) {
                        property.datatype = camelize((String) valueMap.get("type"), false);
                        property.datatypeWithEnum = property.datatype;
                    } else if (valueMap.get("enum") != null) {
                        property.datatype = "String";
                        property.isEnum = true;
                        Map<String, Object> allowableValues = new HashMap<String, Object>();
                        allowableValues.put("values", valueMap.get("enum"));
                        property.allowableValues = allowableValues;
                        property.datatypeWithEnum = toEnumName(property);
                        property.enumName = toEnumName(property);
                    }

                    //Write code for remaining fields
                } else {
                    property.datatype = (String) value;
                    property.datatypeWithEnum = property.datatype;
                }


                vars.add(property);
                for (ModelProperty var : vars) {
                    updateCodegenPropertyEnum(var);
                }
            }
        }

        return vars;
    }

    private void updateCodegenPropertyEnum(ModelProperty var) {
        Map<String, Object> allowableValues = var.allowableValues;

        // handle ArrayProperty
        if (var.items != null) {
            allowableValues = var.items.allowableValues;
        }

        if (allowableValues == null) {
            return;
        }

        List<Object> values = (List<Object>) allowableValues.get("values");
        if (values == null) {
            return;
        }

        // put "enumVars" map into `allowableValues", including `name` and `value`
        List<Map<String, String>> enumVars = new ArrayList<Map<String, String>>();
        String commonPrefix = findCommonPrefixOfVars(values);
        int truncateIdx = commonPrefix.length();
        Map<String, Integer> uniqueNames = new HashMap<>();
        for (Object value : values) {
            Map<String, String> enumVar = new HashMap<String, String>();
            String enumName;
            if (truncateIdx == 0) {
                enumName = value.toString();
            } else {
                enumName = value.toString().substring(truncateIdx);
                if ("".equals(enumName)) {
                    enumName = value.toString();
                }
            }
            String varName = toEnumVarName(enumName, var.datatype);
            enumVar.put("name", ensureUniqueName(uniqueNames, varName));
            enumVar.put("value", toEnumValue(value.toString(), var.datatype));
            enumVars.add(enumVar);
        }
        allowableValues.put("enumVars", enumVars);

        // handle default value for enum, e.g. available => StatusEnum.AVAILABLE
        if (var.defaultValue != null) {
            String enumName = null;
            for (Map<String, String> enumVar : enumVars) {
                if (toEnumValue(var.defaultValue, var.datatype).equals(enumVar.get("value"))) {
                    enumName = enumVar.get("name");
                    break;
                }
            }
            if (enumName != null) {
                var.defaultValue = toEnumDefaultValue(enumName, var.datatypeWithEnum);
            }
        }
    }

    public String findCommonPrefixOfVars(List<Object> vars) {
        if (vars.size() > 1) {
            try {
                String[] listStr = vars.toArray(new String[vars.size()]);
                String prefix = StringUtils.getCommonPrefix(listStr);
                // exclude trailing characters that should be part of a valid variable
                // e.g. ["status-on", "status-off"] => "status-" (not "status-o")
                return prefix.replaceAll("[a-zA-Z0-9]+\\z", "");
            } catch (ArrayStoreException e) {
                // do nothing, just return default value
            }
        }
        return "";
    }

    public String toEnumDefaultValue(String value, String datatype) {
        return datatype + "." + value;
    }

    public boolean isPrimivite(String datatype) {
        return "number".equalsIgnoreCase(datatype)
                || "integer".equalsIgnoreCase(datatype)
                || "boolean".equalsIgnoreCase(datatype);
    }

    public String toEnumValue(String value, String datatype) {
        if (isPrimivite(datatype)) {
            return value;
        } else {
            return "\"" + escapeText(value) + "\"";
        }
    }

    public String escapeText(String input) {
        if (input == null) {
            return input;
        }

        // remove \t, \n, \r
        // replace \ with \\
        // replace " with \"
        // outer unescape to retain the original multi-byte characters
        // finally escalate characters avoiding code injection
        return escapeUnsafeCharacters(
                StringEscapeUtils.unescapeJava(
                        StringEscapeUtils.escapeJava(input)
                                .replace("\\/", "/"))
                        .replaceAll("[\\t\\n\\r]", " ")
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\""));
    }

    public String escapeUnsafeCharacters(String input) {
        // doing nothing by default and code generator should implement
        // the logic to prevent code injection
        // later we'll make this method abstract to make sure
        // code generator implements this method
        return input;
    }

    protected String ensureUniqueName(Map<String, Integer> uniqueNames, String name) {
        int count = uniqueNames.containsKey(name) ? uniqueNames.get(name) + 1 : 1;
        if (uniqueNames.put(name, count) != null)
            name = name + '_' + count;
        return name;
    }

    public String toEnumVarName(String value, String datatype) {
        if (value.length() == 0) {
            return "EMPTY";
        }

        String var = value.replaceAll("\\W+", "_").toUpperCase();
        if (var.matches("\\d.*")) {
            return "_" + var;
        } else {
            return var;
        }
    }

    @SuppressWarnings("static-method")
    public String toEnumName(ModelProperty property) {
        return StringUtils.capitalize(property.name) + "Enum";
    }

    public String toSetter(String name) {
        return "set" + getterAndSetterCapitalize(name);
    }

    public String toGetter(String name) {
        return "get" + getterAndSetterCapitalize(name);
    }

    public String getterAndSetterCapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return camelize(toVarName(name), false);
    }

    public String toVarName(String name) {
        if (Arrays.stream(properties.getProperty("reservedKeywords").split(",")).findAny().equals(name)) {
            return escapeReservedWord(name);
        } else {
            return name.replaceAll("[-+.^:,?]","");
        }
    }

    public String escapeReservedWord(String name) {
        throw new RuntimeException("reserved word " + name + " not allowed");
    }

    public static String camelize(String word, boolean lowercaseFirstLetter) {
        // Replace all slashes with dots (package separator)
        Pattern p = Pattern.compile("\\/(.?)");
        Matcher m = p.matcher(word);
        while (m.find()) {
            word = m.replaceFirst("." + m.group(1)/*.toUpperCase()*/); // FIXME: a parameter should not be assigned. Also declare the methods parameters as 'final'.
            m = p.matcher(word);
        }

        // case out dots
        String[] parts = word.split("\\.");
        StringBuilder f = new StringBuilder();
        for (String z : parts) {
            if (z.length() > 0) {
                f.append(Character.toUpperCase(z.charAt(0))).append(z.substring(1));
            }
        }
        word = f.toString();

        m = p.matcher(word);
        while (m.find()) {
            word = m.replaceFirst("" + Character.toUpperCase(m.group(1).charAt(0)) + m.group(1).substring(1)/*.toUpperCase()*/);
            m = p.matcher(word);
        }

        // Uppercase the class name.
        p = Pattern.compile("(\\.?)(\\w)([^\\.]*)$");
        m = p.matcher(word);
        if (m.find()) {
            String rep = m.group(1) + m.group(2).toUpperCase() + m.group(3);
            rep = rep.replaceAll("\\$", "\\\\\\$");
            word = m.replaceAll(rep);
        }

        // Remove all underscores (underscore_case to camelCase)
        p = Pattern.compile("(_)(.)");
        m = p.matcher(word);
        while (m.find()) {
            String original = m.group(2);
            String upperCase = original.toUpperCase();
            if (original.equals(upperCase)) {
                word = word.replaceFirst("_", "");
            } else {
                word = m.replaceFirst(upperCase);
            }
            m = p.matcher(word);
        }

        // Remove all hyphens (hyphen-case to camelCase)
        p = Pattern.compile("(-)(.)");
        m = p.matcher(word);
        while (m.find()) {
            word = m.replaceFirst(m.group(2).toUpperCase());
            m = p.matcher(word);
        }

        if (lowercaseFirstLetter && word.length() > 0) {
            word = word.substring(0, 1).toLowerCase() + word.substring(1);
        }

        return word;
    }

    public Map<String, Object> generateControllerService(List<File> files) throws IOException {
        Set<String> imports = new HashSet<>();
        List<RamlOperation> ramlOperations = prepareRamlOperations(ramlMap, imports);

        Map<String, Object> operations = new HashMap<>();
        operations.put("operations", ramlOperations);
        operations.put("package", commonAttributes.get("controllerPackage"));

        importMap.put("Controller", properties.getProperty("controller").concat(".").concat("Controller"));

        operations.put("imports", processControllerImports(imports));
        String filename = javaClassFilename("APIController", "controllerPackage");

        File written = processTemplateToFile(operations, "controller.mustache", filename);
        files.add(written);

        operations.put("package", commonAttributes.get("servicePackage"));
        operations.putAll(getClientMapInfo());
        written = processTemplateToFile(operations, "service.mustache",
                basePath
                        + commonAttributes.get("mvnFoldeStruc")
                        + "/" + commonAttributes.get("servicePackage").replaceAll("[.]", "/")
                        + "/APIService.java");
        files.add(written);
        return operations;
    }

    private List<Map<String, String>> processControllerImports(Set<String> imports) {
        List<Map<String, String>> importList = new ArrayList<>();
        for (String importStr : imports) {
            Map<String, String> map = new HashMap<>();
            if (this.importMap.get(importStr) != null) {
                map.put("import", this.importMap.get(importStr));
                importList.add(map);
            }
        }

        return importList;
    }

    private List<RamlOperation> prepareRamlOperations(Map ramlMap, Set<String> imports) {
        List<RamlOperation> ramlOperations = new ArrayList<>();
        Set<String> mapKey = ramlMap.keySet();
        for (String key : mapKey) {
            if (key.startsWith("/")) {
                    Set<String> ops = ((Map)ramlMap.get(key)).keySet();
                    for (String op : ops) {
                        ramlOperations.add(processOperation(key, op, (Map) ((Map)ramlMap.get(key)).get(op), imports));
                    }
            }
        }

        return ramlOperations;
    }

    private RamlOperation processOperation(String path, String op, Map map, Set<String> imports) {
        RamlOperation ramlOperation = new RamlOperation();
        ramlOperation.path = path;
        ramlOperation.httpMethod = (String) properties.get(op.toUpperCase());
        ramlOperation.methodType = op.toUpperCase();
        ramlOperation.operationId =  camelize((String)map.get("displayName"), true);

        if ("PostMapping".equals(ramlOperation.httpMethod) &&
                ((Map)map.get("body")).size() != 0){
            checkRequestBodyForMethod(ramlOperation, (Map) map.get("body"), imports);
        }
        checkPathParams(ramlOperation, path);
        checkQueryParams(ramlOperation, map);
        updateHasTrue(ramlOperation);
        checkReturnTypes(ramlOperation, (Map) map.get("responses"), imports);

        //System.out.println(map);
        return ramlOperation;
    }

    private void updateHasTrue(RamlOperation ramlOperation) {
        Iterator itr = ramlOperation.allParams.iterator();
        RamlParam param = new RamlParam();
        while (itr.hasNext()) {
            param = (RamlParam) itr.next();
            if (itr.hasNext()) {
                param.hasMore = true;
            }
        }
    }

    private void checkRequestBodyForMethod(RamlOperation ramlOperation, Map body, Set<String> imports) {
        List<RamlParam> ramlParams = new ArrayList<>();
        RamlParam ramlParam = new RamlParam();
        ramlParam.isBodyParam = true;
        ramlParam.dataType = (String) ((Map)body.get("application/json")).get("type");
        imports.add(ramlParam.dataType);
        ramlParam.paramName = camelize(ramlParam.dataType, true);
        ramlParam.baseName = camelize(ramlParam.paramName, false);
        ramlParams.add(ramlParam);
        ramlOperation.allParams.addAll(ramlParams);
        ramlOperation.bodyParam = ramlParam;
    }

    private void checkReturnTypes(RamlOperation ramlOperation, Map map, Set<String> imports) {
        if (map != null && map.get("200") != null) {
            ramlOperation.returnType = camelize(getReturnType(map), false);
        } else if (map != null && map.get("201") != null) {
            ramlOperation.returnType = (String)((Map)((Map)((Map)map.get("201")).get("body")).get("application/json")).get("type");
        }
        if (ramlOperation.returnType != null) {
            imports.add(ramlOperation.returnType.replace("[]",""));
        }
        ramlOperation.returnType = checkArray(ramlOperation.returnType);
    }

    private String getReturnType(Map map) {
        Map bodyMap = (Map) ((Map)map.get("200")).get("body");
        if (bodyMap.get("default") != null) {
            return (String) bodyMap.get("default");
        } else  if (bodyMap.get("application/json") != null) {
            return (String)((Map)((Map)((Map)map.get("200")).get("body")).get("application/json")).get("type");
        } else {
            return null;
        }
    }

    private String checkArray(String returnType) {
        if (StringUtils.isEmpty(returnType)) {
            return null;
        }
        return returnType.contains("[]") ? "List<".concat(returnType).concat(">").replace("[]", "") : returnType;
    }

    private void checkQueryParams(RamlOperation ramlOperation, Map queryParamMap) {
        if (queryParamMap.get("queryParameters") == null) {
            return;
        }
        ramlOperation.queryParams = prepareQueryParam((Map) queryParamMap.get("queryParameters"));
        ramlOperation.allParams.addAll(ramlOperation.queryParams);
    }

    private List<RamlParam> prepareQueryParam(Map queryParamMap) {
        List<RamlParam> params = new ArrayList<>();

        Set<String> paramKeys = queryParamMap.keySet();
        for (String param : paramKeys) {
            RamlParam ramlParam = new RamlParam();
            Map<String, Object> paramAttributes = (Map<String, Object>) queryParamMap.get(param);
            ramlParam.isQueryParam = true;
            ramlParam.paramName = param;
            ramlParam.baseName = camelize(param, false);
            ramlParam.description = (String) paramAttributes.get("description");
            ramlParam.dataType = camelize((String) paramAttributes.get("type"), false);
            ramlParam.required = paramAttributes.get("required") != null ? (boolean) paramAttributes.get("required") : false;
            ramlParam.minimum = String.valueOf(paramAttributes.get("minimum"));
            ramlParam.maximum = String.valueOf(paramAttributes.get("maximum"));
            params.add(ramlParam);
        }

        return params;
    }

    private void checkPathParams(RamlOperation ramlOperation, String path) {
        ramlOperation.pathParams = fetchStringBetweenCurlyBraces(path);
        ramlOperation.allParams.addAll(ramlOperation.pathParams);
    }

    private List<RamlParam> fetchStringBetweenCurlyBraces(String path) {
        Pattern p = Pattern.compile("\\{(.*?)\\}");
        Matcher m = p.matcher(path);
        List<RamlParam> params = new ArrayList<>();
        while (m.find()) {
            RamlParam ramlParam = new RamlParam();
            ramlParam.paramName = m.group(1);
            ramlParam.baseName = camelize(ramlParam.paramName, false);
            ramlParam.isPathParam = true;
            ramlParam.dataType = "String";
            params.add(ramlParam);
        }

        return params;
    }

    public void generateService(List<File> files) {

    }

    public void generateSupportingFiles(List<File> files, Map<String, Object> operations, String repoClonePath) throws IOException {
        String filename = null;
        String templateName = null;
        Map<String, Object> muleXMLMap = new MuleXMLParser().parseMuleXML(commonAttributes, repoClonePath);
        ConvertorFactory factory = new ConvertorFactory();
        String convertor = null;

        if (Boolean.parseBoolean(commonAttributes.get("restClient"))){
            operations.put("package", commonAttributes.get("clientPackage"));
            templateName = "client.mustache";
            filename = javaClassFilename("ApiClient", "clientPackage");
            convertor = "REST";
        } else if (Boolean.parseBoolean(commonAttributes.get("my-sql-database-call"))){
            templateName = "db-client.mustache";
            operations.put("package", commonAttributes.get("dbClientPackage"));
            filename = javaClassFilename("DataBaseClient", "dbClientPackage");
            convertor = "DATABASE";
        } else if (Boolean.parseBoolean(commonAttributes.get("mq-client"))){
            templateName = "mq-client.mustache";
            operations.put("package", commonAttributes.get("mqClientPackage"));
            filename = javaClassFilename("MQClient", "mqClientPackage");
            convertor = "MQCLIENT";
        }

        SpringBootConvertor obj = factory.getConvertor(convertor);
        Map<String, String> parsedMap = obj.convertIntoMap(muleXMLMap);

        operations.putAll(parsedMap);
        operations.putAll(getClientMapInfo());

        File written = processTemplateToFile(operations, templateName, filename);
        files.add(written);

        //Populate application.properties
        createApplicationConfigFile(files, operations);
    }

    private void createApplicationConfigFile(List<File> files, Map<String, Object> operations) throws IOException {
        String filename = appPropFilename("application");
        File written = processTemplateToFile(operations, "application-properties.mustache", filename);
        files.add(written);
    }

    private Map<String, Object> getClientMapInfo(){
        Map<String, Object> clientMap = new HashMap();
        clientMap.put("restClient", Boolean.parseBoolean(commonAttributes.get("restClient")));
        clientMap.put("dbClient", Boolean.parseBoolean(commonAttributes.get("my-sql-database-call")));
        clientMap.put("mqClient", Boolean.parseBoolean(commonAttributes.get("mq-client")));

        return clientMap;
    }
}