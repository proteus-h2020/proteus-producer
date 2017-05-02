package com.treelogic.proteus.kafka.offsets;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.util.ArrayList;

/**
 * Created by pablo.mesa on 17/03/17.
 */
public class KafkaProducersFactory {

    // Atributos

    protected FileSystem fs;
    protected String HDFS;
    protected Configuration HDFS_CONF;
    protected double COIL_SPEED;

    public KafkaProducersFactory() {}

    public void createProducers(int num_producers, ArrayList<Integer> ids, int chunk, double COIL_SPEED){

        KafkaProducerThread[] threads = new KafkaProducerThread[num_producers];
        for ( int i = 0; i < num_producers; i++){
            threads[i] = new KafkaProducerThread("thread" + i, i, this.getFileSystem(), this.getHDFS(), this.getHDFS_CONF(), ids, chunk, COIL_SPEED);
            threads[i].start();
        }

    }

    public void setConfiguration(FileSystem fs, String HDFS, Configuration HDFS_CONF, double COIL_SPEED){
        this.fs = fs;
        this.HDFS = HDFS;
        this.HDFS_CONF = HDFS_CONF;
        this.COIL_SPEED = COIL_SPEED;
    }

    // Getters

    public FileSystem getFileSystem(){
        return this.fs;
    }

    public String getHDFS(){
        return this.HDFS;
    }

    public Configuration getHDFS_CONF(){
        return this.HDFS_CONF;
    }
}
