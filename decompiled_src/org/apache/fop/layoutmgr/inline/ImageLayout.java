package org.apache.fop.layoutmgr.inline;

import java.awt.Dimension;
import java.awt.Rectangle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.Constants;
import org.apache.fop.fo.GraphicsProperties;
import org.apache.fop.fo.properties.LengthRangeProperty;

public class ImageLayout implements Constants {
   protected static Log log;
   private GraphicsProperties props;
   private PercentBaseContext percentBaseContext;
   private Dimension intrinsicSize;
   private Rectangle placement;
   private Dimension viewportSize = new Dimension(-1, -1);
   private boolean clip;

   public ImageLayout(GraphicsProperties props, PercentBaseContext percentBaseContext, Dimension intrinsicSize) {
      this.props = props;
      this.percentBaseContext = percentBaseContext;
      this.intrinsicSize = intrinsicSize;
      this.doLayout();
   }

   protected void doLayout() {
      int bpd = -1;
      int ipd = -1;
      Length len = this.props.getBlockProgressionDimension().getOptimum(this.percentBaseContext).getLength();
      if (len.getEnum() != 9) {
         bpd = len.getValue(this.percentBaseContext);
      }

      len = this.props.getBlockProgressionDimension().getMinimum(this.percentBaseContext).getLength();
      if (bpd == -1 && len.getEnum() != 9) {
         bpd = len.getValue(this.percentBaseContext);
      }

      len = this.props.getInlineProgressionDimension().getOptimum(this.percentBaseContext).getLength();
      if (len.getEnum() != 9) {
         ipd = len.getValue(this.percentBaseContext);
      }

      len = this.props.getInlineProgressionDimension().getMinimum(this.percentBaseContext).getLength();
      if (ipd == -1 && len.getEnum() != 9) {
         ipd = len.getValue(this.percentBaseContext);
      }

      boolean constrainIntrinsicSize = false;
      int cwidth = -1;
      int cheight = -1;
      len = this.props.getContentWidth();
      if (len.getEnum() != 9) {
         switch (len.getEnum()) {
            case 125:
               if (ipd != -1) {
                  cwidth = ipd;
               }

               constrainIntrinsicSize = true;
               break;
            case 187:
               if (ipd != -1 && this.intrinsicSize.width > ipd) {
                  cwidth = ipd;
               }

               constrainIntrinsicSize = true;
               break;
            case 188:
               if (ipd != -1 && this.intrinsicSize.width < ipd) {
                  cwidth = ipd;
               }

               constrainIntrinsicSize = true;
               break;
            default:
               cwidth = len.getValue(this.percentBaseContext);
         }
      }

      len = this.props.getContentHeight();
      if (len.getEnum() != 9) {
         switch (len.getEnum()) {
            case 125:
               if (bpd != -1) {
                  cheight = bpd;
               }

               constrainIntrinsicSize = true;
               break;
            case 187:
               if (bpd != -1 && this.intrinsicSize.height > bpd) {
                  cheight = bpd;
               }

               constrainIntrinsicSize = true;
               break;
            case 188:
               if (bpd != -1 && this.intrinsicSize.height < bpd) {
                  cheight = bpd;
               }

               constrainIntrinsicSize = true;
               break;
            default:
               cheight = len.getValue(this.percentBaseContext);
         }
      }

      Dimension constrainedIntrinsicSize;
      if (constrainIntrinsicSize) {
         constrainedIntrinsicSize = this.constrain(this.intrinsicSize);
      } else {
         constrainedIntrinsicSize = this.intrinsicSize;
      }

      Dimension adjustedDim = this.adjustContentSize(cwidth, cheight, constrainedIntrinsicSize);
      cwidth = adjustedDim.width;
      cheight = adjustedDim.height;
      if (ipd == -1) {
         ipd = this.constrainExtent(cwidth, this.props.getInlineProgressionDimension(), this.props.getContentWidth());
      }

      if (bpd == -1) {
         bpd = this.constrainExtent(cheight, this.props.getBlockProgressionDimension(), this.props.getContentHeight());
      }

      this.clip = false;
      int overflow = this.props.getOverflow();
      if (overflow == 57) {
         this.clip = true;
      } else if (overflow == 42) {
         if (cwidth > ipd || cheight > bpd) {
            log.error("Object overflows the viewport: clipping");
         }

         this.clip = true;
      }

      int xoffset = this.computeXOffset(ipd, cwidth);
      int yoffset = this.computeYOffset(bpd, cheight);
      this.viewportSize.setSize(ipd, bpd);
      this.placement = new Rectangle(xoffset, yoffset, cwidth, cheight);
   }

