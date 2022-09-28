package org.apache.batik.css.engine;

import org.w3c.css.sac.SACMediaList;

public class StyleSheet {
   protected Rule[] rules = new Rule[16];
   protected int size;
   protected StyleSheet parent;
   protected boolean alternate;
   protected SACMediaList media;
   protected String title;

   public void setMedia(SACMediaList var1) {
      this.media = var1;
   }

   public SACMediaList getMedia() {
      return this.media;
   }

   public StyleSheet getParent() {
      return this.parent;
   }

   public void setParent(StyleSheet var1) {
      this.parent = var1;
   }

   public void setAlternate(boolean var1) {
      this.alternate = var1;
   }

   public boolean isAlternate() {
      return this.alternate;
   }

   public void setTitle(String var1) {
      this.title = var1;
   }

   public String getTitle() {
      return this.title;
   }

   public int getSize() {
      return this.size;
   }

   public Rule getRule(int var1) {
      return this.rules[var1];
   }

   public void clear() {
      this.size = 0;
      this.rules = new Rule[10];
   }

   public void append(Rule var1) {
      if (this.size == this.rules.length) {
         Rule[] var2 = new Rule[this.size * 2];
         System.arraycopy(this.rules, 0, var2, 0, this.size);
         this.rules = var2;
      }

      this.rules[this.size++] = var1;
   }

   public String toString(CSSEngine var1) {
      StringBuffer var2 = new StringBuffer(this.size * 8);

      for(int var3 = 0; var3 < this.size; ++var3) {
         var2.append(this.rules[var3].toString(var1));
      }

      return var2.toString();
   }
}
