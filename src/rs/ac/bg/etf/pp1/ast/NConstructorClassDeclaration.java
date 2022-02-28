// generated with ast extension for cup
// version 0.8
// 16/1/2022 22:9:54


package rs.ac.bg.etf.pp1.ast;

public class NConstructorClassDeclaration extends ClassDecl {

    private ClassDeclarationName ClassDeclarationName;
    private VarDeclarations VarDeclarations;
    private MethDeclList MethDeclList;

    public NConstructorClassDeclaration (ClassDeclarationName ClassDeclarationName, VarDeclarations VarDeclarations, MethDeclList MethDeclList) {
        this.ClassDeclarationName=ClassDeclarationName;
        if(ClassDeclarationName!=null) ClassDeclarationName.setParent(this);
        this.VarDeclarations=VarDeclarations;
        if(VarDeclarations!=null) VarDeclarations.setParent(this);
        this.MethDeclList=MethDeclList;
        if(MethDeclList!=null) MethDeclList.setParent(this);
    }

    public ClassDeclarationName getClassDeclarationName() {
        return ClassDeclarationName;
    }

    public void setClassDeclarationName(ClassDeclarationName ClassDeclarationName) {
        this.ClassDeclarationName=ClassDeclarationName;
    }

    public VarDeclarations getVarDeclarations() {
        return VarDeclarations;
    }

    public void setVarDeclarations(VarDeclarations VarDeclarations) {
        this.VarDeclarations=VarDeclarations;
    }

    public MethDeclList getMethDeclList() {
        return MethDeclList;
    }

    public void setMethDeclList(MethDeclList MethDeclList) {
        this.MethDeclList=MethDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassDeclarationName!=null) ClassDeclarationName.accept(visitor);
        if(VarDeclarations!=null) VarDeclarations.accept(visitor);
        if(MethDeclList!=null) MethDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassDeclarationName!=null) ClassDeclarationName.traverseTopDown(visitor);
        if(VarDeclarations!=null) VarDeclarations.traverseTopDown(visitor);
        if(MethDeclList!=null) MethDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassDeclarationName!=null) ClassDeclarationName.traverseBottomUp(visitor);
        if(VarDeclarations!=null) VarDeclarations.traverseBottomUp(visitor);
        if(MethDeclList!=null) MethDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NConstructorClassDeclaration(\n");

        if(ClassDeclarationName!=null)
            buffer.append(ClassDeclarationName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclarations!=null)
            buffer.append(VarDeclarations.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethDeclList!=null)
            buffer.append(MethDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NConstructorClassDeclaration]");
        return buffer.toString();
    }
}
