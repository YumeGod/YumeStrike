package org.apache.batik.util.gui.xmleditor;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;

public class XMLDocument extends PlainDocument {
   protected XMLScanner lexer;
   protected XMLContext context;
   protected XMLToken cacheToken;

   public XMLDocument() {
      this(new XMLContext());
   }

   public XMLDocument(XMLContext var1) {
      this.cacheToken = null;
      this.context = var1;
      this.lexer = new XMLScanner();
   }

   public XMLToken getScannerStart(int var1) throws BadLocationException {
      int var2 = 3;
      int var3 = 0;
      int var4 = 0;
      int var6;
      int var7;
      if (this.cacheToken != null) {
         if (this.cacheToken.getStartOffset() > var1) {
            this.cacheToken = null;
         } else {
            var2 = this.cacheToken.getContext();
            var3 = this.cacheToken.getStartOffset();
            var4 = var3;
            Element var5 = this.getDefaultRootElement();
            var6 = var5.getElementIndex(var1);
            var7 = var5.getElementIndex(var3);
            if (var6 - var7 < 50) {
               return this.cacheToken;
            }
         }
      }

      String var8 = this.getText(var3, var1 - var3);
      this.lexer.setString(var8);
      this.lexer.reset();
      var6 = var2;

      for(var7 = var3; var3 < var1; var2 = this.lexer.getScanValue()) {
         var7 = var3;
         var6 = var2;
         var3 = this.lexer.scan(var2) + var4;
      }

      this.cacheToken = new XMLToken(var6, var7, var3);
      return this.cacheToken;
   }

   public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
      super.insertString(var1, var2, var3);
      if (this.cacheToken != null && this.cacheToken.getStartOffset() >= var1) {
         this.cacheToken = null;
      }

   }

   public void remove(int var1, int var2) throws BadLocationException {
      super.remove(var1, var2);
      if (this.cacheToken != null && this.cacheToken.getStartOffset() >= var1) {
         this.cacheToken = null;
      }

   }

   public int find(String var1, int var2, boolean var3) throws BadLocationException {
      int var4 = -1;
      boolean var5 = true;
      boolean var6 = false;
      boolean var7 = false;
      Element var8 = this.getDefaultRootElement();
      int var9 = var8.getElementIndex(var2);
      if (var9 < 0) {
         return var4;
      } else {
         int var15 = var2 - var8.getElement(var9).getStartOffset();

         for(int var10 = var9; var10 < var8.getElementCount(); ++var10) {
            Element var11 = var8.getElement(var10);
            int var13 = var11.getStartOffset();
            int var14;
            if (var11.getEndOffset() > this.getLength()) {
               var14 = this.getLength() - var13;
            } else {
               var14 = var11.getEndOffset() - var13;
            }

            String var12 = this.getText(var13, var14);
            if (!var3) {
               var12 = var12.toLowerCase();
               var1 = var1.toLowerCase();
            }

            var15 = var12.indexOf(var1, var15);
            if (var15 != -1) {
               var4 = var13 + var15;
               break;
            }

            var15 = 0;
         }

         return var4;
      }
   }
}
