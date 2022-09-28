package org.apache.batik.parser;

import java.util.Calendar;

public interface TimingSpecifierHandler {
   void offset(float var1);

   void syncbase(float var1, String var2, String var3);

   void eventbase(float var1, String var2, String var3);

   void repeat(float var1, String var2);

   void repeat(float var1, String var2, int var3);

   void accesskey(float var1, char var2);

   void accessKeySVG12(float var1, String var2);

   void mediaMarker(String var1, String var2);

   void wallclock(Calendar var1);

   void indefinite();
}
