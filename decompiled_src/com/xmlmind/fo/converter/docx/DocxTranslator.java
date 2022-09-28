package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.converter.GraphicLayout;
import com.xmlmind.fo.converter.MsTranslator;
import com.xmlmind.fo.converter.docx.sdt.CustomXml;
import com.xmlmind.fo.converter.docx.sdt.CustomXmlProperties;
import com.xmlmind.fo.converter.docx.sdt.SdtComboBox;
import com.xmlmind.fo.converter.docx.sdt.SdtDataBinding;
import com.xmlmind.fo.converter.docx.sdt.SdtDate;
import com.xmlmind.fo.converter.docx.sdt.SdtDefaultImage;
import com.xmlmind.fo.converter.docx.sdt.SdtElement;
import com.xmlmind.fo.converter.docx.sdt.SdtImageData;
import com.xmlmind.fo.converter.docx.sdt.SdtPicture;
import com.xmlmind.fo.converter.docx.sdt.SdtTextField;
import com.xmlmind.fo.font.GenericFamilies;
import com.xmlmind.fo.graphic.Graphic;
import com.xmlmind.fo.graphic.GraphicEnv;
import com.xmlmind.fo.graphic.GraphicFactories;
import com.xmlmind.fo.objects.Flow;
import com.xmlmind.fo.objects.PageMasterReference;
import com.xmlmind.fo.objects.PageSequence;
import com.xmlmind.fo.objects.PageSequenceMaster;
import com.xmlmind.fo.objects.SimplePageMaster;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoding;
import com.xmlmind.fo.util.TempFile;
import com.xmlmind.fo.util.URLUtil;
import com.xmlmind.fo.zip.ZipFile;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import org.xml.sax.Attributes;

public class DocxTranslator extends MsTranslator {
   private static final double DEFAULT_PAGE_WIDTH = 595.275590551181;
   private static final double DEFAULT_PAGE_HEIGHT = 841.8897637795275;
   private static final String DEFAULT_ENCODING = "UTF8";
   private static final String NAME_MAIN_DOCUMENT = "document.xml";
   private static final String NAME_DOCUMENT_SETTINGS = "settings.xml";
   private static final String NAME_FONT_TABLE = "fonts.xml";
   private static final String NAME_NUMBERING_DEFINITIONS = "numbering.xml";
   private static final String NAME_FOOTNOTES = "footnotes.xml";
   private static final String CUSTOMXML_DIR = "customXml";
   private static final String CUSTOMXML_FILE = "item1.xml";
   private static final String CUSTOMXML_PROPS = "itemProps1.xml";
   private static final String NAME_CUSTOMXML = "customXml/item1.xml";
   private static final String NAME_CUSTOMXML_PROPERTIES = "customXml/itemProps1.xml";
   private static final String SDT_NAMESPACE_URI = "http://www.xmlmind.com/foconverter/xsl/extensions/docx/sdt";
   private static final int SDT_TEXT_FIELD = 1;
   private static final int SDT_DROP_DOWN_LIST = 2;
   private static final int SDT_LIST_ENTRY = 3;
   private static final int SDT_COMBO_BOX = 4;
   private static final int SDT_DATE = 5;
   private static final int SDT_PICTURE = 6;
   private static final int SDT_IMAGE_DATA = 7;
   private static final int SDT_CONFIGURATION = 8;
   private static final Hashtable SDT_LOOKUP = new Hashtable();
   private ContentTypes contentTypes;
   private Relationships pkgRelationships = new Relationships();
   private Relationships docRelationships;
   private Vector tmpFiles;
   private String encoding;
   private String documentPath;
   private PrintWriter documentWriter;
   private int bookmarkId;
   private DocumentSettings settings;
   private FontTable fontTable;
   private NumberingDefinitions numberings;
   private Footnotes footnotes;
   private Images images;
   private boolean prescaleImages;
   private int imageResolution;
   private int imageRendererResolution;
   private boolean alwaysSaveAsPNG;
   private Headers headers;
   private Footers footers;
   private SimplePageMaster oddPageMaster;
   private SimplePageMaster evenPageMaster;
   private SimplePageMaster firstPageMaster;
   private State state;
   private Section section;
   private StaticContent staticContent;
   private Paragraph paragraph;
   private Vector bookmarks;
   private boolean sdtExtension;
   private Vector sdtStack;
   private SdtElement sdtElement;
   private String sdtBinding;
   private int sdtPictureId;
   private File sdtDefaultImage;
   private SdtImageData sdtImageData;
   private String sdtImageFormat;
   private String customXmlTemplate;
   private String prefixMappings;
   private CustomXml customXml;
   private CustomXmlProperties customXmlProperties;

   public DocxTranslator() {
      this.pkgRelationships.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument", "document.xml");
      this.settings = new DocumentSettings();
   }

   public static String[] listEncodings() {
      return new String[]{"UTF-8", "UTF-16"};
   }

   public void abort() {
      try {
         this.clean();
      } catch (Exception var2) {
      }

   }

   public String systemFont(int var1) {
      return "10pt serif";
   }

   public double fontSize(int var1, double var2) {
      double var4 = 10.0;
      switch (var1) {
         case 95:
            var4 = 14.0;
            break;
         case 96:
            var4 = 2.0 * (double)Math.round(var2 / 2.0 + 1.0);
            break;
         case 118:
            var4 = 12.0;
            break;
         case 182:
            var4 = 10.0;
            break;
         case 183:
            var4 = 2.0 * (double)Math.round(var2 / 2.0 - 1.0);
            if (var4 < 2.0) {
               var4 = 2.0;
            }
            break;
         case 235:
            var4 = 18.0;
            break;
         case 236:
            var4 = 6.0;
            break;
         case 239:
            var4 = 16.0;
            break;
         case 243:
            var4 = 8.0;
      }

      return var4;
   }

