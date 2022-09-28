package org.apache.fop.fonts;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import org.apache.fop.fonts.autodetect.FontInfoFinder;

public class FontAdder {
   private FontEventListener listener;
   private FontResolver resolver;
   private FontManager manager;

   public FontAdder(FontManager manager, FontResolver resolver, FontEventListener listener) {
      this.manager = manager;
      this.resolver = resolver;
      this.listener = listener;
   }

   public void add(List fontURLList, List fontInfoList) {
      FontCache cache = this.manager.getFontCache();
      FontInfoFinder finder = new FontInfoFinder();
      finder.setEventListener(this.listener);
      Iterator iter = fontURLList.iterator();

      while(true) {
         EmbedFontInfo[] embedFontInfos;
         do {
            if (!iter.hasNext()) {
               return;
            }

            URL fontUrl = (URL)iter.next();
            embedFontInfos = finder.find(fontUrl, this.resolver, cache);
         } while(embedFontInfos == null);

         int i = 0;

         for(int c = embedFontInfos.length; i < c; ++i) {
            EmbedFontInfo fontInfo = embedFontInfos[i];
            if (fontInfo != null) {
               fontInfoList.add(fontInfo);
            }
         }
      }
   }
}
