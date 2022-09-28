package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.NumberType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.runtime.Operators;

final class EqualityExpr extends Expression {
   private final int _op;
   private Expression _left;
   private Expression _right;

   public EqualityExpr(int op, Expression left, Expression right) {
      this._op = op;
      (this._left = left).setParent(this);
      (this._right = right).setParent(this);
   }

   public void setParser(Parser parser) {
      super.setParser(parser);
      this._left.setParser(parser);
      this._right.setParser(parser);
   }

   public String toString() {
      return Operators.getOpNames(this._op) + '(' + this._left + ", " + this._right + ')';
   }

   public Expression getLeft() {
      return this._left;
   }

   public Expression getRight() {
      return this._right;
   }

   public boolean getOp() {
      return this._op != 1;
   }

   public boolean hasPositionCall() {
      if (this._left.hasPositionCall()) {
         return true;
      } else {
         return this._right.hasPositionCall();
      }
   }

   public boolean hasLastCall() {
      if (this._left.hasLastCall()) {
         return true;
      } else {
         return this._right.hasLastCall();
      }
   }

   private void swapArguments() {
      Expression temp = this._left;
      this._left = this._right;
      this._right = temp;
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      Type tleft = this._left.typeCheck(stable);
      Type tright = this._right.typeCheck(stable);
      if (tleft.isSimple() && tright.isSimple()) {
         if (tleft != tright) {
            if (tleft instanceof BooleanType) {
               this._right = new CastExpr(this._right, Type.Boolean);
            } else if (tright instanceof BooleanType) {
               this._left = new CastExpr(this._left, Type.Boolean);
            } else if (!(tleft instanceof NumberType) && !(tright instanceof NumberType)) {
               this._left = new CastExpr(this._left, Type.String);
               this._right = new CastExpr(this._right, Type.String);
            } else {
               this._left = new CastExpr(this._left, Type.Real);
               this._right = new CastExpr(this._right, Type.Real);
            }
         }
      } else if (tleft instanceof ReferenceType) {
         this._right = new CastExpr(this._right, Type.Reference);
      } else if (tright instanceof ReferenceType) {
         this._left = new CastExpr(this._left, Type.Reference);
      } else if (tleft instanceof NodeType && tright == Type.String) {
         this._left = new CastExpr(this._left, Type.String);
      } else if (tleft == Type.String && tright instanceof NodeType) {
         this._right = new CastExpr(this._right, Type.String);
      } else if (tleft instanceof NodeType && tright instanceof NodeType) {
         this._left = new CastExpr(this._left, Type.String);
         this._right = new CastExpr(this._right, Type.String);
      } else if (!(tleft instanceof NodeType) || !(tright instanceof NodeSetType)) {
         if (tleft instanceof NodeSetType && tright instanceof NodeType) {
            this.swapArguments();
         } else {
            if (tleft instanceof NodeType) {
               this._left = new CastExpr(this._left, Type.NodeSet);
            }

            if (tright instanceof NodeType) {
               this._right = new CastExpr(this._right, Type.NodeSet);
            }

            if (tleft.isSimple() || tleft instanceof ResultTreeType && tright instanceof NodeSetType) {
               this.swapArguments();
            }

            if (this._right.getType() instanceof IntType) {
               this._right = new CastExpr(this._right, Type.Real);
            }
         }
      }

      return super._type = Type.Boolean;
   }

   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
      Type tleft = this._left.getType();
      InstructionList il = methodGen.getInstructionList();
      if (tleft instanceof BooleanType) {
         this._left.translate(classGen, methodGen);
         this._right.translate(classGen, methodGen);
         super._falseList.add(il.append((BranchInstruction)(this._op == 0 ? new IF_ICMPNE((InstructionHandle)null) : new IF_ICMPEQ((InstructionHandle)null))));
      } else if (tleft instanceof NumberType) {
         this._left.translate(classGen, methodGen);
         this._right.translate(classGen, methodGen);
         if (tleft instanceof RealType) {
            il.append(InstructionConstants.DCMPG);
            super._falseList.add(il.append((BranchInstruction)(this._op == 0 ? new IFNE((InstructionHandle)null) : new IFEQ((InstructionHandle)null))));
         } else {
            super._falseList.add(il.append((BranchInstruction)(this._op == 0 ? new IF_ICMPNE((InstructionHandle)null) : new IF_ICMPEQ((InstructionHandle)null))));
         }
      } else {
         this.translate(classGen, methodGen);
         this.desynthesize(classGen, methodGen);
      }

   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      Type tleft = this._left.getType();
      Type tright = this._right.getType();
      if (!(tleft instanceof BooleanType) && !(tleft instanceof NumberType)) {
         if (tleft instanceof StringType) {
            int equals = cpg.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
            this._left.translate(classGen, methodGen);
            this._right.translate(classGen, methodGen);
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(equals)));
            if (this._op == 1) {
               il.append(InstructionConstants.ICONST_1);
               il.append((org.apache.bcel.generic.Instruction)InstructionConstants.IXOR);
            }

         } else {
            int compare;
            if (tleft instanceof ResultTreeType) {
               if (tright instanceof BooleanType) {
                  this._right.translate(classGen, methodGen);
                  if (this._op == 1) {
                     il.append(InstructionConstants.ICONST_1);
                     il.append((org.apache.bcel.generic.Instruction)InstructionConstants.IXOR);
                  }

               } else if (tright instanceof RealType) {
                  this._left.translate(classGen, methodGen);
                  tleft.translateTo(classGen, methodGen, Type.Real);
                  this._right.translate(classGen, methodGen);
                  il.append(InstructionConstants.DCMPG);
                  BranchHandle falsec = il.append((BranchInstruction)(this._op == 0 ? new IFNE((InstructionHandle)null) : new IFEQ((InstructionHandle)null)));
                  il.append(InstructionConstants.ICONST_1);
                  BranchHandle truec = il.append((BranchInstruction)(new GOTO((InstructionHandle)null)));
                  falsec.setTarget(il.append(InstructionConstants.ICONST_0));
                  truec.setTarget(il.append(InstructionConstants.NOP));
               } else {
                  this._left.translate(classGen, methodGen);
                  tleft.translateTo(classGen, methodGen, Type.String);
                  this._right.translate(classGen, methodGen);
                  if (tright instanceof ResultTreeType) {
                     tright.translateTo(classGen, methodGen, Type.String);
                  }

                  compare = cpg.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
                  il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(compare)));
                  if (this._op == 1) {
                     il.append(InstructionConstants.ICONST_1);
                     il.append((org.apache.bcel.generic.Instruction)InstructionConstants.IXOR);
                  }

               }
            } else if (tleft instanceof NodeSetType && tright instanceof BooleanType) {
               this._left.translate(classGen, methodGen);
               this._left.startIterator(classGen, methodGen);
               Type.NodeSet.translateTo(classGen, methodGen, Type.Boolean);
               this._right.translate(classGen, methodGen);
               il.append((org.apache.bcel.generic.Instruction)InstructionConstants.IXOR);
               if (this._op == 0) {
                  il.append(InstructionConstants.ICONST_1);
                  il.append((org.apache.bcel.generic.Instruction)InstructionConstants.IXOR);
               }

            } else if (tleft instanceof NodeSetType && tright instanceof StringType) {
               this._left.translate(classGen, methodGen);
               this._left.startIterator(classGen, methodGen);
               this._right.translate(classGen, methodGen);
               il.append((CompoundInstruction)(new PUSH(cpg, this._op)));
               il.append(methodGen.loadDOM());
               compare = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "compare", "(" + tleft.toSignature() + tright.toSignature() + "I" + "Lorg/apache/xalan/xsltc/DOM;" + ")Z");
               il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(compare)));
            } else {
               this._left.translate(classGen, methodGen);
               this._left.startIterator(classGen, methodGen);
               this._right.translate(classGen, methodGen);
               this._right.startIterator(classGen, methodGen);
               if (tright instanceof ResultTreeType) {
                  tright.translateTo(classGen, methodGen, Type.String);
                  tright = Type.String;
               }

               il.append((CompoundInstruction)(new PUSH(cpg, this._op)));
               il.append(methodGen.loadDOM());
               compare = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "compare", "(" + tleft.toSignature() + tright.toSignature() + "I" + "Lorg/apache/xalan/xsltc/DOM;" + ")Z");
               il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(compare)));
            }
         }
      } else {
         this.translateDesynthesized(classGen, methodGen);
         this.synthesize(classGen, methodGen);
      }
   }
}
