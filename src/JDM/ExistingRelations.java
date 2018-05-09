package JDM;
import RequeterRezo.Mot;
import RequeterRezo.RequeterRezo;
import RequeterRezo.Terme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Relation.ExtractedRelation;

public class ExistingRelations {

	   public ExistingRelations() {
		   
	   }

	   public boolean Requesting(ExtractedRelation toResearch, RequeterRezo systeme){ 
		   try{
		       assert(toResearch.getX() != null);
		       assert(toResearch.getY() != null);
		        if(toResearch.getX() != null ) {
			        Mot result = systeme.requete(toResearch.getX(), true);
			        if(result != null){
			        	//toResearch.getContext().getWordsPositions(toResearch.get
			        		 HashMap<String, ArrayList<Terme>> req = result.getRelations_sortantes();
				        	 //System.out.println("Nombre de requêtes : "+req.size());
							 if(req.containsKey(toResearch.getRelation_type())){
                                 for(Terme t : req.get(toResearch.getRelation_type())) {
                                     if(t.getTerme().equals(toResearch.getY())) {
                                         return true;
                                     }
                                 }
                             }
			        } 
			        
		
		        }
		   }
		   catch(IOException | InterruptedException e){
			   e.printStackTrace();
		   }
	       
	        return false;
	        
	    }
	   
	}
