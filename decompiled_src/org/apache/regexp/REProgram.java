package org.apache.regexp;

public class REProgram {
   static final int OPT_HASBACKREFS = 1;
   char[] instruction;
   int lenInstruction;
   char[] prefix;
   int flags;

   public REProgram(char[] var1) {
      this(var1, var1.length);
   }

   public REProgram(char[] var1, int var2) {
      this.setInstructions(var1, var2);
   }

   public char[] getInstructions() {
      if (this.lenInstruction != 0) {
         char[] var1 = new char[this.lenInstruction];
         System.arraycopy(this.instruction, 0, var1, 0, this.lenInstruction);
         return var1;
      } else {
         return null;
      }
   }

   public void setInstructions(char[] var1, int var2) {
      this.instruction = var1;
      this.lenInstruction = var2;
      this.flags = 0;
      this.prefix = null;
      if (var1 != null && var2 != 0) {
         int var3;
         if (var2 >= 3 && var1[0] == '|') {
            var3 = var1[2];
            if (var1[var3] == 'E' && var2 >= 6 && var1[3] == 'A') {
               char var4 = var1[4];
               this.prefix = new char[var4];
               System.arraycopy(var1, 6, this.prefix, 0, var4);
            }
         }

         for(var3 = 0; var3 < var2; var3 += 3) {
            switch (var1[var3]) {
               case '#':
                  this.flags |= 1;
                  return;
               case 'A':
                  var3 += var1[var3 + 1];
                  break;
               case '[':
                  var3 += var1[var3 + 1] * 2;
            }
         }
      }

   }
}
