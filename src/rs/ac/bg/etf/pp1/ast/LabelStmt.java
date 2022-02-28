// generated with ast extension for cup
// version 0.8
// 16/1/2022 22:9:54


package rs.ac.bg.etf.pp1.ast;

public class LabelStmt extends Statement {

    private Label Label;
    private SingleStmt SingleStmt;

    public LabelStmt (Label Label, SingleStmt SingleStmt) {
        this.Label=Label;
        if(Label!=null) Label.setParent(this);
        this.SingleStmt=SingleStmt;
        if(SingleStmt!=null) SingleStmt.setParent(this);
    }

    public Label getLabel() {
        return Label;
    }

    public void setLabel(Label Label) {
        this.Label=Label;
    }

    public SingleStmt getSingleStmt() {
        return SingleStmt;
    }

    public void setSingleStmt(SingleStmt SingleStmt) {
        this.SingleStmt=SingleStmt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Label!=null) Label.accept(visitor);
        if(SingleStmt!=null) SingleStmt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Label!=null) Label.traverseTopDown(visitor);
        if(SingleStmt!=null) SingleStmt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Label!=null) Label.traverseBottomUp(visitor);
        if(SingleStmt!=null) SingleStmt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("LabelStmt(\n");

        if(Label!=null)
            buffer.append(Label.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleStmt!=null)
            buffer.append(SingleStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [LabelStmt]");
        return buffer.toString();
    }
}
