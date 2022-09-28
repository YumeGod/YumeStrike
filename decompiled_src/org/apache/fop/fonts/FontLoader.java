package org.apache.fop.fonts;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.truetype.TTFFontLoader;
import org.apache.fop.fonts.type1.Type1FontLoader;

public abstract class FontLoader {
   protected static Log log;
   protected String fontFileURI = null;
   protected FontResolver resolver = null;
   protected CustomFont returnFont = null;
   protected boolean loaded = false;
   protected boolean embedded = true;
   protected boolean useKerning = true;

   public FontLoader(String fontFileURI, boolean embedded, boolean useKerning, FontResolver resolver) {
      this.fontFileURI = fontFileURI;
      this.embedded = embedded;
      this.useKerning = useKerning;
      this.resolver = resolver;
   }

   private static boolean isType1(String fontURI) {
      return fontURI.toLowerCase().endsWith(".pfb");
   }

   public static CustomFont loadFont(File fontFile, String subFontName, boolean embedded, EncodingMode encodingMode, FontResolver resolver) throws IOException {
      return loadFont(fontFile.toURI().toURL(), subFontName, embedded, encodingMode, resolver);
   }

   public static CustomFont loadFont(URL fontUrl, String subFontName, boolean embedded, EncodingMode encodingMode, FontResolver resolver) throws IOException {
      return loadFont(fontUrl.toExternalForm(), subFontName, embedded, encodingMode, true, resolver);
   }

   public static CustomFont loadFont(String fontFileURI, String subFontName, boolean embedded, EncodingMode encodingMode, boolean useKerning, FontResolver resolver) throws IOException {
      fontFileURI = fontFileURI.trim();
      boolean type1 = isType1(fontFileURI);
      Object loader;
      if (type1) {
         if (encodingMode == EncodingMode.CID) {
            throw new IllegalArgumentException("CID encoding mode not supported for Type 1 fonts");
         }

         loader = new Type1FontLoader(fontFileURI, embedded, useKerning, resolver);
      } else {
         loader = new TTFFontLoader(fontFileURI, subFontName, embedded, encodingMode, useKerning, resolver);
      }

      return ((FontLoader)loader).getFont();
   }

   public static InputStream openFontUri(FontResolver resolver, String uri) throws IOException, MalformedURLException {
      InputStream in = null;
      if (resolver != null) {
         Source source = resolver.resolve(uri);
         String err;
         if (source == null) {
            err = "Cannot load font: failed to create Source for font file " + uri;
            throw new IOException(err);
         }

         if (source instanceof StreamSource) {
            in = ((StreamSource)source).getInputStream();
         }

         if (in == null && source.getSystemId() != null) {
            in = (new URL(source.getSystemId())).openStream();
         }

         if (in == null) {
            err = "Cannot load font: failed to create InputStream from Source for font file " + uri;
            throw new IOException(err);
         }
      } else {
         in = (new URL(uri)).openStream();
      }

      return in;
   }

   protected abstract void read() throws IOException;

   public CustomFont getFont() throws IOException {
      if (!this.loaded) {
         this.read();
      }

      return this.returnFont;
   }

   static {
      log = LogFactory.getLog(FontLoader.class);
   }
}
