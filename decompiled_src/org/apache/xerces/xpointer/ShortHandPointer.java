package org.apache.xerces.xpointer;

import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xs.AttributePSVI;

class ShortHandPointer implements XPointerPart {
   private String fShortHandPointer;
   private boolean fIsFragmentResolved = false;
   private SymbolTable fSymbolTable;
   int fMatchingChildCount = 0;

   public ShortHandPointer() {
   }

   public ShortHandPointer(SymbolTable var1) {
      this.fSymbolTable = var1;
   }

   public void parseXPointer(String var1) throws XNIException {
      this.fShortHandPointer = var1;
      this.fIsFragmentResolved = false;
   }

   public boolean resolveXPointer(QName var1, XMLAttributes var2, Augmentations var3, int var4) throws XNIException {
      if (this.fMatchingChildCount == 0) {
         this.fIsFragmentResolved = false;
      }

      if (var4 == 0) {
         if (this.fMatchingChildCount == 0) {
            this.fIsFragmentResolved = this.hasMatchingIdentifier(var1, var2, var3, var4);
         }

         if (this.fIsFragmentResolved) {
            ++this.fMatchingChildCount;
         }
      } else if (var4 == 2) {
         if (this.fMatchingChildCount == 0) {
            this.fIsFragmentResolved = this.hasMatchingIdentifier(var1, var2, var3, var4);
         }
      } else if (this.fIsFragmentResolved) {
         --this.fMatchingChildCount;
      }

      return this.fIsFragmentResolved;
   }

   private boolean hasMatchingIdentifier(QName var1, XMLAttributes var2, Augmentations var3, int var4) throws XNIException {
      String var5 = null;
      if (var2 != null) {
         for(int var6 = 0; var6 < var2.getLength(); ++var6) {
            var5 = this.getSchemaDeterminedID(var2, var6);
            if (var5 != null) {
               break;
            }

            var5 = this.getChildrenSchemaDeterminedID(var2, var6);
            if (var5 != null) {
               break;
            }

            var5 = this.getDTDDeterminedID(var2, var6);
            if (var5 != null) {
               break;
            }
         }
      }

      return var5 != null && var5.equals(this.fShortHandPointer);
   }

   public String getDTDDeterminedID(XMLAttributes var1, int var2) throws XNIException {
      return var1.getType(var2).equals("ID") ? var1.getValue(var2) : null;
   }

   public String getSchemaDeterminedID(XMLAttributes var1, int var2) throws XNIException {
      Augmentations var3 = var1.getAugmentations(var2);
      AttributePSVI var4 = (AttributePSVI)var3.getItem("ATTRIBUTE_PSVI");
      if (var4 != null) {
         Object var5 = var4.getMemberTypeDefinition();
         if (var5 != null) {
            var5 = var4.getTypeDefinition();
         }

         if (var5 != null && ((XSSimpleType)var5).isIDType()) {
            return var4.getSchemaNormalizedValue();
         }
      }

      return null;
   }

   public String getChildrenSchemaDeterminedID(XMLAttributes var1, int var2) throws XNIException {
      return null;
   }

   public boolean isFragmentResolved() {
      return this.fIsFragmentResolved;
   }

   public boolean isChildFragmentResolved() {
      return this.fIsFragmentResolved & this.fMatchingChildCount > 0;
   }

   public String getSchemeName() {
      return this.fShortHandPointer;
   }

   public String getSchemeData() {
      return null;
   }

   public void setSchemeName(String var1) {
      this.fShortHandPointer = var1;
   }

   public void setSchemeData(String var1) {
   }
}
