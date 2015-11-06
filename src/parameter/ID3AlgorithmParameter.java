/**
 * 
 */
package parameter;

import java.util.HashMap;
import java.util.Map;

import parameter.ID3ParameterConstants.ID3ParameterEnum;

/**
 * @author sumit
 *
 */
public class ID3AlgorithmParameter implements
		IAlgorithmParameter<ID3ParameterEnum, Object> {
	private Map<ID3ParameterEnum, Object> parameters = new HashMap<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see parameter.IAlgorithmParameter#getAlgorithmParameter()
	 */
	@Override
	public Object getAlgorithmParameter(ID3ParameterEnum key) {
		return this.parameters.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * parameter.IAlgorithmParameter#setAlgorithmParameter(java.lang.Object)
	 */
	@Override
	public void setAlgorithmParameter(ID3ParameterEnum key, Object value) {
		this.parameters.put(key, value);
	}

}
