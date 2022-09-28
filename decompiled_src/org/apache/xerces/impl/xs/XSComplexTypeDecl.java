package org.apache.xerces.impl.xs;

import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.xs.models.CMBuilder;
import org.apache.xerces.impl.xs.models.XSCMValidator;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSWildcard;
import org.w3c.dom.TypeInfo;

public class XSComplexTypeDecl implements XSComplexTypeDefinition, TypeInfo {
   String fName = null;
   String fTargetNamespace = null;
   XSTypeDefinition fBaseType = null;
   short fDerivedBy = 2;
   short fFinal = 0;
   short fBlock = 0;
   short fMiscFlags = 0;
   XSAttributeGroupDecl fAttrGrp = null;
   short fContentType = 0;
   XSSimpleType fXSSimpleType = null;
   XSParticleDecl fParticle = null;
   XSCMValidator fCMValidator = null;
   XSObjectListImpl fAnnotations = null;
   static final int DERIVATION_ANY = 0;
   static final int DERIVATION_RESTRICTION = 1;
   static final int DERIVATION_EXTENSION = 2;
   static final int DERIVATION_UNION = 4;
   static final int DERIVATION_LIST = 8;
   private static final short CT_IS_ABSTRACT = 1;
   private static final short CT_HAS_TYPE_ID = 2;
   private static final short CT_IS_ANONYMOUS = 4;

   public void setValues(String var1, String var2, XSTypeDefinition var3, short var4, short var5, short var6, short var7, boolean var8, XSAttributeGroupDecl var9, XSSimpleType var10, XSParticleDecl var11, XSObjectListImpl var12) {
      this.fTargetNamespace = var2;
      this.fBaseType = var3;
      this.fDerivedBy = var4;
      this.fFinal = var5;
      this.fBlock = var6;
      this.fContentType = var7;
      if (var8) {
         this.fMiscFlags = (short)(this.fMiscFlags | 1);
      }

      this.fAttrGrp = var9;
      this.fXSSimpleType = var10;
      this.fParticle = var11;
      this.fAnnotations = var12;
   }

   public void setName(String var1) {
      this.fName = var1;
   }

   public short getTypeCategory() {
      return 15;
   }

   public String getTypeName() {
      return this.fName;
   }

   public short getFinalSet() {
      return this.fFinal;
   }

   public String getTargetNamespace() {
      return this.fTargetNamespace;
   }

   public boolean containsTypeID() {
      return (this.fMiscFlags & 2) != 0;
   }

   public void setIsAbstractType() {
      this.fMiscFlags = (short)(this.fMiscFlags | 1);
   }

   public void setContainsTypeID() {
      this.fMiscFlags = (short)(this.fMiscFlags | 2);
   }

   public void setIsAnonymous() {
      this.fMiscFlags = (short)(this.fMiscFlags | 4);
   }

   public synchronized XSCMValidator getContentModel(CMBuilder var1) {
      if (this.fCMValidator == null) {
         this.fCMValidator = var1.getContentModel(this);
      }

      return this.fCMValidator;
   }

   public XSAttributeGroupDecl getAttrGrp() {
      return this.fAttrGrp;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      this.appendTypeInfo(var1);
      return var1.toString();
   }

   void appendTypeInfo(StringBuffer var1) {
      String[] var2 = new String[]{"EMPTY", "SIMPLE", "ELEMENT", "MIXED"};
      String[] var3 = new String[]{"EMPTY", "EXTENSION", "RESTRICTION"};
      var1.append("Complex type name='" + this.fTargetNamespace + "," + this.getTypeName() + "', ");
      if (this.fBaseType != null) {
         var1.append(" base type name='" + this.fBaseType.getName() + "', ");
      }

      var1.append(" content type='" + var2[this.fContentType] + "', ");
      var1.append(" isAbstract='" + this.getAbstract() + "', ");
      var1.append(" hasTypeId='" + this.containsTypeID() + "', ");
      var1.append(" final='" + this.fFinal + "', ");
      var1.append(" block='" + this.fBlock + "', ");
      if (this.fParticle != null) {
         var1.append(" particle='" + this.fParticle.toString() + "', ");
      }

      var1.append(" derivedBy='" + var3[this.fDerivedBy] + "'. ");
   }

