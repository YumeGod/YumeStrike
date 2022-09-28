package org.apache.bcel.generic;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;

public final class ObjectType extends ReferenceType {
   private String class_name;

   public ObjectType(String class_name) {
      super((byte)14, "L" + class_name.replace('.', '/') + ";");
      this.class_name = class_name.replace('/', '.');
   }

   public String getClassName() {
      return this.class_name;
   }

   public int hashCode() {
      return this.class_name.hashCode();
   }

   public boolean equals(Object type) {
      return type instanceof ObjectType ? ((ObjectType)type).class_name.equals(this.class_name) : false;
   }

   public boolean referencesClass() {
      JavaClass jc = Repository.lookupClass(this.class_name);
      return jc == null ? false : jc.isClass();
   }

   public boolean referencesInterface() {
      JavaClass jc = Repository.lookupClass(this.class_name);
      if (jc == null) {
         return false;
      } else {
         return !jc.isClass();
      }
   }

   public boolean subclassOf(ObjectType superclass) {
      return !this.referencesInterface() && !superclass.referencesInterface() ? Repository.instanceOf(this.class_name, superclass.class_name) : false;
   }

   public boolean accessibleTo(ObjectType accessor) {
      JavaClass jc = Repository.lookupClass(this.class_name);
      if (jc.isPublic()) {
         return true;
      } else {
         JavaClass acc = Repository.lookupClass(accessor.class_name);
         return acc.getPackageName().equals(jc.getPackageName());
      }
   }
}
