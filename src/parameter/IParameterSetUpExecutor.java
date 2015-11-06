/**
 * 
 */
package parameter;

/**
 * @author sumit
 *
 */
public interface IParameterSetUpExecutor<T> {
	public T createAlgorithmParameter(Object... optionalParameters)
			throws Exception;
}
