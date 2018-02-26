package Relation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;




public class analyseParCas {

	
	
public analyseParCas(){}

public boolean isHyperonymie(Relation relation){
 return searchFile("Hyperonymie.txt",relation);
}
							
public boolean isHolonymie(Relation relation){
return searchFile("Holonymie.txt",relation);
}
							
public boolean isMéronymie(Relation relation){
	return searchFile("Mérominie.txt", relation); 
}
							
public boolean isCause(Relation relation){
return searchFile("Cause.txt", relation); 
}
							
public boolean isEffet(Relation relation){
	return searchFile("Effect.txt", relation); 
							}
							
public boolean isPossession(Relation relation){
return searchFile("Possesion.txt", relation);
}
					
							
	private boolean searchFile(String filePath,Relation relation) {
		boolean find= false;
			try {
				FileReader fileReader = new FileReader(filePath);
				BufferedReader buffReader = new BufferedReader(fileReader);
				String line = new String();
				while( (line = buffReader.readLine()) != null) {
					String[] fields = line.split(";"); 
						for(String s : fields){
							s.replace("_"," ");
							System.out.println("Relation traitée : " + relation + " teste : " + s + "\n");
							if(fields.equals(relation))
								find = true;					
						}
				}	
				buffReader.close();
				fileReader.close();		
		} catch (IOException e) {
				e.printStackTrace();
		}	
			
			return find;
	}

}

