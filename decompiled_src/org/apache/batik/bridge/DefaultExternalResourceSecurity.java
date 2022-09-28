package org.apache.batik.bridge;

import org.apache.batik.util.ParsedURL;

public class DefaultExternalResourceSecurity implements ExternalResourceSecurity {
   public static final String DATA_PROTOCOL = "data";
   public static final String ERROR_CANNOT_ACCESS_DOCUMENT_URL = "DefaultExternalResourceSecurity.error.cannot.access.document.url";
   public static final String ERROR_EXTERNAL_RESOURCE_FROM_DIFFERENT_URL = "DefaultExternalResourceSecurity.error.external.resource.from.different.url";
   protected SecurityException se;

   public void checkLoadExternalResource() {
      if (this.se != null) {
         this.se.fillInStackTrace();
         throw this.se;
      }
   }

   public DefaultExternalResourceSecurity(ParsedURL var1, ParsedURL var2) {
      if (var2 == null) {
         this.se = new SecurityException(Messages.formatMessage("DefaultExternalResourceSecurity.error.cannot.access.document.url", new Object[]{var1}));
      } else {
         String var3 = var2.getHost();
         String var4 = var1.getHost();
         if (var3 != var4 && (var3 == null || !var3.equals(var4)) && (var1 == null || !"data".equals(var1.getProtocol()))) {
            this.se = new SecurityException(Messages.formatMessage("DefaultExternalResourceSecurity.error.external.resource.from.different.url", new Object[]{var1}));
         }
      }

   }
}
