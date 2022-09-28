package org.apache.fop.fonts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SingleByteFont extends CustomFont {
   private static Log log;
   private SingleByteEncoding mapping;
   private boolean useNativeEncoding = false;
   private int[] width = null;
   private Map unencodedCharacters;
   private List additionalEncodings;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public SingleByteFont() {
      this.setEncoding("WinAnsiEncoding");
   }

   public boolean isEmbeddable() {
      return this.getEmbedFileName() != null || this.getEmbedResourceName() != null;
   }

   public String getEncodingName() {
      return this.mapping.getName();
   }

   public SingleByteEncoding getEncoding() {
      return this.mapping;
   }

   public int getWidth(int i, int size) {
      int encodingIndex;
      if (i < 256) {
         encodingIndex = i - this.getFirstChar();
         if (encodingIndex >= 0 && encodingIndex < this.width.length) {
            return size * this.width[i - this.getFirstChar()];
         }
      } else if (this.additionalEncodings != null) {
         encodingIndex = i / 256 - 1;
         SimpleSingleByteEncoding encoding = this.getAdditionalEncoding(encodingIndex);
         int codePoint = i % 256;
         NamedCharacter nc = encoding.getCharacterForIndex(codePoint);
         UnencodedCharacter uc = (UnencodedCharacter)this.unencodedCharacters.get(new Character(nc.getSingleUnicodeValue()));
         return size * uc.getWidth();
      }

      return 0;
   }

   public int[] getWidths() {
      int[] arr = new int[this.width.length];
      System.arraycopy(this.width, 0, arr, 0, this.width.length);
      return arr;
   }

   public char mapChar(char c) {
      this.notifyMapOperation();
      char d = this.mapping.mapChar(c);
      if (d != 0) {
         return d;
      } else {
         d = this.mapUnencodedChar(c);
         if (d != 0) {
            return d;
         } else {
            this.warnMissingGlyph(c);
            return '#';
         }
      }
   }

   private char mapUnencodedChar(char ch) {
      if (this.unencodedCharacters != null) {
         UnencodedCharacter unencoded = (UnencodedCharacter)this.unencodedCharacters.get(new Character(ch));
         if (unencoded != null) {
            if (this.additionalEncodings == null) {
               this.additionalEncodings = new ArrayList();
            }

            SimpleSingleByteEncoding encoding = null;
            char mappedStart = 0;
            int additionalsCount = this.additionalEncodings.size();

            for(int i = 0; i < additionalsCount; ++i) {
               mappedStart = (char)(mappedStart + 256);
               encoding = this.getAdditionalEncoding(i);
               char alt = encoding.mapChar(ch);
               if (alt != 0) {
                  return (char)(mappedStart + alt);
               }
            }

            if (encoding != null && encoding.isFull()) {
               encoding = null;
            }

            if (encoding == null) {
               encoding = new SimpleSingleByteEncoding(this.getFontName() + "EncodingSupp" + (additionalsCount + 1));
               this.additionalEncodings.add(encoding);
               mappedStart = (char)(mappedStart + 256);
            }

            return (char)(mappedStart + encoding.addCharacter(unencoded.getCharacter()));
         }
      }

      return '\u0000';
   }

   public boolean hasChar(char c) {
      char d = this.mapping.mapChar(c);
      if (d != 0) {
         return true;
      } else {
         d = this.mapUnencodedChar(c);
         return d != 0;
      }
   }

   protected void updateMapping(String encoding) {
      try {
         this.mapping = CodePointMapping.getMapping(encoding);
      } catch (UnsupportedOperationException var3) {
         log.error("Font '" + super.getFontName() + "': " + var3.getMessage());
      }

   }

   public void setEncoding(String encoding) {
      this.updateMapping(encoding);
   }

   public void setEncoding(CodePointMapping encoding) {
      this.mapping = encoding;
   }

   public void setUseNativeEncoding(boolean value) {
      this.useNativeEncoding = value;
   }

   public boolean isUsingNativeEncoding() {
      return this.useNativeEncoding;
   }

   public void setWidth(int index, int w) {
      if (this.width == null) {
         this.width = new int[this.getLastChar() - this.getFirstChar() + 1];
      }

      this.width[index - this.getFirstChar()] = w;
   }

   public void addUnencodedCharacter(NamedCharacter ch, int width) {
      if (this.unencodedCharacters == null) {
         this.unencodedCharacters = new HashMap();
      }

      if (ch.hasSingleUnicodeValue()) {
         UnencodedCharacter uc = new UnencodedCharacter(ch, width);
         this.unencodedCharacters.put(new Character(ch.getSingleUnicodeValue()), uc);
      }

   }

   public void encodeAllUnencodedCharacters() {
      if (this.unencodedCharacters != null) {
         Set sortedKeys = new TreeSet(this.unencodedCharacters.keySet());
         Iterator iter = sortedKeys.iterator();

         while(iter.hasNext()) {
            Character ch = (Character)iter.next();
            char mapped = this.mapChar(ch);
            if (!$assertionsDisabled && mapped == '#') {
               throw new AssertionError();
            }
         }
      }

   }

   public boolean hasAdditionalEncodings() {
      return this.additionalEncodings != null && this.additionalEncodings.size() > 0;
   }

   public int getAdditionalEncodingCount() {
      return this.hasAdditionalEncodings() ? this.additionalEncodings.size() : 0;
   }

   public SimpleSingleByteEncoding getAdditionalEncoding(int index) throws IndexOutOfBoundsException {
      if (this.hasAdditionalEncodings()) {
         return (SimpleSingleByteEncoding)this.additionalEncodings.get(index);
      } else {
         throw new IndexOutOfBoundsException("No additional encodings available");
      }
   }

   public int[] getAdditionalWidths(int index) {
      SimpleSingleByteEncoding enc = this.getAdditionalEncoding(index);
      int[] arr = new int[enc.getLastChar() - enc.getFirstChar() + 1];
      int i = 0;

      for(int c = arr.length; i < c; ++i) {
         NamedCharacter nc = enc.getCharacterForIndex(enc.getFirstChar() + i);
         UnencodedCharacter uc = (UnencodedCharacter)this.unencodedCharacters.get(new Character(nc.getSingleUnicodeValue()));
         arr[i] = uc.getWidth();
      }

      return arr;
   }

   static {
      $assertionsDisabled = !SingleByteFont.class.desiredAssertionStatus();
      log = LogFactory.getLog(SingleByteFont.class);
   }

   private static final class UnencodedCharacter {
      private NamedCharacter character;
      private int width;

      public UnencodedCharacter(NamedCharacter character, int width) {
         this.character = character;
         this.width = width;
      }

      public NamedCharacter getCharacter() {
         return this.character;
      }

      public int getWidth() {
         return this.width;
      }

      public String toString() {
         return this.getCharacter().toString();
      }
   }
}
