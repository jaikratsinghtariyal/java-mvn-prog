package com.java.mustache.examples;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstProgram {

    public static void main(String[] args) {
        String text = "One, two, {{three}}. Three sir!";
        Template tmpl = Mustache.compiler().compile(text);
        Map<String, String> data = new HashMap<>();
        data.put("three", "five");
        System.out.println(tmpl.execute(data));

        //String tmpl = "{{#things}}{{-last}}, {{-last}}{{this}}{{/things}}";
        /*String tmpl = "{{#things}}{{^-first}}, {{/-first}}{{this}}{{/things}}";
        String result = Mustache.compiler().compile(tmpl).execute(new Object() {
            List<String> things = Arrays.asList("one", "two", "three");
        });

        System.out.println(result);*/

        //String str = "{{^supportJava6}}import java.util.Objects;\nimport java.util.Arrays;\n{{{supportJava6}}}{{/supportJava6}}";
        /*String str = "{{^supportJava6}}import java.util.Objects;\nimport java.util.Arrays;\n{{/supportJava6}}";
        Template tmpl = Mustache.compiler().compile(str);
        Map<String, Boolean> data = new HashMap<String, Boolean>();
        //data.put("supportJava6", false);
        System.out.println(tmpl.execute(data));*/
    }
}
//First
//true, trueonefalse, falsetwofalse, falsethree
//one, two, three

//Last
//with -->      , one, twothree
//Without -->   false, falseonefalse, falsetwotrue, truethree