   private int constrainExtent(int extent, LengthRangeProperty range, Length contextExtent) {
      boolean mayScaleUp = contextExtent.getEnum() != 187;
      boolean mayScaleDown = contextExtent.getEnum() != 188;
      Length len = range.getMaximum(this.percentBaseContext).getLength();
      int min;
      if (len.getEnum() != 9) {
         min = len.getValue(this.percentBaseContext);
         if (min != -1 && mayScaleDown) {
            extent = Math.min(extent, min);
         }
      }

      len = range.getMinimum(this.percentBaseContext).getLength();
      if (len.getEnum() != 9) {
         min = len.getValue(this.percentBaseContext);
         if (min != -1 && mayScaleUp) {
            extent = Math.max(extent, min);
         }
      }

      return extent;
   }

   private Dimension constrain(Dimension size) {
      Dimension adjusted = new Dimension(size);
      int effWidth = this.constrainExtent(size.width, this.props.getInlineProgressionDimension(), this.props.getContentWidth());
      int effHeight = this.constrainExtent(size.height, this.props.getBlockProgressionDimension(), this.props.getContentHeight());
      int scaling = this.props.getScaling();
      if (scaling == 154) {
         double rat1 = (double)effWidth / (double)size.width;
         double rat2 = (double)effHeight / (double)size.height;
         if (rat1 < rat2) {
            adjusted.width = effWidth;
            adjusted.height = (int)(rat1 * (double)size.height);
         } else if (rat1 > rat2) {
            adjusted.width = (int)(rat2 * (double)size.width);
            adjusted.height = effHeight;
         }
      } else {
         adjusted.width = effWidth;
         adjusted.height = effHeight;
      }

      return adjusted;
   }

   private Dimension adjustContentSize(int cwidth, int cheight, Dimension defaultSize) {
      Dimension dim = new Dimension(cwidth, cheight);
      int scaling = this.props.getScaling();
      if (scaling == 154 || cwidth == -1 || cheight == -1) {
         if (cwidth == -1 && cheight == -1) {
            dim.width = defaultSize.width;
            dim.height = defaultSize.height;
         } else if (cwidth == -1) {
            if (defaultSize.height == 0) {
               dim.width = 0;
            } else {
               dim.width = (int)((double)defaultSize.width * (double)cheight / (double)defaultSize.height);
            }
         } else if (cheight == -1) {
            if (defaultSize.width == 0) {
               dim.height = 0;
            } else {
               dim.height = (int)((double)defaultSize.height * (double)cwidth / (double)defaultSize.width);
            }
         } else if (defaultSize.width != 0 && defaultSize.height != 0) {
            double rat1 = (double)cwidth / (double)defaultSize.width;
            double rat2 = (double)cheight / (double)defaultSize.height;
            if (rat1 < rat2) {
               dim.height = (int)(rat1 * (double)defaultSize.height);
            } else if (rat1 > rat2) {
               dim.width = (int)(rat2 * (double)defaultSize.width);
            }
         } else {
            dim.width = 0;
            dim.height = 0;
         }
      }

      return dim;
   }

   public int computeXOffset(int ipd, int cwidth) {
      int xoffset = 0;
      switch (this.props.getTextAlign()) {
         case 23:
            xoffset = (ipd - cwidth) / 2;
            break;
         case 39:
            xoffset = ipd - cwidth;
         case 70:
         case 135:
      }

      return xoffset;
   }

   public int computeYOffset(int bpd, int cheight) {
      int yoffset = 0;
      switch (this.props.getDisplayAlign()) {
         case 3:
            yoffset = bpd - cheight;
         case 9:
         case 13:
         default:
            break;
         case 23:
            yoffset = (bpd - cheight) / 2;
      }

      return yoffset;
   }

   public Rectangle getPlacement() {
      return this.placement;
   }

   public Dimension getViewportSize() {
      return this.viewportSize;
   }

   public Dimension getIntrinsicSize() {
      return this.intrinsicSize;
   }

   public boolean isClipped() {
      return this.clip;
   }

   static {
      log = LogFactory.getLog(ImageLayout.class);
   }
}
