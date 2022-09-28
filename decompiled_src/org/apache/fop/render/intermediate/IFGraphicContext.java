package org.apache.fop.render.intermediate;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlgraphics.java2d.GraphicContext;

public class IFGraphicContext extends GraphicContext {
   private static final AffineTransform[] EMPTY_TRANSFORM_ARRAY = new AffineTransform[0];
   private List groupList = new ArrayList();

   public IFGraphicContext() {
   }

   protected IFGraphicContext(IFGraphicContext graphicContext) {
      super((GraphicContext)graphicContext);
   }

   public Object clone() {
      return new IFGraphicContext(this);
   }

   public void pushGroup(Group group) {
      this.groupList.add(group);
      int i = 0;

      for(int c = group.getTransforms().length; i < c; ++i) {
         this.transform(group.getTransforms()[i]);
      }

   }

   public Group[] getGroups() {
      return (Group[])this.groupList.toArray(new Group[this.getGroupStackSize()]);
   }

   public Group[] dropGroups() {
      Group[] groups = this.getGroups();
      this.groupList.clear();
      return groups;
   }

   public int getGroupStackSize() {
      return this.groupList.size();
   }

   public static class Viewport extends Group {
      private Dimension size;
      private Rectangle clipRect;

      public Viewport(AffineTransform[] transforms, Dimension size, Rectangle clipRect) {
         super(transforms);
         this.size = size;
         this.clipRect = clipRect;
      }

      public Viewport(AffineTransform transform, Dimension size, Rectangle clipRect) {
         this(new AffineTransform[]{transform}, size, clipRect);
      }

      public Dimension getSize() {
         return this.size;
      }

      public Rectangle getClipRect() {
         return this.clipRect;
      }

      public void start(IFPainter painter) throws IFException {
         painter.startViewport(this.getTransforms(), this.size, this.clipRect);
      }

      public void end(IFPainter painter) throws IFException {
         painter.endViewport();
      }

      public String toString() {
         StringBuffer sb = new StringBuffer("viewport: ");
         IFUtil.toString(this.getTransforms(), sb);
         sb.append(", ").append(this.getSize());
         if (this.getClipRect() != null) {
            sb.append(", ").append(this.getClipRect());
         }

         return sb.toString();
      }
   }

   public static class Group {
      private AffineTransform[] transforms;

      public Group(AffineTransform[] transforms) {
         this.transforms = transforms;
      }

      public Group(AffineTransform transform) {
         this(new AffineTransform[]{transform});
      }

      public Group() {
         this(IFGraphicContext.EMPTY_TRANSFORM_ARRAY);
      }

      public AffineTransform[] getTransforms() {
         return this.transforms;
      }

      public void start(IFPainter painter) throws IFException {
         painter.startGroup(this.transforms);
      }

      public void end(IFPainter painter) throws IFException {
         painter.endGroup();
      }

      public String toString() {
         StringBuffer sb = new StringBuffer("group: ");
         IFUtil.toString(this.getTransforms(), sb);
         return sb.toString();
      }
   }
}
