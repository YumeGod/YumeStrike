package org.apache.xerces.impl.xs.traversers;

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.SchemaSymbols;
import org.apache.xerces.impl.xs.XSAnnotationImpl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.impl.xs.util.XInt;
import org.apache.xerces.util.DOMUtil;
import org.apache.xerces.xs.XSObject;
import org.w3c.dom.Element;

abstract class XSDAbstractParticleTraverser extends XSDAbstractTraverser {
   ParticleArray fPArray = new ParticleArray();

   XSDAbstractParticleTraverser(XSDHandler var1, XSAttributeChecker var2) {
      super(var1, var2);
   }

   XSParticleDecl traverseAll(Element var1, XSDocumentInfo var2, SchemaGrammar var3, int var4, XSObject var5) {
      Object[] var6 = super.fAttrChecker.checkAttributes(var1, false, var2);
      Element var7 = DOMUtil.getFirstChildElement(var1);
      XSAnnotationImpl var8 = null;
      String var9;
      if (var7 != null && DOMUtil.getLocalName(var7).equals(SchemaSymbols.ELT_ANNOTATION)) {
         var8 = this.traverseAnnotationDecl(var7, var6, false, var2);
         var7 = DOMUtil.getNextSiblingElement(var7);
      } else {
         var9 = DOMUtil.getSyntheticAnnotation(var1);
         if (var9 != null) {
            var8 = this.traverseSyntheticAnnotation(var1, var9, var6, false, var2);
         }
      }

      var9 = null;
      this.fPArray.pushContext();

      XSParticleDecl var10;
      for(; var7 != null; var7 = DOMUtil.getNextSiblingElement(var7)) {
         var10 = null;
         var9 = DOMUtil.getLocalName(var7);
         if (var9.equals(SchemaSymbols.ELT_ELEMENT)) {
            var10 = super.fSchemaHandler.fElementTraverser.traverseLocal(var7, var2, var3, 1, var5);
         } else {
            Object[] var11 = new Object[]{"all", "(annotation?, element*)", DOMUtil.getLocalName(var7)};
            this.reportSchemaError("s4s-elt-must-match.1", var11, var7);
         }

         if (var10 != null) {
            this.fPArray.addParticle(var10);
         }
      }

      var10 = null;
      XInt var15 = (XInt)var6[XSAttributeChecker.ATTIDX_MINOCCURS];
      XInt var12 = (XInt)var6[XSAttributeChecker.ATTIDX_MAXOCCURS];
      Long var13 = (Long)var6[XSAttributeChecker.ATTIDX_FROMDEFAULT];
      XSModelGroupImpl var14 = new XSModelGroupImpl();
      var14.fCompositor = 103;
      var14.fParticleCount = this.fPArray.getParticleCount();
      var14.fParticles = this.fPArray.popContext();
      var14.fAnnotation = var8;
      var10 = new XSParticleDecl();
      var10.fType = 3;
      var10.fMinOccurs = var15.intValue();
      var10.fMaxOccurs = var12.intValue();
      var10.fValue = var14;
      var10 = this.checkOccurrences(var10, SchemaSymbols.ELT_ALL, (Element)var1.getParentNode(), var4, var13);
      super.fAttrChecker.returnAttrArray(var6, var2);
      return var10;
   }

   XSParticleDecl traverseSequence(Element var1, XSDocumentInfo var2, SchemaGrammar var3, int var4, XSObject var5) {
      return this.traverseSeqChoice(var1, var2, var3, var4, false, var5);
   }

   XSParticleDecl traverseChoice(Element var1, XSDocumentInfo var2, SchemaGrammar var3, int var4, XSObject var5) {
      return this.traverseSeqChoice(var1, var2, var3, var4, true, var5);
   }

