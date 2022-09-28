package org.apache.fop.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

/** @deprecated */
public class DataURLUtil {
   /** @deprecated */
   public static String createDataURL(InputStream in, String mediatype) throws IOException {
      return org.apache.xmlgraphics.util.uri.DataURLUtil.createDataURL(in, mediatype);
   }

   /** @deprecated */
   public static void writeDataURL(InputStream in, String mediatype, Writer writer) throws IOException {
      org.apache.xmlgraphics.util.uri.DataURLUtil.writeDataURL(in, mediatype, writer);
   }
}
