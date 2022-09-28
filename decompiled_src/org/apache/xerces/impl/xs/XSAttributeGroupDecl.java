package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSWildcard;

public class XSAttributeGroupDecl implements XSAttributeGroupDefinition {
   public String fName = null;
   public String fTargetNamespace = null;
   int fAttrUseNum = 0;
   private static final int INITIAL_SIZE = 5;
   XSAttributeUseImpl[] fAttributeUses = new XSAttributeUseImpl[5];
   public XSWildcardDecl fAttributeWC = null;
   public String fIDAttrName = null;
   public XSAnnotationImpl fAnnotation;
   protected XSObjectListImpl fAttrUses = null;

   public String addAttributeUse(XSAttributeUseImpl var1) {
      if (this.fAttrUseNum == this.fAttributeUses.length) {
         this.fAttributeUses = resize(this.fAttributeUses, this.fAttrUseNum * 2);
      }

      this.fAttributeUses[this.fAttrUseNum++] = var1;
      if (var1.fUse == 2) {
         return null;
      } else {
         if (var1.fAttrDecl.fType.isIDType()) {
            if (this.fIDAttrName != null) {
               return this.fIDAttrName;
            }

            this.fIDAttrName = var1.fAttrDecl.fName;
         }

         return null;
      }
   }

   public XSAttributeUse getAttributeUse(String var1, String var2) {
      for(int var3 = 0; var3 < this.fAttrUseNum; ++var3) {
         if (this.fAttributeUses[var3].fAttrDecl.fTargetNamespace == var1 && this.fAttributeUses[var3].fAttrDecl.fName == var2) {
            return this.fAttributeUses[var3];
         }
      }

      return null;
   }

   public void removeProhibitedAttrs() {
      if (this.fAttrUseNum != 0) {
         int var1 = 0;
         XSAttributeUseImpl[] var2 = new XSAttributeUseImpl[this.fAttrUseNum];

         for(int var3 = 0; var3 < this.fAttrUseNum; ++var3) {
            if (this.fAttributeUses[var3].fUse == 2) {
               ++var1;
               var2[this.fAttrUseNum - var1] = this.fAttributeUses[var3];
            }
         }

         int var4 = 0;
         if (var1 > 0) {
            label44:
            for(int var5 = 0; var5 < this.fAttrUseNum; ++var5) {
               if (this.fAttributeUses[var5].fUse != 2) {
                  for(int var6 = 1; var6 <= var1; ++var6) {
                     if (this.fAttributeUses[var5].fAttrDecl.fName == var2[this.fAttrUseNum - var1].fAttrDecl.fName && this.fAttributeUses[var5].fAttrDecl.fTargetNamespace == var2[this.fAttrUseNum - var1].fAttrDecl.fTargetNamespace) {
                        continue label44;
                     }
                  }

                  var2[var4++] = this.fAttributeUses[var5];
               }
            }

            this.fAttributeUses = var2;
            this.fAttrUseNum = var4;
         }

      }
   }

