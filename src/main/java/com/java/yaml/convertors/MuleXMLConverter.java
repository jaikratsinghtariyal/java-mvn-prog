package com.java.yaml.convertors;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MuleXMLConverter implements Converter {

    public boolean canConvert(Class clazz) {
        return AbstractMap.class.isAssignableFrom(clazz);
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        //Not in use
    }

    public Map<String, Object> unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Map<String, Object> map = new HashMap<>();

        while (reader.hasMoreChildren()) {
            reader.moveDown();
            Iterator<String> itr = reader.getAttributeNames();
            Map<String, String> innerMap = new HashMap<>();
            while (itr.hasNext()) {
                String value = (String) itr.next();
                innerMap.put(value, reader.getAttribute(value));
            }
            map.put(reader.getNodeName(), innerMap);
            if (reader.hasMoreChildren()) {
                Map<String, String> innerMapNew = (Map) unmarshal(reader, context);
                Map<String, String> valueMap = (Map) map.get(reader.getNodeName());
                valueMap.putAll(innerMapNew);
            }
            //Special Handling for few fields like db:sql
            if ("db:sql".equalsIgnoreCase(reader.getNodeName())) {
                map.put(reader.getNodeName(), reader.getValue());
            }

            reader.moveUp();
        }
        return map;
    }
}
