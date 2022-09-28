package org.apache.bcel.verifier.structurals;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.AALOAD;
import org.apache.bcel.generic.AASTORE;
import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ANEWARRAY;
import org.apache.bcel.generic.ARETURN;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.BALOAD;
import org.apache.bcel.generic.BASTORE;
import org.apache.bcel.generic.BIPUSH;
import org.apache.bcel.generic.BREAKPOINT;
import org.apache.bcel.generic.CALOAD;
import org.apache.bcel.generic.CASTORE;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CPInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.D2F;
import org.apache.bcel.generic.D2I;
import org.apache.bcel.generic.D2L;
import org.apache.bcel.generic.DADD;
import org.apache.bcel.generic.DALOAD;
import org.apache.bcel.generic.DASTORE;
import org.apache.bcel.generic.DCMPG;
import org.apache.bcel.generic.DCMPL;
import org.apache.bcel.generic.DCONST;
import org.apache.bcel.generic.DDIV;
import org.apache.bcel.generic.DLOAD;
import org.apache.bcel.generic.DMUL;
import org.apache.bcel.generic.DNEG;
import org.apache.bcel.generic.DREM;
import org.apache.bcel.generic.DRETURN;
import org.apache.bcel.generic.DSTORE;
import org.apache.bcel.generic.DSUB;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.DUP2;
import org.apache.bcel.generic.DUP2_X1;
import org.apache.bcel.generic.DUP2_X2;
import org.apache.bcel.generic.DUP_X1;
import org.apache.bcel.generic.DUP_X2;
import org.apache.bcel.generic.EmptyVisitor;
import org.apache.bcel.generic.F2D;
import org.apache.bcel.generic.F2I;
import org.apache.bcel.generic.F2L;
import org.apache.bcel.generic.FADD;
import org.apache.bcel.generic.FALOAD;
import org.apache.bcel.generic.FASTORE;
import org.apache.bcel.generic.FCMPG;
import org.apache.bcel.generic.FCMPL;
import org.apache.bcel.generic.FCONST;
import org.apache.bcel.generic.FDIV;
import org.apache.bcel.generic.FLOAD;
import org.apache.bcel.generic.FMUL;
import org.apache.bcel.generic.FNEG;
import org.apache.bcel.generic.FREM;
import org.apache.bcel.generic.FRETURN;
import org.apache.bcel.generic.FSTORE;
import org.apache.bcel.generic.FSUB;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.I2B;
import org.apache.bcel.generic.I2C;
import org.apache.bcel.generic.I2D;
import org.apache.bcel.generic.I2F;
import org.apache.bcel.generic.I2L;
import org.apache.bcel.generic.I2S;
import org.apache.bcel.generic.IADD;
import org.apache.bcel.generic.IALOAD;
import org.apache.bcel.generic.IAND;
import org.apache.bcel.generic.IASTORE;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.IDIV;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGE;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.IFLE;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.IFNULL;
import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.bcel.generic.IF_ACMPNE;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.IF_ICMPGE;
import org.apache.bcel.generic.IF_ICMPGT;
import org.apache.bcel.generic.IF_ICMPLE;
import org.apache.bcel.generic.IF_ICMPLT;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.IMPDEP1;
import org.apache.bcel.generic.IMPDEP2;
import org.apache.bcel.generic.IMUL;
import org.apache.bcel.generic.INEG;
import org.apache.bcel.generic.INSTANCEOF;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.IOR;
import org.apache.bcel.generic.IREM;
import org.apache.bcel.generic.IRETURN;
import org.apache.bcel.generic.ISHL;
import org.apache.bcel.generic.ISHR;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.ISUB;
import org.apache.bcel.generic.IUSHR;
import org.apache.bcel.generic.IXOR;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InvokeInstruction;
import org.apache.bcel.generic.JSR;
import org.apache.bcel.generic.JSR_W;
import org.apache.bcel.generic.L2D;
import org.apache.bcel.generic.L2F;
import org.apache.bcel.generic.L2I;
import org.apache.bcel.generic.LADD;
import org.apache.bcel.generic.LALOAD;
import org.apache.bcel.generic.LAND;
import org.apache.bcel.generic.LASTORE;
import org.apache.bcel.generic.LCMP;
import org.apache.bcel.generic.LCONST;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LDC2_W;
import org.apache.bcel.generic.LDC_W;
import org.apache.bcel.generic.LDIV;
import org.apache.bcel.generic.LLOAD;
import org.apache.bcel.generic.LMUL;
import org.apache.bcel.generic.LNEG;
import org.apache.bcel.generic.LOOKUPSWITCH;
import org.apache.bcel.generic.LOR;
import org.apache.bcel.generic.LREM;
import org.apache.bcel.generic.LRETURN;
import org.apache.bcel.generic.LSHL;
import org.apache.bcel.generic.LSHR;
import org.apache.bcel.generic.LSTORE;
import org.apache.bcel.generic.LSUB;
import org.apache.bcel.generic.LUSHR;
import org.apache.bcel.generic.LXOR;
import org.apache.bcel.generic.LoadClass;
import org.apache.bcel.generic.LoadInstruction;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MONITORENTER;
import org.apache.bcel.generic.MONITOREXIT;
import org.apache.bcel.generic.MULTIANEWARRAY;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.POP;
import org.apache.bcel.generic.POP2;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.PUTSTATIC;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.RETURN;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.ReturnaddressType;
import org.apache.bcel.generic.SALOAD;
import org.apache.bcel.generic.SASTORE;
import org.apache.bcel.generic.SIPUSH;
import org.apache.bcel.generic.SWAP;
import org.apache.bcel.generic.StackConsumer;
import org.apache.bcel.generic.StackInstruction;
import org.apache.bcel.generic.StackProducer;
import org.apache.bcel.generic.StoreInstruction;
import org.apache.bcel.generic.TABLESWITCH;
import org.apache.bcel.generic.Type;
import org.apache.bcel.generic.Visitor;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;

public class InstConstraintVisitor extends EmptyVisitor implements Visitor {
   private static ObjectType GENERIC_ARRAY = new ObjectType("org.apache.bcel.verifier.structurals.GenericArray");
   private Frame frame = null;
   private ConstantPoolGen cpg = null;
   private MethodGen mg = null;

   private OperandStack stack() {
      return this.frame.getStack();
   }

   private LocalVariables locals() {
      return this.frame.getLocals();
   }

   private void constraintViolated(Instruction violator, String description) {
      String fq_classname = violator.getClass().getName();
      throw new StructuralCodeConstraintException("Instruction " + fq_classname.substring(fq_classname.lastIndexOf(46) + 1) + " constraint violated: " + description);
   }

   public void setFrame(Frame f) {
      this.frame = f;
   }

   public void setConstantPoolGen(ConstantPoolGen cpg) {
      this.cpg = cpg;
   }

   public void setMethodGen(MethodGen mg) {
      this.mg = mg;
   }

   private void indexOfInt(Instruction o, Type index) {
      if (!index.equals(Type.INT)) {
         this.constraintViolated(o, "The 'index' is not of type int but of type " + index + ".");
      }

   }

