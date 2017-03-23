package com.treelogic.proteus.kafka.offsets;

import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import java.util.Properties;


/**
 * Created by TREELOGIC\david.piris on 15/03/17.
 */
public class KafkaAdmin {

    public void createTopic(String mytopic){

        String zookeeperConnect = "clusteridi.slave01.treelogic.local:2181,clusteridi.slave02.treelogic.local:2181,clusteridi.slave03.treelogic.local:2181";
        int sessionTimeoutMs = 10 * 1000;
        int connectionTimeoutMs = 8 * 1000;
        int partitions = 1;
        int replication = 1;

        ZkClient zkClient = new ZkClient(
                zookeeperConnect,
                sessionTimeoutMs,
                connectionTimeoutMs,
                ZKStringSerializer$.MODULE$);

        // Security for Kafka was added in Kafka 0.9.0.0
        boolean isSecureKafkaCluster = false;
        // ZkUtils for Kafka was used in Kafka 0.9.0.0 for the AdminUtils API
        ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperConnect), isSecureKafkaCluster);

        // Add topic configuration here
        Properties topicConfig = new Properties();

        if (!AdminUtils.topicExists(zkUtils,mytopic))
            AdminUtils.createTopic(zkUtils, mytopic, partitions, replication, topicConfig);
        zkClient.close();

    }

}
