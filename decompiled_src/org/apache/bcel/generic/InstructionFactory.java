package org.apache.bcel.generic;

public class InstructionFactory implements InstructionConstants {
   protected ClassGen cg;
   protected ConstantPoolGen cp;
   private static MethodObject[] append_mos;

   public InstructionFactory(ClassGen cg, ConstantPoolGen cp) {
      this.cg = cg;
      this.cp = cp;
   }

   public InstructionFactory(ClassGen cg) {
      this(cg, cg.getConstantPool());
   }

   public InstructionFactory(ConstantPoolGen cp) {
      this((ClassGen)null, cp);
   }

   public InvokeInstruction createInvoke(String class_name, String name, Type ret_type, Type[] arg_types, short kind) {
      int nargs = 0;
      String signature = Type.getMethodSignature(ret_type, arg_types);

      for(int i = 0; i < arg_types.length; ++i) {
         nargs += arg_types[i].getSize();
      }

      int index;
      if (kind == 185) {
         index = this.cp.addInterfaceMethodref(class_name, name, signature);
      } else {
         index = this.cp.addMethodref(class_name, name, signature);
      }

      switch (kind) {
         case 182:
            return new INVOKEVIRTUAL(index);
         case 183:
            return new INVOKESPECIAL(index);
         case 184:
            return new INVOKESTATIC(index);
         case 185:
            return new INVOKEINTERFACE(index, nargs + 1);
         default:
            throw new RuntimeException("Oops: Unknown invoke kind:" + kind);
      }
   }

