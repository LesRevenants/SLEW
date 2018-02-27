package TextStructure;

import java.util.LinkedList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 *
 */
public class TextSequence {

    /** A list of each word into the sequence
     *  ex : "Le requin est un poisson" -> {Le,requin,est,un,poisson}
     */
    private LinkedHashSet<String> words;


    public TextSequence() {
    	 this.words = new LinkedHashSet<>();
    }
    
    /*public TextSequence(LinkedList<String> words) {
        this();
        this.words = words;
    }*/
    
    public TextSequence(Collection<String> words) {
        this();
        this.words.addAll(words);
    }

    public TextSequence(String[] words){
        this.words = new LinkedHashSet<>();
        for(String word : words){
            this.words.add(word);
        }
    }

    
    public LinkedHashSet<String> getWords() {
        return words;
    }

    public void setWords(LinkedHashSet<String> words) {
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
