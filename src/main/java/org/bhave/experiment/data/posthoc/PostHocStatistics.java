package org.bhave.experiment.data.posthoc;

import java.util.List;
import java.util.Properties;

import org.bhave.experiment.Configurable;
import org.bhave.experiment.Model;
import org.bhave.experiment.Prototype;
import org.bhave.experiment.data.DataExporter;

/**
 * <p>
 * PostHocStatistics are measurements that can be made after the model has been
 * executed. These measurements, reports or descriptions do not need information
 * from the modelo while this is running, as such they do not need to be
 * integrated in a producer - consumer environment. Instead we take the model,
 * generate the data, and store it directly with the appropriate DataExporter.
 * </p>
 * 
 * @author Davide Nunes
 * 
 */
public interface PostHocStatistics extends Configurable,
		Prototype<PostHocStatistics> {
	/**
	 * Returns the name of this statistics measure or report.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * This is used to verify if the data statistics class was designed for the
	 * model being measured. This means that when you implement this class you
	 * must provide the target model class.
	 * 
	 * @return a Model Class a model being measured by this statistics
	 * @see Model
	 * 
	 */
	public Class<? extends Model> getTargetModelClass();

	/**
	 * <p>
	 * Analyses a model and returns a list of properties. This is basically a
	 * table and the list provives the multiple rows of the table.
	 * </p>
	 * 
	 * <p>
	 * For each element in the table you can access the relevant column using
	 * the keys from the Properties object ({@link Properties#propertyNames()});
	 * </p>
	 * 
	 * @param model
	 *            the model being measured
	 * @return a list of {@link Properties} with the measurements for the model
	 */
	public List<Properties> measure(Model model);

	/**
	 * <p>
	 * Retuns a list of the names for the columns that contain data, while the
	 * order is not important, the list should correspond to keys on each item
	 * in the list returned by {@link #measure(Model)}.
	 * </p>
	 * 
	 * @return a list of names for the data columns.
	 */
	public List<String> getDataColumns();

	public void addExporter(DataExporter export);

	/**
	 * Should use the {@link #measure(Model)} method to get the information out
	 * of the model and produce a report for each exporter.
	 * 
	 * @param the
	 *            model to be inspected
	 * @return
	 */
	public void report(Model model);
}
