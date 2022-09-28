package org.apache.xmlgraphics.util.uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.commons.io.IOUtils;
import org.apache.xmlgraphics.util.WriterOutputStream;
import org.apache.xmlgraphics.util.io.Base64EncodeStream;

public class DataURLUtil {
   public static String createDataURL(InputStream in, String mediatype) throws IOException {
      StringWriter writer = new StringWriter();
      writeDataURL(in, mediatype, writer);
      return writer.toString();
   }

   public static void writeDataURL(InputStream in, String mediatype, Writer writer) throws IOException {
      writer.write("data:");
      if (mediatype != null) {
         writer.write(mediatype);
      }

      writer.write(";base64,");
      Base64EncodeStream out = new Base64EncodeStream(new WriterOutputStream(writer, "US-ASCII"), false);
      IOUtils.copy((InputStream)in, (OutputStream)out);
      out.close();
   }
}
