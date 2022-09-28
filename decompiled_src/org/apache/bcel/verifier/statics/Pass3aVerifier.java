package org.apache.bcel.verifier.statics;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BREAKPOINT;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DLOAD;
import org.apache.bcel.generic.DSTORE;
import org.apache.bcel.generic.EmptyVisitor;
import org.apache.bcel.generic.FLOAD;
import org.apache.bcel.generic.FSTORE;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.GotoInstruction;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.IMPDEP1;
import org.apache.bcel.generic.IMPDEP2;
import org.apache.bcel.generic.INSTANCEOF;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.JsrInstruction;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LLOAD;
import org.apache.bcel.generic.LOOKUPSWITCH;
import org.apache.bcel.generic.LSTORE;
import org.apache.bcel.generic.LoadClass;
import org.apache.bcel.generic.MULTIANEWARRAY;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUTSTATIC;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.TABLESWITCH;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.PassVerifier;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.ClassConstraintException;
import org.apache.bcel.verifier.exc.InvalidMethodException;
import org.apache.bcel.verifier.exc.StaticCodeConstraintException;
import org.apache.bcel.verifier.exc.StaticCodeInstructionConstraintException;
import org.apache.bcel.verifier.exc.StaticCodeInstructionOperandConstraintException;

public final class Pass3aVerifier extends PassVerifier {
   private Verifier myOwner;
   private int method_no;
   InstructionList instructionList;
   Code code;

   public Pass3aVerifier(Verifier owner, int method_no) {
      this.myOwner = owner;
      this.method_no = method_no;
   }

   public VerificationResult do_verify() {
      if (this.myOwner.doPass2().equals(VerificationResult.VR_OK)) {
         JavaClass jc = Repository.lookupClass(this.myOwner.getClassName());
         Method[] methods = jc.getMethods();
         if (this.method_no >= methods.length) {
            throw new InvalidMethodException("METHOD DOES NOT EXIST!");
         } else {
            Method method = methods[this.method_no];
            this.code = method.getCode();
            if (!method.isAbstract() && !method.isNative()) {
               try {
                  this.instructionList = new InstructionList(method.getCode().getCode());
               } catch (RuntimeException var8) {
                  return new VerificationResult(2, "Bad bytecode in the code array of the Code attribute of method '" + method + "'.");
               }

               this.instructionList.setPositions(true);
               VerificationResult vr = VerificationResult.VR_OK;

               try {
                  this.delayedPass2Checks();
               } catch (ClassConstraintException var7) {
                  vr = new VerificationResult(2, var7.getMessage());
                  return vr;
               }

               try {
                  this.pass3StaticInstructionChecks();
                  this.pass3StaticInstructionOperandsChecks();
               } catch (StaticCodeConstraintException var6) {
                  vr = new VerificationResult(2, var6.getMessage());
               }

               return vr;
            } else {
               return VerificationResult.VR_OK;
            }
         }
      } else {
         return VerificationResult.VR_NOTYET;
      }
   }

