package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class NamespaceAlias extends TopLevelElement {
   private String sPrefix;
   private String rPrefix;

   public void parseContents(Parser parser) {
      this.sPrefix = this.getAttribute("stylesheet-prefix");
      this.rPrefix = this.getAttribute("result-prefix");
      parser.getSymbolTable().addPrefixAlias(this.sPrefix, this.rPrefix);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
   }
}
