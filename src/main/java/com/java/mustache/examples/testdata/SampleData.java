package com.java.mustache.examples.testdata;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SampleData {

    //public CompoundClass compound;
    public String foo = "foo";
    public String getBar() {
        return "bar";
    }

    public String[] strings = {"S1", "S2", "S3"};

    public List list = Arrays.asList("L1", "L2", "L3");

    public Map<String, String> map =
            new HashMap<String, String>();
    {
        map.put("key1","value1");
        map.put("key2","value2");
        map.put("key3","value3");
    }
}