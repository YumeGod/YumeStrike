package org.apache.fop.fonts.substitute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.FontInfo;
import org.apache.fop.fonts.FontTriplet;

public class FontSubstitutions extends ArrayList {
   private static final long serialVersionUID = -9173104935431899722L;
   protected static Log log;

   public void adjustFontInfo(FontInfo fontInfo) {
      Iterator subsIt = super.iterator();

      while(true) {
         while(subsIt.hasNext()) {
            FontSubstitution substitution = (FontSubstitution)subsIt.next();
            FontQualifier toQualifier = substitution.getToQualifier();
            FontTriplet fontTriplet = toQualifier.bestMatch(fontInfo);
            if (fontTriplet == null) {
               log.error("Unable to match font substitution for destination qualifier " + toQualifier);
            } else {
               String internalFontKey = fontInfo.getInternalFontKey(fontTriplet);
               FontQualifier fromQualifier = substitution.getFromQualifier();
               List tripletList = fromQualifier.getTriplets();
               Iterator tripletit = tripletList.iterator();

               while(tripletit.hasNext()) {
                  FontTriplet triplet = (FontTriplet)tripletit.next();
                  fontInfo.addFontProperties(internalFontKey, triplet);
               }
            }
         }

         return;
      }
   }

   static {
      log = LogFactory.getLog(FontSubstitutions.class);
   }
}
