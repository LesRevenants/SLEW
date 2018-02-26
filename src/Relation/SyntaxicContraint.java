package Relation;

/**
 *
 */
public class SyntaxicContraint {

    /** */
    String var;

    /** */
    String type;

    public SyntaxicContraint(String var, String type) {
        this.var = var;
        this.type = type;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
