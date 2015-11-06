/**
 * 
 */
package parameter;

/**
 * @author sumit
 *
 */
public interface IAlgorithmParameter<K, E> {
   	public E getAlgorithmParameter(K k);
	public void setAlgorithmParameter(K k, E e);
}
