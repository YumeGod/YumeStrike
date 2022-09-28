package org.apache.xerces.impl.xpath.regex;

import java.text.CharacterIterator;

public class Match implements Cloneable {
   int[] beginpos = null;
   int[] endpos = null;
   int nofgroups = 0;
   CharacterIterator ciSource = null;
   String strSource = null;
   char[] charSource = null;

   public synchronized Object clone() {
      Match var1 = new Match();
      if (this.nofgroups > 0) {
         var1.setNumberOfGroups(this.nofgroups);
         if (this.ciSource != null) {
            var1.setSource(this.ciSource);
         }

         if (this.strSource != null) {
            var1.setSource(this.strSource);
         }

         for(int var2 = 0; var2 < this.nofgroups; ++var2) {
            var1.setBeginning(var2, this.getBeginning(var2));
            var1.setEnd(var2, this.getEnd(var2));
         }
      }

      return var1;
   }

   protected void setNumberOfGroups(int var1) {
      int var2 = this.nofgroups;
      this.nofgroups = var1;
      if (var2 <= 0 || var2 < var1 || var1 * 2 < var2) {
         this.beginpos = new int[var1];
         this.endpos = new int[var1];
      }

      for(int var3 = 0; var3 < var1; ++var3) {
         this.beginpos[var3] = -1;
         this.endpos[var3] = -1;
      }

   }

   protected void setSource(CharacterIterator var1) {
      this.ciSource = var1;
      this.strSource = null;
      this.charSource = null;
   }

   protected void setSource(String var1) {
      this.ciSource = null;
      this.strSource = var1;
      this.charSource = null;
   }

   protected void setSource(char[] var1) {
      this.ciSource = null;
      this.strSource = null;
      this.charSource = var1;
   }

   protected void setBeginning(int var1, int var2) {
      this.beginpos[var1] = var2;
   }

   protected void setEnd(int var1, int var2) {
      this.endpos[var1] = var2;
   }

   public int getNumberOfGroups() {
      if (this.nofgroups <= 0) {
         throw new IllegalStateException("A result is not set.");
      } else {
         return this.nofgroups;
      }
   }

   public int getBeginning(int var1) {
      if (this.beginpos == null) {
         throw new IllegalStateException("A result is not set.");
      } else if (var1 >= 0 && this.nofgroups > var1) {
         return this.beginpos[var1];
      } else {
         throw new IllegalArgumentException("The parameter must be less than " + this.nofgroups + ": " + var1);
      }
   }

   public int getEnd(int var1) {
      if (this.endpos == null) {
         throw new IllegalStateException("A result is not set.");
      } else if (var1 >= 0 && this.nofgroups > var1) {
         return this.endpos[var1];
      } else {
         throw new IllegalArgumentException("The parameter must be less than " + this.nofgroups + ": " + var1);
      }
   }

   public String getCapturedText(int var1) {
      if (this.beginpos == null) {
         throw new IllegalStateException("match() has never been called.");
      } else if (var1 >= 0 && this.nofgroups > var1) {
         int var3 = this.beginpos[var1];
         int var4 = this.endpos[var1];
         if (var3 >= 0 && var4 >= 0) {
            String var2;
            if (this.ciSource != null) {
               var2 = REUtil.substring(this.ciSource, var3, var4);
            } else if (this.strSource != null) {
               var2 = this.strSource.substring(var3, var4);
            } else {
               var2 = new String(this.charSource, var3, var4 - var3);
            }

            return var2;
         } else {
            return null;
         }
      } else {
         throw new IllegalArgumentException("The parameter must be less than " + this.nofgroups + ": " + var1);
      }
   }
}
