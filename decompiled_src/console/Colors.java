package console;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Properties;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Colors {
   public static final char bold = '\u0002';
   public static final char underline = '\u001f';
   public static final char color = '\u0003';
   public static final char cancel = '\u000f';
   public static final char reverse = '\u0016';
   protected boolean showcolors = true;
   protected Color[] colorTable = new Color[16];
   private StyledDocument dummy = new DefaultStyledDocument();
   private static final int MAX_DOCUMENT_LENGTH = 262144;

   public static String color(String var0, String var1) {
      return '\u0003' + var1 + var0;
   }

   public static String underline(String var0) {
      return '\u001f' + var0 + '\u000f';
   }

   public Colors(Properties var1) {
      this.colorTable[0] = Color.white;
      this.colorTable[1] = new Color(0, 0, 0);
      this.colorTable[2] = Color.decode("#3465A4");
      this.colorTable[3] = Color.decode("#4E9A06");
      this.colorTable[4] = Color.decode("#EF2929");
      this.colorTable[5] = Color.decode("#CC0000");
      this.colorTable[6] = Color.decode("#75507B");
      this.colorTable[7] = Color.decode("#C4A000");
      this.colorTable[8] = Color.decode("#FCE94F");
      this.colorTable[9] = Color.decode("#8AE234");
      this.colorTable[10] = Color.decode("#06989A");
      this.colorTable[11] = Color.decode("#34E2E2");
      this.colorTable[12] = Color.decode("#729FCF");
      this.colorTable[13] = Color.decode("#AD7FA8");
      this.colorTable[14] = Color.decode("#808080");
      this.colorTable[15] = Color.lightGray;

      for(int var2 = 0; var2 < 16; ++var2) {
         String var3 = var1.getProperty("console.color_" + var2 + ".color", (String)null);
         if (var3 != null) {
            this.colorTable[var2] = Color.decode(var3);
         }
      }

      this.showcolors = "true".equals(var1.getProperty("console.show_colors.boolean", "true"));
   }

   public String strip(String var1) {
      Fragment var2 = this.parse(var1);
      return this.strip(var2);
   }

   private String strip(Fragment var1) {
      StringBuffer var2;
      for(var2 = new StringBuffer(128); var1 != null; var1 = var1.next) {
         var2.append(var1.text);
      }

      return var2.toString();
   }

   private void append(StyledDocument var1, Fragment var2) {
      for(; var2 != null; var2 = var2.next) {
         try {
            if (var2.text.length() > 0) {
               var1.insertString(var1.getLength(), var2.text.toString(), var2.attr);
            }
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

   }

   public void append(StyledDocument var1, String var2) {
      if (var2.length() > 262144) {
         var2 = var2.substring(var2.length() - 262144, var2.length());
      }

      Fragment var3 = this.parse(var2);
      this.append(var1, var3);
      if (var1.getLength() > 262144) {
         try {
            var1.remove(0, var1.getLength() - 262144 + 131072);
         } catch (BadLocationException var5) {
         }
      }

   }

   public void append(JTextPane var1, String var2) {
      StyledDocument var3 = var1.getStyledDocument();
      if (this.showcolors) {
         var1.setDocument(this.dummy);
         this.append(var3, var2);
         var1.setDocument(var3);
      } else {
         Fragment var4 = this.parse(var2);
         this.append(var3, this.parse(this.strip(var4)));
      }

   }

   public void set(JTextPane var1, String var2) {
      Fragment var3 = this.parse(var2);
      if (!this.strip(var3).equals(var1.getText())) {
         DefaultStyledDocument var4 = new DefaultStyledDocument();
         if (this.showcolors) {
            this.append((StyledDocument)var4, (Fragment)var3);
         } else {
            this.append((StyledDocument)var4, (Fragment)this.parse(this.strip(var3)));
         }

         var1.setDocument(var4);
         var1.setSize(new Dimension(1000, var1.getSize().height));
      }
   }

   public void setNoHack(JTextPane var1, String var2) {
      Fragment var3 = this.parse(var2);
      if (!this.strip(var3).equals(var1.getText())) {
         DefaultStyledDocument var4 = new DefaultStyledDocument();
         if (this.showcolors) {
            this.append((StyledDocument)var4, (Fragment)var3);
         } else {
            this.append((StyledDocument)var4, (Fragment)this.parse(this.strip(var3)));
         }

         var1.setDocument(var4);
      }
   }

   private Fragment parse(String var1) {
      Fragment var2 = new Fragment();
      if (var1 == null) {
         return var2;
      } else {
         char[] var4 = var1.toCharArray();

         for(int var7 = 0; var7 < var4.length; ++var7) {
            switch (var4[var7]) {
               case '\u0002':
                  var2.advance();
                  StyleConstants.setBold(var2.next.attr, !StyleConstants.isBold(var2.attr));
                  var2 = var2.next;
                  break;
               case '\u0003':
                  var2.advance();
                  if (var7 + 1 < var4.length && (var4[var7 + 1] >= '0' && var4[var7 + 1] <= '9' || var4[var7 + 1] >= 'A' && var4[var7 + 1] <= 'F')) {
                     int var8 = Integer.parseInt(var4[var7 + 1] + "", 16);
                     StyleConstants.setForeground(var2.next.attr, this.colorTable[var8]);
                     ++var7;
                  }

                  var2 = var2.next;
                  break;
               case '\n':
                  var2.advance();
                  var2 = var2.next;
                  var2.attr = new SimpleAttributeSet();
                  var2.text.append(var4[var7]);
                  break;
               case '\u000f':
                  var2.advance();
                  var2 = var2.next;
                  var2.attr = new SimpleAttributeSet();
                  break;
               case '\u001f':
                  var2.advance();
                  StyleConstants.setUnderline(var2.next.attr, !StyleConstants.isUnderline(var2.attr));
                  var2 = var2.next;
                  break;
               default:
                  var2.text.append(var4[var7]);
            }
         }

         return var2;
      }
   }

   private static final class Fragment {
      protected SimpleAttributeSet attr;
      protected StringBuffer text;
      protected Fragment next;

      private Fragment() {
         this.attr = new SimpleAttributeSet();
         this.text = new StringBuffer(32);
         this.next = null;
      }

      public void advance() {
         this.next = new Fragment();
         this.next.attr = (SimpleAttributeSet)this.attr.clone();
      }

      // $FF: synthetic method
      Fragment(Object var1) {
         this();
      }
   }
}
