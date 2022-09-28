package org.apache.fop.render.pdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.pdf.PDFAMode;
import org.apache.fop.pdf.PDFEncryptionParams;
import org.apache.fop.pdf.PDFXMode;
import org.apache.fop.render.PrintRendererConfigurator;
import org.apache.fop.render.Renderer;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.util.LogUtil;

public class PDFRendererConfigurator extends PrintRendererConfigurator {
   public PDFRendererConfigurator(FOUserAgent userAgent) {
      super(userAgent);
   }

   public void configure(Renderer renderer) throws FOPException {
      Configuration cfg = super.getRendererConfig(renderer);
      if (cfg != null) {
         PDFRenderer pdfRenderer = (PDFRenderer)renderer;
         super.configure(renderer);
         PDFRenderingUtil pdfUtil = pdfRenderer.getPDFUtil();
         this.configure(cfg, pdfUtil);
      }

   }

   private void configure(Configuration cfg, PDFRenderingUtil pdfUtil) throws FOPException {
      try {
         Map filterMap = buildFilterMapFromConfiguration(cfg);
         if (filterMap != null) {
            pdfUtil.setFilterMap(filterMap);
         }
      } catch (ConfigurationException var12) {
         LogUtil.handleException(log, var12, false);
      }

      String s = cfg.getChild("pdf-a-mode", true).getValue((String)null);
      if (s != null) {
         pdfUtil.setAMode(PDFAMode.valueOf(s));
      }

      s = cfg.getChild("pdf-x-mode", true).getValue((String)null);
      if (s != null) {
         pdfUtil.setXMode(PDFXMode.valueOf(s));
      }

      Configuration encryptionParamsConfig = cfg.getChild("encryption-params", false);
      if (encryptionParamsConfig != null) {
         PDFEncryptionParams encryptionParams = new PDFEncryptionParams();
         Configuration ownerPasswordConfig = encryptionParamsConfig.getChild("owner-password", false);
         if (ownerPasswordConfig != null) {
            String ownerPassword = ownerPasswordConfig.getValue((String)null);
            if (ownerPassword != null) {
               encryptionParams.setOwnerPassword(ownerPassword);
            }
         }

         Configuration userPasswordConfig = encryptionParamsConfig.getChild("user-password", false);
         if (userPasswordConfig != null) {
            String userPassword = userPasswordConfig.getValue((String)null);
            if (userPassword != null) {
               encryptionParams.setUserPassword(userPassword);
            }
         }

         Configuration noPrintConfig = encryptionParamsConfig.getChild("noprint", false);
         if (noPrintConfig != null) {
            encryptionParams.setAllowPrint(false);
         }

         Configuration noCopyContentConfig = encryptionParamsConfig.getChild("nocopy", false);
         if (noCopyContentConfig != null) {
            encryptionParams.setAllowCopyContent(false);
         }

         Configuration noEditContentConfig = encryptionParamsConfig.getChild("noedit", false);
         if (noEditContentConfig != null) {
            encryptionParams.setAllowEditContent(false);
         }

         Configuration noAnnotationsConfig = encryptionParamsConfig.getChild("noannotations", false);
         if (noAnnotationsConfig != null) {
            encryptionParams.setAllowEditAnnotations(false);
         }

         pdfUtil.setEncryptionParams(encryptionParams);
      }

      s = cfg.getChild("output-profile", true).getValue((String)null);
      if (s != null) {
         pdfUtil.setOutputProfileURI(s);
      }

      Configuration disableColorSpaceConfig = cfg.getChild("disable-srgb-colorspace", false);
      if (disableColorSpaceConfig != null) {
         pdfUtil.setDisableSRGBColorSpace(disableColorSpaceConfig.getValueAsBoolean(false));
      }

   }

   public static Map buildFilterMapFromConfiguration(Configuration cfg) throws ConfigurationException {
      Map filterMap = new HashMap();
      Configuration[] filterLists = cfg.getChildren("filterList");

      for(int i = 0; i < filterLists.length; ++i) {
         Configuration filters = filterLists[i];
         String type = filters.getAttribute("type", (String)null);
         Configuration[] filt = filters.getChildren("value");
         List filterList = new ArrayList();

         for(int j = 0; j < filt.length; ++j) {
            String name = filt[j].getValue();
            filterList.add(name);
         }

         if (type == null) {
            type = "default";
         }

         if (!filterList.isEmpty() && log.isDebugEnabled()) {
            StringBuffer debug = new StringBuffer("Adding PDF filter");
            if (filterList.size() != 1) {
               debug.append("s");
            }

            debug.append(" for type ").append(type).append(": ");

            for(int j = 0; j < filterList.size(); ++j) {
               if (j != 0) {
                  debug.append(", ");
               }

               debug.append(filterList.get(j));
            }

            log.debug(debug.toString());
         }

         if (filterMap.get(type) != null) {
            throw new ConfigurationException("A filterList of type '" + type + "' has already been defined");
         }

         filterMap.put(type, filterList);
      }

      return filterMap;
   }

   public void configure(IFDocumentHandler documentHandler) throws FOPException {
      Configuration cfg = super.getRendererConfig(documentHandler.getMimeType());
      if (cfg != null) {
         PDFDocumentHandler pdfDocumentHandler = (PDFDocumentHandler)documentHandler;
         PDFRenderingUtil pdfUtil = pdfDocumentHandler.getPDFUtil();
         this.configure(cfg, pdfUtil);
      }

   }
}
