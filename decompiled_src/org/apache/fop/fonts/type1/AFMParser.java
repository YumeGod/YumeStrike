package org.apache.fop.fonts.type1;

import java.awt.Rectangle;
import java.beans.Statement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.fonts.NamedCharacter;

public class AFMParser {
   private static Log log;
   private static final String START_FONT_METRICS = "StartFontMetrics";
   private static final String FONT_NAME = "FontName";
   private static final String FULL_NAME = "FullName";
   private static final String FAMILY_NAME = "FamilyName";
   private static final String WEIGHT = "Weight";
   private static final String FONT_BBOX = "FontBBox";
   private static final String ENCODING_SCHEME = "EncodingScheme";
   private static final String CHARACTER_SET = "CharacterSet";
   private static final String IS_BASE_FONT = "IsBaseFont";
   private static final String IS_CID_FONT = "IsCIDFont";
   private static final String CAP_HEIGHT = "CapHeight";
   private static final String X_HEIGHT = "XHeight";
   private static final String ASCENDER = "Ascender";
   private static final String DESCENDER = "Descender";
   private static final String STDHW = "StdHW";
   private static final String STDVW = "StdVW";
   private static final String UNDERLINE_POSITION = "UnderlinePosition";
   private static final String UNDERLINE_THICKNESS = "UnderlineThickness";
   private static final String ITALIC_ANGLE = "ItalicAngle";
   private static final String IS_FIXED_PITCH = "IsFixedPitch";
   private static final String START_DIRECTION = "StartDirection";
   private static final String END_DIRECTION = "EndDirection";
   private static final String START_CHAR_METRICS = "StartCharMetrics";
   private static final String END_CHAR_METRICS = "EndCharMetrics";
   private static final String C = "C";
   private static final String CH = "CH";
   private static final String WX = "WX";
   private static final String W0X = "W0X";
   private static final String W1X = "W1X";
   private static final String WY = "WY";
   private static final String W0Y = "W0Y";
   private static final String W1Y = "W1Y";
   private static final String W = "W";
   private static final String W0 = "W0";
   private static final String W1 = "W1";
   private static final String N = "N";
   private static final String B = "B";
   private static final String START_TRACK_KERN = "StartTrackKern";
   private static final String END_TRACK_KERN = "EndTrackKern";
   private static final String START_KERN_PAIRS1 = "StartKernPairs1";
   private static final String START_COMPOSITES = "StartComposites";
   private static final String START_COMP_FONT_METRICS = "StartCompFontMetrics";
   private static final String KP = "KP";
   private static final String KPH = "KPH";
   private static final String KPX = "KPX";
   private static final String KPY = "KPY";
   private static final int PARSE_NORMAL = 0;
   private static final int PARSE_CHAR_METRICS = 1;
   private static final Map VALUE_PARSERS;
   private static final Map PARSE_MODE_CHANGES;

   public AFMFile parse(File afmFile) throws IOException {
      InputStream in = new FileInputStream(afmFile);

      AFMFile var3;
      try {
         var3 = this.parse((InputStream)in);
      } finally {
         IOUtils.closeQuietly((InputStream)in);
      }

      return var3;
   }

   public AFMFile parse(InputStream in) throws IOException {
      Reader reader = new InputStreamReader(in, "US-ASCII");

      AFMFile var3;
      try {
         var3 = this.parse(new BufferedReader(reader));
      } finally {
         IOUtils.closeQuietly((Reader)reader);
      }

      return var3;
   }

   public AFMFile parse(BufferedReader reader) throws IOException {
      Stack stack = new Stack();
      int parseMode = 0;

      while(true) {
         String line = reader.readLine();
         if (line == null) {
            return (AFMFile)stack.pop();
         }

         String key = null;
         switch (parseMode) {
            case 0:
               key = this.parseLine(line, stack);
               break;
            case 1:
               key = this.parseCharMetrics(line, stack);
               break;
            default:
               throw new IllegalStateException("Invalid parse mode");
         }

         Integer newParseMode = (Integer)PARSE_MODE_CHANGES.get(key);
         if (newParseMode != null) {
            parseMode = newParseMode;
         }
      }
   }

