package org.apache.xerces.impl.xs.traversers;

import java.util.Vector;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.impl.dv.XSFacets;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.impl.validation.ValidationState;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSAttributeGroupDecl;
import org.apache.xerces.impl.xs.XSAttributeUseImpl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

abstract class XSDAbstractTraverser {
   protected static final String NO_NAME = "(no name)";
   protected static final int NOT_ALL_CONTEXT = 0;
   protected static final int PROCESSING_ALL_EL = 1;
   protected static final int GROUP_REF_WITH_ALL = 2;
   protected static final int CHILD_OF_GROUP = 4;
   protected static final int PROCESSING_ALL_GP = 8;
   protected XSDHandler fSchemaHandler = null;
   protected SymbolTable fSymbolTable = null;
   protected XSAttributeChecker fAttrChecker = null;
   protected boolean fValidateAnnotations = false;
   ValidationState fValidationState = new ValidationState();
   private static final XSSimpleType fQNameDV;
   private StringBuffer fPattern = new StringBuffer();
   private final XSFacets xsFacets = new XSFacets();

   XSDAbstractTraverser(XSDHandler var1, XSAttributeChecker var2) {
      this.fSchemaHandler = var1;
      this.fAttrChecker = var2;
   }

   void reset(SymbolTable var1, boolean var2) {
      this.fSymbolTable = var1;
      this.fValidateAnnotations = var2;
      this.fValidationState.setExtraChecking(false);
      this.fValidationState.setSymbolTable(var1);
   }

   XSAnnotationImpl traverseAnnotationDecl(Element var1, Object[] var2, boolean var3, XSDocumentInfo var4) {
      Object[] var5 = this.fAttrChecker.checkAttributes(var1, var3, var4);
      this.fAttrChecker.returnAttrArray(var5, var4);
      String var6 = null;
      Element var7 = DOMUtil.getFirstChildElement(var1);
      if (var7 == null) {
         Node var18 = var1.getFirstChild();
         if (var18 != null && var18.getNodeType() == 3) {
            var6 = ((Text)var18).getData();
         }
      } else {
         do {
            String var8 = DOMUtil.getLocalName(var7);
            if (!var8.equals(SchemaSymbols.ELT_APPINFO) && !var8.equals(SchemaSymbols.ELT_DOCUMENTATION)) {
               this.reportSchemaError("src-annotation", new Object[]{var8}, var7);
            } else {
               Node var9 = var7.getFirstChild();
               if (var9 != null && var9.getNodeType() == 3) {
                  var6 = ((Text)var9).getData();
               }
            }

            var5 = this.fAttrChecker.checkAttributes(var7, true, var4);
            this.fAttrChecker.returnAttrArray(var5, var4);
            var7 = DOMUtil.getNextSiblingElement(var7);
         } while(var7 != null);
      }

      if (var6 == null) {
         return null;
      } else {
         SchemaGrammar var19 = this.fSchemaHandler.getGrammar(var4.fTargetNamespace);
         Vector var20 = (Vector)var2[XSAttributeChecker.ATTIDX_NONSCHEMA];
         if (var20 != null && !var20.isEmpty()) {
            StringBuffer var10 = new StringBuffer(64);
            var10.append(" ");
            int var11 = 0;

            int var13;
            String var14;
            while(var11 < var20.size()) {
               String var12 = (String)var20.elementAt(var11++);
               var13 = var12.indexOf(58);
               String var15;
               if (var13 == -1) {
                  var14 = "";
                  var15 = var12;
               } else {
                  var14 = var12.substring(0, var13);
                  var15 = var12.substring(var13 + 1);
               }

               String var16 = var4.fNamespaceSupport.getURI(var14.intern());
               if (!var1.getAttributeNS(var16, var15).equals("")) {
                  ++var11;
               } else {
                  var10.append(var12).append("=\"");
                  String var17 = (String)var20.elementAt(var11++);
                  var17 = processAttValue(var17);
                  var10.append(var17).append("\" ");
               }
            }

            StringBuffer var21 = new StringBuffer(var6.length() + var10.length());
            var13 = var6.indexOf(SchemaSymbols.ELT_ANNOTATION);
            if (var13 == -1) {
               return null;
            } else {
               var13 += SchemaSymbols.ELT_ANNOTATION.length();
               var21.append(var6.substring(0, var13));
               var21.append(var10.toString());
               var21.append(var6.substring(var13, var6.length()));
               var14 = var21.toString();
               if (this.fValidateAnnotations) {
                  var4.addAnnotation(new XSAnnotationInfo(var14, var1));
               }

               return new XSAnnotationImpl(var14, var19);
            }
         } else {
            if (this.fValidateAnnotations) {
               var4.addAnnotation(new XSAnnotationInfo(var6, var1));
            }

            return new XSAnnotationImpl(var6, var19);
         }
      }
   }

