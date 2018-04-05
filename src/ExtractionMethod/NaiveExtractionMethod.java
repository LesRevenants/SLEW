package ExtractionMethod;

import Relation.ExtractedRelation;
import Relation.RelationPattern;
import TextStructure.TextSequence;

public class NaiveExtractionMethod extends ExtractionMethod {

	@Override
	public ExtractedRelation extract(TextSequence textSequence, RelationPattern pattern, int pattern_word_idx) {
		int subject_idx=pattern_word_idx-1;
		int object_idx=pattern_word_idx+1;
		int window = 10;
		
		String patternStr = textSequence.getWords().get(pattern_word_idx);
		String objectGramPosition = textSequence.getWordsPositions().get(object_idx);
		String subjectGramPosition = textSequence.getWordsPositions().get(subject_idx);
		
		int cpt = 0; 
		boolean found = false;
		//sujet
		while(cpt < window && !found){
			if(subject_idx <0 || object_idx == textSequence.getWords().size()) {
				return null;
			}
			if(pattern.getSyntaxicContraint().getyConstraints().contains(subjectGramPosition)) found = true;
			else {
				subject_idx--;
				if(subject_idx >=0) {
					subjectGramPosition = textSequence.getWordsPositions().get(subject_idx);
				}
			}
			cpt++;
		}
		if(!found) return null;
		else {
			found = false; cpt = 0;
			while(cpt < window && !found){
				if(subject_idx <0 || object_idx >= textSequence.getWords().size()) {
					return null;
				}
				if(pattern.getSyntaxicContraint().getxConstraints().contains(objectGramPosition)) found = true;
				else {
					object_idx++;
					if(object_idx != textSequence.getWordsPositions().size()){
						objectGramPosition = textSequence.getWordsPositions().get(object_idx);
					}

				}
				cpt++;
			}
		}
		
		String object=textSequence.getWords().get(object_idx);
		String subject=textSequence.getWords().get(subject_idx);
				
		return new ExtractedRelation(pattern.getRelationType(),patternStr, object, subject,textSequence,subject_idx,object_idx);
	}

}
