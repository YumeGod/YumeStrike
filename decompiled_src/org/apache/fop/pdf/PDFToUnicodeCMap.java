package org.apache.fop.pdf;

import java.io.IOException;
import java.io.Writer;

public class PDFToUnicodeCMap extends PDFCMap {
   protected char[] unicodeCharMap;
   private boolean singleByte;

   public PDFToUnicodeCMap(char[] unicodeCharMap, String name, PDFCIDSystemInfo sysInfo, boolean singleByte) {
      super(name, sysInfo);
      if (singleByte && unicodeCharMap.length > 256) {
         throw new IllegalArgumentException("unicodeCharMap may not contain more than 256 characters for single-byte encodings");
      } else {
         this.unicodeCharMap = unicodeCharMap;
         this.singleByte = singleByte;
      }
   }

   protected CMapBuilder createCMapBuilder(Writer writer) {
      return new ToUnicodeCMapBuilder(writer);
   }

   class ToUnicodeCMapBuilder extends CMapBuilder {
      public ToUnicodeCMapBuilder(Writer writer) {
         super(writer, (String)null);
      }

      public void writeCMap() throws IOException {
         this.writeCIDInit();
         this.writeCIDSystemInfo("Adobe", "UCS", 0);
         this.writeName("Adobe-Identity-UCS");
         this.writeType("2");
         this.writeCodeSpaceRange(PDFToUnicodeCMap.this.singleByte);
         this.writeBFEntries();
         this.writeWrapUp();
      }

      protected void writeBFEntries() throws IOException {
         if (PDFToUnicodeCMap.this.unicodeCharMap != null) {
            this.writeBFCharEntries(PDFToUnicodeCMap.this.unicodeCharMap);
            this.writeBFRangeEntries(PDFToUnicodeCMap.this.unicodeCharMap);
         }

      }

      protected void writeBFCharEntries(char[] charArray) throws IOException {
         int totalEntries = 0;

         int remainingEntries;
         for(remainingEntries = 0; remainingEntries < charArray.length; ++remainingEntries) {
            if (!this.partOfRange(charArray, remainingEntries)) {
               ++totalEntries;
            }
         }

         if (totalEntries >= 1) {
            remainingEntries = totalEntries;
            int charIndex = 0;

            do {
               int entriesThisSection = Math.min(remainingEntries, 100);
               this.writer.write(entriesThisSection + " beginbfchar\n");

               for(int i = 0; i < entriesThisSection; ++i) {
                  while(this.partOfRange(charArray, charIndex)) {
                     ++charIndex;
                  }

                  this.writer.write("<" + this.padCharIndex(charIndex) + "> ");
                  this.writer.write("<" + this.padHexString(Integer.toHexString(charArray[charIndex]), 4) + ">\n");
                  ++charIndex;
               }

               remainingEntries -= entriesThisSection;
               this.writer.write("endbfchar\n");
            } while(remainingEntries > 0);

         }
      }

      private String padCharIndex(int charIndex) {
         return this.padHexString(Integer.toHexString(charIndex), PDFToUnicodeCMap.this.singleByte ? 2 : 4);
      }

      protected void writeBFRangeEntries(char[] charArray) throws IOException {
         int totalEntries = 0;

         int remainingEntries;
         for(remainingEntries = 0; remainingEntries < charArray.length; ++remainingEntries) {
            if (this.startOfRange(charArray, remainingEntries)) {
               ++totalEntries;
            }
         }

         if (totalEntries >= 1) {
            remainingEntries = totalEntries;
            int charIndex = 0;

            do {
               int entriesThisSection = Math.min(remainingEntries, 100);
               this.writer.write(entriesThisSection + " beginbfrange\n");

               for(int i = 0; i < entriesThisSection; ++i) {
                  while(!this.startOfRange(charArray, charIndex)) {
                     ++charIndex;
                  }

                  this.writer.write("<" + this.padCharIndex(charIndex) + "> ");
                  this.writer.write("<" + this.padCharIndex(this.endOfRange(charArray, charIndex)) + "> ");
                  this.writer.write("<" + this.padHexString(Integer.toHexString(charArray[charIndex]), 4) + ">\n");
                  ++charIndex;
               }

               remainingEntries -= entriesThisSection;
               this.writer.write("endbfrange\n");
            } while(remainingEntries > 0);

         }
      }

      private int endOfRange(char[] charArray, int startOfRange) {
         int i;
         for(i = startOfRange; i < charArray.length - 1 && this.sameRangeEntryAsNext(charArray, i); ++i) {
         }

         return i;
      }

      private boolean partOfRange(char[] charArray, int arrayIndex) {
         if (charArray.length < 2) {
            return false;
         } else if (arrayIndex == 0) {
            return this.sameRangeEntryAsNext(charArray, 0);
         } else if (arrayIndex == charArray.length - 1) {
            return this.sameRangeEntryAsNext(charArray, arrayIndex - 1);
         } else if (this.sameRangeEntryAsNext(charArray, arrayIndex - 1)) {
            return true;
         } else {
            return this.sameRangeEntryAsNext(charArray, arrayIndex);
         }
      }

      private boolean sameRangeEntryAsNext(char[] charArray, int firstItem) {
         if (charArray[firstItem] + 1 != charArray[firstItem + 1]) {
            return false;
         } else {
            return firstItem / 256 == (firstItem + 1) / 256;
         }
      }

      private boolean startOfRange(char[] charArray, int arrayIndex) {
         if (!this.partOfRange(charArray, arrayIndex)) {
            return false;
         } else if (arrayIndex == 0) {
            return true;
         } else if (arrayIndex == charArray.length - 1) {
            return false;
         } else {
            return !this.sameRangeEntryAsNext(charArray, arrayIndex - 1);
         }
      }

      private String padHexString(String input, int numChars) {
         int length = input.length();
         if (length >= numChars) {
            return input;
         } else {
            StringBuffer returnString = new StringBuffer();

            for(int i = 1; i <= numChars - length; ++i) {
               returnString.append("0");
            }

            returnString.append(input);
            return returnString.toString();
         }
      }
   }
}
