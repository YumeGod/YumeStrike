package org.apache.fop.afp.util;

import java.io.IOException;
import java.net.URL;
import org.apache.fop.afp.fonts.FontRuntimeException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class DTDEntityResolver implements EntityResolver {
   public static final String AFP_DTD_1_0_ID = "-//APACHE/DTD AFP Installed Font Definition DTD 1.0//EN";
   public static final String AFP_DTD_1_0_RESOURCE = "afp-fonts-1.0.dtd";
   public static final String AFP_DTD_1_1_ID = "-//APACHE/DTD AFP Installed Font Definition DTD 1.1//EN";
   public static final String AFP_DTD_1_1_RESOURCE = "afp-fonts-1.1.dtd";
   public static final String AFP_DTD_1_2_ID = "-//APACHE/DTD AFP Installed Font Definition DTD 1.2//EN";
   public static final String AFP_DTD_1_2_RESOURCE = "afp-fonts-1.2.dtd";

   public InputSource resolveEntity(String publicId, String systemId) throws IOException {
      URL resource = null;
      if ("-//APACHE/DTD AFP Installed Font Definition DTD 1.2//EN".equals(publicId)) {
         resource = this.getResource("afp-fonts-1.2.dtd");
      } else {
         if (!"-//APACHE/DTD AFP Installed Font Definition DTD 1.1//EN".equals(publicId)) {
            if ("-//APACHE/DTD AFP Installed Font Definition DTD 1.0//EN".equals(publicId)) {
               throw new FontRuntimeException("The AFP Installed Font Definition 1.0 DTD is not longer supported");
            }

            if (systemId != null && systemId.indexOf("afp-fonts.dtd") >= 0) {
               throw new FontRuntimeException("The AFP Installed Font Definition DTD must be specified using the public id");
            }

            return null;
         }

         resource = this.getResource("afp-fonts-1.1.dtd");
      }

      InputSource inputSource = new InputSource(resource.openStream());
      inputSource.setPublicId(publicId);
      inputSource.setSystemId(systemId);
      return inputSource;
   }

   private URL getResource(String resourcePath) {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      if (cl == null) {
         cl = ClassLoader.getSystemClassLoader();
      }

      URL resource = cl.getResource(resourcePath);
      if (resource == null) {
         throw new FontRuntimeException("Resource " + resourcePath + "could not be found on the classpath");
      } else {
         return resource;
      }
   }
}
