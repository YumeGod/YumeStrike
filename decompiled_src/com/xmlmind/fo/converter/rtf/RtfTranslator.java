package com.xmlmind.fo.converter.rtf;

import com.xmlmind.fo.converter.Context;
import com.xmlmind.fo.converter.MsTranslator;
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
import com.xmlmind.fo.util.TempFile;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.StringTokenizer;
import java.util.Vector;

public class RtfTranslator extends MsTranslator {
   public static final int TARGET_NONE = 0;
   public static final int TARGET_MSWORD = 1;
   private static final int DEFAULT_PAGE_WIDTH = Rtf.toTwips(21.0, 2);
   private static final int DEFAULT_PAGE_HEIGHT = Rtf.toTwips(29.7, 2);
   private static final String DEFAULT_ENCODING = "Cp1252";
   private static final Encoding[] encodings = new Encoding[]{new Encoding("ASCII", 1252, 0), new Encoding("Cp1250", 1250, 238), new Encoding("Cp1251", 1251, 204), new Encoding("Cp1252", 1252, 0), new Encoding("ISO8859_1", 1252, 0)};
   private static final ColorTable.Color[] colors = new ColorTable.Color[]{new ColorTable.Color("aqua", 127, 255, 212), new ColorTable.Color("black", 0, 0, 0), new ColorTable.Color("blue", 0, 0, 255), new ColorTable.Color("fuchsia", 255, 0, 255), new ColorTable.Color("gray", 190, 190, 190), new ColorTable.Color("green", 0, 255, 0), new ColorTable.Color("lime", 50, 205, 50), new ColorTable.Color("maroon", 176, 48, 96), new ColorTable.Color("navy", 0, 0, 128), new ColorTable.Color("olive", 85, 107, 47), new ColorTable.Color("purple", 160, 32, 240), new ColorTable.Color("red", 255, 0, 0), new ColorTable.Color("silver", 192, 192, 192), new ColorTable.Color("teal", 0, 128, 128), new ColorTable.Color("white", 255, 255, 255), new ColorTable.Color("yellow", 255, 255, 0)};
   private Encoding encoding;
   private Encoder encoder;
   private File tmpFile;
   private PrintWriter tmpWriter;
   private PrintWriter outputWriter;
   private boolean closeOutputWriter;
   private int target;
   private int symbolFont;
   private SimplePageMaster oddPageMaster;
   private SimplePageMaster evenPageMaster;
   private SimplePageMaster firstPageMaster;
   private FontTable fontTable;
   private ColorTable colorTable;
   private ListTable listTable;
   private boolean singleSided;
   private boolean mirrorMargins;
   private boolean prescaleImages;
   private int imageResolution;
   private int imageRendererResolution;
   private boolean alwaysSaveAsPNG;
   private State state;
   private Section section;
   private StaticContent staticContent;
   private Paragraph paragraph;
   private Vector bookmarks;

   public static String[] listEncodings() {
      String[] var0 = new String[]{"ASCII", "Cp1250", "Cp1251", "Cp1252"};
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
      return this.state != null ? (double)this.state.referenceWidth / 20.0 : 0.0;
   }

   private void clean() throws Exception {
      if (this.tmpWriter != null) {
         this.tmpWriter.close();
         this.tmpWriter = null;
      }

      if (this.tmpFile != null) {
         this.tmpFile.delete();
         this.tmpFile = null;
      }

   }

   protected void finalize() throws Throwable {
      this.clean();
   }

