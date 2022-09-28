package org.apache.fop.render.afp;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.fop.afp.AFPResourceLevel;
import org.apache.fop.afp.AFPResourceLevelDefaults;
import org.apache.fop.afp.fonts.AFPFont;
import org.apache.fop.afp.fonts.AFPFontCollection;
import org.apache.fop.afp.fonts.AFPFontInfo;
import org.apache.fop.afp.fonts.CharacterSet;
import org.apache.fop.afp.fonts.CharacterSetBuilder;
import org.apache.fop.afp.fonts.DoubleByteFont;
import org.apache.fop.afp.fonts.OutlineFont;
import org.apache.fop.afp.fonts.RasterFont;
import org.apache.fop.afp.util.DefaultFOPResourceAccessor;
import org.apache.fop.afp.util.ResourceAccessor;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.fonts.FontCollection;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.fonts.FontManagerConfigurator;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.fonts.FontUtil;
import org.apache.fop.fonts.Typeface;
import org.apache.fop.render.PrintRendererConfigurator;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;
import org.apache.fop.util.LogUtil;

public class AFPRendererConfigurator extends PrintRendererConfigurator implements IFDocumentHandlerConfigurator {
   private static final String IMAGES_MODE_GRAYSCALE = "b+w";
   private static final String IMAGES_MODE_COLOR = "color";

   public AFPRendererConfigurator(FOUserAgent userAgent) {
      super(userAgent);
   }

   private AFPFontInfo buildFont(Configuration fontCfg, String fontPath) throws ConfigurationException {
      FontManager fontManager = this.userAgent.getFactory().getFontManager();
      Configuration[] triple = fontCfg.getChildren("font-triplet");
      List tripletList = new ArrayList();
      if (triple.length == 0) {
         log.error("Mandatory font configuration element '<font-triplet...' is missing");
         return null;
      } else {
         for(int j = 0; j < triple.length; ++j) {
            int weight = FontUtil.parseCSS2FontWeight(triple[j].getAttribute("weight"));
            FontTriplet triplet = new FontTriplet(triple[j].getAttribute("name"), triple[j].getAttribute("style"), weight);
            tripletList.add(triplet);
         }

         Configuration afpFontCfg = fontCfg.getChild("afp-font");
         if (afpFontCfg == null) {
            log.error("Mandatory font configuration element '<afp-font...' is missing");
            return null;
         } else {
            URI baseURI = null;
            String uri = afpFontCfg.getAttribute("base-uri", fontPath);
            if (uri == null) {
               String path = afpFontCfg.getAttribute("path", fontPath);
               if (path != null) {
                  File f = new File(path);
                  baseURI = f.toURI();
               }
            } else {
               try {
                  baseURI = new URI(uri);
               } catch (URISyntaxException var14) {
                  log.error("Invalid URI: " + var14.getMessage());
                  return null;
               }
            }

            ResourceAccessor accessor = new DefaultFOPResourceAccessor(this.userAgent, fontManager.getFontBaseURL(), baseURI);
            String type = afpFontCfg.getAttribute("type");
            if (type == null) {
               log.error("Mandatory afp-font configuration attribute 'type=' is missing");
               return null;
            } else {
               String codepage = afpFontCfg.getAttribute("codepage");
               if (codepage == null) {
                  log.error("Mandatory afp-font configuration attribute 'code=' is missing");
                  return null;
               } else {
                  String encoding = afpFontCfg.getAttribute("encoding");
                  if (encoding == null) {
                     log.error("Mandatory afp-font configuration attribute 'encoding=' is missing");
                     return null;
                  } else {
                     AFPFont font = this.fontFromType(type, codepage, encoding, accessor, afpFontCfg);
                     return font != null ? new AFPFontInfo(font, tripletList) : null;
                  }
               }
            }
         }
      }
   }