   XSAnnotationImpl traverseSyntheticAnnotation(Element var1, String var2, Object[] var3, boolean var4, XSDocumentInfo var5) {
      SchemaGrammar var7 = this.fSchemaHandler.getGrammar(var5.fTargetNamespace);
      Vector var8 = (Vector)var3[XSAttributeChecker.ATTIDX_NONSCHEMA];
      if (var8 != null && !var8.isEmpty()) {
         StringBuffer var9 = new StringBuffer(64);
         var9.append(" ");
         int var10 = 0;

         int var12;
         String var13;
         while(var10 < var8.size()) {
            String var11 = (String)var8.elementAt(var10++);
            var12 = var11.indexOf(58);
            if (var12 == -1) {
               var13 = "";
            } else {
               var13 = var11.substring(0, var12);
               var11.substring(var12 + 1);
            }

            String var15 = var5.fNamespaceSupport.getURI(var13.intern());
            var9.append(var11).append("=\"");
            String var16 = (String)var8.elementAt(var10++);
            var16 = processAttValue(var16);
            var9.append(var16).append("\" ");
         }

         StringBuffer var17 = new StringBuffer(var2.length() + var9.length());
         var12 = var2.indexOf(SchemaSymbols.ELT_ANNOTATION);
         if (var12 == -1) {
            return null;
         } else {
            var12 += SchemaSymbols.ELT_ANNOTATION.length();
            var17.append(var2.substring(0, var12));
            var17.append(var9.toString());
            var17.append(var2.substring(var12, var2.length()));
            var13 = var17.toString();
            if (this.fValidateAnnotations) {
               var5.addAnnotation(new XSAnnotationInfo(var13, var1));
            }

            return new XSAnnotationImpl(var13, var7);
         }
      } else {
         if (this.fValidateAnnotations) {
            var5.addAnnotation(new XSAnnotationInfo(var2, var1));
         }

         return new XSAnnotationImpl(var2, var7);
      }
   }