   public boolean derivedFromType(XSTypeDefinition var1, short var2) {
      if (var1 == null) {
         return false;
      } else if (var1 == SchemaGrammar.fAnyType) {
         return true;
      } else {
         Object var3;
         for(var3 = this; var3 != var1 && var3 != SchemaGrammar.fAnySimpleType && var3 != SchemaGrammar.fAnyType; var3 = ((XSTypeDefinition)var3).getBaseType()) {
         }

         return var3 == var1;
      }
   }

   public boolean derivedFrom(String var1, String var2, short var3) {
      if (var2 == null) {
         return false;
      } else if (var1 != null && var1.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && var2.equals("anyType")) {
         return true;
      } else {
         Object var4;
         for(var4 = this; (!var2.equals(((XSObject)var4).getName()) || (var1 != null || ((XSObject)var4).getNamespace() != null) && (var1 == null || !var1.equals(((XSObject)var4).getNamespace()))) && var4 != SchemaGrammar.fAnySimpleType && var4 != SchemaGrammar.fAnyType; var4 = ((XSTypeDefinition)var4).getBaseType()) {
         }

         return var4 != SchemaGrammar.fAnySimpleType && var4 != SchemaGrammar.fAnyType;
      }
   }

   public boolean isDOMDerivedFrom(String var1, String var2, int var3) {
      if (var2 == null) {
         return false;
      } else if (var1 != null && var1.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && var2.equals("anyType") && var3 == 1 && var3 == 2) {
         return true;
      } else if ((var3 & 1) != 0 && this.isDerivedByRestriction(var1, var2, var3, this)) {
         return true;
      } else if ((var3 & 2) != 0 && this.isDerivedByExtension(var1, var2, var3, this)) {
         return true;
      } else {
         if (((var3 & 8) != 0 || (var3 & 4) != 0) && (var3 & 1) == 0 && (var3 & 2) == 0) {
            if (var1.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && var2.equals("anyType")) {
               var2 = "anySimpleType";
            }

            if (!this.fName.equals("anyType") || !this.fTargetNamespace.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
               if (this.fBaseType != null && this.fBaseType instanceof XSSimpleTypeDecl) {
                  return ((XSSimpleTypeDecl)this.fBaseType).isDOMDerivedFrom(var1, var2, var3);
               }

               if (this.fBaseType != null && this.fBaseType instanceof XSComplexTypeDecl) {
                  return ((XSComplexTypeDecl)this.fBaseType).isDOMDerivedFrom(var1, var2, var3);
               }
            }
         }

         return (var3 & 2) == 0 && (var3 & 1) == 0 && (var3 & 8) == 0 && (var3 & 4) == 0 ? this.isDerivedByAny(var1, var2, var3, this) : false;
      }
   }

   private boolean isDerivedByAny(String var1, String var2, int var3, XSTypeDefinition var4) {
      XSTypeDefinition var5 = null;

      boolean var6;
      for(var6 = false; var4 != null && var4 != var5; var4 = var4.getBaseType()) {
         if (var2.equals(var4.getName()) && (var1 == null && var4.getNamespace() == null || var1 != null && var1.equals(var4.getNamespace()))) {
            var6 = true;
            break;
         }

         if (this.isDerivedByRestriction(var1, var2, var3, var4)) {
            return true;
         }

         if (!this.isDerivedByExtension(var1, var2, var3, var4)) {
            return true;
         }

         var5 = var4;
      }

      return var6;
   }

   private boolean isDerivedByRestriction(String var1, String var2, int var3, XSTypeDefinition var4) {
      for(XSTypeDefinition var5 = null; var4 != null && var4 != var5; var4 = var4.getBaseType()) {
         if (var1 != null && var1.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && var2.equals("anySimpleType")) {
            return false;
         }

         if (var2.equals(var4.getName()) && var1 != null && var1.equals(var4.getNamespace()) || var4.getNamespace() == null && var1 == null) {
            return true;
         }

         if (var4 instanceof XSSimpleTypeDecl) {
            if (var1.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && var2.equals("anyType")) {
               var2 = "anySimpleType";
            }

            return ((XSSimpleTypeDecl)var4).isDOMDerivedFrom(var1, var2, var3);
         }

         if (((XSComplexTypeDecl)var4).getDerivationMethod() != 2) {
            return false;
         }

         var5 = var4;
      }

      return false;
   }

