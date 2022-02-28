package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class Compiler {

	
	public static void main(String[] args) throws Exception {
		

		
		Reader br = null;
		try {
			 File sourceCode = new File(args[0]);
			System.out.println("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        
	        Program prog = (Program)(s.value); 
	        Tab.init();
			// ispis sintaksnog stabla
	        System.out.println(prog.toString(""));
			 System.out.println("===================================");

			// ispis prepoznatih programskih konstrukcija
			SemanticAnalyzer v = new SemanticAnalyzer();
			prog.traverseBottomUp(v); 
			
			 System.out.println("===================================");
			Tab.dump();
			
			if(!p.errorDetected && v.passed()){
				
				File objFile = new File(args[1]);
	            if (objFile.exists())
	                objFile.delete();
				
				CodeGenerator codeGenerator = new CodeGenerator();
				prog.traverseBottomUp(codeGenerator);
				Code.dataSize = v.nVars;
				Code.mainPc = codeGenerator.getMainPc();
				Code.write(new FileOutputStream(objFile));
				 System.out.println("Compilation successful!");
			}else{
				 System.out.println("Compilation unsuccessful!");
			}
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) {  System.out.println(e1.getMessage()); }
		}

	}
	
	
}