   public double referenceWidth() {
      return this.state != null ? this.state.referenceWidth : 0.0;
   }

   private void clean() throws Exception {
      if (this.documentWriter != null) {
         this.documentWriter.close();
         this.documentWriter = null;
      }

      this.deleteTmpFiles();
   }

   private void deleteTmpFiles() {
      if (this.tmpFiles != null) {
         int var1 = 0;

         for(int var2 = this.tmpFiles.size(); var1 < var2; ++var1) {
            File var3 = (File)this.tmpFiles.elementAt(var1);
            var3.delete();
         }

         this.tmpFiles = null;
      }

   }

   protected void finalize() throws Throwable {
      this.clean();
   }

   public void startDocument() throws Exception {
      this.contentTypes = new ContentTypes();
      this.contentTypes.addOverride("/document.xml", "application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml");
      this.contentTypes.addOverride("/settings.xml", "application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml");
      this.contentTypes.addOverride("/fonts.xml", "application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml");
      this.contentTypes.addOverride("/numbering.xml", "application/vnd.openxmlformats-officedocument.wordprocessingml.numbering+xml");
      this.contentTypes.addOverride("/footnotes.xml", "application/vnd.openxmlformats-officedocument.wordprocessingml.footnotes+xml");
      this.docRelationships = new Relationships();
      this.docRelationships.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings", "settings.xml");
      this.docRelationships.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable", "fonts.xml");
      this.docRelationships.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/numbering", "numbering.xml");
      this.docRelationships.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/footnotes", "footnotes.xml");
      this.tmpFiles = new Vector();
      this.encoding = this.properties.getProperty("outputEncoding");
      if (this.encoding == null) {
         this.encoding = "UTF8";
      }

      String var1 = Encoding.internalName(this.encoding);
      if (var1 == null || !var1.equals("UTF8") && !var1.equals("UTF16")) {
         throw new Exception("unsupported encoding \"" + this.encoding + "\"");
      } else {
         this.encoding = var1;
         File var2 = TempFile.create("xfc", (String)null);
         this.tmpFiles.addElement(var2);
         this.documentPath = var2.getPath();
         FileOutputStream var3 = new FileOutputStream(this.documentPath);
         OutputStreamWriter var4 = new OutputStreamWriter(new BufferedOutputStream(var3), this.encoding);
         this.documentWriter = new PrintWriter(new BufferedWriter(var4));
         this.bookmarkId = 0;
         this.settings.singleSided = this.singleSided();
         this.settings.mirrorMargins = true;
         this.fontTable = new FontTable();
         GenericFamilies var5 = this.genericFontFamilies((String)null, (String)null, (String)null);
         this.fontTable.add(new FontFace(var5.serif, "roman"));
         this.fontTable.aliasFontFamily("serif", var5.serif);
         this.fontTable.add(new FontFace(var5.sansSerif, "swiss"));
         this.fontTable.aliasFontFamily("sans-serif", var5.sansSerif);
         this.fontTable.add(new FontFace(var5.monospace, "modern"));
         this.fontTable.aliasFontFamily("monospace", var5.monospace);
         if (var5.fantasy != null) {
            this.fontTable.add(new FontFace(var5.fantasy, "decorative"));
            this.fontTable.aliasFontFamily("fantasy", var5.fantasy);
         }

         if (var5.cursive != null) {
            this.fontTable.add(new FontFace(var5.cursive, "script"));
            this.fontTable.aliasFontFamily("cursive", var5.cursive);
         }

         this.numberings = new NumberingDefinitions();
         this.footnotes = new Footnotes();
         this.images = new Images();
         this.prescaleImages = this.prescaleImages();
         this.imageResolution = this.imageResolution();
         this.imageRendererResolution = this.imageRendererResolution();
         this.alwaysSaveAsPNG = this.alwaysSaveAsPNG();
         this.headers = new Headers();
         this.footers = new Footers();
         this.sdtExtension = false;
         this.sdtStack = new Vector();
         this.sdtElement = null;
         this.sdtBinding = null;
         this.sdtPictureId = 0;
         this.sdtDefaultImage = null;
         this.sdtImageData = null;
         this.customXmlTemplate = null;
         this.prefixMappings = null;
         this.customXml = null;
         this.customXmlProperties = null;
         this.documentWriter.println("<?xml version=\"1.0\" encoding=\"" + Encoding.officialName(this.encoding) + "\"?>");
         this.documentWriter.println("<w:document xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:p=\"http://schemas.openxmlformats.org/drawingml/2006/picture\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:v=\"urn:schemas-microsoft-com:vml\">");
         this.documentWriter.println("<w:body>");
      }
   }

