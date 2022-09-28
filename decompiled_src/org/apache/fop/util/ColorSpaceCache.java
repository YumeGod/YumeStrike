package org.apache.fop.util;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ColorSpaceCache {
   private static Log log;
   private URIResolver resolver;
   private Map colorSpaceMap = Collections.synchronizedMap(new HashMap());

   public ColorSpaceCache(URIResolver resolver) {
      this.resolver = resolver;
   }

   public ColorSpace get(String base, String iccProfileSrc) {
      ColorSpace colorSpace = null;
      if (!this.colorSpaceMap.containsKey(base + iccProfileSrc)) {
         try {
            ICC_Profile iccProfile = null;
            Source src = this.resolver.resolve(iccProfileSrc, base);
            if (src != null && src instanceof StreamSource) {
               iccProfile = ICC_Profile.getInstance(((StreamSource)src).getInputStream());
            }

            if (iccProfile != null) {
               colorSpace = new ICC_ColorSpace(iccProfile);
            }
         } catch (Exception var6) {
         }

         if (colorSpace != null) {
            this.colorSpaceMap.put(base + iccProfileSrc, colorSpace);
         } else {
            log.warn("Color profile '" + iccProfileSrc + "' not found.");
         }
      } else {
         colorSpace = (ColorSpace)this.colorSpaceMap.get(base + iccProfileSrc);
      }

      return (ColorSpace)colorSpace;
   }

   static {
      log = LogFactory.getLog(ColorSpaceCache.class);
   }
}
