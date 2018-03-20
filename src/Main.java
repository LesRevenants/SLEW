import TextStructure.CompoundWordBuilder;
import TextStructure.StructuredText;
import TextStructure.TextSequence;
import TextStructure.WordPatriciaTrie;
import lib.org.ardverk.collection.PatriciaTrie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import org.apache.commons.lang3.RandomStringUtils;

import DataExtraction.DataExtractor;
import DataExtraction.RawTextExtractor;
import DataExtraction.wikipedia.WikipediaDataExtractor;
import Relation.RelationExtractor;
import Relation.RelationPatternReader;
import RequeterRezo.RequeterRezo;

public class Main {

	
	private static void testMC() {
		long tStart = System.currentTimeMillis();
        CompoundWordBuilder compoundWordBuilder = new CompoundWordBuilder();
        /*compoundWordBuilder.read("jdm-mc.txt",false);
        compoundWordBuilder.write("jdm-mc.ser");*/
        compoundWordBuilder.read("datas/jdm-mc.ser",true);
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        System.out.println("Time1 : "+tDelta + "  ms");

        //String[] sentence = {"il","existe","une","balancoire","ainsi","qu'une","radiographie","du","thorax","verte","grâce","à","une","technique","d'imagerie","médicale"};
        String[] sentence = {"radiographie","du","thorax","grâce","à","une","technique","d'imagerie","médicale","radiographie","du","thorax"};
        ArrayList<String> sentenceAsList = new ArrayList<>(Arrays.asList(sentence));
        
        tStart = System.currentTimeMillis();
        for(int j=0 ;j<1; j++){
            for(int i=sentence.length ;i<=sentence.length;i++){
                System.out.println(
                		compoundWordBuilder.replaceSequence( new TextSequence(sentenceAsList.subList(0,i)),3)
                );
            }
        }
        tEnd = System.currentTimeMillis();
        tDelta = tEnd - tStart;
        System.out.println("Time2 : "+tDelta + "  ms");
	}
	
	private static void testWiki() {
		WikipediaDataExtractor wikipediaDataExtractor = new WikipediaDataExtractor("Marmotte");
		wikipediaDataExtractor.getTextSequences();
		/*for(TextSequence seq : wikipediaDataExtractor.getTextSequences()) {
			System.out.println(se);
		}*/
	}
	
	private static void testPatternWrite() {
		HashMap<String,String> relations = new HashMap<>();
		//relations.put("r_isa","datas/patterns/hypéromynie.txt");
		relations.put("r_holo","datas/patterns/Holonymie.txt");
		relations.put("r_cause","datas/patterns/Cause.txt");
		relations.put("r_effect","datas/patterns/Effet.txt");
		relations.put("r_isa","datas/patterns/Hypéromynie.txt");
		relations.put("r_mero","datas/patterns/Méronymie.txt");
		relations.put("r_has_part","datas/patterns/Possession.txt");
		RelationPatternReader factory = new RelationPatternReader("datas/patterns/patterns.json");
		//factory.merge(relations);
	}
	
