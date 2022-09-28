package org.apache.fop.fonts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.SAXParserFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.fonts.apps.TTFReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class FontReader extends DefaultHandler {
   private Locator locator = null;
   private boolean isCID = false;
   private CustomFont returnFont = null;
   private MultiByteFont multiFont = null;
   private SingleByteFont singleFont = null;
   private StringBuffer text = new StringBuffer();
   private List cidWidths = null;
   private int cidWidthIndex = 0;
   private Map currentKerning = null;
   private List bfranges = null;

   private void createFont(InputSource source) throws FOPException {
      XMLReader parser = null;

      try {
         SAXParserFactory factory = SAXParserFactory.newInstance();
         factory.setNamespaceAware(true);
         parser = factory.newSAXParser().getXMLReader();
      } catch (Exception var7) {
         throw new FOPException(var7);
      }

      if (parser == null) {
         throw new FOPException("Unable to create SAX parser");
      } else {
         try {
            parser.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
         } catch (SAXException var6) {
            throw new FOPException("You need a SAX parser which supports SAX version 2", var6);
         }

         parser.setContentHandler(this);

         try {
            parser.parse(source);
         } catch (SAXException var4) {
            throw new FOPException(var4);
         } catch (IOException var5) {
            throw new FOPException(var5);
         }
      }
   }

   public void setFontEmbedPath(String path) {
      this.returnFont.setEmbedFileName(path);
   }

   public void setKerningEnabled(boolean enabled) {
      this.returnFont.setKerningEnabled(enabled);
   }

   public void setResolver(FontResolver resolver) {
      this.returnFont.setResolver(resolver);
   }

   public Typeface getFont() {
      return this.returnFont;
   }

   public FontReader(InputSource source) throws FOPException {
      this.createFont(source);
   }

   public void startDocument() {
   }

   public void setDocumentLocator(Locator locator) {
      this.locator = locator;
   }

   public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
      if (localName.equals("font-metrics")) {
         if ("TYPE0".equals(attributes.getValue("type"))) {
            this.multiFont = new MultiByteFont();
            this.returnFont = this.multiFont;
            this.isCID = true;
            TTFReader.checkMetricsVersion(attributes);
         } else if ("TRUETYPE".equals(attributes.getValue("type"))) {
            this.singleFont = new SingleByteFont();
            this.singleFont.setFontType(FontType.TRUETYPE);
            this.returnFont = this.singleFont;
            this.isCID = false;
            TTFReader.checkMetricsVersion(attributes);
         } else {
            this.singleFont = new SingleByteFont();
            this.singleFont.setFontType(FontType.TYPE1);
            this.returnFont = this.singleFont;
            this.isCID = false;
         }
      } else if ("embed".equals(localName)) {
         this.returnFont.setEmbedFileName(attributes.getValue("file"));
         this.returnFont.setEmbedResourceName(attributes.getValue("class"));
      } else if ("cid-widths".equals(localName)) {
         this.cidWidthIndex = this.getInt(attributes.getValue("start-index"));
         this.cidWidths = new ArrayList();
      } else if ("kerning".equals(localName)) {
         this.currentKerning = new HashMap();
         this.returnFont.putKerningEntry(new Integer(attributes.getValue("kpx1")), this.currentKerning);
      } else if ("bfranges".equals(localName)) {
         this.bfranges = new ArrayList();
      } else if ("bf".equals(localName)) {
         BFEntry entry = new BFEntry(this.getInt(attributes.getValue("us")), this.getInt(attributes.getValue("ue")), this.getInt(attributes.getValue("gi")));
         this.bfranges.add(entry);
      } else if ("wx".equals(localName)) {
         this.cidWidths.add(new Integer(attributes.getValue("w")));
      } else if (!"widths".equals(localName)) {
         if ("char".equals(localName)) {
            try {
               this.singleFont.setWidth(Integer.parseInt(attributes.getValue("idx")), Integer.parseInt(attributes.getValue("wdt")));
            } catch (NumberFormatException var6) {
               throw new SAXException("Malformed width in metric file: " + var6.getMessage(), var6);
            }
         } else if ("pair".equals(localName)) {
            this.currentKerning.put(new Integer(attributes.getValue("kpx2")), new Integer(attributes.getValue("kern")));
         }
      }

   }

   private int getInt(String str) throws SAXException {
      int ret = false;

      try {
         int ret = Integer.parseInt(str);
         return ret;
      } catch (Exception var4) {
         throw new SAXException("Error while parsing integer value: " + str, var4);
      }
   }

   public void endElement(String uri, String localName, String qName) throws SAXException {
      String content = this.text.toString().trim();
      if ("font-name".equals(localName)) {
         this.returnFont.setFontName(content);
      } else if ("full-name".equals(localName)) {
         this.returnFont.setFullName(content);
      } else if ("family-name".equals(localName)) {
         Set s = new HashSet();
         s.add(content);
         this.returnFont.setFamilyNames(s);
      } else if ("ttc-name".equals(localName) && this.isCID) {
         this.multiFont.setTTCName(content);
      } else if ("encoding".equals(localName)) {
         if (this.singleFont != null && this.singleFont.getFontType() == FontType.TYPE1) {
            this.singleFont.setEncoding(content);
         }
      } else if ("cap-height".equals(localName)) {
         this.returnFont.setCapHeight(this.getInt(content));
      } else if ("x-height".equals(localName)) {
         this.returnFont.setXHeight(this.getInt(content));
      } else if ("ascender".equals(localName)) {
         this.returnFont.setAscender(this.getInt(content));
      } else if ("descender".equals(localName)) {
         this.returnFont.setDescender(this.getInt(content));
      } else {
         int[] wds;
         if ("left".equals(localName)) {
            wds = this.returnFont.getFontBBox();
            wds[0] = this.getInt(content);
            this.returnFont.setFontBBox(wds);
         } else if ("bottom".equals(localName)) {
            wds = this.returnFont.getFontBBox();
            wds[1] = this.getInt(content);
            this.returnFont.setFontBBox(wds);
         } else if ("right".equals(localName)) {
            wds = this.returnFont.getFontBBox();
            wds[2] = this.getInt(content);
            this.returnFont.setFontBBox(wds);
         } else if ("top".equals(localName)) {
            wds = this.returnFont.getFontBBox();
            wds[3] = this.getInt(content);
            this.returnFont.setFontBBox(wds);
         } else if ("first-char".equals(localName)) {
            this.returnFont.setFirstChar(this.getInt(content));
         } else if ("last-char".equals(localName)) {
            this.returnFont.setLastChar(this.getInt(content));
         } else if ("flags".equals(localName)) {
            this.returnFont.setFlags(this.getInt(content));
         } else if ("stemv".equals(localName)) {
            this.returnFont.setStemV(this.getInt(content));
         } else if ("italic-angle".equals(localName)) {
            this.returnFont.setItalicAngle(this.getInt(content));
         } else if ("missing-width".equals(localName)) {
            this.returnFont.setMissingWidth(this.getInt(content));
         } else if ("cid-type".equals(localName)) {
            this.multiFont.setCIDType(CIDFontType.byName(content));
         } else if ("default-width".equals(localName)) {
            this.multiFont.setDefaultWidth(this.getInt(content));
         } else if ("cid-widths".equals(localName)) {
            wds = new int[this.cidWidths.size()];
            int j = 0;

            for(int count = 0; count < this.cidWidths.size(); ++count) {
               Integer i = (Integer)this.cidWidths.get(count);
               wds[j++] = i;
            }

            this.multiFont.setWidthArray(wds);
         } else if ("bfranges".equals(localName)) {
            this.multiFont.setBFEntries((BFEntry[])this.bfranges.toArray(new BFEntry[0]));
         }
      }

      this.text.setLength(0);
   }

   public void characters(char[] ch, int start, int length) {
      this.text.append(ch, start, length);
   }
}
