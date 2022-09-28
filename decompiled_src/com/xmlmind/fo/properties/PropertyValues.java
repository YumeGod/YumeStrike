package com.xmlmind.fo.properties;

import com.xmlmind.fo.properties.compound.BorderWidthConditional;
import com.xmlmind.fo.properties.compound.Compound;
import com.xmlmind.fo.properties.compound.PaddingConditional;
import com.xmlmind.fo.properties.compound.Space;
import com.xmlmind.fo.properties.shorthand.Shorthand;
import java.io.PrintStream;

public final class PropertyValues {
   public static final byte TYPE_INITIAL = 1;
   public static final byte TYPE_INHERITED = 2;
   public static final byte TYPE_SPECIFIED = 3;
   public static final int BREAK_NONE = 0;
   public static final int BREAK_COLUMN = 1;
   public static final int BREAK_PAGE = 2;
   public static final int TOP_TO_BOTTOM = 1;
   public static final int BOTTOM_TO_TOP = 2;
   public static final int LEFT_TO_RIGHT = 3;
   public static final int RIGHT_TO_LEFT = 4;
   public static final int SIDE_TOP = 1;
   public static final int SIDE_BOTTOM = 2;
   public byte[] types = new byte[323];
   public Value[] values = new Value[323];

   public PropertyValues() {
      for(int var1 = 0; var1 < 323; ++var1) {
         this.types[var1] = 1;
         this.values[var1] = Property.list[var1].initialValue;
      }

   }

   public void inherit(PropertyValues var1) {
      int var2;
      int var3;
      for(var2 = 0; var2 < Shorthand.list.length; ++var2) {
         var3 = Shorthand.list[var2];
         if (Property.list[var3].inherited) {
            this.values[var3] = var1.values[var3];
            this.types[var3] = 2;
            if (this.values[var3] != null) {
               Shorthand var4 = (Shorthand)Property.list[var3];
               var4.expand(this);
            }
         }
      }

      for(var2 = 0; var2 < Compound.list.length; ++var2) {
         var3 = Compound.list[var2];
         if (Property.list[var3].inherited) {
            this.values[var3] = var1.values[var3];
            this.types[var3] = 2;
            Compound var5 = (Compound)Property.list[var3];
            var5.expand(this);
         }
      }

      for(var2 = 0; var2 < 323; ++var2) {
         if (Property.list[var2].inherited) {
            this.values[var2] = var1.values[var2];
            this.types[var2] = 2;
         }
      }

   }

   public void set(int var1, Value var2) {
      this.values[var1] = var2;
      this.types[var1] = 3;
   }

   public void reset(int var1) {
      this.values[var1] = Property.list[var1].initialValue;
      this.types[var1] = 1;
   }

   public void setCorresponding() {
      int var1 = this.bpDirection();
      int var2 = this.ipDirection();
      switch (var1) {
         case 1:
         default:
            this.setCorresponding(65, 27);
            this.setCorresponding(66, 29);
            this.setCorresponding(67, 30, 32);
            this.setCorresponding(208, 196, 198);
            this.setSpace(252, 176);
            this.setCorresponding(34, 21);
            this.setCorresponding(35, 23);
            this.setCorresponding(36, 24, 26);
            this.setCorresponding(199, 193, 195);
            this.setSpace(246, 173);
            break;
         case 4:
            this.setCorresponding(46, 21);
            this.setCorresponding(47, 23);
            this.setCorresponding(48, 24, 26);
            this.setCorresponding(203, 193, 195);
            this.setSpace(246, 174);
            this.setCorresponding(50, 27);
            this.setCorresponding(51, 29);
            this.setCorresponding(52, 30, 32);
            this.setCorresponding(204, 196, 198);
            this.setSpace(252, 175);
      }

      switch (var2) {
         case 1:
            this.setCorresponding(65, 57);
            this.setCorresponding(66, 59);
            this.setCorresponding(67, 60, 62);
            this.setCorresponding(208, 205, 207);
            this.setSpace(264, 176);
            this.setIndent(277, 176, 205, 60, 59);
            this.setCorresponding(34, 39);
            this.setCorresponding(35, 41);
            this.setCorresponding(36, 42, 44);
            this.setCorresponding(199, 200, 202);
            this.setSpace(258, 173);
            this.setIndent(97, 173, 200, 42, 41);
            break;
         case 2:
         case 3:
         default:
            this.setCorresponding(46, 57);
            this.setCorresponding(47, 59);
            this.setCorresponding(48, 60, 62);
            this.setCorresponding(203, 205, 207);
            this.setSpace(264, 174);
            this.setIndent(277, 174, 205, 60, 59);
            this.setCorresponding(50, 39);
            this.setCorresponding(51, 41);
            this.setCorresponding(52, 42, 44);
            this.setCorresponding(204, 200, 202);
            this.setSpace(258, 175);
            this.setIndent(97, 175, 200, 42, 41);
            break;
         case 4:
            this.setCorresponding(46, 39);
            this.setCorresponding(47, 41);
            this.setCorresponding(48, 42, 44);
            this.setCorresponding(203, 200, 202);
            this.setSpace(258, 174);
            this.setIndent(97, 174, 200, 42, 41);
            this.setCorresponding(50, 57);
            this.setCorresponding(51, 59);
            this.setCorresponding(52, 60, 62);
            this.setCorresponding(204, 205, 207);
            this.setSpace(264, 175);
            this.setIndent(277, 175, 205, 60, 59);
      }

   }

