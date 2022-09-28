package org.apache.xerces.impl.xs;

import java.util.Vector;
import org.apache.xerces.impl.xs.util.NSItemListImpl;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSNamedMap4Types;
import org.apache.xerces.impl.xs.util.XSNamedMapImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeGroupDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSModelGroupDefinition;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSNamespaceItemList;
import org.apache.xerces.xs.XSNotationDeclaration;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;

public class XSModelImpl implements XSModel {
   private static final short MAX_COMP_IDX = 16;
   private static final boolean[] GLOBAL_COMP = new boolean[]{false, true, true, true, false, true, true, false, false, false, false, true, false, false, false, true, true};
   private int fGrammarCount;
   private String[] fNamespaces;
   private SchemaGrammar[] fGrammarList;
   private SymbolHash fGrammarMap;
   private SymbolHash fSubGroupMap;
   private XSNamedMap[] fGlobalComponents;
   private XSNamedMap[][] fNSComponents;
   private XSObjectListImpl fAnnotations = null;
   private boolean fHasIDC = false;

   public XSModelImpl(SchemaGrammar[] var1) {
      int var2 = var1.length;
      this.fNamespaces = new String[Math.max(var2 + 1, 5)];
      this.fGrammarList = new SchemaGrammar[Math.max(var2 + 1, 5)];
      boolean var3 = false;

      for(int var4 = 0; var4 < var2; ++var4) {
         this.fNamespaces[var4] = var1[var4].getTargetNamespace();
         this.fGrammarList[var4] = var1[var4];
         if (this.fNamespaces[var4] == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
            var3 = true;
         }
      }

      if (!var3) {
         this.fNamespaces[var2] = SchemaSymbols.URI_SCHEMAFORSCHEMA;
         this.fGrammarList[var2++] = SchemaGrammar.SG_SchemaNS;
      }

      int var8;
      for(var8 = 0; var8 < var2; ++var8) {
         SchemaGrammar var5 = this.fGrammarList[var8];
         Vector var7 = var5.getImportedGrammars();

         for(int var9 = var7 == null ? -1 : var7.size() - 1; var9 >= 0; --var9) {
            SchemaGrammar var6 = (SchemaGrammar)var7.elementAt(var9);

            int var10;
            for(var10 = 0; var10 < var2 && var6 != this.fGrammarList[var10]; ++var10) {
            }

            if (var10 == var2) {
               if (var2 == this.fGrammarList.length) {
                  String[] var11 = new String[var2 * 2];
                  System.arraycopy(this.fNamespaces, 0, var11, 0, var2);
                  this.fNamespaces = var11;
                  SchemaGrammar[] var12 = new SchemaGrammar[var2 * 2];
                  System.arraycopy(this.fGrammarList, 0, var12, 0, var2);
                  this.fGrammarList = var12;
               }

               this.fNamespaces[var2] = var6.getTargetNamespace();
               this.fGrammarList[var2] = var6;
               ++var2;
            }
         }
      }

      this.fGrammarMap = new SymbolHash(var2 * 2);

      for(var8 = 0; var8 < var2; ++var8) {
         this.fGrammarMap.put(null2EmptyString(this.fNamespaces[var8]), this.fGrammarList[var8]);
         if (this.fGrammarList[var8].hasIDConstraints()) {
            this.fHasIDC = true;
         }
      }

      this.fGrammarCount = var2;
      this.fGlobalComponents = new XSNamedMap[17];
      this.fNSComponents = new XSNamedMap[var2][17];
      this.buildSubGroups();
   }

   private void buildSubGroups() {
      SubstitutionGroupHandler var1 = new SubstitutionGroupHandler((XSGrammarBucket)null);

      for(int var2 = 0; var2 < this.fGrammarCount; ++var2) {
         var1.addSubstitutionGroup(this.fGrammarList[var2].getSubstitutionGroups());
      }

      XSNamedMap var3 = this.getComponents((short)2);
      int var4 = var3.getLength();
      this.fSubGroupMap = new SymbolHash(var4 * 2);

      for(int var7 = 0; var7 < var4; ++var7) {
         XSElementDecl var5 = (XSElementDecl)var3.item(var7);
         XSElementDecl[] var6 = var1.getSubstitutionGroup(var5);
         this.fSubGroupMap.put(var5, var6.length > 0 ? new XSObjectListImpl(var6, var6.length) : XSObjectListImpl.EMPTY_LIST);
      }

   }

   public StringList getNamespaces() {
      return new StringListImpl(this.fNamespaces, this.fGrammarCount);
   }

   public XSNamespaceItemList getNamespaceItems() {
      return new NSItemListImpl(this.fGrammarList, this.fGrammarCount);
   }

