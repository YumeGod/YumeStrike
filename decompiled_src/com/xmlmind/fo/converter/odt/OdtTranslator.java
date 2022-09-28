package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.converter.Translator;
import com.xmlmind.fo.font.GenericFamilies;
import com.xmlmind.fo.graphic.Graphic;
import com.xmlmind.fo.graphic.GraphicEnv;
import com.xmlmind.fo.objects.Flow;
import com.xmlmind.fo.objects.PageMasterReference;
import com.xmlmind.fo.objects.PageSequence;
import com.xmlmind.fo.objects.PageSequenceMaster;
import com.xmlmind.fo.objects.SimplePageMaster;
import com.xmlmind.fo.properties.Value;
import com.xmlmind.fo.util.Encoder;
import com.xmlmind.fo.util.EncoderFactory;
import com.xmlmind.fo.util.Encoding;
import com.xmlmind.fo.util.TempFile;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Vector;

public class OdtTranslator extends Translator {
   private static final String DEFAULT_ENCODING = "UTF8";
   private FontTable fontTable;
   private StyleTable styleTable;
   private OdtEntry manifestFile;
   private OdtEntry stylesFile;
   private OdtEntry contentFile;
   private Vector fileEntries;
   private Vector tmpFiles;
   private boolean prescaleImages;
   private int imageResolution;
   private int imageRendererResolution;
   private boolean alwaysSaveAsPNG;
   private int imageId;
   private String encoding;
   private Encoder encoder;
   private PrintWriter contentFileWriter;
   private SimplePageMaster oddPageMaster;
   private SimplePageMaster evenPageMaster;
   private SimplePageMaster firstPageMaster;
   private PageLayout oddPageLayout;
   private PageLayout evenPageLayout;
   private PageLayout firstPageLayout;
   private MasterPage oddMasterPage;
   private MasterPage evenMasterPage;
   private MasterPage firstMasterPage;
   private String masterPageName;
   private boolean flowStart;
   private State state;
   private Paragraph paragraph;
   private Vector bookmarks;
   private StaticContent staticContent;

