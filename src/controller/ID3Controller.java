/**
 * 
 */
package controller;

import java.io.IOException;

import parameter.IAlgorithmParameter;
import parameter.ID3ParameterConstants;
import parameter.ID3ParameterConstants.DATA_SOURCE_TYPE;
import parameter.ID3ParameterConstants.ID3ParameterEnum;
import parameter.ID3ParameterSetUp;
import report.ID3ReportGenerator;
import test.TestID3BeanFileData;
import test.TestID3TreeAccuracy;
import algorithm.IAlgorithm;
import algorithm.ID3Algorithm;
import datastructure.ID3Node;

/**
 * @author sumit
 *
 */
public class ID3Controller {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int targetColumnIndex = 16;
		// 1. create the parameter required by the ID3 algorithm
		IAlgorithmParameter<ID3ParameterEnum, Object> algoParameter = createParameters(targetColumnIndex);
		// 2. start algorithm and get the output of the algorithm
		IAlgorithm<IAlgorithmParameter<ID3ParameterEnum, Object>, IAlgorithmParameter<ID3ParameterEnum, Object>> id3Algorithm = new ID3Algorithm();
		algoParameter = id3Algorithm.run(algoParameter);

		// printing tree
		ID3ReportGenerator.generateReport(algoParameter);

		// accuracy testing
		testAccuracy(
				(ID3Node) algoParameter
						.getAlgorithmParameter(ID3ParameterEnum.OUTPUT_OF_ALGORITHM),
				targetColumnIndex);
	}

	private static void testAccuracy(ID3Node root, int targetColumnIndex)
			throws IOException {
		String testFileLocation = "L:/I526 Machine Learning/zoo-test_mod.csv";//"L:/I526 Machine Learning/sfo data mining/test_random_sample_1_1.csv";

		TestID3BeanFileData beanTest = new TestID3BeanFileData(
				testFileLocation, targetColumnIndex);
		beanTest.setFieldSeperator(ID3ParameterConstants.COMMA);
		beanTest.setHeaderPresent(false);
		beanTest.setTargetColumnName(ID3ParameterConstants.DEFAULT_FEATURE_NAME_PREFIX
				+ targetColumnIndex);

		TestID3TreeAccuracy.testAccuracyTestData(beanTest, root);

	}

	private static IAlgorithmParameter<ID3ParameterEnum, Object> createParameters(
			int targetColumnIndex) throws IOException {
		// 1. ask user for file name or data source location
		String dataUrl = "L:/I526 Machine Learning/zoo-train_mod.csv";//"L:/I526 Machine Learning/sfo data mining/train_random_sample_1_1.csv"
		// data source type
		DATA_SOURCE_TYPE data_source_type = DATA_SOURCE_TYPE.FILE;
		// filed separator
		String fieldSeparator = ID3ParameterConstants.COMMA;
		// . is depth check required
		String depthCheckRequired = ID3ParameterConstants.DEPTH_CHECK_REQUIRED_YES;
		// . the maximum depth allowed
		int maxDepthAllowed = 4;

		IAlgorithmParameter<ID3ParameterEnum, Object> algoParameter = new ID3ParameterSetUp()
				.createAlgorithmParameter(dataUrl, data_source_type,
						fieldSeparator, depthCheckRequired, maxDepthAllowed);

		// customize any parameters you want to modify, before returning
		// 1. Column_15 is my target column
		algoParameter.setAlgorithmParameter(
				ID3ParameterEnum.TARGET_FEATURE_NAME,
				ID3ParameterConstants.DEFAULT_FEATURE_NAME_PREFIX
						+ targetColumnIndex);

		return algoParameter;
	}
}
