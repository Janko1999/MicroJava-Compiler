// generated with ast extension for cup
// version 0.8
// 16/1/2022 22:9:55


package rs.ac.bg.etf.pp1.ast;

public class SimpleArray extends Designator {

    private LeftBracket LeftBracket;
    private Expr Expr;

    public SimpleArray (LeftBracket LeftBracket, Expr Expr) {
        this.LeftBracket=LeftBracket;
        if(LeftBracket!=null) LeftBracket.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public LeftBracket getLeftBracket() {
        return LeftBracket;
    }

    public void setLeftBracket(LeftBracket LeftBracket) {
        this.LeftBracket=LeftBracket;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(LeftBracket!=null) LeftBracket.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(LeftBracket!=null) LeftBracket.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(LeftBracket!=null) LeftBracket.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SimpleArray(\n");

        if(LeftBracket!=null)
            buffer.append(LeftBracket.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SimpleArray]");
        return buffer.toString();
    }
}
