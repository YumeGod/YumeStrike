package org.apache.fop.render.rtf.rtflib.rtfdoc;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class RtfStringConverter {
   private static final RtfStringConverter INSTANCE = new RtfStringConverter();
   private static final Map SPECIAL_CHARS = new HashMap();
   private static final Character DBLQUOTE = new Character('"');
   private static final Character QUOTE = new Character('\'');
   private static final Character SPACE = new Character(' ');

   private RtfStringConverter() {
   }

   public static RtfStringConverter getInstance() {
      return INSTANCE;
   }

   public void writeRtfString(Writer w, String str) throws IOException {
      if (str != null) {
         w.write(this.escape(str));
      }
   }

   public String escape(String str) {
      if (str == null) {
         return null;
      } else {
         StringBuffer sb = new StringBuffer(Math.max(16, str.length()));

         for(int i = 0; i < str.length(); ++i) {
            Character c = new Character(str.charAt(i));
            Character d;
            if (i != 0) {
               d = new Character(str.charAt(i - 1));
            } else {
               d = new Character(str.charAt(i));
            }

            String replacement;
            if (c.equals(DBLQUOTE) && d.equals(SPACE)) {
               replacement = "ldblquote";
            } else if (c.equals(QUOTE) && d.equals(SPACE)) {
               replacement = "lquote";
            } else {
               replacement = (String)SPECIAL_CHARS.get(c);
            }

            if (replacement != null) {
               sb.append('\\');
               sb.append(replacement);
               sb.append(' ');
            } else if (c > 127) {
               sb.append("\\u");
               sb.append(Integer.toString(c));
               sb.append("\\'3f");
            } else {
               sb.append(c);
            }
         }

         return sb.toString();
      }
   }

   static {
      SPECIAL_CHARS.put(new Character('\t'), "tab");
      SPECIAL_CHARS.put(new Character('\n'), "line");
      SPECIAL_CHARS.put(new Character('\''), "rquote");
      SPECIAL_CHARS.put(new Character('"'), "rdblquote");
      SPECIAL_CHARS.put(new Character('\\'), "\\");
      SPECIAL_CHARS.put(new Character('{'), "{");
      SPECIAL_CHARS.put(new Character('}'), "}");
   }
}
