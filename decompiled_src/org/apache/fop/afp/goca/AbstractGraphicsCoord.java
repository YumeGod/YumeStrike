package org.apache.fop.afp.goca;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.fop.afp.util.BinaryUtils;

public abstract class AbstractGraphicsCoord extends AbstractGraphicsDrawingOrder {
   protected int[] coords;
   protected boolean relative;

   public AbstractGraphicsCoord(int[] coords) {
      this.coords = null;
      this.relative = false;
      if (coords == null) {
         this.relative = true;
      } else {
         this.coords = coords;
      }

   }

   public AbstractGraphicsCoord(int[] coords, boolean relative) {
      this(coords);
      this.relative = relative;
   }

   public AbstractGraphicsCoord(int x, int y) {
      this(new int[]{x, y});
   }

   public AbstractGraphicsCoord(int x1, int y1, int x2, int y2) {
      this(new int[]{x1, y1, x2, y2});
   }

   public int getDataLength() {
      return 2 + (this.coords != null ? this.coords.length * 2 : 0);
   }

   int getCoordinateDataStartIndex() {
      return 2;
   }

   byte[] getData() {
      byte[] data = super.getData();
      if (this.coords != null) {
         this.addCoords(data, this.getCoordinateDataStartIndex());
      }

      return data;
   }

   public void writeToStream(OutputStream os) throws IOException {
      os.write(this.getData());
   }

   protected void addCoords(byte[] data, int fromIndex) {
      for(int i = 0; i < this.coords.length; fromIndex += 2) {
         byte[] coord = BinaryUtils.convert(this.coords[i], 2);
         data[fromIndex] = coord[0];
         data[fromIndex + 1] = coord[1];
         ++i;
      }

   }

   public String toString() {
      String coordsStr = "";

      for(int i = 0; i < this.coords.length; ++i) {
         coordsStr = coordsStr + (i % 2 == 0 ? "x" : "y");
         coordsStr = coordsStr + i / 2 + "=" + this.coords[i] + ",";
      }

      coordsStr = coordsStr.substring(0, coordsStr.length() - 1);
      return this.getName() + "{" + coordsStr + "}";
   }

   protected boolean isRelative() {
      return this.relative;
   }
}
