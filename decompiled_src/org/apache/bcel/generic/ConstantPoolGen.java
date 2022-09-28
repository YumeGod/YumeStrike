package org.apache.bcel.generic;

import java.util.HashMap;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantDouble;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantInterfaceMethodref;
import org.apache.bcel.classfile.ConstantLong;
import org.apache.bcel.classfile.ConstantMethodref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;

public class ConstantPoolGen {
   protected int size;
   protected Constant[] constants;
   protected int index;
   private static final String METHODREF_DELIM = ":";
   private static final String IMETHODREF_DELIM = "#";
   private static final String FIELDREF_DELIM = "&";
   private static final String NAT_DELIM = "%";
   private HashMap string_table;
   private HashMap class_table;
   private HashMap utf8_table;
   private HashMap n_a_t_table;
   private HashMap cp_table;

   public ConstantPoolGen(Constant[] cs) {
      this.size = 1024;
      this.constants = new Constant[this.size];
      this.index = 1;
      this.string_table = new HashMap();
      this.class_table = new HashMap();
      this.utf8_table = new HashMap();
      this.n_a_t_table = new HashMap();
      this.cp_table = new HashMap();
      if (cs.length > this.size) {
         this.size = cs.length;
         this.constants = new Constant[this.size];
      }

      System.arraycopy(cs, 0, this.constants, 0, cs.length);
      if (cs.length > 0) {
         this.index = cs.length;
      }

      for(int i = 1; i < this.index; ++i) {
         Constant c = this.constants[i];
         ConstantUtf8 u8;
         if (c instanceof ConstantString) {
            ConstantString s = (ConstantString)c;
            u8 = (ConstantUtf8)this.constants[s.getStringIndex()];
            this.string_table.put(u8.getBytes(), new Index(i));
         } else if (c instanceof ConstantClass) {
            ConstantClass s = (ConstantClass)c;
            u8 = (ConstantUtf8)this.constants[s.getNameIndex()];
            this.class_table.put(u8.getBytes(), new Index(i));
         } else if (c instanceof ConstantNameAndType) {
            ConstantNameAndType n = (ConstantNameAndType)c;
            u8 = (ConstantUtf8)this.constants[n.getNameIndex()];
            ConstantUtf8 u8_2 = (ConstantUtf8)this.constants[n.getSignatureIndex()];
            this.n_a_t_table.put(u8.getBytes() + "%" + u8_2.getBytes(), new Index(i));
         } else if (c instanceof ConstantUtf8) {
            ConstantUtf8 u = (ConstantUtf8)c;
            this.utf8_table.put(u.getBytes(), new Index(i));
         } else if (c instanceof ConstantCP) {
            ConstantCP m = (ConstantCP)c;
            ConstantClass clazz = (ConstantClass)this.constants[m.getClassIndex()];
            ConstantNameAndType n = (ConstantNameAndType)this.constants[m.getNameAndTypeIndex()];
            ConstantUtf8 u8 = (ConstantUtf8)this.constants[clazz.getNameIndex()];
            String class_name = u8.getBytes().replace('/', '.');
            u8 = (ConstantUtf8)this.constants[n.getNameIndex()];
            String method_name = u8.getBytes();
            u8 = (ConstantUtf8)this.constants[n.getSignatureIndex()];
            String signature = u8.getBytes();
            String delim = ":";
            if (c instanceof ConstantInterfaceMethodref) {
               delim = "#";
            } else if (c instanceof ConstantFieldref) {
               delim = "&";
            }

            this.cp_table.put(class_name + delim + method_name + delim + signature, new Index(i));
         }
      }

   }

   public ConstantPoolGen(ConstantPool cp) {
      this(cp.getConstantPool());
   }

   public ConstantPoolGen() {
      this.size = 1024;
      this.constants = new Constant[this.size];
      this.index = 1;
      this.string_table = new HashMap();
      this.class_table = new HashMap();
      this.utf8_table = new HashMap();
      this.n_a_t_table = new HashMap();
      this.cp_table = new HashMap();
   }

   protected void adjustSize() {
      if (this.index + 3 >= this.size) {
         Constant[] cs = this.constants;
         this.size *= 2;
         this.constants = new Constant[this.size];
         System.arraycopy(cs, 0, this.constants, 0, this.index);
      }

   }

   public int lookupString(String str) {
      Index index = (Index)this.string_table.get(str);
      return index != null ? index.index : -1;
   }

   public int addString(String str) {
      int ret;
      if ((ret = this.lookupString(str)) != -1) {
         return ret;
      } else {
         this.adjustSize();
         ConstantUtf8 u8 = new ConstantUtf8(str);
         ConstantString s = new ConstantString(this.index);
         this.constants[this.index++] = u8;
         ret = this.index;
         this.constants[this.index++] = s;
         this.string_table.put(str, new Index(ret));
         return ret;
      }
   }

   public int lookupClass(String str) {
      Index index = (Index)this.class_table.get(str.replace('.', '/'));
      return index != null ? index.index : -1;
   }

