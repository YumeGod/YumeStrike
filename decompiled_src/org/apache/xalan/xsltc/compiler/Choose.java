package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class Choose extends Instruction {
   public void display(int indent) {
      this.indent(indent);
      Util.println("Choose");
      this.indent(indent + 4);
      this.displayContents(indent + 4);
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      Vector whenElements = new Vector();
      Otherwise otherwise = null;
      Enumeration elements = this.elements();
      ErrorMsg error = null;
      int line = this.getLineNumber();

      while(elements.hasMoreElements()) {
         Object element = elements.nextElement();
         if (element instanceof When) {
            whenElements.addElement(element);
         } else if (element instanceof Otherwise) {
            if (otherwise == null) {
               otherwise = (Otherwise)element;
            } else {
               error = new ErrorMsg("MULTIPLE_OTHERWISE_ERR", this);
               this.getParser().reportError(3, error);
            }
         } else if (element instanceof Text) {
            ((Text)element).ignore();
         } else {
            error = new ErrorMsg("WHEN_ELEMENT_ERR", this);
            this.getParser().reportError(3, error);
         }
      }

      if (whenElements.size() == 0) {
         error = new ErrorMsg("MISSING_WHEN_ERR", this);
         this.getParser().reportError(3, error);
      } else {
         InstructionList il = methodGen.getInstructionList();
         BranchHandle nextElement = null;
         Vector exitHandles = new Vector();
         InstructionHandle exit = null;

         Expression test;
         InstructionHandle truec;
         for(Enumeration whens = whenElements.elements(); whens.hasMoreElements(); test.backPatchTrueList(truec.getNext())) {
            When when = (When)whens.nextElement();
            test = when.getTest();
            truec = il.getEnd();
            if (nextElement != null) {
               nextElement.setTarget(il.append(InstructionConstants.NOP));
            }

            test.translateDesynthesized(classGen, methodGen);
            if (test instanceof FunctionCall) {
               FunctionCall call = (FunctionCall)test;

               try {
                  Type type = call.typeCheck(this.getParser().getSymbolTable());
                  if (type != Type.Boolean) {
                     test._falseList.add(il.append((BranchInstruction)(new IFEQ((InstructionHandle)null))));
                  }
               } catch (TypeCheckError var18) {
               }
            }

            truec = il.getEnd();
            if (!when.ignore()) {
               when.translateContents(classGen, methodGen);
            }

            exitHandles.addElement(il.append((BranchInstruction)(new GOTO((InstructionHandle)null))));
            if (!whens.hasMoreElements() && otherwise == null) {
               test.backPatchFalseList(exit = il.append(InstructionConstants.NOP));
            } else {
               nextElement = il.append((BranchInstruction)(new GOTO((InstructionHandle)null)));
               test.backPatchFalseList(nextElement);
            }
         }

         if (otherwise != null) {
            nextElement.setTarget(il.append(InstructionConstants.NOP));
            otherwise.translateContents(classGen, methodGen);
            exit = il.append(InstructionConstants.NOP);
         }

         Enumeration exitGotos = exitHandles.elements();

         while(exitGotos.hasMoreElements()) {
            BranchHandle gotoExit = (BranchHandle)exitGotos.nextElement();
            gotoExit.setTarget(exit);
         }

      }
   }
}
