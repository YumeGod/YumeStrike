package org.apache.fop.fonts.apps;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.Version;
import org.apache.fop.fonts.type1.PFMFile;
import org.apache.fop.util.CommandLineLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PFMReader extends AbstractFontReader {
   private static void displayUsage() {
      System.out.println("java " + PFMReader.class.getName() + " [options] metricfile.pfm xmlfile.xml");
      System.out.println();
      System.out.println("where options can be:");
      System.out.println("-d  Debug mode");
      System.out.println("-q  Quiet mode");
      System.out.println("-fn <fontname>");
      System.out.println("    default is to use the fontname in the .pfm file, but");
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
      Map options = new HashMap();
      String[] arguments = parseArguments(options, args);
      LogFactory logFactory = LogFactory.getFactory();
      if (System.getProperty("org.apache.commons.logging.Log") == null) {
         logFactory.setAttribute("org.apache.commons.logging.Log", CommandLineLogger.class.getName());
      }

      determineLogLevel(options);
      PFMReader app = new PFMReader();
      log.info("PFM Reader for Apache FOP " + Version.getVersion() + "\n");
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
            PFMFile pfm = app.loadPFM(arguments[0]);
            if (pfm != null) {
               app.preview(pfm);
               Document doc = app.constructFontXML(pfm, fontName, className, embResource, embFile);
               app.writeFontXML(doc, arguments[1]);
            }

            log.info("XML font metrics file successfullly created.");
         } catch (Exception var11) {
            log.error("Error while building XML font metrics file", var11);
            System.exit(-1);
         }
      } else {
         displayUsage();
      }

   }

   public PFMFile loadPFM(String filename) throws IOException {
      log.info("Reading " + filename + "...");
      log.info("");
      InputStream in = new FileInputStream(filename);

      PFMFile var4;
      try {
         PFMFile pfm = new PFMFile();
         pfm.load(in);
         var4 = pfm;
      } finally {
         in.close();
      }

      return var4;
   }

   public void preview(PFMFile pfm) {
      if (log != null && log.isInfoEnabled()) {
         log.info("Font: " + pfm.getWindowsName());
         log.info("Name: " + pfm.getPostscriptName());
         log.info("CharSet: " + pfm.getCharSetName());
         log.info("CapHeight: " + pfm.getCapHeight());
         log.info("XHeight: " + pfm.getXHeight());
         log.info("LowerCaseAscent: " + pfm.getLowerCaseAscent());
         log.info("LowerCaseDescent: " + pfm.getLowerCaseDescent());
         log.info("Having widths for " + (pfm.getLastChar() - pfm.getFirstChar()) + " characters (" + pfm.getFirstChar() + "-" + pfm.getLastChar() + ").");
         log.info("for example: Char " + pfm.getFirstChar() + " has a width of " + pfm.getCharWidth(pfm.getFirstChar()));
         log.info("");
      }

   }

   public Document constructFontXML(PFMFile pfm, String fontName, String className, String resource, String file) {
      log.info("Creating xml font file...");
      log.info("");

      Document doc;
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         doc = factory.newDocumentBuilder().newDocument();
      } catch (ParserConfigurationException var22) {
         log.error("Can't create DOM implementation", var22);
         return null;
      }

      Element root = doc.createElement("font-metrics");
      doc.appendChild(root);
      root.setAttribute("type", "TYPE1");
      Element el = doc.createElement("font-name");
      root.appendChild(el);
      el.appendChild(doc.createTextNode(pfm.getPostscriptName()));
      el = doc.createElement("embed");
      root.appendChild(el);
      if (file != null) {
         el.setAttribute("file", file);
      }

      if (resource != null) {
         el.setAttribute("class", resource);
      }

      el = doc.createElement("encoding");
      root.appendChild(el);
      el.appendChild(doc.createTextNode(pfm.getCharSetName() + "Encoding"));
      el = doc.createElement("cap-height");
      root.appendChild(el);
      Integer value = new Integer(pfm.getCapHeight());
      el.appendChild(doc.createTextNode(value.toString()));
      el = doc.createElement("x-height");
      root.appendChild(el);
      value = new Integer(pfm.getXHeight());
      el.appendChild(doc.createTextNode(value.toString()));
      el = doc.createElement("ascender");
      root.appendChild(el);
      value = new Integer(pfm.getLowerCaseAscent());
      el.appendChild(doc.createTextNode(value.toString()));
      el = doc.createElement("descender");
      root.appendChild(el);
      value = new Integer(pfm.getLowerCaseDescent());
      el.appendChild(doc.createTextNode(value.toString()));
      Element bbox = doc.createElement("bbox");
      root.appendChild(bbox);
      int[] bb = pfm.getFontBBox();
      String[] names = new String[]{"left", "bottom", "right", "top"};

      for(int i = 0; i < names.length; ++i) {
         el = doc.createElement(names[i]);
         bbox.appendChild(el);
         value = new Integer(bb[i]);
         el.appendChild(doc.createTextNode(value.toString()));
      }

      el = doc.createElement("flags");
      root.appendChild(el);
      value = new Integer(pfm.getFlags());
      el.appendChild(doc.createTextNode(value.toString()));
      el = doc.createElement("stemv");
      root.appendChild(el);
      value = new Integer(pfm.getStemV());
      el.appendChild(doc.createTextNode(value.toString()));
      el = doc.createElement("italicangle");
      root.appendChild(el);
      value = new Integer(pfm.getItalicAngle());
      el.appendChild(doc.createTextNode(value.toString()));
      el = doc.createElement("first-char");
      root.appendChild(el);
      value = new Integer(pfm.getFirstChar());
      el.appendChild(doc.createTextNode(value.toString()));
      el = doc.createElement("last-char");
      root.appendChild(el);
      value = new Integer(pfm.getLastChar());
      el.appendChild(doc.createTextNode(value.toString()));
      Element widths = doc.createElement("widths");
      root.appendChild(widths);

      for(short i = pfm.getFirstChar(); i <= pfm.getLastChar(); ++i) {
         el = doc.createElement("char");
         widths.appendChild(el);
         el.setAttribute("idx", Integer.toString(i));
         el.setAttribute("wdt", Integer.toString(pfm.getCharWidth(i)));
      }

      Iterator iter = pfm.getKerning().keySet().iterator();

      while(iter.hasNext()) {
         Integer kpx1 = (Integer)iter.next();
         el = doc.createElement("kerning");
         el.setAttribute("kpx1", kpx1.toString());
         root.appendChild(el);
         Element el2 = null;
         Map h2 = (Map)pfm.getKerning().get(kpx1);
         Iterator enum2 = h2.entrySet().iterator();

         while(enum2.hasNext()) {
            Map.Entry entry = (Map.Entry)enum2.next();
            Integer kpx2 = (Integer)entry.getKey();
            el2 = doc.createElement("pair");
            el2.setAttribute("kpx2", kpx2.toString());
            Integer val = (Integer)entry.getValue();
            el2.setAttribute("kern", val.toString());
            el.appendChild(el2);
         }
      }

      return doc;
   }
}
