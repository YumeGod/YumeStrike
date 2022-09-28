package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ACONST_NULL;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;

public final class CompareGenerator extends MethodGenerator {
   private static int DOM_INDEX = 1;
   private static int CURRENT_INDEX = 2;
   private static int LEVEL_INDEX = 3;
   private static int TRANSLET_INDEX = 4;
   private static int LAST_INDEX = 5;
   private int ITERATOR_INDEX = 6;
   private final Instruction _iloadCurrent;
   private final Instruction _istoreCurrent;
   private final Instruction _aloadDom;
   private final Instruction _iloadLast;
   private final Instruction _aloadIterator;
   private final Instruction _astoreIterator;

   public CompareGenerator(int access_flags, org.apache.bcel.generic.Type return_type, org.apache.bcel.generic.Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp) {
      super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cp);
      this._iloadCurrent = new ILOAD(CURRENT_INDEX);
      this._istoreCurrent = new ISTORE(CURRENT_INDEX);
      this._aloadDom = new ALOAD(DOM_INDEX);
      this._iloadLast = new ILOAD(LAST_INDEX);
      LocalVariableGen iterator = this.addLocalVariable("iterator", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), (InstructionHandle)null, (InstructionHandle)null);
      this.ITERATOR_INDEX = iterator.getIndex();
      this._aloadIterator = new ALOAD(this.ITERATOR_INDEX);
      this._astoreIterator = new ASTORE(this.ITERATOR_INDEX);
      il.append((Instruction)(new ACONST_NULL()));
      il.append(this.storeIterator());
   }

   public Instruction loadLastNode() {
      return this._iloadLast;
   }

   public Instruction loadCurrentNode() {
      return this._iloadCurrent;
   }

   public Instruction storeCurrentNode() {
      return this._istoreCurrent;
   }

   public Instruction loadDOM() {
      return this._aloadDom;
   }

   public int getHandlerIndex() {
      return -1;
   }

   public int getIteratorIndex() {
      return -1;
   }

   public Instruction storeIterator() {
      return this._astoreIterator;
   }

   public Instruction loadIterator() {
      return this._aloadIterator;
   }

   public int getLocalIndex(String name) {
      return name.equals("current") ? CURRENT_INDEX : super.getLocalIndex(name);
   }
}
