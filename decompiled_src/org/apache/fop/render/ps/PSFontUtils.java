package org.apache.fop.render.ps;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.Base14Font;
import org.apache.fop.fonts.CustomFont;
import org.apache.fop.fonts.Font;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontType;
import org.apache.fop.fonts.LazyFont;
import org.apache.fop.fonts.SingleByteEncoding;
import org.apache.fop.fonts.SingleByteFont;
import org.apache.fop.fonts.Typeface;
import org.apache.xmlgraphics.ps.PSGenerator;
import org.apache.xmlgraphics.ps.PSResource;
import org.apache.xmlgraphics.ps.dsc.ResourceTracker;

public class PSFontUtils extends org.apache.xmlgraphics.ps.PSFontUtils {
   protected static Log log;

   public static Map writeFontDict(PSGenerator gen, FontInfo fontInfo) throws IOException {
      return writeFontDict(gen, fontInfo, fontInfo.getFonts(), true);
   }

   public static Map writeFontDict(PSGenerator gen, FontInfo fontInfo, Map fonts) throws IOException {
      return writeFontDict(gen, fontInfo, fonts, false);
   }

   private static Map writeFontDict(PSGenerator gen, FontInfo fontInfo, Map fonts, boolean encodeAllCharacters) throws IOException {
      gen.commentln("%FOPBeginFontDict");
      Map fontResources = new HashMap();
      Iterator iter = fonts.keySet().iterator();

      while(true) {
         String key;
         Typeface tf;
         do {
            if (!iter.hasNext()) {
               gen.commentln("%FOPEndFontDict");
               reencodeFonts(gen, fonts);
               return fontResources;
            }

            key = (String)iter.next();
            tf = getTypeFace(fontInfo, fonts, key);
            PSResource fontRes = new PSResource("font", tf.getFontName());
            fontResources.put(key, fontRes);
            embedFont(gen, tf, fontRes);
         } while(!(tf instanceof SingleByteFont));

         SingleByteFont sbf = (SingleByteFont)tf;
         if (encodeAllCharacters) {
            sbf.encodeAllUnencodedCharacters();
         }

         int i = 0;

         for(int c = sbf.getAdditionalEncodingCount(); i < c; ++i) {
            SingleByteEncoding encoding = sbf.getAdditionalEncoding(i);
            defineEncoding(gen, encoding);
            String postFix = "_" + (i + 1);
            PSResource derivedFontRes = defineDerivedFont(gen, tf.getFontName(), tf.getFontName() + postFix, encoding.getName());
            fontResources.put(key + postFix, derivedFontRes);
         }
      }
   }

   private static void reencodeFonts(PSGenerator gen, Map fonts) throws IOException {
      ResourceTracker tracker = gen.getResourceTracker();
      if (!tracker.isResourceSupplied(WINANSI_ENCODING_RESOURCE)) {
         defineWinAnsiEncoding(gen);
      }

      gen.commentln("%FOPBeginFontReencode");
      Iterator iter = fonts.keySet().iterator();

      while(true) {
         Typeface tf;
         do {
            if (!iter.hasNext()) {
               gen.commentln("%FOPEndFontReencode");
               return;
            }

            String key = (String)iter.next();
            tf = (Typeface)fonts.get(key);
            if (!(tf instanceof LazyFont)) {
               break;
            }

            tf = ((LazyFont)tf).getRealFont();
         } while(tf == null);

         if (null != tf.getEncodingName() && !"SymbolEncoding".equals(tf.getEncodingName()) && !"ZapfDingbatsEncoding".equals(tf.getEncodingName())) {
            if (tf instanceof Base14Font) {
               redefineFontEncoding(gen, tf.getFontName(), tf.getEncodingName());
            } else if (tf instanceof SingleByteFont) {
               SingleByteFont sbf = (SingleByteFont)tf;
               if (!sbf.isUsingNativeEncoding()) {
                  redefineFontEncoding(gen, tf.getFontName(), tf.getEncodingName());
               }
            }
         }
      }
   }

   private static Typeface getTypeFace(FontInfo fontInfo, Map fonts, String key) {
      Typeface tf = (Typeface)fonts.get(key);
      if (tf instanceof LazyFont) {
         tf = ((LazyFont)tf).getRealFont();
      }

      if (tf == null) {
         String fallbackKey = fontInfo.getInternalFontKey(Font.DEFAULT_FONT);
         tf = (Typeface)fonts.get(fallbackKey);
      }

      return tf;
   }

   public static void embedFont(PSGenerator gen, Typeface tf, PSResource fontRes) throws IOException {
      boolean embeddedFont = false;
      if (FontType.TYPE1 == tf.getFontType() && tf instanceof CustomFont) {
         CustomFont cf = (CustomFont)tf;
         if (isEmbeddable(cf)) {
            InputStream in = getInputStreamOnFont(gen, cf);
            if (in != null) {
               gen.writeDSCComment("BeginResource", (Object)fontRes);
               embedType1Font(gen, in);
               gen.writeDSCComment("EndResource");
               gen.getResourceTracker().registerSuppliedResource(fontRes);
               embeddedFont = true;
            } else {
               gen.commentln("%WARNING: Could not embed font: " + cf.getFontName());
               log.warn("Font " + cf.getFontName() + " is marked as supplied in the" + " PostScript file but could not be embedded!");
            }
         }
      }

      if (!embeddedFont) {
         gen.writeDSCComment("IncludeResource", (Object)fontRes);
      }

   }