   public Object[] validRestrictionOf(String var1, XSAttributeGroupDecl var2) {
      Object[] var3 = null;
      XSAttributeUseImpl var4 = null;
      XSAttributeDecl var5 = null;
      XSAttributeUseImpl var6 = null;
      XSAttributeDecl var7 = null;

      int var9;
      for(int var8 = 0; var8 < this.fAttrUseNum; ++var8) {
         var4 = this.fAttributeUses[var8];
         var5 = var4.fAttrDecl;
         var6 = (XSAttributeUseImpl)var2.getAttributeUse(var5.fTargetNamespace, var5.fName);
         if (var6 != null) {
            if (var6.getRequired() && !var4.getRequired()) {
               var3 = new Object[]{var1, var5.fName, var4.fUse == 0 ? "optional" : "prohibited", "derivation-ok-restriction.2.1.1"};
               return var3;
            }

            if (var4.fUse != 2) {
               var7 = var6.fAttrDecl;
               if (!XSConstraints.checkSimpleDerivationOk(var5.fType, var7.fType, var7.fType.getFinal())) {
                  var3 = new Object[]{var1, var5.fName, var5.fType.getName(), var7.fType.getName(), "derivation-ok-restriction.2.1.2"};
                  return var3;
               }

               var9 = var6.fConstraintType != 0 ? var6.fConstraintType : var7.getConstraintType();
               short var10 = var4.fConstraintType != 0 ? var4.fConstraintType : var5.getConstraintType();
               if (var9 == 2) {
                  if (var10 != 2) {
                     var3 = new Object[]{var1, var5.fName, "derivation-ok-restriction.2.1.3.a"};
                     return var3;
                  }

                  ValidatedInfo var11 = var6.fDefault != null ? var6.fDefault : var7.fDefault;
                  ValidatedInfo var12 = var4.fDefault != null ? var4.fDefault : var5.fDefault;
                  if (!var11.actualValue.equals(var12.actualValue)) {
                     var3 = new Object[]{var1, var5.fName, var12.stringValue(), var11.stringValue(), "derivation-ok-restriction.2.1.3.b"};
                     return var3;
                  }
               }
            }
         } else {
            if (var2.fAttributeWC == null) {
               var3 = new Object[]{var1, var5.fName, "derivation-ok-restriction.2.2.a"};
               return var3;
            }

            if (!var2.fAttributeWC.allowNamespace(var5.fTargetNamespace)) {
               var3 = new Object[]{var1, var5.fName, var5.fTargetNamespace == null ? "" : var5.fTargetNamespace, "derivation-ok-restriction.2.2.b"};
               return var3;
            }
         }
      }

      for(var9 = 0; var9 < var2.fAttrUseNum; ++var9) {
         var6 = var2.fAttributeUses[var9];
         if (var6.fUse == 1) {
            var7 = var6.fAttrDecl;
            if (this.getAttributeUse(var7.fTargetNamespace, var7.fName) == null) {
               var3 = new Object[]{var1, var6.fAttrDecl.fName, "derivation-ok-restriction.3"};
               return var3;
            }
         }
      }

      if (this.fAttributeWC != null) {
         if (var2.fAttributeWC == null) {
            var3 = new Object[]{var1, "derivation-ok-restriction.4.1"};
            return var3;
         }

         if (!this.fAttributeWC.isSubsetOf(var2.fAttributeWC)) {
            var3 = new Object[]{var1, "derivation-ok-restriction.4.2"};
            return var3;
         }

         if (this.fAttributeWC.weakerProcessContents(var2.fAttributeWC)) {
            var3 = new Object[]{var1, this.fAttributeWC.getProcessContentsAsString(), var2.fAttributeWC.getProcessContentsAsString(), "derivation-ok-restriction.4.3"};
            return var3;
         }
      }

      return null;
   }

   static final XSAttributeUseImpl[] resize(XSAttributeUseImpl[] var0, int var1) {
      XSAttributeUseImpl[] var2 = new XSAttributeUseImpl[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public void reset() {
      this.fName = null;
      this.fTargetNamespace = null;

      for(int var1 = 0; var1 < this.fAttrUseNum; ++var1) {
         this.fAttributeUses[var1] = null;
      }

      this.fAttrUseNum = 0;
      this.fAttributeWC = null;
      this.fAnnotation = null;
      this.fIDAttrName = null;
   }

   public short getType() {
      return 5;
   }

   public String getName() {
      return this.fName;
   }

   public String getNamespace() {
      return this.fTargetNamespace;
   }

   public XSObjectList getAttributeUses() {
      if (this.fAttrUses == null) {
         this.fAttrUses = new XSObjectListImpl(this.fAttributeUses, this.fAttrUseNum);
      }

      return this.fAttrUses;
   }

   public XSWildcard getAttributeWildcard() {
      return this.fAttributeWC;
   }

   public XSAnnotation getAnnotation() {
      return this.fAnnotation;
   }

   public XSNamespaceItem getNamespaceItem() {
      return null;
   }
}
