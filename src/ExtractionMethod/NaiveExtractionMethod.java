package ExtractionMethod;

import Relation.ExtractedRelation;
import Relation.RelationPattern;
import TextStructure.TextSequence;

public class NaiveExtractionMethod extends ExtractionMethod {

	@Override
	public ExtractedRelation extract(TextSequence textSequence, RelationPattern pattern, int pattern_word_idx) {

		int x_idx=pattern_word_idx-1;
		int y_idx=pattern_word_idx+1;
		if(textSequence.getWords().get(x_idx).equals(",") || textSequence.getWords().get(y_idx).equals(",")){
			return null; // virgule just before or after the pattern
		}
		int window = 6;
		
		String patternStr = textSequence.getWords().get(pattern_word_idx);
		String x_pos = textSequence.getWordsPositions().get(x_idx);
		String y_pos = textSequence.getWordsPositions().get(y_idx);
		
		int cpt = 0; 
		boolean found = false;
		//sujet
		while(cpt < window && !found){
			if(x_idx <0) {
				return null;
			}
			if(pattern.getSyntaxicContraint().getyConstraints().contains(x_pos)) found = true;
			else {
				x_idx--;
				if(x_idx >=0) {
					x_pos = textSequence.getWordsPositions().get(x_idx);
				}
			}
			cpt++;
		}
		
		if(!found) return null;
		else {
			found = false; cpt = 0;
			while(cpt < window && !found){
				if(x_idx <0 || y_idx >= textSequence.getWords().size() || textSequence.getWords().get(y_idx).equals(",")) {
					/*
					 * échec si pas de x/on sort de la textsequence/on rencotre une virgule
					 */
					return null;
				}
				if(pattern.getSyntaxicContraint().getxConstraints().contains(y_pos)) found = true;
				else {
					y_idx++;
					if(y_idx != textSequence.getWordsPositions().size()){
						y_pos = textSequence.getWordsPositions().get(y_idx);
					}

				}
				cpt++;
			}
		}
		String x=textSequence.getWords().get(x_idx);
		String y=textSequence.getWords().get(y_idx);
		ExtractedRelation extractedRelation=new ExtractedRelation(pattern,patternStr, x, y,textSequence,x_idx,y_idx);
		extractedRelation.setX_end(x_idx);
		extractedRelation.setY_idx(y_idx);
		extractedRelation.setLing_pattern_idx(pattern_word_idx);
		return extractedRelation;
	}

}
