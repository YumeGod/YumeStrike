package org.apache.fop.area;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.traits.BorderProps;

public class Area extends AreaTreeObject implements Serializable {
   public static final int LR = 0;
   public static final int RL = 1;
   public static final int TB = 2;
   public static final int BT = 3;
   public static final int ORIENT_0 = 0;
   public static final int ORIENT_90 = 1;
   public static final int ORIENT_180 = 2;
   public static final int ORIENT_270 = 3;
   public static final int CLASS_NORMAL = 0;
   public static final int CLASS_FIXED = 1;
   public static final int CLASS_ABSOLUTE = 2;
   public static final int CLASS_BEFORE_FLOAT = 3;
   public static final int CLASS_FOOTNOTE = 4;
   public static final int CLASS_SIDE_FLOAT = 5;
   public static final int CLASS_MAX = 6;
   private int areaClass = 0;
   protected int ipd;
   protected int bpd;
   protected Map props = null;
   protected static Log log;

   public int getAreaClass() {
      return this.areaClass;
   }

   public void setAreaClass(int areaClass) {
      this.areaClass = areaClass;
   }

   public void setIPD(int ipd) {
      this.ipd = ipd;
   }

   public int getIPD() {
      return this.ipd;
   }

   public void setBPD(int bpd) {
      this.bpd = bpd;
   }

   public int getBPD() {
      return this.bpd;
   }

   public int getAllocIPD() {
      return this.getBorderAndPaddingWidthStart() + this.getIPD() + this.getBorderAndPaddingWidthEnd();
   }

   public int getAllocBPD() {
      return this.getSpaceBefore() + this.getBorderAndPaddingWidthBefore() + this.getBPD() + this.getBorderAndPaddingWidthAfter() + this.getSpaceAfter();
   }

   public int getBorderAndPaddingWidthBefore() {
      int margin = 0;
      BorderProps bps = (BorderProps)this.getTrait(Trait.BORDER_BEFORE);
      if (bps != null) {
         margin = bps.width;
      }

      Integer padWidth = (Integer)this.getTrait(Trait.PADDING_BEFORE);
      if (padWidth != null) {
         margin += padWidth;
      }

      return margin;
   }

   public int getBorderAndPaddingWidthAfter() {
      int margin = 0;
      BorderProps bps = (BorderProps)this.getTrait(Trait.BORDER_AFTER);
      if (bps != null) {
         margin = bps.width;
      }

      Integer padWidth = (Integer)this.getTrait(Trait.PADDING_AFTER);
      if (padWidth != null) {
         margin += padWidth;
      }

      return margin;
   }

   public int getBorderAndPaddingWidthStart() {
      int margin = 0;
      BorderProps bps = (BorderProps)this.getTrait(Trait.BORDER_START);
      if (bps != null) {
         margin = bps.width;
      }

      Integer padWidth = (Integer)this.getTrait(Trait.PADDING_START);
      if (padWidth != null) {
         margin += padWidth;
      }

      return margin;
   }

   public int getBorderAndPaddingWidthEnd() {
      int margin = 0;
      BorderProps bps = (BorderProps)this.getTrait(Trait.BORDER_END);
      if (bps != null) {
         margin = bps.width;
      }

      Integer padWidth = (Integer)this.getTrait(Trait.PADDING_END);
      if (padWidth != null) {
         margin += padWidth;
      }

      return margin;
   }

   public int getSpaceBefore() {
      int margin = 0;
      Integer space = (Integer)this.getTrait(Trait.SPACE_BEFORE);
      if (space != null) {
         margin = space;
      }

      return margin;
   }

   public int getSpaceAfter() {
      int margin = 0;
      Integer space = (Integer)this.getTrait(Trait.SPACE_AFTER);
      if (space != null) {
         margin = space;
      }

      return margin;
   }

   public int getSpaceStart() {
      int margin = 0;
      Integer space = (Integer)this.getTrait(Trait.SPACE_START);
      if (space != null) {
         margin = space;
      }

      return margin;
   }

   public int getSpaceEnd() {
      int margin = 0;
      Integer space = (Integer)this.getTrait(Trait.SPACE_END);
      if (space != null) {
         margin = space;
      }

      return margin;
   }

   public void addChildArea(Area child) {
   }

   public void addTrait(Object traitCode, Object prop) {
      if (this.props == null) {
         this.props = new HashMap(20);
      }

      this.props.put(traitCode, prop);
   }

   public Map getTraits() {
      return this.props;
   }

   public boolean hasTraits() {
      return this.props != null;
   }

   public Object getTrait(Object oTraitCode) {
      return this.props != null ? this.props.get(oTraitCode) : null;
   }

   public boolean hasTrait(Object oTraitCode) {
      return this.getTrait(oTraitCode) != null;
   }

   public boolean getTraitAsBoolean(Object oTraitCode) {
      return Boolean.TRUE.equals(this.getTrait(oTraitCode));
   }

   public int getTraitAsInteger(Object oTraitCode) {
      Object obj = this.getTrait(oTraitCode);
      if (obj instanceof Integer) {
         return (Integer)obj;
      } else {
         throw new IllegalArgumentException("Trait " + oTraitCode.getClass().getName() + " could not be converted to an integer");
      }
   }

   public String toString() {
      StringBuffer sb = new StringBuffer(super.toString());
      sb.append(" {ipd=").append(Integer.toString(this.getIPD()));
      sb.append(", bpd=").append(Integer.toString(this.getBPD()));
      sb.append("}");
      return sb.toString();
   }

   static {
      log = LogFactory.getLog(Area.class);
   }
}
