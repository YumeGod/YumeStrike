package org.apache.batik.anim.timing;

import java.util.Calendar;
import java.util.LinkedList;
import org.apache.batik.parser.DefaultTimingSpecifierListHandler;
import org.apache.batik.parser.TimingSpecifierListParser;

public class TimingSpecifierListProducer extends DefaultTimingSpecifierListHandler {
   protected LinkedList timingSpecifiers = new LinkedList();
   protected TimedElement owner;
   protected boolean isBegin;

   public TimingSpecifierListProducer(TimedElement var1, boolean var2) {
      this.owner = var1;
      this.isBegin = var2;
   }

   public TimingSpecifier[] getTimingSpecifiers() {
      return (TimingSpecifier[])this.timingSpecifiers.toArray(new TimingSpecifier[0]);
   }

   public static TimingSpecifier[] parseTimingSpecifierList(TimedElement var0, boolean var1, String var2, boolean var3, boolean var4) {
      TimingSpecifierListParser var5 = new TimingSpecifierListParser(var3, var4);
      TimingSpecifierListProducer var6 = new TimingSpecifierListProducer(var0, var1);
      var5.setTimingSpecifierListHandler(var6);
      var5.parse(var2);
      TimingSpecifier[] var7 = var6.getTimingSpecifiers();
      return var7;
   }

   public void offset(float var1) {
      OffsetTimingSpecifier var2 = new OffsetTimingSpecifier(this.owner, this.isBegin, var1);
      this.timingSpecifiers.add(var2);
   }

   public void syncbase(float var1, String var2, String var3) {
      SyncbaseTimingSpecifier var4 = new SyncbaseTimingSpecifier(this.owner, this.isBegin, var1, var2, var3.charAt(0) == 'b');
      this.timingSpecifiers.add(var4);
   }

   public void eventbase(float var1, String var2, String var3) {
      EventbaseTimingSpecifier var4 = new EventbaseTimingSpecifier(this.owner, this.isBegin, var1, var2, var3);
      this.timingSpecifiers.add(var4);
   }

   public void repeat(float var1, String var2) {
      RepeatTimingSpecifier var3 = new RepeatTimingSpecifier(this.owner, this.isBegin, var1, var2);
      this.timingSpecifiers.add(var3);
   }

   public void repeat(float var1, String var2, int var3) {
      RepeatTimingSpecifier var4 = new RepeatTimingSpecifier(this.owner, this.isBegin, var1, var2, var3);
      this.timingSpecifiers.add(var4);
   }

   public void accesskey(float var1, char var2) {
      AccesskeyTimingSpecifier var3 = new AccesskeyTimingSpecifier(this.owner, this.isBegin, var1, var2);
      this.timingSpecifiers.add(var3);
   }

   public void accessKeySVG12(float var1, String var2) {
      AccesskeyTimingSpecifier var3 = new AccesskeyTimingSpecifier(this.owner, this.isBegin, var1, var2);
      this.timingSpecifiers.add(var3);
   }

   public void mediaMarker(String var1, String var2) {
      MediaMarkerTimingSpecifier var3 = new MediaMarkerTimingSpecifier(this.owner, this.isBegin, var1, var2);
      this.timingSpecifiers.add(var3);
   }

   public void wallclock(Calendar var1) {
      WallclockTimingSpecifier var2 = new WallclockTimingSpecifier(this.owner, this.isBegin, var1);
      this.timingSpecifiers.add(var2);
   }

   public void indefinite() {
      IndefiniteTimingSpecifier var1 = new IndefiniteTimingSpecifier(this.owner, this.isBegin);
      this.timingSpecifiers.add(var1);
   }
}