   private void referenceTypeIsInitialized(Instruction o, ReferenceType r) {
      if (r instanceof UninitializedObjectType) {
         this.constraintViolated(o, "Working on an uninitialized object '" + r + "'.");
      }

   }

   private void valueOfInt(Instruction o, Type value) {
      if (!value.equals(Type.INT)) {
         this.constraintViolated(o, "The 'value' is not of type int but of type " + value + ".");
      }

   }

   private boolean arrayrefOfArrayType(Instruction o, Type arrayref) {
      if (!(arrayref instanceof ArrayType) && !arrayref.equals(Type.NULL)) {
         this.constraintViolated(o, "The 'arrayref' does not refer to an array but is of type " + arrayref + ".");
      }

      return arrayref instanceof ArrayType;
   }

   private void _visitStackAccessor(Instruction o) {
      int consume = o.consumeStack(this.cpg);
      if (consume > this.stack().slotsUsed()) {
         this.constraintViolated(o, "Cannot consume " + consume + " stack slots: only " + this.stack().slotsUsed() + " slot(s) left on stack!\nStack:\n" + this.stack());
      }

      int produce = o.produceStack(this.cpg) - o.consumeStack(this.cpg);
      if (produce + this.stack().slotsUsed() > this.stack().maxStack()) {
         this.constraintViolated(o, "Cannot produce " + produce + " stack slots: only " + (this.stack().maxStack() - this.stack().slotsUsed()) + " free stack slot(s) left.\nStack:\n" + this.stack());
      }

   }

