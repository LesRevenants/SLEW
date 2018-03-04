package Relation;

import java.util.LinkedList;

/**
 *
 */
public class SyntaxicContraint {

   private LinkedList<String> xConstraints;
   private LinkedList<String> yConstraints;
   
	public SyntaxicContraint(LinkedList<String> xConstraints, LinkedList<String> yConstraints) {
		super();
		this.xConstraints = xConstraints;
		this.yConstraints = yConstraints;
	}

	public LinkedList<String> getxConstraints() {
		return xConstraints;
	}

	public LinkedList<String> getyConstraints() {
		return yConstraints;
	}

	@Override
	public String toString() {
		return "[" + xConstraints + " , " + yConstraints + "]";
	}
	
	
	public boolean isEmpty() {
		return xConstraints.isEmpty() && yConstraints.isEmpty();
	}
	
	
   
   
   
   
}
