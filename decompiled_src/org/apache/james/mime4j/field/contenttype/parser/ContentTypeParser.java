package org.apache.james.mime4j.field.contenttype.parser;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ContentTypeParser implements ContentTypeParserConstants {
   private String type;
   private String subtype;
   private List paramNames;
   private List paramValues;
   public ContentTypeParserTokenManager token_source;
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

   public String getType() {
      return this.type;
   }

   public String getSubType() {
      return this.subtype;
   }

   public List getParamNames() {
      return this.paramNames;
   }

   public List getParamValues() {
      return this.paramValues;
   }

   public static void main(String[] args) throws ParseException {
      while(true) {
         try {
            ContentTypeParser parser = new ContentTypeParser(System.in);
            parser.parseLine();
         } catch (Exception var2) {
            var2.printStackTrace();
            return;
         }
      }
   }

   public final void parseLine() throws ParseException {
      this.parse();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 1:
            this.jj_consume_token(1);
            break;
         default:
            this.jj_la1[0] = this.jj_gen;
      }

      this.jj_consume_token(2);
   }

   public final void parseAll() throws ParseException {
      this.parse();
      this.jj_consume_token(0);
   }

   public final void parse() throws ParseException {
      Token type = this.jj_consume_token(21);
      this.jj_consume_token(3);
      Token subtype = this.jj_consume_token(21);
      this.type = type.image;
      this.subtype = subtype.image;

      while(true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 4:
               this.jj_consume_token(4);
               this.parameter();
               break;
            default:
               this.jj_la1[1] = this.jj_gen;
               return;
         }
      }
   }

   public final void parameter() throws ParseException {
      Token attrib = this.jj_consume_token(21);
      this.jj_consume_token(5);
      String val = this.value();
      this.paramNames.add(attrib.image);
      this.paramValues.add(val);
   }

   public final String value() throws ParseException {
      Token t;
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 19:
            t = this.jj_consume_token(19);
            break;
         case 20:
            t = this.jj_consume_token(20);
            break;
         case 21:
            t = this.jj_consume_token(21);
            break;
         default:
            this.jj_la1[2] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }

      return t.image;
   }

   private static void jj_la1_init_0() {
      jj_la1_0 = new int[]{2, 16, 3670016};
   }

   public ContentTypeParser(InputStream stream) {
      this(stream, (String)null);
   }

   public ContentTypeParser(InputStream stream, String encoding) {
      this.paramNames = new ArrayList();
      this.paramValues = new ArrayList();
      this.jj_la1 = new int[3];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;

      try {
         this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }

      this.token_source = new ContentTypeParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 3; ++i) {
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

      for(int i = 0; i < 3; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public ContentTypeParser(Reader stream) {
      this.paramNames = new ArrayList();
      this.paramValues = new ArrayList();
      this.jj_la1 = new int[3];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
      this.token_source = new ContentTypeParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 3; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public void ReInit(Reader stream) {
      this.jj_input_stream.ReInit((Reader)stream, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 3; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public ContentTypeParser(ContentTypeParserTokenManager tm) {
      this.paramNames = new ArrayList();
      this.paramValues = new ArrayList();
      this.jj_la1 = new int[3];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 3; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public void ReInit(ContentTypeParserTokenManager tm) {
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 3; ++i) {
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
      boolean[] la1tokens = new boolean[24];
      if (this.jj_kind >= 0) {
         la1tokens[this.jj_kind] = true;
         this.jj_kind = -1;
      }

      int i;
      int j;
      for(i = 0; i < 3; ++i) {
         if (this.jj_la1[i] == this.jj_gen) {
            for(j = 0; j < 32; ++j) {
               if ((jj_la1_0[i] & 1 << j) != 0) {
                  la1tokens[j] = true;
               }
            }
         }
      }

      for(i = 0; i < 24; ++i) {
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
