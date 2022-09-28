package org.apache.xerces.dom;

import java.util.Vector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;

public class DOMImplementationListImpl implements DOMImplementationList {
   private Vector fImplementations;

   public DOMImplementationListImpl() {
      this.fImplementations = new Vector();
   }

   public DOMImplementationListImpl(Vector var1) {
      this.fImplementations = var1;
   }

   public DOMImplementation item(int var1) {
      try {
         return (DOMImplementation)this.fImplementations.elementAt(var1);
      } catch (ArrayIndexOutOfBoundsException var3) {
         return null;
      }
   }

   public int getLength() {
      return this.fImplementations.size();
   }
}
