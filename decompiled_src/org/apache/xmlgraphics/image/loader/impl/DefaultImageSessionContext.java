package org.apache.xmlgraphics.image.loader.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.xmlgraphics.image.loader.ImageContext;

public class DefaultImageSessionContext extends AbstractImageSessionContext {
   private ImageContext context;
   private File baseDir;

   public DefaultImageSessionContext(ImageContext context, File baseDir) {
      this.context = context;
      this.baseDir = baseDir;
   }

   public ImageContext getParentContext() {
      return this.context;
   }

   public File getBaseDir() {
      return this.baseDir;
   }

   protected Source resolveURI(String uri) {
      try {
         URL url = new URL(uri);
         return new StreamSource(url.openStream(), url.toExternalForm());
      } catch (MalformedURLException var4) {
         File f = new File(this.baseDir, uri);
         return f.isFile() ? new StreamSource(f) : null;
      } catch (IOException var5) {
         return null;
      }
   }

   public float getTargetResolution() {
      return this.getParentContext().getSourceResolution();
   }
}
