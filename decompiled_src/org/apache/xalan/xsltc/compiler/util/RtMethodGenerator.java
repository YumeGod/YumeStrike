package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;

public final class RtMethodGenerator extends MethodGenerator {
   private static final int HANDLER_INDEX = 2;
   private final Instruction _astoreHandler = new ASTORE(2);
   private final Instruction _aloadHandler = new ALOAD(2);

   public RtMethodGenerator(int access_flags, org.apache.bcel.generic.Type return_type, org.apache.bcel.generic.Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp) {
      super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cp);
   }

   public int getIteratorIndex() {
      return -1;
   }

   public final Instruction storeHandler() {
      return this._astoreHandler;
   }

   public final Instruction loadHandler() {
      return this._aloadHandler;
   }

   public int getLocalIndex(String name) {
      return -1;
   }
}
