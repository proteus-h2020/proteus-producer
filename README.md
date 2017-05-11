# proteus-producer
[![Build Status](https://travis-ci.org/proteus-h2020/proteus-producer.svg?branch=fix-remove-unused-consumer-module)](https://travis-ci.org/proteus-h2020/proteus-producer)

A Kafka Producer that is in charge of producing all the PROTEUS data in order to be consumed by different actors. This is intended to simulate the current industry-based scenario of AMII (sequential generation of multiple coils). To achieve this, the producer uses three different topics:

* **proteus-realtime** manages all the time-series data with 1 dimension (position x) and 2 Dimension (position x and position y), produced and available in real time (streaming) during the coil production.
* **proteus-hsm** manages all HSM data, produced as aggregated information at the end of the process and available only once the coil production has finalised.
* **proteus-flatness**: manages the flatness data variables, produced as measures of the flatness of the resulting coil, and available only after a certain delay after the coil production has finalised.

## Getting started

### Installing software dependencies
(see requirements section)

### Moving files to HDFS
An important requirement before running the producer is to have the heterogeneous PROTEUS data (provided by e-mail to all the project partners) in an HDFS cluster (single-node deployments are also valid).

Use the following command to move your data (**PROTEUS_HETEROGENEOUS_FILE.csv**) to your HDFS:

```
hdfs dfs -put <path_to_PROTEUS_HETEROGENEOUS_FILE.csv> /proteus/heterogeneous/final.csv
```

If you want to use a different HDFS location, you need to configure the variable `com.treelogic.proteus.hdfs.streamingPath` in the  **```src/main/resources/config.properties```** before running the program.

Since HSM data is also managed by this producer (when a coil has finished, its corresponding HSM record is produced using the **proteus-hsm** topic), you need to move your HSM to your HDFS too:

```
hdfs dfs -put <path_to_HSM_subset.csv> /proteus/hsm/HSM_subset.csv
```
If you want to use a different HDFS location, you need to configure the variable `com.treelogic.proteus.hdfs.hsmPath` in the  **```src/main/resources/config.properties```** before running the program.

THe HSM_subset.csv file was also provided by e-mail to all the PROTEUS partners. Actually, this file (2GB) is a subset of the original HSM data (40GB), containing only those coils present in the real-time dataset (PROTEUS_HETEROGENEOUS_FILE.csv). 

**IMPORTANT**
If you need to use the HSM data for traning and learning purposes, please, keep in mind that the HSM_subset.csv is just a subset of the original HSM.

### Creating Kafka topics
You need also to create the abovementioned kafka topics. You can use the following commands (by default, we create one partition per topic. This should be improved in the future):

```
/opt/kafka/bin/kafka-topics.sh --zookeeper <your_zookeeper_url>:2181 --create --topic proteus-realtime --partitions 1 --replication-factor 1

```

```
/opt/kafka/bin/kafka-topics.sh --zookeeper <your_zookeeper_url>:2181 --create --topic proteus-hsm --partitions 1 --replication-factor 1

```

```
/opt/kafka/bin/kafka-topics.sh --zookeeper <your_zookeeper_url>:2181 --create --topic proteus-flatness --partitions 1 --replication-factor 1

```

## How to run it
You can run the kafka producer in different ways. If you are using a terminal, please, run the following command.
```
mvn exec:java
```

If you want to import and run the project into your prefered IDE (e.g. eclipse, intellij), you need to import the maven project and execute the `com.treelogic.proteus.Runner` class. 


## Configuration
The following shows the default configuration of the producer, specified in the  **```src/main/resources/config.properties```** file:

```properties
com.treelogic.proteus.hdfs.baseUrl=hdfs://192.168.4.245:8020 # Base URL of your HDFS
com.treelogic.proteus.hdfs.streamingPath=/proteus/heterogeneous/final.csv # Path to realtime data
com.treelogic.proteus.hdfs.hsmPath=/proteus/hsm/HSM_subset.csv #Path to HSM data

com.treelogic.proteus.kafka.bootstrapServers=clusterIDI.slave01.treelogic.local:6667,clusterIDI.slave02.treelogic.local:6667,clusterIDI.slave03.treelogic.local:6667 # Bootstrap servers
com.treelogic.proteus.kafka.topicName=proteus-realtime # Topic name of real-time data
com.treelogic.proteus.kafka.flatnessTopicName=proteus-flatness # Topic name of flatness data
com.treelogic.proteus.kafka.hsmTopicName=proteus-hsm # Topic name of HSM data

com.treelogic.proteus.model.timeBetweenCoils=10000 # The time (in ms) that the program takes between generation of different coils
com.treelogic.proteus.model.coilTime=120000 #The time (in ms) that the producer takes to produce a single coil
com.treelogic.proteus.model.flatnessDelay=20000 #When a coil finishes, the program schedules its corresponding flatness generation with a delay time here indicated

com.treelogic.proteus.model.hsm.splitter=;
```


## Software Requirements
* **Java 8**
* **Maven >= 3.0.0**
* **Kafka 0.10.x**

## Logs and monitoring
immediately after running the program two logs files are created:
* **kafka.log**: this contains logging infomation about the kafka cluster that you previously configured. Kafka messages are written into this file

* **proteus.log**: this contains information about the coil generation process.


By default these files are created in the main directory (the same as the pom.xml is), but you can customize this in the **src/main/resources/loback.xml**. Both kafka and proteus logs are also printed to STDOUT. 

