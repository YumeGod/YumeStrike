package org.apache.fop.fonts.apps;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.Version;
import org.apache.fop.fonts.FontUtil;
import org.apache.fop.fonts.truetype.FontFileReader;
import org.apache.fop.fonts.truetype.TTFCmapEntry;
import org.apache.fop.fonts.truetype.TTFFile;
import org.apache.fop.util.CommandLineLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TTFReader extends AbstractFontReader {
   public static final String METRICS_VERSION_ATTR = "metrics-version";
   public static final int METRICS_VERSION = 2;

   private static void displayUsage() {
      System.out.println("java " + TTFReader.class.getName() + " [options] fontfile.ttf xmlfile.xml");
      System.out.println();
      System.out.println("where options can be:");
      System.out.println("-d  Debug mode");
      System.out.println("-q  Quiet mode");
      System.out.println("-enc ansi");
      System.out.println("    With this option you create a WinAnsi encoded font.");
      System.out.println("    The default is to create a CID keyed font.");
      System.out.println("    If you're not going to use characters outside the");
      System.out.println("    pdfencoding range (almost the same as iso-8889-1)");
      System.out.println("    you can add this option.");
      System.out.println("-ttcname <fontname>");
      System.out.println("    If you're reading data from a TrueType Collection");
      System.out.println("    (.ttc file) you must specify which font from the");
      System.out.println("    collection you will read metrics from. If you read");
      System.out.println("    from a .ttc file without this option, the fontnames");
      System.out.println("    will be listed for you.");
      System.out.println(" -fn <fontname>");
      System.out.println("    default is to use the fontname in the .ttf file, but");
      System.out.println("    you can override that name to make sure that the");
      System.out.println("    embedded font is used (if you're embedding fonts)");
      System.out.println("    instead of installed fonts when viewing documents ");
      System.out.println("    with Acrobat Reader.");
   }

   public static void main(String[] args) {
      String embFile = null;
      String embResource = null;
      String className = null;
      String fontName = null;
      String ttcName = null;
      boolean isCid = true;
      Map options = new HashMap();
      String[] arguments = parseArguments(options, args);
      LogFactory logFactory = LogFactory.getFactory();
      if (System.getProperty("org.apache.commons.logging.Log") == null) {
         logFactory.setAttribute("org.apache.commons.logging.Log", CommandLineLogger.class.getName());
      }

      determineLogLevel(options);
      TTFReader app = new TTFReader();
      log.info("TTF Reader for Apache FOP " + Version.getVersion() + "\n");
      if (options.get("-enc") != null) {
         String enc = (String)options.get("-enc");
         if ("ansi".equals(enc)) {
            isCid = false;
         }
      }

      if (options.get("-ttcname") != null) {
         ttcName = (String)options.get("-ttcname");
      }

      if (options.get("-ef") != null) {
         embFile = (String)options.get("-ef");
      }

      if (options.get("-er") != null) {
         embResource = (String)options.get("-er");
      }

      if (options.get("-fn") != null) {
         fontName = (String)options.get("-fn");
      }

      if (options.get("-cn") != null) {
         className = (String)options.get("-cn");
      }

      if (arguments.length == 2 && options.get("-h") == null && options.get("-help") == null && options.get("--help") == null) {
         try {
            log.info("Parsing font...");
            TTFFile ttf = app.loadTTF(arguments[0], ttcName);
            if (ttf != null) {
               Document doc = app.constructFontXML(ttf, fontName, className, embResource, embFile, isCid, ttcName);
               if (isCid) {
                  log.info("Creating CID encoded metrics...");
               } else {
                  log.info("Creating WinAnsi encoded metrics...");
               }

               if (doc != null) {
                  app.writeFontXML(doc, arguments[1]);
               }

               if (ttf.isEmbeddable()) {
                  log.info("This font contains no embedding license restrictions.");
               } else {
                  log.info("** Note: This font contains license retrictions for\n         embedding. This font shouldn't be embedded.");
               }
            }

            log.info("");
            log.info("XML font metrics file successfully created.");
         } catch (Exception var13) {
            log.error("Error while building XML font metrics file.", var13);
            System.exit(-1);
         }
      } else {
         displayUsage();
      }

   }

   public TTFFile loadTTF(String fileName, String fontName) throws IOException {
      TTFFile ttfFile = new TTFFile();
      log.info("Reading " + fileName + "...");
      FontFileReader reader = new FontFileReader(fileName);
      boolean supported = ttfFile.readFont(reader, fontName);
      if (!supported) {
         return null;
      } else {
         log.info("Font Family: " + ttfFile.getFamilyNames());
         if (ttfFile.isCFF()) {
            throw new UnsupportedOperationException("OpenType fonts with CFF data are not supported, yet");
         } else {
            return ttfFile;
         }
      }
   }

   public Document constructFontXML(TTFFile ttf, String fontName, String className, String resource, String file, boolean isCid, String ttcName) {
      log.info("Creating xml font file...");

      Document doc;
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         doc = factory.newDocumentBuilder().newDocument();
      } catch (ParserConfigurationException var17) {
         log.error("Can't create DOM implementation", var17);
         return null;
      }

      Element root = doc.createElement("font-metrics");
      doc.appendChild(root);
      root.setAttribute("metrics-version", String.valueOf(2));
      if (isCid) {
         root.setAttribute("type", "TYPE0");
      } else {
         root.setAttribute("type", "TRUETYPE");
      }

      Element el = doc.createElement("font-name");
      root.appendChild(el);
      String s = FontUtil.stripWhiteSpace(ttf.getPostScriptName());
      if (fontName != null) {
         el.appendChild(doc.createTextNode(FontUtil.stripWhiteSpace(fontName)));
      } else {
         el.appendChild(doc.createTextNode(s));
      }

      if (ttf.getFullName() != null) {
         el = doc.createElement("full-name");
         root.appendChild(el);
         el.appendChild(doc.createTextNode(ttf.getFullName()));
      }

      Set familyNames = ttf.getFamilyNames();
      if (familyNames.size() > 0) {
         String familyName = (String)familyNames.iterator().next();
         el = doc.createElement("family-name");
         root.appendChild(el);
         el.appendChild(doc.createTextNode(familyName));
      }

      el = doc.createElement("embed");
      root.appendChild(el);
      if (file != null && ttf.isEmbeddable()) {
         el.setAttribute("file", file);
      }

      if (resource != null && ttf.isEmbeddable()) {
         el.setAttribute("class", resource);
      }

      el = doc.createElement("cap-height");
      root.appendChild(el);
      el.appendChild(doc.createTextNode(String.valueOf(ttf.getCapHeight())));
      el = doc.createElement("x-height");
      root.appendChild(el);
      el.appendChild(doc.createTextNode(String.valueOf(ttf.getXHeight())));
      el = doc.createElement("ascender");
      root.appendChild(el);
      el.appendChild(doc.createTextNode(String.valueOf(ttf.getLowerCaseAscent())));
      el = doc.createElement("descender");
      root.appendChild(el);
      el.appendChild(doc.createTextNode(String.valueOf(ttf.getLowerCaseDescent())));
      Element bbox = doc.createElement("bbox");
      root.appendChild(bbox);
      int[] bb = ttf.getFontBBox();
      String[] names = new String[]{"left", "bottom", "right", "top"};

      for(int i = 0; i < names.length; ++i) {
         el = doc.createElement(names[i]);
         bbox.appendChild(el);
         el.appendChild(doc.createTextNode(String.valueOf(bb[i])));
      }

      el = doc.createElement("flags");
      root.appendChild(el);
      el.appendChild(doc.createTextNode(String.valueOf(ttf.getFlags())));
      el = doc.createElement("stemv");
      root.appendChild(el);
      el.appendChild(doc.createTextNode(ttf.getStemV()));
      el = doc.createElement("italicangle");
      root.appendChild(el);
      el.appendChild(doc.createTextNode(ttf.getItalicAngle()));
      if (ttcName != null) {
         el = doc.createElement("ttc-name");
         root.appendChild(el);
         el.appendChild(doc.createTextNode(ttcName));
      }

      el = doc.createElement("subtype");
      root.appendChild(el);
      if (isCid) {
         el.appendChild(doc.createTextNode("TYPE0"));
         this.generateDOM4MultiByteExtras(root, ttf, isCid);
      } else {
         el.appendChild(doc.createTextNode("TRUETYPE"));
         this.generateDOM4SingleByteExtras(root, ttf, isCid);
      }

      this.generateDOM4Kerning(root, ttf, isCid);
      return doc;
   }

   private void generateDOM4MultiByteExtras(Element parent, TTFFile ttf, boolean isCid) {
      Document doc = parent.getOwnerDocument();
      Element mel = doc.createElement("multibyte-extras");
      parent.appendChild(mel);
      Element el = doc.createElement("cid-type");
      mel.appendChild(el);
      el.appendChild(doc.createTextNode("CIDFontType2"));
      el = doc.createElement("default-width");
      mel.appendChild(el);
      el.appendChild(doc.createTextNode("0"));
      el = doc.createElement("bfranges");
      mel.appendChild(el);
      Iterator iter = ttf.getCMaps().listIterator();

      while(iter.hasNext()) {
         TTFCmapEntry ce = (TTFCmapEntry)iter.next();
         Element el2 = doc.createElement("bf");
         el.appendChild(el2);
         el2.setAttribute("us", String.valueOf(ce.getUnicodeStart()));
         el2.setAttribute("ue", String.valueOf(ce.getUnicodeEnd()));
         el2.setAttribute("gi", String.valueOf(ce.getGlyphStartIndex()));
      }

      el = doc.createElement("cid-widths");
      el.setAttribute("start-index", "0");
      mel.appendChild(el);
      int[] wx = ttf.getWidths();

      for(int i = 0; i < wx.length; ++i) {
         Element wxel = doc.createElement("wx");
         wxel.setAttribute("w", String.valueOf(wx[i]));
         el.appendChild(wxel);
      }

   }

   private void generateDOM4SingleByteExtras(Element parent, TTFFile ttf, boolean isCid) {
      Document doc = parent.getOwnerDocument();
      Element sel = doc.createElement("singlebyte-extras");
      parent.appendChild(sel);
      Element el = doc.createElement("encoding");
      sel.appendChild(el);
      el.appendChild(doc.createTextNode(ttf.getCharSetName()));
      el = doc.createElement("first-char");
      sel.appendChild(el);
      el.appendChild(doc.createTextNode(String.valueOf(ttf.getFirstChar())));
      el = doc.createElement("last-char");
      sel.appendChild(el);
      el.appendChild(doc.createTextNode(String.valueOf(ttf.getLastChar())));
      Element widths = doc.createElement("widths");
      sel.appendChild(widths);

      for(short i = ttf.getFirstChar(); i <= ttf.getLastChar(); ++i) {
         el = doc.createElement("char");
         widths.appendChild(el);
         el.setAttribute("idx", String.valueOf(i));
         el.setAttribute("wdt", String.valueOf(ttf.getCharWidth(i)));
      }

   }

   private void generateDOM4Kerning(Element parent, TTFFile ttf, boolean isCid) {
      Document doc = parent.getOwnerDocument();
      Iterator iter;
      if (isCid) {
         iter = ttf.getKerning().keySet().iterator();
      } else {
         iter = ttf.getAnsiKerning().keySet().iterator();
      }

      label35:
      while(iter.hasNext()) {
         Integer kpx1 = (Integer)iter.next();
         Element el = doc.createElement("kerning");
         el.setAttribute("kpx1", kpx1.toString());
         parent.appendChild(el);
         Element el2 = null;
         Map h2;
         if (isCid) {
            h2 = (Map)ttf.getKerning().get(kpx1);
         } else {
            h2 = (Map)ttf.getAnsiKerning().get(kpx1);
         }

         Iterator iter2 = h2.keySet().iterator();

         while(true) {
            Integer kpx2;
            do {
               if (!iter2.hasNext()) {
                  continue label35;
               }

               kpx2 = (Integer)iter2.next();
            } while(!isCid && kpx2 >= 256);

            el2 = doc.createElement("pair");
            el2.setAttribute("kpx2", kpx2.toString());
            Integer val = (Integer)h2.get(kpx2);
            el2.setAttribute("kern", val.toString());
            el.appendChild(el2);
         }
      }

   }

   public static void checkMetricsVersion(Attributes attr) throws SAXException {
      String err = null;
      String str = attr.getValue("metrics-version");
      if (str == null) {
         err = "Missing metrics-version attribute";
      } else {
         int version = false;

         try {
            int version = Integer.parseInt(str);
            if (version < 2) {
               err = "Incompatible metrics-version value (" + version + ", should be " + 2 + ")";
            }
         } catch (NumberFormatException var5) {
            err = "Invalid metrics-version attribute value (" + str + ")";
         }
      }

      if (err != null) {
         throw new SAXException(err + " - please regenerate the font metrics file with " + "a more recent version of FOP.");
      }
   }
}
