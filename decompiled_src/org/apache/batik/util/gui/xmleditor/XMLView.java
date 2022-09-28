package org.apache.batik.util.gui.xmleditor;

import java.awt.Graphics;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;

public class XMLView extends PlainView {
   protected XMLContext context = null;
   protected XMLScanner lexer = new XMLScanner();
   protected int tabSize = 4;

   public XMLView(XMLContext var1, Element var2) {
      super(var2);
      this.context = var1;
   }

   public int getTabSize() {
      return this.tabSize;
   }

   protected int drawUnselectedText(Graphics var1, int var2, int var3, int var4, int var5) throws BadLocationException {
      XMLDocument var6 = (XMLDocument)this.getDocument();
      XMLToken var7 = var6.getScannerStart(var4);
      String var8 = var6.getText(var7.getStartOffset(), var5 - var7.getStartOffset() + 1);
      this.lexer.setString(var8);
      this.lexer.reset();
      int var9 = var7.getStartOffset();
      int var10 = var7.getContext();

      int var11;
      for(var11 = var10; var9 < var4; var10 = this.lexer.getScanValue()) {
         var9 = this.lexer.scan(var10) + var7.getStartOffset();
         var11 = var10;
      }

      int var12;
      Segment var13;
      for(var12 = var4; var9 < var5; var10 = this.lexer.getScanValue()) {
         if (var11 != var10) {
            var1.setColor(this.context.getSyntaxForeground(var11));
            var1.setFont(this.context.getSyntaxFont(var11));
            var13 = this.getLineBuffer();
            var6.getText(var12, var9 - var12, var13);
            var2 = Utilities.drawTabbedText(var13, var2, var3, var1, this, var12);
            var12 = var9;
         }

         var9 = this.lexer.scan(var10) + var7.getStartOffset();
         var11 = var10;
      }

      var1.setColor(this.context.getSyntaxForeground(var11));
      var1.setFont(this.context.getSyntaxFont(var11));
      var13 = this.getLineBuffer();
      var6.getText(var12, var5 - var12, var13);
      var2 = Utilities.drawTabbedText(var13, var2, var3, var1, this, var12);
      return var2;
   }
}
