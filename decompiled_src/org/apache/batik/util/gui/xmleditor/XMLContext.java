package org.apache.batik.util.gui.xmleditor;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.StyleContext;

public class XMLContext extends StyleContext {
   public static final String XML_DECLARATION_STYLE = "xml_declaration";
   public static final String DOCTYPE_STYLE = "doctype";
   public static final String COMMENT_STYLE = "comment";
   public static final String ELEMENT_STYLE = "element";
   public static final String CHARACTER_DATA_STYLE = "character_data";
   public static final String ATTRIBUTE_NAME_STYLE = "attribute_name";
   public static final String ATTRIBUTE_VALUE_STYLE = "attribute_value";
   public static final String CDATA_STYLE = "cdata";
   protected Map syntaxForegroundMap = null;
   protected Map syntaxFontMap = null;

   public XMLContext() {
      this.syntaxFontMap = new HashMap();
      this.syntaxForegroundMap = new HashMap();
      Font var4 = new Font("Monospaced", 0, 12);
      String var1 = "default";
      Color var3 = Color.black;
      this.syntaxFontMap.put(var1, var4);
      this.syntaxForegroundMap.put(var1, var3);
      var1 = "xml_declaration";
      Font var2 = var4.deriveFont(1);
      var3 = new Color(0, 0, 124);
      this.syntaxFontMap.put(var1, var2);
      this.syntaxForegroundMap.put(var1, var3);
      var1 = "doctype";
      var2 = var4.deriveFont(1);
      var3 = new Color(0, 0, 124);
      this.syntaxFontMap.put(var1, var2);
      this.syntaxForegroundMap.put(var1, var3);
      var1 = "comment";
      var3 = new Color(128, 128, 128);
      this.syntaxFontMap.put(var1, var4);
      this.syntaxForegroundMap.put(var1, var3);
      var1 = "element";
      var3 = new Color(0, 0, 255);
      this.syntaxFontMap.put(var1, var4);
      this.syntaxForegroundMap.put(var1, var3);
      var1 = "character_data";
      var3 = Color.black;
      this.syntaxFontMap.put(var1, var4);
      this.syntaxForegroundMap.put(var1, var3);
      var1 = "attribute_name";
      var3 = new Color(0, 124, 0);
      this.syntaxFontMap.put(var1, var4);
      this.syntaxForegroundMap.put(var1, var3);
      var1 = "attribute_value";
      var3 = new Color(153, 0, 107);
      this.syntaxFontMap.put(var1, var4);
      this.syntaxForegroundMap.put(var1, var3);
      var1 = "cdata";
      var3 = new Color(124, 98, 0);
      this.syntaxFontMap.put(var1, var4);
      this.syntaxForegroundMap.put(var1, var3);
   }

   public XMLContext(Map var1, Map var2) {
      this.setSyntaxFont(var1);
      this.setSyntaxForeground(var2);
   }

   public void setSyntaxForeground(Map var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("syntaxForegroundMap can not be null");
      } else {
         this.syntaxForegroundMap = var1;
      }
   }

   public void setSyntaxFont(Map var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("syntaxFontMap can not be null");
      } else {
         this.syntaxFontMap = var1;
      }
   }

   public Color getSyntaxForeground(int var1) {
      String var2 = this.getSyntaxName(var1);
      return this.getSyntaxForeground(var2);
   }

   public Color getSyntaxForeground(String var1) {
      return (Color)this.syntaxForegroundMap.get(var1);
   }

   public Font getSyntaxFont(int var1) {
      String var2 = this.getSyntaxName(var1);
      return this.getSyntaxFont(var2);
   }

   public Font getSyntaxFont(String var1) {
      return (Font)this.syntaxFontMap.get(var1);
   }

   public String getSyntaxName(int var1) {
      String var2 = "character_data";
      switch (var1) {
         case 1:
            var2 = "comment";
            break;
         case 2:
            var2 = "element";
            break;
         case 3:
         case 8:
         case 9:
         default:
            var2 = "default";
            break;
         case 4:
            var2 = "attribute_name";
            break;
         case 5:
            var2 = "attribute_value";
            break;
         case 6:
            var2 = "xml_declaration";
            break;
         case 7:
            var2 = "doctype";
            break;
         case 10:
            var2 = "cdata";
      }

      return var2;
   }
}
