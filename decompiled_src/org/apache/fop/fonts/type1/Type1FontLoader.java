package org.apache.fop.fonts.type1;

import java.awt.geom.RectangularShape;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.fop.fonts.CodePointMapping;
import org.apache.fop.fonts.FontLoader;
import org.apache.fop.fonts.FontResolver;
import org.apache.fop.fonts.FontType;
import org.apache.fop.fonts.SingleByteEncoding;
import org.apache.fop.fonts.SingleByteFont;

public class Type1FontLoader extends FontLoader {
   private SingleByteFont singleFont;
   private static final String[] AFM_EXTENSIONS = new String[]{".AFM", ".afm", ".Afm"};

   public Type1FontLoader(String fontFileURI, boolean embedded, boolean useKerning, FontResolver resolver) throws IOException {
      super(fontFileURI, embedded, useKerning, resolver);
   }

   private String getPFMURI(String pfbURI) {
      String pfbExt = pfbURI.substring(pfbURI.length() - 3, pfbURI.length());
      String pfmExt = pfbExt.substring(0, 2) + (Character.isUpperCase(pfbExt.charAt(2)) ? "M" : "m");
      return pfbURI.substring(0, pfbURI.length() - 4) + "." + pfmExt;
   }

   protected void read() throws IOException {
      AFMFile afm = null;
      PFMFile pfm = null;
      InputStream afmIn = null;

      for(int i = 0; i < AFM_EXTENSIONS.length; ++i) {
         try {
            String afmUri = this.fontFileURI.substring(0, this.fontFileURI.length() - 4) + AFM_EXTENSIONS[i];
            afmIn = openFontUri(this.resolver, afmUri);
            if (afmIn != null) {
               break;
            }
         } catch (IOException var22) {
         }
      }

      if (afmIn != null) {
         try {
            AFMParser afmParser = new AFMParser();
            afm = afmParser.parse(afmIn);
         } finally {
            IOUtils.closeQuietly(afmIn);
         }
      }

      String pfmUri = this.getPFMURI(this.fontFileURI);
      InputStream pfmIn = null;

      try {
         pfmIn = openFontUri(this.resolver, pfmUri);
      } catch (IOException var18) {
      }

      if (pfmIn != null) {
         try {
            pfm = new PFMFile();
            pfm.load(pfmIn);
         } catch (IOException var20) {
            if (afm == null) {
               throw var20;
            }
         } finally {
            IOUtils.closeQuietly(pfmIn);
         }
      }

      if (afm == null && pfm == null) {
         throw new FileNotFoundException("Neither an AFM nor a PFM file was found for " + this.fontFileURI);
      } else {
         this.buildFont(afm, pfm);
         this.loaded = true;
      }
   }

   private void buildFont(AFMFile afm, PFMFile pfm) {
      if (afm == null && pfm == null) {
         throw new IllegalArgumentException("Need at least an AFM or a PFM!");
      } else {
         this.singleFont = new SingleByteFont();
         this.singleFont.setFontType(FontType.TYPE1);
         this.singleFont.setResolver(this.resolver);
         if (this.embedded) {
            this.singleFont.setEmbedFileName(this.fontFileURI);
         }

         this.returnFont = this.singleFont;
         this.handleEncoding(afm, pfm);
         this.handleFontName(afm, pfm);
         this.handleMetrics(afm, pfm);
      }
   }

   private void handleEncoding(AFMFile afm, PFMFile pfm) {
      if (afm != null) {
         String encoding = afm.getEncodingScheme();
         this.singleFont.setUseNativeEncoding(true);
         if ("AdobeStandardEncoding".equals(encoding)) {
            this.singleFont.setEncoding("StandardEncoding");
            this.addUnencodedBasedOnEncoding(afm);
            afm.overridePrimaryEncoding(this.singleFont.getEncoding());
         } else {
            String effEncodingName;
            if ("FontSpecific".equals(encoding)) {
               effEncodingName = afm.getFontName() + "Encoding";
            } else {
               effEncodingName = encoding;
            }

            if (log.isDebugEnabled()) {
               log.debug("Unusual font encoding encountered: " + encoding + " -> " + effEncodingName);
            }

            CodePointMapping mapping = this.buildCustomEncoding(effEncodingName, afm);
            this.singleFont.setEncoding(mapping);
            this.addUnencodedBasedOnAFM(afm);
         }
      } else if (pfm.getCharSet() >= 0 && pfm.getCharSet() <= 2) {
         this.singleFont.setEncoding(pfm.getCharSetName() + "Encoding");
      } else {
         log.warn("The PFM reports an unsupported encoding (" + pfm.getCharSetName() + "). The font may not work as expected.");
         this.singleFont.setEncoding("WinAnsiEncoding");
      }

   }

