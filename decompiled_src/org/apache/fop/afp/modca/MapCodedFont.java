package org.apache.fop.afp.modca;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.fop.afp.fonts.AFPFont;
import org.apache.fop.afp.fonts.CharacterSet;
import org.apache.fop.afp.fonts.DoubleByteFont;
import org.apache.fop.afp.fonts.FontRuntimeException;
import org.apache.fop.afp.fonts.OutlineFont;
import org.apache.fop.afp.fonts.RasterFont;
import org.apache.fop.afp.util.BinaryUtils;

public class MapCodedFont extends AbstractStructuredObject {
   private final List fontList = new ArrayList();

   public void writeToStream(OutputStream os) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] startData = new byte[9];
      this.copySF(startData, (byte)-85, (byte)-118);
      baos.write(startData);
      Iterator iter = this.fontList.iterator();

      while(iter.hasNext()) {
         FontDefinition fd = (FontDefinition)iter.next();
         baos.write(0);
         if (fd.scale == 0) {
            baos.write(34);
         } else {
            baos.write(58);
         }

         baos.write(12);
         baos.write(2);
         baos.write(-122);
         baos.write(0);
         baos.write(fd.characterSet);
         baos.write(12);
         baos.write(2);
         baos.write(-123);
         baos.write(0);
         baos.write(fd.codePage);
         baos.write(4);
         baos.write(38);
         baos.write(fd.orientation);
         baos.write(0);
         baos.write(4);
         baos.write(36);
         baos.write(5);
         baos.write(fd.fontReferenceKey);
         if (fd.scale != 0) {
            baos.write(20);
            baos.write(31);
            baos.write(0);
            baos.write(0);
            baos.write(BinaryUtils.convert(fd.scale, 2));
            baos.write(new byte[]{0, 0});
            baos.write(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
            baos.write(96);
            baos.write(4);
            baos.write(93);
            baos.write(BinaryUtils.convert(fd.scale, 2));
         }
      }

      byte[] data = baos.toByteArray();
      byte[] rl1 = BinaryUtils.convert(data.length - 1, 2);
      data[1] = rl1[0];
      data[2] = rl1[1];
      os.write(data);
   }

   public void addFont(int fontReference, AFPFont font, int size, int orientation) throws MaximumSizeExceededException {
      FontDefinition fontDefinition = new FontDefinition();
      fontDefinition.fontReferenceKey = BinaryUtils.convert(fontReference)[0];
      switch (orientation) {
         case 90:
            fontDefinition.orientation = 45;
            break;
         case 180:
            fontDefinition.orientation = 90;
            break;
         case 270:
            fontDefinition.orientation = -121;
            break;
         default:
            fontDefinition.orientation = 0;
      }

      try {
         CharacterSet cs;
         if (font instanceof RasterFont) {
            RasterFont raster = (RasterFont)font;
            cs = raster.getCharacterSet(size);
            if (cs == null) {
               String msg = "Character set not found for font " + font.getFontName() + " with point size " + size;
               log.error(msg);
               throw new FontRuntimeException(msg);
            }

            fontDefinition.characterSet = cs.getNameBytes();
            if (fontDefinition.characterSet.length != 8) {
               throw new IllegalArgumentException("The character set " + new String(fontDefinition.characterSet, "Cp1146") + " must have a fixed length of 8 characters.");
            }

            fontDefinition.codePage = cs.getCodePage().getBytes("Cp1146");
            if (fontDefinition.codePage.length != 8) {
               throw new IllegalArgumentException("The code page " + new String(fontDefinition.codePage, "Cp1146") + " must have a fixed length of 8 characters.");
            }
         } else if (font instanceof OutlineFont) {
            OutlineFont outline = (OutlineFont)font;
            cs = outline.getCharacterSet();
            fontDefinition.characterSet = cs.getNameBytes();
            fontDefinition.scale = 20 * size / 1000;
            fontDefinition.codePage = cs.getCodePage().getBytes("Cp1146");
            if (fontDefinition.codePage.length != 8) {
               throw new IllegalArgumentException("The code page " + new String(fontDefinition.codePage, "Cp1146") + " must have a fixed length of 8 characters.");
            }
         } else {
            if (!(font instanceof DoubleByteFont)) {
               String msg = "Font of type " + font.getClass().getName() + " not recognized.";
               log.error(msg);
               throw new FontRuntimeException(msg);
            }

            DoubleByteFont outline = (DoubleByteFont)font;
            cs = outline.getCharacterSet();
            fontDefinition.characterSet = cs.getNameBytes();
            fontDefinition.scale = 20 * size / 1000;
            fontDefinition.codePage = cs.getCodePage().getBytes("Cp1146");
            if (fontDefinition.codePage.length != 8) {
               throw new IllegalArgumentException("The code page " + new String(fontDefinition.codePage, "Cp1146") + " must have a fixed length of 8 characters.");
            }
         }

         if (this.fontList.size() > 253) {
            throw new MaximumSizeExceededException();
         } else {
            this.fontList.add(fontDefinition);
         }
      } catch (UnsupportedEncodingException var9) {
         throw new FontRuntimeException("Failed to create font  due to a UnsupportedEncodingException", var9);
      }
   }

   private class FontDefinition {
      private byte[] codePage;
      private byte[] characterSet;
      private byte fontReferenceKey;
      private byte orientation;
      private int scale;

      private FontDefinition() {
         this.scale = 0;
      }

      // $FF: synthetic method
      FontDefinition(Object x1) {
         this();
      }
   }
}