   private void delayedPass2Checks() {
      int[] instructionPositions = this.instructionList.getInstructionPositions();
      int codeLength = this.code.getCode().length;
      LineNumberTable lnt = this.code.getLineNumberTable();
      int i;
      int startpc;
      if (lnt != null) {
         LineNumber[] lineNumbers = lnt.getLineNumberTable();
         IntList offsets = new IntList();

         label87:
         for(int i = 0; i < lineNumbers.length; ++i) {
            for(i = 0; i < instructionPositions.length; ++i) {
               startpc = lineNumbers[i].getStartPC();
               if (instructionPositions[i] == startpc) {
                  if (offsets.contains(startpc)) {
                     this.addMessage("LineNumberTable attribute '" + this.code.getLineNumberTable() + "' refers to the same code offset ('" + startpc + "') more than once which is violating the semantics [but is sometimes produced by IBM's 'jikes' compiler].");
                  } else {
                     offsets.add(startpc);
                  }
                  continue label87;
               }
            }

            throw new ClassConstraintException("Code attribute '" + this.code + "' has a LineNumberTable attribute '" + this.code.getLineNumberTable() + "' referring to a code offset ('" + lineNumbers[i].getStartPC() + "') that does not exist.");
         }
      }

      Attribute[] atts = this.code.getAttributes();

      int startpc;
      int handlerpc;
      for(int a = 0; a < atts.length; ++a) {
         if (atts[a] instanceof LocalVariableTable) {
            LocalVariableTable lvt = (LocalVariableTable)atts[a];
            if (lvt != null) {
               LocalVariable[] localVariables = lvt.getLocalVariableTable();

               for(startpc = 0; startpc < localVariables.length; ++startpc) {
                  startpc = localVariables[startpc].getStartPC();
                  handlerpc = localVariables[startpc].getLength();
                  if (!contains(instructionPositions, startpc)) {
                     throw new ClassConstraintException("Code attribute '" + this.code + "' has a LocalVariableTable attribute '" + this.code.getLocalVariableTable() + "' referring to a code offset ('" + startpc + "') that does not exist.");
                  }

                  if (!contains(instructionPositions, startpc + handlerpc) && startpc + handlerpc != codeLength) {
                     throw new ClassConstraintException("Code attribute '" + this.code + "' has a LocalVariableTable attribute '" + this.code.getLocalVariableTable() + "' referring to a code offset start_pc+length ('" + (startpc + handlerpc) + "') that does not exist.");
                  }
               }
            }
         }
      }

      CodeException[] exceptionTable = this.code.getExceptionTable();

      for(i = 0; i < exceptionTable.length; ++i) {
         startpc = exceptionTable[i].getStartPC();
         startpc = exceptionTable[i].getEndPC();
         handlerpc = exceptionTable[i].getHandlerPC();
         if (startpc >= startpc) {
            throw new ClassConstraintException("Code attribute '" + this.code + "' has an exception_table entry '" + exceptionTable[i] + "' that has its start_pc ('" + startpc + "') not smaller than its end_pc ('" + startpc + "').");
         }

         if (!contains(instructionPositions, startpc)) {
            throw new ClassConstraintException("Code attribute '" + this.code + "' has an exception_table entry '" + exceptionTable[i] + "' that has a non-existant bytecode offset as its start_pc ('" + startpc + "').");
         }

         if (!contains(instructionPositions, startpc) && startpc != codeLength) {
            throw new ClassConstraintException("Code attribute '" + this.code + "' has an exception_table entry '" + exceptionTable[i] + "' that has a non-existant bytecode offset as its end_pc ('" + startpc + "') [that is also not equal to code_length ('" + codeLength + "')].");
         }

         if (!contains(instructionPositions, handlerpc)) {
            throw new ClassConstraintException("Code attribute '" + this.code + "' has an exception_table entry '" + exceptionTable[i] + "' that has a non-existant bytecode offset as its handler_pc ('" + handlerpc + "').");
         }
      }

   }

   private void pass3StaticInstructionChecks() {
      if (this.code.getCode().length >= 65536) {
         throw new StaticCodeInstructionConstraintException("Code array in code attribute '" + this.code + "' too big: must be smaller than 65536 bytes.");
      } else {
         Instruction i;
         for(InstructionHandle ih = this.instructionList.getStart(); ih != null; ih = ih.getNext()) {
            i = ih.getInstruction();
            if (i instanceof IMPDEP1) {
               throw new StaticCodeInstructionConstraintException("IMPDEP1 must not be in the code, it is an illegal instruction for _internal_ JVM use!");
            }

            if (i instanceof IMPDEP2) {
               throw new StaticCodeInstructionConstraintException("IMPDEP2 must not be in the code, it is an illegal instruction for _internal_ JVM use!");
            }

            if (i instanceof BREAKPOINT) {
               throw new StaticCodeInstructionConstraintException("BREAKPOINT must not be in the code, it is an illegal instruction for _internal_ JVM use!");
            }
         }

         i = this.instructionList.getEnd().getInstruction();
         if (!(i instanceof ReturnInstruction) && !(i instanceof RET) && !(i instanceof GotoInstruction) && !(i instanceof ATHROW)) {
            throw new StaticCodeInstructionConstraintException("Execution must not fall off the bottom of the code array. This constraint is enforced statically as some existing verifiers do - so it may be a false alarm if the last instruction is not reachable.");
         }
      }
   }

