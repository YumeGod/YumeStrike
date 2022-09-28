package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xml.dtm.Axis;

final class UnionPathExpr extends Expression {
   private final Expression _pathExpr;
   private final Expression _rest;
   private boolean _reverse = false;
   private Expression[] _components;

   public UnionPathExpr(Expression pathExpr, Expression rest) {
      this._pathExpr = pathExpr;
      this._rest = rest;
   }

   public void setParser(Parser parser) {
      super.setParser(parser);
      Vector components = new Vector();
      this.flatten(components);
      int size = components.size();
      this._components = (Expression[])components.toArray(new Expression[size]);

      for(int i = 0; i < size; ++i) {
         this._components[i].setParser(parser);
         this._components[i].setParent(this);
         if (this._components[i] instanceof Step) {
            Step step = (Step)this._components[i];
            int axis = step.getAxis();
            int type = step.getNodeType();
            if (axis == 2 || type == 2) {
               this._components[i] = this._components[0];
               this._components[0] = step;
            }

            if (Axis.isReverse(axis)) {
               this._reverse = true;
            }
         }
      }

      if (this.getParent() instanceof Expression) {
         this._reverse = false;
      }

   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      int length = this._components.length;

      for(int i = 0; i < length; ++i) {
         if (this._components[i].typeCheck(stable) != Type.NodeSet) {
            this._components[i] = new CastExpr(this._components[i], Type.NodeSet);
         }
      }

      return super._type = Type.NodeSet;
   }

   public String toString() {
      return "union(" + this._pathExpr + ", " + this._rest + ')';
   }

   private void flatten(Vector components) {
      components.addElement(this._pathExpr);
      if (this._rest != null) {
         if (this._rest instanceof UnionPathExpr) {
            ((UnionPathExpr)this._rest).flatten(components);
         } else {
            components.addElement(this._rest);
         }
      }

   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int init = cpg.addMethodref("org.apache.xalan.xsltc.dom.UnionIterator", "<init>", "(Lorg/apache/xalan/xsltc/DOM;)V");
      int iter = cpg.addMethodref("org.apache.xalan.xsltc.dom.UnionIterator", "addIterator", "(Lorg/apache/xml/dtm/DTMAxisIterator;)Lorg/apache/xalan/xsltc/dom/UnionIterator;");
      il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.UnionIterator"))));
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
      il.append(methodGen.loadDOM());
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(init)));
      int length = this._components.length;

      for(int i = 0; i < length; ++i) {
         this._components[i].translate(classGen, methodGen);
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(iter)));
      }

      if (this._reverse) {
         int order = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "orderNodes", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)Lorg/apache/xml/dtm/DTMAxisIterator;");
         il.append(methodGen.loadDOM());
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.SWAP);
         il.append(methodGen.loadContextNode());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(order, 3)));
      }

   }
}
