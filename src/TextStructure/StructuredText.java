package TextStructure;


import java.util.*;

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
    
  
	public StructuredText(Collection<TextSequence>  inTextSequences, CompoundWordBuilder compoundWordBuilder, LinkedList<String> knownPatterns) {
		super();
		this.textSequences = new LinkedList<>();
		textSequenceAnalyser = TextSequenceAnalyser.getInstance();

		ArrayList<ArrayList<String>> compoundWordRules=new ArrayList<>();
		compoundWordRules.add(new ArrayList<>(Arrays.asList("NOM","ADJ")));
        compoundWordRules.add(new ArrayList<>(Arrays.asList("NOM","NOM")));
        compoundWordRules.add(new ArrayList<>(Arrays.asList("NOM","PRP","NOM")));
        compoundWordRules.add(new ArrayList<>(Arrays.asList("NOM","ADV","ADJ")));
        compoundWordRules.add(new ArrayList<>(Arrays.asList("NOM","ADJ","ADJ")));

        LinkedList<String> new_compound_words=new LinkedList<>();

		patterns = new HashSet<>(knownPatterns);
		inTextSequences.forEach(textSequence -> {

            ArrayList<String> positions = textSequenceAnalyser.getPositionsOf(textSequence);
            compoundWordRules.forEach(rule -> {
                int j=0;
                for(int i=0;i<positions.size();i++){
                    if(positions.get(i).equals(rule.get(j))){
                        j++;
                    }
                    else if(j>0){
                        j=0;
                    }
                    if(j==rule.size()){
                        StringBuilder new_compound_word = new StringBuilder();
                        for(int k=i-j+1;k<i;k++){
                            new_compound_word.append(textSequence.getWords().get(k)+"_");
                        }
                        new_compound_word.append(textSequence.getWords().get(i));
                        new_compound_words.add(new_compound_word.toString());
                        j=0;
                    }
                }
            });

            compoundWordBuilder.addToTrie(new_compound_words);
			TextSequence newTextSequence=compoundWordBuilder.replaceSequence(textSequence, 5);
			newTextSequence.setWordsGramPositions(positions, patterns);
			this.textSequences.add(newTextSequence);
		});

	}


	public LinkedList<TextSequence> getTextSequences() {
		return textSequences;
	}


    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	textSequences.forEach(textSequence -> {
    	    sb.append(textSequence.toString());
            sb.append("\n");
        });
    	return sb.toString();
    }
 
}
