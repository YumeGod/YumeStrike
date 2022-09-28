package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.IntType;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.runtime.Operators;

final class RelationalExpr extends Expression {
   private int _op;
   private Expression _left;
   private Expression _right;

   public RelationalExpr(int op, Expression left, Expression right) {
      this._op = op;
      (this._left = left).setParent(this);
      (this._right = right).setParent(this);
   }

   public void setParser(Parser parser) {
      super.setParser(parser);
      this._left.setParser(parser);
      this._right.setParser(parser);
   }

   public boolean hasPositionCall() {
      if (this._left.hasPositionCall()) {
         return true;
      } else {
         return this._right.hasPositionCall();
      }
   }

   public boolean hasLastCall() {
      return this._left.hasLastCall() || this._right.hasLastCall();
   }

   public boolean hasReferenceArgs() {
      return this._left.getType() instanceof ReferenceType || this._right.getType() instanceof ReferenceType;
   }

   public boolean hasNodeArgs() {
      return this._left.getType() instanceof NodeType || this._right.getType() instanceof NodeType;
   }

   public boolean hasNodeSetArgs() {
      return this._left.getType() instanceof NodeSetType || this._right.getType() instanceof NodeSetType;
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      Type tleft = this._left.typeCheck(stable);
      Type tright = this._right.typeCheck(stable);
      if (tleft instanceof ResultTreeType && tright instanceof ResultTreeType) {
         this._right = new CastExpr(this._right, Type.Real);
         this._left = new CastExpr(this._left, Type.Real);
         return super._type = Type.Boolean;
      } else {
         MethodType ptype;
         Type arg1;
         Type arg2;
         if (this.hasReferenceArgs()) {
            ptype = null;
            arg1 = null;
            arg2 = null;
            VariableRefBase ref;
            VariableBase var;
            if (tleft instanceof ReferenceType && this._left instanceof VariableRefBase) {
               ref = (VariableRefBase)this._left;
               var = ref.getVariable();
               arg1 = var.getType();
            }

            if (tright instanceof ReferenceType && this._right instanceof VariableRefBase) {
               ref = (VariableRefBase)this._right;
               var = ref.getVariable();
               arg2 = var.getType();
            }

            Type type;
            if (arg1 == null) {
               type = arg2;
            } else if (arg2 == null) {
               type = arg1;
            } else {
               type = Type.Real;
            }

            if (type == null) {
               type = Type.Real;
            }

            this._right = new CastExpr(this._right, type);
            this._left = new CastExpr(this._left, type);
            return super._type = Type.Boolean;
         } else if (this.hasNodeSetArgs()) {
            if (tright instanceof NodeSetType) {
               Expression temp = this._right;
               this._right = this._left;
               this._left = temp;
               this._op = this._op == 2 ? 3 : (this._op == 3 ? 2 : (this._op == 4 ? 5 : 4));
               tright = this._right.getType();
            }

            if (tright instanceof NodeType) {
               this._right = new CastExpr(this._right, Type.NodeSet);
            }

            if (tright instanceof IntType) {
               this._right = new CastExpr(this._right, Type.Real);
            }

            if (tright instanceof ResultTreeType) {
               this._right = new CastExpr(this._right, Type.String);
            }

            return super._type = Type.Boolean;
         } else {
            if (this.hasNodeArgs()) {
               if (tleft instanceof BooleanType) {
                  this._right = new CastExpr(this._right, Type.Boolean);
                  tright = Type.Boolean;
               }

               if (tright instanceof BooleanType) {
                  this._left = new CastExpr(this._left, Type.Boolean);
                  tleft = Type.Boolean;
               }
            }

            ptype = this.lookupPrimop(stable, Operators.getOpNames(this._op), new MethodType(Type.Void, tleft, tright));
            if (ptype != null) {
               arg1 = (Type)ptype.argsType().elementAt(0);
               if (!arg1.identicalTo(tleft)) {
                  this._left = new CastExpr(this._left, arg1);
               }

               arg2 = (Type)ptype.argsType().elementAt(1);
               if (!arg2.identicalTo(tright)) {
                  this._right = new CastExpr(this._right, arg1);
               }

               return super._type = ptype.resultType();
            } else {
               throw new TypeCheckError(this);
            }
         }
      }
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      if (!this.hasNodeSetArgs() && !this.hasReferenceArgs()) {
         this.translateDesynthesized(classGen, methodGen);
         this.synthesize(classGen, methodGen);
      } else {
         ConstantPoolGen cpg = classGen.getConstantPool();
         InstructionList il = methodGen.getInstructionList();
         this._left.translate(classGen, methodGen);
         this._left.startIterator(classGen, methodGen);
         this._right.translate(classGen, methodGen);
         this._right.startIterator(classGen, methodGen);
         il.append((CompoundInstruction)(new PUSH(cpg, this._op)));
         il.append(methodGen.loadDOM());
         int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "compare", "(" + this._left.getType().toSignature() + this._right.getType().toSignature() + "I" + "Lorg/apache/xalan/xsltc/DOM;" + ")Z");
         il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(index)));
      }

   }

   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
      if (!this.hasNodeSetArgs() && !this.hasReferenceArgs()) {
         BranchInstruction bi = null;
         InstructionList il = methodGen.getInstructionList();
         this._left.translate(classGen, methodGen);
         this._right.translate(classGen, methodGen);
         boolean tozero = false;
         Type tleft = this._left.getType();
         if (tleft instanceof RealType) {
            il.append(tleft.CMP(this._op == 3 || this._op == 5));
            tleft = Type.Int;
            tozero = true;
         }

         switch (this._op) {
            case 2:
               bi = tleft.LE(tozero);
               break;
            case 3:
               bi = tleft.GE(tozero);
               break;
            case 4:
               bi = tleft.LT(tozero);
               break;
            case 5:
               bi = tleft.GT(tozero);
               break;
            default:
               ErrorMsg msg = new ErrorMsg("ILLEGAL_RELAT_OP_ERR", this);
               this.getParser().reportError(2, msg);
         }

         super._falseList.add(il.append(bi));
      } else {
         this.translate(classGen, methodGen);
         this.desynthesize(classGen, methodGen);
      }

   }

   public String toString() {
      return Operators.getOpNames(this._op) + '(' + this._left + ", " + this._right + ')';
   }
}
