/**
 * 
 */
package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sumit
 *
 */
public final class ID3AlgorithmUtil {

	/**
	 * 
	 * @param inputData
	 *            : list of un-splitted string separated by separator
	 * @param rowIds
	 *            : contains row id's starting from zero column index
	 * @param targetColumnIndex
	 *            : based on 1 column index not 0 based
	 * @param
	 * @return
	 */

	public static double computeEntropy(List<String> inputData,
			List<Integer> rowIds, int targetColumnIndex, String separator) {
		Map<String, Integer> classDistribution = new HashMap<>();
		double entropy = 0.0;
		String classLabel = null;
		// if we can avoid redundant data rows split operation somehow we can
		// improve performance.

		for (int rowid : rowIds) {
			classLabel = inputData.get(rowid).split(separator)[targetColumnIndex - 1];
			if (!classDistribution.containsKey(classLabel)) {
				classDistribution.put(classLabel, 1);
				continue;
			}
			classDistribution.put(classLabel,
					classDistribution.get(classLabel) + 1);
		}

		int noOfDataPoints = rowIds.size();
		double probability = 0.0;
		for (int countOfClass : classDistribution.values()) {
			probability = ((double)countOfClass) / noOfDataPoints;
			entropy = entropy - probability * Math.log(probability);
		}
		return entropy;
	}

	public static double computeInformationGain(double entropy,
			List<String> inputData, List<Integer> rowIds,
			int currentFeatureIndex, int targetColumnIndex, String separator) {
		Map<String, List<Integer>> fetureRowDistribution = getFetureRowIdsDistribution(
				inputData, rowIds, currentFeatureIndex, separator);
		double entropyPerValuesType = 0.0;
		double weightFactor = 0.0;
		for (List<Integer> rowIdsForValue : fetureRowDistribution.values()) {
			weightFactor = ((double)rowIdsForValue.size()) / rowIds.size();
			entropyPerValuesType = entropyPerValuesType
					+ (weightFactor * computeEntropy(inputData, rowIdsForValue,
							targetColumnIndex, separator));
		}

		return entropy - entropyPerValuesType;
	}

	/**
	 * 
	 * @param featureNames
	 * @param columnName
	 * @return Return the 1 based index for given column name
	 */
	public static int getColumnIndex(List<String> featureNames,
			String columnName) {
		int index = 0;
		int size = featureNames.size();
		for (String featureName : featureNames) {
			if (featureName.equals(columnName)) {
				break;
			}
			index++;
		}
		return index == size ? -1 : index + 1;
	}

	public static Map<String, List<Integer>> getFetureRowIdsDistribution(
			List<String> inputData, List<Integer> rowIds,
			int currentFeatureIndex, String separator) {
		Map<String, List<Integer>> classRowDistribution = new HashMap<>();
		String featureValue = null;
		for (int rowid : rowIds) {
			featureValue = inputData.get(rowid).split(separator)[currentFeatureIndex - 1];
			if (!classRowDistribution.containsKey(featureValue)) {
				classRowDistribution
						.put(featureValue, new ArrayList<Integer>());
			}
			classRowDistribution.get(featureValue).add(rowid);
		}
		return classRowDistribution;
	}

	public static Map<String, Integer> getFetureRowValueCountDistribution(
			List<String> inputData, List<Integer> rowIds,
			int currentFeatureIndex, String separator) {
		Map<String, Integer> classRowDistribution = new HashMap<>();
		String featureValue = null;
		for (int rowid : rowIds) {
			featureValue = inputData.get(rowid).split(separator)[currentFeatureIndex - 1];
			if (!classRowDistribution.containsKey(featureValue)) {
				classRowDistribution.put(featureValue, 0);
			}
			classRowDistribution.put(featureValue,
					classRowDistribution.get(featureValue) + 1);
		}
		return classRowDistribution;
	}
}
