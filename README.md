# proteus-producer
[![Build Status](https://travis-ci.org/proteus-h2020/proteus-producer.svg?branch=fix-remove-unused-consumer-module)](https://travis-ci.org/proteus-h2020/proteus-producer)

A Kafka Producer that is in charge of producing all the PROTEUS data in order to be consumed by differnet actors. This is intended to simulate the data production for coils. For this purpose we use three different topics:

* **proteus-realtime** manages all the time-series data with 1 dimension (position x) and 2 Dimension (position x and position y), produced and available in real time (streaming) during the coil production.
* **proteus-hsm** manages all HSM data, produced as aggregated information at the end of the process and available only once the coil production has finalised.
* **proteus-flatness**: manages the flatness data variables, produced as measures of the flatness of the resulting coil, and available only after a certain delay after the coil production has finalised.

## How to run it

```
mvn exec:java
```

## Configuration

## Software Requirements
* **Java 8**
* **Maven >= 3.0.0**

## Logs and monitoring
Inmediatly after running the program two logs files are created:
* **kafka.log**: this contains logging infomation about the kafka cluster that you previously configured. Kafka messages are written into this file

* **proteus.log**: this contains information about the coil generation process.


By default these files are created in the main directory (the same as the pom.xml is), but you can customize this in the **```src/main/resources/loback.xml```**. Both kafka and proteus logs are also printed to STDOUT. 
mvn clean compile assembly:single

