package eu.proteus.producer.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;

/** @author Treelogic */

public final class ProteusData {

    /** Constructor. */
    private ProteusData() {
    }

    /** Time between coils. Obtained from the PROTEUS configuration file. */
    private static int timeBetweenCoils;

    /** Coil time. Obtained from the PROTEUS configuration file */
    private static int coilTime;

    /** Mapping between COIL_ID and its x maximum value. Obtained from the
     * PROTEUS-maxX.json file */
    private static Map<?, ?> coilxMax = new HashMap<String, String>();

    /** Pointer to the PROTEUS properties object. */
    private static Properties properties;

    /** List of flatness variable names. */
    private static List<Integer> flatnessVarNames = Arrays.asList(42, 28, 11);

    /** Getters and Setters */

    /** Method: setTimeBetweenCoils().
     *
     * @param time */
    public static void setTimeBetweenCoils(final int time) {
        timeBetweenCoils = time;
    }

    /** Method: getTimeBetweenCoils().
     *
     * @return */
    public static int getTimeBetweenCoils() {
        return timeBetweenCoils;
    }

    /** Method: setCoilTime().
     *
     * @param time */
    public static void setCoilTime(final int time) {
        coilTime = time;
    }

    /** Method: getCoilTime().
     *
     * @return */
    public static int getCoilTime() {
        return coilTime;
    }

    /** Method: setFlatnessVarName().
     *
     * @param list */
    public static void setFlatnessVarName(final List<Integer> list) {
        flatnessVarNames = list;
    }

    /** Method. getFlatnessVarName().
     *
     * @return */
    public static List<Integer> getFlatnessVarName() {
        return flatnessVarNames;
    }

    static {
        properties = new Properties();
        try {
            properties.load(ProteusData.class.getClassLoader()
                    .getResource("config.properties").openStream());
            setTimeBetweenCoils(Integer.parseInt(
                    (String) ProteusData.get("model.timeBetweenCoils")));
            setCoilTime(Integer
                    .parseInt((String) ProteusData.get("model.coilTime")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Method get().
     *
     * @param property
     * @return */
    public static Object get(final String property) {
        return properties.get("com.treelogic.proteus." + property);
    }

    /** Method getXmax().
     *
     * @param coil
     *            Coil identifier.
     * @return */
    public static Double getXmax(final int coil) {
        String coilString = String.valueOf(coil);
        Double maxX = Double
                .parseDouble(String.valueOf(coilxMax.get(coilString)));
        return maxX;
    }

    /** Method loadData(). */
    public static void loadData() {
        ClassLoader classLoader = ProteusData.class.getClassLoader();
        String json = null;
        try {
            json = new String(Files.readAllBytes(Paths.get(
                    classLoader.getResource("PROTEUS-maxX.json").toURI())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            coilxMax = new ObjectMapper().readValue(json, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
