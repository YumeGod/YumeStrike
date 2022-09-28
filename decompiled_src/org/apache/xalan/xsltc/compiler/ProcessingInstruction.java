package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

final class ProcessingInstruction extends Instruction {
   private AttributeValue _name;
   private boolean _isLiteral = false;

   public void parseContents(Parser parser) {
      String name = this.getAttribute("name");
      if (name.length() > 0) {
         this._isLiteral = Util.isLiteral(name);
         if (this._isLiteral && !XML11Char.isXML11ValidNCName(name)) {
            ErrorMsg err = new ErrorMsg("INVALID_NCNAME_ERR", name, this);
            parser.reportError(3, err);
         }

         this._name = AttributeValue.create(this, name, parser);
      } else {
         this.reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
      }

      if (name.equals("xml")) {
         this.reportError(this, parser, "ILLEGAL_PI_ERR", "xml");
      }

      this.parseChildren(parser);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      this._name.typeCheck(stable);
      this.typeCheckContents(stable);
      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      if (!this._isLiteral) {
         LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType("Ljava/lang/String;"), il.getEnd());
         this._name.translate(classGen, methodGen);
         il.append((org.apache.bcel.generic.Instruction)(new ASTORE(nameValue.getIndex())));
         il.append((org.apache.bcel.generic.Instruction)(new ALOAD(nameValue.getIndex())));
         int check = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "checkNCName", "(Ljava/lang/String;)V");
         il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(check)));
         il.append(methodGen.loadHandler());
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((org.apache.bcel.generic.Instruction)(new ALOAD(nameValue.getIndex())));
      } else {
         il.append(methodGen.loadHandler());
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         this._name.translate(classGen, methodGen);
      }

      il.append(classGen.loadTranslet());
      il.append((org.apache.bcel.generic.Instruction)(new GETFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "stringValueHandler", "Lorg/apache/xalan/xsltc/runtime/StringValueHandler;"))));
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
      il.append(methodGen.storeHandler());
      this.translateContents(classGen, methodGen);
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "getValueOfPI", "()Ljava/lang/String;"))));
      int processingInstruction = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "processingInstruction", "(Ljava/lang/String;Ljava/lang/String;)V");
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(processingInstruction, 3)));
      il.append(methodGen.storeHandler());
   }
}
