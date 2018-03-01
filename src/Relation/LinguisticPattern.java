package Relation;

import java.util.LinkedList;

/**
 * A linguisticPattern
 * ex : $x est un $y || est_un || est un
 *
 */


public class LinguisticPattern {

    /**
     * Symbol which represent a variable
     */
    public static final char var_symbol = '$';

    /**
     * The pattern 
     * ex : est_un
     */
    String pattern_str;
    

    /**
     * The list of vars from the pattern
     */
    LinkedList<String> vars;
    

    /**
     * 
     * @param pattern : a pattern as $x pattern_str $y ; constraints
     */
    LinguisticPattern(String pattern){

    	String[] senteceParts = pattern.split(";");
    	
    	String patternPart = senteceParts[0];
    	String[] patternParts = patternPart.split(" ");
    	
    	if(patternParts.length == 1) { // no variable -> just read the pattern_str
    		this.pattern_str = patternParts[0];
    	}
    	else {
    		for(String exp : patternParts){ // extract vars from the sentence
                if(exp.length() > 1 && exp.charAt(0) == var_symbol){
                    vars.add(exp);
                }
                else {
                	this.pattern_str = exp;
                }
            }
    	}
    }


	@Override
	public String toString() {
		return pattern_str;
	}


	public String getPattern() {
		return pattern_str;
	}


	public LinkedList<String> getVars() {
		return vars;
	}

    
    
}