   private static boolean isEmbeddable(CustomFont font) {
      return font.isEmbeddable();
   }

   private static InputStream getInputStreamOnFont(PSGenerator gen, CustomFont font) throws IOException {
      if (isEmbeddable(font)) {
         Source source = font.getEmbedFileSource();
         if (source == null && font.getEmbedResourceName() != null) {
            source = new StreamSource(PSFontUtils.class.getResourceAsStream(font.getEmbedResourceName()));
         }

         if (source == null) {
            return null;
         } else {
            InputStream in = null;
            if (source instanceof StreamSource) {
               in = ((StreamSource)source).getInputStream();
            }

            if (in == null && ((Source)source).getSystemId() != null) {
               try {
                  in = (new URL(((Source)source).getSystemId())).openStream();
               } catch (MalformedURLException var5) {
                  new FileNotFoundException("File not found. URL could not be resolved: " + var5.getMessage());
               }
            }

            if (in == null) {
               return null;
            } else {
               if (!(in instanceof BufferedInputStream)) {
                  in = new BufferedInputStream((InputStream)in);
               }

               return (InputStream)in;
            }
         }
      } else {
         return null;
      }
   }

   public static Map determineSuppliedFonts(ResourceTracker resTracker, FontInfo fontInfo, Map fonts) {
      Map fontResources = new HashMap();
      Iterator iter = fonts.keySet().iterator();

      while(true) {
         Typeface tf;
         do {
            PSResource fontRes;
            do {
               do {
                  if (!iter.hasNext()) {
                     return fontResources;
                  }

                  String key = (String)iter.next();
                  tf = getTypeFace(fontInfo, fonts, key);
                  fontRes = new PSResource("font", tf.getFontName());
                  fontResources.put(key, fontRes);
               } while(FontType.TYPE1 != tf.getFontType());
            } while(!(tf instanceof CustomFont));

            CustomFont cf = (CustomFont)tf;
            if (isEmbeddable(cf)) {
               resTracker.registerSuppliedResource(fontRes);
            }
         } while(!(tf instanceof SingleByteFont));

         SingleByteFont sbf = (SingleByteFont)tf;
         int i = 0;

         for(int c = sbf.getAdditionalEncodingCount(); i < c; ++i) {
            SingleByteEncoding encoding = sbf.getAdditionalEncoding(i);
            PSResource encodingRes = new PSResource("encoding", encoding.getName());
            resTracker.registerSuppliedResource(encodingRes);
            PSResource derivedFontRes = new PSResource("font", tf.getFontName() + "_" + (i + 1));
            resTracker.registerSuppliedResource(derivedFontRes);
         }
      }
   }

   public static PSResource defineEncoding(PSGenerator gen, SingleByteEncoding encoding) throws IOException {
      PSResource res = new PSResource("encoding", encoding.getName());
      gen.writeDSCComment("BeginResource", (Object)res);
      gen.writeln("/" + encoding.getName() + " [");
      String[] charNames = encoding.getCharNameMap();

      for(int i = 0; i < 256; ++i) {
         if (i > 0) {
            if (i % 5 == 0) {
               gen.newLine();
            } else {
               gen.write(" ");
            }
         }

         String glyphname = null;
         if (i < charNames.length) {
            glyphname = charNames[i];
         }

         if (glyphname == null || "".equals(glyphname)) {
            glyphname = ".notdef";
         }

         gen.write("/");
         gen.write(glyphname);
      }

      gen.newLine();
      gen.writeln("] def");
      gen.writeDSCComment("EndResource");
      gen.getResourceTracker().registerSuppliedResource(res);
      return res;
   }

   public static PSResource defineDerivedFont(PSGenerator gen, String baseFontName, String fontName, String encoding) throws IOException {
      PSResource res = new PSResource("font", fontName);
      gen.writeDSCComment("BeginResource", (Object)res);
      gen.commentln("%XGCDependencies: font " + baseFontName);
      gen.commentln("%XGC+ encoding " + encoding);
      gen.writeln("/" + baseFontName + " findfont");
      gen.writeln("dup length dict begin");
      gen.writeln("  {1 index /FID ne {def} {pop pop} ifelse} forall");
      gen.writeln("  /Encoding " + encoding + " def");
      gen.writeln("  currentdict");
      gen.writeln("end");
      gen.writeln("/" + fontName + " exch definefont pop");
      gen.writeDSCComment("EndResource");
      gen.getResourceTracker().registerSuppliedResource(res);
      return res;
   }

   static {
      log = LogFactory.getLog(PSFontUtils.class);
   }
}
