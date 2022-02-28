package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.Stack;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.AndDummy;
import rs.ac.bg.etf.pp1.ast.BoolConstFactor;
import rs.ac.bg.etf.pp1.ast.BreakStmt;
import rs.ac.bg.etf.pp1.ast.CharConstFactor;
import rs.ac.bg.etf.pp1.ast.ContinueStmt;

import rs.ac.bg.etf.pp1.ast.DesignatorAssign;
import rs.ac.bg.etf.pp1.ast.DesignatorDecrement;
import rs.ac.bg.etf.pp1.ast.DesignatorFactor;
import rs.ac.bg.etf.pp1.ast.DesignatorIncrement;
import rs.ac.bg.etf.pp1.ast.DesignatorNoPars;
import rs.ac.bg.etf.pp1.ast.DesignatorNoParsFactor;
import rs.ac.bg.etf.pp1.ast.DesignatorPars;
import rs.ac.bg.etf.pp1.ast.DesignatorParsFactor;
import rs.ac.bg.etf.pp1.ast.DiffOp;
import rs.ac.bg.etf.pp1.ast.DoWhileStmt;
import rs.ac.bg.etf.pp1.ast.ElseDummy;
import rs.ac.bg.etf.pp1.ast.Empty;
import rs.ac.bg.etf.pp1.ast.EqualOp;
import rs.ac.bg.etf.pp1.ast.ExprCond;
import rs.ac.bg.etf.pp1.ast.Factors;

import rs.ac.bg.etf.pp1.ast.FieldArrayName;
import rs.ac.bg.etf.pp1.ast.FieldName;
import rs.ac.bg.etf.pp1.ast.GtOp;
import rs.ac.bg.etf.pp1.ast.GteOp;
import rs.ac.bg.etf.pp1.ast.IfCondParen_Cond;

import rs.ac.bg.etf.pp1.ast.IfDummy;
import rs.ac.bg.etf.pp1.ast.IfStatement;
import rs.ac.bg.etf.pp1.ast.LeftBracket;
import rs.ac.bg.etf.pp1.ast.LtOp;
import rs.ac.bg.etf.pp1.ast.LteOp;

import rs.ac.bg.etf.pp1.ast.MethodTypeName;

import rs.ac.bg.etf.pp1.ast.NegativeExpr;
import rs.ac.bg.etf.pp1.ast.NewFactor;
import rs.ac.bg.etf.pp1.ast.NewFactorExpr;
import rs.ac.bg.etf.pp1.ast.NumConstFactor;
import rs.ac.bg.etf.pp1.ast.OptElseBranch_Eps;
import rs.ac.bg.etf.pp1.ast.OptElseBranch_NoEps;
import rs.ac.bg.etf.pp1.ast.OrDummy;
import rs.ac.bg.etf.pp1.ast.PrintStmt;
import rs.ac.bg.etf.pp1.ast.ProgName;
import rs.ac.bg.etf.pp1.ast.ReadStmt;
import rs.ac.bg.etf.pp1.ast.RegularMethod;
import rs.ac.bg.etf.pp1.ast.ReturnExprStmt;

import rs.ac.bg.etf.pp1.ast.ReturnStmt;
import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.ac.bg.etf.pp1.ast.Terms;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.ast.VoidMethod;
import rs.ac.bg.etf.pp1.ast.VoidTypeName;
import rs.ac.bg.etf.pp1.ast.WhileDummy;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;


