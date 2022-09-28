package org.apache.james.mime4j.field.structured.parser;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class StructuredFieldParser implements StructuredFieldParserConstants {
   private boolean preserveFolding;
   public StructuredFieldParserTokenManager token_source;
   SimpleCharStream jj_input_stream;
   public Token token;
   public Token jj_nt;
   private int jj_ntk;
   private int jj_gen;
   private final int[] jj_la1;
   private static int[] jj_la1_0;
   private List jj_expentries;
   private int[] jj_expentry;
   private int jj_kind;

   public boolean isFoldingPreserved() {
      return this.preserveFolding;
   }

   public void setFoldingPreserved(boolean preserveFolding) {
      this.preserveFolding = preserveFolding;
   }

   public String parse() throws ParseException {
      try {
         return this.doParse();
      } catch (TokenMgrError var2) {
         throw new ParseException(var2);
      }
   }

   private final String doParse() throws ParseException {
      StringBuffer buffer = new StringBuffer(50);
      boolean whitespace = false;
      boolean first = true;

      while(true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
               Token t;
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 11:
                     t = this.jj_consume_token(11);
                     buffer.append(t.image);
                     continue;
                  case 12:
                     t = this.jj_consume_token(12);
                     if (this.preserveFolding) {
                        buffer.append("\r\n");
                     }
                     continue;
                  case 13:
                     t = this.jj_consume_token(13);
                     if (first) {
                        first = false;
                     } else if (whitespace) {
                        buffer.append(" ");
                        whitespace = false;
                     }

                     buffer.append(t.image);
                     continue;
                  case 14:
                     t = this.jj_consume_token(14);
                     whitespace = true;
                     continue;
                  case 15:
                     t = this.jj_consume_token(15);
                     if (first) {
                        first = false;
                     } else if (whitespace) {
                        buffer.append(" ");
                        whitespace = false;
                     }

                     buffer.append(t.image);
                     continue;
                  default:
                     this.jj_la1[1] = this.jj_gen;
                     this.jj_consume_token(-1);
                     throw new ParseException();
               }
            default:
               this.jj_la1[0] = this.jj_gen;
               return buffer.toString();
         }
      }
   }

   private static void jj_la1_init_0() {
      jj_la1_0 = new int[]{63488, 63488};
   }

   public StructuredFieldParser(InputStream stream) {
      this(stream, (String)null);
   }

   public StructuredFieldParser(InputStream stream, String encoding) {
      this.preserveFolding = false;
      this.jj_la1 = new int[2];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;

      try {
         this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }

      this.token_source = new StructuredFieldParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 2; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public void ReInit(InputStream stream) {
      this.ReInit(stream, (String)null);
   }

   public void ReInit(InputStream stream, String encoding) {
      try {
         this.jj_input_stream.ReInit(stream, encoding, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }

      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 2; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public StructuredFieldParser(Reader stream) {
      this.preserveFolding = false;
      this.jj_la1 = new int[2];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
      this.token_source = new StructuredFieldParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 2; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public void ReInit(Reader stream) {
      this.jj_input_stream.ReInit((Reader)stream, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 2; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public StructuredFieldParser(StructuredFieldParserTokenManager tm) {
      this.preserveFolding = false;
      this.jj_la1 = new int[2];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 2; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public void ReInit(StructuredFieldParserTokenManager tm) {
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 2; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   private Token jj_consume_token(int kind) throws ParseException {
      Token oldToken;
      if ((oldToken = this.token).next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      if (this.token.kind == kind) {
         ++this.jj_gen;
         return this.token;
      } else {
         this.token = oldToken;
         this.jj_kind = kind;
         throw this.generateParseException();
      }
   }

   public final Token getNextToken() {
      if (this.token.next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      ++this.jj_gen;
      return this.token;
   }

   public final Token getToken(int index) {
      Token t = this.token;

      for(int i = 0; i < index; ++i) {
         if (t.next != null) {
            t = t.next;
         } else {
            t = t.next = this.token_source.getNextToken();
         }
      }

      return t;
   }

   private int jj_ntk() {
      return (this.jj_nt = this.token.next) == null ? (this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind) : (this.jj_ntk = this.jj_nt.kind);
   }

   public ParseException generateParseException() {
      this.jj_expentries.clear();
      boolean[] la1tokens = new boolean[18];
      if (this.jj_kind >= 0) {
         la1tokens[this.jj_kind] = true;
         this.jj_kind = -1;
      }

      int i;
      int j;
      for(i = 0; i < 2; ++i) {
         if (this.jj_la1[i] == this.jj_gen) {
            for(j = 0; j < 32; ++j) {
               if ((jj_la1_0[i] & 1 << j) != 0) {
                  la1tokens[j] = true;
               }
            }
         }
      }

      for(i = 0; i < 18; ++i) {
         if (la1tokens[i]) {
            this.jj_expentry = new int[1];
            this.jj_expentry[0] = i;
            this.jj_expentries.add(this.jj_expentry);
         }
      }

      int[][] exptokseq = new int[this.jj_expentries.size()][];

      for(j = 0; j < this.jj_expentries.size(); ++j) {
         exptokseq[j] = (int[])this.jj_expentries.get(j);
      }

      return new ParseException(this.token, exptokseq, tokenImage);
   }

   public final void enable_tracing() {
   }

   public final void disable_tracing() {
   }

   static {
      jj_la1_init_0();
   }
}
