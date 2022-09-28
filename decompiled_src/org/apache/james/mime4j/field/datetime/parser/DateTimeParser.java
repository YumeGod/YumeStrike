package org.apache.james.mime4j.field.datetime.parser;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.james.mime4j.dom.datetime.DateTime;

public class DateTimeParser implements DateTimeParserConstants {
   private static final boolean ignoreMilitaryZoneOffset = true;
   public DateTimeParserTokenManager token_source;
   SimpleCharStream jj_input_stream;
   public Token token;
   public Token jj_nt;
   private int jj_ntk;
   private int jj_gen;
   private final int[] jj_la1;
   private static int[] jj_la1_0;
   private static int[] jj_la1_1;
   private List jj_expentries;
   private int[] jj_expentry;
   private int jj_kind;

   public static void main(String[] args) throws ParseException {
      while(true) {
         try {
            DateTimeParser parser = new DateTimeParser(System.in);
            parser.parseLine();
         } catch (Exception var2) {
            var2.printStackTrace();
            return;
         }
      }
   }

   private static int parseDigits(Token token) {
      return Integer.parseInt(token.image, 10);
   }

   private static int getMilitaryZoneOffset(char c) {
      return 0;
   }

   public final DateTime parseLine() throws ParseException {
      DateTime dt = this.date_time();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 1:
            this.jj_consume_token(1);
            break;
         default:
            this.jj_la1[0] = this.jj_gen;
      }