   private void setCorresponding(int var1, int var2) {
      if (this.isSpecified(var1)) {
         this.set(var2, this.values[var1]);
      } else if (this.isSpecified(var2)) {
         this.set(var1, this.values[var2]);
      }

   }

   private void setCorresponding(int var1, int var2, int var3) {
      if (this.isSpecified(var1)) {
         this.set(var2, this.values[var1]);
         Compound var4 = (Compound)Property.list[var2];
         var4.expand(this);
      } else if (this.isSpecified(var3)) {
         this.set(var1, this.values[var3]);
      }

   }

   private void setSpace(int var1, int var2) {
      if (this.isSpecified(var2)) {
         if (this.values[var2].type == 4) {
            this.set(var1, Space.space(this.values[var2]));
            Compound var3 = (Compound)Property.list[var1];
            var3.expand(this);
         }
      } else if (this.isSpecified(var1) && this.values[var1].type == 9) {
         this.set(var2, Space.length(this.values[var1]));
      }

   }

   private void setIndent(int var1, int var2, int var3, int var4, int var5) {
      if (!this.isSpecified(var1) && this.isSpecified(var2) && this.values[var1].type == 4) {
         double var6 = this.values[var1].length();
         if (this.values[var2].type == 4) {
            var6 += this.values[var2].length();
         }

         var6 += PaddingConditional.length(this.values[var3]);
         int var8 = this.values[var5].keyword();
         if (var8 != 125 && var8 != 75) {
            var6 += BorderWidthConditional.length(this.values[var4]);
         }

         this.set(var1, new Value((byte)4, 4, var6));
      }

   }

   public boolean isSpecified(int var1) {
      return this.types[var1] == 3;
   }

   public double fontSize() {
      return this.values[106].length();
   }

   public int bpDirection() {
      byte var1 = 1;
      switch (this.values[316].keyword()) {
         case 196:
         case 197:
            var1 = 4;
         default:
            return var1;
      }
   }

   public int ipDirection() {
      byte var1 = 3;
      switch (this.values[316].keyword()) {
         case 168:
         case 169:
            var1 = 4;
            break;
         case 196:
         case 197:
            var1 = 1;
      }

      return var1;
   }

   public int breakBefore() {
      byte var1 = 0;
      switch (this.values[71].keyword()) {
         case 10:
         default:
            break;
         case 38:
            var1 = 1;
            break;
         case 57:
         case 141:
         case 146:
            var1 = 2;
      }

      return var1;
   }

   public int breakAfter() {
      byte var1 = 0;
      switch (this.values[70].keyword()) {
         case 10:
         default:
            break;
         case 38:
            var1 = 1;
            break;
         case 57:
         case 141:
         case 146:
            var1 = 2;
      }

      return var1;
   }

   public int captionSide() {
      byte var1;
      switch (this.values[72].keyword()) {
         case 4:
         case 27:
            var1 = 2;
            break;
         case 16:
         case 204:
            var1 = 1;
            break;
         default:
            var1 = 1;
      }

      return var1;
   }

   public int linefeedTreatment() {
      return this.values[163].keyword();
   }

   public int whiteSpaceTreatment() {
      return this.values[306].keyword();
   }

   public boolean whiteSpaceCollapse() {
      Value var1 = this.values[305];
      int var2 = var1.keyword();
      return var2 == 209;
   }

   public boolean startsRow() {
      Value var1 = this.values[279];
      return var1.keyword() == 209;
   }

   public Color backgroundColor() {
      Color var1 = null;
      if (this.values[8].type == 24) {
         var1 = this.values[8].color();
      }

      return var1;
   }

   public String id() {
      return this.values[125] != null ? this.values[125].id() : null;
   }

   public void dump(int var1, PrintStream var2) {
      for(int var3 = 0; var3 < 323; ++var3) {
         if (this.types[var3] == var1) {
            var2.print(Property.name(var3) + " = ");
            if (this.values[var3] != null) {
               var2.println(this.values[var3].toString());
            } else {
               var2.println("null");
            }
         }
      }

   }

   public void dump(PrintStream var1) {
      for(int var3 = 0; var3 < 323; ++var3) {
         var1.print(Property.name(var3) + " = ");
         if (this.values[var3] != null) {
            var1.print(this.values[var3].toString());
         } else {
            var1.print("null");
         }

         char var2;
         switch (this.types[var3]) {
            case 1:
               var2 = 'd';
               break;
            case 2:
               var2 = 'i';
               break;
            case 3:
               var2 = 's';
               break;
            default:
               var2 = '?';
         }

         var1.println(" [" + var2 + "]");
      }

   }
}
