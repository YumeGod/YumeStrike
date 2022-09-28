package org.apache.bcel.classfile;

import java.util.Stack;

public class DescendingVisitor implements Visitor {
   private JavaClass clazz;
   private Visitor visitor;
   private Stack stack = new Stack();

   public Object predecessor() {
      return this.predecessor(0);
   }

   public Object predecessor(int level) {
      int size = this.stack.size();
      return size >= 2 && level >= 0 ? this.stack.elementAt(size - (level + 2)) : null;
   }

   public Object current() {
      return this.stack.peek();
   }

   public DescendingVisitor(JavaClass clazz, Visitor visitor) {
      this.clazz = clazz;
      this.visitor = visitor;
   }

   public void visit() {
      this.clazz.accept(this);
   }

   public void visitJavaClass(JavaClass clazz) {
      this.stack.push(clazz);
      clazz.accept(this.visitor);
      Field[] fields = clazz.getFields();

      for(int i = 0; i < fields.length; ++i) {
         fields[i].accept(this);
      }

      Method[] methods = clazz.getMethods();

      for(int i = 0; i < methods.length; ++i) {
         methods[i].accept(this);
      }

      Attribute[] attributes = clazz.getAttributes();

      for(int i = 0; i < attributes.length; ++i) {
         attributes[i].accept(this);
      }

      clazz.getConstantPool().accept(this);
      this.stack.pop();
   }

   public void visitField(Field field) {
      this.stack.push(field);
      field.accept(this.visitor);
      Attribute[] attributes = field.getAttributes();

      for(int i = 0; i < attributes.length; ++i) {
         attributes[i].accept(this);
      }

      this.stack.pop();
   }

   public void visitConstantValue(ConstantValue cv) {
      this.stack.push(cv);
      cv.accept(this.visitor);
      this.stack.pop();
   }

   public void visitMethod(Method method) {
      this.stack.push(method);
      method.accept(this.visitor);
      Attribute[] attributes = method.getAttributes();

      for(int i = 0; i < attributes.length; ++i) {
         attributes[i].accept(this);
      }

      this.stack.pop();
   }

   public void visitExceptionTable(ExceptionTable table) {
      this.stack.push(table);
      table.accept(this.visitor);
      this.stack.pop();
   }

   public void visitCode(Code code) {
      this.stack.push(code);
      code.accept(this.visitor);
      CodeException[] table = code.getExceptionTable();

      for(int i = 0; i < table.length; ++i) {
         table[i].accept(this);
      }

      Attribute[] attributes = code.getAttributes();

      for(int i = 0; i < attributes.length; ++i) {
         attributes[i].accept(this);
      }

      this.stack.pop();
   }

   public void visitCodeException(CodeException ce) {
      this.stack.push(ce);
      ce.accept(this.visitor);
      this.stack.pop();
   }

   public void visitLineNumberTable(LineNumberTable table) {
      this.stack.push(table);
      table.accept(this.visitor);
      LineNumber[] numbers = table.getLineNumberTable();

      for(int i = 0; i < numbers.length; ++i) {
         numbers[i].accept(this);
      }

      this.stack.pop();
   }

   public void visitLineNumber(LineNumber number) {
      this.stack.push(number);
      number.accept(this.visitor);
      this.stack.pop();
   }

   public void visitLocalVariableTable(LocalVariableTable table) {
      this.stack.push(table);
      table.accept(this.visitor);
      LocalVariable[] vars = table.getLocalVariableTable();

      for(int i = 0; i < vars.length; ++i) {
         vars[i].accept(this);
      }

      this.stack.pop();
   }

   public void visitStackMap(StackMap table) {
      this.stack.push(table);
      table.accept(this.visitor);
      StackMapEntry[] vars = table.getStackMap();

      for(int i = 0; i < vars.length; ++i) {
         vars[i].accept(this);
      }

      this.stack.pop();
   }

   public void visitStackMapEntry(StackMapEntry var) {
      this.stack.push(var);
      var.accept(this.visitor);
      this.stack.pop();
   }

   public void visitLocalVariable(LocalVariable var) {
      this.stack.push(var);
      var.accept(this.visitor);
      this.stack.pop();
   }

   public void visitConstantPool(ConstantPool cp) {
      this.stack.push(cp);
      cp.accept(this.visitor);
      Constant[] constants = cp.getConstantPool();

      for(int i = 1; i < constants.length; ++i) {
         if (constants[i] != null) {
            constants[i].accept(this);
         }
      }

      this.stack.pop();
   }

   public void visitConstantClass(ConstantClass constant) {
      this.stack.push(constant);
      constant.accept(this.visitor);
      this.stack.pop();
   }

   public void visitConstantDouble(ConstantDouble constant) {
      this.stack.push(constant);
      constant.accept(this.visitor);
      this.stack.pop();
   }

   public void visitConstantFieldref(ConstantFieldref constant) {
      this.stack.push(constant);
      constant.accept(this.visitor);
      this.stack.pop();
   }

   public void visitConstantFloat(ConstantFloat constant) {
      this.stack.push(constant);
      constant.accept(this.visitor);
      this.stack.pop();
   }

   public void visitConstantInteger(ConstantInteger constant) {
      this.stack.push(constant);
      constant.accept(this.visitor);
      this.stack.pop();
   }

   public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref constant) {
      this.stack.push(constant);
      constant.accept(this.visitor);
      this.stack.pop();
   }

   public void visitConstantLong(ConstantLong constant) {
      this.stack.push(constant);
      constant.accept(this.visitor);
      this.stack.pop();
   }

   public void visitConstantMethodref(ConstantMethodref constant) {
      this.stack.push(constant);
      constant.accept(this.visitor);
      this.stack.pop();
   }

   public void visitConstantNameAndType(ConstantNameAndType constant) {
      this.stack.push(constant);
      constant.accept(this.visitor);
      this.stack.pop();
   }

   public void visitConstantString(ConstantString constant) {
      this.stack.push(constant);
      constant.accept(this.visitor);
      this.stack.pop();
   }

   public void visitConstantUtf8(ConstantUtf8 constant) {
      this.stack.push(constant);
      constant.accept(this.visitor);
      this.stack.pop();
   }

   public void visitInnerClasses(InnerClasses ic) {
      this.stack.push(ic);
      ic.accept(this.visitor);
      InnerClass[] ics = ic.getInnerClasses();

      for(int i = 0; i < ics.length; ++i) {
         ics[i].accept(this);
      }

      this.stack.pop();
   }

   public void visitInnerClass(InnerClass inner) {
      this.stack.push(inner);
      inner.accept(this.visitor);
      this.stack.pop();
   }

   public void visitDeprecated(Deprecated attribute) {
      this.stack.push(attribute);
      attribute.accept(this.visitor);
      this.stack.pop();
   }

   public void visitSourceFile(SourceFile attribute) {
      this.stack.push(attribute);
      attribute.accept(this.visitor);
      this.stack.pop();
   }

   public void visitSynthetic(Synthetic attribute) {
      this.stack.push(attribute);
      attribute.accept(this.visitor);
      this.stack.pop();
   }

   public void visitUnknown(Unknown attribute) {
      this.stack.push(attribute);
      attribute.accept(this.visitor);
      this.stack.pop();
   }
}
