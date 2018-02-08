import java.util.LinkedHashSet;

/**
 * 
 * Describe a extraction pattern 
 */

public class ExtractionPattern {
	
	/** the name of the relation (ex : r_isa || r_holo ) */
	private String relation_name;
	
	/** The list of pattern which identifiate a relation (ex : r_isa : {est un , est une} */
	private LinkedHashSet<String> patterns;
	
	
	public String getRelation() {
		return relation_name;
	}
	
	public LinkedHashSet<String> getPatterns() {
		return patterns;
	}
	
	public void addPattern(String pattern) {
		patterns.add(pattern);
	}
	
	public void removePattern(String pattern) {
		patterns.remove(pattern);
	}
	
	public ExtractionPattern(String relation) {
		super();
		this.relation_name = relation;
		this.patterns = new LinkedHashSet<>();
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder(relation_name + "  ");
		for(String pattern : patterns) {
			sb.append(": "+ pattern );
		}
		return sb.toString();
	}
	

}
