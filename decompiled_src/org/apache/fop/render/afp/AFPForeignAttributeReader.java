package org.apache.fop.render.afp;

import java.io.File;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.AFPResourceInfo;
import org.apache.fop.afp.AFPResourceLevel;
import org.apache.xmlgraphics.util.QName;

public class AFPForeignAttributeReader {
   private static final Log log = LogFactory.getLog("org.apache.xmlgraphics.afp");
   public static final QName RESOURCE_NAME = new QName("http://xmlgraphics.apache.org/fop/extensions/afp", "afp:resource-name");
   public static final QName RESOURCE_LEVEL = new QName("http://xmlgraphics.apache.org/fop/extensions/afp", "afp:resource-level");
   public static final QName RESOURCE_GROUP_FILE = new QName("http://xmlgraphics.apache.org/fop/extensions/afp", "afp:resource-group-file");

   public AFPResourceInfo getResourceInfo(Map foreignAttributes) {
      AFPResourceInfo resourceInfo = new AFPResourceInfo();
      if (foreignAttributes != null && !foreignAttributes.isEmpty()) {
         String resourceName = (String)foreignAttributes.get(RESOURCE_NAME);
         if (resourceName != null) {
            resourceInfo.setName(resourceName);
         }

         AFPResourceLevel level = this.getResourceLevel(foreignAttributes);
         if (level != null) {
            resourceInfo.setLevel(level);
         }
      }

      return resourceInfo;
   }

   public AFPResourceLevel getResourceLevel(Map foreignAttributes) {
      AFPResourceLevel resourceLevel = null;
      if (foreignAttributes != null && !foreignAttributes.isEmpty() && foreignAttributes.containsKey(RESOURCE_LEVEL)) {
         String levelString = (String)foreignAttributes.get(RESOURCE_LEVEL);
         resourceLevel = AFPResourceLevel.valueOf(levelString);
         if (resourceLevel != null && resourceLevel.isExternal()) {
            String resourceGroupFile = (String)foreignAttributes.get(RESOURCE_GROUP_FILE);
            if (resourceGroupFile == null) {
               String msg = RESOURCE_GROUP_FILE + " not specified";
               log.error(msg);
               throw new UnsupportedOperationException(msg);
            }

            File resourceExternalGroupFile = new File(resourceGroupFile);
            SecurityManager security = System.getSecurityManager();

            String msg;
            try {
               if (security != null) {
                  security.checkWrite(resourceExternalGroupFile.getPath());
               }
            } catch (SecurityException var10) {
               msg = "unable to gain write access to external resource file: " + resourceGroupFile;
               log.error(msg);
            }

            try {
               boolean exists = resourceExternalGroupFile.exists();
               if (exists) {
                  log.warn("overwriting external resource file: " + resourceGroupFile);
               }

               resourceLevel.setExternalFilePath(resourceGroupFile);
            } catch (SecurityException var9) {
               msg = "unable to gain read access to external resource file: " + resourceGroupFile;
               log.error(msg);
            }
         }
      }

      return resourceLevel;
   }
}
