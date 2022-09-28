package org.apache.bcel.util;

import java.util.Stack;
import org.apache.bcel.classfile.JavaClass;

public class ClassStack {
   private Stack stack = new Stack();

   public void push(JavaClass clazz) {
      this.stack.push(clazz);
   }

   public JavaClass pop() {
      return (JavaClass)this.stack.pop();
   }

   public JavaClass top() {
      return (JavaClass)this.stack.peek();
   }

   public boolean empty() {
      return this.stack.empty();
   }
}
