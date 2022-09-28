package org.apache.fop.afp;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.fonts.AFPFont;
import org.apache.fop.afp.fonts.AFPFontAttributes;
import org.apache.fop.afp.fonts.CharacterSet;
import org.apache.fop.afp.modca.AbstractPageObject;
import org.apache.fop.afp.modca.Document;
import org.apache.fop.afp.modca.InterchangeSet;
import org.apache.fop.afp.modca.Overlay;
import org.apache.fop.afp.modca.PageGroup;
import org.apache.fop.afp.modca.PageObject;
import org.apache.fop.afp.modca.ResourceGroup;
import org.apache.fop.afp.modca.TagLogicalElementBean;
import org.apache.fop.afp.ptoca.PtocaBuilder;
import org.apache.fop.afp.ptoca.PtocaProducer;
import org.apache.fop.fonts.Font;
import org.apache.fop.util.CharUtilities;

public class DataStream {
   protected static final Log log = LogFactory.getLog("org.apache.xmlgraphics.afp");
   private boolean complete = false;
   private Document document = null;
   private PageGroup currentPageGroup = null;
   private PageObject currentPageObject = null;
   private Overlay currentOverlay = null;
   private AbstractPageObject currentPage = null;
   private int tleSequence = 0;
   private InterchangeSet interchangeSet = InterchangeSet.valueOf("MO:DCA-P IS/2");
   private final Factory factory;
   private OutputStream outputStream;
   private final AFPPaintingState paintingState;

   public DataStream(Factory factory, AFPPaintingState paintingState, OutputStream outputStream) {
      this.paintingState = paintingState;
      this.factory = factory;
      this.outputStream = outputStream;
   }

   public OutputStream getOutputStream() {
      return this.outputStream;
   }

   private Document getDocument() {
      return this.document;
   }

   public AbstractPageObject getCurrentPage() {
      return this.currentPage;
   }

   public void setDocumentName(String name) {
      if (name != null) {
         this.getDocument().setFullyQualifiedName((byte)-125, (byte)0, name);
      }

   }

   public void endDocument() throws IOException {
      if (this.complete) {
         String msg = "Invalid state - document already ended.";
         log.warn("endDocument():: " + msg);
         throw new IllegalStateException(msg);
      } else {
         if (this.currentPageObject != null) {
            this.endPage();
         }

         if (this.currentPageGroup != null) {
            this.endPageGroup();
         }

         if (this.document != null) {
            this.document.endDocument();
            this.document.writeToStream(this.outputStream);
         }

         this.outputStream.flush();
         this.complete = true;
         this.document = null;
         this.outputStream = null;
      }
   }

   public void startPage(int pageWidth, int pageHeight, int pageRotation, int pageWidthRes, int pageHeightRes) {
      this.currentPageObject = this.factory.createPage(pageWidth, pageHeight, pageRotation, pageWidthRes, pageHeightRes);
      this.currentPage = this.currentPageObject;
      this.currentOverlay = null;
   }

   public void startOverlay(int x, int y, int width, int height, int widthRes, int heightRes, int overlayRotation) {
      this.currentOverlay = this.factory.createOverlay(width, height, widthRes, heightRes, overlayRotation);
      String overlayName = this.currentOverlay.getName();
      this.currentPageObject.createIncludePageOverlay(overlayName, x, y, 0);
      this.currentPage = this.currentOverlay;
   }

   public void endOverlay() throws IOException {
      if (this.currentOverlay != null) {
         this.currentOverlay.endPage();
         this.currentOverlay = null;
         this.currentPage = this.currentPageObject;
      }

   }

   public PageObject savePage() {
      PageObject pageObject = this.currentPageObject;
      if (this.currentPageGroup != null) {
         this.currentPageGroup.addPage(this.currentPageObject);
      } else {
         this.document.addPage(this.currentPageObject);
      }

      this.currentPageObject = null;
      this.currentPage = null;
      return pageObject;
   }

   public void restorePage(PageObject pageObject) {
      this.currentPageObject = pageObject;
      this.currentPage = pageObject;
   }

