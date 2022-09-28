package org.apache.fop.util.text;

import java.util.Map;
import org.apache.xmlgraphics.fonts.Glyphs;

public class GlyphNameFieldPart implements AdvancedMessageFormat.Part {
   private String fieldName;

   public GlyphNameFieldPart(String fieldName) {
      this.fieldName = fieldName;
   }

   public boolean isGenerated(Map params) {
      Object obj = params.get(this.fieldName);
      return obj != null && this.getGlyphName(obj).length() > 0;
   }

   private String getGlyphName(Object obj) {
      if (obj instanceof Character) {
         return Glyphs.charToGlyphName((Character)obj);
      } else {
         throw new IllegalArgumentException("Value for glyph name part must be a Character but was: " + obj.getClass().getName());
      }
   }

   public void write(StringBuffer sb, Map params) {
      if (!params.containsKey(this.fieldName)) {
         throw new IllegalArgumentException("Message pattern contains unsupported field name: " + this.fieldName);
      } else {
         Object obj = params.get(this.fieldName);
         sb.append(this.getGlyphName(obj));
      }
   }

   public String toString() {
      return "{" + this.fieldName + ",glyph-name}";
   }

   public static class Factory implements AdvancedMessageFormat.PartFactory {
      public AdvancedMessageFormat.Part newPart(String fieldName, String values) {
         return new GlyphNameFieldPart(fieldName);
      }

      public String getFormat() {
         return "glyph-name";
      }
   }
}
