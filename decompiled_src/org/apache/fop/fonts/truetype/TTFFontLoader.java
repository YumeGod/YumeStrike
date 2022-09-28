package org.apache.fop.fonts.truetype;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.fop.fonts.BFEntry;
import org.apache.fop.fonts.CIDFontType;
import org.apache.fop.fonts.EncodingMode;
import org.apache.fop.fonts.FontLoader;
import org.apache.fop.fonts.FontResolver;
import org.apache.fop.fonts.FontType;
import org.apache.fop.fonts.MultiByteFont;
import org.apache.fop.fonts.NamedCharacter;
import org.apache.fop.fonts.SingleByteFont;
import org.apache.xmlgraphics.fonts.Glyphs;

public class TTFFontLoader extends FontLoader {
   private MultiByteFont multiFont;
   private SingleByteFont singleFont;
   private String subFontName;
   private EncodingMode encodingMode;

   public TTFFontLoader(String fontFileURI, FontResolver resolver) {
      this(fontFileURI, (String)null, true, EncodingMode.AUTO, true, resolver);
   }

   public TTFFontLoader(String fontFileURI, String subFontName, boolean embedded, EncodingMode encodingMode, boolean useKerning, FontResolver resolver) {
      super(fontFileURI, embedded, true, resolver);
      this.subFontName = subFontName;
      this.encodingMode = encodingMode;
      if (this.encodingMode == EncodingMode.AUTO) {
         this.encodingMode = EncodingMode.CID;
      }

   }

   protected void read() throws IOException {
      this.read(this.subFontName);
   }

   private void read(String ttcFontName) throws IOException {
      InputStream in = openFontUri(this.resolver, this.fontFileURI);

      try {
         TTFFile ttf = new TTFFile();
         FontFileReader reader = new FontFileReader(in);
         boolean supported = ttf.readFont(reader, ttcFontName);
         if (!supported) {
            throw new IOException("TrueType font is not supported: " + this.fontFileURI);
         }

         this.buildFont(ttf, ttcFontName);
         this.loaded = true;
      } finally {
         IOUtils.closeQuietly(in);
      }

   }

   private void buildFont(TTFFile ttf, String ttcFontName) {
      if (ttf.isCFF()) {
         throw new UnsupportedOperationException("OpenType fonts with CFF data are not supported, yet");
      } else {
         boolean isCid = this.embedded;
         if (this.encodingMode == EncodingMode.SINGLE_BYTE) {
            isCid = false;
         }

         if (isCid) {
            this.multiFont = new MultiByteFont();
            this.returnFont = this.multiFont;
            this.multiFont.setTTCName(ttcFontName);
         } else {
            this.singleFont = new SingleByteFont();
            this.returnFont = this.singleFont;
         }

         this.returnFont.setResolver(this.resolver);
         this.returnFont.setFontName(ttf.getPostScriptName());
         this.returnFont.setFullName(ttf.getFullName());
         this.returnFont.setFamilyNames(ttf.getFamilyNames());
         this.returnFont.setFontSubFamilyName(ttf.getSubFamilyName());
         this.returnFont.setCapHeight(ttf.getCapHeight());
         this.returnFont.setXHeight(ttf.getXHeight());
         this.returnFont.setAscender(ttf.getLowerCaseAscent());
         this.returnFont.setDescender(ttf.getLowerCaseDescent());
         this.returnFont.setFontBBox(ttf.getFontBBox());
         this.returnFont.setFlags(ttf.getFlags());
         this.returnFont.setStemV(Integer.parseInt(ttf.getStemV()));
         this.returnFont.setItalicAngle(Integer.parseInt(ttf.getItalicAngle()));
         this.returnFont.setMissingWidth(0);
         this.returnFont.setWeight(ttf.getWeightClass());
         if (isCid) {
            this.multiFont.setCIDType(CIDFontType.CIDTYPE2);
            int[] wx = ttf.getWidths();
            this.multiFont.setWidthArray(wx);
            List entries = ttf.getCMaps();
            BFEntry[] bfentries = new BFEntry[entries.size()];
            int pos = 0;

            for(Iterator iter = ttf.getCMaps().listIterator(); iter.hasNext(); ++pos) {
               TTFCmapEntry ce = (TTFCmapEntry)iter.next();
               bfentries[pos] = new BFEntry(ce.getUnicodeStart(), ce.getUnicodeEnd(), ce.getGlyphStartIndex());
            }

            this.multiFont.setBFEntries(bfentries);
         } else {
            this.singleFont.setFontType(FontType.TRUETYPE);
            this.singleFont.setEncoding(ttf.getCharSetName());
            this.returnFont.setFirstChar(ttf.getFirstChar());
            this.returnFont.setLastChar(ttf.getLastChar());
            this.copyWidthsSingleByte(ttf);
         }

         if (this.useKerning) {
            this.copyKerning(ttf, isCid);
         }

         if (this.embedded && ttf.isEmbeddable()) {
            this.returnFont.setEmbedFileName(this.fontFileURI);
         }

      }
   }

   private void copyWidthsSingleByte(TTFFile ttf) {
      int[] wx = ttf.getWidths();

      for(int i = this.singleFont.getFirstChar(); i <= this.singleFont.getLastChar(); ++i) {
         this.singleFont.setWidth(i, ttf.getCharWidth(i));
      }

      Iterator iter = ttf.getCMaps().listIterator();

      while(true) {
         TTFCmapEntry ce;
         do {
            if (!iter.hasNext()) {
               return;
            }

            ce = (TTFCmapEntry)iter.next();
         } while(ce.getUnicodeStart() >= 65534);

         for(char u = (char)ce.getUnicodeStart(); u <= ce.getUnicodeEnd(); ++u) {
            int codePoint = this.singleFont.getEncoding().mapChar(u);
            if (codePoint <= 0) {
               String unicode = Character.toString(u);
               String charName = Glyphs.stringToGlyph(unicode);
               if (charName.length() > 0) {
                  NamedCharacter nc = new NamedCharacter(charName, unicode);
                  int glyphIndex = ce.getGlyphStartIndex() + u - ce.getUnicodeStart();
                  this.singleFont.addUnencodedCharacter(nc, wx[glyphIndex]);
               }
            }
         }
      }
   }

   private void copyKerning(TTFFile ttf, boolean isCid) {
      Iterator iter;
      if (isCid) {
         iter = ttf.getKerning().keySet().iterator();
      } else {
         iter = ttf.getAnsiKerning().keySet().iterator();
      }

      Integer kpx1;
      Map h2;
      for(; iter.hasNext(); this.returnFont.putKerningEntry(kpx1, h2)) {
         kpx1 = (Integer)iter.next();
         if (isCid) {
            h2 = (Map)ttf.getKerning().get(kpx1);
         } else {
            h2 = (Map)ttf.getAnsiKerning().get(kpx1);
         }
      }

   }
}
