package org.apache.fop.area.inline;

public class WordArea extends InlineArea {
   protected String word;
   protected int offset = 0;
   protected int[] letterAdjust;

   public WordArea(String w, int o, int[] la) {
      this.word = w;
      this.offset = o;
      this.letterAdjust = la;
   }

   public String getWord() {
      return this.word;
   }

   public int getOffset() {
      return this.offset;
   }

   public void setOffset(int o) {
      this.offset = o;
   }

   public int[] getLetterAdjustArray() {
      return this.letterAdjust;
   }
}
