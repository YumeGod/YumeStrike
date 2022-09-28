package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.SIPUSH;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MultiHashtable;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class CastExpr extends Expression {
   private final Expression _left;
   private static MultiHashtable InternalTypeMap = new MultiHashtable();
   private boolean _typeTest = false;

   public CastExpr(Expression left, Type type) throws TypeCheckError {
      this._left = left;
      super._type = type;
      if (this._left instanceof Step && super._type == Type.Boolean) {
         Step step = (Step)this._left;
         if (step.getAxis() == 13 && step.getNodeType() != -1) {
            this._typeTest = true;
         }
      }

      this.setParser(left.getParser());
      this.setParent(left.getParent());
      left.setParent(this);
      this.typeCheck(left.getParser().getSymbolTable());
   }

   public Expression getExpr() {
      return this._left;
   }

   public boolean hasPositionCall() {
      return this._left.hasPositionCall();
   }

   public boolean hasLastCall() {
      return this._left.hasLastCall();
   }

   public String toString() {
      return "cast(" + this._left + ", " + super._type + ")";
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      Type tleft = this._left.getType();
      if (tleft == null) {
         tleft = this._left.typeCheck(stable);
      }

      if (tleft instanceof NodeType) {
         tleft = Type.Node;
      } else if (tleft instanceof ResultTreeType) {
         tleft = Type.ResultTree;
      }

      if (InternalTypeMap.maps(tleft, super._type) != null) {
         return super._type;
      } else {
         throw new TypeCheckError(new ErrorMsg("DATA_CONVERSION_ERR", tleft.toString(), super._type.toString()));
      }
   }

   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
      Type ltype = this._left.getType();
      if (this._typeTest) {
         ConstantPoolGen cpg = classGen.getConstantPool();
         InstructionList il = methodGen.getInstructionList();
         int idx = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
         il.append((org.apache.bcel.generic.Instruction)(new SIPUSH((short)((Step)this._left).getNodeType())));
         il.append(methodGen.loadDOM());
         il.append(methodGen.loadContextNode());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(idx, 2)));
         super._falseList.add(il.append((BranchInstruction)(new IF_ICMPNE((InstructionHandle)null))));
      } else {
         this._left.translate(classGen, methodGen);
         if (super._type != ltype) {
            this._left.startIterator(classGen, methodGen);
            if (super._type instanceof BooleanType) {
               FlowList fl = ltype.translateToDesynthesized(classGen, methodGen, super._type);
               if (fl != null) {
                  super._falseList.append(fl);
               }
            } else {
               ltype.translateTo(classGen, methodGen, super._type);
            }
         }
      }

   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      Type ltype = this._left.getType();
      this._left.translate(classGen, methodGen);
      if (!super._type.identicalTo(ltype)) {
         this._left.startIterator(classGen, methodGen);
         ltype.translateTo(classGen, methodGen, super._type);
      }

   }

   static {
      InternalTypeMap.put(Type.Boolean, Type.Boolean);
      InternalTypeMap.put(Type.Boolean, Type.Real);
      InternalTypeMap.put(Type.Boolean, Type.String);
      InternalTypeMap.put(Type.Boolean, Type.Reference);
      InternalTypeMap.put(Type.Boolean, Type.Object);
      InternalTypeMap.put(Type.Real, Type.Real);
      InternalTypeMap.put(Type.Real, Type.Int);
      InternalTypeMap.put(Type.Real, Type.Boolean);
      InternalTypeMap.put(Type.Real, Type.String);
      InternalTypeMap.put(Type.Real, Type.Reference);
      InternalTypeMap.put(Type.Real, Type.Object);
      InternalTypeMap.put(Type.Int, Type.Int);
      InternalTypeMap.put(Type.Int, Type.Real);
      InternalTypeMap.put(Type.Int, Type.Boolean);
      InternalTypeMap.put(Type.Int, Type.String);
      InternalTypeMap.put(Type.Int, Type.Reference);
      InternalTypeMap.put(Type.Int, Type.Object);
      InternalTypeMap.put(Type.String, Type.String);
      InternalTypeMap.put(Type.String, Type.Boolean);
      InternalTypeMap.put(Type.String, Type.Real);
      InternalTypeMap.put(Type.String, Type.Reference);
      InternalTypeMap.put(Type.String, Type.Object);
      InternalTypeMap.put(Type.NodeSet, Type.NodeSet);
      InternalTypeMap.put(Type.NodeSet, Type.Boolean);
      InternalTypeMap.put(Type.NodeSet, Type.Real);
      InternalTypeMap.put(Type.NodeSet, Type.String);
      InternalTypeMap.put(Type.NodeSet, Type.Node);
      InternalTypeMap.put(Type.NodeSet, Type.Reference);
      InternalTypeMap.put(Type.NodeSet, Type.Object);
      InternalTypeMap.put(Type.Node, Type.Node);
      InternalTypeMap.put(Type.Node, Type.Boolean);
      InternalTypeMap.put(Type.Node, Type.Real);
      InternalTypeMap.put(Type.Node, Type.String);
      InternalTypeMap.put(Type.Node, Type.NodeSet);
      InternalTypeMap.put(Type.Node, Type.Reference);
      InternalTypeMap.put(Type.Node, Type.Object);
      InternalTypeMap.put(Type.ResultTree, Type.ResultTree);
      InternalTypeMap.put(Type.ResultTree, Type.Boolean);
      InternalTypeMap.put(Type.ResultTree, Type.Real);
      InternalTypeMap.put(Type.ResultTree, Type.String);
      InternalTypeMap.put(Type.ResultTree, Type.NodeSet);
      InternalTypeMap.put(Type.ResultTree, Type.Reference);
      InternalTypeMap.put(Type.ResultTree, Type.Object);
      InternalTypeMap.put(Type.Reference, Type.Reference);
      InternalTypeMap.put(Type.Reference, Type.Boolean);
      InternalTypeMap.put(Type.Reference, Type.Int);
      InternalTypeMap.put(Type.Reference, Type.Real);
      InternalTypeMap.put(Type.Reference, Type.String);
      InternalTypeMap.put(Type.Reference, Type.Node);
      InternalTypeMap.put(Type.Reference, Type.NodeSet);
      InternalTypeMap.put(Type.Reference, Type.ResultTree);
      InternalTypeMap.put(Type.Reference, Type.Object);
      InternalTypeMap.put(Type.Object, Type.String);
      InternalTypeMap.put(Type.Void, Type.String);
   }
}
