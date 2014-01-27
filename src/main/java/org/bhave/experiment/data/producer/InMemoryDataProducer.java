/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.data.producer;

import java.util.List;

/**
 * Tagging interface of a data producer that stores its data in memory
 *
 * @author Davide Nunes
 */
public interface InMemoryDataProducer extends DataProducer {

    public List<String> getData();


}
