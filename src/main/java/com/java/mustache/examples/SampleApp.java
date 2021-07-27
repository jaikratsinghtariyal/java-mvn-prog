package com.java.mustache.examples;

import com.java.mustache.examples.testdata.SampleData;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class SampleApp {
    public static void main(String[] args) throws IOException {
        new SampleApp().invoke();
    }

    public void invoke() throws IOException {

        //Load Mustache template file
        String templateText = getResourceText("src/main/resources/my_template.html");
        Template template = Mustache.compiler().compile(templateText);

        //Pour objects into Mustache templates
        SampleData data = new SampleData();
        String out = template.execute(data);

        //Output the result
        System.out.println(out);
    }

    //Read a file from the resource directory
    public String getResourceText(String path) throws IOException {
        String content = null;
        try {
            content = Files.lines(Paths.get(path))
                    .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }
}
