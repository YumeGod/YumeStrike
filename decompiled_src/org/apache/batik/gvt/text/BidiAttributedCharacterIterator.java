package org.apache.batik.gvt.text;

import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BidiAttributedCharacterIterator implements AttributedCharacterIterator {
   private AttributedCharacterIterator reorderedACI;
   private FontRenderContext frc;
   private int chunkStart;
   private int[] newCharOrder;
   private static final Float FLOAT_NAN = new Float(Float.NaN);

   protected BidiAttributedCharacterIterator(AttributedCharacterIterator var1, FontRenderContext var2, int var3, int[] var4) {
      this.reorderedACI = var1;
      this.frc = var2;
      this.chunkStart = var3;
      this.newCharOrder = var4;
   }

   public BidiAttributedCharacterIterator(AttributedCharacterIterator var1, FontRenderContext var2, int var3) {
      this.frc = var2;
      this.chunkStart = var3;
      var1.first();
      int var4 = var1.getEndIndex() - var1.getBeginIndex();
      StringBuffer var6 = new StringBuffer(var4);
      char var7 = var1.first();

      int var8;
      for(var8 = 0; var8 < var4; ++var8) {
         var6.append(var7);
         var7 = var1.next();
      }

      AttributedString var5 = new AttributedString(var6.toString());
      var8 = var1.getBeginIndex();
      int var9 = var1.getEndIndex();

      int var12;
      for(int var10 = var8; var10 < var9; var10 = var12) {
         var1.setIndex(var10);
         Map var11 = var1.getAttributes();
         var12 = var1.getRunLimit();
         HashMap var13 = new HashMap(var11.size());
         Iterator var14 = var11.entrySet().iterator();

         while(var14.hasNext()) {
            Map.Entry var15 = (Map.Entry)var14.next();
            Object var16 = var15.getKey();
            if (var16 != null) {
               Object var17 = var15.getValue();
               if (var17 != null) {
                  var13.put(var16, var17);
               }
            }
         }

         var5.addAttributes(var13, var10 - var8, var12 - var8);
      }

      TextLayout var25 = new TextLayout(var5.getIterator(), var2);
      int[] var26 = new int[var4];
      int[] var27 = new int[var4];
      var9 = 0;
      byte var28 = var25.getCharacterLevel(0);
      var26[0] = 0;
      var27[0] = var28;
      byte var29 = var28;

      int var31;
      for(var12 = 1; var12 < var4; ++var12) {
         var31 = var25.getCharacterLevel(var12);
         var26[var12] = var12;
         var27[var12] = var31;
         if (var31 != var28) {
            var5.addAttribute(GVTAttributedCharacterIterator.TextAttribute.BIDI_LEVEL, new Integer(var28), var9, var12);
            var9 = var12;
            var28 = (byte)var31;
            if (var31 > var29) {
               var29 = (byte)var31;
            }
         }
      }

      var5.addAttribute(GVTAttributedCharacterIterator.TextAttribute.BIDI_LEVEL, new Integer(var28), var9, var4);
      var1 = var5.getIterator();
      if (var9 == 0 && var28 == 0) {
         this.reorderedACI = var1;
         this.newCharOrder = new int[var4];

         for(var12 = 0; var12 < var4; ++var12) {
            this.newCharOrder[var12] = var3 + var12;
         }

      } else {
         this.newCharOrder = this.doBidiReorder(var26, var27, var4, var29);
         StringBuffer var30 = new StringBuffer(var4);
         var31 = 0;

         int var36;
         int var37;
         for(int var32 = 0; var32 < var4; ++var32) {
            int var34 = this.newCharOrder[var32];
            var36 = var1.setIndex(var34);
            if (var34 == 0) {
               var31 = var32;
            }

            var37 = var25.getCharacterLevel(var34);
            if ((var37 & 1) != 0) {
               var36 = (char)mirrorChar(var36);
            }

            var30.append((char)var36);
         }

         AttributedString var33 = new AttributedString(var30.toString());
         Map[] var35 = new Map[var4];
         var36 = var1.getBeginIndex();
         var37 = var1.getEndIndex();

         Map var19;
         int var20;
         for(int var18 = var36; var18 < var37; var18 = var20) {
            var1.setIndex(var18);
            var19 = var1.getAttributes();
            var20 = var1.getRunLimit();

            for(int var21 = var18; var21 < var20; ++var21) {
               var35[var21 - var36] = var19;
            }
         }

         var9 = 0;
         var19 = var35[this.newCharOrder[0]];

         for(var20 = 1; var20 < var4; ++var20) {
            Map var39 = var35[this.newCharOrder[var20]];
            if (var39 != var19) {
               var33.addAttributes(var19, var9, var20);
               var19 = var39;
               var9 = var20;
            }
         }

         var33.addAttributes(var19, var9, var4);
         var1.first();
         Float var38 = (Float)var1.getAttribute(GVTAttributedCharacterIterator.TextAttribute.X);
         if (var38 != null && !var38.isNaN()) {
            var33.addAttribute(GVTAttributedCharacterIterator.TextAttribute.X, FLOAT_NAN, var31, var31 + 1);
            var33.addAttribute(GVTAttributedCharacterIterator.TextAttribute.X, var38, 0, 1);
         }

         Float var40 = (Float)var1.getAttribute(GVTAttributedCharacterIterator.TextAttribute.Y);
         if (var40 != null && !var40.isNaN()) {
            var33.addAttribute(GVTAttributedCharacterIterator.TextAttribute.Y, FLOAT_NAN, var31, var31 + 1);
            var33.addAttribute(GVTAttributedCharacterIterator.TextAttribute.Y, var40, 0, 1);
         }

         Float var22 = (Float)var1.getAttribute(GVTAttributedCharacterIterator.TextAttribute.DX);
         if (var22 != null && !var22.isNaN()) {
            var33.addAttribute(GVTAttributedCharacterIterator.TextAttribute.DX, FLOAT_NAN, var31, var31 + 1);
            var33.addAttribute(GVTAttributedCharacterIterator.TextAttribute.DX, var22, 0, 1);
         }

         Float var23 = (Float)var1.getAttribute(GVTAttributedCharacterIterator.TextAttribute.DY);
         if (var23 != null && !var23.isNaN()) {
            var33.addAttribute(GVTAttributedCharacterIterator.TextAttribute.DY, FLOAT_NAN, var31, var31 + 1);
            var33.addAttribute(GVTAttributedCharacterIterator.TextAttribute.DY, var23, 0, 1);
         }

         var33 = ArabicTextHandler.assignArabicForms(var33);

         for(int var24 = 0; var24 < this.newCharOrder.length; ++var24) {
            int[] var10000 = this.newCharOrder;
            var10000[var24] += var3;
         }

         this.reorderedACI = var33.getIterator();
      }
   }

   public int[] getCharMap() {
      return this.newCharOrder;
   }

   private int[] doBidiReorder(int[] var1, int[] var2, int var3, int var4) {
      if (var4 == 0) {
         return var1;
      } else {
         int var5 = 0;

         while(var5 < var3) {
            while(var5 < var3 && var2[var5] < var4) {
               ++var5;
            }

            if (var5 == var3) {
               break;
            }

            int var6;
            for(var6 = var5++; var5 < var3 && var2[var5] == var4; ++var5) {
            }

            int var7 = var5 - 1;
            int var8 = (var7 - var6 >> 1) + 1;

            for(int var9 = 0; var9 < var8; ++var9) {
               int var10 = var1[var6 + var9];
               var1[var6 + var9] = var1[var7 - var9];
               var1[var7 - var9] = var10;
               var2[var6 + var9] = var4 - 1;
               var2[var7 - var9] = var4 - 1;
            }
         }

         return this.doBidiReorder(var1, var2, var3, var4 - 1);
      }
   }

   public Set getAllAttributeKeys() {
      return this.reorderedACI.getAllAttributeKeys();
   }

   public Object getAttribute(AttributedCharacterIterator.Attribute var1) {
      return this.reorderedACI.getAttribute(var1);
   }

   public Map getAttributes() {
      return this.reorderedACI.getAttributes();
   }

   public int getRunLimit() {
      return this.reorderedACI.getRunLimit();
   }

   public int getRunLimit(AttributedCharacterIterator.Attribute var1) {
      return this.reorderedACI.getRunLimit(var1);
   }

   public int getRunLimit(Set var1) {
      return this.reorderedACI.getRunLimit(var1);
   }

   public int getRunStart() {
      return this.reorderedACI.getRunStart();
   }

   public int getRunStart(AttributedCharacterIterator.Attribute var1) {
      return this.reorderedACI.getRunStart(var1);
   }

   public int getRunStart(Set var1) {
      return this.reorderedACI.getRunStart(var1);
   }

   public Object clone() {
      return new BidiAttributedCharacterIterator((AttributedCharacterIterator)this.reorderedACI.clone(), this.frc, this.chunkStart, (int[])this.newCharOrder.clone());
   }

   public char current() {
      return this.reorderedACI.current();
   }

   public char first() {
      return this.reorderedACI.first();
   }

   public int getBeginIndex() {
      return this.reorderedACI.getBeginIndex();
   }

   public int getEndIndex() {
      return this.reorderedACI.getEndIndex();
   }

   public int getIndex() {
      return this.reorderedACI.getIndex();
   }

   public char last() {
      return this.reorderedACI.last();
   }

   public char next() {
      return this.reorderedACI.next();
   }

   public char previous() {
      return this.reorderedACI.previous();
   }

   public char setIndex(int var1) {
      return this.reorderedACI.setIndex(var1);
   }

   public static int mirrorChar(int var0) {
      switch (var0) {
         case 40:
            return 41;
         case 41:
            return 40;
         case 60:
            return 62;
         case 62:
            return 60;
         case 91:
            return 93;
         case 93:
            return 91;
         case 123:
            return 125;
         case 125:
            return 123;
         case 171:
            return 187;
         case 187:
            return 171;
         case 8249:
            return 8250;
         case 8250:
            return 8249;
         case 8261:
            return 8262;
         case 8262:
            return 8261;
         case 8317:
            return 8318;
         case 8318:
            return 8317;
         case 8333:
            return 8334;
         case 8334:
            return 8333;
         case 8712:
            return 8715;
         case 8713:
            return 8716;
         case 8714:
            return 8717;
         case 8715:
            return 8712;
         case 8716:
            return 8713;
         case 8717:
            return 8714;
         case 8764:
            return 8765;
         case 8765:
            return 8764;
         case 8771:
            return 8909;
         case 8786:
            return 8787;
         case 8787:
            return 8786;
         case 8788:
            return 8789;
         case 8789:
            return 8788;
         case 8804:
            return 8805;
         case 8805:
            return 8804;
         case 8806:
            return 8807;
         case 8807:
            return 8806;
         case 8808:
            return 8809;
         case 8809:
            return 8808;
         case 8810:
            return 8811;
         case 8811:
            return 8810;
         case 8814:
            return 8815;
         case 8815:
            return 8814;
         case 8816:
            return 8817;
         case 8817:
            return 8816;
         case 8818:
            return 8819;
         case 8819:
            return 8818;
         case 8820:
            return 8821;
         case 8821:
            return 8820;
         case 8822:
            return 8823;
         case 8823:
            return 8822;
         case 8824:
            return 8825;
         case 8825:
            return 8824;
         case 8826:
            return 8827;
         case 8827:
            return 8826;
         case 8828:
            return 8829;
         case 8829:
            return 8828;
         case 8830:
            return 8831;
         case 8831:
            return 8830;
         case 8832:
            return 8833;
         case 8833:
            return 8832;
         case 8834:
            return 8835;
         case 8835:
            return 8834;
         case 8836:
            return 8837;
         case 8837:
            return 8836;
         case 8838:
            return 8839;
         case 8839:
            return 8838;
         case 8840:
            return 8841;
         case 8841:
            return 8840;
         case 8842:
            return 8843;
         case 8843:
            return 8842;
         case 8847:
            return 8848;
         case 8848:
            return 8847;
         case 8849:
            return 8850;
         case 8850:
            return 8849;
         case 8866:
            return 8867;
         case 8867:
            return 8866;
         case 8880:
            return 8881;
         case 8881:
            return 8880;
         case 8882:
            return 8883;
         case 8883:
            return 8882;
         case 8884:
            return 8885;
         case 8885:
            return 8884;
         case 8886:
            return 8887;
         case 8887:
            return 8886;
         case 8905:
            return 8906;
         case 8906:
            return 8905;
         case 8907:
            return 8908;
         case 8908:
            return 8907;
         case 8909:
            return 8771;
         case 8912:
            return 8913;
         case 8913:
            return 8912;
         case 8918:
            return 8919;
         case 8919:
            return 8918;
         case 8920:
            return 8921;
         case 8921:
            return 8920;
         case 8922:
            return 8923;
         case 8923:
            return 8922;
         case 8924:
            return 8925;
         case 8925:
            return 8924;
         case 8926:
            return 8927;
         case 8927:
            return 8926;
         case 8928:
            return 8929;
         case 8929:
            return 8928;
         case 8930:
            return 8931;
         case 8931:
            return 8930;
         case 8932:
            return 8933;
         case 8933:
            return 8932;
         case 8934:
            return 8935;
         case 8935:
            return 8934;
         case 8936:
            return 8937;
         case 8937:
            return 8936;
         case 8938:
            return 8939;
         case 8939:
            return 8938;
         case 8940:
            return 8941;
         case 8941:
            return 8940;
         case 8944:
            return 8945;
         case 8945:
            return 8944;
         case 8968:
            return 8969;
         case 8969:
            return 8968;
         case 8970:
            return 8971;
         case 8971:
            return 8970;
         case 9001:
            return 9002;
         case 9002:
            return 9001;
         case 12296:
            return 12297;
         case 12297:
            return 12296;
         case 12298:
            return 12299;
         case 12299:
            return 12298;
         case 12300:
            return 12301;
         case 12301:
            return 12300;
         case 12302:
            return 12303;
         case 12303:
            return 12302;
         case 12304:
            return 12305;
         case 12305:
            return 12304;
         case 12308:
            return 12309;
         case 12309:
            return 12308;
         case 12310:
            return 12311;
         case 12311:
            return 12310;
         case 12312:
            return 12313;
         case 12313:
            return 12312;
         case 12314:
            return 12315;
         case 12315:
            return 12314;
         default:
            return var0;
      }
   }
}
