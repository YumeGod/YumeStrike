package org.apache.bcel.generic;

import org.apache.bcel.classfile.LocalVariable;

public class LocalVariableGen implements InstructionTargeter, NamedAndTyped, Cloneable {
   private int index;
   private String name;
   private Type type;
   private InstructionHandle start;
   private InstructionHandle end;

   public LocalVariableGen(int index, String name, Type type, InstructionHandle start, InstructionHandle end) {
      if (index >= 0 && index <= 65535) {
         this.name = name;
         this.type = type;
         this.index = index;
         this.setStart(start);
         this.setEnd(end);
      } else {
         throw new ClassGenException("Invalid index index: " + index);
      }
   }

   public LocalVariable getLocalVariable(ConstantPoolGen cp) {
      int start_pc = this.start.getPosition();
      int length = this.end.getPosition() - start_pc;
      if (length > 0) {
         length += this.end.getInstruction().getLength();
      }

      int name_index = cp.addUtf8(this.name);
      int signature_index = cp.addUtf8(this.type.getSignature());
      return new LocalVariable(start_pc, length, name_index, signature_index, this.index, cp.getConstantPool());
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public int getIndex() {
      return this.index;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setType(Type type) {
      this.type = type;
   }

   public Type getType() {
      return this.type;
   }

   public InstructionHandle getStart() {
      return this.start;
   }

   public InstructionHandle getEnd() {
      return this.end;
   }

   public void setStart(InstructionHandle start) {
      BranchInstruction.notifyTarget(this.start, start, this);
      this.start = start;
   }

   public void setEnd(InstructionHandle end) {
      BranchInstruction.notifyTarget(this.end, end, this);
      this.end = end;
   }

   public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) {
      boolean targeted = false;
      if (this.start == old_ih) {
         targeted = true;
         this.setStart(new_ih);
      }

      if (this.end == old_ih) {
         targeted = true;
         this.setEnd(new_ih);
      }

      if (!targeted) {
         throw new ClassGenException("Not targeting " + old_ih + ", but {" + this.start + ", " + this.end + "}");
      }
   }

   public boolean containsTarget(InstructionHandle ih) {
      return this.start == ih || this.end == ih;
   }

   public boolean equals(Object o) {
      if (!(o instanceof LocalVariableGen)) {
         return false;
      } else {
         LocalVariableGen l = (LocalVariableGen)o;
         return l.index == this.index && l.start == this.start && l.end == this.end;
      }
   }

   public String toString() {
      return "LocalVariableGen(" + this.name + ", " + this.type + ", " + this.start + ", " + this.end + ")";
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         System.err.println(var2);
         return null;
      }
   }
}
