/**
 * 
 */
package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import parameter.IAlgorithmParameter;
import parameter.ID3ParameterConstants;
import parameter.ID3ParameterConstants.ID3ParameterEnum;
import datastructure.ID3Node;

/**
 * @author sumit
 *
 */
public class ID3Algorithm
		implements
		IAlgorithm<IAlgorithmParameter<ID3ParameterEnum, Object>, IAlgorithmParameter<ID3ParameterEnum, Object>> {

	IAlgorithmParameter<ID3ParameterEnum, Object> input = null;
	int maxDepthAllowed = 0;
	String depthCheckedAllowed = ID3ParameterConstants.DEPTH_CHECK_REQUIRED_NO;
	Map<String, Integer> featureColumnIndex = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see algorithm.IAlgorithm#run(java.lang.Object)
	 */
	@Override
	public IAlgorithmParameter<ID3ParameterEnum, Object> run(
			IAlgorithmParameter<ID3ParameterEnum, Object> input) {
		init(input);

		// call the tree build algorithm
		int targetColumnIndex = ((Map<String, Integer>) input
				.getAlgorithmParameter(ID3ParameterEnum.FEATURE_VETOR_NAMES))
				.get((String) input
						.getAlgorithmParameter(ID3ParameterEnum.TARGET_FEATURE_NAME));

		List<String> featureVector = new ArrayList<>(
				(this.featureColumnIndex).keySet());

		featureVector.remove((String) input
				.getAlgorithmParameter(ID3ParameterEnum.TARGET_FEATURE_NAME));

		List<Integer> rowIds = new ArrayList<>();
		int size = ((List<String>) this.input
				.getAlgorithmParameter(ID3ParameterEnum.INPUT_DATA_SETS))
				.size();

		for (int i = 0; i < size; i++) {
			rowIds.add(i);
		}
		ID3Node root = createID3Tree(rowIds, featureVector, targetColumnIndex,
				0);

		input.setAlgorithmParameter(ID3ParameterEnum.OUTPUT_OF_ALGORITHM, root);
		return input;
	}

	private void init(IAlgorithmParameter<ID3ParameterEnum, Object> pInput) {
		this.input = pInput;
		this.depthCheckedAllowed = (String) this.input
				.getAlgorithmParameter(ID3ParameterEnum.MAX_DEPTH_CHECK_REQUIRED);
		this.featureColumnIndex = (Map<String, Integer>) this.input
				.getAlgorithmParameter(ID3ParameterEnum.FEATURE_VETOR_NAMES);
		 this.maxDepthAllowed = (int) this.input
					.getAlgorithmParameter(ID3ParameterEnum.ALLOWED_MAX_DEPTH);

	}

	private ID3Node createID3Tree(List<Integer> rowIds,
			List<String> featureVector, int targetColumnIndex, int depth) {
		ID3Node root = null;

		List<String> inputData = (List<String>) this.input
				.getAlgorithmParameter(ID3ParameterEnum.INPUT_DATA_SETS);

		if (isStoppingCriteria(rowIds, featureVector, depth)) {
			return childID3Node(inputData, rowIds, featureVector,
					targetColumnIndex, depth);
		}

		int sizeFeatureVector = featureVector.size();
		double maxInformationGain = Double.MIN_VALUE;
		int minInformationGainColumnIndex = -1;

		double entropyNode = ID3AlgorithmUtil
				.computeEntropy(
						inputData,
						rowIds,
						targetColumnIndex,
						(String) this.input
								.getAlgorithmParameter(ID3ParameterEnum.DATA_FIELD_SEPARATOR));

		// all of same class type
		if (entropyNode == 0) {
			return childID3Node(inputData, rowIds, featureVector,
					targetColumnIndex, depth);
		}

		double tempInformationGain = 0.0;
		int currFeatureOriginalIndex = -1;
		int currFeatureIndex = -1;
		for (int i = 0; i < sizeFeatureVector; i++) {
			currFeatureOriginalIndex = this.featureColumnIndex
					.get(featureVector.get(i));

			tempInformationGain = ID3AlgorithmUtil
					.computeInformationGain(
							entropyNode,
							inputData,
							rowIds,
							currFeatureOriginalIndex,
							targetColumnIndex,
							(String) this.input
									.getAlgorithmParameter(ID3ParameterEnum.DATA_FIELD_SEPARATOR));

			if (tempInformationGain > maxInformationGain) {
				maxInformationGain = tempInformationGain;
				minInformationGainColumnIndex = currFeatureOriginalIndex;
				currFeatureIndex = i;
			}
		}
		// separate data sets based on the current maximum information Gain
		Map<String, List<Integer>> featureRowDistribution = ID3AlgorithmUtil
				.getFetureRowIdsDistribution(
						inputData,
						rowIds,
						minInformationGainColumnIndex,
						(String) this.input
								.getAlgorithmParameter(ID3ParameterEnum.DATA_FIELD_SEPARATOR));

		root = new ID3Node(featureVector.get(currFeatureIndex), depth,
				rowIds.size());

		Map<String, Integer> featureRowCountDistribution = ID3AlgorithmUtil
				.getFetureRowValueCountDistribution(
						inputData,
						rowIds,
						targetColumnIndex,
						(String) this.input
								.getAlgorithmParameter(ID3ParameterEnum.DATA_FIELD_SEPARATOR));

		root.setTargetClassDistribution(featureRowCountDistribution);

		List<String> newFeatureVectors = new ArrayList<String>(featureVector);
		newFeatureVectors.remove(currFeatureIndex);

		ID3Node children = null;

		for (Map.Entry<String, List<Integer>> attributeRowIdsPair : featureRowDistribution
				.entrySet()) {

			children = createID3Tree(attributeRowIdsPair.getValue(),
					featureVector, targetColumnIndex, depth + 1);
			root.addChildrenNode(attributeRowIdsPair.getKey(), children);
			children.setParentNode(root);

		}

		return root;
	}

	private ID3Node childID3Node(List<String> inputData, List<Integer> rowIds,
			List<String> featureVector, int targetColumnIndex, int depth) {
		// get the class dsitibution for these row sets
		Map<String, Integer> featureRowDistribution = ID3AlgorithmUtil
				.getFetureRowValueCountDistribution(
						inputData,
						rowIds,
						targetColumnIndex,
						(String) this.input
								.getAlgorithmParameter(ID3ParameterEnum.DATA_FIELD_SEPARATOR));

		String featureName = featureVector.size() == 1 ? featureVector.get(0)
				: null;

		ID3Node childNode = new ID3Node(featureName, depth, rowIds.size());

		for (Map.Entry<String, Integer> attributeRowPair : featureRowDistribution
				.entrySet()) {
			childNode.addTargetClassDistribution(attributeRowPair.getKey(),
					attributeRowPair.getValue());
		}

		return childNode;
	}

	private boolean isStoppingCriteria(List<Integer> rowIds,
			List<String> featureVector, int depth) {
		boolean stoppingCriteria = featureVector.size() <= 1
				|| (this.depthCheckedAllowed
						.equals(ID3ParameterConstants.DEPTH_CHECK_REQUIRED_YES) && depth >= this.maxDepthAllowed);
		return stoppingCriteria;
	}
}
