/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.data;

import java.util.ArrayList;
import java.util.List;
import org.bhave.experiment.data.consumer.DataConsumer;
import org.bhave.experiment.data.producer.DataProducer;

/**
 * Container for data record to be created by data producers and consumed by
 * data consumers
 *
 * @see DataProducer
 * @see DataConsumer
 *
 * @author Davide Nunes
 */
public class DataRecord {

    private ArrayList<String> columns;
    private ArrayList<String> data;

    public DataRecord(List<String> columns, List<String> data) {
        this.columns = new ArrayList<>(columns);
        this.data = new ArrayList<>(data);
    }

    public ArrayList<String> columns() {
        return new ArrayList<>(columns);
    }

    public ArrayList<String> data() {
        return new ArrayList<>(data);
    }
}
