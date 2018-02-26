package TextStructure;


import java.util.LinkedList;

import DataExtraction.DataExtractor;

/**
 * 
 * @author user
 *
 */
public class StructuredText {

    private LinkedList<TextSequence> textSequences;
    
  
	public StructuredText(DataExtractor dataExtractor, CompoundWordBuilder compoundWordBuilder) {
		super();
		this.textSequences = new LinkedList<>();
		dataExtractor.getTextSequences().forEach( 
			textSequence -> {
				textSequences.add(compoundWordBuilder.getCompoundWordFrom(textSequence, 5));
			}
		);
	}


	public LinkedList<TextSequence> getTextSequences() {
		return textSequences;
	}


    
 
}
