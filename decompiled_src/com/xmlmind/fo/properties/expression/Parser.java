package com.xmlmind.fo.properties.expression;

import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.properties.Keyword;
import com.xmlmind.fo.properties.Property;
import com.xmlmind.fo.properties.Value;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Vector;

public class Parser implements ParserConstants {
   public ParserTokenManager token_source;
   SimpleCharStream jj_input_stream;
   public Token token;
   public Token jj_nt;
   private int jj_ntk;
   private Token jj_scanpos;
   private Token jj_lastpos;
   private int jj_la;
   public boolean lookingAhead = false;
   private boolean jj_semLA;
   private int jj_gen;
   private final int[] jj_la1 = new int[9];
   private static int[] jj_la1_0;
   private final JJCalls[] jj_2_rtns = new JJCalls[1];
   private boolean jj_rescan = false;
   private int jj_gc = 0;
   private final LookaheadSuccess jj_ls = new LookaheadSuccess();
   private Vector jj_expentries = new Vector();
   private int[] jj_expentry;
   private int jj_kind = -1;
   private int[] jj_lasttokens = new int[100];
   private int jj_endpos;

   public static Expression parse(String var0) throws ParseException {
      Parser var1 = new Parser(new StringReader(var0));
      return var1.parse();
   }

   public Expression parse() throws ParseException {
      return this.Expression();
   }

   private static Value number(String var0) {
      double var1 = 0.0;

      try {
         var1 = Double.parseDouble(var0);
      } catch (NumberFormatException var4) {
      }

      return Value.number(var1);
   }

   private static Value length(String var0) {
      double var2 = 0.0;
      byte var1;
      if (var0.endsWith("cm")) {
         var1 = 1;
      } else if (var0.endsWith("mm")) {
         var1 = 2;
      } else if (var0.endsWith("in")) {
         var1 = 3;
      } else if (var0.endsWith("pt")) {
         var1 = 4;
      } else if (var0.endsWith("pc")) {
         var1 = 5;
      } else if (var0.endsWith("px")) {
         var1 = 6;
      } else if (var0.endsWith("ex")) {
         var1 = 8;
      } else {
         var1 = 7;
      }

      try {
         var2 = Double.parseDouble(var0.substring(0, var0.length() - 2));
      } catch (NumberFormatException var5) {
      }

      return Value.length(var2, var1);
   }

   private static Value percentage(String var0) {
      double var1 = 0.0;

      try {
         var1 = Double.parseDouble(var0.substring(0, var0.length() - 1));
      } catch (NumberFormatException var4) {
      }

      return Value.percentage(var1);
   }

   private static Value name(String var0) throws ParseException {
      int var1 = Keyword.index(var0);
      if (var1 < 0) {
         int var2 = Property.index(var0);
         if (var2 < 0) {
            throw new ParseException("unknown keyword '" + var0 + "'");
         } else {
            return Value.name(var0);
         }
      } else {
         return Value.keyword(var1);
      }
   }

   private static Value color(String var0) throws ParseException {
      Color var1 = Color.parse(var0);
      if (var1 == null) {
         throw new ParseException("bad color specification '" + var0 + "'");
      } else {
         return Value.color(var1);
      }
   }

   private static Value function(String var0, Vector var1) throws ParseException {
      Function var2 = Function.function(var0);
      Value[] var3 = null;
      if (var2 == null) {
         throw new ParseException("unknown function name '" + var0 + "'");
      } else {
         if (var1 != null) {
            int var4 = var1.size();
            if (var4 < var2.minArgs || var4 > var2.maxArgs) {
               throw new ParseException("bad argument count (function '" + var0 + "')");
            }

            var3 = new Value[var4];

            for(int var5 = 0; var5 < var4; ++var5) {
               var3[var5] = Value.expression((Expression)var1.elementAt(var5));
            }
         } else if (var2.minArgs > 0) {
            throw new ParseException("bad argument count (function '" + var0 + "')");
         }

         switch (var2.index) {
            case 4:
            case 5:
            case 6:
            case 7:
               if (var3 != null) {
                  Expression var6 = var3[0].expression();
                  if (var6.operator != 0 || var6.operand1.type != 16) {
                     throw new ParseException("bad argument type (function '" + var0 + "')");
                  }
               }
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 15:
            default:
               return Value.function(var2.index, var3);
            case 10:
            case 14:
            case 16:
            case 17:
               throw new ParseException("unsupported function '" + var0 + "'");
         }
      }
   }

