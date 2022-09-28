package org.apache.bcel.verifier.statics;

import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
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
import org.apache.bcel.classfile.ConstantValue;
import org.apache.bcel.classfile.Deprecated;
import org.apache.bcel.classfile.EmptyVisitor;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.InnerClass;
import org.apache.bcel.classfile.InnerClasses;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Node;
import org.apache.bcel.classfile.SourceFile;
import org.apache.bcel.classfile.Synthetic;
import org.apache.bcel.classfile.Unknown;
import org.apache.bcel.classfile.Visitor;

public class StringRepresentation extends EmptyVisitor implements Visitor {
   private String tostring;

   public StringRepresentation(Node n) {
      n.accept(this);
   }

   public String toString() {
      return this.tostring;
   }

   private String toString(Node obj) {
      String ret;
      try {
         ret = obj.toString();
      } catch (RuntimeException var5) {
         String s = obj.getClass().getName();
         s = s.substring(s.lastIndexOf(".") + 1);
         ret = "<<" + s + ">>";
      }

      return ret;
   }

   public void visitCode(Code obj) {
      this.tostring = "<CODE>";
   }

   public void visitCodeException(CodeException obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantClass(ConstantClass obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantDouble(ConstantDouble obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantFieldref(ConstantFieldref obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantFloat(ConstantFloat obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantInteger(ConstantInteger obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantLong(ConstantLong obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantMethodref(ConstantMethodref obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantNameAndType(ConstantNameAndType obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantPool(ConstantPool obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantString(ConstantString obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantUtf8(ConstantUtf8 obj) {
      this.tostring = this.toString(obj);
   }

   public void visitConstantValue(ConstantValue obj) {
      this.tostring = this.toString(obj);
   }

   public void visitDeprecated(Deprecated obj) {
      this.tostring = this.toString(obj);
   }

   public void visitExceptionTable(ExceptionTable obj) {
      this.tostring = this.toString(obj);
   }

   public void visitField(Field obj) {
      this.tostring = this.toString(obj);
   }

   public void visitInnerClass(InnerClass obj) {
      this.tostring = this.toString(obj);
   }

   public void visitInnerClasses(InnerClasses obj) {
      this.tostring = this.toString(obj);
   }

   public void visitJavaClass(JavaClass obj) {
      this.tostring = this.toString(obj);
   }

   public void visitLineNumber(LineNumber obj) {
      this.tostring = this.toString(obj);
   }

   public void visitLineNumberTable(LineNumberTable obj) {
      this.tostring = "<LineNumberTable: " + this.toString(obj) + ">";
   }

   public void visitLocalVariable(LocalVariable obj) {
      this.tostring = this.toString(obj);
   }

   public void visitLocalVariableTable(LocalVariableTable obj) {
      this.tostring = "<LocalVariableTable: " + this.toString(obj) + ">";
   }

   public void visitMethod(Method obj) {
      this.tostring = this.toString(obj);
   }

   public void visitSourceFile(SourceFile obj) {
      this.tostring = this.toString(obj);
   }

   public void visitSynthetic(Synthetic obj) {
      this.tostring = this.toString(obj);
   }

   public void visitUnknown(Unknown obj) {
      this.tostring = this.toString(obj);
   }
}
