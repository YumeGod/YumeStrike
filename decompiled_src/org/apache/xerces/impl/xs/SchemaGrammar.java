package org.apache.xerces.impl.xs;

import java.util.Vector;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.identity.IdentityConstraint;
import org.apache.xerces.impl.xs.util.SimpleLocator;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSNamedMap4Types;
import org.apache.xerces.impl.xs.util.XSNamedMapImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.parsers.IntegratedParserConfiguration;
import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroupDefinition;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSNotationDeclaration;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSWildcard;

public class SchemaGrammar implements XSGrammar, XSNamespaceItem {
   String fTargetNamespace;
   SymbolHash fGlobalAttrDecls;
   SymbolHash fGlobalAttrGrpDecls;
   SymbolHash fGlobalElemDecls;
   SymbolHash fGlobalGroupDecls;
   SymbolHash fGlobalNotationDecls;
   SymbolHash fGlobalIDConstraintDecls;
   SymbolHash fGlobalTypeDecls;
   XSDDescription fGrammarDescription = null;
   XSAnnotationImpl[] fAnnotations = null;
   int fNumAnnotations;
   private SymbolTable fSymbolTable = null;
   private SAXParser fSAXParser = null;
   private DOMParser fDOMParser = null;
   private static final int BASICSET_COUNT = 29;
   private static final int FULLSET_COUNT = 46;
   private static final int GRAMMAR_XS = 1;
   private static final int GRAMMAR_XSI = 2;
   Vector fImported = null;
   private static final int INITIAL_SIZE = 16;
   private static final int INC_SIZE = 16;
   private int fCTCount = 0;
   private XSComplexTypeDecl[] fComplexTypeDecls = new XSComplexTypeDecl[16];
   private SimpleLocator[] fCTLocators = new SimpleLocator[16];
   private static final int REDEFINED_GROUP_INIT_SIZE = 2;
   private int fRGCount = 0;
   private XSGroupDecl[] fRedefinedGroupDecls = new XSGroupDecl[2];
   private SimpleLocator[] fRGLocators = new SimpleLocator[1];
   boolean fFullChecked = false;
   private int fSubGroupCount = 0;
   private XSElementDecl[] fSubGroups = new XSElementDecl[16];
   public static final XSComplexTypeDecl fAnyType = new XSAnyType();
   public static final BuiltinSchemaGrammar SG_SchemaNS = new BuiltinSchemaGrammar(1);
   public static final Schema4Annotations SG_Schema4Annotations = new Schema4Annotations();
   public static final XSSimpleType fAnySimpleType;
   public static final BuiltinSchemaGrammar SG_XSI;
   private static final short MAX_COMP_IDX = 16;
   private static final boolean[] GLOBAL_COMP;
   private XSNamedMap[] fComponents = null;
   private Vector fDocuments = null;
   private Vector fLocations = null;

   protected SchemaGrammar() {
   }

   public SchemaGrammar(String var1, XSDDescription var2, SymbolTable var3) {
      this.fTargetNamespace = var1;
      this.fGrammarDescription = var2;
      this.fSymbolTable = var3;
      this.fGlobalAttrDecls = new SymbolHash();
      this.fGlobalAttrGrpDecls = new SymbolHash();
      this.fGlobalElemDecls = new SymbolHash();
      this.fGlobalGroupDecls = new SymbolHash();
      this.fGlobalNotationDecls = new SymbolHash();
      this.fGlobalIDConstraintDecls = new SymbolHash();
      if (this.fTargetNamespace == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
         this.fGlobalTypeDecls = SG_SchemaNS.fGlobalTypeDecls.makeClone();
      } else {
         this.fGlobalTypeDecls = new SymbolHash();
      }

   }

   public XMLGrammarDescription getGrammarDescription() {
      return this.fGrammarDescription;
   }

   public boolean isNamespaceAware() {
      return true;
   }