   FacetInfo traverseFacets(Element var1, XSSimpleType var2, XSDocumentInfo var3) {
      short var4 = 0;
      short var5 = 0;
      boolean var7 = this.containsQName(var2);
      Vector var8 = null;
      XSObjectListImpl var9 = null;
      XSObjectListImpl var10 = null;
      Vector var11 = var7 ? new Vector() : null;
      boolean var12 = false;
      this.xsFacets.reset();

      while(var1 != null) {
         Object[] var13 = null;
         String var6 = DOMUtil.getLocalName(var1);
         if (var6.equals(SchemaSymbols.ELT_ENUMERATION)) {
            var13 = this.fAttrChecker.checkAttributes(var1, false, var3, var7);
            String var14 = (String)var13[XSAttributeChecker.ATTIDX_VALUE];
            NamespaceSupport var15 = (NamespaceSupport)var13[XSAttributeChecker.ATTIDX_ENUMNSDECLS];
            if (var2.getVariety() == 1 && var2.getPrimitiveKind() == 20) {
               var3.fValidationContext.setNamespaceSupport(var15);

               try {
                  QName var16 = (QName)fQNameDV.validate((String)var14, var3.fValidationContext, (ValidatedInfo)null);
                  this.fSchemaHandler.getGlobalDecl(var3, 6, var16, var1);
               } catch (InvalidDatatypeValueException var18) {
                  this.reportSchemaError(var18.getKey(), var18.getArgs(), var1);
               }

               var3.fValidationContext.setNamespaceSupport(var3.fNamespaceSupport);
            }

            if (var8 == null) {
               var8 = new Vector();
               var9 = new XSObjectListImpl();
            }

            var8.addElement(var14);
            var9.add((XSObject)null);
            if (var7) {
               var11.addElement(var15);
            }

            Element var24 = DOMUtil.getFirstChildElement(var1);
            String var17;
            if (var24 != null) {
               if (DOMUtil.getLocalName(var24).equals(SchemaSymbols.ELT_ANNOTATION)) {
                  var9.add(var9.getLength() - 1, this.traverseAnnotationDecl(var24, var13, false, var3));
                  var24 = DOMUtil.getNextSiblingElement(var24);
               } else {
                  var17 = DOMUtil.getSyntheticAnnotation(var1);
                  if (var17 != null) {
                     var9.add(var9.getLength() - 1, this.traverseSyntheticAnnotation(var1, var17, var13, false, var3));
                  }
               }

               if (var24 != null && DOMUtil.getLocalName(var24).equals(SchemaSymbols.ELT_ANNOTATION)) {
                  this.reportSchemaError("s4s-elt-must-match.1", new Object[]{"enumeration", "(annotation?)", DOMUtil.getLocalName(var24)}, var24);
               }
            } else {
               var17 = DOMUtil.getSyntheticAnnotation(var1);
               if (var17 != null) {
                  var9.add(var9.getLength() - 1, this.traverseSyntheticAnnotation(var1, var17, var13, false, var3));
               }
            }
         } else {
            Element var20;
            if (var6.equals(SchemaSymbols.ELT_PATTERN)) {
               var13 = this.fAttrChecker.checkAttributes(var1, false, var3);
               if (this.fPattern.length() == 0) {
                  this.fPattern.append((String)var13[XSAttributeChecker.ATTIDX_VALUE]);
               } else {
                  this.fPattern.append("|");
                  this.fPattern.append((String)var13[XSAttributeChecker.ATTIDX_VALUE]);
               }

               var20 = DOMUtil.getFirstChildElement(var1);
               if (var20 != null) {
                  if (DOMUtil.getLocalName(var20).equals(SchemaSymbols.ELT_ANNOTATION)) {
                     if (var10 == null) {
                        var10 = new XSObjectListImpl();
                     }

                     var10.add(this.traverseAnnotationDecl(var20, var13, false, var3));
                     var20 = DOMUtil.getNextSiblingElement(var20);
                  }

                  if (var20 != null && DOMUtil.getLocalName(var20).equals(SchemaSymbols.ELT_ANNOTATION)) {
                     this.reportSchemaError("s4s-elt-must-match.1", new Object[]{"pattern", "(annotation?)", DOMUtil.getLocalName(var20)}, var20);
                  }
               }
            } else {
               short var19;
               if (var6.equals(SchemaSymbols.ELT_MINLENGTH)) {
                  var19 = 2;
               } else if (var6.equals(SchemaSymbols.ELT_MAXLENGTH)) {
                  var19 = 4;
               } else if (var6.equals(SchemaSymbols.ELT_MAXEXCLUSIVE)) {
                  var19 = 64;
               } else if (var6.equals(SchemaSymbols.ELT_MAXINCLUSIVE)) {
                  var19 = 32;
               } else if (var6.equals(SchemaSymbols.ELT_MINEXCLUSIVE)) {
                  var19 = 128;
               } else if (var6.equals(SchemaSymbols.ELT_MININCLUSIVE)) {
                  var19 = 256;
               } else if (var6.equals(SchemaSymbols.ELT_TOTALDIGITS)) {
                  var19 = 512;
               } else if (var6.equals(SchemaSymbols.ELT_FRACTIONDIGITS)) {
                  var19 = 1024;
               } else if (var6.equals(SchemaSymbols.ELT_WHITESPACE)) {
                  var19 = 16;
               } else {
                  if (!var6.equals(SchemaSymbols.ELT_LENGTH)) {
                     break;
                  }

                  var19 = 1;
               }

               var13 = this.fAttrChecker.checkAttributes(var1, false, var3);
               if ((var4 & var19) != 0) {
                  this.reportSchemaError("src-single-facet-value", new Object[]{var6}, var1);
               } else if (var13[XSAttributeChecker.ATTIDX_VALUE] != null) {
                  var4 |= var19;
                  if ((Boolean)var13[XSAttributeChecker.ATTIDX_FIXED]) {
                     var5 |= var19;
                  }

                  switch (var19) {
                     case 1:
                        this.xsFacets.length = ((XInt)var13[XSAttributeChecker.ATTIDX_VALUE]).intValue();
                        break;
                     case 2:
                        this.xsFacets.minLength = ((XInt)var13[XSAttributeChecker.ATTIDX_VALUE]).intValue();
                        break;
                     case 4:
                        this.xsFacets.maxLength = ((XInt)var13[XSAttributeChecker.ATTIDX_VALUE]).intValue();
                        break;
                     case 16:
                        this.xsFacets.whiteSpace = ((XInt)var13[XSAttributeChecker.ATTIDX_VALUE]).shortValue();
                        break;
                     case 32:
                        this.xsFacets.maxInclusive = (String)var13[XSAttributeChecker.ATTIDX_VALUE];
                        break;
                     case 64:
                        this.xsFacets.maxExclusive = (String)var13[XSAttributeChecker.ATTIDX_VALUE];
                        break;
                     case 128:
                        this.xsFacets.minExclusive = (String)var13[XSAttributeChecker.ATTIDX_VALUE];
                        break;
                     case 256:
                        this.xsFacets.minInclusive = (String)var13[XSAttributeChecker.ATTIDX_VALUE];
                        break;
                     case 512:
                        this.xsFacets.totalDigits = ((XInt)var13[XSAttributeChecker.ATTIDX_VALUE]).intValue();
                        break;
                     case 1024:
                        this.xsFacets.fractionDigits = ((XInt)var13[XSAttributeChecker.ATTIDX_VALUE]).intValue();
                  }
               }

               var20 = DOMUtil.getFirstChildElement(var1);
               if (var20 != null) {
                  if (DOMUtil.getLocalName(var20).equals(SchemaSymbols.ELT_ANNOTATION)) {
                     XSAnnotationImpl var22 = this.traverseAnnotationDecl(var20, var13, false, var3);
                     switch (var19) {
                        case 1:
                           this.xsFacets.lengthAnnotation = var22;
                           break;
                        case 2:
                           this.xsFacets.minLengthAnnotation = var22;
                           break;
                        case 4:
                           this.xsFacets.maxLengthAnnotation = var22;
                           break;
                        case 16:
                           this.xsFacets.whiteSpaceAnnotation = var22;
                           break;
                        case 32:
                           this.xsFacets.maxInclusiveAnnotation = var22;
                           break;
                        case 64:
                           this.xsFacets.maxExclusiveAnnotation = var22;
                           break;
                        case 128:
                           this.xsFacets.minExclusiveAnnotation = var22;
                           break;
                        case 256:
                           this.xsFacets.minInclusiveAnnotation = var22;
                           break;
                        case 512:
                           this.xsFacets.totalDigitsAnnotation = var22;
                           break;
                        case 1024:
                           this.xsFacets.fractionDigitsAnnotation = var22;
                     }

                     var20 = DOMUtil.getNextSiblingElement(var20);
                  } else {
                     String var23 = DOMUtil.getSyntheticAnnotation(var1);
                     if (var23 != null) {
                        XSAnnotationImpl var25 = this.traverseSyntheticAnnotation(var1, var23, var13, false, var3);
                        switch (var19) {
                           case 1:
                              this.xsFacets.lengthAnnotation = var25;
                              break;
                           case 2:
                              this.xsFacets.minLengthAnnotation = var25;
                              break;
                           case 4:
                              this.xsFacets.maxLengthAnnotation = var25;
                              break;
                           case 16:
                              this.xsFacets.whiteSpaceAnnotation = var25;
                              break;
                           case 32:
                              this.xsFacets.maxInclusiveAnnotation = var25;
                              break;
                           case 64:
                              this.xsFacets.maxExclusiveAnnotation = var25;
                              break;
                           case 128:
                              this.xsFacets.minExclusiveAnnotation = var25;
                              break;
                           case 256:
                              this.xsFacets.minInclusiveAnnotation = var25;
                              break;
                           case 512:
                              this.xsFacets.totalDigitsAnnotation = var25;
                              break;
                           case 1024:
                              this.xsFacets.fractionDigitsAnnotation = var25;
                        }
                     }
                  }

                  if (var20 != null && DOMUtil.getLocalName(var20).equals(SchemaSymbols.ELT_ANNOTATION)) {
                     this.reportSchemaError("s4s-elt-must-match.1", new Object[]{var6, "(annotation?)", DOMUtil.getLocalName(var20)}, var20);
                  }
               }
            }
         }

         this.fAttrChecker.returnAttrArray(var13, var3);
         var1 = DOMUtil.getNextSiblingElement(var1);
      }

      if (var8 != null) {
         var4 = (short)(var4 | 2048);
         this.xsFacets.enumeration = var8;
         this.xsFacets.enumNSDecls = var11;
         this.xsFacets.enumAnnotations = var9;
      }

      if (this.fPattern.length() != 0) {
         var4 = (short)(var4 | 8);
         this.xsFacets.pattern = this.fPattern.toString();
         this.xsFacets.patternAnnotations = var10;
      }

      this.fPattern.setLength(0);
      FacetInfo var21 = new FacetInfo();
      var21.facetdata = this.xsFacets;
      var21.nodeAfterFacets = var1;
      var21.fPresentFacets = var4;
      var21.fFixedFacets = var5;
      return var21;
   }

