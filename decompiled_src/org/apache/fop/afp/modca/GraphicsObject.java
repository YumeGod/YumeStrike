package org.apache.fop.afp.modca;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.fop.afp.AFPDataObjectInfo;
import org.apache.fop.afp.AFPObjectAreaInfo;
import org.apache.fop.afp.Completable;
import org.apache.fop.afp.Factory;
import org.apache.fop.afp.StructuredData;
import org.apache.fop.afp.goca.GraphicsAreaBegin;
import org.apache.fop.afp.goca.GraphicsAreaEnd;
import org.apache.fop.afp.goca.GraphicsBox;
import org.apache.fop.afp.goca.GraphicsChainedSegment;
import org.apache.fop.afp.goca.GraphicsCharacterString;
import org.apache.fop.afp.goca.GraphicsData;
import org.apache.fop.afp.goca.GraphicsFillet;
import org.apache.fop.afp.goca.GraphicsFullArc;
import org.apache.fop.afp.goca.GraphicsImage;
import org.apache.fop.afp.goca.GraphicsLine;
import org.apache.fop.afp.goca.GraphicsSetArcParameters;
import org.apache.fop.afp.goca.GraphicsSetCharacterSet;
import org.apache.fop.afp.goca.GraphicsSetCurrentPosition;
import org.apache.fop.afp.goca.GraphicsSetLineType;
import org.apache.fop.afp.goca.GraphicsSetLineWidth;
import org.apache.fop.afp.goca.GraphicsSetPatternSymbol;
import org.apache.fop.afp.goca.GraphicsSetProcessColor;
import org.apache.xmlgraphics.java2d.color.ColorConverter;

public class GraphicsObject extends AbstractDataObject {
   private GraphicsData currentData = null;
   protected List objects = new ArrayList();
   private final GraphicsState graphicsState = new GraphicsState();
   private ColorConverter colorConverter = null;

   public GraphicsObject(Factory factory, String name) {
      super(factory, name);
   }

   public void setViewport(AFPDataObjectInfo dataObjectInfo) {
      super.setViewport(dataObjectInfo);
      AFPObjectAreaInfo objectAreaInfo = dataObjectInfo.getObjectAreaInfo();
      int width = objectAreaInfo.getWidth();
      int height = objectAreaInfo.getHeight();
      int widthRes = objectAreaInfo.getWidthRes();
      int heightRes = objectAreaInfo.getHeightRes();
      int leftEdge = false;
      int topEdge = false;
      GraphicsDataDescriptor graphicsDataDescriptor = this.factory.createGraphicsDataDescriptor(0, width, 0, height, widthRes, heightRes);
      this.getObjectEnvironmentGroup().setDataDescriptor(graphicsDataDescriptor);
   }

   public void addObject(StructuredData object) {
      if (this.currentData == null) {
         this.newData();
      } else if (this.currentData.getDataLength() + object.getDataLength() >= 8192) {
         GraphicsChainedSegment currentSegment = (GraphicsChainedSegment)this.currentData.removeCurrentSegment();
         currentSegment.setName(this.newData().createSegmentName());
         this.currentData.addSegment(currentSegment);
      }

      this.currentData.addObject(object);
   }

   private GraphicsData getData() {
      return this.currentData == null ? this.newData() : this.currentData;
   }

   private GraphicsData newData() {
      if (this.currentData != null) {
         this.currentData.setComplete(true);
      }

      this.currentData = this.factory.createGraphicsData();
      this.objects.add(this.currentData);
      return this.currentData;
   }

   public void setColor(Color color) {
      if (!color.equals(this.graphicsState.color)) {
         this.addObject(new GraphicsSetProcessColor(this.colorConverter.convert(color)));
         this.graphicsState.color = color;
      }

   }

   public void setColorConverter(ColorConverter colorConverter) {
      this.colorConverter = colorConverter;
   }

   public void setCurrentPosition(int[] coords) {
      this.addObject(new GraphicsSetCurrentPosition(coords));
   }

   public void setLineWidth(int lineWidth) {
      if (lineWidth != this.graphicsState.lineWidth) {
         this.addObject(new GraphicsSetLineWidth(lineWidth));
         this.graphicsState.lineWidth = lineWidth;
      }

   }

   public void setLineType(byte lineType) {
      if (lineType != this.graphicsState.lineType) {
         this.addObject(new GraphicsSetLineType(lineType));
         this.graphicsState.lineType = lineType;
      }

   }

   public void setFill(boolean fill) {
      this.setPatternSymbol((byte)(fill ? 16 : 15));
   }

   public void setPatternSymbol(byte patternSymbol) {
      if (patternSymbol != this.graphicsState.patternSymbol) {
         this.addObject(new GraphicsSetPatternSymbol(patternSymbol));
         this.graphicsState.patternSymbol = patternSymbol;
      }

   }

   public void setCharacterSet(int characterSet) {
      if (characterSet != this.graphicsState.characterSet) {
         this.addObject(new GraphicsSetCharacterSet(characterSet));
         this.graphicsState.characterSet = characterSet;
      }

   }

   public void addLine(int[] coords) {
      this.addLine(coords, false);
   }

   public void addLine(int[] coords, boolean relative) {
      this.addObject(new GraphicsLine(coords, relative));
   }

   public void addBox(int[] coords) {
      this.addObject(new GraphicsBox(coords));
   }

   public void addFillet(int[] coords) {
      this.addFillet(coords, false);
   }

   public void addFillet(int[] coords, boolean relative) {
      this.addObject(new GraphicsFillet(coords, relative));
   }

   public void setArcParams(int xmaj, int ymin, int xmin, int ymaj) {
      this.addObject(new GraphicsSetArcParameters(xmaj, ymin, xmin, ymaj));
   }

   public void addFullArc(int x, int y, int mh, int mhr) {
      this.addObject(new GraphicsFullArc(x, y, mh, mhr));
   }

   public void addImage(int x, int y, int width, int height, byte[] imgData) {
      this.addObject(new GraphicsImage(x, y, width, height, imgData));
   }

   public void addString(String str, int x, int y) {
      this.addObject(new GraphicsCharacterString(str, x, y));
   }

   public void beginArea() {
      this.addObject(new GraphicsAreaBegin());
   }

   public void endArea() {
      this.addObject(new GraphicsAreaEnd());
   }

   public String toString() {
      return "GraphicsObject: " + this.getName();
   }

   public void newSegment() {
      this.getData().newSegment();
      this.graphicsState.lineWidth = 0;
   }

   public void setComplete(boolean complete) {
      Iterator it = this.objects.iterator();

      while(it.hasNext()) {
         Completable completedObject = (Completable)it.next();
         completedObject.setComplete(true);
      }

      super.setComplete(complete);
   }

   protected void writeStart(OutputStream os) throws IOException {
      super.writeStart(os);
      byte[] data = new byte[17];
      this.copySF(data, (byte)-88, (byte)-69);
      os.write(data);
   }

   protected void writeContent(OutputStream os) throws IOException {
      super.writeContent(os);
      this.writeObjects(this.objects, os);
   }

   protected void writeEnd(OutputStream os) throws IOException {
      byte[] data = new byte[17];
      this.copySF(data, (byte)-87, (byte)-69);
      os.write(data);
   }

   private static class GraphicsState {
      private Color color;
      private byte lineType;
      private int lineWidth;
      private byte patternSymbol;
      private int characterSet;

      private GraphicsState() {
      }

      // $FF: synthetic method
      GraphicsState(Object x0) {
         this();
      }
   }
}
