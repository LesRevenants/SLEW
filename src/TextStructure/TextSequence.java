package TextStructure;



import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
    private ArrayList<String> wordsPositions;
    private HashMap<String,LinkedList<String>> words_replacements;


    public TextSequence() {
    	 this.words = new ArrayList<>();
    	 words_replacements = new HashMap<>();
    }
    
    /*public TextSequence(LinkedList<String> words) {
        this();
        this.words = words;
    }*/
    
    public void setWordsPositions(ArrayList<String> wordsPositions) {
		this.wordsPositions = wordsPositions;
	}

	public TextSequence(Collection<String> words) {
        this();
        this.words.addAll(words);
    }

    public TextSequence(String[] words){
    	this();
        for(String word : words){
            this.words.add(word);
        }
    }

    
    public ArrayList<String> getWords() {
        return words;
    }
    

    public void setWords_replacements(HashMap<String, LinkedList<String>> words_replacements) {
		this.words_replacements = words_replacements;
	}

	public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i=0 ;i<words.size(); i++) {
        	sb.append(words.get(i) + " \t\t ");
        	//sb.append(wordsPositions.get(i));
        	sb.append("\n");
        }
        /*words_replacements.forEach((k,v) -> {
        	sb.append(k + " : [");
        	v.forEach(sub_word -> sb.append(sub_word+","));
        	sb.append("]\n");
        });*/
        /*for(String word : words){
            sb.append(word + ", ");
        }*/
        return sb.toString();
    }

	public HashMap<String, LinkedList<String>> getWords_replacements() {
		return words_replacements;
	}
    
    
}

