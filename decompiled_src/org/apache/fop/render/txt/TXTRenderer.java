package org.apache.fop.render.txt;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.apache.fop.apps.FOPException;
import org.apache.fop.area.Area;
import org.apache.fop.area.CTM;
import org.apache.fop.area.PageViewport;
import org.apache.fop.area.inline.Image;
import org.apache.fop.area.inline.TextArea;
import org.apache.fop.render.AbstractPathOrientedRenderer;
import org.apache.fop.render.txt.border.BorderManager;
import org.apache.xmlgraphics.util.UnitConv;

public class TXTRenderer extends AbstractPathOrientedRenderer {
   private static final char LIGHT_SHADE = '░';
   private static final char MEDIUM_SHADE = '▒';
   private static final char DARK_SHADE = '▓';
   private static final char FULL_BLOCK = '█';
   private static final char IMAGE_CHAR = '#';
   private OutputStream outputStream;
   private TXTStream currentStream;
   private StringBuffer[] charData;
   private StringBuffer[] decoData;
   public static final int CHAR_HEIGHT = 7860;
   public static final int CHAR_WIDTH = 6000;
   private int pageWidth;
   private int pageHeight;
   private final String lineEnding = "\r\n";
   private final String pageEnding = "\f";
   private boolean firstPage = false;
   private BorderManager bm;
   private char fillChar;
   private final TXTState currentState = new TXTState();
   private String encoding;

