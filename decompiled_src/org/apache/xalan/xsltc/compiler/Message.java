package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class Message extends Instruction {
   private boolean _terminate = false;

   public void parseContents(Parser parser) {
      String termstr = this.getAttribute("terminate");
      if (termstr != null) {
         this._terminate = termstr.equals("yes");
      }

      this.parseChildren(parser);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      this.typeCheckContents(stable);
      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append(classGen.loadTranslet());
      switch (this.elementCount()) {
         case 0:
            il.append((CompoundInstruction)(new PUSH(cpg, "")));
            break;
         case 1:
            SyntaxTreeNode child = (SyntaxTreeNode)this.elementAt(0);
            if (child instanceof Text) {
               il.append((CompoundInstruction)(new PUSH(cpg, ((Text)child).getText())));
               break;
            }
         default:
            il.append(methodGen.loadHandler());
            il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("org.apache.xml.serializer.ToXMLStream"))));
            il.append(methodGen.storeHandler());
            il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("java.io.StringWriter"))));
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(cpg.addMethodref("java.io.StringWriter", "<init>", "()V"))));
            il.append(methodGen.loadHandler());
            il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(cpg.addMethodref("org.apache.xml.serializer.ToXMLStream", "<init>", "()V"))));
            il.append(methodGen.loadHandler());
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.SWAP);
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xml.serializer.SerializerBase", "setWriter", "(Ljava/io/Writer;)V"))));
            il.append(methodGen.loadHandler());
            il.append((CompoundInstruction)(new PUSH(cpg, "UTF-8")));
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xml.serializer.SerializerBase", "setEncoding", "(Ljava/lang/String;)V"))));
            il.append(methodGen.loadHandler());
            il.append(InstructionConstants.ICONST_1);
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xml.serializer.SerializerBase", "setOmitXMLDeclaration", "(Z)V"))));
            il.append(methodGen.loadHandler());
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xml.serializer.SerializerBase", "startDocument", "()V"))));
            this.translateContents(classGen, methodGen);
            il.append(methodGen.loadHandler());
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xml.serializer.SerializerBase", "endDocument", "()V"))));
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("java.io.StringWriter", "toString", "()Ljava/lang/String;"))));
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.SWAP);
            il.append(methodGen.storeHandler());
      }

      il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "displayMessage", "(Ljava/lang/String;)V"))));
      if (this._terminate) {
         int einit = cpg.addMethodref("java.lang.RuntimeException", "<init>", "(Ljava/lang/String;)V");
         il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("java.lang.RuntimeException"))));
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((CompoundInstruction)(new PUSH(cpg, "Termination forced by an xsl:message instruction")));
         il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(einit)));
         il.append(InstructionConstants.ATHROW);
      }

   }
}
