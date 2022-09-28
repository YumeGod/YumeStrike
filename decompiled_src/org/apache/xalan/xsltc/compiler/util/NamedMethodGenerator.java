package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;

public final class NamedMethodGenerator extends MethodGenerator {
   protected static final int CURRENT_INDEX = 4;
   private static final int PARAM_START_INDEX = 5;

   public NamedMethodGenerator(int access_flags, org.apache.bcel.generic.Type return_type, org.apache.bcel.generic.Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp) {
      super(access_flags, return_type, arg_types, arg_names, method_name, class_name, il, cp);
   }

   public int getLocalIndex(String name) {
      return name.equals("current") ? 4 : super.getLocalIndex(name);
   }

   public Instruction loadParameter(int index) {
      return new ALOAD(index + 5);
   }

   public Instruction storeParameter(int index) {
      return new ASTORE(index + 5);
   }
}
