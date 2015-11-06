/**
 * 
 */
package api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sumit
 * @version 1.0
 * 
 */
public class FileReaderUtil {

	public static List<String> readAllLines(String fileName) throws IOException {
		List<String> lines = null;
		try(BufferedReader bfrdRdr=new BufferedReader(new FileReader(fileName));){
			lines = new ArrayList<>();
			String text = null;
			while ((text = bfrdRdr.readLine() )!= null) {
				lines.add(text);
			}
		}
		return lines;
	}
}
