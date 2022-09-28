package org.apache.xerces.impl.xs.models;

import java.util.Hashtable;
import java.util.Vector;
import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMStateSet;
import org.apache.xerces.impl.xs.SubstitutionGroupHandler;
import org.apache.xerces.impl.xs.XMLSchemaException;
import org.apache.xerces.impl.xs.XSConstraints;
import org.apache.xerces.impl.xs.XSElementDecl;
import org.apache.xerces.impl.xs.XSWildcardDecl;
import org.apache.xerces.xni.QName;

public class XSDFACM implements XSCMValidator {
   private static final boolean DEBUG = false;
   private static final boolean DEBUG_VALIDATE_CONTENT = false;
   private Object[] fElemMap = null;
   private int[] fElemMapType = null;
   private int[] fElemMapId = null;
   private int fElemMapSize = 0;
   private boolean[] fFinalStateFlags = null;
   private CMStateSet[] fFollowList = null;
   private CMNode fHeadNode = null;
   private int fLeafCount = 0;
   private XSCMLeaf[] fLeafList = null;
   private int[] fLeafListType = null;
   private int[][] fTransTable = null;
   private int fTransTableSize = 0;
   private static long time = 0L;

   public XSDFACM(CMNode var1, int var2) {
      this.fLeafCount = var2;
      this.buildDFA(var1);
   }

   public boolean isFinalState(int var1) {
      return var1 < 0 ? false : this.fFinalStateFlags[var1];
   }

   public Object oneTransition(QName var1, int[] var2, SubstitutionGroupHandler var3) {
      int var4 = var2[0];
      if (var4 != -1 && var4 != -2) {
         int var5 = 0;
         int var6 = 0;

         Object var7;
         for(var7 = null; var6 < this.fElemMapSize; ++var6) {
            var5 = this.fTransTable[var4][var6];
            if (var5 != -1) {
               int var8 = this.fElemMapType[var6];
               if (var8 == 1) {
                  var7 = var3.getMatchingElemDecl(var1, (XSElementDecl)this.fElemMap[var6]);
                  if (var7 != null) {
                     break;
                  }
               } else if (var8 == 2 && ((XSWildcardDecl)this.fElemMap[var6]).allowNamespace(var1.uri)) {
                  var7 = this.fElemMap[var6];
                  break;
               }
            }
         }

         if (var6 == this.fElemMapSize) {
            var2[1] = var2[0];
            var2[0] = -1;
            return this.findMatchingDecl(var1, var3);
         } else {
            var2[0] = var5;
            return var7;
         }
      } else {
         if (var4 == -1) {
            var2[0] = -2;
         }

         return this.findMatchingDecl(var1, var3);
      }
   }

   Object findMatchingDecl(QName var1, SubstitutionGroupHandler var2) {
      XSElementDecl var3 = null;

      for(int var4 = 0; var4 < this.fElemMapSize; ++var4) {
         int var5 = this.fElemMapType[var4];
         if (var5 == 1) {
            var3 = var2.getMatchingElemDecl(var1, (XSElementDecl)this.fElemMap[var4]);
            if (var3 != null) {
               return var3;
            }
         } else if (var5 == 2 && ((XSWildcardDecl)this.fElemMap[var4]).allowNamespace(var1.uri)) {
            return this.fElemMap[var4];
         }
      }

      return null;
   }

   public int[] startContentModel() {
      int[] var1 = new int[]{0, 0};
      return var1;
   }

   public boolean endContentModel(int[] var1) {
      return this.fFinalStateFlags[var1[0]];
   }

