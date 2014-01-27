/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment;

import org.bhave.experiment.dummy.DummyModel;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author davide
 */
public class TestRunnableModel {

    @Test
    public void testRunnableModel() {
        try {
            Model model = new DummyModel();
            Model model2 = model.create();

            Thread t = new Thread(model2);
            t.start();

            t.join();
        } catch (InterruptedException ex) {
            fail(ex.getMessage());
        }

    }

}
