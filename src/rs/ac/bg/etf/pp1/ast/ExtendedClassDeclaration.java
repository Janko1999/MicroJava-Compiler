// generated with ast extension for cup
// version 0.8
// 16/1/2022 22:9:54


package rs.ac.bg.etf.pp1.ast;

public class ExtendedClassDeclaration extends ClassDecl {

    private ExtendedClassDeclarationName ExtendedClassDeclarationName;
    private VarDeclarations VarDeclarations;

    public ExtendedClassDeclaration (ExtendedClassDeclarationName ExtendedClassDeclarationName, VarDeclarations VarDeclarations) {
        this.ExtendedClassDeclarationName=ExtendedClassDeclarationName;
        if(ExtendedClassDeclarationName!=null) ExtendedClassDeclarationName.setParent(this);
        this.VarDeclarations=VarDeclarations;
        if(VarDeclarations!=null) VarDeclarations.setParent(this);
    }

    public ExtendedClassDeclarationName getExtendedClassDeclarationName() {
        return ExtendedClassDeclarationName;
    }

    public void setExtendedClassDeclarationName(ExtendedClassDeclarationName ExtendedClassDeclarationName) {
        this.ExtendedClassDeclarationName=ExtendedClassDeclarationName;
    }

    public VarDeclarations getVarDeclarations() {
        return VarDeclarations;
    }

    public void setVarDeclarations(VarDeclarations VarDeclarations) {
        this.VarDeclarations=VarDeclarations;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExtendedClassDeclarationName!=null) ExtendedClassDeclarationName.accept(visitor);
        if(VarDeclarations!=null) VarDeclarations.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExtendedClassDeclarationName!=null) ExtendedClassDeclarationName.traverseTopDown(visitor);
        if(VarDeclarations!=null) VarDeclarations.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExtendedClassDeclarationName!=null) ExtendedClassDeclarationName.traverseBottomUp(visitor);
        if(VarDeclarations!=null) VarDeclarations.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExtendedClassDeclaration(\n");

        if(ExtendedClassDeclarationName!=null)
            buffer.append(ExtendedClassDeclarationName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclarations!=null)
            buffer.append(VarDeclarations.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExtendedClassDeclaration]");
        return buffer.toString();
    }
}
