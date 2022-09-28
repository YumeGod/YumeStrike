package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class FieldOrMethod extends AccessFlags implements Cloneable, Node {
   protected int name_index;
   protected int signature_index;
   protected int attributes_count;
   protected Attribute[] attributes;
   protected ConstantPool constant_pool;

   FieldOrMethod() {
   }

   protected FieldOrMethod(FieldOrMethod c) {
      this(c.getAccessFlags(), c.getNameIndex(), c.getSignatureIndex(), c.getAttributes(), c.getConstantPool());
   }

   protected FieldOrMethod(DataInputStream file, ConstantPool constant_pool) throws IOException, ClassFormatError {
      this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), (Attribute[])null, constant_pool);
      this.attributes_count = file.readUnsignedShort();
      this.attributes = new Attribute[this.attributes_count];

      for(int i = 0; i < this.attributes_count; ++i) {
         this.attributes[i] = Attribute.readAttribute(file, constant_pool);
      }

   }

   protected FieldOrMethod(int access_flags, int name_index, int signature_index, Attribute[] attributes, ConstantPool constant_pool) {
      super.access_flags = access_flags;
      this.name_index = name_index;
      this.signature_index = signature_index;
      this.constant_pool = constant_pool;
      this.setAttributes(attributes);
   }

   public final void dump(DataOutputStream file) throws IOException {
      file.writeShort(super.access_flags);
      file.writeShort(this.name_index);
      file.writeShort(this.signature_index);
      file.writeShort(this.attributes_count);

      for(int i = 0; i < this.attributes_count; ++i) {
         this.attributes[i].dump(file);
      }

   }

   public final Attribute[] getAttributes() {
      return this.attributes;
   }

   public final void setAttributes(Attribute[] attributes) {
      this.attributes = attributes;
      this.attributes_count = attributes == null ? 0 : attributes.length;
   }

   public final ConstantPool getConstantPool() {
      return this.constant_pool;
   }

   public final void setConstantPool(ConstantPool constant_pool) {
      this.constant_pool = constant_pool;
   }

   public final int getNameIndex() {
      return this.name_index;
   }

   public final void setNameIndex(int name_index) {
      this.name_index = name_index;
   }

   public final int getSignatureIndex() {
      return this.signature_index;
   }

   public final void setSignatureIndex(int signature_index) {
      this.signature_index = signature_index;
   }

   public final String getName() {
      ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.name_index, (byte)1);
      return c.getBytes();
   }

   public final String getSignature() {
      ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.signature_index, (byte)1);
      return c.getBytes();
   }

   protected FieldOrMethod copy_(ConstantPool constant_pool) {
      FieldOrMethod c = null;

      try {
         c = (FieldOrMethod)this.clone();
      } catch (CloneNotSupportedException var4) {
      }

      c.constant_pool = constant_pool;
      c.attributes = new Attribute[this.attributes_count];

      for(int i = 0; i < this.attributes_count; ++i) {
         c.attributes[i] = this.attributes[i].copy(constant_pool);
      }

      return c;
   }

   public abstract void accept(Visitor var1);
}