	private static void testPatternRead() {
		long tStart = System.currentTimeMillis();
		RelationPatternReader factory = new RelationPatternReader("datas/patterns/patterns.json");
		factory.readPattern();
		long tEnd = System.currentTimeMillis();
		long   tDelta = tEnd - tStart;
	    System.out.println("testPatternRead() : "+tDelta + "  ms");
	}
	
	
	/*private static void TestTrieOrHashSet() throws Exception{
		
		
		HashSet<String> hashSet = new HashSet<>();
		PatriciaTrie<String, Boolean> patriciaTrie = new WordPatriciaTrie();
		org.trie4j.patricia.PatriciaTrie patriciaTrie2 = new org.trie4j.patricia.PatriciaTrie();
		 
		
		int n=0,kmin=4,kmax=12;
		Collection<String> data = new ArrayList<>(n);
		Random rand = new Random(48216515L);
		for(int i=0;i<n;i++) {
			int k=rand.nextInt(kmax)+kmin;
			data.add(RandomStringUtils.random(k));
		}
		
		long tStart = System.currentTimeMillis();
		//Files.readAllLines(Paths.get("datas/jdm-mc.ser")).stream() 
			data
			.forEach( word ->patriciaTrie.put(word,true));
		System.out.println(
				"PatricianTrie build time: "+(System.currentTimeMillis()-tStart +"ms")
				+"\n\tSize="+patriciaTrie.size());
		
		tStart = System.currentTimeMillis();
		//Files.readAllLines(Paths.get("datas/jdm-mc.ser")).stream() 
		data
		.forEach(word -> hashSet.add(word));
		 System.out.println("Hashet build time: "+(System.currentTimeMillis()-tStart +"ms")
				 +"\n\tSize="+hashSet.size());
		 
		tStart = System.currentTimeMillis();
		//Files.readAllLines(Paths.get("datas/jdm-mc.ser")).stream() 
			data
			.forEach(word -> patriciaTrie2.insert(word));
			 System.out.println("PatricianTrie2 build time: "+(System.currentTimeMillis()-tStart +"ms")
					 +"\n\tSize="+patriciaTrie2.size());

			 
			 
		System.out.println();
			 
			 
		 int nb_repeat = 5;
		 tStart = System.currentTimeMillis();
		 for(int i=0;i<nb_repeat;i++) {
			 hashSet.stream().forEach(word -> {
				 patriciaTrie.containsKey(word);
			 });
		 }
		 System.out.println("PatriciaTrie search time ("
				 +""+(patriciaTrie.size()*nb_repeat)+") "
				 +(System.currentTimeMillis()-tStart +"ms"));
		
		 
		 tStart = System.currentTimeMillis();
		 for(int i=0;i<nb_repeat;i++) {
			 patriciaTrie.keySet().forEach(word -> {
				 hashSet.contains(word);
			 });
		 }
		 System.out.println("Hashet search time: ("
				 +""+(hashSet.size()*nb_repeat)+") "
				 +(System.currentTimeMillis()-tStart +"ms"));
		  
		 tStart = System.currentTimeMillis();
		 for(int i=0;i<nb_repeat;i++) {
			 hashSet.stream().forEach(word -> {
				 hashSet.contains(word);
			 });
		 }
		 System.out.println("Hashet2 search time: ("
				 +""+(hashSet.size()*nb_repeat)+") "
				 +(System.currentTimeMillis()-tStart +"ms"));
		 
		  
		 tStart = System.currentTimeMillis();
		 for(int i=0;i<nb_repeat;i++) {
			 hashSet.stream().forEach(word -> {
				 patriciaTrie2.contains(word);
			 });
		 }
		 System.out.println("PatriciaTrie2 search time: ("
				 +""+(hashSet.size()*nb_repeat)+") "
				 +(System.currentTimeMillis()-tStart +"ms"));
		 
		 
		 System.out.println();
		 patriciaTrie.clear();
		 tStart = System.currentTimeMillis();
		 for(int i=0;i<nb_repeat;i++) {
			 hashSet.stream().forEach(word -> {
				 patriciaTrie.put("a"+word,true);
			 });
		 }
		 System.out.println("PatriciaTrie insert time: ("
				 +""+(patriciaTrie.size()*nb_repeat)+") "
				 +(System.currentTimeMillis()-tStart +"ms"));
		 
		 hashSet.clear();
		 tStart = System.currentTimeMillis();
		 for(int i=0;i<nb_repeat;i++) {
			 patriciaTrie.keySet().forEach(word -> {
				 hashSet.add("a"+word);
			 });
		 }
		 System.out.println("HashSet insert time: ("
				 +""+(hashSet.size()*nb_repeat)+") "
				 +(System.currentTimeMillis()-tStart +"ms"));
		
		 tStart = System.currentTimeMillis();
		 for(int i=0;i<nb_repeat;i++) {
			 hashSet.stream().forEach(word -> {
				 patriciaTrie2.insert("a"+word);
			 });
		 }
		 System.out.println("PatriciaTrie2 insert time: ("
				 +""+(patriciaTrie.size()*nb_repeat)+") "
				 +(System.currentTimeMillis()-tStart +"ms"));
		
		
		//fail();
	}*/
	
	private static void test() {
		
		
		long tStart = System.currentTimeMillis();
		String text = "Le requin baleine est un poisson cartilagineux, "
    			+ "seul membre du genre Rhincodon et seule espèce actuelle de la famille des Rhincodontidae."
    			+ "Le chamois est une espèce de "
    			+ "la sous-famille des Caprinés."
    			+ "Les marmottes (Marmota) forment un genre de mammifère fouisseur de l'ordre des rongeurs."
				+ "L'espèce la plus connue en Europe est la marmotte vivant dans les montagnes (Marmota marmota)."
				+ "En Amérique du Nord, la Marmota monax, appelée familièrement « siffleux » au Québec, constitue l'espèce la plus courante.";
		
		System.out.println("Lecture du texte... ");
		
		RelationPatternReader relationPatternFactory = new RelationPatternReader("datas/patterns/patterns.json");
		
		CompoundWordBuilder compoundWordBuilder = new CompoundWordBuilder("datas/jdm-mc.ser",true); 
		compoundWordBuilder.addToTrie(relationPatternFactory.getCompoundWords()); // add compound word from patterns into compound word dictionary
		//System.out.println(compoundWordBuilder.getTrie().hasPrefix("du_genre"));
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<1;i++) {
			sb.append(text);
		}
		System.out.println("Taille du texte : "+sb.length()+"\n");
		
		
		DataExtractor dataExtractor = new RawTextExtractor(sb.toString());
		StructuredText structuredText = new StructuredText(dataExtractor, compoundWordBuilder, relationPatternFactory.getCompoundWords());
		//actually is only patterns
		RequeterRezo system_query = new RequeterRezo("72h",100000);
		
		RelationExtractor relationExtractor = new RelationExtractor(structuredText, system_query,relationPatternFactory);
		
		
		System.out.println(structuredText.toString());
		
		System.out.println("Relations extraites : \n");
		relationExtractor.extract();	
		long tEnd = System.currentTimeMillis();
		long   tDelta = tEnd - tStart;
	    System.out.println("\nTemps d'éxécution : "+tDelta + "  ms");
       
	}

    public static void main(String[] args){
    	//testMC();
    	testWiki();
    	//testPatternWrite();
    	//testPatternRead();
    	//test();
    	/*try {
			TestTrieOrHashSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
}