   public void startDocument() throws Exception {
      this.setEncoding();
      this.encoder = EncoderFactory.newEncoder(this.encoding.name);
      this.tmpFile = TempFile.create("xfc", (String)null);
      BufferedOutputStream var1 = new BufferedOutputStream(new FileOutputStream(this.tmpFile));
      OutputStreamWriter var2 = new OutputStreamWriter(var1, this.encoding.name);
      this.tmpWriter = new PrintWriter(new BufferedWriter(var2));
      this.target = 0;
      String var3 = this.properties.getProperty("rtf.target");
      if (var3 != null && var3.length() > 0) {
         if (var3.equals("MSWord")) {
            this.target = 1;
         } else {
            this.warning("unknown target \"" + var3 + "\"");
         }
      }

      this.fontTable = new FontTable(this.encoding.charSet);
      GenericFamilies var4 = this.genericFontFamilies((String)null, (String)null, (String)null);
      this.fontTable.add(new FontTable.Font("roman", 0, var4.serif, new String[]{"serif"}));
      this.fontTable.add(new FontTable.Font("swiss", 0, var4.sansSerif, new String[]{"sans-serif"}));
      this.fontTable.add(new FontTable.Font("modern", 0, var4.monospace, new String[]{"monospace"}));
      if (var4.fantasy != null) {
         this.fontTable.add(new FontTable.Font("decor", 0, var4.fantasy, new String[]{"fantasy"}));
      }

      if (var4.cursive != null) {
         this.fontTable.add(new FontTable.Font("script", 0, var4.cursive, new String[]{"cursive"}));
      }

      this.fontTable.add(new FontTable.Font("tech", 2, "Symbol", (String[])null));
      this.symbolFont = this.fontTable.index("Symbol", 2);
      this.colorTable = new ColorTable();
      this.colorTable.add(colors);
      this.listTable = new ListTable();
      this.singleSided = this.singleSided();
      this.mirrorMargins = true;
      this.prescaleImages = this.prescaleImages();
      this.imageResolution = this.imageResolution();
      this.imageRendererResolution = this.imageRendererResolution();
      this.alwaysSaveAsPNG = this.alwaysSaveAsPNG();
      this.section = null;
      this.paragraph = null;
      this.bookmarks = null;
   }

   private void setEncoding() throws Exception {
      String var1 = null;
      if (this.output != null) {
         var1 = this.output.getEncoding();
      }

      if (var1 == null) {
         var1 = this.properties.getProperty("outputEncoding");
         if (var1 == null) {
            var1 = "Cp1252";
         }
      }

      this.encoding = null;

      for(int var2 = 0; var2 < encodings.length; ++var2) {
         if (var1.equals(encodings[var2].name)) {
            this.encoding = encodings[var2];
            break;
         }
      }

      if (this.encoding == null) {
         throw new Exception("unsupported encoding \"" + var1 + "\"");
      }
   }

   public void endDocument() throws Exception {
      this.tmpWriter.flush();
      this.tmpWriter.close();
      this.tmpWriter = null;
      if (this.output != null) {
         this.setOutputWriter();
         this.writeDocument();
         this.outputWriter.flush();
         if (this.closeOutputWriter) {
            this.outputWriter.close();
         }

         this.outputWriter = null;
      }

      this.tmpFile.delete();
      this.tmpFile = null;
   }

   private void setOutputWriter() throws Exception {
      Object var1 = this.output.getCharacterStream();
      this.closeOutputWriter = false;
      if (var1 == null) {
         Object var2 = this.output.getByteStream();
         if (var2 == null) {
            String var3 = this.output.getFileName();
            if (var3 != null) {
               var2 = new FileOutputStream(var3);
               this.closeOutputWriter = true;
            } else {
               var2 = System.out;
            }
         }

         BufferedOutputStream var4 = new BufferedOutputStream((OutputStream)var2);
         var1 = new OutputStreamWriter(var4, this.encoding.name);
      }

      this.outputWriter = new PrintWriter(new BufferedWriter((Writer)var1));
   }

   private void writeDocument() throws Exception {
      this.outputWriter.print("{");
      this.writeHeader();
      this.writeInformationGroup();
      this.writeDocumentProperties();
      this.writeDocumentBody();
      this.outputWriter.println("}");
   }

   private void writeHeader() {
      this.outputWriter.print("\\rtf1");
      this.outputWriter.print("\\ansi\\ansicpg" + this.encoding.codePage);
      this.outputWriter.println("\\deff0");
      this.fontTable.print(this.outputWriter);
      this.colorTable.print(this.outputWriter);
      this.listTable.print(this.outputWriter);
   }

   private void writeInformationGroup() {
      this.outputWriter.println("{\\info");
      this.outputWriter.println("{\\*\\userprops");
      this.outputWriter.println("{\\propname creator}\\proptype30");
      this.outputWriter.println("{\\staticval " + Rtf.escape("XMLmind XSL-FO Converter Professional Edition 4.7.0") + "}");
      this.outputWriter.println("}");
      this.outputWriter.println("}");
   }

