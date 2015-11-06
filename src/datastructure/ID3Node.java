/**
 * 
 */
package datastructure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author sumit
 *
 */
public class ID3Node {

	private ID3Node parentNode;
	private String fetaureName;
	private int depth;
	private int dataSize;
	private Map<String, ID3Node> childrens;
	private Map<String, Integer> targetClassDistribution;
	private String uniqeNodeIdentifier;
	private static int classCount = 1;

	public ID3Node() {

	}

	public ID3Node(String pFeatureName, int pDepth, int pDataSize) {
		this.fetaureName = pFeatureName;
		this.depth = pDepth;
		this.dataSize = pDataSize;
		this.targetClassDistribution = new HashMap<String, Integer>();
		this.uniqeNodeIdentifier = "" + classCount++; // UUID.randomUUID().toString();
	}

	public Collection<ID3Node> getChildrens() {
		return this.childrens == null ? null : this.childrens.values();
	}

	public boolean isLeafNode() {
		return this.fetaureName == null || this.childrens == null;
	}

	public void addChildrenNode(String featureValue, ID3Node children) {
		if (this.childrens == null) {
			this.childrens = new HashMap<String, ID3Node>();
		}
		this.childrens.put(featureValue, children);
	}

	/**
	 * @return the fetaureName
	 */
	public String getFetaureName() {
		return fetaureName;
	}

	/**
	 * @param fetaureName
	 *            the fetaureName to set
	 */
	public void setFetaureName(String fetaureName) {
		this.fetaureName = fetaureName;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth
	 *            the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * @return the dataSize
	 */
	public int getDataSize() {
		return dataSize;
	}

	/**
	 * @param dataSize
	 *            the dataSize to set
	 */
	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	/**
	 * @return the targetClassDistribution
	 */
	public Map<String, Integer> getTargetClassDistribution() {
		return targetClassDistribution;
	}

	public void addTargetClassDistribution(String valueKey, int noOfExamples) {
		this.targetClassDistribution.put(valueKey, noOfExamples);
	}

	/**
	 * @param targetClassDistribution
	 *            the targetClassDistribution to set
	 */
	public void setTargetClassDistribution(
			Map<String, Integer> targetClassDistribution) {
		this.targetClassDistribution = targetClassDistribution;
	}

	public String toString() {
		return "Parent Node:" + getParentNodeName() + "\n" + "Depth Of Node :"
				+ this.depth + " \nFeature Name :" + this.fetaureName + "\n"
				+ "No Of Examples :" + this.dataSize + "\n"
				+ "Class Label Distribution" + this.targetClassDistribution
				+ "\n" + "Is Leaf Node :" + this.isLeafNode() + "\n";
	}

	private String getParentNodeName() {

		return this.parentNode == null ? "ROOT-NODE"
				: this.parentNode.fetaureName + " [ node number::"
						+ this.parentNode.uniqeNodeIdentifier + "]";
	}

	/**
	 * @return the parentNode
	 */
	public ID3Node getParentNode() {
		return parentNode;
	}

	/**
	 * @param parentNode
	 *            the parentNode to set
	 */
	public void setParentNode(ID3Node parentNode) {
		this.parentNode = parentNode;
	}

	public ID3Node getChildNode(String keyValue) {
		return this.childrens == null ? null : (this.childrens.get(keyValue));
	}
}