   private boolean isDerivedByExtension(String var1, String var2, int var3, XSTypeDefinition var4) {
      boolean var5 = false;

      for(XSTypeDefinition var6 = null; var4 != null && var4 != var6 && (var1 == null || !var1.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) || !var2.equals("anySimpleType") || !SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(var4.getNamespace()) || !"anyType".equals(var4.getName())); var4 = var4.getBaseType()) {
         if (var2.equals(var4.getName()) && (var1 == null && var4.getNamespace() == null || var1 != null && var1.equals(var4.getNamespace()))) {
            return var5;
         }

         if (var4 instanceof XSSimpleTypeDecl) {
            if (var1.equals(SchemaSymbols.URI_SCHEMAFORSCHEMA) && var2.equals("anyType")) {
               var2 = "anySimpleType";
            }

            if ((var3 & 2) != 0) {
               return var5 & ((XSSimpleTypeDecl)var4).isDOMDerivedFrom(var1, var2, var3 & 1);
            }

            return var5 & ((XSSimpleTypeDecl)var4).isDOMDerivedFrom(var1, var2, var3);
         }

         if (((XSComplexTypeDecl)var4).getDerivationMethod() == 1) {
            var5 |= true;
         }

         var6 = var4;
      }

      return false;
   }

   public void reset() {
      this.fName = null;
      this.fTargetNamespace = null;
      this.fBaseType = null;
      this.fDerivedBy = 2;
      this.fFinal = 0;
      this.fBlock = 0;
      this.fMiscFlags = 0;
      this.fAttrGrp.reset();
      this.fContentType = 0;
      this.fXSSimpleType = null;
      this.fParticle = null;
      this.fCMValidator = null;
      if (this.fAnnotations != null) {
         this.fAnnotations.clear();
      }

      this.fAnnotations = null;
   }

   public short getType() {
      return 3;
   }

   public String getName() {
      return this.getAnonymous() ? null : this.fName;
   }

   public boolean getAnonymous() {
      return (this.fMiscFlags & 4) != 0;
   }

   public String getNamespace() {
      return this.fTargetNamespace;
   }

   public XSTypeDefinition getBaseType() {
      return this.fBaseType;
   }

   public short getDerivationMethod() {
      return this.fDerivedBy;
   }

   public boolean isFinal(short var1) {
      return (this.fFinal & var1) != 0;
   }

   public short getFinal() {
      return this.fFinal;
   }

   public boolean getAbstract() {
      return (this.fMiscFlags & 1) != 0;
   }

   public XSObjectList getAttributeUses() {
      return this.fAttrGrp.getAttributeUses();
   }

   public XSWildcard getAttributeWildcard() {
      return this.fAttrGrp.getAttributeWildcard();
   }

   public short getContentType() {
      return this.fContentType;
   }

   public XSSimpleTypeDefinition getSimpleType() {
      return this.fXSSimpleType;
   }

   public XSParticle getParticle() {
      return this.fParticle;
   }

   public boolean isProhibitedSubstitution(short var1) {
      return (this.fBlock & var1) != 0;
   }

   public short getProhibitedSubstitutions() {
      return this.fBlock;
   }

   public XSObjectList getAnnotations() {
      return this.fAnnotations;
   }

   public XSNamespaceItem getNamespaceItem() {
      return null;
   }

   public XSAttributeUse getAttributeUse(String var1, String var2) {
      return this.fAttrGrp.getAttributeUse(var1, var2);
   }

   public String getTypeNamespace() {
      return this.getNamespace();
   }

   public boolean isDerivedFrom(String var1, String var2, int var3) {
      return this.isDOMDerivedFrom(var1, var2, var3);
   }
}