   public void setImportedGrammars(Vector var1) {
      this.fImported = var1;
   }

   public Vector getImportedGrammars() {
      return this.fImported;
   }

   public final String getTargetNamespace() {
      return this.fTargetNamespace;
   }

   public void addGlobalAttributeDecl(XSAttributeDecl var1) {
      this.fGlobalAttrDecls.put(var1.fName, var1);
   }

   public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl var1) {
      this.fGlobalAttrGrpDecls.put(var1.fName, var1);
   }

   public void addGlobalElementDecl(XSElementDecl var1) {
      this.fGlobalElemDecls.put(var1.fName, var1);
      if (var1.fSubGroup != null) {
         if (this.fSubGroupCount == this.fSubGroups.length) {
            this.fSubGroups = resize(this.fSubGroups, this.fSubGroupCount + 16);
         }

         this.fSubGroups[this.fSubGroupCount++] = var1;
      }

   }

   public void addGlobalGroupDecl(XSGroupDecl var1) {
      this.fGlobalGroupDecls.put(var1.fName, var1);
   }

   public void addGlobalNotationDecl(XSNotationDecl var1) {
      this.fGlobalNotationDecls.put(var1.fName, var1);
   }

   public void addGlobalTypeDecl(XSTypeDefinition var1) {
      this.fGlobalTypeDecls.put(var1.getName(), var1);
   }

   public final void addIDConstraintDecl(XSElementDecl var1, IdentityConstraint var2) {
      var1.addIDConstraint(var2);
      this.fGlobalIDConstraintDecls.put(var2.getIdentityConstraintName(), var2);
   }

   public final XSAttributeDecl getGlobalAttributeDecl(String var1) {
      return (XSAttributeDecl)this.fGlobalAttrDecls.get(var1);
   }

   public final XSAttributeGroupDecl getGlobalAttributeGroupDecl(String var1) {
      return (XSAttributeGroupDecl)this.fGlobalAttrGrpDecls.get(var1);
   }

   public final XSElementDecl getGlobalElementDecl(String var1) {
      return (XSElementDecl)this.fGlobalElemDecls.get(var1);
   }

   public final XSGroupDecl getGlobalGroupDecl(String var1) {
      return (XSGroupDecl)this.fGlobalGroupDecls.get(var1);
   }

   public final XSNotationDecl getGlobalNotationDecl(String var1) {
      return (XSNotationDecl)this.fGlobalNotationDecls.get(var1);
   }

   public final XSTypeDefinition getGlobalTypeDecl(String var1) {
      return (XSTypeDefinition)this.fGlobalTypeDecls.get(var1);
   }

   public final IdentityConstraint getIDConstraintDecl(String var1) {
      return (IdentityConstraint)this.fGlobalIDConstraintDecls.get(var1);
   }

   public final boolean hasIDConstraints() {
      return this.fGlobalIDConstraintDecls.getLength() > 0;
   }

   public void addComplexTypeDecl(XSComplexTypeDecl var1, SimpleLocator var2) {
      if (this.fCTCount == this.fComplexTypeDecls.length) {
         this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount + 16);
         this.fCTLocators = resize(this.fCTLocators, this.fCTCount + 16);
      }

      this.fCTLocators[this.fCTCount] = var2;
      this.fComplexTypeDecls[this.fCTCount++] = var1;
   }

   public void addRedefinedGroupDecl(XSGroupDecl var1, XSGroupDecl var2, SimpleLocator var3) {
      if (this.fRGCount == this.fRedefinedGroupDecls.length) {
         this.fRedefinedGroupDecls = resize(this.fRedefinedGroupDecls, this.fRGCount << 1);
         this.fRGLocators = resize(this.fRGLocators, this.fRGCount);
      }

      this.fRGLocators[this.fRGCount / 2] = var3;
      this.fRedefinedGroupDecls[this.fRGCount++] = var1;
      this.fRedefinedGroupDecls[this.fRGCount++] = var2;
   }

   final XSComplexTypeDecl[] getUncheckedComplexTypeDecls() {
      if (this.fCTCount < this.fComplexTypeDecls.length) {
         this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount);
         this.fCTLocators = resize(this.fCTLocators, this.fCTCount);
      }

      return this.fComplexTypeDecls;
   }

   final SimpleLocator[] getUncheckedCTLocators() {
      if (this.fCTCount < this.fCTLocators.length) {
         this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount);
         this.fCTLocators = resize(this.fCTLocators, this.fCTCount);
      }

      return this.fCTLocators;
   }

   final XSGroupDecl[] getRedefinedGroupDecls() {
      if (this.fRGCount < this.fRedefinedGroupDecls.length) {
         this.fRedefinedGroupDecls = resize(this.fRedefinedGroupDecls, this.fRGCount);
         this.fRGLocators = resize(this.fRGLocators, this.fRGCount / 2);
      }

      return this.fRedefinedGroupDecls;
   }

   final SimpleLocator[] getRGLocators() {
      if (this.fRGCount < this.fRedefinedGroupDecls.length) {
         this.fRedefinedGroupDecls = resize(this.fRedefinedGroupDecls, this.fRGCount);
         this.fRGLocators = resize(this.fRGLocators, this.fRGCount / 2);
      }

      return this.fRGLocators;
   }

   final void setUncheckedTypeNum(int var1) {
      this.fCTCount = var1;
      this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount);
      this.fCTLocators = resize(this.fCTLocators, this.fCTCount);
   }

   final XSElementDecl[] getSubstitutionGroups() {
      if (this.fSubGroupCount < this.fSubGroups.length) {
         this.fSubGroups = resize(this.fSubGroups, this.fSubGroupCount);
      }

      return this.fSubGroups;
   }

   static final XSComplexTypeDecl[] resize(XSComplexTypeDecl[] var0, int var1) {
      XSComplexTypeDecl[] var2 = new XSComplexTypeDecl[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   static final XSGroupDecl[] resize(XSGroupDecl[] var0, int var1) {
      XSGroupDecl[] var2 = new XSGroupDecl[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   static final XSElementDecl[] resize(XSElementDecl[] var0, int var1) {
      XSElementDecl[] var2 = new XSElementDecl[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   static final SimpleLocator[] resize(SimpleLocator[] var0, int var1) {
      SimpleLocator[] var2 = new SimpleLocator[var1];
      System.arraycopy(var0, 0, var2, 0, Math.min(var0.length, var1));
      return var2;
   }

   public synchronized void addDocument(Object var1, String var2) {
      if (this.fDocuments == null) {
         this.fDocuments = new Vector();
         this.fLocations = new Vector();
      }

      this.fDocuments.addElement(var1);
      this.fLocations.addElement(var2);
   }

   public String getSchemaNamespace() {
      return this.fTargetNamespace;
   }

   synchronized DOMParser getDOMParser() {
      if (this.fDOMParser != null) {
         return this.fDOMParser;
      } else {
         IntegratedParserConfiguration var1 = new IntegratedParserConfiguration(this.fSymbolTable);
         var1.setFeature("http://xml.org/sax/features/namespaces", true);
         var1.setFeature("http://xml.org/sax/features/validation", false);
         this.fDOMParser = new DOMParser(var1);
         return this.fDOMParser;
      }
   }

   synchronized SAXParser getSAXParser() {
      if (this.fSAXParser != null) {
         return this.fSAXParser;
      } else {
         IntegratedParserConfiguration var1 = new IntegratedParserConfiguration(this.fSymbolTable);
         var1.setFeature("http://xml.org/sax/features/namespaces", true);
         var1.setFeature("http://xml.org/sax/features/validation", false);
         this.fSAXParser = new SAXParser(var1);
         return this.fSAXParser;
      }
   }

   public synchronized XSNamedMap getComponents(short var1) {
      if (var1 > 0 && var1 <= 16 && GLOBAL_COMP[var1]) {
         if (this.fComponents == null) {
            this.fComponents = new XSNamedMap[17];
         }

         if (this.fComponents[var1] == null) {
            SymbolHash var2 = null;
            switch (var1) {
               case 1:
                  var2 = this.fGlobalAttrDecls;
                  break;
               case 2:
                  var2 = this.fGlobalElemDecls;
                  break;
               case 3:
               case 15:
               case 16:
                  var2 = this.fGlobalTypeDecls;
               case 4:
               case 7:
               case 8:
               case 9:
               case 10:
               case 12:
               case 13:
               case 14:
               default:
                  break;
               case 5:
                  var2 = this.fGlobalAttrGrpDecls;
                  break;
               case 6:
                  var2 = this.fGlobalGroupDecls;
                  break;
               case 11:
                  var2 = this.fGlobalNotationDecls;
            }

            if (var1 != 15 && var1 != 16) {
               this.fComponents[var1] = new XSNamedMapImpl(this.fTargetNamespace, var2);
            } else {
               this.fComponents[var1] = new XSNamedMap4Types(this.fTargetNamespace, var2, var1);
            }
         }

         return this.fComponents[var1];
      } else {
         return XSNamedMapImpl.EMPTY_MAP;
      }
   }

   public XSTypeDefinition getTypeDefinition(String var1) {
      return this.getGlobalTypeDecl(var1);
   }

   public XSAttributeDeclaration getAttributeDeclaration(String var1) {
      return this.getGlobalAttributeDecl(var1);
   }

   public XSElementDeclaration getElementDeclaration(String var1) {
      return this.getGlobalElementDecl(var1);
   }

   public XSAttributeGroupDefinition getAttributeGroup(String var1) {
      return this.getGlobalAttributeGroupDecl(var1);
   }

   public XSModelGroupDefinition getModelGroupDefinition(String var1) {
      return this.getGlobalGroupDecl(var1);
   }

   public XSNotationDeclaration getNotationDeclaration(String var1) {
      return this.getGlobalNotationDecl(var1);
   }

   public StringList getDocumentLocations() {
      return new StringListImpl(this.fLocations);
   }

   public XSModel toXSModel() {
      return new XSModelImpl(new SchemaGrammar[]{this});
   }

   public XSModel toXSModel(XSGrammar[] var1) {
      if (var1 != null && var1.length != 0) {
         int var2 = var1.length;
         boolean var3 = false;

         for(int var4 = 0; var4 < var2; ++var4) {
            if (var1[var4] == this) {
               var3 = true;
               break;
            }
         }

         SchemaGrammar[] var5 = new SchemaGrammar[var3 ? var2 : var2 + 1];

         for(int var6 = 0; var6 < var2; ++var6) {
            var5[var6] = (SchemaGrammar)var1[var6];
         }

         if (!var3) {
            var5[var2] = this;
         }

         return new XSModelImpl(var5);
      } else {
         return this.toXSModel();
      }
   }

   public XSObjectList getAnnotations() {
      return new XSObjectListImpl(this.fAnnotations, this.fNumAnnotations);
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

   static {
      fAnySimpleType = (XSSimpleType)SG_SchemaNS.getGlobalTypeDecl("anySimpleType");
      SG_XSI = new BuiltinSchemaGrammar(2);
      GLOBAL_COMP = new boolean[]{false, true, true, true, false, true, true, false, false, false, false, true, false, false, false, true, true};
   }

   private static class BuiltinAttrDecl extends XSAttributeDecl {
      public BuiltinAttrDecl(String var1, String var2, XSSimpleType var3, short var4) {
         super.fName = var1;
         super.fTargetNamespace = var2;
         super.fType = var3;
         super.fScope = var4;
      }

      public void setValues(String var1, String var2, XSSimpleType var3, short var4, short var5, ValidatedInfo var6, XSComplexTypeDecl var7) {
      }

      public void reset() {
      }

      public XSAnnotation getAnnotation() {
         return null;
      }
   }

   private static class XSAnyType extends XSComplexTypeDecl {
      public XSAnyType() {
         super.fName = "anyType";
         super.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
         super.fBaseType = this;
         super.fDerivedBy = 2;
         super.fContentType = 3;
         super.fParticle = null;
         super.fAttrGrp = null;
      }

      public void setValues(String var1, String var2, XSTypeDefinition var3, short var4, short var5, short var6, short var7, boolean var8, XSAttributeGroupDecl var9, XSSimpleType var10, XSParticleDecl var11) {
      }

      public void setName(String var1) {
      }

      public void setIsAbstractType() {
      }

      public void setContainsTypeID() {
      }

      public void setIsAnonymous() {
      }

      public void reset() {
      }

      public XSObjectList getAttributeUses() {
         return new XSObjectListImpl((XSObject[])null, 0);
      }

      public XSAttributeGroupDecl getAttrGrp() {
         XSWildcardDecl var1 = new XSWildcardDecl();
         var1.fProcessContents = 3;
         XSAttributeGroupDecl var2 = new XSAttributeGroupDecl();
         var2.fAttributeWC = var1;
         return var2;
      }

      public XSWildcard getAttributeWildcard() {
         XSWildcardDecl var1 = new XSWildcardDecl();
         var1.fProcessContents = 3;
         return var1;
      }

      public XSParticle getParticle() {
         XSWildcardDecl var1 = new XSWildcardDecl();
         var1.fProcessContents = 3;
         XSParticleDecl var2 = new XSParticleDecl();
         var2.fMinOccurs = 0;
         var2.fMaxOccurs = -1;
         var2.fType = 2;
         var2.fValue = var1;
         XSModelGroupImpl var3 = new XSModelGroupImpl();
         var3.fCompositor = 102;
         var3.fParticleCount = 1;
         var3.fParticles = new XSParticleDecl[1];
         var3.fParticles[0] = var2;
         XSParticleDecl var4 = new XSParticleDecl();
         var4.fType = 3;
         var4.fValue = var3;
         return var4;
      }

      public XSObjectList getAnnotations() {
         return null;
      }
   }

   public static final class Schema4Annotations extends SchemaGrammar {
      public Schema4Annotations() {
         super.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
         super.fGrammarDescription = new XSDDescription();
         super.fGrammarDescription.fContextType = 3;
         super.fGrammarDescription.setNamespace(SchemaSymbols.URI_SCHEMAFORSCHEMA);
         super.fGlobalAttrDecls = new SymbolHash(1);
         super.fGlobalAttrGrpDecls = new SymbolHash(1);
         super.fGlobalElemDecls = new SymbolHash(6);
         super.fGlobalGroupDecls = new SymbolHash(1);
         super.fGlobalNotationDecls = new SymbolHash(1);
         super.fGlobalIDConstraintDecls = new SymbolHash(1);
         super.fGlobalTypeDecls = SchemaGrammar.SG_SchemaNS.fGlobalTypeDecls;
         XSElementDecl var1 = this.createAnnotationElementDecl(SchemaSymbols.ELT_ANNOTATION);
         XSElementDecl var2 = this.createAnnotationElementDecl(SchemaSymbols.ELT_DOCUMENTATION);
         XSElementDecl var3 = this.createAnnotationElementDecl(SchemaSymbols.ELT_APPINFO);
         super.fGlobalElemDecls.put(var1.fName, var1);
         super.fGlobalElemDecls.put(var2.fName, var2);
         super.fGlobalElemDecls.put(var3.fName, var3);
         XSComplexTypeDecl var4 = new XSComplexTypeDecl();
         XSComplexTypeDecl var5 = new XSComplexTypeDecl();
         XSComplexTypeDecl var6 = new XSComplexTypeDecl();
         var1.fType = var4;
         var2.fType = var5;
         var3.fType = var6;
         XSAttributeGroupDecl var7 = new XSAttributeGroupDecl();
         XSAttributeGroupDecl var8 = new XSAttributeGroupDecl();
         XSAttributeGroupDecl var9 = new XSAttributeGroupDecl();
         XSAttributeUseImpl var10 = new XSAttributeUseImpl();
         var10.fAttrDecl = new XSAttributeDecl();
         var10.fAttrDecl.setValues(SchemaSymbols.ATT_ID, (String)null, (XSSimpleType)super.fGlobalTypeDecls.get("ID"), (short)0, (short)2, (ValidatedInfo)null, var4, (XSAnnotationImpl)null);
         var10.fUse = 0;
         var10.fConstraintType = 0;
         XSAttributeUseImpl var11 = new XSAttributeUseImpl();
         var11.fAttrDecl = new XSAttributeDecl();
         var11.fAttrDecl.setValues(SchemaSymbols.ATT_SOURCE, (String)null, (XSSimpleType)super.fGlobalTypeDecls.get("anyURI"), (short)0, (short)2, (ValidatedInfo)null, var5, (XSAnnotationImpl)null);
         var11.fUse = 0;
         var11.fConstraintType = 0;
         XSAttributeUseImpl var12 = new XSAttributeUseImpl();
         var12.fAttrDecl = new XSAttributeDecl();
         var12.fAttrDecl.setValues("lang".intern(), NamespaceContext.XML_URI, (XSSimpleType)super.fGlobalTypeDecls.get("language"), (short)0, (short)2, (ValidatedInfo)null, var5, (XSAnnotationImpl)null);
         var12.fUse = 0;
         var12.fConstraintType = 0;
         XSAttributeUseImpl var13 = new XSAttributeUseImpl();
         var13.fAttrDecl = new XSAttributeDecl();
         var13.fAttrDecl.setValues(SchemaSymbols.ATT_SOURCE, (String)null, (XSSimpleType)super.fGlobalTypeDecls.get("anyURI"), (short)0, (short)2, (ValidatedInfo)null, var6, (XSAnnotationImpl)null);
         var13.fUse = 0;
         var13.fConstraintType = 0;
         XSWildcardDecl var14 = new XSWildcardDecl();
         var14.fNamespaceList = new String[]{super.fTargetNamespace, null};
         var14.fType = 2;
         var14.fProcessContents = 3;
         var7.addAttributeUse(var10);
         var7.fAttributeWC = var14;
         var8.addAttributeUse(var11);
         var8.addAttributeUse(var12);
         var8.fAttributeWC = var14;
         var9.addAttributeUse(var13);
         var9.fAttributeWC = var14;
         XSParticleDecl var15 = this.createUnboundedModelGroupParticle();
         XSModelGroupImpl var16 = new XSModelGroupImpl();
         var16.fCompositor = 101;
         var16.fParticleCount = 2;
         var16.fParticles = new XSParticleDecl[2];
         var16.fParticles[0] = this.createChoiceElementParticle(var3);
         var16.fParticles[1] = this.createChoiceElementParticle(var2);
         var15.fValue = var16;
         XSParticleDecl var17 = this.createUnboundedAnyWildcardSequenceParticle();
         var4.setValues("#AnonType_" + SchemaSymbols.ELT_ANNOTATION, super.fTargetNamespace, SchemaGrammar.fAnyType, (short)2, (short)0, (short)3, (short)2, false, var7, (XSSimpleType)null, var15, new XSObjectListImpl((XSObject[])null, 0));
         var4.setName("#AnonType_" + SchemaSymbols.ELT_ANNOTATION);
         var4.setIsAnonymous();
         var5.setValues("#AnonType_" + SchemaSymbols.ELT_DOCUMENTATION, super.fTargetNamespace, SchemaGrammar.fAnyType, (short)2, (short)0, (short)3, (short)3, false, var8, (XSSimpleType)null, var17, new XSObjectListImpl((XSObject[])null, 0));
         var5.setName("#AnonType_" + SchemaSymbols.ELT_DOCUMENTATION);
         var5.setIsAnonymous();
         var6.setValues("#AnonType_" + SchemaSymbols.ELT_APPINFO, super.fTargetNamespace, SchemaGrammar.fAnyType, (short)2, (short)0, (short)3, (short)3, false, var9, (XSSimpleType)null, var17, new XSObjectListImpl((XSObject[])null, 0));
         var6.setName("#AnonType_" + SchemaSymbols.ELT_APPINFO);
         var6.setIsAnonymous();
      }

      public XMLGrammarDescription getGrammarDescription() {
         return super.fGrammarDescription.makeClone();
      }

      public void setImportedGrammars(Vector var1) {
      }

      public void addGlobalAttributeDecl(XSAttributeDecl var1) {
      }

      public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl var1) {
      }

      public void addGlobalElementDecl(XSElementDecl var1) {
      }

      public void addGlobalGroupDecl(XSGroupDecl var1) {
      }

      public void addGlobalNotationDecl(XSNotationDecl var1) {
      }

      public void addGlobalTypeDecl(XSTypeDefinition var1) {
      }

      public void addComplexTypeDecl(XSComplexTypeDecl var1, SimpleLocator var2) {
      }

      public void addRedefinedGroupDecl(XSGroupDecl var1, XSGroupDecl var2, SimpleLocator var3) {
      }

      public synchronized void addDocument(Object var1, String var2) {
      }

      synchronized DOMParser getDOMParser() {
         return null;
      }

      synchronized SAXParser getSAXParser() {
         return null;
      }

      private XSElementDecl createAnnotationElementDecl(String var1) {
         XSElementDecl var2 = new XSElementDecl();
         var2.fName = var1;
         var2.fTargetNamespace = super.fTargetNamespace;
         var2.setIsGlobal();
         var2.fBlock = 7;
         var2.setConstraintType((short)0);
         return var2;
      }

      private XSParticleDecl createUnboundedModelGroupParticle() {
         XSParticleDecl var1 = new XSParticleDecl();
         var1.fMinOccurs = 0;
         var1.fMaxOccurs = -1;
         var1.fType = 3;
         return var1;
      }

      private XSParticleDecl createChoiceElementParticle(XSElementDecl var1) {
         XSParticleDecl var2 = new XSParticleDecl();
         var2.fMinOccurs = 1;
         var2.fMaxOccurs = 1;
         var2.fType = 1;
         var2.fValue = var1;
         return var2;
      }

      private XSParticleDecl createUnboundedAnyWildcardSequenceParticle() {
         XSParticleDecl var1 = this.createUnboundedModelGroupParticle();
         XSModelGroupImpl var2 = new XSModelGroupImpl();
         var2.fCompositor = 102;
         var2.fParticleCount = 1;
         var2.fParticles = new XSParticleDecl[1];
         var2.fParticles[0] = this.createAnyLaxWildcardParticle();
         var1.fValue = var2;
         return var1;
      }

      private XSParticleDecl createAnyLaxWildcardParticle() {
         XSParticleDecl var1 = new XSParticleDecl();
         var1.fMinOccurs = 1;
         var1.fMaxOccurs = 1;
         var1.fType = 2;
         XSWildcardDecl var2 = new XSWildcardDecl();
         var2.fNamespaceList = null;
         var2.fType = 1;
         var2.fProcessContents = 3;
         var1.fValue = var2;
         return var1;
      }
   }

   public static class BuiltinSchemaGrammar extends SchemaGrammar {
      public BuiltinSchemaGrammar(int var1) {
         SchemaDVFactory var2 = SchemaDVFactory.getInstance();
         if (var1 == 1) {
            super.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
            super.fGrammarDescription = new XSDDescription();
            super.fGrammarDescription.fContextType = 3;
            super.fGrammarDescription.setNamespace(SchemaSymbols.URI_SCHEMAFORSCHEMA);
            super.fGlobalAttrDecls = new SymbolHash(1);
            super.fGlobalAttrGrpDecls = new SymbolHash(1);
            super.fGlobalElemDecls = new SymbolHash(1);
            super.fGlobalGroupDecls = new SymbolHash(1);
            super.fGlobalNotationDecls = new SymbolHash(1);
            super.fGlobalIDConstraintDecls = new SymbolHash(1);
            super.fGlobalTypeDecls = var2.getBuiltInTypes();
            super.fGlobalTypeDecls.put(SchemaGrammar.fAnyType.getName(), SchemaGrammar.fAnyType);
         } else if (var1 == 2) {
            super.fTargetNamespace = SchemaSymbols.URI_XSI;
            super.fGrammarDescription = new XSDDescription();
            super.fGrammarDescription.fContextType = 3;
            super.fGrammarDescription.setNamespace(SchemaSymbols.URI_XSI);
            super.fGlobalAttrGrpDecls = new SymbolHash(1);
            super.fGlobalElemDecls = new SymbolHash(1);
            super.fGlobalGroupDecls = new SymbolHash(1);
            super.fGlobalNotationDecls = new SymbolHash(1);
            super.fGlobalIDConstraintDecls = new SymbolHash(1);
            super.fGlobalTypeDecls = new SymbolHash(1);
            super.fGlobalAttrDecls = new SymbolHash(8);
            String var3 = null;
            String var4 = null;
            XSSimpleType var5 = null;
            byte var6 = 1;
            var3 = SchemaSymbols.XSI_TYPE;
            var4 = SchemaSymbols.URI_XSI;
            var5 = var2.getBuiltInType("QName");
            super.fGlobalAttrDecls.put(var3, new BuiltinAttrDecl(var3, var4, var5, var6));
            var3 = SchemaSymbols.XSI_NIL;
            var4 = SchemaSymbols.URI_XSI;
            var5 = var2.getBuiltInType("boolean");
            super.fGlobalAttrDecls.put(var3, new BuiltinAttrDecl(var3, var4, var5, var6));
            XSSimpleType var7 = var2.getBuiltInType("anyURI");
            var3 = SchemaSymbols.XSI_SCHEMALOCATION;
            var4 = SchemaSymbols.URI_XSI;
            var5 = var2.createTypeList((String)null, SchemaSymbols.URI_XSI, (short)0, var7, (XSObjectList)null);
            super.fGlobalAttrDecls.put(var3, new BuiltinAttrDecl(var3, var4, var5, var6));
            var3 = SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION;
            var4 = SchemaSymbols.URI_XSI;
            super.fGlobalAttrDecls.put(var3, new BuiltinAttrDecl(var3, var4, var7, var6));
         }

      }

      public XMLGrammarDescription getGrammarDescription() {
         return super.fGrammarDescription.makeClone();
      }

      public void setImportedGrammars(Vector var1) {
      }

      public void addGlobalAttributeDecl(XSAttributeDecl var1) {
      }

      public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl var1) {
      }

      public void addGlobalElementDecl(XSElementDecl var1) {
      }

      public void addGlobalGroupDecl(XSGroupDecl var1) {
      }

      public void addGlobalNotationDecl(XSNotationDecl var1) {
      }

      public void addGlobalTypeDecl(XSTypeDefinition var1) {
      }

      public void addComplexTypeDecl(XSComplexTypeDecl var1, SimpleLocator var2) {
      }

      public void addRedefinedGroupDecl(XSGroupDecl var1, XSGroupDecl var2, SimpleLocator var3) {
      }

      public synchronized void addDocument(Object var1, String var2) {
      }

      synchronized DOMParser getDOMParser() {
         return null;
      }

      synchronized SAXParser getSAXParser() {
         return null;
      }
   }
}
