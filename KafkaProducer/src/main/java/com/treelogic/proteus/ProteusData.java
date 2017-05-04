package com.treelogic.proteus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ignacio.g.fernandez on 3/05/17.
 */
public class ProteusData {

    public static final int TIME_BETWEEN_COILS = 3000;

    public static final int COIL_TIME = 120000;

    private static Map<String, String> coil_xMax = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(ProteusData.class);


    public static Double getXmax(int coil){
        String coilString = String.valueOf(coil);
        Double maxX = Double.parseDouble(coil_xMax.get(coilString));
        logger.info("Trying to obtain maxX for coil : " + coilString + " = " + maxX);
        return maxX;
    }

    public static void loadData() {
        ClassLoader classLoader = ProteusData.class.getClassLoader();
        String json = null;
        try {
            json = new String(Files.readAllBytes(Paths.get(classLoader.getResource("PROTEUS-maxX.json").toURI())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            coil_xMax = new ObjectMapper().readValue(json, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
