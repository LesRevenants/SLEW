package TextStructure;

//import com.google.common.io.Files;



import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
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
     * @param textSequence : A collection of word
     * @param limitSize : The maximum size of a compound word which can be finded
     * @return The list of compound word from the sentence
     * Complexity :
     */
    public TextSequence getCompoundWordFrom(TextSequence textSequence, int limitSize){
        if(limitSize < 2){
            throw new IllegalArgumentException("Limit size of a compoundWord must be >= 2 : "+limitSize);
        }

        //TextSequence newTextSequence = new TextSequence();
        LinkedList<String> newTextSequence = new LinkedList<String>();
        int compoundWordSize = 0;
        String prefixWord = "";
        boolean new_prefix_check = false;
       
        for(String word : textSequence.getWords()){
            if(compoundWordSize > 0){
                if(compoundWordSize == limitSize){ // maximum size of compound word -> add it if exist into trie
                    if(trie.containsKey(prefixWord)){
                       newTextSequence.add(prefixWord);
                    }
                    new_prefix_check = true;
                    compoundWordSize = 0;
                }
                else{
                    String newPrefix = prefixWord.concat("_").concat(word);
                    if( trie.hasPrefix(newPrefix)){ // check if word with newPrefix as prefix exists
                        prefixWord = newPrefix;
                        compoundWordSize++;
                    }
                    else{
                        if(compoundWordSize > 1 && trie.containsKey(prefixWord)){ // add prefixWord if it exist into trie
                            newTextSequence.add(prefixWord);
                        }
                        else { //
                            for(String subword : prefixWord.split("_")){
                                newTextSequence.add(subword);
                            }
                        }
                        new_prefix_check = true;
                        compoundWordSize = 0;
                    }
                }
            }
            if(compoundWordSize == 0 || new_prefix_check){ // no compound word begin finded or
                if(trie.hasPrefix(word)){
                    compoundWordSize++;
                    prefixWord = word;
                }
                else{
                    newTextSequence.add(word);
                }
                new_prefix_check = false;
            }
        }

        
        if(compoundWordSize > 1  && compoundWordSize <= limitSize && trie.containsKey(prefixWord) ){
            newTextSequence.add(prefixWord);
        }
       /* textSequence.getWords().forEach(word -> System.out.print(word + ","));
        System.out.println("  :  ");*/
        /*sentenceCompoundWords.forEach(word -> System.out.println("\t"+word));
        System.out.println("New sequnce");*/
        //newTextSequence.forEach(word -> System.out.println("\t"+word));
        
        return new TextSequence(newTextSequence);
    }

    public LinkedList<String> getCompoundWordFrom2(TextSequence textSequence, int limitSize){
        if(limitSize < 2){
            throw new IllegalArgumentException("Limit size of a compoundWord must be >= 2 : "+limitSize);
        }

        LinkedList<String> newTextSequence = new LinkedList<>();
        int compoundWordSize = 0;
        String prefixWord = "";
        boolean new_prefix_check = false;
        LinkedList<LinkedList<Integer>> indexes = new LinkedList<>();

        for(String word : textSequence.getWords()){
            if(compoundWordSize > 0){
                if(compoundWordSize == limitSize){ // maximum size of compound word -> add it if exist into trie
                    if(trie.containsKey(prefixWord)){
                        newTextSequence.add(prefixWord);
                    }
                    new_prefix_check = true;
                    compoundWordSize = 0;
                }
                else{
                    String newPrefix = prefixWord.concat("_").concat(word);
                    if( trie.hasPrefix(newPrefix)){ // check if word with newPrefix as prefix exists
                        prefixWord = newPrefix;
                        compoundWordSize++;
                    }
                    else{
                        if(compoundWordSize > 1 && trie.containsKey(prefixWord)){ // add prefixWord if it exist into trie
                            newTextSequence.add(prefixWord);
                        }
                        else { //
                            for(String subword : prefixWord.split("_")){
                                newTextSequence.add(subword);
                            }
                        }
                        new_prefix_check = true;
                        compoundWordSize = 0;
                    }
                }
            }
            if(compoundWordSize == 0 || new_prefix_check){ // no compound word begin finded or
                if(trie.hasPrefix(word)){
                    compoundWordSize++;
                    prefixWord = word;
                }
                else{
                    newTextSequence.add(word);
                }
                new_prefix_check = false;
            }
        }
        //for(String word : textSequence.getWords()){

        //}
        if(compoundWordSize > 1 && trie.containsKey(prefixWord)){
            newTextSequence.add(prefixWord);
        }
        textSequence.getWords().forEach(word -> System.out.print(word + ","));
        System.out.println("  :  ");
        /*sentenceCompoundWords.forEach(word -> System.out.println("\t"+word));
        System.out.println("New sequnce");*/
        newTextSequence.forEach(word -> System.out.println("\t"+word));
        return newTextSequence;
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