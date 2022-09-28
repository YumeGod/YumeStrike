package org.apache.fop.afp.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class SimpleResourceAccessor implements ResourceAccessor {
   private URI baseURI;

   public SimpleResourceAccessor(URI baseURI) {
      this.baseURI = baseURI;
   }

   public SimpleResourceAccessor(File baseDir) {
      this(baseDir != null ? baseDir.toURI() : null);
   }

   public URI getBaseURI() {
      return this.baseURI;
   }

   protected URI resolveAgainstBase(URI uri) {
      return this.getBaseURI() != null ? this.getBaseURI().resolve(uri) : uri;
   }

   public InputStream createInputStream(URI uri) throws IOException {
      URI resolved = this.resolveAgainstBase(uri);
      URL url = resolved.toURL();
      return url.openStream();
   }
}