   private int addClass_(String clazz) {
      int ret;
      if ((ret = this.lookupClass(clazz)) != -1) {
         return ret;
      } else {
         this.adjustSize();
         ConstantClass c = new ConstantClass(this.addUtf8(clazz));
         ret = this.index;
         this.constants[this.index++] = c;
         this.class_table.put(clazz, new Index(ret));
         return ret;
      }
   }

   public int addClass(String str) {
      return this.addClass_(str.replace('.', '/'));
   }

   public int addClass(ObjectType type) {
      return this.addClass(type.getClassName());
   }

   public int addArrayClass(ArrayType type) {
      return this.addClass_(type.getSignature());
   }

   public int lookupInteger(int n) {
      for(int i = 1; i < this.index; ++i) {
         if (this.constants[i] instanceof ConstantInteger) {
            ConstantInteger c = (ConstantInteger)this.constants[i];
            if (c.getBytes() == n) {
               return i;
            }
         }
      }

      return -1;
   }

   public int addInteger(int n) {
      int ret;
      if ((ret = this.lookupInteger(n)) != -1) {
         return ret;
      } else {
         this.adjustSize();
         ret = this.index;
         this.constants[this.index++] = new ConstantInteger(n);
         return ret;
      }
   }

   public int lookupFloat(float n) {
      for(int i = 1; i < this.index; ++i) {
         if (this.constants[i] instanceof ConstantFloat) {
            ConstantFloat c = (ConstantFloat)this.constants[i];
            if (c.getBytes() == n) {
               return i;
            }
         }
      }

      return -1;
   }

   public int addFloat(float n) {
      int ret;
      if ((ret = this.lookupFloat(n)) != -1) {
         return ret;
      } else {
         this.adjustSize();
         ret = this.index;
         this.constants[this.index++] = new ConstantFloat(n);
         return ret;
      }
   }

   public int lookupUtf8(String n) {
      Index index = (Index)this.utf8_table.get(n);
      return index != null ? index.index : -1;
   }

   public int addUtf8(String n) {
      int ret;
      if ((ret = this.lookupUtf8(n)) != -1) {
         return ret;
      } else {
         this.adjustSize();
         ret = this.index;
         this.constants[this.index++] = new ConstantUtf8(n);
         this.utf8_table.put(n, new Index(ret));
         return ret;
      }
   }

   public int lookupLong(long n) {
      for(int i = 1; i < this.index; ++i) {
         if (this.constants[i] instanceof ConstantLong) {
            ConstantLong c = (ConstantLong)this.constants[i];
            if (c.getBytes() == n) {
               return i;
            }
         }
      }

      return -1;
   }

   public int addLong(long n) {
      int ret;
      if ((ret = this.lookupLong(n)) != -1) {
         return ret;
      } else {
         this.adjustSize();
         ret = this.index;
         this.constants[this.index] = new ConstantLong(n);
         this.index += 2;
         return ret;
      }
   }

   public int lookupDouble(double n) {
      for(int i = 1; i < this.index; ++i) {
         if (this.constants[i] instanceof ConstantDouble) {
            ConstantDouble c = (ConstantDouble)this.constants[i];
            if (c.getBytes() == n) {
               return i;
            }
         }
      }

      return -1;
   }

   public int addDouble(double n) {
      int ret;
      if ((ret = this.lookupDouble(n)) != -1) {
         return ret;
      } else {
         this.adjustSize();
         ret = this.index;
         this.constants[this.index] = new ConstantDouble(n);
         this.index += 2;
         return ret;
      }
   }

   public int lookupNameAndType(String name, String signature) {
      Index index = (Index)this.n_a_t_table.get(name + "%" + signature);
      return index != null ? index.index : -1;
   }

   public int addNameAndType(String name, String signature) {
      int ret;
      if ((ret = this.lookupNameAndType(name, signature)) != -1) {
         return ret;
      } else {
         this.adjustSize();
         int name_index = this.addUtf8(name);
         int signature_index = this.addUtf8(signature);
         ret = this.index;
         this.constants[this.index++] = new ConstantNameAndType(name_index, signature_index);
         this.n_a_t_table.put(name + "%" + signature, new Index(ret));
         return ret;
      }
   }

   public int lookupMethodref(String class_name, String method_name, String signature) {
      Index index = (Index)this.cp_table.get(class_name + ":" + method_name + ":" + signature);
      return index != null ? index.index : -1;
   }

   public int lookupMethodref(MethodGen method) {
      return this.lookupMethodref(method.getClassName(), method.getName(), method.getSignature());
   }

   public int addMethodref(String class_name, String method_name, String signature) {
      int ret;
      if ((ret = this.lookupMethodref(class_name, method_name, signature)) != -1) {
         return ret;
      } else {
         this.adjustSize();
         int name_and_type_index = this.addNameAndType(method_name, signature);
         int class_index = this.addClass(class_name);
         ret = this.index;
         this.constants[this.index++] = new ConstantMethodref(class_index, name_and_type_index);
         this.cp_table.put(class_name + ":" + method_name + ":" + signature, new Index(ret));
         return ret;
      }
   }

   public int addMethodref(MethodGen method) {
      return this.addMethodref(method.getClassName(), method.getName(), method.getSignature());
   }

