package com.java.mustache.examples;

import com.samskivert.mustache.Mustache;

public class CompoundCase {
    public static void main(String[] args) {
        new CompoundCase().invoke();
    }

    private void invoke() {
        String result = Mustache.compiler().compile("Hello {{field.who}}!").execute(new Object() {
            public Object field = new Object() {
                public String who () { return "world"; }
            };
        });

        System.out.println(result);
    }
}