   public InstructionList createPrintln(String s) {
      InstructionList il = new InstructionList();
      int out = this.cp.addFieldref("java.lang.System", "out", "Ljava/io/PrintStream;");
      int println = this.cp.addMethodref("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
      il.append((Instruction)(new GETSTATIC(out)));
      il.append((CompoundInstruction)(new PUSH(this.cp, s)));
      il.append((Instruction)(new INVOKEVIRTUAL(println)));
      return il;
   }

   private InvokeInstruction createInvoke(MethodObject m, short kind) {
      return this.createInvoke(m.class_name, m.name, m.result_type, m.arg_types, kind);
   }

   private static final boolean isString(Type type) {
      return type instanceof ObjectType && ((ObjectType)type).getClassName().equals("java.lang.String");
   }

   public Instruction createAppend(Type type) {
      byte t = type.getType();
      if (isString(type)) {
         return this.createInvoke(append_mos[0], (short)182);
      } else {
         switch (t) {
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
               return this.createInvoke(append_mos[t], (short)182);
            case 12:
            default:
               throw new RuntimeException("Oops: No append for this type? " + type);
            case 13:
            case 14:
               return this.createInvoke(append_mos[1], (short)182);
         }
      }
   }

   public FieldInstruction createFieldAccess(String class_name, String name, Type type, short kind) {
      String signature = type.getSignature();
      int index = this.cp.addFieldref(class_name, name, signature);
      switch (kind) {
         case 178:
            return new GETSTATIC(index);
         case 179:
            return new PUTSTATIC(index);
         case 180:
            return new GETFIELD(index);
         case 181:
            return new PUTFIELD(index);
         default:
            throw new RuntimeException("Oops: Unknown getfield kind:" + kind);
      }
   }

   public static Instruction createThis() {
      return new ALOAD(0);
   }

   public static ReturnInstruction createReturn(Type type) {
      switch (type.getType()) {
         case 4:
         case 5:
         case 8:
         case 9:
         case 10:
            return InstructionConstants.IRETURN;
         case 6:
            return InstructionConstants.FRETURN;
         case 7:
            return InstructionConstants.DRETURN;
         case 11:
            return InstructionConstants.LRETURN;
         case 12:
            return InstructionConstants.RETURN;
         case 13:
         case 14:
            return InstructionConstants.ARETURN;
         default:
            throw new RuntimeException("Invalid type: " + type);
      }
   }

   private static final ArithmeticInstruction createBinaryIntOp(char first, String op) {
      switch (first) {
         case '%':
            return InstructionConstants.IREM;
         case '&':
            return InstructionConstants.IAND;
         case '*':
            return InstructionConstants.IMUL;
         case '+':
            return InstructionConstants.IADD;
         case '-':
            return InstructionConstants.ISUB;
         case '/':
            return InstructionConstants.IDIV;
         case '<':
            return InstructionConstants.ISHL;
         case '>':
            return op.equals(">>>") ? InstructionConstants.IUSHR : InstructionConstants.ISHR;
         case '^':
            return InstructionConstants.IXOR;
         case '|':
            return InstructionConstants.IOR;
         default:
            throw new RuntimeException("Invalid operand " + op);
      }
   }

   private static final ArithmeticInstruction createBinaryLongOp(char first, String op) {
      switch (first) {
         case '%':
            return InstructionConstants.LREM;
         case '&':
            return InstructionConstants.LAND;
         case '*':
            return InstructionConstants.LMUL;
         case '+':
            return InstructionConstants.LADD;
         case '-':
            return InstructionConstants.LSUB;
         case '/':
            return InstructionConstants.LDIV;
         case '<':
            return InstructionConstants.LSHL;
         case '>':
            return op.equals(">>>") ? InstructionConstants.LUSHR : InstructionConstants.LSHR;
         case '^':
            return InstructionConstants.LXOR;
         case '|':
            return InstructionConstants.LOR;
         default:
            throw new RuntimeException("Invalid operand " + op);
      }
   }

   private static final ArithmeticInstruction createBinaryFloatOp(char op) {
      switch (op) {
         case '*':
            return InstructionConstants.FMUL;
         case '+':
            return InstructionConstants.FADD;
         case ',':
         case '.':
         default:
            throw new RuntimeException("Invalid operand " + op);
         case '-':
            return InstructionConstants.FSUB;
         case '/':
            return InstructionConstants.FDIV;
      }
   }

   private static final ArithmeticInstruction createBinaryDoubleOp(char op) {
      switch (op) {
         case '*':
            return InstructionConstants.DMUL;
         case '+':
            return InstructionConstants.DADD;
         case ',':
         case '.':
         default:
            throw new RuntimeException("Invalid operand " + op);
         case '-':
            return InstructionConstants.DSUB;
         case '/':
            return InstructionConstants.DDIV;
      }
   }

   public static ArithmeticInstruction createBinaryOperation(String op, Type type) {
      char first = op.toCharArray()[0];
      switch (type.getType()) {
         case 5:
         case 8:
         case 9:
         case 10:
            return createBinaryIntOp(first, op);
         case 6:
            return createBinaryFloatOp(first);
         case 7:
            return createBinaryDoubleOp(first);
         case 11:
            return createBinaryLongOp(first, op);
         default:
            throw new RuntimeException("Invalid type " + type);
      }
   }

   public static StackInstruction createPop(int size) {
      return size == 2 ? InstructionConstants.POP2 : InstructionConstants.POP;
   }

   public static StackInstruction createDup(int size) {
      return size == 2 ? InstructionConstants.DUP2 : InstructionConstants.DUP;
   }

   public static StackInstruction createDup_2(int size) {
      return size == 2 ? InstructionConstants.DUP2_X2 : InstructionConstants.DUP_X2;
   }

   public static StackInstruction createDup_1(int size) {
      return size == 2 ? InstructionConstants.DUP2_X1 : InstructionConstants.DUP_X1;
   }

   public static LocalVariableInstruction createStore(Type type, int index) {
      switch (type.getType()) {
         case 4:
         case 5:
         case 8:
         case 9:
         case 10:
            return new ISTORE(index);
         case 6:
            return new FSTORE(index);
         case 7:
            return new DSTORE(index);
         case 11:
            return new LSTORE(index);
         case 12:
         default:
            throw new RuntimeException("Invalid type " + type);
         case 13:
         case 14:
            return new ASTORE(index);
      }
   }

   public static LocalVariableInstruction createLoad(Type type, int index) {
      switch (type.getType()) {
         case 4:
         case 5:
         case 8:
         case 9:
         case 10:
            return new ILOAD(index);
         case 6:
            return new FLOAD(index);
         case 7:
            return new DLOAD(index);
         case 11:
            return new LLOAD(index);
         case 12:
         default:
            throw new RuntimeException("Invalid type " + type);
         case 13:
         case 14:
            return new ALOAD(index);
      }
   }

   public static ArrayInstruction createArrayLoad(Type type) {
      switch (type.getType()) {
         case 4:
         case 8:
            return InstructionConstants.BALOAD;
         case 5:
            return InstructionConstants.CALOAD;
         case 6:
            return InstructionConstants.FALOAD;
         case 7:
            return InstructionConstants.DALOAD;
         case 9:
            return InstructionConstants.SALOAD;
         case 10:
            return InstructionConstants.IALOAD;
         case 11:
            return InstructionConstants.LALOAD;
         case 12:
         default:
            throw new RuntimeException("Invalid type " + type);
         case 13:
         case 14:
            return InstructionConstants.AALOAD;
      }
   }

   public static ArrayInstruction createArrayStore(Type type) {
      switch (type.getType()) {
         case 4:
         case 8:
            return InstructionConstants.BASTORE;
         case 5:
            return InstructionConstants.CASTORE;
         case 6:
            return InstructionConstants.FASTORE;
         case 7:
            return InstructionConstants.DASTORE;
         case 9:
            return InstructionConstants.SASTORE;
         case 10:
            return InstructionConstants.IASTORE;
         case 11:
            return InstructionConstants.LASTORE;
         case 12:
         default:
            throw new RuntimeException("Invalid type " + type);
         case 13:
         case 14:
            return InstructionConstants.AASTORE;
      }
   }

   public Instruction createCast(Type src_type, Type dest_type) {
      if (src_type instanceof BasicType && dest_type instanceof BasicType) {
         byte dest = dest_type.getType();
         byte src = src_type.getType();
         if (dest == 11 && (src == 5 || src == 8 || src == 9)) {
            src = 10;
         }

         String[] short_names = new String[]{"C", "F", "D", "B", "S", "I", "L"};
         String name = "org.apache.bcel.generic." + short_names[src - 5] + "2" + short_names[dest - 5];
         Instruction i = null;

         try {
            i = (Instruction)Class.forName(name).newInstance();
            return i;
         } catch (Exception var9) {
            throw new RuntimeException("Could not find instruction: " + name);
         }
      } else if (src_type instanceof ReferenceType && dest_type instanceof ReferenceType) {
         return dest_type instanceof ArrayType ? new CHECKCAST(this.cp.addArrayClass((ArrayType)dest_type)) : new CHECKCAST(this.cp.addClass(((ObjectType)dest_type).getClassName()));
      } else {
         throw new RuntimeException("Can not cast " + src_type + " to " + dest_type);
      }
   }

   public GETFIELD createGetField(String class_name, String name, Type t) {
      return new GETFIELD(this.cp.addFieldref(class_name, name, t.getSignature()));
   }

   public GETSTATIC createGetStatic(String class_name, String name, Type t) {
      return new GETSTATIC(this.cp.addFieldref(class_name, name, t.getSignature()));
   }

   public PUTFIELD createPutField(String class_name, String name, Type t) {
      return new PUTFIELD(this.cp.addFieldref(class_name, name, t.getSignature()));
   }

   public PUTSTATIC createPutStatic(String class_name, String name, Type t) {
      return new PUTSTATIC(this.cp.addFieldref(class_name, name, t.getSignature()));
   }

   public CHECKCAST createCheckCast(ReferenceType t) {
      return t instanceof ArrayType ? new CHECKCAST(this.cp.addArrayClass((ArrayType)t)) : new CHECKCAST(this.cp.addClass((ObjectType)t));
   }

   public NEW createNew(ObjectType t) {
      return new NEW(this.cp.addClass(t));
   }

   public NEW createNew(String s) {
      return this.createNew(new ObjectType(s));
   }

   public AllocationInstruction createNewArray(Type t, short dim) {
      if (dim == 1) {
         if (t instanceof ObjectType) {
            return new ANEWARRAY(this.cp.addClass((ObjectType)t));
         } else {
            return (AllocationInstruction)(t instanceof ArrayType ? new ANEWARRAY(this.cp.addArrayClass((ArrayType)t)) : new NEWARRAY(((BasicType)t).getType()));
         }
      } else {
         ArrayType at;
         if (t instanceof ArrayType) {
            at = (ArrayType)t;
         } else {
            at = new ArrayType(t, dim);
         }

         return new MULTIANEWARRAY(this.cp.addArrayClass(at), dim);
      }
   }

   public static Instruction createNull(Type type) {
      switch (type.getType()) {
         case 4:
         case 5:
         case 8:
         case 9:
         case 10:
            return InstructionConstants.ICONST_0;
         case 6:
            return InstructionConstants.FCONST_0;
         case 7:
            return InstructionConstants.DCONST_0;
         case 11:
            return InstructionConstants.LCONST_0;
         case 12:
            return InstructionConstants.NOP;
         case 13:
         case 14:
            return InstructionConstants.ACONST_NULL;
         default:
            throw new RuntimeException("Invalid type: " + type);
      }
   }

   public static BranchInstruction createBranchInstruction(short opcode, InstructionHandle target) {
      switch (opcode) {
         case 153:
            return new IFEQ(target);
         case 154:
            return new IFNE(target);
         case 155:
            return new IFLT(target);
         case 156:
            return new IFGE(target);
         case 157:
            return new IFGT(target);
         case 158:
            return new IFLE(target);
         case 159:
            return new IF_ICMPEQ(target);
         case 160:
            return new IF_ICMPNE(target);
         case 161:
            return new IF_ICMPLT(target);
         case 162:
            return new IF_ICMPGE(target);
         case 163:
            return new IF_ICMPGT(target);
         case 164:
            return new IF_ICMPLE(target);
         case 165:
            return new IF_ACMPEQ(target);
         case 166:
            return new IF_ACMPNE(target);
         case 167:
            return new GOTO(target);
         case 168:
            return new JSR(target);
         case 169:
         case 170:
         case 171:
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 177:
         case 178:
         case 179:
         case 180:
         case 181:
         case 182:
         case 183:
         case 184:
         case 185:
         case 186:
         case 187:
         case 188:
         case 189:
         case 190:
         case 191:
         case 192:
         case 193:
         case 194:
         case 195:
         case 196:
         case 197:
         default:
            throw new RuntimeException("Invalid opcode: " + opcode);
         case 198:
            return new IFNULL(target);
         case 199:
            return new IFNONNULL(target);
         case 200:
            return new GOTO_W(target);
         case 201:
            return new JSR_W(target);
      }
   }

   public void setClassGen(ClassGen c) {
      this.cg = c;
   }

   public ClassGen getClassGen() {
      return this.cg;
   }

   public void setConstantPool(ConstantPoolGen c) {
      this.cp = c;
   }

   public ConstantPoolGen getConstantPool() {
      return this.cp;
   }

   static {
      append_mos = new MethodObject[]{new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[]{Type.STRING}, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[]{Type.OBJECT}, 1), null, null, new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[]{Type.BOOLEAN}, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[]{Type.CHAR}, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[]{Type.FLOAT}, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[]{Type.DOUBLE}, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[]{Type.INT}, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[]{Type.INT}, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[]{Type.INT}, 1), new MethodObject("java.lang.StringBuffer", "append", Type.STRINGBUFFER, new Type[]{Type.LONG}, 1)};
   }

   private static class MethodObject {
      Type[] arg_types;
      Type result_type;
      String[] arg_names;
      String class_name;
      String name;
      int access;

      MethodObject(String c, String n, Type r, Type[] a, int acc) {
         this.class_name = c;
         this.name = n;
         this.result_type = r;
         this.arg_types = a;
         this.access = acc;
      }
   }
}