   private Set toGlyphSet(String[] glyphNames) {
      Set glyphSet = new HashSet();
      int i = 0;

      for(int c = glyphNames.length; i < c; ++i) {
         glyphSet.add(glyphNames[i]);
      }

      return glyphSet;
   }

   private void addUnencodedBasedOnEncoding(AFMFile afm) {
      SingleByteEncoding encoding = this.singleFont.getEncoding();
      Set glyphNames = this.toGlyphSet(encoding.getCharNameMap());
      List charMetrics = afm.getCharMetrics();
      int i = 0;

      for(int c = afm.getCharCount(); i < c; ++i) {
         AFMCharMetrics metrics = (AFMCharMetrics)charMetrics.get(i);
         String charName = metrics.getCharName();
         if (charName != null && !glyphNames.contains(charName)) {
            this.singleFont.addUnencodedCharacter(metrics.getCharacter(), (int)Math.round(metrics.getWidthX()));
         }
      }

   }

   private void addUnencodedBasedOnAFM(AFMFile afm) {
      List charMetrics = afm.getCharMetrics();
      int i = 0;

      for(int c = afm.getCharCount(); i < c; ++i) {
         AFMCharMetrics metrics = (AFMCharMetrics)charMetrics.get(i);
         if (!metrics.hasCharCode() && metrics.getCharacter() != null) {
            this.singleFont.addUnencodedCharacter(metrics.getCharacter(), (int)Math.round(metrics.getWidthX()));
         }
      }

   }

   private void handleFontName(AFMFile afm, PFMFile pfm) {
      if (afm != null) {
         this.returnFont.setFontName(afm.getFontName());
         this.returnFont.setFullName(afm.getFullName());
         Set names = new HashSet();
         names.add(afm.getFamilyName());
         this.returnFont.setFamilyNames(names);
      } else {
         this.returnFont.setFontName(pfm.getPostscriptName());
         String fullName = pfm.getPostscriptName();
         fullName = fullName.replace('-', ' ');
         this.returnFont.setFullName(fullName);
         Set names = new HashSet();
         names.add(pfm.getWindowsName());
         this.returnFont.setFamilyNames(names);
      }

   }

