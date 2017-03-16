package com.treelogic.proteus.kafka.producer;

import org.apache.commons.io.input.BoundedInputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import static java.lang.Math.incrementExact;
import static java.lang.Math.toIntExact;

import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.hdfs.protocol.LocatedBlock;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;
import org.apache.hadoop.io.DataInputByteBuffer;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.treelogic.proteus.kafka.producer.ProteusKafkaProducer.HDFS_URI;
import static com.treelogic.proteus.kafka.producer.ProteusKafkaProducer.PROTEUS_MERGED_TABLE;

/**
 * Created by pablo.mesa on 16/03/17.
 */
public class preprocessingOffsets implements Runnable {

    public FileSystem fs;
    public String HDFS;
    public String TABLE;
    public Configuration conf;
    private String Threadname;
    private Thread t;
    private int block;

    preprocessingOffsets(){}

    preprocessingOffsets(String Threadname, int block, FileSystem fs, String HDFS_URI, String TABLE, Configuration conf){
        this.Threadname = Threadname;
        this.fs = fs;
        this.HDFS = HDFS_URI;
        this.TABLE = TABLE;
        this.conf = conf;
        this.block = block;

    }

    public long getLengthFile() throws IOException {
        return fs.getFileStatus(new Path(HDFS_URI + TABLE)).getLen();
    }

    public long getBlockSize() throws IOException {
        return fs.getDefaultBlockSize(new Path(HDFS_URI + TABLE));
    }

    public BlockLocation[] getBlocks() throws IOException {
        return fs.getFileBlockLocations(new Path(HDFS_URI + TABLE), 0, getLengthFile());
    }

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

    public void readSpecificBlock(int numerodebloque) throws IOException, InterruptedException {

            BlockLocation[] blocklist = getBlocks();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(fs.open(new Path(HDFS + TABLE))));

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

}
