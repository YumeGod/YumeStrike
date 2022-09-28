package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSIDCDefinition;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;

public abstract class IdentityConstraint implements XSIDCDefinition {
   protected short type;
   protected String fNamespace;
   protected String fIdentityConstraintName;
   protected String fElementName;
   protected Selector fSelector;
   protected int fFieldCount;
   protected Field[] fFields;
   protected XSAnnotationImpl[] fAnnotations = null;
   protected int fNumAnnotations;

   protected IdentityConstraint(String var1, String var2, String var3) {
      this.fNamespace = var1;
      this.fIdentityConstraintName = var2;
      this.fElementName = var3;
   }

   public String getIdentityConstraintName() {
      return this.fIdentityConstraintName;
   }

   public void setSelector(Selector var1) {
      this.fSelector = var1;
   }

   public Selector getSelector() {
      return this.fSelector;
   }

   public void addField(Field var1) {
      if (this.fFields == null) {
         this.fFields = new Field[4];
      } else if (this.fFieldCount == this.fFields.length) {
         this.fFields = resize(this.fFields, this.fFieldCount * 2);
      }

      this.fFields[this.fFieldCount++] = var1;
   }

   public int getFieldCount() {
      return this.fFieldCount;
   }

   public Field getFieldAt(int var1) {
      return this.fFields[var1];
   }

   public String getElementName() {
      return this.fElementName;
   }

   public String toString() {
      String var1 = super.toString();
      int var2 = var1.lastIndexOf(36);
      if (var2 != -1) {
         return var1.substring(var2 + 1);
      } else {
         int var3 = var1.lastIndexOf(46);
         return var3 != -1 ? var1.substring(var3 + 1) : var1;
      }
   }

   public boolean equals(IdentityConstraint var1) {
      boolean var2 = this.fIdentityConstraintName.equals(var1.fIdentityConstraintName);
      if (!var2) {
         return false;
      } else {
         var2 = this.fSelector.toString().equals(var1.fSelector.toString());
         if (!var2) {
            return false;
         } else {
            var2 = this.fFieldCount == var1.fFieldCount;
            if (!var2) {
               return false;
            } else {
               for(int var3 = 0; var3 < this.fFieldCount; ++var3) {
                  if (!this.fFields[var3].toString().equals(var1.fFields[var3].toString())) {
                     return false;
                  }
               }

               return true;
            }
         }
      }
   }

   static final Field[] resize(Field[] var0, int var1) {
      Field[] var2 = new Field[var1];
      System.arraycopy(var0, 0, var2, 0, var0.length);
      return var2;
   }

   public short getType() {
      return 10;
   }

   public String getName() {
      return this.fIdentityConstraintName;
   }

   public String getNamespace() {
      return this.fNamespace;
   }

   public short getCategory() {
      return this.type;
   }

   public String getSelectorStr() {
      return this.fSelector != null ? this.fSelector.toString() : null;
   }

   public StringList getFieldStrs() {
      String[] var1 = new String[this.fFieldCount];

      for(int var2 = 0; var2 < this.fFieldCount; ++var2) {
         var1[var2] = this.fFields[var2].toString();
      }

      return new StringListImpl(var1, this.fFieldCount);
   }

   public XSIDCDefinition getRefKey() {
      return null;
   }

   public XSObjectList getAnnotations() {
      return new XSObjectListImpl(this.fAnnotations, this.fNumAnnotations);
   }

   public XSNamespaceItem getNamespaceItem() {
      return null;
   }

   public void addAnnotation(XSAnnotationImpl var1) {
      if (var1 != null) {
         if (this.fAnnotations == null) {
            this.fAnnotations = new XSAnnotationImpl[2];
         } else if (this.fNumAnnotations == this.fAnnotations.length) {
            XSAnnotationImpl[] var2 = new XSAnnotationImpl[this.fNumAnnotations << 1];
            System.arraycopy(this.fAnnotations, 0, var2, 0, this.fNumAnnotations);
            this.fAnnotations = var2;
         }

         this.fAnnotations[this.fNumAnnotations++] = var1;
      }
   }
}
