// generated with ast extension for cup
// version 0.8
// 16/1/2022 22:9:55


package rs.ac.bg.etf.pp1.ast;

public class IfCondition_Cond extends IfCondition {

    private IfDummy IfDummy;
    private IfCondParen IfCondParen;

    public IfCondition_Cond (IfDummy IfDummy, IfCondParen IfCondParen) {
        this.IfDummy=IfDummy;
        if(IfDummy!=null) IfDummy.setParent(this);
        this.IfCondParen=IfCondParen;
        if(IfCondParen!=null) IfCondParen.setParent(this);
    }

    public IfDummy getIfDummy() {
        return IfDummy;
    }

    public void setIfDummy(IfDummy IfDummy) {
        this.IfDummy=IfDummy;
    }

    public IfCondParen getIfCondParen() {
        return IfCondParen;
    }

    public void setIfCondParen(IfCondParen IfCondParen) {
        this.IfCondParen=IfCondParen;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfDummy!=null) IfDummy.accept(visitor);
        if(IfCondParen!=null) IfCondParen.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfDummy!=null) IfDummy.traverseTopDown(visitor);
        if(IfCondParen!=null) IfCondParen.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfDummy!=null) IfDummy.traverseBottomUp(visitor);
        if(IfCondParen!=null) IfCondParen.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfCondition_Cond(\n");

        if(IfDummy!=null)
            buffer.append(IfDummy.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfCondParen!=null)
            buffer.append(IfCondParen.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfCondition_Cond]");
        return buffer.toString();
    }
}
