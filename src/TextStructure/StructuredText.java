package TextStructure;


import java.util.ArrayList;
import java.util.LinkedList;

import DataExtraction.DataExtractor;

/**
 * 
 * @author user
 *
 */
public class StructuredText {

    private LinkedList<TextSequence> textSequences;
    private TextSequenceAnalyser textSequenceAnalyser;
    
  
	public StructuredText(DataExtractor dataExtractor, CompoundWordBuilder compoundWordBuilder) {
		super();
		this.textSequences = new LinkedList<>();
		textSequenceAnalyser = TextSequenceAnalyser.getInstance();
		
		dataExtractor.getTextSequences().forEach( 
			textSequence -> {
				textSequences.add(compoundWordBuilder.replaceSequence(textSequence, 5));
			}
		);
		
		textSequences.forEach(textSequence -> {
			textSequence.setWordsGramPositions(textSequenceAnalyser.getPositionsOf(textSequence));
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
