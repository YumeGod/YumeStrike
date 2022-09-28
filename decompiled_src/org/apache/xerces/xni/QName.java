package org.apache.xerces.xni;

public class QName implements Cloneable {
   public String prefix;
   public String localpart;
   public String rawname;
   public String uri;

   public QName() {
      this.clear();
   }

   public QName(String var1, String var2, String var3, String var4) {
      this.setValues(var1, var2, var3, var4);
   }

   public QName(QName var1) {
      this.setValues(var1);
   }

   public void setValues(QName var1) {
      this.prefix = var1.prefix;
      this.localpart = var1.localpart;
      this.rawname = var1.rawname;
      this.uri = var1.uri;
   }

   public void setValues(String var1, String var2, String var3, String var4) {
      this.prefix = var1;
      this.localpart = var2;
      this.rawname = var3;
      this.uri = var4;
   }

   public void clear() {
      this.prefix = null;
      this.localpart = null;
      this.rawname = null;
      this.uri = null;
   }

   public Object clone() {
      return new QName(this);
   }

   public int hashCode() {
      if (this.uri != null) {
         return this.uri.hashCode() + (this.localpart != null ? this.localpart.hashCode() : 0);
      } else {
         return this.rawname != null ? this.rawname.hashCode() : 0;
      }
   }

   public boolean equals(Object var1) {
      if (var1 instanceof QName) {
         QName var2 = (QName)var1;
         if (var2.uri != null) {
            return this.uri == var2.uri && this.localpart == var2.localpart;
         }

         if (this.uri == null) {
            return this.rawname == var2.rawname;
         }
      }

      return false;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      boolean var2 = false;
      if (this.prefix != null) {
         var1.append("prefix=\"" + this.prefix + '"');
         var2 = true;
      }

      if (this.localpart != null) {
         if (var2) {
            var1.append(',');
         }

         var1.append("localpart=\"" + this.localpart + '"');
         var2 = true;
      }

      if (this.rawname != null) {
         if (var2) {
            var1.append(',');
         }

         var1.append("rawname=\"" + this.rawname + '"');
         var2 = true;
      }

      if (this.uri != null) {
         if (var2) {
            var1.append(',');
         }

         var1.append("uri=\"" + this.uri + '"');
      }

      return var1.toString();
   }
}
