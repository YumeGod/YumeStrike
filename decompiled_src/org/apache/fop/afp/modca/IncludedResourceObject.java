package org.apache.fop.afp.modca;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import org.apache.commons.io.IOUtils;
import org.apache.fop.afp.util.ResourceAccessor;

public class IncludedResourceObject extends AbstractNamedAFPObject {
   private ResourceAccessor resourceAccessor;
   private URI uri;

   public IncludedResourceObject(String name, ResourceAccessor resourceAccessor, URI uri) {
      super(name);
      this.resourceAccessor = resourceAccessor;
      this.uri = uri;
   }

   public void writeToStream(OutputStream os) throws IOException {
      InputStream in = this.resourceAccessor.createInputStream(this.uri);

      try {
         IOUtils.copy(in, os);
      } finally {
         IOUtils.closeQuietly(in);
      }

   }
}
