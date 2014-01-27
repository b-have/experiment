package org.bhave.experiment.data;

import java.io.File;
import java.util.ArrayList;
import org.apache.commons.configuration.Configuration;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A simple test for the file data exporter
 *
 * @author Davide Nunes
 */
public class TestFileDataExporter {

    public TestFileDataExporter() {
    }

    @Test
    public void testDefaultConfiguration() {
        System.out.println("Testing FileDataExporter");
        FileDataExporter exporter = new FileDataExporter();
        Configuration config = exporter.getConfiguration();

        assertNotNull(config);

        assertTrue(config.containsKey(FileDataExporter.P_APPEND));
        assertTrue(config.containsKey(FileDataExporter.P_FILENAME));

        ArrayList<String> columns = new ArrayList<>();
        columns.add("column-1");
        columns.add("column-2");

        ArrayList<String> data = new ArrayList<>();

        data.add("0.5");
        data.add("0.2");

        exporter.loadColumns(columns);

        assertNull(exporter.getCurrentFile());

        System.out.println("Exporting data...");
        exporter.exportRecord(data);

        assertNotNull(exporter.getCurrentFile());

        File file = exporter.getCurrentFile();

        System.out.println("Closing the file...");
        //clean up
        exporter.finish();

        assertTrue(file.exists());
        System.out.println("Test file: " + file.getPath());

        System.out.println("Deleting the file...");
        file.delete();

        assertFalse(file.exists());

    }

}
