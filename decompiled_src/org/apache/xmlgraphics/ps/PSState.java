package org.apache.xmlgraphics.ps;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PSState implements Serializable {
   public static final String DEFAULT_DASH = "[] 0";
   public static final Color DEFAULT_RGB_COLOR;
   private AffineTransform transform = new AffineTransform();
   private List transformConcatList = new ArrayList();
   private int linecap = 0;
   private int linejoin = 0;
   private float miterLimit = 0.0F;
   private double linewidth = 1.0;
   private String dashpattern = "[] 0";
   private Color color;
   private String fontname;
   private float fontsize;

   public PSState() {
      this.color = DEFAULT_RGB_COLOR;
   }

   public PSState(PSState org, boolean copyTransforms) {
      this.color = DEFAULT_RGB_COLOR;
      this.transform = (AffineTransform)org.transform.clone();
      if (copyTransforms) {
         this.transformConcatList.addAll(org.transformConcatList);
      }

      this.linecap = org.linecap;
      this.linejoin = org.linejoin;
      this.miterLimit = org.miterLimit;
      this.linewidth = org.linewidth;
      this.dashpattern = org.dashpattern;
      this.color = org.color;
      this.fontname = org.fontname;
      this.fontsize = org.fontsize;
   }

   public AffineTransform getTransform() {
      return this.transform;
   }

   public boolean checkTransform(AffineTransform tf) {
      return !tf.equals(this.transform);
   }

   public void concatMatrix(AffineTransform transform) {
      this.transformConcatList.add(transform);
      this.transform.concatenate(transform);
   }

   public boolean useLineCap(int value) {
      if (this.linecap != value) {
         this.linecap = value;
         return true;
      } else {
         return false;
      }
   }

   public boolean useLineJoin(int value) {
      if (this.linejoin != value) {
         this.linejoin = value;
         return true;
      } else {
         return false;
      }
   }

   public boolean useMiterLimit(float value) {
      if (this.miterLimit != value) {
         this.miterLimit = value;
         return true;
      } else {
         return false;
      }
   }

   public boolean useLineWidth(double value) {
      if (this.linewidth != value) {
         this.linewidth = value;
         return true;
      } else {
         return false;
      }
   }

   public boolean useDash(String pattern) {
      if (!this.dashpattern.equals(pattern)) {
         this.dashpattern = pattern;
         return true;
      } else {
         return false;
      }
   }

   public boolean useColor(Color value) {
      if (!this.color.equals(value)) {
         this.color = value;
         return true;
      } else {
         return false;
      }
   }

   public boolean useFont(String name, float size) {
      if (name == null) {
         throw new NullPointerException("font name must not be null");
      } else if (this.fontname != null && this.fontname.equals(name) && this.fontsize == size) {
         return false;
      } else {
         this.fontname = name;
         this.fontsize = size;
         return true;
      }
   }

   public void reestablish(PSGenerator gen) throws IOException {
      int i = 0;

      for(int len = this.transformConcatList.size(); i < len; ++i) {
         gen.concatMatrix((AffineTransform)this.transformConcatList.get(i));
      }

      gen.useLineCap(this.linecap);
      gen.useLineWidth(this.linewidth);
      gen.useDash(this.dashpattern);
      gen.useColor(this.color);
      if (this.fontname != null) {
         gen.useFont(this.fontname, this.fontsize);
      }

   }

   static {
      DEFAULT_RGB_COLOR = Color.black;
   }
}
