package com.treelogic.proteus.kafka.producer;

import com.arturmkrtchyan.sizeof4j.SizeOf;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple3;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by pablo.mesa on 17/03/17.
 */
public class CoilOffsetsLenghtsFile {

    // Atributos

    protected FileSystem fs;
    protected String PROTEUS_TABLE;
    protected String HDFS;
    protected Integer coilidentifier = Integer.MIN_VALUE;
    protected ArrayList<Tuple3<Integer, Integer, Integer>> coilsbufferfile = new ArrayList<>();
    protected Integer coilscounter = 0;
    protected Integer coilsize = 0;
    protected Integer linesize = 0;
    protected String timeStampInicio;
    protected String timeStampFinal;


    // Logger

    private static final Logger logger = LoggerFactory.getLogger(CoilOffsetsLenghtsFile.class);

    // Constructor

    CoilOffsetsLenghtsFile(FileSystem fs, String HDFS, String PROTEUS_TABLE){
        this.fs = fs;
        this.HDFS = HDFS;
        this.PROTEUS_TABLE = PROTEUS_TABLE;
        this.timeStampInicio = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
    }

    // Funciones

    public void createOffsetsLenghtsFile() throws IOException {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(fs.open(new Path(HDFS + PROTEUS_TABLE))));
        try {
            // La primera línea del CSV es una cabecera
            String line = br.readLine();
            // Primera linea a procesar
            line = br.readLine();
            while (line != null) {
                String [] mensaje = line.split(",");
                Coil coil = new Coil().generateCoilObject(mensaje);
                createCoilBuffer(coil, SizeOf.shallowSize(line));
                line = br.readLine();
            }
        } catch (Exception e) {
            logger.error("Error in the Proteus Kafka producer", e);
        }
        dumpToFile();
    }

    public void createCoilBuffer(Coil coil, int size){

        if ( coilidentifier.equals(coil.getID())){
            generarBufferBobinas(coil, size);
        } else {
            if ( coilsize != 0 && linesize != 0) {
                dumpCoilInfotoArray();
                coilscounter++;
            }
            coilidentifier = coil.getID();
        }
    }

    public void generarBufferBobinas(Coil coil, int size){
        coilsize = coilsize + SizeOf.shallowSize(coil);
        linesize = linesize + size;
    }

    public void dumpCoilInfotoArray(){
        System.out.println(" Tamaño bobina[" + coilidentifier + "]: " + coilsize);
        coilsbufferfile.add(new Tuple3<>(coilidentifier, coilsize, linesize));
        coilsize = 0;
        linesize = 0;
    }

    public void dumpToFile() throws IOException {
        FileWriter writer = new FileWriter("/home/pablo.mesa/Escritorio/offsets.csv");
        writer.write(timeStampInicio + System.getProperty( "line.separator" ));
        for(Tuple3<Integer, Integer, Integer> str: coilsbufferfile) {
            writer.write(str.toString() + System.getProperty( "line.separator" ));
        }
        writer.write(timeStampFinal = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()) + System.getProperty( "line.separator" ));
        writer.close();
    }
}
