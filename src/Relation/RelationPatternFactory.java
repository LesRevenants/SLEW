package Relation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Read the pattern from a file
 * @author user
 *
 */
public class RelationPatternFactory {

	/**
	 * Path of the file which store relationPatterns
	 */
	private String filePath;

    public RelationPatternFactory(String filePath) {
		super();
		this.filePath = filePath;
	}


    /**
     * 
     * @return the list of relationPattern from the file
     */
	public LinkedList<RelationPattern> readPattern(){
    	LinkedList<RelationPattern> relationPatterns = new LinkedList<>();
    	try {
			JSONObject jsonObject = new JSONObject(FileUtils.readFileToString(new File(filePath),Charset.defaultCharset()));
		    for(String relationType :  jsonObject.keySet()) {
		    	JSONArray pattern_list = jsonObject.getJSONArray(relationType);
		    	//System.out.println(relationType);
		    	LinkedList<LinguisticPattern> linguisticPatterns = new LinkedList<>();
		    	for(Object pattern : pattern_list) {
		    		LinguisticPattern linguisticPattern = new LinguisticPattern( (String) pattern);
		    		linguisticPatterns.add(linguisticPattern);
		    	}
		    	relationPatterns.add(new RelationPattern(relationType, linguisticPatterns));
		    }
		    //relationPatterns.forEach(relationPattern -> System.out.println(relationPattern.toString()));
			return relationPatterns;
		    
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
    }
    
	/**
     * 
     * @return the list of relationPattern from the file and extract each pattern_str 
     * ex : { est_un, possède_un , }
     */
	public LinkedHashSet<String> readPatternAsString(){
		LinkedHashSet<String> relationPatternsStr = new LinkedHashSet<String>();
		 readPattern()
		 	.forEach(relationPattern -> {
				 relationPattern.getLinguisticPatterns().forEach(
						 linguisticPattern -> { 
							 relationPatternsStr.add(linguisticPattern.getPattern()); 
						 }
				);
		 	});
		 return relationPatternsStr;
	}
	
	
	/**
	 * Extract the compound word from the list of relationPattern
	 * @return
	 */
	public LinkedList<String> getCompoundWords(){
		return readPatternAsString()
				.stream()
				//.filter(Objects::nonNull)
				.filter(word -> word.contains("_"))
				.collect(Collectors.toCollection(LinkedList::new));
	}
    
    /**
     * Merge different pattern file into {@link filePath}
     * @param relations
     */
    public void merge(Map<String,String> relations) {
    	JSONObject obj = new JSONObject();
    	relations.forEach( (relationType,relationFile) -> {
    		JSONArray pattern_list = new JSONArray();
    		Collection<String> patterns = getPatternFromFile(relationFile);
    		for(String pattern : patterns ) {
    			pattern_list.put(pattern);
    		}
    		obj.put(relationType,pattern_list);
    	});
    	
    	try {
    		FileWriter fileWriter = new FileWriter(filePath);
    		fileWriter.write(obj.toString());
    		fileWriter.close();
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * extract all pattern from a file 
     * @param filePath
     * @return
     */
    private  Collection<String> getPatternFromFile(String filePath){
    	 LinkedList<String> patterns = new LinkedList<>();
    	 try {
			Files.readAllLines(Paths.get(filePath))
			 .forEach(word -> patterns.add(word));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return patterns;
    }

}
