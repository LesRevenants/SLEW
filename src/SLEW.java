import TextStructure.CompoundWordBuilder;
import TextStructure.StructuredText;
import TextStructure.TextSequence;
import Util.Pair;
import Util.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//import org.apache.commons.lang3.RandomStringUtils;

import DataExtraction.DataExtractor;
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
	 */
	public void run(DataExtractor dataExtractor, String patternPath, String jdmMcPath, String sources_file_path, String outFile, boolean isFile,Properties properties) {

		RelationPatternReader relationPatternReader = buildPatterns(patternPath);
		CompoundWordBuilder compoundWordBuilder = buildCWB(jdmMcPath, relationPatternReader);
		RequeterRezo system_query=buildRequeterRezo();
		
		RelationDB relationDB=null;
		WikiArticleDB wikiArticleDB=null;
		boolean use_db = properties.containsKey("use_db") && properties.getProperty("use_db").equals("true");
		boolean verbose = properties.containsKey("verbose") && properties.getProperty("verbose").equals("true");
		boolean valid = properties.containsKey("valid") && properties.getProperty("valid").equals("true");

		System.out.println("Running with properties : "+properties.toString());

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
				articlesName=Files.readAllLines(Paths.get(sources_file_path),StandardCharsets.ISO_8859_1);
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
				System.out.println("Text structural : ");
				StructuredText structuredText=new StructuredText(
						sequences,
						compoundWordBuilder,
						relationPatternReader.getCompoundWords(),properties);

				System.out.println("\tTextSize : "+structuredText.getTotal_size());
				tStart=Utils.display_ellapsed_time(tStart,"\t");


				if(verbose) {
					//System.out.println(structuredText.toString());
				}
				System.out.println("Relation extraction : \n");
				RelationExtractor relationExtractor = new RelationExtractor(
						structuredText,
						system_query,
						relationPatternReader
				);


				extractedRelations.addAll(extract(relationExtractor,relationDB,use_db,data_key,system_query,valid));
				tStart=Utils.display_ellapsed_time(tStart,"");
				System.out.println();
			}

			System.out.println("Extraction results written on "+outFile);
			exportInJSONFile(extractedRelations,outFile);
			system_query.sauvegarder();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public Collection<ExtractedRelation> extract(RelationExtractor relationExtractor, RelationDB relationDB,
												 boolean use_db,String article_name,RequeterRezo system_query,
												 boolean valid){
		ExistingRelations existingRelations=new ExistingRelations();
		ArrayList<ExtractedRelation> rex=new ArrayList<>();

		int indent_size=100;
		Collection<ExtractedRelation> expected_relations=null;
		/*if(use_db){
			expected_relations= relationDB.getRelationsFromArticle(article_name,true);
		}*/

		Collection<ExtractedRelation> extractedRelations = relationExtractor.extract();
		ArrayList<String> displays=new ArrayList<>();

		for(ExtractedRelation extractedRelation : extractedRelations) {
			StringBuilder flags=new StringBuilder();
			flags.append(existingRelations.Requesting(extractedRelation,system_query) ? UtilColor.ANSI_GREEN : UtilColor.ANSI_RED);
			flags.append("[JDM] "+UtilColor.ANSI_RESET);

			//System.out.println(extractedRelation);
			//System.out.println("Résulat test : "+existingRelations.Requesting(extractedRelation));
			flags.append(UtilColor.ANSI_WHITE +extractedRelation.toString());
			flags.append(UtilColor.ANSI_RESET);
			for(int i=0;i<indent_size-flags.length();i++){
				flags.append(" ");
			}
			flags.append("["+extractedRelation.getContextAsStr()+"]");
			System.out.println(flags);
			displays.add(flags.toString());
			rex.add(extractedRelation);
		}

		if(valid){
			Scanner in_sc=new Scanner(System.in);
			System.out.println(); System.out.println();
			ArrayList<Integer> rex_valid=new ArrayList<>(extractedRelations.size());
			for(int i=0;i<extractedRelations.size();i++){
				System.out.println((displays.get(i)));
				System.out.println("Correct ? Key Y/N/Q (yes/no/quit) :");

				boolean _continue=false;
				while (! _continue){
					String choice = in_sc.nextLine();
					if(choice.toUpperCase().equals("Y")){
						rex_valid.add(1);
						_continue = true;
					}
					else if(choice.toUpperCase().equals("N")){
						rex_valid.add(0);
						_continue = true;
					}
					else if(choice.toUpperCase().equals("Q")){
						return rex;
					}
					else{
						System.err.println("RTFM : !!!! Key Y/N/Q (yes/no/quit");
					}
				}
			}
			long nb_valid= rex_valid.stream().filter(is_valid -> is_valid == 1).count();
			System.out.println(nb_valid  + " : "+rex_valid.size());
			float precision= (float) nb_valid/(float)rex_valid.size();
			System.out.println("Precision : "+precision);
		}

		//exportInJSONFile(rex,);
		return rex;

	}



	/**
	 *
	 * @param patternPath
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

	private void exportInJSONFile(Collection<ExtractedRelation> rex,String outFile) {

		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(outFile), "utf-8"))) {
			writer.write("[");
			int rex_size=rex.size();
			int i=0;
			for(ExtractedRelation relex : rex) {
				writer.write(
						"\n\t{ "
							+ "\n\t\t \"x \": \"" + relex.getX() + "\","
							+ "\n\t\t \"y \": \"" + relex.getY() + "\","
							+ "\n\t\t \"relation_type\" : \"" + relex.getRelation_type().getRelation_name() +"\","
							+ "\n\t\t \"predicate\" : \"" + relex.getLinguisticPattern() +"\","
							//+ "\n\t\t \"context\" : \"" + relex.getContext().getWords() +"\","
								+"\"\n\t}");
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
