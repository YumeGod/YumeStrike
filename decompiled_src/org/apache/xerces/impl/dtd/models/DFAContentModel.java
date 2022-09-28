package org.apache.xerces.impl.dtd.models;

import java.util.Hashtable;
import org.apache.xerces.xni.QName;

public class DFAContentModel implements ContentModelValidator {
   private static String fEpsilonString = "<<CMNODE_EPSILON>>";
   private static String fEOCString = "<<CMNODE_EOC>>";
   private static final boolean DEBUG_VALIDATE_CONTENT = false;
   private QName[] fElemMap = null;
   private int[] fElemMapType = null;
   private int fElemMapSize = 0;
   private boolean fMixed;
   private int fEOCPos = 0;
   private boolean[] fFinalStateFlags = null;
   private CMStateSet[] fFollowList = null;
   private CMNode fHeadNode = null;
   private int fLeafCount = 0;
   private CMLeaf[] fLeafList = null;
   private int[] fLeafListType = null;
   private int[][] fTransTable = null;
   private int fTransTableSize = 0;
   private boolean fEmptyContentIsValid = false;
   private QName fQName = new QName();

   public DFAContentModel(CMNode var1, int var2, boolean var3) {
      this.fLeafCount = var2;
      this.fMixed = var3;
      this.buildDFA(var1);
   }

   public int validate(QName[] var1, int var2, int var3) {
      if (var3 == 0) {
         return this.fEmptyContentIsValid ? -1 : 0;
      } else {
         int var4 = 0;

         for(int var5 = 0; var5 < var3; ++var5) {
            QName var6 = var1[var2 + var5];
            if (!this.fMixed || var6.localpart != null) {
               int var7;
               for(var7 = 0; var7 < this.fElemMapSize; ++var7) {
                  int var8 = this.fElemMapType[var7] & 15;
                  if (var8 == 0) {
                     if (this.fElemMap[var7].rawname == var6.rawname) {
                        break;
                     }
                  } else if (var8 == 6) {
                     String var9 = this.fElemMap[var7].uri;
                     if (var9 == null || var9 == var6.uri) {
                        break;
                     }
                  } else if (var8 == 8) {
                     if (var6.uri == null) {
                        break;
                     }
                  } else if (var8 == 7 && this.fElemMap[var7].uri != var6.uri) {
                     break;
                  }
               }

               if (var7 == this.fElemMapSize) {
                  return var5;
               }

               var4 = this.fTransTable[var4][var7];
               if (var4 == -1) {
                  return var5;
               }
            }
         }

         if (!this.fFinalStateFlags[var4]) {
            return var3;
         } else {
            return -1;
         }
      }
   }

   private void buildDFA(CMNode var1) {
      this.fQName.setValues((String)null, fEOCString, fEOCString, (String)null);
      CMLeaf var2 = new CMLeaf(this.fQName);
      this.fHeadNode = new CMBinOp(5, var1, var2);
      this.fEOCPos = this.fLeafCount;
      var2.setPosition(this.fLeafCount++);
      this.fLeafList = new CMLeaf[this.fLeafCount];
      this.fLeafListType = new int[this.fLeafCount];
      this.postTreeBuildInit(this.fHeadNode, 0);
      this.fFollowList = new CMStateSet[this.fLeafCount];

      for(int var3 = 0; var3 < this.fLeafCount; ++var3) {
         this.fFollowList[var3] = new CMStateSet(this.fLeafCount);
      }

      this.calcFollowList(this.fHeadNode);
      this.fElemMap = new QName[this.fLeafCount];
      this.fElemMapType = new int[this.fLeafCount];
      this.fElemMapSize = 0;

      int var6;
      for(int var4 = 0; var4 < this.fLeafCount; ++var4) {
         this.fElemMap[var4] = new QName();
         QName var5 = this.fLeafList[var4].getElement();

         for(var6 = 0; var6 < this.fElemMapSize && this.fElemMap[var6].rawname != var5.rawname; ++var6) {
         }

         if (var6 == this.fElemMapSize) {
            this.fElemMap[this.fElemMapSize].setValues(var5);
            this.fElemMapType[this.fElemMapSize] = this.fLeafListType[var4];
            ++this.fElemMapSize;
         }
      }

      int[] var26 = new int[this.fLeafCount + this.fElemMapSize];
      var6 = 0;

      int var8;
      for(int var7 = 0; var7 < this.fElemMapSize; ++var7) {
         for(var8 = 0; var8 < this.fLeafCount; ++var8) {
            QName var9 = this.fLeafList[var8].getElement();
            QName var10 = this.fElemMap[var7];
            if (var9.rawname == var10.rawname) {
               var26[var6++] = var8;
            }
         }

         var26[var6++] = -1;
      }

      var8 = this.fLeafCount * 4;
      CMStateSet[] var27 = new CMStateSet[var8];
      this.fFinalStateFlags = new boolean[var8];
      this.fTransTable = new int[var8][];
      CMStateSet var28 = this.fHeadNode.firstPos();
      int var11 = 0;
      int var12 = 0;
      this.fTransTable[var12] = this.makeDefStateList();
      var27[var12] = var28;
      ++var12;
      Hashtable var13 = new Hashtable();

      while(var11 < var12) {
         var28 = var27[var11];
         int[] var14 = this.fTransTable[var11];
         this.fFinalStateFlags[var11] = var28.getBit(this.fEOCPos);
         ++var11;
         CMStateSet var15 = null;
         int var16 = 0;

         for(int var17 = 0; var17 < this.fElemMapSize; ++var17) {
            if (var15 == null) {
               var15 = new CMStateSet(this.fLeafCount);
            } else {
               var15.zeroBits();
            }

            for(int var18 = var26[var16++]; var18 != -1; var18 = var26[var16++]) {
               if (var28.getBit(var18)) {
                  var15.union(this.fFollowList[var18]);
               }
            }

            if (!var15.isEmpty()) {
               Integer var19 = (Integer)var13.get(var15);
               int var20 = var19 == null ? var12 : var19;
               if (var20 == var12) {
                  var27[var12] = var15;
                  this.fTransTable[var12] = this.makeDefStateList();
                  var13.put(var15, new Integer(var12));
                  ++var12;
                  var15 = null;
               }

               var14[var17] = var20;
               if (var12 == var8) {
                  int var21 = (int)((double)var8 * 1.5);
                  CMStateSet[] var22 = new CMStateSet[var21];
                  boolean[] var23 = new boolean[var21];
                  int[][] var24 = new int[var21][];

                  for(int var25 = 0; var25 < var8; ++var25) {
                     var22[var25] = var27[var25];
                     var23[var25] = this.fFinalStateFlags[var25];
                     var24[var25] = this.fTransTable[var25];
                  }

                  var8 = var21;
                  var27 = var22;
                  this.fFinalStateFlags = var23;
                  this.fTransTable = var24;
               }
            }
         }
      }

      this.fEmptyContentIsValid = ((CMBinOp)this.fHeadNode).getLeft().isNullable();
      this.fHeadNode = null;
      this.fLeafList = null;
      this.fFollowList = null;
   }

