package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class TransletOutput extends Instruction {
   private Expression _filename;
   private boolean _append;

   public void display(int indent) {
      this.indent(indent);
      Util.println("TransletOutput: " + this._filename);
   }

   public void parseContents(Parser parser) {
      String filename = this.getAttribute("file");
      String append = this.getAttribute("append");
      if (filename == null || filename.equals("")) {
         this.reportError(this, parser, "REQUIRED_ATTR_ERR", "file");
      }

      this._filename = AttributeValue.create(this, filename, parser);
      if (append == null || !append.toLowerCase().equals("yes") && !append.toLowerCase().equals("true")) {
         this._append = false;
      } else {
         this._append = true;
      }

      this.parseChildren(parser);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      Type type = this._filename.typeCheck(stable);
      if (!(type instanceof StringType)) {
         this._filename = new CastExpr(this._filename, Type.String);
      }

      this.typeCheckContents(stable);
      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      boolean isSecureProcessing = classGen.getParser().getXSLTC().isSecureProcessing();
      int open;
      if (isSecureProcessing) {
         open = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "unallowed_extension_elementF", "(Ljava/lang/String;)V");
         il.append((CompoundInstruction)(new PUSH(cpg, "redirect")));
         il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(open)));
      } else {
         il.append(methodGen.loadHandler());
         open = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "openOutputHandler", "(Ljava/lang/String;Z)Lorg/apache/xml/serializer/SerializationHandler;");
         int close = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "closeOutputHandler", "(Lorg/apache/xml/serializer/SerializationHandler;)V");
         il.append(classGen.loadTranslet());
         this._filename.translate(classGen, methodGen);
         il.append((CompoundInstruction)(new PUSH(cpg, this._append)));
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(open)));
         il.append(methodGen.storeHandler());
         this.translateContents(classGen, methodGen);
         il.append(classGen.loadTranslet());
         il.append(methodGen.loadHandler());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(close)));
         il.append(methodGen.storeHandler());
      }
   }
}
