/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bhave.experiment.run;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.bhave.experiment.Experiment;
import org.bhave.experiment.ExperimentRunner;
import org.joda.time.Period;

/**
 *
 * @author Davide Nunes
 */
public class GUIConsole extends javax.swing.JFrame implements ExperimentConsole {
	private static final long serialVersionUID = 1L;
	
	Experiment experiment;
    ExperimentRunner runner;
    
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GUIConsole console = new GUIConsole();
                console.setVisible(true);
            }
        });
        
    }

    /**
     * Creates new form Console
     */
    public GUIConsole() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progress = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        notification = new javax.swing.JTextArea();
        load = new javax.swing.JButton();
        filePath = new javax.swing.JTextField();
        browse = new javax.swing.JButton();
        run = new javax.swing.JButton();
        clear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simple Experiment Console");

        notification.setColumns(20);
        notification.setRows(5);
        jScrollPane1.setViewportView(notification);

        load.setText("load experiment");
        load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });

        filePath.setEditable(false);

        browse.setText("Browse...");
        browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseActionPerformed(evt);
            }
        });

        run.setText("Run Experiment");
        run.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runActionPerformed(evt);
            }
        });

        clear.setText("Clear");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(run, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(load, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filePath, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(clear)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(filePath, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(browse, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(load, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(run)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progress, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    class RunExperimenTask extends SwingWorker<Void, Void> {
        
        ExperimentConsole console;
        private ExperimentRunner runner;
        Experiment experiment;
        
        public RunExperimenTask(Experiment experiment, ExperimentConsole console) {
            this.console = console;
            this.experiment = experiment;
        }

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            if (experiment != null) {
                run.setEnabled(false);
                this.runner = experiment.getRunner();
                
                this.runner.attach(console);
                
                this.runner.load(experiment);
                runner.start();
            }
            
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            notification.append("Experiment done\n");
            progress.setString("done");
            run.setEnabled(true);
            load.setEnabled(true);
            browse.setEnabled(true);
        }
    }
    
    RunExperimenTask runTask;

    private void runActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runActionPerformed
        if (experiment == null) {
            notification.append("Please load an experiment first...nothing loaded\n");
        } else {
            load.setEnabled(false);
            browse.setEnabled(false);
            run.setEnabled(false);
            notification.append("Starting experiment...\n");
            
            progress.setStringPainted(true);

            //Create a swing worker task to be executed in background
            runTask = new RunExperimenTask(experiment, this);
            runTask.execute();
        }
    }//GEN-LAST:event_runActionPerformed
    
    File currentlyLoaded;
    private void browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseActionPerformed
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        //Handle open button action.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentlyLoaded = fc.getSelectedFile();
            
            this.filePath.setText(currentlyLoaded.getPath());
        }
        

    }//GEN-LAST:event_browseActionPerformed

    private void loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadActionPerformed
        if (currentlyLoaded != null) {
            try {
                experiment = Experiment.fromFile(currentlyLoaded);
                progress.setValue(0);
                progress.setString("");
                
                notification.append("Experiment Loaded: " + experiment.getUID() + " \n");
                
                notification.append("Runner: " + experiment.getRunner().getClass().getName() + " \n");
                notification.append("Model: " + experiment.getModel().getClass().getName() + " \n");
                
                notification.append("Parameter Space: " + experiment.getParameterSpace().size() + " runs\n");
                
            } catch (Exception exception) {
                notification.append("Error: \n");
                notification.append(exception.getMessage() + " \n");
            }
        } else {
            notification.append("Please choose an experiment first...nothing selected\n");
        }
    }//GEN-LAST:event_loadActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        notification.setText("");
    }//GEN-LAST:event_clearActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browse;
    private javax.swing.JButton clear;
    private javax.swing.JTextField filePath;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton load;
    private javax.swing.JTextArea notification;
    private javax.swing.JProgressBar progress;
    private javax.swing.JButton run;
    // End of variables declaration//GEN-END:variables

    public void updateProgress(final Period remaining, final int value, final int currentRun, final int totalRuns) {
        SwingUtilities.invokeLater(new Runnable() {
            
            public void run() {
                
                progress.setValue(value);
                
                progress.setString("estimated time remaining: "
                        + String.format("%02d:%02d:%02d", remaining.getHours(),
                                remaining.getMinutes(), remaining.getSeconds()));
                
                notification.append("completed run: " + currentRun + " / "
                        + totalRuns + "\n");
                
            }
        });
    }
}
