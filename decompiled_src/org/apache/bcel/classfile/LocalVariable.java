package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;

public final class LocalVariable implements Constants, Cloneable, Node {
   private int start_pc;
   private int length;
   private int name_index;
   private int signature_index;
   private int index;
   private ConstantPool constant_pool;

   public LocalVariable(LocalVariable c) {
      this(c.getStartPC(), c.getLength(), c.getNameIndex(), c.getSignatureIndex(), c.getIndex(), c.getConstantPool());
   }

   LocalVariable(DataInputStream file, ConstantPool constant_pool) throws IOException {
      this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), constant_pool);
   }

   public LocalVariable(int start_pc, int length, int name_index, int signature_index, int index, ConstantPool constant_pool) {
      this.start_pc = start_pc;
      this.length = length;
      this.name_index = name_index;
      this.signature_index = signature_index;
      this.index = index;
      this.constant_pool = constant_pool;
   }

   public void accept(Visitor v) {
      v.visitLocalVariable(this);
   }

   public final void dump(DataOutputStream file) throws IOException {
      file.writeShort(this.start_pc);
      file.writeShort(this.length);
      file.writeShort(this.name_index);
      file.writeShort(this.signature_index);
      file.writeShort(this.index);
   }

   public final ConstantPool getConstantPool() {
      return this.constant_pool;
   }

   public final int getLength() {
      return this.length;
   }

   public final String getName() {
      ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.name_index, (byte)1);
      return c.getBytes();
   }

   public final int getNameIndex() {
      return this.name_index;
   }

   public final String getSignature() {
      ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.signature_index, (byte)1);
      return c.getBytes();
   }

   public final int getSignatureIndex() {
      return this.signature_index;
   }

   public final int getIndex() {
      return this.index;
   }

   public final int getStartPC() {
      return this.start_pc;
   }

   public final void setConstantPool(ConstantPool constant_pool) {
      this.constant_pool = constant_pool;
   }

   public final void setLength(int length) {
      this.length = length;
   }

   public final void setNameIndex(int name_index) {
      this.name_index = name_index;
   }

   public final void setSignatureIndex(int signature_index) {
      this.signature_index = signature_index;
   }

   public final void setIndex(int index) {
      this.index = index;
   }

   public final void setStartPC(int start_pc) {
      this.start_pc = start_pc;
   }

   public final String toString() {
      String name = this.getName();
      String signature = Utility.signatureToString(this.getSignature());
      return "LocalVariable(start_pc = " + this.start_pc + ", length = " + this.length + ", index = " + this.index + ":" + signature + " " + name + ")";
   }

   public LocalVariable copy() {
      try {
         return (LocalVariable)this.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }
}
