package JDM;
import RequeterRezo.Annotation;
import RequeterRezo.Mot;
import RequeterRezo.RequeterRezo;
import RequeterRezo.Terme;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import Relation.ExtractedRelation;

public class ExistingRelations {

	   public ExistingRelations() {
		   
	   }

	   public boolean Requesting(ExtractedRelation toResearch){ 
		   try{
			   RequeterRezo systeme = new RequeterRezo("36h", 3000);
		        if(toResearch.getObject() != null) {	  
			        Mot result = systeme.requete(toResearch.getSubject(), true);
			        if(result != null){
			        	 /*ArrayList<Annotation> annotations = result.getAnnotations();
			        	 System.out.println("Nombre d'annotations : "+annotations.size());
					        for(Annotation annotation : annotations) {
					        	if(annotation.getMot_entrant().equals("genou")||annotation.getMot_sortant().equals("genou")){
					        		System.out.println("Annotation : "+annotation.toString());
					        	    //System.out.println("toresearch : "+ toResearch.getObject() + " " + toResearch.getSubject());
					        		//System.out.println("motentrant,motsortant,relation : "+annotation.getMot_sortant().equals(toResearch.getObject())+
					        		//	" "+annotation.getMot_entrant().equals(toResearch.getSubject())+
					        		//	" "+annotation.getType_relation().equals(toResearch.getRelation_type()));
					        		//System.out.println("Annotation_relation : "+annotation.getType_relation());
					        		//System.out.println("toresearch : "+ toResearch.getRelation_type());
					        		
					        	}
						        if(annotation.getMot_sortant().equals(toResearch.getObject()) &&
						           annotation.getMot_entrant().equals(toResearch.getSubject()) &&
						           annotation.getType_relation().equals(toResearch.getRelation_type())) {
						        	return true; 
						        } 
					        }*/
					        
			        		 HashMap<String, ArrayList<Terme>> req = result.getRelations_sortantes();
				        	 //System.out.println("Nombre de requêtes : "+req.size());
						        for(Terme t : req.get(toResearch.getRelation_type())) {
							        if(t.getTerme().equals(toResearch.getObject())) {
							        	return true; 
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
