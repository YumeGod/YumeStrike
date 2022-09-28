package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.dv.InvalidDatatypeFacetException;
import org.apache.xerces.impl.dv.SchemaDVFactory;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Element;

class XSDComplexTypeTraverser extends XSDAbstractParticleTraverser {
   private static final int GLOBAL_NUM = 11;
   private String fName = null;
   private String fTargetNamespace = null;
   private short fDerivedBy = 2;
   private short fFinal = 0;
   private short fBlock = 0;
   private short fContentType = 0;
   private XSTypeDefinition fBaseType = null;
   private XSAttributeGroupDecl fAttrGrp = null;
   private XSSimpleType fXSSimpleType = null;
   private XSParticleDecl fParticle = null;
   private boolean fIsAbstract = false;
   private XSComplexTypeDecl fComplexTypeDecl = null;
   private XSAnnotationImpl[] fAnnotations = null;
   private XSParticleDecl fEmptyParticle = null;
   private Object[] fGlobalStore = null;
   private int fGlobalStorePos = 0;
   private static final boolean DEBUG = false;
   private SchemaDVFactory schemaFactory = SchemaDVFactory.getInstance();

   XSDComplexTypeTraverser(XSDHandler var1, XSAttributeChecker var2) {
      super(var1, var2);
   }