   private void writeDocumentProperties() {
      if (!this.singleSided) {
         this.outputWriter.print("\\facingp");
         if (this.mirrorMargins) {
            this.outputWriter.print("\\margmirror");
         }
      }

      this.outputWriter.println("\\fet0\\ftnbj");
   }

   private void writeDocumentBody() throws Exception {
      BufferedInputStream var1 = new BufferedInputStream(new FileInputStream(this.tmpFile));
      BufferedReader var2 = new BufferedReader(new InputStreamReader(var1, this.encoding.name));

      while(true) {
         char[] var3 = new char[1024];
         int var4 = var2.read(var3, 0, var3.length);
         if (var4 < 0) {
            var2.close();
            return;
         }

         if (var4 > 0) {
            this.outputWriter.write(var3, 0, var4);
         }
      }
   }

   public void characters(String var1, Context var2) throws Exception {
      if (!this.state.skip) {
         if (this.paragraph == null) {
            this.startParagraph(var2);
         }

         if (this.state.runProperties == null) {
            this.state.runProperties = this.runProperties(var2);
         }

         StringTokenizer var3 = new StringTokenizer(var1, "\n", true);

         while(var3.hasMoreTokens()) {
            String var4 = var3.nextToken();
            if (var4.equals("\n")) {
               Break var5 = new Break("line", this.state.runProperties);
               this.paragraph.add(var5);
            } else {
               Text var6 = new Text(var4, this.state.runProperties);
               this.paragraph.add(var6);
            }
         }

      }
   }

   private RunProperties runProperties(Context var1) {
      return new RunProperties(var1, this.fontTable, this.colorTable);
   }

   private void startParagraph(Context var1) {
      this.paragraph = new Paragraph(new ParProperties(var1, this.colorTable));
      int var2;
      if (this.bookmarks != null && this.state.context != 2) {
         this.paragraph.bookmarks = new Bookmark[this.bookmarks.size()];

         for(var2 = 0; var2 < this.paragraph.bookmarks.length; ++var2) {
            this.paragraph.bookmarks[var2] = (Bookmark)this.bookmarks.elementAt(var2);
         }

         this.bookmarks = null;
      }

      if (this.state.context == 3 && this.state.list.item != null) {
         List.Item.Label var4 = this.state.list.item.label;
         ParProperties var5 = this.paragraph.properties;
         this.paragraph.startsListItem = true;
         var5.breakBefore = this.state.list.item.breakBefore;
         var5.spaceBefore = this.state.list.item.spaceBefore;
         if (var5.alignment == 1 || var5.alignment == 2) {
            var5.alignment = 0;
         }

         var2 = var5.startIndent;
         int var3 = var4.startIndent - var2;
         if (!this.state.list.plainText && var4.alignment != 0) {
            int var6 = var2 - var4.startIndent - this.state.list.labelSeparation;
            switch (var4.alignment) {
               case 1:
                  var3 += var6 / 2;
                  break;
               case 2:
                  var3 += var6;
            }
         }

         var5.firstLineIndent = var3;
         if (var2 > 0) {
            var5.addTab(new ParProperties.Tab(var2));
         }

         if (this.state.list.plainText) {
            this.paragraph.add(var4.runs);
            if (var2 > 0) {
               Tab var7 = new Tab(var4.properties);
               var7.isSeparator = true;
               this.paragraph.add(var7);
            }
         } else {
            var5.listId = this.state.list.id;
            if (var2 > 0) {
               var5.listText = new Text(var4.text, var4.properties);
            }
         }

         this.state.list.item = null;
      }

   }

   private void endParagraph() throws Exception {
      int var1 = this.state.context;
      if (var1 == 3) {
         var1 = this.state.parentContext();
      }

      if (this.paragraph.isVoid()) {
         this.paragraph = null;
      } else {
         if (this.paragraph.hasTabs()) {
            this.setTabs();
         }

         if (this.paragraph.hasPicture && var1 != 4) {
            this.paragraph.layout(this.state.referenceWidth);
         }

         switch (var1) {
            case 2:
               if (this.state.list.item == null) {
                  this.state.list.item = new List.Item(this.paragraph);
               }
               break;
            case 3:
            default:
               this.paragraph.print(this.tmpWriter, this.encoder);
               break;
            case 4:
               this.state.table.add(this.paragraph);
               break;
            case 5:
               this.state.tableAndCaption.caption.add(this.paragraph);
               break;
            case 6:
               this.state.footnote.add(this.paragraph);
               break;
            case 7:
               this.staticContent.add(this.paragraph);
         }

         this.paragraph = null;
      }
   }