   private AFPFont fontFromType(String type, String codepage, String encoding, ResourceAccessor accessor, Configuration afpFontCfg) throws ConfigurationException {
      String characterset;
      String characterset;
      if ("raster".equalsIgnoreCase(type)) {
         characterset = afpFontCfg.getAttribute("name", "Unknown");
         RasterFont font = new RasterFont(characterset);
         Configuration[] rasters = afpFontCfg.getChildren("afp-raster-font");
         if (rasters.length == 0) {
            log.error("Mandatory font configuration elements '<afp-raster-font...' are missing at " + afpFontCfg.getLocation());
            return null;
         } else {
            for(int j = 0; j < rasters.length; ++j) {
               Configuration rasterCfg = rasters[j];
               characterset = rasterCfg.getAttribute("characterset");
               if (characterset == null) {
                  log.error("Mandatory afp-raster-font configuration attribute 'characterset=' is missing");
                  return null;
               }

               float size = rasterCfg.getAttributeAsFloat("size");
               int sizeMpt = (int)(size * 1000.0F);
               String base14 = rasterCfg.getAttribute("base14-font", (String)null);
               if (base14 != null) {
                  try {
                     Class clazz = Class.forName("org.apache.fop.fonts.base14." + base14);

                     try {
                        Typeface tf = (Typeface)clazz.newInstance();
                        font.addCharacterSet(sizeMpt, CharacterSetBuilder.getInstance().build(characterset, codepage, encoding, tf));
                     } catch (Exception var18) {
                        String msg = "The base 14 font class " + clazz.getName() + " could not be instantiated";
                        log.error(msg);
                     }
                  } catch (ClassNotFoundException var19) {
                     String msg = "The base 14 font class for " + characterset + " could not be found";
                     log.error(msg);
                  }
               } else {
                  try {
                     font.addCharacterSet(sizeMpt, CharacterSetBuilder.getInstance().build(characterset, codepage, encoding, accessor));
                  } catch (IOException var20) {
                     this.toConfigurationException(codepage, characterset, var20);
                  }
               }
            }

            return font;
         }
      } else {
         String name;
         CharacterSet characterSet;
         if ("outline".equalsIgnoreCase(type)) {
            characterset = afpFontCfg.getAttribute("characterset");
            if (characterset == null) {
               log.error("Mandatory afp-font configuration attribute 'characterset=' is missing");
               return null;
            } else {
               name = afpFontCfg.getAttribute("name", characterset);
               characterSet = null;
               String base14 = afpFontCfg.getAttribute("base14-font", (String)null);
               if (base14 != null) {
                  try {
                     Class clazz = Class.forName("org.apache.fop.fonts.base14." + base14);

                     try {
                        Typeface tf = (Typeface)clazz.newInstance();
                        characterSet = CharacterSetBuilder.getInstance().build(characterset, codepage, encoding, tf);
                     } catch (Exception var22) {
                        String msg = "The base 14 font class " + clazz.getName() + " could not be instantiated";
                        log.error(msg);
                     }
                  } catch (ClassNotFoundException var23) {
                     characterset = "The base 14 font class for " + characterset + " could not be found";
                     log.error(characterset);
                  }
               } else {
                  try {
                     characterSet = CharacterSetBuilder.getInstance().build(characterset, codepage, encoding, accessor);
                  } catch (IOException var21) {
                     this.toConfigurationException(codepage, characterset, var21);
                  }
               }

               return new OutlineFont(name, characterSet);
            }
         } else if ("CIDKeyed".equalsIgnoreCase(type)) {
            characterset = afpFontCfg.getAttribute("characterset");
            if (characterset == null) {
               log.error("Mandatory afp-font configuration attribute 'characterset=' is missing");
               return null;
            } else {
               name = afpFontCfg.getAttribute("name", characterset);
               characterSet = null;

               try {
                  characterSet = CharacterSetBuilder.getDoubleByteInstance().build(characterset, codepage, encoding, accessor);
               } catch (IOException var24) {
                  this.toConfigurationException(codepage, characterset, var24);
               }

               DoubleByteFont font = new DoubleByteFont(name, characterSet);
               return font;
            }
         } else {
            log.error("No or incorrect type attribute: " + type);
            return null;
         }
      }
   }

