package org.apache.bcel.generic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;

public class MethodGen extends FieldGenOrMethodGen {
   private String class_name;
   private Type[] arg_types;
   private String[] arg_names;
   private int max_locals;
   private int max_stack;
   private InstructionList il;
   private boolean strip_attributes;
   private ArrayList variable_vec;
   private ArrayList line_number_vec;
   private ArrayList exception_vec;
   private ArrayList throws_vec;
   private ArrayList code_attrs_vec;
   private ArrayList observers;

   public MethodGen(int access_flags, Type return_type, Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp) {
      this.variable_vec = new ArrayList();
      this.line_number_vec = new ArrayList();
      this.exception_vec = new ArrayList();
      this.throws_vec = new ArrayList();
      this.code_attrs_vec = new ArrayList();
      this.setAccessFlags(access_flags);
      this.setType(return_type);
      this.setArgumentTypes(arg_types);
      this.setArgumentNames(arg_names);
      this.setName(method_name);
      this.setClassName(class_name);
      this.setInstructionList(il);
      this.setConstantPool(cp);
      if ((access_flags & 1280) == 0) {
         InstructionHandle start = il.getStart();
         InstructionHandle end = il.getEnd();
         if (!this.isStatic() && class_name != null) {
            this.addLocalVariable("this", new ObjectType(class_name), start, end);
         }

         if (arg_types != null) {
            int size = arg_types.length;
            int i;
            if (arg_names != null) {
               if (size != arg_names.length) {
                  throw new ClassGenException("Mismatch in argument array lengths: " + size + " vs. " + arg_names.length);
               }
            } else {
               arg_names = new String[size];

               for(i = 0; i < size; ++i) {
                  arg_names[i] = "arg" + i;
               }

               this.setArgumentNames(arg_names);
            }

            for(i = 0; i < size; ++i) {
               this.addLocalVariable(arg_names[i], arg_types[i], start, end);
            }
         }
      }

   }

   public MethodGen(Method m, String class_name, ConstantPoolGen cp) {
      this(m.getAccessFlags(), Type.getReturnType(m.getSignature()), Type.getArgumentTypes(m.getSignature()), (String[])null, m.getName(), class_name, (m.getAccessFlags() & 1280) == 0 ? new InstructionList(m.getCode().getCode()) : null, cp);
      Attribute[] attributes = m.getAttributes();

      for(int i = 0; i < attributes.length; ++i) {
         Attribute a = attributes[i];
         if (!(a instanceof Code)) {
            if (a instanceof ExceptionTable) {
               String[] names = ((ExceptionTable)a).getExceptionNames();

               for(int j = 0; j < names.length; ++j) {
                  this.addException(names[j]);
               }
            } else {
               this.addAttribute(a);
            }
         } else {
            Code c = (Code)a;
            this.setMaxStack(c.getMaxStack());
            this.setMaxLocals(c.getMaxLocals());
            CodeException[] ces = c.getExceptionTable();
            InstructionHandle end;
            if (ces != null) {
               for(int j = 0; j < ces.length; ++j) {
                  CodeException ce = ces[j];
                  int type = ce.getCatchType();
                  ObjectType c_type = null;
                  if (type > 0) {
                     String cen = m.getConstantPool().getConstantString(type, (byte)7);
                     c_type = new ObjectType(cen);
                  }

                  int end_pc = ce.getEndPC();
                  int length = m.getCode().getCode().length;
                  if (length == end_pc) {
                     end = this.il.getEnd();
                  } else {
                     end = this.il.findHandle(end_pc);
                     end = end.getPrev();
                  }

                  this.addExceptionHandler(this.il.findHandle(ce.getStartPC()), end, this.il.findHandle(ce.getHandlerPC()), c_type);
               }
            }

            Attribute[] c_attributes = c.getAttributes();

            for(int j = 0; j < c_attributes.length; ++j) {
               a = c_attributes[j];
               int k;
               if (a instanceof LineNumberTable) {
                  LineNumber[] ln = ((LineNumberTable)a).getLineNumberTable();

                  for(k = 0; k < ln.length; ++k) {
                     LineNumber l = ln[k];
                     this.addLineNumber(this.il.findHandle(l.getStartPC()), l.getLineNumber());
                  }
               } else if (a instanceof LocalVariableTable) {
                  LocalVariable[] lv = ((LocalVariableTable)a).getLocalVariableTable();

                  for(k = 0; k < lv.length; ++k) {
                     LocalVariable l = lv[k];
                     InstructionHandle start = this.il.findHandle(l.getStartPC());
                     end = this.il.findHandle(l.getStartPC() + l.getLength());
                     if (start == null) {
                        start = this.il.getStart();
                     }

                     if (end == null) {
                        end = this.il.getEnd();
                     }

                     this.addLocalVariable(l.getName(), Type.getType(l.getSignature()), l.getIndex(), start, end);
                  }
               } else {
                  this.addCodeAttribute(a);
               }
            }
         }
      }

   }

