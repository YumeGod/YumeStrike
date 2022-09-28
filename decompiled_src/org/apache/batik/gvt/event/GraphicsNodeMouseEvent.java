package org.apache.batik.gvt.event;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import org.apache.batik.gvt.GraphicsNode;

public class GraphicsNodeMouseEvent extends GraphicsNodeInputEvent {
   static final int MOUSE_FIRST = 500;
   public static final int MOUSE_CLICKED = 500;
   public static final int MOUSE_PRESSED = 501;
   public static final int MOUSE_RELEASED = 502;
   public static final int MOUSE_MOVED = 503;
   public static final int MOUSE_ENTERED = 504;
   public static final int MOUSE_EXITED = 505;
   public static final int MOUSE_DRAGGED = 506;
   float x;
   float y;
   int clientX;
   int clientY;
   int screenX;
   int screenY;
   int clickCount;
   int button;
   GraphicsNode relatedNode = null;

   public GraphicsNodeMouseEvent(GraphicsNode var1, int var2, long var3, int var5, int var6, int var7, float var8, float var9, int var10, int var11, int var12, int var13, int var14, GraphicsNode var15) {
      super(var1, var2, var3, var5, var6);
      this.button = var7;
      this.x = var8;
      this.y = var9;
      this.clientX = var10;
      this.clientY = var11;
      this.screenX = var12;
      this.screenY = var13;
      this.clickCount = var14;
      this.relatedNode = var15;
   }

   public GraphicsNodeMouseEvent(GraphicsNode var1, MouseEvent var2, int var3, int var4) {
      super(var1, var2, var4);
      this.button = var3;
      this.x = (float)var2.getX();
      this.y = (float)var2.getY();
      this.clickCount = var2.getClickCount();
   }

   public int getButton() {
      return this.button;
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }

   public float getClientX() {
      return (float)this.clientX;
   }

   public float getClientY() {
      return (float)this.clientY;
   }

   public int getScreenX() {
      return this.screenX;
   }

   public int getScreenY() {
      return this.screenY;
   }

   public Point getScreenPoint() {
      return new Point(this.screenX, this.screenY);
   }

   public Point getClientPoint() {
      return new Point(this.clientX, this.clientY);
   }

   public Point2D getPoint2D() {
      return new Point2D.Float(this.x, this.y);
   }

   public int getClickCount() {
      return this.clickCount;
   }

   public GraphicsNode getRelatedNode() {
      return this.relatedNode;
   }
}
