/**
 * 
 */
package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import parameter.ID3ParameterConstants;
import api.FileReaderUtil;
import datastructure.ID3Node;

/**
 * @author sumit
 *
 */
public class TestID3TreeAccuracy {

	public static void testAccuracyTestData(TestID3BeanFileData bean,
			ID3Node root) throws IOException {
		List<String> testData = getTestData(bean.getFileUrl());
		if (testData.size() == 0) {
			System.out.println("Nothing to Test !!");
		}
		Map<String, Integer> featuredVectors = getFeatureVector(bean, testData
				.get(0).split(bean.getFieldSeperator()));
		// remove the test data 0th index element if, header is present
		if (bean.isHeaderPresent())
			testData.remove(0);

		computeAccuracyMatrix(bean.getFieldSeperator(), root, testData,
				featuredVectors, bean.getTargetColumnIndex());
	}

	private static void computeAccuracyMatrix(String fieldSeparator,
			ID3Node root, List<String> testData,
			Map<String, Integer> featuredVectors, int targetColumnIndex) {
		Map<String, Map<String, Integer>> confusionMatrix = new HashMap<String, Map<String, Integer>>();

		boolean matched = false;
		List<Integer> failedRowIds = new ArrayList<Integer>();
		int successCount = 0;
		int index = 0;
		String[] dataPoint = null;
		for (String data : testData) {
			index++;
			dataPoint = data.split(fieldSeparator);
			matched = accurayCheck(dataPoint, root, featuredVectors,
					targetColumnIndex, confusionMatrix);

			if (!matched) {
				failedRowIds.add(index);
				continue;
			}
			successCount++;
		}

		// printing here
		System.out.println("-------Accuracy Measure ---------");
		System.out.println("Total Number of examples tested: "
				+ testData.size());
		System.out.println("Number of accurate class label identified :"
				+ successCount);
		System.out.println("Accuarcy % :" + 100.00
				* ((double) successCount / testData.size()));
		//System.out.println(failedRowIds.size()+" "+failedRowIds);

		// print confusion matrix
		printConfusionMatrix(confusionMatrix);
	}

	private static void printConfusionMatrix(
			Map<String, Map<String, Integer>> confusionMatrix) {
		System.out.println("----------Confusion Matrix Table--------------");

		TreeSet<String> labelValues = new TreeSet<>(confusionMatrix.keySet());
		int i = 0;
		for (Map.Entry<String, Map<String, Integer>> x : confusionMatrix
				.entrySet()) {
			labelValues.addAll(x.getValue().keySet());
		}

		for (String x : labelValues) {
			System.out.print((char) ('a' + i) + "\t");
			i++;
		}

		System.out.println("<---classified as");

		i = 0;

		Map<String, Integer> labelConfusionMatrix = null;
		for (String label : labelValues) {
			labelConfusionMatrix = confusionMatrix.get(label);
			if (labelConfusionMatrix == null) {
				System.out.println("0");
				i++;
				continue;
			}
			for (String labelPrint : labelValues) {
				System.out.print(labelConfusionMatrix.getOrDefault(labelPrint,
						0) + "\t");
			}
			System.out.println("| " + ((char) ('a' + i)) + " = " + label);
			i++;
		}
	}

	private static boolean accurayCheck(String[] testData, ID3Node root,
			Map<String, Integer> featuredVectors, int targetColumnIndex,
			Map<String, Map<String, Integer>> confusionMatrix) {

		if (root == null)
			return false;

		String rootFeatureName = null;
		ID3Node lastRoot = null;

		do {
			rootFeatureName = root.getFetaureName();
			if (root.isLeafNode()) {
				boolean matched = isMatchingTargetValue(
						testData[targetColumnIndex - 1], root);
				updateConfusionMatrix(confusionMatrix,
						testData[targetColumnIndex - 1], root);
				return matched;
			}
			lastRoot = root;
			root = root.getChildNode(testData[featuredVectors
					.get(rootFeatureName) - 1]);
		} while (root != null);

		//if we reach here, we are unlucky to find any training value that has this test column value
		if (lastRoot != null) {
			updateConfusionMatrix(confusionMatrix,
					testData[targetColumnIndex - 1], lastRoot);
		}
		// System.out.println(root);
		// throw new
		// IllegalStateException("Algorithm should not reach here !!:"+Arrays.toString(testData));
		return false;
	}

	private static void updateConfusionMatrix(
			Map<String, Map<String, Integer>> confusionMatrix,
			String expectedTargetLabel, ID3Node root) {

		if (!confusionMatrix.containsKey(expectedTargetLabel)) {
			confusionMatrix.put(expectedTargetLabel,
					new HashMap<String, Integer>());
		}
		String obtainedTargetLabel = getProbableTargetLabel(root);

		if (!confusionMatrix.get(expectedTargetLabel).containsKey(
				obtainedTargetLabel)) {
			confusionMatrix.get(expectedTargetLabel)
					.put(obtainedTargetLabel, 0);
		}

		confusionMatrix.get(expectedTargetLabel).put(
				obtainedTargetLabel,
				confusionMatrix.get(expectedTargetLabel).get(
						obtainedTargetLabel) + 1);
	}

	private static boolean isMatchingTargetValue(String targetLabel,
			ID3Node root) {
		String probableTargetlabel = getProbableTargetLabel(root);
		return probableTargetlabel != null
				&& probableTargetlabel.equals(targetLabel);
	}

	/**
	 * @param root
	 * @return
	 */
	private static String getProbableTargetLabel(ID3Node root) {
		int maxAttributeCountValue = 0;
		String probableTargetlabel = null;
		for (Map.Entry<String, Integer> classCountValuePair : root
				.getTargetClassDistribution().entrySet()) {
			if (classCountValuePair.getValue() > maxAttributeCountValue) {
				maxAttributeCountValue = classCountValuePair.getValue();
				probableTargetlabel = classCountValuePair.getKey();
			}
		}
		return probableTargetlabel;
	}

	/**
	 * 
	 * @param bean
	 * @param pFeatureNames
	 * @return all features respective name without the target column name
	 */
	private static Map<String, Integer> getFeatureVector(
			TestID3BeanFileData bean, String[] pFeatureNames) {
		Map<String, Integer> featuresName = new HashMap<>();
		int size = pFeatureNames.length;
		String columnName = null;
		for (int i = 1; i <= size; i++) {
			// skip when there is no feature-header in the file
			if (bean.getTargetColumnIndex() == i && !bean.isHeaderPresent()) {
				continue;
			}
			columnName = pFeatureNames[i - 1];
			if (!bean.isHeaderPresent()) {
				columnName = ID3ParameterConstants.DEFAULT_FEATURE_NAME_PREFIX
						+ i;
			}
			featuresName.put(columnName, i);
		}

		return featuresName;
	}

	private static List<String> getTestData(String fileUrl) throws IOException {
		return FileReaderUtil.readAllLines(fileUrl);
	}
}