   private void setTabs() {
      int var3 = this.state.referenceWidth;
      int var4 = this.paragraph.properties.startIndent;
      ParProperties.Tab[] var5 = this.paragraph.properties.tabs();

      int var1;
      for(var1 = 0; var1 < var5.length; ++var1) {
         if (var5[var1].position < 0) {
            var5[var1].position += var3;
         }
      }

      for(var1 = 0; var1 < var5.length; ++var1) {
         if (var5[var1].position != 0) {
            var4 = var5[var1].position;
         } else {
            int var6 = var3 - this.paragraph.properties.endIndent;
            int var7 = 1;

            int var2;
            for(var2 = var1 + 1; var2 < var5.length; ++var2) {
               if (var5[var2].position != 0) {
                  var6 = var5[var2].position;
                  break;
               }

               ++var7;
            }

            for(int var8 = (var6 - var4) / (var7 + 1); var1 < var2; ++var1) {
               var5[var1].position = var4 + var8;
               var4 = var5[var1].position;
            }
         }
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
      if (this.singleSided) {
         this.evenPageMaster = null;
      }

      this.section = new Section();
      this.section.pageWidth = DEFAULT_PAGE_WIDTH;
      this.section.pageHeight = DEFAULT_PAGE_HEIGHT;
      this.section.initialize(this.oddPageMaster, this.evenPageMaster, this.firstPageMaster, var1, this.colorTable);
      if (!this.section.mirrorMargins) {
         this.mirrorMargins = false;
      }

      this.state = new State(31);
      this.state.referenceWidth = this.section.contentWidth;
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
      this.section.end(this.tmpWriter);
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
      this.section.start(this.tmpWriter, this.encoder, this.target);
      this.state = this.state.update(10);
      this.state.referenceWidth = this.section.columnWidth;
      if (var1.region(this.oddPageMaster) != 0) {
         this.state.skip = true;
      } else {
         if (var2[125] != null) {
            this.addBookmark(var2[125].id(), var3);
         }

      }
   }

   public void endFlow(Context var1) throws Exception {
      if (!this.state.skip && this.bookmarks != null) {
         this.writeBookmarks();
         this.bookmarks = null;
      }

      this.state = this.state.restore();
   }

   private void writeBookmarks() {
      Bookmark[] var1 = new Bookmark[this.bookmarks.size()];

      int var2;
      for(var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = (Bookmark)this.bookmarks.elementAt(var2);
      }

      for(var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].start(this.tmpWriter, this.encoder);
      }

      for(var2 = var1.length - 1; var2 >= 0; --var2) {
         var1[var2].end(this.tmpWriter, this.encoder);
      }

   }

   public void startStaticContent(Flow var1, Value[] var2, Context var3) throws Exception {
      this.state = this.state.update(44);
      this.staticContent = null;
      switch (var1.region(this.oddPageMaster)) {
         case 1:
            this.staticContent = new StaticContent();
            if (!this.singleSided) {
               this.section.headerOdd = new Header("r", this.staticContent);
               if (this.evenPageMaster == null) {
                  this.section.headerEven = new Header("l", this.staticContent);
               }
            } else {
               this.section.headerOdd = new Header("", this.staticContent);
            }
            break;
         case 2:
            this.staticContent = new StaticContent();
            if (!this.singleSided) {
               this.section.footerOdd = new Footer("r", this.staticContent);
               if (this.evenPageMaster == null) {
                  this.section.footerEven = new Footer("l", this.staticContent);
               }
            } else {
               this.section.footerOdd = new Footer("", this.staticContent);
            }
      }

      if (this.evenPageMaster != null) {
         switch (var1.region(this.evenPageMaster)) {
            case 1:
               if (this.staticContent == null) {
                  this.staticContent = new StaticContent();
               }

               this.section.headerEven = new Header("l", this.staticContent);
               break;
            case 2:
               if (this.staticContent == null) {
                  this.staticContent = new StaticContent();
               }

               this.section.footerEven = new Footer("l", this.staticContent);
         }
      }

      if (this.firstPageMaster != null) {
         switch (var1.region(this.firstPageMaster)) {
            case 1:
               if (this.staticContent == null) {
                  this.staticContent = new StaticContent();
               }

               this.section.headerFirst = new Header("f", this.staticContent);
               break;
            case 2:
               if (this.staticContent == null) {
                  this.staticContent = new StaticContent();
               }

               this.section.footerFirst = new Footer("f", this.staticContent);
         }
      }

      if (this.staticContent == null) {
         this.state.skip = true;
      }

   }

