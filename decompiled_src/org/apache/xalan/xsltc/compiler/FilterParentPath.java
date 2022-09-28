package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class FilterParentPath extends Expression {
   private Expression _filterExpr;
   private Expression _path;
   private boolean _hasDescendantAxis = false;

   public FilterParentPath(Expression filterExpr, Expression path) {
      (this._path = path).setParent(this);
      (this._filterExpr = filterExpr).setParent(this);
   }

   public void setParser(Parser parser) {
      super.setParser(parser);
      this._filterExpr.setParser(parser);
      this._path.setParser(parser);
   }

   public String toString() {
      return "FilterParentPath(" + this._filterExpr + ", " + this._path + ')';
   }

   public void setDescendantAxis() {
      this._hasDescendantAxis = true;
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      Type ftype = this._filterExpr.typeCheck(stable);
      if (!(ftype instanceof NodeSetType)) {
         if (ftype instanceof ReferenceType) {
            this._filterExpr = new CastExpr(this._filterExpr, Type.NodeSet);
         } else {
            if (!(ftype instanceof NodeType)) {
               throw new TypeCheckError(this);
            }

            this._filterExpr = new CastExpr(this._filterExpr, Type.NodeSet);
         }
      }

      Type ptype = this._path.typeCheck(stable);
      if (!(ptype instanceof NodeSetType)) {
         this._path = new CastExpr(this._path, Type.NodeSet);
      }

      return super._type = Type.NodeSet;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int initSI = cpg.addMethodref("org.apache.xalan.xsltc.dom.StepIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xml/dtm/DTMAxisIterator;)V");
      this._filterExpr.translate(classGen, methodGen);
      LocalVariableGen filterTemp = methodGen.addLocalVariable("filter_parent_path_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), il.getEnd(), (InstructionHandle)null);
      il.append((org.apache.bcel.generic.Instruction)(new ASTORE(filterTemp.getIndex())));
      this._path.translate(classGen, methodGen);
      LocalVariableGen pathTemp = methodGen.addLocalVariable("filter_parent_path_tmp2", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), il.getEnd(), (InstructionHandle)null);
      il.append((org.apache.bcel.generic.Instruction)(new ASTORE(pathTemp.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.StepIterator"))));
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(filterTemp.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new ALOAD(pathTemp.getIndex())));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(initSI)));
      int order;
      if (this._hasDescendantAxis) {
         order = cpg.addMethodref("org.apache.xml.dtm.ref.DTMAxisIteratorBase", "includeSelf", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(order)));
      }

      if (!(this.getParent() instanceof RelativeLocationPath) && !(this.getParent() instanceof FilterParentPath)) {
         order = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "orderNodes", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)Lorg/apache/xml/dtm/DTMAxisIterator;");
         il.append(methodGen.loadDOM());
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.SWAP);
         il.append(methodGen.loadContextNode());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(order, 3)));
      }

   }
}
