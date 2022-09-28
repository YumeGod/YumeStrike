package org.apache.batik.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.Reader;
import org.apache.batik.util.io.StreamNormalizingReader;
import org.apache.batik.util.io.UTF16Decoder;

public class XMLStreamNormalizingReader extends StreamNormalizingReader {
   public XMLStreamNormalizingReader(InputStream var1, String var2) throws IOException {
      PushbackInputStream var3 = new PushbackInputStream(var1, 128);
      byte[] var4 = new byte[4];
      int var5 = var3.read(var4);
      if (var5 > 0) {
         var3.unread(var4, 0, var5);
      }

      if (var5 == 4) {
         Reader var6;
         String var7;
         label49:
         switch (var4[0] & 255) {
            case 0:
               if (var4[1] == 60 && var4[2] == 0 && var4[3] == 63) {
                  this.charDecoder = new UTF16Decoder(var3, true);
                  return;
               }
               break;
            case 60:
               switch (var4[1] & 255) {
                  case 0:
                     if (var4[2] == 63 && var4[3] == 0) {
                        this.charDecoder = new UTF16Decoder(var3, false);
                        return;
                     }
                     break label49;
                  case 63:
                     if (var4[2] == 120 && var4[3] == 109) {
                        var6 = XMLUtilities.createXMLDeclarationReader(var3, "UTF8");
                        var7 = XMLUtilities.getXMLDeclarationEncoding(var6, "UTF-8");
                        this.charDecoder = this.createCharDecoder(var3, var7);
                        return;
                     }
                  default:
                     break label49;
               }
            case 76:
               if (var4[1] == 111 && (var4[2] & 255) == 167 && (var4[3] & 255) == 148) {
                  var6 = XMLUtilities.createXMLDeclarationReader(var3, "CP037");
                  var7 = XMLUtilities.getXMLDeclarationEncoding(var6, "EBCDIC-CP-US");
                  this.charDecoder = this.createCharDecoder(var3, var7);
                  return;
               }
               break;
            case 254:
               if ((var4[1] & 255) == 255) {
                  this.charDecoder = this.createCharDecoder(var3, "UTF-16");
                  return;
               }
               break;
            case 255:
               if ((var4[1] & 255) == 254) {
                  this.charDecoder = this.createCharDecoder(var3, "UTF-16");
                  return;
               }
         }
      }

      var2 = var2 == null ? "UTF-8" : var2;
      this.charDecoder = this.createCharDecoder(var3, var2);
   }
}
