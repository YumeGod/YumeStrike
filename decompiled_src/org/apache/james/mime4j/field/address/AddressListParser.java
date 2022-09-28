package org.apache.james.mime4j.field.address;

import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddressListParser implements AddressListParserTreeConstants, AddressListParserConstants {
   protected JJTAddressListParserState jjtree;
   public AddressListParserTokenManager token_source;
   SimpleCharStream jj_input_stream;
   public Token token;
   public Token jj_nt;
   private int jj_ntk;
   private Token jj_scanpos;
   private Token jj_lastpos;
   private int jj_la;
   private int jj_gen;
   private final int[] jj_la1;
   private static int[] jj_la1_0;
   private static int[] jj_la1_1;
   private final JJCalls[] jj_2_rtns;
   private boolean jj_rescan;
   private int jj_gc;
   private final LookaheadSuccess jj_ls;
   private List jj_expentries;
   private int[] jj_expentry;
   private int jj_kind;
   private int[] jj_lasttokens;
   private int jj_endpos;

   public static void main(String[] args) throws ParseException {
      while(true) {
         try {
            AddressListParser parser = new AddressListParser(System.in);
            parser.parseLine();
            ((SimpleNode)parser.jjtree.rootNode()).dump("> ");
         } catch (Exception var2) {
            var2.printStackTrace();
            return;
         }
      }
   }

   public ASTaddress_list parseAddressList() throws ParseException {
      try {
         this.parseAddressList0();
         return (ASTaddress_list)this.jjtree.rootNode();
      } catch (TokenMgrError var2) {
         throw new ParseException(var2.getMessage());
      }
   }

   public ASTaddress parseAddress() throws ParseException {
      try {
         this.parseAddress0();
         return (ASTaddress)this.jjtree.rootNode();
      } catch (TokenMgrError var2) {
         throw new ParseException(var2.getMessage());
      }
   }

   public ASTmailbox parseMailbox() throws ParseException {
      try {
         this.parseMailbox0();
         return (ASTmailbox)this.jjtree.rootNode();
      } catch (TokenMgrError var2) {
         throw new ParseException(var2.getMessage());
      }
   }

   void jjtreeOpenNodeScope(Node n) {
      ((SimpleNode)n).firstToken = this.getToken(1);
   }

   void jjtreeCloseNodeScope(Node n) {
      ((SimpleNode)n).lastToken = this.getToken(0);
   }

   public final void parseLine() throws ParseException {
      this.address_list();
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 1:
            this.jj_consume_token(1);
            break;
         default:
            this.jj_la1[0] = this.jj_gen;
      }

      this.jj_consume_token(2);
   }

   public final void parseAddressList0() throws ParseException {
      this.address_list();
      this.jj_consume_token(0);
   }

   public final void parseAddress0() throws ParseException {
      this.address();
      this.jj_consume_token(0);
   }

   public final void parseMailbox0() throws ParseException {
      this.mailbox();
      this.jj_consume_token(0);
   }

   public final void address_list() throws ParseException {
      ASTaddress_list jjtn000 = new ASTaddress_list(1);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      this.jjtreeOpenNodeScope(jjtn000);

      try {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 6:
            case 14:
            case 31:
               this.address();
               break;
            default:
               this.jj_la1[1] = this.jj_gen;
         }

         while(true) {
            switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 3:
                  this.jj_consume_token(3);
                  switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                     case 6:
                     case 14:
                     case 31:
                        this.address();
                        continue;
                     default:
                        this.jj_la1[3] = this.jj_gen;
                        continue;
                  }
               default:
                  this.jj_la1[2] = this.jj_gen;
                  return;
            }
         }
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         } else {
            throw (Error)var7;
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
            this.jjtreeCloseNodeScope(jjtn000);
         }

      }
   }

   public final void address() throws ParseException {
      ASTaddress jjtn000 = new ASTaddress(2);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      this.jjtreeOpenNodeScope(jjtn000);

      try {
         if (this.jj_2_1(Integer.MAX_VALUE)) {
            this.addr_spec();
         } else {
            switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 6:
                  this.angle_addr();
                  break;
               case 14:
               case 31:
                  this.phrase();
                  switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                     case 4:
                        this.group_body();
                        return;
                     case 6:
                        this.angle_addr();
                        return;
                     default:
                        this.jj_la1[4] = this.jj_gen;
                        this.jj_consume_token(-1);
                        throw new ParseException();
                  }
               default:
                  this.jj_la1[5] = this.jj_gen;
                  this.jj_consume_token(-1);
                  throw new ParseException();
            }
         }
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
            this.jjtreeCloseNodeScope(jjtn000);
         }

      }

   }

   public final void mailbox() throws ParseException {
      ASTmailbox jjtn000 = new ASTmailbox(3);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      this.jjtreeOpenNodeScope(jjtn000);

      try {
         if (this.jj_2_2(Integer.MAX_VALUE)) {
            this.addr_spec();
         } else {
            switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 6:
                  this.angle_addr();
                  break;
               case 14:
               case 31:
                  this.name_addr();
                  break;
               default:
                  this.jj_la1[6] = this.jj_gen;
                  this.jj_consume_token(-1);
                  throw new ParseException();
            }
         }
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
            this.jjtreeCloseNodeScope(jjtn000);
         }

      }

   }

   public final void name_addr() throws ParseException {
      ASTname_addr jjtn000 = new ASTname_addr(4);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      this.jjtreeOpenNodeScope(jjtn000);

      try {
         this.phrase();
         this.angle_addr();
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
            this.jjtreeCloseNodeScope(jjtn000);
         }

      }

   }

   public final void group_body() throws ParseException {
      ASTgroup_body jjtn000 = new ASTgroup_body(5);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      this.jjtreeOpenNodeScope(jjtn000);

      try {
         this.jj_consume_token(4);
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 6:
            case 14:
            case 31:
               this.mailbox();
               break;
            default:
               this.jj_la1[7] = this.jj_gen;
         }

         while(true) {
            switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 3:
                  this.jj_consume_token(3);
                  switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                     case 6:
                     case 14:
                     case 31:
                        this.mailbox();
                        continue;
                     default:
                        this.jj_la1[9] = this.jj_gen;
                        continue;
                  }
               default:
                  this.jj_la1[8] = this.jj_gen;
                  this.jj_consume_token(5);
                  return;
            }
         }
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         } else {
            throw (Error)var7;
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
            this.jjtreeCloseNodeScope(jjtn000);
         }

      }
   }

   public final void angle_addr() throws ParseException {
      ASTangle_addr jjtn000 = new ASTangle_addr(6);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      this.jjtreeOpenNodeScope(jjtn000);

      try {
         this.jj_consume_token(6);
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 8:
               this.route();
               break;
            default:
               this.jj_la1[10] = this.jj_gen;
         }

         this.addr_spec();
         this.jj_consume_token(7);
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
            this.jjtreeCloseNodeScope(jjtn000);
         }

      }

   }

   public final void route() throws ParseException {
      ASTroute jjtn000 = new ASTroute(7);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      this.jjtreeOpenNodeScope(jjtn000);

      try {
         this.jj_consume_token(8);
         this.domain();

         label130:
         while(true) {
            switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 3:
               case 8:
                  while(true) {
                     switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                        case 3:
                           this.jj_consume_token(3);
                           break;
                        default:
                           this.jj_la1[12] = this.jj_gen;
                           this.jj_consume_token(8);
                           this.domain();
                           continue label130;
                     }
                  }
               default:
                  this.jj_la1[11] = this.jj_gen;
                  this.jj_consume_token(4);
                  return;
            }
         }
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         } else {
            throw (Error)var7;
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
            this.jjtreeCloseNodeScope(jjtn000);
         }

      }
   }

   public final void phrase() throws ParseException {
      ASTphrase jjtn000 = new ASTphrase(8);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      this.jjtreeOpenNodeScope(jjtn000);

      try {
         while(true) {
            switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 14:
                  this.jj_consume_token(14);
                  break;
               case 31:
                  this.jj_consume_token(31);
                  break;
               default:
                  this.jj_la1[13] = this.jj_gen;
                  this.jj_consume_token(-1);
                  throw new ParseException();
            }

            switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 14:
               case 31:
                  break;
               default:
                  this.jj_la1[14] = this.jj_gen;
                  return;
            }
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
            this.jjtreeCloseNodeScope(jjtn000);
         }

      }
   }

   public final void addr_spec() throws ParseException {
      ASTaddr_spec jjtn000 = new ASTaddr_spec(9);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      this.jjtreeOpenNodeScope(jjtn000);

      try {
         this.local_part();
         this.jj_consume_token(8);
         this.domain();
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
            this.jjtreeCloseNodeScope(jjtn000);
         }

      }

   }

   public final void local_part() throws ParseException {
      ASTlocal_part jjtn000 = new ASTlocal_part(10);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      this.jjtreeOpenNodeScope(jjtn000);

      try {
         Token t;
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 14:
               t = this.jj_consume_token(14);
               break;
            case 31:
               t = this.jj_consume_token(31);
               break;
            default:
               this.jj_la1[15] = this.jj_gen;
               this.jj_consume_token(-1);
               throw new ParseException();
         }

         while(true) {
            switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
               case 9:
               case 14:
               case 31:
                  switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                     case 9:
                        t = this.jj_consume_token(9);
                        break;
                     default:
                        this.jj_la1[17] = this.jj_gen;
                  }

                  if (t.kind != 31 && t.image.charAt(t.image.length() - 1) == '.') {
                     switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                        case 14:
                           t = this.jj_consume_token(14);
                           continue;
                        case 31:
                           t = this.jj_consume_token(31);
                           continue;
                        default:
                           this.jj_la1[18] = this.jj_gen;
                           this.jj_consume_token(-1);
                           throw new ParseException();
                     }
                  }

                  throw new ParseException("Words in local part must be separated by '.'");
               default:
                  this.jj_la1[16] = this.jj_gen;
                  return;
            }
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
            this.jjtreeCloseNodeScope(jjtn000);
         }

      }
   }

   public final void domain() throws ParseException {
      ASTdomain jjtn000 = new ASTdomain(11);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);
      this.jjtreeOpenNodeScope(jjtn000);

      try {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 14:
               Token t = this.jj_consume_token(14);

               while(true) {
                  switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                     case 9:
                     case 14:
                        switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                           case 9:
                              t = this.jj_consume_token(9);
                              break;
                           default:
                              this.jj_la1[20] = this.jj_gen;
                        }

                        if (t.image.charAt(t.image.length() - 1) != '.') {
                           throw new ParseException("Atoms in domain names must be separated by '.'");
                        }

                        t = this.jj_consume_token(14);
                        break;
                     default:
                        this.jj_la1[19] = this.jj_gen;
                        return;
                  }
               }
            case 18:
               this.jj_consume_token(18);
               return;
            default:
               this.jj_la1[21] = this.jj_gen;
               this.jj_consume_token(-1);
               throw new ParseException();
         }
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
            this.jjtreeCloseNodeScope(jjtn000);
         }

      }
   }

   private boolean jj_2_1(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_1();
         return var2;
      } catch (LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(0, xla);
      }

      return var3;
   }

   private boolean jj_2_2(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         boolean var2 = !this.jj_3_2();
         return var2;
      } catch (LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(1, xla);
      }

      return var3;
   }

   private boolean jj_3R_11() {
      Token xsp = this.jj_scanpos;
      if (this.jj_scan_token(9)) {
         this.jj_scanpos = xsp;
      }

      xsp = this.jj_scanpos;
      if (this.jj_scan_token(14)) {
         this.jj_scanpos = xsp;
         if (this.jj_scan_token(31)) {
            return true;
         }
      }

      return false;
   }

   private boolean jj_3R_13() {
      Token xsp = this.jj_scanpos;
      if (this.jj_scan_token(9)) {
         this.jj_scanpos = xsp;
      }

      return this.jj_scan_token(14);
   }

   private boolean jj_3R_8() {
      if (this.jj_3R_9()) {
         return true;
      } else if (this.jj_scan_token(8)) {
         return true;
      } else {
         return this.jj_3R_10();
      }
   }

   private boolean jj_3_1() {
      return this.jj_3R_8();
   }

   private boolean jj_3R_12() {
      if (this.jj_scan_token(14)) {
         return true;
      } else {
         Token xsp;
         do {
            xsp = this.jj_scanpos;
         } while(!this.jj_3R_13());

         this.jj_scanpos = xsp;
         return false;
      }
   }

   private boolean jj_3R_10() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3R_12()) {
         this.jj_scanpos = xsp;
         if (this.jj_scan_token(18)) {
            return true;
         }
      }

      return false;
   }

   private boolean jj_3_2() {
      return this.jj_3R_8();
   }

   private boolean jj_3R_9() {
      Token xsp = this.jj_scanpos;
      if (this.jj_scan_token(14)) {
         this.jj_scanpos = xsp;
         if (this.jj_scan_token(31)) {
            return true;
         }
      }

      do {
         xsp = this.jj_scanpos;
      } while(!this.jj_3R_11());

      this.jj_scanpos = xsp;
      return false;
   }

   private static void jj_la1_init_0() {
      jj_la1_0 = new int[]{2, -2147467200, 8, -2147467200, 80, -2147467200, -2147467200, -2147467200, 8, -2147467200, 256, 264, 8, -2147467264, -2147467264, -2147467264, -2147466752, 512, -2147467264, 16896, 512, 278528};
   }

   private static void jj_la1_init_1() {
      jj_la1_1 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
   }

   public AddressListParser(InputStream stream) {
      this(stream, (String)null);
   }

   public AddressListParser(InputStream stream, String encoding) {
      this.jjtree = new JJTAddressListParserState();
      this.jj_la1 = new int[22];
      this.jj_2_rtns = new JJCalls[2];
      this.jj_rescan = false;
      this.jj_gc = 0;
      this.jj_ls = new LookaheadSuccess();
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.jj_lasttokens = new int[100];

      try {
         this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }

      this.token_source = new AddressListParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      int i;
      for(i = 0; i < 22; ++i) {
         this.jj_la1[i] = -1;
      }

      for(i = 0; i < this.jj_2_rtns.length; ++i) {
         this.jj_2_rtns[i] = new JJCalls();
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
      this.jjtree.reset();
      this.jj_gen = 0;

      int i;
      for(i = 0; i < 22; ++i) {
         this.jj_la1[i] = -1;
      }

      for(i = 0; i < this.jj_2_rtns.length; ++i) {
         this.jj_2_rtns[i] = new JJCalls();
      }

   }

   public AddressListParser(Reader stream) {
      this.jjtree = new JJTAddressListParserState();
      this.jj_la1 = new int[22];
      this.jj_2_rtns = new JJCalls[2];
      this.jj_rescan = false;
      this.jj_gc = 0;
      this.jj_ls = new LookaheadSuccess();
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.jj_lasttokens = new int[100];
      this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
      this.token_source = new AddressListParserTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      int i;
      for(i = 0; i < 22; ++i) {
         this.jj_la1[i] = -1;
      }

      for(i = 0; i < this.jj_2_rtns.length; ++i) {
         this.jj_2_rtns[i] = new JJCalls();
      }

   }

   public void ReInit(Reader stream) {
      this.jj_input_stream.ReInit((Reader)stream, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jjtree.reset();
      this.jj_gen = 0;

      int i;
      for(i = 0; i < 22; ++i) {
         this.jj_la1[i] = -1;
      }

      for(i = 0; i < this.jj_2_rtns.length; ++i) {
         this.jj_2_rtns[i] = new JJCalls();
      }

   }

   public AddressListParser(AddressListParserTokenManager tm) {
      this.jjtree = new JJTAddressListParserState();
      this.jj_la1 = new int[22];
      this.jj_2_rtns = new JJCalls[2];
      this.jj_rescan = false;
      this.jj_gc = 0;
      this.jj_ls = new LookaheadSuccess();
      this.jj_expentries = new ArrayList();
      this.jj_kind = -1;
      this.jj_lasttokens = new int[100];
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      int i;
      for(i = 0; i < 22; ++i) {
         this.jj_la1[i] = -1;
      }

      for(i = 0; i < this.jj_2_rtns.length; ++i) {
         this.jj_2_rtns[i] = new JJCalls();
      }

   }

   public void ReInit(AddressListParserTokenManager tm) {
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jjtree.reset();
      this.jj_gen = 0;

      int i;
      for(i = 0; i < 22; ++i) {
         this.jj_la1[i] = -1;
      }

      for(i = 0; i < this.jj_2_rtns.length; ++i) {
         this.jj_2_rtns[i] = new JJCalls();
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
      if (this.token.kind != kind) {
         this.token = oldToken;
         this.jj_kind = kind;
         throw this.generateParseException();
      } else {
         ++this.jj_gen;
         if (++this.jj_gc > 100) {
            this.jj_gc = 0;

            for(int i = 0; i < this.jj_2_rtns.length; ++i) {
               for(JJCalls c = this.jj_2_rtns[i]; c != null; c = c.next) {
                  if (c.gen < this.jj_gen) {
                     c.first = null;
                  }
               }
            }
         }

         return this.token;
      }
   }

   private boolean jj_scan_token(int kind) {
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
         int i = 0;

         Token tok;
         for(tok = this.token; tok != null && tok != this.jj_scanpos; tok = tok.next) {
            ++i;
         }

         if (tok != null) {
            this.jj_add_error_token(kind, i);
         }
      }

      if (this.jj_scanpos.kind != kind) {
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

   private void jj_add_error_token(int kind, int pos) {
      if (pos < 100) {
         if (pos == this.jj_endpos + 1) {
            this.jj_lasttokens[this.jj_endpos++] = kind;
         } else if (this.jj_endpos != 0) {
            this.jj_expentry = new int[this.jj_endpos];

            for(int i = 0; i < this.jj_endpos; ++i) {
               this.jj_expentry[i] = this.jj_lasttokens[i];
            }

            Iterator it = this.jj_expentries.iterator();

            label41:
            while(true) {
               int[] oldentry;
               do {
                  if (!it.hasNext()) {
                     break label41;
                  }

                  oldentry = (int[])((int[])it.next());
               } while(oldentry.length != this.jj_expentry.length);

               for(int i = 0; i < this.jj_expentry.length; ++i) {
                  if (oldentry[i] != this.jj_expentry[i]) {
                     continue label41;
                  }
               }

               this.jj_expentries.add(this.jj_expentry);
               break;
            }

            if (pos != 0) {
               this.jj_lasttokens[(this.jj_endpos = pos) - 1] = kind;
            }
         }

      }
   }

   public ParseException generateParseException() {
      this.jj_expentries.clear();
      boolean[] la1tokens = new boolean[34];
      if (this.jj_kind >= 0) {
         la1tokens[this.jj_kind] = true;
         this.jj_kind = -1;
      }

      int i;
      int j;
      for(i = 0; i < 22; ++i) {
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

      for(i = 0; i < 34; ++i) {
         if (la1tokens[i]) {
            this.jj_expentry = new int[1];
            this.jj_expentry[0] = i;
            this.jj_expentries.add(this.jj_expentry);
         }
      }

      this.jj_endpos = 0;
      this.jj_rescan_token();
      this.jj_add_error_token(0, 0);
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

   private void jj_rescan_token() {
      this.jj_rescan = true;

      for(int i = 0; i < 2; ++i) {
         try {
            JJCalls p = this.jj_2_rtns[i];

            do {
               if (p.gen > this.jj_gen) {
                  this.jj_la = p.arg;
                  this.jj_lastpos = this.jj_scanpos = p.first;
                  switch (i) {
                     case 0:
                        this.jj_3_1();
                        break;
                     case 1:
                        this.jj_3_2();
                  }
               }

               p = p.next;
            } while(p != null);
         } catch (LookaheadSuccess var3) {
         }
      }

      this.jj_rescan = false;
   }

   private void jj_save(int index, int xla) {
      JJCalls p;
      for(p = this.jj_2_rtns[index]; p.gen > this.jj_gen; p = p.next) {
         if (p.next == null) {
            p = p.next = new JJCalls();
            break;
         }
      }

      p.gen = this.jj_gen + xla - this.jj_la;
      p.first = this.token;
      p.arg = xla;
   }

   static {
      jj_la1_init_0();
      jj_la1_init_1();
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
      LookaheadSuccess(Object x0) {
         this();
      }
   }
}
