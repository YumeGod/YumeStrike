package org.apache.fop.tools.fontlist;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.regex.Pattern;
import org.apache.fop.fonts.FontTriplet;
import org.apache.fop.util.GenerationHelperContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class FontListSerializer {
   private static final String FONTS = "fonts";
   private static final String FAMILY = "family";
   private static final String FONT = "font";
   private static final String TRIPLETS = "triplets";
   private static final String TRIPLET = "triplet";
   private static final String NAME = "name";
   private static final String STRIPPED_NAME = "stripped-name";
   private static final String TYPE = "type";
   private static final String KEY = "key";
   private static final String STYLE = "style";
   private static final String WEIGHT = "weight";
   private static final String CDATA = "CDATA";
   private final Pattern quotePattern = Pattern.compile("'");

   public void generateSAX(SortedMap fontFamilies, GenerationHelperContentHandler handler) throws SAXException {
      this.generateSAX(fontFamilies, (String)null, handler);
   }

   public void generateSAX(SortedMap fontFamilies, String singleFamily, GenerationHelperContentHandler handler) throws SAXException {
      handler.startDocument();
      AttributesImpl atts = new AttributesImpl();
      handler.startElement((String)"fonts", atts);
      Iterator iter = fontFamilies.entrySet().iterator();

      while(true) {
         Map.Entry entry;
         String familyName;
         do {
            if (!iter.hasNext()) {
               handler.endElement("fonts");
               handler.endDocument();
               return;
            }

            entry = (Map.Entry)iter.next();
            familyName = (String)entry.getKey();
         } while(singleFamily != null && familyName != singleFamily);

         atts.clear();
         atts.addAttribute((String)null, "name", "name", "CDATA", familyName);
         atts.addAttribute((String)null, "stripped-name", "stripped-name", "CDATA", this.stripQuotes(familyName));
         handler.startElement((String)"family", atts);
         List containers = (List)entry.getValue();
         this.generateXMLForFontContainers(handler, containers);
         handler.endElement("family");
      }
   }

   private String stripQuotes(String name) {
      return this.quotePattern.matcher(name).replaceAll("");
   }

   private void generateXMLForFontContainers(GenerationHelperContentHandler handler, List containers) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      Iterator fontIter = containers.iterator();

      while(fontIter.hasNext()) {
         FontSpec cont = (FontSpec)fontIter.next();
         atts.clear();
         atts.addAttribute((String)null, "key", "key", "CDATA", cont.getKey());
         atts.addAttribute((String)null, "type", "type", "CDATA", cont.getFontMetrics().getFontType().getName());
         handler.startElement((String)"font", atts);
         this.generateXMLForTriplets(handler, cont.getTriplets());
         handler.endElement("font");
      }

   }

   private void generateXMLForTriplets(GenerationHelperContentHandler handler, Collection triplets) throws SAXException {
      AttributesImpl atts = new AttributesImpl();
      atts.clear();
      handler.startElement((String)"triplets", atts);
      Iterator iter = triplets.iterator();

      while(iter.hasNext()) {
         FontTriplet triplet = (FontTriplet)iter.next();
         atts.clear();
         atts.addAttribute((String)null, "name", "name", "CDATA", triplet.getName());
         atts.addAttribute((String)null, "style", "style", "CDATA", triplet.getStyle());
         atts.addAttribute((String)null, "weight", "weight", "CDATA", Integer.toString(triplet.getWeight()));
         handler.element((String)"triplet", atts);
      }

      handler.endElement("triplets");
   }
}
