package org.apache.bcel.generic;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantObject;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.Utility;

public class FieldGen extends FieldGenOrMethodGen {
   private Object value;
   private ArrayList observers;

   public FieldGen(int access_flags, Type type, String name, ConstantPoolGen cp) {
      this.value = null;
      this.setAccessFlags(access_flags);
      this.setType(type);
      this.setName(name);
      this.setConstantPool(cp);
   }

   public FieldGen(Field field, ConstantPoolGen cp) {
      this(field.getAccessFlags(), Type.getType(field.getSignature()), field.getName(), cp);
      Attribute[] attrs = field.getAttributes();

      for(int i = 0; i < attrs.length; ++i) {
         if (attrs[i] instanceof ConstantValue) {
            this.setValue(((ConstantValue)attrs[i]).getConstantValueIndex());
         } else {
            this.addAttribute(attrs[i]);
         }
      }

   }

   private void setValue(int index) {
      ConstantPool cp = super.cp.getConstantPool();
      Constant c = cp.getConstant(index);
      this.value = ((ConstantObject)c).getConstantValue(cp);
   }

   public void setInitValue(String str) {
      this.checkType(new ObjectType("java.lang.String"));
      if (str != null) {
         this.value = str;
      }

   }

   public void setInitValue(long l) {
      this.checkType(Type.LONG);
      if (l != 0L) {
         this.value = new Long(l);
      }

   }

   public void setInitValue(int i) {
      this.checkType(Type.INT);
      if (i != 0) {
         this.value = new Integer(i);
      }

   }

   public void setInitValue(short s) {
      this.checkType(Type.SHORT);
      if (s != 0) {
         this.value = new Integer(s);
      }

   }

   public void setInitValue(char c) {
      this.checkType(Type.CHAR);
      if (c != 0) {
         this.value = new Integer(c);
      }

   }

   public void setInitValue(byte b) {
      this.checkType(Type.BYTE);
      if (b != 0) {
         this.value = new Integer(b);
      }

   }

   public void setInitValue(boolean b) {
      this.checkType(Type.BOOLEAN);
      if (b) {
         this.value = new Integer(1);
      }

   }

   public void setInitValue(float f) {
      this.checkType(Type.FLOAT);
      if ((double)f != 0.0) {
         this.value = new Float(f);
      }

   }

   public void setInitValue(double d) {
      this.checkType(Type.DOUBLE);
      if (d != 0.0) {
         this.value = new Double(d);
      }

   }

   public void cancelInitValue() {
      this.value = null;
   }

   private void checkType(Type atype) {
      if (super.type == null) {
         throw new ClassGenException("You haven't defined the type of the field yet");
      } else if (!this.isFinal()) {
         throw new ClassGenException("Only final fields may have an initial value!");
      } else if (!super.type.equals(atype)) {
         throw new ClassGenException("Types are not compatible: " + super.type + " vs. " + atype);
      }
   }

   public Field getField() {
      String signature = this.getSignature();
      int name_index = super.cp.addUtf8(super.name);
      int signature_index = super.cp.addUtf8(signature);
      if (this.value != null) {
         this.checkType(super.type);
         int index = this.addConstant();
         this.addAttribute(new ConstantValue(super.cp.addUtf8("ConstantValue"), 2, index, super.cp.getConstantPool()));
      }

      return new Field(super.access_flags, name_index, signature_index, this.getAttributes(), super.cp.getConstantPool());
   }

   private int addConstant() {
      switch (super.type.getType()) {
         case 4:
         case 5:
         case 8:
         case 9:
         case 10:
            return super.cp.addInteger((Integer)this.value);
         case 6:
            return super.cp.addFloat((Float)this.value);
         case 7:
            return super.cp.addDouble((Double)this.value);
         case 11:
            return super.cp.addLong((Long)this.value);
         case 12:
         case 13:
         default:
            throw new RuntimeException("Oops: Unhandled : " + super.type.getType());
         case 14:
            return super.cp.addString((String)this.value);
      }
   }

   public String getSignature() {
      return super.type.getSignature();
   }

   public void addObserver(FieldObserver o) {
      if (this.observers == null) {
         this.observers = new ArrayList();
      }

      this.observers.add(o);
   }

   public void removeObserver(FieldObserver o) {
      if (this.observers != null) {
         this.observers.remove(o);
      }

   }

   public void update() {
      if (this.observers != null) {
         Iterator e = this.observers.iterator();

         while(e.hasNext()) {
            ((FieldObserver)e.next()).notify(this);
         }
      }

   }

   public String getInitValue() {
      return this.value != null ? this.value.toString() : null;
   }

   public final String toString() {
      String access = Utility.accessToString(super.access_flags);
      access = access.equals("") ? "" : access + " ";
      String signature = super.type.toString();
      String name = this.getName();
      StringBuffer buf = new StringBuffer(access + signature + " " + name);
      String value = this.getInitValue();
      if (value != null) {
         buf.append(" = " + value);
      }

      return buf.toString();
   }

   public FieldGen copy(ConstantPoolGen cp) {
      FieldGen fg = (FieldGen)this.clone();
      fg.setConstantPool(cp);
      return fg;
   }
}
