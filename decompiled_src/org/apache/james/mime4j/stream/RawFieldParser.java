package org.apache.james.mime4j.stream;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.util.ByteSequence;
import org.apache.james.mime4j.util.CharsetUtil;
import org.apache.james.mime4j.util.ContentUtil;

public class RawFieldParser {
   static final BitSet COLON = INIT_BITSET(58);
   static final BitSet EQUAL_OR_SEMICOLON = INIT_BITSET(61, 59);
   static final BitSet SEMICOLON = INIT_BITSET(59);
   public static final RawFieldParser DEFAULT = new RawFieldParser();

   public static BitSet INIT_BITSET(int... b) {
      BitSet bitset = new BitSet(b.length);

      for(int i = 0; i < b.length; ++i) {
         bitset.set(b[i]);
      }

      return bitset;
   }

   public RawField parseField(ByteSequence raw) throws MimeException {
      if (raw == null) {
         return null;
      } else {
         ParserCursor cursor = new ParserCursor(0, raw.length());
         String name = this.parseToken(raw, cursor, COLON);
         if (cursor.atEnd()) {
            throw new MimeException("Invalid MIME field: no name/value separator found: " + raw.toString());
         } else {
            return new RawField(raw, cursor.getPos(), name, (String)null);
         }
      }
   }

   public RawBody parseRawBody(RawField field) {
      ByteSequence buf = field.getRaw();
      int pos = field.getDelimiterIdx() + 1;
      if (buf == null) {
         String body = field.getBody();
         if (body == null) {
            return new RawBody("", (List)null);
         }

         buf = ContentUtil.encode(body);
         pos = 0;
      }

      ParserCursor cursor = new ParserCursor(pos, buf.length());
      return this.parseRawBody(buf, cursor);
   }

   public RawBody parseRawBody(ByteSequence buf, ParserCursor cursor) {
      String value = this.parseToken(buf, cursor, SEMICOLON);
      if (cursor.atEnd()) {
         return new RawBody(value, new ArrayList());
      } else {
         cursor.updatePos(cursor.getPos() + 1);
         List params = this.parseParameters(buf, cursor);
         return new RawBody(value, params);
      }
   }

   public List parseParameters(ByteSequence buf, ParserCursor cursor) {
      List params = new ArrayList();
      this.skipWhiteSpace(buf, cursor);

      while(!cursor.atEnd()) {
         NameValuePair param = this.parseParameter(buf, cursor);
         params.add(param);
      }

      return params;
   }

   public NameValuePair parseParameter(ByteSequence buf, ParserCursor cursor) {
      String name = this.parseToken(buf, cursor, EQUAL_OR_SEMICOLON);
      if (cursor.atEnd()) {
         return new NameValuePair(name, (String)null);
      } else {
         int delim = buf.byteAt(cursor.getPos());
         cursor.updatePos(cursor.getPos() + 1);
         if (delim == 59) {
            return new NameValuePair(name, (String)null);
         } else {
            String value = this.parseValue(buf, cursor, SEMICOLON);
            if (!cursor.atEnd()) {
               cursor.updatePos(cursor.getPos() + 1);
            }

            return new NameValuePair(name, value);
         }
      }
   }

   public String parseToken(ByteSequence buf, ParserCursor cursor, BitSet delimiters) {
      StringBuilder dst = new StringBuilder();
      boolean whitespace = false;

      while(!cursor.atEnd()) {
         char current = (char)(buf.byteAt(cursor.getPos()) & 255);
         if (delimiters != null && delimiters.get(current)) {
            break;
         }

         if (CharsetUtil.isWhitespace(current)) {
            this.skipWhiteSpace(buf, cursor);
            whitespace = true;
         } else if (current == '(') {
            this.skipComment(buf, cursor);
         } else {
            if (dst.length() > 0 && whitespace) {
               dst.append(' ');
            }

            this.copyContent(buf, cursor, delimiters, dst);
            whitespace = false;
         }
      }

      return dst.toString();
   }

