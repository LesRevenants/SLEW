
package DataExtraction;

import DataExtraction.DataExtractor;
import TextStructure.TextSequence;

import java.util.*;
import java.util.regex.Pattern;


/**
 *  
 * Extract TextSequence from raw text
 */
public class RawTextExtractor implements DataExtractor {

	public RawTextExtractor() {
		super();
	}

	@Override
	public LinkedList<TextSequence> getTextSequences(String text) {
		LinkedList<TextSequence> wordSequences = new LinkedList<>();
		List<Character> punctuations = new ArrayList<>(Arrays.asList(';',':',','));
		String[] sequences = text.split("[\\.\n]"); // split text into sentences 
		for(String seq : sequences ) {
			if(! seq.isEmpty()) {
				String[] sentence =  seq.split("\\s+"); // split sentence into words
				ArrayList<String> wordList=new ArrayList<String>(sentence.length); 
				for(String word:sentence) {
					if(! word.isEmpty()) {
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
					
					
				}
				wordSequences.add(new TextSequence(wordList));
			}
				
		}
		return wordSequences;
	}

	@Override
	public Collection<LinkedList<TextSequence>> extractAll(Collection<String> data_sources, int limit) {
		return null;
	}
}
