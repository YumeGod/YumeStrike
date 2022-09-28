package org.apache.xmlgraphics.ps.dsc.events;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDSCComment extends AbstractEvent implements DSCComment {
   private final boolean isWhitespace(char c) {
      return c == ' ' || c == '\t';
   }

   private int parseNextParam(String value, int pos, List lst) {
      int startPos;
      for(startPos = pos++; pos < value.length() && !this.isWhitespace(value.charAt(pos)); ++pos) {
      }

      String param = value.substring(startPos, pos);
      lst.add(param);
      return pos;
   }

   private int parseNextParentheseString(String value, int pos, List lst) {
      int nestLevel = 1;
      ++pos;

      StringBuffer sb;
      for(sb = new StringBuffer(); pos < value.length() && nestLevel > 0; ++pos) {
         char c = value.charAt(pos);
         switch (c) {
            case '(':
               ++nestLevel;
               if (nestLevel > 1) {
                  sb.append(c);
               }
               break;
            case ')':
               if (nestLevel > 1) {
                  sb.append(c);
               }

               --nestLevel;
               break;
            case '\\':
               ++pos;
               char cnext = value.charAt(pos);
               switch (cnext) {
                  case '(':
                     sb.append('(');
                     continue;
                  case ')':
                     sb.append(')');
                     continue;
                  case '\\':
                     sb.append(cnext);
                     continue;
                  case 'b':
                     sb.append('\b');
                     continue;
                  case 'f':
                     sb.append('\f');
                     continue;
                  case 'n':
                     sb.append('\n');
                     continue;
                  case 'r':
                     sb.append('\r');
                     continue;
                  case 't':
                     sb.append('\t');
                     continue;
                  default:
                     int code = Integer.parseInt(value.substring(pos, pos + 3), 8);
                     sb.append((char)code);
                     pos += 2;
                     continue;
               }
            default:
               sb.append(c);
         }
      }

      lst.add(sb.toString());
      ++pos;
      return pos;
   }

   protected List splitParams(String value) {
      List lst = new ArrayList();
      int pos = 0;
      value = value.trim();

      while(pos < value.length()) {
         if (this.isWhitespace(value.charAt(pos))) {
            ++pos;
         } else if (value.charAt(pos) == '(') {
            pos = this.parseNextParentheseString(value, pos, lst);
         } else {
            pos = this.parseNextParam(value, pos, lst);
         }
      }

      return lst;
   }

   public boolean isAtend() {
      return false;
   }

   public DSCComment asDSCComment() {
      return this;
   }

   public boolean isDSCComment() {
      return true;
   }

   public int getEventType() {
      return 1;
   }
}
