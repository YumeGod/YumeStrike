package org.apache.fop.pdf;

import java.util.List;

public class PDFFunction extends PDFObject {
   protected int functionType = 0;
   protected List domain = null;
   protected List range = null;
   protected List size = null;
   protected int bitsPerSample = 1;
   protected int order = 1;
   protected List encode = null;
   protected List decode = null;
   protected StringBuffer functionDataStream = null;
   protected List filter = null;
   protected List cZero = null;
   protected List cOne = null;
   protected double interpolationExponentN = 1.0;
   protected List functions = null;
   protected List bounds = null;

   public PDFFunction(int theFunctionType, List theDomain, List theRange, List theSize, int theBitsPerSample, int theOrder, List theEncode, List theDecode, StringBuffer theFunctionDataStream, List theFilter) {
      this.functionType = 0;
      this.size = theSize;
      this.bitsPerSample = theBitsPerSample;
      this.order = theOrder;
      this.encode = theEncode;
      this.decode = theDecode;
      this.functionDataStream = theFunctionDataStream;
      this.filter = theFilter;
      this.domain = theDomain;
      this.range = theRange;
   }

   public PDFFunction(int theFunctionType, List theDomain, List theRange, List theCZero, List theCOne, double theInterpolationExponentN) {
      this.functionType = 2;
      this.cZero = theCZero;
      this.cOne = theCOne;
      this.interpolationExponentN = theInterpolationExponentN;
      this.domain = theDomain;
      this.range = theRange;
   }

   public PDFFunction(int theFunctionType, List theDomain, List theRange, List theFunctions, List theBounds, List theEncode) {
      this.functionType = 3;
      this.functions = theFunctions;
      this.bounds = theBounds;
      this.encode = theEncode;
      this.domain = theDomain;
      this.range = theRange;
   }

   public PDFFunction(int theFunctionType, List theDomain, List theRange, StringBuffer theFunctionDataStream) {
      this.functionType = 4;
      this.functionDataStream = theFunctionDataStream;
      this.domain = theDomain;
      this.range = theRange;
   }

   public byte[] toPDF() {
      int vectorSize = false;
      int numberOfFunctions = 0;
      int tempInt = false;
      StringBuffer p = new StringBuffer(256);
      p.append(this.getObjectID() + "<< \n/FunctionType " + this.functionType + " \n");
      int vectorSize;
      int tempInt;
      if (this.functionType == 0) {
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

         if (this.size != null) {
            p.append("/Size [ ");
            vectorSize = this.size.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.size.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         if (this.encode != null) {
            p.append("/Encode [ ");
            vectorSize = this.encode.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.encode.get(tempInt)) + " ");
            }

            p.append("] \n");
         } else {
            p.append("/Encode [ ");
            vectorSize = this.functions.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append("0 1 ");
            }

