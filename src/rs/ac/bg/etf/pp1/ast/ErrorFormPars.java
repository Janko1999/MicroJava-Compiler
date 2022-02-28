// generated with ast extension for cup
// version 0.8
// 16/1/2022 22:9:54


package rs.ac.bg.etf.pp1.ast;

public class ErrorFormPars extends FormPars {

    public ErrorFormPars () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ErrorFormPars(\n");

        buffer.append(tab);
        buffer.append(") [ErrorFormPars]");
        return buffer.toString();
    }
}
