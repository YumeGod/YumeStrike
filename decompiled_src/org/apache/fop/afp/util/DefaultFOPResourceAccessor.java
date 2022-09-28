package org.apache.fop.afp.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.FOUserAgent;

public class DefaultFOPResourceAccessor extends SimpleResourceAccessor {
   private FOUserAgent userAgent;
   private String categoryBaseURI;

   public DefaultFOPResourceAccessor(FOUserAgent userAgent, String categoryBaseURI, URI baseURI) {
      super(baseURI);
      this.userAgent = userAgent;
      this.categoryBaseURI = categoryBaseURI;
   }

   public InputStream createInputStream(URI uri) throws IOException {
      URI resolved = this.resolveAgainstBase(uri);
      String base = this.categoryBaseURI != null ? this.categoryBaseURI : this.userAgent.getBaseURL();
      Source src = this.userAgent.resolveURI(resolved.toASCIIString(), base);
      if (src == null) {
         throw new FileNotFoundException("Resource not found: " + uri.toASCIIString());
      } else {
         if (src instanceof StreamSource) {
            StreamSource ss = (StreamSource)src;
            InputStream in = ss.getInputStream();
            if (in != null) {
               return in;
            }

            if (ss.getReader() != null) {
               IOUtils.closeQuietly(ss.getReader());
            }
         }

         URL url = new URL(src.getSystemId());
         return url.openStream();
      }
   }
}
