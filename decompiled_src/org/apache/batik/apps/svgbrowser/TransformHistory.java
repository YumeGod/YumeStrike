package org.apache.batik.apps.svgbrowser;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class TransformHistory {
   protected List transforms = new ArrayList();
   protected int position = -1;

   public void back() {
      this.position -= 2;
   }

   public boolean canGoBack() {
      return this.position > 0;
   }

   public void forward() {
   }

   public boolean canGoForward() {
      return this.position < this.transforms.size() - 1;
   }

   public AffineTransform currentTransform() {
      return (AffineTransform)this.transforms.get(this.position + 1);
   }

   public void update(AffineTransform var1) {
      if (this.position < -1) {
         this.position = -1;
      }

      if (++this.position < this.transforms.size()) {
         if (!this.transforms.get(this.position).equals(var1)) {
            this.transforms = this.transforms.subList(0, this.position + 1);
         }

         this.transforms.set(this.position, var1);
      } else {
         this.transforms.add(var1);
      }

   }
}
