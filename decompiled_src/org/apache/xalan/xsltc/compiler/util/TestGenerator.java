package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;

public final class TestGenerator extends MethodGenerator {
   private static int CONTEXT_NODE_INDEX = 1;
   private static int CURRENT_NODE_INDEX = 4;
   private static int ITERATOR_INDEX = 6;
   private Instruction _aloadDom;
   private final Instruction _iloadCurrent;
   private final Instruction _iloadContext;
   private final Instruction _istoreCurrent;
   private final Instruction _istoreContext;
   private final Instruction _astoreIterator;
   private final Instruction _aloadIterator;

   public TestGenerator(int access_flags, org.apache.bcel.generic.Type return_type, org.apache.bcel.generic.Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp) {
      super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cp);
      this._iloadCurrent = new ILOAD(CURRENT_NODE_INDEX);
      this._istoreCurrent = new ISTORE(CURRENT_NODE_INDEX);
      this._iloadContext = new ILOAD(CONTEXT_NODE_INDEX);
      this._istoreContext = new ILOAD(CONTEXT_NODE_INDEX);
      this._astoreIterator = new ASTORE(ITERATOR_INDEX);
      this._aloadIterator = new ALOAD(ITERATOR_INDEX);
   }

   public int getHandlerIndex() {
      return -1;
   }

   public int getIteratorIndex() {
      return ITERATOR_INDEX;
   }

   public void setDomIndex(int domIndex) {
      this._aloadDom = new ALOAD(domIndex);
   }

   public Instruction loadDOM() {
      return this._aloadDom;
   }

   public Instruction loadCurrentNode() {
      return this._iloadCurrent;
   }

   public Instruction loadContextNode() {
      return this._iloadContext;
   }

   public Instruction storeContextNode() {
      return this._istoreContext;
   }

   public Instruction storeCurrentNode() {
      return this._istoreCurrent;
   }

   public Instruction storeIterator() {
      return this._astoreIterator;
   }

   public Instruction loadIterator() {
      return this._aloadIterator;
   }

   public int getLocalIndex(String name) {
      return name.equals("current") ? CURRENT_NODE_INDEX : super.getLocalIndex(name);
   }
}