   public int lookupInterfaceMethodref(String class_name, String method_name, String signature) {
      Index index = (Index)this.cp_table.get(class_name + "#" + method_name + "#" + signature);
      return index != null ? index.index : -1;
   }

   public int lookupInterfaceMethodref(MethodGen method) {
      return this.lookupInterfaceMethodref(method.getClassName(), method.getName(), method.getSignature());
   }

   public int addInterfaceMethodref(String class_name, String method_name, String signature) {
      int ret;
      if ((ret = this.lookupInterfaceMethodref(class_name, method_name, signature)) != -1) {
         return ret;
      } else {
         this.adjustSize();
         int class_index = this.addClass(class_name);
         int name_and_type_index = this.addNameAndType(method_name, signature);
         ret = this.index;
         this.constants[this.index++] = new ConstantInterfaceMethodref(class_index, name_and_type_index);
         this.cp_table.put(class_name + "#" + method_name + "#" + signature, new Index(ret));
         return ret;
      }
   }

   public int addInterfaceMethodref(MethodGen method) {
      return this.addInterfaceMethodref(method.getClassName(), method.getName(), method.getSignature());
   }

   public int lookupFieldref(String class_name, String field_name, String signature) {
      Index index = (Index)this.cp_table.get(class_name + "&" + field_name + "&" + signature);
      return index != null ? index.index : -1;
   }

   public int addFieldref(String class_name, String field_name, String signature) {
      int ret;
      if ((ret = this.lookupFieldref(class_name, field_name, signature)) != -1) {
         return ret;
      } else {
         this.adjustSize();
         int class_index = this.addClass(class_name);
         int name_and_type_index = this.addNameAndType(field_name, signature);
         ret = this.index;
         this.constants[this.index++] = new ConstantFieldref(class_index, name_and_type_index);
         this.cp_table.put(class_name + "&" + field_name + "&" + signature, new Index(ret));
         return ret;
      }
   }

   public Constant getConstant(int i) {
      return this.constants[i];
   }

   public void setConstant(int i, Constant c) {
      this.constants[i] = c;
   }

   public ConstantPool getConstantPool() {
      return new ConstantPool(this.constants);
   }

   public int getSize() {
      return this.index;
   }

   public ConstantPool getFinalConstantPool() {
      Constant[] cs = new Constant[this.index];
      System.arraycopy(this.constants, 0, cs, 0, this.index);
      return new ConstantPool(cs);
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();

      for(int i = 1; i < this.index; ++i) {
         buf.append(i + ")" + this.constants[i] + "\n");
      }

      return buf.toString();
   }

   public int addConstant(Constant c, ConstantPoolGen cp) {
      Constant[] constants = cp.getConstantPool().getConstantPool();
      ConstantUtf8 u8;
      switch (c.getTag()) {
         case 1:
            return this.addUtf8(((ConstantUtf8)c).getBytes());
         case 2:
         default:
            throw new RuntimeException("Unknown constant type " + c);
         case 3:
            return this.addInteger(((ConstantInteger)c).getBytes());
         case 4:
            return this.addFloat(((ConstantFloat)c).getBytes());
         case 5:
            return this.addLong(((ConstantLong)c).getBytes());
         case 6:
            return this.addDouble(((ConstantDouble)c).getBytes());
         case 7:
            ConstantClass s = (ConstantClass)c;
            u8 = (ConstantUtf8)constants[s.getNameIndex()];
            return this.addClass(u8.getBytes());
         case 8:
            ConstantString s = (ConstantString)c;
            u8 = (ConstantUtf8)constants[s.getStringIndex()];
            return this.addString(u8.getBytes());
         case 9:
         case 10:
         case 11:
            ConstantCP m = (ConstantCP)c;
            ConstantClass clazz = (ConstantClass)constants[m.getClassIndex()];
            ConstantNameAndType n = (ConstantNameAndType)constants[m.getNameAndTypeIndex()];
            ConstantUtf8 u8 = (ConstantUtf8)constants[clazz.getNameIndex()];
            String class_name = u8.getBytes().replace('/', '.');
            u8 = (ConstantUtf8)constants[n.getNameIndex()];
            String name = u8.getBytes();
            u8 = (ConstantUtf8)constants[n.getSignatureIndex()];
            String signature = u8.getBytes();
            switch (c.getTag()) {
               case 9:
                  return this.addFieldref(class_name, name, signature);
               case 10:
                  return this.addMethodref(class_name, name, signature);
               case 11:
                  return this.addInterfaceMethodref(class_name, name, signature);
               default:
                  throw new RuntimeException("Unknown constant type " + c);
            }
         case 12:
            ConstantNameAndType n = (ConstantNameAndType)c;
            u8 = (ConstantUtf8)constants[n.getNameIndex()];
            ConstantUtf8 u8_2 = (ConstantUtf8)constants[n.getSignatureIndex()];
            return this.addNameAndType(u8.getBytes(), u8_2.getBytes());
      }
   }

   private static class Index {
      int index;

      Index(int i) {
         this.index = i;
      }
   }
}
