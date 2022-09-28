package org.apache.batik.parser;

public interface FragmentIdentifierHandler extends PreserveAspectRatioHandler, TransformListHandler {
   void startFragmentIdentifier() throws ParseException;

   void idReference(String var1) throws ParseException;

   void viewBox(float var1, float var2, float var3, float var4) throws ParseException;

   void startViewTarget() throws ParseException;

   void viewTarget(String var1) throws ParseException;

   void endViewTarget() throws ParseException;

   void zoomAndPan(boolean var1);

   void endFragmentIdentifier() throws ParseException;
}
