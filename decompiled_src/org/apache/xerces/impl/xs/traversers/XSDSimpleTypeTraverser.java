package org.apache.xerces.impl.xs.traversers;

import java.util.Vector;
import org.apache.xerces.impl.dv.InvalidDatatypeFacetException;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.dv.xs.SchemaDVFactoryImpl;
import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Element;

class XSDSimpleTypeTraverser extends XSDAbstractTraverser {
   private final SchemaDVFactory schemaFactory = SchemaDVFactory.getInstance();
   private boolean fIsBuiltIn = false;

   XSDSimpleTypeTraverser(XSDHandler var1, XSAttributeChecker var2) {
      super(var1, var2);
      if (this.schemaFactory instanceof SchemaDVFactoryImpl) {
         ((SchemaDVFactoryImpl)this.schemaFactory).setDeclPool(var1.fDeclPool);
      }

   }

   XSSimpleType traverseGlobal(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, true, var2);
      String var5 = (String)var4[XSAttributeChecker.ATTIDX_NAME];
      XSSimpleType var6 = this.traverseSimpleTypeDecl(var1, var4, var2, var3);
      super.fAttrChecker.returnAttrArray(var4, var2);
      if (var5 == null) {
         this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, SchemaSymbols.ATT_NAME}, var1);
         var6 = null;
      }

      if (var6 != null) {
         var3.addGlobalTypeDecl(var6);
      }

      return var6;
   }

   XSSimpleType traverseLocal(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, false, var2);
      String var5 = this.genAnonTypeName(var1);
      XSSimpleType var6 = this.getSimpleType(var5, var1, var4, var2, var3);
      if (var6 instanceof XSSimpleTypeDecl) {
         ((XSSimpleTypeDecl)var6).setAnonymous(true);
      }

      super.fAttrChecker.returnAttrArray(var4, var2);
      return var6;
   }

   private XSSimpleType traverseSimpleTypeDecl(Element var1, Object[] var2, XSDocumentInfo var3, SchemaGrammar var4) {
      String var5 = (String)var2[XSAttributeChecker.ATTIDX_NAME];
      return this.getSimpleType(var5, var1, var2, var3, var4);
   }

   private String genAnonTypeName(Element var1) {
      StringBuffer var2 = new StringBuffer("#AnonType_");

      for(Element var3 = DOMUtil.getParent(var1); var3 != null && var3 != DOMUtil.getRoot(DOMUtil.getDocument(var3)); var3 = DOMUtil.getParent(var3)) {
         var2.append(var3.getAttribute(SchemaSymbols.ATT_NAME));
      }

      return var2.toString();
   }

   private XSSimpleType getSimpleType(String var1, Element var2, Object[] var3, XSDocumentInfo var4, SchemaGrammar var5) {
      XInt var6 = (XInt)var3[XSAttributeChecker.ATTIDX_FINAL];
      int var7 = var6 == null ? var4.fFinalDefault : var6.intValue();
      Element var8 = DOMUtil.getFirstChildElement(var2);
      XSAnnotationImpl[] var9 = null;
      String var10;
      if (var8 != null && DOMUtil.getLocalName(var8).equals(SchemaSymbols.ELT_ANNOTATION)) {
         XSAnnotationImpl var29 = this.traverseAnnotationDecl(var8, var3, false, var4);
         if (var29 != null) {
            var9 = new XSAnnotationImpl[]{var29};
         }

         var8 = DOMUtil.getNextSiblingElement(var8);
      } else {
         var10 = DOMUtil.getSyntheticAnnotation(var2);
         if (var10 != null) {
            XSAnnotationImpl var11 = this.traverseSyntheticAnnotation(var2, var10, var3, false, var4);
            var9 = new XSAnnotationImpl[]{var11};
         }
      }

      if (var8 == null) {
         this.reportSchemaError("s4s-elt-must-match.2", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))"}, var2);
         return this.errorType(var1, var4.fTargetNamespace, (short)2);
      } else {
         var10 = DOMUtil.getLocalName(var8);
         boolean var30 = true;
         boolean var12 = false;
         boolean var13 = false;
         boolean var14 = false;
         byte var31;
         if (var10.equals(SchemaSymbols.ELT_RESTRICTION)) {
            var31 = 2;
            var12 = true;
         } else if (var10.equals(SchemaSymbols.ELT_LIST)) {
            var31 = 16;
            var13 = true;
         } else {
            if (!var10.equals(SchemaSymbols.ELT_UNION)) {
               this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))", var10}, var2);
               return this.errorType(var1, var4.fTargetNamespace, (short)2);
            }

            var31 = 8;
            var14 = true;
         }

         Element var15 = DOMUtil.getNextSiblingElement(var8);
         if (var15 != null) {
            this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_SIMPLETYPE, "(annotation?, (restriction | list | union))", DOMUtil.getLocalName(var15)}, var15);
         }

         Object[] var16 = super.fAttrChecker.checkAttributes(var8, false, var4);
         QName var17 = (QName)var16[var12 ? XSAttributeChecker.ATTIDX_BASE : XSAttributeChecker.ATTIDX_ITEMTYPE];
         Vector var18 = (Vector)var16[XSAttributeChecker.ATTIDX_MEMBERTYPES];
         Element var19 = DOMUtil.getFirstChildElement(var8);
         XSAnnotationImpl[] var22;
         if (var19 != null && DOMUtil.getLocalName(var19).equals(SchemaSymbols.ELT_ANNOTATION)) {
            XSAnnotationImpl var32 = this.traverseAnnotationDecl(var19, var16, false, var4);
            if (var32 != null) {
               if (var9 == null) {
                  var9 = new XSAnnotationImpl[]{var32};
               } else {
                  XSAnnotationImpl[] var34 = new XSAnnotationImpl[]{var9[0], null};
                  var9 = var34;
                  var34[1] = var32;
               }
            }

            var19 = DOMUtil.getNextSiblingElement(var19);
         } else {
            String var20 = DOMUtil.getSyntheticAnnotation(var8);
            if (var20 != null) {
               XSAnnotationImpl var21 = this.traverseSyntheticAnnotation(var8, var20, var16, false, var4);
               if (var9 == null) {
                  var9 = new XSAnnotationImpl[]{var21};
               } else {
                  var22 = new XSAnnotationImpl[]{var9[0], null};
                  var9 = var22;
                  var22[1] = var21;
               }
            }
         }

         XSSimpleType var33 = null;
         if ((var12 || var13) && var17 != null) {
            var33 = this.findDTValidator(var8, var1, var17, var31, var4);
            if (var33 == null && this.fIsBuiltIn) {
               this.fIsBuiltIn = false;
               return null;
            }
         }

         Vector var35 = null;
         var22 = null;
         XSObjectList var23;
         int var25;
         XSSimpleType var36;
         if (var14 && var18 != null && var18.size() > 0) {
            int var24 = var18.size();
            var35 = new Vector(var24, 2);

            for(var25 = 0; var25 < var24; ++var25) {
               var36 = this.findDTValidator(var8, var1, (QName)var18.elementAt(var25), (short)8, var4);
               if (var36 != null) {
                  if (var36.getVariety() == 3) {
                     var23 = var36.getMemberTypes();

                     for(int var26 = 0; var26 < var23.getLength(); ++var26) {
                        var35.addElement(var23.item(var26));
                     }
                  } else {
                     var35.addElement(var36);
                  }
               }
            }
         }

         boolean var37 = false;
         if (var19 != null && DOMUtil.getLocalName(var19).equals(SchemaSymbols.ELT_SIMPLETYPE)) {
            if (!var12 && !var13) {
               if (var14) {
                  if (var35 == null) {
                     var35 = new Vector(2, 2);
                  }

                  do {
                     var36 = this.traverseLocal(var19, var4, var5);
                     if (var36 != null) {
                        if (var36.getVariety() == 3) {
                           var23 = var36.getMemberTypes();

                           for(var25 = 0; var25 < var23.getLength(); ++var25) {
                              var35.addElement(var23.item(var25));
                           }
                        } else {
                           var35.addElement(var36);
                        }
                     }

                     var19 = DOMUtil.getNextSiblingElement(var19);
                  } while(var19 != null && DOMUtil.getLocalName(var19).equals(SchemaSymbols.ELT_SIMPLETYPE));
               }
            } else {
               if (var17 != null) {
                  this.reportSchemaError(var13 ? "src-simple-type.3.a" : "src-simple-type.2.a", (Object[])null, var19);
               } else {
                  var33 = this.traverseLocal(var19, var4, var5);
               }

               var19 = DOMUtil.getNextSiblingElement(var19);
            }
         } else if ((var12 || var13) && var17 == null) {
            this.reportSchemaError(var13 ? "src-simple-type.3.b" : "src-simple-type.2.b", (Object[])null, var8);
            var37 = true;
            var33 = SchemaGrammar.fAnySimpleType;
         } else if (var14 && (var18 == null || var18.size() == 0)) {
            this.reportSchemaError("src-union-memberTypes-or-simpleTypes", (Object[])null, var8);
            var35 = new Vector(1);
            var35.addElement(SchemaGrammar.fAnySimpleType);
         }

         if ((var12 || var13) && var33 == null) {
            var33 = SchemaGrammar.fAnySimpleType;
         }

         if (var14 && (var35 == null || var35.size() == 0)) {
            var35 = new Vector(1);
            var35.addElement(SchemaGrammar.fAnySimpleType);
         }

         if (var13 && this.isListDatatype(var33)) {
            this.reportSchemaError("cos-st-restricts.2.1", new Object[]{var1, var33.getName()}, var8);
         }

         XSSimpleType var38 = null;
         if (var12) {
            var38 = this.schemaFactory.createTypeRestriction(var1, var4.fTargetNamespace, (short)var7, var33, var9 == null ? null : new XSObjectListImpl(var9, var9.length));
         } else if (var13) {
            var38 = this.schemaFactory.createTypeList(var1, var4.fTargetNamespace, (short)var7, var33, var9 == null ? null : new XSObjectListImpl(var9, var9.length));
         } else if (var14) {
            XSSimpleType[] var39 = new XSSimpleType[var35.size()];

            for(int var27 = 0; var27 < var35.size(); ++var27) {
               var39[var27] = (XSSimpleType)var35.elementAt(var27);
            }

            var38 = this.schemaFactory.createTypeUnion(var1, var4.fTargetNamespace, (short)var7, var39, var9 == null ? null : new XSObjectListImpl(var9, var9.length));
         }

         if (var12 && var19 != null) {
            XSDAbstractTraverser.FacetInfo var40 = this.traverseFacets(var19, var33, var4);
            var19 = var40.nodeAfterFacets;
            if (!var37) {
               try {
                  super.fValidationState.setNamespaceSupport(var4.fNamespaceSupport);
                  var38.applyFacets(var40.facetdata, var40.fPresentFacets, var40.fFixedFacets, super.fValidationState);
               } catch (InvalidDatatypeFacetException var28) {
                  this.reportSchemaError(var28.getKey(), var28.getArgs(), var8);
               }
            }
         }

         if (var19 != null) {
            if (var12) {
               this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_RESTRICTION, "(annotation?, (simpleType?, (minExclusive | minInclusive | maxExclusive | maxInclusive | totalDigits | fractionDigits | length | minLength | maxLength | enumeration | whiteSpace | pattern)*))", DOMUtil.getLocalName(var19)}, var19);
            } else if (var13) {
               this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_LIST, "(annotation?, (simpleType?))", DOMUtil.getLocalName(var19)}, var19);
            } else if (var14) {
               this.reportSchemaError("s4s-elt-must-match.1", new Object[]{SchemaSymbols.ELT_UNION, "(annotation?, (simpleType*))", DOMUtil.getLocalName(var19)}, var19);
            }
         }

         super.fAttrChecker.returnAttrArray(var16, var4);
         return var38;
      }
   }

   private XSSimpleType findDTValidator(Element var1, String var2, QName var3, short var4, XSDocumentInfo var5) {
      if (var3 == null) {
         return null;
      } else {
         XSTypeDefinition var6 = (XSTypeDefinition)super.fSchemaHandler.getGlobalDecl(var5, 7, var3, var1);
         if (var6 != null) {
            if (var6.getTypeCategory() != 16 || var6 == SchemaGrammar.fAnySimpleType && var4 == 2) {
               if (var6 == SchemaGrammar.fAnySimpleType && this.checkBuiltIn(var2, var5.fTargetNamespace)) {
                  return null;
               }

               this.reportSchemaError("cos-st-restricts.1.1", new Object[]{var3.rawname, var2}, var1);
               return SchemaGrammar.fAnySimpleType;
            }

            if ((var6.getFinal() & var4) != 0) {
               if (var4 == 2) {
                  this.reportSchemaError("st-props-correct.3", new Object[]{var2, var3.rawname}, var1);
               } else if (var4 == 16) {
                  this.reportSchemaError("cos-st-restricts.2.3.1.1", new Object[]{var3.rawname, var2}, var1);
               } else if (var4 == 8) {
                  this.reportSchemaError("cos-st-restricts.3.3.1.1", new Object[]{var3.rawname, var2}, var1);
               }
            }
         }

         return (XSSimpleType)var6;
      }
   }

   private final boolean checkBuiltIn(String var1, String var2) {
      if (var2 != SchemaSymbols.URI_SCHEMAFORSCHEMA) {
         return false;
      } else {
         if (SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(var1) != null) {
            this.fIsBuiltIn = true;
         }

         return this.fIsBuiltIn;
      }
   }

   private boolean isListDatatype(XSSimpleType var1) {
      if (var1.getVariety() == 2) {
         return true;
      } else {
         if (var1.getVariety() == 3) {
            XSObjectList var2 = var1.getMemberTypes();

            for(int var3 = 0; var3 < var2.getLength(); ++var3) {
               if (((XSSimpleType)var2.item(var3)).getVariety() == 2) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private XSSimpleType errorType(String var1, String var2, short var3) {
      switch (var3) {
         case 2:
            return this.schemaFactory.createTypeRestriction(var1, var2, (short)0, SchemaGrammar.fAnySimpleType, (XSObjectList)null);
         case 8:
            return this.schemaFactory.createTypeUnion(var1, var2, (short)0, new XSSimpleType[]{SchemaGrammar.fAnySimpleType}, (XSObjectList)null);
         case 16:
            return this.schemaFactory.createTypeList(var1, var2, (short)0, SchemaGrammar.fAnySimpleType, (XSObjectList)null);
         default:
            return null;
      }
   }
}
