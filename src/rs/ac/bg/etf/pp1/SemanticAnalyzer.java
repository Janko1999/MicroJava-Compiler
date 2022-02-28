
package rs.ac.bg.etf.pp1;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;

import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class SemanticAnalyzer extends VisitorAdaptor {
	int arrayCounter = 0;
	int recordCount=0;
	public int funcCallCount=0;
	int globalVarDeclCount = 0;
	int localVarDeclCount = 0;
	Obj currentMethod = null;
	Struct currentClass = null;
	Struct currentRecord = null;
	Struct currentType = null;
	String currentField;

	public static final Struct boolType = new Struct(Struct.Bool);

	boolean returnFound = false;
	boolean returnVoidFound = false;
	boolean errorDetected = false;
	boolean dowhile = false;
	boolean bracketFlag = false;
	boolean dotFlag = false;
	int accessToArrayElement = 0;
	int numOfFormPars = 0;
	int nVars;
	LinkedList<Struct> pars = new LinkedList<Struct>();

	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
		
	}

	
	public void visit(ProgName progName) {

		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", boolType));
		Tab.openScope();
	}

	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
	}

	public void visit(Var varDecl) {

		DumpSymbolTableVisitor dstv=new DumpSymbolTableVisitor();
		
		Obj obj = Tab.currentScope.findSymbol(varDecl.getVarName());
		
		if (obj == null) {
			
			if (currentClass == null && currentRecord == null) {
				Obj objj= Tab.insert(Obj.Var, varDecl.getVarName(), currentType);
				dstv.visitObjNode(objj);
					 if(objj.getLevel()==0) {
						 globalVarDeclCount++;
					 report_info("Deklarisana globalna promenljiva "+varDecl.getVarName()+" na liniji "+varDecl.getLine()
					 +" "+dstv.getOutput() , null);
					 }
					 else {
						 localVarDeclCount++;
						 report_info("Deklarisana lokalna promenljiva "+varDecl.getVarName()+" na liniji "+varDecl.getLine()+" "+dstv.getOutput(), null);
					 }
				
					if(objj.getType().getKind()==Struct.Class)
						report_info("Deklarisan rekord "+varDecl.getVarName()+" na liniji "+varDecl.getLine()+" "+dstv.getOutput(), null);
						recordCount++;
			} else {
				 Tab.insert(Obj.Fld, varDecl.getVarName(), currentType);
			}

		} else {
			report_error("Promenljiva " + varDecl.getVarName() + " vec deklarisana! ", null);
		}
	}

	public void visit(DesignatorNoPars funcCall) {
		funcCallCount++;
		DumpSymbolTableVisitor dstv=new DumpSymbolTableVisitor();
		Obj func = funcCall.getDesignator().obj;
		dstv.visitObjNode(func);
		if (Obj.Meth == func.getKind()) {
			report_info(
					"Pronadjen poziv funkcije " + func.getName() + func.getLevel() + " na liniji " + funcCall.getLine()+" "+dstv.getOutput(),
					null);
			if (func.getLevel() != 0) {
				report_error(
						"Greska na liniji " + funcCall.getLine()
								+ " : Poziv funkcije ima vise parametara nego ocekivano, ocekivano: " + func.getLevel(),
						null);
			}

		} else {
			report_error("Greska na liniji " + funcCall.getLine() + " : ime " + func.getName() + " nije funkcija!",
					null);

		}
	}

	public void visit(DesignatorPars funcCall) {
		funcCallCount++;
		DumpSymbolTableVisitor dstv=new DumpSymbolTableVisitor();
		Obj func = funcCall.getDesignator().obj;
		dstv.visitObjNode(func);
		if (Obj.Meth == func.getKind()) {
			report_info(
					"Pronadjen poziv funkcije " + func.getName() + func.getLevel() + " na liniji " + funcCall.getLine()+" "+dstv.getOutput(),
					null);
			Collection<Obj> members = funcCall.getDesignator().obj.getLocalSymbols();
			if (funcCall.getDesignator().obj.getLevel() != pars.size()) {
				report_error("Greska na liniji " + funcCall.getLine()
						+ " : Neodgovarajuci broj parametara pri pozivu funkcije "
						+ funcCall.getDesignator().obj.getName(), null);
			} else {
				int i = 0;
				for (Obj obj : members) {
					if (i == pars.size())
						break;
					if (!obj.getType().compatibleWith(pars.get(i))) {
						report_error("Greska na liniji " + funcCall.getLine()
								+ " : Tipovi parametara nisu odgovarajuci pri pozivu funkcije "
								+ funcCall.getDesignator().obj.getName(), null);
						break;
					}
					i++;
				}
			}
			pars.clear();

		} else {
			report_error("Greska na liniji " + funcCall.getLine() + " : ime " + currentField + " nije funkcija!", null);

		}
	}

	public void visit(Array vardeclArray) {
		DumpSymbolTableVisitor dstv=new DumpSymbolTableVisitor();
		Obj obj = Tab.currentScope.findSymbol(vardeclArray.getVarName());

		if (obj == null) {
		
			if (currentClass == null && currentRecord == null) {
				 Obj objj=Tab.insert(Obj.Var, vardeclArray.getVarName(), new Struct(Struct.Array, currentType));
				 dstv.visitObjNode(objj);
				 if(objj.getLevel()==0) {
					 globalVarDeclCount++;
				 report_info("Deklarisana globalna promenljiva "+vardeclArray.getVarName()+" na liniji "+vardeclArray.getLine()+" "+dstv.getOutput()
				 , null);
				 
				 }
				 else {
					 localVarDeclCount++;
					 report_info("Deklarisana lokalna promenljiva "+vardeclArray.getVarName()+" na liniji "+vardeclArray.getLine()+" "+dstv.getOutput(), null);
				 }
			} else {
				 Tab.insert(Obj.Fld, vardeclArray.getVarName(), new Struct(Struct.Array, currentType));
			}

		} else {
			report_error("Promenljiva " + vardeclArray.getVarName() + " vec deklarisana! ", null);
		}
	}

	public void visit(VarDeclType vardeclType) {

		currentType = vardeclType.getType().struct;
	}

	public void visit(Variables vardeclArray) {
		currentType = null;
	}

	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", null);
			type.struct = Tab.noType;
		} else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
			} else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
				type.struct = Tab.noType;
			}
		}
	}

	public void visit(MethodTypeName methodTypeName) {
		if(currentRecord!=null) 
			report_error("Greska na liniji "+methodTypeName.getLine()+": Rekord ne sme da ima metode!", null);
		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethName(), methodTypeName.getType().struct);

		methodTypeName.obj = currentMethod;
		Tab.openScope();
	
			
		
		
	}

	public void visit(VoidTypeName voidTypeName) {
		if(currentRecord!=null) 
			report_error("Greska na liniji "+voidTypeName.getLine()+": Rekord ne sme da ima metode!", null);
		currentMethod = Tab.insert(Obj.Meth, voidTypeName.getMethName(), Tab.noType);
		voidTypeName.obj = currentMethod;
		Tab.openScope();
		
		
	}

	public void visit(RegularMethod methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName()
					+ " nema return iskaz!", null);
		}
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		Collection<Obj> pars = currentMethod.getLocalSymbols();
		int i = 0;
		for (Obj obj : pars) {
			if (i == currentMethod.getLevel())
				break;
			
			i++;
		}
		numOfFormPars = 0;
		returnFound = false;
		currentMethod = null;

	}

	public void visit(VoidMethod methodDecl) {
		if (returnFound) {
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName()
					+ " ne vraca odgovarajuci tip", null);
		}
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();

		Collection<Obj> pars = currentMethod.getLocalSymbols();
		int i = 0;
		numOfFormPars = 0;
		for (Obj obj : pars) {
			if (i == currentMethod.getLevel())
				break;
			
			i++;
		}

		returnFound = false;
		returnVoidFound = false;
		currentMethod = null;

	}

	public void visit(ClassDeclarationName className) {

		Obj obj = Tab.insert(Obj.Type, className.getClassName(), new Struct(Struct.Class));
		currentClass = obj.getType();
		className.struct = currentClass;
		Tab.openScope();
		report_info("Obradjuje se klasa " + className.getClassName(), className);
	}

	public void visit(ExtendedClassDeclarationName className) {

		Obj obj = Tab.insert(Obj.Type, className.getClassName(), new Struct(Struct.Class));
		currentClass = obj.getType();
		className.struct = currentClass;
		Tab.openScope();
		report_info("Obradjuje se klasa " + className.getClassName(), className);
	}

	public void visit(ExtendedNConstructorClassDeclaration classDecl) {

		Tab.chainLocalSymbols(currentClass);
		Tab.closeScope();

		currentClass = null;
	}

	public void visit(ExtendedClassDeclaration classDecl) {

		Tab.chainLocalSymbols(currentClass);
		Tab.closeScope();

		currentClass = null;
	}

	public void visit(NConstructorClassDeclaration classDecl) {

		Tab.chainLocalSymbols(currentClass);
		Tab.closeScope();

		currentClass = null;
	}

	public void visit(ClassDeclaration classDecl) {

		Tab.chainLocalSymbols(currentClass);
		Tab.closeScope();

		currentClass = null;
	}

	public void visit(RecordDeclName recordName) {
		Obj obj = Tab.insert(Obj.Type, recordName.getRecordName(), new Struct(Struct.Class));
		currentRecord = obj.getType();
		recordName.struct = currentRecord;

		Tab.openScope();
		report_info("Obradjuje se rekord " + recordName.getRecordName(), recordName);
	}

	public void visit(Record record) {

		Tab.chainLocalSymbols(currentRecord);
		Tab.closeScope();

		currentRecord = null;
	}

	public void visit(ConstType constType) {
		currentType = constType.getType().struct;
	}

	public void visit(Constants constType) {
		currentType = null;
	}

	public void visit(NumConstant numConst) {
		Tab.insert(Obj.Con, numConst.getConstName(), Tab.intType).setAdr(numConst.getN1());;
		if (!currentType.equals(Tab.intType)) {
			report_error(
					"Greska: Tipovi pri dodeli vrednosti konstanti " + numConst.getConstName() + " nisu ekvivalentni!",
					numConst);
		}
	}

	public void visit(CharConstant charConst) {
		Tab.insert(Obj.Con, charConst.getConstName(), Tab.charType).setAdr(charConst.getC1());;
		if (!currentType.equals(Tab.charType)) {
			report_error(
					"Greska: Tipovi pri dodeli vrednosti konstanti " + charConst.getConstName() + " nisu ekvivalentni!",
					charConst);
		}
	}

	public void visit(BoolConstant boolConst) {
		Tab.insert(Obj.Con, boolConst.getConstName(), boolType);
		if (!currentType.equals(boolType)) {
			report_error(
					"Greska: Tipovi pri dodeli vrednosti konstanti " + boolConst.getConstName() + " nisu ekvivalentni!",
					boolConst);
		}
	}

	public void visit(ReturnExprStmt returnExpr) {
		if (currentMethod != null) {
			returnFound = true;
			Struct currMethType = currentMethod.getType();
			if (currMethType != Tab.noType) {
				if (!bracketFlag) {
					if (!currMethType.compatibleWith(returnExpr.getExpr().struct)) {
						report_error("Greska na liniji " + returnExpr.getLine() + " : "
								+ "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije "
								+ currentMethod.getName(), null);
					}
				} else {
					if (!currMethType.compatibleWith(returnExpr.getExpr().struct.getElemType())) {
						report_error("Greska na liniji " + returnExpr.getLine() + " : "
								+ "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije "
								+ currentMethod.getName(), null);
					}
				}
			}
		} else {
			report_error(
					"Greska na liniji " + returnExpr.getLine() + " : " + "RETURN naredba van tela metode/funkcije ",
					null);
		}
		bracketFlag = false;
		dotFlag = false;
	}

	public void visit(ReturnStmt returnExpr) {
		returnVoidFound = true;
		Struct currMethType = currentMethod.getType();
		if (currMethType != Tab.noType) {

			report_error("Greska na liniji " + returnExpr.getLine() + " : "
					+ "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije "
					+ currentMethod.getName(), null);

		}
	}

	public void visit(OneIdent var) {
		Obj obj = Tab.find(var.getName());
		if (obj == Tab.noObj) {
			report_error("Greska na liniji " + var.getLine() + " : ime " + var.getName() + " nije deklarisano! ", null);
		}
		var.obj = obj;
	}
	
	
	 
	 
	public void visit(FieldName fldName) {
		fldName.obj=Tab.find(fldName.getDesName());
	}
	public void visit(FieldArrayName fldArrName) {
		Obj obj=Tab.find(fldArrName.getFieldName().getDesName());
		if (obj.getType().getKind() == Struct.Class) { 
		Collection<Obj>members=obj.getType().getMembers();
		 boolean found = false;
		for(Obj object:members) {
			if(object.getName().equals(fldArrName.getName())) {
				fldArrName.obj=object;
				found=true;
			}
		}
		if(!found) {
			report_error("Greska na liniji " + fldArrName.getLine() + " : Rekord " +
					fldArrName.getFieldName().getDesName() + " ne sadrzi " + fldArrName.getName(), null); 
					fldArrName.obj = Tab.noObj;
		}
		}else {
	 		report_error( "Greska na liniji " + fldArrName.getLine() + " : ime " + fldArrName.getFieldName().getDesName() + " nije klasnog tipa! ", null); 
	 		}
		
	}
