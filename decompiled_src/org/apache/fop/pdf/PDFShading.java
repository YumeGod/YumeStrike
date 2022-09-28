package org.apache.fop.pdf;

import java.util.List;

public class PDFShading extends PDFObject {
   protected String shadingName = null;
   protected int shadingType = 3;
   protected PDFDeviceColorSpace colorSpace = null;
   protected List background = null;
   protected List bBox = null;
   protected boolean antiAlias = false;
   protected List domain = null;
   protected List matrix = null;
   protected PDFFunction function = null;
   protected List coords = null;
   protected List extend = null;
   protected int bitsPerCoordinate = 0;
   protected int bitsPerFlag = 0;
   protected List decode = null;
   protected int bitsPerComponent = 0;
   protected int verticesPerRow = 0;

   public PDFShading(int theShadingType, PDFDeviceColorSpace theColorSpace, List theBackground, List theBBox, boolean theAntiAlias, List theDomain, List theMatrix, PDFFunction theFunction) {
      this.shadingType = theShadingType;
      this.colorSpace = theColorSpace;
      this.background = theBackground;
      this.bBox = theBBox;
      this.antiAlias = theAntiAlias;
      this.domain = theDomain;
      this.matrix = theMatrix;
      this.function = theFunction;
   }

   public PDFShading(int theShadingType, PDFDeviceColorSpace theColorSpace, List theBackground, List theBBox, boolean theAntiAlias, List theCoords, List theDomain, PDFFunction theFunction, List theExtend) {
      this.shadingType = theShadingType;
      this.colorSpace = theColorSpace;
      this.background = theBackground;
      this.bBox = theBBox;
      this.antiAlias = theAntiAlias;
      this.coords = theCoords;
      this.domain = theDomain;
      this.function = theFunction;
      this.extend = theExtend;
   }

   public PDFShading(int theShadingType, PDFDeviceColorSpace theColorSpace, List theBackground, List theBBox, boolean theAntiAlias, int theBitsPerCoordinate, int theBitsPerComponent, int theBitsPerFlag, List theDecode, PDFFunction theFunction) {
      this.shadingType = theShadingType;
      this.colorSpace = theColorSpace;
      this.background = theBackground;
      this.bBox = theBBox;
      this.antiAlias = theAntiAlias;
      this.bitsPerCoordinate = theBitsPerCoordinate;
      this.bitsPerComponent = theBitsPerComponent;
      this.bitsPerFlag = theBitsPerFlag;
      this.decode = theDecode;
      this.function = theFunction;
   }

   public PDFShading(int theShadingType, PDFDeviceColorSpace theColorSpace, List theBackground, List theBBox, boolean theAntiAlias, int theBitsPerCoordinate, int theBitsPerComponent, List theDecode, int theVerticesPerRow, PDFFunction theFunction) {
      this.shadingType = theShadingType;
      this.colorSpace = theColorSpace;
      this.background = theBackground;
      this.bBox = theBBox;
      this.antiAlias = theAntiAlias;
      this.bitsPerCoordinate = theBitsPerCoordinate;
      this.bitsPerComponent = theBitsPerComponent;
      this.decode = theDecode;
      this.verticesPerRow = theVerticesPerRow;
      this.function = theFunction;
   }

   public String getName() {
      return this.shadingName;
   }

   public void setName(String name) {
      if (name.indexOf(" ") >= 0) {
         throw new IllegalArgumentException("Shading name must not contain any spaces");
      } else {
         this.shadingName = name;
      }
   }

   public String toPDFString() {
      StringBuffer p = new StringBuffer(128);
      p.append(this.getObjectID() + "<< \n/ShadingType " + this.shadingType + " \n");
      if (this.colorSpace != null) {
         p.append("/ColorSpace /" + this.colorSpace.getName() + " \n");
      }

      int vectorSize;
      int tempInt;
      if (this.background != null) {
         p.append("/Background [ ");
         vectorSize = this.background.size();

         for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
            p.append(PDFNumber.doubleOut((Double)this.background.get(tempInt)) + " ");
         }

         p.append("] \n");
      }

      if (this.bBox != null) {
         p.append("/BBox [ ");
         vectorSize = this.bBox.size();

         for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
            p.append(PDFNumber.doubleOut((Double)this.bBox.get(tempInt)) + " ");
         }

