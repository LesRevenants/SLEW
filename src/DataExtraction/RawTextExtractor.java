
package DataExtraction;

import DataExtraction.DataExtractor;
import TextStructure.TextSequence;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 *  
 * Extract TextSequence from raw text
 */
public class RawTextExtractor implements DataExtractor {

	/**
	 *
	 */
	private String text;

	/**
	 *
	 * @param text
	 */
	public RawTextExtractor(String text) {
		super();
		this.text = text;
	}


	@Override
	public LinkedList<TextSequence> getTextSequences() {
		LinkedList<TextSequence> wordSequences = new LinkedList<>();
		String[] sequences = text.split("\\.");
		for(String seq : sequences) {
			String[] sentence =  seq.split(" ");
			wordSequences.add(new TextSequence(sentence));
		}
		return wordSequences;
	}
}
