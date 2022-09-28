package org.apache.batik.dom;

import java.io.Serializable;
import org.apache.batik.dom.events.DocumentEventSupport;
import org.apache.batik.dom.events.EventSupport;
import org.apache.batik.dom.util.HashTable;
import org.w3c.dom.DOMImplementation;

public abstract class AbstractDOMImplementation implements DOMImplementation, Serializable {
   protected final HashTable features = new HashTable();

   protected void registerFeature(String var1, Object var2) {
      this.features.put(var1.toLowerCase(), var2);
   }

   protected AbstractDOMImplementation() {
      this.registerFeature("Core", new String[]{"2.0", "3.0"});
      this.registerFeature("XML", new String[]{"1.0", "2.0", "3.0"});
      this.registerFeature("Events", new String[]{"2.0", "3.0"});
      this.registerFeature("UIEvents", new String[]{"2.0", "3.0"});
      this.registerFeature("MouseEvents", new String[]{"2.0", "3.0"});
      this.registerFeature("TextEvents", "3.0");
      this.registerFeature("KeyboardEvents", "3.0");
      this.registerFeature("MutationEvents", new String[]{"2.0", "3.0"});
      this.registerFeature("MutationNameEvents", "3.0");
      this.registerFeature("Traversal", "2.0");
      this.registerFeature("XPath", "3.0");
   }

   public boolean hasFeature(String var1, String var2) {
      if (var1 != null && var1.length() != 0) {
         if (var1.charAt(0) == '+') {
            var1 = var1.substring(1);
         }

         Object var3 = this.features.get(var1.toLowerCase());
         if (var3 == null) {
            return false;
         } else if (var2 != null && var2.length() != 0) {
            if (var3 instanceof String) {
               return var2.equals(var3);
            } else {
               String[] var4 = (String[])var3;

               for(int var5 = 0; var5 < var4.length; ++var5) {
                  if (var2.equals(var4[var5])) {
                     return true;
                  }
               }

               return false;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public Object getFeature(String var1, String var2) {
      return this.hasFeature(var1, var2) ? this : null;
   }

   public DocumentEventSupport createDocumentEventSupport() {
      return new DocumentEventSupport();
   }

   public EventSupport createEventSupport(AbstractNode var1) {
      return new EventSupport(var1);
   }
}
