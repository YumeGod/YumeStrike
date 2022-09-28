package org.apache.batik.gvt.text;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import org.apache.batik.ext.awt.geom.PathLength;

public class TextPath {
   private PathLength pathLength;
   private float startOffset;

   public TextPath(GeneralPath var1) {
      this.pathLength = new PathLength(var1);
      this.startOffset = 0.0F;
   }

   public void setStartOffset(float var1) {
      this.startOffset = var1;
   }

   public float getStartOffset() {
      return this.startOffset;
   }

   public float lengthOfPath() {
      return this.pathLength.lengthOfPath();
   }

   public float angleAtLength(float var1) {
      return this.pathLength.angleAtLength(var1);
   }

   public Point2D pointAtLength(float var1) {
      return this.pathLength.pointAtLength(var1);
   }
}
