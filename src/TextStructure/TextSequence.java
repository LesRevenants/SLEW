package TextStructure;



import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.LinkedList;

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
    
    /**
     * Key/Value associative array [ a_multiple_word -> {a,multiple_word} ]
     */
    private HashMap<String, LinkedList<String>> words_replacements; 
    
    private HashMap<String,LinkedList<String>> compoundWordPositions;
    
   


    public TextSequence() {
    	 words = new ArrayList<>();
    	 words_replacements = new HashMap<>();
    	 wordsGramPositions=new ArrayList<>();
    	 compoundWordPositions=new HashMap<>();
    }
    
    /*public TextSequence(LinkedList<String> words) {
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
		int i=0;
		int offset=0;
		while(i<wordsGramPositions.size()) {
			String word = words.get(i-offset);
			if(isCompoundWord(word)) {
				int compoundWordSize = words_replacements.get(word).size();
				this.wordsGramPositions.add("MWE");
				compoundWordPositions.put(word, new LinkedList<>(wordsGramPositions.subList(i, i+compoundWordSize)));
				i += compoundWordSize;
				offset += compoundWordSize-1;
			}
			else {
				String word_pos = wordsGramPositions.get(i);
				this.wordsGramPositions.add(word_pos);
				i++;
			}
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
    
   /* public void setWords_replacements(HashMap<String, LinkedList<String>> words_replacements) {
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

	public boolean setWords_replacements(HashMap<String, LinkedList<String>> words_replacements) {
		if(words.isEmpty())
			return false;
		this.words_replacements = words_replacements;
		return true;
	}

	public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i=0 ;i<words.size(); i++) {
        	String word = words.get(i);
        	sb.append(word);
        	if(words_replacements.get(word) != null){
        		sb.append(" : [");
        		words_replacements.get(word).forEach(v -> sb.append(v+","));
        		sb.append("]");
        	}
        	/*if(words_replacements.get(index)) {
        		sb.append(" : [");
        		words_replacements.get(word).forEach(v -> sb.append(v+","));
        		sb.append("]");
        	}*/
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

	public HashMap<String, LinkedList<String>> getWords_replacements() {
		return words_replacements;
	}
    
    
}

