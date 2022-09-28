package org.apache.xerces.dom;

import java.util.StringTokenizer;
import java.util.Vector;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DOMImplementationList;
import org.w3c.dom.DOMImplementationSource;

public class DOMImplementationSourceImpl implements DOMImplementationSource {
   public DOMImplementation getDOMImplementation(String var1) {
      DOMImplementation var2 = CoreDOMImplementationImpl.getDOMImplementation();
      if (this.testImpl(var2, var1)) {
         return var2;
      } else {
         var2 = DOMImplementationImpl.getDOMImplementation();
         return this.testImpl(var2, var1) ? var2 : null;
      }
   }

   public DOMImplementationList getDOMImplementationList(String var1) {
      DOMImplementation var2 = CoreDOMImplementationImpl.getDOMImplementation();
      Vector var3 = new Vector();
      if (this.testImpl(var2, var1)) {
         var3.addElement(var2);
      }

      var2 = DOMImplementationImpl.getDOMImplementation();
      if (this.testImpl(var2, var1)) {
         var3.addElement(var2);
      }

      return new DOMImplementationListImpl(var3);
   }

   boolean testImpl(DOMImplementation var1, String var2) {
      StringTokenizer var3 = new StringTokenizer(var2);
      String var4 = null;
      String var5 = null;
      if (var3.hasMoreTokens()) {
         var4 = var3.nextToken();
      }

      while(var4 != null) {
         boolean var6 = false;
         if (var3.hasMoreTokens()) {
            var5 = var3.nextToken();
            char var7 = var5.charAt(0);
            switch (var7) {
               case '0':
               case '1':
               case '2':
               case '3':
               case '4':
               case '5':
               case '6':
               case '7':
               case '8':
               case '9':
                  var6 = true;
            }
         } else {
            var5 = null;
         }

         if (var6) {
            if (!var1.hasFeature(var4, var5)) {
               return false;
            }

            if (var3.hasMoreTokens()) {
               var4 = var3.nextToken();
            } else {
               var4 = null;
            }
         } else {
            if (!var1.hasFeature(var4, (String)null)) {
               return false;
            }

            var4 = var5;
         }
      }

      return true;
   }
}
