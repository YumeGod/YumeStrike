package org.apache.fop.apps;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.fonts.FontManagerConfigurator;
import org.apache.fop.util.LogUtil;
import org.apache.xmlgraphics.image.loader.spi.ImageImplRegistry;
import org.apache.xmlgraphics.image.loader.util.Penalty;
import org.xml.sax.SAXException;

public class FopFactoryConfigurator {
   public static final boolean DEFAULT_BREAK_INDENT_INHERITANCE = false;
   public static final boolean DEFAULT_STRICT_USERCONFIG_VALIDATION = true;
   public static final boolean DEFAULT_STRICT_FO_VALIDATION = true;
   public static final String DEFAULT_PAGE_WIDTH = "8.26in";
   public static final String DEFAULT_PAGE_HEIGHT = "11in";
   public static final float DEFAULT_SOURCE_RESOLUTION = 72.0F;
   public static final float DEFAULT_TARGET_RESOLUTION = 72.0F;
   private static final String PREFER_RENDERER = "prefer-renderer";
   private final Log log;
   private FopFactory factory;
   private Configuration cfg;

   public FopFactoryConfigurator(FopFactory factory) {
      this.log = LogFactory.getLog(FopFactoryConfigurator.class);
      this.factory = null;
      this.cfg = null;
      this.factory = factory;
   }

   public void configure(FopFactory factory) throws FOPException {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Initializing FopFactory Configuration");
      }

      if (this.cfg.getChild("accessibility", false) != null) {
         try {
            this.factory.setAccessibility(this.cfg.getChild("accessibility").getValueAsBoolean());
         } catch (ConfigurationException var12) {
            throw new FOPException(var12);
         }
      }

      if (this.cfg.getChild("strict-configuration", false) != null) {
         try {
            factory.setStrictUserConfigValidation(this.cfg.getChild("strict-configuration").getValueAsBoolean());
         } catch (ConfigurationException var11) {
            LogUtil.handleException(this.log, var11, false);
         }
      }

      boolean strict = factory.validateUserConfigStrictly();
      if (this.cfg.getChild("strict-validation", false) != null) {
         try {
            factory.setStrictValidation(this.cfg.getChild("strict-validation").getValueAsBoolean());
         } catch (ConfigurationException var10) {
            LogUtil.handleException(this.log, var10, strict);
         }
      }

      if (this.cfg.getChild("base", false) != null) {
         try {
            factory.setBaseURL(this.cfg.getChild("base").getValue((String)null));
         } catch (MalformedURLException var9) {
            LogUtil.handleException(this.log, var9, strict);
         }
      }

      if (this.cfg.getChild("hyphenation-base", false) != null) {
         try {
            factory.setHyphenBaseURL(this.cfg.getChild("hyphenation-base").getValue((String)null));
         } catch (MalformedURLException var8) {
            LogUtil.handleException(this.log, var8, strict);
         }
      }

      if (this.cfg.getChild("source-resolution", false) != null) {
         factory.setSourceResolution(this.cfg.getChild("source-resolution").getValueAsFloat(72.0F));
         if (this.log.isDebugEnabled()) {
            this.log.debug("source-resolution set to: " + factory.getSourceResolution() + "dpi (px2mm=" + factory.getSourcePixelUnitToMillimeter() + ")");
         }
      }

      if (this.cfg.getChild("target-resolution", false) != null) {
         factory.setTargetResolution(this.cfg.getChild("target-resolution").getValueAsFloat(72.0F));
         if (this.log.isDebugEnabled()) {
            this.log.debug("target-resolution set to: " + factory.getTargetResolution() + "dpi (px2mm=" + factory.getTargetPixelUnitToMillimeter() + ")");
         }
      }

      if (this.cfg.getChild("break-indent-inheritance", false) != null) {
         try {
            factory.setBreakIndentInheritanceOnReferenceAreaBoundary(this.cfg.getChild("break-indent-inheritance").getValueAsBoolean());
         } catch (ConfigurationException var7) {
            LogUtil.handleException(this.log, var7, strict);
         }
      }

      Configuration pageConfig = this.cfg.getChild("default-page-settings");
      if (pageConfig.getAttribute("height", (String)null) != null) {
         factory.setPageHeight(pageConfig.getAttribute("height", "11in"));
         if (this.log.isInfoEnabled()) {
            this.log.info("Default page-height set to: " + factory.getPageHeight());
         }
      }

      if (pageConfig.getAttribute("width", (String)null) != null) {
         factory.setPageWidth(pageConfig.getAttribute("width", "8.26in"));
         if (this.log.isInfoEnabled()) {
            this.log.info("Default page-width set to: " + factory.getPageWidth());
         }
      }

      if (this.cfg.getChild("prefer-renderer", false) != null) {
         try {
            factory.getRendererFactory().setRendererPreferred(this.cfg.getChild("prefer-renderer").getValueAsBoolean());
         } catch (ConfigurationException var6) {
            LogUtil.handleException(this.log, var6, strict);
         }
      }

      FontManager fontManager = factory.getFontManager();
      FontManagerConfigurator fontManagerConfigurator = new FontManagerConfigurator(this.cfg);
      fontManagerConfigurator.configure(fontManager, strict);
      this.configureImageLoading(this.cfg.getChild("image-loading", false), strict);
   }

   private void configureImageLoading(Configuration parent, boolean strict) throws FOPException {
      if (parent != null) {
         ImageImplRegistry registry = this.factory.getImageManager().getRegistry();
         Configuration[] penalties = parent.getChildren("penalty");

         try {
            int i = 0;

            for(int c = penalties.length; i < c; ++i) {
               Configuration penaltyCfg = penalties[i];
               String className = penaltyCfg.getAttribute("class");
               String value = penaltyCfg.getAttribute("value");
               Penalty p = null;
               if (value.toUpperCase().startsWith("INF")) {
                  p = Penalty.INFINITE_PENALTY;
               } else {
                  try {
                     p = Penalty.toPenalty(Integer.parseInt(value));
                  } catch (NumberFormatException var12) {
                     LogUtil.handleException(this.log, var12, strict);
                  }
               }

               if (p != null) {
                  registry.setAdditionalPenalty(className, p);
               }
            }
         } catch (ConfigurationException var13) {
            LogUtil.handleException(this.log, var13, strict);
         }

      }
   }

   public void setUserConfig(File userConfigFile) throws SAXException, IOException {
      try {
         DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
         this.setUserConfig(cfgBuilder.buildFromFile(userConfigFile));
      } catch (ConfigurationException var3) {
         throw new FOPException(var3);
      }
   }

   public void setUserConfig(String uri) throws SAXException, IOException {
      try {
         DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
         this.setUserConfig(cfgBuilder.build(uri));
      } catch (ConfigurationException var3) {
         throw new FOPException(var3);
      }
   }

   public void setUserConfig(Configuration cfg) throws FOPException {
      this.cfg = cfg;
      this.configure(this.factory);
   }

   public Configuration getUserConfig() {
      return this.cfg;
   }
}
