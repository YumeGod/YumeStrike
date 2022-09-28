package org.apache.fop.render;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;

public class XMLHandlerConfigurator extends AbstractRendererConfigurator {
   protected static Log log;

   public XMLHandlerConfigurator(FOUserAgent userAgent) {
      super(userAgent);
   }

   private Configuration getHandlerConfig(Configuration cfg, String namespace) {
      if (cfg != null && namespace != null) {
         Configuration handlerConfig = null;
         Configuration[] children = cfg.getChildren("xml-handler");

         for(int i = 0; i < children.length; ++i) {
            try {
               if (children[i].getAttribute("namespace").equals(namespace)) {
                  handlerConfig = children[i];
                  break;
               }
            } catch (ConfigurationException var7) {
            }
         }

         if (log.isDebugEnabled()) {
            log.debug((handlerConfig == null ? "No" : "") + "XML handler configuration found for namespace " + namespace);
         }

         return handlerConfig;
      } else {
         return null;
      }
   }

   public void configure(RendererContext context, String ns) throws FOPException {
      Configuration cfg = this.getRendererConfig(context.getRenderer());
      if (cfg != null) {
         cfg = this.getHandlerConfig(cfg, ns);
         if (cfg != null) {
            context.setProperty("cfg", cfg);
         }
      }

   }

   static {
      log = LogFactory.getLog(XMLHandlerConfigurator.class);
   }
}
