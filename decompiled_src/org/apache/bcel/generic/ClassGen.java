package org.apache.bcel.generic;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.SourceFile;

public class ClassGen extends AccessFlags implements Cloneable {
   private String class_name;
   private String super_class_name;
   private String file_name;
   private int class_name_index = -1;
   private int superclass_name_index = -1;
   private int major = 45;
   private int minor = 3;
   private ConstantPoolGen cp;
   private ArrayList field_vec = new ArrayList();
   private ArrayList method_vec = new ArrayList();
   private ArrayList attribute_vec = new ArrayList();
   private ArrayList interface_vec = new ArrayList();
   private ArrayList observers;

   public ClassGen(String class_name, String super_class_name, String file_name, int access_flags, String[] interfaces) {
      this.class_name = class_name;
      this.super_class_name = super_class_name;
      this.file_name = file_name;
      super.access_flags = access_flags;
      this.cp = new ConstantPoolGen();
      this.addAttribute(new SourceFile(this.cp.addUtf8("SourceFile"), 2, this.cp.addUtf8(file_name), this.cp.getConstantPool()));
      this.class_name_index = this.cp.addClass(class_name);
      this.superclass_name_index = this.cp.addClass(super_class_name);
      if (interfaces != null) {
         for(int i = 0; i < interfaces.length; ++i) {
            this.addInterface(interfaces[i]);
         }
      }

   }

   public ClassGen(JavaClass clazz) {
      this.class_name_index = clazz.getClassNameIndex();
      this.superclass_name_index = clazz.getSuperclassNameIndex();
      this.class_name = clazz.getClassName();
      this.super_class_name = clazz.getSuperclassName();
      this.file_name = clazz.getSourceFileName();
      super.access_flags = clazz.getAccessFlags();
      this.cp = new ConstantPoolGen(clazz.getConstantPool());
      this.major = clazz.getMajor();
      this.minor = clazz.getMinor();
      Attribute[] attributes = clazz.getAttributes();
      Method[] methods = clazz.getMethods();
      Field[] fields = clazz.getFields();
      String[] interfaces = clazz.getInterfaceNames();

      for(int i = 0; i < interfaces.length; ++i) {
         this.addInterface(interfaces[i]);
      }

      for(int i = 0; i < attributes.length; ++i) {
         this.addAttribute(attributes[i]);
      }

      for(int i = 0; i < methods.length; ++i) {
         this.addMethod(methods[i]);
      }

      for(int i = 0; i < fields.length; ++i) {
         this.addField(fields[i]);
      }

   }

   public JavaClass getJavaClass() {
      int[] interfaces = this.getInterfaces();
      Field[] fields = this.getFields();
      Method[] methods = this.getMethods();
      Attribute[] attributes = this.getAttributes();
      ConstantPool cp = this.cp.getFinalConstantPool();
      return new JavaClass(this.class_name_index, this.superclass_name_index, this.file_name, this.major, this.minor, super.access_flags, cp, interfaces, fields, methods, attributes);
   }

   public void addInterface(String name) {
      this.interface_vec.add(name);
   }

   public void removeInterface(String name) {
      this.interface_vec.remove(name);
   }

   public int getMajor() {
      return this.major;
   }

   public void setMajor(int major) {
      this.major = major;
   }

   public void setMinor(int minor) {
      this.minor = minor;
   }

   public int getMinor() {
      return this.minor;
   }

   public void addAttribute(Attribute a) {
      this.attribute_vec.add(a);
   }

   public void addMethod(Method m) {
      this.method_vec.add(m);
   }

   public void addEmptyConstructor(int access_flags) {
      InstructionList il = new InstructionList();
      il.append((Instruction)InstructionConstants.THIS);
      il.append((Instruction)(new INVOKESPECIAL(this.cp.addMethodref(this.super_class_name, "<init>", "()V"))));
      il.append((Instruction)InstructionConstants.RETURN);
      MethodGen mg = new MethodGen(access_flags, Type.VOID, Type.NO_ARGS, (String[])null, "<init>", this.class_name, il, this.cp);
      mg.setMaxStack(1);
      this.addMethod(mg.getMethod());
   }

   public void addField(Field f) {
      this.field_vec.add(f);
   }

   public boolean containsField(Field f) {
      return this.field_vec.contains(f);
   }

   public Field containsField(String name) {
      Iterator e = this.field_vec.iterator();

      while(e.hasNext()) {
         Field f = (Field)e.next();
         if (f.getName().equals(name)) {
            return f;
         }
      }

      return null;
   }