   public static String[] listEncodings() {
      String[] var0 = Encoding.list();
      Vector var1 = new Vector();

      int var2;
      for(var2 = 0; var2 < var0.length; ++var2) {
         String var3 = Encoding.officialName(var0[var2]);
         if (var3 != null && !var3.equals("ISO-2022-CN") && !var3.equals("x-JISAutoDetect")) {
            var1.addElement(var3);
         }
      }

      var0 = new String[var1.size()];

      for(var2 = 0; var2 < var0.length; ++var2) {
         var0[var2] = (String)var1.elementAt(var2);
      }

      return var0;
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

   public String unaliasFontFamily(String var1) {
      return this.fontTable.unaliasFontFamily(var1);
   }

   private void clean() throws Exception {
      if (this.contentFileWriter != null) {
         this.contentFileWriter.close();
         this.contentFileWriter = null;
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
      this.fontTable = new FontTable();
      GenericFamilies var1 = this.genericFontFamilies("DejaVu Serif", "DejaVu Sans", "DejaVu Sans Mono");
      this.fontTable.add(new FontFace(var1.serif, "roman"));
      this.fontTable.aliasFontFamily("serif", var1.serif);
      this.fontTable.add(new FontFace(var1.sansSerif, "swiss"));
      this.fontTable.aliasFontFamily("sans-serif", var1.sansSerif);
      this.fontTable.add(new FontFace(var1.monospace, "modern"));
      this.fontTable.aliasFontFamily("monospace", var1.monospace);
      if (var1.fantasy != null) {
         this.fontTable.add(new FontFace(var1.fantasy, "decorative"));
         this.fontTable.aliasFontFamily("fantasy", var1.fantasy);
      }

      if (var1.cursive != null) {
         this.fontTable.add(new FontFace(var1.cursive, "script"));
         this.fontTable.aliasFontFamily("cursive", var1.cursive);
      }

      this.styleTable = new StyleTable(this.fontTable);
      this.manifestFile = new OdtEntry("META-INF/manifest.xml", "text/xml");
      this.stylesFile = new OdtEntry("styles.xml", "text/xml");
      this.contentFile = new OdtEntry("content.xml", "text/xml", new String[2]);
      this.fileEntries = new Vector();
      this.fileEntries.addElement(this.manifestFile);
      this.fileEntries.addElement(this.stylesFile);
      this.fileEntries.addElement(this.contentFile);
      this.fileEntries.addElement(new OdtEntry("Pictures/", ""));
      this.tmpFiles = new Vector();
      this.prescaleImages = this.prescaleImages();
      this.imageResolution = this.imageResolution();
      this.imageRendererResolution = this.imageRendererResolution();
      this.alwaysSaveAsPNG = this.alwaysSaveAsPNG();
      this.imageId = 1;
      this.encoding = this.properties.getProperty("outputEncoding");
      if (this.encoding == null) {
         this.encoding = "UTF8";
      }

      if (Encoding.internalName(this.encoding) == null) {
         throw new Exception("unsupported encoding \"" + this.encoding + "\"");
      } else {
         this.encoding = Encoding.internalName(this.encoding);
         String var2 = Encoding.officialName(this.encoding);
         if (!var2.startsWith("UTF")) {
            this.encoder = EncoderFactory.newEncoder(var2);
         } else {
            this.encoder = null;
         }

         File var3 = TempFile.create("xfc", (String)null);
         this.tmpFiles.addElement(var3);
         this.contentFile.paths[1] = var3.getPath();
         FileOutputStream var4 = new FileOutputStream(this.contentFile.paths[1]);
         OutputStreamWriter var5 = new OutputStreamWriter(new BufferedOutputStream(var4), this.encoding);
         this.contentFileWriter = new PrintWriter(new BufferedWriter(var5));
         this.contentFileWriter.println("<office:body>");
         this.contentFileWriter.println("<office:text>");
      }
   }

   public void endDocument() throws Exception {
      this.contentFileWriter.println("</office:text>");
      this.contentFileWriter.println("</office:body>");
      this.contentFileWriter.println("</office:document-content>");
      this.contentFileWriter.flush();
      this.contentFileWriter.close();
      this.contentFileWriter = null;
      File var1 = TempFile.create("xfc", (String)null);
      this.tmpFiles.addElement(var1);
      this.contentFile.paths[0] = var1.getPath();
      FileOutputStream var3 = new FileOutputStream(this.contentFile.paths[0]);
      OutputStreamWriter var4 = new OutputStreamWriter(new BufferedOutputStream(var3), this.encoding);
      this.contentFileWriter = new PrintWriter(new BufferedWriter(var4));
      this.contentFileWriter.println("<?xml version=\"1.0\" encoding=\"" + Encoding.officialName(this.encoding) + "\"?>");
      this.contentFileWriter.print("<office:document-content");
      this.contentFileWriter.print(" xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\"");
      this.contentFileWriter.print(" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\"");
      this.contentFileWriter.print(" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\"");
      this.contentFileWriter.print(" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\"");
      this.contentFileWriter.print(" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\"");
      this.contentFileWriter.print(" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\"");
      this.contentFileWriter.print(" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\"");
      this.contentFileWriter.print(" xmlns:xlink=\"http://www.w3.org/1999/xlink\"");
      this.contentFileWriter.print(" office:version=\"1.0\"");
      this.contentFileWriter.println(">");
      this.styleTable.print(this.contentFileWriter);
      this.contentFileWriter.flush();
      this.contentFileWriter.close();
      this.contentFileWriter = null;
      var1 = TempFile.create("xfc", (String)null);
      this.tmpFiles.addElement(var1);
      String var2 = var1.getPath();
      this.stylesFile.setPath(var2);
      this.styleTable.write(var2, this.encoding);
      Manifest var5 = new Manifest();
      int var6 = 1;

      int var7;
      for(var7 = this.fileEntries.size(); var6 < var7; ++var6) {
         OdtEntry var8 = (OdtEntry)this.fileEntries.elementAt(var6);
         var5.add(var8.name, var8.type);
      }

      var1 = TempFile.create("xfc", (String)null);
      this.tmpFiles.addElement(var1);
      var2 = var1.getPath();
      this.manifestFile.setPath(var2);
      var5.write(var2, this.encoding);
      OdtFile var11 = new OdtFile();
      var7 = 0;

      for(int var13 = this.fileEntries.size(); var7 < var13; ++var7) {
         OdtEntry var9 = (OdtEntry)this.fileEntries.elementAt(var7);
         var11.add(var9);
      }

      if (this.output != null) {
         Object var10 = this.output.getByteStream();
         boolean var12 = false;
         if (var10 == null) {
            String var14 = this.output.getFileName();
            if (var14 != null) {
               var10 = new FileOutputStream(var14);
               var12 = true;
            } else {
               var10 = System.out;
            }
         }

         BufferedOutputStream var15 = new BufferedOutputStream((OutputStream)var10);
         var11.write(var15);
         var15.flush();
         if (var12) {
            var15.close();
         }
      }

      this.deleteTmpFiles();
   }

   public void characters(String var1, Context var2) throws Exception {
      if (!this.state.skipContent) {
         if (this.state.textStyle == null) {
            this.state.textStyle = this.styleTable.add(new TextStyle(var2));
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
               this.paragraph.addBreak(this.state.textStyle);
            } else {
               Text var6 = new Text(var5, this.state.textStyle, var3);
               this.paragraph.add(var6);
            }
         }

      }
   }

   private void startParagraph(Context var1) {
      this.paragraph = new Paragraph(new ParagraphStyle(var1));
      if (this.bookmarks != null && this.state.context != 2) {
         this.paragraph.bookmarks = this.bookmarks;
         this.bookmarks = null;
      }

   }

   private void endParagraph() throws Exception {
      int var1 = this.state.context;
      double var2 = this.state.referenceWidth;
      if (this.paragraph.isEmpty()) {
         this.paragraph = null;
      } else {
         if (this.flowStart) {
            this.paragraph.style.masterPageName = this.masterPageName;
            this.flowStart = false;
         }

         switch (var1) {
            case 2:
               this.state.listItem.setLabel(this.paragraph);
               break;
            case 3:
               this.state.listItem.add(this.paragraph);
               break;
            case 4:
               if (!this.paragraph.requiresLayout()) {
                  this.paragraph.style = this.styleTable.add(this.paragraph.style);
               }

               this.state.table.add(this.paragraph);
               break;
            case 5:
               if (this.paragraph.requiresLayout()) {
                  this.paragraph.layout(var2);
               }

               this.paragraph.style = this.styleTable.add(this.paragraph.style);
               this.state.tableAndCaption.caption.add(this.paragraph);
               break;
            case 6:
            default:
               if (this.paragraph.requiresLayout()) {
                  this.paragraph.layout(var2);
               }

               this.paragraph.style = this.styleTable.add(this.paragraph.style);
               this.paragraph.print(this.contentFileWriter, this.encoder);
               break;
            case 7:
               if (this.paragraph.requiresLayout()) {
                  this.paragraph.layout(var2);
               }

               this.paragraph.style = this.styleTable.add(this.paragraph.style);
               this.state.footnote.add(this.paragraph);
               break;
            case 8:
               if (this.paragraph.requiresLayout()) {
                  this.paragraph.layout(var2);
               }

               this.paragraph.style = this.styleTable.add(this.paragraph.style);
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
      this.selectPageMasters(var1);
      this.oddPageLayout = new PageLayout(this.oddPageMaster);
      this.styleTable.add(this.oddPageLayout);
      this.oddMasterPage = new MasterPage(this.oddPageLayout.name);
      this.styleTable.add(this.oddMasterPage);
      this.evenPageLayout = null;
      this.firstPageLayout = null;
      this.evenMasterPage = null;
      this.firstMasterPage = null;
      if (this.evenPageMaster != null) {
         this.evenPageLayout = new PageLayout(this.evenPageMaster);
         this.styleTable.add(this.evenPageLayout);
         this.evenMasterPage = new MasterPage(this.evenPageLayout.name);
         this.styleTable.add(this.evenMasterPage);
      }

      if (this.firstPageMaster != null) {
         this.firstPageLayout = new PageLayout(this.firstPageMaster);
         this.styleTable.add(this.firstPageLayout);
         this.firstMasterPage = new MasterPage(this.firstPageLayout.name);
         this.styleTable.add(this.firstMasterPage);
      }

      if (this.firstPageLayout != null) {
         this.firstPageLayout.setPageNumbering(var1);
         this.oddPageLayout.firstPageNumber = 0;
         this.oddPageLayout.numberFormat = this.firstPageLayout.numberFormat;
      } else {
         this.oddPageLayout.setPageNumbering(var1);
      }

      if (this.evenPageLayout != null) {
         this.evenPageLayout.firstPageNumber = 0;
         this.evenPageLayout.numberFormat = this.oddPageLayout.numberFormat;
      }

      MasterPage var4;
      if (this.firstMasterPage != null) {
         var4 = this.evenMasterPage != null ? this.evenMasterPage : this.oddMasterPage;
         this.firstMasterPage.nextMasterPage = var4.name;
      }

      if (this.evenMasterPage != null) {
         this.oddMasterPage.nextMasterPage = this.evenMasterPage.name;
         this.evenMasterPage.nextMasterPage = this.oddMasterPage.name;
      }

      var4 = this.firstMasterPage != null ? this.firstMasterPage : this.oddMasterPage;
      this.masterPageName = var4.name;
      this.flowStart = false;
      this.state = new State(31);
      this.state.referenceWidth = this.oddPageLayout.contentWidth();
      this.paragraph = null;
      this.bookmarks = null;
      if (var2[125] != null) {
         this.bookmarks = new Vector();
         this.bookmarks.addElement(new Bookmark(var2[125].id()));
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
      this.state = null;
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
      if (var1.region(this.oddPageMaster) != 0) {
         this.state.skipContent = true;
      } else {
         this.flowStart = true;
         if (var2[125] != null) {
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(new Bookmark(var2[125].id()));
         }

      }
   }

   public void endFlow(Context var1) throws Exception {
      if (!this.state.skipContent && this.bookmarks != null) {
         this.paragraph = new Paragraph();
         this.paragraph.bookmarks = this.bookmarks;
         this.paragraph.print(this.contentFileWriter, this.encoder);
         this.paragraph = null;
         this.bookmarks = null;
      }

      this.state = this.state.restore();
   }

   public void startStaticContent(Flow var1, Value[] var2, Context var3) throws Exception {
      this.state = this.state.update(44);
      this.styleTable.select(1);
      this.staticContent = null;
      switch (var1.region(this.oddPageMaster)) {
         case 1:
            this.staticContent = new StaticContent();
            this.oddMasterPage.header = new Header(this.staticContent);
            break;
         case 2:
            this.staticContent = new StaticContent();
            this.oddMasterPage.footer = new Footer(this.staticContent);
      }

      if (this.evenPageMaster != null) {
         switch (var1.region(this.evenPageMaster)) {
            case 1:
               if (this.staticContent == null) {
                  this.staticContent = new StaticContent();
               }

               this.evenMasterPage.header = new Header(this.staticContent);
               break;
            case 2:
               if (this.staticContent == null) {
                  this.staticContent = new StaticContent();
               }

               this.evenMasterPage.footer = new Footer(this.staticContent);
         }
      }

      if (this.firstPageMaster != null) {
         switch (var1.region(this.firstPageMaster)) {
            case 1:
               if (this.staticContent == null) {
                  this.staticContent = new StaticContent();
               }

               this.firstMasterPage.header = new Header(this.staticContent);
               break;
            case 2:
               if (this.staticContent == null) {
                  this.staticContent = new StaticContent();
               }

               this.firstMasterPage.footer = new Footer(this.staticContent);
         }
      }

      if (this.staticContent == null) {
         this.state.skipContent = true;
      }

   }

   public void endStaticContent(Context var1) throws Exception {
      this.styleTable.select(0);
      this.state = this.state.restore();
   }

   public void startTitle(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(54);
   }

   public void endTitle(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startBlock(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(2);
      if (!this.state.skipContent) {
         if (this.paragraph != null) {
            this.endParagraph();
         }

         if (var1[125] != null) {
            String var3 = var1[125].id();
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(new Bookmark(var3));
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
            if (this.paragraph == null) {
               this.startParagraph(var4);
            }

            this.paragraph.add(new Bookmark(var3[125].id()));
         }

         OdtGraphicEnv var5 = new OdtGraphicEnv(var2, this.fileEntries, this.styleTable);
         Image var6 = new Image(this.imageId++, var1, var5, var4, this.prescaleImages, this.imageResolution, this.imageRendererResolution, this.alwaysSaveAsPNG, this.encoder);
         if (this.paragraph == null) {
            this.startParagraph(var4);
         }

         this.paragraph.add(var6);
      }

   }

   public void endExternalGraphic(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startInstreamForeignObject(Value[] var1, Context var2) throws Exception {
   }

   public void endInstreamForeignObject(Graphic var1, GraphicEnv var2, Value[] var3, Context var4) throws Exception {
      this.state = this.state.update(16);
      if (!this.state.skipContent) {
         if (var3[125] != null) {
            if (this.paragraph == null) {
               this.startParagraph(var4);
            }

            this.paragraph.add(new Bookmark(var3[125].id()));
         }

         OdtGraphicEnv var5 = new OdtGraphicEnv(var2, this.fileEntries, this.styleTable);
         Image var6 = new Image(this.imageId++, var1, var5, var4, this.prescaleImages, this.imageResolution, this.imageRendererResolution, this.alwaysSaveAsPNG, this.encoder);
         if (this.paragraph == null) {
            this.startParagraph(var4);
         }

         this.paragraph.add(var6);
      }

      this.state = this.state.restore();
   }

   public void startInline(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(14);
      if (!this.state.skipContent && var1[125] != null) {
         if (this.paragraph == null) {
            this.startParagraph(var2);
         }

         this.paragraph.add(new Bookmark(var1[125].id()));
      }

   }

   public void endInline(Context var1) throws Exception {
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
         var3.textStyle = this.styleTable.add(new TextStyle(var2));
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

         TextStyle var3 = this.styleTable.add(new TextStyle(var2));
         this.paragraph.addPageNumber(var3);
      }

   }

   public void endPageNumber(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startPageNumberCitation(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(30);
      if (!this.state.skipContent && var1[226] != null) {
         String var3 = var1[226].idref();
         BookmarkReference var4 = BookmarkReference.page(var3);
         var4.style = this.styleTable.add(new TextStyle(var2));
         if (this.paragraph == null) {
            this.startParagraph(var2);
         }

         this.paragraph.add(var4);
      }

   }

   public void endPageNumberCitation(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startTableAndCaption(Value[] var1, Context var2) throws Exception {
      if (this.paragraph != null) {
         this.endParagraph();
      }

      this.state = this.state.update(46);
      if (!this.state.skipContent) {
         this.state.tableAndCaption = new TableAndCaption(var2);
         if (var1[125] != null) {
            String var3 = var1[125].id();
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(new Bookmark(var3));
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
            case 5:
            case 6:
            default:
               var2.layout(this.state.referenceWidth, this.styleTable);
               var2.print(this.contentFileWriter, this.encoder);
               break;
            case 7:
               var2.layout(this.state.referenceWidth, this.styleTable);
               this.state.footnote.add(var2);
               break;
            case 8:
               var2.layout(this.state.referenceWidth, this.styleTable);
               this.staticContent.add(var2);
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
         if (this.flowStart) {
            this.state.table.setMasterPageName(this.masterPageName);
            this.flowStart = false;
         }

         if (var2.parent().fo == 46) {
            this.state.tableAndCaption.table = this.state.table;
         }

         if (var1[125] != null) {
            String var3 = var1[125].id();
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(new Bookmark(var3));
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
            case 5:
            case 6:
            default:
               var2.layout(this.state.referenceWidth, this.styleTable);
               var2.print(this.contentFileWriter, this.encoder);
               break;
            case 7:
               var2.layout(this.state.referenceWidth, this.styleTable);
               this.state.footnote.add(var2);
               break;
            case 8:
               var2.layout(this.state.referenceWidth, this.styleTable);
               this.staticContent.add(var2);
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
            String var3 = var1[125].id();
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(new Bookmark(var3));
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
            String var3 = var1[125].id();
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(new Bookmark(var3));
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
            String var3 = var1[125].id();
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(new Bookmark(var3));
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
         this.state.table.startRow(var2);
         if (var1[125] != null) {
            String var3 = var1[125].id();
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(new Bookmark(var3));
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
         var2.background = null;
         if (var1[125] != null) {
            String var3 = var1[125].id();
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(new Bookmark(var3));
         }
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
         this.state.list = new List(this.state.listLevel, var2);
         if (var1[125] != null) {
            String var3 = var1[125].id();
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(new Bookmark(var3));
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
               var2.layout(this.state.referenceWidth, this.styleTable);
               var2.print(this.contentFileWriter, this.encoder);
               break;
            case 7:
               var2.layout(1, this.state.referenceWidth, this.styleTable);
               this.state.footnote.add(var2);
               break;
            case 8:
               var2.layout(this.state.referenceWidth, this.styleTable);
               this.staticContent.add(var2);
         }
      }

   }

   public void startListItem(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(20);
      if (!this.state.skipContent) {
         this.state.listItem = new ListItem(var2);
         if (var1[125] != null) {
            String var3 = var1[125].id();
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(new Bookmark(var3));
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
         String var3 = var1[125].id();
         if (this.bookmarks == null) {
            this.bookmarks = new Vector();
         }

         this.bookmarks.addElement(new Bookmark(var3));
      }

   }

   public void endListItemBody(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startListItemLabel(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(22);
      if (!this.state.skipContent && var1[125] != null) {
         String var3 = var1[125].id();
         if (this.bookmarks == null) {
            this.bookmarks = new Vector();
         }

         this.bookmarks.addElement(new Bookmark(var3));
      }

   }

   public void endListItemLabel(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startBasicLink(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(0);
      if (!this.state.skipContent) {
         if (var1[125] != null) {
            if (this.paragraph == null) {
               this.startParagraph(var2);
            }

            this.paragraph.add(new Bookmark(var1[125].id()));
         }

         this.state.link = new Link(var2);
         if (this.paragraph == null) {
            this.startParagraph(var2);
         }

         this.paragraph.start(this.state.link);
      }

   }

   public void endBasicLink(Context var1) throws Exception {
      if (!this.state.skipContent) {
         this.paragraph.end(this.state.link);
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
      if (!this.state.skipContent) {
         this.state.footnote = new Footnote();
         this.state.paragraph = this.paragraph;
         this.state.bookmarks = this.bookmarks;
         this.paragraph = new Paragraph();
         this.bookmarks = null;
      }

   }

   public void endFootnote(Context var1) throws Exception {
      if (!this.state.skipContent) {
         this.paragraph = this.state.paragraph;
         this.bookmarks = this.state.bookmarks;
         if (this.paragraph == null) {
            this.startParagraph(var1);
         }

         this.paragraph.add(this.state.footnote);
      }

      this.state = this.state.restore();
   }

   public void startFootnoteBody(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(12);
      if (!this.state.skipContent) {
         this.state.footnote.setLabel(this.paragraph);
         this.paragraph = null;
         this.state.referenceWidth = this.oddPageLayout.contentWidth();
      }

   }

   public void endFootnoteBody(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startWrapper(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(55);
      if (!this.state.skipContent && var1[125] != null) {
         Bookmark var3 = new Bookmark(var1[125].id());
         if (this.paragraph != null && this.state.context != 2) {
            this.paragraph.add(var3);
         } else {
            if (this.bookmarks == null) {
               this.bookmarks = new Vector();
            }

            this.bookmarks.addElement(var3);
         }
      }

   }

   public void endWrapper(Context var1) throws Exception {
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
}