   private String parseLine(String line, Stack stack) throws IOException {
      int startpos = 0;
      startpos = skipToNonWhiteSpace(line, startpos);
      int endpos = skipToWhiteSpace(line, startpos);
      String key = line.substring(startpos, endpos);
      startpos = skipToNonWhiteSpace(line, endpos);
      ValueHandler vp = (ValueHandler)VALUE_PARSERS.get(key);
      if (vp != null) {
         vp.parse(line, startpos, stack);
      }

      return key;
   }

   private String parseCharMetrics(String line, Stack stack) throws IOException {
      int startpos = 0;
      AFMCharMetrics chm = new AFMCharMetrics();
      stack.push(chm);

      while(true) {
         startpos = skipToNonWhiteSpace(line, startpos);
         int endpos = skipToWhiteSpace(line, startpos);
         String key = line.substring(startpos, endpos);
         if ("EndCharMetrics".equals(key)) {
            stack.pop();
            return key;
         }

         if (key.length() == 0) {
            stack.pop();
            AFMFile afm = (AFMFile)stack.peek();
            afm.addCharMetrics(chm);
            return null;
         }

         startpos = skipToNonWhiteSpace(line, endpos);
         endpos = skipToSemicolon(line, startpos);
         String value = line.substring(startpos, endpos).trim();
         startpos = endpos + 1;
         ValueHandler vp = (ValueHandler)VALUE_PARSERS.get(key);
         if (vp != null) {
            vp.parse(value, 0, stack);
         }
      }
   }

   private static int skipToNonWhiteSpace(String line, int startpos) {
      int pos;
      for(pos = startpos; pos < line.length() && isWhitespace(line.charAt(pos)); ++pos) {
      }

      return pos;
   }

   private static int skipToWhiteSpace(String line, int startpos) {
      int pos;
      for(pos = startpos; pos < line.length() && !isWhitespace(line.charAt(pos)); ++pos) {
      }

      return pos;
   }

   private static int skipToSemicolon(String line, int startpos) {
      int pos;
      for(pos = startpos; pos < line.length() && ';' != line.charAt(pos); ++pos) {
      }

      return pos;
   }

   private static boolean isWhitespace(char ch) {
      return ch == ' ' || ch == '\t';
   }

