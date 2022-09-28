package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ICONST;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.dtm.Axis;

final class Step extends RelativeLocationPath {
   private int _axis;
   private Vector _predicates;
   private boolean _hadPredicates = false;
   private int _nodeType;

   public Step(int axis, int nodeType, Vector predicates) {
      this._axis = axis;
      this._nodeType = nodeType;
      this._predicates = predicates;
   }

   public void setParser(Parser parser) {
      super.setParser(parser);
      if (this._predicates != null) {
         int n = this._predicates.size();

         for(int i = 0; i < n; ++i) {
            Predicate exp = (Predicate)this._predicates.elementAt(i);
            exp.setParser(parser);
            exp.setParent(this);
         }
      }

   }

   public int getAxis() {
      return this._axis;
   }

   public void setAxis(int axis) {
      this._axis = axis;
   }

   public int getNodeType() {
      return this._nodeType;
   }

   public Vector getPredicates() {
      return this._predicates;
   }

   public void addPredicates(Vector predicates) {
      if (this._predicates == null) {
         this._predicates = predicates;
      } else {
         this._predicates.addAll(predicates);
      }

   }

   private boolean hasParentPattern() {
      SyntaxTreeNode parent = this.getParent();
      return parent instanceof ParentPattern || parent instanceof ParentLocationPath || parent instanceof UnionPathExpr || parent instanceof FilterParentPath;
   }

   private boolean hasPredicates() {
      return this._predicates != null && this._predicates.size() > 0;
   }

   private boolean isPredicate() {
      SyntaxTreeNode parent = this;

      while(parent != null) {
         parent = ((SyntaxTreeNode)parent).getParent();
         if (parent instanceof Predicate) {
            return true;
         }
      }

      return false;
   }

   public boolean isAbbreviatedDot() {
      return this._nodeType == -1 && this._axis == 13;
   }

   public boolean isAbbreviatedDDot() {
      return this._nodeType == -1 && this._axis == 10;
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      this._hadPredicates = this.hasPredicates();
      if (this.isAbbreviatedDot()) {
         super._type = !this.hasParentPattern() && !this.hasPredicates() ? Type.Node : Type.NodeSet;
      } else {
         super._type = Type.NodeSet;
      }

      if (this._predicates != null) {
         int n = this._predicates.size();

         for(int i = 0; i < n; ++i) {
            Expression pred = (Expression)this._predicates.elementAt(i);
            pred.typeCheck(stable);
         }
      }

      return super._type;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      if (this.hasPredicates()) {
         this.translatePredicates(classGen, methodGen);
      } else {
         int star = 0;
         String name = null;
         XSLTC xsltc = this.getParser().getXSLTC();
         if (this._nodeType >= 14) {
            Vector ni = xsltc.getNamesIndex();
            name = (String)ni.elementAt(this._nodeType - 14);
            star = name.lastIndexOf(42);
         }

         if (this._axis == 2 && this._nodeType != 2 && this._nodeType != -1 && !this.hasParentPattern() && star == 0) {
            int iter = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getTypedAxisIterator", "(II)Lorg/apache/xml/dtm/DTMAxisIterator;");
            il.append(methodGen.loadDOM());
            il.append((CompoundInstruction)(new PUSH(cpg, 2)));
            il.append((CompoundInstruction)(new PUSH(cpg, this._nodeType)));
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(iter, 3)));
            return;
         }

