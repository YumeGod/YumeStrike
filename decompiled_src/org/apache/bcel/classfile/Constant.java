package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;

public abstract class Constant implements Cloneable, Node {
   protected byte tag;

   Constant(byte tag) {
      this.tag = tag;
   }

   public abstract void accept(Visitor var1);

   public abstract void dump(DataOutputStream var1) throws IOException;

   public final byte getTag() {
      return this.tag;
   }

   public String toString() {
      return Constants.CONSTANT_NAMES[this.tag] + "[" + this.tag + "]";
   }

   public Constant copy() {
      try {
         return (Constant)super.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   static final Constant readConstant(DataInputStream file) throws IOException, ClassFormatError {
      byte b = file.readByte();
      switch (b) {
         case 1:
            return new ConstantUtf8(file);
         case 2:
         default:
            throw new ClassFormatError("Invalid byte tag in constant pool: " + b);
         case 3:
            return new ConstantInteger(file);
         case 4:
            return new ConstantFloat(file);
         case 5:
            return new ConstantLong(file);
         case 6:
            return new ConstantDouble(file);
         case 7:
            return new ConstantClass(file);
         case 8:
            return new ConstantString(file);
         case 9:
            return new ConstantFieldref(file);
         case 10:
            return new ConstantMethodref(file);
         case 11:
            return new ConstantInterfaceMethodref(file);
         case 12:
            return new ConstantNameAndType(file);
      }
   }
}
