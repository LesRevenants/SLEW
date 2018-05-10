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



	public static String extract(String articlename,boolean verbose, boolean use_db, boolean valid) {
		WikipediaDataExtractor wikipediaDataExtractor = new WikipediaDataExtractor();
    	SLEW slew=new SLEW();
    	if(articlename == null)
    		slew.run(wikipediaDataExtractor,"datas/patterns/patterns.json","datas/jdm-mc.ser","datas/wiki_articles_id", "datas/out/slew_output.json",verbose, true,use_db,valid);
    	else
    		slew.run(wikipediaDataExtractor,"datas/patterns/patterns.json","datas/jdm-mc.ser",articlename,"datas/out/slew_output.json",  verbose, false,use_db,valid);
        return wikipediaDataExtractor.getText();
	}

	
	public static void connectJava2Php(int port) {
		          ServerSocket listenSock; //the listening server socket
		          Socket sock;             //the socket that will actually be used for communication
		          try {
		              listenSock = new ServerSocket(port);
		              System.out.println("Waiting query ");
		              while (true) {       //we want the server to run till the end of times
		            	 
		                 sock = listenSock.accept();             //will block until connection recieved
		                 
		                 BufferedReader br =  new BufferedReader(new InputStreamReader(sock.getInputStream()));
		                 BufferedWriter bw =  new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		                 String article = br.readLine();
		                 System.out.println(article);
		                 int max_size=1000;

		                 String text=extract(article,true,false,false);
		                 String relationsStr = FileUtils.readFileToString(new File("json.txt"),Charsets.UTF_8);
		                 System.out.println(relationsStr);
		                 if(text.length()<max_size){
		                	 bw.write(text+"@"+relationsStr);
		                 }
		                 else{
		                	 bw.write(text.substring(0, max_size)+"@"+relationsStr);
		                 }
		                 //Closing streams and the current socket (not the listening socket!)
		                 bw.close();
		                 br.close();
		                 sock.close();
		             }
		         } catch (IOException ex) {
             ex.printStackTrace();
		       }
		    
		}
	
	
	
    public static void main(String[] args){
    	/*if(args.length >= 1) {
    	connectJava2Php(Integer.parseInt(args[0])); }
    	//else
    	//extract(null);*/
    	if(args.length == 0){
    		System.err.println("Error bad argument number, use help" );
    		return;
    	}
    	switch(args[0]){
    		case "valid":{
				extract(null,false,false,true);
			}
    		case "rex": {
    		    if(args.length >= 2){
    		        if(args[1].equals("net")){
                        connectJava2Php(Integer.parseInt(args[2]));
                    }
                    else if(args[1].equals("sta")){
    		            extract(null,false,false,false);
                    }

                }
            }
    		case "help": {
    			//System.out.println("i : ");
    		}
    	}
		//extract(null,false,false);
    		
    }
    
}
