/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.dummy;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.bhave.experiment.Model;
import org.bhave.experiment.data.AbstractStatistics;
import org.bhave.experiment.data.Statistics;

/**
 *
 * @author davide
 */
public class DummyStats extends AbstractStatistics {

    @Override
    protected Statistics createPrototype() {
        return new DummyStats();
    }

    @Override
    public Properties measure(Model model) {
        DummyModel dModel = (DummyModel) model;
        Properties props = new Properties();
        props.setProperty("testStat", Integer.toString(dModel.dummyStatValue()));

        return props;
    }

    @Override
    public String getName() {
        return "dummy stats";
    }

    @Override
    public Class<? extends Model> getTargetModelClass() {
        return DummyModel.class;
    }

    @Override
    public List<String> getColumnNames() {
        List<String> list = new LinkedList<>();
        list.add("testStat");
        return list;
    }

}
