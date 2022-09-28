package org.apache.batik.ext.swing;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DoubleDocument extends PlainDocument {
   public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
      if (var2 != null) {
         String var4 = this.getText(0, this.getLength());
         boolean var5 = var4.indexOf(46) != -1;
         char[] var6 = var2.toCharArray();
         char[] var7 = new char[var6.length];
         int var8 = 0;
         if (var1 == 0 && var6 != null && var6.length > 0 && var6[0] == '-') {
            var7[var8++] = var6[0];
         }

         for(int var9 = 0; var9 < var6.length; ++var9) {
            if (Character.isDigit(var6[var9])) {
               var7[var8++] = var6[var9];
            }

            if (!var5 && var6[var9] == '.') {
               var7[var8++] = '.';
               var5 = true;
            }
         }

         String var13 = new String(var7, 0, var8);

         try {
            StringBuffer var10 = new StringBuffer(var4);
            var10.insert(var1, var13);
            String var11 = var10.toString();
            if (!var11.equals(".") && !var11.equals("-") && !var11.equals("-.")) {
               Double.valueOf(var11);
               super.insertString(var1, var13, var3);
            } else {
               super.insertString(var1, var13, var3);
            }
         } catch (NumberFormatException var12) {
         }

      }
   }

   public void setValue(double var1) {
      try {
         this.remove(0, this.getLength());
         this.insertString(0, String.valueOf(var1), (AttributeSet)null);
      } catch (BadLocationException var4) {
      }

   }

   public double getValue() {
      try {
         String var1 = this.getText(0, this.getLength());
         return var1 != null && var1.length() > 0 ? Double.parseDouble(var1) : 0.0;
      } catch (BadLocationException var2) {
         throw new Error(var2.getMessage());
      }
   }
}
