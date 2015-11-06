/**
 * 
 */
package test;

import parameter.ID3ParameterConstants;

/**
 * @author sumit
 *
 */
public class TestID3BeanFileData {

	private String fileUrl;
	private String fieldSeperator = ID3ParameterConstants.COMMA;
	private int targetColumnIndex;
	private boolean headerPresent = false;
	private String targetColumnName;

	/**
	 * 
	 * @param pfileUrl
	 *            - location of the file path
	 * @param targetColumnIndex
	 *            - based on index 1, non zero index based column number of
	 *            target column
	 */
	public TestID3BeanFileData(String pfileUrl, int pTargetColumnIndex) {
		this.fileUrl = pfileUrl;
		this.targetColumnIndex = pTargetColumnIndex;
	}

	/**
	 * @return the fileUrl
	 */
	public String getFileUrl() {
		return fileUrl;
	}

	/**
	 * @return the fieldSeperator
	 */
	public String getFieldSeperator() {
		return fieldSeperator;
	}

	/**
	 * @param fieldSeperator
	 *            the fieldSeperator to set
	 */
	public void setFieldSeperator(String fieldSeperator) {
		this.fieldSeperator = fieldSeperator;
	}

	/**
	 * @return the headerPresent
	 */
	public boolean isHeaderPresent() {
		return headerPresent;
	}

	/**
	 * @param headerPresent
	 *            the headerPresent to set
	 */
	public void setHeaderPresent(boolean headerPresent) {
		this.headerPresent = headerPresent;
	}

	/**
	 * @return the targetColumnIndex
	 */
	public int getTargetColumnIndex() {
		return targetColumnIndex;
	}

	/**
	 * @param targetColumnIndex
	 *            the targetColumnIndex to set
	 */
	public void setTargetColumnIndex(int targetColumnIndex) {
		this.targetColumnIndex = targetColumnIndex;
	}

	/**
	 * @return the targetColumnName
	 */
	public String getTargetColumnName() {
		return targetColumnName;
	}

	/**
	 * @param targetColumnName
	 *            the targetColumnName to set
	 */
	public void setTargetColumnName(String targetColumnName) {
		this.targetColumnName = targetColumnName;
	}

}
