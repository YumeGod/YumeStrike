package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

public abstract class Pattern extends Expression {
   public abstract Type typeCheck(SymbolTable var1) throws TypeCheckError;

   public abstract void translate(ClassGenerator var1, MethodGenerator var2);

   public abstract double getPriority();
}
