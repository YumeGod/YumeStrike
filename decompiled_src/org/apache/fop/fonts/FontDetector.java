package org.apache.fop.fonts;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fonts.autodetect.FontFileFinder;
import org.apache.fop.util.LogUtil;
import org.apache.xmlgraphics.util.ClasspathResource;

public class FontDetector {
   private static Log log;
   private static final String[] FONT_MIMETYPES;
   private FontManager fontManager;
   private FontAdder fontAdder;
   private boolean strict;

   public FontDetector(FontManager manager, FontAdder adder, boolean strict) {
      this.fontManager = manager;
      this.fontAdder = adder;
      this.strict = strict;
   }

   public void detect(List fontInfoList) throws FOPException {
      FontFileFinder fontFileFinder = new FontFileFinder();
      String fontBaseURL = this.fontManager.getFontBaseURL();
      if (fontBaseURL != null) {
         try {
            File fontBase = FileUtils.toFile(new URL(fontBaseURL));
            if (fontBase != null) {
               List fontURLList = fontFileFinder.find(fontBase.getAbsolutePath());
               this.fontAdder.add(fontURLList, fontInfoList);
            }
         } catch (IOException var8) {
            LogUtil.handleException(log, var8, this.strict);
         }
      }

      try {
         List systemFontList = fontFileFinder.find();
         this.fontAdder.add(systemFontList, fontInfoList);
      } catch (IOException var7) {
         LogUtil.handleException(log, var7, this.strict);
      }

      ClasspathResource resource = ClasspathResource.getInstance();

      for(int i = 0; i < FONT_MIMETYPES.length; ++i) {
         this.fontAdder.add(resource.listResourcesOfMimeType(FONT_MIMETYPES[i]), fontInfoList);
      }

   }

   static {
      log = LogFactory.getLog(FontDetector.class);
      FONT_MIMETYPES = new String[]{"application/x-font", "application/x-font-truetype"};
   }
}