   private void pass3StaticInstructionOperandsChecks() {
      ConstantPoolGen cpg = new ConstantPoolGen(Repository.lookupClass(this.myOwner.getClassName()).getConstantPool());
      InstOperandConstraintVisitor v = new InstOperandConstraintVisitor(cpg);

      for(InstructionHandle ih = this.instructionList.getStart(); ih != null; ih = ih.getNext()) {
         Instruction i = ih.getInstruction();
         if (i instanceof JsrInstruction) {
            InstructionHandle target = ((JsrInstruction)i).getTarget();
            if (target == this.instructionList.getStart()) {
               throw new StaticCodeInstructionOperandConstraintException("Due to JustIce's clear definition of subroutines, no JSR or JSR_W may have a top-level instruction (such as the very first instruction, which is targeted by instruction '" + ih + "' as its target.");
            }

            if (!(target.getInstruction() instanceof ASTORE)) {
               throw new StaticCodeInstructionOperandConstraintException("Due to JustIce's clear definition of subroutines, no JSR or JSR_W may target anything else than an ASTORE instruction. Instruction '" + ih + "' targets '" + target + "'.");
            }
         }

         ih.accept(v);
      }

   }

   private static boolean contains(int[] ints, int i) {
      for(int j = 0; j < ints.length; ++j) {
         if (ints[j] == i) {
            return true;
         }
      }

      return false;
   }

   public int getMethodNo() {
      return this.method_no;
   }

   private class InstOperandConstraintVisitor extends EmptyVisitor {
      private ConstantPoolGen cpg;

      InstOperandConstraintVisitor(ConstantPoolGen cpg) {
         this.cpg = cpg;
      }

      private int max_locals() {
         return Repository.lookupClass(Pass3aVerifier.this.myOwner.getClassName()).getMethods()[Pass3aVerifier.this.method_no].getCode().getMaxLocals();
      }

      private void constraintViolated(Instruction i, String message) {
         throw new StaticCodeInstructionOperandConstraintException("Instruction " + i + " constraint violated: " + message);
      }

      private void indexValid(Instruction i, int idx) {
         if (idx < 0 || idx >= this.cpg.getSize()) {
            this.constraintViolated(i, "Illegal constant pool index '" + idx + "'.");
         }

      }

      public void visitLoadClass(LoadClass o) {
         ObjectType t = o.getLoadClassType(this.cpg);
         if (t != null) {
            Verifier v = VerifierFactory.getVerifier(t.getClassName());
            VerificationResult vr = v.doPass1();
            if (vr.getStatus() != 1) {
               this.constraintViolated((Instruction)o, "Class '" + o.getLoadClassType(this.cpg).getClassName() + "' is referenced, but cannot be loaded: '" + vr + "'.");
            }
         }

      }

      public void visitLDC(LDC o) {
         this.indexValid(o, o.getIndex());
         Constant c = this.cpg.getConstant(o.getIndex());
         if (!(c instanceof ConstantInteger) && !(c instanceof ConstantFloat) && !(c instanceof ConstantString)) {
            this.constraintViolated(o, "Operand of LDC or LDC_W must be one of CONSTANT_Integer, CONSTANT_Float or CONSTANT_String, but is '" + c + "'.");
         }

      }

