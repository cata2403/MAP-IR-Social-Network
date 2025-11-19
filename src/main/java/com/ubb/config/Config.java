package com.ubb.config;

import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static String FILE = "config.properties";
    private static Properties PROPERTIES = new Properties();

    public static Properties getProperties() {
        return PROPERTIES;
    }

    public static void initProperties() {

        Properties prop = new Properties();
        try( InputStream in = Config.class.getClassLoader().getResourceAsStream(FILE) ) {

            prop.load(in);
            PROPERTIES = prop;

        }
        catch(Exception error) {
            System.out.println("<<Cannot load properties file: " + FILE +">>");
        }
    }

}
