package com.treelogic.proteus.kafka.offsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treelogic.proteus.kafka.model.Coil;
import com.treelogic.proteus.kafka.producer.ProducerLogic;
import com.treelogic.proteus.kafka.producer.Proteus;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by pablo.mesa on 16/03/17.
 */
public class KafkaProducerThread implements Runnable {

    protected FileSystem fs;
    protected String HDFS;
    protected String TABLE;
    protected Configuration conf;
    protected String Threadname;
    protected Thread t;
    protected int thread_num;
    protected ArrayList<Integer> coilsId;
    protected int chunk;
    protected double COIL_SPEED;
    private static Producer<String, String> producer;

    private static final Logger logger = LoggerFactory.getLogger(Proteus.class);

    KafkaProducerThread(String Threadname, int thread_num, FileSystem fs, String HDFS_URI, String TABLE, Configuration conf, ArrayList<Integer> ids, int chunk, double COIL_SPEED) {
        this.Threadname = Threadname;
        this.fs = fs;
        this.HDFS = HDFS_URI;
        this.TABLE = TABLE;
        this.conf = conf;
        this.thread_num = thread_num;
        this.coilsId = ids;
        this.chunk = chunk;
        this.COIL_SPEED = COIL_SPEED;

    }

    public void run(){
        System.out.println("My thread is in running state.");
        try {

            String timeStampInicio;
            String timeStampFinal;

            logger.info("Starting Proteus Kafka producer...");

            int loopIteration = 1;
            Properties properties = new Properties();
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    "192.168.4.246:6667,192.168.4.247:6667,192.168.4.248:6667");
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringSerializer");
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringSerializer");
            properties.put(ProducerConfig.ACKS_CONFIG, "all");
            properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 100);

            producer = new KafkaProducer<>(properties);
            KafkaAdmin kafkaAdmin = new KafkaAdmin();

            ObjectMapper mapper = new ObjectMapper();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z");
            mapper.setDateFormat(df);

            ProducerLogic logic = new ProducerLogic();

            // Read line by line HDFS

            timeStampInicio = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

            List<String> coilList = new ArrayList<String>();

            while (true) {
                logger.info("Starting a new kafka iteration over the HDFS: ", (loopIteration++));
                for ( int i = chunk*thread_num; i < (chunk*thread_num)+chunk; i++){

                    String idCoil = Integer.toString(i);
                    String topic = "COIL-" + coilsId.get(i);

                    if (!coilList.contains(idCoil)) {
                        kafkaAdmin.createTopic(topic);
                        coilList.add(idCoil);
                    }

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(fs.open(new Path(HDFS + "/proteus/split/proteus-awk/" + topic + ".csv"))));

                try {
                    // La primera línea del CSV es una cabecera
                    //String line = br.readLine();

                    // Primera linea a procesar
                    String line = br.readLine();
                    while (line != null) {

                        String[] mensaje = line.split(",");
                        Coil coil = new Coil().generateCoilObject(mensaje);
                        logic.buffer(coil, producer,topic, COIL_SPEED);
                        line = br.readLine();
                    }
                } catch (Exception e) {
                    logger.error("Error in the Proteus Kafka producer", e);
                }

                timeStampFinal = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

            }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        t = new Thread(this, Threadname);
        t.start();
    }

}


    /*

    public long getLengthFile() throws IOException {
        return fs.getFileStatus(new Path(HDFS + TABLE)).getLen();
    }

    public BlockLocation[] getBlocks() throws IOException {
        return fs.getFileBlockLocations(new Path(HDFS + TABLE), 0, getLengthFile());
    }

    protected String tamcoil;
    protected String tamlinea;

    public Integer getCoilSizeForOffsetsCalculation() throws IOException {

        File f = new File("/home/pablo.mesa/Escritorio/PROTEUS-OFFSETS.csv");
        if(f.exists() && !f.isDirectory()) {
            System.out.println("Existe el fichero");
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            line = br.readLine();

            System.out.println("line");

            String[] direccion = line.split(",");
            tamcoil = direccion[1];
            tamlinea = direccion[2];

            System.out.println("tam coil: " + tamcoil);
            System.out.println("tam linea: " + tamlinea);



        }
        else {
            System.out.println("No existe");
        }

        return Integer.valueOf(tamcoil);

    }

    public String[] getCoilUbications() throws IOException {

        String[] host_ubications = new String[10];
        // Tamaño

        int tam = getCoilSizeForOffsetsCalculation();

        // Calcular en que bloques está


        // Añadir los bloques en los que está al arry host_ubications

        // Retornar

        return host_ubications;
    }


    public void readSpecificBlock(int numerodebloque) throws IOException, InterruptedException {

            BlockLocation[] blocklist = getBlocks();
            FileSplit fileSplit = new FileSplit(new Path(HDFS + TABLE), blocklist[numerodebloque].getOffset(), blocklist[numerodebloque].getLength(), blocklist[numerodebloque].getHosts());

            FSDataInputStream fsin = fs.open(fileSplit.getPath());

            long start = fileSplit.getStart()-1; // Byte before the first

            if (start >= 0) { fsin.seek(start); }

            InputStream is = new BoundedInputStream(fsin, fileSplit.getLength());

            int i = 0;
            while (true){
                byte[] buffer = new byte[60];
                is.read(buffer, 0, 60);
                String text = new String(buffer, "UTF-8");
                System.out.println("Thread: " + Threadname + ", Byte: " + text );
                Thread.sleep(1);
                i++;
            }
    }

    public void run(){
        System.out.println("My thread is in running state.");
        try {


            readSpecificBlock(this.block);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        t = new Thread(this, Threadname);
        t.start();
    }




    ///////


    public void getSpecificBlock() throws IOException {

        BlockLocation[] blocklist = getBlocks();
        int i = 0;

        while ( i < blocklist.length ){
            System.out.println("Block[" + i + "]: " + blocklist[i].getHosts());
            i++;
        }

    }

    public void getBlocksOffsets() throws IOException {
        BlockLocation[] blocklist = getBlocks();
        int i = 0;
        while ( i < blocklist.length ){
            System.out.println("Offset Block[" + i + "]: " + blocklist[i].getOffset());
            i++;
        }
    }


    public long getBlockSize() throws IOException {
        return fs.getDefaultBlockSize(new Path(HDFS + TABLE));
    }




*/