   public Method containsMethod(String name, String signature) {
      Iterator e = this.method_vec.iterator();

      Method m;
      do {
         if (!e.hasNext()) {
            return null;
         }

         m = (Method)e.next();
      } while(!m.getName().equals(name) || !m.getSignature().equals(signature));

      return m;
   }

   public void removeAttribute(Attribute a) {
      this.attribute_vec.remove(a);
   }

   public void removeMethod(Method m) {
      this.method_vec.remove(m);
   }

   public void replaceMethod(Method old, Method new_) {
      if (new_ == null) {
         throw new ClassGenException("Replacement method must not be null");
      } else {
         int i = this.method_vec.indexOf(old);
         if (i < 0) {
            this.method_vec.add(new_);
         } else {
            this.method_vec.set(i, new_);
         }

      }
   }

   public void replaceField(Field old, Field new_) {
      if (new_ == null) {
         throw new ClassGenException("Replacement method must not be null");
      } else {
         int i = this.field_vec.indexOf(old);
         if (i < 0) {
            this.field_vec.add(new_);
         } else {
            this.field_vec.set(i, new_);
         }

      }
   }

   public void removeField(Field f) {
      this.field_vec.remove(f);
   }

   public String getClassName() {
      return this.class_name;
   }

   public String getSuperclassName() {
      return this.super_class_name;
   }

   public String getFileName() {
      return this.file_name;
   }

   public void setClassName(String name) {
      this.class_name = name.replace('/', '.');
      this.class_name_index = this.cp.addClass(name);
   }

   public void setSuperclassName(String name) {
      this.super_class_name = name.replace('/', '.');
      this.superclass_name_index = this.cp.addClass(name);
   }

   public Method[] getMethods() {
      Method[] methods = new Method[this.method_vec.size()];
      this.method_vec.toArray(methods);
      return methods;
   }

   public void setMethods(Method[] methods) {
      this.method_vec.clear();

      for(int m = 0; m < methods.length; ++m) {
         this.addMethod(methods[m]);
      }

   }

   public void setMethodAt(Method method, int pos) {
      this.method_vec.set(pos, method);
   }

   public Method getMethodAt(int pos) {
      return (Method)this.method_vec.get(pos);
   }

   public String[] getInterfaceNames() {
      int size = this.interface_vec.size();
      String[] interfaces = new String[size];
      this.interface_vec.toArray(interfaces);
      return interfaces;
   }

   public int[] getInterfaces() {
      int size = this.interface_vec.size();
      int[] interfaces = new int[size];

      for(int i = 0; i < size; ++i) {
         interfaces[i] = this.cp.addClass((String)this.interface_vec.get(i));
      }

      return interfaces;
   }

   public Field[] getFields() {
      Field[] fields = new Field[this.field_vec.size()];
      this.field_vec.toArray(fields);
      return fields;
   }

   public Attribute[] getAttributes() {
      Attribute[] attributes = new Attribute[this.attribute_vec.size()];
      this.attribute_vec.toArray(attributes);
      return attributes;
   }

   public ConstantPoolGen getConstantPool() {
      return this.cp;
   }

   public void setConstantPool(ConstantPoolGen constant_pool) {
      this.cp = constant_pool;
   }

   public void setClassNameIndex(int class_name_index) {
      this.class_name_index = class_name_index;
      this.class_name = this.cp.getConstantPool().getConstantString(class_name_index, (byte)7).replace('/', '.');
   }

   public void setSuperclassNameIndex(int superclass_name_index) {
      this.superclass_name_index = superclass_name_index;
      this.super_class_name = this.cp.getConstantPool().getConstantString(superclass_name_index, (byte)7).replace('/', '.');
   }

   public int getSuperclassNameIndex() {
      return this.superclass_name_index;
   }

   public int getClassNameIndex() {
      return this.class_name_index;
   }

   public void addObserver(ClassObserver o) {
      if (this.observers == null) {
         this.observers = new ArrayList();
      }

      this.observers.add(o);
   }

   public void removeObserver(ClassObserver o) {
      if (this.observers != null) {
         this.observers.remove(o);
      }

   }

   public void update() {
      if (this.observers != null) {
         Iterator e = this.observers.iterator();

         while(e.hasNext()) {
            ((ClassObserver)e.next()).notify(this);
         }
      }

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
