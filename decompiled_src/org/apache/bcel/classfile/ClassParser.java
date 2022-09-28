package org.apache.bcel.classfile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ClassParser {
   private DataInputStream file;
   private ZipFile zip;
   private String file_name;
   private int class_name_index;
   private int superclass_name_index;
   private int major;
   private int minor;
   private int access_flags;
   private int[] interfaces;
   private ConstantPool constant_pool;
   private Field[] fields;
   private Method[] methods;
   private Attribute[] attributes;
   private boolean is_zip;
   private static final int BUFSIZE = 8192;

   public ClassParser(InputStream file, String file_name) {
      this.file_name = file_name;
      String clazz = file.getClass().getName();
      this.is_zip = clazz.startsWith("java.util.zip.") || clazz.startsWith("java.util.jar.");
      if (file instanceof DataInputStream) {
         this.file = (DataInputStream)file;
      } else {
         this.file = new DataInputStream(new BufferedInputStream(file, 8192));
      }

   }

   public ClassParser(String file_name) throws IOException {
      this.is_zip = false;
      this.file_name = file_name;
      this.file = new DataInputStream(new BufferedInputStream(new FileInputStream(file_name), 8192));
   }

   public ClassParser(String zip_file, String file_name) throws IOException {
      this.is_zip = true;
      this.zip = new ZipFile(zip_file);
      ZipEntry entry = this.zip.getEntry(file_name);
      this.file_name = file_name;
      this.file = new DataInputStream(new BufferedInputStream(this.zip.getInputStream(entry), 8192));
   }

   public JavaClass parse() throws IOException, ClassFormatError {
      this.readID();
      this.readVersion();
      this.readConstantPool();
      this.readClassInfo();
      this.readInterfaces();
      this.readFields();
      this.readMethods();
      this.readAttributes();
      this.file.close();
      if (this.zip != null) {
         this.zip.close();
      }

      return new JavaClass(this.class_name_index, this.superclass_name_index, this.file_name, this.major, this.minor, this.access_flags, this.constant_pool, this.interfaces, this.fields, this.methods, this.attributes, (byte)(this.is_zip ? 3 : 2));
   }

   private final void readAttributes() throws IOException, ClassFormatError {
      int attributes_count = this.file.readUnsignedShort();
      this.attributes = new Attribute[attributes_count];

      for(int i = 0; i < attributes_count; ++i) {
         this.attributes[i] = Attribute.readAttribute(this.file, this.constant_pool);
      }

   }

   private final void readClassInfo() throws IOException, ClassFormatError {
      this.access_flags = this.file.readUnsignedShort();
      if ((this.access_flags & 512) != 0) {
         this.access_flags |= 1024;
      }

      if ((this.access_flags & 1024) != 0 && (this.access_flags & 16) != 0) {
         throw new ClassFormatError("Class can't be both final and abstract");
      } else {
         this.class_name_index = this.file.readUnsignedShort();
         this.superclass_name_index = this.file.readUnsignedShort();
      }
   }

   private final void readConstantPool() throws IOException, ClassFormatError {
      this.constant_pool = new ConstantPool(this.file);
   }

   private final void readFields() throws IOException, ClassFormatError {
      int fields_count = this.file.readUnsignedShort();
      this.fields = new Field[fields_count];

      for(int i = 0; i < fields_count; ++i) {
         this.fields[i] = new Field(this.file, this.constant_pool);
      }

   }

   private final void readID() throws IOException, ClassFormatError {
      int magic = -889275714;
      if (this.file.readInt() != magic) {
         throw new ClassFormatError(this.file_name + " is not a Java .class file");
      }
   }

   private final void readInterfaces() throws IOException, ClassFormatError {
      int interfaces_count = this.file.readUnsignedShort();
      this.interfaces = new int[interfaces_count];

      for(int i = 0; i < interfaces_count; ++i) {
         this.interfaces[i] = this.file.readUnsignedShort();
      }

   }

   private final void readMethods() throws IOException, ClassFormatError {
      int methods_count = this.file.readUnsignedShort();
      this.methods = new Method[methods_count];

      for(int i = 0; i < methods_count; ++i) {
         this.methods[i] = new Method(this.file, this.constant_pool);
      }

   }

   private final void readVersion() throws IOException, ClassFormatError {
      this.minor = this.file.readUnsignedShort();
      this.major = this.file.readUnsignedShort();
   }
}
