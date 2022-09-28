package org.apache.fop.afp.fonts;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.afp.util.ResourceAccessor;
import org.apache.fop.afp.util.SimpleResourceAccessor;
import org.apache.fop.afp.util.StringUtils;

public class CharacterSet {
   protected static final Log LOG;
   public static final String DEFAULT_CODEPAGE = "T1V10500";
   public static final String DEFAULT_ENCODING = "Cp500";
   private static final int MAX_NAME_LEN = 8;
   protected String codePage;
   protected String encoding;
   private CharsetEncoder encoder;
   protected String name;
   private ResourceAccessor accessor;
   private final String currentOrientation;
   private Map characterSetOrientations;
   private int nominalVerticalSize;

   /** @deprecated */
   public CharacterSet(String codePage, String encoding, String name, String path) {
      this(codePage, encoding, name, (ResourceAccessor)(new SimpleResourceAccessor(path != null ? new File(path) : null)));
   }

   CharacterSet(String codePage, String encoding, String name, ResourceAccessor accessor) {
      this.currentOrientation = "0";
      this.characterSetOrientations = null;
      this.nominalVerticalSize = 0;
      if (name.length() > 8) {
         String msg = "Character set name '" + name + "' must be a maximum of " + 8 + " characters";
         LOG.error("Constructor:: " + msg);
         throw new IllegalArgumentException(msg);
      } else {
         if (name.length() < 8) {
            this.name = StringUtils.rpad(name, ' ', 8);
         } else {
            this.name = name;
         }

         this.codePage = codePage;
         this.encoding = encoding;

         try {
            this.encoder = Charset.forName(encoding).newEncoder();
            this.encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
         } catch (UnsupportedCharsetException var6) {
            this.encoder = null;
         }

         this.accessor = accessor;
         this.characterSetOrientations = new HashMap(4);
      }
   }

   public void addCharacterSetOrientation(CharacterSetOrientation cso) {
      this.characterSetOrientations.put(String.valueOf(cso.getOrientation()), cso);
   }

   public void setNominalVerticalSize(int nominalVerticalSize) {
      this.nominalVerticalSize = nominalVerticalSize;
   }

   public int getNominalVerticalSize() {
      return this.nominalVerticalSize;
   }

   public int getAscender() {
      return this.getCharacterSetOrientation().getAscender();
   }

   public int getCapHeight() {
      return this.getCharacterSetOrientation().getCapHeight();
   }

   public int getDescender() {
      return this.getCharacterSetOrientation().getDescender();
   }

   public char getFirstChar() {
      return this.getCharacterSetOrientation().getFirstChar();
   }

   public char getLastChar() {
      return this.getCharacterSetOrientation().getLastChar();
   }

   public ResourceAccessor getResourceAccessor() {
      return this.accessor;
   }

   public int[] getWidths() {
      return this.getCharacterSetOrientation().getWidths();
   }

   public int getXHeight() {
      return this.getCharacterSetOrientation().getXHeight();
   }

   public int getWidth(char character) {
      return this.getCharacterSetOrientation().getWidth(character);
   }

   public String getName() {
      return this.name;
   }

   public byte[] getNameBytes() {
      byte[] nameBytes = null;

      byte[] nameBytes;
      try {
         nameBytes = this.name.getBytes("Cp1146");
      } catch (UnsupportedEncodingException var3) {
         nameBytes = this.name.getBytes();
         LOG.warn("UnsupportedEncodingException translating the name " + this.name);
      }

      return nameBytes;
   }

   public String getCodePage() {
      return this.codePage;
   }

   public String getEncoding() {
      return this.encoding;
   }

   private CharacterSetOrientation getCharacterSetOrientation() {
      CharacterSetOrientation c = (CharacterSetOrientation)this.characterSetOrientations.get("0");
      return c;
   }

   public boolean hasChar(char c) {
      return this.encoder != null ? this.encoder.canEncode(c) : true;
   }

   public byte[] encodeChars(CharSequence chars) throws CharacterCodingException {
      if (this.encoder != null) {
         ByteBuffer bb;
         synchronized(this.encoder) {
            bb = this.encoder.encode(CharBuffer.wrap(chars));
         }

         if (bb.hasArray()) {
            return bb.array();
         } else {
            bb.rewind();
            byte[] bytes = new byte[bb.remaining()];
            bb.get(bytes);
            return bytes;
         }
      } else {
         try {
            byte[] bytes = chars.toString().getBytes(this.encoding);
            return bytes;
         } catch (UnsupportedEncodingException var6) {
            throw new UnsupportedOperationException("Unsupported encoding: " + var6.getMessage());
         }
      }
   }

   public char mapChar(char c) {
      return c;
   }

   public int getSpaceIncrement() {
      return this.getCharacterSetOrientation().getSpaceIncrement();
   }

   public int getEmSpaceIncrement() {
      return this.getCharacterSetOrientation().getEmSpaceIncrement();
   }

   static {
      LOG = LogFactory.getLog(CharacterSet.class.getName());
   }
}
