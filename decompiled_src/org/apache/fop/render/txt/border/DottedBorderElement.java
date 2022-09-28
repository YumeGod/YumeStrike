package org.apache.fop.render.txt.border;

public class DottedBorderElement extends AbstractBorderElement {
   private static final char MIDDLE_DOT = '·';

   public AbstractBorderElement merge(AbstractBorderElement e) {
      return this;
   }

   public char convert2Char() {
      return '·';
   }
}
