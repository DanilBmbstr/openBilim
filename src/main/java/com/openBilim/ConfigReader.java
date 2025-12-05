package com.openBilim;

import java.io.BufferedReader;
import java.io.FileReader;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigReader {
    public static Configuration readConfig(){
        try(BufferedReader br = new BufferedReader(new FileReader("config.json"))) {
    StringBuilder sb = new StringBuilder();
    String line = br.readLine();

    while (line != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = br.readLine();
    }
    String everything = sb.toString();

    ObjectMapper mapper = new ObjectMapper();
    Configuration conf = new Configuration();
    conf = mapper.readValue(everything, Configuration.class);
    return conf;
}
catch(Exception e){e.printStackTrace(); return null;}
    }
}
