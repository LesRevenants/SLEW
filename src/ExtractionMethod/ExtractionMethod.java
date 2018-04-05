package ExtractionMethod;

import Relation.ExtractedRelation;
import Relation.RelationPattern;
import TextStructure.TextSequence;

public abstract class ExtractionMethod {
	
	public abstract ExtractedRelation extract(TextSequence textSequence, RelationPattern pattern, int pattern_word_idx);

	
}