public void visit(LeftBracket lftBracket) {
	lftBracket.obj=Tab.find(lftBracket.getName());

}
	public void visit(Field field) {
		 Obj obj = Tab.find(field.getFieldName().getDesName()); 
		 if (obj.getType().getKind() == Struct.Class) { 
			 Collection<Obj> members = obj.getType().getMembers(); 
			 boolean found = false; 
			 for (Obj object : members){ 
				 if (object.getName().equals(field.getClassFieldName())) {
					 found = true;
					 field.obj = object; 
					 } 
				 } 
			 if (!found) {
				 	report_error("Greska na liniji " + field.getLine() + " : Rekord " +
				 			field.getFieldName().getDesName() + " ne sadrzi " + field.getFieldName(), null); field.obj = Tab.noObj;
			 		}
		  
		 	} else {
		 		report_error( "Greska na liniji " + field.getLine() + " : ime " + field.getFieldName().getDesName() + " nije klasnog tipa! ", null); 
		 		} 
		 
	}
	
	  public void visit(FieldArray fieldArray) { 
		  DumpSymbolTableVisitor dstv=new DumpSymbolTableVisitor();
		  fieldArray.obj=new Obj(Obj.Elem, fieldArray.getFieldArrayName().getName(), fieldArray.getFieldArrayName().obj.getType().getElemType());
		  dstv.visitObjNode(fieldArray.obj);
		  arrayCounter++;
		  report_info("Pristupilo se elementu niza "+fieldArray.getFieldArrayName().getName()+" na liniji "+fieldArray.getLine()+" "+dstv.getOutput(), null);
	  }
	 

	
	public void  visit(SimpleArray smpArray) {
		DumpSymbolTableVisitor dstv=new DumpSymbolTableVisitor();
		Obj obj = Tab.find(smpArray.getLeftBracket().getName());
		
		if (obj == Tab.noObj) {
			report_error("Greska na liniji " + smpArray.getLine() + " : ime " + smpArray.getLeftBracket().getName() + " nije deklarisano! ", null);
		}
		arrayCounter++;
	
		smpArray.getLeftBracket().obj=obj;
		smpArray.obj = new Obj(Obj.Elem, obj.getName(), obj.getType().getElemType());
		dstv.visitObjNode(smpArray.obj);
		report_info("Pristupilo se elementu niza "+smpArray.getLeftBracket().getName()+" na liniji "+smpArray.getLine()+" "+dstv.getOutput(), null);
		
	}
	
	

	public void visit(DesignatorFactor var) {
		var.struct = var.getDesignator().obj.getType();
	}

	public void visit(DesignatorNoParsFactor var) {
		funcCallCount++;
		DumpSymbolTableVisitor dstv=new DumpSymbolTableVisitor();
		if (var.getDesignator().obj.getType() != Tab.noType) {
			dstv.visitObjNode(var.getDesignator().obj);
			report_info("Pronadjen poziv funkcije " + var.getDesignator().obj.getName() + " na liniji " + var.getLine()+" "+dstv.getOutput(),
					null);
			Collection<Obj> members = var.getDesignator().obj.getLocalSymbols();
			if (var.getDesignator().obj.getLevel() != pars.size()) {
				report_error("Greska na liniji " + var.getLine()
						+ " : Neodgovarajuci broj parametara pri pozivu funkcije " + var.getDesignator().obj.getName(),
						null);
			} else {
				int i = 0;
				for (Obj obj : members) {
					if (i == pars.size())
						break;
					if (!obj.getType().compatibleWith(pars.get(i))) {
						report_error("Greska na liniji " + var.getLine()
								+ " : Tipovi parametara nisu odgovarajuci pri pozivu funkcije "
								, null);
						break;
					}
					i++;
				}
			}
		}
		pars.clear();
		var.struct = var.getDesignator().obj.getType();
	}

	public void visit(DesignatorParsFactor var) {
		DumpSymbolTableVisitor dstv=new DumpSymbolTableVisitor();
		funcCallCount++;
		if (var.getDesignator().obj.getType() != Tab.noType) {
			dstv.visitObjNode(var.getDesignator().obj);
			report_info("Pronadjen poziv funkcije " + var.getDesignator().obj.getName() + " na liniji " + var.getLine()+" "+dstv.getOutput(),
					null);
			Collection<Obj> members = var.getDesignator().obj.getLocalSymbols();
			if (var.getDesignator().obj.getLevel() != pars.size()) {
				report_error("Greska na liniji " + var.getLine()
						+ " : Neodgovarajuci broj parametara pri pozivu funkcije " + var.getDesignator().obj.getName(),
						null);
			} else {
				int i = 0;
				for (Obj obj : members) {
					if (i == pars.size())
						break;
					if (!obj.getType().compatibleWith(pars.get(i))) {
						report_error("Greska na liniji " + var.getLine()
								+ " : Tipovi parametara nisu odgovarajuci pri pozivu funkcije "
								+ var.getDesignator().obj.getName(), null);
						break;
					}
					i++;
				}
			}
		}
		pars.clear();
		var.struct = var.getDesignator().obj.getType();

	}

	public void visit(NumConstFactor var) {
		var.struct = Tab.intType;
	}

	public void visit(CharConstFactor var) {
		var.struct = Tab.charType;
	}

	public void visit(BoolConstFactor var) {
		var.struct = boolType;
	}

	public void visit(NewFactor var) {
		var.struct = var.getType().struct;
		if (var.getType().struct.getKind() != Struct.Class) {
			report_error(
					"Greska na liniji " + var.getLine() + " : promenljiva se moze inicijalizovati samo klasnim tipom.",
					null);
		} else {
			var.struct = var.getType().struct;
		}
	}

	public void visit(NewFactorExpr var) {
		if (var.getExpr().struct != Tab.intType) {
			report_error("Greska na liniji " + var.getLine() + " : tip parametra u uglastim zagradama mora biti int.",
					null);
		} else {
			var.struct = new Struct(Struct.Array, var.getType().struct);
		}
	}

	public void visit(ExprFactor var) {
		var.struct = var.getExpression().struct;
	}

	public void visit(Factors mullFact) {
		Struct t = mullFact.getFactor().struct;
		Struct te = mullFact.getTerm().struct;
		if (te.equals(t) && te == Tab.intType) {
			mullFact.struct = te;
		} else {
			report_error("Greska na liniji " + mullFact.getLine() + " : nekompatibilni tipovi u izrazu za mnozenje.",
					null);
			mullFact.struct = Tab.noType;
		}
	}

	public void visit(OneFactor var) {
		var.struct = var.getFactor().struct;
	}

	public void visit(OneTerm var) {
		var.struct = var.getTerm().struct;
	}

	public void visit(Terms addFact) {
		Struct t = addFact.getExpression().struct;
		Struct te = addFact.getTerm().struct;
		if (te.equals(t) && te == Tab.intType) {
			addFact.struct = te;
		} else {
			report_error("Greska na liniji " + addFact.getLine() + " : nekompatibilni tipovi u izrazu za sabiranje.",
					null);
			addFact.struct = Tab.noType;
		}
	}

	public void visit(PositiveExpr var) {
		var.struct = var.getExpression().struct;
	}

	public void visit(NegativeExpr var) {
		if (var.getExpression().struct == Tab.intType) {
			var.struct = var.getExpression().struct;
		} else {
			var.struct = Tab.noType;
			report_error("Greska na liniji " + var.getLine() + " : izraz mora biti tipa int.", null);
		}
	}

	public void visit(Parameter parameter) {
		Obj obj = Tab.currentScope.findSymbol(parameter.getParameterName());
		if (obj == null) {
			Tab.insert(Obj.Var, parameter.getParameterName(), parameter.getType().struct);

			numOfFormPars++;
		} else {
			report_error("Greska na liniji " + parameter.getLine() + ": Promenljiva " + parameter.getParameterName() + " vec deklarisana! ", null);
		}
	}

	public void visit(ParamenterArray parameterArray) {

		Obj obj = Tab.currentScope.findSymbol(parameterArray.getParameterName());

		if (obj == null) {
			numOfFormPars++;
			 Tab.insert(Obj.Var, parameterArray.getParameterName(),
					new Struct(Struct.Array, parameterArray.getType().struct));

		} else {
			report_error("Greska na liniji " + parameterArray.getLine() + ": Promenljiva "
					+ parameterArray.getParameterName() + " vec deklarisana! ", null);
		}
	}

	public void visit(PrintStmt printStmt) {
		Struct kind = printStmt.getExpr().struct;
		boolean element = false;
		if (kind != Tab.noType) {
			if (kind.getKind() == Struct.Array && bracketFlag)
				element = true;
			if (printStmt.getExpr().struct != Tab.charType && printStmt.getExpr().struct != Tab.intType
					&& printStmt.getExpr().struct != boolType && !element) {
				report_error("Greska na liniji " + printStmt.getLine()
						+ " : operand PRINT instrukcije nije odgovarajuceg tipa", null);
			}
		}
	}

	public void visit(DesignatorAssign designatorAssign) {

		Obj kind = designatorAssign.getDesignator().obj;
	
		if (kind != Tab.noObj) {
			if (kind.getKind() != Obj.Var && kind.getKind() != Obj.Fld && kind.getKind()!=Obj.Elem) {
				report_error("Greska na liniji " + designatorAssign.getLine() + ": Promenljiva " + designatorAssign.getDesignator().obj.getName()
						+ " mora biti polje, element niza ili promenljiva! ", null);
			}
				if (!designatorAssign.getDesignator().obj.getType().assignableTo(designatorAssign.getExpr().struct)) {
					report_error("Greska na liniji " + designatorAssign.getLine() + ": Promenljivoj "
							+ designatorAssign.getDesignator().obj.getName() + " nije moguce dodeliti vrednost ", null);
				}
		}
		bracketFlag = false;
		dotFlag = false;
	}

	public void visit(DesignatorIncrement designatorAssign) {

		int kind = designatorAssign.getDesignator().obj.getKind();
		if (kind != Obj.Var && kind != Obj.Fld && kind!=Obj.Elem) {
			report_error("Promenljiva " + designatorAssign.getDesignator().obj.getName()
					+ " mora biti polje, element niza ili promenljiva! ", null);
		}
		if (designatorAssign.getDesignator().obj.getType() != Tab.intType) {
			report_error("Promenljiva " + designatorAssign.getDesignator().obj.getName() + " mora biti tipa int! ",
					null);
		}
	}

	public void visit(DesignatorDecrement designatorAssign) {

		int kind = designatorAssign.getDesignator().obj.getKind();
		if (kind != Obj.Fld && kind != Obj.Var && kind!=Obj.Elem) {
			
			report_error("Greska na liniji " + designatorAssign.getLine()
					+ " : operand READ instrukcije mora biti polje, element niza ili promenljiva!", null);
	}
		if (designatorAssign.getDesignator().obj.getType() != Tab.intType) {
			report_error("Promenljiva " + designatorAssign.getDesignator().obj.getName() + " mora biti tipa int! ",
					null);
		}
	}

	public void visit(Empty empty) {
		
		dowhile = true;
	}

	public void visit(DoWhileStmt empty) {
		
		dowhile = false;
	}

	public void visit(BreakStmt breakStmt) {
		if (!dowhile) {
			report_error("Greska na liniji " + breakStmt.getLine() + " Break instrukcija van do while petlje", null);
		}

	}

	public void visit(ContinueStmt continueStmt) {
		if (!dowhile) {
			report_error("Greska na liniji " + continueStmt.getLine() + " Continue instrukcija van do while petlje",
					null);
		}

	}

	public void visit(ReadStmt readStmt) {
		int kind = readStmt.getDesignator().obj.getKind();
		if (kind != Obj.Fld && kind != Obj.Var && kind!=Obj.Elem) {
			
			report_error("Greska na liniji " + readStmt.getLine()
					+ " : operand READ instrukcije mora biti polje, element niza ili promenljiva!", null);
	}
	
			if (readStmt.getDesignator().obj.getType() != Tab.charType
					&& readStmt.getDesignator().obj.getType() != Tab.intType
					&& readStmt.getDesignator().obj.getType() != boolType) {
				report_error("Greska na liniji " + readStmt.getLine()
						+ " : operand READ instrukcije nije odgovarajuceg tipa", null);
			}
		
		bracketFlag = false;
		dotFlag = false;
	}

	public void visit(PrintNumStmt printStmt) {
		if (printStmt.getExpr().struct != Tab.charType && printStmt.getExpr().struct != Tab.intType
				&& printStmt.getExpr().struct != boolType) {
			report_error(
					"Greska na liniji " + printStmt.getLine() + " : operand PRINT instrukcije nije odgovarajuceg tipa",
					null);
		}
	}

	public void visit(FormalPars formPars) {
		currentMethod.setLevel(numOfFormPars);

	}

	public void visit(FormalPar formPar) {
		currentMethod.setLevel(numOfFormPars);
	}

	public void visit(NoFormalPar noPars) {
		currentMethod.setLevel(numOfFormPars);

	}
	
	public void visit(ActParsList actPar) {
		pars.add(actPar.getExpr().struct);
	}

	public void visit(OneActPars actPar) {
		pars.add(actPar.getExpr().struct);
	}
	public void visit(PlusOp addOp) {
		addOp.obj=new Obj(Obj.Var, "plus", Tab.charType);
	}
	public void visit(MinusOp addOp) {
		addOp.obj=new Obj(Obj.Var, "minus", Tab.charType);
	}
	public void visit(DivOp addOp) {
		addOp.obj=new Obj(Obj.Var, "div", Tab.charType);
	}
	public void visit(MulOperation addOp) {
		addOp.obj=new Obj(Obj.Var, "mul", Tab.charType);
	}
	public void visit(ModOp addOp) {
		addOp.obj=new Obj(Obj.Var, "mod", Tab.charType);
	}
	public boolean passed() {
		return !errorDetected;
	}
	
	
	public void visit(EqualOp eqOp) {
		eqOp.obj=new Obj(0,"equal", Tab.noType);
	}
	public void visit(DiffOp eqOp) {
		eqOp.obj=new Obj(1,"diff", Tab.noType);
	}
	public void visit(GtOp eqOp) {
		eqOp.obj=new Obj(4,"gt", Tab.noType);
	}
	public void visit(GteOp eqOp) {
		eqOp.obj=new Obj(5,"gte", Tab.noType);
	}
	public void visit(LtOp eqOp) {
		eqOp.obj=new Obj(2,"lt", Tab.noType);
	}
	public void visit(LteOp lte) {
		lte.obj=new Obj(3,"equal", Tab.noType);
	}
	public void visit(OneOrCondition oneOr) {
		oneOr.obj=oneOr.getCondTerm().obj;
	}
	public void visit(OneAndTerm oneAnd) {
		oneAnd.obj=oneAnd.getCondFact().obj;
	}
	public void visit(RelCond relCond) {
	 if(!relCond.getExpr().struct.compatibleWith(relCond.getExpr1().struct)){
		 report_error("Greska na liniji "+relCond.getLine()+ ": Tipovi operanada nisu kompatibilni", null);
	 }
		relCond.obj=relCond.getRelop().obj;
	}
	public void visit(IfStatement ifStmt) {
		
	}

	

	
}