   private void calcFollowList(CMNode var1) {
      if (var1.type() == 4) {
         this.calcFollowList(((CMBinOp)var1).getLeft());
         this.calcFollowList(((CMBinOp)var1).getRight());
      } else {
         CMStateSet var2;
         CMStateSet var3;
         int var4;
         if (var1.type() == 5) {
            this.calcFollowList(((CMBinOp)var1).getLeft());
            this.calcFollowList(((CMBinOp)var1).getRight());
            var2 = ((CMBinOp)var1).getLeft().lastPos();
            var3 = ((CMBinOp)var1).getRight().firstPos();

            for(var4 = 0; var4 < this.fLeafCount; ++var4) {
               if (var2.getBit(var4)) {
                  this.fFollowList[var4].union(var3);
               }
            }
         } else if (var1.type() != 2 && var1.type() != 3) {
            if (var1.type() == 1) {
               this.calcFollowList(((CMUniOp)var1).getChild());
            }
         } else {
            this.calcFollowList(((CMUniOp)var1).getChild());
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
      if (var4 != 4 && var4 != 5) {
         if (var1.type() == 2) {
            System.out.print("Rep Node ");
            if (var1.isNullable()) {
               System.out.print("Nullable ");
            }

            System.out.print("firstPos=");
            System.out.print(var1.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(var1.lastPos().toString());
            this.dumpTree(((CMUniOp)var1).getChild(), var2 + 1);
         } else {
            if (var1.type() != 0) {
               throw new RuntimeException("ImplementationMessages.VAL_NIICM");
            }

            System.out.print("Leaf: (pos=" + ((CMLeaf)var1).getPosition() + "), " + ((CMLeaf)var1).getElement() + "(elemIndex=" + ((CMLeaf)var1).getElement() + ") ");
            if (var1.isNullable()) {
               System.out.print(" Nullable ");
            }

            System.out.print("firstPos=");
            System.out.print(var1.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(var1.lastPos().toString());
         }
      } else {
         if (var4 == 4) {
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
         this.dumpTree(((CMBinOp)var1).getLeft(), var2 + 1);
         this.dumpTree(((CMBinOp)var1).getRight(), var2 + 1);
      }

   }

   private int[] makeDefStateList() {
      int[] var1 = new int[this.fElemMapSize];

      for(int var2 = 0; var2 < this.fElemMapSize; ++var2) {
         var1[var2] = -1;
      }

      return var1;
   }

   private int postTreeBuildInit(CMNode var1, int var2) {
      var1.setMaxStates(this.fLeafCount);
      QName var3;
      if ((var1.type() & 15) != 6 && (var1.type() & 15) != 8 && (var1.type() & 15) != 7) {
         if (var1.type() != 4 && var1.type() != 5) {
            if (var1.type() != 2 && var1.type() != 3 && var1.type() != 1) {
               if (var1.type() != 0) {
                  throw new RuntimeException("ImplementationMessages.VAL_NIICM: type=" + var1.type());
               }

               var3 = ((CMLeaf)var1).getElement();
               if (var3.localpart != fEpsilonString) {
                  this.fLeafList[var2] = (CMLeaf)var1;
                  this.fLeafListType[var2] = 0;
                  ++var2;
               }
            } else {
               var2 = this.postTreeBuildInit(((CMUniOp)var1).getChild(), var2);
            }
         } else {
            var2 = this.postTreeBuildInit(((CMBinOp)var1).getLeft(), var2);
            var2 = this.postTreeBuildInit(((CMBinOp)var1).getRight(), var2);
         }
      } else {
         var3 = new QName((String)null, (String)null, (String)null, ((CMAny)var1).getURI());
         this.fLeafList[var2] = new CMLeaf(var3, ((CMAny)var1).getPosition());
         this.fLeafListType[var2] = var1.type();
         ++var2;
      }

      return var2;
   }

   static {
      fEpsilonString = fEpsilonString.intern();
      fEOCString = fEOCString.intern();
   }
}
