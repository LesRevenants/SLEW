package TextStructure;

import java.util.LinkedList;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 */
public class TextSequence {

    /** A list of each word into the sequence
     *  ex : "Le requin est un poisson" -> {Le,requin,est,un,poisson}
     */
    private LinkedList<String> words;


    public TextSequence() {
    	 this.words = new LinkedList<>();
    }
    
    public TextSequence(LinkedList<String> words) {
        this();
        this.words = words;
    }
    
    public TextSequence(Collection<String> words) {
        this();
        this.words.addAll(words);
    }

    public TextSequence(String[] words){
        this.words = new LinkedList<>();
        for(String word : words){
            this.words.add(word);
        }
    }

    
    public LinkedList<String> getWords() {
        return words;
    }

    public void setWords(LinkedList<String> words) {
        this.words = words;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(String word : words){
            sb.append(word + ", ");
        }
        return sb.toString();
    }
}
