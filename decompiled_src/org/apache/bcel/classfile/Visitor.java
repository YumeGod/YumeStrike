package org.apache.bcel.classfile;

public interface Visitor {
   void visitCode(Code var1);

   void visitCodeException(CodeException var1);

   void visitConstantClass(ConstantClass var1);

   void visitConstantDouble(ConstantDouble var1);

   void visitConstantFieldref(ConstantFieldref var1);

   void visitConstantFloat(ConstantFloat var1);

   void visitConstantInteger(ConstantInteger var1);

   void visitConstantInterfaceMethodref(ConstantInterfaceMethodref var1);

   void visitConstantLong(ConstantLong var1);

   void visitConstantMethodref(ConstantMethodref var1);

   void visitConstantNameAndType(ConstantNameAndType var1);

   void visitConstantPool(ConstantPool var1);

   void visitConstantString(ConstantString var1);

   void visitConstantUtf8(ConstantUtf8 var1);

   void visitConstantValue(ConstantValue var1);

   void visitDeprecated(Deprecated var1);

   void visitExceptionTable(ExceptionTable var1);

   void visitField(Field var1);

   void visitInnerClass(InnerClass var1);

   void visitInnerClasses(InnerClasses var1);

   void visitJavaClass(JavaClass var1);

   void visitLineNumber(LineNumber var1);

   void visitLineNumberTable(LineNumberTable var1);

   void visitLocalVariable(LocalVariable var1);

   void visitLocalVariableTable(LocalVariableTable var1);

   void visitMethod(Method var1);

   void visitSourceFile(SourceFile var1);

   void visitSynthetic(Synthetic var1);

   void visitUnknown(Unknown var1);

   void visitStackMap(StackMap var1);

   void visitStackMapEntry(StackMapEntry var1);
}