   public void endDocument() throws Exception {
      if (this.section != null) {
         this.section.isLast = true;
         this.section.end(this.documentWriter);
      }

      this.documentWriter.println("</w:body>");
      this.documentWriter.println("</w:document>");
      this.documentWriter.flush();
      this.documentWriter.close();
      this.documentWriter = null;
      if (this.customXmlProperties != null) {
         this.contentTypes.addOverride("/customXml/itemProps1.xml", "application/vnd.openxmlformats-officedocument.customXmlProperties+xml");
         this.docRelationships.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXml", "customXml/item1.xml");
      }

      ZipFile var1 = new ZipFile();
      var1.add("[Content_Types].xml", this.contentTypes.getBytes(this.encoding));
      var1.add("_rels/.rels", this.pkgRelationships.getBytes(this.encoding));
      var1.add("_rels/document.xml.rels", this.docRelationships.getBytes(this.encoding));
      var1.add("document.xml", this.documentPath);
      var1.add("settings.xml", this.settings.getBytes(this.encoding));
      var1.add("fonts.xml", this.fontTable.getBytes(this.encoding));
      var1.add("numbering.xml", this.numberings.getBytes(this.encoding));
      this.footnotes.store(var1, "footnotes.xml", this.encoding);
      this.headers.store(var1, this.encoding);
      this.footers.store(var1, this.encoding);
      this.images.store(var1);
      if (this.customXmlProperties != null) {
         if (this.customXmlTemplate != null) {
            var1.add("customXml/item1.xml", this.customXmlTemplate);
         } else {
            var1.add("customXml/item1.xml", this.customXml.getBytes(this.encoding));
         }

         var1.add("customXml/itemProps1.xml", this.customXmlProperties.getBytes(this.encoding));
         Relationships var2 = new Relationships();
         var2.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/customXmlProps", "itemProps1.xml");
         var1.add("customXml/_rels/item1.xml.rels", var2.getBytes(this.encoding));
      }

      if (this.output != null) {
         Object var5 = this.output.getByteStream();
         boolean var3 = false;
         if (var5 == null) {
            String var4 = this.output.getFileName();
            if (var4 != null) {
               var5 = new FileOutputStream(var4);
               var3 = true;
            } else {
               var5 = System.out;
            }
         }

         BufferedOutputStream var6 = new BufferedOutputStream((OutputStream)var5);
         var1.write(var6);
         var6.flush();
         if (var3) {
            var6.close();
         }
      }

      this.deleteTmpFiles();
   }

   public void characters(String var1, Context var2) throws Exception {
      if (!this.state.skipContent) {
         if (this.sdtExtension) {
            this.sdtCharacters(var1);
         } else {
            if (this.state.runProperties == null) {
               this.state.runProperties = this.runProperties(var2);
            }

            boolean var3 = false;
            if (var2.whiteSpaceTreatment() == 152) {
               var3 = true;
            }

            StringTokenizer var4 = new StringTokenizer(var1, "\n", true);
            if (this.paragraph == null) {
               this.startParagraph(var2);
            }

            while(var4.hasMoreTokens()) {
               String var5 = var4.nextToken();
               if (var5.equals("\n")) {
                  this.paragraph.addBreak(this.state.runProperties);
               } else {
                  Text var6 = new Text(var5, this.state.runProperties, var3);
                  this.paragraph.add(var6);
               }
            }

         }
      }
   }

   private RunProperties runProperties(Context var1) {
      return new RunProperties(var1, this.fontTable);
   }

   private void startParagraph(Context var1) {
      this.paragraph = new Paragraph(new ParagraphProperties(var1));
      if (this.bookmarks != null && this.state.context != 2) {
         this.paragraph.bookmarks = this.bookmarks();
         this.bookmarks = null;
      }

   }

   private Bookmark[] bookmarks() {
      Bookmark[] var1 = new Bookmark[this.bookmarks.size()];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = (Bookmark)this.bookmarks.elementAt(var2);
      }

