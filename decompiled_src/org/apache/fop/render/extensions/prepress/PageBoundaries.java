package org.apache.fop.render.extensions.prepress;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.text.MessageFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.fop.fo.properties.FixedLength;
import org.apache.xmlgraphics.util.QName;

public class PageBoundaries {
   public static final QName EXT_BLEED = new QName("http://xmlgraphics.apache.org/fop/extensions", (String)null, "bleed");
   public static final QName EXT_CROP_OFFSET = new QName("http://xmlgraphics.apache.org/fop/extensions", (String)null, "crop-offset");
   public static final QName EXT_CROP_BOX = new QName("http://xmlgraphics.apache.org/fop/extensions", (String)null, "crop-box");
   private static final Pattern SIZE_UNIT_PATTERN = Pattern.compile("^(-?\\d*\\.?\\d*)(px|in|cm|mm|pt|pc|mpt)$");
   private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
   private Rectangle trimBox;
   private Rectangle bleedBox;
   private Rectangle mediaBox;
   private Rectangle cropBox;

   public PageBoundaries(Dimension pageSize, String bleed, String cropOffset, String cropBoxSelector) {
      this.calculate(pageSize, bleed, cropOffset, cropBoxSelector);
   }

   public PageBoundaries(Dimension pageSize, Map foreignAttributes) {
      String bleed = (String)foreignAttributes.get(EXT_BLEED);
      String cropOffset = (String)foreignAttributes.get(EXT_CROP_OFFSET);
      String cropBoxSelector = (String)foreignAttributes.get(EXT_CROP_BOX);
      this.calculate(pageSize, bleed, cropOffset, cropBoxSelector);
   }

   private void calculate(Dimension pageSize, String bleed, String cropOffset, String cropBoxSelector) {
      this.trimBox = new Rectangle(pageSize);
      this.bleedBox = getBleedBoxRectangle(this.trimBox, bleed);
      Rectangle cropMarksBox = getCropMarksAreaRectangle(this.trimBox, cropOffset);
      this.mediaBox = new Rectangle();
      this.mediaBox.add(this.trimBox);
      this.mediaBox.add(this.bleedBox);
      this.mediaBox.add(cropMarksBox);
      if ("trim-box".equals(cropBoxSelector)) {
         this.cropBox = this.trimBox;
      } else if ("bleed-box".equals(cropBoxSelector)) {
         this.cropBox = this.bleedBox;
      } else {
         if (!"media-box".equals(cropBoxSelector) && cropBoxSelector != null && !"".equals(cropBoxSelector)) {
            String err = "The crop-box has invalid value: {0}, possible values of crop-box: (trim-box|bleed-box|media-box)";
            throw new IllegalArgumentException(MessageFormat.format("The crop-box has invalid value: {0}, possible values of crop-box: (trim-box|bleed-box|media-box)", cropBoxSelector));
         }

         this.cropBox = this.mediaBox;
      }

   }

   public Rectangle getTrimBox() {
      return this.trimBox;
   }

   public Rectangle getBleedBox() {
      return this.bleedBox;
   }

   public Rectangle getMediaBox() {
      return this.mediaBox;
   }

   public Rectangle getCropBox() {
      return this.cropBox;
   }

   private static Rectangle getBleedBoxRectangle(Rectangle trimBox, String bleed) {
      return getRectangleUsingOffset(trimBox, bleed);
   }

   private static Rectangle getCropMarksAreaRectangle(Rectangle trimBox, String cropOffsets) {
      return getRectangleUsingOffset(trimBox, cropOffsets);
   }

   private static Rectangle getRectangleUsingOffset(Rectangle originalRect, String offset) {
      if (offset != null && !"".equals(offset) && originalRect != null) {
         String[] offsets = WHITESPACE_PATTERN.split(offset);
         int[] coords = new int[4];
         switch (offsets.length) {
            case 1:
               coords[0] = getLengthIntValue(offsets[0]);
               coords[1] = coords[0];
               coords[2] = coords[0];
               coords[3] = coords[0];
               break;
            case 2:
               coords[0] = getLengthIntValue(offsets[0]);
               coords[1] = getLengthIntValue(offsets[1]);
               coords[2] = coords[0];
               coords[3] = coords[1];
               break;
            case 3:
               coords[0] = getLengthIntValue(offsets[0]);
               coords[1] = getLengthIntValue(offsets[1]);
               coords[2] = getLengthIntValue(offsets[2]);
               coords[3] = coords[1];
               break;
            case 4:
               coords[0] = getLengthIntValue(offsets[0]);
               coords[1] = getLengthIntValue(offsets[1]);
               coords[2] = getLengthIntValue(offsets[2]);
               coords[3] = getLengthIntValue(offsets[3]);
               break;
            default:
               throw new IllegalArgumentException("Too many arguments");
         }

         return new Rectangle(originalRect.x - coords[3], originalRect.y - coords[0], originalRect.width + coords[3] + coords[1], originalRect.height + coords[0] + coords[2]);
      } else {
         return originalRect;
      }
   }

   private static int getLengthIntValue(String length) {
      String err = "Incorrect length value: {0}";
      Matcher m = SIZE_UNIT_PATTERN.matcher(length);
      if (m.find()) {
         return FixedLength.getInstance(Double.parseDouble(m.group(1)), m.group(2)).getLength().getValue();
      } else {
         throw new IllegalArgumentException(MessageFormat.format("Incorrect length value: {0}", length));
      }
   }
}
