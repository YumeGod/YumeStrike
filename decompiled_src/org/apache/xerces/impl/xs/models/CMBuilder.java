package org.apache.xerces.impl.xs.models;

import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.impl.xs.XSDeclarationPool;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSModelGroupImpl;
import org.apache.xerces.impl.xs.XSParticleDecl;

public class CMBuilder {
   private XSDeclarationPool fDeclPool = null;
   private static XSEmptyCM fEmptyCM = new XSEmptyCM();
   private int fLeafCount;
   private int fParticleCount;
   private CMNodeFactory fNodeFactory;

   public CMBuilder(CMNodeFactory var1) {
      this.fDeclPool = null;
      this.fNodeFactory = var1;
   }

   public void setDeclPool(XSDeclarationPool var1) {
      this.fDeclPool = var1;
   }

   public XSCMValidator getContentModel(XSComplexTypeDecl var1) {
      short var2 = var1.getContentType();
      if (var2 != 1 && var2 != 0) {
         XSParticleDecl var3 = (XSParticleDecl)var1.getParticle();
         if (var3 == null) {
            return fEmptyCM;
         } else {
            Object var4 = null;
            if (var3.fType == 3 && ((XSModelGroupImpl)var3.fValue).fCompositor == 103) {
               var4 = this.createAllCM(var3);
            } else {
               var4 = this.createDFACM(var3);
            }

            this.fNodeFactory.resetNodeCount();
            if (var4 == null) {
               var4 = fEmptyCM;
            }

            return (XSCMValidator)var4;
         }
      } else {
         return null;
      }
   }

   XSCMValidator createAllCM(XSParticleDecl var1) {
      if (var1.fMaxOccurs == 0) {
         return null;
      } else {
         XSModelGroupImpl var2 = (XSModelGroupImpl)var1.fValue;
         XSAllCM var3 = new XSAllCM(var1.fMinOccurs == 0, var2.fParticleCount);

         for(int var4 = 0; var4 < var2.fParticleCount; ++var4) {
            var3.addElement((XSElementDecl)var2.fParticles[var4].fValue, var2.fParticles[var4].fMinOccurs == 0);
         }

         return var3;
      }
   }

   XSCMValidator createDFACM(XSParticleDecl var1) {
      this.fLeafCount = 0;
      this.fParticleCount = 0;
      CMNode var2 = this.buildSyntaxTree(var1);
      return var2 == null ? null : new XSDFACM(var2, this.fLeafCount);
   }

   private CMNode buildSyntaxTree(XSParticleDecl var1) {
      int var2 = var1.fMaxOccurs;
      int var3 = var1.fMinOccurs;
      short var4 = var1.fType;
      CMNode var5 = null;
      if (var4 != 2 && var4 != 1) {
         if (var4 == 3) {
            XSModelGroupImpl var6 = (XSModelGroupImpl)var1.fValue;
            CMNode var7 = null;
            boolean var8 = false;

            for(int var9 = 0; var9 < var6.fParticleCount; ++var9) {
               var7 = this.buildSyntaxTree(var6.fParticles[var9]);
               if (var7 != null) {
                  if (var5 == null) {
                     var5 = var7;
                  } else {
                     var5 = this.fNodeFactory.getCMBinOpNode(var6.fCompositor, var5, var7);
                     var8 = true;
                  }
               }
            }

            if (var5 != null) {
               if (var6.fCompositor == 101 && !var8 && var6.fParticleCount > 1) {
                  var5 = this.fNodeFactory.getCMUniOpNode(5, var5);
               }

               var5 = this.expandContentModel(var5, var3, var2);
            }
         }
      } else {
         var5 = this.fNodeFactory.getCMLeafNode(var1.fType, var1.fValue, this.fParticleCount++, this.fLeafCount++);
         var5 = this.expandContentModel(var5, var3, var2);
      }

      return var5;
   }

   private CMNode expandContentModel(CMNode var1, int var2, int var3) {
      CMNode var4 = null;
      if (var2 == 1 && var3 == 1) {
         var4 = var1;
      } else if (var2 == 0 && var3 == 1) {
         var4 = this.fNodeFactory.getCMUniOpNode(5, var1);
      } else if (var2 == 0 && var3 == -1) {
         var4 = this.fNodeFactory.getCMUniOpNode(4, var1);
      } else if (var2 == 1 && var3 == -1) {
         var4 = this.fNodeFactory.getCMUniOpNode(6, var1);
      } else if (var3 == -1) {
         var4 = this.fNodeFactory.getCMUniOpNode(6, var1);
         var4 = this.fNodeFactory.getCMBinOpNode(102, this.multiNodes(var1, var2 - 1, true), var4);
      } else {
         if (var2 > 0) {
            var4 = this.multiNodes(var1, var2, false);
         }

         if (var3 > var2) {
            var1 = this.fNodeFactory.getCMUniOpNode(5, var1);
            if (var4 == null) {
               var4 = this.multiNodes(var1, var3 - var2, false);
            } else {
               var4 = this.fNodeFactory.getCMBinOpNode(102, var4, this.multiNodes(var1, var3 - var2, true));
            }
         }
      }

      return var4;
   }

   private CMNode multiNodes(CMNode var1, int var2, boolean var3) {
      if (var2 == 0) {
         return null;
      } else if (var2 == 1) {
         return var3 ? this.copyNode(var1) : var1;
      } else {
         int var4 = var2 / 2;
         return this.fNodeFactory.getCMBinOpNode(102, this.multiNodes(var1, var4, var3), this.multiNodes(var1, var2 - var4, true));
      }
   }

   private CMNode copyNode(CMNode var1) {
      int var2 = var1.type();
      if (var2 != 101 && var2 != 102) {
         if (var2 != 4 && var2 != 6 && var2 != 5) {
            if (var2 == 1 || var2 == 2) {
               XSCMLeaf var5 = (XSCMLeaf)var1;
               var1 = this.fNodeFactory.getCMLeafNode(var5.type(), var5.getLeaf(), var5.getParticleId(), this.fLeafCount++);
            }
         } else {
            XSCMUniOp var4 = (XSCMUniOp)var1;
            var1 = this.fNodeFactory.getCMUniOpNode(var2, this.copyNode(var4.getChild()));
         }
      } else {
         XSCMBinOp var3 = (XSCMBinOp)var1;
         var1 = this.fNodeFactory.getCMBinOpNode(var2, this.copyNode(var3.getLeft()), this.copyNode(var3.getRight()));
      }

      return var1;
   }
}
