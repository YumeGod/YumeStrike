package org.apache.xerces.util;

import org.apache.xerces.xni.XMLResourceIdentifier;

public class XMLResourceIdentifierImpl implements XMLResourceIdentifier {
   protected String fPublicId;
   protected String fLiteralSystemId;
   protected String fBaseSystemId;
   protected String fExpandedSystemId;
   protected String fNamespace;

   public XMLResourceIdentifierImpl() {
   }

   public XMLResourceIdentifierImpl(String var1, String var2, String var3, String var4) {
      this.setValues(var1, var2, var3, var4, (String)null);
   }

   public XMLResourceIdentifierImpl(String var1, String var2, String var3, String var4, String var5) {
      this.setValues(var1, var2, var3, var4, var5);
   }

   public void setValues(String var1, String var2, String var3, String var4) {
      this.setValues(var1, var2, var3, var4, (String)null);
   }

   public void setValues(String var1, String var2, String var3, String var4, String var5) {
      this.fPublicId = var1;
      this.fLiteralSystemId = var2;
      this.fBaseSystemId = var3;
      this.fExpandedSystemId = var4;
      this.fNamespace = var5;
   }

   public void clear() {
      this.fPublicId = null;
      this.fLiteralSystemId = null;
      this.fBaseSystemId = null;
      this.fExpandedSystemId = null;
      this.fNamespace = null;
   }

   public void setPublicId(String var1) {
      this.fPublicId = var1;
   }

   public void setLiteralSystemId(String var1) {
      this.fLiteralSystemId = var1;
   }

   public void setBaseSystemId(String var1) {
      this.fBaseSystemId = var1;
   }

   public void setExpandedSystemId(String var1) {
      this.fExpandedSystemId = var1;
   }

   public void setNamespace(String var1) {
      this.fNamespace = var1;
   }

   public String getPublicId() {
      return this.fPublicId;
   }

   public String getLiteralSystemId() {
      return this.fLiteralSystemId;
   }

   public String getBaseSystemId() {
      return this.fBaseSystemId;
   }

   public String getExpandedSystemId() {
      return this.fExpandedSystemId;
   }

   public String getNamespace() {
      return this.fNamespace;
   }

   public int hashCode() {
      int var1 = 0;
      if (this.fPublicId != null) {
         var1 += this.fPublicId.hashCode();
      }

      if (this.fLiteralSystemId != null) {
         var1 += this.fLiteralSystemId.hashCode();
      }

      if (this.fBaseSystemId != null) {
         var1 += this.fBaseSystemId.hashCode();
      }

      if (this.fExpandedSystemId != null) {
         var1 += this.fExpandedSystemId.hashCode();
      }

      if (this.fNamespace != null) {
         var1 += this.fNamespace.hashCode();
      }

      return var1;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.fPublicId != null) {
         var1.append(this.fPublicId);
      }

      var1.append(':');
      if (this.fLiteralSystemId != null) {
         var1.append(this.fLiteralSystemId);
      }

      var1.append(':');
      if (this.fBaseSystemId != null) {
         var1.append(this.fBaseSystemId);
      }

      var1.append(':');
      if (this.fExpandedSystemId != null) {
         var1.append(this.fExpandedSystemId);
      }

      var1.append(':');
      if (this.fNamespace != null) {
         var1.append(this.fNamespace);
      }

      return var1.toString();
   }
}
