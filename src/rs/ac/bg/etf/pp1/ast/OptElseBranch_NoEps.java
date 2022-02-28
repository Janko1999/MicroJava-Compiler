// generated with ast extension for cup
// version 0.8
// 16/1/2022 22:9:55


package rs.ac.bg.etf.pp1.ast;

public class OptElseBranch_NoEps extends OptElseBranch {

    private ElseDummy ElseDummy;
    private Statement Statement;

    public OptElseBranch_NoEps (ElseDummy ElseDummy, Statement Statement) {
        this.ElseDummy=ElseDummy;
        if(ElseDummy!=null) ElseDummy.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public ElseDummy getElseDummy() {
        return ElseDummy;
    }

    public void setElseDummy(ElseDummy ElseDummy) {
        this.ElseDummy=ElseDummy;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ElseDummy!=null) ElseDummy.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ElseDummy!=null) ElseDummy.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ElseDummy!=null) ElseDummy.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("OptElseBranch_NoEps(\n");

        if(ElseDummy!=null)
            buffer.append(ElseDummy.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [OptElseBranch_NoEps]");
        return buffer.toString();
    }
}