         SyntaxTreeNode parent = this.getParent();
         int git;
         if (this.isAbbreviatedDot()) {
            if (super._type == Type.Node) {
               il.append(methodGen.loadContextNode());
            } else if (parent instanceof ParentLocationPath) {
               git = cpg.addMethodref("org.apache.xalan.xsltc.dom.SingletonIterator", "<init>", "(I)V");
               il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.SingletonIterator"))));
               il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
               il.append(methodGen.loadContextNode());
               il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(git)));
            } else {
               git = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getAxisIterator", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
               il.append(methodGen.loadDOM());
               il.append((CompoundInstruction)(new PUSH(cpg, this._axis)));
               il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(git, 2)));
            }

            return;
         }

         if (parent instanceof ParentLocationPath && parent.getParent() instanceof ParentLocationPath && this._nodeType == 1 && !this._hadPredicates) {
            this._nodeType = -1;
         }

         switch (this._nodeType) {
            case 0:
            default:
               if (star > 1) {
                  String namespace;
                  if (this._axis == 2) {
                     namespace = name.substring(0, star - 2);
                  } else {
                     namespace = name.substring(0, star - 1);
                  }

                  int nsType = xsltc.registerNamespace(namespace);
                  int ns = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNamespaceAxisIterator", "(II)Lorg/apache/xml/dtm/DTMAxisIterator;");
                  il.append(methodGen.loadDOM());
                  il.append((CompoundInstruction)(new PUSH(cpg, this._axis)));
                  il.append((CompoundInstruction)(new PUSH(cpg, nsType)));
                  il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(ns, 3)));
                  break;
               }
            case 1:
               int ty = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getTypedAxisIterator", "(II)Lorg/apache/xml/dtm/DTMAxisIterator;");
               il.append(methodGen.loadDOM());
               il.append((CompoundInstruction)(new PUSH(cpg, this._axis)));
               il.append((CompoundInstruction)(new PUSH(cpg, this._nodeType)));
               il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(ty, 3)));
               break;
            case 2:
               this._axis = 2;
            case -1:
               git = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getAxisIterator", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
               il.append(methodGen.loadDOM());
               il.append((CompoundInstruction)(new PUSH(cpg, this._axis)));
               il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(git, 2)));
         }
      }

   }

   public void translatePredicates(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int idx = false;
      if (this._predicates.size() == 0) {
         this.translate(classGen, methodGen);
      } else {
         Predicate predicate = (Predicate)this._predicates.lastElement();
         this._predicates.remove(predicate);
         int idx;
         if (predicate.isNodeValueTest()) {
            Step step = predicate.getStep();
            il.append(methodGen.loadDOM());
            if (step.isAbbreviatedDot()) {
               this.translate(classGen, methodGen);
               il.append((org.apache.bcel.generic.Instruction)(new ICONST(0)));
            } else {
               ParentLocationPath path = new ParentLocationPath(this, step);

               try {
                  path.typeCheck(this.getParser().getSymbolTable());
               } catch (TypeCheckError var10) {
               }

               path.translate(classGen, methodGen);
               il.append((org.apache.bcel.generic.Instruction)(new ICONST(1)));
            }

            predicate.translate(classGen, methodGen);
            idx = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeValueIterator", "(Lorg/apache/xml/dtm/DTMAxisIterator;ILjava/lang/String;Z)Lorg/apache/xml/dtm/DTMAxisIterator;");
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(idx, 5)));
         } else if (predicate.isNthDescendant()) {
            il.append(methodGen.loadDOM());
            il.append((org.apache.bcel.generic.Instruction)(new ICONST(predicate.getPosType())));
            predicate.translate(classGen, methodGen);
            il.append((org.apache.bcel.generic.Instruction)(new ICONST(0)));
            idx = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNthDescendant", "(IIZ)Lorg/apache/xml/dtm/DTMAxisIterator;");
            il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(idx, 4)));
         } else {
            LocalVariableGen iteratorTemp;
            LocalVariableGen predicateValueTemp;
            if (predicate.isNthPositionFilter()) {
               idx = cpg.addMethodref("org.apache.xalan.xsltc.dom.NthIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)V");
               this.translatePredicates(classGen, methodGen);
               iteratorTemp = methodGen.addLocalVariable("step_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), il.getEnd(), (InstructionHandle)null);
               il.append((org.apache.bcel.generic.Instruction)(new ASTORE(iteratorTemp.getIndex())));
               predicate.translate(classGen, methodGen);
               predicateValueTemp = methodGen.addLocalVariable("step_tmp2", Util.getJCRefType("I"), il.getEnd(), (InstructionHandle)null);
               il.append((org.apache.bcel.generic.Instruction)(new ISTORE(predicateValueTemp.getIndex())));
               il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.NthIterator"))));
               il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
               il.append((org.apache.bcel.generic.Instruction)(new ALOAD(iteratorTemp.getIndex())));
               il.append((org.apache.bcel.generic.Instruction)(new ILOAD(predicateValueTemp.getIndex())));
               il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(idx)));
            } else {
               idx = cpg.addMethodref("org.apache.xalan.xsltc.dom.CurrentNodeListIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xalan/xsltc/dom/CurrentNodeListFilter;ILorg/apache/xalan/xsltc/runtime/AbstractTranslet;)V");
               this.translatePredicates(classGen, methodGen);
               iteratorTemp = methodGen.addLocalVariable("step_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), il.getEnd(), (InstructionHandle)null);
               il.append((org.apache.bcel.generic.Instruction)(new ASTORE(iteratorTemp.getIndex())));
               predicate.translateFilter(classGen, methodGen);
               predicateValueTemp = methodGen.addLocalVariable("step_tmp2", Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/CurrentNodeListFilter;"), il.getEnd(), (InstructionHandle)null);
               il.append((org.apache.bcel.generic.Instruction)(new ASTORE(predicateValueTemp.getIndex())));
               il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.CurrentNodeListIterator"))));
               il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
               il.append((org.apache.bcel.generic.Instruction)(new ALOAD(iteratorTemp.getIndex())));
               il.append((org.apache.bcel.generic.Instruction)(new ALOAD(predicateValueTemp.getIndex())));
               il.append(methodGen.loadCurrentNode());
               il.append(classGen.loadTranslet());
               if (classGen.isExternal()) {
                  String className = classGen.getClassName();
                  il.append((org.apache.bcel.generic.Instruction)(new CHECKCAST(cpg.addClass(className))));
               }

               il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(idx)));
            }
         }
      }

   }

   public String toString() {
      StringBuffer buffer = new StringBuffer("step(\"");
      buffer.append(Axis.getNames(this._axis)).append("\", ").append(this._nodeType);
      if (this._predicates != null) {
         int n = this._predicates.size();

         for(int i = 0; i < n; ++i) {
            Predicate pred = (Predicate)this._predicates.elementAt(i);
            buffer.append(", ").append(pred.toString());
         }
      }

      return buffer.append(')').toString();
   }
}
