package org.apache.bcel.util;

import java.util.ArrayList;
import org.apache.bcel.classfile.JavaClass;

public class ClassVector {
   protected ArrayList vec = new ArrayList();

   public void addElement(JavaClass clazz) {
      this.vec.add(clazz);
   }

   public JavaClass elementAt(int index) {
      return (JavaClass)this.vec.get(index);
   }

   public void removeElementAt(int index) {
      this.vec.remove(index);
   }

   public JavaClass[] toArray() {
      JavaClass[] classes = new JavaClass[this.vec.size()];
      this.vec.toArray(classes);
      return classes;
   }
}