   public LocalVariableGen addLocalVariable(String name, Type type, int slot, InstructionHandle start, InstructionHandle end) {
      byte t = type.getType();
      int add = type.getSize();
      if (slot + add > this.max_locals) {
         this.max_locals = slot + add;
      }

      LocalVariableGen l = new LocalVariableGen(slot, name, type, start, end);
      int i;
      if ((i = this.variable_vec.indexOf(l)) >= 0) {
         this.variable_vec.set(i, l);
      } else {
         this.variable_vec.add(l);
      }

      return l;
   }

   public LocalVariableGen addLocalVariable(String name, Type type, InstructionHandle start, InstructionHandle end) {
      return this.addLocalVariable(name, type, this.max_locals, start, end);
   }

   public void removeLocalVariable(LocalVariableGen l) {
      this.variable_vec.remove(l);
   }

   public void removeLocalVariables() {
      this.variable_vec.clear();
   }

   private static final void sort(LocalVariableGen[] vars, int l, int r) {
      int i = l;
      int j = r;
      int m = vars[(l + r) / 2].getIndex();

      do {
         while(vars[i].getIndex() < m) {
            ++i;
         }

         while(m < vars[j].getIndex()) {
            --j;
         }

         if (i <= j) {
            LocalVariableGen h = vars[i];
            vars[i] = vars[j];
            vars[j] = h;
            ++i;
            --j;
         }
      } while(i <= j);

      if (l < j) {
         sort(vars, l, j);
      }

      if (i < r) {
         sort(vars, i, r);
      }

   }

   public LocalVariableGen[] getLocalVariables() {
      int size = this.variable_vec.size();
      LocalVariableGen[] lg = new LocalVariableGen[size];
      this.variable_vec.toArray(lg);

      for(int i = 0; i < size; ++i) {
         if (lg[i].getStart() == null) {
            lg[i].setStart(this.il.getStart());
         }

         if (lg[i].getEnd() == null) {
            lg[i].setEnd(this.il.getEnd());
         }
      }

      if (size > 1) {
         sort(lg, 0, size - 1);
      }

      return lg;
   }

   public LocalVariableTable getLocalVariableTable(ConstantPoolGen cp) {
      LocalVariableGen[] lg = this.getLocalVariables();
      int size = lg.length;
      LocalVariable[] lv = new LocalVariable[size];

      for(int i = 0; i < size; ++i) {
         lv[i] = lg[i].getLocalVariable(cp);
      }

      return new LocalVariableTable(cp.addUtf8("LocalVariableTable"), 2 + lv.length * 10, lv, cp.getConstantPool());
   }

   public LineNumberGen addLineNumber(InstructionHandle ih, int src_line) {
      LineNumberGen l = new LineNumberGen(ih, src_line);
      this.line_number_vec.add(l);
      return l;
   }

   public void removeLineNumber(LineNumberGen l) {
      this.line_number_vec.remove(l);
   }

   public void removeLineNumbers() {
      this.line_number_vec.clear();
   }

   public LineNumberGen[] getLineNumbers() {
      LineNumberGen[] lg = new LineNumberGen[this.line_number_vec.size()];
      this.line_number_vec.toArray(lg);
      return lg;
   }