      public void visitLDC2_W(LDC2_W o) {
         this.indexValid(o, o.getIndex());
         Constant c = this.cpg.getConstant(o.getIndex());
         if (!(c instanceof ConstantLong) && !(c instanceof ConstantDouble)) {
            this.constraintViolated(o, "Operand of LDC2_W must be CONSTANT_Long or CONSTANT_Double, but is '" + c + "'.");
         }

         try {
            this.indexValid(o, o.getIndex() + 1);
         } catch (StaticCodeInstructionOperandConstraintException var4) {
            throw new AssertionViolatedException("OOPS: Does not BCEL handle that? LDC2_W operand has a problem.");
         }
      }

      public void visitFieldInstruction(FieldInstruction o) {
         this.indexValid(o, o.getIndex());
         Constant c = this.cpg.getConstant(o.getIndex());
         if (!(c instanceof ConstantFieldref)) {
            this.constraintViolated(o, "Indexing a constant that's not a CONSTANT_Fieldref but a '" + c + "'.");
         }

      }

      public void visitInvokeInstruction(InvokeInstruction o) {
         this.indexValid(o, o.getIndex());
         Constant c;
         ConstantNameAndType cnat;
         if (!(o instanceof INVOKEVIRTUAL) && !(o instanceof INVOKESPECIAL) && !(o instanceof INVOKESTATIC)) {
            c = this.cpg.getConstant(o.getIndex());
            if (!(c instanceof ConstantInterfaceMethodref)) {
               this.constraintViolated(o, "Indexing a constant that's not a CONSTANT_InterfaceMethodref but a '" + c + "'.");
            }

            cnat = (ConstantNameAndType)this.cpg.getConstant(((ConstantInterfaceMethodref)c).getNameAndTypeIndex());
            String name = ((ConstantUtf8)this.cpg.getConstant(cnat.getNameIndex())).getBytes();
            if (name.equals("<init>")) {
               this.constraintViolated(o, "Method to invoke must not be '<init>'.");
            }

            if (name.equals("<clinit>")) {
               this.constraintViolated(o, "Method to invoke must not be '<clinit>'.");
            }
         } else {
            c = this.cpg.getConstant(o.getIndex());
            if (!(c instanceof ConstantMethodref)) {
               this.constraintViolated(o, "Indexing a constant that's not a CONSTANT_Methodref but a '" + c + "'.");
            } else {
               cnat = (ConstantNameAndType)this.cpg.getConstant(((ConstantMethodref)c).getNameAndTypeIndex());
               ConstantUtf8 cutf8 = (ConstantUtf8)this.cpg.getConstant(cnat.getNameIndex());
               if (cutf8.getBytes().equals("<init>") && !(o instanceof INVOKESPECIAL)) {
                  this.constraintViolated(o, "Only INVOKESPECIAL is allowed to invoke instance initialization methods.");
               }

               if (!cutf8.getBytes().equals("<init>") && cutf8.getBytes().startsWith("<")) {
                  this.constraintViolated(o, "No method with a name beginning with '<' other than the instance initialization methods may be called by the method invocation instructions.");
               }
            }
         }

         Type t = o.getReturnType(this.cpg);
         if (t instanceof ArrayType) {
            t = ((ArrayType)t).getBasicType();
         }

         if (t instanceof ObjectType) {
            Verifier vx = VerifierFactory.getVerifier(((ObjectType)t).getClassName());
            VerificationResult vr = vx.doPass2();
            if (vr.getStatus() != 1) {
               this.constraintViolated(o, "Return type class/interface could not be verified successfully: '" + vr.getMessage() + "'.");
            }
         }

         Type[] ts = o.getArgumentTypes(this.cpg);

         for(int i = 0; i < ts.length; ++i) {
            t = ts[i];
            if (t instanceof ArrayType) {
               t = ((ArrayType)t).getBasicType();
            }

            if (t instanceof ObjectType) {
               Verifier v = VerifierFactory.getVerifier(((ObjectType)t).getClassName());
               VerificationResult vrx = v.doPass2();
               if (vrx.getStatus() != 1) {
                  this.constraintViolated(o, "Argument type class/interface could not be verified successfully: '" + vrx.getMessage() + "'.");
               }
            }
         }

      }

