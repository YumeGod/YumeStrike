package org.apache.fop.svg;

import java.util.ArrayList;
import java.util.List;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fonts.FontEventListener;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontInfoConfigurator;
import org.apache.fop.fonts.FontManager;
import org.apache.fop.fonts.FontResolver;
import org.apache.fop.fonts.FontSetup;
import org.apache.fop.pdf.PDFDocument;
import org.apache.fop.render.pdf.PDFRendererConfigurator;

public class PDFDocumentGraphics2DConfigurator {
   public void configure(PDFDocumentGraphics2D graphics, Configuration cfg) throws ConfigurationException {
      PDFDocument pdfDoc = graphics.getPDFDocument();
      pdfDoc.setFilterMap(PDFRendererConfigurator.buildFilterMapFromConfiguration(cfg));

      try {
         FontInfo fontInfo = createFontInfo(cfg);
         graphics.setFontInfo(fontInfo);
      } catch (FOPException var5) {
         throw new ConfigurationException("Error while setting up fonts", var5);
      }
   }

   public static FontInfo createFontInfo(Configuration cfg) throws FOPException {
      FontInfo fontInfo = new FontInfo();
      if (cfg != null) {
         FontResolver fontResolver = FontManager.createMinimalFontResolver();
         FontManager fontManager = new FontManager();
         FontEventListener listener = null;
         boolean strict = false;
         FontInfoConfigurator fontInfoConfigurator = new FontInfoConfigurator(cfg, fontManager, fontResolver, (FontEventListener)listener, false);
         List fontInfoList = new ArrayList();
         fontInfoConfigurator.configure(fontInfoList);
         if (fontManager.useCache()) {
            fontManager.getFontCache().save();
         }

         FontSetup.setup(fontInfo, fontInfoList, fontResolver);
      } else {
         FontSetup.setup(fontInfo);
      }

      return fontInfo;
   }
}
