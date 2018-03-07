package TextStructure;



import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

/**
 *
 */
public class TextSequence {

    /** A list of each word into the sequence
     *  ex : "Le requin est un poisson" -> {Le,requin,est,un,poisson}
     */
    private ArrayList<String> words;
    
    
    
    /**
     * The list of Grammatical positions of the words
     */
    private ArrayList<String> wordsGramPositions;
    
    private HashMap<String, ArrayList<Integer>> wordsByGramPositions;
    
    /**
     * Key/Value associative array [ a_multiple_word -> {a,multiple_word} ]
     */
    private HashMap<String, ArrayList<String>> words_replacements; 
    
    /**
     *  Key/Value associative array [ a_multiple_word -> { pos(a),pos(multiple),pos(word)}
     */
    private HashMap<String,ArrayList<String>> compoundWordPositions;
    
    
    /**
     * Array of index of the subSequence which contains a word
     * A begin of, some text 
     * two subSequence[0,2] and [4,5] with index 0 and 1 into @link{wordsSubSequence}
     * wordsSubSequenceIdx = {0,1}
     */
    private ArrayList<Integer> wordsSubSequenceIdx;
    
    /**
     * The list of subsequence into the sequence 
     * ex : A begin of, some text -> { subSequence({A begin of}) , subSequence({some text})
     */
    private ArrayList<SubSequence> wordsSubSequence;
    
    
    
   
    private void buildSubSequenceIdx() {
    	List<Character> punctuations = new ArrayList<>(Arrays.asList(';',':',','));
    	wordsSubSequenceIdx=new ArrayList<>(words.size());
    	wordsSubSequence=new ArrayList<>();
    	int currentSequenceIdx=0;
    	int currentSequenceSize=0;
    	
    	for(int i=0;i<words.size();i++) {
    		String word=words.get(i);
    		// if the word is punctuation
    		if( word.length() == 1 && punctuations.contains(word.charAt(0))) {
    			wordsSubSequence.add(new SubSequence(i-currentSequenceSize,i-1)); // new subsequence find
    			wordsSubSequenceIdx.add(-1); // the subsequence of punctuation is -1
    			currentSequenceIdx++;
    			currentSequenceSize=0;
    		}
    		else {
    			currentSequenceSize++;
    			wordsSubSequenceIdx.add(currentSequenceIdx); // define the subsequence idx of the word
    		}
    		
    	}
    	wordsSubSequence.add(new SubSequence(words.size()-currentSequenceSize,words.size()-1));
    	
	}

    public TextSequence() {
    	 words = new ArrayList<>();
    	 words_replacements = new HashMap<>();
    	 wordsGramPositions=new ArrayList<>();
    	 compoundWordPositions=new HashMap<>();
    }
    
    /*public TextSequence(ArrayList<String> words) {
        this();
        this.words = words;
    }*/


	public ArrayList<String> getWordsGramPositions() {
		return wordsGramPositions;
	}

	public boolean setWordsGramPositions(ArrayList<String> wordsGramPositions) {
		/*if(words.size() != wordsGramPositions.size()) {
    		return false;
    	}*/
		wordsByGramPositions=new HashMap<>();
		int i=0;
		int offset=0;
		while(i<wordsGramPositions.size()) {
			String word = words.get(i-offset);
			if(isCompoundWord(word)) {
				int compoundWordSize = words_replacements.get(word).size();
				this.wordsGramPositions.add("NOM");
				compoundWordPositions.put(word, new ArrayList<>(wordsGramPositions.subList(i, i+compoundWordSize)));
				i += compoundWordSize;
				offset += compoundWordSize-1;
			}
			else {
				String word_pos = wordsGramPositions.get(i);
				this.wordsGramPositions.add(word_pos);
				i++;
			}
			
		}
		
		for(i=0;i<words.size();i++) {
			String word_pos=this.wordsGramPositions.get(i);
			if(!wordsByGramPositions.containsKey(word_pos)) {
				wordsByGramPositions.put(word_pos, new ArrayList<>());
			}
			wordsByGramPositions.get(word_pos).add(i);
		}
    	return true;
	}
    

	public TextSequence(Collection<String> words) {
        this();
        this.words.addAll(words);
        buildSubSequenceIdx();
    }

	/**
	 * Read words and add into this.words
	 * @param words
	 * Complexity :  O(2*|words|) : O(|words|)
	 */
    public TextSequence(String[] words){
    	this();
        for(String word : words){
            this.words.add(word);
        }
        buildSubSequenceIdx();
    }
    
    
    public ArrayList<String> getWords() {
        return words;
    }
    
   /* public void setWords_replacements(HashMap<String, ArrayList<String>> words_replacements) {
		this.words_replacements = words_replacements;
	}*/
    
    public boolean isCompoundWord(String word) {
    	return words_replacements.containsKey(word);
    }
    

	public void setWords(ArrayList<String> words) {
        this.words = words;
        wordsGramPositions.clear();
        words_replacements.clear();    
    }

    public ArrayList<String> getWordsPositions() {
		return wordsGramPositions;
	}

	public boolean setWords_replacements(HashMap<String, ArrayList<String>> words_replacements) {
		if(words.isEmpty())
			return false;
		this.words_replacements = words_replacements;
		return true;
	}

	public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i=0 ;i<words.size(); i++) {
        	String word = words.get(i);
        	sb.append(i+":"+word);
        	ArrayList<String> word_replacement = words_replacements.get(word);
        	if(word_replacement != null){
        		sb.append(" : [");
        		for(int j=0;j<word_replacement.size();j++) {
        			sb.append(word_replacement.get(j)+"[");
        			sb.append(compoundWordPositions.get(word).get(j)+"], ");
        		}

        	
        		sb.append("]");
        	}
        	sb.append("		["+wordsGramPositions.get(i)+"]");
        	sb.append("[ss_idx="+wordsSubSequenceIdx.get(i)+"]");
        	sb.append("\n");
        }
        return sb.toString();
    }
    
    public String shortToString() {
    	StringBuilder sb = new StringBuilder();
        for(int i=0 ;i<words.size(); i++) {
        	sb.append(words.get(i));
        	sb.append("\n");
        }
        return sb.toString();
    }

	public HashMap<String, ArrayList<String>> getWords_replacements() {
		return words_replacements;
	}

	public ArrayList<SubSequence> getWordsSubSequenceIndex() {
		return wordsSubSequence;
	}
	
	public SubSequence getWordsSubSequenceIndex(int word_idx) {
		return wordsSubSequence.get(wordsSubSequenceIdx.get(word_idx));
	}
    
    
}

