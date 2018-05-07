package ExtractionMethod;

import Relation.ExtractedRelation;
import Relation.RelationPattern;
import TextStructure.TextSequence;

/**
 * Abstract ExtractionMethod use by @see RelationExtractor
 */
public abstract class ExtractionMethod {

	/**
	 * Extract a relation x,r,y from a textSequence T , according to a pattern p with p.lingustic pattern==r
	 * ex (le chat est_un félin omnivore, est_un, 2) -> chat,est_un,félin
	 * @param textSequence : the textSequence to read
	 * @param pattern : the pattern find
	 * @param pattern_word_idx : the idx of pattern into @textSequence.words
	 * @return the extractedRelation if find, null else
	 */
	public abstract ExtractedRelation extract(TextSequence textSequence, RelationPattern pattern, int pattern_word_idx);

	
}
