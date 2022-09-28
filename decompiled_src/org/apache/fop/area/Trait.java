package org.apache.fop.area;

import java.awt.Color;
import java.io.Serializable;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.util.ColorUtil;
import org.apache.xmlgraphics.image.loader.ImageInfo;

public class Trait implements Serializable {
   public static final Integer INTERNAL_LINK = new Integer(1);
   public static final Integer EXTERNAL_LINK = new Integer(2);
   public static final Integer FONT = new Integer(3);
   public static final Integer FONT_SIZE = new Integer(4);
   public static final Integer COLOR = new Integer(7);
   public static final Integer PROD_ID = new Integer(8);
   public static final Integer BACKGROUND = new Integer(9);
   public static final Integer UNDERLINE = new Integer(10);
   public static final Integer OVERLINE = new Integer(11);
   public static final Integer LINETHROUGH = new Integer(12);
   public static final Integer BORDER_START = new Integer(15);
   public static final Integer BORDER_END = new Integer(16);
   public static final Integer BORDER_BEFORE = new Integer(17);
   public static final Integer BORDER_AFTER = new Integer(18);
   public static final Integer PADDING_START = new Integer(19);
   public static final Integer PADDING_END = new Integer(20);
   public static final Integer PADDING_BEFORE = new Integer(21);
   public static final Integer PADDING_AFTER = new Integer(22);
   public static final Integer SPACE_START = new Integer(23);
   public static final Integer SPACE_END = new Integer(24);
   public static final Integer START_INDENT = new Integer(27);
   public static final Integer END_INDENT = new Integer(28);
   public static final Integer SPACE_BEFORE = new Integer(29);
   public static final Integer SPACE_AFTER = new Integer(30);
   public static final Integer IS_REFERENCE_AREA = new Integer(31);
   public static final Integer IS_VIEWPORT_AREA = new Integer(32);
   public static final Integer BLINK = new Integer(33);
   public static final Integer UNDERLINE_COLOR = new Integer(34);
   public static final Integer OVERLINE_COLOR = new Integer(35);
   public static final Integer LINETHROUGH_COLOR = new Integer(36);
   public static final Integer PTR = new Integer(37);
   public static final int MAX_TRAIT_KEY = 37;
   private static final TraitInfo[] TRAIT_INFO = new TraitInfo[38];

   private static void put(Integer key, TraitInfo info) {
      TRAIT_INFO[key] = info;
   }

   public static String getTraitName(Object traitCode) {
      return TRAIT_INFO[(Integer)traitCode].getName();
   }

   public static Class getTraitClass(Object traitCode) {
      return TRAIT_INFO[(Integer)traitCode].getClazz();
   }

   static {
      put(PTR, new TraitInfo("ptr", String.class));
      put(INTERNAL_LINK, new TraitInfo("internal-link", InternalLink.class));
      put(EXTERNAL_LINK, new TraitInfo("external-link", ExternalLink.class));
      put(FONT, new TraitInfo("font", FontTriplet.class));
      put(FONT_SIZE, new TraitInfo("font-size", Integer.class));
      put(COLOR, new TraitInfo("color", Color.class));
      put(PROD_ID, new TraitInfo("prod-id", String.class));
      put(BACKGROUND, new TraitInfo("background", Background.class));
      put(UNDERLINE, new TraitInfo("underline-score", Boolean.class));
      put(UNDERLINE_COLOR, new TraitInfo("underline-score-color", Color.class));
      put(OVERLINE, new TraitInfo("overline-score", Boolean.class));
      put(OVERLINE_COLOR, new TraitInfo("overline-score-color", Color.class));
      put(LINETHROUGH, new TraitInfo("through-score", Boolean.class));
      put(LINETHROUGH_COLOR, new TraitInfo("through-score-color", Color.class));
      put(BLINK, new TraitInfo("blink", Boolean.class));
      put(BORDER_START, new TraitInfo("border-start", BorderProps.class));
      put(BORDER_END, new TraitInfo("border-end", BorderProps.class));
      put(BORDER_BEFORE, new TraitInfo("border-before", BorderProps.class));
      put(BORDER_AFTER, new TraitInfo("border-after", BorderProps.class));
      put(PADDING_START, new TraitInfo("padding-start", Integer.class));
      put(PADDING_END, new TraitInfo("padding-end", Integer.class));
      put(PADDING_BEFORE, new TraitInfo("padding-before", Integer.class));
      put(PADDING_AFTER, new TraitInfo("padding-after", Integer.class));
      put(SPACE_START, new TraitInfo("space-start", Integer.class));
      put(SPACE_END, new TraitInfo("space-end", Integer.class));
      put(START_INDENT, new TraitInfo("start-indent", Integer.class));
      put(END_INDENT, new TraitInfo("end-indent", Integer.class));
      put(SPACE_BEFORE, new TraitInfo("space-before", Integer.class));
      put(SPACE_AFTER, new TraitInfo("space-after", Integer.class));
      put(IS_REFERENCE_AREA, new TraitInfo("is-reference-area", Boolean.class));
      put(IS_VIEWPORT_AREA, new TraitInfo("is-viewport-area", Boolean.class));
   }

   public static class Background implements Serializable {
      private Color color = null;
      private String url = null;
      private ImageInfo imageInfo = null;
      private int repeat;
      private int horiz;
      private int vertical;

      public Color getColor() {
         return this.color;
      }

      public int getHoriz() {
         return this.horiz;
      }