   XSComplexTypeDecl traverseLocal(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, false, var2);
      String var5 = this.genAnonTypeName(var1);
      this.contentBackup();
      XSComplexTypeDecl var6 = this.traverseComplexTypeDecl(var1, var5, var4, var2, var3);
      this.contentRestore();
      var3.addComplexTypeDecl(var6, super.fSchemaHandler.element2Locator(var1));
      var6.setIsAnonymous();
      super.fAttrChecker.returnAttrArray(var4, var2);
      return var6;
   }

   XSComplexTypeDecl traverseGlobal(Element var1, XSDocumentInfo var2, SchemaGrammar var3) {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, true, var2);
      String var5 = (String)var4[XSAttributeChecker.ATTIDX_NAME];
      this.contentBackup();
      XSComplexTypeDecl var6 = this.traverseComplexTypeDecl(var1, var5, var4, var2, var3);
      this.contentRestore();
      if (var5 == null) {
         this.reportSchemaError("s4s-att-must-appear", new Object[]{SchemaSymbols.ELT_COMPLEXTYPE, SchemaSymbols.ATT_NAME}, var1);
      } else {
         var3.addGlobalTypeDecl(var6);
      }

      var3.addComplexTypeDecl(var6, super.fSchemaHandler.element2Locator(var1));
      super.fAttrChecker.returnAttrArray(var4, var2);
      return var6;
   }

   private XSComplexTypeDecl traverseComplexTypeDecl(Element var1, String var2, Object[] var3, XSDocumentInfo var4, SchemaGrammar var5) {
      this.fComplexTypeDecl = new XSComplexTypeDecl();
      this.fAttrGrp = new XSAttributeGroupDecl();
      Boolean var6 = (Boolean)var3[XSAttributeChecker.ATTIDX_ABSTRACT];
      XInt var7 = (XInt)var3[XSAttributeChecker.ATTIDX_BLOCK];
      Boolean var8 = (Boolean)var3[XSAttributeChecker.ATTIDX_MIXED];
      XInt var9 = (XInt)var3[XSAttributeChecker.ATTIDX_FINAL];
      this.fName = var2;
      this.fComplexTypeDecl.setName(this.fName);
      this.fTargetNamespace = var4.fTargetNamespace;
      this.fBlock = var7 == null ? var4.fBlockDefault : var7.shortValue();
      this.fFinal = var9 == null ? var4.fFinalDefault : var9.shortValue();
      this.fBlock = (short)(this.fBlock & 3);
      this.fFinal = (short)(this.fFinal & 3);
      this.fIsAbstract = var6 != null && var6;
      Element var10 = null;

      try {
         var10 = DOMUtil.getFirstChildElement(var1);
         String var11;
         if (var10 != null) {
            if (DOMUtil.getLocalName(var10).equals(SchemaSymbols.ELT_ANNOTATION)) {
               this.addAnnotation(this.traverseAnnotationDecl(var10, var3, false, var4));
               var10 = DOMUtil.getNextSiblingElement(var10);
            } else {
               var11 = DOMUtil.getSyntheticAnnotation(var1);
               if (var11 != null) {
                  this.addAnnotation(this.traverseSyntheticAnnotation(var1, var11, var3, false, var4));
               }
            }

            if (var10 != null && DOMUtil.getLocalName(var10).equals(SchemaSymbols.ELT_ANNOTATION)) {
               throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, SchemaSymbols.ELT_ANNOTATION}, var10);
            }
         } else {
            var11 = DOMUtil.getSyntheticAnnotation(var1);
            if (var11 != null) {
               this.addAnnotation(this.traverseSyntheticAnnotation(var1, var11, var3, false, var4));
            }
         }

         if (var10 == null) {
            this.fBaseType = SchemaGrammar.fAnyType;
            this.processComplexContent(var10, var8, false, var4, var5);
         } else {
            String var12;
            Element var14;
            if (DOMUtil.getLocalName(var10).equals(SchemaSymbols.ELT_SIMPLECONTENT)) {
               this.traverseSimpleContent(var10, var4, var5);
               var14 = DOMUtil.getNextSiblingElement(var10);
               if (var14 != null) {
                  var12 = DOMUtil.getLocalName(var14);
                  throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, var12}, var14);
               }
            } else if (DOMUtil.getLocalName(var10).equals(SchemaSymbols.ELT_COMPLEXCONTENT)) {
               this.traverseComplexContent(var10, var8, var4, var5);
               var14 = DOMUtil.getNextSiblingElement(var10);
               if (var14 != null) {
                  var12 = DOMUtil.getLocalName(var14);
                  throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, var12}, var14);
               }
            } else {
               this.fBaseType = SchemaGrammar.fAnyType;
               this.processComplexContent(var10, var8, false, var4, var5);
            }
         }
      } catch (ComplexTypeRecoverableError var13) {
         this.handleComplexTypeError(var13.getMessage(), var13.errorSubstText, var13.errorElem);
      }

      this.fComplexTypeDecl.setValues(this.fName, this.fTargetNamespace, this.fBaseType, this.fDerivedBy, this.fFinal, this.fBlock, this.fContentType, this.fIsAbstract, this.fAttrGrp, this.fXSSimpleType, this.fParticle, new XSObjectListImpl(this.fAnnotations, this.fAnnotations == null ? 0 : this.fAnnotations.length));
      return this.fComplexTypeDecl;
   }

   private void traverseSimpleContent(Element var1, XSDocumentInfo var2, SchemaGrammar var3) throws ComplexTypeRecoverableError {
      Object[] var4 = super.fAttrChecker.checkAttributes(var1, false, var2);
      this.fContentType = 1;
      this.fParticle = null;
      Element var5 = DOMUtil.getFirstChildElement(var1);
      String var6;
      if (var5 != null && DOMUtil.getLocalName(var5).equals(SchemaSymbols.ELT_ANNOTATION)) {
         this.addAnnotation(this.traverseAnnotationDecl(var5, var4, false, var2));
         var5 = DOMUtil.getNextSiblingElement(var5);
      } else {
         var6 = DOMUtil.getSyntheticAnnotation(var1);
         if (var6 != null) {
            this.addAnnotation(this.traverseSyntheticAnnotation(var1, var6, var4, false, var2));
         }
      }

      if (var5 == null) {
         super.fAttrChecker.returnAttrArray(var4, var2);
         throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.2", new Object[]{this.fName, SchemaSymbols.ELT_SIMPLECONTENT}, var1);
      } else {
         var6 = DOMUtil.getLocalName(var5);
         if (var6.equals(SchemaSymbols.ELT_RESTRICTION)) {
            this.fDerivedBy = 2;
         } else {
            if (!var6.equals(SchemaSymbols.ELT_EXTENSION)) {
               super.fAttrChecker.returnAttrArray(var4, var2);
               throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, var6}, var5);
            }

            this.fDerivedBy = 1;
         }

         Element var7 = DOMUtil.getNextSiblingElement(var5);
         if (var7 != null) {
            super.fAttrChecker.returnAttrArray(var4, var2);
            String var23 = DOMUtil.getLocalName(var7);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, var23}, var7);
         } else {
            Object[] var8 = super.fAttrChecker.checkAttributes(var5, false, var2);
            QName var9 = (QName)var8[XSAttributeChecker.ATTIDX_BASE];
            if (var9 == null) {
               super.fAttrChecker.returnAttrArray(var4, var2);
               super.fAttrChecker.returnAttrArray(var8, var2);
               throw new ComplexTypeRecoverableError("s4s-att-must-appear", new Object[]{var6, "base"}, var5);
            } else {
               XSTypeDefinition var10 = (XSTypeDefinition)super.fSchemaHandler.getGlobalDecl(var2, 7, var9, var5);
               if (var10 == null) {
                  super.fAttrChecker.returnAttrArray(var4, var2);
                  super.fAttrChecker.returnAttrArray(var8, var2);
                  throw new ComplexTypeRecoverableError();
               } else {
                  this.fBaseType = var10;
                  XSSimpleType var11 = null;
                  XSComplexTypeDecl var12 = null;
                  boolean var13 = false;
                  short var24;
                  if (var10.getTypeCategory() == 15) {
                     var12 = (XSComplexTypeDecl)var10;
                     var24 = var12.getFinal();
                     if (var12.getContentType() == 1) {
                        var11 = (XSSimpleType)var12.getSimpleType();
                     } else if (this.fDerivedBy != 2 || var12.getContentType() != 3 || !((XSParticleDecl)var12.getParticle()).emptiable()) {
                        super.fAttrChecker.returnAttrArray(var4, var2);
                        super.fAttrChecker.returnAttrArray(var8, var2);
                        throw new ComplexTypeRecoverableError("src-ct.2.1", new Object[]{this.fName, var12.getName()}, var5);
                     }
                  } else {
                     var11 = (XSSimpleType)var10;
                     if (this.fDerivedBy == 2) {
                        super.fAttrChecker.returnAttrArray(var4, var2);
                        super.fAttrChecker.returnAttrArray(var8, var2);
                        throw new ComplexTypeRecoverableError("src-ct.2.1", new Object[]{this.fName, var11.getName()}, var5);
                     }

                     var24 = var11.getFinal();
                  }

                  if ((var24 & this.fDerivedBy) != 0) {
                     super.fAttrChecker.returnAttrArray(var4, var2);
                     super.fAttrChecker.returnAttrArray(var8, var2);
                     String var25 = this.fDerivedBy == 1 ? "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
                     throw new ComplexTypeRecoverableError(var25, new Object[]{this.fName, this.fBaseType.getName()}, var5);
                  } else {
                     Element var14 = var5;
                     var5 = DOMUtil.getFirstChildElement(var5);
                     String var15;
                     if (var5 != null) {
                        if (DOMUtil.getLocalName(var5).equals(SchemaSymbols.ELT_ANNOTATION)) {
                           this.addAnnotation(this.traverseAnnotationDecl(var5, var8, false, var2));
                           var5 = DOMUtil.getNextSiblingElement(var5);
                        } else {
                           var15 = DOMUtil.getSyntheticAnnotation(var14);
                           if (var15 != null) {
                              this.addAnnotation(this.traverseSyntheticAnnotation(var14, var15, var8, false, var2));
                           }
                        }

                        if (var5 != null && DOMUtil.getLocalName(var5).equals(SchemaSymbols.ELT_ANNOTATION)) {
                           super.fAttrChecker.returnAttrArray(var4, var2);
                           super.fAttrChecker.returnAttrArray(var8, var2);
                           throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, SchemaSymbols.ELT_ANNOTATION}, var5);
                        }
                     } else {
                        var15 = DOMUtil.getSyntheticAnnotation(var14);
                        if (var15 != null) {
                           this.addAnnotation(this.traverseSyntheticAnnotation(var14, var15, var8, false, var2));
                        }
                     }

                     if (this.fDerivedBy == 2) {
                        if (var5 != null && DOMUtil.getLocalName(var5).equals(SchemaSymbols.ELT_SIMPLETYPE)) {
                           XSSimpleType var26 = super.fSchemaHandler.fSimpleTypeTraverser.traverseLocal(var5, var2, var3);
                           if (var26 == null) {
                              super.fAttrChecker.returnAttrArray(var4, var2);
                              super.fAttrChecker.returnAttrArray(var8, var2);
                              throw new ComplexTypeRecoverableError();
                           }

                           if (var11 != null && !XSConstraints.checkSimpleDerivationOk(var26, var11, var11.getFinal())) {
                              super.fAttrChecker.returnAttrArray(var4, var2);
                              super.fAttrChecker.returnAttrArray(var8, var2);
                              throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.2.2.1", new Object[]{this.fName, var26.getName(), var11.getName()}, var5);
                           }

                           var11 = var26;
                           var5 = DOMUtil.getNextSiblingElement(var5);
                        }

                        if (var11 == null) {
                           super.fAttrChecker.returnAttrArray(var4, var2);
                           super.fAttrChecker.returnAttrArray(var8, var2);
                           throw new ComplexTypeRecoverableError("src-ct.2.2", new Object[]{this.fName}, var5);
                        }

                        Element var27 = null;
                        XSFacets var16 = null;
                        short var17 = 0;
                        short var18 = 0;
                        if (var5 != null) {
                           XSDAbstractTraverser.FacetInfo var19 = this.traverseFacets(var5, var11, var2);
                           var27 = var19.nodeAfterFacets;
                           var16 = var19.facetdata;
                           var17 = var19.fPresentFacets;
                           var18 = var19.fFixedFacets;
                        }

                        this.fXSSimpleType = this.schemaFactory.createTypeRestriction((String)null, var2.fTargetNamespace, (short)0, var11, (XSObjectList)null);

                        try {
                           super.fValidationState.setNamespaceSupport(var2.fNamespaceSupport);
                           this.fXSSimpleType.applyFacets(var16, var17, var18, super.fValidationState);
                        } catch (InvalidDatatypeFacetException var22) {
                           this.reportSchemaError(var22.getKey(), var22.getArgs(), var5);
                        }

                        if (var27 != null) {
                           if (!this.isAttrOrAttrGroup(var27)) {
                              super.fAttrChecker.returnAttrArray(var4, var2);
                              super.fAttrChecker.returnAttrArray(var8, var2);
                              throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(var27)}, var27);
                           }

                           Element var29 = this.traverseAttrsAndAttrGrps(var27, this.fAttrGrp, var2, var3, this.fComplexTypeDecl);
                           if (var29 != null) {
                              super.fAttrChecker.returnAttrArray(var4, var2);
                              super.fAttrChecker.returnAttrArray(var8, var2);
                              throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(var29)}, var29);
                           }
                        }

                        try {
                           this.mergeAttributes(var12.getAttrGrp(), this.fAttrGrp, this.fName, false, var1);
                        } catch (ComplexTypeRecoverableError var21) {
                           super.fAttrChecker.returnAttrArray(var4, var2);
                           super.fAttrChecker.returnAttrArray(var8, var2);
                           throw var21;
                        }

                        this.fAttrGrp.removeProhibitedAttrs();
                        Object[] var30 = this.fAttrGrp.validRestrictionOf(this.fName, var12.getAttrGrp());
                        if (var30 != null) {
                           super.fAttrChecker.returnAttrArray(var4, var2);
                           super.fAttrChecker.returnAttrArray(var8, var2);
                           throw new ComplexTypeRecoverableError((String)var30[var30.length - 1], var30, var27);
                        }
                     } else {
                        this.fXSSimpleType = var11;
                        if (var5 != null) {
                           if (!this.isAttrOrAttrGroup(var5)) {
                              super.fAttrChecker.returnAttrArray(var4, var2);
                              super.fAttrChecker.returnAttrArray(var8, var2);
                              throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(var5)}, var5);
                           }

                           Element var28 = this.traverseAttrsAndAttrGrps(var5, this.fAttrGrp, var2, var3, this.fComplexTypeDecl);
                           if (var28 != null) {
                              super.fAttrChecker.returnAttrArray(var4, var2);
                              super.fAttrChecker.returnAttrArray(var8, var2);
                              throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(var28)}, var28);
                           }

                           this.fAttrGrp.removeProhibitedAttrs();
                        }

                        if (var12 != null) {
                           try {
                              this.mergeAttributes(var12.getAttrGrp(), this.fAttrGrp, this.fName, true, var1);
                           } catch (ComplexTypeRecoverableError var20) {
                              super.fAttrChecker.returnAttrArray(var4, var2);
                              super.fAttrChecker.returnAttrArray(var8, var2);
                              throw var20;
                           }
                        }
                     }

                     super.fAttrChecker.returnAttrArray(var4, var2);
                     super.fAttrChecker.returnAttrArray(var8, var2);
                  }
               }
            }
         }
      }
   }

   private void traverseComplexContent(Element var1, boolean var2, XSDocumentInfo var3, SchemaGrammar var4) throws ComplexTypeRecoverableError {
      Object[] var5 = super.fAttrChecker.checkAttributes(var1, false, var3);
      boolean var6 = var2;
      Boolean var7 = (Boolean)var5[XSAttributeChecker.ATTIDX_MIXED];
      if (var7 != null) {
         var6 = var7;
      }

      this.fXSSimpleType = null;
      Element var8 = DOMUtil.getFirstChildElement(var1);
      String var9;
      if (var8 != null && DOMUtil.getLocalName(var8).equals(SchemaSymbols.ELT_ANNOTATION)) {
         this.addAnnotation(this.traverseAnnotationDecl(var8, var5, false, var3));
         var8 = DOMUtil.getNextSiblingElement(var8);
      } else {
         var9 = DOMUtil.getSyntheticAnnotation(var1);
         if (var9 != null) {
            this.addAnnotation(this.traverseSyntheticAnnotation(var1, var9, var5, false, var3));
         }
      }

      if (var8 == null) {
         super.fAttrChecker.returnAttrArray(var5, var3);
         throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.2", new Object[]{this.fName, SchemaSymbols.ELT_COMPLEXCONTENT}, var1);
      } else {
         var9 = DOMUtil.getLocalName(var8);
         if (var9.equals(SchemaSymbols.ELT_RESTRICTION)) {
            this.fDerivedBy = 2;
         } else {
            if (!var9.equals(SchemaSymbols.ELT_EXTENSION)) {
               super.fAttrChecker.returnAttrArray(var5, var3);
               throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, var9}, var8);
            }

            this.fDerivedBy = 1;
         }

         Element var10 = DOMUtil.getNextSiblingElement(var8);
         if (var10 != null) {
            super.fAttrChecker.returnAttrArray(var5, var3);
            String var21 = DOMUtil.getLocalName(var10);
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, var21}, var10);
         } else {
            Object[] var11 = super.fAttrChecker.checkAttributes(var8, false, var3);
            QName var12 = (QName)var11[XSAttributeChecker.ATTIDX_BASE];
            if (var12 == null) {
               super.fAttrChecker.returnAttrArray(var5, var3);
               super.fAttrChecker.returnAttrArray(var11, var3);
               throw new ComplexTypeRecoverableError("s4s-att-must-appear", new Object[]{var9, "base"}, var8);
            } else {
               XSTypeDefinition var13 = (XSTypeDefinition)super.fSchemaHandler.getGlobalDecl(var3, 7, var12, var8);
               if (var13 == null) {
                  super.fAttrChecker.returnAttrArray(var5, var3);
                  super.fAttrChecker.returnAttrArray(var11, var3);
                  throw new ComplexTypeRecoverableError();
               } else if (!(var13 instanceof XSComplexTypeDecl)) {
                  super.fAttrChecker.returnAttrArray(var5, var3);
                  super.fAttrChecker.returnAttrArray(var11, var3);
                  throw new ComplexTypeRecoverableError("src-ct.1", new Object[]{this.fName, var13.getName()}, var8);
               } else {
                  XSComplexTypeDecl var14 = (XSComplexTypeDecl)var13;
                  this.fBaseType = var14;
                  String var15;
                  if ((var14.getFinal() & this.fDerivedBy) != 0) {
                     super.fAttrChecker.returnAttrArray(var5, var3);
                     super.fAttrChecker.returnAttrArray(var11, var3);
                     var15 = this.fDerivedBy == 1 ? "cos-ct-extends.1.1" : "derivation-ok-restriction.1";
                     throw new ComplexTypeRecoverableError(var15, new Object[]{this.fName, this.fBaseType.getName()}, var8);
                  } else {
                     var8 = DOMUtil.getFirstChildElement(var8);
                     if (var8 != null) {
                        if (DOMUtil.getLocalName(var8).equals(SchemaSymbols.ELT_ANNOTATION)) {
                           this.addAnnotation(this.traverseAnnotationDecl(var8, var11, false, var3));
                           var8 = DOMUtil.getNextSiblingElement(var8);
                        } else {
                           var15 = DOMUtil.getSyntheticAnnotation(var8);
                           if (var15 != null) {
                              this.addAnnotation(this.traverseSyntheticAnnotation(var8, var15, var11, false, var3));
                           }
                        }

                        if (var8 != null && DOMUtil.getLocalName(var8).equals(SchemaSymbols.ELT_ANNOTATION)) {
                           super.fAttrChecker.returnAttrArray(var5, var3);
                           super.fAttrChecker.returnAttrArray(var11, var3);
                           throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, SchemaSymbols.ELT_ANNOTATION}, var8);
                        }
                     } else {
                        var15 = DOMUtil.getSyntheticAnnotation(var8);
                        if (var15 != null) {
                           this.addAnnotation(this.traverseSyntheticAnnotation(var8, var15, var11, false, var3));
                        }
                     }

                     try {
                        this.processComplexContent(var8, var6, true, var3, var4);
                     } catch (ComplexTypeRecoverableError var20) {
                        super.fAttrChecker.returnAttrArray(var5, var3);
                        super.fAttrChecker.returnAttrArray(var11, var3);
                        throw var20;
                     }

                     XSParticleDecl var22 = (XSParticleDecl)var14.getParticle();
                     if (this.fDerivedBy == 2) {
                        if (this.fContentType == 3 && var14.getContentType() != 3) {
                           super.fAttrChecker.returnAttrArray(var5, var3);
                           super.fAttrChecker.returnAttrArray(var11, var3);
                           throw new ComplexTypeRecoverableError("derivation-ok-restriction.5.4.1.2", new Object[]{this.fName, var14.getName()}, var8);
                        }

                        try {
                           this.mergeAttributes(var14.getAttrGrp(), this.fAttrGrp, this.fName, false, var8);
                        } catch (ComplexTypeRecoverableError var19) {
                           super.fAttrChecker.returnAttrArray(var5, var3);
                           super.fAttrChecker.returnAttrArray(var11, var3);
                           throw var19;
                        }

                        this.fAttrGrp.removeProhibitedAttrs();
                        if (var14 != SchemaGrammar.fAnyType) {
                           Object[] var16 = this.fAttrGrp.validRestrictionOf(this.fName, var14.getAttrGrp());
                           if (var16 != null) {
                              super.fAttrChecker.returnAttrArray(var5, var3);
                              super.fAttrChecker.returnAttrArray(var11, var3);
                              throw new ComplexTypeRecoverableError((String)var16[var16.length - 1], var16, var8);
                           }
                        }
                     } else {
                        if (this.fParticle == null) {
                           this.fContentType = var14.getContentType();
                           this.fXSSimpleType = (XSSimpleType)var14.getSimpleType();
                           this.fParticle = var22;
                        } else if (var14.getContentType() != 0) {
                           if (this.fContentType == 2 && var14.getContentType() != 2) {
                              super.fAttrChecker.returnAttrArray(var5, var3);
                              super.fAttrChecker.returnAttrArray(var11, var3);
                              throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.3.2.2.1.a", new Object[]{this.fName}, var8);
                           }

                           if (this.fContentType == 3 && var14.getContentType() != 3) {
                              super.fAttrChecker.returnAttrArray(var5, var3);
                              super.fAttrChecker.returnAttrArray(var11, var3);
                              throw new ComplexTypeRecoverableError("cos-ct-extends.1.4.3.2.2.1.b", new Object[]{this.fName}, var8);
                           }

                           if (this.fParticle.fType == 3 && ((XSModelGroupImpl)this.fParticle.fValue).fCompositor == 103 || ((XSParticleDecl)var14.getParticle()).fType == 3 && ((XSModelGroupImpl)((XSParticleDecl)var14.getParticle()).fValue).fCompositor == 103) {
                              super.fAttrChecker.returnAttrArray(var5, var3);
                              super.fAttrChecker.returnAttrArray(var11, var3);
                              throw new ComplexTypeRecoverableError("cos-all-limited.1.2", new Object[0], var8);
                           }

                           XSModelGroupImpl var23 = new XSModelGroupImpl();
                           var23.fCompositor = 102;
                           var23.fParticleCount = 2;
                           var23.fParticles = new XSParticleDecl[2];
                           var23.fParticles[0] = (XSParticleDecl)var14.getParticle();
                           var23.fParticles[1] = this.fParticle;
                           XSParticleDecl var17 = new XSParticleDecl();
                           var17.fType = 3;
                           var17.fValue = var23;
                           this.fParticle = var17;
                        }

                        this.fAttrGrp.removeProhibitedAttrs();

                        try {
                           this.mergeAttributes(var14.getAttrGrp(), this.fAttrGrp, this.fName, true, var8);
                        } catch (ComplexTypeRecoverableError var18) {
                           super.fAttrChecker.returnAttrArray(var5, var3);
                           super.fAttrChecker.returnAttrArray(var11, var3);
                           throw var18;
                        }
                     }

                     super.fAttrChecker.returnAttrArray(var5, var3);
                     super.fAttrChecker.returnAttrArray(var11, var3);
                  }
               }
            }
         }
      }
   }

   private void mergeAttributes(XSAttributeGroupDecl var1, XSAttributeGroupDecl var2, String var3, boolean var4, Element var5) throws ComplexTypeRecoverableError {
      XSObjectList var6 = var1.getAttributeUses();
      Object var7 = null;
      XSAttributeUseImpl var8 = null;
      int var9 = var6.getLength();

      for(int var10 = 0; var10 < var9; ++var10) {
         var8 = (XSAttributeUseImpl)var6.item(var10);
         XSAttributeUse var11 = var2.getAttributeUse(var8.fAttrDecl.getNamespace(), var8.fAttrDecl.getName());
         if (var11 == null) {
            String var12 = var2.addAttributeUse(var8);
            if (var12 != null) {
               throw new ComplexTypeRecoverableError("ct-props-correct.5", new Object[]{var3, var12, var8.fAttrDecl.getName()}, var5);
            }
         } else if (var4) {
            throw new ComplexTypeRecoverableError("ct-props-correct.4", new Object[]{var3, var8.fAttrDecl.getName()}, var5);
         }
      }

      if (var4) {
         if (var2.fAttributeWC == null) {
            var2.fAttributeWC = var1.fAttributeWC;
         } else if (var1.fAttributeWC != null) {
            var2.fAttributeWC = var2.fAttributeWC.performUnionWith(var1.fAttributeWC, var2.fAttributeWC.fProcessContents);
         }
      }

   }

   private void processComplexContent(Element var1, boolean var2, boolean var3, XSDocumentInfo var4, SchemaGrammar var5) throws ComplexTypeRecoverableError {
      Element var6 = null;
      XSParticleDecl var7 = null;
      boolean var8 = false;
      if (var1 != null) {
         String var9 = DOMUtil.getLocalName(var1);
         if (var9.equals(SchemaSymbols.ELT_GROUP)) {
            var7 = super.fSchemaHandler.fGroupTraverser.traverseLocal(var1, var4, var5);
            var6 = DOMUtil.getNextSiblingElement(var1);
         } else {
            XSModelGroupImpl var10;
            if (var9.equals(SchemaSymbols.ELT_SEQUENCE)) {
               var7 = this.traverseSequence(var1, var4, var5, 0, this.fComplexTypeDecl);
               if (var7 != null) {
                  var10 = (XSModelGroupImpl)var7.fValue;
                  if (var10.fParticleCount == 0) {
                     var8 = true;
                  }
               }

               var6 = DOMUtil.getNextSiblingElement(var1);
            } else if (var9.equals(SchemaSymbols.ELT_CHOICE)) {
               var7 = this.traverseChoice(var1, var4, var5, 0, this.fComplexTypeDecl);
               if (var7 != null && var7.fMinOccurs == 0) {
                  var10 = (XSModelGroupImpl)var7.fValue;
                  if (var10.fParticleCount == 0) {
                     var8 = true;
                  }
               }

               var6 = DOMUtil.getNextSiblingElement(var1);
            } else if (var9.equals(SchemaSymbols.ELT_ALL)) {
               var7 = this.traverseAll(var1, var4, var5, 8, this.fComplexTypeDecl);
               if (var7 != null) {
                  var10 = (XSModelGroupImpl)var7.fValue;
                  if (var10.fParticleCount == 0) {
                     var8 = true;
                  }
               }

               var6 = DOMUtil.getNextSiblingElement(var1);
            } else {
               var6 = var1;
            }
         }
      }

      Element var11;
      if (var8) {
         var11 = DOMUtil.getFirstChildElement(var1);
         if (var11 != null && DOMUtil.getLocalName(var11).equals(SchemaSymbols.ELT_ANNOTATION)) {
            var11 = DOMUtil.getNextSiblingElement(var11);
         }

         if (var11 == null) {
            var7 = null;
         }
      }

      if (var7 == null && var2) {
         if (this.fEmptyParticle == null) {
            XSModelGroupImpl var12 = new XSModelGroupImpl();
            var12.fCompositor = 102;
            var12.fParticleCount = 0;
            var12.fParticles = null;
            this.fEmptyParticle = new XSParticleDecl();
            this.fEmptyParticle.fType = 3;
            this.fEmptyParticle.fValue = var12;
         }

         var7 = this.fEmptyParticle;
      }

      this.fParticle = var7;
      if (this.fParticle == null) {
         this.fContentType = 0;
      } else if (var2) {
         this.fContentType = 3;
      } else {
         this.fContentType = 2;
      }

      if (var6 != null) {
         if (!this.isAttrOrAttrGroup(var6)) {
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(var6)}, var6);
         }

         var11 = this.traverseAttrsAndAttrGrps(var6, this.fAttrGrp, var4, var5, this.fComplexTypeDecl);
         if (var11 != null) {
            throw new ComplexTypeRecoverableError("s4s-elt-invalid-content.1", new Object[]{this.fName, DOMUtil.getLocalName(var11)}, var11);
         }

         if (!var3) {
            this.fAttrGrp.removeProhibitedAttrs();
         }
      }

   }

   private boolean isAttrOrAttrGroup(Element var1) {
      String var2 = DOMUtil.getLocalName(var1);
      return var2.equals(SchemaSymbols.ELT_ATTRIBUTE) || var2.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP) || var2.equals(SchemaSymbols.ELT_ANYATTRIBUTE);
   }

   private void traverseSimpleContentDecl(Element var1) {
   }

   private void traverseComplexContentDecl(Element var1, boolean var2) {
   }

   private String genAnonTypeName(Element var1) {
      StringBuffer var2 = new StringBuffer("#AnonType_");

      for(Element var3 = DOMUtil.getParent(var1); var3 != null && var3 != DOMUtil.getRoot(DOMUtil.getDocument(var3)); var3 = DOMUtil.getParent(var3)) {
         var2.append(var3.getAttribute(SchemaSymbols.ATT_NAME));
      }

      return var2.toString();
   }

   private void handleComplexTypeError(String var1, Object[] var2, Element var3) {
      if (var1 != null) {
         this.reportSchemaError(var1, var2, var3);
      }

      this.fBaseType = SchemaGrammar.fAnyType;
      this.fContentType = 3;
      this.fParticle = this.getErrorContent();
      this.fAttrGrp.fAttributeWC = this.getErrorWildcard();
   }

   private XSParticleDecl getErrorContent() {
      XSParticleDecl var1 = new XSParticleDecl();
      var1.fType = 2;
      var1.fValue = this.getErrorWildcard();
      var1.fMinOccurs = 0;
      var1.fMaxOccurs = -1;
      XSModelGroupImpl var2 = new XSModelGroupImpl();
      var2.fCompositor = 102;
      var2.fParticleCount = 1;
      var2.fParticles = new XSParticleDecl[1];
      var2.fParticles[0] = var1;
      XSParticleDecl var3 = new XSParticleDecl();
      var3.fType = 3;
      var3.fValue = var2;
      return var3;
   }

   private XSWildcardDecl getErrorWildcard() {
      XSWildcardDecl var1 = new XSWildcardDecl();
      var1.fProcessContents = 2;
      return var1;
   }

   private void contentBackup() {
      if (this.fGlobalStore == null) {
         this.fGlobalStore = new Object[11];
         this.fGlobalStorePos = 0;
      }

      if (this.fGlobalStorePos == this.fGlobalStore.length) {
         Object[] var1 = new Object[this.fGlobalStorePos + 11];
         System.arraycopy(this.fGlobalStore, 0, var1, 0, this.fGlobalStorePos);
         this.fGlobalStore = var1;
      }

      this.fGlobalStore[this.fGlobalStorePos++] = this.fComplexTypeDecl;
      this.fGlobalStore[this.fGlobalStorePos++] = this.fIsAbstract ? Boolean.TRUE : Boolean.FALSE;
      this.fGlobalStore[this.fGlobalStorePos++] = this.fName;
      this.fGlobalStore[this.fGlobalStorePos++] = this.fTargetNamespace;
      this.fGlobalStore[this.fGlobalStorePos++] = new Integer((this.fDerivedBy << 16) + this.fFinal);
      this.fGlobalStore[this.fGlobalStorePos++] = new Integer((this.fBlock << 16) + this.fContentType);
      this.fGlobalStore[this.fGlobalStorePos++] = this.fBaseType;
      this.fGlobalStore[this.fGlobalStorePos++] = this.fAttrGrp;
      this.fGlobalStore[this.fGlobalStorePos++] = this.fParticle;
      this.fGlobalStore[this.fGlobalStorePos++] = this.fXSSimpleType;
      this.fGlobalStore[this.fGlobalStorePos++] = this.fAnnotations;
   }

   private void contentRestore() {
      this.fAnnotations = (XSAnnotationImpl[])this.fGlobalStore[--this.fGlobalStorePos];
      this.fXSSimpleType = (XSSimpleType)this.fGlobalStore[--this.fGlobalStorePos];
      this.fParticle = (XSParticleDecl)this.fGlobalStore[--this.fGlobalStorePos];
      this.fAttrGrp = (XSAttributeGroupDecl)this.fGlobalStore[--this.fGlobalStorePos];
      this.fBaseType = (XSTypeDefinition)this.fGlobalStore[--this.fGlobalStorePos];
      int var1 = (Integer)this.fGlobalStore[--this.fGlobalStorePos];
      this.fBlock = (short)(var1 >> 16);
      this.fContentType = (short)var1;
      var1 = (Integer)this.fGlobalStore[--this.fGlobalStorePos];
      this.fDerivedBy = (short)(var1 >> 16);
      this.fFinal = (short)var1;
      this.fTargetNamespace = (String)this.fGlobalStore[--this.fGlobalStorePos];
      this.fName = (String)this.fGlobalStore[--this.fGlobalStorePos];
      this.fIsAbstract = (Boolean)this.fGlobalStore[--this.fGlobalStorePos];
      this.fComplexTypeDecl = (XSComplexTypeDecl)this.fGlobalStore[--this.fGlobalStorePos];
   }

   private void addAnnotation(XSAnnotationImpl var1) {
      if (var1 != null) {
         if (this.fAnnotations == null) {
            this.fAnnotations = new XSAnnotationImpl[1];
         } else {
            XSAnnotationImpl[] var2 = new XSAnnotationImpl[this.fAnnotations.length + 1];
            System.arraycopy(this.fAnnotations, 0, var2, 0, this.fAnnotations.length);
            this.fAnnotations = var2;
         }

         this.fAnnotations[this.fAnnotations.length - 1] = var1;
      }
   }

   private class ComplexTypeRecoverableError extends Exception {
      private static final long serialVersionUID = 3762247556666831417L;
      Object[] errorSubstText = null;
      Element errorElem = null;

      ComplexTypeRecoverableError() {
      }

      ComplexTypeRecoverableError(String var2, Object[] var3, Element var4) {
         super(var2);
         this.errorSubstText = var3;
         this.errorElem = var4;
      }
   }
}
