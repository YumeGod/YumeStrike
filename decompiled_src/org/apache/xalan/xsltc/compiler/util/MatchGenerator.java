package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;

public final class MatchGenerator extends MethodGenerator {
   private static int CURRENT_INDEX = 1;
   private int _iteratorIndex = -1;
   private final Instruction _iloadCurrent;
   private final Instruction _istoreCurrent;
   private Instruction _aloadDom;

   public MatchGenerator(int access_flags, org.apache.bcel.generic.Type return_type, org.apache.bcel.generic.Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp) {
      super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cp);
      this._iloadCurrent = new ILOAD(CURRENT_INDEX);
      this._istoreCurrent = new ISTORE(CURRENT_INDEX);
   }

   public Instruction loadCurrentNode() {
      return this._iloadCurrent;
   }

   public Instruction storeCurrentNode() {
      return this._istoreCurrent;
   }

   public int getHandlerIndex() {
      return -1;
   }

   public Instruction loadDOM() {
      return this._aloadDom;
   }

   public void setDomIndex(int domIndex) {
      this._aloadDom = new ALOAD(domIndex);
   }

   public int getIteratorIndex() {
      return this._iteratorIndex;
   }

   public void setIteratorIndex(int iteratorIndex) {
      this._iteratorIndex = iteratorIndex;
   }

   public int getLocalIndex(String name) {
      return name.equals("current") ? CURRENT_INDEX : super.getLocalIndex(name);
   }
}