         p.append("] \n");
      }

      if (this.antiAlias) {
         p.append("/AntiAlias " + this.antiAlias + " \n");
      }

      if (this.shadingType == 1) {
         if (this.domain == null) {
            p.append("/Domain [ 0 1 ] \n");
         } else {
            p.append("/Domain [ ");
            vectorSize = this.domain.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.domain.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         if (this.matrix != null) {
            p.append("/Matrix [ ");
            vectorSize = this.matrix.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.matrix.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         if (this.function != null) {
            p.append("/Function ");
            p.append(this.function.referencePDF() + " \n");
         }
      } else if (this.shadingType != 2 && this.shadingType != 3) {
         if (this.shadingType != 4 && this.shadingType != 6 && this.shadingType != 7) {
            if (this.shadingType == 5) {
               if (this.bitsPerCoordinate > 0) {
                  p.append("/BitsPerCoordinate " + this.bitsPerCoordinate + " \n");
               } else {
                  p.append("/BitsPerCoordinate 1 \n");
               }

               if (this.bitsPerComponent > 0) {
                  p.append("/BitsPerComponent " + this.bitsPerComponent + " \n");
               } else {
                  p.append("/BitsPerComponent 1 \n");
               }

               if (this.decode != null) {
                  p.append("/Decode [ ");
                  vectorSize = this.decode.size();

                  for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
                     p.append((Boolean)this.decode.get(tempInt) + " ");
                  }

                  p.append("] \n");
               }

               if (this.function != null) {
                  p.append("/Function ");
                  p.append(this.function.referencePDF() + " \n");
               }

               if (this.verticesPerRow > 0) {
                  p.append("/VerticesPerRow " + this.verticesPerRow + " \n");
               } else {
                  p.append("/VerticesPerRow 2 \n");
               }
            }
         } else {
            if (this.bitsPerCoordinate > 0) {
               p.append("/BitsPerCoordinate " + this.bitsPerCoordinate + " \n");
            } else {
               p.append("/BitsPerCoordinate 1 \n");
            }

            if (this.bitsPerComponent > 0) {
               p.append("/BitsPerComponent " + this.bitsPerComponent + " \n");
            } else {
               p.append("/BitsPerComponent 1 \n");
            }

            if (this.bitsPerFlag > 0) {
               p.append("/BitsPerFlag " + this.bitsPerFlag + " \n");
            } else {
               p.append("/BitsPerFlag 2 \n");
            }

            if (this.decode != null) {
               p.append("/Decode [ ");
               vectorSize = this.decode.size();

               for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
                  p.append((Boolean)this.decode.get(tempInt) + " ");
               }

               p.append("] \n");
            }

            if (this.function != null) {
               p.append("/Function ");
               p.append(this.function.referencePDF() + " \n");
            }
         }
      } else {
         if (this.coords != null) {
            p.append("/Coords [ ");
            vectorSize = this.coords.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.coords.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         if (this.domain == null) {
            p.append("/Domain [ 0 1 ] \n");
         } else {
            p.append("/Domain [ ");
            vectorSize = this.domain.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.domain.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         if (this.extend == null) {
            p.append("/Extend [ true true ] \n");
         } else {
            p.append("/Extend [ ");
            vectorSize = this.extend.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append((Boolean)this.extend.get(tempInt) + " ");
            }

            p.append("] \n");
         }

         if (this.function != null) {
            p.append("/Function ");
            p.append(this.function.referencePDF() + " \n");
         }
      }

      p.append(">> \nendobj\n");
      return p.toString();
   }

   protected boolean contentEquals(PDFObject obj) {
      if (obj == null) {
         return false;
      } else if (obj == this) {
         return true;
      } else if (!(obj instanceof PDFShading)) {
         return false;
      } else {
         PDFShading shad = (PDFShading)obj;
         if (this.shadingType != shad.shadingType) {
            return false;
         } else if (this.antiAlias != shad.antiAlias) {
            return false;
         } else if (this.bitsPerCoordinate != shad.bitsPerCoordinate) {
            return false;
         } else if (this.bitsPerFlag != shad.bitsPerFlag) {
            return false;
         } else if (this.bitsPerComponent != shad.bitsPerComponent) {
            return false;
         } else if (this.verticesPerRow != shad.verticesPerRow) {
            return false;
         } else {
            if (this.colorSpace != null) {
               if (!this.colorSpace.equals(shad.colorSpace)) {
                  return false;
               }
            } else if (shad.colorSpace != null) {
               return false;
            }

            if (this.background != null) {
               if (!this.background.equals(shad.background)) {
                  return false;
               }
            } else if (shad.background != null) {
               return false;
            }

            if (this.bBox != null) {
               if (!this.bBox.equals(shad.bBox)) {
                  return false;
               }
            } else if (shad.bBox != null) {
               return false;
            }

            if (this.domain != null) {
               if (!this.domain.equals(shad.domain)) {
                  return false;
               }
            } else if (shad.domain != null) {
               return false;
            }

            if (this.matrix != null) {
               if (!this.matrix.equals(shad.matrix)) {
                  return false;
               }
            } else if (shad.matrix != null) {
               return false;
            }

            if (this.coords != null) {
               if (!this.coords.equals(shad.coords)) {
                  return false;
               }
            } else if (shad.coords != null) {
               return false;
            }

            if (this.extend != null) {
               if (!this.extend.equals(shad.extend)) {
                  return false;
               }
            } else if (shad.extend != null) {
               return false;
            }

            if (this.decode != null) {
               if (!this.decode.equals(shad.decode)) {
                  return false;
               }
            } else if (shad.decode != null) {
               return false;
            }

            if (this.function != null) {
               if (!this.function.equals(shad.function)) {
                  return false;
               }
            } else if (shad.function != null) {
               return false;
            }

            return true;
         }
      }
   }
}