   public void endPage() throws IOException {
      if (this.currentPageObject != null) {
         this.currentPageObject.endPage();
         if (this.currentPageGroup != null) {
            this.currentPageGroup.addPage(this.currentPageObject);
            this.currentPageGroup.writeToStream(this.outputStream);
         } else {
            this.document.addPage(this.currentPageObject);
            this.document.writeToStream(this.outputStream);
         }

         this.currentPageObject = null;
         this.currentPage = null;
      }

   }

   public void addFontsToCurrentPage(Map pageFonts) {
      Iterator iter = pageFonts.values().iterator();

      while(iter.hasNext()) {
         AFPFontAttributes afpFontAttributes = (AFPFontAttributes)iter.next();
         this.createFont(afpFontAttributes.getFontReference(), afpFontAttributes.getFont(), afpFontAttributes.getPointSize());
      }

   }

   public void createFont(int fontReference, AFPFont font, int size) {
      this.currentPage.createFont(fontReference, font, size);
   }

   private Point getPoint(int x, int y) {
      return this.paintingState.getPoint(x, y);
   }

   public void createText(final AFPTextDataInfo textDataInfo, final int letterSpacing, final int wordSpacing, final Font font, final CharacterSet charSet) throws UnsupportedEncodingException {
      int rotation = this.paintingState.getRotation();
      if (rotation != 0) {
         textDataInfo.setRotation(rotation);
         Point p = this.getPoint(textDataInfo.getX(), textDataInfo.getY());
         textDataInfo.setX(p.x);
         textDataInfo.setY(p.y);
      }

      PtocaProducer producer = new PtocaProducer() {
         public void produce(PtocaBuilder builder) throws IOException {
            builder.setTextOrientation(textDataInfo.getRotation());
            builder.absoluteMoveBaseline(textDataInfo.getY());
            builder.absoluteMoveInline(textDataInfo.getX());
            builder.setExtendedTextColor(textDataInfo.getColor());
            builder.setCodedFont((byte)textDataInfo.getFontReference());
            int l = textDataInfo.getString().length();
            StringBuffer sb = new StringBuffer();
            int interCharacterAdjustment = 0;
            AFPUnitConverter unitConv = DataStream.this.paintingState.getUnitConverter();
            if (letterSpacing != 0) {
               interCharacterAdjustment = Math.round(unitConv.mpt2units((float)letterSpacing));
            }

            builder.setInterCharacterAdjustment(interCharacterAdjustment);
            int spaceWidth = font.getCharWidth(' ');
            int spacing = spaceWidth + letterSpacing;
            int fixedSpaceCharacterIncrement = Math.round(unitConv.mpt2units((float)spacing));
            int varSpaceCharacterIncrement = fixedSpaceCharacterIncrement;
            if (wordSpacing != 0) {
               varSpaceCharacterIncrement = Math.round(unitConv.mpt2units((float)(spaceWidth + wordSpacing + letterSpacing)));
            }

            builder.setVariableSpaceCharacterIncrement(varSpaceCharacterIncrement);
            boolean fixedSpaceMode = false;

            for(int i = 0; i < l; ++i) {
               char orgChar = textDataInfo.getString().charAt(i);
               float glyphAdjust = 0.0F;
               int increment;
               if (CharUtilities.isFixedWidthSpace(orgChar)) {
                  this.flushText(builder, sb, charSet);
                  builder.setVariableSpaceCharacterIncrement(fixedSpaceCharacterIncrement);
                  fixedSpaceMode = true;
                  sb.append(' ');
                  increment = font.getCharWidth(orgChar);
                  glyphAdjust += (float)(increment - spaceWidth);
               } else {
                  if (fixedSpaceMode) {
                     this.flushText(builder, sb, charSet);
                     builder.setVariableSpaceCharacterIncrement(varSpaceCharacterIncrement);
                     fixedSpaceMode = false;
                  }

                  char ch;
                  if (orgChar == 160) {
                     ch = ' ';
                  } else {
                     ch = orgChar;
                  }

                  sb.append(ch);
               }

               if (glyphAdjust != 0.0F) {
                  this.flushText(builder, sb, charSet);
                  increment = Math.round(unitConv.mpt2units(glyphAdjust));
                  builder.relativeMoveInline(increment);
               }
            }

            this.flushText(builder, sb, charSet);
         }

         private void flushText(PtocaBuilder builder, StringBuffer sb, CharacterSet charSetx) throws IOException {
            if (sb.length() > 0) {
               builder.addTransparentData(charSetx.encodeChars(sb));
               sb.setLength(0);
            }

         }
      };
      this.currentPage.createText(producer);
   }

