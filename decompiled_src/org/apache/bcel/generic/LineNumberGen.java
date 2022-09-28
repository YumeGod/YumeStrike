package org.apache.bcel.generic;

import org.apache.bcel.classfile.LineNumber;

public class LineNumberGen implements InstructionTargeter, Cloneable {
   private InstructionHandle ih;
   private int src_line;

   public LineNumberGen(InstructionHandle ih, int src_line) {
      this.setInstruction(ih);
      this.setSourceLine(src_line);
   }

   public boolean containsTarget(InstructionHandle ih) {
      return this.ih == ih;
   }

   public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) {
      if (old_ih != this.ih) {
         throw new ClassGenException("Not targeting " + old_ih + ", but " + this.ih + "}");
      } else {
         this.setInstruction(new_ih);
      }
   }

   public LineNumber getLineNumber() {
      return new LineNumber(this.ih.getPosition(), this.src_line);
   }

   public void setInstruction(InstructionHandle ih) {
      BranchInstruction.notifyTarget(this.ih, ih, this);
      this.ih = ih;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         System.err.println(var2);
         return null;
      }
   }

   public InstructionHandle getInstruction() {
      return this.ih;
   }

   public void setSourceLine(int src_line) {
      this.src_line = src_line;
   }

   public int getSourceLine() {
      return this.src_line;
   }
}