      this.jj_consume_token(2);
      return dt;
   }

   public final DateTime parseAll() throws ParseException {
      DateTime dt = this.date_time();
      this.jj_consume_token(0);
      return dt;
   }

   public final DateTime date_time() throws ParseException {
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
         case 9:
         case 10:
            this.day_of_week();
            this.jj_consume_token(3);
            break;
         default:
            this.jj_la1[1] = this.jj_gen;
      }

      Date d = this.date();
      Time t = this.time();
      return new DateTime(d.getYear(), d.getMonth(), d.getDay(), t.getHour(), t.getMinute(), t.getSecond(), t.getZone());
   }

   public final String day_of_week() throws ParseException {
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 4:
            this.jj_consume_token(4);
            break;
         case 5:
            this.jj_consume_token(5);
            break;
         case 6:
            this.jj_consume_token(6);
            break;
         case 7:
            this.jj_consume_token(7);
            break;
         case 8:
            this.jj_consume_token(8);
            break;
         case 9:
            this.jj_consume_token(9);
            break;
         case 10:
            this.jj_consume_token(10);
            break;
         default:
            this.jj_la1[2] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }

      return this.token.image;
   }

   public final Date date() throws ParseException {
      int d = this.day();
      int m = this.month();
      String y = this.year();
      return new Date(y, m, d);
   }

   public final int day() throws ParseException {
      Token t = this.jj_consume_token(46);
      return parseDigits(t);
   }

   public final int month() throws ParseException {
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 11:
            this.jj_consume_token(11);
            return 1;
         case 12:
            this.jj_consume_token(12);
            return 2;
         case 13:
            this.jj_consume_token(13);
            return 3;
         case 14:
            this.jj_consume_token(14);
            return 4;
         case 15:
            this.jj_consume_token(15);
            return 5;
         case 16:
            this.jj_consume_token(16);
            return 6;
         case 17:
            this.jj_consume_token(17);
            return 7;
         case 18:
            this.jj_consume_token(18);
            return 8;
         case 19:
            this.jj_consume_token(19);
            return 9;
         case 20:
            this.jj_consume_token(20);
            return 10;
         case 21:
            this.jj_consume_token(21);
            return 11;
         case 22:
            this.jj_consume_token(22);
            return 12;
         default:
            this.jj_la1[3] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }
   }

   public final String year() throws ParseException {
      Token t = this.jj_consume_token(46);
      return t.image;
   }

   public final Time time() throws ParseException {
      int s = 0;
      int h = this.hour();
      this.jj_consume_token(23);
      int m = this.minute();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 23:
            this.jj_consume_token(23);
            s = this.second();
            break;
         default:
            this.jj_la1[4] = this.jj_gen;
      }

      int z = this.zone();
      return new Time(h, m, s, z);
   }

   public final int hour() throws ParseException {
      Token t = this.jj_consume_token(46);
      return parseDigits(t);
   }

   public final int minute() throws ParseException {
      Token t = this.jj_consume_token(46);
      return parseDigits(t);
   }

   public final int second() throws ParseException {
      Token t = this.jj_consume_token(46);
      return parseDigits(t);
   }

   public final int zone() throws ParseException {
      int z;
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 24:
            Token t = this.jj_consume_token(24);
            Token u = this.jj_consume_token(46);
            z = parseDigits(u) * (t.image.equals("-") ? -1 : 1);
            break;
         case 25:
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
            z = this.obs_zone();
            break;
         default:
            this.jj_la1[5] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }

      return z;
   }

   public final int obs_zone() throws ParseException {
      int z;
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 25:
            this.jj_consume_token(25);
            z = 0;
            break;
         case 26:
            this.jj_consume_token(26);
            z = 0;
            break;
         case 27:
            this.jj_consume_token(27);
            z = -5;
            break;
         case 28:
            this.jj_consume_token(28);
            z = -4;
            break;
         case 29:
            this.jj_consume_token(29);
            z = -6;
            break;
         case 30:
            this.jj_consume_token(30);
            z = -5;
            break;
         case 31:
            this.jj_consume_token(31);
            z = -7;
            break;
         case 32:
            this.jj_consume_token(32);
            z = -6;
            break;
         case 33:
            this.jj_consume_token(33);
            z = -8;
            break;
         case 34:
            this.jj_consume_token(34);
            z = -7;
            break;
         case 35:
            Token t = this.jj_consume_token(35);
            z = getMilitaryZoneOffset(t.image.charAt(0));
            break;
         default:
            this.jj_la1[6] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
      }

      return z * 100;
   }

   private static void jj_la1_init_0() {
      jj_la1_0 = new int[]{2, 2032, 2032, 8386560, 8388608, -16777216, -33554432};
   }

   private static void jj_la1_init_1() {
      jj_la1_1 = new int[]{0, 0, 0, 0, 0, 15, 15};
   }

   public DateTimeParser(InputStream stream) {
      this(stream, (String)null);
   }

   public DateTimeParser(InputStream stream, String encoding) {
      this.jj_la1 = new int[7];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;

      try {
         this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }

      this.token_source = new DateTimeParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 7; ++i) {
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

      for(int i = 0; i < 7; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public DateTimeParser(Reader stream) {
      this.jj_la1 = new int[7];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
      this.token_source = new DateTimeParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 7; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public void ReInit(Reader stream) {
      this.jj_input_stream.ReInit((Reader)stream, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 7; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public DateTimeParser(DateTimeParserTokenManager tm) {
      this.jj_la1 = new int[7];
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 7; ++i) {
         this.jj_la1[i] = -1;
      }

   }

   public void ReInit(DateTimeParserTokenManager tm) {
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for(int i = 0; i < 7; ++i) {
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
      boolean[] la1tokens = new boolean[49];
      if (this.jj_kind >= 0) {
         la1tokens[this.jj_kind] = true;
         this.jj_kind = -1;
      }

      int i;
      int j;
      for(i = 0; i < 7; ++i) {
         if (this.jj_la1[i] == this.jj_gen) {
            for(j = 0; j < 32; ++j) {
               if ((jj_la1_0[i] & 1 << j) != 0) {
                  la1tokens[j] = true;
               }

               if ((jj_la1_1[i] & 1 << j) != 0) {
                  la1tokens[32 + j] = true;
               }
            }
         }
      }

      for(i = 0; i < 49; ++i) {
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
      jj_la1_init_1();
   }

   private static class Date {
      private String year;
      private int month;
      private int day;

      public Date(String year, int month, int day) {
         this.year = year;
         this.month = month;
         this.day = day;
      }

      public String getYear() {
         return this.year;
      }

      public int getMonth() {
         return this.month;
      }

      public int getDay() {
         return this.day;
      }
   }

   private static class Time {
      private int hour;
      private int minute;
      private int second;
      private int zone;

      public Time(int hour, int minute, int second, int zone) {
         this.hour = hour;
         this.minute = minute;
         this.second = second;
         this.zone = zone;
      }

      public int getHour() {
         return this.hour;
      }

      public int getMinute() {
         return this.minute;
      }

      public int getSecond() {
         return this.second;
      }

      public int getZone() {
         return this.zone;
      }
   }
}
