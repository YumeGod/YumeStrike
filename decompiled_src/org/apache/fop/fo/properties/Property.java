package org.apache.fop.fo.properties;

import java.awt.Color;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.Numeric;

public class Property {
   protected static Log log;
   private String specVal;

   public void setSpecifiedValue(String value) {
      this.specVal = value;
   }

   public String getSpecifiedValue() {
      return this.specVal;
   }

   public Length getLength() {
      return null;
   }

   public Color getColor(FOUserAgent foUserAgent) {
      return null;
   }

   public CondLengthProperty getCondLength() {
      return null;
   }

   public LengthRangeProperty getLengthRange() {
      return null;
   }

   public LengthPairProperty getLengthPair() {
      return null;
   }

   public SpaceProperty getSpace() {
      return null;
   }

   public KeepProperty getKeep() {
      return null;
   }

   public int getEnum() {
      return 0;
   }

   public boolean isAuto() {
      return this.getEnum() == 9;
   }

   public char getCharacter() {
      return '\u0000';
   }

   public List getList() {
      return null;
   }

   public Number getNumber() {
      return null;
   }

   public Numeric getNumeric() {
      return null;
   }

   public String getNCname() {
      return null;
   }

   public Object getObject() {
      return null;
   }

   public String getString() {
      return null;
   }

   public String toString() {
      Object obj = this.getObject();
      return obj != this ? obj.toString() : null;
   }

   static {
      log = LogFactory.getLog(PropertyMaker.class);
   }
}
