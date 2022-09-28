package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class LineNumber implements Cloneable, Node {
   private int start_pc;
   private int line_number;

   public LineNumber(LineNumber c) {
      this(c.getStartPC(), c.getLineNumber());
   }

   LineNumber(DataInputStream file) throws IOException {
      this(file.readUnsignedShort(), file.readUnsignedShort());
   }

   public LineNumber(int start_pc, int line_number) {
      this.start_pc = start_pc;
      this.line_number = line_number;
   }

   public void accept(Visitor v) {
      v.visitLineNumber(this);
   }

   public final void dump(DataOutputStream file) throws IOException {
      file.writeShort(this.start_pc);
      file.writeShort(this.line_number);
   }

   public final int getLineNumber() {
      return this.line_number;
   }

   public final int getStartPC() {
      return this.start_pc;
   }

   public final void setLineNumber(int line_number) {
      this.line_number = line_number;
   }

   public final void setStartPC(int start_pc) {
      this.start_pc = start_pc;
   }

   public final String toString() {
      return "LineNumber(" + this.start_pc + ", " + this.line_number + ")";
   }

   public LineNumber copy() {
      try {
         return (LineNumber)this.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }
}
