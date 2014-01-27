package org.bhave.experiment.data;

import java.util.List;
import java.util.Properties;
import org.bhave.experiment.Configurable;
import org.bhave.experiment.Model;
import org.bhave.experiment.Prototype;

/**
 * Statistics objects measure the model and return a set of properties to be
 * forwarded by {@link DataProducer} objects.
 *
 * @author Davide Nunes
 */
public interface Statistics extends Configurable, Prototype<Statistics> {

    public Properties measure(Model model);

    public String getName();

    /**
     * Since we have type erasure in java, this is used to verify in runtime if
     * the data producer is adequate to the model being used.
     *
     * @return a Model Class
     * @see Model
     *
     */
    public Class<? extends Model> getTargetModelClass();

    public List<String> getColumnNames();

}
