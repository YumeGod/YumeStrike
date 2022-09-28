package org.apache.fop.render.txt.border;

import java.util.Arrays;

public class DashedBorderElement extends AbstractBorderElement {
   private static final char DASH_HORIZONTAL = '-';
   private static final char DASH_VERTICAL = '|';
   private static final char UNDEFINED = '?';
   private static final int UP2 = 1;
   private static final int RIGHT2 = 2;
   private static final int DOWN2 = 4;
   private static final int LEFT2 = 8;
   private static char[] map = new char[20];

   public DashedBorderElement(int type) {
      super(type);
   }

   private AbstractBorderElement mergeSolid(SolidAndDoubleBorderElement sdb) {
      AbstractBorderElement e = new SolidAndDoubleBorderElement(133, 0);

      for(int i = 0; i < 4; ++i) {
         e.setData(i, Math.max(this.data[i], sdb.getData(i)));
      }

      return e;
   }

   private AbstractBorderElement mergeDashed(DashedBorderElement dbe) {
      for(int i = 0; i < 4; ++i) {
         this.data[i] = Math.max(this.data[i], dbe.getData(i));
      }

      return this;
   }

   private AbstractBorderElement toSolidAndDouble() {
      AbstractBorderElement e = new SolidAndDoubleBorderElement(133, 0);

      for(int i = 0; i < 4; ++i) {
         e.setData(i, this.data[i]);
      }

      return e;
   }

   public AbstractBorderElement merge(AbstractBorderElement e) {
      AbstractBorderElement abe;
      if (e instanceof SolidAndDoubleBorderElement) {
         abe = this.mergeSolid((SolidAndDoubleBorderElement)e);
      } else if (e instanceof DashedBorderElement) {
         abe = this.mergeDashed((DashedBorderElement)e);
      } else {
         abe = e;
      }

      return abe;
   }

   public char convert2Char() {
      int key = 0;
      key += this.data[0] * 1;
      key += this.data[2] * 4;
      key += this.data[3] * 8;
      key += this.data[1] * 2;
      char ch = map[key];
      if (ch == '?') {
         ch = this.toSolidAndDouble().convert2Char();
      }

      return ch;
   }

   static {
      Arrays.fill(map, '?');
      map[0] = ' ';
      map[1] = '|';
      map[4] = '|';
      map[5] = '|';
      map[8] = '-';
      map[2] = '-';
      map[10] = '-';
   }
}