   public void endStaticContent(Context var1) throws Exception {
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
      if (!this.state.skip) {
         if (this.paragraph != null) {
            this.endParagraph();
         }

         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   private void addBookmark(String var1, Context var2) {
      Bookmark var3 = new Bookmark(var1, var2);
      if (this.bookmarks == null) {
         this.bookmarks = new Vector();
      }

      this.bookmarks.addElement(var3);
   }

   public void endBlock(Context var1) throws Exception {
      if (!this.state.skip && this.paragraph != null) {
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
      if (!this.state.skip) {
         if (var3[125] != null) {
            this.startBookmark(var3[125].id(), var4);
         }

         Picture var5 = new Picture(var1, var2, this.runProperties(var4), this.colorTable, this.prescaleImages, this.imageResolution, this.imageRendererResolution, this.alwaysSaveAsPNG);
         if (this.paragraph == null) {
            this.startParagraph(var4);
         }

         this.paragraph.add(var5);
      }

   }

   public void endExternalGraphic(Context var1) throws Exception {
      if (!this.state.skip && this.state.bookmark != null) {
         this.endBookmark();
      }

      this.state = this.state.restore();
   }

   public void startInstreamForeignObject(Value[] var1, Context var2) throws Exception {
   }

   public void endInstreamForeignObject(Graphic var1, GraphicEnv var2, Value[] var3, Context var4) throws Exception {
      this.state = this.state.update(16);
      if (!this.state.skip) {
         if (var3[125] != null) {
            this.startBookmark(var3[125].id(), var4);
         }

         Picture var5 = new Picture(var1, var2, this.runProperties(var4), this.colorTable, this.prescaleImages, this.imageResolution, this.imageRendererResolution, this.alwaysSaveAsPNG);
         if (this.paragraph == null) {
            this.startParagraph(var4);
         }

         this.paragraph.add(var5);
         if (this.state.bookmark != null) {
            this.endBookmark();
         }
      }

      this.state = this.state.restore();
   }

   public void startInline(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(14);
      if (!this.state.skip && var1[125] != null) {
         this.startBookmark(var1[125].id(), var2);
      }

   }

   private void startBookmark(String var1, Context var2) {
      this.state.bookmark = new Bookmark(var1, var2);
      if (this.paragraph == null) {
         this.startParagraph(var2);
      }

      this.paragraph.add(this.state.bookmark.start(this.encoder));
   }

   private void endBookmark() {
      this.paragraph.add(this.state.bookmark.end(this.encoder));
   }

   public void endInline(Context var1) throws Exception {
      if (!this.state.skip && this.state.bookmark != null) {
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
      if (!this.state.skip) {
         Leader var3 = new Leader(var2);
         if (this.paragraph == null) {
            this.startParagraph(var2);
         }

         this.paragraph.properties.addTab(var3.Tab());
         this.paragraph.add(new Tab(this.runProperties(var2)));
         this.state.skip = true;
      }

   }

   public void endLeader(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startPageNumber(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(29);
      if (!this.state.skip) {
         if (var1[125] != null) {
            this.startBookmark(var1[125].id(), var2);
         }

         if (this.paragraph == null) {
            this.startParagraph(var2);
         }

         this.paragraph.add(new PageNumber(this.runProperties(var2)));
      }

   }

   public void endPageNumber(Context var1) throws Exception {
      if (!this.state.skip && this.state.bookmark != null) {
         this.endBookmark();
      }

      this.state = this.state.restore();
   }

   public void startPageNumberCitation(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(30);
      if (!this.state.skip) {
         if (var1[125] != null) {
            this.startBookmark(var1[125].id(), var2);
         }

         if (var1[226] != null) {
            String var3 = var1[226].idref();
            PageReference var4 = new PageReference(var3, var2);
            var4.properties = this.runProperties(var2);
            if (this.paragraph == null) {
               this.startParagraph(var2);
            }

            this.paragraph.add((Field)var4);
         }
      }

   }

   public void endPageNumberCitation(Context var1) throws Exception {
      if (!this.state.skip && this.state.bookmark != null) {
         this.endBookmark();
      }

      this.state = this.state.restore();
   }

   public void startTableAndCaption(Value[] var1, Context var2) throws Exception {
      if (!this.state.skip && this.paragraph != null) {
         this.endParagraph();
      }

      this.state = this.state.update(46);
      if (!this.state.skip) {
         this.state.tableAndCaption = new TableAndCaption(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endTableAndCaption(Context var1) throws Exception {
      TableAndCaption var2 = null;
      if (!this.state.skip) {
         var2 = this.state.tableAndCaption;
      }

      this.state = this.state.restore();
      if (var2 != null) {
         int var3 = this.state.context;
         if (var3 == 3) {
            var3 = this.state.parentContext();
         }

         switch (var3) {
            case 4:
               this.state.table.add(var2);
               break;
            case 7:
               var2.layout(this.state.referenceWidth);
               this.staticContent.add(var2);
               break;
            default:
               var2.layout(this.state.referenceWidth);
               var2.print(this.tmpWriter, this.encoder, this.target);
         }
      }

   }

   public void startTable(Value[] var1, Context var2) throws Exception {
      if (!this.state.skip && this.paragraph != null) {
         this.endParagraph();
      }

      this.state = this.state.update(45);
      if (!this.state.skip) {
         this.state.table = new Table(++this.state.nestingLevel, this.colorTable, var2);
         if (var2.parent().fo == 46) {
            this.state.tableAndCaption.table = this.state.table;
         }

         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endTable(Context var1) throws Exception {
      Table var2 = null;
      if (!this.state.skip && var1.parent().fo != 46) {
         var2 = this.state.table;
      }

      this.state = this.state.restore();
      if (var2 != null) {
         int var3 = this.state.context;
         if (var3 == 3) {
            var3 = this.state.parentContext();
         }

         switch (var3) {
            case 4:
               this.state.table.add(var2);
               break;
            case 7:
               var2.layout(this.state.referenceWidth);
               this.staticContent.add(var2);
               break;
            default:
               var2.layout(this.state.referenceWidth);
               var2.print(this.tmpWriter, this.encoder, this.target);
         }
      }

   }

   public void startTableColumn(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(50);
      if (!this.state.skip) {
         this.state.table.setColumn(var2);
      }

   }

   public void endTableColumn(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startTableCaption(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(48);
      if (!this.state.skip) {
         this.state.tableAndCaption.caption = new Caption(this.state.nestingLevel, var2);
         this.state.bookmarks = this.bookmarks;
         this.bookmarks = null;
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endTableCaption(Context var1) throws Exception {
      if (!this.state.skip) {
         this.bookmarks = this.state.bookmarks;
      }

      this.state = this.state.restore();
   }

   public void startTableHeader(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(52);
      if (!this.state.skip) {
         this.state.table.startHeader(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endTableHeader(Context var1) throws Exception {
      if (!this.state.skip) {
         this.state.table.endHeader();
      }

      this.state = this.state.restore();
   }

   public void startTableFooter(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(51);
      if (!this.state.skip) {
         this.state.table.startFooter(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endTableFooter(Context var1) throws Exception {
      if (!this.state.skip) {
         this.state.table.endFooter();
      }

      this.state = this.state.restore();
   }

   public void startTableBody(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(47);
      if (!this.state.skip) {
         this.state.table.startBody(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endTableBody(Context var1) throws Exception {
      if (!this.state.skip) {
         this.state.table.endBody();
      }

      this.state = this.state.restore();
   }

   public void startTableRow(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(53);
      if (!this.state.skip) {
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }

         this.state.table.startRow(var2, this.bookmarks);
         this.bookmarks = null;
      }

   }

   public void endTableRow(Context var1) throws Exception {
      if (!this.state.skip) {
         this.state.table.endRow();
      }

      this.state = this.state.restore();
   }

   public void startTableCell(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(49);
      if (!this.state.skip) {
         this.state.table.startCell(var2);
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }

         var2.background = null;
      }

   }

   public void endTableCell(Context var1) throws Exception {
      if (!this.state.skip) {
         this.state.table.endCell();
      }

      this.state = this.state.restore();
   }

   public void startListBlock(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(19);
      if (!this.state.skip) {
         if (this.paragraph != null) {
            this.endParagraph();
         }

         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }

         if (this.state.context == 3 && this.state.list.item != null) {
            this.startParagraph(var2);
            this.endParagraph();
         }

         this.state.list = new List(var2);
      }

   }

   public void endListBlock(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startListItem(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(20);
      if (!this.state.skip && var1[125] != null) {
         this.addBookmark(var1[125].id(), var2);
      }

   }

   public void endListItem(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startListItemBody(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(21);
      if (!this.state.skip && var1[125] != null) {
         this.addBookmark(var1[125].id(), var2);
      }

   }

   public void endListItemBody(Context var1) throws Exception {
      this.state = this.state.restore();
   }

   public void startListItemLabel(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(22);
      if (!this.state.skip && var1[125] != null) {
         this.addBookmark(var1[125].id(), var2);
      }

   }

   public void endListItemLabel(Context var1) throws Exception {
      if (!this.state.skip) {
         if (this.state.list.item == null) {
            this.state.list.item = new List.Item();
            this.state.list.item.label = new List.Item.Label();
         }

         if (this.state.list.id < 0) {
            ListTable.List var2 = this.state.list.item.label.list(this.state.list.labelFormat);
            if (var2 == null) {
               this.state.list.id = 0;
               this.state.list.plainText = true;
            } else {
               if (var2.style == 23) {
                  char var3 = var2.getBullet();
                  if (var3 > 127 && var2.properties != null) {
                     int var4 = Rtf.toSymbolChar(var3);
                     if (var4 <= 0) {
                        var4 = 183;
                     }

                     var2.setBullet((char)var4);
                     var2.properties.fontIndex = this.symbolFont;
                  }
               }

               this.state.list.id = this.listTable.add(var2);
            }
         }
      }

      this.state = this.state.restore();
   }

   public void startBasicLink(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(0);
      if (!this.state.skip) {
         if (var1[125] != null) {
            this.startBookmark(var1[125].id(), var2);
         }

         this.state.link = new Link(var2);
         if (this.paragraph == null) {
            this.startParagraph(var2);
         }

         this.paragraph.add(this.state.link.start(this.encoder));
      }

   }

   public void endBasicLink(Context var1) throws Exception {
      if (!this.state.skip) {
         this.paragraph.add(this.state.link.end());
         if (this.state.bookmark != null) {
            this.endBookmark();
         }
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
      if (!this.state.skip) {
         this.state.footnote = new Footnote();
         this.state.paragraph = this.paragraph;
         this.state.bookmarks = this.bookmarks;
         this.paragraph = null;
         this.bookmarks = null;
         if (var1[125] != null) {
            this.addBookmark(var1[125].id(), var2);
         }
      }

   }

   public void endFootnoteBody(Context var1) throws Exception {
      if (!this.state.skip) {
         this.paragraph = this.state.paragraph;
         this.bookmarks = this.state.bookmarks;
         if (this.paragraph == null) {
            this.startParagraph(var1);
         }

         this.paragraph.add(this.state.footnote);
      }

      this.state = this.state.restore();
   }

   public void startWrapper(Value[] var1, Context var2) throws Exception {
      this.state = this.state.update(55);
      if (!this.state.skip && var1[125] != null) {
         this.addBookmark(var1[125].id(), var2);
      }

   }

   public void endWrapper(Context var1) throws Exception {
      if (!this.state.skip && this.bookmarks != null && this.paragraph != null) {
         int var2;
         if (this.paragraph.bookmarks != null) {
            for(var2 = 0; var2 < this.paragraph.bookmarks.length; ++var2) {
               this.bookmarks.insertElementAt(this.paragraph.bookmarks[var2], var2);
            }
         }

         this.paragraph.bookmarks = new Bookmark[this.bookmarks.size()];

         for(var2 = 0; var2 < this.paragraph.bookmarks.length; ++var2) {
            this.paragraph.bookmarks[var2] = (Bookmark)this.bookmarks.elementAt(var2);
         }

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

   private static class Encoding {
      String name;
      int codePage;
      int charSet;

      Encoding(String var1, int var2, int var3) {
         this.name = var1;
         this.codePage = var2;
         this.charSet = var3;
      }
   }
}