public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	
	public int getMainPc(){
		return mainPc;
	}

	public int varrCnt=0;
	public int fromCnt=0;
	private Stack<Integer> doWhileTopStack;
    private ArrayList<Integer> condTrueFixupList;
    private Stack<ArrayList<Integer>> condFalseFixupStack;
    private Stack<ArrayList<Integer>> breakFixupStack;
    private Stack<ArrayList<Integer>> contFixupStack;
    private Stack<Integer> skipElseJmpAdrStack;
    private Stack<Integer> relop;
	
	Logger log = Logger.getLogger(getClass());

	  CodeGenerator() {
	        super();
	   
	        doWhileTopStack = new Stack<>();
	        condTrueFixupList = new ArrayList<>();
	        condFalseFixupStack = new Stack<>();
	        breakFixupStack = new Stack<>();
	        contFixupStack = new Stack<>();
	        skipElseJmpAdrStack = new Stack<>();
	    

	        relop = new Stack<>();
	   
	  }
	  public void visit(ProgName progName) {
		  defineOrdObj();
		  defineChrObj();
		  defineLenObj();
	  }
	  public void defineOrdObj()
	  {
		  Obj ordObj=Tab.find("ord");
		  ordObj.setAdr(Code.pc);
		  Code.put(Code.return_);
		  		
	  }
	  public void defineChrObj()
	  {
		  Obj ordObj=Tab.find("ord");
		  ordObj.setAdr(Code.pc);
		  Code.put(Code.return_);
		  		
	  }
	  public void defineLenObj()
	  {
		  Obj lenObj=Tab.find("len");
		  lenObj.setAdr(Code.pc);
		  Code.put(Code.arraylength);
		  Code.put(Code.return_);
		  		
	  }
	  public void visit(PrintStmt printStmt){
		if(printStmt.getExpr().struct == Tab.intType){
			Code.loadConst(5);
			Code.put(Code.print);
		}else{
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	public void visit(ReadStmt readStatement){
		if(readStatement.getDesignator().obj.getType() == Tab.intType){
			Code.put(Code.read);
		}else{
			Code.put(Code.bread);
		}
		
		Code.store(readStatement.getDesignator().obj);
	}
	public void visit(BoolConstFactor boolFactor) {
		String value = boolFactor.getB1();
		if (value.equals("true")) {
			Code.loadConst(1);
		} else {
			Code.loadConst(0);
		}
	}

	public void visit(NumConstFactor cnst){
	
		Obj con = Tab.insert(Obj.Con, "$", cnst.struct);
		con.setLevel(0);
		con.setAdr(cnst.getN1());
		
		Code.load(con);
		
		
	}
	public void visit(CharConstFactor cnst){
		
		Obj con = Tab.insert(Obj.Con, "$", cnst.struct);
		con.setLevel(0);
		con.setAdr(cnst.getC1());
		
		Code.load(con);
		
		                                                                                  
	}                                                                                     
	public void visit(DesignatorIncrement designatorIncrement) {                          
		Obj obj = designatorIncrement.getDesignator().obj;                                
		                                                                                
		  if (obj.getKind() == Obj.Elem) { Code.put(Code.dup2); }                        
		                                                                                
		 
		if(currentRecord!=null)
			Code.load(currentRecord);
		Code.load(obj);
		Code.loadConst(1);                                                                
		Code.put(Code.add);                                                               
		                                                                                  
		Code.store(obj);                                                                  
	}

	public void visit(DesignatorDecrement designatorDecrement) {
		Obj obj = designatorDecrement.getDesignator().obj;
		
		  if (obj.getKind() == Obj.Elem) { Code.put(Code.dup2); }
		 
		if(currentRecord!=null)
			Code.load(currentRecord);
		Code.load(obj);
		Code.loadConst(1);
		Code.put(Code.sub);
		Code.store(obj);
	}

	
public void visit(VoidTypeName methodTypeName){
		
		if("main".equalsIgnoreCase(methodTypeName.getMethName())){
			mainPc = Code.pc;
		}
		methodTypeName.obj.setAdr(Code.pc);
		// Collect arguments and local variables
		SyntaxNode methodNode = methodTypeName.getParent();
	
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		
		varrCnt=varCnt.getCount();
		fromCnt=fpCnt.getCount();
		// Generate the entry
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	
	}
	public void visit(MethodTypeName methodTypeName){
		
		if("main".equalsIgnoreCase(methodTypeName.getMethName())){
			mainPc = Code.pc;
		}
		methodTypeName.obj.setAdr(Code.pc);
		// Collect arguments and local variables
		SyntaxNode methodNode = methodTypeName.getParent();
	
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		
		
		varrCnt=varCnt.getCount();
		fromCnt=fpCnt.getCount();
		// Generate the entry
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	
	}
	
	public void visit(RegularMethod methodDecl){
		
		  Code.put(Code.exit); Code.put(Code.return_);
		 
	}
	public void visit(VoidMethod methodDecl){
		 Code.put(Code.exit); 
		Code.put(Code.return_);
		
		
	}

	
	public void visit(DesignatorAssign assignment){
		Code.store(assignment.getDesignator().obj);
	}
	
	public void visit(DesignatorFactor designator){
	
		Code.load(designator.getDesignator().obj);
	
		
	}


	public void visit(DesignatorNoParsFactor funcCall){
		
		Obj functionObj = funcCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		
		Code.put2(offset);
	}
	
	public void visit(DesignatorNoPars procCall){
		
		Obj functionObj = procCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		if(procCall.getDesignator().obj.getType() != Tab.noType){
			Code.put(Code.pop);
		}
	}
	public void visit(DesignatorParsFactor funcCall){
	
		Obj functionObj = funcCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		
		Code.put2(offset);
	}
	
	public void visit(DesignatorPars procCall){
	
		Obj functionObj = procCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		if(procCall.getDesignator().obj.getType() != Tab.noType){
			Code.put(Code.pop);
		}
		
	}
	boolean returnFound=false;
	public void visit(ReturnExprStmt returnExpr){
		returnFound=true;
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(ReturnStmt returnNoExpr){
		returnFound=true;
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	
	
	public void visit(Terms addExpr){ 
		
		if(addExpr.getAddop().obj.getName().equals("plus")) {
			Code.put(Code.add);
		}else {
			if(addExpr.getAddop().obj.getName().equals("minus")) {
				Code.put(Code.sub);
			}
		}
		
	}
	

	
	public void visit(Factors addExpr){ 
		if(addExpr.getMulop().obj.getName().equals("mul")) {
			Code.put(Code.mul);
		}else {
			if(addExpr.getMulop().obj.getName().equals("div")) {
				Code.put(Code.div);
			}
			else {
				if(addExpr.getMulop().obj.getName().equals("mod")) {
					Code.put(Code.rem);
				}
			}
		}
		
	}

	public void visit(LeftBracket lBracket) {
		
		Code.load(lBracket.obj);
		
	}
	
	public void visit(NegativeExpr negExpr){
		Code.put(Code.neg);
	}

	
	public void visit(NewFactor newFactor) {
		
		Code.put(Code.new_);
		Code.put2(newFactor.struct.getNumberOfFields()*4);
		
		
	}
	Obj currentRecord;
	public void visit(FieldName fldName) {
//		SyntaxNode parent =fldName.getParent();
//		if(parent.getClass()==Field.class) {
			Code.load(fldName.obj);
			if(fldName.getParent().getClass()!=FieldArrayName.class){
			currentRecord=fldName.obj;
			}
			else
				currentRecord=null;
//		}
	}
	
	
	  public void visit(FieldArrayName fldArrName) { 
			/* Code.load(Tab.find(fldArrName.getFieldName().getDesName())); */
		  Code.load(fldArrName.obj); 
	  }
	 

	/*
	 * public void visit(FieldArray fld) {
	 * Code.loadConst(Tab.find(fld.getFieldArrayName().getDesName()).getAdr()); }
	 */
	public void visit(NewFactorExpr newFactor) {
		
		Code.put(Code.newarray);
		if (newFactor.getExpr().struct == Tab.intType)
			Code.put(1);
		else
			Code.put(0);
	
	}
	int pc;
	public void visit(DoWhileStmt statement_doWhile) {
        int top = doWhileTopStack.pop();

        Code.put(Code.jcc + relop.pop());
        Code.put2(top - Code.pc + 1);

        for (int adr : condFalseFixupStack.peek()) {
            Code.fixup(adr);
        }

        for (int adr : condTrueFixupList) {
            Code.put2(adr, (top - adr + 1));
        }
        condTrueFixupList.clear();

        for (int adr : breakFixupStack.peek()) {
            Code.fixup(adr);
        }

        condFalseFixupStack.pop();
        breakFixupStack.pop();
        contFixupStack.pop();
    }

    public void visit(Empty doDummy) {
	    doWhileTopStack.push(Code.pc);
        breakFixupStack.push(new ArrayList<>());
        contFixupStack.push(new ArrayList<>());
    }

    public void visit(WhileDummy whileDummy) {
        for (int adr : contFixupStack.peek()) {
            Code.fixup(adr);
        }

        condFalseFixupStack.push(new ArrayList<>());
    }

    public void visit(BreakStmt statement_break) {
        Code.putJump(0);
        breakFixupStack.peek().add(Code.pc - 2);
    }

    public void visit(ContinueStmt statement_cont) {
        Code.putJump(0);
        contFixupStack.peek().add(Code.pc - 2);
    }
    public void visit(EqualOp relop_eq) {
        relop.push(Code.eq);
    }

    public void visit(DiffOp relop_neq) {
        relop.push(Code.ne);
    }

    public void visit(GtOp relop_gre) {
        relop.push(Code.gt);
    }

    public void visit(GteOp relop_geq) {
        relop.push(Code.ge);
    }

    public void visit(LtOp relop_les) {
        relop.push(Code.lt);
    }

    public void visit(LteOp relop_leq) {
        relop.push(Code.le);
    }
    public void visit(OrDummy orDummy) {
        Code.put(Code.jcc + relop.pop());
        Code.put2(0);
        condTrueFixupList.add(Code.pc - 2);

        for (int adr : condFalseFixupStack.peek()) {
            Code.fixup(adr);
        }
    }

    public void visit(AndDummy andDummy) {
        Code.putFalseJump(relop.pop(), 0);
        condFalseFixupStack.peek().add(Code.pc - 2);
    }
    public void visit(IfStatement statement_if) {
        condFalseFixupStack.pop();
    }

	public void visit(IfCondParen_Cond ifCondParen_cond) {
        Code.putFalseJump(relop.pop(), 0);
        condFalseFixupStack.peek().add(Code.pc - 2);

        for (int adr : condTrueFixupList) {
            Code.fixup(adr);
        }
        condTrueFixupList.clear();
    }

    public void visit(IfDummy ifDummy) {
        condFalseFixupStack.add(new ArrayList<>());
    }

    public void visit(OptElseBranch_NoEps optElseBranch_noEps) {
        int skipElseJmpAdr = skipElseJmpAdrStack.pop();
        Code.fixup(skipElseJmpAdr);
    }

    public void visit(ElseDummy elseDummy) {
        Code.putJump(0);
        skipElseJmpAdrStack.add(Code.pc - 2);

        for (int adr : condFalseFixupStack.peek()) {
            Code.fixup(adr);
        }
    }

    public void visit(OptElseBranch_Eps optElseBranch_eps) {
        for (int adr : condFalseFixupStack.peek()) {
            Code.fixup(adr);
        }
    }
  
    public void visit(ExprCond condFact_expr) {
        Code.loadConst(0); // value of 'false'
        relop.push(Code.ne);
    }
}
