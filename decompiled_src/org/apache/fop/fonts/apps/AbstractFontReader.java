package org.apache.fop.fonts.apps;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.util.CommandLineLogger;
import org.w3c.dom.Document;

public abstract class AbstractFontReader {
   protected static Log log;

   protected AbstractFontReader() {
      if (log == null) {
         log = LogFactory.getLog(AbstractFontReader.class);
      }

   }

   protected static String[] parseArguments(Map options, String[] args) {
      List arguments = new ArrayList();

      for(int i = 0; i < args.length; ++i) {
         if (args[i].startsWith("-")) {
            if (!"-d".equals(args[i]) && !"-q".equals(args[i])) {
               if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                  options.put(args[i], args[i + 1]);
                  ++i;
               } else {
                  options.put(args[i], "");
               }
            } else {
               options.put(args[i], "");
            }
         } else {
            arguments.add(args[i]);
         }
      }

      return (String[])arguments.toArray(new String[0]);
   }

   protected static void setLogLevel(String level) {
      LogFactory.getFactory().setAttribute("level", level);
      if (log instanceof CommandLineLogger) {
         ((CommandLineLogger)log).setLogLevel(level);
      }

   }

   protected static void determineLogLevel(Map options) {
      if (options.get("-d") != null) {
         setLogLevel("debug");
      } else if (options.get("-q") != null) {
         setLogLevel("error");
      } else {
         setLogLevel("info");
      }

   }

   public void writeFontXML(Document doc, String target) throws TransformerException {
      this.writeFontXML(doc, new File(target));
   }

   public void writeFontXML(Document doc, File target) throws TransformerException {
      log.info("Writing xml font file " + target + "...");

      try {
         OutputStream out = new FileOutputStream(target);
         OutputStream out = new BufferedOutputStream(out);

         try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(out));
         } finally {
            out.close();
         }

      } catch (IOException var10) {
         throw new TransformerException("Error writing the output file", var10);
      }
   }
}
