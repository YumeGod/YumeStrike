package org.apache.fop.afp.fonts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.util.ResourceAccessor;
import org.apache.fop.afp.util.StructuredFieldReader;
import org.apache.fop.fonts.Typeface;
import org.apache.xmlgraphics.image.loader.util.SoftMapCache;

public class CharacterSetBuilder {
   protected static final Log LOG;
   private static CharacterSetBuilder instance;
   private static final CharacterSetOrientation[] EMPTY_CSO_ARRAY;
   private static final byte[] CODEPAGE_SF;
   private static final byte[] CHARACTER_TABLE_SF;
   private static final byte[] FONT_DESCRIPTOR_SF;
   private static final byte[] FONT_CONTROL_SF;
   private static final byte[] FONT_ORIENTATION_SF;
   private static final byte[] FONT_POSITION_SF;
   private static final byte[] FONT_INDEX_SF;
   private final Map codePagesCache;
   private final SoftMapCache characterSetsCache;

   private CharacterSetBuilder() {
      this.codePagesCache = new WeakHashMap();
      this.characterSetsCache = new SoftMapCache(true);
   }

   public static CharacterSetBuilder getInstance() {
      if (instance == null) {
         instance = new CharacterSetBuilder();
      }

      return instance;
   }

   public static CharacterSetBuilder getDoubleByteInstance() {
      return new DoubleByteLoader();
   }

   protected InputStream openInputStream(ResourceAccessor accessor, String filename) throws IOException {
      URI uri;
      try {
         uri = new URI(filename.trim());
      } catch (URISyntaxException var5) {
         throw new FileNotFoundException("Invalid filename: " + filename + " (" + var5.getMessage() + ")");
      }

      if (LOG.isDebugEnabled()) {
         LOG.debug("Opening " + uri);
      }

      InputStream inputStream = accessor.createInputStream(uri);
      return inputStream;
   }

   protected void closeInputStream(InputStream inputStream) {
      try {
         if (inputStream != null) {
            inputStream.close();
         }
      } catch (Exception var3) {
         LOG.error(var3.getMessage());
      }

   }

   public CharacterSet build(String characterSetName, String codePageName, String encoding, ResourceAccessor accessor) throws IOException {
      String descriptor = characterSetName + "_" + encoding + "_" + codePageName;
      CharacterSet characterSet = (CharacterSet)this.characterSetsCache.get(descriptor);
      if (characterSet != null) {
         return characterSet;
      } else {
         characterSet = new CharacterSet(codePageName, encoding, characterSetName, accessor);
         InputStream inputStream = null;

         try {
            Map codePage = (Map)this.codePagesCache.get(codePageName);
            if (codePage == null) {
               codePage = this.loadCodePage(codePageName, encoding, accessor);
               this.codePagesCache.put(codePageName, codePage);
            }

            inputStream = this.openInputStream(accessor, characterSetName);
            StructuredFieldReader structuredFieldReader = new StructuredFieldReader(inputStream);
            FontDescriptor fontDescriptor = processFontDescriptor(structuredFieldReader);
            characterSet.setNominalVerticalSize(fontDescriptor.getNominalFontSizeInMillipoints());
            FontControl fontControl = this.processFontControl(structuredFieldReader);
            if (fontControl == null) {
               throw new IOException("Missing D3AE89 Font Control structured field.");
            }

            CharacterSetOrientation[] characterSetOrientations = this.processFontOrientation(structuredFieldReader);
            int metricNormalizationFactor;
            int i;
            if (fontControl.isRelative()) {
               metricNormalizationFactor = 1;
            } else {
               i = fontControl.getDpi();
               metricNormalizationFactor = 72000000 / fontDescriptor.getNominalFontSizeInMillipoints() / i;
            }

            this.processFontPosition(structuredFieldReader, characterSetOrientations, (double)metricNormalizationFactor);

            for(i = 0; i < characterSetOrientations.length; ++i) {
               this.processFontIndex(structuredFieldReader, characterSetOrientations[i], codePage, (double)metricNormalizationFactor);
               characterSet.addCharacterSetOrientation(characterSetOrientations[i]);
            }
         } finally {
            this.closeInputStream(inputStream);
         }

         this.characterSetsCache.put(descriptor, characterSet);
         return characterSet;
      }
   }

   public CharacterSet build(String characterSetName, String codePageName, String encoding, Typeface typeface) {
      return new FopCharacterSet(codePageName, encoding, characterSetName, typeface);
   }

   protected Map loadCodePage(String codePage, String encoding, ResourceAccessor accessor) throws IOException {
      Map codePages = new HashMap();
      InputStream inputStream = null;

      try {
         inputStream = this.openInputStream(accessor, codePage.trim());
         StructuredFieldReader structuredFieldReader = new StructuredFieldReader(inputStream);
         byte[] data = structuredFieldReader.getNext(CHARACTER_TABLE_SF);
         int position = 0;
         byte[] gcgiBytes = new byte[8];
         byte[] charBytes = new byte[1];

         for(int index = 3; index < data.length; ++index) {
            if (position < 8) {
               gcgiBytes[position] = data[index];
               ++position;
            } else if (position == 9) {
               position = 0;
               charBytes[0] = data[index];
               String gcgiString = new String(gcgiBytes, "Cp1146");
               String charString = new String(charBytes, encoding);
               codePages.put(gcgiString, charString);
            } else {
               ++position;
            }
         }
      } finally {
         this.closeInputStream(inputStream);
      }

      return codePages;
   }

