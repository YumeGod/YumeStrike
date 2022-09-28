package com.xmlmind.fo.converter;

import com.xmlmind.fo.converter.docx.DocxTranslator;
import com.xmlmind.fo.converter.odt.OdtTranslator;
import com.xmlmind.fo.converter.rtf.RtfTranslator;
import com.xmlmind.fo.converter.wml.WmlTranslator;
import com.xmlmind.fo.graphic.Graphic;
import com.xmlmind.fo.graphic.GraphicEnv;
import com.xmlmind.fo.graphic.GraphicFactories;
import com.xmlmind.fo.graphic.GraphicImpl;
import com.xmlmind.fo.graphic.MissingGraphic;
import com.xmlmind.fo.objects.ExternalGraphic;
import com.xmlmind.fo.objects.Flow;
import com.xmlmind.fo.objects.Fo;
import com.xmlmind.fo.objects.PageMasterReference;
import com.xmlmind.fo.objects.PageSequence;
import com.xmlmind.fo.objects.PageSequenceMaster;
import com.xmlmind.fo.objects.Region;
import com.xmlmind.fo.objects.SimplePageMaster;
import com.xmlmind.fo.objects.TableCell;
import com.xmlmind.fo.objects.TableColumn;
import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.PropertyValues;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.properties.compound.Compound;
import com.xmlmind.fo.properties.shorthand.Shorthand;
import com.xmlmind.fo.util.URLUtil;
import com.xmlmind.fo.util.XMLWriter;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class Converter extends DefaultHandler implements ErrorHandler, UriResolver, Translator.UriResolver {
   private static final String DEFAULT_FORMAT = "rtf";
   private static final String FO_NAMESPACE_URI = "http://www.w3.org/1999/XSL/Format";
   private static final String SAX_FEATURE_NAMESPACES = "http://xml.org/sax/features/namespaces";
   private static final String SAX_FEATURE_NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
   private Translator translator;
   private InputSource input;
   private OutputDestination output;
   private Properties properties = new Properties();
   private ErrorHandler errorHandler = this;
   private UriResolver uriResolver = this;
   private XMLReader reader;
   private ConverterGraphicEnv graphicEnv;
   private Locator locator;
   private Vector pageMasters;
   private Vector pageSequenceMasters;
   private Context context;
   private StringBuffer charDataBuffer;
   private Table table;
   private Vector tables;
   private boolean foreignContent;
   private int foreignContentLevel;
   private boolean skipUnknownContent;
   private XMLWriter copiedContent;
   private Value[] copiedContentProperties;

   public Converter() {
      Product.checkVersion();
   }

   public static String[] listEncodings(String var0) throws IllegalArgumentException {
      var0 = var0.toLowerCase();
      if (var0.equals("rtf")) {
         return RtfTranslator.listEncodings();
      } else if (var0.equals("wml")) {
         return WmlTranslator.listEncodings();
      } else if (var0.equals("docx")) {
         return DocxTranslator.listEncodings();
      } else if (var0.equals("odt")) {
         return OdtTranslator.listEncodings();
      } else {
         throw new IllegalArgumentException("bad output format \"" + var0 + "\"");
      }
   }

   public void setProperty(String var1, String var2) {
      if (var2 == null) {
         this.properties.remove(var1);
      } else {
         this.properties.put(var1, var2);
      }

   }

   public void setProperties(Properties var1) {
      Enumeration var2 = var1.propertyNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         this.setProperty(var3, var1.getProperty(var3));
      }

   }

   public void setErrorHandler(ErrorHandler var1) {
      if (var1 != null) {
         this.errorHandler = var1;
      } else {
         this.errorHandler = this;
      }

   }

   public void setUriResolver(UriResolver var1) {
      if (var1 != null) {
         this.uriResolver = var1;
      } else {
         this.uriResolver = this;
      }

   }

   public UriResolver getUriResolver() {
      return this.uriResolver != this ? this.uriResolver : null;
   }

   public void setXMLReader(XMLReader var1) {
      this.reader = var1;
   }

   public XMLReader getXMLReader() {
      return this.reader;
   }

   protected void setOutput(OutputDestination var1) {
      this.output = var1;
   }

   public void convert(InputSource var1, OutputDestination var2) throws Exception {
      this.input = var1;
      this.output = var2;

      try {
         if (this.reader != null) {
            this.reader.setContentHandler(this);
            this.reader.setErrorHandler(this);
            this.reader.setFeature("http://xml.org/sax/features/namespaces", true);
            this.reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
            this.reader.parse(var1);
         } else {
            SAXParserFactory var7 = SAXParserFactory.newInstance();
            var7.setNamespaceAware(true);
            SAXParser var8 = var7.newSAXParser();
            var8.parse((InputSource)var1, (DefaultHandler)this);
         }
      } catch (Exception var6) {
         Exception var3 = var6;
         this.abort();
         if (var6 instanceof SAXException) {
            SAXException var4 = (SAXException)var6;
            Exception var5 = var4.getException();
            if (var5 != null) {
               var3 = var5;
            }
         }

         this.errorHandler.error(var3);
      }

   }

   protected void abort() {
      try {
         this.translator.abort();
         this.clean();
      } catch (Exception var2) {
      }

   }

   protected void clean() throws Exception {
      if (this.graphicEnv != null) {
         this.graphicEnv.dispose();
         this.graphicEnv = null;
      }

   }

   protected void finalize() throws Throwable {
      this.clean();
   }

   public void startDocument() throws SAXException {
      String var1 = this.properties.getProperty("outputFormat");
      if (var1 == null) {
         var1 = "rtf";
      } else {
         var1 = var1.toLowerCase();
      }

      if (var1.equals("rtf")) {
         this.translator = new RtfTranslator();
      } else if (var1.equals("wml")) {
         this.translator = new WmlTranslator();
      } else if (var1.equals("docx")) {
         this.translator = new DocxTranslator();
      } else {
         if (!var1.equals("odt")) {
            throw new SAXException("bad output format \"" + var1 + "\"");
         }

         this.translator = new OdtTranslator();
      }

      if (this.output == null) {
         throw new SAXException("no output specified");
      } else if ((var1.equals("odt") || var1.equals("docx")) && this.output.getByteStream() == null && this.output.getFileName() == null) {
         throw new SAXException("output type not suitable for output format");
      } else {
         this.translator.setOutput(this.output);
         this.translator.setProperties(this.properties);
         this.translator.setErrorHandler(this.errorHandler);
         this.translator.setUriResolver(this);
         this.graphicEnv = new ConverterGraphicEnv(this.errorHandler);
         this.pageMasters = new Vector();
         this.pageSequenceMasters = new Vector();
         this.context = new Context();
         this.context.translator = this.translator;
         this.context.screenResolution = GraphicLayout.getResolutionProperty(this.properties, "screenResolution", 96);
         this.charDataBuffer = null;
         this.table = null;
         this.tables = new Vector();
         this.foreignContent = false;
         this.foreignContentLevel = 0;
         this.skipUnknownContent = false;
         this.copiedContent = null;
         this.copiedContentProperties = null;

         try {
            this.translator.startDocument();
         } catch (Exception var3) {
            throw new SAXException(var3);
         }
      }
   }

   public void endDocument() throws SAXException {
      try {
         this.translator.endDocument();
      } catch (Exception var2) {
         throw new SAXException(var2);
      }

      if (this.graphicEnv != null) {
         this.graphicEnv.dispose();
         this.graphicEnv = null;
      }

   }

   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (this.charDataBuffer != null) {
         this.characters(this.charDataBuffer);
         this.charDataBuffer = null;
      }

      if (this.foreignContent) {
         ++this.foreignContentLevel;
         if (!this.skipUnknownContent) {
            this.startForeignObject(var1, var2, var4);
         }

      } else if (this.copiedContent != null) {
         startElement(var1, var2, var3, var4, this.copiedContent);
      } else {
         int var5 = Fo.index(var2);
         if (var5 < 0) {
            this.foreignContent = true;
            this.foreignContentLevel = 1;
            this.skipUnknownContent = false;
            if (var1.equals("http://www.w3.org/1999/XSL/Format")) {
               this.warning("unknown element \"" + var2 + "\"");
               this.skipUnknownContent = true;
            } else if (this.translator.supportsExtension(var1)) {
               this.startForeignObject(var1, var2, var4);
            } else {
               this.skipUnknownContent = true;
            }

         } else {
            PropertyValues var6 = new PropertyValues();
            Context var7 = this.context;
            this.context = this.context.startElement(var5, var6);
            if (var7.properties != null) {
               var6.inherit(var7.properties);
            } else {
               this.computeFontSize(var6);
            }

            String var8;
            if ((var8 = var4.getValue("", "font")) != null) {
               this.setFont(var6, var8);
            }

            if ((var8 = var4.getValue("", "font-size")) != null) {
               this.setFontSize(var6, var8);
            }

            this.setProperties(var6, var4);
            this.setDefaultValues(var6);
            if (Fo.isReference(var5)) {
               this.resetIndentProperties(var6);
            }

            var6.setCorresponding();
            this.context.update();
            switch (var5) {
               case 0:
                  this.startBasicLink(var6.values);
                  break;
               case 1:
                  this.startBidiOverride(var6.values);
                  break;
               case 2:
                  this.startBlock(var6.values);
                  break;
               case 3:
                  this.startBlockContainer(var6.values);
                  break;
               case 4:
                  this.startCharacter(var6.values);
                  break;
               case 5:
                  this.startColorProfile(var6.values);
                  break;
               case 6:
                  this.startConditionalPageMasterReference(var6.values);
                  break;
               case 7:
                  this.startDeclarations(var6.values);
                  break;
               case 8:
                  this.startExternalGraphic(var6.values);
                  break;
               case 9:
                  this.startFloat(var6.values);
                  break;
               case 10:
                  this.startFlow(var6.values);
                  break;
               case 11:
                  this.startFootnote(var6.values);
                  break;
               case 12:
                  this.startFootnoteBody(var6.values);
                  break;
               case 13:
                  this.startInitialPropertySet(var6.values);
                  break;
               case 14:
                  this.startInline(var6.values);
                  break;
               case 15:
                  this.startInlineContainer(var6.values);
                  break;
               case 16:
                  this.startInstreamForeignObject(var6.values);
                  break;
               case 17:
                  this.startLayoutMasterSet(var6.values);
                  break;
               case 18:
                  this.startLeader(var6.values);
                  break;
               case 19:
                  this.startListBlock(var6.values);
                  break;
               case 20:
                  this.startListItem(var6.values);
                  break;
               case 21:
                  this.startListItemBody(var6.values);
                  break;
               case 22:
                  this.startListItemLabel(var6.values);
                  break;
               case 23:
                  this.startMarker(var6.values);
                  break;
               case 24:
                  this.startMultiCase(var6.values);
                  break;
               case 25:
                  this.startMultiProperties(var6.values);
                  break;
               case 26:
                  this.startMultiPropertySet(var6.values);
                  break;
               case 27:
                  this.startMultiSwitch(var6.values);
                  break;
               case 28:
                  this.startMultiToggle(var6.values);
                  break;
               case 29:
                  this.startPageNumber(var6.values);
                  break;
               case 30:
                  this.startPageNumberCitation(var6.values);
                  break;
               case 31:
                  this.startPageSequence(var6.values);
                  break;
               case 32:
                  this.startPageSequenceMaster(var6.values);
                  break;
               case 33:
                  this.startRegionAfter(var6.values);
                  break;
               case 34:
                  this.startRegionBefore(var6.values);
                  break;
               case 35:
                  this.startRegionBody(var6.values);
                  break;
               case 36:
                  this.startRegionEnd(var6.values);
                  break;
               case 37:
                  this.startRegionStart(var6.values);
                  break;
               case 38:
                  this.startRepeatablePageMasterAlternatives(var6.values);
                  break;
               case 39:
                  this.startRepeatablePageMasterReference(var6.values);
                  break;
               case 40:
                  this.startRetrieveMarker(var6.values);
                  break;
               case 41:
                  this.startRoot(var6.values);
                  break;
               case 42:
                  this.startSimplePageMaster(var6.values);
                  break;
               case 43:
                  this.startSinglePageMasterReference(var6.values);
                  break;
               case 44:
                  this.startStaticContent(var6.values);
                  break;
               case 45:
                  this.startTable(var6.values);
                  break;
               case 46:
                  this.startTableAndCaption(var6.values);
                  break;
               case 47:
                  this.startTableBody(var6.values);
                  break;
               case 48:
                  this.startTableCaption(var6.values);
                  break;
               case 49:
                  this.startTableCell(var6.values);
                  break;
               case 50:
                  this.startTableColumn(var6.values);
                  break;
               case 51:
                  this.startTableFooter(var6.values);
                  break;
               case 52:
                  this.startTableHeader(var6.values);
                  break;
               case 53:
                  this.startTableRow(var6.values);
                  break;
               case 54:
                  this.startTitle(var6.values);
                  break;
               case 55:
                  this.startWrapper(var6.values);
            }

         }
      }
   }

   private static void startElement(String var0, String var1, String var2, Attributes var3, XMLWriter var4) throws SAXException {
      try {
         var4.startElement(var0, var1, var2, var3);
      } catch (Exception var6) {
         throw new SAXException(var6);
      }
   }

   public void endElement(String var1, String var2, String var3) throws SAXException {
      if (this.charDataBuffer != null) {
         this.characters(this.charDataBuffer);
         this.charDataBuffer = null;
      }

      if (this.foreignContent) {
         if (!this.skipUnknownContent) {
            this.endForeignObject(var1, var2);
         }

         if (--this.foreignContentLevel == 0) {
            this.foreignContent = false;
         }

      } else if (this.copiedContent != null && (!"instream-foreign-object".equals(var2) || !"http://www.w3.org/1999/XSL/Format".equals(var1))) {
         endElement(var1, var2, var3, this.copiedContent);
      } else {
         int var4 = Fo.index(var2);
         switch (var4) {
            case 0:
               this.endBasicLink();
               break;
            case 1:
               this.endBidiOverride();
               break;
            case 2:
               this.endBlock();
               break;
            case 3:
               this.endBlockContainer();
               break;
            case 4:
               this.endCharacter();
               break;
            case 5:
               this.endColorProfile();
               break;
            case 6:
               this.endConditionalPageMasterReference();
               break;
            case 7:
               this.endDeclarations();
               break;
            case 8:
               this.endExternalGraphic();
               break;
            case 9:
               this.endFloat();
               break;
            case 10:
               this.endFlow();
               break;
            case 11:
               this.endFootnote();
               break;
            case 12:
               this.endFootnoteBody();
               break;
            case 13:
               this.endInitialPropertySet();
               break;
            case 14:
               this.endInline();
               break;
            case 15:
               this.endInlineContainer();
               break;
            case 16:
               this.endInstreamForeignObject();
               break;
            case 17:
               this.endLayoutMasterSet();
               break;
            case 18:
               this.endLeader();
               break;
            case 19:
               this.endListBlock();
               break;
            case 20:
               this.endListItem();
               break;
            case 21:
               this.endListItemBody();
               break;
            case 22:
               this.endListItemLabel();
               break;
            case 23:
               this.endMarker();
               break;
            case 24:
               this.endMultiCase();
               break;
            case 25:
               this.endMultiProperties();
               break;
            case 26:
               this.endMultiPropertySet();
               break;
            case 27:
               this.endMultiSwitch();
               break;
            case 28:
               this.endMultiToggle();
               break;
            case 29:
               this.endPageNumber();
               break;
            case 30:
               this.endPageNumberCitation();
               break;
            case 31:
               this.endPageSequence();
               break;
            case 32:
               this.endPageSequenceMaster();
               break;
            case 33:
               this.endRegionAfter();
               break;
            case 34:
               this.endRegionBefore();
               break;
            case 35:
               this.endRegionBody();
               break;
            case 36:
               this.endRegionEnd();
               break;
            case 37:
               this.endRegionStart();
               break;
            case 38:
               this.endRepeatablePageMasterAlternatives();
               break;
            case 39:
               this.endRepeatablePageMasterReference();
               break;
            case 40:
               this.endRetrieveMarker();
               break;
            case 41:
               this.endRoot();
               break;
            case 42:
               this.endSimplePageMaster();
               break;
            case 43:
               this.endSinglePageMasterReference();
               break;
            case 44:
               this.endStaticContent();
               break;
            case 45:
               this.endTable();
               break;
            case 46:
               this.endTableAndCaption();
               break;
            case 47:
               this.endTableBody();
               break;
            case 48:
               this.endTableCaption();
               break;
            case 49:
               this.endTableCell();
               break;
            case 50:
               this.endTableColumn();
               break;
            case 51:
               this.endTableFooter();
               break;
            case 52:
               this.endTableHeader();
               break;
            case 53:
               this.endTableRow();
               break;
            case 54:
               this.endTitle();
               break;
            case 55:
               this.endWrapper();
         }

         this.context = this.context.endElement();
      }
   }

   private static void endElement(String var0, String var1, String var2, XMLWriter var3) throws SAXException {
      try {
         var3.endElement(var0, var1, var2);
      } catch (Exception var5) {
         throw new SAXException(var5);
      }
   }

   public void characters(char[] var1, int var2, int var3) throws SAXException {
      if (Product.IS_MANGLED) {
         mangle(var1, var2, var3);
      }

      if (this.foreignContent) {
         if (!this.skipUnknownContent) {
            try {
               String var4 = new String(var1, var2, var3);
               this.translator.characters(var4, this.context);
            } catch (Exception var5) {
               throw new SAXException(var5);
            }
         }

      } else if (this.copiedContent != null) {
         characters(var1, var2, var3, this.copiedContent);
      } else if (this.context.charDataAllowed()) {
         if (this.charDataBuffer == null) {
            this.charDataBuffer = new StringBuffer();
         }

         this.charDataBuffer.append(var1, var2, var3);
      }
   }

   private static void mangle(char[] var0, int var1, int var2) {
      int var3 = var1 + var2;

      for(int var4 = var1 + 10; var4 < var3; var4 += 20) {
         char var5 = var0[var4];
         if (Character.isLetter(var5)) {
            if (var4 + 1 < var3 && Character.isLetter(var0[var4 + 1])) {
               var0[var4 + 1] = var5;
            } else if (var4 - 1 >= var1 && Character.isLetter(var0[var4 - 1])) {
               var0[var4 - 1] = var5;
            }
         }
      }

   }

   private static void characters(char[] var0, int var1, int var2, XMLWriter var3) throws SAXException {
      try {
         var3.characters(var0, var1, var2);
      } catch (Exception var5) {
         throw new SAXException(var5);
      }
   }

   private void characters(StringBuffer var1) throws SAXException {
      int var2 = var1.length();
      char[] var3 = new char[var2];
      var1.getChars(0, var2, var3, 0);
      this.characters(var3);
   }

   private void characters(char[] var1) throws SAXException {
      String var2 = this.normalize(var1);
      if (var2.length() != 0) {
         try {
            this.translator.characters(var2, this.context);
         } catch (Exception var4) {
            throw new SAXException(var4);
         }

         this.context.characters(var2);
      }
   }

   private String normalize(char[] var1) {
      char var2 = 8203;
      int var3 = this.context.linefeedTreatment();
      int var4 = this.context.whiteSpaceTreatment();
      boolean var5 = this.context.whiteSpaceCollapse();
      boolean var6 = false;
      StringBuffer var7 = new StringBuffer(var1.length);
      StringBuffer var8 = new StringBuffer();

      int var9;
      char var10;
      for(var9 = 0; var9 < var1.length; ++var9) {
         var10 = var1[var9];
         switch (var10) {
            case '\t':
            case ' ':
               switch (var4) {
                  case 82:
                     continue;
                  case 83:
                  case 85:
                     if (!var6) {
                        var8.append(' ');
                     }
                     continue;
                  case 84:
                  default:
                     var8.append(' ');
                     continue;
               }
            case '\n':
               switch (var4) {
                  case 84:
                  case 85:
                     var8.setLength(0);
                  default:
                     if (var8.length() > 0) {
                        var7.append(var8.toString());
                        var8 = new StringBuffer();
                     }

                     switch (var3) {
                        case 82:
                        default:
                           break;
                        case 152:
                           var7.append('\n');
                           break;
                        case 207:
                           var7.append(' ');
                           break;
                        case 208:
                           var7.append(var2);
                     }

                     var6 = true;
                     continue;
               }
            default:
               if (var8.length() > 0) {
                  var7.append(var8.toString());
                  var8 = new StringBuffer();
               }

               var7.append(var10);
               var6 = false;
         }
      }

      if (var8.length() > 0) {
         var7.append(var8.toString());
      }

      if (var5 && var7.length() > 0) {
         var9 = var7.length();
         var10 = 0;
         var1 = new char[var9];
         var7.getChars(0, var9, var1, 0);
         var7.setLength(0);

         for(int var11 = 0; var11 < var9; ++var11) {
            char var12 = var1[var11];
            switch (var12) {
               case '\n':
                  if (var10 == ' ') {
                     var7.setLength(var7.length() - 1);
                  }
                  break;
               case ' ':
                  if (var10 == ' ' || var10 == '\n') {
                     continue;
                  }
            }

            var7.append(var12);
            var10 = var12;
         }
      }

      return var7.toString();
   }

   public void ignorableWhitespace(char[] var1, int var2, int var3) throws SAXException {
   }

   public void processingInstruction(String var1, String var2) throws SAXException {
   }

   public void skippedEntity(String var1) throws SAXException {
   }

   public void startPrefixMapping(String var1, String var2) throws SAXException {
   }

   public void endPrefixMapping(String var1) throws SAXException {
   }

   public void setDocumentLocator(Locator var1) {
      this.locator = var1;
   }

   private void startRoot(Value[] var1) throws SAXException {
      try {
         this.translator.startRoot(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endRoot() throws SAXException {
      try {
         this.translator.endRoot(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startDeclarations(Value[] var1) throws SAXException {
      try {
         this.translator.startDeclarations(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endDeclarations() throws SAXException {
      try {
         this.translator.endDeclarations(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startColorProfile(Value[] var1) throws SAXException {
      try {
         this.translator.startColorProfile(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endColorProfile() throws SAXException {
      try {
         this.translator.endColorProfile(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startPageSequence(Value[] var1) throws SAXException {
      PageSequence var2 = new PageSequence(var1);
      if (var2.masterReference == null) {
         this.error(this.requiredValue("master-reference", "page-sequence"));
      }

      var2.pageMaster = this.pageMaster(var2.masterReference);
      if (var2.pageMaster == null) {
         var2.pageSequenceMaster = this.pageSequenceMaster(var2.masterReference);
         if (var2.pageSequenceMaster == null) {
            this.error(this.badValue("master-reference", var2.masterReference));
         }
      }

      try {
         this.translator.startPageSequence(var2, var1, this.context);
      } catch (Exception var4) {
         throw new SAXException(var4);
      }

      this.context.pageSequence = var2;
   }

   private SimplePageMaster pageMaster(String var1) {
      int var2 = 0;

      for(int var3 = this.pageMasters.size(); var2 < var3; ++var2) {
         SimplePageMaster var4 = (SimplePageMaster)this.pageMasters.elementAt(var2);
         if (var1.equals(var4.masterName)) {
            return var4;
         }
      }

      return null;
   }

   private PageSequenceMaster pageSequenceMaster(String var1) {
      int var2 = 0;

      for(int var3 = this.pageSequenceMasters.size(); var2 < var3; ++var2) {
         PageSequenceMaster var4 = (PageSequenceMaster)this.pageSequenceMasters.elementAt(var2);
         if (var1.equals(var4.masterName)) {
            return var4;
         }
      }

      return null;
   }

   private void endPageSequence() throws SAXException {
      try {
         this.translator.endPageSequence(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }

      this.context.pageSequence = null;
   }

   private void startLayoutMasterSet(Value[] var1) throws SAXException {
      try {
         this.translator.startLayoutMasterSet(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endLayoutMasterSet() throws SAXException {
      int var1 = 0;

      for(int var3 = this.pageSequenceMasters.size(); var1 < var3; ++var1) {
         PageSequenceMaster var5 = (PageSequenceMaster)this.pageSequenceMasters.elementAt(var1);
         int var2 = 0;

         for(int var4 = var5.subSequences.size(); var2 < var4; ++var2) {
            PageMasterReference var6 = var5.subSequence(var2);
            var6.setPageMaster(this.pageMasters);
            this.checkReference(var6);
         }
      }

      try {
         this.translator.endLayoutMasterSet(this.context);
      } catch (Exception var7) {
         throw new SAXException(var7);
      }
   }

   private void checkReference(PageMasterReference var1) throws SAXException {
      if (var1.type == 3) {
         int var2 = 0;

         for(int var3 = var1.alternatives.size(); var2 < var3; ++var2) {
            this.checkReference(var1.alternative(var2));
         }
      } else if (var1.pageMaster == null) {
         this.error(this.badValue("master-reference", var1.masterReference));
      }

   }

   private void startPageSequenceMaster(Value[] var1) throws SAXException {
      PageSequenceMaster var2 = new PageSequenceMaster(var1);
      if (var2.masterName == null) {
         this.error(this.requiredValue("master-name", "page-sequence-master"));
      }

      this.pageSequenceMasters.addElement(var2);

      try {
         this.translator.startPageSequenceMaster(var1, this.context);
      } catch (Exception var4) {
         throw new SAXException(var4);
      }
   }

   private void endPageSequenceMaster() throws SAXException {
      try {
         this.translator.endPageSequenceMaster(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startSinglePageMasterReference(Value[] var1) throws SAXException {
      byte var4 = 1;
      PageMasterReference var3 = new PageMasterReference(var4, var1);
      if (var3.masterReference == null) {
         this.error(this.requiredValue("master-reference", "single-page-master-reference"));
      }

      PageSequenceMaster var2 = (PageSequenceMaster)this.pageSequenceMasters.lastElement();
      var2.subSequences.addElement(var3);

      try {
         this.translator.startSinglePageMasterReference(var1, this.context);
      } catch (Exception var6) {
         throw new SAXException(var6);
      }
   }

   private void endSinglePageMasterReference() throws SAXException {
      try {
         this.translator.endSinglePageMasterReference(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startRepeatablePageMasterReference(Value[] var1) throws SAXException {
      byte var4 = 2;
      PageMasterReference var3 = new PageMasterReference(var4, var1);
      if (var3.masterReference == null) {
         this.error(this.requiredValue("master-reference", "repeatable-page-master-reference"));
      }

      PageSequenceMaster var2 = (PageSequenceMaster)this.pageSequenceMasters.lastElement();
      var2.subSequences.addElement(var3);

      try {
         this.translator.startRepeatablePageMasterReference(var1, this.context);
      } catch (Exception var6) {
         throw new SAXException(var6);
      }
   }

   private void endRepeatablePageMasterReference() throws SAXException {
      try {
         this.translator.endRepeatablePageMasterReference(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startRepeatablePageMasterAlternatives(Value[] var1) throws SAXException {
      byte var4 = 3;
      PageMasterReference var3 = new PageMasterReference(var4, var1);
      PageSequenceMaster var2 = (PageSequenceMaster)this.pageSequenceMasters.lastElement();
      var2.subSequences.addElement(var3);

      try {
         this.translator.startRepeatablePageMasterAlternatives(var1, this.context);
      } catch (Exception var6) {
         throw new SAXException(var6);
      }
   }

   private void endRepeatablePageMasterAlternatives() throws SAXException {
      try {
         this.translator.endRepeatablePageMasterAlternatives(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startConditionalPageMasterReference(Value[] var1) throws SAXException {
      byte var5 = 4;
      PageMasterReference var3 = new PageMasterReference(var5, var1);
      if (var3.masterReference == null) {
         this.error(this.requiredValue("master-reference", "conditional-page-master-reference"));
      }

      PageSequenceMaster var2 = (PageSequenceMaster)this.pageSequenceMasters.lastElement();
      PageMasterReference var4 = (PageMasterReference)var2.subSequences.lastElement();
      var4.alternatives.addElement(var3);

      try {
         this.translator.startConditionalPageMasterReference(var1, this.context);
      } catch (Exception var7) {
         throw new SAXException(var7);
      }
   }

   private void endConditionalPageMasterReference() throws SAXException {
      try {
         this.translator.endConditionalPageMasterReference(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startSimplePageMaster(Value[] var1) throws SAXException {
      SimplePageMaster var2 = new SimplePageMaster(var1);
      if (var2.masterName == null) {
         this.error(this.requiredValue("master-name", "simple-page-master"));
      }

      this.pageMasters.addElement(var2);

      try {
         this.translator.startSimplePageMaster(var1, this.context);
      } catch (Exception var4) {
         throw new SAXException(var4);
      }
   }

   private void endSimplePageMaster() throws SAXException {
      try {
         this.translator.endSimplePageMaster(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startRegionBody(Value[] var1) throws SAXException {
      SimplePageMaster var2 = (SimplePageMaster)this.pageMasters.lastElement();
      var2.regions[0] = new Region(var1);

      try {
         this.translator.startRegionBody(var1, this.context);
      } catch (Exception var4) {
         throw new SAXException(var4);
      }
   }

   private void endRegionBody() throws SAXException {
      try {
         this.translator.endRegionBody(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startRegionBefore(Value[] var1) throws SAXException {
      SimplePageMaster var2 = (SimplePageMaster)this.pageMasters.lastElement();
      var2.regions[1] = new Region(var1);

      try {
         this.translator.startRegionBefore(var1, this.context);
      } catch (Exception var4) {
         throw new SAXException(var4);
      }
   }

   private void endRegionBefore() throws SAXException {
      try {
         this.translator.endRegionBefore(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startRegionAfter(Value[] var1) throws SAXException {
      SimplePageMaster var2 = (SimplePageMaster)this.pageMasters.lastElement();
      var2.regions[2] = new Region(var1);

      try {
         this.translator.startRegionAfter(var1, this.context);
      } catch (Exception var4) {
         throw new SAXException(var4);
      }
   }

   private void endRegionAfter() throws SAXException {
      try {
         this.translator.endRegionAfter(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startRegionStart(Value[] var1) throws SAXException {
      SimplePageMaster var2 = (SimplePageMaster)this.pageMasters.lastElement();
      var2.regions[3] = new Region(var1);

      try {
         this.translator.startRegionStart(var1, this.context);
      } catch (Exception var4) {
         throw new SAXException(var4);
      }
   }

   private void endRegionStart() throws SAXException {
      try {
         this.translator.endRegionStart(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startRegionEnd(Value[] var1) throws SAXException {
      SimplePageMaster var2 = (SimplePageMaster)this.pageMasters.lastElement();
      var2.regions[4] = new Region(var1);

      try {
         this.translator.startRegionEnd(var1, this.context);
      } catch (Exception var4) {
         throw new SAXException(var4);
      }
   }

   private void endRegionEnd() throws SAXException {
      try {
         this.translator.endRegionEnd(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startFlow(Value[] var1) throws SAXException {
      Flow var2 = new Flow(1, var1);
      if (var2.flowName == null) {
         this.error(this.requiredValue("flow-name", "flow"));
      }

      try {
         this.translator.startFlow(var2, var1, this.context);
      } catch (Exception var4) {
         throw new SAXException(var4);
      }

      this.context.flow = var2;
   }

   private void endFlow() throws SAXException {
      try {
         this.translator.endFlow(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }

      this.context.flow = null;
   }

   private void startStaticContent(Value[] var1) throws SAXException {
      Flow var2 = new Flow(2, var1);
      if (var2.flowName == null) {
         this.error(this.requiredValue("flow-name", "static-content"));
      }

      try {
         this.translator.startStaticContent(var2, var1, this.context);
      } catch (Exception var4) {
         throw new SAXException(var4);
      }

      this.context.flow = var2;
   }

   private void endStaticContent() throws SAXException {
      try {
         this.translator.endStaticContent(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }

      this.context.flow = null;
   }

   private int region(Flow var1) {
      int var2 = -1;
      Context var3 = this.context.parent();
      if (var3 != null && var3.fo == 31) {
         PageSequence var4 = var3.pageSequence;
         if (var4 != null) {
            SimplePageMaster var5 = var4.pageMaster;
            if (var5 != null) {
               var2 = var1.region(var5);
            } else {
               PageSequenceMaster var6 = var4.pageSequenceMaster;
               Vector var7;
               if (var6 != null && (var7 = var6.subSequences) != null) {
                  int var8 = var7.size();

                  for(int var9 = 0; var9 < var8; ++var9) {
                     PageMasterReference var10 = (PageMasterReference)var7.elementAt(var9);
                     Vector var11;
                     if (var10.type == 3 && (var11 = var10.alternatives) != null) {
                        int var12 = var11.size();

                        for(int var13 = 0; var13 < var12; ++var13) {
                           PageMasterReference var14 = (PageMasterReference)var11.elementAt(var13);
                           var5 = var14.pageMaster;
                           if (var5 != null) {
                              var2 = var1.region(var5);
                              if (var2 != -1) {
                                 return var2;
                              }
                           }
                        }
                     } else {
                        var5 = var10.pageMaster;
                        if (var5 != null) {
                           var2 = var1.region(var5);
                           if (var2 != -1) {
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   private void startTitle(Value[] var1) throws SAXException {
      try {
         this.translator.startTitle(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endTitle() throws SAXException {
      try {
         this.translator.endTitle(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startBlock(Value[] var1) throws SAXException {
      try {
         this.translator.startBlock(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endBlock() throws SAXException {
      try {
         this.translator.endBlock(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startBlockContainer(Value[] var1) throws SAXException {
      try {
         this.translator.startBlockContainer(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endBlockContainer() throws SAXException {
      try {
         this.translator.endBlockContainer(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startBidiOverride(Value[] var1) throws SAXException {
      try {
         this.translator.startBidiOverride(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endBidiOverride() throws SAXException {
      try {
         this.translator.endBidiOverride(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startCharacter(Value[] var1) throws SAXException {
      try {
         this.translator.startCharacter(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endCharacter() throws SAXException {
      try {
         this.translator.endCharacter(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startInitialPropertySet(Value[] var1) throws SAXException {
      try {
         this.translator.startInitialPropertySet(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endInitialPropertySet() throws SAXException {
      try {
         this.translator.endInitialPropertySet(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startExternalGraphic(Value[] var1) throws SAXException {
      ExternalGraphic var2 = new ExternalGraphic(var1);
      Graphic var3 = this.createGraphic(var2);
      if (var3 == null) {
         var3 = createMissingGraphic(var2, this.graphicEnv);
      }

      try {
         this.translator.startExternalGraphic(var3, this.graphicEnv, var1, this.context);
      } catch (Exception var5) {
         throw new SAXException(var5);
      }
   }

   private Graphic createGraphic(ExternalGraphic var1) throws SAXException {
      if (var1.src == null) {
         this.warning(this.requiredValue("src", "external-graphic"));
         return null;
      } else {
         String var2 = this.resolveUri(var1.src);
         if (var2 == null) {
            this.warning("cannot resolve URI \"" + var1.src + "\"");
            return null;
         } else {
            String var3 = var1.mimeType();
            if (var3 == null) {
               this.warning("unknown graphic format (src=\"" + var1.src + "\")");
               return null;
            } else {
               try {
                  return GraphicFactories.createGraphic(var2, var3, var1, this.graphicEnv);
               } catch (Exception var5) {
                  this.warning("failed to load image \"" + var2 + "\": " + var5.getMessage());
                  return null;
               }
            }
         }
      }
   }

   private static Graphic createMissingGraphic(ExternalGraphic var0, GraphicEnv var1) {
      GraphicImpl var2 = MissingGraphic.create(var0, var1);
      var0.src = var2.getLocation();
      String var3 = "content-type:" + var2.getFormat();
      var0.contentType = new Value((byte)15, var3);
      var0.contentWidth = Value.KEYWORD_AUTO;
      var0.contentHeight = Value.KEYWORD_AUTO;
      return var2;
   }

   private void endExternalGraphic() throws SAXException {
      try {
         this.translator.endExternalGraphic(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startInstreamForeignObject(Value[] var1) throws SAXException {
      try {
         File var2 = this.graphicEnv.createTempFile(".xml");
         this.copiedContent = new XMLWriter(var2);
         this.copiedContentProperties = var1;
         this.translator.startInstreamForeignObject(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endInstreamForeignObject() throws SAXException {
      Value[] var1 = this.copiedContentProperties;
      this.copiedContentProperties = null;
      File var2 = this.copiedContent.getOutputFile();
      String var3 = this.copiedContent.getRootElementNamespaceURI();

      try {
         this.copiedContent.close();
      } catch (Exception var11) {
         throw new SAXException(var11);
      } finally {
         this.copiedContent = null;
      }

      ExternalGraphic var4 = new ExternalGraphic(var1);
      var4.src = URLUtil.fileToLocation(var2);
      if (var4.contentType.type != 15 || var4.contentType.string().startsWith("namespace-prefix:")) {
         String var5 = null;
         if ("http://www.w3.org/1998/Math/MathML".equals(var3)) {
            var5 = "application/mathml+xml";
         } else if ("http://www.w3.org/2000/svg".equals(var3)) {
            var5 = "image/svg+xml";
         }

         if (var5 != null) {
            var4.contentType = new Value((byte)15, "content-type:" + var5);
         }
      }

      Graphic var13 = this.createGraphic(var4);
      if (var13 == null) {
         var13 = createMissingGraphic(var4, this.graphicEnv);
      }

      try {
         this.translator.endInstreamForeignObject(var13, this.graphicEnv, var1, this.context);
      } catch (Exception var10) {
         throw new SAXException(var10);
      }
   }

   private void startInline(Value[] var1) throws SAXException {
      try {
         this.translator.startInline(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endInline() throws SAXException {
      try {
         this.translator.endInline(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startInlineContainer(Value[] var1) throws SAXException {
      try {
         this.translator.startInlineContainer(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endInlineContainer() throws SAXException {
      try {
         this.translator.endInlineContainer(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startLeader(Value[] var1) throws SAXException {
      try {
         this.translator.startLeader(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endLeader() throws SAXException {
      try {
         this.translator.endLeader(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startPageNumber(Value[] var1) throws SAXException {
      try {
         this.translator.startPageNumber(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endPageNumber() throws SAXException {
      try {
         this.translator.endPageNumber(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startPageNumberCitation(Value[] var1) throws SAXException {
      try {
         this.translator.startPageNumberCitation(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endPageNumberCitation() throws SAXException {
      try {
         this.translator.endPageNumberCitation(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startTableAndCaption(Value[] var1) throws SAXException {
      try {
         this.translator.startTableAndCaption(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endTableAndCaption() throws SAXException {
      try {
         this.translator.endTableAndCaption(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startTable(Value[] var1) throws SAXException {
      if (this.table != null) {
         this.tables.addElement(this.table);
      }

      this.table = new Table();
      this.context.tableColumns = this.table.columns;

      try {
         this.translator.startTable(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endTable() throws SAXException {
      int var1 = this.tables.size();

      try {
         this.translator.endTable(this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }

      if (var1 > 0) {
         this.table = (Table)this.tables.lastElement();
         this.tables.removeElementAt(var1 - 1);
      } else {
         this.table = null;
      }

   }

   private void startTableColumn(Value[] var1) throws SAXException {
      TableColumn var2 = new TableColumn(var1);
      int var3 = var2.numberColumnsRepeated * var2.numberColumnsSpanned;
      int var4 = var2.columnNumber;

      for(int var5 = var4 + var3; var4 < var5; ++var4) {
         this.table.columns.put(new Integer(var4), var2);
      }

      this.table.column = var2.columnNumber + var3;

      try {
         this.translator.startTableColumn(var1, this.context);
      } catch (Exception var6) {
         throw new SAXException(var6);
      }
   }

   private void endTableColumn() throws SAXException {
      try {
         this.translator.endTableColumn(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startTableCaption(Value[] var1) throws SAXException {
      try {
         this.translator.startTableCaption(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endTableCaption() throws SAXException {
      try {
         this.translator.endTableCaption(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startTableHeader(Value[] var1) throws SAXException {
      this.table.row = 1;
      this.table.column = 1;
      this.table.cell = null;
      this.table.cells = new HashSet();

      try {
         this.translator.startTableHeader(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endTableHeader() throws SAXException {
      try {
         this.translator.endTableHeader(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startTableFooter(Value[] var1) throws SAXException {
      this.table.row = 1;
      this.table.column = 1;
      this.table.cell = null;
      this.table.cells = new HashSet();

      try {
         this.translator.startTableFooter(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endTableFooter() throws SAXException {
      try {
         this.translator.endTableFooter(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startTableBody(Value[] var1) throws SAXException {
      this.table.row = 1;
      this.table.column = 1;
      this.table.cell = null;
      this.table.cells = new HashSet();

      try {
         this.translator.startTableBody(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endTableBody() throws SAXException {
      try {
         this.translator.endTableBody(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startTableRow(Value[] var1) throws SAXException {
      ++this.table.row;
      this.table.column = 1;

      try {
         this.translator.startTableRow(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endTableRow() throws SAXException {
      try {
         this.translator.endTableRow(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startTableCell(Value[] var1) throws SAXException {
      this.table.cell = new TableCell(var1);
      if (this.table.cell.startsRow) {
         ++this.table.row;
         this.table.column = 1;
      }

      this.context.tableCell = this.table.cell;

      try {
         this.translator.startTableCell(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endTableCell() throws SAXException {
      try {
         this.translator.endTableCell(this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }

      if (this.table.cell.numberRowsSpanned > 1) {
         for(int var1 = 1; var1 < this.table.cell.numberRowsSpanned; ++var1) {
            for(int var2 = 0; var2 < this.table.cell.numberColumnsSpanned; ++var2) {
               this.table.allocate(this.table.row + var1, this.table.cell.columnNumber + var2);
            }
         }
      }

      this.table.column = this.table.cell.columnNumber + this.table.cell.numberColumnsSpanned;
      if (this.table.cell.endsRow) {
         ++this.table.row;
         this.table.column = 1;
      }

   }

   private void startListBlock(Value[] var1) throws SAXException {
      try {
         this.translator.startListBlock(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endListBlock() throws SAXException {
      try {
         this.translator.endListBlock(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startListItem(Value[] var1) throws SAXException {
      try {
         this.translator.startListItem(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endListItem() throws SAXException {
      try {
         this.translator.endListItem(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startListItemBody(Value[] var1) throws SAXException {
      try {
         this.translator.startListItemBody(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endListItemBody() throws SAXException {
      try {
         this.translator.endListItemBody(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startListItemLabel(Value[] var1) throws SAXException {
      try {
         this.translator.startListItemLabel(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endListItemLabel() throws SAXException {
      try {
         this.translator.endListItemLabel(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startBasicLink(Value[] var1) throws SAXException {
      try {
         this.translator.startBasicLink(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endBasicLink() throws SAXException {
      try {
         this.translator.endBasicLink(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startMultiSwitch(Value[] var1) throws SAXException {
      try {
         this.translator.startMultiSwitch(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endMultiSwitch() throws SAXException {
      try {
         this.translator.endMultiSwitch(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startMultiCase(Value[] var1) throws SAXException {
      try {
         this.translator.startMultiCase(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endMultiCase() throws SAXException {
      try {
         this.translator.endMultiCase(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startMultiToggle(Value[] var1) throws SAXException {
      try {
         this.translator.startMultiToggle(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endMultiToggle() throws SAXException {
      try {
         this.translator.endMultiToggle(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startMultiProperties(Value[] var1) throws SAXException {
      try {
         this.translator.startMultiProperties(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endMultiProperties() throws SAXException {
      try {
         this.translator.endMultiProperties(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startMultiPropertySet(Value[] var1) throws SAXException {
      try {
         this.translator.startMultiPropertySet(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endMultiPropertySet() throws SAXException {
      try {
         this.translator.endMultiPropertySet(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startFloat(Value[] var1) throws SAXException {
      try {
         this.translator.startFloat(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endFloat() throws SAXException {
      try {
         this.translator.endFloat(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startFootnote(Value[] var1) throws SAXException {
      try {
         this.translator.startFootnote(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endFootnote() throws SAXException {
      try {
         this.translator.endFootnote(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startFootnoteBody(Value[] var1) throws SAXException {
      try {
         this.translator.startFootnoteBody(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endFootnoteBody() throws SAXException {
      try {
         this.translator.endFootnoteBody(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startWrapper(Value[] var1) throws SAXException {
      try {
         this.translator.startWrapper(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endWrapper() throws SAXException {
      try {
         this.translator.endWrapper(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startMarker(Value[] var1) throws SAXException {
      try {
         this.translator.startMarker(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endMarker() throws SAXException {
      try {
         this.translator.endMarker(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startRetrieveMarker(Value[] var1) throws SAXException {
      try {
         this.translator.startRetrieveMarker(var1, this.context);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   private void endRetrieveMarker() throws SAXException {
      try {
         this.translator.endRetrieveMarker(this.context);
      } catch (Exception var2) {
         throw new SAXException(var2);
      }
   }

   private void startForeignObject(String var1, String var2, Attributes var3) throws SAXException {
      try {
         this.translator.startForeignObject(var1, var2, var3, this.context);
      } catch (Exception var5) {
         throw new SAXException(var5);
      }
   }

   private void endForeignObject(String var1, String var2) throws SAXException {
      try {
         this.translator.endForeignObject(var1, var2, this.context);
      } catch (Exception var4) {
         throw new SAXException(var4);
      }
   }

   private void setFont(PropertyValues var1, String var2) throws SAXException {
      Shorthand var3 = (Shorthand)Property.list[103];
      Value var4 = var3.evaluate(var2, this.context);
      if (var4 == null) {
         this.warning(this.badValue("font", var2));
      } else {
         var4 = var3.compute(var4, this.context);
         if (var4 == null) {
            this.warning(this.badValue("font", var2));
         } else {
            var1.set(103, var4);
            var3.expand(var1);
         }
      }
   }

   private void setFontSize(PropertyValues var1, String var2) throws SAXException {
      Property var3 = Property.list[106];
      Value var4 = var3.evaluate(var2, this.context);
      if (var4 == null) {
         this.warning(this.badValue("font-size", var2));
      } else {
         var4 = var3.compute(var4, this.context);
         if (var4 == null) {
            this.warning(this.badValue("font-size", var2));
         } else {
            var1.set(106, var4);
         }
      }
   }

   private void computeFontSize(PropertyValues var1) {
      Property var2 = Property.list[106];
      Value var3 = var1.values[106];
      var3 = var2.compute(var3, this.context);
      var1.values[106] = var3;
   }

   private void setProperties(PropertyValues var1, Attributes var2) throws SAXException {
      int var3;
      int var4;
      String var6;
      Value var7;
      Shorthand var8;
      for(var3 = 0; var3 < Shorthand.list.length; ++var3) {
         var4 = Shorthand.list[var3];
         if (var4 != 103) {
            var8 = (Shorthand)Property.list[var4];
            if ((var6 = var2.getValue("", var8.name)) != null) {
               var7 = var8.evaluate(var6, this.context);
               if (var7 == null) {
                  this.warning(this.badValue(var8.name, var6));
               } else {
                  var7 = var8.compute(var7, this.context);
                  if (var7 == null) {
                     this.warning(this.badValue(var8.name, var6));
                  } else {
                     var1.set(var4, var7);
                     var8.expand(var1);
                  }
               }
            }
         }
      }

      if ((var6 = var2.getValue("xml:lang")) != null) {
         var8 = (Shorthand)Property.list[317];
         var7 = var8.evaluate(var6, this.context);
         if (var7 != null) {
            var7 = var8.compute(var7, this.context);
            if (var7 != null) {
               var1.set(317, var7);
               var8.expand(var1);
            } else {
               this.warning(this.badValue("xml:lang", var6));
            }
         } else {
            this.warning(this.badValue("xml:lang", var6));
         }
      }

      for(var3 = 0; var3 < Compound.list.length; ++var3) {
         var4 = Compound.list[var3];
         Compound var11 = (Compound)Property.list[var4];
         if ((var6 = var2.getValue("", var11.name)) != null) {
            var7 = var11.evaluate(var6, this.context);
            if (var7 == null) {
               this.warning(this.badValue(var11.name, var6));
            } else {
               if (var4 != 164) {
                  var7 = var11.compute(var7, this.context);
                  if (var7 == null) {
                     this.warning(this.badValue(var11.name, var6));
                     continue;
                  }
               }

               var1.set(var4, var7);
               var11.expand(var1);
            }
         }
      }

      var3 = 0;

      for(int var5 = var2.getLength(); var3 < var5; ++var3) {
         String var9 = var2.getLocalName(var3);
         if (var9 != null) {
            var4 = Property.index(var9);
            if (var4 < 0) {
               String var10 = var2.getURI(var3);
               if (var10 != null && (var10.equals("") || var10.equals("http://www.w3.org/1999/XSL/Format"))) {
                  this.warning("unknown property name \"" + var9 + "\"");
               }
            } else if (var4 != 106) {
               Property var12 = Property.list[var4];
               if (var12.type == 0) {
                  var7 = var12.evaluate(var2.getValue(var3), this.context);
                  if (var7 == null) {
                     this.warning(this.badValue(var9, var2.getValue(var3)));
                  } else {
                     var7 = var12.compute(var7, this.context);
                     if (var7 == null) {
                        this.warning(this.badValue(var9, var2.getValue(var3)));
                     } else {
                        var1.set(var4, var7);
                     }
                  }
               }
            }
         }
      }

   }

   private void setDefaultValues(PropertyValues var1) {
      if (!var1.isSpecified(227)) {
         switch (this.context.fo) {
            case 33:
               var1.values[227] = new Value((byte)1, 230);
               break;
            case 34:
               var1.values[227] = new Value((byte)1, 231);
               break;
            case 35:
               var1.values[227] = new Value((byte)1, 232);
               break;
            case 36:
               var1.values[227] = new Value((byte)1, 233);
               break;
            case 37:
               var1.values[227] = new Value((byte)1, 234);
         }
      }

      if (var1.values[65] == null) {
         var1.values[65] = var1.values[78];
      }

      if (var1.values[34] == null) {
         var1.values[34] = var1.values[78];
      }

      if (var1.values[46] == null) {
         var1.values[46] = var1.values[78];
      }

      if (var1.values[50] == null) {
         var1.values[50] = var1.values[78];
      }

      if (!var1.isSpecified(82)) {
         switch (this.context.fo) {
            case 49:
               int var2 = this.table.row;
               int var3 = this.table.column;
               if (var1.startsRow()) {
                  ++var2;
                  var3 = 1;
               }

               while(this.table.isAllocated(var2, var3)) {
                  ++var3;
               }

               var1.values[82] = new Value((byte)3, (double)var3);
               break;
            case 50:
               var1.values[82] = new Value((byte)3, (double)this.table.column);
         }
      }

   }

   private void resetIndentProperties(PropertyValues var1) {
      int[] var2 = new int[]{277, 97, 294};

      for(int var3 = 0; var3 < var2.length; ++var3) {
         int var4 = var2[var3];
         if (!var1.isSpecified(var4)) {
            var1.reset(var4);
         }
      }

   }

   private String badValue(String var1, String var2) {
      return "bad property value " + var1 + "=\"" + var2 + "\"";
   }

   private String requiredValue(String var1, String var2) {
      return "property \"" + var1 + "\" required for object \"" + var2 + "\"";
   }

   private void warning(String var1) throws SAXException {
      String var2 = this.location();
      if (var2 != null) {
         var1 = var1 + " (" + var2 + ")";
      }

      try {
         this.errorHandler.warning(new Exception(var1));
      } catch (Exception var4) {
         throw new SAXException(var4);
      }
   }

   private void error(String var1) throws SAXException {
      String var2 = this.location();
      if (var2 != null) {
         var1 = var1 + " (" + var2 + ")";
      }

      throw new SAXException(var1);
   }

   private String location() {
      if (this.locator != null) {
         int var1 = this.locator.getLineNumber();
         int var2 = this.locator.getColumnNumber();
         String var3 = this.locator.getSystemId();
         StringBuffer var4 = new StringBuffer();
         if (var3 != null) {
            var4.append(var3);
         }

         if (var1 > 0) {
            if (var4.length() > 0) {
               var4.append(", ");
            }

            var4.append("line #" + var1);
            if (var2 > 0) {
               var4.append(", column #" + var2);
            }
         }

         return var4.toString();
      } else {
         return null;
      }
   }

   public void error(Exception var1) throws Exception {
      throw var1;
   }

   public void warning(Exception var1) throws Exception {
      String var2 = var1.getMessage();
      System.err.println("warning: " + var2);
   }

   public void fatalError(SAXParseException var1) throws SAXException {
      throw var1;
   }

   public void error(SAXParseException var1) throws SAXException {
      try {
         this.errorHandler.warning(var1);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   public void warning(SAXParseException var1) throws SAXException {
      try {
         this.errorHandler.warning(var1);
      } catch (Exception var3) {
         throw new SAXException(var3);
      }
   }

   public URL resolve(String var1, URL var2) throws Exception {
      try {
         return new URL(var1);
      } catch (MalformedURLException var6) {
         String var3 = this.properties.getProperty("baseURL");
         if (var3 != null && (var3 = var3.trim()).length() > 0) {
            try {
               URL var4 = new URL(var3);
               var2 = var4;
            } catch (MalformedURLException var5) {
            }
         }

         return new URL(var2, var1);
      }
   }

   public String resolve(String var1) throws Exception {
      return this.resolveUri(var1);
   }

   private String resolveUri(String var1) throws SAXException {
      if (var1.startsWith("data:")) {
         return var1;
      } else {
         URL var2 = null;

         try {
            String var3 = null;
            if (this.locator != null) {
               var3 = this.locator.getSystemId();
            } else if (this.input != null) {
               var3 = this.input.getSystemId();
            }

            if (var3 != null) {
               var2 = URLUtil.urlOrFile(var3);
            }
         } catch (Exception var7) {
         }

         URL var9 = null;

         try {
            var9 = this.uriResolver.resolve(var1, var2);
         } catch (Exception var6) {
            this.warning("URI resolver error: " + var6.getMessage());
         }

         if (var9 == null) {
            return null;
         } else {
            try {
               return var9.toExternalForm();
            } catch (Exception var8) {
               String var5 = var9.getFile();
               if (var5 == null || var5.length() == 0) {
                  var5 = "/";
               }

               if (!var5.startsWith("/")) {
                  var5 = "/" + var5;
               }

               return "file:" + var5;
            }
         }
      }
   }

   public static void main(String[] var0) throws Exception {
      Converter var1 = new Converter();
      InputSource var2 = new InputSource();
      OutputDestination var3 = new OutputDestination();
      Fo.check();
      Property.check();
      Keyword.check();
      switch (var0.length) {
         case 1:
            var2.setSystemId(var0[0]);
            break;
         case 2:
            var2.setSystemId(var0[0]);
            var3.setFileName(var0[1]);
            break;
         case 3:
            var1.setProperty("outputFormat", var0[0]);
            var2.setSystemId(var0[1]);
            var3.setFileName(var0[2]);
      }

      try {
         var1.convert(var2, var3);
      } catch (Exception var5) {
         var5.printStackTrace(System.err);
         System.exit(2);
      }

      System.exit(0);
   }

   private class Table {
      int row;
      int column;
      TableCell cell;
      Hashtable columns;
      HashSet cells;

      private Table() {
         this.row = 1;
         this.column = 1;
         this.columns = new Hashtable();
      }

      void allocate(int var1, int var2) {
         this.cells.add(this.cell(var1, var2));
      }

      boolean isAllocated(int var1, int var2) {
         return this.cells.contains(this.cell(var1, var2));
      }

      String cell(int var1, int var2) {
         return var1 + ":" + var2;
      }

      // $FF: synthetic method
      Table(Object var2) {
         this();
      }
   }
}
