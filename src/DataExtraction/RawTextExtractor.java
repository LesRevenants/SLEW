
package DataExtraction;

import DataExtraction.DataExtractor;
import TextStructure.TextSequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


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
		List<Character> punctuations = new ArrayList<>(Arrays.asList(';',':',','));
		String[] sequences = text.split("\\.");
		for(String seq : sequences) {
			String[] sentence =  seq.split(" ");
			ArrayList<String> wordList=new ArrayList(sentence.length); 
			for(String word:sentence) {
				if(word.length() != 1) {
					int punct_begin_idx=punctuations.indexOf(word.charAt(0));
					int punct_end_idx=punctuations.indexOf(word.charAt(word.length()-1));
					
					if(punct_begin_idx != -1) {
						wordList.add(""+punctuations.get(punct_begin_idx));
						wordList.add(word.substring(1, word.length()-2));
					}
					else if(punct_end_idx != -1) {
						wordList.add(word.substring(0, word.length()-1));
						wordList.add(""+punctuations.get(punct_end_idx));
					}
					else {
						wordList.add(word);
					}
				}
				else {
					wordList.add(word);
				}
				
			}
			wordSequences.add(new TextSequence(wordList));
			
		}
		return wordSequences;
	}
}
