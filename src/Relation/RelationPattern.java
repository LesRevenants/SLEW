package Relation;

import java.util.LinkedList;

public class RelationPattern {

    /** the name of the relation (ex : r_isa || r_holo )*/
    private String relationType;

    /** The list of pattern which identifiate a relation*/
    private LinkedList<LinguisticPattern> linguisticPatterns;
    
    private  SyntaxicContraint syntaxicContraint;

   
    
    
    /**
     * 
     * @param relationType
     * @param linguisticPatterns
     * @param syntaxicContraint
     */
	public RelationPattern(String relationType, LinkedList<LinguisticPattern> linguisticPatterns,
			SyntaxicContraint syntaxicContraint) {
		super();
		this.relationType = relationType;
		this.linguisticPatterns = linguisticPatterns;
		this.syntaxicContraint = syntaxicContraint;
	}



	public String getRelationType() {
		return relationType;
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
		sb.append(relationType);
		linguisticPatterns.forEach(pattern -> sb.append("\t"+pattern.toString()+"\n"));
		return sb.toString();
	}
	
	public String shortToString() {
		StringBuilder sb = new StringBuilder();
		sb.append(relationType + " : ");
		sb.append(syntaxicContraint.toString());
		return sb.toString();
	}
    
    


}