      public void visitINSTANCEOF(INSTANCEOF o) {
         this.indexValid(o, o.getIndex());
         Constant c = this.cpg.getConstant(o.getIndex());
         if (!(c instanceof ConstantClass)) {
            this.constraintViolated(o, "Expecting a CONSTANT_Class operand, but found a '" + c + "'.");
         }

      }

      public void visitCHECKCAST(CHECKCAST o) {
         this.indexValid(o, o.getIndex());
         Constant c = this.cpg.getConstant(o.getIndex());
         if (!(c instanceof ConstantClass)) {
            this.constraintViolated(o, "Expecting a CONSTANT_Class operand, but found a '" + c + "'.");
         }

      }

      public void visitNEW(NEW o) {
         this.indexValid(o, o.getIndex());
         Constant c = this.cpg.getConstant(o.getIndex());
         if (!(c instanceof ConstantClass)) {
            this.constraintViolated(o, "Expecting a CONSTANT_Class operand, but found a '" + c + "'.");
         } else {
            ConstantUtf8 cutf8 = (ConstantUtf8)this.cpg.getConstant(((ConstantClass)c).getNameIndex());
            Type t = Type.getType("L" + cutf8.getBytes() + ";");
            if (t instanceof ArrayType) {
               this.constraintViolated(o, "NEW must not be used to create an array.");
            }
         }

      }

      public void visitMULTIANEWARRAY(MULTIANEWARRAY o) {
         this.indexValid(o, o.getIndex());
         Constant c = this.cpg.getConstant(o.getIndex());
         if (!(c instanceof ConstantClass)) {
            this.constraintViolated(o, "Expecting a CONSTANT_Class operand, but found a '" + c + "'.");
         }

         int dimensions2create = o.getDimensions();
         if (dimensions2create < 1) {
            this.constraintViolated(o, "Number of dimensions to create must be greater than zero.");
         }

         Type t = o.getType(this.cpg);
         if (t instanceof ArrayType) {
            int dimensions = ((ArrayType)t).getDimensions();
            if (dimensions < dimensions2create) {
               this.constraintViolated(o, "Not allowed to create array with more dimensions ('+dimensions2create+') than the one referenced by the CONSTANT_Class '" + t + "'.");
            }
         } else {
            this.constraintViolated(o, "Expecting a CONSTANT_Class referencing an array type. [Constraint not found in The Java Virtual Machine Specification, Second Edition, 4.8.1]");
         }

      }

      public void visitANEWARRAY(ANEWARRAY o) {
         this.indexValid(o, o.getIndex());
         Constant c = this.cpg.getConstant(o.getIndex());
         if (!(c instanceof ConstantClass)) {
            this.constraintViolated(o, "Expecting a CONSTANT_Class operand, but found a '" + c + "'.");
         }

         Type t = o.getType(this.cpg);
         if (t instanceof ArrayType) {
            int dimensions = ((ArrayType)t).getDimensions();
            if (dimensions >= 255) {
               this.constraintViolated(o, "Not allowed to create an array with more than 255 dimensions.");
            }
         }

      }

      public void visitNEWARRAY(NEWARRAY o) {
         byte t = o.getTypecode();
         if (t != 4 && t != 5 && t != 6 && t != 7 && t != 8 && t != 9 && t != 10 && t != 11) {
            this.constraintViolated(o, "Illegal type code '+t+' for 'atype' operand.");
         }

      }

