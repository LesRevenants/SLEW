package TextStructure;

//import com.google.common.io.Files;



import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;


/**
 *
 */
public class CompoundWordBuilder {

	
	private String filePath;
	
    /**
     * A PatraiciaTrie which store the compound word list
     * Allow fast prefix retrieval
     * {@link WordPatriciaTrie}
     */
    private WordPatriciaTrie trie;

    /**
     * Flag which indicate if data have been read
     */
    private boolean dataLoaded;


    public CompoundWordBuilder(){
       trie = new WordPatriciaTrie();
       dataLoaded = false;
    }
    
    

    public CompoundWordBuilder(String filePath,boolean serialized) {
		this();
		this.filePath = filePath;
		read(filePath, serialized);
	}


    public TextSequence getCompoundWordFrom(LinkedList<String> textSequence, int limitSize){
    	return replaceSequence(new TextSequence(textSequence), limitSize);
    }

	
    
    /**
     * 
     * @param condition : the condition to check in order to add prefixWord as unique word 
     * @param newWords : the list of words which represent the new text sequence
     * @param prefixWord : the current prefixWord 
     * @param newTextSequenceReplacements : the current map< multipleWord, List<each word into multipleWord>>
     */
    private boolean addWordToTextSequence(boolean condition,LinkedList<String> newWords,String prefixWord,HashMap<String,ArrayList<String>> newTextSequenceReplacements) {
    	if(condition && trie.containsKey(prefixWord)){ 
            newWords.add(prefixWord);
            newTextSequenceReplacements.put(prefixWord,new ArrayList<String>( Arrays.asList(prefixWord.split("_"))));
            return true;
         }
    	else { //
            for(String subword : prefixWord.split("_")){ // the multiple word does not exist so add each part of multiple word
                newWords.add(subword);
            }
            return false;
        }
    }
    
    /**
     * @param oldTextSequence : A collection of word
     * @param limitSize : The maximum size of a compound word which can be finded
     * @return The list of compound word from the sentence
     * Complexity :
     */
      
     public TextSequence replaceSequence(TextSequence oldTextSequence, int limitSize){
        if(limitSize < 2){
            throw new IllegalArgumentException("Limit size of a compoundWord must be >= 2 : "+limitSize);
        }

        LinkedList<String> newWords = new LinkedList<String>();
        
        /**
         * The list of replacement of each multiple_word. Ex replace(est_un) -> {est,un}
         */
        HashMap<String,ArrayList<String>> newTextSequenceReplacements = new HashMap<>();
        build(oldTextSequence,"",0,0,limitSize,newWords,newTextSequenceReplacements);
        TextSequence textSequence = new TextSequence(newWords);
        textSequence.setWords_replacements(newTextSequenceReplacements);
        return textSequence;
        
    }
    
     public void build(TextSequence oldTextSequence,String prefixWord, int i, int compoundWordSize, int limitSize,
    		 LinkedList<String> newWords,
    		 HashMap<String,ArrayList<String>> newTextSequenceReplacements) {
    	 
    	 if(i==oldTextSequence.getWords().size()) {
    		 return;
    	 }
    	 String word=oldTextSequence.getWords().get(i);
    	 if(compoundWordSize == 0) {
    		 if(trie.hasPrefix(word)) {
    			 build(oldTextSequence,word,i+1,compoundWordSize+1,limitSize,newWords,newTextSequenceReplacements);
    		 }
    		 else {
    			 newWords.add(word);
    			 build(oldTextSequence,"",i+1,0,limitSize,newWords,newTextSequenceReplacements);
    		 }
    	 }
    	 else {
    		 if(compoundWordSize==limitSize) {
    			 if(trie.containsKey(prefixWord)) {
    				 newWords.add(prefixWord);
    		         newTextSequenceReplacements.put(prefixWord,new ArrayList<String>( Arrays.asList(prefixWord.split("_"))));
    		         build(oldTextSequence,"",i+1,0,limitSize,newWords,newTextSequenceReplacements);
    			 }
    			 else {
    				 for(String subword : prefixWord.split("_")){ // the multiple word does not exist so add each part of multiple word
                         newWords.add(subword);
                     }
    				 build(oldTextSequence,"",i+1,0,limitSize,newWords,newTextSequenceReplacements);
    			 }
    		 }
    		 else {
    			 String newPrefix = prefixWord.concat("_").concat(word);
                 if( trie.hasPrefix(newPrefix)){ // check if word with newPrefix as prefix exists     
                     build(oldTextSequence,newPrefix,i+1,compoundWordSize+1,limitSize,newWords,newTextSequenceReplacements);
                 }
                 else{ // no longer multiple-word found     
                	 if( (compoundWordSize > 1) ) {
                		 if(trie.containsKey(prefixWord)){  	// add the multiple_word if it exist
                			  newWords.add(prefixWord);
                              newTextSequenceReplacements.put(prefixWord,new ArrayList<String>( Arrays.asList(prefixWord.split("_"))));
                              build(oldTextSequence,"",i,0,limitSize,newWords,newTextSequenceReplacements);
                		 }
                		 else {
                			 newWords.add(oldTextSequence.getWords().get(i-compoundWordSize));
                			 build(oldTextSequence,"",i-compoundWordSize+1,0,limitSize,newWords,newTextSequenceReplacements);
                		 }
                			                  
                    }
                 	else { //
                         newWords.add(prefixWord);
                         build(oldTextSequence,"",i,0,limitSize,newWords,newTextSequenceReplacements);
                     }
                 }
    		 }
    		
    	 }
     }
    /**
     * Build the compound word list from a file
     * @param filePath : path of the file with store all compound words
     * @param serialized : If true -> read directly the formatted file , else read a non-serialized file and parse it
     * File reading is faster with serialized == true
     */
    public void read(String filePath,boolean serialized){
        try {
            if(!serialized){
                      Files.readAllLines(Paths.get(filePath))
                        .stream()
                        .map(word -> word.split(";")[1])
                        //.map(word -> word.replace(";",""))
                        .map(word -> word.replace(" ","_"))
                        .forEach(word -> trie.put(word,true));
            }
            else{
                Files.readAllLines(Paths.get(filePath))
                        .forEach( word -> trie.put(word,true));
            }
            dataLoaded = true;
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the trie into a formatted file
     * @param filePath : the file which sotre compound-word list
     */
    public void write(String filePath){
        if(! dataLoaded)
           throw new IllegalStateException("Attempting to write data before read them");
        try{
            FileWriter fileWriter = new FileWriter(filePath);
            trie.keySet()
                    .parallelStream()
                    .forEach(word ->writeToFile(fileWriter,word));
            fileWriter.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void writeToFile(FileWriter fileWriter, String content){
        try{
            fileWriter.write(content+"\n");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    
    
    public void addToTrie(Collection<String> compoundWords) {
    	compoundWords.forEach(word -> trie.put(word, true));
    }



	public WordPatriciaTrie getTrie() {
		return trie;
	}
    
    
}