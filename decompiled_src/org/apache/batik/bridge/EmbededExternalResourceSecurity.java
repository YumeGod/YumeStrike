package org.apache.batik.bridge;

import org.apache.batik.util.ParsedURL;

public class EmbededExternalResourceSecurity implements ExternalResourceSecurity {
   public static final String DATA_PROTOCOL = "data";
   public static final String ERROR_EXTERNAL_RESOURCE_NOT_EMBEDED = "EmbededExternalResourceSecurity.error.external.esource.not.embeded";
   protected SecurityException se;

   public void checkLoadExternalResource() {
      if (this.se != null) {
         throw this.se;
      }
   }

   public EmbededExternalResourceSecurity(ParsedURL var1) {
      if (var1 == null || !"data".equals(var1.getProtocol())) {
         this.se = new SecurityException(Messages.formatMessage("EmbededExternalResourceSecurity.error.external.esource.not.embeded", new Object[]{var1}));
      }

   }
}
