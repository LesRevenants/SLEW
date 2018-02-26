package Relation;

public class Relation {
	
	protected String firstTerme;
	protected String secondTerme;
	
	Relation(String t1, String t2){
		firstTerme = t1;
		secondTerme = t2;
	}
	
	public String toString(){
		String os = new String();
		os += "First termes : " + firstTerme + "\nSecond termes : " + secondTerme;
		return os;
	}

}
