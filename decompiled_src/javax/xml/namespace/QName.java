package javax.xml.namespace;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class QName implements Serializable {
   private static final long serialVersionUID;
   private static final long defaultSerialVersionUID = -9120448754896609940L;
   private static final long compatabilitySerialVersionUID = 4418622981026545151L;
   private final String namespaceURI;
   private final String localPart;
   private String prefix;
   private transient String qNameAsString;

   public QName(String var1, String var2) {
      this(var1, var2, "");
   }

   public QName(String var1, String var2, String var3) {
      if (var1 == null) {
         this.namespaceURI = "";
      } else {
         this.namespaceURI = var1;
      }

      if (var2 == null) {
         throw new IllegalArgumentException("local part cannot be \"null\" when creating a QName");
      } else {
         this.localPart = var2;
         if (var3 == null) {
            throw new IllegalArgumentException("prefix cannot be \"null\" when creating a QName");
         } else {
            this.prefix = var3;
         }
      }
   }

   public QName(String var1) {
      this("", var1, "");
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public String getLocalPart() {
      return this.localPart;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof QName)) {
         return false;
      } else {
         QName var2 = (QName)var1;
         return this.localPart.equals(var2.localPart) && this.namespaceURI.equals(var2.namespaceURI);
      }
   }

   public final int hashCode() {
      return this.namespaceURI.hashCode() ^ this.localPart.hashCode();
   }

   public String toString() {
      String var1 = this.qNameAsString;
      if (var1 == null) {
         int var2 = this.namespaceURI.length();
         if (var2 == 0) {
            var1 = this.localPart;
         } else {
            StringBuffer var3 = new StringBuffer(var2 + this.localPart.length() + 2);
            var3.append('{');
            var3.append(this.namespaceURI);
            var3.append('}');
            var3.append(this.localPart);
            var1 = var3.toString();
         }

         this.qNameAsString = var1;
      }

      return var1;
   }

   public static QName valueOf(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("cannot create QName from \"null\" or \"\" String");
      } else if (var0.length() == 0) {
         return new QName("", var0, "");
      } else if (var0.charAt(0) != '{') {
         return new QName("", var0, "");
      } else if (var0.startsWith("{}")) {
         throw new IllegalArgumentException("Namespace URI .equals(XMLConstants.NULL_NS_URI), .equals(\"\"), only the local part, \"" + var0.substring(2 + "".length()) + "\", " + "should be provided.");
      } else {
         int var1 = var0.indexOf(125);
         if (var1 == -1) {
            throw new IllegalArgumentException("cannot create QName from \"" + var0 + "\", missing closing \"}\"");
         } else {
            return new QName(var0.substring(1, var1), var0.substring(var1 + 1), "");
         }
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      if (this.prefix == null) {
         this.prefix = "";
      }

   }

   static {
      String var0 = null;

      try {
         var0 = (String)AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
               return System.getProperty("org.apache.xml.namespace.QName.useCompatibleSerialVersionUID");
            }
         });
      } catch (Exception var2) {
      }

      serialVersionUID = !"1.0".equals(var0) ? -9120448754896609940L : 4418622981026545151L;
   }
}