   static {
      log = LogFactory.getLog(AFMParser.class);
      VALUE_PARSERS = new HashMap();
      VALUE_PARSERS.put("StartFontMetrics", new StartFontMetrics());
      VALUE_PARSERS.put("FontName", new StringSetter("FontName"));
      VALUE_PARSERS.put("FullName", new StringSetter("FullName"));
      VALUE_PARSERS.put("FamilyName", new StringSetter("FamilyName"));
      VALUE_PARSERS.put("Weight", new StringSetter("Weight"));
      VALUE_PARSERS.put("EncodingScheme", new StringSetter("EncodingScheme"));
      VALUE_PARSERS.put("FontBBox", new FontBBox());
      VALUE_PARSERS.put("CharacterSet", new StringSetter("CharacterSet"));
      VALUE_PARSERS.put("IsBaseFont", new IsBaseFont());
      VALUE_PARSERS.put("IsCIDFont", new IsCIDFont());
      VALUE_PARSERS.put("CapHeight", new NumberSetter("CapHeight"));
      VALUE_PARSERS.put("XHeight", new NumberSetter("XHeight"));
      VALUE_PARSERS.put("Ascender", new NumberSetter("Ascender"));
      VALUE_PARSERS.put("Descender", new NumberSetter("Descender"));
      VALUE_PARSERS.put("StdHW", new NumberSetter("StdHW"));
      VALUE_PARSERS.put("StdVW", new NumberSetter("StdVW"));
      VALUE_PARSERS.put("StartDirection", new StartDirection());
      VALUE_PARSERS.put("EndDirection", new EndDirection());
      VALUE_PARSERS.put("UnderlinePosition", new WritingDirNumberSetter("UnderlinePosition"));
      VALUE_PARSERS.put("UnderlineThickness", new WritingDirNumberSetter("UnderlineThickness"));
      VALUE_PARSERS.put("ItalicAngle", new WritingDirDoubleSetter("ItalicAngle"));
      VALUE_PARSERS.put("IsFixedPitch", new WritingDirBooleanSetter("IsFixedPitch"));
      VALUE_PARSERS.put("C", new IntegerSetter("CharCode"));
      VALUE_PARSERS.put("CH", new NotImplementedYet("CH"));
      VALUE_PARSERS.put("WX", new DoubleSetter("WidthX"));
      VALUE_PARSERS.put("W0X", new DoubleSetter("WidthX"));
      VALUE_PARSERS.put("W1X", new NotImplementedYet("W1X"));
      VALUE_PARSERS.put("WY", new DoubleSetter("WidthY"));
      VALUE_PARSERS.put("W0Y", new DoubleSetter("WidthY"));
      VALUE_PARSERS.put("W1Y", new NotImplementedYet("W1Y"));
      VALUE_PARSERS.put("W", new NotImplementedYet("W"));
      VALUE_PARSERS.put("W0", new NotImplementedYet("W0"));
      VALUE_PARSERS.put("W1", new NotImplementedYet("W1"));
      VALUE_PARSERS.put("N", new NamedCharacterSetter("Character"));
      VALUE_PARSERS.put("B", new CharBBox());
      VALUE_PARSERS.put("StartTrackKern", new NotImplementedYet("StartTrackKern"));
      VALUE_PARSERS.put("StartKernPairs1", new NotImplementedYet("StartKernPairs1"));
      VALUE_PARSERS.put("StartComposites", new NotImplementedYet("StartComposites"));
      VALUE_PARSERS.put("StartCompFontMetrics", new NotImplementedYet("StartCompFontMetrics"));
      VALUE_PARSERS.put("KP", new NotImplementedYet("KP"));
      VALUE_PARSERS.put("KPH", new NotImplementedYet("KPH"));
      VALUE_PARSERS.put("KPX", new KPXHandler());
      VALUE_PARSERS.put("KPY", new NotImplementedYet("KPY"));
      PARSE_MODE_CHANGES = new HashMap();
      PARSE_MODE_CHANGES.put("StartCharMetrics", new Integer(1));
      PARSE_MODE_CHANGES.put("EndCharMetrics", new Integer(0));
   }

   private static class KPXHandler extends AbstractValueHandler {
      private KPXHandler() {
         super(null);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         AFMFile afm = (AFMFile)stack.peek();
         int endpos = this.findValue(line, startpos);
         String name1 = line.substring(startpos, endpos);
         startpos = AFMParser.skipToNonWhiteSpace(line, endpos);
         endpos = this.findValue(line, startpos);
         String name2 = line.substring(startpos, endpos);
         startpos = AFMParser.skipToNonWhiteSpace(line, endpos);
         endpos = this.findValue(line, startpos);
         double kx = Double.parseDouble(line.substring(startpos, endpos));
         AFMParser.skipToNonWhiteSpace(line, endpos);
         afm.addXKerning(name1, name2, kx);
      }

      // $FF: synthetic method
      KPXHandler(Object x0) {
         this();
      }
   }

   private static class EndDirection extends AbstractValueHandler {
      private EndDirection() {
         super(null);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         if (!(stack.pop() instanceof AFMWritingDirectionMetrics)) {
            throw new IOException("AFM format error: nesting incorrect");
         }
      }

      // $FF: synthetic method
      EndDirection(Object x0) {
         this();
      }
   }

   private static class StartDirection extends AbstractValueHandler {
      private StartDirection() {
         super(null);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         int index = this.getIntegerValue(line, startpos);
         AFMWritingDirectionMetrics wdm = new AFMWritingDirectionMetrics();
         AFMFile afm = (AFMFile)stack.peek();
         afm.setWritingDirectionMetrics(index, wdm);
         stack.push(wdm);
      }

      // $FF: synthetic method
      StartDirection(Object x0) {
         this();
      }
   }

   private static class NotImplementedYet extends AbstractValueHandler {
      private String key;

      public NotImplementedYet(String key) {
         super(null);
         this.key = key;
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         AFMParser.log.warn("Support for '" + this.key + "' has not been implemented, yet!" + " Some font data in the AFM file will be ignored.");
      }
   }

