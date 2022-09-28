package org.apache.xerces.dom;

import java.util.Vector;
import org.w3c.dom.DOMStringList;

public class DOMStringListImpl implements DOMStringList {
   private Vector fStrings;

   public DOMStringListImpl() {
      this.fStrings = new Vector();
   }

   public DOMStringListImpl(Vector var1) {
      this.fStrings = var1;
   }

   public String item(int var1) {
      try {
         return (String)this.fStrings.elementAt(var1);
      } catch (ArrayIndexOutOfBoundsException var3) {
         return null;
      }
   }

   public int getLength() {
      return this.fStrings.size();
   }

   public boolean contains(String var1) {
      return this.fStrings.contains(var1);
   }

   public void add(String var1) {
      this.fStrings.add(var1);
   }
}
