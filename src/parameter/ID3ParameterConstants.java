package parameter;

public class ID3ParameterConstants {
	public enum ID3ParameterEnum {
		DATA_SOURCE_URL, // file or data base connection
		ALLOWED_MAX_DEPTH, // long type, should be check when
							// MAX_DEPTH_CHECK_REQUIRED is 'Y'
		OUTPUT_OF_ALGORITHM, // Decision Tree
		MAX_DEPTH_CHECK_REQUIRED, // Y or N value
		INPUT_DATA_SETS, // Set of all data items
		FEATURE_VETOR_NAMES, // Column header or feature vector name
		TARGET_FEATURE_NAME, // target feature name
		DATA_FIELD_SEPARATOR, // how feature attributes are separated.
	}

	public static final String DEFAULT_FEATURE_NAME_PREFIX = "Column_";
	public static final String DEPTH_CHECK_REQUIRED_YES = "Y";
	public static final String DEPTH_CHECK_REQUIRED_NO = "N";
	public static final String COMMA = ",";

	public enum DATA_SOURCE_TYPE {
		FILE, DB
	}

}