      public int getRepeat() {
         return this.repeat;
      }

      public String getURL() {
         return this.url;
      }

      public ImageInfo getImageInfo() {
         return this.imageInfo;
      }

      public int getVertical() {
         return this.vertical;
      }

      public void setColor(Color color) {
         this.color = color;
      }

      public void setHoriz(int horiz) {
         this.horiz = horiz;
      }

      public void setRepeat(int repeat) {
         this.repeat = repeat;
      }

      public void setRepeat(String repeat) {
         this.setRepeat(getConstantForRepeat(repeat));
      }

      public void setURL(String url) {
         this.url = url;
      }

      public void setImageInfo(ImageInfo info) {
         this.imageInfo = info;
      }

      public void setVertical(int vertical) {
         this.vertical = vertical;
      }

      private String getRepeatString() {
         switch (this.getRepeat()) {
            case 96:
               return "no-repeat";
            case 112:
               return "repeat";
            case 113:
               return "repeat-x";
            case 114:
               return "repeat-y";
            default:
               throw new IllegalStateException("Illegal repeat style: " + this.getRepeat());
         }
      }

      private static int getConstantForRepeat(String repeat) {
         if ("repeat".equalsIgnoreCase(repeat)) {
            return 112;
         } else if ("repeat-x".equalsIgnoreCase(repeat)) {
            return 113;
         } else if ("repeat-y".equalsIgnoreCase(repeat)) {
            return 114;
         } else if ("no-repeat".equalsIgnoreCase(repeat)) {
            return 96;
         } else {
            throw new IllegalStateException("Illegal repeat style: " + repeat);
         }
      }

      public String toString() {
         StringBuffer sb = new StringBuffer();
         if (this.color != null) {
            sb.append("color=").append(ColorUtil.colorToString(this.color));
         }

         if (this.url != null) {
            if (this.color != null) {
               sb.append(",");
            }

            sb.append("url=").append(this.url);
            sb.append(",repeat=").append(this.getRepeatString());
            sb.append(",horiz=").append(this.horiz);
            sb.append(",vertical=").append(this.vertical);
         }

         return sb.toString();
      }
   }

   public static class ExternalLink implements Serializable {
      private String destination;
      private boolean newWindow;

      public ExternalLink(String destination, boolean newWindow) {
         this.destination = destination;
         this.newWindow = newWindow;
      }

      protected static ExternalLink makeFromTraitValue(String traitValue) {
         String dest = null;
         boolean newWindow = false;
         String[] values = traitValue.split(",");
         int i = 0;

         for(int c = values.length; i < c; ++i) {
            String v = values[i];
            if (v.startsWith("dest=")) {
               dest = v.substring(5);
            } else {
               if (!v.startsWith("newWindow=")) {
                  throw new IllegalArgumentException("Malformed trait value for Trait.ExternalLink: " + traitValue);
               }

               newWindow = Boolean.valueOf(v.substring(10));
            }
         }

         return new ExternalLink(dest, newWindow);
      }

      public String getDestination() {
         return this.destination;
      }

      public boolean newWindow() {
         return this.newWindow;
      }

      public String toString() {
         StringBuffer sb = new StringBuffer(64);
         sb.append("newWindow=").append(this.newWindow);
         sb.append(",dest=").append(this.destination);
         return sb.toString();
      }
   }

   public static class InternalLink implements Serializable {
      private String pvKey;
      private String idRef;

      public InternalLink(String pvKey, String idRef) {
         this.setPVKey(pvKey);
         this.setIDRef(idRef);
      }

      public InternalLink(String attrValue) {
         String[] values = parseXMLAttribute(attrValue);
         this.setPVKey(values[0]);
         this.setIDRef(values[1]);
      }

      public void setPVKey(String pvKey) {
         this.pvKey = pvKey;
      }

      public String getPVKey() {
         return this.pvKey;
      }

      public void setIDRef(String idRef) {
         this.idRef = idRef;
      }

      public String getIDRef() {
         return this.idRef;
      }

      public String xmlAttribute() {
         return makeXMLAttribute(this.pvKey, this.idRef);
      }

      public static String makeXMLAttribute(String pvKey, String idRef) {
         return "(" + (pvKey == null ? "" : pvKey) + "," + (idRef == null ? "" : idRef) + ")";
      }

      public static String[] parseXMLAttribute(String attrValue) {
         String[] result = new String[]{null, null};
         if (attrValue != null) {
            int len = attrValue.length();
            if (len >= 2 && attrValue.charAt(0) == '(' && attrValue.charAt(len - 1) == ')' && attrValue.indexOf(44) != -1) {
               String value = attrValue.substring(1, len - 1);
               int delimIndex = value.indexOf(44);
               result[0] = value.substring(0, delimIndex).trim();
               result[1] = value.substring(delimIndex + 1, value.length()).trim();
            } else {
               result[0] = attrValue;
            }
         }

         return result;
      }

      public String toString() {
         StringBuffer sb = new StringBuffer();
         sb.append("pvKey=").append(this.pvKey);
         sb.append(",idRef=").append(this.idRef);
         return sb.toString();
      }
   }

   private static class TraitInfo {
      private String name;
      private Class clazz;

      public TraitInfo(String name, Class clazz) {
         this.name = name;
         this.clazz = clazz;
      }

      public String getName() {
         return this.name;
      }

      public Class getClazz() {
         return this.clazz;
      }
   }
}
