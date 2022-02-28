package rs.ac.bg.etf.pp1;


import rs.ac.bg.etf.pp1.ast.Array;

import rs.ac.bg.etf.pp1.ast.ParamenterArray;
import rs.ac.bg.etf.pp1.ast.Parameter;
import rs.ac.bg.etf.pp1.ast.Var;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {

	protected int count;
	
	public int getCount(){
		return count;
	}
	
	public static class FormParamCounter extends CounterVisitor{
		
		public void visit(Parameter formParamDecl){
			count++;
		}
		public void visit(ParamenterArray formParamDecl){
			count++;
		}
	}
	
	public static class VarCounter extends CounterVisitor{
		
		public void visit(Var varDecl){
			count++;
		}
		public void visit(Array varDecl){
			count++;
		}
	}
}
