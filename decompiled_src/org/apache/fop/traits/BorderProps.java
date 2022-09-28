package org.apache.fop.traits;

import java.awt.Color;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fo.expr.PropertyException;
import org.apache.fop.util.ColorUtil;

public class BorderProps implements Serializable {
   public static final int SEPARATE = 0;
   public static final int COLLAPSE_INNER = 1;
   public static final int COLLAPSE_OUTER = 2;
   public int style;
   public Color color;
   public int width;
   public int mode;

   public BorderProps(int style, int width, Color color, int mode) {
      this.style = style;
      this.width = width;
      this.color = color;
      this.mode = mode;
   }

   public BorderProps(String style, int width, Color color, int mode) {
      this(getConstantForStyle(style), width, color, mode);
   }

   public static int getClippedWidth(BorderProps bp) {
      return bp != null && bp.mode != 0 ? bp.width / 2 : 0;
   }

   private String getStyleString() {
      return BorderStyle.valueOf(this.style).getName();
   }

   private static int getConstantForStyle(String style) {
      return BorderStyle.valueOf(style).getEnumValue();
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (obj == this) {
         return true;
      } else if (!(obj instanceof BorderProps)) {
         return false;
      } else {
         BorderProps other = (BorderProps)obj;
         return this.style == other.style && this.color.equals(other.color) && this.width == other.width && this.mode == other.mode;
      }
   }

   public static BorderProps valueOf(FOUserAgent foUserAgent, String s) {
      if (s.startsWith("(") && s.endsWith(")")) {
         s = s.substring(1, s.length() - 1);
         Pattern pattern = Pattern.compile("([^,\\(]+(?:\\(.*\\))?)");
         Matcher m = pattern.matcher(s);
         boolean found = m.find();
         String style = m.group();
         found = m.find();
         String color = m.group();
         found = m.find();
         int width = Integer.parseInt(m.group());
         int mode = 0;
         found = m.find();
         if (found) {
            String ms = m.group();
            if ("collapse-inner".equalsIgnoreCase(ms)) {
               mode = 1;
            } else if ("collapse-outer".equalsIgnoreCase(ms)) {
               mode = 2;
            }
         }

         Color c;
         try {
            c = ColorUtil.parseColorString(foUserAgent, color);
         } catch (PropertyException var11) {
            throw new IllegalArgumentException(var11.getMessage());
         }

         return new BorderProps(style, width, c, mode);
      } else {
         throw new IllegalArgumentException("BorderProps must be surrounded by parentheses");
      }
   }

   public String toString() {
      StringBuffer sbuf = new StringBuffer();
      sbuf.append('(');
      sbuf.append(this.getStyleString());
      sbuf.append(',');
      sbuf.append(ColorUtil.colorToString(this.color));
      sbuf.append(',');
      sbuf.append(this.width);
      if (this.mode != 0) {
         sbuf.append(',');
         if (this.mode == 1) {
            sbuf.append("collapse-inner");
         } else {
            sbuf.append("collapse-outer");
         }
      }

      sbuf.append(')');
      return sbuf.toString();
   }
}
