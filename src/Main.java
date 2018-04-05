import TextStructure.CompoundWordBuilder;
import TextStructure.StructuredText;
import TextStructure.TextSequence;
import TextStructure.WordPatriciaTrie;
import Util.Utils;
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

import org.apache.commons.io.FileUtils;

//import org.apache.commons.lang3.RandomStringUtils;

import DataExtraction.DataExtractor;
import DataExtraction.RawTextExtractor;
import DataExtraction.wikipedia.WikipediaDataExtractor;
import Relation.ExtractedRelation;
import Relation.RelationDB;
import Relation.RelationExtractor;
import Relation.RelationPatternReader;
import Relation.WikiArticleDB;
import RequeterRezo.RequeterRezo;

public class Main {


	
	private static void test() {
		
		String text = "Le requin baleine est un poisson cartilagineux, "
    			+ "seul membre du genre Rhincodon et seule espèce actuelle de la famille des Rhincodontidae."
    			+ "Le chamois est une espèce de "
    			+ "la sous-famille des Caprinés."
    			+ "Les marmottes (Marmota) forment un genre de mammifère fouisseur de l'ordre des rongeurs."
				+ "L'espèce la plus connue en Europe est la marmotte vivant dans les montagnes (Marmota marmota)."
				+ "En Amérique du Nord, la Marmota monax, appelée familièrement « siffleux » au Québec, constitue l'espèce la plus courante.";
				      
	}

	public static void extract(){
		WikipediaDataExtractor wikipediaDataExtractor = new WikipediaDataExtractor();
    	SLEW slew=new SLEW();
    	slew.run(wikipediaDataExtractor,"datas/patterns/patterns.json","datas/jdm-mc.ser","datas/wiki_articles_id",true);
	}
	
	
    public static void main(String[] args){
    	extract();
    	/*if(args.length < 1){
    		System.err.println("Error bad argument number, use help" );
    		return;
    	}
    	switch(args[0]){
    		case "i":
    		case "re":extract();
    		case "help": {
    			//System.out.println("i : ");
    		}
    	}*/
      		
    		
    }
    
}
