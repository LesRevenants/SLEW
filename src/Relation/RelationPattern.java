package Relation;

import java.util.LinkedList;

public class RelationPattern {

    /** the name of the relation (ex : r_isa || r_holo )*/
    private String relation_name;

    /** The list of pattern which identifiate a relation*/
    private LinkedList<LinguisticPattern> linguisticPatterns;
    
    private  SyntaxicContraint syntaxicContraint;

   
    
    
    /**
     * 
     * @param relation_name
     * @param linguisticPatterns
     * @param syntaxicContraint
     */
	public RelationPattern(String relation_name, LinkedList<LinguisticPattern> linguisticPatterns,
						   SyntaxicContraint syntaxicContraint) {
		super();
		this.relation_name = relation_name;
		this.linguisticPatterns = linguisticPatterns;
		this.syntaxicContraint = syntaxicContraint;
	}



	public String getRelation_name() {
		return relation_name;
	}


	public LinkedList<LinguisticPattern> getLinguisticPatterns() {
		return linguisticPatterns;
	}

	


	public SyntaxicContraint getSyntaxicContraint() {
		return syntaxicContraint;
	}



	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(relation_name);
		linguisticPatterns.forEach(pattern -> sb.append("\t"+pattern.toString()+"\n"));
		return sb.toString();
	}
	
	public String shortToString() {
		StringBuilder sb = new StringBuilder();
		sb.append(relation_name + " : ");
		sb.append(syntaxicContraint.toString());
		return sb.toString();
	}
    
    


}