   protected static FontDescriptor processFontDescriptor(StructuredFieldReader structuredFieldReader) throws IOException {
      byte[] fndData = structuredFieldReader.getNext(FONT_DESCRIPTOR_SF);
      return new FontDescriptor(fndData);
   }

   protected FontControl processFontControl(StructuredFieldReader structuredFieldReader) throws IOException {
      byte[] fncData = structuredFieldReader.getNext(FONT_CONTROL_SF);
      FontControl fontControl = null;
      if (fncData != null) {
         fontControl = new FontControl();
         if (fncData[7] == 2) {
            fontControl.setRelative(true);
         }

         int metricResolution = getUBIN(fncData, 9);
         if (metricResolution == 1000) {
            fontControl.setUnitsPerEm(1000);
         } else {
            fontControl.setDpi(metricResolution / 10);
         }
      }

      return fontControl;
   }

   protected CharacterSetOrientation[] processFontOrientation(StructuredFieldReader structuredFieldReader) throws IOException {
      byte[] data = structuredFieldReader.getNext(FONT_ORIENTATION_SF);
      int position = 0;
      byte[] fnoData = new byte[26];
      List orientations = new ArrayList();

      for(int index = 3; index < data.length; ++index) {
         fnoData[position] = data[index];
         ++position;
         if (position == 26) {
            position = 0;
            int orientation = determineOrientation(fnoData[2]);
            int space = ((fnoData[8] & 255) << 8) + (fnoData[9] & 255);
            int em = ((fnoData[14] & 255) << 8) + (fnoData[15] & 255);
            CharacterSetOrientation cso = new CharacterSetOrientation(orientation);
            cso.setSpaceIncrement(space);
            cso.setEmSpaceIncrement(em);
            orientations.add(cso);
         }
      }

      return (CharacterSetOrientation[])orientations.toArray(EMPTY_CSO_ARRAY);
   }

   protected void processFontPosition(StructuredFieldReader structuredFieldReader, CharacterSetOrientation[] characterSetOrientations, double metricNormalizationFactor) throws IOException {
      byte[] data = structuredFieldReader.getNext(FONT_POSITION_SF);
      int position = 0;
      byte[] fpData = new byte[26];
      int characterSetOrientationIndex = 0;

      for(int index = 3; index < data.length; ++index) {
         if (position < 22) {
            fpData[position] = data[index];
            if (position == 9) {
               CharacterSetOrientation characterSetOrientation = characterSetOrientations[characterSetOrientationIndex];
               int xHeight = getSBIN(fpData, 2);
               int capHeight = getSBIN(fpData, 4);
               int ascHeight = getSBIN(fpData, 6);
               int dscHeight = getSBIN(fpData, 8);
               dscHeight *= -1;
               characterSetOrientation.setXHeight((int)Math.round((double)xHeight * metricNormalizationFactor));
               characterSetOrientation.setCapHeight((int)Math.round((double)capHeight * metricNormalizationFactor));
               characterSetOrientation.setAscender((int)Math.round((double)ascHeight * metricNormalizationFactor));
               characterSetOrientation.setDescender((int)Math.round((double)dscHeight * metricNormalizationFactor));
            }
         } else if (position == 22) {
            position = 0;
            ++characterSetOrientationIndex;
            fpData[position] = data[index];
         }

         ++position;
      }

   }

   protected void processFontIndex(StructuredFieldReader structuredFieldReader, CharacterSetOrientation cso, Map codepage, double metricNormalizationFactor) throws IOException {
      byte[] data = structuredFieldReader.getNext(FONT_INDEX_SF);
      int position = 0;
      byte[] gcgid = new byte[8];
      byte[] fiData = new byte[20];
      char lowest = 255;
      char highest = 0;
      String firstABCMismatch = null;

      for(int index = 3; index < data.length; ++index) {
         if (position < 8) {
            gcgid[position] = data[index];
            ++position;
         } else if (position < 27) {
            fiData[position - 8] = data[index];
            ++position;
         } else if (position == 27) {
            fiData[position - 8] = data[index];
            position = 0;
            String gcgiString = new String(gcgid, "Cp1146");
            String idx = (String)codepage.get(gcgiString);
            if (idx != null) {
               char cidx = idx.charAt(0);
               int width = getUBIN(fiData, 0);
               int a = getSBIN(fiData, 10);
               int b = getUBIN(fiData, 12);
               int c = getSBIN(fiData, 14);
               int abc = a + b + c;
               int diff = Math.abs(abc - width);
               if (diff != 0 && width != 0) {
                  double diffPercent = (double)(100 * diff) / (double)width;
                  if (diffPercent > 2.0) {
                     if (LOG.isTraceEnabled()) {
                        LOG.trace(gcgiString + ": " + a + " + " + b + " + " + c + " = " + (a + b + c) + " but found: " + width);
                     }

                     if (firstABCMismatch == null) {
                        firstABCMismatch = gcgiString;
                     }
                  }
               }

               if (cidx < lowest) {
                  lowest = cidx;
               }

               if (cidx > highest) {
                  highest = cidx;
               }

               int normalizedWidth = (int)Math.round((double)width * metricNormalizationFactor);
               cso.setWidth(cidx, normalizedWidth);
            }
         }
      }

      cso.setFirstChar(lowest);
      cso.setLastChar(highest);
      if (LOG.isDebugEnabled() && firstABCMismatch != null) {
         LOG.debug("Font has metrics inconsitencies where A+B+C doesn't equal the character increment. The first such character found: " + firstABCMismatch);
      }

   }