   public String parseValue(ByteSequence buf, ParserCursor cursor, BitSet delimiters) {
      StringBuilder dst = new StringBuilder();
      boolean whitespace = false;

      while(!cursor.atEnd()) {
         char current = (char)(buf.byteAt(cursor.getPos()) & 255);
         if (delimiters != null && delimiters.get(current)) {
            break;
         }

         if (CharsetUtil.isWhitespace(current)) {
            this.skipWhiteSpace(buf, cursor);
            whitespace = true;
         } else if (current == '(') {
            this.skipComment(buf, cursor);
         } else if (current == '"') {
            if (dst.length() > 0 && whitespace) {
               dst.append(' ');
            }

            this.copyQuotedContent(buf, cursor, dst);
            whitespace = false;
         } else {
            if (dst.length() > 0 && whitespace) {
               dst.append(' ');
            }

            this.copyContent(buf, cursor, delimiters, dst);
            whitespace = false;
         }
      }

      return dst.toString();
   }

   public void skipWhiteSpace(ByteSequence buf, ParserCursor cursor) {
      int pos = cursor.getPos();
      int indexFrom = cursor.getPos();
      int indexTo = cursor.getUpperBound();

      for(int i = indexFrom; i < indexTo; ++i) {
         char current = (char)(buf.byteAt(i) & 255);
         if (!CharsetUtil.isWhitespace(current)) {
            break;
         }

         ++pos;
      }

      cursor.updatePos(pos);
   }

   public void skipComment(ByteSequence buf, ParserCursor cursor) {
      if (!cursor.atEnd()) {
         int pos = cursor.getPos();
         int indexFrom = cursor.getPos();
         int indexTo = cursor.getUpperBound();
         char current = (char)(buf.byteAt(pos) & 255);
         if (current == '(') {
            ++pos;
            ++indexFrom;
            int level = 1;
            boolean escaped = false;

            for(int i = indexFrom; i < indexTo; ++pos) {
               current = (char)(buf.byteAt(i) & 255);
               if (escaped) {
                  escaped = false;
               } else if (current == '\\') {
                  escaped = true;
               } else if (current == '(') {
                  ++level;
               } else if (current == ')') {
                  --level;
               }

               if (level <= 0) {
                  ++pos;
                  break;
               }

               ++i;
            }

            cursor.updatePos(pos);
         }
      }
   }

   public void skipAllWhiteSpace(ByteSequence buf, ParserCursor cursor) {
      while(true) {
         if (!cursor.atEnd()) {
            char current = (char)(buf.byteAt(cursor.getPos()) & 255);
            if (CharsetUtil.isWhitespace(current)) {
               this.skipWhiteSpace(buf, cursor);
               continue;
            }

            if (current == '(') {
               this.skipComment(buf, cursor);
               continue;
            }
         }

         return;
      }
   }

   public void copyContent(ByteSequence buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
      int pos = cursor.getPos();
      int indexFrom = cursor.getPos();
      int indexTo = cursor.getUpperBound();

      for(int i = indexFrom; i < indexTo; ++i) {
         char current = (char)(buf.byteAt(i) & 255);
         if (delimiters != null && delimiters.get(current) || CharsetUtil.isWhitespace(current) || current == '(') {
            break;
         }

         ++pos;
         dst.append(current);
      }

      cursor.updatePos(pos);
   }

   public void copyQuotedContent(ByteSequence buf, ParserCursor cursor, StringBuilder dst) {
      if (!cursor.atEnd()) {
         int pos = cursor.getPos();
         int indexFrom = cursor.getPos();
         int indexTo = cursor.getUpperBound();
         char current = (char)(buf.byteAt(pos) & 255);
         if (current == '"') {
            ++pos;
            ++indexFrom;
            boolean escaped = false;

            for(int i = indexFrom; i < indexTo; ++pos) {
               current = (char)(buf.byteAt(i) & 255);
               if (escaped) {
                  if (current != '"' && current != '\\') {
                     dst.append('\\');
                  }

                  dst.append(current);
                  escaped = false;
               } else {
                  if (current == '"') {
                     ++pos;
                     break;
                  }

                  if (current == '\\') {
                     escaped = true;
                  } else if (current != '\r' && current != '\n') {
                     dst.append(current);
                  }
               }

               ++i;
            }

            cursor.updatePos(pos);
         }
      }
   }
}