   public void createLine(AFPLineDataInfo lineDataInfo) {
      this.currentPage.createLine(lineDataInfo);
   }

   public void createShading(int x, int y, int w, int h, Color col) {
      this.currentPageObject.createShading(x, y, w, h, col.getRed(), col.getGreen(), col.getBlue());
   }

   public void createIncludePageOverlay(String name, int x, int y) {
      this.currentPageObject.createIncludePageOverlay(name, x, y, this.paintingState.getRotation());
      this.currentPageObject.getActiveEnvironmentGroup().createOverlay(name);
   }

   public void createInvokeMediumMap(String name) {
      this.currentPageGroup.createInvokeMediumMap(name);
   }

   public void createIncludePageSegment(String name, int x, int y, int width, int height) {
      int orientation = this.paintingState.getRotation();
      int xOrigin;
      int yOrigin;
      switch (orientation) {
         case 90:
            xOrigin = x - height;
            yOrigin = y;
            break;
         case 180:
            xOrigin = x - width;
            yOrigin = y - height;
            break;
         case 270:
            xOrigin = x;
            yOrigin = y - width;
            break;
         default:
            xOrigin = x;
            yOrigin = y;
      }

      boolean createHardPageSegments = true;
      this.currentPage.createIncludePageSegment(name, xOrigin, yOrigin, createHardPageSegments);
   }

   public void createPageTagLogicalElement(TagLogicalElementBean[] attributes) {
      for(int i = 0; i < attributes.length; ++i) {
         String name = attributes[i].getKey();
         String value = attributes[i].getValue();
         this.currentPage.createTagLogicalElement(name, value, this.tleSequence++);
      }

   }

   public void createPageGroupTagLogicalElement(TagLogicalElementBean[] attributes) {
      for(int i = 0; i < attributes.length; ++i) {
         String name = attributes[i].getKey();
         String value = attributes[i].getValue();
         this.currentPageGroup.createTagLogicalElement(name, value);
      }

   }

   public void createTagLogicalElement(String name, String value) {
      if (this.currentPage != null) {
         this.currentPage.createTagLogicalElement(name, value, this.tleSequence++);
      } else {
         this.currentPageGroup.createTagLogicalElement(name, value);
      }

   }

   public void createNoOperation(String content) {
      this.currentPage.createNoOperation(content);
   }

   public PageGroup getCurrentPageGroup() {
      return this.currentPageGroup;
   }

   public void startDocument() throws IOException {
      this.document = this.factory.createDocument();
      this.document.writeToStream(this.outputStream);
   }

   public void startPageGroup() throws IOException {
      this.endPageGroup();
      this.currentPageGroup = this.factory.createPageGroup(this.tleSequence);
   }

   public void endPageGroup() throws IOException {
      if (this.currentPageGroup != null) {
         this.currentPageGroup.endPageGroup();
         this.tleSequence = this.currentPageGroup.getTleSequence();
         this.document.addPageGroup(this.currentPageGroup);
         this.document.writeToStream(this.outputStream);
         this.currentPageGroup = null;
      }

   }

   public void setInterchangeSet(InterchangeSet interchangeSet) {
      this.interchangeSet = interchangeSet;
   }

   public InterchangeSet getInterchangeSet() {
      return this.interchangeSet;
   }

   public ResourceGroup getResourceGroup(AFPResourceLevel level) {
      ResourceGroup resourceGroup = null;
      if (level.isDocument()) {
         resourceGroup = this.document.getResourceGroup();
      } else if (level.isPageGroup()) {
         resourceGroup = this.currentPageGroup.getResourceGroup();
      } else if (level.isPage()) {
         resourceGroup = this.currentPageObject.getResourceGroup();
      }

      return resourceGroup;
   }
}
