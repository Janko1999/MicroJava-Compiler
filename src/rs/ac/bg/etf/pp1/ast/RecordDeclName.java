// generated with ast extension for cup
// version 0.8
// 16/1/2022 22:9:54


package rs.ac.bg.etf.pp1.ast;

public class RecordDeclName extends RecordDecName {

    private String recordName;

    public RecordDeclName (String recordName) {
        this.recordName=recordName;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName=recordName;
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
        buffer.append("RecordDeclName(\n");

        buffer.append(" "+tab+recordName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [RecordDeclName]");
        return buffer.toString();
    }
}
