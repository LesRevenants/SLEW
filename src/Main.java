import TextStructure.CompoundWordBuilder;
import java.io.BufferedReader;
import java.net.*;
import java.io.*;
import TextStructure.StructuredText;
import TextStructure.TextSequence;
import TextStructure.WordPatriciaTrie;
import Util.Utils;
import lib.org.ardverk.collection.PatriciaTrie;
import java.util.Scanner;
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

import org.apache.commons.io.Charsets;
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

	private static ServerSocket socket;
    private static Socket connection;
    private static String command;
    private static String responseStr ;
    //private static int port;

	
	private static void test() {
		
		String text = "Le requin baleine est un poisson cartilagineux, "
    			+ "seul membre du genre Rhincodon et seule espèce actuelle de la famille des Rhincodontidae."
    			+ "Le chamois est une espèce de "
    			+ "la sous-famille des Caprinés."
    			+ "Les marmottes (Marmota) forment un genre de mammifère fouisseur de l'ordre des rongeurs."
				+ "L'espèce la plus connue en Europe est la marmotte vivant dans les montagnes (Marmota marmota)."
				+ "En Amérique du Nord, la Marmota monax, appelée familièrement « siffleux » au Québec, constitue l'espèce la plus courante.";
				      
	}

	public static void extract(String articlename) throws UnsupportedEncodingException, FileNotFoundException, IOException{
		WikipediaDataExtractor wikipediaDataExtractor = new WikipediaDataExtractor();
    	SLEW slew=new SLEW();
    	if(articlename == null)
    		slew.run(wikipediaDataExtractor,"datas/patterns/patterns.json","datas/jdm-mc.ser","datas/wiki_articles_id", true, true);
    	else
    		slew.run(wikipediaDataExtractor,"datas/patterns/patterns.json","datas/jdm-mc.ser", articlename, true, false);
	}


	public static String extractArg(String articlename) {
		
		WikipediaDataExtractor wikipediaDataExtractor = new WikipediaDataExtractor();
		String rcv = wikipediaDataExtractor.getText(articlename);
		System.out.println(rcv);
		return rcv;
    	//SLEW slew=new SLEW();
    	//String tab = slew.run(wikipediaDataExtractor,"datas/patterns/patterns.json","datas/jdm-mc.ser", articlename, true, false);
    	
	}
	
	
	public static void connectJava2Php(int port) {
		          ServerSocket listenSock = null; //the listening server socket
		          Socket sock = null;             //the socket that will actually be used for communication
		          
		          try {
		  
		              listenSock = new ServerSocket(port);
		              System.out.println("Waiting query ");
		             while (true) {       //we want the server to run till the end of times
		            	 
		                 sock = listenSock.accept();             //will block until connection recieved
		                 
		                 BufferedReader br =    new BufferedReader(new InputStreamReader(sock.getInputStream()));
		                 BufferedWriter bw =    new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		                 String line = "";
		                 String content = "";
		                 String article = br.readLine();
		                 System.out.println(article);
		                 /*while ((line = br.readLine()) != null) {
		                	 content += extractArg(line);
		                     //bw.write("PHP said: " + line + "\n");
		                     //bw.flush();
		                 }*/
		                 int max_size=1000;
		                 String text=extractArg(article);
		                 extract(article);
		                 String relationsStr = FileUtils.readFileToString(new File("json.txt"),Charsets.UTF_8);
		                 
		                 if(text.length()<max_size){
		                	 bw.write(text+relationsStr);
		                 }
		                 else{
		                	 bw.write(text.substring(0, max_size)+relationsStr);
		                 }
		                // bw.write(extractArg(article).substring(0, 1000),0,10000);
		               
		                 /*while ((line = br.readLine()) != "hehe") {}
		                     //bw.write("PHP said: " + line + "\n");
		                     //bw.flush*/
		                 //Closing streams and the current socket (not the listening socket!)
		                 bw.close();
		                 br.close();
		                 sock.close();
		             }
		         } catch (IOException ex) {
             ex.printStackTrace();
		       }
		    
		}
	
	
	
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException{
    	//if(args.length = 2)
    	System.out.println(args[0]);
    	connectJava2Php(Integer.parseInt(args[0]));
    	if(args.length == 1) {
    	connectJava2Php(Integer.parseInt(args[0])); }
    	//else
    	//extract(null);
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