   public String getMimeType() {
      return "text/plain";
   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public boolean isLayInside(int x, int y) {
      return x >= 0 && x < this.pageWidth && y >= 0 && y < this.pageHeight;
   }

   protected void addChar(int x, int y, char ch, boolean ischar) {
      Point point = this.currentState.transformPoint(x, y);
      this.putChar(point.x, point.y, ch, ischar);
   }

   protected void putChar(int x, int y, char ch, boolean ischar) {
      if (this.isLayInside(x, y)) {
         StringBuffer sb = ischar ? this.charData[y] : this.decoData[y];

         while(sb.length() <= x) {
            sb.append(' ');
         }

         sb.setCharAt(x, ch);
      }

   }

   protected void addString(int row, int col, String s) {
      for(int l = 0; l < s.length(); ++l) {
         this.addChar(col + l, row, s.charAt(l), true);
      }

   }

   protected void renderText(TextArea area) {
      int col = Helper.ceilPosition(this.currentIPPosition, 6000);
      int row = Helper.ceilPosition(this.currentBPPosition, 7860);
      String s = area.getText();
      this.addString(row, col, s);
      super.renderText(area);
   }

   public void renderPage(PageViewport page) throws IOException, FOPException {
      if (this.firstPage) {
         this.firstPage = false;
      } else {
         this.currentStream.add("\f");
      }

      Rectangle2D bounds = page.getViewArea();
      double width = bounds.getWidth();
      double height = bounds.getHeight();
      this.pageWidth = Helper.ceilPosition((int)width, 6000);
      this.pageHeight = Helper.ceilPosition((int)height, 7860);
      this.charData = new StringBuffer[this.pageHeight];
      this.decoData = new StringBuffer[this.pageHeight];

      for(int i = 0; i < this.pageHeight; ++i) {
         this.charData[i] = new StringBuffer();
         this.decoData[i] = new StringBuffer();
      }

      this.bm = new BorderManager(this.pageWidth, this.pageHeight, this.currentState);
      super.renderPage(page);
      this.flushBorderToBuffer();
      this.flushBuffer();
   }

   private void flushBorderToBuffer() {
      for(int x = 0; x < this.pageWidth; ++x) {
         for(int y = 0; y < this.pageHeight; ++y) {
            Character c = this.bm.getCharacter(x, y);
            if (c != null) {
               this.putChar(x, y, c, false);
            }
         }
      }

   }

   private void flushBuffer() {
      for(int row = 0; row < this.pageHeight; ++row) {
         StringBuffer cr = this.charData[row];
         StringBuffer dr = this.decoData[row];
         StringBuffer outr = null;
         if (cr != null && dr == null) {
            outr = cr;
         } else if (dr != null && cr == null) {
            outr = dr;
         } else if (cr != null && dr != null) {
            int len = dr.length();
            if (cr.length() > len) {
               len = cr.length();
            }

            outr = new StringBuffer();

            for(int countr = 0; countr < len; ++countr) {
               if (countr < cr.length() && cr.charAt(countr) != ' ') {
                  outr.append(cr.charAt(countr));
               } else if (countr < dr.length()) {
                  outr.append(dr.charAt(countr));
               } else {
                  outr.append(' ');
               }
            }
         }

         if (outr != null) {
            this.currentStream.add(outr.toString());
         }

         if (row < this.pageHeight) {
            this.currentStream.add("\r\n");
         }
      }

   }

   public void startRenderer(OutputStream os) throws IOException {
      log.info("Rendering areas to TEXT.");
      this.outputStream = os;
      this.currentStream = new TXTStream(os);
      this.currentStream.setEncoding(this.encoding);
      this.firstPage = true;
   }

   public void stopRenderer() throws IOException {
      log.info("writing out TEXT");
      this.outputStream.flush();
      super.stopRenderer();
   }

   protected void restoreStateStackAfterBreakOut(List breakOutList) {
   }

   protected List breakOutOfStateStack() {
      return null;
   }

   protected void saveGraphicsState() {
      this.currentState.push(new CTM());
   }

   protected void restoreGraphicsState() {
      this.currentState.pop();
   }

   protected void beginTextObject() {
   }

   protected void endTextObject() {
   }

   protected void clip() {
   }

   protected void clipRect(float x, float y, float width, float height) {
   }

   protected void moveTo(float x, float y) {
   }

   protected void lineTo(float x, float y) {
   }

   protected void closePath() {
   }

   private void fillRect(int startX, int startY, int width, int height, char charToFill) {
      for(int x = startX; x < startX + width; ++x) {
         for(int y = startY; y < startY + height; ++y) {
            this.addChar(x, y, charToFill, false);
         }
      }

   }

   protected void fillRect(float x, float y, float width, float height) {
      this.fillRect(this.bm.getStartX(), this.bm.getStartY(), this.bm.getWidth(), this.bm.getHeight(), this.fillChar);
   }

   protected void updateColor(Color col, boolean fill) {
      if (col != null) {
         double fillShade = (double)(0.0011764707F * (float)col.getRed() + 0.0023137254F * (float)col.getGreen() + 4.3137255E-4F * (float)col.getBlue());
         fillShade = 1.0 - fillShade;
         if (fillShade > 0.800000011920929) {
            this.fillChar = 9608;
         } else if (fillShade > 0.6000000238418579) {
            this.fillChar = 9619;
         } else if (fillShade > 0.4000000059604645) {
            this.fillChar = 9618;
         } else if (fillShade > 0.20000000298023224) {
            this.fillChar = 9617;
         } else {
            this.fillChar = ' ';
         }

      }
   }

   protected void drawImage(String url, Rectangle2D pos, Map foreignAttributes) {
   }

   public void renderImage(Image image, Rectangle2D pos) {
      int x1 = Helper.ceilPosition(this.currentIPPosition, 6000);
      int y1 = Helper.ceilPosition(this.currentBPPosition, 7860);
      int width = Helper.ceilPosition((int)pos.getWidth(), 6000);
      int height = Helper.ceilPosition((int)pos.getHeight(), 7860);
      this.fillRect(x1, y1, width, height, '#');
   }

   protected int toMilli(float x) {
      return Math.round(x * 1000.0F);
   }

   private void addBitOfBorder(int x, int y, int style, int type) {
      Point point = this.currentState.transformPoint(x, y);
      if (this.isLayInside(point.x, point.y)) {
         this.bm.addBorderElement(point.x, point.y, style, type);
      }

   }

   protected void drawBorderLine(float x1, float y1, float x2, float y2, boolean horz, boolean startOrBefore, int style, Color col) {
      int borderHeight = this.bm.getHeight();
      int borderWidth = this.bm.getWidth();
      int borderStartX = this.bm.getStartX();
      int borderStartY = this.bm.getStartY();
      int x;
      int y;
      if (horz && startOrBefore) {
         x = borderStartX;
         y = borderStartY;
      } else if (horz && !startOrBefore) {
         x = borderStartX;
         y = borderStartY + borderHeight - 1;
      } else if (!horz && startOrBefore) {
         x = borderStartX;
         y = borderStartY;
      } else {
         x = borderStartX + borderWidth - 1;
         y = borderStartY;
      }

      byte dx;
      byte dy;
      int length;
      byte startType;
      byte endType;
      if (horz) {
         length = borderWidth;
         dx = 1;
         dy = 0;
         startType = 2;
         endType = 8;
      } else {
         length = borderHeight;
         dx = 0;
         dy = 1;
         startType = 4;
         endType = 1;
      }

      this.addBitOfBorder(x, y, style, startType);

      for(int i = 0; i < length - 2; ++i) {
         x += dx;
         y += dy;
         this.addBitOfBorder(x, y, style, startType + endType);
      }

      x += dx;
      y += dy;
      this.addBitOfBorder(x, y, style, endType);
   }

   protected void drawBackAndBorders(Area area, float startx, float starty, float width, float height) {
      this.bm.setWidth(Helper.ceilPosition(this.toMilli(width), 6000));
      this.bm.setHeight(Helper.ceilPosition(this.toMilli(height), 7860));
      this.bm.setStartX(Helper.ceilPosition(this.toMilli(startx), 6000));
      this.bm.setStartY(Helper.ceilPosition(this.toMilli(starty), 7860));
      super.drawBackAndBorders(area, startx, starty, width, height);
   }

   protected void startVParea(CTM ctm, Rectangle2D clippingRect) {
      this.currentState.push(ctm);
   }

   protected void endVParea() {
      this.currentState.pop();
   }

   protected void concatenateTransformationMatrix(AffineTransform at) {
      this.currentState.push(new CTM(UnitConv.ptToMpt(at)));
   }
}
