package com.java.yaml.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.yaml.model.*;
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

    public CodeGenerator(RAML raml, Properties properties, Map ramlMap) {
        this.raml = raml;
        this.properties = properties;
        this.ramlMap = ramlMap;
    }

    public void generateModels(List<File> files) throws IOException {
        Map<String, Map> types = raml.getTypes();
        if (types.size() == 0) {
            return;
        }
        Map<String, Object> modelMap = processModel(types);

        for (String modelName : modelMap.keySet()) {
            //Map<String, Object> models = (Map<String, Object>) modelMap.get(modelName);
            RamlModel model = (RamlModel) modelMap.get(modelName);
            ObjectMapper m = new ObjectMapper();
            Map<String, Object> models = m.convertValue(model, Map.class);
            models.put("package", properties.getProperty("model"));
            //models.put("modelPackage", properties.getProperty("model"));
            String filename = modelFilename("model.mustache", modelName);
            importMap.put(modelName, properties.getProperty("model").concat(".").concat(modelName));
            File written = processTemplateToFile(models, "model.mustache", filename);
            if (written != null) {
                files.add(written);
            }
        }
    }

    private File processTemplateToFile(Map<String, Object> models, String templateName, String outputFilename) throws IOException {
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

        writeToFile(adjustedOutputFilename, tmpl.execute(models));
        return new File(adjustedOutputFilename);
    }

    public File writeToFile(String filename, String contents) throws IOException {
        File output = new File(filename);

        if (output.getParent() != null && !new File(output.getParent()).exists()) {
            File parent = new File(output.getParent());
            parent.mkdirs();
        }
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(output), "UTF-8"));

        out.write(contents);
        out.close();
        return output;
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
        } catch (Exception e) {
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
        } catch (Exception e) {
        }
        throw new RuntimeException("can't load template " + name);
    }

    public String getCPResourcePath(String name) {
        if (!"/".equals(File.separator)) {
            return name.replaceAll(Pattern.quote(File.separator), "/");
        }
        return name;
    }

    public String modelFilename(String templateName, String modelName) {
        String suffix = modelTemplateFiles("mustacheModel").get(templateName);
        return modelFileFolder("model") + File.separator + toModelFilename(modelName) + suffix;
    }

    public String controllerFilename(String templateName, String modelName) {
        String suffix = modelTemplateFiles("mustacheController").get(templateName);
        return controllerFileFolder("controller") + File.separator + toModelFilename(modelName) + suffix;
    }

    public Map<String, String> modelTemplateFiles(String type) {
        return Collections.singletonMap(properties.getProperty(type), ".java");
    }

    public String modelFileFolder(String type) {
        return "/Users/ja20105259/projects/java-mvn-prog/target" + "/" + properties.getProperty(type).replace('.', '/');
    }

    public String controllerFileFolder(String type) {
        return "/Users/ja20105259/projects/java-mvn-prog/target" + "/" + properties.getProperty(type).replace('.', '/');
    }

    public Map<String, String> controllerTemplateFiles(String type) {
        return Collections.singletonMap(properties.getProperty(type), ".java");
    }

    public String toModelFilename(String name) {
        return initialCaps(name);
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
        // outter unescape to retain the original multi-byte characters
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
            return name;
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

    public void generateController(List<File> files) throws IOException {
        Set<String> imports = new HashSet<>();
        Map<String, RamlOperation> paths = processPaths(ramlMap, imports);

        Map<String, Object> operations = new HashMap<>();
        List<RamlOperation> objs = new ArrayList();
        for (String path : paths.keySet()) {
            //Map<String, Object> models = (Map<String, Object>) modelMap.get(modelName);
            //RamlOperation operation = (RamlOperation) paths.get(path);
            ObjectMapper m = new ObjectMapper();
            objs.add(paths.get(path));
        }
        operations.put("operations", objs);
        operations.put("package", properties.getProperty("controller"));
        importMap.put("Controller", properties.getProperty("controller").concat(".").concat("Controller"));

        operations.put("imports", processControllerImports(imports));
        //operation.put("importPath", config.toApiImport(tag));
        //models.put("modelPackage", properties.getProperty("model"));
        String filename = controllerFilename("controller.mustache", "Controller");

        File written = processTemplateToFile(operations, "controller.mustache", filename);
        files.add(written);
    }

    private List<Map<String, String>> processControllerImports(Set<String> imports) {
        List<Map<String, String>> importList = new ArrayList<>();
        for (String importStr : imports) {
            Map<String, String> map = new HashMap<>();
            map.put("import", this.importMap.get(importStr));
            importList.add(map);
        }

        return importList;
    }

    private Map<String, RamlOperation> processPaths(Map ramlMap, Set<String> imports) {
        Map<String, RamlOperation> paths = new HashMap<>();
        Set<String> mapKey = ramlMap.keySet();
        for (String key : mapKey) {
            if (key.startsWith("/")) {
                //if(ramlMap.get(key) instanceof Map){
                    Set<String> ops = ((Map)ramlMap.get(key)).keySet();
                    for (String op : ops) {
                        paths.put(key+op, processOperation(key, op, (Map) ((Map)ramlMap.get(key)).get(op), imports));
                    }
                //}
                //paths.put(key, processOperation(key, (Map) ramlMap.get(key), imports));
            }
        }

 /*       mapKey = paths.keySet();
        for (String key : mapKey) {

        }*/

        return paths;
    }

    private RamlOperation processOperation(String path, String op, Map map, Set<String> imports) {
        RamlOperation ramlOperation = new RamlOperation();
        ramlOperation.path = path;
        ramlOperation.httpMethod = (String) properties.get(op.toUpperCase());

       /* if (map.get("get") != null) {
            ramlOperation.httpMethod = (String) properties.get("GET");
            methodType = "op";
        } else if (map.get("post") != null) {
            ramlOperation.httpMethod = (String) properties.get("POST");
            methodType = "post";
        } else if (map.get("put") != null) {
            ramlOperation.httpMethod = (String) properties.get("PUT");
            methodType = "put";
        } else if (map.get("delete") != null) {
            ramlOperation.httpMethod = (String) properties.get("DELETE");
            methodType = "delete";
        }*/

        ramlOperation.operationId =  camelize((String)map.get("displayName"), true);

        checkPathParams(ramlOperation, path);
        checkQueryParams(ramlOperation, map);
        updateReturnTypes(ramlOperation, (Map) map.get("responses"), imports);

        //System.out.println(map);
        return ramlOperation;
    }

    private void updateReturnTypes(RamlOperation ramlOperation, Map map, Set<String> imports) {
        if (map != null && map.get("200") != null) {
            ramlOperation.returnType = (String)((Map)((Map)((Map)map.get("200")).get("body")).get("application/json")).get("type");
        } else if (map != null && map.get("201") != null) {
            ramlOperation.returnType = (String)((Map)((Map)((Map)map.get("201")).get("body")).get("application/json")).get("type");
        } else {
            //ramlOperation.returnType = "Void";
        }

        imports.add(ramlOperation.returnType);
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
            ramlParam.description = (String) paramAttributes.get("description");
            ramlParam.dataType = "@RequestParam " + camelize((String) paramAttributes.get("type"), false);
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

    private List<RamlParam>  fetchStringBetweenCurlyBraces(String path) {
        Pattern p = Pattern.compile("\\{(.*?)\\}");
        Matcher m = p.matcher(path);
        List<RamlParam> params = new ArrayList<>();
        while (m.find()) {
            RamlParam ramlParam = new RamlParam();
            ramlParam.paramName = m.group(1);
            ramlParam.isPathParam = true;
            ramlParam.dataType = "@PathVariable String";
            params.add(ramlParam);
        }

        return params;
    }
}