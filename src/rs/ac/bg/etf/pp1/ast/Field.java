// generated with ast extension for cup
// version 0.8
// 16/1/2022 22:9:55


package rs.ac.bg.etf.pp1.ast;

public class Field extends Designator {

    private FieldName FieldName;
    private String classFieldName;

    public Field (FieldName FieldName, String classFieldName) {
        this.FieldName=FieldName;
        if(FieldName!=null) FieldName.setParent(this);
        this.classFieldName=classFieldName;
    }

    public FieldName getFieldName() {
        return FieldName;
    }

    public void setFieldName(FieldName FieldName) {
        this.FieldName=FieldName;
    }

    public String getClassFieldName() {
        return classFieldName;
    }

    public void setClassFieldName(String classFieldName) {
        this.classFieldName=classFieldName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FieldName!=null) FieldName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FieldName!=null) FieldName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FieldName!=null) FieldName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Field(\n");

        if(FieldName!=null)
            buffer.append(FieldName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+classFieldName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Field]");
        return buffer.toString();
    }
}
