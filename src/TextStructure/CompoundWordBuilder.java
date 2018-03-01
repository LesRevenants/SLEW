package TextStructure;

//import com.google.common.io.Files;



import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
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
    
    

    public CompoundWordBuilder(String filePath) {
		this();
		this.filePath = filePath;
	}


    public TextSequence getCompoundWordFrom(LinkedList<String> textSequence, int limitSize){
    	return getCompoundWordFrom(new TextSequence(textSequence), limitSize);
    }

	
    
    /**
     * 
     * @param condition : the condition to check in order to add prefixWord as unique word 
     * @param newWords : the list of words which represent the new text sequence
     * @param prefixWord : the current prefixWord 
     * @param newTextSequenceReplacements : the current map< multipleWord, List<each word into multipleWord>>
     */
    private boolean addWordToTextSequence(boolean condition,LinkedList<String> newWords,String prefixWord,HashMap<String,LinkedList<String>> newTextSequenceReplacements) {
    	if(condition && trie.containsKey(prefixWord)){ 
            newWords.add(prefixWord);
            newTextSequenceReplacements.put(prefixWord,new LinkedList<String>( Arrays.asList(prefixWord.split("_"))));
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
    public TextSequence getCompoundWordFrom(TextSequence oldTextSequence, int limitSize){
        if(limitSize < 2){
            throw new IllegalArgumentException("Limit size of a compoundWord must be >= 2 : "+limitSize);
        }

        LinkedList<String> newWords = new LinkedList<String>();
        HashMap<String,LinkedList<String>> newTextSequenceReplacements = new HashMap<>();
        int compoundWordSize = 0;
        String prefixWord = "";
        boolean new_prefix_check = false;
       
        for(String word : oldTextSequence.getWords()){
        	new_prefix_check = false;
            if(compoundWordSize > 0){ // if the begin of a multiple word was found
            	
                if(compoundWordSize == limitSize){ // maximum size of compound word -> add it 
                	if(! addWordToTextSequence(true,newWords, prefixWord, newTextSequenceReplacements)) {
                		// if the limit size of a multiple word is reached and if does not exist then 
                		new_prefix_check = true;
                	}
                    compoundWordSize = 0;
                }
                else{
                    String newPrefix = prefixWord.concat("_").concat(word);
                    if( trie.hasPrefix(newPrefix)){ // check if word with newPrefix as prefix exists
                        prefixWord = newPrefix;
                        compoundWordSize++;
                    }
                    else{ // no longer multiple-word found                   
                        addWordToTextSequence( (compoundWordSize > 1) ,newWords, prefixWord, newTextSequenceReplacements);
                        compoundWordSize = 0;
                    }
                }
            }
            if(compoundWordSize == 0 || new_prefix_check){    
                if(trie.hasPrefix(word)){
                    compoundWordSize++;
                    prefixWord = word;
                }
                else{
                    newWords.add(word);
                }
            }
        }
        // add last words to newTextSequence
        addWordToTextSequence( (compoundWordSize > 1  && compoundWordSize <= limitSize) ,newWords, prefixWord, newTextSequenceReplacements);
        TextSequence textSequence = new TextSequence(newWords);
        textSequence.setWords_replacements(newTextSequenceReplacements);
        return textSequence;
        
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
}