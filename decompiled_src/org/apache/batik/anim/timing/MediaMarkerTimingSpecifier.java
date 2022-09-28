package org.apache.batik.anim.timing;

public class MediaMarkerTimingSpecifier extends TimingSpecifier {
   protected String syncbaseID;
   protected TimedElement mediaElement;
   protected String markerName;
   protected InstanceTime instance;

   public MediaMarkerTimingSpecifier(TimedElement var1, boolean var2, String var3, String var4) {
      super(var1, var2);
      this.syncbaseID = var3;
      this.markerName = var4;
      this.mediaElement = var1.getTimedElementById(var3);
   }

   public String toString() {
      return this.syncbaseID + ".marker(" + this.markerName + ")";
   }

   public boolean isEventCondition() {
      return false;
   }
}