   private static class IsCIDFont extends AbstractValueHandler {
      private IsCIDFont() {
         super(null);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         if (this.getBooleanValue(line, startpos)) {
            throw new IOException("CID fonts are currently not supported!");
         }
      }

      // $FF: synthetic method
      IsCIDFont(Object x0) {
         this();
      }
   }

   private static class IsBaseFont extends AbstractValueHandler {
      private IsBaseFont() {
         super(null);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         if (this.getBooleanValue(line, startpos)) {
            throw new IOException("Only base fonts are currently supported!");
         }
      }

      // $FF: synthetic method
      IsBaseFont(Object x0) {
         this();
      }
   }

   private static class CharBBox extends FontBBox {
      private CharBBox() {
         super(null);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         Rectangle rect = this.parseBBox(line, startpos);
         AFMCharMetrics metrics = (AFMCharMetrics)stack.peek();
         metrics.setBBox(rect);
      }

      // $FF: synthetic method
      CharBBox(Object x0) {
         this();
      }
   }

   private static class FontBBox extends AbstractValueHandler {
      private FontBBox() {
         super(null);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         Rectangle rect = this.parseBBox(line, startpos);
         AFMFile afm = (AFMFile)stack.peek();
         afm.setFontBBox(rect);
      }

      protected Rectangle parseBBox(String line, int startpos) {
         Rectangle rect = new Rectangle();
         int endpos = this.findValue(line, startpos);
         rect.x = Integer.parseInt(line.substring(startpos, endpos));
         startpos = AFMParser.skipToNonWhiteSpace(line, endpos);
         endpos = this.findValue(line, startpos);
         rect.y = Integer.parseInt(line.substring(startpos, endpos));
         startpos = AFMParser.skipToNonWhiteSpace(line, endpos);
         endpos = this.findValue(line, startpos);
         int v = Integer.parseInt(line.substring(startpos, endpos));
         rect.width = v - rect.x;
         startpos = AFMParser.skipToNonWhiteSpace(line, endpos);
         endpos = this.findValue(line, startpos);
         v = Integer.parseInt(line.substring(startpos, endpos));
         rect.height = v - rect.y;
         AFMParser.skipToNonWhiteSpace(line, endpos);
         return rect;
      }

      // $FF: synthetic method
      FontBBox(Object x0) {
         this();
      }
   }

   private static class WritingDirBooleanSetter extends BooleanSetter {
      public WritingDirBooleanSetter(String variable) {
         super(variable);
      }

      protected Object getContextObject(Stack stack) {
         if (stack.peek() instanceof AFMWritingDirectionMetrics) {
            return (AFMWritingDirectionMetrics)stack.peek();
         } else {
            AFMFile afm = (AFMFile)stack.peek();
            AFMWritingDirectionMetrics wdm = afm.getWritingDirectionMetrics(0);
            if (wdm == null) {
               wdm = new AFMWritingDirectionMetrics();
               afm.setWritingDirectionMetrics(0, wdm);
            }

            return wdm;
         }
      }
   }

   private static class BooleanSetter extends AbstractValueHandler {
      private String method;

      public BooleanSetter(String variable) {
         super(null);
         this.method = "set" + variable.substring(2);
      }

      protected Object getContextObject(Stack stack) {
         return (AFMFile)stack.peek();
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         Boolean b = this.getBooleanValue(line, startpos);
         Statement statement = new Statement(this.getContextObject(stack), this.method, new Object[]{b});

         try {
            statement.execute();
         } catch (Exception var7) {
            throw new RuntimeException("Bean error: " + var7.getMessage());
         }
      }
   }

   private static class WritingDirDoubleSetter extends WritingDirNumberSetter {
      public WritingDirDoubleSetter(String variable) {
         super(variable);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         double value = this.getDoubleValue(line, startpos);
         this.setValue(this.getContextObject(stack), new Double(value));
      }
   }

   private static class WritingDirNumberSetter extends NumberSetter {
      public WritingDirNumberSetter(String variable) {
         super(variable);
      }