   private void toConfigurationException(String codepage, String characterset, IOException ioe) throws ConfigurationException {
      String msg = "Failed to load the character set metrics " + characterset + " with code page " + codepage + ". I/O error: " + ioe.getMessage();
      throw new ConfigurationException(msg, ioe);
   }

   private List buildFontListFromConfiguration(Configuration cfg) throws FOPException, ConfigurationException {
      Configuration fonts = cfg.getChild("fonts");
      FontManager fontManager = this.userAgent.getFactory().getFontManager();
      FontTriplet.Matcher referencedFontsMatcher = fontManager.getReferencedFontsMatcher();
      FontTriplet.Matcher localMatcher = null;
      Configuration referencedFontsCfg = fonts.getChild("referenced-fonts", false);
      if (referencedFontsCfg != null) {
         localMatcher = FontManagerConfigurator.createFontsMatcher(referencedFontsCfg, this.userAgent.getFactory().validateUserConfigStrictly());
      }

      List fontList = new ArrayList();
      Configuration[] font = fonts.getChildren("font");
      String fontPath = null;

      for(int i = 0; i < font.length; ++i) {
         AFPFontInfo afi = this.buildFont(font[i], (String)fontPath);
         if (afi != null) {
            if (log.isDebugEnabled()) {
               log.debug("Adding font " + afi.getAFPFont().getFontName());
            }

            List fontTriplets = afi.getFontTriplets();

            for(int j = 0; j < fontTriplets.size(); ++j) {
               FontTriplet triplet = (FontTriplet)fontTriplets.get(j);
               if (log.isDebugEnabled()) {
                  log.debug("  Font triplet " + triplet.getName() + ", " + triplet.getStyle() + ", " + triplet.getWeight());
               }

               if (referencedFontsMatcher != null && referencedFontsMatcher.matches(triplet) || localMatcher != null && localMatcher.matches(triplet)) {
                  afi.getAFPFont().setEmbeddable(false);
                  break;
               }
            }

            fontList.add(afi);
         }
      }

      return fontList;
   }

   public void configure(Renderer renderer) throws FOPException {
      Configuration cfg = super.getRendererConfig(renderer);
      if (cfg != null) {
         AFPRenderer afpRenderer = (AFPRenderer)renderer;

         try {
            List fontList = this.buildFontListFromConfiguration(cfg);
            afpRenderer.setFontList(fontList);
         } catch (ConfigurationException var5) {
            LogUtil.handleException(log, var5, this.userAgent.getFactory().validateUserConfigStrictly());
         }

         this.configure(afpRenderer, cfg);
      }

   }

