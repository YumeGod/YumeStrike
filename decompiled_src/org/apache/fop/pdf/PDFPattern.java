package org.apache.fop.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PDFPattern extends PDFPathPaint {
   protected PDFResources resources = null;
   protected int patternType = 2;
   protected String patternName = null;
   protected int paintType = 2;
   protected int tilingType = 1;
   protected List bBox = null;
   protected double xStep = -1.0;
   protected double yStep = -1.0;
   protected PDFShading shading = null;
   protected List xUID = null;
   protected StringBuffer extGState = null;
   protected List matrix = null;
   protected StringBuffer patternDataStream = null;

   public PDFPattern(PDFResources theResources, int thePatternType, int thePaintType, int theTilingType, List theBBox, double theXStep, double theYStep, List theMatrix, List theXUID, StringBuffer thePatternDataStream) {
      this.resources = theResources;
      this.patternType = 1;
      this.paintType = thePaintType;
      this.tilingType = theTilingType;
      this.bBox = theBBox;
      this.xStep = theXStep;
      this.yStep = theYStep;
      this.matrix = theMatrix;
      this.xUID = theXUID;
      this.patternDataStream = thePatternDataStream;
   }

   public PDFPattern(int thePatternType, PDFShading theShading, List theXUID, StringBuffer theExtGState, List theMatrix) {
      this.patternType = 2;
      this.shading = theShading;
      this.xUID = theXUID;
      this.extGState = theExtGState;
      this.matrix = theMatrix;
   }

   public String getName() {
      return this.patternName;
   }

   public void setName(String name) {
      if (name.indexOf(" ") >= 0) {
         throw new IllegalArgumentException("Pattern name must not contain any spaces");
      } else {
         this.patternName = name;
      }
   }

   public String getColorSpaceOut(boolean fillNotStroke) {
      return fillNotStroke ? "/Pattern cs /" + this.getName() + " scn \n" : "/Pattern CS /" + this.getName() + " SCN \n";
   }

   protected int output(OutputStream stream) throws IOException {
      int vectorSize = false;
      int tempInt = false;
      StringBuffer p = new StringBuffer(64);
      p.append(this.getObjectID());
      p.append("<< \n/Type /Pattern \n");
      if (this.resources != null) {
         p.append("/Resources " + this.resources.referencePDF() + " \n");
      }

      p.append("/PatternType " + this.patternType + " \n");
      PDFStream pdfStream = null;
      StreamCache encodedStream = null;
      int vectorSize;
      int tempInt;
      if (this.patternType == 1) {
         p.append("/PaintType " + this.paintType + " \n");
         p.append("/TilingType " + this.tilingType + " \n");
         if (this.bBox != null) {
            vectorSize = this.bBox.size();
            p.append("/BBox [ ");

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.bBox.get(tempInt)));
               p.append(" ");
            }

            p.append("] \n");
         }

         p.append("/XStep " + PDFNumber.doubleOut(new Double(this.xStep)) + " \n");
         p.append("/YStep " + PDFNumber.doubleOut(new Double(this.yStep)) + " \n");
         if (this.matrix != null) {
            vectorSize = this.matrix.size();
            p.append("/Matrix [ ");

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.matrix.get(tempInt), 8));
               p.append(" ");
            }

            p.append("] \n");
         }

         if (this.xUID != null) {
            vectorSize = this.xUID.size();
            p.append("/XUID [ ");

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append((Integer)this.xUID.get(tempInt) + " ");
            }

            p.append("] \n");
         }

         if (this.patternDataStream != null) {
            pdfStream = new PDFStream();
            pdfStream.setDocument(this.getDocumentSafely());
            pdfStream.add(this.patternDataStream.toString());
            pdfStream.getFilterList().addDefaultFilters(this.getDocument().getFilterMap(), "content");
            encodedStream = pdfStream.encodeStream();
            p.append(pdfStream.getFilterList().buildFilterDictEntries());
            p.append("/Length " + (encodedStream.getSize() + 1) + " \n");
         }
      } else {
         if (this.shading != null) {
            p.append("/Shading " + this.shading.referencePDF() + " \n");
         }

         if (this.xUID != null) {
            vectorSize = this.xUID.size();
            p.append("/XUID [ ");

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append((Integer)this.xUID.get(tempInt) + " ");
            }

            p.append("] \n");
         }

         if (this.extGState != null) {
            p.append("/ExtGState " + this.extGState + " \n");
         }

         if (this.matrix != null) {
            vectorSize = this.matrix.size();
            p.append("/Matrix [ ");

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.matrix.get(tempInt), 8));
               p.append(" ");
            }

            p.append("] \n");
         }
      }

      p.append(">> \n");
      byte[] buffer = encode(p.toString());
      int length = buffer.length;
      stream.write(buffer);
      if (pdfStream != null) {
         length += pdfStream.outputStreamData(encodedStream, stream);
      }

      buffer = encode("\nendobj\n");
      stream.write(buffer);
      length += buffer.length;
      return length;
   }

   public byte[] toPDF() {
      return null;
   }

   protected boolean contentEquals(PDFObject obj) {
      if (obj == null) {
         return false;
      } else if (obj == this) {
         return true;
      } else if (!(obj instanceof PDFPattern)) {
         return false;
      } else {
         PDFPattern patt = (PDFPattern)obj;
         if (this.patternType != patt.patternType) {
            return false;
         } else if (this.paintType != patt.paintType) {
            return false;
         } else if (this.tilingType != patt.tilingType) {
            return false;
         } else if (this.xStep != patt.xStep) {
            return false;
         } else if (this.yStep != patt.yStep) {
            return false;
         } else {
            if (this.bBox != null) {
               if (!this.bBox.equals(patt.bBox)) {
                  return false;
               }
            } else if (patt.bBox != null) {
               return false;
            }

            if (this.bBox != null) {
               if (!this.bBox.equals(patt.bBox)) {
                  return false;
               }
            } else if (patt.bBox != null) {
               return false;
            }

            if (this.xUID != null) {
               if (!this.xUID.equals(patt.xUID)) {
                  return false;
               }
            } else if (patt.xUID != null) {
               return false;
            }

            if (this.extGState != null) {
               if (!this.extGState.equals(patt.extGState)) {
                  return false;
               }
            } else if (patt.extGState != null) {
               return false;
            }

            if (this.matrix != null) {
               if (!this.matrix.equals(patt.matrix)) {
                  return false;
               }
            } else if (patt.matrix != null) {
               return false;
            }

            if (this.resources != null) {
               if (!this.resources.equals(patt.resources)) {
                  return false;
               }
            } else if (patt.resources != null) {
               return false;
            }

            if (this.shading != null) {
               if (!this.shading.equals(patt.shading)) {
                  return false;
               }
            } else if (patt.shading != null) {
               return false;
            }

            if (this.patternDataStream != null) {
               if (!this.patternDataStream.equals(patt.patternDataStream)) {
                  return false;
               }
            } else if (patt.patternDataStream != null) {
               return false;
            }

            return true;
         }
      }
   }
}
