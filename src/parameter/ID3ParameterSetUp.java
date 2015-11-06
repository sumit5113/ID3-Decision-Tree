/**
 * 
 */
package parameter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parameter.ID3ParameterConstants.DATA_SOURCE_TYPE;
import parameter.ID3ParameterConstants.ID3ParameterEnum;
import api.FileReaderUtil;

/**
 * 
 * @author sumit
 *
 */
public class ID3ParameterSetUp implements
		IParameterSetUpExecutor<IAlgorithmParameter<ID3ParameterEnum, Object>> {

	/**
	 * 1st parameter[Mandatory] : file name or data source location. 2nd
	 * Parameter [Mandatory] : is DATA_SOURCE_TYPE = {File, DB, etc}, 3rd
	 * parameter [Mandatory] : Data Separator depends upon 2nd parameter.
	 * parameter[optional default = 'N'] : allowed maximum depth check flag 'Y'
	 * or 'N'. 4th parameter[optional default = -1] : if above parameter is 'Y'
	 * , then this value is considered.
	 * 
	 * @throws IOException
	 */
	@Override
	public IAlgorithmParameter<ID3ParameterEnum, Object> createAlgorithmParameter(
			Object... optionalParameters) throws IOException {
		IAlgorithmParameter<ID3ParameterEnum, Object> id3Parameter = new ID3AlgorithmParameter();
		if (optionalParameters != null) {
			String dataSourceURL = (String) optionalParameters[0];
			id3Parameter.setAlgorithmParameter(
					ID3ParameterEnum.DATA_SOURCE_URL, dataSourceURL);

			DATA_SOURCE_TYPE data_source_type = (DATA_SOURCE_TYPE) optionalParameters[1];
			String fieldSeparator = (String) optionalParameters[2];
			id3Parameter.setAlgorithmParameter(
					ID3ParameterEnum.DATA_FIELD_SEPARATOR, fieldSeparator);
			String yes_no = "N";
			if (optionalParameters.length >= 4) {
				yes_no = ((String) optionalParameters[3]).toUpperCase();
				yes_no = yes_no.startsWith("Y") ? "Y" : "N";

				if (yes_no.startsWith("Y") && optionalParameters.length > 4) {
					id3Parameter.setAlgorithmParameter(
							ID3ParameterEnum.ALLOWED_MAX_DEPTH,
							(Integer) optionalParameters[4]);
				}
			}

			id3Parameter.setAlgorithmParameter(
					ID3ParameterEnum.MAX_DEPTH_CHECK_REQUIRED, yes_no);

			// read data set
			List<String> dataSets = readData(dataSourceURL, data_source_type,
					fieldSeparator);
			id3Parameter.setAlgorithmParameter(
					ID3ParameterEnum.INPUT_DATA_SETS, dataSets);
			// we don't have any header for our current data set, so we will
			// create the headers, as column_<id>
			id3Parameter.setAlgorithmParameter(
					ID3ParameterEnum.FEATURE_VETOR_NAMES,
					getFeatureVectors(data_source_type, fieldSeparator,
							dataSets.get(0), false));
		}
		return id3Parameter;
	}

	private Map<String, Integer> getFeatureVectors(
			DATA_SOURCE_TYPE data_source_type,
			Object... dataSourceMetaInformation) {
		Map<String, Integer> featureVectors = new HashMap<>();
		switch (data_source_type) {
		case FILE:
			// for time being we have data set that is separated by ,
			String fieldSeparator = (String) dataSourceMetaInformation[0];
			String[] columnNames = ((String) dataSourceMetaInformation[1])
					.split(fieldSeparator);
			boolean isColumnHeaderPresent = (boolean) dataSourceMetaInformation[2];
			int numberOfColumns = columnNames.length;
			String columnName = null;
			for (int i = 1; i <= numberOfColumns; i++) {
				columnName = columnNames[i - 1];
				if (!isColumnHeaderPresent) {
					columnName = ID3ParameterConstants.DEFAULT_FEATURE_NAME_PREFIX
							+ i;
				}
				featureVectors.put(columnName, i);
			}
			break;
		case DB:
			break;
		}
		return featureVectors;
	}

	private List<String> readData(String dataSourceURL,
			DATA_SOURCE_TYPE data_source_type,
			Object... dataSourceMetaInformation) throws IOException {
		List<String> dataSets = null;
		switch (data_source_type) {
		case FILE:
			dataSets = FileReaderUtil.readAllLines(dataSourceURL);
			break;
		case DB:
			break;
		default:
			throw new IllegalArgumentException(
					"Data Source Type Not Supported!");
		}
		return dataSets;
	}
}