   private void handleMetrics(AFMFile afm, PFMFile pfm) {
      if (afm != null) {
         if (afm.getCapHeight() != null) {
            this.returnFont.setCapHeight(afm.getCapHeight().intValue());
         }

         if (afm.getXHeight() != null) {
            this.returnFont.setXHeight(afm.getXHeight().intValue());
         }

         if (afm.getAscender() != null) {
            this.returnFont.setAscender(afm.getAscender().intValue());
         }

         if (afm.getDescender() != null) {
            this.returnFont.setDescender(afm.getDescender().intValue());
         }

         this.returnFont.setFontBBox(afm.getFontBBoxAsIntArray());
         if (afm.getStdVW() != null) {
            this.returnFont.setStemV(afm.getStdVW().intValue());
         } else {
            this.returnFont.setStemV(80);
         }

         this.returnFont.setItalicAngle((int)afm.getWritingDirectionMetrics(0).getItalicAngle());
      } else {
         this.returnFont.setFontBBox(pfm.getFontBBox());
         this.returnFont.setStemV(pfm.getStemV());
         this.returnFont.setItalicAngle(pfm.getItalicAngle());
      }

      if (pfm != null) {
         if (this.returnFont.getCapHeight() == 0) {
            this.returnFont.setCapHeight(pfm.getCapHeight());
         }

         if (this.returnFont.getXHeight(1) == 0) {
            this.returnFont.setXHeight(pfm.getXHeight());
         }

         if (this.returnFont.getAscender() == 0) {
            this.returnFont.setAscender(pfm.getLowerCaseAscent());
         }

         if (this.returnFont.getDescender() == 0) {
            this.returnFont.setDescender(pfm.getLowerCaseDescent());
         }
      }

      int desc;
      AFMCharMetrics chm;
      RectangularShape rect;
      if (this.returnFont.getXHeight(1) == 0) {
         desc = 0;
         if (afm != null) {
            chm = afm.getChar("x");
            if (chm != null) {
               rect = chm.getBBox();
               if (rect != null) {
                  desc = (int)Math.round(rect.getMinX());
               }
            }
         }

         if (desc == 0) {
            desc = Math.round((float)this.returnFont.getFontBBox()[3] * 0.6F);
         }

         this.returnFont.setXHeight(desc);
      }

      if (this.returnFont.getAscender() == 0) {
         desc = 0;
         if (afm != null) {
            chm = afm.getChar("d");
            if (chm != null) {
               rect = chm.getBBox();
               if (rect != null) {
                  desc = (int)Math.round(rect.getMinX());
               }
            }
         }

         if (desc == 0) {
            desc = Math.round((float)this.returnFont.getFontBBox()[3] * 0.9F);
         }

         this.returnFont.setAscender(desc);
      }

      if (this.returnFont.getDescender() == 0) {
         desc = 0;
         if (afm != null) {
            chm = afm.getChar("p");
            if (chm != null) {
               rect = chm.getBBox();
               if (rect != null) {
                  desc = (int)Math.round(rect.getMinX());
               }
            }
         }

         if (desc == 0) {
            desc = this.returnFont.getFontBBox()[1];
         }

         this.returnFont.setDescender(desc);
      }

      if (this.returnFont.getCapHeight() == 0) {
         this.returnFont.setCapHeight(this.returnFont.getAscender());
      }

      if (afm != null) {
         String charSet = afm.getCharacterSet();
         int flags = 0;
         if ("Special".equals(charSet)) {
            flags |= 4;
         } else if (this.singleFont.getEncoding().mapChar('A') == 'A') {
            flags |= 32;
         } else {
            flags |= 4;
         }

         if (afm.getWritingDirectionMetrics(0).isFixedPitch()) {
            flags |= 1;
         }

         if (afm.getWritingDirectionMetrics(0).getItalicAngle() != 0.0) {
            flags |= 64;
         }

         this.returnFont.setFlags(flags);
         this.returnFont.setFirstChar(afm.getFirstChar());
         this.returnFont.setLastChar(afm.getLastChar());
         Iterator iter = afm.getCharMetrics().iterator();

         while(iter.hasNext()) {
            AFMCharMetrics chm = (AFMCharMetrics)iter.next();
            if (chm.hasCharCode()) {
               this.singleFont.setWidth(chm.getCharCode(), (int)Math.round(chm.getWidthX()));
            }
         }

         if (this.useKerning) {
            this.returnFont.replaceKerningMap(afm.createXKerningMapEncoded());
         }
      } else {
         this.returnFont.setFlags(pfm.getFlags());
         this.returnFont.setFirstChar(pfm.getFirstChar());
         this.returnFont.setLastChar(pfm.getLastChar());

         for(short i = pfm.getFirstChar(); i <= pfm.getLastChar(); ++i) {
            this.singleFont.setWidth(i, pfm.getCharWidth(i));
         }

         if (this.useKerning) {
            this.returnFont.replaceKerningMap(pfm.getKerning());
         }
      }

   }

   private CodePointMapping buildCustomEncoding(String encodingName, AFMFile afm) {
      List chars = afm.getCharMetrics();
      int mappingCount = 0;
      Iterator iter = chars.iterator();

      while(iter.hasNext()) {
         AFMCharMetrics charMetrics = (AFMCharMetrics)iter.next();
         if (charMetrics.getCharCode() >= 0) {
            String u = charMetrics.getUnicodeSequence();
            if (u != null && u.length() == 1) {
               ++mappingCount;
            }
         }
      }

      int[] table = new int[mappingCount * 2];
      String[] charNameMap = new String[256];
      iter = chars.iterator();
      int idx = 0;

      while(iter.hasNext()) {
         AFMCharMetrics charMetrics = (AFMCharMetrics)iter.next();
         if (charMetrics.getCharCode() >= 0) {
            charNameMap[charMetrics.getCharCode()] = charMetrics.getCharName();
            String unicodes = charMetrics.getUnicodeSequence();
            if (unicodes == null) {
               log.info("No Unicode mapping for glyph: " + charMetrics);
            } else if (unicodes.length() == 1) {
               table[idx] = charMetrics.getCharCode();
               ++idx;
               table[idx] = unicodes.charAt(0);
               ++idx;
            } else {
               log.warn("Multi-character representation of glyph not currently supported: " + charMetrics);
            }
         }
      }

      return new CodePointMapping(encodingName, table, charNameMap);
   }
}