   private void buildDFA(CMNode var1) {
      int var2 = this.fLeafCount;
      XSCMLeaf var3 = new XSCMLeaf(1, (Object)null, -1, this.fLeafCount++);
      this.fHeadNode = new XSCMBinOp(102, var1, var3);
      this.fLeafList = new XSCMLeaf[this.fLeafCount];
      this.fLeafListType = new int[this.fLeafCount];
      this.postTreeBuildInit(this.fHeadNode);
      this.fFollowList = new CMStateSet[this.fLeafCount];

      for(int var4 = 0; var4 < this.fLeafCount; ++var4) {
         this.fFollowList[var4] = new CMStateSet(this.fLeafCount);
      }

      this.calcFollowList(this.fHeadNode);
      this.fElemMap = new Object[this.fLeafCount];
      this.fElemMapType = new int[this.fLeafCount];
      this.fElemMapId = new int[this.fLeafCount];
      this.fElemMapSize = 0;

      int var7;
      for(int var5 = 0; var5 < this.fLeafCount; ++var5) {
         this.fElemMap[var5] = null;
         int var6 = 0;

         for(var7 = this.fLeafList[var5].getParticleId(); var6 < this.fElemMapSize && var7 != this.fElemMapId[var6]; ++var6) {
         }

         if (var6 == this.fElemMapSize) {
            this.fElemMap[this.fElemMapSize] = this.fLeafList[var5].getLeaf();
            this.fElemMapType[this.fElemMapSize] = this.fLeafListType[var5];
            this.fElemMapId[this.fElemMapSize] = var7;
            ++this.fElemMapSize;
         }
      }

      --this.fElemMapSize;
      int[] var27 = new int[this.fLeafCount + this.fElemMapSize];
      var7 = 0;

      int var9;
      for(int var8 = 0; var8 < this.fElemMapSize; ++var8) {
         var9 = this.fElemMapId[var8];

         for(int var10 = 0; var10 < this.fLeafCount; ++var10) {
            if (var9 == this.fLeafList[var10].getParticleId()) {
               var27[var7++] = var10;
            }
         }

         var27[var7++] = -1;
      }

      var9 = this.fLeafCount * 4;
      CMStateSet[] var28 = new CMStateSet[var9];
      this.fFinalStateFlags = new boolean[var9];
      this.fTransTable = new int[var9][];
      CMStateSet var11 = this.fHeadNode.firstPos();
      int var12 = 0;
      int var13 = 0;
      this.fTransTable[var13] = this.makeDefStateList();
      var28[var13] = var11;
      ++var13;
      Hashtable var14 = new Hashtable();

      while(var12 < var13) {
         var11 = var28[var12];
         int[] var15 = this.fTransTable[var12];
         this.fFinalStateFlags[var12] = var11.getBit(var2);
         ++var12;
         CMStateSet var16 = null;
         int var17 = 0;

         for(int var18 = 0; var18 < this.fElemMapSize; ++var18) {
            if (var16 == null) {
               var16 = new CMStateSet(this.fLeafCount);
            } else {
               var16.zeroBits();
            }

            for(int var19 = var27[var17++]; var19 != -1; var19 = var27[var17++]) {
               if (var11.getBit(var19)) {
                  var16.union(this.fFollowList[var19]);
               }
            }

            if (!var16.isEmpty()) {
               Integer var20 = (Integer)var14.get(var16);
               int var21 = var20 == null ? var13 : var20;
               if (var21 == var13) {
                  var28[var13] = var16;
                  this.fTransTable[var13] = this.makeDefStateList();
                  var14.put(var16, new Integer(var13));
                  ++var13;
                  var16 = null;
               }

               var15[var18] = var21;
               if (var13 == var9) {
                  int var22 = (int)((double)var9 * 1.5);
                  CMStateSet[] var23 = new CMStateSet[var22];
                  boolean[] var24 = new boolean[var22];
                  int[][] var25 = new int[var22][];

                  for(int var26 = 0; var26 < var9; ++var26) {
                     var23[var26] = var28[var26];
                     var24[var26] = this.fFinalStateFlags[var26];
                     var25[var26] = this.fTransTable[var26];
                  }

                  var9 = var22;
                  var28 = var23;
                  this.fFinalStateFlags = var24;
                  this.fTransTable = var25;
               }
            }
         }
      }

      this.fHeadNode = null;
      this.fLeafList = null;
      this.fFollowList = null;
      this.fLeafListType = null;
      this.fElemMapId = null;
   }

   private void calcFollowList(CMNode var1) {
      if (var1.type() == 101) {
         this.calcFollowList(((XSCMBinOp)var1).getLeft());
         this.calcFollowList(((XSCMBinOp)var1).getRight());
      } else {
         CMStateSet var2;
         CMStateSet var3;
         int var4;
         if (var1.type() == 102) {
            this.calcFollowList(((XSCMBinOp)var1).getLeft());
            this.calcFollowList(((XSCMBinOp)var1).getRight());
            var2 = ((XSCMBinOp)var1).getLeft().lastPos();
            var3 = ((XSCMBinOp)var1).getRight().firstPos();

            for(var4 = 0; var4 < this.fLeafCount; ++var4) {
               if (var2.getBit(var4)) {
                  this.fFollowList[var4].union(var3);
               }
            }
         } else if (var1.type() != 4 && var1.type() != 6) {
            if (var1.type() == 5) {
               this.calcFollowList(((XSCMUniOp)var1).getChild());
            }
         } else {
            this.calcFollowList(((XSCMUniOp)var1).getChild());
            var2 = var1.firstPos();
            var3 = var1.lastPos();

            for(var4 = 0; var4 < this.fLeafCount; ++var4) {
               if (var3.getBit(var4)) {
                  this.fFollowList[var4].union(var2);
               }
            }
         }
      }

   }