      return var1;
   }

   private void endParagraph() throws Exception {
      int var1 = this.state.context;
      double var2 = this.state.referenceWidth;
      if (this.paragraph.isEmpty()) {
         this.paragraph = null;
      } else {
         switch (var1) {
            case 2:
               this.state.listItem.setLabel(this.paragraph);
               break;
            case 3:
               this.state.listItem.add(this.paragraph);
               break;
            case 4:
               this.state.table.add(this.paragraph);
               break;
            case 5:
               if (this.paragraph.requiresLayout()) {
                  this.paragraph.layout(var2);
               }

               this.state.tableAndCaption.caption.add(this.paragraph);
               break;
            case 6:
            default:
               if (this.paragraph.requiresLayout()) {
                  this.paragraph.layout(var2);
               }

               this.paragraph.print(this.documentWriter);
               break;
            case 7:
               if (this.paragraph.requiresLayout()) {
                  this.paragraph.layout(var2);
               }

               this.state.footnote.add(this.paragraph);
               break;
            case 8:
               if (this.paragraph.requiresLayout()) {
                  this.paragraph.layout(var2);
               }

               this.staticContent.add(this.paragraph);
         }

         this.paragraph = null;
      }
   }

   public void startRoot(Value[] var1, Context var2) throws Exception {
   }

   public void endRoot(Context var1) throws Exception {
   }

   public void startDeclarations(Value[] var1, Context var2) throws Exception {
   }

   public void endDeclarations(Context var1) throws Exception {
   }

   public void startColorProfile(Value[] var1, Context var2) throws Exception {
   }

   public void endColorProfile(Context var1) throws Exception {
   }

   public void startPageSequence(PageSequence var1, Value[] var2, Context var3) throws Exception {
      if (this.section != null) {
         this.section.end(this.documentWriter);
      }

      this.selectPageMasters(var1);
      if (this.settings.singleSided) {
         this.evenPageMaster = null;
      }

      this.section = new Section();
      this.section.pageWidth = 595.275590551181;
      this.section.pageHeight = 841.8897637795275;
      this.section.initialize(this.oddPageMaster, this.evenPageMaster, this.firstPageMaster, var1);
      if (!this.section.mirrorMargins) {
         this.settings.mirrorMargins = false;
      }

      this.section.start(this.documentWriter);
      this.state = new State(31);
      this.state.referenceWidth = this.section.columnWidth;
      this.staticContent = null;
      this.paragraph = null;
      this.bookmarks = null;
      if (var2[125] != null) {
         this.addBookmark(var2[125].id(), var3);
      }

   }

   private void selectPageMasters(PageSequence var1) {
      this.oddPageMaster = null;
      this.evenPageMaster = null;
      this.firstPageMaster = null;
      if (var1.pageMaster != null) {
         this.oddPageMaster = var1.pageMaster;
      } else {
         PageSequenceMaster var2 = var1.pageSequenceMaster;
         PageMasterReference var3 = var2.subSequence(0);
         switch (var3.type) {
            case 1:
               this.firstPageMaster = var3.pageMaster;
               break;
            case 2:
               if (var3.maximumRepeats == 1) {
                  this.firstPageMaster = var3.pageMaster;
               }
         }

         PageMasterReference var4 = null;
         int var5 = 0;
         int var6 = var2.subSequences.size();

         for(int var7 = 0; var5 < var6; ++var5) {
            var3 = var2.subSequence(var5);
            if (var3.maximumRepeats < 0) {
               var4 = var3;
               break;
            }

            if (var3.maximumRepeats > var7) {
               var7 = var3.maximumRepeats;
               var4 = var3;
            }
         }

         if (var4.type == 3) {
            var5 = 0;

            for(var6 = var4.alternatives.size(); var5 < var6; ++var5) {
               PageMasterReference var8 = var4.alternative(var5);
               if (var8.blankOrNotBlank != 21) {
                  switch (var8.pagePosition) {
                     case 66:
                        if (this.firstPageMaster == null) {
                           this.firstPageMaster = var8.pageMaster;
                        }
                     case 97:
                        break;
                     default:
                        switch (var8.oddOrEven) {
                           case 56:
                              this.evenPageMaster = var8.pageMaster;
                              break;
                           default:
                              this.oddPageMaster = var8.pageMaster;
                        }
                  }
               }
            }

            if (this.oddPageMaster == null) {
               this.oddPageMaster = var4.alternative(0).pageMaster;
            }
         } else {
            this.oddPageMaster = var4.pageMaster;
         }

         if (this.firstPageMaster == this.oddPageMaster) {
            this.firstPageMaster = null;
         }

      }
   }

   public void endPageSequence(Context var1) throws Exception {
   }

   public void startLayoutMasterSet(Value[] var1, Context var2) throws Exception {
   }

   public void endLayoutMasterSet(Context var1) throws Exception {
   }

   public void startPageSequenceMaster(Value[] var1, Context var2) throws Exception {
   }

   public void endPageSequenceMaster(Context var1) throws Exception {
   }

   public void startSinglePageMasterReference(Value[] var1, Context var2) throws Exception {
   }

   public void endSinglePageMasterReference(Context var1) throws Exception {
   }

   public void startRepeatablePageMasterReference(Value[] var1, Context var2) throws Exception {
   }

   public void endRepeatablePageMasterReference(Context var1) throws Exception {
   }

   public void startRepeatablePageMasterAlternatives(Value[] var1, Context var2) throws Exception {
   }

   public void endRepeatablePageMasterAlternatives(Context var1) throws Exception {
   }

   public void startConditionalPageMasterReference(Value[] var1, Context var2) throws Exception {
   }

   public void endConditionalPageMasterReference(Context var1) throws Exception {
   }

   public void startSimplePageMaster(Value[] var1, Context var2) throws Exception {
   }

   public void endSimplePageMaster(Context var1) throws Exception {
   }

   public void startRegionBody(Value[] var1, Context var2) throws Exception {
   }

   public void endRegionBody(Context var1) throws Exception {
   }

   public void startRegionBefore(Value[] var1, Context var2) throws Exception {
   }

   public void endRegionBefore(Context var1) throws Exception {
   }

   public void startRegionAfter(Value[] var1, Context var2) throws Exception {
   }

   public void endRegionAfter(Context var1) throws Exception {
   }

   public void startRegionStart(Value[] var1, Context var2) throws Exception {
   }

   public void endRegionStart(Context var1) throws Exception {
   }

   public void startRegionEnd(Value[] var1, Context var2) throws Exception {
   }

   public void endRegionEnd(Context var1) throws Exception {
   }

   public void startFlow(Flow var1, Value[] var2, Context var3) throws Exception {
      this.state = this.state.update(10);
      this.state.referenceWidth = this.section.columnWidth;
      if (var1.region(this.oddPageMaster) != 0) {
         this.state.skipContent = true;
      } else {
         if (var2[125] != null) {
            this.addBookmark(var2[125].id(), var3);
         }

      }
   }

   public void endFlow(Context var1) throws Exception {
      if (!this.state.skipContent && this.bookmarks != null) {
         this.writeBookmarks();
         this.bookmarks = null;
      }

      this.state = this.state.restore();
   }

   public void startStaticContent(Flow var1, Value[] var2, Context var3) throws Exception {
      this.state = this.state.update(44);
      this.state.referenceWidth = this.section.contentWidth;
      String var4 = null;
      switch (var1.region(this.oddPageMaster)) {
         case 1:
            var4 = this.createHeader();
            this.section.oddHeaderId = var4;
            if (!this.settings.singleSided && this.evenPageMaster == null) {
               this.section.evenHeaderId = var4;
            }
            break;
         case 2:
            var4 = this.createFooter();
            this.section.oddFooterId = var4;
            if (!this.settings.singleSided && this.evenPageMaster == null) {
               this.section.evenFooterId = var4;
            }
      }

      if (this.evenPageMaster != null) {
         switch (var1.region(this.evenPageMaster)) {
            case 1:
               if (var4 == null) {
                  var4 = this.createHeader();
               }

               this.section.evenHeaderId = var4;
               break;
            case 2:
               if (var4 == null) {
                  var4 = this.createFooter();
               }

               this.section.evenFooterId = var4;
         }
      }

      if (this.firstPageMaster != null) {
         switch (var1.region(this.firstPageMaster)) {
            case 1:
               if (var4 == null) {
                  var4 = this.createHeader();
               }

               this.section.firstHeaderId = var4;
               break;
            case 2:
               if (var4 == null) {
                  var4 = this.createFooter();
               }

               this.section.firstFooterId = var4;
         }
      }

      if (var4 == null) {
         this.state.skipContent = true;
      }

   }

   private String createHeader() throws Exception {
      this.staticContent = new StaticContent(0);
      String var1 = this.headers.add(this.staticContent);
      this.contentTypes.addOverride("/" + var1, "application/vnd.openxmlformats-officedocument.wordprocessingml.header+xml");
      return this.docRelationships.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/header", var1);
   }

   private String createFooter() throws Exception {
      this.staticContent = new StaticContent(1);
      String var1 = this.footers.add(this.staticContent);
      this.contentTypes.addOverride("/" + var1, "application/vnd.openxmlformats-officedocument.wordprocessingml.footer+xml");
      return this.docRelationships.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/footer", var1);
   }

   public void endStaticContent(Context var1) throws Exception {
      this.staticContent = null;
      this.state = this.state.restore();
   }

   public void startTitle(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(54);
   }

   public void endTitle(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   private void addBookmark(String var1, Context var2) {
      Bookmark var3 = new Bookmark(this.bookmarkId++, var1, var2);
      if (this.bookmarks == null) {
         this.bookmarks = new Vector();
      }

      this.bookmarks.addElement(var3);
   }

   private void writeBookmarks() {
      Bookmark[] var1 = this.bookmarks();

      int var2;
      for(var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].start(this.documentWriter);
      }

      for(var2 = var1.length - 1; var2 >= 0; --var2) {
         var1[var2].end(this.documentWriter);
      }

   }

   public void startBlock(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(2);
      if (!this.state.skipContent) {
         if (this.paragraph != null) {
            this.endParagraph();
         }

         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endBlock(Context var1) throws Exception {
      if (!this.state.skipContent && this.paragraph != null) {
         this.endParagraph();
      }

      this.state = this.state.restore();
   }

   public void startBlockContainer(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(3);
   }

   public void endBlockContainer(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startBidiOverride(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(1);
   }

   public void endBidiOverride(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startCharacter(Value[] var1, Context var2) throws Exception {
   }

   public void endCharacter(Context var1) throws Exception {
   }

   public void startInitialPropertySet(Value[] var1, Context var2) throws Exception {
   }

   public void endInitialPropertySet(Context var1) throws Exception {
   }

   public void startExternalGraphic(Graphic var1, GraphicEnv var2, Value[] var3, Context var4) throws Exception {
      this.state = this.state.update(8);
      if (!this.state.skipContent) {
         if (var3[125] != null) {
            this.startBookmark(var3[125].id(), var4);
         }

         Relationships var5;
         if (this.state.footnote != null) {
            var5 = this.footnotes.relationships();
         } else if (this.staticContent != null) {
            var5 = this.staticContent.relationships();
         } else {
            var5 = this.docRelationships;
         }

         DocxGraphicEnv var6 = new DocxGraphicEnv(var2, this.images, var5);
         Picture var7 = new Picture(var1, var6, this.runProperties(var4), this.prescaleImages, this.imageResolution, this.imageRendererResolution, this.alwaysSaveAsPNG, this.useVML());
         if (this.paragraph == null) {
            this.startParagraph(var4);
         }

         this.paragraph.add(var7);
      }

   }

   private boolean useVML() {
      return "true".equals(this.properties.getProperty("docx.useVML"));
   }

   public void endExternalGraphic(Context var1) throws Exception {
      if (!this.state.skipContent && this.state.bookmark != null) {
         this.endBookmark();
      }

      this.state = this.state.restore();
   }

   public void startInstreamForeignObject(Value[] var1, Context var2) throws Exception {
   }

   public void endInstreamForeignObject(Graphic var1, GraphicEnv var2, Value[] var3, Context var4) throws Exception {
      this.state = this.state.update(16);
      if (!this.state.skipContent) {
         if (var3[125] != null) {
            this.startBookmark(var3[125].id(), var4);
         }

         Relationships var5;
         if (this.state.footnote != null) {
            var5 = this.footnotes.relationships();
         } else if (this.staticContent != null) {
            var5 = this.staticContent.relationships();
         } else {
            var5 = this.docRelationships;
         }

         DocxGraphicEnv var6 = new DocxGraphicEnv(var2, this.images, var5);
         Picture var7 = new Picture(var1, var6, this.runProperties(var4), this.prescaleImages, this.imageResolution, this.imageRendererResolution, this.alwaysSaveAsPNG, this.useVML());
         if (this.paragraph == null) {
            this.startParagraph(var4);
         }

         this.paragraph.add(var7);
         if (this.state.bookmark != null) {
            this.endBookmark();
         }
      }

      this.state = this.state.restore();
   }

   public void startInline(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(14);
      if (!this.state.skipContent && var1[125] != null) {
         this.startBookmark(var1[125].id(), var2);
      }

   }

   public void endInline(Context var1) throws Exception {
      if (!this.state.skipContent && this.state.bookmark != null) {
         this.endBookmark();
      }

      this.state = this.state.restore();
   }

   public void startInlineContainer(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(15);
   }

   public void endInlineContainer(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startLeader(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(18);
      if (!this.state.skipContent) {
         Leader var3 = new Leader(var2);
         var3.properties = this.runProperties(var2);
         if (this.paragraph == null) {
            this.startParagraph(var2);
         }

         this.paragraph.add(var3);
         this.state.skipContent = true;
      }

   }

   public void endLeader(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startPageNumber(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(29);
      if (!this.state.skipContent) {
         if (this.paragraph == null) {
            this.startParagraph(var2);
         }

         this.paragraph.addPageNumber(this.runProperties(var2));
      }

   }

   public void endPageNumber(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startPageNumberCitation(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(30);
      if (!this.state.skipContent && var1[226] != null) {
         String var3 = var1[226].idref();
         PageReference var4 = new PageReference(var3, var2);
         var4.properties = this.runProperties(var2);
         if (this.paragraph == null) {
            this.startParagraph(var2);
         }

         this.paragraph.add((Field)var4);
      }

   }

   public void endPageNumberCitation(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   private void startBookmark(String var1, Context var2) {
      this.state.bookmark = new Bookmark(this.bookmarkId++, var1, var2);
      if (this.paragraph == null) {
         this.startParagraph(var2);
      }

      this.paragraph.startBookmark(this.state.bookmark);
   }

   private void endBookmark() {
      this.paragraph.endBookmark(this.state.bookmark);
   }

   public void startTableAndCaption(Value[] var1, Context var2) throws Exception {
      if (this.paragraph != null) {
         this.endParagraph();
      }

      this.state = this.state.update(46);
      if (!this.state.skipContent) {
         this.state.tableAndCaption = new TableAndCaption(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      } else {
         this.state.tableAndCaption = null;
      }

   }

   public void endTableAndCaption(Context var1) throws Exception {
      TableAndCaption var2 = this.state.tableAndCaption;
      this.state = this.state.restore();
      if (var2 != null) {
         switch (this.state.context) {
            case 3:
               this.state.listItem.add(var2);
               break;
            case 4:
               this.state.table.add(var2);
               break;
            case 8:
               var2.layout(this.state.referenceWidth, this.numberings);
               this.staticContent.add(var2);
               break;
            default:
               var2.layout(this.state.referenceWidth, this.numberings);
               var2.print(this.documentWriter);
         }
      }

   }

   public void startTable(Value[] var1, Context var2) throws Exception {
      if (this.paragraph != null) {
         this.endParagraph();
      }

      this.state = this.state.update(45);
      if (!this.state.skipContent) {
         this.state.table = new Table(var2);
         if (var2.parent().fo == 46) {
            this.state.tableAndCaption.table = this.state.table;
         }

         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      } else {
         this.state.table = null;
      }

   }

   public void endTable(Context var1) throws Exception {
      Table var2 = null;
      if (var1.parent().fo != 46) {
         var2 = this.state.table;
      }

      this.state = this.state.restore();
      if (var2 != null) {
         switch (this.state.context) {
            case 3:
               this.state.listItem.add(var2);
               break;
            case 4:
               this.state.table.add(var2);
               break;
            case 8:
               var2.layout(this.state.referenceWidth, this.numberings);
               this.staticContent.add(var2);
               break;
            default:
               var2.layout(this.state.referenceWidth, this.numberings);
               var2.print(this.documentWriter);
         }
      }

   }

   public void startTableColumn(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(50);
      if (!this.state.skipContent) {
         this.state.table.add(new TableColumn(var2));
      }

   }

   public void endTableColumn(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startTableCaption(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(48);
      if (!this.state.skipContent) {
         this.state.tableAndCaption.caption = new Caption(var2);
         this.state.bookmarks = this.bookmarks;
         this.bookmarks = null;
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endTableCaption(Context var1) throws Exception {
      if (!this.state.skipContent) {
         this.bookmarks = this.state.bookmarks;
      }

      this.state = this.state.restore();
   }

   public void startTableHeader(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(52);
      if (!this.state.skipContent) {
         this.state.table.startHeader(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endTableHeader(Context var1) throws Exception {
      if (!this.state.skipContent) {
         this.state.table.endHeader();
      }

      this.state = this.state.restore();
   }

   public void startTableFooter(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(51);
      if (!this.state.skipContent) {
         this.state.table.startFooter(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endTableFooter(Context var1) throws Exception {
      if (!this.state.skipContent) {
         this.state.table.endFooter();
      }

      this.state = this.state.restore();
   }

   public void startTableBody(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(47);
      if (!this.state.skipContent) {
         this.state.table.startBody(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endTableBody(Context var1) throws Exception {
      if (!this.state.skipContent) {
         this.state.table.endBody();
      }

      this.state = this.state.restore();
   }

   public void startTableRow(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(53);
      if (!this.state.skipContent) {
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }

         if (this.bookmarks != null) {
            this.state.table.startRow(var2, this.bookmarks());
            this.bookmarks = null;
         } else {
            this.state.table.startRow(var2, (Bookmark[])null);
         }
      }

   }

   public void endTableRow(Context var1) throws Exception {
      if (!this.state.skipContent) {
         this.state.table.endRow();
      }

      this.state = this.state.restore();
   }

   public void startTableCell(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(49);
      if (!this.state.skipContent) {
         this.state.referenceWidth = this.state.table.startCell(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }

         var2.background = null;
      }

   }

   public void endTableCell(Context var1) throws Exception {
      if (!this.state.skipContent) {
         this.state.table.endCell();
      }

      this.state = this.state.restore();
   }

   public void startListBlock(Value[] var1, Context var2) throws Exception {
      if (this.paragraph != null) {
         this.endParagraph();
      }

      this.state = this.state.update(19);
      if (!this.state.skipContent) {
         this.state.list = new List(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      } else {
         this.state.list = null;
      }

   }

   public void endListBlock(Context var1) throws Exception {
      List var2 = this.state.list;
      this.state = this.state.restore();
      if (var2 != null) {
         switch (this.state.context) {
            case 3:
               this.state.listItem.add(var2);
               break;
            case 4:
               this.state.table.add(var2);
               break;
            case 5:
            case 6:
            default:
               var2.layout(this.state.referenceWidth, this.numberings);
               var2.print(this.documentWriter);
               break;
            case 7:
               var2.layout(this.state.referenceWidth, this.numberings);
               this.state.footnote.add(var2);
               break;
            case 8:
               var2.layout(this.state.referenceWidth, this.numberings);
               this.staticContent.add(var2);
         }
      }

   }

   public void startListItem(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(20);
      if (!this.state.skipContent) {
         this.state.listItem = new ListItem(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      } else {
         this.state.listItem = null;
      }

   }

   public void endListItem(Context var1) throws Exception {
      ListItem var2 = this.state.listItem;
      this.state = this.state.restore();
      if (var2 != null) {
         this.state.list.add(var2);
      }

   }

   public void startListItemBody(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(21);
      if (!this.state.skipContent && var1[125] != null) {
         this.addBookmark(var1[125].id(), var2);
      }

   }

   public void endListItemBody(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startListItemLabel(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(22);
      if (!this.state.skipContent && var1[125] != null) {
         this.addBookmark(var1[125].id(), var2);
      }

   }

   public void endListItemLabel(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startBasicLink(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(0);
      if (!this.state.skipContent) {
         Link var3 = new Link(var2);
         if (var3.type == 1 && var3.target != null) {
            Relationships var4;
            if (this.state.footnote != null) {
               var4 = this.footnotes.relationships();
            } else if (this.staticContent != null) {
               var4 = this.staticContent.relationships();
            } else {
               var4 = this.docRelationships;
            }

            var3.id = var4.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink", Wml.escape(var3.target), 1);
         }

         this.state.link = var3;
         if (this.paragraph == null) {
            this.startParagraph(var2);
         }

         this.paragraph.startLink(this.state.link);
      }

   }

   public void endBasicLink(Context var1) throws Exception {
      if (!this.state.skipContent) {
         this.paragraph.endLink(this.state.link);
      }

      this.state = this.state.restore();
   }

   public void startMultiSwitch(Value[] var1, Context var2) throws Exception {
   }

   public void endMultiSwitch(Context var1) throws Exception {
   }

   public void startMultiCase(Value[] var1, Context var2) throws Exception {
   }

   public void endMultiCase(Context var1) throws Exception {
   }

   public void startMultiToggle(Value[] var1, Context var2) throws Exception {
   }

   public void endMultiToggle(Context var1) throws Exception {
   }

   public void startMultiProperties(Value[] var1, Context var2) throws Exception {
   }

   public void endMultiProperties(Context var1) throws Exception {
   }

   public void startMultiPropertySet(Value[] var1, Context var2) throws Exception {
   }

   public void endMultiPropertySet(Context var1) throws Exception {
   }

   public void startFloat(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(9);
   }

   public void endFloat(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startFootnote(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(11);
   }

   public void endFootnote(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startFootnoteBody(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(12);
      if (!this.state.skipContent) {
         this.state.footnote = new Footnote();
         this.state.referenceWidth = this.section.contentWidth;
         this.state.paragraph = this.paragraph;
         this.state.bookmarks = this.bookmarks;
         this.paragraph = null;
         this.bookmarks = null;
      }

   }

   public void endFootnoteBody(Context var1) throws Exception {
      if (!this.state.skipContent) {
         this.footnotes.add(this.state.footnote);
         this.paragraph = this.state.paragraph;
         this.bookmarks = this.state.bookmarks;
         if (this.paragraph != null) {
            this.paragraph.addFootnote(this.state.footnote.id);
         }
      }

      this.state = this.state.restore();
   }

   public void startWrapper(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(55);
      if (!this.state.skipContent && var1[125] != null) {
         this.addBookmark(var1[125].id(), var2);
      }

   }

   public void endWrapper(Context var1) throws Exception {
      if (!this.state.skipContent && this.bookmarks != null && this.paragraph != null) {
         if (this.paragraph.bookmarks != null) {
            for(int var2 = 0; var2 < this.paragraph.bookmarks.length; ++var2) {
               this.bookmarks.insertElementAt(this.paragraph.bookmarks[var2], var2);
            }
         }

         this.paragraph.bookmarks = this.bookmarks();
         this.bookmarks = null;
      }

      this.state = this.state.restore();
   }

   public void startMarker(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(23);
   }

   public void endMarker(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startRetrieveMarker(Value[] var1, Context var2) throws Exception {
   }

   public void endRetrieveMarker(Context var1) throws Exception {
   }

   public boolean supportsExtension(String var1) {
      return var1.equals("http://www.xmlmind.com/foconverter/xsl/extensions/docx/sdt");
   }

   public void startForeignObject(String var1, String var2, Attributes var3, Context var4) throws Exception {
      if (var1.equals("http://www.xmlmind.com/foconverter/xsl/extensions/docx/sdt")) {
         this.startSdtElement(var2, var3, var4);
      }

   }

   public void endForeignObject(String var1, String var2, Context var3) throws Exception {
      if (var1.equals("http://www.xmlmind.com/foconverter/xsl/extensions/docx/sdt")) {
         this.endSdtElement(var2, var3);
      }

   }

   public void startSdtElement(String var1, Attributes var2, Context var3) throws Exception {
      int var4 = sdtType(var1);
      RunProperties var5 = this.runProperties(var3);
      this.sdtExtension = true;
      this.sdtStack.addElement(new Integer(var4));
      if (var4 < 0) {
         this.warning("unknown element \"" + var1 + "\"");
      } else {
         switch (var4) {
            case 1:
               this.sdtElement = new SdtTextField(var2, var5);
               break;
            case 2:
               this.sdtElement = new SdtComboBox(var2, var5, false);
               break;
            case 3:
               if (this.sdtElement != null && this.sdtElement instanceof SdtComboBox) {
                  SdtComboBox var8 = (SdtComboBox)this.sdtElement;
                  var8.addEntry(var2);
               }
               break;
            case 4:
               this.sdtElement = new SdtComboBox(var2, var5, true);
               break;
            case 5:
               this.sdtElement = new SdtDate(var2, var5);
               break;
            case 6:
               this.sdtElement = new SdtPicture(var2, var5);
               this.sdtImageData = null;
               break;
            case 7:
               String var6 = var2.getValue("", "format");
               if (var6 == null) {
                  this.warning("attribute \"format\" required for element \"" + var1 + "\"");
               } else {
                  this.sdtImageData = new SdtImageData();
                  this.sdtImageFormat = var6;
               }
               break;
            case 8:
               String var7 = var2.getValue("", "custom-xml-template");
               if (var7 != null) {
                  this.customXmlTemplate = this.resolve(var7);
                  if (this.customXmlTemplate == null) {
                     throw new Exception("cannot resolve URI \"" + var7 + "\"");
                  }

                  this.customXmlProperties = new CustomXmlProperties();
               }

               this.prefixMappings = var2.getValue("", "prefix-mappings");
         }

         switch (var4) {
            case 1:
            case 2:
            case 4:
            case 5:
            case 6:
               this.sdtBinding = var2.getValue("", "binding");
               if (this.sdtBinding != null && this.customXmlTemplate == null) {
                  if (!checkElementName(this.sdtBinding)) {
                     this.warning("bad attribute value: binding=\"" + this.sdtBinding + "\"");
                     this.sdtBinding = null;
                  } else if (this.customXml == null) {
                     this.customXml = new CustomXml();
                     this.customXmlProperties = this.customXml.properties();
                  }
               }

               if (this.paragraph == null) {
                  this.startParagraph(var3);
               }

               this.paragraph.add(this.sdtElement);
            case 3:
            default:
         }
      }
   }

   public void endSdtElement(String var1, Context var2) throws Exception {
      int var3 = sdtType(var1);
      if (var3 == 6) {
         SdtPicture var4 = (SdtPicture)this.sdtElement;
         File var5 = null;
         String var6 = this.sdtImageFormat;
         if (this.sdtImageData != null) {
            var5 = TempFile.create("xfc", (String)null);
            this.tmpFiles.addElement(var5);

            try {
               this.sdtImageData.write(var5);
            } catch (Exception var11) {
               this.warning("failed to write image file: " + var11.getMessage());
               var5 = null;
            }

            if (var5 != null) {
               TmpGraphicEnv var7 = new TmpGraphicEnv(this.tmpFiles, this.errorHandler);

               try {
                  Graphic var8 = GraphicFactories.createGraphic(URLUtil.fileToLocation(var5), var6, (Object)null, var7);
                  var4.setGeometry(GraphicLayout.intrinsicWidth(var8, this.imageResolution), GraphicLayout.intrinsicHeight(var8, this.imageResolution));
               } catch (Exception var10) {
                  this.warning("failed to load image: " + var10.getMessage());
                  var5 = null;
               }
            }

            if (var5 != null) {
               var4.setInitialValue(this.sdtImageData.data());
            }

            this.sdtImageData = null;
            this.sdtImageFormat = null;
         }

         if (var5 == null) {
            if (this.sdtDefaultImage == null) {
               this.sdtDefaultImage = TempFile.create("xfc", (String)null);
               this.tmpFiles.addElement(this.sdtDefaultImage);
               SdtDefaultImage.write(this.sdtDefaultImage);
            }

            var5 = this.sdtDefaultImage;
            var6 = SdtDefaultImage.format();
         }

         int var13 = this.sdtPictureId;
         this.sdtPictureId += 2;
         String var14 = this.images.add(var5.getPath(), var6);
         String var9 = this.docRelationships.add("http://schemas.openxmlformats.org/officeDocument/2006/relationships/image", var14);
         var4.setImageData(var13, var9);
      }

      switch (var3) {
         case 1:
         case 2:
         case 4:
         case 5:
         case 6:
            if (this.sdtBinding != null) {
               SdtDataBinding var12;
               if (this.customXmlTemplate != null) {
                  var12 = new SdtDataBinding(this.customXmlProperties.id(), this.sdtBinding, this.prefixMappings);
               } else {
                  var12 = this.customXml.add(this.sdtBinding, this.sdtElement.initialValue(), this.sdtElement.preserveSpace());
               }

               this.sdtElement.setBinding(var12);
            }

            this.sdtElement = null;
         case 3:
         default:
            this.sdtStack.removeElementAt(this.sdtStack.size() - 1);
            if (this.sdtStack.size() == 0) {
               this.sdtExtension = false;
            }

      }
   }

   private void sdtCharacters(String var1) {
      Integer var2 = (Integer)this.sdtStack.lastElement();
      int var3 = var2;
      switch (var3) {
         case 7:
            if (this.sdtImageData != null) {
               this.sdtImageData.append(var1);
            }
         default:
      }
   }

   private static int sdtType(String var0) {
      Integer var1 = (Integer)SDT_LOOKUP.get(var0);
      return var1 != null ? var1 : -1;
   }

   private static boolean checkElementName(String var0) {
      char[] var1 = var0.toCharArray();
      if (!Character.isLetter(var1[0]) && var1[0] != '_') {
         return false;
      } else {
         for(int var2 = 1; var2 < var1.length; ++var2) {
            if (!Character.isLetterOrDigit(var1[var2]) && var1[var2] != '.' && var1[var2] != '-' && var1[var2] != '_') {
               return false;
            }
         }

         return true;
      }
   }

   static {
      SDT_LOOKUP.put("text-field", new Integer(1));
      SDT_LOOKUP.put("drop-down-list", new Integer(2));
      SDT_LOOKUP.put("list-entry", new Integer(3));
      SDT_LOOKUP.put("combo-box", new Integer(4));
      SDT_LOOKUP.put("date", new Integer(5));
      SDT_LOOKUP.put("picture", new Integer(6));
      SDT_LOOKUP.put("image-data", new Integer(7));
      SDT_LOOKUP.put("configuration", new Integer(8));
   }
}