   public LineNumberTable getLineNumberTable(ConstantPoolGen cp) {
      int size = this.line_number_vec.size();
      LineNumber[] ln = new LineNumber[size];

      try {
         for(int i = 0; i < size; ++i) {
            ln[i] = ((LineNumberGen)this.line_number_vec.get(i)).getLineNumber();
         }
      } catch (ArrayIndexOutOfBoundsException var5) {
      }

      return new LineNumberTable(cp.addUtf8("LineNumberTable"), 2 + ln.length * 4, ln, cp.getConstantPool());
   }

   public CodeExceptionGen addExceptionHandler(InstructionHandle start_pc, InstructionHandle end_pc, InstructionHandle handler_pc, ObjectType catch_type) {
      if (start_pc != null && end_pc != null && handler_pc != null) {
         CodeExceptionGen c = new CodeExceptionGen(start_pc, end_pc, handler_pc, catch_type);
         this.exception_vec.add(c);
         return c;
      } else {
         throw new ClassGenException("Exception handler target is null instruction");
      }
   }

   public void removeExceptionHandler(CodeExceptionGen c) {
      this.exception_vec.remove(c);
   }

   public void removeExceptionHandlers() {
      this.exception_vec.clear();
   }

   public CodeExceptionGen[] getExceptionHandlers() {
      CodeExceptionGen[] cg = new CodeExceptionGen[this.exception_vec.size()];
      this.exception_vec.toArray(cg);
      return cg;
   }

   private CodeException[] getCodeExceptions() {
      int size = this.exception_vec.size();
      CodeException[] c_exc = new CodeException[size];

      try {
         for(int i = 0; i < size; ++i) {
            CodeExceptionGen c = (CodeExceptionGen)this.exception_vec.get(i);
            c_exc[i] = c.getCodeException(super.cp);
         }
      } catch (ArrayIndexOutOfBoundsException var5) {
      }

      return c_exc;
   }

   public void addException(String class_name) {
      this.throws_vec.add(class_name);
   }

   public void removeException(String c) {
      this.throws_vec.remove(c);
   }

   public void removeExceptions() {
      this.throws_vec.clear();
   }

   public String[] getExceptions() {
      String[] e = new String[this.throws_vec.size()];
      this.throws_vec.toArray(e);
      return e;
   }

   private ExceptionTable getExceptionTable(ConstantPoolGen cp) {
      int size = this.throws_vec.size();
      int[] ex = new int[size];

      try {
         for(int i = 0; i < size; ++i) {
            ex[i] = cp.addClass((String)this.throws_vec.get(i));
         }
      } catch (ArrayIndexOutOfBoundsException var5) {
      }

      return new ExceptionTable(cp.addUtf8("Exceptions"), 2 + 2 * size, ex, cp.getConstantPool());
   }

   public void addCodeAttribute(Attribute a) {
      this.code_attrs_vec.add(a);
   }

   public void removeCodeAttribute(Attribute a) {
      this.code_attrs_vec.remove(a);
   }

   public void removeCodeAttributes() {
      this.code_attrs_vec.clear();
   }

   public Attribute[] getCodeAttributes() {
      Attribute[] attributes = new Attribute[this.code_attrs_vec.size()];
      this.code_attrs_vec.toArray(attributes);
      return attributes;
   }

   public Method getMethod() {
      String signature = this.getSignature();
      int name_index = super.cp.addUtf8(super.name);
      int signature_index = super.cp.addUtf8(signature);
      byte[] byte_code = null;
      if (this.il != null) {
         byte_code = this.il.getByteCode();
      }

      LineNumberTable lnt = null;
      LocalVariableTable lvt = null;
      if (this.variable_vec.size() > 0 && !this.strip_attributes) {
         this.addCodeAttribute(lvt = this.getLocalVariableTable(super.cp));
      }

      if (this.line_number_vec.size() > 0 && !this.strip_attributes) {
         this.addCodeAttribute(lnt = this.getLineNumberTable(super.cp));
      }

      Attribute[] code_attrs = this.getCodeAttributes();
      int attrs_len = 0;

      for(int i = 0; i < code_attrs.length; ++i) {
         attrs_len += code_attrs[i].getLength() + 6;
      }

      CodeException[] c_exc = this.getCodeExceptions();
      int exc_len = c_exc.length * 8;
      Code code = null;
      if (this.il != null && !this.isAbstract()) {
         code = new Code(super.cp.addUtf8("Code"), 8 + byte_code.length + 2 + exc_len + 2 + attrs_len, this.max_stack, this.max_locals, byte_code, c_exc, code_attrs, super.cp.getConstantPool());
         this.addAttribute(code);
      }

      ExceptionTable et = null;
      if (this.throws_vec.size() > 0) {
         this.addAttribute(et = this.getExceptionTable(super.cp));
      }

      Method m = new Method(super.access_flags, name_index, signature_index, this.getAttributes(), super.cp.getConstantPool());
      if (lvt != null) {
         this.removeCodeAttribute(lvt);
      }

      if (lnt != null) {
         this.removeCodeAttribute(lnt);
      }

      if (code != null) {
         this.removeAttribute(code);
      }

      if (et != null) {
         this.removeAttribute(et);
      }

      return m;
   }