   private XSParticleDecl traverseSeqChoice(Element var1, XSDocumentInfo var2, SchemaGrammar var3, int var4, boolean var5, XSObject var6) {
      Object[] var7 = super.fAttrChecker.checkAttributes(var1, false, var2);
      Element var8 = DOMUtil.getFirstChildElement(var1);
      XSAnnotationImpl var9 = null;
      if (var8 != null && DOMUtil.getLocalName(var8).equals(SchemaSymbols.ELT_ANNOTATION)) {
         var9 = this.traverseAnnotationDecl(var8, var7, false, var2);
         var8 = DOMUtil.getNextSiblingElement(var8);
      } else {
         String var10 = DOMUtil.getSyntheticAnnotation(var1);
         if (var10 != null) {
            var9 = this.traverseSyntheticAnnotation(var1, var10, var7, false, var2);
         }
      }

      boolean var17 = false;
      String var11 = null;
      this.fPArray.pushContext();

      XSParticleDecl var12;
      for(; var8 != null; var8 = DOMUtil.getNextSiblingElement(var8)) {
         var12 = null;
         var11 = DOMUtil.getLocalName(var8);
         if (var11.equals(SchemaSymbols.ELT_ELEMENT)) {
            var12 = super.fSchemaHandler.fElementTraverser.traverseLocal(var8, var2, var3, 0, var6);
         } else if (var11.equals(SchemaSymbols.ELT_GROUP)) {
            var12 = super.fSchemaHandler.fGroupTraverser.traverseLocal(var8, var2, var3);
            if (this.hasAllContent(var12)) {
               var12 = null;
               this.reportSchemaError("cos-all-limited.1.2", (Object[])null, var8);
            }
         } else if (var11.equals(SchemaSymbols.ELT_CHOICE)) {
            var12 = this.traverseChoice(var8, var2, var3, 0, var6);
         } else if (var11.equals(SchemaSymbols.ELT_SEQUENCE)) {
            var12 = this.traverseSequence(var8, var2, var3, 0, var6);
         } else if (var11.equals(SchemaSymbols.ELT_ANY)) {
            var12 = super.fSchemaHandler.fWildCardTraverser.traverseAny(var8, var2, var3);
         } else {
            Object[] var13;
            if (var5) {
               var13 = new Object[]{"choice", "(annotation?, (element | group | choice | sequence | any)*)", DOMUtil.getLocalName(var8)};
            } else {
               var13 = new Object[]{"sequence", "(annotation?, (element | group | choice | sequence | any)*)", DOMUtil.getLocalName(var8)};
            }

            this.reportSchemaError("s4s-elt-must-match.1", var13, var8);
         }

         if (var12 != null) {
            this.fPArray.addParticle(var12);
         }
      }

      var12 = null;
      XInt var18 = (XInt)var7[XSAttributeChecker.ATTIDX_MINOCCURS];
      XInt var14 = (XInt)var7[XSAttributeChecker.ATTIDX_MAXOCCURS];
      Long var15 = (Long)var7[XSAttributeChecker.ATTIDX_FROMDEFAULT];
      XSModelGroupImpl var16 = new XSModelGroupImpl();
      var16.fCompositor = (short)(var5 ? 101 : 102);
      var16.fParticleCount = this.fPArray.getParticleCount();
      var16.fParticles = this.fPArray.popContext();
      var16.fAnnotation = var9;
      var12 = new XSParticleDecl();
      var12.fType = 3;
      var12.fMinOccurs = var18.intValue();
      var12.fMaxOccurs = var14.intValue();
      var12.fValue = var16;
      var12 = this.checkOccurrences(var12, var5 ? SchemaSymbols.ELT_CHOICE : SchemaSymbols.ELT_SEQUENCE, (Element)var1.getParentNode(), var4, var15);
      super.fAttrChecker.returnAttrArray(var7, var2);
      return var12;
   }

   protected boolean hasAllContent(XSParticleDecl var1) {
      if (var1 != null && var1.fType == 3) {
         return ((XSModelGroupImpl)var1.fValue).fCompositor == 103;
      } else {
         return false;
      }
   }

   protected static class ParticleArray {
      XSParticleDecl[] fParticles = new XSParticleDecl[10];
      int[] fPos = new int[5];
      int fContextCount = 0;

      void pushContext() {
         ++this.fContextCount;
         if (this.fContextCount == this.fPos.length) {
            int var1 = this.fContextCount * 2;
            int[] var2 = new int[var1];
            System.arraycopy(this.fPos, 0, var2, 0, this.fContextCount);
            this.fPos = var2;
         }

         this.fPos[this.fContextCount] = this.fPos[this.fContextCount - 1];
      }

      int getParticleCount() {
         return this.fPos[this.fContextCount] - this.fPos[this.fContextCount - 1];
      }

      void addParticle(XSParticleDecl var1) {
         if (this.fPos[this.fContextCount] == this.fParticles.length) {
            int var2 = this.fPos[this.fContextCount] * 2;
            XSParticleDecl[] var3 = new XSParticleDecl[var2];
            System.arraycopy(this.fParticles, 0, var3, 0, this.fPos[this.fContextCount]);
            this.fParticles = var3;
         }

         this.fParticles[this.fPos[this.fContextCount]++] = var1;
      }

      XSParticleDecl[] popContext() {
         int var1 = this.fPos[this.fContextCount] - this.fPos[this.fContextCount - 1];
         XSParticleDecl[] var2 = null;
         if (var1 != 0) {
            var2 = new XSParticleDecl[var1];
            System.arraycopy(this.fParticles, this.fPos[this.fContextCount - 1], var2, 0, var1);

            for(int var3 = this.fPos[this.fContextCount - 1]; var3 < this.fPos[this.fContextCount]; ++var3) {
               this.fParticles[var3] = null;
            }
         }

         --this.fContextCount;
         return var2;
      }
   }
}