   public static void main(String[] var0) {
      Expression var1 = null;
      if (var0.length != 1) {
         String var2 = "com.xmlmind.fo.properties.expression.Parser";
         System.err.println("usage: java " + var2 + "<expr>");
         System.exit(1);
      }

      try {
         var1 = parse(var0[0]);
      } catch (ParseException var3) {
         System.out.println("Error: " + var3.toString());
         System.exit(2);
      }

      System.out.println(var1.toString());
      System.exit(0);
   }

   public final Expression Expression() throws ParseException {
      Expression var1 = this.AdditiveExpression();
      this.jj_consume_token(0);
      return var1;
   }

   public final Expression AdditiveExpression() throws ParseException {
      Expression var2 = this.MultiplicativeExpression();
      Expression var1 = var2;

      while(true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 1:
            case 2:
               Expression var3;
               byte var4;
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 1:
                     this.jj_consume_token(1);
                     var3 = this.MultiplicativeExpression();
                     var4 = 2;
                     break;
                  case 2:
                     this.jj_consume_token(2);
                     var3 = this.MultiplicativeExpression();
                     var4 = 3;
                     break;
                  default:
                     this.jj_la1[1] = this.jj_gen;
                     this.jj_consume_token(-1);
                     throw new ParseException();
               }

               var2 = var1;
               var1 = new Expression();
               var1.operator = var4;
               var1.operand1 = new Value((byte)28, var2);
               var1.operand2 = new Value((byte)28, var3);
               break;
            default:
               this.jj_la1[0] = this.jj_gen;
               return var1;
         }
      }
   }

   public final Expression MultiplicativeExpression() throws ParseException {
      Expression var2 = this.UnaryExpression();
      Expression var1 = var2;

      while(true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 3:
            case 4:
            case 5:
               Expression var3;
               byte var4;
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 3:
                     this.jj_consume_token(3);
                     var3 = this.UnaryExpression();
                     var4 = 4;
                     break;
                  case 4:
                     this.jj_consume_token(4);
                     var3 = this.UnaryExpression();
                     var4 = 5;
                     break;
                  case 5:
                     this.jj_consume_token(5);
                     var3 = this.UnaryExpression();
                     var4 = 6;
                     break;
                  default:
                     this.jj_la1[3] = this.jj_gen;
                     this.jj_consume_token(-1);
                     throw new ParseException();
               }

               var2 = var1;
               var1 = new Expression();
               var1.operator = var4;
               var1.operand1 = new Value((byte)28, var2);
               var1.operand2 = new Value((byte)28, var3);
               break;
            default:
               this.jj_la1[2] = this.jj_gen;
               return var1;
         }
      }
   }

   public final Expression UnaryExpression() throws ParseException {
      Expression var1;
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 2:
            this.jj_consume_token(2);
            var1 = this.PrimaryExpression();
            Expression var2 = new Expression();
            var2.operator = 1;
            var2.operand1 = new Value((byte)28, var1);
            return var2;
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         case 9:
         default:
            this.jj_la1[4] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         case 6:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
            var1 = this.PrimaryExpression();
            return var1;
      }
   }

   public final Expression PrimaryExpression() throws ParseException {
      Expression var1;
      Token var3;
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 10:
            var3 = this.jj_consume_token(10);
            var1 = new Expression();
            var1.operand1 = length(var3.image);
            return var1;
         case 11:
            var3 = this.jj_consume_token(11);
            var1 = new Expression();
            var1.operand1 = percentage(var3.image);
            return var1;
         case 12:
            var3 = this.jj_consume_token(12);
            var1 = new Expression();
            var1.operand1 = number(var3.image);
            return var1;
         default:
            this.jj_la1[5] = this.jj_gen;
            if (this.jj_2_1(2)) {
               Value var2 = this.FunctionCall();
               var1 = new Expression();
               var1.operand1 = var2;
               return var1;
            } else {
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 6:
                     this.jj_consume_token(6);
                     var1 = this.AdditiveExpression();
                     this.jj_consume_token(7);
                     return var1;
                  case 13:
                     var3 = this.jj_consume_token(13);
                     var1 = new Expression();
                     var1.operand1 = name(var3.image);
                     return var1;
                  case 14:
                     var3 = this.jj_consume_token(14);
                     var1 = new Expression();
                     var1.operand1 = color(var3.image);
                     return var1;
                  default:
                     this.jj_la1[6] = this.jj_gen;
                     this.jj_consume_token(-1);
                     throw new ParseException();
               }
            }
      }
   }

   public final Value FunctionCall() throws ParseException {
      Vector var2 = null;
      Token var1 = this.jj_consume_token(13);
      this.jj_consume_token(6);
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 2:
         case 6:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
            var2 = this.FunctionArguments();
            break;
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         case 9:
         default:
            this.jj_la1[7] = this.jj_gen;
      }

      this.jj_consume_token(7);
      return function(var1.image, var2);
   }

   public final Vector FunctionArguments() throws ParseException {
      Vector var1 = new Vector();
      Expression var2 = this.AdditiveExpression();
      var1.addElement(var2);

      while(true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 8:
               this.jj_consume_token(8);
               var2 = this.AdditiveExpression();
               var1.addElement(var2);
               break;
            default:
               this.jj_la1[8] = this.jj_gen;
               return var1;
         }
      }
   }

   private final boolean jj_2_1(int var1) {
      this.jj_la = var1;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_1();
         return var2;
      } catch (LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(0, var1);
      }

      return var3;
   }

   private final boolean jj_3R_4() {
      if (this.jj_scan_token(13)) {
         return true;
      } else {
         return this.jj_scan_token(6);
      }
   }

   private final boolean jj_3_1() {
      return this.jj_3R_4();
   }

   private static void jj_la1_0() {
      jj_la1_0 = new int[]{6, 6, 56, 56, 31812, 7168, 24640, 31812, 256};
   }

   public Parser(InputStream var1) {
      this.jj_input_stream = new SimpleCharStream(var1, 1, 1);
      this.token_source = new ParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      int var2;
      for(var2 = 0; var2 < 9; ++var2) {
         this.jj_la1[var2] = -1;
      }

      for(var2 = 0; var2 < this.jj_2_rtns.length; ++var2) {
         this.jj_2_rtns[var2] = new JJCalls();
      }

   }

   public void ReInit(InputStream var1) {
      this.jj_input_stream.ReInit((InputStream)var1, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      int var2;
      for(var2 = 0; var2 < 9; ++var2) {
         this.jj_la1[var2] = -1;
      }

      for(var2 = 0; var2 < this.jj_2_rtns.length; ++var2) {
         this.jj_2_rtns[var2] = new JJCalls();
      }

   }

   public Parser(Reader var1) {
      this.jj_input_stream = new SimpleCharStream(var1, 1, 1);
      this.token_source = new ParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      int var2;
      for(var2 = 0; var2 < 9; ++var2) {
         this.jj_la1[var2] = -1;
      }

      for(var2 = 0; var2 < this.jj_2_rtns.length; ++var2) {
         this.jj_2_rtns[var2] = new JJCalls();
      }

   }

   public void ReInit(Reader var1) {
      this.jj_input_stream.ReInit((Reader)var1, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      int var2;
      for(var2 = 0; var2 < 9; ++var2) {
         this.jj_la1[var2] = -1;
      }

      for(var2 = 0; var2 < this.jj_2_rtns.length; ++var2) {
         this.jj_2_rtns[var2] = new JJCalls();
      }

   }

   public Parser(ParserTokenManager var1) {
      this.token_source = var1;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      int var2;
      for(var2 = 0; var2 < 9; ++var2) {
         this.jj_la1[var2] = -1;
      }

      for(var2 = 0; var2 < this.jj_2_rtns.length; ++var2) {
         this.jj_2_rtns[var2] = new JJCalls();
      }

   }

   public void ReInit(ParserTokenManager var1) {
      this.token_source = var1;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      int var2;
      for(var2 = 0; var2 < 9; ++var2) {
         this.jj_la1[var2] = -1;
      }

      for(var2 = 0; var2 < this.jj_2_rtns.length; ++var2) {
         this.jj_2_rtns[var2] = new JJCalls();
      }

   }

   private final Token jj_consume_token(int var1) throws ParseException {
      Token var2;
      if ((var2 = this.token).next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      if (this.token.kind != var1) {
         this.token = var2;
         this.jj_kind = var1;
         throw this.generateParseException();
      } else {
         ++this.jj_gen;
         if (++this.jj_gc > 100) {
            this.jj_gc = 0;

            for(int var3 = 0; var3 < this.jj_2_rtns.length; ++var3) {
               for(JJCalls var4 = this.jj_2_rtns[var3]; var4 != null; var4 = var4.next) {
                  if (var4.gen < this.jj_gen) {
                     var4.first = null;
                  }
               }
            }
         }

         return this.token;
      }
   }

   private final boolean jj_scan_token(int var1) {
      if (this.jj_scanpos == this.jj_lastpos) {
         --this.jj_la;
         if (this.jj_scanpos.next == null) {
            this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken();
         } else {
            this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next;
         }
      } else {
         this.jj_scanpos = this.jj_scanpos.next;
      }

      if (this.jj_rescan) {
         int var2 = 0;

         Token var3;
         for(var3 = this.token; var3 != null && var3 != this.jj_scanpos; var3 = var3.next) {
            ++var2;
         }

         if (var3 != null) {
            this.jj_add_error_token(var1, var2);
         }
      }

      if (this.jj_scanpos.kind != var1) {
         return true;
      } else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) {
         throw this.jj_ls;
      } else {
         return false;
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

   public final Token getToken(int var1) {
      Token var2 = this.lookingAhead ? this.jj_scanpos : this.token;

      for(int var3 = 0; var3 < var1; ++var3) {
         if (var2.next != null) {
            var2 = var2.next;
         } else {
            var2 = var2.next = this.token_source.getNextToken();
         }
      }

      return var2;
   }

   private final int jj_ntk() {
      return (this.jj_nt = this.token.next) == null ? (this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind) : (this.jj_ntk = this.jj_nt.kind);
   }

   private void jj_add_error_token(int var1, int var2) {
      if (var2 < 100) {
         if (var2 == this.jj_endpos + 1) {
            this.jj_lasttokens[this.jj_endpos++] = var1;
         } else if (this.jj_endpos != 0) {
            this.jj_expentry = new int[this.jj_endpos];

            for(int var3 = 0; var3 < this.jj_endpos; ++var3) {
               this.jj_expentry[var3] = this.jj_lasttokens[var3];
            }

            boolean var7 = false;
            Enumeration var4 = this.jj_expentries.elements();

            label48:
            do {
               int[] var5;
               do {
                  if (!var4.hasMoreElements()) {
                     break label48;
                  }

                  var5 = (int[])((int[])var4.nextElement());
               } while(var5.length != this.jj_expentry.length);

               var7 = true;

               for(int var6 = 0; var6 < this.jj_expentry.length; ++var6) {
                  if (var5[var6] != this.jj_expentry[var6]) {
                     var7 = false;
                     break;
                  }
               }
            } while(!var7);

            if (!var7) {
               this.jj_expentries.addElement(this.jj_expentry);
            }

            if (var2 != 0) {
               this.jj_lasttokens[(this.jj_endpos = var2) - 1] = var1;
            }
         }

      }
   }

   public ParseException generateParseException() {
      this.jj_expentries.removeAllElements();
      boolean[] var1 = new boolean[19];

      int var2;
      for(var2 = 0; var2 < 19; ++var2) {
         var1[var2] = false;
      }

      if (this.jj_kind >= 0) {
         var1[this.jj_kind] = true;
         this.jj_kind = -1;
      }

      int var3;
      for(var2 = 0; var2 < 9; ++var2) {
         if (this.jj_la1[var2] == this.jj_gen) {
            for(var3 = 0; var3 < 32; ++var3) {
               if ((jj_la1_0[var2] & 1 << var3) != 0) {
                  var1[var3] = true;
               }
            }
         }
      }

      for(var2 = 0; var2 < 19; ++var2) {
         if (var1[var2]) {
            this.jj_expentry = new int[1];
            this.jj_expentry[0] = var2;
            this.jj_expentries.addElement(this.jj_expentry);
         }
      }

      this.jj_endpos = 0;
      this.jj_rescan_token();
      this.jj_add_error_token(0, 0);
      int[][] var4 = new int[this.jj_expentries.size()][];

      for(var3 = 0; var3 < this.jj_expentries.size(); ++var3) {
         var4[var3] = (int[])((int[])this.jj_expentries.elementAt(var3));
      }

      return new ParseException(this.token, var4, tokenImage);
   }

   public final void enable_tracing() {
   }

   public final void disable_tracing() {
   }

   private final void jj_rescan_token() {
      this.jj_rescan = true;

      for(int var1 = 0; var1 < 1; ++var1) {
         JJCalls var2 = this.jj_2_rtns[var1];

         do {
            if (var2.gen > this.jj_gen) {
               this.jj_la = var2.arg;
               this.jj_lastpos = this.jj_scanpos = var2.first;
               switch (var1) {
                  case 0:
                     this.jj_3_1();
               }
            }

            var2 = var2.next;
         } while(var2 != null);
      }

      this.jj_rescan = false;
   }

   private final void jj_save(int var1, int var2) {
      JJCalls var3;
      for(var3 = this.jj_2_rtns[var1]; var3.gen > this.jj_gen; var3 = var3.next) {
         if (var3.next == null) {
            var3 = var3.next = new JJCalls();
            break;
         }
      }

      var3.gen = this.jj_gen + var2 - this.jj_la;
      var3.first = this.token;
      var3.arg = var2;
   }

   static {
      jj_la1_0();
   }

   static final class JJCalls {
      int gen;
      Token first;
      int arg;
      JJCalls next;
   }

   private static final class LookaheadSuccess extends Error {
      private LookaheadSuccess() {
      }

      // $FF: synthetic method
      LookaheadSuccess(Object var1) {
         this();
      }
   }
}
