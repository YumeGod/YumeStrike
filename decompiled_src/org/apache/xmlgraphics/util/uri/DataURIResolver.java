package org.apache.xmlgraphics.util.uri;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlgraphics.util.io.Base64DecodeStream;

public class DataURIResolver implements URIResolver {
   private static Log LOG;

   public Source resolve(String href, String base) throws TransformerException {
      return href.startsWith("data:") ? this.parseDataURI(href) : null;
   }

   private Source parseDataURI(String href) {
      int commaPos = href.indexOf(44);
      String header = href.substring(0, commaPos);
      String data = href.substring(commaPos + 1);
      if (header.endsWith(";base64")) {
         byte[] bytes = data.getBytes();
         ByteArrayInputStream encodedStream = new ByteArrayInputStream(bytes);
         Base64DecodeStream decodedStream = new Base64DecodeStream(encodedStream);
         return new StreamSource(decodedStream);
      } else {
         String encoding = "UTF-8";
         int charsetpos = header.indexOf(";charset=");
         if (charsetpos > 0) {
            encoding = header.substring(charsetpos + 9);
         }

         try {
            String unescapedString = URLDecoder.decode(data, encoding);
            return new StreamSource(new StringReader(unescapedString));
         } catch (IllegalArgumentException var8) {
            LOG.warn(var8.getMessage());
         } catch (UnsupportedEncodingException var9) {
            LOG.warn(var9.getMessage());
         }

         return null;
      }
   }

   static {
      LOG = LogFactory.getLog(URIResolver.class);
   }
}
