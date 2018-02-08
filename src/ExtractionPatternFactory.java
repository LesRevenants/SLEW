import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;


/**
 * 
 * Build and store a  list of pattern 
 */
public class ExtractionPatternFactory {

	/** The set of ExtractionPattern */ 
	private HashSet<ExtractionPattern> pattern_datas;

	public ExtractionPatternFactory(String file_path) {
		super();
		pattern_datas = new LinkedHashSet<>();
		read(file_path);
	}
	
	/**
	 * Read ExtractionPattern list from a file
	 * file format : 
	 * relation_name:pattern1:pattern2:pattern_n
	 * @param filePath : the path of the file
	 */
	private void read(String filePath) {
		try {
			FileReader fileReader = new FileReader(filePath);
			BufferedReader buffReader = new BufferedReader(fileReader);
			String line = null;
			while( (line = buffReader.readLine()) != null) {
				String[] fields = line.split(":");
				if(fields.length < 2) {
					return;
				}
				String pattern_name = fields[0];
				if(! pattern_datas.contains(pattern_name)) {
					ExtractionPattern new_pattern = new ExtractionPattern(pattern_name);
					for(int i=1 ;i<fields.length;i++) {
						new_pattern.addPattern(fields[i]);
					}
					pattern_datas.add(new_pattern);
				}
			}
			buffReader.close();
			fileReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public HashSet<ExtractionPattern> getPattern_datas() {
		return pattern_datas;
	}
	
	
	
}
