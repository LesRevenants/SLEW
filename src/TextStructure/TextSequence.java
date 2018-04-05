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


    
    private ArrayList<Integer> patternsIdx;




    public TextSequence() {
    	 words = new ArrayList<>();
    	 words_replacements = new HashMap<>();
    	 wordsGramPositions=new ArrayList<>();
    	 compoundWordPositions=new HashMap<>();
    	 patternsIdx=new ArrayList<>();
    }
    
    /*public TextSequence(ArrayList<String> words) {
        this();
        this.words = words;
    }*/


	public ArrayList<String> getWordsGramPositions() {
		return wordsGramPositions;
	}

	public boolean setWordsGramPositions(ArrayList<String> wordsGramPositions, HashSet<String> patterns) {
		/*if(words.size() != wordsGramPositions.size()) {
    		return false;
    	}*/
		wordsByGramPositions=new HashMap<>();
		int i=0;
		int offset=0;
		while(i<wordsGramPositions.size() && (i-offset) < words.size()) {
			//if(i-offset == words.size())
			String word = words.get(i-offset);
			if(isCompoundWord(word)) {
				int compoundWordSize = words_replacements.get(word).size();
				if(patterns.contains(word)) {
					this.wordsGramPositions.add("PAT");
					patternsIdx.add(i-offset);
				}
				else
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
		/*int indent = 90; int compound = 0;
        StringBuilder sb = new StringBuilder();
        for(int i=0 ;i<words.size(); i++) {
        	String word = words.get(i);
        	sb.append(i+":"+word);
        	ArrayList<String> word_replacement = words_replacements.get(word);
        	if(word_replacement != null){
        		sb.append(" : [");
        		compound = compound + 4;
        		for(int j=0;j<word_replacement.size();j++) {
        			sb.append(word_replacement.get(j)+"[");
        			compound += (word_replacement.get(j)+"[").length();
        			sb.append(compoundWordPositions.get(word).get(j)+"], ");
        			compound += (compoundWordPositions.get(word).get(j)+"], ").length();
        		}

        	
        		sb.append("]");
        		compound ++;
        	}
        	int temp = indent - word.length() - compound;
        	if(i<10){
        		for(int j = 0; j<temp; j++){
        			sb.append(" ");
        		}
        	} else {
        		for(int j = 0; j<temp-1; j++){
        			sb.append(" ");
        		}
        	}
        	sb.append("["+wordsGramPositions.get(i)+"]");
        	sb.append("\n");
        	compound =0;
        }
        return sb.toString();*/
		return "";
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

	public ArrayList<Integer> getPatternsIdx() {
		return patternsIdx;
	}
	
	
    
    
}