   public void removeNOPs() {
      InstructionHandle next;
      if (this.il != null) {
         for(InstructionHandle ih = this.il.getStart(); ih != null; ih = next) {
            next = ih.next;
            if (next != null && ih.getInstruction() instanceof NOP) {
               try {
                  this.il.delete(ih);
               } catch (TargetLostException var8) {
                  InstructionHandle[] targets = var8.getTargets();

                  for(int i = 0; i < targets.length; ++i) {
                     InstructionTargeter[] targeters = targets[i].getTargeters();

                     for(int j = 0; j < targeters.length; ++j) {
                        targeters[j].updateTarget(targets[i], next);
                     }
                  }
               }
            }
         }
      }

   }

   public void setMaxLocals(int m) {
      this.max_locals = m;
   }

   public int getMaxLocals() {
      return this.max_locals;
   }

   public void setMaxStack(int m) {
      this.max_stack = m;
   }

   public int getMaxStack() {
      return this.max_stack;
   }

   public String getClassName() {
      return this.class_name;
   }

   public void setClassName(String class_name) {
      this.class_name = class_name;
   }

   public void setReturnType(Type return_type) {
      this.setType(return_type);
   }

   public Type getReturnType() {
      return this.getType();
   }

   public void setArgumentTypes(Type[] arg_types) {
      this.arg_types = arg_types;
   }

   public Type[] getArgumentTypes() {
      return (Type[])this.arg_types.clone();
   }

   public void setArgumentType(int i, Type type) {
      this.arg_types[i] = type;
   }

   public Type getArgumentType(int i) {
      return this.arg_types[i];
   }

   public void setArgumentNames(String[] arg_names) {
      this.arg_names = arg_names;
   }

   public String[] getArgumentNames() {
      return (String[])this.arg_names.clone();
   }

   public void setArgumentName(int i, String name) {
      this.arg_names[i] = name;
   }

   public String getArgumentName(int i) {
      return this.arg_names[i];
   }

   public InstructionList getInstructionList() {
      return this.il;
   }

   public void setInstructionList(InstructionList il) {
      this.il = il;
   }

   public String getSignature() {
      return Type.getMethodSignature(super.type, this.arg_types);
   }

   public void setMaxStack() {
      if (this.il != null) {
         this.max_stack = getMaxStack(super.cp, this.il, this.getExceptionHandlers());
      } else {
         this.max_stack = 0;
      }

   }

   public void setMaxLocals() {
      if (this.il != null) {
         int max = this.isStatic() ? 0 : 1;
         if (this.arg_types != null) {
            for(int i = 0; i < this.arg_types.length; ++i) {
               max += this.arg_types[i].getSize();
            }
         }

         for(InstructionHandle ih = this.il.getStart(); ih != null; ih = ih.getNext()) {
            Instruction ins = ih.getInstruction();
            if (ins instanceof LocalVariableInstruction || ins instanceof RET || ins instanceof IINC) {
               int index = ((IndexedInstruction)ins).getIndex() + ((TypedInstruction)ins).getType(super.cp).getSize();
               if (index > max) {
                  max = index;
               }
            }
         }

         this.max_locals = max;
      } else {
         this.max_locals = 0;
      }

   }

   public void stripAttributes(boolean flag) {
      this.strip_attributes = flag;
   }

