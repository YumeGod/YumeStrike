package org.apache.xerces.dom;

import java.util.Vector;
import org.apache.xerces.impl.xs.XSImplementationImpl;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;

public class DOMXSImplementationSourceImpl extends DOMImplementationSourceImpl {
   public DOMImplementation getDOMImplementation(String var1) {
      DOMImplementation var2 = super.getDOMImplementation(var1);
      if (var2 != null) {
         return var2;
      } else {
         var2 = PSVIDOMImplementationImpl.getDOMImplementation();
         if (this.testImpl(var2, var1)) {
            return var2;
         } else {
            var2 = XSImplementationImpl.getDOMImplementation();
            return this.testImpl(var2, var1) ? var2 : null;
         }
      }
   }

   public DOMImplementationList getDOMImplementationList(String var1) {
      Vector var2 = new Vector();
      DOMImplementationList var3 = super.getDOMImplementationList(var1);

      for(int var4 = 0; var4 < var3.getLength(); ++var4) {
         var2.addElement(var3.item(var4));
      }

      DOMImplementation var5 = PSVIDOMImplementationImpl.getDOMImplementation();
      if (this.testImpl(var5, var1)) {
         var2.addElement(var5);
      }

      var5 = XSImplementationImpl.getDOMImplementation();
      if (this.testImpl(var5, var1)) {
         var2.addElement(var5);
      }

      return new DOMImplementationListImpl(var2);
   }
}
