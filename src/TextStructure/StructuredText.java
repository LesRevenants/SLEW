package TextStructure;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import DataExtraction.DataExtractor;

/**
 * 
 * @author user
 *
 */
public class StructuredText {

	private HashSet<String> patterns;
    private LinkedList<TextSequence> textSequences;
    private TextSequenceAnalyser textSequenceAnalyser;
    
  
	public StructuredText(DataExtractor dataExtractor, CompoundWordBuilder compoundWordBuilder, LinkedList<String> knownPatterns) {
		super();
		this.textSequences = new LinkedList<>();
		textSequenceAnalyser = TextSequenceAnalyser.getInstance();
		
		dataExtractor.getTextSequences().forEach( 
			textSequence -> {
				textSequences.add(compoundWordBuilder.replaceSequence(textSequence, 5));
			}
		);
		patterns = new HashSet<String>(knownPatterns);
		textSequences.forEach(textSequence -> {
			textSequence.setWordsGramPositions(textSequenceAnalyser.getPositionsOf(textSequence), patterns);
		});
		
		
	}


	public LinkedList<TextSequence> getTextSequences() {
		return textSequences;
	}


    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	textSequences.forEach(textSequence -> sb.append(textSequence.toString() + "\n"));
    	return sb.toString();
    }
 
}
