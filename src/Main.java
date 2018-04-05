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
	
	
	
	public static void connectJava2Php(int port){
		
		       try  {
		        	
		    		socket = new ServerSocket(port);
		    		command = new String("");
		     	    responseStr  = new String("ttt");

			        System.out.println("Signal Server is running on socket " + socket.getInetAddress()+"\n");

		            while (true) {
		                System.out.println("le serveur attends une chaine\n");

		                connection = socket.accept();
		                
		                InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
		                DataOutputStream response = new DataOutputStream(connection.getOutputStream());
		                BufferedReader input = new BufferedReader(inputStream);

		                command = input.readLine();
		                System.out.println("The input is " + command + "\n");
		                response.writeUTF("nike ta mere");
		                
		                if (response.equals(null)){
		                	break;
		                } else {
		                	
		              // response.writeBytes(responseStr);             
		               // System.out.println("response : " + response + "\n");
		               // System.out.println("responseStr l : " + responseStr.length() +"\n");

		                response.flush();		               
		                response.close();	                		              
		                System.out.println("Running" + "\n");
		                }
		            }
		            
		        } catch (IOException e)  {
		           System.out.println("Fail!: " + e.toString());
		       }

		       // System.out.println("Closing...");
	}
		
	
	
	
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException{
    	//if(args.length = 2)
    	System.out.println(args[0]);
    	connectJava2Php(Integer.parseInt(args[0]));
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