   public static int getMaxStack(ConstantPoolGen cp, InstructionList il, CodeExceptionGen[] et) {
      BranchStack branchTargets = new BranchStack();

      for(int i = 0; i < et.length; ++i) {
         InstructionHandle handler_pc = et[i].getHandlerPC();
         if (handler_pc != null) {
            branchTargets.push(handler_pc, 1);
         }
      }

      int stackDepth = 0;
      int maxStackDepth = 0;
      InstructionHandle ih = il.getStart();

      while(ih != null) {
         Instruction instruction = ih.getInstruction();
         short opcode = instruction.getOpcode();
         int delta = instruction.produceStack(cp) - instruction.consumeStack(cp);
         stackDepth += delta;
         if (stackDepth > maxStackDepth) {
            maxStackDepth = stackDepth;
         }

         if (!(instruction instanceof BranchInstruction)) {
            if (opcode == 191 || opcode == 169 || opcode >= 172 && opcode <= 177) {
               ih = null;
            }
         } else {
            BranchInstruction branch = (BranchInstruction)instruction;
            if (instruction instanceof Select) {
               Select select = (Select)branch;
               InstructionHandle[] targets = select.getTargets();

               for(int i = 0; i < targets.length; ++i) {
                  branchTargets.push(targets[i], stackDepth);
               }

               ih = null;
            } else if (!(branch instanceof IfInstruction)) {
               if (opcode == 168 || opcode == 201) {
                  branchTargets.push(ih.getNext(), stackDepth - 1);
               }

               ih = null;
            }

            branchTargets.push(branch.getTarget(), stackDepth);
         }

         if (ih != null) {
            ih = ih.getNext();
         }

         if (ih == null) {
            BranchTarget bt = branchTargets.pop();
            if (bt != null) {
               ih = bt.target;
               stackDepth = bt.stackDepth;
            }
         }
      }

      return maxStackDepth;
   }

   public void addObserver(MethodObserver o) {
      if (this.observers == null) {
         this.observers = new ArrayList();
      }

      this.observers.add(o);
   }

   public void removeObserver(MethodObserver o) {
      if (this.observers != null) {
         this.observers.remove(o);
      }

   }

   public void update() {
      if (this.observers != null) {
         Iterator e = this.observers.iterator();

         while(e.hasNext()) {
            ((MethodObserver)e.next()).notify(this);
         }
      }

   }

   public final String toString() {
      String access = Utility.accessToString(super.access_flags);
      String signature = Type.getMethodSignature(super.type, this.arg_types);
      signature = Utility.methodSignatureToString(signature, super.name, access, true, this.getLocalVariableTable(super.cp));
      StringBuffer buf = new StringBuffer(signature);
      if (this.throws_vec.size() > 0) {
         Iterator e = this.throws_vec.iterator();

         while(e.hasNext()) {
            buf.append("\n\t\tthrows " + e.next());
         }
      }

      return buf.toString();
   }

   public MethodGen copy(String class_name, ConstantPoolGen cp) {
      Method m = ((MethodGen)this.clone()).getMethod();
      MethodGen mg = new MethodGen(m, class_name, super.cp);
      if (super.cp != cp) {
         mg.setConstantPool(cp);
         mg.getInstructionList().replaceConstantPool(super.cp, cp);
      }

      return mg;
   }

   static final class BranchStack {
      Stack branchTargets = new Stack();
      Hashtable visitedTargets = new Hashtable();

      public void push(InstructionHandle target, int stackDepth) {
         if (!this.visited(target)) {
            this.branchTargets.push(this.visit(target, stackDepth));
         }
      }

      public BranchTarget pop() {
         if (!this.branchTargets.empty()) {
            BranchTarget bt = (BranchTarget)this.branchTargets.pop();
            return bt;
         } else {
            return null;
         }
      }

      private final BranchTarget visit(InstructionHandle target, int stackDepth) {
         BranchTarget bt = new BranchTarget(target, stackDepth);
         this.visitedTargets.put(target, bt);
         return bt;
      }

      private final boolean visited(InstructionHandle target) {
         return this.visitedTargets.get(target) != null;
      }
   }

   static final class BranchTarget {
      InstructionHandle target;
      int stackDepth;

      BranchTarget(InstructionHandle target, int stackDepth) {
         this.target = target;
         this.stackDepth = stackDepth;
      }
   }
}