            p.append("] \n");
         }

         p.append("/BitsPerSample " + this.bitsPerSample);
         if (this.order == 1 || this.order == 3) {
            p.append(" \n/Order " + this.order + " \n");
         }

         if (this.range != null) {
            p.append("/Range [ ");
            vectorSize = this.range.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.range.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         if (this.decode != null) {
            p.append("/Decode [ ");
            vectorSize = this.decode.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.decode.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         if (this.functionDataStream != null) {
            p.append("/Length " + (this.functionDataStream.length() + 1) + " \n");
         }

         if (this.filter != null) {
            vectorSize = this.filter.size();
            p.append("/Filter ");
            if (vectorSize == 1) {
               p.append("/" + (String)this.filter.get(0) + " \n");
            } else {
               p.append("[ ");

               for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
                  p.append("/" + (String)this.filter.get(0) + " ");
               }

               p.append("] \n");
            }
         }

         p.append(">> \n");
         if (this.functionDataStream != null) {
            p.append("stream\n" + this.functionDataStream + "\nendstream\n");
         }

         p.append("endobj\n");
      } else if (this.functionType == 2) {
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

         if (this.range != null) {
            p.append("/Range [ ");
            vectorSize = this.range.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.range.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         if (this.cZero != null) {
            p.append("/C0 [ ");
            vectorSize = this.cZero.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.cZero.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         if (this.cOne != null) {
            p.append("/C1 [ ");
            vectorSize = this.cOne.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.cOne.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         p.append("/N " + PDFNumber.doubleOut(new Double(this.interpolationExponentN)) + " \n");
         p.append(">> \nendobj\n");
      } else if (this.functionType == 3) {
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

         if (this.range != null) {
            p.append("/Range [ ");
            vectorSize = this.range.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.range.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         if (this.functions != null) {
            p.append("/Functions [ ");
            numberOfFunctions = this.functions.size();

            for(tempInt = 0; tempInt < numberOfFunctions; ++tempInt) {
               p.append(((PDFFunction)this.functions.get(tempInt)).referencePDF() + " ");
            }

            p.append("] \n");
         }

         if (this.encode != null) {
            p.append("/Encode [ ");
            vectorSize = this.encode.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.encode.get(tempInt)) + " ");
            }

            p.append("] \n");
         } else {
            p.append("/Encode [ ");
            vectorSize = this.functions.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append("0 1 ");
            }

            p.append("] \n");
         }

         p.append("/Bounds [ ");
         if (this.bounds != null) {
            vectorSize = this.bounds.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.bounds.get(tempInt)) + " ");
            }
         } else if (this.functions != null) {
            String functionsFraction = PDFNumber.doubleOut(new Double(1.0 / (double)numberOfFunctions));

            for(tempInt = 0; tempInt + 1 < numberOfFunctions; ++tempInt) {
               p.append(functionsFraction + " ");
            }

            functionsFraction = null;
         }

         p.append("] \n");
         p.append(">> \nendobj\n");
      } else if (this.functionType == 4) {
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

         if (this.range != null) {
            p.append("/Range [ ");
            vectorSize = this.range.size();

            for(tempInt = 0; tempInt < vectorSize; ++tempInt) {
               p.append(PDFNumber.doubleOut((Double)this.range.get(tempInt)) + " ");
            }

            p.append("] \n");
         }

         if (this.functionDataStream != null) {
            p.append("/Length " + (this.functionDataStream.length() + 1) + " \n");
         }

         p.append(">> \n");
         if (this.functionDataStream != null) {
            p.append("stream\n{ " + this.functionDataStream + " } \nendstream\n");
         }

         p.append("endobj\n");
      }

      return encode(p.toString());
   }

   protected boolean contentEquals(PDFObject obj) {
      if (obj == null) {
         return false;
      } else if (obj == this) {
         return true;
      } else if (!(obj instanceof PDFFunction)) {
         return false;
      } else {
         PDFFunction func = (PDFFunction)obj;
         if (this.functionType != func.functionType) {
            return false;
         } else if (this.bitsPerSample != func.bitsPerSample) {
            return false;
         } else if (this.order != func.order) {
            return false;
         } else if (this.interpolationExponentN != func.interpolationExponentN) {
            return false;
         } else {
            if (this.domain != null) {
               if (!this.domain.equals(func.domain)) {
                  return false;
               }
            } else if (func.domain != null) {
               return false;
            }

            if (this.range != null) {
               if (!this.range.equals(func.range)) {
                  return false;
               }
            } else if (func.range != null) {
               return false;
            }

            if (this.size != null) {
               if (!this.size.equals(func.size)) {
                  return false;
               }
            } else if (func.size != null) {
               return false;
            }

            if (this.encode != null) {
               if (!this.encode.equals(func.encode)) {
                  return false;
               }
            } else if (func.encode != null) {
               return false;
            }

            if (this.decode != null) {
               if (!this.decode.equals(func.decode)) {
                  return false;
               }
            } else if (func.decode != null) {
               return false;
            }

            if (this.functionDataStream != null) {
               if (!this.functionDataStream.equals(func.functionDataStream)) {
                  return false;
               }
            } else if (func.functionDataStream != null) {
               return false;
            }

            if (this.filter != null) {
               if (!this.filter.equals(func.filter)) {
                  return false;
               }
            } else if (func.filter != null) {
               return false;
            }

            if (this.cZero != null) {
               if (!this.cZero.equals(func.cZero)) {
                  return false;
               }
            } else if (func.cZero != null) {
               return false;
            }

            if (this.cOne != null) {
               if (!this.cOne.equals(func.cOne)) {
                  return false;
               }
            } else if (func.cOne != null) {
               return false;
            }

            if (this.functions != null) {
               if (!this.functions.equals(func.functions)) {
                  return false;
               }
            } else if (func.functions != null) {
               return false;
            }

            if (this.bounds != null) {
               if (!this.bounds.equals(func.bounds)) {
                  return false;
               }
            } else if (func.bounds != null) {
               return false;
            }

            return true;
         }
      }
   }
}
