package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DLOAD;
import org.apache.bcel.generic.DSTORE;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.FlowList;

public final class RealType extends NumberType {
   // $FF: synthetic field
   static Class class$java$lang$Double;

   protected RealType() {
   }

   public String toString() {
      return "real";
   }

   public boolean identicalTo(Type other) {
      return this == other;
   }

   public String toSignature() {
      return "D";
   }

   public org.apache.bcel.generic.Type toJCType() {
      return org.apache.bcel.generic.Type.DOUBLE;
   }

   public int distanceTo(Type type) {
      if (type == this) {
         return 0;
      } else {
         return type == Type.Int ? 1 : Integer.MAX_VALUE;
      }
   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type) {
      if (type == Type.String) {
         this.translateTo(classGen, methodGen, (StringType)type);
      } else if (type == Type.Boolean) {
         this.translateTo(classGen, methodGen, (BooleanType)type);
      } else if (type == Type.Reference) {
         this.translateTo(classGen, methodGen, (ReferenceType)type);
      } else if (type == Type.Int) {
         this.translateTo(classGen, methodGen, (IntType)type);
      } else {
         ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", this.toString(), type.toString());
         classGen.getParser().reportError(2, err);
      }

   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append((Instruction)(new INVOKESTATIC(cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "realToString", "(D)Ljava/lang/String;"))));
   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
      InstructionList il = methodGen.getInstructionList();
      FlowList falsel = this.translateToDesynthesized(classGen, methodGen, type);
      il.append(InstructionConstants.ICONST_1);
      BranchHandle truec = il.append((BranchInstruction)(new GOTO((InstructionHandle)null)));
      falsel.backPatch(il.append(InstructionConstants.ICONST_0));
      truec.setTarget(il.append(InstructionConstants.NOP));
   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, IntType type) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append((Instruction)(new INVOKESTATIC(cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "realToInt", "(D)I"))));
   }

   public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
      FlowList flowlist = new FlowList();
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append((Instruction)InstructionConstants.DUP2);
      LocalVariableGen local = methodGen.addLocalVariable("real_to_boolean_tmp", org.apache.bcel.generic.Type.DOUBLE, il.getEnd(), (InstructionHandle)null);
      il.append((Instruction)(new DSTORE(local.getIndex())));
      il.append(InstructionConstants.DCONST_0);
      il.append(InstructionConstants.DCMPG);
      flowlist.add(il.append((BranchInstruction)(new IFEQ((InstructionHandle)null))));
      il.append((Instruction)(new DLOAD(local.getIndex())));
      il.append((Instruction)(new DLOAD(local.getIndex())));
      il.append(InstructionConstants.DCMPG);
      flowlist.add(il.append((BranchInstruction)(new IFNE((InstructionHandle)null))));
      return flowlist;
   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append((Instruction)(new NEW(cpg.addClass("java.lang.Double"))));
      il.append((Instruction)InstructionConstants.DUP_X2);
      il.append((Instruction)InstructionConstants.DUP_X2);
      il.append((Instruction)InstructionConstants.POP);
      il.append((Instruction)(new INVOKESPECIAL(cpg.addMethodref("java.lang.Double", "<init>", "(D)V"))));
   }

   public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
      InstructionList il = methodGen.getInstructionList();
      if (clazz == Character.TYPE) {
         il.append((Instruction)InstructionConstants.D2I);
         il.append((Instruction)InstructionConstants.I2C);
      } else if (clazz == Byte.TYPE) {
         il.append((Instruction)InstructionConstants.D2I);
         il.append((Instruction)InstructionConstants.I2B);
      } else if (clazz == Short.TYPE) {
         il.append((Instruction)InstructionConstants.D2I);
         il.append((Instruction)InstructionConstants.I2S);
      } else if (clazz == Integer.TYPE) {
         il.append((Instruction)InstructionConstants.D2I);
      } else if (clazz == Long.TYPE) {
         il.append((Instruction)InstructionConstants.D2L);
      } else if (clazz == Float.TYPE) {
         il.append((Instruction)InstructionConstants.D2F);
      } else if (clazz == Double.TYPE) {
         il.append(InstructionConstants.NOP);
      } else if (clazz.isAssignableFrom(class$java$lang$Double == null ? (class$java$lang$Double = class$("java.lang.Double")) : class$java$lang$Double)) {
         this.translateTo(classGen, methodGen, Type.Reference);
      } else {
         ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", this.toString(), clazz.getName());
         classGen.getParser().reportError(2, err);
      }

   }

   public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
      InstructionList il = methodGen.getInstructionList();
      if (clazz != Character.TYPE && clazz != Byte.TYPE && clazz != Short.TYPE && clazz != Integer.TYPE) {
         if (clazz == Long.TYPE) {
            il.append((Instruction)InstructionConstants.L2D);
         } else if (clazz == Float.TYPE) {
            il.append((Instruction)InstructionConstants.F2D);
         } else if (clazz == Double.TYPE) {
            il.append(InstructionConstants.NOP);
         } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", this.toString(), clazz.getName());
            classGen.getParser().reportError(2, err);
         }
      } else {
         il.append((Instruction)InstructionConstants.I2D);
      }

   }

   public void translateBox(ClassGenerator classGen, MethodGenerator methodGen) {
      this.translateTo(classGen, methodGen, Type.Reference);
   }

   public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append((Instruction)(new CHECKCAST(cpg.addClass("java.lang.Double"))));
      il.append((Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("java.lang.Double", "doubleValue", "()D"))));
   }

   public Instruction ADD() {
      return InstructionConstants.DADD;
   }

   public Instruction SUB() {
      return InstructionConstants.DSUB;
   }

   public Instruction MUL() {
      return InstructionConstants.DMUL;
   }

   public Instruction DIV() {
      return InstructionConstants.DDIV;
   }

   public Instruction REM() {
      return InstructionConstants.DREM;
   }

   public Instruction NEG() {
      return InstructionConstants.DNEG;
   }

   public Instruction LOAD(int slot) {
      return new DLOAD(slot);
   }

   public Instruction STORE(int slot) {
      return new DSTORE(slot);
   }

   public Instruction POP() {
      return InstructionConstants.POP2;
   }

   public Instruction CMP(boolean less) {
      return less ? InstructionConstants.DCMPG : InstructionConstants.DCMPL;
   }

   public Instruction DUP() {
      return InstructionConstants.DUP2;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
