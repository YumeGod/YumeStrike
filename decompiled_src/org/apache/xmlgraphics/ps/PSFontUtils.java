package org.apache.xmlgraphics.ps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.commons.io.EndianUtils;
import org.apache.commons.io.IOUtils;
import org.apache.xmlgraphics.fonts.Glyphs;
import org.apache.xmlgraphics.util.io.ASCIIHexOutputStream;
import org.apache.xmlgraphics.util.io.SubInputStream;

public class PSFontUtils {
   public static final PSResource WINANSI_ENCODING_RESOURCE = new PSResource("encoding", "WinAnsiEncoding");
   public static final PSResource ADOBECYRILLIC_ENCODING_RESOURCE = new PSResource("encoding", "AdobeStandardCyrillicEncoding");

   public static void embedType1Font(PSGenerator gen, InputStream in) throws IOException {
      boolean finished = false;

      label35:
      while(!finished) {
         int segIndicator = in.read();
         if (segIndicator < 0) {
            throw new IOException("Unexpected end-of-file while reading segment indicator");
         }

         if (segIndicator != 128) {
            throw new IOException("Expected ASCII 128, found: " + segIndicator);
         }

         int segType = in.read();
         if (segType < 0) {
            throw new IOException("Unexpected end-of-file while reading segment type");
         }

         int dataSegLen = false;
         int dataSegLen;
         switch (segType) {
            case 1:
               dataSegLen = EndianUtils.readSwappedInteger(in);
               BufferedReader reader = new BufferedReader(new InputStreamReader(new SubInputStream(in, (long)dataSegLen), "US-ASCII"));

               while(true) {
                  String line;
                  if ((line = reader.readLine()) == null) {
                     continue label35;
                  }

                  gen.writeln(line);
               }
            case 2:
               dataSegLen = EndianUtils.readSwappedInteger(in);
               SubInputStream sin = new SubInputStream(in, (long)dataSegLen);
               ASCIIHexOutputStream hexOut = new ASCIIHexOutputStream(gen.getOutputStream());
               IOUtils.copy((InputStream)sin, (OutputStream)hexOut);
               gen.newLine();
               break;
            case 3:
               finished = true;
               break;
            default:
               throw new IOException("Unsupported segment type: " + segType);
         }
      }

   }

   public static void defineWinAnsiEncoding(PSGenerator gen) throws IOException {
      gen.writeDSCComment("BeginResource", (Object)WINANSI_ENCODING_RESOURCE);
      gen.writeln("/WinAnsiEncoding [");

      for(int i = 0; i < Glyphs.WINANSI_ENCODING.length; ++i) {
         if (i > 0) {
            if (i % 5 == 0) {
               gen.newLine();
            } else {
               gen.write(" ");
            }
         }

         char ch = Glyphs.WINANSI_ENCODING[i];
         String glyphname = Glyphs.charToGlyphName(ch);
         if ("".equals(glyphname)) {
            gen.write("/.notdef");
         } else {
            gen.write("/");
            gen.write(glyphname);
         }
      }

      gen.newLine();
      gen.writeln("] def");
      gen.writeDSCComment("EndResource");
      gen.getResourceTracker().registerSuppliedResource(WINANSI_ENCODING_RESOURCE);
   }

   public static void defineAdobeCyrillicEncoding(PSGenerator gen) throws IOException {
      gen.writeDSCComment("BeginResource", (Object)ADOBECYRILLIC_ENCODING_RESOURCE);
      gen.writeln("/AdobeStandardCyrillicEncoding [");

      for(int i = 0; i < Glyphs.ADOBECYRILLIC_ENCODING.length; ++i) {
         if (i > 0) {
            if (i % 5 == 0) {
               gen.newLine();
            } else {
               gen.write(" ");
            }
         }

         char ch = Glyphs.ADOBECYRILLIC_ENCODING[i];
         String glyphname = Glyphs.charToGlyphName(ch);
         if ("".equals(glyphname)) {
            gen.write("/.notdef");
         } else {
            gen.write("/");
            gen.write(glyphname);
         }
      }

      gen.newLine();
      gen.writeln("] def");
      gen.writeDSCComment("EndResource");
      gen.getResourceTracker().registerSuppliedResource(ADOBECYRILLIC_ENCODING_RESOURCE);
   }

   public static void redefineFontEncoding(PSGenerator gen, String fontName, String encoding) throws IOException {
      gen.writeln("/" + fontName + " findfont");
      gen.writeln("dup length dict begin");
      gen.writeln("  {1 index /FID ne {def} {pop pop} ifelse} forall");
      gen.writeln("  /Encoding " + encoding + " def");
      gen.writeln("  currentdict");
      gen.writeln("end");
      gen.writeln("/" + fontName + " exch definefont pop");
   }
}
