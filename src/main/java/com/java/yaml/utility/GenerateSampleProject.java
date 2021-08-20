package com.java.yaml.utility;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.*;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GenerateSampleProject {
    String downloadPath = "/Users/ja20105259/projects/mule-to-spring-boot/";
    String extractDirectory = "/Users/ja20105259/projects/mule-to-spring-boot";

    String downloadnextractDirectory = "/Users/ja20105259/projects/mule-to-spring-boot/";

    private static final int BUFFER_SIZE = 4096;

    public String createSpringBootSampleApp(Map<String, String> attributes) throws IOException {
        callStartSpringIO(attributes);
        unzipTheFolder(attributes);

        return downloadnextractDirectory.concat(attributes.get("projectName"));
    }

    private void unzipTheFolder(Map<String, String> attributes) throws IOException {
        File destDir = new File(downloadnextractDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(downloadnextractDirectory.concat(attributes.get("fileName"))));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = downloadnextractDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdirs();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }


    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    private void callStartSpringIO(Map<String, String> attributes) throws IOException {
        String url = "https://start.spring.io/starter.zip?type=maven-project&language=$language&bootVersion=$bootVersion&baseDir=$baseDir&groupId=$groupId&artifactId=$artifactId&name=$name&description=$description&packageName=$packageName&packaging=$packaging&javaVersion=$javaVersion&dependencies=$dependencies";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(replacePlaceHolders(url, attributes))
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        FileOutputStream fos = new FileOutputStream(downloadnextractDirectory.concat(attributes.get("fileName")));
        fos.write(response.body().bytes());
        fos.close();
    }

    private String replacePlaceHolders(String url, Map<String, String> attributes) {
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            url = url.replace(entry.getKey(), entry.getValue());
        }

        return url;
    }
}
