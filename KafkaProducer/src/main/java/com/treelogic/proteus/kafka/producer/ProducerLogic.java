package com.treelogic.proteus.kafka.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by pablo.mesa on 13/03/17.
 */
public class ProducerLogic {




    ArrayList<Coil> coilsbuffer = new ArrayList<>();
    ArrayList<Double> xpositionsbuffer = new ArrayList<>();
    Integer identificadorbobina = Integer.MIN_VALUE;
    ArrayList<Double> delays = new ArrayList<>();
    Double tiempogeneracionbobina = 0.0;
    Double bobina = 120000.0;
    public Producer<String, String> productor;
    String PROTEUS_KAFKA_TOPIC = "test-timestamp";
    private static ObjectMapper mapper = new ObjectMapper();

    ProducerLogic(){}

    public void buffer(Coil coil, Producer<String,String> producer, String topic) throws InterruptedException {

        this.PROTEUS_KAFKA_TOPIC = topic;
        this.productor = producer;
        Integer idcoil = coil.getID();

        if ( identificadorbobina.equals(idcoil)){
            generarBufferBobinas(coil);
        } else {
            if ( !coilsbuffer.isEmpty()) imprimirBufferBobinas();
            identificadorbobina = idcoil;
        }
    }

    public void generarBufferBobinas(Coil coil){
        coilsbuffer.add(coil);
        xpositionsbuffer.add(coil.getPositionX());
    }

    public void imprimirBufferBobinas() throws InterruptedException {


        calculaDelays();

        System.out.println("Tamaño buffer: " + coilsbuffer.size());
        System.out.println("Tamaño posiciones x: " + xpositionsbuffer.size());
        System.out.println("Tiempo generacion bobina: " + this.tiempogeneracionbobina);
        System.out.println("Tamaño delays: " + delays.size());
        System.out.println("Factor Delay: " + (bobina/tiempogeneracionbobina));

        printCoils();

        Thread.sleep(5000);


        Thread.sleep(2500);

        this.tiempogeneracionbobina = 0.0;
        delays.clear();
        coilsbuffer.clear();
        xpositionsbuffer.clear();
    }

    public void calculaDelays(){
        int i = 0;
        double delay = 0.0;

        while ( i < xpositionsbuffer.size()-1){
            delay = xpositionsbuffer.get(i+1) - xpositionsbuffer.get(i);
            this.delays.add(delay);
            this.tiempogeneracionbobina += delay;
            i++;
        }
    }

    public void printCoils() throws InterruptedException {

        int contadorestimestamp = 0;
        int i = 0;
        int j = 0;

        String timeStampInicioBobina = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

        coilsbuffer.get(i).setTimeStamp(timeStamp);
        String message = null;
        try {
            message = mapper.writeValueAsString(coilsbuffer.get(i));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Thread.sleep(5000);
        productor.send(new ProducerRecord<String, String>(PROTEUS_KAFKA_TOPIC, message));

        contadorestimestamp++;
        i++;

        while ( i < coilsbuffer.size()){
            // System.out.println("Tiempo a parar enter envio de mensaje: " + delays.get(j));
            // Thread.sleep((long)(xpositionsbuffer.get(j+1) - xpositionsbuffer.get(j)*(bobina%tiempogeneracionbobina)*1000));
            ;
            Thread.sleep((long) (delays.get(j) * (bobina/tiempogeneracionbobina)));
            timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            coilsbuffer.get(i-1).setTimeStamp(timeStamp);
            try {
                message = mapper.writeValueAsString(coilsbuffer.get(i-1));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            productor.send(new ProducerRecord<String, String>(PROTEUS_KAFKA_TOPIC, message));
            contadorestimestamp++;
            i++;
            j++;
        }

        String timeStampFinBobina = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

        System.out.println("TimeStamp Inicio Bobina: " + timeStampInicioBobina);
        System.out.println("TimeStamp Fin Bobina: " + timeStampFinBobina);
        System.out.println("TimeStamps asigados a bobinas: " + contadorestimestamp);
    }
}
