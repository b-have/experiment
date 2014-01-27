package org.bhave.experiment.data;

import java.util.List;
import org.bhave.experiment.Configurable;
import org.bhave.experiment.Prototype;

/**
 * Data Exporters have access to data consumers and use them to access the data
 * and store it in a file or in a database for instance.
 *
 * @author Davide Nunes
 */
public interface DataExporter extends Configurable, Prototype<DataExporter> {

    /**
     * Loads the names of the data columns
     *
     * @param columns the data columns that are being considered
     */
    public void loadColumns(List<String> columns);

    /**
     * Exports the given data
     *
     * Note: if loads columns as used, the number of elements in the column list
     * must be the same as the number of elements in the data being exported
     *
     * @param data a data record to be exported
     */
    public void exportRecord(List<String> data);

    /**
     * If the data comes pre-formated
     *
     * @param data a line record with data
     */
    public void exportRecord(String data);

    /**
     * Data exporter cleanup goes here
     */
    public void finish();
    
}
