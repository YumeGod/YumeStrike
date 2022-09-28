package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class Comment extends Instruction {
   public void parseContents(Parser parser) {
      this.parseChildren(parser);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      this.typeCheckContents(stable);
      return Type.String;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      Text rawText = null;
      if (this.elementCount() == 1) {
         Object content = this.elementAt(0);
         if (content instanceof Text) {
            rawText = (Text)content;
         }
      }

      int comment;
      if (rawText != null) {
         il.append(methodGen.loadHandler());
         if (rawText.canLoadAsArrayOffsetLength()) {
            rawText.loadAsArrayOffsetLength(classGen, methodGen);
            comment = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "comment", "([CII)V");
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(comment, 4)));
         } else {
            il.append((CompoundInstruction)(new PUSH(cpg, rawText.getText())));
            comment = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "comment", "(Ljava/lang/String;)V");
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(comment, 2)));
         }
      } else {
         il.append(methodGen.loadHandler());
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append(classGen.loadTranslet());
         il.append((org.apache.bcel.generic.Instruction)(new GETFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "stringValueHandler", "Lorg/apache/xalan/xsltc/runtime/StringValueHandler;"))));
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append(methodGen.storeHandler());
         this.translateContents(classGen, methodGen);
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "getValue", "()Ljava/lang/String;"))));
         comment = cpg.addInterfaceMethodref("org.apache.xml.serializer.SerializationHandler", "comment", "(Ljava/lang/String;)V");
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(comment, 2)));
         il.append(methodGen.storeHandler());
      }

   }
}