   private boolean containsQName(XSSimpleType var1) {
      if (var1.getVariety() == 1) {
         short var4 = var1.getPrimitiveKind();
         return var4 == 18 || var4 == 20;
      } else if (var1.getVariety() == 2) {
         return this.containsQName((XSSimpleType)var1.getItemType());
      } else {
         if (var1.getVariety() == 3) {
            XSObjectList var2 = var1.getMemberTypes();

            for(int var3 = 0; var3 < var2.getLength(); ++var3) {
               if (this.containsQName((XSSimpleType)var2.item(var3))) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   Element traverseAttrsAndAttrGrps(Element var1, XSAttributeGroupDecl var2, XSDocumentInfo var3, SchemaGrammar var4, XSComplexTypeDecl var5) {
      Element var6 = null;
      XSAttributeGroupDecl var7 = null;
      XSAttributeUseImpl var8 = null;

      String var9;
      String var11;
      String var12;
      for(var6 = var1; var6 != null; var6 = DOMUtil.getNextSiblingElement(var6)) {
         var9 = DOMUtil.getLocalName(var6);
         if (var9.equals(SchemaSymbols.ELT_ATTRIBUTE)) {
            var8 = this.fSchemaHandler.fAttributeTraverser.traverseLocal(var6, var3, var4, var5);
            if (var8 == null) {
               break;
            }

            String var10;
            if (var2.getAttributeUse(var8.fAttrDecl.getNamespace(), var8.fAttrDecl.getName()) == null) {
               var10 = var2.addAttributeUse(var8);
               if (var10 != null) {
                  var11 = var5 == null ? "ag-props-correct.3" : "ct-props-correct.5";
                  var12 = var5 == null ? var2.fName : var5.getName();
                  this.reportSchemaError(var11, new Object[]{var12, var8.fAttrDecl.getName(), var10}, var6);
               }
            } else {
               var10 = var5 == null ? "ag-props-correct.2" : "ct-props-correct.4";
               var11 = var5 == null ? var2.fName : var5.getName();
               this.reportSchemaError(var10, new Object[]{var11, var8.fAttrDecl.getName()}, var6);
            }
         } else {
            if (!var9.equals(SchemaSymbols.ELT_ATTRIBUTEGROUP)) {
               break;
            }

            var7 = this.fSchemaHandler.fAttributeGroupTraverser.traverseLocal(var6, var3, var4);
            if (var7 == null) {
               break;
            }

            XSObjectList var18 = var7.getAttributeUses();
            var11 = null;
            int var13 = var18.getLength();

            String var15;
            String var16;
            for(int var14 = 0; var14 < var13; ++var14) {
               XSAttributeUseImpl var20 = (XSAttributeUseImpl)var18.item(var14);
               if (var11 == var2.getAttributeUse(var20.fAttrDecl.getNamespace(), var20.fAttrDecl.getName())) {
                  var15 = var2.addAttributeUse(var20);
                  if (var15 != null) {
                     var16 = var5 == null ? "ag-props-correct.3" : "ct-props-correct.5";
                     String var17 = var5 == null ? var2.fName : var5.getName();
                     this.reportSchemaError(var16, new Object[]{var17, var20.fAttrDecl.getName(), var15}, var6);
                  }
               } else {
                  var15 = var5 == null ? "ag-props-correct.2" : "ct-props-correct.4";
                  var16 = var5 == null ? var2.fName : var5.getName();
                  this.reportSchemaError(var15, new Object[]{var16, var20.fAttrDecl.getName()}, var6);
               }
            }

            if (var7.fAttributeWC != null) {
               if (var2.fAttributeWC == null) {
                  var2.fAttributeWC = var7.fAttributeWC;
               } else {
                  var2.fAttributeWC = var2.fAttributeWC.performIntersectionWith(var7.fAttributeWC, var2.fAttributeWC.fProcessContents);
                  if (var2.fAttributeWC == null) {
                     var15 = var5 == null ? "src-attribute_group.2" : "src-ct.4";
                     var16 = var5 == null ? var2.fName : var5.getName();
                     this.reportSchemaError(var15, new Object[]{var16}, var6);
                  }
               }
            }
         }
      }

      if (var6 != null) {
         var9 = DOMUtil.getLocalName(var6);
         if (var9.equals(SchemaSymbols.ELT_ANYATTRIBUTE)) {
            XSWildcardDecl var19 = this.fSchemaHandler.fWildCardTraverser.traverseAnyAttribute(var6, var3, var4);
            if (var2.fAttributeWC == null) {
               var2.fAttributeWC = var19;
            } else {
               var2.fAttributeWC = var19.performIntersectionWith(var2.fAttributeWC, var19.fProcessContents);
               if (var2.fAttributeWC == null) {
                  var11 = var5 == null ? "src-attribute_group.2" : "src-ct.4";
                  var12 = var5 == null ? var2.fName : var5.getName();
                  this.reportSchemaError(var11, new Object[]{var12}, var6);
               }
            }

            var6 = DOMUtil.getNextSiblingElement(var6);
         }
      }

      return var6;
   }

   void reportSchemaError(String var1, Object[] var2, Element var3) {
      this.fSchemaHandler.reportSchemaError(var1, var2, var3);
   }

   void checkNotationType(String var1, XSTypeDefinition var2, Element var3) {
      if (var2.getTypeCategory() == 16 && ((XSSimpleType)var2).getVariety() == 1 && ((XSSimpleType)var2).getPrimitiveKind() == 20 && (((XSSimpleType)var2).getDefinedFacets() & 2048) == 0) {
         this.reportSchemaError("enumeration-required-notation", new Object[]{var2.getName(), var1, DOMUtil.getLocalName(var3)}, var3);
      }

   }

   protected XSParticleDecl checkOccurrences(XSParticleDecl var1, String var2, Element var3, int var4, long var5) {
      int var7 = var1.fMinOccurs;
      int var8 = var1.fMaxOccurs;
      boolean var9 = (var5 & (long)(1 << XSAttributeChecker.ATTIDX_MINOCCURS)) != 0L;
      boolean var10 = (var5 & (long)(1 << XSAttributeChecker.ATTIDX_MAXOCCURS)) != 0L;
      boolean var11 = (var4 & 1) != 0;
      boolean var12 = (var4 & 8) != 0;
      boolean var13 = (var4 & 2) != 0;
      boolean var14 = (var4 & 4) != 0;
      if (var14) {
         Object[] var15;
         if (!var9) {
            var15 = new Object[]{var2, "minOccurs"};
            this.reportSchemaError("s4s-att-not-allowed", var15, var3);
            var7 = 1;
         }

         if (!var10) {
            var15 = new Object[]{var2, "maxOccurs"};
            this.reportSchemaError("s4s-att-not-allowed", var15, var3);
            var8 = 1;
         }
      }

      if (var7 == 0 && var8 == 0) {
         var1.fType = 0;
         return null;
      } else {
         if (var11) {
            if (var8 != 1) {
               this.reportSchemaError("cos-all-limited.2", new Object[]{new Integer(var8), ((XSElementDecl)var1.fValue).getName()}, var3);
               var8 = 1;
               if (var7 > 1) {
                  var7 = 1;
               }
            }
         } else if ((var12 || var13) && var8 != 1) {
            this.reportSchemaError("cos-all-limited.1.2", (Object[])null, var3);
            if (var7 > 1) {
               var7 = 1;
            }

            var8 = 1;
         }

         var1.fMaxOccurs = var7;
         var1.fMaxOccurs = var8;
         return var1;
      }
   }

   private static String processAttValue(String var0) {
      StringBuffer var1 = new StringBuffer(var0.length());

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         if (var3 == '"') {
            var1.append("&quot;");
         } else if (var3 == '>') {
            var1.append("&gt;");
         } else if (var3 == '&') {
            var1.append("&amp;");
         } else {
            var1.append(var3);
         }
      }

      return var1.toString();
   }

   static {
      fQNameDV = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("QName");
   }

   class FacetInfo {
      XSFacets facetdata;
      Element nodeAfterFacets;
      short fPresentFacets;
      short fFixedFacets;
   }
}