   private static int getUBIN(byte[] data, int start) {
      return ((data[start] & 255) << 8) + (data[start + 1] & 255);
   }

   private static int getSBIN(byte[] data, int start) {
      int ubin = ((data[start] & 255) << 8) + (data[start + 1] & 255);
      return (ubin & '耀') != 0 ? ubin | -65536 : ubin;
   }

   private static int determineOrientation(byte orientation) {
      int degrees = false;
      short degrees;
      switch (orientation) {
         case -121:
            degrees = 270;
            break;
         case 0:
            degrees = 0;
            break;
         case 45:
            degrees = 90;
            break;
         case 90:
            degrees = 180;
            break;
         default:
            throw new IllegalStateException("Invalid orientation: " + orientation);
      }

      return degrees;
   }

   // $FF: synthetic method
   CharacterSetBuilder(Object x0) {
      this();
   }

   static {
      LOG = LogFactory.getLog(CharacterSetBuilder.class);
      EMPTY_CSO_ARRAY = new CharacterSetOrientation[0];
      CODEPAGE_SF = new byte[]{-45, -88, -121};
      CHARACTER_TABLE_SF = new byte[]{-45, -116, -121};
      FONT_DESCRIPTOR_SF = new byte[]{-45, -90, -119};
      FONT_CONTROL_SF = new byte[]{-45, -89, -119};
      FONT_ORIENTATION_SF = new byte[]{-45, -82, -119};
      FONT_POSITION_SF = new byte[]{-45, -84, -119};
      FONT_INDEX_SF = new byte[]{-45, -116, -119};
   }

   private static class DoubleByteLoader extends CharacterSetBuilder {
      private DoubleByteLoader() {
         super(null);
      }

      protected Map loadCodePage(String codePage, String encoding, ResourceAccessor accessor) throws IOException {
         Map codePages = new HashMap();
         InputStream inputStream = null;

         try {
            inputStream = this.openInputStream(accessor, codePage.trim());
            StructuredFieldReader structuredFieldReader = new StructuredFieldReader(inputStream);

            byte[] data;
            while((data = structuredFieldReader.getNext(CharacterSetBuilder.CHARACTER_TABLE_SF)) != null) {
               int position = 0;
               byte[] gcgiBytes = new byte[8];
               byte[] charBytes = new byte[2];

               for(int index = 3; index < data.length; ++index) {
                  if (position < 8) {
                     gcgiBytes[position] = data[index];
                     ++position;
                  } else if (position == 9) {
                     charBytes[0] = data[index];
                     ++position;
                  } else if (position == 10) {
                     position = 0;
                     charBytes[1] = data[index];
                     String gcgiString = new String(gcgiBytes, "Cp1146");
                     String charString = new String(charBytes, encoding);
                     codePages.put(gcgiString, charString);
                  } else {
                     ++position;
                  }
               }
            }
         } finally {
            this.closeInputStream(inputStream);
         }

         return codePages;
      }

      // $FF: synthetic method
      DoubleByteLoader(Object x0) {
         this();
      }
   }

   private static class FontDescriptor {
      private byte[] data;

      public FontDescriptor(byte[] data) {
         this.data = data;
      }

      public int getNominalFontSizeInMillipoints() {
         int nominalFontSize = 100 * CharacterSetBuilder.getUBIN(this.data, 39);
         return nominalFontSize;
      }
   }

   private class FontControl {
      private int dpi;
      private int unitsPerEm;
      private boolean isRelative;

      private FontControl() {
         this.isRelative = false;
      }

      public int getDpi() {
         return this.dpi;
      }

      public void setDpi(int i) {
         this.dpi = i;
      }

      public int getUnitsPerEm() {
         return this.unitsPerEm;
      }

      public void setUnitsPerEm(int value) {
         this.unitsPerEm = value;
      }

      public boolean isRelative() {
         return this.isRelative;
      }

      public void setRelative(boolean b) {
         this.isRelative = b;
      }

      // $FF: synthetic method
      FontControl(Object x1) {
         this();
      }
   }
}
