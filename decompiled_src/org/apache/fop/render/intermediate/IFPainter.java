package org.apache.fop.render.intermediate;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import org.apache.fop.traits.BorderProps;
import org.apache.fop.traits.RuleStyle;
import org.w3c.dom.Document;

public interface IFPainter {
   void startViewport(AffineTransform var1, Dimension var2, Rectangle var3) throws IFException;

   void startViewport(AffineTransform[] var1, Dimension var2, Rectangle var3) throws IFException;

   void endViewport() throws IFException;

   void startGroup(AffineTransform[] var1) throws IFException;

   void startGroup(AffineTransform var1) throws IFException;

   void endGroup() throws IFException;

   void setFont(String var1, String var2, Integer var3, String var4, Integer var5, Color var6) throws IFException;

   void drawText(int var1, int var2, int var3, int var4, int[] var5, String var6) throws IFException;

   void clipRect(Rectangle var1) throws IFException;

   void fillRect(Rectangle var1, Paint var2) throws IFException;

   void drawBorderRect(Rectangle var1, BorderProps var2, BorderProps var3, BorderProps var4, BorderProps var5) throws IFException;

   void drawLine(Point var1, Point var2, int var3, Color var4, RuleStyle var5) throws IFException;

   void drawImage(String var1, Rectangle var2) throws IFException;

   void drawImage(Document var1, Rectangle var2) throws IFException;
}
