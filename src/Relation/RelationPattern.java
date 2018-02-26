package Relation;

import java.util.LinkedList;

public class RelationPattern {

    /** the name of the relation (ex : r_isa || r_holo )*/
    String relationType;

    /** The list of pattern which identifiate a relation*/
    LinkedList<LinguisticPattern> linguisticPatterns;

    /**
     *
     * @param relationType
     * @param linguisticPatterns
     */
    public RelationPattern(String relationType, LinkedList<LinguisticPattern> linguisticPatterns) {
        this.relationType = relationType;
        this.linguisticPatterns = linguisticPatterns;
    }
    
    

	public String getRelationType() {
		return relationType;
	}



	public LinkedList<LinguisticPattern> getLinguisticPatterns() {
		return linguisticPatterns;
	}



	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(relationType);
		linguisticPatterns.forEach(pattern -> sb.append("\t"+pattern.toString()+"\n"));
		return sb.toString();
	}
    
    


}