   public synchronized XSNamedMap getComponents(short var1) {
      if (var1 > 0 && var1 <= 16 && GLOBAL_COMP[var1]) {
         SymbolHash[] var2 = new SymbolHash[this.fGrammarCount];
         if (this.fGlobalComponents[var1] == null) {
            for(int var3 = 0; var3 < this.fGrammarCount; ++var3) {
               switch (var1) {
                  case 1:
                     var2[var3] = this.fGrammarList[var3].fGlobalAttrDecls;
                     break;
                  case 2:
                     var2[var3] = this.fGrammarList[var3].fGlobalElemDecls;
                     break;
                  case 3:
                  case 15:
                  case 16:
                     var2[var3] = this.fGrammarList[var3].fGlobalTypeDecls;
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
                     var2[var3] = this.fGrammarList[var3].fGlobalAttrGrpDecls;
                     break;
                  case 6:
                     var2[var3] = this.fGrammarList[var3].fGlobalGroupDecls;
                     break;
                  case 11:
                     var2[var3] = this.fGrammarList[var3].fGlobalNotationDecls;
               }
            }

            if (var1 != 15 && var1 != 16) {
               this.fGlobalComponents[var1] = new XSNamedMapImpl(this.fNamespaces, var2, this.fGrammarCount);
            } else {
               this.fGlobalComponents[var1] = new XSNamedMap4Types(this.fNamespaces, var2, this.fGrammarCount, var1);
            }
         }

         return this.fGlobalComponents[var1];
      } else {
         return XSNamedMapImpl.EMPTY_MAP;
      }
   }

   public synchronized XSNamedMap getComponentsByNamespace(short var1, String var2) {
      if (var1 > 0 && var1 <= 16 && GLOBAL_COMP[var1]) {
         int var3 = 0;
         if (var2 != null) {
            while(var3 < this.fGrammarCount && !var2.equals(this.fNamespaces[var3])) {
               ++var3;
            }
         } else {
            while(var3 < this.fGrammarCount && this.fNamespaces[var3] != null) {
               ++var3;
            }
         }

         if (var3 == this.fGrammarCount) {
            return XSNamedMapImpl.EMPTY_MAP;
         } else {
            if (this.fNSComponents[var3][var1] == null) {
               SymbolHash var4 = null;
               switch (var1) {
                  case 1:
                     var4 = this.fGrammarList[var3].fGlobalAttrDecls;
                     break;
                  case 2:
                     var4 = this.fGrammarList[var3].fGlobalElemDecls;
                     break;
                  case 3:
                  case 15:
                  case 16:
                     var4 = this.fGrammarList[var3].fGlobalTypeDecls;
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
                     var4 = this.fGrammarList[var3].fGlobalAttrGrpDecls;
                     break;
                  case 6:
                     var4 = this.fGrammarList[var3].fGlobalGroupDecls;
                     break;
                  case 11:
                     var4 = this.fGrammarList[var3].fGlobalNotationDecls;
               }

               if (var1 != 15 && var1 != 16) {
                  this.fNSComponents[var3][var1] = new XSNamedMapImpl(var2, var4);
               } else {
                  this.fNSComponents[var3][var1] = new XSNamedMap4Types(var2, var4, var1);
               }
            }

            return this.fNSComponents[var3][var1];
         }
      } else {
         return XSNamedMapImpl.EMPTY_MAP;
      }
   }

   public XSTypeDefinition getTypeDefinition(String var1, String var2) {
      SchemaGrammar var3 = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(var2));
      return var3 == null ? null : (XSTypeDefinition)var3.fGlobalTypeDecls.get(var1);
   }

   public XSAttributeDeclaration getAttributeDeclaration(String var1, String var2) {
      SchemaGrammar var3 = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(var2));
      return var3 == null ? null : (XSAttributeDeclaration)var3.fGlobalAttrDecls.get(var1);
   }

   public XSElementDeclaration getElementDeclaration(String var1, String var2) {
      SchemaGrammar var3 = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(var2));
      return var3 == null ? null : (XSElementDeclaration)var3.fGlobalElemDecls.get(var1);
   }

   public XSAttributeGroupDefinition getAttributeGroup(String var1, String var2) {
      SchemaGrammar var3 = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(var2));
      return var3 == null ? null : (XSAttributeGroupDefinition)var3.fGlobalAttrGrpDecls.get(var1);
   }

   public XSModelGroupDefinition getModelGroupDefinition(String var1, String var2) {
      SchemaGrammar var3 = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(var2));
      return var3 == null ? null : (XSModelGroupDefinition)var3.fGlobalGroupDecls.get(var1);
   }

   public XSNotationDeclaration getNotationDeclaration(String var1, String var2) {
      SchemaGrammar var3 = (SchemaGrammar)this.fGrammarMap.get(null2EmptyString(var2));
      return var3 == null ? null : (XSNotationDeclaration)var3.fGlobalNotationDecls.get(var1);
   }

   public synchronized XSObjectList getAnnotations() {
      if (this.fAnnotations != null) {
         return this.fAnnotations;
      } else {
         int var1 = 0;

         for(int var2 = 0; var2 < this.fGrammarCount; ++var2) {
            var1 += this.fGrammarList[var2].fNumAnnotations;
         }

         XSAnnotationImpl[] var3 = new XSAnnotationImpl[var1];
         int var4 = 0;

         for(int var5 = 0; var5 < this.fGrammarCount; ++var5) {
            SchemaGrammar var6 = this.fGrammarList[var5];
            if (var6.fNumAnnotations > 0) {
               System.arraycopy(var6.fAnnotations, 0, var3, var4, var6.fNumAnnotations);
               var4 += var6.fNumAnnotations;
            }
         }

         this.fAnnotations = new XSObjectListImpl(var3, var3.length);
         return this.fAnnotations;
      }
   }

   private static final String null2EmptyString(String var0) {
      return var0 == null ? XMLSymbols.EMPTY_STRING : var0;
   }

   public boolean hasIDConstraints() {
      return this.fHasIDC;
   }

   public XSObjectList getSubstitutionGroup(XSElementDeclaration var1) {
      return (XSObjectList)this.fSubGroupMap.get(var1);
   }
}
