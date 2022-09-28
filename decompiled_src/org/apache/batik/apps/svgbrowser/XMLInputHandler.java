package org.apache.batik.apps.svgbrowser;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.dom.util.HashTable;
import org.apache.batik.util.ParsedURL;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.svg.SVGDocument;

public class XMLInputHandler implements SquiggleInputHandler {
   public static final String[] XVG_MIME_TYPES = new String[]{"image/xml+xsl+svg"};
   public static final String[] XVG_FILE_EXTENSIONS = new String[]{".xml", ".xsl"};
   public static final String ERROR_NO_XML_STYLESHEET_PROCESSING_INSTRUCTION = "XMLInputHandler.error.no.xml.stylesheet.processing.instruction";
   public static final String ERROR_TRANSFORM_OUTPUT_NOT_SVG = "XMLInputHandler.error.transform.output.not.svg";
   public static final String ERROR_TRANSFORM_PRODUCED_NO_CONTENT = "XMLInputHandler.error.transform.produced.no.content";
   public static final String ERROR_TRANSFORM_OUTPUT_WRONG_NS = "XMLInputHandler.error.transform.output.wrong.ns";
   public static final String ERROR_RESULT_GENERATED_EXCEPTION = "XMLInputHandler.error.result.generated.exception";
   public static final String XSL_PROCESSING_INSTRUCTION_TYPE = "text/xsl";
   public static final String PSEUDO_ATTRIBUTE_TYPE = "type";
   public static final String PSEUDO_ATTRIBUTE_HREF = "href";

   public String[] getHandledMimeTypes() {
      return XVG_MIME_TYPES;
   }

   public String[] getHandledExtensions() {
      return XVG_FILE_EXTENSIONS;
   }

   public String getDescription() {
      return "";
   }

   public boolean accept(File var1) {
      return var1.isFile() && this.accept(var1.getPath());
   }

   public boolean accept(ParsedURL var1) {
      if (var1 == null) {
         return false;
      } else {
         String var2 = var1.getPath();
         return this.accept(var2);
      }
   }

   public boolean accept(String var1) {
      if (var1 == null) {
         return false;
      } else {
         for(int var2 = 0; var2 < XVG_FILE_EXTENSIONS.length; ++var2) {
            if (var1.endsWith(XVG_FILE_EXTENSIONS[var2])) {
               return true;
            }
         }

         return false;
      }
   }

   public void handle(ParsedURL var1, JSVGViewerFrame var2) throws Exception {
      String var3 = var1.toString();
      TransformerFactory var4 = TransformerFactory.newInstance();
      DocumentBuilderFactory var5 = DocumentBuilderFactory.newInstance();
      var5.setValidating(false);
      var5.setNamespaceAware(true);
      DocumentBuilder var6 = var5.newDocumentBuilder();
      Document var7 = var6.parse(var3);
      String var8 = this.extractXSLProcessingInstruction(var7);
      if (var8 == null) {
         var8 = var3;
      }

      ParsedURL var9 = new ParsedURL(var3, var8);
      Transformer var10 = var4.newTransformer(new StreamSource(var9.toString()));
      var10.setURIResolver(new DocumentURIResolver(var9.toString()));
      StringWriter var11 = new StringWriter();
      StreamResult var12 = new StreamResult(var11);
      var10.transform(new DOMSource(var7), var12);
      var11.flush();
      var11.close();
      String var13 = XMLResourceDescriptor.getXMLParserClassName();
      SAXSVGDocumentFactory var14 = new SAXSVGDocumentFactory(var13);
      SVGDocument var15 = null;

      try {
         var15 = var14.createSVGDocument(var3, (Reader)(new StringReader(var11.toString())));
      } catch (Exception var17) {
         System.err.println("======================================");
         System.err.println(var11.toString());
         System.err.println("======================================");
         throw new IllegalArgumentException(Resources.getString("XMLInputHandler.error.result.generated.exception"));
      }

      var2.getJSVGCanvas().setSVGDocument(var15);
      var2.setSVGDocument(var15, var3, var15.getTitle());
   }

   protected void checkAndPatch(Document var1) {
      Element var2 = var1.getDocumentElement();
      Node var3 = var2.getFirstChild();
      String var4 = "http://www.w3.org/2000/svg";
      if (var3 == null) {
         throw new IllegalArgumentException(Resources.getString("XMLInputHandler.error.transform.produced.no.content"));
      } else if (var3.getNodeType() == 1 && "svg".equals(var3.getLocalName())) {
         if (!var4.equals(var3.getNamespaceURI())) {
            throw new IllegalArgumentException(Resources.getString("XMLInputHandler.error.transform.output.wrong.ns"));
         } else {
            for(Node var5 = var3.getFirstChild(); var5 != null; var5 = var3.getFirstChild()) {
               var2.appendChild(var5);
            }

            NamedNodeMap var6 = var3.getAttributes();
            int var7 = var6.getLength();

            for(int var8 = 0; var8 < var7; ++var8) {
               var2.setAttributeNode((Attr)var6.item(var8));
            }

            var2.removeChild(var3);
         }
      } else {
         throw new IllegalArgumentException(Resources.getString("XMLInputHandler.error.transform.output.not.svg"));
      }
   }

   protected String extractXSLProcessingInstruction(Document var1) {
      for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (var2.getNodeType() == 7) {
            ProcessingInstruction var3 = (ProcessingInstruction)var2;
            HashTable var4 = new HashTable();
            DOMUtilities.parseStyleSheetPIData(var3.getData(), var4);
            Object var5 = var4.get("type");
            if ("text/xsl".equals(var5)) {
               Object var6 = var4.get("href");
               if (var6 != null) {
                  return var6.toString();
               }

               return null;
            }
         }
      }

      return null;
   }

   public class DocumentURIResolver implements URIResolver {
      String documentURI;

      public DocumentURIResolver(String var2) {
         this.documentURI = var2;
      }

      public Source resolve(String var1, String var2) {
         if (var2 == null || "".equals(var2)) {
            var2 = this.documentURI;
         }

         ParsedURL var3 = new ParsedURL(var2, var1);
         return new StreamSource(var3.toString());
      }
   }
}
