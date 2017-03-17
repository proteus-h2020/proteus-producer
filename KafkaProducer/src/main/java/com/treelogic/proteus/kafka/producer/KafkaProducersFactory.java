package com.treelogic.proteus.kafka.producer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

/**
 * Created by pablo.mesa on 17/03/17.
 */
public class KafkaProducersFactory {

    // Atributos

    protected FileSystem fs;
    protected String HDFS;
    protected String PROTEUS_TABLE;
    protected Configuration HDFS_CONF;

    KafkaProducersFactory() {}

    public void createProducers(int num_producers){

        KafkaProducerThread[] threads = new KafkaProducerThread[num_producers];
        for ( int i = 0; i < num_producers; i++){
            threads[i] = new KafkaProducerThread("thread" + 1, i, this.getFileSystem(), this.getHDFS(), this.getPROTEUS_TABLE(), this.getHDFS_CONF());
            threads[i].start();
        }

    }

    public void setConfiguration(FileSystem fs, String HDFS, String PROTEUS_TABLE, Configuration HDFS_CONF){
        this.fs = fs;
        this.HDFS = HDFS;
        this.PROTEUS_TABLE = PROTEUS_TABLE;
        this.HDFS_CONF = HDFS_CONF;
    }

    // Getters

    public FileSystem getFileSystem(){
        return this.fs;
    }

    public String getHDFS(){
        return this.HDFS;
    }

    public String getPROTEUS_TABLE(){
        return this.PROTEUS_TABLE;
    }

    public Configuration getHDFS_CONF(){
        return this.HDFS_CONF;
    }
}
