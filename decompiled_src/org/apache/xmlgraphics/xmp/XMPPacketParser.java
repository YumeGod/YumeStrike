package org.apache.xmlgraphics.xmp;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.output.ByteArrayOutputStream;

public class XMPPacketParser {
   private static final byte[] PACKET_HEADER;
   private static final byte[] PACKET_HEADER_END;
   private static final byte[] PACKET_TRAILER;

   public static Metadata parse(InputStream in) throws IOException, TransformerException {
      if (!((InputStream)in).markSupported()) {
         in = new BufferedInputStream((InputStream)in);
      }

      boolean foundXMP = skipAfter((InputStream)in, PACKET_HEADER);
      if (!foundXMP) {
         return null;
      } else if (!skipAfter((InputStream)in, PACKET_HEADER_END)) {
         throw new IOException("Invalid XMP packet header!");
      } else {
         ByteArrayOutputStream baout = new ByteArrayOutputStream();
         if (!skipAfter((InputStream)in, PACKET_TRAILER, baout)) {
            throw new IOException("XMP packet not properly terminated!");
         } else {
            Metadata metadata = XMPParser.parseXMP((Source)(new StreamSource(new ByteArrayInputStream(baout.toByteArray()))));
            return metadata;
         }
      }
   }

   private static boolean skipAfter(InputStream in, byte[] match) throws IOException {
      return skipAfter(in, match, (OutputStream)null);
   }

   private static boolean skipAfter(InputStream in, byte[] match, OutputStream out) throws IOException {
      int found = 0;
      int len = match.length;

      int b;
      while((b = in.read()) >= 0) {
         if (b == match[found]) {
            ++found;
            if (found == len) {
               return true;
            }
         } else {
            if (out != null) {
               if (found > 0) {
                  out.write(match, 0, found);
               }

               out.write(b);
            }

            found = 0;
         }
      }

      return false;
   }

   static {
      try {
         PACKET_HEADER = "<?xpacket begin=".getBytes("US-ASCII");
         PACKET_HEADER_END = "?>".getBytes("US-ASCII");
         PACKET_TRAILER = "<?xpacket".getBytes("US-ASCII");
      } catch (UnsupportedEncodingException var1) {
         throw new RuntimeException("Incompatible JVM! US-ASCII encoding not supported.");
      }
   }
}
