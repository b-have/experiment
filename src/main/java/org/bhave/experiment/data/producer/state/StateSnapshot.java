package org.bhave.experiment.data.producer.state;

import java.util.List;
import java.util.Properties;

import org.bhave.experiment.Configurable;
import org.bhave.experiment.Model;
import org.bhave.experiment.Prototype;

/**
 * Used to store different model component descriptions that can be later
 * analysed. For example if a simulation model works with different network
 * models, the network structures can be stored for latter use.
 * 
 * Each state should be stored in a separate file uppon export
 * 
 * @author Davide
 * 
 */
public interface StateSnapshot extends Configurable, Prototype<StateSnapshot> {
	/**
	 * If a model has different component values to be recorded they can be
	 * recorder as a table.
	 * 
	 * @param model
	 *            the model to be inspected
	 * @return
	 */
	public List<Properties> store(Model model);

	/**
	 * The name of this state snapshot
	 * 
	 * @return a string with the name
	 */
	public String getName();

	/**
	 * The model for which this state snapshot was designed to.
	 * 
	 * @return a class object subtype of model
	 */
	public Class<? extends Model> getTargetModelClass();

	/**
	 * Returns the column names for our state snapshot table
	 * 
	 * @return
	 */
	public List<String> getColumnNames();
}
