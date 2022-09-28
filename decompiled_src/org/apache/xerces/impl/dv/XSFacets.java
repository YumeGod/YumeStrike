package org.apache.xerces.impl.dv;

import java.util.Vector;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSObjectList;

public class XSFacets {
   public int length;
   public int minLength;
   public int maxLength;
   public short whiteSpace;
   public int totalDigits;
   public int fractionDigits;
   public String pattern;
   public Vector enumeration;
   public Vector enumNSDecls;
   public String maxInclusive;
   public String maxExclusive;
   public String minInclusive;
   public String minExclusive;
   public XSAnnotation lengthAnnotation;
   public XSAnnotation minLengthAnnotation;
   public XSAnnotation maxLengthAnnotation;
   public XSAnnotation whiteSpaceAnnotation;
   public XSAnnotation totalDigitsAnnotation;
   public XSAnnotation fractionDigitsAnnotation;
   public XSObjectListImpl patternAnnotations;
   public XSObjectList enumAnnotations;
   public XSAnnotation maxInclusiveAnnotation;
   public XSAnnotation maxExclusiveAnnotation;
   public XSAnnotation minInclusiveAnnotation;
   public XSAnnotation minExclusiveAnnotation;

   public void reset() {
      this.lengthAnnotation = null;
      this.minLengthAnnotation = null;
      this.maxLengthAnnotation = null;
      this.whiteSpaceAnnotation = null;
      this.totalDigitsAnnotation = null;
      this.fractionDigitsAnnotation = null;
      this.patternAnnotations = null;
      this.enumAnnotations = null;
      this.maxInclusiveAnnotation = null;
      this.maxExclusiveAnnotation = null;
      this.minInclusiveAnnotation = null;
      this.minExclusiveAnnotation = null;
   }
}