      public void visitILOAD(ILOAD o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative.");
         } else {
            int maxminus1 = this.max_locals() - 1;
            if (idx > maxminus1) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-1 '" + maxminus1 + "'.");
            }
         }

      }

      public void visitFLOAD(FLOAD o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative.");
         } else {
            int maxminus1 = this.max_locals() - 1;
            if (idx > maxminus1) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-1 '" + maxminus1 + "'.");
            }
         }

      }

      public void visitALOAD(ALOAD o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative.");
         } else {
            int maxminus1 = this.max_locals() - 1;
            if (idx > maxminus1) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-1 '" + maxminus1 + "'.");
            }
         }

      }

      public void visitISTORE(ISTORE o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative.");
         } else {
            int maxminus1 = this.max_locals() - 1;
            if (idx > maxminus1) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-1 '" + maxminus1 + "'.");
            }
         }

      }

      public void visitFSTORE(FSTORE o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative.");
         } else {
            int maxminus1 = this.max_locals() - 1;
            if (idx > maxminus1) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-1 '" + maxminus1 + "'.");
            }
         }

      }

      public void visitASTORE(ASTORE o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative.");
         } else {
            int maxminus1 = this.max_locals() - 1;
            if (idx > maxminus1) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-1 '" + maxminus1 + "'.");
            }
         }

      }

      public void visitIINC(IINC o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative.");
         } else {
            int maxminus1 = this.max_locals() - 1;
            if (idx > maxminus1) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-1 '" + maxminus1 + "'.");
            }
         }

      }

      public void visitRET(RET o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative.");
         } else {
            int maxminus1 = this.max_locals() - 1;
            if (idx > maxminus1) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-1 '" + maxminus1 + "'.");
            }
         }

      }

      public void visitLLOAD(LLOAD o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative. [Constraint by JustIce as an analogon to the single-slot xLOAD/xSTORE instructions; may not happen anyway.]");
         } else {
            int maxminus2 = this.max_locals() - 2;
            if (idx > maxminus2) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-2 '" + maxminus2 + "'.");
            }
         }

      }

      public void visitDLOAD(DLOAD o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative. [Constraint by JustIce as an analogon to the single-slot xLOAD/xSTORE instructions; may not happen anyway.]");
         } else {
            int maxminus2 = this.max_locals() - 2;
            if (idx > maxminus2) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-2 '" + maxminus2 + "'.");
            }
         }

      }

      public void visitLSTORE(LSTORE o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative. [Constraint by JustIce as an analogon to the single-slot xLOAD/xSTORE instructions; may not happen anyway.]");
         } else {
            int maxminus2 = this.max_locals() - 2;
            if (idx > maxminus2) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-2 '" + maxminus2 + "'.");
            }
         }

      }

      public void visitDSTORE(DSTORE o) {
         int idx = o.getIndex();
         if (idx < 0) {
            this.constraintViolated(o, "Index '" + idx + "' must be non-negative. [Constraint by JustIce as an analogon to the single-slot xLOAD/xSTORE instructions; may not happen anyway.]");
         } else {
            int maxminus2 = this.max_locals() - 2;
            if (idx > maxminus2) {
               this.constraintViolated(o, "Index '" + idx + "' must not be greater than max_locals-2 '" + maxminus2 + "'.");
            }
         }

      }

      public void visitLOOKUPSWITCH(LOOKUPSWITCH o) {
         int[] matchs = o.getMatchs();
         int max = Integer.MIN_VALUE;

         for(int i = 0; i < matchs.length; ++i) {
            if (matchs[i] == max && i != 0) {
               this.constraintViolated(o, "Match '" + matchs[i] + "' occurs more than once.");
            }

            if (matchs[i] < max) {
               this.constraintViolated(o, "Lookup table must be sorted but isn't.");
            } else {
               max = matchs[i];
            }
         }

      }

      public void visitTABLESWITCH(TABLESWITCH o) {
      }

      public void visitPUTSTATIC(PUTSTATIC o) {
         String field_name = o.getFieldName(this.cpg);
         JavaClass jc = Repository.lookupClass(o.getClassType(this.cpg).getClassName());
         Field[] fields = jc.getFields();
         Field f = null;

         for(int i = 0; i < fields.length; ++i) {
            if (fields[i].getName().equals(field_name)) {
               f = fields[i];
               break;
            }
         }

         if (f == null) {
            throw new AssertionViolatedException("Field not found?!?");
         } else {
            if (f.isFinal() && !Pass3aVerifier.this.myOwner.getClassName().equals(o.getClassType(this.cpg).getClassName())) {
               this.constraintViolated(o, "Referenced field '" + f + "' is final and must therefore be declared in the current class '" + Pass3aVerifier.this.myOwner.getClassName() + "' which is not the case: it is declared in '" + o.getClassType(this.cpg).getClassName() + "'.");
            }

            if (!f.isStatic()) {
               this.constraintViolated(o, "Referenced field '" + f + "' is not static which it should be.");
            }

            String meth_name = Repository.lookupClass(Pass3aVerifier.this.myOwner.getClassName()).getMethods()[Pass3aVerifier.this.method_no].getName();
            if (!jc.isClass() && !meth_name.equals("<clinit>")) {
               this.constraintViolated(o, "Interface field '" + f + "' must be set in a '" + "<clinit>" + "' method.");
            }

         }
      }

      public void visitGETSTATIC(GETSTATIC o) {
         String field_name = o.getFieldName(this.cpg);
         JavaClass jc = Repository.lookupClass(o.getClassType(this.cpg).getClassName());
         Field[] fields = jc.getFields();
         Field f = null;

         for(int i = 0; i < fields.length; ++i) {
            if (fields[i].getName().equals(field_name)) {
               f = fields[i];
               break;
            }
         }

         if (f == null) {
            throw new AssertionViolatedException("Field not found?!?");
         } else {
            if (!f.isStatic()) {
               this.constraintViolated(o, "Referenced field '" + f + "' is not static which it should be.");
            }

         }
      }

      public void visitINVOKEINTERFACE(INVOKEINTERFACE o) {
         String classname = o.getClassName(this.cpg);
         JavaClass jc = Repository.lookupClass(classname);
         Method[] ms = jc.getMethods();
         Method m = null;

         for(int i = 0; i < ms.length; ++i) {
            if (ms[i].getName().equals(o.getMethodName(this.cpg)) && Type.getReturnType(ms[i].getSignature()).equals(o.getReturnType(this.cpg)) && this.objarrayequals(Type.getArgumentTypes(ms[i].getSignature()), o.getArgumentTypes(this.cpg))) {
               m = ms[i];
               break;
            }
         }

         if (m == null) {
            this.constraintViolated(o, "Referenced method '" + o.getMethodName(this.cpg) + "' with expected signature not found in class '" + jc.getClassName() + "'. The native verfier does allow the method to be declared in some superinterface, which the Java Virtual Machine Specification, Second Edition does not.");
         }

         if (jc.isClass()) {
            this.constraintViolated(o, "Referenced class '" + jc.getClassName() + "' is a class, but not an interface as expected.");
         }

      }

      public void visitINVOKESPECIAL(INVOKESPECIAL o) {
         String classname = o.getClassName(this.cpg);
         JavaClass jc = Repository.lookupClass(classname);
         Method[] ms = jc.getMethods();
         Method m = null;

         for(int ix = 0; ix < ms.length; ++ix) {
            if (ms[ix].getName().equals(o.getMethodName(this.cpg)) && Type.getReturnType(ms[ix].getSignature()).equals(o.getReturnType(this.cpg)) && this.objarrayequals(Type.getArgumentTypes(ms[ix].getSignature()), o.getArgumentTypes(this.cpg))) {
               m = ms[ix];
               break;
            }
         }

         if (m == null) {
            this.constraintViolated(o, "Referenced method '" + o.getMethodName(this.cpg) + "' with expected signature not found in class '" + jc.getClassName() + "'. The native verfier does allow the method to be declared in some superclass or implemented interface, which the Java Virtual Machine Specification, Second Edition does not.");
         }

         JavaClass current = Repository.lookupClass(Pass3aVerifier.this.myOwner.getClassName());
         if (current.isSuper() && Repository.instanceOf(current, jc) && !current.equals(jc) && !o.getMethodName(this.cpg).equals("<init>")) {
            int supidx = -1;
            Method meth = null;

            while(supidx != 0) {
               supidx = current.getSuperclassNameIndex();
               current = Repository.lookupClass(current.getSuperclassName());
               Method[] meths = current.getMethods();

               for(int i = 0; i < meths.length; ++i) {
                  if (meths[i].getName().equals(o.getMethodName(this.cpg)) && Type.getReturnType(meths[i].getSignature()).equals(o.getReturnType(this.cpg)) && this.objarrayequals(Type.getArgumentTypes(meths[i].getSignature()), o.getArgumentTypes(this.cpg))) {
                     meth = meths[i];
                     break;
                  }
               }

               if (meth != null) {
                  break;
               }
            }

            if (meth == null) {
               this.constraintViolated(o, "ACC_SUPER special lookup procedure not successful: method '" + o.getMethodName(this.cpg) + "' with proper signature not declared in superclass hierarchy.");
            }
         }

      }

      public void visitINVOKESTATIC(INVOKESTATIC o) {
         String classname = o.getClassName(this.cpg);
         JavaClass jc = Repository.lookupClass(classname);
         Method[] ms = jc.getMethods();
         Method m = null;

         for(int i = 0; i < ms.length; ++i) {
            if (ms[i].getName().equals(o.getMethodName(this.cpg)) && Type.getReturnType(ms[i].getSignature()).equals(o.getReturnType(this.cpg)) && this.objarrayequals(Type.getArgumentTypes(ms[i].getSignature()), o.getArgumentTypes(this.cpg))) {
               m = ms[i];
               break;
            }
         }

         if (m == null) {
            this.constraintViolated(o, "Referenced method '" + o.getMethodName(this.cpg) + "' with expected signature not found in class '" + jc.getClassName() + "'. The native verifier possibly allows the method to be declared in some superclass or implemented interface, which the Java Virtual Machine Specification, Second Edition does not.");
         }

         if (!m.isStatic()) {
            this.constraintViolated(o, "Referenced method '" + o.getMethodName(this.cpg) + "' has ACC_STATIC unset.");
         }

      }

      public void visitINVOKEVIRTUAL(INVOKEVIRTUAL o) {
         String classname = o.getClassName(this.cpg);
         JavaClass jc = Repository.lookupClass(classname);
         Method[] ms = jc.getMethods();
         Method m = null;

         for(int i = 0; i < ms.length; ++i) {
            if (ms[i].getName().equals(o.getMethodName(this.cpg)) && Type.getReturnType(ms[i].getSignature()).equals(o.getReturnType(this.cpg)) && this.objarrayequals(Type.getArgumentTypes(ms[i].getSignature()), o.getArgumentTypes(this.cpg))) {
               m = ms[i];
               break;
            }
         }

         if (m == null) {
            this.constraintViolated(o, "Referenced method '" + o.getMethodName(this.cpg) + "' with expected signature not found in class '" + jc.getClassName() + "'. The native verfier does allow the method to be declared in some superclass or implemented interface, which the Java Virtual Machine Specification, Second Edition does not.");
         }

         if (!jc.isClass()) {
            this.constraintViolated(o, "Referenced class '" + jc.getClassName() + "' is an interface, but not a class as expected.");
         }

      }

      private boolean objarrayequals(Object[] o, Object[] p) {
         if (o.length != p.length) {
            return false;
         } else {
            for(int i = 0; i < o.length; ++i) {
               if (!o[i].equals(p[i])) {
                  return false;
               }
            }

            return true;
         }
      }
   }
}
