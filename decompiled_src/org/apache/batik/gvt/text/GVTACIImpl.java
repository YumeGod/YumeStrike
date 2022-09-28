package org.apache.batik.gvt.text;

import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GVTACIImpl implements GVTAttributedCharacterIterator {
   private String simpleString;
   private Set allAttributes;
   private ArrayList mapList;
   private static int START_RUN = 2;
   private static int END_RUN = 3;
   private static int MID_RUN = 1;
   private static int SINGLETON = 0;
   private int[] charInRun;
   private CharacterIterator iter = null;
   private int currentIndex = -1;

   public GVTACIImpl() {
      this.simpleString = "";
      this.buildAttributeTables();
   }

   public GVTACIImpl(AttributedCharacterIterator var1) {
      this.buildAttributeTables(var1);
   }

   public void setString(String var1) {
      this.simpleString = var1;
      this.iter = new StringCharacterIterator(this.simpleString);
      this.buildAttributeTables();
   }

   public void setString(AttributedString var1) {
      this.iter = var1.getIterator();
      this.buildAttributeTables((AttributedCharacterIterator)this.iter);
   }

   public void setAttributeArray(GVTAttributedCharacterIterator.TextAttribute var1, Object[] var2, int var3, int var4) {
      var3 = Math.max(var3, 0);
      var4 = Math.min(var4, this.simpleString.length());
      if (this.charInRun[var3] == END_RUN) {
         if (this.charInRun[var3 - 1] == MID_RUN) {
            this.charInRun[var3 - 1] = END_RUN;
         } else {
            this.charInRun[var3 - 1] = SINGLETON;
         }
      }

      if (this.charInRun[var4 + 1] == END_RUN) {
         this.charInRun[var4 + 1] = SINGLETON;
      } else if (this.charInRun[var4 + 1] == MID_RUN) {
         this.charInRun[var4 + 1] = START_RUN;
      }

      for(int var5 = var3; var5 <= var4; ++var5) {
         this.charInRun[var5] = SINGLETON;
         int var6 = Math.min(var5, var2.length - 1);
         ((Map)this.mapList.get(var5)).put(var1, var2[var6]);
      }

   }

   public Set getAllAttributeKeys() {
      return this.allAttributes;
   }

   public Object getAttribute(AttributedCharacterIterator.Attribute var1) {
      return this.getAttributes().get(var1);
   }

   public Map getAttributes() {
      return (Map)this.mapList.get(this.currentIndex);
   }

   public int getRunLimit() {
      int var1 = this.currentIndex;

      do {
         ++var1;
      } while(this.charInRun[var1] == MID_RUN);

      return var1;
   }

   public int getRunLimit(AttributedCharacterIterator.Attribute var1) {
      int var2 = this.currentIndex;
      Object var3 = this.getAttributes().get(var1);
      if (var3 == null) {
         do {
            ++var2;
         } while(((Map)this.mapList.get(var2)).get(var1) == null);
      } else {
         do {
            ++var2;
         } while(var3.equals(((Map)this.mapList.get(var2)).get(var1)));
      }

      return var2;
   }

   public int getRunLimit(Set var1) {
      int var2 = this.currentIndex;

      do {
         ++var2;
      } while(var1.equals(this.mapList.get(var2)));

      return var2;
   }

   public int getRunStart() {
      int var1;
      for(var1 = this.currentIndex; this.charInRun[var1] == MID_RUN; --var1) {
      }

      return var1;
   }

   public int getRunStart(AttributedCharacterIterator.Attribute var1) {
      int var2 = this.currentIndex - 1;
      Object var3 = this.getAttributes().get(var1);

      try {
         if (var3 == null) {
            while(((Map)this.mapList.get(var2 - 1)).get(var1) == null) {
               --var2;
            }
         } else {
            while(var3.equals(((Map)this.mapList.get(var2 - 1)).get(var1))) {
               --var2;
            }
         }
      } catch (IndexOutOfBoundsException var5) {
      }

      return var2;
   }

   public int getRunStart(Set var1) {
      int var2 = this.currentIndex;

      try {
         while(var1.equals(this.mapList.get(var2 - 1))) {
            --var2;
         }
      } catch (IndexOutOfBoundsException var4) {
      }

      return var2;
   }

   public Object clone() {
      GVTACIImpl var1 = new GVTACIImpl(this);
      return var1;
   }

   public char current() {
      return this.iter.current();
   }

   public char first() {
      return this.iter.first();
   }

   public int getBeginIndex() {
      return this.iter.getBeginIndex();
   }

   public int getEndIndex() {
      return this.iter.getEndIndex();
   }

   public int getIndex() {
      return this.iter.getIndex();
   }

   public char last() {
      return this.iter.last();
   }

   public char next() {
      return this.iter.next();
   }

   public char previous() {
      return this.iter.previous();
   }

   public char setIndex(int var1) {
      return this.iter.setIndex(var1);
   }

   private void buildAttributeTables() {
      this.allAttributes = new HashSet();
      this.mapList = new ArrayList(this.simpleString.length());
      this.charInRun = new int[this.simpleString.length()];

      for(int var1 = 0; var1 < this.charInRun.length; ++var1) {
         this.charInRun[var1] = SINGLETON;
         this.mapList.set(var1, new HashMap());
      }

   }

   private void buildAttributeTables(AttributedCharacterIterator var1) {
      this.allAttributes = var1.getAllAttributeKeys();
      int var2 = var1.getEndIndex() - var1.getBeginIndex();
      this.mapList = new ArrayList(var2);
      this.charInRun = new int[var2];
      char var3 = var1.first();
      char[] var4 = new char[var2];

      for(int var5 = 0; var5 < var2; ++var5) {
         var4[var5] = var3;
         this.charInRun[var5] = SINGLETON;
         this.mapList.set(var5, new HashMap(var1.getAttributes()));
         var3 = var1.next();
      }

      this.simpleString = new String(var4);
   }

   public class TransformAttributeFilter implements GVTAttributedCharacterIterator.AttributeFilter {
      public AttributedCharacterIterator mutateAttributes(AttributedCharacterIterator var1) {
         return var1;
      }
   }
}
