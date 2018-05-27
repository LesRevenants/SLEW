
import java.io.BufferedReader;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

import DataExtraction.wikipedia.WikipediaDataExtractor;


public class Main {

	private static ServerSocket socket;
    private static Socket connection;
    private static String command;
    private static String responseStr ;
    //private static int port;



	public static String extract(String articlename, Properties properties) {
		WikipediaDataExtractor wikipediaDataExtractor = new WikipediaDataExtractor();
    	SLEW slew=new SLEW(properties);
    	if(articlename == null)
    		slew.run(wikipediaDataExtractor,"datas/patterns/patterns.json","datas/jdm-mc.txt.ser","datas/wiki_articles_id", "datas/out/slew_output.json",true);
    	else
    		slew.run(wikipediaDataExtractor,"datas/patterns/patterns.json","datas/jdm-mc.txt.ser",articlename,"datas/out/slew_output.json",false);
        return wikipediaDataExtractor.getText();
	}

	
	public static void connectJava2Php(int port, Properties properties) {
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
		                 String text=extract(article,properties);
		                 String relationsStr = FileUtils.readFileToString(new File("json.txt"),StandardCharsets.UTF_8);
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
    	Properties properties=new Properties();
    	properties.setProperty("verbose","false");
    	properties.setProperty("use_db","false");
    	properties.setProperty("use_corenlp","false");
    	properties.setProperty("use_jdm","false");

    	switch(args[0]){
    		case "valid":{
    			properties.setProperty("valid","true");
				extract(null,properties);
			}
    		case "rex": {
    			properties.setProperty("valid","false");
    		    if(args.length >= 2){
    		        if(args[1].equals("net")){
						properties.setProperty("verbose","true");
                        connectJava2Php(Integer.parseInt(args[2]),properties);
                    }
                    else if(args[1].equals("sta")){
    		            extract(null,properties);
                    }

                }
            }
    		case "help": {
    			//System.out.println("i : ");
    		}
    	}
    }
    
}
