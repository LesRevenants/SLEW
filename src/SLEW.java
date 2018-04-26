import TextStructure.CompoundWordBuilder;
import TextStructure.StructuredText;
import TextStructure.TextSequence;
import TextStructure.WordPatriciaTrie;
import Util.Pair;
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
import JDM.ExistingRelations;
import Relation.ExtractedRelation;
import Relation.RelationDB;
import Relation.RelationExtractor;
import Relation.RelationPatternReader;
import Relation.WikiArticleDB;
import RequeterRezo.RequeterRezo;

import Util.UtilColor;

public class SLEW {

	public SLEW(){

	}

	
	/**
	 *
	 * @param dataExtractor
	 * @param patternPath
	 * @param jdmMcPath
	 * @param sources_file_path
	 * @param verbose
	 */
	public void run(DataExtractor dataExtractor, String patternPath, String jdmMcPath, String sources_file_path,boolean verbose, boolean isFile, boolean use_db) {

		RelationPatternReader relationPatternReader = buildPatterns(patternPath);
		CompoundWordBuilder compoundWordBuilder = buildCWB(jdmMcPath, relationPatternReader);
		RequeterRezo system_query=buildRequeterRezo();
		RelationDB relationDB=null;
		WikiArticleDB wikiArticleDB=null;
		if(use_db){
			relationDB=new RelationDB("jdbc:mysql://venus/rcolin", "rcolin", "mysqlpwd");
			wikiArticleDB=new WikiArticleDB("jdbc:mysql://venus/rcolin", "rcolin", "mysqlpwd");
		}


		System.out.println("Read data sources : ");
		long tStart = System.currentTimeMillis();

		try {

			Collection<ExtractedRelation> extractedRelations=new LinkedList<>();
			Collection<String> articlesName;
			if(isFile){
				articlesName=Files.readAllLines(Paths.get(sources_file_path));
			}
			else{
				articlesName=new LinkedList<>();
				articlesName.add(sources_file_path);
			}

			Collection<Pair<String,LinkedList<TextSequence>>> data_sources = dataExtractor.extractAll(articlesName,3);
			tStart=Utils.display_ellapsed_time(tStart,"\t");

			for(Pair<String,LinkedList<TextSequence>> data_src : data_sources) {
				String data_key=data_src.getLeft();
				System.out.println("Get Wikipedia page ["+ UtilColor.ANSI_GREEN+data_key+"]"+UtilColor.ANSI_RESET);

				LinkedList<TextSequence> sequences=data_src.getRight();
				System.out.println("Structuration du texte : ");
				StructuredText structuredText=new StructuredText(
						sequences,
						compoundWordBuilder,
						relationPatternReader.getCompoundWords());

				System.out.println("\tTextSize : "+structuredText.getTotal_size());
				tStart=Utils.display_ellapsed_time(tStart,"\t");

				if(verbose) {
					System.out.println(structuredText.toString());
				}
				System.out.println("Extraction des relations : \n");
				RelationExtractor relationExtractor = new RelationExtractor(
						structuredText,
						system_query,
						relationPatternReader
				);

				extractedRelations.addAll(extract(relationExtractor,relationDB,use_db,data_key));
				tStart=Utils.display_ellapsed_time(tStart,"");
				System.out.println();
			}
			exportInJSONFile(extractedRelations);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public Collection<ExtractedRelation> extract(RelationExtractor relationExtractor, RelationDB relationDB, boolean use_db,String article_name){
		ExistingRelations existingRelations=new ExistingRelations();
		ArrayList<ExtractedRelation> rex=new ArrayList<>();

		System.out.println("Relations extraites : \n");

		Collection<ExtractedRelation> expected_relations=null;
		if(use_db){
			expected_relations= relationDB.getRelationsFromArticle(article_name,true);
		}

		for(ExtractedRelation extractedRelation : relationExtractor.extract()) {
			String flags="";

			if(use_db){
				flags += expected_relations.contains(extractedRelation) ? UtilColor.ANSI_GREEN : UtilColor.ANSI_RED;
			}
			else{
				flags += UtilColor.ANSI_PURPLE;
			}
			flags += "[ANNOT]";
			//System.out.println("------------------------------MAIN CALL---------------------------------");
			flags += existingRelations.Requesting(extractedRelation) ? UtilColor.ANSI_GREEN : UtilColor.ANSI_RED;
			flags += "[JDM] "+UtilColor.ANSI_RESET;

			//System.out.println(extractedRelation);
			//System.out.println("Résulat test : "+existingRelations.Requesting(extractedRelation));
			System.out.println(flags+UtilColor.ANSI_YELLOW +extractedRelation.toString() +UtilColor.ANSI_RESET + extractedRelation.getContextAsStr());
			rex.add(extractedRelation);
		}
		//exportInJSONFile(rex);
		return rex;

	}



	/**
	 *
	 * @param patternPath
	 * @param jdmMcPath
	 * @param verbose
	 * @param relationPatternFactory
	 * @param compoundWordBuilder
	 */

	private RelationPatternReader buildPatterns(String patternPath) {
		long tStart = System.currentTimeMillis();
		System.out.println("Lecture des patrons :");
		RelationPatternReader relationPatternFactory = new RelationPatternReader(patternPath);
		Utils.display_ellapsed_time(tStart,"\t");
		return relationPatternFactory;
	}

	private CompoundWordBuilder buildCWB(String jdmMcPath,RelationPatternReader relationPatternReader) {
		long tStart = System.currentTimeMillis();
		System.out.println("Lecture des mots composés de JDM : ");
		CompoundWordBuilder compoundWordBuilder = new CompoundWordBuilder(jdmMcPath,true);
		compoundWordBuilder.addToTrie(relationPatternReader.getCompoundWords()); // add compound word from patterns into compound word dictionary
		Utils.display_ellapsed_time(tStart,"\t");
		return compoundWordBuilder;
	}

	private RequeterRezo buildRequeterRezo() {
		long tStart = System.currentTimeMillis();
		System.out.println("Initialisation du moteur requeterRezo :");
		RequeterRezo system_query = new RequeterRezo("72h",100000);
		Utils.display_ellapsed_time(tStart,"\t");
		return system_query;
	}

	private void exportInJSONFile(Collection<ExtractedRelation> rex) {

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("json.txt"), "utf-8"))) {
			writer.write("[");
			int rex_size=rex.size();
			int i=0;
			for(ExtractedRelation relex : rex) {
				writer.write("\n\t{ "
						+ "\n\t\t \"x \": \"" + relex.getObject() + "\","
						+ "\n\t\t \"y \": \"" + relex.getSubject() + "\","
						+ "\n\t\t \"relation_type\" : \"" + relex.getRelation_type() + "\","
						+ "\n\t\t \"predicate\" : \"" + relex.getLinguisticPattern() + "\"\n\t}");
						if(i != rex_size-1){
							writer.write(",");
						}
						i++;
			}
			writer.write("]");
		}
		catch (IOException e){
			e.printStackTrace();
		}

	}

}
