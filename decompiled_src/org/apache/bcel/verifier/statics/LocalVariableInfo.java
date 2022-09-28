package org.apache.bcel.verifier.statics;

import java.util.Hashtable;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.LocalVariableInfoInconsistentException;

public class LocalVariableInfo {
   private Hashtable types = new Hashtable();
   private Hashtable names = new Hashtable();

   private void setName(int offset, String name) {
      this.names.put(Integer.toString(offset), name);
   }

   private void setType(int offset, Type t) {
      this.types.put(Integer.toString(offset), t);
   }

   public Type getType(int offset) {
      return (Type)this.types.get(Integer.toString(offset));
   }

   public String getName(int offset) {
      return (String)this.names.get(Integer.toString(offset));
   }

   public void add(String name, int startpc, int length, Type t) throws LocalVariableInfoInconsistentException {
      for(int i = startpc; i <= startpc + length; ++i) {
         this.add(i, name, t);
      }

   }

   private void add(int offset, String name, Type t) throws LocalVariableInfoInconsistentException {
      if (this.getName(offset) != null && !this.getName(offset).equals(name)) {
         throw new LocalVariableInfoInconsistentException("At bytecode offset '" + offset + "' a local variable has two different names: '" + this.getName(offset) + "' and '" + name + "'.");
      } else if (this.getType(offset) != null && !this.getType(offset).equals(t)) {
         throw new LocalVariableInfoInconsistentException("At bytecode offset '" + offset + "' a local variable has two different types: '" + this.getType(offset) + "' and '" + t + "'.");
      } else {
         this.setName(offset, name);
         this.setType(offset, t);
      }
   }
}
