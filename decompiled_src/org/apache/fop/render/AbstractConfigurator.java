package org.apache.fop.render;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;

public abstract class AbstractConfigurator {
   protected static Log log;
   private static final String MIME = "mime";
   protected FOUserAgent userAgent = null;

   public AbstractConfigurator(FOUserAgent userAgent) {
      this.userAgent = userAgent;
   }

   protected Configuration getConfig(String mimeType) {
      Configuration cfg = this.userAgent.getFactory().getUserConfig();
      if (cfg == null) {
         if (log.isDebugEnabled()) {
            log.debug("userconfig is null");
         }

         return null;
      } else {
         Configuration userConfig = null;
         String type = this.getType();
         Configuration[] cfgs = cfg.getChild(type + "s").getChildren(type);

         for(int i = 0; i < cfgs.length; ++i) {
            Configuration child = cfgs[i];

            try {
               if (child.getAttribute("mime").equals(mimeType)) {
                  userConfig = child;
                  break;
               }
            } catch (ConfigurationException var9) {
            }
         }

         log.debug((userConfig == null ? "No u" : "U") + "ser configuration found for MIME type " + mimeType);
         return userConfig;
      }
   }

   public abstract String getType();

   static {
      log = LogFactory.getLog(AbstractConfigurator.class);
   }
}
