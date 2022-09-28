package org.apache.xerces.impl.dv;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DatatypeException extends Exception {
   static final long serialVersionUID = 1940805832730465578L;
   protected String key;
   protected Object[] args;

   public DatatypeException(String var1, Object[] var2) {
      super(var1);
      this.key = var1;
      this.args = var2;
   }

   public String getKey() {
      return this.key;
   }

   public Object[] getArgs() {
      return this.args;
   }

   public String getMessage() {
      ResourceBundle var1 = null;
      var1 = ResourceBundle.getBundle("org.apache.xerces.impl.msg.XMLSchemaMessages");
      if (var1 == null) {
         throw new MissingResourceException("Property file not found!", "org.apache.xerces.impl.msg.XMLSchemaMessages", this.key);
      } else {
         String var2 = var1.getString(this.key);
         if (var2 == null) {
            var2 = var1.getString("BadMessageKey");
            throw new MissingResourceException(var2, "org.apache.xerces.impl.msg.XMLSchemaMessages", this.key);
         } else {
            if (this.args != null) {
               try {
                  var2 = MessageFormat.format(var2, this.args);
               } catch (Exception var4) {
                  var2 = var1.getString("FormatFailed");
                  var2 = var2 + " " + var1.getString(this.key);
               }
            }

            return var2;
         }
      }
   }
}
