package org.apache.fop.render.txt.border;

import java.util.Arrays;

public class SolidAndDoubleBorderElement extends AbstractBorderElement {
   private static final char LIGHT_HORIZONTAL = '─';
   private static final char LIGHT_VERTICAL = '│';
   private static final char LIGHT_DOWN_AND_RIGHT = '┌';
   private static final char LIGHT_DOWN_AND_LEFT = '┐';
   private static final char LIGHT_UP_AND_RIGHT = '└';
   private static final char LIGHT_UP_AND_LEFT = '┘';
   private static final char LIGHT_VERTICAL_AND_RIGHT = '├';
   private static final char LIGHT_VERTICAL_AND_LEFT = '┤';
   private static final char LIGHT_DOWN_AND_HORIZONTAL = '┬';
   private static final char LIGHT_UP_AND_HORIZONTAL = '┴';
   private static final char LIGHT_VERTICAL_AND_HORIZONTAL = '┼';
   private static final char DOUBLE_HORIZONTAL = '═';
   private static final char DOUBLE_VERTICAL = '║';
   private static final char DOUBLE_DOWN_AND_RIGHT = '╔';
   private static final char DOUBLE_DOWN_AND_LEFT = '╗';
   private static final char DOUBLE_UP_AND_RIGHT = '╚';
   private static final char DOUBLE_UP_AND_LEFT = '╝';
   private static final char DOUBLE_VERTICAL_AND_RIGHT = '╠';
   private static final char DOUBLE_VERTICAL_AND_LEFT = '╣';
   private static final char DOUBLE_DOWN_AND_HORIZONTAL = '╦';
   private static final char DOUBLE_UP_AND_HORIZONTAL = '╩';
   private static final char DOUBLE_VERTICAL_AND_HORIZONTAL = '╬';
   private static final char DOWN_SINGLE_AND_RIGHT_DOUBLE = '╒';
   private static final char DOWN_DOUBLE_AND_RIGHT_SINGLE = '╓';
   private static final char DOWN_SINGLE_AND_LEFT_DOUBLE = '╕';
   private static final char DOWN_DOUBLE_AND_LEFT_SINGLE = '╖';
   private static final char UP_SINGLE_AND_RIGHT_DOUBLE = '╘';
   private static final char UP_DOUBLE_AND_RIGHT_SINGLE = '╙';
   private static final char UP_SINGLE_AND_LEFT_DOUBLE = '╛';
   private static final char UP_DOUBLE_AND_LEFT_SINGLE = '╜';
   private static final char VERTICAL_SINGLE_AND_RIGHT_DOUBLE = '╞';
   private static final char VERTICAL_DOUBLE_AND_RIGHT_SINGLE = '╟';
   private static final char VERTICAL_SINGLE_AND_LEFT_DOUBLE = '╡';
   private static final char VERTICAL_DOUBLE_AND_LEFT_SINGLE = '╢';
   private static final char DOWN_SINGLE_AND_HORIZONTAL_DOUBLE = '╤';
   private static final char DOWN_DOUBLE_AND_HORIZONTAL_SINGLE = '╥';
   private static final char UP_SINGLE_AND_HORIZONTAL_DOUBLE = '╧';
   private static final char UP_DOUBLE_AND_HORIZONTAL_SINGLE = '╨';
   private static final char VERTICAL_SINGLE_AND_HORIZONTAL_DOUBLE = '╪';
   private static final char VERTICAL_DOUBLE_AND_HORIZONTAL_SINGLE = '╫';
   private static final char UNDEFINED = '?';
   private static final int UP3 = 1;
   private static final int DOWN3 = 3;
   private static final int LEFT3 = 9;
   private static final int RIGHT3 = 27;
   private static final char[] MAP = new char[100];

   public SolidAndDoubleBorderElement() {
   }

   public SolidAndDoubleBorderElement(int style, int type) {
      super(type);
      if (style == 37) {
         for(int i = 0; i < 4; ++i) {
            int[] var10000 = this.data;
            var10000[i] *= 2;
         }
      }

   }

   public AbstractBorderElement mergeSolid(SolidAndDoubleBorderElement sde) {
      AbstractBorderElement e = new SolidAndDoubleBorderElement(133, 0);

      for(int i = 0; i < 4; ++i) {
         if (sde.getData(i) != 0) {
            e.setData(i, sde.getData(i));
         } else {
            e.setData(i, this.data[i]);
         }
      }

      return e;
   }

   public AbstractBorderElement merge(AbstractBorderElement e) {
      AbstractBorderElement abe = this;
      if (e instanceof SolidAndDoubleBorderElement) {
         abe = this.mergeSolid((SolidAndDoubleBorderElement)e);
      } else if (e instanceof DottedBorderElement) {
         abe = e;
      } else if (e instanceof DashedBorderElement) {
         abe = e.merge(this);
      }

      return (AbstractBorderElement)abe;
   }

   private char map2Char() {
      int key = 0;
      key += this.data[0] * 1;
      key += this.data[3] * 9;
      key += this.data[2] * 3;
      key += this.data[1] * 27;
      return MAP[key];
   }

   private void modifyData() {
      int c1 = 0;
      int c2 = 0;

      int m;
      for(m = 0; m < 4; ++m) {
         c1 += this.data[m] == 1 ? 1 : 0;
         c2 += this.data[m] == 2 ? 1 : 0;
      }

      m = c1 > c2 ? 1 : 0;
      int[] p = new int[]{0, m, 2 * (1 - m)};

      for(int i = 0; i < 4; ++i) {
         this.data[i] = p[this.data[i]];
      }

   }

   public char convert2Char() {
      char ch = this.map2Char();
      if (ch == '?') {
         this.modifyData();
         ch = this.map2Char();
      }

      return ch;
   }

   static {
      Arrays.fill(MAP, '?');
      MAP[0] = ' ';
      MAP[1] = 9474;
      MAP[3] = 9474;
      MAP[27] = 9472;
      MAP[9] = 9472;
      MAP[4] = 9474;
      MAP[36] = 9472;
      MAP[10] = 9496;
      MAP[12] = 9488;
      MAP[30] = 9484;
      MAP[28] = 9492;
      MAP[31] = 9500;
      MAP[13] = 9508;
      MAP[39] = 9516;
      MAP[37] = 9524;
      MAP[40] = 9532;
      MAP[2] = 9553;
      MAP[6] = 9553;
      MAP[54] = 9552;
      MAP[18] = 9552;
      MAP[8] = 9553;
      MAP[72] = 9552;
      MAP[20] = 9565;
      MAP[24] = 9559;
      MAP[60] = 9556;
      MAP[56] = 9562;
      MAP[62] = 9568;
      MAP[26] = 9571;
      MAP[78] = 9574;
      MAP[74] = 9577;
      MAP[80] = 9580;
      MAP[57] = 9554;
      MAP[33] = 9555;
      MAP[21] = 9557;
      MAP[15] = 9558;
      MAP[55] = 9560;
      MAP[29] = 9561;
      MAP[19] = 9563;
      MAP[11] = 9564;
      MAP[58] = 9566;
      MAP[35] = 9567;
      MAP[22] = 9569;
      MAP[17] = 9570;
      MAP[75] = 9572;
      MAP[42] = 9573;
      MAP[73] = 9575;
      MAP[38] = 9576;
      MAP[76] = 9578;
      MAP[44] = 9579;
   }
}
