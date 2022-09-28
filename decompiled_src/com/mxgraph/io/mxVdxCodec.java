package com.mxgraph.io;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxConnectionConstraint;
import com.mxgraph.view.mxGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class mxVdxCodec {
   private static double screenCoordinatesPerCm = 40.0;
   private static final double CENTIMETERS_PER_INCHES = 2.54;
   private static HashMap vertexMap = new HashMap();
   private static HashMap edgeShapeMap = new HashMap();
   private static HashMap vertexShapeMap = new HashMap();
   private static HashMap masterMap = new HashMap();
   private static HashMap parentsMap = new HashMap();
   private static HashMap masterElementsMap = new HashMap();
   private static double pageHeight = 11.8;

   private static void cleanMaps() {
      vertexMap.clear();
      edgeShapeMap.clear();
      vertexShapeMap.clear();
      masterMap.clear();
      parentsMap.clear();
      masterElementsMap.clear();
   }

   private static double convertUnityToDefault(String var0) {
      double var1 = 0.0;
      var1 = screenCoordinatesPerCm * 2.54;
      return var1;
   }

   private static mxPoint getOriginPoint(Element var0, Element var1, double var2) {
      NodeList var4 = var0.getChildNodes();
      NodeList var5 = null;
      if (var1 != null) {
         var5 = var1.getChildNodes();
      }

      Element var6 = null;
      Element var7 = null;
      if (nodeListHasTag(var4, "XForm")) {
         var6 = nodeListTag(var4, "XForm");
      }

      if (nodeListHasTag(var5, "XForm")) {
         var7 = nodeListTag(var4, "XForm");
      }

      NodeList var8 = null;
      if (var6 != null) {
         var8 = var6.getChildNodes();
      }

      NodeList var9 = null;
      if (var7 != null) {
         var9 = var7.getChildNodes();
      }

      Element var10 = null;
      if (nodeListHasTag(var8, "PinX")) {
         var10 = nodeListTag(var8, "PinX");
      } else if (nodeListHasTag(var9, "PinX")) {
         var10 = nodeListTag(var9, "PinX");
      }

      double var11 = 0.0;
      if (var10 != null) {
         String var13 = "IN";
         if (var10.hasAttribute("Unit")) {
            var13 = var10.getAttribute("Unit");
         }

         var11 = Double.parseDouble(var10.getTextContent()) * convertUnityToDefault(var13);
      }

      Element var32 = null;
      if (nodeListHasTag(var8, "PinY")) {
         var32 = nodeListTag(var8, "PinY");
      } else if (nodeListHasTag(var9, "PinY")) {
         var32 = nodeListTag(var9, "PinY");
      }

      double var14 = 0.0;
      if (var32 != null) {
         String var16 = "IN";
         if (var32.hasAttribute("Unit")) {
            var16 = var32.getAttribute("Unit");
         }

         var14 = Double.parseDouble(var32.getTextContent()) * convertUnityToDefault(var16);
      }

      Element var33 = null;
      if (nodeListHasTag(var8, "LocPinY")) {
         var33 = nodeListTag(var8, "LocPinY");
      } else if (nodeListHasTag(var9, "LocPinY")) {
         var33 = nodeListTag(var9, "LocPinY");
      }

      double var17 = 0.0;
      if (var33 != null) {
         String var19 = "IN";
         if (var33.hasAttribute("Unit")) {
            var19 = var33.getAttribute("Unit");
         }

         var17 = Double.parseDouble(var33.getTextContent()) * convertUnityToDefault(var19);
      }

      Element var34 = null;
      if (nodeListHasTag(var8, "LocPinX")) {
         var34 = nodeListTag(var8, "LocPinX");
      } else if (nodeListHasTag(var9, "LocPinX")) {
         var34 = nodeListTag(var9, "LocPinX");
      }

      double var20 = 0.0;
      if (var34 != null) {
         String var22 = "IN";
         if (var34.hasAttribute("Unit")) {
            var22 = var34.getAttribute("Unit");
         }

         var20 = Double.parseDouble(var34.getTextContent()) * convertUnityToDefault(var22);
      }

      Element var35 = null;
      if (nodeListHasTag(var8, "Width")) {
         var35 = nodeListTag(var8, "Width");
      } else if (nodeListHasTag(var9, "Width")) {
         var35 = nodeListTag(var9, "Width");
      }

      double var23 = 0.0;
      if (var35 != null) {
         String var25 = "IN";
         if (var35.hasAttribute("Unit")) {
            var25 = var35.getAttribute("Unit");
         }

         var23 = Double.parseDouble(var35.getTextContent()) * convertUnityToDefault(var25);
      }

      Element var36 = null;
      if (nodeListHasTag(var8, "Height")) {
         var36 = nodeListTag(var8, "Height");
      } else if (nodeListHasTag(var9, "Height")) {
         var36 = nodeListTag(var9, "Height");
      }

      double var26 = 0.0;
      if (var36 != null) {
         String var28 = "IN";
         if (var36.hasAttribute("Unit")) {
            var28 = var36.getAttribute("Unit");
         }

         var26 = Double.parseDouble(var36.getTextContent()) * convertUnityToDefault(var28);
      }

      double var37 = var11 - var20;
      double var30 = var2 - (var14 + (var26 - var17));
      return new mxPoint(var37, var30);
   }

   private static mxPoint getDimentions(Element var0, Element var1) {
      NodeList var2 = var0.getChildNodes();
      NodeList var3 = null;
      if (var1 != null) {
         var3 = var1.getChildNodes();
      }

      Element var4 = null;
      Element var5 = null;
      if (nodeListHasTag(var2, "XForm")) {
         var4 = nodeListTag(var2, "XForm");
      }

      if (nodeListHasTag(var3, "XForm")) {
         var5 = nodeListTag(var2, "XForm");
      }

      NodeList var6 = null;
      if (var4 != null) {
         var6 = var4.getChildNodes();
      }

      NodeList var7 = null;
      if (var5 != null) {
         var7 = var5.getChildNodes();
      }

      Element var8 = null;
      if (nodeListHasTag(var6, "Width")) {
         var8 = nodeListTag(var6, "Width");
      } else if (nodeListHasTag(var7, "Width")) {
         var8 = nodeListTag(var7, "Width");
      }

      double var9 = 0.0;
      if (var8 != null) {
         String var11 = "IN";
         if (var8.hasAttribute("Unit")) {
            var11 = var8.getAttribute("Unit");
         }

         var9 = Double.parseDouble(var8.getTextContent()) * convertUnityToDefault(var11);
      }

      Element var15 = null;
      if (nodeListHasTag(var6, "Height")) {
         var15 = nodeListTag(var6, "Height");
      } else if (nodeListHasTag(var7, "Height")) {
         var15 = nodeListTag(var7, "Height");
      }

      double var12 = 0.0;
      if (var15 != null) {
         String var14 = "IN";
         if (var15.hasAttribute("Unit")) {
            var14 = var15.getAttribute("Unit");
         }

         var12 = Double.parseDouble(var15.getTextContent()) * convertUnityToDefault(var14);
      }

      return new mxPoint(var9, var12);
   }

   private static mxPoint getPageDimentions(Element var0) {
      Element var1 = (Element)var0.getElementsByTagName("PageHeight").item(0);
      String var2 = "IN";
      if (var1.hasAttribute("Unit")) {
         var2 = var1.getAttribute("Unit");
      }

      double var3 = Double.valueOf(var1.getTextContent()) * convertUnityToDefault(var2);
      Element var5 = (Element)var0.getElementsByTagName("PageWidth").item(0);
      String var6 = "IN";
      if (var5.hasAttribute("Unit")) {
         var6 = var5.getAttribute("Unit");
      }

      double var7 = Double.valueOf(var5.getTextContent()) * convertUnityToDefault(var6);
      return new mxPoint(var7, var3);
   }

   private static mxPoint getBeginXY(Element var0, double var1) {
      Element var3 = (Element)((Element)var0.getElementsByTagName("XForm1D").item(0));
      Element var4 = (Element)((Element)var3.getElementsByTagName("BeginX").item(0));
      String var5 = "IN";
      if (var4.hasAttribute("Unit")) {
         var5 = var4.getAttribute("Unit");
      }

      Element var6 = (Element)((Element)var3.getElementsByTagName("BeginY").item(0));
      String var7 = "IN";
      if (var6.hasAttribute("Unit")) {
         var7 = var6.getAttribute("Unit");
      }

      double var8 = Double.valueOf(var4.getTextContent()) * convertUnityToDefault(var5);
      double var10 = var1 - Double.valueOf(var6.getTextContent()) * convertUnityToDefault(var7);
      return new mxPoint(var8, var10);
   }

   private static mxPoint getEndXY(Element var0, double var1) {
      Element var3 = (Element)((Element)var0.getElementsByTagName("XForm1D").item(0));
      Element var4 = (Element)((Element)var3.getElementsByTagName("EndX").item(0));
      String var5 = "IN";
      if (var4.hasAttribute("Unit")) {
         var5 = var4.getAttribute("Unit");
      }

      Element var6 = (Element)((Element)var3.getElementsByTagName("EndY").item(0));
      String var7 = "IN";
      if (var6.hasAttribute("Unit")) {
         var7 = var6.getAttribute("Unit");
      }

      double var8 = Double.valueOf(var4.getTextContent()) * convertUnityToDefault(var5);
      double var10 = var1 - Double.valueOf(var6.getTextContent()) * convertUnityToDefault(var7);
      return new mxPoint(var8, var10);
   }

   private static mxPoint getLineToXY(Element var0) {
      Element var1 = (Element)var0.getElementsByTagName("X").item(0);
      String var2 = "IN";
      if (var1.hasAttribute("Unit")) {
         var2 = var1.getAttribute("Unit");
      }

      Element var3 = (Element)var0.getElementsByTagName("Y").item(0);
      String var4 = "IN";
      if (var3.hasAttribute("Unit")) {
         var4 = var3.getAttribute("Unit");
      }

      double var5 = Double.valueOf(var1.getTextContent()) * convertUnityToDefault(var2);
      double var7 = Double.valueOf(var3.getTextContent()) * convertUnityToDefault(var4) * -1.0;
      return new mxPoint(var5, var7);
   }

   private static List getRoutingPoints(Element var0, double var1) {
      mxPoint var3 = getBeginXY(var0, var1);
      ArrayList var4 = new ArrayList();
      NodeList var5 = var0.getElementsByTagName("LineTo");
      ArrayList var6 = new ArrayList();
      int var7 = var5.getLength();

      int var8;
      for(var8 = 0; var8 < var7; ++var8) {
         Element var9 = (Element)var5.item(var8);
         if (!var9.hasAttribute("Del") || !var9.getAttribute("Del").equals("1")) {
            var6.add(var9);
         }
      }

      var8 = var6.size();

      for(int var14 = 0; var14 < var8 - 1; ++var14) {
         Element var10 = (Element)var6.get(var14);
         mxPoint var11 = getLineToXY(var10);
         Double var12 = var3.getX() + var11.getX();
         Double var13 = var3.getY() + var11.getY();
         var4.add(new mxPoint(var12, var13));
      }

      return var4;
   }

   private static mxPoint calculateAbsolutePoint(mxCell var0, mxGraph var1, mxPoint var2) {
      if (var0 != null) {
         for(mxCell var3 = (mxCell)var1.getDefaultParent(); !var3.equals(var0); var0 = (mxCell)var0.getParent()) {
            var2.setX(var2.getX() + var0.getGeometry().getX());
            var2.setY(var2.getY() + var0.getGeometry().getY());
         }
      }

      return var2;
   }

   private static boolean nodeListHasTag(NodeList var0, String var1) {
      boolean var2 = false;
      if (var0 != null) {
         int var3 = var0.getLength();

         for(int var4 = 0; var4 < var3 && !var2; ++var4) {
            var2 = var0.item(var4).getNodeName().equals(var1);
         }
      }

      return var2;
   }

   private static Element nodeListTag(NodeList var0, String var1) {
      if (var0 != null) {
         int var2 = var0.getLength();
         boolean var3 = false;

         for(int var4 = 0; var4 < var2 && !var3; ++var4) {
            var3 = var0.item(var4).getNodeName().equals(var1);
            if (var3) {
               return (Element)var0.item(var4);
            }
         }
      }

      return null;
   }

   private static List nodeListTags(NodeList var0, String var1) {
      if (var0 != null) {
         ArrayList var2 = new ArrayList();
         int var3 = var0.getLength();

         for(int var4 = 0; var4 < var3; ++var4) {
            if (var0.item(var4).getNodeName().equals(var1)) {
               var2.add((Element)var0.item(var4));
            }
         }
      }

      return null;
   }

   private static List copyNodeList(NodeList var0) {
      ArrayList var1 = new ArrayList();
      int var2 = var0.getLength();

      for(int var3 = 0; var3 < var2; ++var3) {
         var1.add((Element)var0.item(var3));
      }

      return var1;
   }

   private static String getMasterId(Element var0) {
      return var0.hasAttribute("Master") ? var0.getAttribute("Master") : null;
   }

   private static String lookForMasterId(Element var0) {
      String var1;
      for(var1 = null; var1 == null && !var0.getTagName().equals("Page"); var0 = (Element)var0.getParentNode()) {
         var1 = getMasterId(var0);
      }

      return var1;
   }

   private static String getShapeMasterId(Element var0) {
      return var0.hasAttribute("MasterShape") ? var0.getAttribute("MasterShape") : null;
   }

   private static String getStyleString(Map var0) {
      String var1 = "";
      Iterator var2 = var0.values().iterator();

      String var4;
      Object var5;
      for(Iterator var3 = var0.keySet().iterator(); var3.hasNext(); var1 = var1 + var4 + "=" + var5 + ";") {
         var4 = (String)var3.next();
         var5 = var2.next();
      }

      return var1;
   }

   private static mxPoint[] getVertexPoints(Element var0, Element var1, double var2) {
      NodeList var4 = var0.getChildNodes();
      mxPoint var5 = getOriginPoint(var0, var1, var2);
      mxPoint var6 = getDimentions(var0, var1);
      Element var7 = nodeListTag(var4, "Geom");
      NodeList var8 = var7.getElementsByTagName("LineTo");
      int var9 = var8.getLength();
      mxPoint[] var10 = new mxPoint[var9];

      for(int var11 = 0; var11 < var9; ++var11) {
         Element var12 = (Element)var8.item(var11);
         var10[var11] = getLineToXY(var12);
         var10[var11].setX(var10[var11].getX() + var5.getX());
         var10[var11].setY(var10[var11].getY() + var5.getY() + var6.getY());
      }

      return var10;
   }

   private static boolean isRhombus(Element var0, Element var1, double var2) {
      boolean var4 = false;
      NodeList var5 = var0.getChildNodes();
      NodeList var6 = null;
      if (var1 != null) {
         var6 = var1.getChildNodes();
      }

      Element var7;
      mxPoint[] var8;
      if (nodeListHasTag(var5, "Geom")) {
         var7 = nodeListTag(var5, "Geom");
         if (var7.getElementsByTagName("EllipticalArcTo").getLength() == 0) {
            var4 = var7.getElementsByTagName("LineTo").getLength() == 4;
            if (var4) {
               var8 = getVertexPoints(var0, var1, var2);
               var4 &= var8[0].getX() == var8[2].getX() && var8[1].getY() == var8[3].getY();
            }
         }
      } else if (nodeListHasTag(var6, "Geom")) {
         var7 = nodeListTag(var6, "Geom");
         if (var7.getElementsByTagName("EllipticalArcTo").getLength() == 0) {
            var4 = var7.getElementsByTagName("LineTo").getLength() == 4;
            if (var4) {
               var8 = getVertexPoints(var1, var1, var2);
               var4 &= var8[0].getX() == var8[2].getX() && var8[1].getY() == var8[3].getY();
            }
         }
      }

      return var4;
   }

   private static boolean isEllipse(Element var0, Element var1, double var2) {
      boolean var4 = false;
      NodeList var5 = var0.getChildNodes();
      NodeList var6 = null;
      if (var1 != null) {
         var6 = var1.getChildNodes();
      }

      Element var7;
      if (nodeListHasTag(var5, "Geom")) {
         var7 = nodeListTag(var5, "Geom");
         var4 = var7.getElementsByTagName("Ellipse").getLength() > 0;
         if (!var4) {
            var4 = var7.getElementsByTagName("EllipticalArcTo").getLength() > 0;
            var4 &= var7.getElementsByTagName("LineTo").getLength() < 2;
         }
      } else if (nodeListHasTag(var6, "Geom")) {
         var7 = nodeListTag(var6, "Geom");
         var4 = var7.getElementsByTagName("Ellipse").getLength() > 0;
         if (!var4) {
            var4 = var7.getElementsByTagName("EllipticalArcTo").getLength() > 0;
            var4 &= var7.getElementsByTagName("LineTo").getLength() < 2;
         }
      }

      return var4;
   }

   private static boolean isRounded(Element var0, Element var1, double var2) {
      boolean var4 = false;
      NodeList var5 = var0.getChildNodes();
      NodeList var6 = null;
      if (var1 != null) {
         var6 = var1.getChildNodes();
      }

      Element var7;
      if (nodeListHasTag(var5, "Geom")) {
         var7 = nodeListTag(var5, "Geom");
         var4 = var7.getElementsByTagName("LineTo").getLength() == 2 && var7.getElementsByTagName("EllipticalArcTo").getLength() == 2;
         var4 |= var7.getElementsByTagName("LineTo").getLength() == 4 && var7.getElementsByTagName("ArcTo").getLength() == 4;
      } else if (nodeListHasTag(var6, "Geom")) {
         var7 = nodeListTag(var6, "Geom");
         var4 = var7.getElementsByTagName("LineTo").getLength() == 2 && var7.getElementsByTagName("EllipticalArcTo").getLength() == 2;
         var4 |= var7.getElementsByTagName("LineTo").getLength() == 4 && var7.getElementsByTagName("ArcTo").getLength() == 4;
      }

      return var4;
   }

   private static boolean isTriangle(Element var0, Element var1, double var2) {
      boolean var4 = false;
      NodeList var5 = var0.getChildNodes();
      NodeList var6 = null;
      if (var1 != null) {
         var6 = var1.getChildNodes();
      }

      Element var7;
      if (nodeListHasTag(var5, "Geom")) {
         var7 = nodeListTag(var5, "Geom");
         if (var7.getElementsByTagName("EllipticalArcTo").getLength() == 0) {
            var4 = var7.getElementsByTagName("LineTo").getLength() == 3;
         }
      } else if (nodeListHasTag(var6, "Geom")) {
         var7 = nodeListTag(var6, "Geom");
         if (var7.getElementsByTagName("EllipticalArcTo").getLength() == 0) {
            var4 = var7.getElementsByTagName("LineTo").getLength() == 3;
         }
      }

      return var4;
   }

   private static boolean isSwimlane(Element var0, Element var1, double var2) {
      boolean var4 = false;
      if (var0.hasAttribute("NameU") && (var0.getAttribute("NameU").equals("Vertical holder") || var0.getAttribute("NameU").equals("Functional band"))) {
         var4 = true;
      } else {
         String var5 = getMasterId(var0);
         Element var6 = (Element)masterElementsMap.get(var5);
         if (var6 != null && var6.hasAttribute("NameU") && (var6.getAttribute("NameU").equals("Vertical holder") || var6.getAttribute("NameU").equals("Functional band"))) {
            var4 = true;
         }
      }

      return var4;
   }

   private static boolean isHexagon(Element var0, Element var1, double var2) {
      boolean var4 = false;
      NodeList var5 = var0.getChildNodes();
      NodeList var6 = null;
      if (var1 != null) {
         var6 = var1.getChildNodes();
      }

      Element var7;
      if (nodeListHasTag(var5, "Geom")) {
         var7 = nodeListTag(var5, "Geom");
         if (var7.getElementsByTagName("EllipticalArcTo").getLength() == 0) {
            var4 = var7.getElementsByTagName("LineTo").getLength() == 6;
         }
      } else if (nodeListHasTag(var6, "Geom")) {
         var7 = nodeListTag(var6, "Geom");
         if (var7.getElementsByTagName("EllipticalArcTo").getLength() == 0) {
            var4 = var7.getElementsByTagName("LineTo").getLength() == 6;
         }
      }

      return var4;
   }

   private static double getAngle(Element var0) {
      NodeList var1 = var0.getChildNodes();
      Element var2 = nodeListTag(var1, "XForm");
      NodeList var3 = var2.getChildNodes();
      Element var4 = nodeListTag(var3, "Angle");
      String var5 = "0";
      if (var4 != null) {
         var5 = var4.getTextContent();
         return 360.0 - Math.toDegrees(Double.valueOf(var5));
      } else {
         return -1.0;
      }
   }

   private static double getOpacity(Element var0, Element var1) {
      double var2;
      if (var0.hasAttribute("Type") && var0.getAttribute("Type").equals("Group")) {
         var2 = 1.0;
      } else {
         var2 = 0.0;
      }

      NodeList var4 = var0.getChildNodes();
      NodeList var5 = null;
      if (var1 != null) {
         var5 = var1.getChildNodes();
      }

      if (!nodeListHasTag(var4, "Misc") && !nodeListHasTag(var5, "Misc")) {
         var2 = 1.0;
      }

      Element var6;
      NodeList var7;
      Element var8;
      if (nodeListHasTag(var4, "Fill")) {
         var6 = nodeListTag(var4, "Fill");
         var7 = var6.getChildNodes();
         if (nodeListHasTag(var7, "FillBkgndTrans")) {
            var8 = nodeListTag(var7, "FillBkgndTrans");
            var2 = Double.valueOf(var8.getTextContent());
         }
      } else if (nodeListHasTag(var5, "Fill")) {
         var6 = nodeListTag(var5, "Fill");
         var7 = var6.getChildNodes();
         if (nodeListHasTag(var7, "FillBkgndTrans")) {
            var8 = nodeListTag(var7, "FillBkgndTrans");
            var2 = Double.valueOf(var8.getTextContent());
         }
      }

      return 100.0 - var2 * 100.0;
   }

   private static String getStyleFromShape(Element var0, Element var1, double var2) {
      Hashtable var4 = new Hashtable();
      var4.put(mxConstants.STYLE_WHITE_SPACE, "wrap");
      double var5 = getAngle(var0);
      if (var5 == -1.0) {
         var5 = getAngle(var1);
         var4.put(mxConstants.STYLE_HORIZONTAL, getAngle(var1) == 0.0 || getAngle(var1) == 360.0);
      } else {
         var4.put(mxConstants.STYLE_HORIZONTAL, getAngle(var0) == 0.0 || getAngle(var0) == 360.0);
      }

      if (var5 > -1.0) {
         var4.put(mxConstants.STYLE_ROTATION, var5);
      }

      double var7 = getOpacity(var0, var1);
      var4.put(mxConstants.STYLE_OPACITY, var7);
      if (isSwimlane(var0, var1, var2)) {
         var4.put(mxConstants.STYLE_SHAPE, "swimlane");
         var4.put(mxConstants.STYLE_PERIMETER, "rectanglePerimeter");
      } else if (isEllipse(var0, var1, var2)) {
         var4.put(mxConstants.STYLE_SHAPE, "ellipse");
         var4.put(mxConstants.STYLE_PERIMETER, "ellipsePerimeter");
      } else if (isRounded(var0, var1, var2)) {
         var4.put(mxConstants.STYLE_ROUNDED, 1);
      } else if (isTriangle(var0, var1, var2)) {
         var4.put(mxConstants.STYLE_SHAPE, "triangle");
         var4.put(mxConstants.STYLE_PERIMETER, "trianglePerimeter");
         var4.put(mxConstants.STYLE_DIRECTION, "north");
      } else if (isHexagon(var0, var1, var2)) {
         var4.put(mxConstants.STYLE_SHAPE, "hexagon");
      } else if (isRhombus(var0, var1, var2)) {
         var4.put(mxConstants.STYLE_SHAPE, "rhombus");
         var4.put(mxConstants.STYLE_PERIMETER, "rhombusPerimeter");
      }

      return getStyleString(var4);
   }

   private static String getStyleFromEdgeShape(Element var0, double var1) {
      Hashtable var3 = new Hashtable();
      NodeList var4 = var0.getChildNodes();
      if (nodeListHasTag(var4, "Line")) {
         var3.put(mxConstants.STYLE_STARTARROW, mxConstants.NONE);
         var3.put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
      }

      return getStyleString(var3);
   }

   private static mxPoint adjustConstraint(mxPoint var0) {
      var0.setX(Math.max(0.0, var0.getX()));
      var0.setY(Math.max(0.0, var0.getY()));
      var0.setX(Math.min(1.0, var0.getX()));
      var0.setY(Math.min(1.0, var0.getY()));
      return var0;
   }

   private static mxCell addShape(mxGraph var0, Object var1, Element var2, Element var3, double var4) {
      if (!var2.getAttribute("Type").equals("Shape") && !var2.getAttribute("Type").equals("Group")) {
         return null;
      } else {
         String var6 = var2.getAttribute("ID");
         NodeList var7 = var2.getChildNodes();
         NodeList var8 = null;
         if (var3 != null) {
            var8 = var3.getChildNodes();
         }

         if (!nodeListHasTag(var7, "XForm1D")) {
            Element var9 = nodeListTag(var7, "Text");
            Element var10 = nodeListTag(var8, "Text");
            String var11 = "";
            if (var9 != null) {
               var11 = var9.getTextContent();
            } else if (var10 != null) {
               var11 = var10.getTextContent();
            }

            mxPoint var12 = getOriginPoint(var2, var3, var4);
            mxPoint var13 = getDimentions(var2, var3);
            String var14 = getStyleFromShape(var2, var3, var4);
            mxCell var15 = (mxCell)var0.insertVertex(var1, (String)null, var11, var12.getX(), var12.getY(), var13.getX(), var13.getY(), var14);
            vertexMap.put(var6, var15);
            vertexShapeMap.put(var6, var2);
            return var15;
         } else {
            edgeShapeMap.put(var6, var2);
            return null;
         }
      }
   }

   private static Object addConectedEdge(mxGraph var0, Element var1, Element var2) {
      mxCell var3 = null;
      String var4 = var1.getAttribute("FromSheet");
      String var5 = var1.getAttribute("ToSheet");
      Element var6 = (Element)edgeShapeMap.get(var4);
      edgeShapeMap.remove(var4);
      Object var7 = parentsMap.get(var6.getAttribute("ID"));
      Element var8 = (Element)vertexShapeMap.get(var5);
      Element var9 = getMaster(var8);
      NodeList var10 = var6.getChildNodes();
      mxCell var11 = (mxCell)vertexMap.get(var5);
      mxCell var12 = null;
      Element var13 = nodeListTag(var10, "Text");
      String var14 = "";
      if (var13 != null) {
         var14 = var13.getTextContent();
      }

      mxPoint var15 = getDimentions(var8, var9);
      double var16 = pageHeight;
      mxCell var18 = (mxCell)var7;
      if (var18 != null) {
         mxGeometry var19 = var18.getGeometry();
         if (var19 != null) {
            var16 = var19.getHeight();
         }
      }

      double var35 = pageHeight;
      if (var11.getParent() != null && var11.getParent().getGeometry() != null) {
         var35 = var11.getParent().getGeometry().getHeight();
      }

      mxPoint var21 = getOriginPoint(var8, var9, var35);
      mxPoint var22 = calculateAbsolutePoint((mxCell)var11.getParent(), var0, var21);
      mxPoint var23 = getBeginXY(var6, var16);
      var23 = calculateAbsolutePoint((mxCell)var7, var0, var23);
      mxPoint var24 = getEndXY(var6, var16);
      var24 = calculateAbsolutePoint((mxCell)var7, var0, var24);
      mxPoint var25 = new mxPoint((var23.getX() - var22.getX()) / var15.getX(), (var23.getY() - var22.getY()) / var15.getY());
      mxPoint var26 = null;
      if (var2 != null) {
         String var27 = var2.getAttribute("ToSheet");
         Element var28 = (Element)vertexShapeMap.get(var27);
         Element var29 = getMaster(var28);
         var12 = (mxCell)vertexMap.get(var27);
         mxPoint var30 = getDimentions(var28, var29);
         var35 = pageHeight;
         if (var12.getParent() != null && var12.getParent().getGeometry() != null) {
            var35 = var12.getParent().getGeometry().getHeight();
         }

         mxPoint var31 = getOriginPoint(var28, var29, var35);
         mxPoint var32 = calculateAbsolutePoint((mxCell)var12.getParent(), var0, var31);
         var26 = new mxPoint((var24.getX() - var32.getX()) / var30.getX(), (var24.getY() - var32.getY()) / var30.getY());
      } else {
         var12 = new mxCell();
         var12.setGeometry(new mxGeometry(var24.getX(), var24.getY(), 0.0, 0.0));
         var0.addCell(var12, var11.getParent());
         var26 = new mxPoint(0.0, 0.0);
      }

      var25 = adjustConstraint(var25);
      var26 = adjustConstraint(var26);
      var3 = (mxCell)var0.insertEdge(var7, (String)null, var14, var11, var12);
      var0.setConnectionConstraint(var3, var11, true, new mxConnectionConstraint(var25, false));
      var0.setConnectionConstraint(var3, var12, false, new mxConnectionConstraint(var26, false));
      mxGeometry var33 = var3.getGeometry();
      List var34 = getRoutingPoints(var6, var16);
      var33.setPoints(var34);
      return var3;
   }

   private static Object addNotConnectedEdge(mxGraph var0, Object var1, Element var2) {
      mxCell var3 = null;
      NodeList var4 = var2.getChildNodes();
      Element var5 = nodeListTag(var4, "Text");
      String var6 = "";
      if (var5 != null) {
         var6 = var5.getTextContent();
      }

      double var7 = pageHeight;
      mxPoint var9 = getBeginXY(var2, var7);
      mxPoint var10 = getEndXY(var2, var7);
      mxCell var11 = new mxCell();
      var11.setGeometry(new mxGeometry(var10.getX(), var10.getY(), 0.0, 0.0));
      var0.addCell(var11, var1);
      mxCell var12 = new mxCell();
      var12.setGeometry(new mxGeometry(var9.getX(), var9.getY(), 0.0, 0.0));
      var0.addCell(var12, var1);
      String var13 = null;
      NodeList var14 = var2.getChildNodes();
      if (nodeListHasTag(var14, "Line")) {
         var13 = getStyleFromEdgeShape(var2, pageHeight);
      }

      mxPoint var15 = new mxPoint(0.0, 0.0);
      mxPoint var16 = new mxPoint(0.0, 0.0);
      var3 = (mxCell)var0.insertEdge(var12.getParent(), (String)null, var6, var12, var11, var13);
      var0.setConnectionConstraint(var3, var12, true, new mxConnectionConstraint(var15, false));
      var0.setConnectionConstraint(var3, var11, false, new mxConnectionConstraint(var16, false));
      mxGeometry var17 = var3.getGeometry();
      List var18 = getRoutingPoints(var2, var7);
      var17.setPoints(var18);
      return var3;
   }

   private static Element findSigConnect(List var0, Element var1, int var2) {
      int var3 = var0.size();
      String var4 = var1.getAttribute("FromSheet");
      Element var5 = null;
      boolean var6 = false;

      for(int var7 = var2 + 1; var7 < var3 && !var6; ++var7) {
         var5 = (Element)var0.get(var7);
         String var8 = var5.getAttribute("FromSheet");
         if (var4.equals(var8)) {
            var6 = true;
         } else {
            var5 = null;
         }
      }

      return var5;
   }

   private static void decodeShape(Element var0, mxGraph var1, Object var2, String var3) {
      Element var5;
      NodeList var6;
      if (isSwimlane(var0, (Element)null, 0.0)) {
         mxCell var4 = (mxCell)var2;
         var5 = (Element)var0.getElementsByTagName("Text").item(0);
         if (var5 != null) {
            var4.setValue(var5.getTextContent());
         }

         var6 = var0.getElementsByTagName("Shapes");
         Element var7 = (Element)var6.item(0);
         NodeList var8 = var7.getChildNodes();
         Element var9 = (Element)var8.item(var8.getLength() - 2);
         mxPoint var10 = getDimentions(var9, (Element)null);
         var4.getGeometry().setHeight(var10.getY());
      } else {
         NodeList var16 = var0.getChildNodes();
         if (nodeListHasTag(var16, "Shapes")) {
            var5 = nodeListTag(var16, "Shapes");
            var6 = var5.getChildNodes();
            int var17 = var6.getLength();
            Element var18 = getMaster(var0);
            double var19 = getDimentions(var0, var18).getY();

            for(int var11 = 0; var11 < var17; ++var11) {
               Element var12 = (Element)var6.item(var11);
               Element var13 = getMaster(var12);
               String var14 = var12.getAttribute("ID");
               parentsMap.put(var14, var2);
               mxCell var15 = addShape(var1, var2, var12, var13, var19);
               if (var15 != null) {
                  decodeShape(var12, var1, var15, var3);
               }
            }
         }
      }

   }

   private static HashMap retrieveMasterShapes(HashMap var0, Element var1) {
      NodeList var2 = var1.getChildNodes();
      String var3 = var1.getAttribute("ID");
      if (nodeListHasTag(var2, "Shapes")) {
         Element var4 = nodeListTag(var2, "Shapes");
         NodeList var5 = var4.getChildNodes();

         for(int var6 = 0; var6 < var5.getLength(); ++var6) {
            Element var7 = (Element)var5.item(var6);
            var0.put(var3, var7);
            var0 = retrieveMasterShapes(var0, var7);
         }
      }

      return var0;
   }

   private static void retrieveMasters(Document var0) {
      NodeList var1 = var0.getElementsByTagName("Masters");
      if (var1.getLength() > 0) {
         Element var2 = (Element)var1.item(0);
         NodeList var3 = var2.getElementsByTagName("Master");
         int var4 = var3.getLength();

         for(int var5 = 0; var5 < var4; ++var5) {
            Element var6 = (Element)var3.item(var5);
            HashMap var7 = new HashMap();
            String var8 = var6.getAttribute("ID");
            masterElementsMap.put(var8, var6);
            masterMap.put(var8, var7);
            retrieveMasterShapes(var7, var6);
         }
      }

   }

   private static Element getMaster(Element var0) {
      Element var1 = null;
      String var2 = getShapeMasterId(var0);
      String var3;
      HashMap var4;
      if (var2 != null) {
         var3 = lookForMasterId(var0);
         if (var3 != null) {
            var4 = (HashMap)masterMap.get(var3);
            if (var4 != null) {
               var1 = (Element)var4.get(var2);
            }
         }
      } else {
         var3 = getMasterId(var0);
         if (var3 != null) {
            var4 = (HashMap)masterMap.get(var3);
            if (var4 != null) {
               var1 = (Element)var4.values().toArray()[0];
            }
         }
      }

      return var1;
   }

   public static void decode(Document var0, mxGraph var1) {
      Object var2 = var1.getDefaultParent();
      var1.getModel().beginUpdate();
      Document var3 = var0;
      NodeList var4 = var0.getElementsByTagName("Pages");
      if (var4.getLength() > 0) {
         Element var5 = (Element)var4.item(0);
         NodeList var6 = var5.getElementsByTagName("Page");
         if (var6.getLength() > 0) {
            for(int var7 = 0; var7 < var6.getLength(); ++var7) {
               Element var8 = (Element)var6.item(var7);
               String var9 = var8.getAttribute("Background");
               if (var9 == null || !var9.equals("1")) {
                  pageHeight = getPageDimentions(var8).getY();
                  retrieveMasters(var3);
                  NodeList var10 = var8.getElementsByTagName("Shapes");
                  if (var10.getLength() > 0) {
                     Element var11 = (Element)var10.item(0);
                     NodeList var12 = var11.getChildNodes();
                     int var13 = var12.getLength();

                     Element var15;
                     for(int var14 = 0; var14 < var13; ++var14) {
                        var15 = (Element)var12.item(var14);
                        String var16 = getMasterId(var15);
                        Element var17 = getMaster(var15);
                        mxCell var18 = addShape(var1, var2, var15, var17, pageHeight);
                        decodeShape(var15, var1, var18, var16);
                     }

                     NodeList var21 = var8.getElementsByTagName("Connects");
                     if (var21.getLength() > 0) {
                        var15 = (Element)var21.item(0);
                        NodeList var23 = var15.getElementsByTagName("Connect");
                        List var25 = copyNodeList(var23);

                        for(int var26 = 0; var26 < var25.size(); ++var26) {
                           Element var19 = (Element)var25.get(var26);
                           Element var20 = findSigConnect(var25, var19, var26);
                           var25.remove(var20);
                           addConectedEdge(var1, var19, var20);
                        }
                     }

                     Iterator var22 = edgeShapeMap.values().iterator();

                     while(var22.hasNext()) {
                        Element var24 = (Element)var22.next();
                        addNotConnectedEdge(var1, parentsMap.get(var24.getAttribute("ID")), var24);
                     }
                  }
               }
            }
         }
      }

      var1.getModel().endUpdate();
      cleanMaps();
   }
}
