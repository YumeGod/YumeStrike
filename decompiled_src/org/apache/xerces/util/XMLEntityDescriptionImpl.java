package org.apache.xerces.util;

import org.apache.xerces.impl.XMLEntityDescription;

public class XMLEntityDescriptionImpl extends XMLResourceIdentifierImpl implements XMLEntityDescription {
   protected String fEntityName;

   public XMLEntityDescriptionImpl() {
   }

   public XMLEntityDescriptionImpl(String var1, String var2, String var3, String var4, String var5) {
      this.setDescription(var1, var2, var3, var4, var5);
   }

   public XMLEntityDescriptionImpl(String var1, String var2, String var3, String var4, String var5, String var6) {
      this.setDescription(var1, var2, var3, var4, var5, var6);
   }

   public void setEntityName(String var1) {
      this.fEntityName = var1;
   }

   public String getEntityName() {
      return this.fEntityName;
   }

   public void setDescription(String var1, String var2, String var3, String var4, String var5) {
      this.setDescription(var1, var2, var3, var4, var5, (String)null);
   }

   public void setDescription(String var1, String var2, String var3, String var4, String var5, String var6) {
      this.fEntityName = var1;
      this.setValues(var2, var3, var4, var5, var6);
   }

   public void clear() {
      super.clear();
      this.fEntityName = null;
   }

   public int hashCode() {
      int var1 = super.hashCode();
      if (this.fEntityName != null) {
         var1 += this.fEntityName.hashCode();
      }

      return var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.fEntityName != null) {
         var1.append(this.fEntityName);
      }

      var1.append(':');
      if (super.fPublicId != null) {
         var1.append(super.fPublicId);
      }

      var1.append(':');
      if (super.fLiteralSystemId != null) {
         var1.append(super.fLiteralSystemId);
      }

      var1.append(':');
      if (super.fBaseSystemId != null) {
         var1.append(super.fBaseSystemId);
      }

      var1.append(':');
      if (super.fExpandedSystemId != null) {
         var1.append(super.fExpandedSystemId);
      }

      var1.append(':');
      if (super.fNamespace != null) {
         var1.append(super.fNamespace);
      }

      return var1.toString();
   }
}
