package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class ApplyImports extends Instruction {
   private QName _modeName;
   private int _precedence;

   public void display(int indent) {
      this.indent(indent);
      Util.println("ApplyTemplates");
      this.indent(indent + 4);
      if (this._modeName != null) {
         this.indent(indent + 4);
         Util.println("mode " + this._modeName);
      }

   }

   public boolean hasWithParams() {
      return this.hasContents();
   }

   private int getMinPrecedence(int max) {
      Stylesheet includeRoot;
      for(includeRoot = this.getStylesheet(); includeRoot._includedFrom != null; includeRoot = includeRoot._includedFrom) {
      }

      return includeRoot.getMinimumDescendantPrecedence();
   }

   public void parseContents(Parser parser) {
      Stylesheet stylesheet = this.getStylesheet();
      stylesheet.setTemplateInlining(false);
      Template template = this.getTemplate();
      this._modeName = template.getModeName();
      this._precedence = template.getImportPrecedence();
      stylesheet = parser.getTopLevelStylesheet();
      this.parseChildren(parser);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      this.typeCheckContents(stable);
      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      Stylesheet stylesheet = classGen.getStylesheet();
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int current = methodGen.getLocalIndex("current");
      il.append(classGen.loadTranslet());
      il.append(methodGen.loadDOM());
      il.append(methodGen.loadIterator());
      il.append(methodGen.loadHandler());
      il.append(methodGen.loadCurrentNode());
      int maxPrecedence;
      if (stylesheet.hasLocalParams()) {
         il.append(classGen.loadTranslet());
         maxPrecedence = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "pushParamFrame", "()V");
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(maxPrecedence)));
      }

      maxPrecedence = this._precedence;
      int minPrecedence = this.getMinPrecedence(maxPrecedence);
      Mode mode = stylesheet.getMode(this._modeName);
      String functionName = mode.functionName(minPrecedence, maxPrecedence);
      String className = classGen.getStylesheet().getClassName();
      String signature = classGen.getApplyTemplatesSigForImport();
      int applyTemplates = cpg.addMethodref(className, functionName, signature);
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(applyTemplates)));
      if (stylesheet.hasLocalParams()) {
         il.append(classGen.loadTranslet());
         int pushFrame = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "popParamFrame", "()V");
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(pushFrame)));
      }

   }
}