   public void visitLoadClass(LoadClass o) {
      ObjectType t = o.getLoadClassType(this.cpg);
      if (t != null) {
         Verifier v = VerifierFactory.getVerifier(t.getClassName());
         VerificationResult vr = v.doPass2();
         if (vr.getStatus() != 1) {
            this.constraintViolated((Instruction)o, "Class '" + o.getLoadClassType(this.cpg).getClassName() + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
         }
      }

   }

   public void visitStackConsumer(StackConsumer o) {
      this._visitStackAccessor((Instruction)o);
   }

   public void visitStackProducer(StackProducer o) {
      this._visitStackAccessor((Instruction)o);
   }

   public void visitCPInstruction(CPInstruction o) {
      int idx = o.getIndex();
      if (idx < 0 || idx >= this.cpg.getSize()) {
         throw new AssertionViolatedException("Huh?! Constant pool index of instruction '" + o + "' illegal? Pass 3a should have checked this!");
      }
   }

   public void visitFieldInstruction(FieldInstruction o) {
      Constant c = this.cpg.getConstant(o.getIndex());
      if (!(c instanceof ConstantFieldref)) {
         this.constraintViolated(o, "Index '" + o.getIndex() + "' should refer to a CONSTANT_Fieldref_info structure, but refers to '" + c + "'.");
      }

      Type t = o.getType(this.cpg);
      if (t instanceof ObjectType) {
         String name = ((ObjectType)t).getClassName();
         Verifier v = VerifierFactory.getVerifier(name);
         VerificationResult vr = v.doPass2();
         if (vr.getStatus() != 1) {
            this.constraintViolated(o, "Class '" + name + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
         }
      }

   }

   public void visitInvokeInstruction(InvokeInstruction o) {
   }

   public void visitStackInstruction(StackInstruction o) {
      this._visitStackAccessor(o);
   }

   public void visitLocalVariableInstruction(LocalVariableInstruction o) {
      if (this.locals().maxLocals() <= (o.getType(this.cpg).getSize() == 1 ? o.getIndex() : o.getIndex() + 1)) {
         this.constraintViolated(o, "The 'index' is not a valid index into the local variable array.");
      }

   }

   public void visitLoadInstruction(LoadInstruction o) {
      if (this.locals().get(o.getIndex()) == Type.UNKNOWN) {
         this.constraintViolated(o, "Read-Access on local variable " + o.getIndex() + " with unknown content.");
      }

      if (o.getType(this.cpg).getSize() == 2 && this.locals().get(o.getIndex() + 1) != Type.UNKNOWN) {
         this.constraintViolated(o, "Reading a two-locals value from local variables " + o.getIndex() + " and " + (o.getIndex() + 1) + " where the latter one is destroyed.");
      }

      if (!(o instanceof ALOAD)) {
         if (this.locals().get(o.getIndex()) != o.getType(this.cpg)) {
            this.constraintViolated(o, "Local Variable type and LOADing Instruction type mismatch: Local Variable: '" + this.locals().get(o.getIndex()) + "'; Instruction type: '" + o.getType(this.cpg) + "'.");
         }
      } else if (!(this.locals().get(o.getIndex()) instanceof ReferenceType)) {
         this.constraintViolated(o, "Local Variable type and LOADing Instruction type mismatch: Local Variable: '" + this.locals().get(o.getIndex()) + "'; Instruction expects a ReferenceType.");
      }

      if (this.stack().maxStack() - this.stack().slotsUsed() < o.getType(this.cpg).getSize()) {
         this.constraintViolated(o, "Not enough free stack slots to load a '" + o.getType(this.cpg) + "' onto the OperandStack.");
      }

   }

   public void visitStoreInstruction(StoreInstruction o) {
      if (this.stack().isEmpty()) {
         this.constraintViolated(o, "Cannot STORE: Stack to read from is empty.");
      }

      if (!(o instanceof ASTORE)) {
         if (this.stack().peek() != o.getType(this.cpg)) {
            this.constraintViolated(o, "Stack top type and STOREing Instruction type mismatch: Stack top: '" + this.stack().peek() + "'; Instruction type: '" + o.getType(this.cpg) + "'.");
         }
      } else {
         Type stacktop = this.stack().peek();
         if (!(stacktop instanceof ReferenceType) && !(stacktop instanceof ReturnaddressType)) {
            this.constraintViolated(o, "Stack top type and STOREing Instruction type mismatch: Stack top: '" + this.stack().peek() + "'; Instruction expects a ReferenceType or a ReturnadressType.");
         }

         if (stacktop instanceof ReferenceType) {
            this.referenceTypeIsInitialized(o, (ReferenceType)stacktop);
         }
      }

   }

   public void visitReturnInstruction(ReturnInstruction o) {
      if (!(o instanceof RETURN)) {
         if (o instanceof ARETURN) {
            if (this.stack().peek() == Type.NULL) {
               return;
            }

            if (!(this.stack().peek() instanceof ReferenceType)) {
               this.constraintViolated(o, "Reference type expected on top of stack, but is: '" + this.stack().peek() + "'.");
            }

            this.referenceTypeIsInitialized(o, (ReferenceType)this.stack().peek());
            ReferenceType var2 = (ReferenceType)this.stack().peek();
         } else {
            Type method_type = this.mg.getType();
            if (method_type == Type.BOOLEAN || method_type == Type.BYTE || method_type == Type.SHORT || method_type == Type.CHAR) {
               method_type = Type.INT;
            }

            if (!method_type.equals(this.stack().peek())) {
               this.constraintViolated(o, "Current method has return type of '" + this.mg.getType() + "' expecting a '" + method_type + "' on top of the stack. But stack top is a '" + this.stack().peek() + "'.");
            }
         }

      }
   }

   public void visitAALOAD(AALOAD o) {
      Type arrayref = this.stack().peek(1);
      Type index = this.stack().peek(0);
      this.indexOfInt(o, index);
      if (this.arrayrefOfArrayType(o, arrayref)) {
         if (!(((ArrayType)arrayref).getElementType() instanceof ReferenceType)) {
            this.constraintViolated(o, "The 'arrayref' does not refer to an array with elements of a ReferenceType but to an array of " + ((ArrayType)arrayref).getElementType() + ".");
         }

         this.referenceTypeIsInitialized(o, (ReferenceType)((ArrayType)arrayref).getElementType());
      }

   }

   public void visitAASTORE(AASTORE o) {
      Type arrayref = this.stack().peek(2);
      Type index = this.stack().peek(1);
      Type value = this.stack().peek(0);
      this.indexOfInt(o, index);
      if (!(value instanceof ReferenceType)) {
         this.constraintViolated(o, "The 'value' is not of a ReferenceType but of type " + value + ".");
      } else {
         this.referenceTypeIsInitialized(o, (ReferenceType)value);
      }

      if (this.arrayrefOfArrayType(o, arrayref)) {
         if (!(((ArrayType)arrayref).getElementType() instanceof ReferenceType)) {
            this.constraintViolated(o, "The 'arrayref' does not refer to an array with elements of a ReferenceType but to an array of " + ((ArrayType)arrayref).getElementType() + ".");
         }

         if (!((ReferenceType)value).isAssignmentCompatibleWith((ReferenceType)((ArrayType)arrayref).getElementType())) {
            this.constraintViolated(o, "The type of 'value' ('" + value + "') is not assignment compatible to the components of the array 'arrayref' refers to. ('" + ((ArrayType)arrayref).getElementType() + "')");
         }
      }

   }

   public void visitACONST_NULL(ACONST_NULL o) {
   }

   public void visitALOAD(ALOAD o) {
   }

   public void visitANEWARRAY(ANEWARRAY o) {
      if (!this.stack().peek().equals(Type.INT)) {
         this.constraintViolated(o, "The 'count' at the stack top is not of type '" + Type.INT + "' but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitARETURN(ARETURN o) {
      if (!(this.stack().peek() instanceof ReferenceType)) {
         this.constraintViolated(o, "The 'objectref' at the stack top is not of a ReferenceType but of type '" + this.stack().peek() + "'.");
      }

      ReferenceType objectref = (ReferenceType)this.stack().peek();
      this.referenceTypeIsInitialized(o, objectref);
   }

   public void visitARRAYLENGTH(ARRAYLENGTH o) {
      Type arrayref = this.stack().peek(0);
      this.arrayrefOfArrayType(o, arrayref);
   }

   public void visitASTORE(ASTORE o) {
      if (!(this.stack().peek() instanceof ReferenceType) && !(this.stack().peek() instanceof ReturnaddressType)) {
         this.constraintViolated(o, "The 'objectref' is not of a ReferenceType or of ReturnaddressType but of " + this.stack().peek() + ".");
      }

      if (this.stack().peek() instanceof ReferenceType) {
         this.referenceTypeIsInitialized(o, (ReferenceType)this.stack().peek());
      }

   }

   public void visitATHROW(ATHROW o) {
      if (!(this.stack().peek() instanceof ObjectType) && !this.stack().peek().equals(Type.NULL)) {
         this.constraintViolated(o, "The 'objectref' is not of an (initialized) ObjectType but of type " + this.stack().peek() + ".");
      }

      if (!this.stack().peek().equals(Type.NULL)) {
         ObjectType exc = (ObjectType)this.stack().peek();
         ObjectType throwable = (ObjectType)Type.getType("Ljava/lang/Throwable;");
         if (!exc.subclassOf(throwable) && !exc.equals(throwable)) {
            this.constraintViolated(o, "The 'objectref' is not of class Throwable or of a subclass of Throwable, but of '" + this.stack().peek() + "'.");
         }

      }
   }

   public void visitBALOAD(BALOAD o) {
      Type arrayref = this.stack().peek(1);
      Type index = this.stack().peek(0);
      this.indexOfInt(o, index);
      if (this.arrayrefOfArrayType(o, arrayref) && !((ArrayType)arrayref).getElementType().equals(Type.BOOLEAN) && !((ArrayType)arrayref).getElementType().equals(Type.BYTE)) {
         this.constraintViolated(o, "The 'arrayref' does not refer to an array with elements of a Type.BYTE or Type.BOOLEAN but to an array of '" + ((ArrayType)arrayref).getElementType() + "'.");
      }

   }

   public void visitBASTORE(BASTORE o) {
      Type arrayref = this.stack().peek(2);
      Type index = this.stack().peek(1);
      Type value = this.stack().peek(0);
      this.indexOfInt(o, index);
      this.valueOfInt(o, index);
      if (this.arrayrefOfArrayType(o, arrayref) && !((ArrayType)arrayref).getElementType().equals(Type.BOOLEAN) && !((ArrayType)arrayref).getElementType().equals(Type.BYTE)) {
         this.constraintViolated(o, "The 'arrayref' does not refer to an array with elements of a Type.BYTE or Type.BOOLEAN but to an array of '" + ((ArrayType)arrayref).getElementType() + "'.");
      }

   }

   public void visitBIPUSH(BIPUSH o) {
   }

   public void visitBREAKPOINT(BREAKPOINT o) {
      throw new AssertionViolatedException("In this JustIce verification pass there should not occur an illegal instruction such as BREAKPOINT.");
   }

   public void visitCALOAD(CALOAD o) {
      Type arrayref = this.stack().peek(1);
      Type index = this.stack().peek(0);
      this.indexOfInt(o, index);
      this.arrayrefOfArrayType(o, arrayref);
   }

   public void visitCASTORE(CASTORE o) {
      Type arrayref = this.stack().peek(2);
      Type index = this.stack().peek(1);
      Type value = this.stack().peek(0);
      this.indexOfInt(o, index);
      this.valueOfInt(o, index);
      if (this.arrayrefOfArrayType(o, arrayref) && !((ArrayType)arrayref).getElementType().equals(Type.CHAR)) {
         this.constraintViolated(o, "The 'arrayref' does not refer to an array with elements of type char but to an array of type " + ((ArrayType)arrayref).getElementType() + ".");
      }

   }

   public void visitCHECKCAST(CHECKCAST o) {
      Type objectref = this.stack().peek(0);
      if (!(objectref instanceof ReferenceType)) {
         this.constraintViolated(o, "The 'objectref' is not of a ReferenceType but of type " + objectref + ".");
      } else {
         this.referenceTypeIsInitialized(o, (ReferenceType)objectref);
      }

      Constant c = this.cpg.getConstant(o.getIndex());
      if (!(c instanceof ConstantClass)) {
         this.constraintViolated(o, "The Constant at 'index' is not a ConstantClass, but '" + c + "'.");
      }

   }

   public void visitD2F(D2F o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitD2I(D2I o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitD2L(D2L o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitDADD(DADD o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitDALOAD(DALOAD o) {
      this.indexOfInt(o, this.stack().peek());
      if (this.stack().peek(1) != Type.NULL) {
         if (!(this.stack().peek(1) instanceof ArrayType)) {
            this.constraintViolated(o, "Stack next-to-top must be of type double[] but is '" + this.stack().peek(1) + "'.");
         }

         Type t = ((ArrayType)this.stack().peek(1)).getBasicType();
         if (t != Type.DOUBLE) {
            this.constraintViolated(o, "Stack next-to-top must be of type double[] but is '" + this.stack().peek(1) + "'.");
         }

      }
   }

   public void visitDASTORE(DASTORE o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

      this.indexOfInt(o, this.stack().peek(1));
      if (this.stack().peek(2) != Type.NULL) {
         if (!(this.stack().peek(2) instanceof ArrayType)) {
            this.constraintViolated(o, "Stack next-to-next-to-top must be of type double[] but is '" + this.stack().peek(2) + "'.");
         }

         Type t = ((ArrayType)this.stack().peek(2)).getBasicType();
         if (t != Type.DOUBLE) {
            this.constraintViolated(o, "Stack next-to-next-to-top must be of type double[] but is '" + this.stack().peek(2) + "'.");
         }

      }
   }

   public void visitDCMPG(DCMPG o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitDCMPL(DCMPL o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitDCONST(DCONST o) {
   }

   public void visitDDIV(DDIV o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitDLOAD(DLOAD o) {
   }

   public void visitDMUL(DMUL o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitDNEG(DNEG o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitDREM(DREM o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitDRETURN(DRETURN o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitDSTORE(DSTORE o) {
   }

   public void visitDSUB(DSUB o) {
      if (this.stack().peek() != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack top is not of type 'double', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.DOUBLE) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'double', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitDUP(DUP o) {
      if (this.stack().peek().getSize() != 1) {
         this.constraintViolated(o, "Won't DUP type on stack top '" + this.stack().peek() + "' because it must occupy exactly one slot, not '" + this.stack().peek().getSize() + "'.");
      }

   }

   public void visitDUP_X1(DUP_X1 o) {
      if (this.stack().peek().getSize() != 1) {
         this.constraintViolated(o, "Type on stack top '" + this.stack().peek() + "' should occupy exactly one slot, not '" + this.stack().peek().getSize() + "'.");
      }

      if (this.stack().peek(1).getSize() != 1) {
         this.constraintViolated(o, "Type on stack next-to-top '" + this.stack().peek(1) + "' should occupy exactly one slot, not '" + this.stack().peek(1).getSize() + "'.");
      }

   }

   public void visitDUP_X2(DUP_X2 o) {
      if (this.stack().peek().getSize() != 1) {
         this.constraintViolated(o, "Stack top type must be of size 1, but is '" + this.stack().peek() + "' of size '" + this.stack().peek().getSize() + "'.");
      }

      if (this.stack().peek(1).getSize() != 2) {
         if (this.stack().peek(2).getSize() != 1) {
            this.constraintViolated(o, "If stack top's size is 1 and stack next-to-top's size is 1, stack next-to-next-to-top's size must also be 1, but is: '" + this.stack().peek(2) + "' of size '" + this.stack().peek(2).getSize() + "'.");
         }

      }
   }

   public void visitDUP2(DUP2 o) {
      if (this.stack().peek().getSize() != 2) {
         if (this.stack().peek(1).getSize() != 1) {
            this.constraintViolated(o, "If stack top's size is 1, then stack next-to-top's size must also be 1. But it is '" + this.stack().peek(1) + "' of size '" + this.stack().peek(1).getSize() + "'.");
         }

      }
   }

   public void visitDUP2_X1(DUP2_X1 o) {
      if (this.stack().peek().getSize() == 2) {
         if (this.stack().peek(1).getSize() == 1) {
            return;
         }

         this.constraintViolated(o, "If stack top's size is 2, then stack next-to-top's size must be 1. But it is '" + this.stack().peek(1) + "' of size '" + this.stack().peek(1).getSize() + "'.");
      } else {
         if (this.stack().peek(1).getSize() != 1) {
            this.constraintViolated(o, "If stack top's size is 1, then stack next-to-top's size must also be 1. But it is '" + this.stack().peek(1) + "' of size '" + this.stack().peek(1).getSize() + "'.");
         }

         if (this.stack().peek(2).getSize() != 1) {
            this.constraintViolated(o, "If stack top's size is 1, then stack next-to-next-to-top's size must also be 1. But it is '" + this.stack().peek(2) + "' of size '" + this.stack().peek(2).getSize() + "'.");
         }
      }

   }

   public void visitDUP2_X2(DUP2_X2 o) {
      if (this.stack().peek(0).getSize() == 2) {
         if (this.stack().peek(1).getSize() == 2) {
            return;
         }

         if (this.stack().peek(2).getSize() == 1) {
            return;
         }

         this.constraintViolated(o, "If stack top's size is 2 and stack-next-to-top's size is 1, then stack next-to-next-to-top's size must also be 1. But it is '" + this.stack().peek(2) + "' of size '" + this.stack().peek(2).getSize() + "'.");
      } else if (this.stack().peek(1).getSize() == 1) {
         if (this.stack().peek(2).getSize() == 2) {
            return;
         }

         if (this.stack().peek(3).getSize() == 1) {
            return;
         }
      }

      this.constraintViolated(o, "The operand sizes on the stack do not match any of the four forms of usage of this instruction.");
   }

   public void visitF2D(F2D o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitF2I(F2I o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitF2L(F2L o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitFADD(FADD o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitFALOAD(FALOAD o) {
      this.indexOfInt(o, this.stack().peek());
      if (this.stack().peek(1) != Type.NULL) {
         if (!(this.stack().peek(1) instanceof ArrayType)) {
            this.constraintViolated(o, "Stack next-to-top must be of type float[] but is '" + this.stack().peek(1) + "'.");
         }

         Type t = ((ArrayType)this.stack().peek(1)).getBasicType();
         if (t != Type.FLOAT) {
            this.constraintViolated(o, "Stack next-to-top must be of type float[] but is '" + this.stack().peek(1) + "'.");
         }

      }
   }

   public void visitFASTORE(FASTORE o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

      this.indexOfInt(o, this.stack().peek(1));
      if (this.stack().peek(2) != Type.NULL) {
         if (!(this.stack().peek(2) instanceof ArrayType)) {
            this.constraintViolated(o, "Stack next-to-next-to-top must be of type float[] but is '" + this.stack().peek(2) + "'.");
         }

         Type t = ((ArrayType)this.stack().peek(2)).getBasicType();
         if (t != Type.FLOAT) {
            this.constraintViolated(o, "Stack next-to-next-to-top must be of type float[] but is '" + this.stack().peek(2) + "'.");
         }

      }
   }

   public void visitFCMPG(FCMPG o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitFCMPL(FCMPL o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitFCONST(FCONST o) {
   }

   public void visitFDIV(FDIV o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitFLOAD(FLOAD o) {
   }

   public void visitFMUL(FMUL o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitFNEG(FNEG o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitFREM(FREM o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitFRETURN(FRETURN o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitFSTORE(FSTORE o) {
   }

   public void visitFSUB(FSUB o) {
      if (this.stack().peek() != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'float', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.FLOAT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'float', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitGETFIELD(GETFIELD o) {
      Type objectref = this.stack().peek();
      if (!(objectref instanceof ObjectType) && objectref != Type.NULL) {
         this.constraintViolated(o, "Stack top should be an object reference that's not an array reference, but is '" + objectref + "'.");
      }

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
         if (f.isProtected()) {
            ObjectType classtype = o.getClassType(this.cpg);
            ObjectType curr = new ObjectType(this.mg.getClassName());
            if (classtype.equals(curr) || curr.subclassOf(classtype)) {
               Type t = this.stack().peek();
               if (t == Type.NULL) {
                  return;
               }

               if (!(t instanceof ObjectType)) {
                  this.constraintViolated(o, "The 'objectref' must refer to an object that's not an array. Found instead: '" + t + "'.");
               }

               ObjectType objreftype = (ObjectType)t;
               if (!objreftype.equals(curr) && !objreftype.subclassOf(curr)) {
               }
            }
         }

         if (f.isStatic()) {
            this.constraintViolated(o, "Referenced field '" + f + "' is static which it shouldn't be.");
         }

      }
   }

   public void visitGETSTATIC(GETSTATIC o) {
   }

   public void visitGOTO(GOTO o) {
   }

   public void visitGOTO_W(GOTO_W o) {
   }

   public void visitI2B(I2B o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitI2C(I2C o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitI2D(I2D o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitI2F(I2F o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitI2L(I2L o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitI2S(I2S o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitIADD(IADD o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIALOAD(IALOAD o) {
      this.indexOfInt(o, this.stack().peek());
      if (this.stack().peek(1) != Type.NULL) {
         if (!(this.stack().peek(1) instanceof ArrayType)) {
            this.constraintViolated(o, "Stack next-to-top must be of type int[] but is '" + this.stack().peek(1) + "'.");
         }

         Type t = ((ArrayType)this.stack().peek(1)).getBasicType();
         if (t != Type.INT) {
            this.constraintViolated(o, "Stack next-to-top must be of type int[] but is '" + this.stack().peek(1) + "'.");
         }

      }
   }

   public void visitIAND(IAND o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIASTORE(IASTORE o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      this.indexOfInt(o, this.stack().peek(1));
      if (this.stack().peek(2) != Type.NULL) {
         if (!(this.stack().peek(2) instanceof ArrayType)) {
            this.constraintViolated(o, "Stack next-to-next-to-top must be of type int[] but is '" + this.stack().peek(2) + "'.");
         }

         Type t = ((ArrayType)this.stack().peek(2)).getBasicType();
         if (t != Type.INT) {
            this.constraintViolated(o, "Stack next-to-next-to-top must be of type int[] but is '" + this.stack().peek(2) + "'.");
         }

      }
   }

   public void visitICONST(ICONST o) {
   }

   public void visitIDIV(IDIV o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIF_ACMPEQ(IF_ACMPEQ o) {
      if (!(this.stack().peek() instanceof ReferenceType)) {
         this.constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '" + this.stack().peek() + "'.");
      }

      this.referenceTypeIsInitialized(o, (ReferenceType)this.stack().peek());
      if (!(this.stack().peek(1) instanceof ReferenceType)) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of a ReferenceType, but of type '" + this.stack().peek(1) + "'.");
      }

      this.referenceTypeIsInitialized(o, (ReferenceType)this.stack().peek(1));
   }

   public void visitIF_ACMPNE(IF_ACMPNE o) {
      if (!(this.stack().peek() instanceof ReferenceType)) {
         this.constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '" + this.stack().peek() + "'.");
         this.referenceTypeIsInitialized(o, (ReferenceType)this.stack().peek());
      }

      if (!(this.stack().peek(1) instanceof ReferenceType)) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of a ReferenceType, but of type '" + this.stack().peek(1) + "'.");
         this.referenceTypeIsInitialized(o, (ReferenceType)this.stack().peek(1));
      }

   }

   public void visitIF_ICMPEQ(IF_ICMPEQ o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIF_ICMPGE(IF_ICMPGE o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIF_ICMPGT(IF_ICMPGT o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIF_ICMPLE(IF_ICMPLE o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIF_ICMPLT(IF_ICMPLT o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIF_ICMPNE(IF_ICMPNE o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIFEQ(IFEQ o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitIFGE(IFGE o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitIFGT(IFGT o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitIFLE(IFLE o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitIFLT(IFLT o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitIFNE(IFNE o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitIFNONNULL(IFNONNULL o) {
      if (!(this.stack().peek() instanceof ReferenceType)) {
         this.constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '" + this.stack().peek() + "'.");
      }

      this.referenceTypeIsInitialized(o, (ReferenceType)this.stack().peek());
   }

   public void visitIFNULL(IFNULL o) {
      if (!(this.stack().peek() instanceof ReferenceType)) {
         this.constraintViolated(o, "The value at the stack top is not of a ReferenceType, but of type '" + this.stack().peek() + "'.");
      }

      this.referenceTypeIsInitialized(o, (ReferenceType)this.stack().peek());
   }

   public void visitIINC(IINC o) {
      if (this.locals().maxLocals() <= (o.getType(this.cpg).getSize() == 1 ? o.getIndex() : o.getIndex() + 1)) {
         this.constraintViolated(o, "The 'index' is not a valid index into the local variable array.");
      }

      this.indexOfInt(o, this.locals().get(o.getIndex()));
   }

   public void visitILOAD(ILOAD o) {
   }

   public void visitIMPDEP1(IMPDEP1 o) {
      throw new AssertionViolatedException("In this JustIce verification pass there should not occur an illegal instruction such as IMPDEP1.");
   }

   public void visitIMPDEP2(IMPDEP2 o) {
      throw new AssertionViolatedException("In this JustIce verification pass there should not occur an illegal instruction such as IMPDEP2.");
   }

   public void visitIMUL(IMUL o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitINEG(INEG o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitINSTANCEOF(INSTANCEOF o) {
      Type objectref = this.stack().peek(0);
      if (!(objectref instanceof ReferenceType)) {
         this.constraintViolated(o, "The 'objectref' is not of a ReferenceType but of type " + objectref + ".");
      } else {
         this.referenceTypeIsInitialized(o, (ReferenceType)objectref);
      }

      Constant c = this.cpg.getConstant(o.getIndex());
      if (!(c instanceof ConstantClass)) {
         this.constraintViolated(o, "The Constant at 'index' is not a ConstantClass, but '" + c + "'.");
      }

   }

   public void visitINVOKEINTERFACE(INVOKEINTERFACE o) {
      int count = o.getCount();
      if (count == 0) {
         this.constraintViolated(o, "The 'count' argument must not be 0.");
      }

      ConstantInterfaceMethodref var10000 = (ConstantInterfaceMethodref)this.cpg.getConstant(o.getIndex());
      Type t = o.getType(this.cpg);
      if (t instanceof ObjectType) {
         String name = ((ObjectType)t).getClassName();
         Verifier v = VerifierFactory.getVerifier(name);
         VerificationResult vr = v.doPass2();
         if (vr.getStatus() != 1) {
            this.constraintViolated(o, "Class '" + name + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
         }
      }

      Type[] argtypes = o.getArgumentTypes(this.cpg);
      int nargs = argtypes.length;

      for(int i = nargs - 1; i >= 0; --i) {
         Type fromStack = this.stack().peek(nargs - 1 - i);
         Type fromDesc = argtypes[i];
         if (fromDesc == Type.BOOLEAN || fromDesc == Type.BYTE || fromDesc == Type.CHAR || fromDesc == Type.SHORT) {
            fromDesc = Type.INT;
         }

         if (!fromStack.equals(fromDesc)) {
            if (fromStack instanceof ReferenceType && fromDesc instanceof ReferenceType) {
               ReferenceType rFromStack = (ReferenceType)fromStack;
               ReferenceType var11 = (ReferenceType)fromDesc;
            } else {
               this.constraintViolated(o, "Expecting a '" + fromDesc + "' but found a '" + fromStack + "' on the stack.");
            }
         }
      }

      Type objref = this.stack().peek(nargs);
      if (objref != Type.NULL) {
         if (!(objref instanceof ReferenceType)) {
            this.constraintViolated(o, "Expecting a reference type as 'objectref' on the stack, not a '" + objref + "'.");
         }

         this.referenceTypeIsInitialized(o, (ReferenceType)objref);
         if (!(objref instanceof ObjectType)) {
            if (!(objref instanceof ArrayType)) {
               this.constraintViolated(o, "Expecting an ObjectType as 'objectref' on the stack, not a '" + objref + "'.");
            } else {
               objref = GENERIC_ARRAY;
            }
         }

         String objref_classname = ((ObjectType)objref).getClassName();
         o.getClassName(this.cpg);
         int counted_count = 1;

         for(int i = 0; i < nargs; ++i) {
            counted_count += argtypes[i].getSize();
         }

         if (count != counted_count) {
            this.constraintViolated(o, "The 'count' argument should probably read '" + counted_count + "' but is '" + count + "'.");
         }

      }
   }

   public void visitINVOKESPECIAL(INVOKESPECIAL o) {
      if (o.getMethodName(this.cpg).equals("<init>") && !(this.stack().peek(o.getArgumentTypes(this.cpg).length) instanceof UninitializedObjectType)) {
         this.constraintViolated(o, "Possibly initializing object twice. A valid instruction sequence must not have an uninitialized object on the operand stack or in a local variable during a backwards branch, or in a local variable in code protected by an exception handler. Please see The Java Virtual Machine Specification, Second Edition, 4.9.4 (pages 147 and 148) for details.");
      }

      Type t = o.getType(this.cpg);
      if (t instanceof ObjectType) {
         String name = ((ObjectType)t).getClassName();
         Verifier v = VerifierFactory.getVerifier(name);
         VerificationResult vr = v.doPass2();
         if (vr.getStatus() != 1) {
            this.constraintViolated(o, "Class '" + name + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
         }
      }

      Type[] argtypes = o.getArgumentTypes(this.cpg);
      int nargs = argtypes.length;

      Object fromDesc;
      for(int i = nargs - 1; i >= 0; --i) {
         Type fromStack = this.stack().peek(nargs - 1 - i);
         fromDesc = argtypes[i];
         if (fromDesc == Type.BOOLEAN || fromDesc == Type.BYTE || fromDesc == Type.CHAR || fromDesc == Type.SHORT) {
            fromDesc = Type.INT;
         }

         if (!fromStack.equals(fromDesc)) {
            if (fromStack instanceof ReferenceType && fromDesc instanceof ReferenceType) {
               ReferenceType rFromStack = (ReferenceType)fromStack;
               ReferenceType var9 = (ReferenceType)fromDesc;
            } else {
               this.constraintViolated(o, "Expecting a '" + fromDesc + "' but found a '" + fromStack + "' on the stack.");
            }
         }
      }

      Type objref = this.stack().peek(nargs);
      if (objref != Type.NULL) {
         if (!(objref instanceof ReferenceType)) {
            this.constraintViolated(o, "Expecting a reference type as 'objectref' on the stack, not a '" + objref + "'.");
         }

         fromDesc = null;
         String objref_classname;
         if (!o.getMethodName(this.cpg).equals("<init>")) {
            this.referenceTypeIsInitialized(o, (ReferenceType)objref);
            if (!(objref instanceof ObjectType)) {
               if (!(objref instanceof ArrayType)) {
                  this.constraintViolated(o, "Expecting an ObjectType as 'objectref' on the stack, not a '" + objref + "'.");
               } else {
                  objref = GENERIC_ARRAY;
               }
            }

            objref_classname = ((ObjectType)objref).getClassName();
         } else {
            if (!(objref instanceof UninitializedObjectType)) {
               this.constraintViolated(o, "Expecting an UninitializedObjectType as 'objectref' on the stack, not a '" + objref + "'. Otherwise, you couldn't invoke a method since an array has no methods (not to speak of a return address).");
            }

            objref_classname = ((UninitializedObjectType)objref).getInitialized().getClassName();
         }

         String theClass = o.getClassName(this.cpg);
         if (!Repository.instanceOf(objref_classname, theClass)) {
            this.constraintViolated(o, "The 'objref' item '" + objref + "' does not implement '" + theClass + "' as expected.");
         }

      }
   }

   public void visitINVOKESTATIC(INVOKESTATIC o) {
      Type t = o.getType(this.cpg);
      if (t instanceof ObjectType) {
         String name = ((ObjectType)t).getClassName();
         Verifier v = VerifierFactory.getVerifier(name);
         VerificationResult vr = v.doPass2();
         if (vr.getStatus() != 1) {
            this.constraintViolated(o, "Class '" + name + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
         }
      }

      Type[] argtypes = o.getArgumentTypes(this.cpg);
      int nargs = argtypes.length;

      for(int i = nargs - 1; i >= 0; --i) {
         Type fromStack = this.stack().peek(nargs - 1 - i);
         Type fromDesc = argtypes[i];
         if (fromDesc == Type.BOOLEAN || fromDesc == Type.BYTE || fromDesc == Type.CHAR || fromDesc == Type.SHORT) {
            fromDesc = Type.INT;
         }

         if (!fromStack.equals(fromDesc)) {
            if (fromStack instanceof ReferenceType && fromDesc instanceof ReferenceType) {
               ReferenceType rFromStack = (ReferenceType)fromStack;
               ReferenceType var9 = (ReferenceType)fromDesc;
            } else {
               this.constraintViolated(o, "Expecting a '" + fromDesc + "' but found a '" + fromStack + "' on the stack.");
            }
         }
      }

   }

   public void visitINVOKEVIRTUAL(INVOKEVIRTUAL o) {
      Type t = o.getType(this.cpg);
      if (t instanceof ObjectType) {
         String name = ((ObjectType)t).getClassName();
         Verifier v = VerifierFactory.getVerifier(name);
         VerificationResult vr = v.doPass2();
         if (vr.getStatus() != 1) {
            this.constraintViolated(o, "Class '" + name + "' is referenced, but cannot be loaded and resolved: '" + vr + "'.");
         }
      }

      Type[] argtypes = o.getArgumentTypes(this.cpg);
      int nargs = argtypes.length;

      for(int i = nargs - 1; i >= 0; --i) {
         Type fromStack = this.stack().peek(nargs - 1 - i);
         Type fromDesc = argtypes[i];
         if (fromDesc == Type.BOOLEAN || fromDesc == Type.BYTE || fromDesc == Type.CHAR || fromDesc == Type.SHORT) {
            fromDesc = Type.INT;
         }

         if (!fromStack.equals(fromDesc)) {
            if (fromStack instanceof ReferenceType && fromDesc instanceof ReferenceType) {
               ReferenceType rFromStack = (ReferenceType)fromStack;
               ReferenceType var9 = (ReferenceType)fromDesc;
            } else {
               this.constraintViolated(o, "Expecting a '" + fromDesc + "' but found a '" + fromStack + "' on the stack.");
            }
         }
      }

      Type objref = this.stack().peek(nargs);
      if (objref != Type.NULL) {
         if (!(objref instanceof ReferenceType)) {
            this.constraintViolated(o, "Expecting a reference type as 'objectref' on the stack, not a '" + objref + "'.");
         }

         this.referenceTypeIsInitialized(o, (ReferenceType)objref);
         if (!(objref instanceof ObjectType)) {
            if (!(objref instanceof ArrayType)) {
               this.constraintViolated(o, "Expecting an ObjectType as 'objectref' on the stack, not a '" + objref + "'.");
            } else {
               objref = GENERIC_ARRAY;
            }
         }

         String objref_classname = ((ObjectType)objref).getClassName();
         String theClass = o.getClassName(this.cpg);
         if (!Repository.instanceOf(objref_classname, theClass)) {
            this.constraintViolated(o, "The 'objref' item '" + objref + "' does not implement '" + theClass + "' as expected.");
         }

      }
   }

   public void visitIOR(IOR o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIREM(IREM o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIRETURN(IRETURN o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitISHL(ISHL o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitISHR(ISHR o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitISTORE(ISTORE o) {
   }

   public void visitISUB(ISUB o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIUSHR(IUSHR o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitIXOR(IXOR o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.INT) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'int', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitJSR(JSR o) {
   }

   public void visitJSR_W(JSR_W o) {
   }

   public void visitL2D(L2D o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitL2F(L2F o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitL2I(L2I o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitLADD(LADD o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitLALOAD(LALOAD o) {
      this.indexOfInt(o, this.stack().peek());
      if (this.stack().peek(1) != Type.NULL) {
         if (!(this.stack().peek(1) instanceof ArrayType)) {
            this.constraintViolated(o, "Stack next-to-top must be of type long[] but is '" + this.stack().peek(1) + "'.");
         }

         Type t = ((ArrayType)this.stack().peek(1)).getBasicType();
         if (t != Type.LONG) {
            this.constraintViolated(o, "Stack next-to-top must be of type long[] but is '" + this.stack().peek(1) + "'.");
         }

      }
   }

   public void visitLAND(LAND o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitLASTORE(LASTORE o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

      this.indexOfInt(o, this.stack().peek(1));
      if (this.stack().peek(2) != Type.NULL) {
         if (!(this.stack().peek(2) instanceof ArrayType)) {
            this.constraintViolated(o, "Stack next-to-next-to-top must be of type long[] but is '" + this.stack().peek(2) + "'.");
         }

         Type t = ((ArrayType)this.stack().peek(2)).getBasicType();
         if (t != Type.LONG) {
            this.constraintViolated(o, "Stack next-to-next-to-top must be of type long[] but is '" + this.stack().peek(2) + "'.");
         }

      }
   }

   public void visitLCMP(LCMP o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitLCONST(LCONST o) {
   }

   public void visitLDC(LDC o) {
      Constant c = this.cpg.getConstant(o.getIndex());
      if (!(c instanceof ConstantInteger) && !(c instanceof ConstantFloat) && !(c instanceof ConstantString)) {
         this.constraintViolated(o, "Referenced constant should be a CONSTANT_Integer, a CONSTANT_Float or a CONSTANT_String, but is '" + c + "'.");
      }

   }

   public void visitLDC_W(LDC_W o) {
      Constant c = this.cpg.getConstant(o.getIndex());
      if (!(c instanceof ConstantInteger) && !(c instanceof ConstantFloat) && !(c instanceof ConstantString)) {
         this.constraintViolated(o, "Referenced constant should be a CONSTANT_Integer, a CONSTANT_Float or a CONSTANT_String, but is '" + c + "'.");
      }

   }

   public void visitLDC2_W(LDC2_W o) {
      Constant c = this.cpg.getConstant(o.getIndex());
      if (!(c instanceof ConstantLong) && !(c instanceof ConstantDouble)) {
         this.constraintViolated(o, "Referenced constant should be a CONSTANT_Integer, a CONSTANT_Float or a CONSTANT_String, but is '" + c + "'.");
      }

   }

   public void visitLDIV(LDIV o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitLLOAD(LLOAD o) {
   }

   public void visitLMUL(LMUL o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitLNEG(LNEG o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitLOOKUPSWITCH(LOOKUPSWITCH o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitLOR(LOR o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitLREM(LREM o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitLRETURN(LRETURN o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitLSHL(LSHL o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitLSHR(LSHR o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitLSTORE(LSTORE o) {
   }

   public void visitLSUB(LSUB o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitLUSHR(LUSHR o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitLXOR(LXOR o) {
      if (this.stack().peek() != Type.LONG) {
         this.constraintViolated(o, "The value at the stack top is not of type 'long', but of type '" + this.stack().peek() + "'.");
      }

      if (this.stack().peek(1) != Type.LONG) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of type 'long', but of type '" + this.stack().peek(1) + "'.");
      }

   }

   public void visitMONITORENTER(MONITORENTER o) {
      if (!(this.stack().peek() instanceof ReferenceType)) {
         this.constraintViolated(o, "The stack top should be of a ReferenceType, but is '" + this.stack().peek() + "'.");
      }

      this.referenceTypeIsInitialized(o, (ReferenceType)this.stack().peek());
   }

   public void visitMONITOREXIT(MONITOREXIT o) {
      if (!(this.stack().peek() instanceof ReferenceType)) {
         this.constraintViolated(o, "The stack top should be of a ReferenceType, but is '" + this.stack().peek() + "'.");
      }

      this.referenceTypeIsInitialized(o, (ReferenceType)this.stack().peek());
   }

   public void visitMULTIANEWARRAY(MULTIANEWARRAY o) {
      int dimensions = o.getDimensions();

      for(int i = 0; i < dimensions; ++i) {
         if (this.stack().peek(i) != Type.INT) {
            this.constraintViolated(o, "The '" + dimensions + "' upper stack types should be 'int' but aren't.");
         }
      }

   }

   public void visitNEW(NEW o) {
      Type t = o.getType(this.cpg);
      if (!(t instanceof ReferenceType)) {
         throw new AssertionViolatedException("NEW.getType() returning a non-reference type?!");
      } else {
         if (!(t instanceof ObjectType)) {
            this.constraintViolated(o, "Expecting a class type (ObjectType) to work on. Found: '" + t + "'.");
         }

         ObjectType obj = (ObjectType)t;
         if (!obj.referencesClass()) {
            this.constraintViolated(o, "Expecting a class type (ObjectType) to work on. Found: '" + obj + "'.");
         }

      }
   }

   public void visitNEWARRAY(NEWARRAY o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

   }

   public void visitNOP(NOP o) {
   }

   public void visitPOP(POP o) {
      if (this.stack().peek().getSize() != 1) {
         this.constraintViolated(o, "Stack top size should be 1 but stack top is '" + this.stack().peek() + "' of size '" + this.stack().peek().getSize() + "'.");
      }

   }

   public void visitPOP2(POP2 o) {
      if (this.stack().peek().getSize() != 2) {
         this.constraintViolated(o, "Stack top size should be 2 but stack top is '" + this.stack().peek() + "' of size '" + this.stack().peek().getSize() + "'.");
      }

   }

   public void visitPUTFIELD(PUTFIELD o) {
      Type objectref = this.stack().peek(1);
      if (!(objectref instanceof ObjectType) && objectref != Type.NULL) {
         this.constraintViolated(o, "Stack next-to-top should be an object reference that's not an array reference, but is '" + objectref + "'.");
      }

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
         Type value = this.stack().peek();
         Type t = Type.getType(f.getSignature());
         Type shouldbe = t;
         if (t == Type.BOOLEAN || t == Type.BYTE || t == Type.CHAR || t == Type.SHORT) {
            shouldbe = Type.INT;
         }

         if (t instanceof ReferenceType) {
            ReferenceType rvalue = null;
            if (value instanceof ReferenceType) {
               rvalue = (ReferenceType)value;
               this.referenceTypeIsInitialized(o, rvalue);
            } else {
               this.constraintViolated(o, "The stack top type '" + value + "' is not of a reference type as expected.");
            }
         } else if (shouldbe != value) {
            this.constraintViolated(o, "The stack top type '" + value + "' is not of type '" + shouldbe + "' as expected.");
         }

         if (f.isProtected()) {
            ObjectType classtype = o.getClassType(this.cpg);
            ObjectType curr = new ObjectType(this.mg.getClassName());
            if (classtype.equals(curr) || curr.subclassOf(classtype)) {
               Type tp = this.stack().peek(1);
               if (tp == Type.NULL) {
                  return;
               }

               if (!(tp instanceof ObjectType)) {
                  this.constraintViolated(o, "The 'objectref' must refer to an object that's not an array. Found instead: '" + tp + "'.");
               }

               ObjectType objreftype = (ObjectType)tp;
               if (!objreftype.equals(curr) && !objreftype.subclassOf(curr)) {
                  this.constraintViolated(o, "The referenced field has the ACC_PROTECTED modifier, and it's a member of the current class or a superclass of the current class. However, the referenced object type '" + this.stack().peek() + "' is not the current class or a subclass of the current class.");
               }
            }
         }

         if (f.isStatic()) {
            this.constraintViolated(o, "Referenced field '" + f + "' is static which it shouldn't be.");
         }

      }
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
         Type value = this.stack().peek();
         Type t = Type.getType(f.getSignature());
         Type shouldbe = t;
         if (t == Type.BOOLEAN || t == Type.BYTE || t == Type.CHAR || t == Type.SHORT) {
            shouldbe = Type.INT;
         }

         if (t instanceof ReferenceType) {
            ReferenceType rvalue = null;
            if (value instanceof ReferenceType) {
               rvalue = (ReferenceType)value;
               this.referenceTypeIsInitialized(o, rvalue);
            } else {
               this.constraintViolated(o, "The stack top type '" + value + "' is not of a reference type as expected.");
            }

            if (!rvalue.isAssignmentCompatibleWith((Type)shouldbe)) {
               this.constraintViolated(o, "The stack top type '" + value + "' is not assignment compatible with '" + shouldbe + "'.");
            }
         } else if (shouldbe != value) {
            this.constraintViolated(o, "The stack top type '" + value + "' is not of type '" + shouldbe + "' as expected.");
         }

      }
   }

   public void visitRET(RET o) {
      if (!(this.locals().get(o.getIndex()) instanceof ReturnaddressType)) {
         this.constraintViolated(o, "Expecting a ReturnaddressType in local variable " + o.getIndex() + ".");
      }

      if (this.locals().get(o.getIndex()) == ReturnaddressType.NO_TARGET) {
         throw new AssertionViolatedException("Oops: RET expecting a target!");
      }
   }

   public void visitRETURN(RETURN o) {
      if (this.mg.getName().equals("<init>") && Frame._this != null && !this.mg.getClassName().equals(Type.OBJECT.getClassName())) {
         this.constraintViolated(o, "Leaving a constructor that itself did not call a constructor.");
      }

   }

   public void visitSALOAD(SALOAD o) {
      this.indexOfInt(o, this.stack().peek());
      if (this.stack().peek(1) != Type.NULL) {
         if (!(this.stack().peek(1) instanceof ArrayType)) {
            this.constraintViolated(o, "Stack next-to-top must be of type short[] but is '" + this.stack().peek(1) + "'.");
         }

         Type t = ((ArrayType)this.stack().peek(1)).getBasicType();
         if (t != Type.SHORT) {
            this.constraintViolated(o, "Stack next-to-top must be of type short[] but is '" + this.stack().peek(1) + "'.");
         }

      }
   }

   public void visitSASTORE(SASTORE o) {
      if (this.stack().peek() != Type.INT) {
         this.constraintViolated(o, "The value at the stack top is not of type 'int', but of type '" + this.stack().peek() + "'.");
      }

      this.indexOfInt(o, this.stack().peek(1));
      if (this.stack().peek(2) != Type.NULL) {
         if (!(this.stack().peek(2) instanceof ArrayType)) {
            this.constraintViolated(o, "Stack next-to-next-to-top must be of type short[] but is '" + this.stack().peek(2) + "'.");
         }

         Type t = ((ArrayType)this.stack().peek(2)).getBasicType();
         if (t != Type.SHORT) {
            this.constraintViolated(o, "Stack next-to-next-to-top must be of type short[] but is '" + this.stack().peek(2) + "'.");
         }

      }
   }

   public void visitSIPUSH(SIPUSH o) {
   }

   public void visitSWAP(SWAP o) {
      if (this.stack().peek().getSize() != 1) {
         this.constraintViolated(o, "The value at the stack top is not of size '1', but of size '" + this.stack().peek().getSize() + "'.");
      }

      if (this.stack().peek(1).getSize() != 1) {
         this.constraintViolated(o, "The value at the stack next-to-top is not of size '1', but of size '" + this.stack().peek(1).getSize() + "'.");
      }

   }

   public void visitTABLESWITCH(TABLESWITCH o) {
      this.indexOfInt(o, this.stack().peek());
   }
}
