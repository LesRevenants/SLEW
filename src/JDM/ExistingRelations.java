package JDM;
import RequeterRezo.Annotation;
import RequeterRezo.Mot;
import RequeterRezo.RequeterRezo;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import Relation.ExtractedRelation;

public class ExistingRelations {

	   public ExistingRelations() {
		   
	   }

	   public boolean Requesting(ExtractedRelation toResearch){ 
		   try{
			   RequeterRezo systeme = new RequeterRezo("36h", 3000);
		        if(toResearch.getObject() != null) {	   
			        Mot result = systeme.requete(toResearch.getObject(), true);
			        if(result != null){
			        	 ArrayList<Annotation> annotations = result.getAnnotations();
					        for(Annotation annotation : annotations) {
						        if(annotation.getMot_entrant().equals(toResearch.getObject()) &&
						           annotation.getMot_sortant().equals(toResearch.getSubject()) &&
						           annotation.getType_relation().equals(toResearch.getRelation_type())) {
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