   private void configure(AFPCustomizable customizable, Configuration cfg) throws FOPException {
      Configuration imagesCfg = cfg.getChild("images");
      String imagesMode = imagesCfg.getAttribute("mode", "b+w");
      if ("color".equals(imagesMode)) {
         customizable.setColorImages(true);
         boolean cmyk = imagesCfg.getAttributeAsBoolean("cmyk", false);
         customizable.setCMYKImagesSupported(cmyk);
      } else {
         customizable.setColorImages(false);
         int bitsPerPixel = imagesCfg.getAttributeAsInteger("bits-per-pixel", 8);
         customizable.setBitsPerPixel(bitsPerPixel);
      }

      String dithering = imagesCfg.getAttribute("dithering-quality", "medium");
      float dq = 0.5F;
      if (dithering.startsWith("min")) {
         dq = 0.0F;
      } else if (dithering.startsWith("max")) {
         dq = 1.0F;
      } else {
         try {
            dq = Float.parseFloat(dithering);
         } catch (NumberFormatException var23) {
         }
      }

      customizable.setDitheringQuality(dq);
      boolean nativeImageSupport = imagesCfg.getAttributeAsBoolean("native", false);
      customizable.setNativeImagesSupported(nativeImageSupport);
      Configuration shadingCfg = cfg.getChild("shading");
      AFPShadingMode shadingMode = AFPShadingMode.valueOf(shadingCfg.getValue(AFPShadingMode.COLOR.getName()));
      customizable.setShadingMode(shadingMode);
      Configuration rendererResolutionCfg = cfg.getChild("renderer-resolution", false);
      if (rendererResolutionCfg != null) {
         customizable.setResolution(rendererResolutionCfg.getValueAsInteger(240));
      }

      Configuration resourceGroupFileCfg = cfg.getChild("resource-group-file", false);
      if (resourceGroupFileCfg != null) {
         String resourceGroupDest = null;

         try {
            resourceGroupDest = resourceGroupFileCfg.getValue();
            if (resourceGroupDest != null) {
               File resourceGroupFile = new File(resourceGroupDest);
               resourceGroupFile.createNewFile();
               if (resourceGroupFile.canWrite()) {
                  customizable.setDefaultResourceGroupFilePath(resourceGroupDest);
               } else {
                  log.warn("Unable to write to default external resource group file '" + resourceGroupDest + "'");
               }
            }
         } catch (ConfigurationException var21) {
            LogUtil.handleException(log, var21, this.userAgent.getFactory().validateUserConfigStrictly());
         } catch (IOException var22) {
            throw new FOPException("Could not create default external resource group file", var22);
         }
      }

      Configuration defaultResourceLevelCfg = cfg.getChild("default-resource-levels", false);
      if (defaultResourceLevelCfg != null) {
         AFPResourceLevelDefaults defaults = new AFPResourceLevelDefaults();
         String[] types = defaultResourceLevelCfg.getAttributeNames();
         int i = 0;

         for(int c = types.length; i < c; ++i) {
            String type = types[i];

            try {
               String level = defaultResourceLevelCfg.getAttribute(type);
               defaults.setDefaultResourceLevel(type, AFPResourceLevel.valueOf(level));
            } catch (IllegalArgumentException var19) {
               LogUtil.handleException(log, var19, this.userAgent.getFactory().validateUserConfigStrictly());
            } catch (ConfigurationException var20) {
               LogUtil.handleException(log, var20, this.userAgent.getFactory().validateUserConfigStrictly());
            }
         }

         customizable.setResourceLevelDefaults(defaults);
      }

   }

   public void configure(IFDocumentHandler documentHandler) throws FOPException {
      Configuration cfg = super.getRendererConfig(documentHandler.getMimeType());
      if (cfg != null) {
         AFPDocumentHandler afpDocumentHandler = (AFPDocumentHandler)documentHandler;
         this.configure(afpDocumentHandler, cfg);
      }

   }

   public void setupFontInfo(IFDocumentHandler documentHandler, FontInfo fontInfo) throws FOPException {
      FontManager fontManager = this.userAgent.getFactory().getFontManager();
      List fontCollections = new ArrayList();
      Configuration cfg = super.getRendererConfig(documentHandler.getMimeType());
      if (cfg != null) {
         try {
            List fontList = this.buildFontListFromConfiguration(cfg);
            fontCollections.add(new AFPFontCollection(this.userAgent.getEventBroadcaster(), fontList));
         } catch (ConfigurationException var7) {
            LogUtil.handleException(log, var7, this.userAgent.getFactory().validateUserConfigStrictly());
         }
      } else {
         fontCollections.add(new AFPFontCollection(this.userAgent.getEventBroadcaster(), (List)null));
      }

      fontManager.setup(fontInfo, (FontCollection[])fontCollections.toArray(new FontCollection[fontCollections.size()]));
      documentHandler.setFontInfo(fontInfo);
   }
}