      protected Object getContextObject(Stack stack) {
         if (stack.peek() instanceof AFMWritingDirectionMetrics) {
            return (AFMWritingDirectionMetrics)stack.peek();
         } else {
            AFMFile afm = (AFMFile)stack.peek();
            AFMWritingDirectionMetrics wdm = afm.getWritingDirectionMetrics(0);
            if (wdm == null) {
               wdm = new AFMWritingDirectionMetrics();
               afm.setWritingDirectionMetrics(0, wdm);
            }

            return wdm;
         }
      }
   }

   private static class DoubleSetter extends NumberSetter {
      public DoubleSetter(String variable) {
         super(variable);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         double value = this.getDoubleValue(line, startpos);
         this.setValue(this.getContextObject(stack), new Double(value));
      }
   }

   private static class IntegerSetter extends NumberSetter {
      public IntegerSetter(String variable) {
         super(variable);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         int value = this.getIntegerValue(line, startpos);
         this.setValue(this.getContextObject(stack), new Integer(value));
      }
   }

   private static class NumberSetter extends BeanSetter {
      public NumberSetter(String variable) {
         super(variable);
      }

      protected Object getContextObject(Stack stack) {
         return stack.peek();
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         Number num = this.getNumberValue(line, startpos);
         this.setValue(this.getContextObject(stack), num);
      }
   }

   private static class NamedCharacterSetter extends BeanSetter {
      public NamedCharacterSetter(String variable) {
         super(variable);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         NamedCharacter ch = new NamedCharacter(this.getStringValue(line, startpos));
         Object obj = stack.peek();
         this.setValue(obj, ch);
      }
   }

   private static class StringSetter extends BeanSetter {
      public StringSetter(String variable) {
         super(variable);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         String s = this.getStringValue(line, startpos);
         Object obj = stack.peek();
         this.setValue(obj, s);
      }
   }

   private abstract static class BeanSetter extends AbstractValueHandler {
      private String method;

      public BeanSetter(String variable) {
         super(null);
         this.method = "set" + variable;
      }

      protected void setValue(Object target, Object value) {
         Statement statement = new Statement(target, this.method, new Object[]{value});

         try {
            statement.execute();
         } catch (Exception var5) {
            throw new RuntimeException("Bean error: " + var5.getMessage());
         }
      }
   }

   private static class StartFontMetrics extends AbstractValueHandler {
      private StartFontMetrics() {
         super(null);
      }

      public void parse(String line, int startpos, Stack stack) throws IOException {
         int endpos = this.findValue(line, startpos);
         double version = Double.parseDouble(line.substring(startpos, endpos));
         if (version < 2.0) {
            throw new IOException("AFM version must be at least 2.0 but it is " + version + "!");
         } else {
            AFMFile afm = new AFMFile();
            stack.push(afm);
         }
      }

      // $FF: synthetic method
      StartFontMetrics(Object x0) {
         this();
      }
   }

   private abstract static class AbstractValueHandler implements ValueHandler {
      private AbstractValueHandler() {
      }

      protected int findValue(String line, int startpos) {
         return AFMParser.skipToWhiteSpace(line, startpos);
      }

      protected String getStringValue(String line, int startpos) {
         return line.substring(startpos);
      }

      protected Number getNumberValue(String line, int startpos) {
         try {
            return new Integer(this.getIntegerValue(line, startpos));
         } catch (NumberFormatException var4) {
            return new Double(this.getDoubleValue(line, startpos));
         }
      }

      protected int getIntegerValue(String line, int startpos) {
         int endpos = this.findValue(line, startpos);
         return Integer.parseInt(line.substring(startpos, endpos));
      }

      protected double getDoubleValue(String line, int startpos) {
         int endpos = this.findValue(line, startpos);
         return Double.parseDouble(line.substring(startpos, endpos));
      }

      protected Boolean getBooleanValue(String line, int startpos) {
         return Boolean.valueOf(this.getStringValue(line, startpos));
      }

      // $FF: synthetic method
      AbstractValueHandler(Object x0) {
         this();
      }
   }

   private interface ValueHandler {
      void parse(String var1, int var2, Stack var3) throws IOException;
   }
}