   private void dumpTree(CMNode var1, int var2) {
      for(int var3 = 0; var3 < var2; ++var3) {
         System.out.print("   ");
      }

      int var4 = var1.type();
      switch (var4) {
         case 1:
            System.out.print("Leaf: (pos=" + ((XSCMLeaf)var1).getPosition() + "), " + "(elemIndex=" + ((XSCMLeaf)var1).getLeaf() + ") ");
            if (var1.isNullable()) {
               System.out.print(" Nullable ");
            }

            System.out.print("firstPos=");
            System.out.print(var1.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(var1.lastPos().toString());
            break;
         case 2:
            System.out.print("Any Node: ");
            System.out.print("firstPos=");
            System.out.print(var1.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(var1.lastPos().toString());
            break;
         case 4:
         case 5:
         case 6:
            System.out.print("Rep Node ");
            if (var1.isNullable()) {
               System.out.print("Nullable ");
            }

            System.out.print("firstPos=");
            System.out.print(var1.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(var1.lastPos().toString());
            this.dumpTree(((XSCMUniOp)var1).getChild(), var2 + 1);
            break;
         case 101:
         case 102:
            if (var4 == 101) {
               System.out.print("Choice Node ");
            } else {
               System.out.print("Seq Node ");
            }

            if (var1.isNullable()) {
               System.out.print("Nullable ");
            }

            System.out.print("firstPos=");
            System.out.print(var1.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(var1.lastPos().toString());
            this.dumpTree(((XSCMBinOp)var1).getLeft(), var2 + 1);
            this.dumpTree(((XSCMBinOp)var1).getRight(), var2 + 1);
            break;
         default:
            throw new RuntimeException("ImplementationMessages.VAL_NIICM");
      }

   }

   private int[] makeDefStateList() {
      int[] var1 = new int[this.fElemMapSize];

      for(int var2 = 0; var2 < this.fElemMapSize; ++var2) {
         var1[var2] = -1;
      }

      return var1;
   }

   private void postTreeBuildInit(CMNode var1) throws RuntimeException {
      var1.setMaxStates(this.fLeafCount);
      XSCMLeaf var2 = null;
      boolean var3 = false;
      int var4;
      if (var1.type() == 2) {
         var2 = (XSCMLeaf)var1;
         var4 = var2.getPosition();
         this.fLeafList[var4] = var2;
         this.fLeafListType[var4] = 2;
      } else if (var1.type() != 101 && var1.type() != 102) {
         if (var1.type() != 4 && var1.type() != 6 && var1.type() != 5) {
            if (var1.type() != 1) {
               throw new RuntimeException("ImplementationMessages.VAL_NIICM");
            }

            var2 = (XSCMLeaf)var1;
            var4 = var2.getPosition();
            this.fLeafList[var4] = var2;
            this.fLeafListType[var4] = 1;
         } else {
            this.postTreeBuildInit(((XSCMUniOp)var1).getChild());
         }
      } else {
         this.postTreeBuildInit(((XSCMBinOp)var1).getLeft());
         this.postTreeBuildInit(((XSCMBinOp)var1).getRight());
      }

   }

   public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler var1) throws XMLSchemaException {
      byte[][] var2 = new byte[this.fElemMapSize][this.fElemMapSize];

      int var4;
      int var5;
      for(int var3 = 0; var3 < this.fTransTable.length && this.fTransTable[var3] != null; ++var3) {
         for(var4 = 0; var4 < this.fElemMapSize; ++var4) {
            for(var5 = var4 + 1; var5 < this.fElemMapSize; ++var5) {
               if (this.fTransTable[var3][var4] != -1 && this.fTransTable[var3][var5] != -1 && var2[var4][var5] == 0) {
                  var2[var4][var5] = (byte)(XSConstraints.overlapUPA(this.fElemMap[var4], this.fElemMap[var5], var1) ? 1 : -1);
               }
            }
         }
      }

      for(var4 = 0; var4 < this.fElemMapSize; ++var4) {
         for(var5 = 0; var5 < this.fElemMapSize; ++var5) {
            if (var2[var4][var5] == 1) {
               throw new XMLSchemaException("cos-nonambig", new Object[]{this.fElemMap[var4].toString(), this.fElemMap[var5].toString()});
            }
         }
      }

      for(var5 = 0; var5 < this.fElemMapSize; ++var5) {
         if (this.fElemMapType[var5] == 2) {
            XSWildcardDecl var6 = (XSWildcardDecl)this.fElemMap[var5];
            if (var6.fType == 3 || var6.fType == 2) {
               return true;
            }
         }
      }

      return false;
   }

   public Vector whatCanGoHere(int[] var1) {
      int var2 = var1[0];
      if (var2 < 0) {
         var2 = var1[1];
      }

      Vector var3 = new Vector();

      for(int var4 = 0; var4 < this.fElemMapSize; ++var4) {
         if (this.fTransTable[var2][var4] != -1) {
            var3.addElement(this.fElemMap[var4]);
         }
      }

      return var3;
   }
}
