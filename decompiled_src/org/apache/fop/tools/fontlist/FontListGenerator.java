package org.apache.fop.tools.fontlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.fonts.FontEventListener;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontMetrics;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.render.intermediate.IFDocumentHandler;
import org.apache.fop.render.intermediate.IFDocumentHandlerConfigurator;

public class FontListGenerator {
   public SortedMap listFonts(FopFactory fopFactory, String mime, FontEventListener listener) throws FOPException {
      FontInfo fontInfo = this.setupFonts(fopFactory, mime, listener);
      SortedMap fontFamilies = this.buildFamilyMap(fontInfo);
      return fontFamilies;
   }

   private FontInfo setupFonts(FopFactory fopFactory, String mime, FontEventListener listener) throws FOPException {
      FOUserAgent userAgent = fopFactory.newFOUserAgent();
      IFDocumentHandler documentHandler = fopFactory.getRendererFactory().createDocumentHandler(userAgent, mime);
      IFDocumentHandlerConfigurator configurator = documentHandler.getConfigurator();
      FontInfo fontInfo = new FontInfo();
      configurator.setupFontInfo(documentHandler, fontInfo);
      return fontInfo;
   }

   private SortedMap buildFamilyMap(FontInfo fontInfo) {
      Map fonts = fontInfo.getFonts();
      Set keyBag = new HashSet(fonts.keySet());
      Map keys = new HashMap();
      SortedMap fontFamilies = new TreeMap();

      FontTriplet triplet;
      FontSpec container;
      for(Iterator iter = fontInfo.getFontTriplets().entrySet().iterator(); iter.hasNext(); container.addTriplet(triplet)) {
         Map.Entry entry = (Map.Entry)iter.next();
         triplet = (FontTriplet)entry.getKey();
         String key = (String)entry.getValue();
         if (keyBag.contains(key)) {
            keyBag.remove(key);
            FontMetrics metrics = (FontMetrics)fonts.get(key);
            container = new FontSpec(key, metrics);
            container.addFamilyNames(metrics.getFamilyNames());
            keys.put(key, container);
            String firstFamilyName = (String)container.getFamilyNames().first();
            List containers = (List)fontFamilies.get(firstFamilyName);
            if (containers == null) {
               containers = new ArrayList();
               fontFamilies.put(firstFamilyName, containers);
            }

            ((List)containers).add(container);
            Collections.sort((List)containers);
         } else {
            container = (FontSpec)keys.get(key);
         }
      }

      return fontFamilies;
   }
}
