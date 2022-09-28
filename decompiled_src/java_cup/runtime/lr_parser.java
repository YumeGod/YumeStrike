package java_cup.runtime;

import java.util.Stack;

public abstract class lr_parser {
   protected static final int _error_sync_size = 3;
   protected boolean _done_parsing;
   protected int tos;
   protected Symbol cur_token;
   protected Stack stack;
   protected short[][] production_tab;
   protected short[][] action_tab;
   protected short[][] reduce_tab;
   private Scanner _scanner;
   protected Symbol[] lookahead;
   protected int lookahead_pos;

   public lr_parser() {
      this._done_parsing = false;
      this.stack = new Stack();
   }

   public lr_parser(Scanner var1) {
      this();
      this.setScanner(var1);
   }

   public abstract int EOF_sym();

   public abstract short[][] action_table();

   protected boolean advance_lookahead() {
      ++this.lookahead_pos;
      return this.lookahead_pos < this.error_sync_size();
   }

   protected Symbol cur_err_token() {
      return this.lookahead[this.lookahead_pos];
   }

   public void debug_message(String var1) {
      System.err.println(var1);
   }

   public Symbol debug_parse() throws Exception {
      Symbol var2 = null;
      this.production_tab = this.production_table();
      this.action_tab = this.action_table();
      this.reduce_tab = this.reduce_table();
      this.debug_message("# Initializing parser");
      this.init_actions();
      this.user_init();
      this.cur_token = this.scan();
      this.debug_message("# Current Symbol is #" + this.cur_token.sym);
      this.stack.removeAllElements();
      this.stack.push(new Symbol(0, this.start_state()));
      this.tos = 0;
      this._done_parsing = false;

      while(!this._done_parsing) {
         if (this.cur_token.used_by_parser) {
            throw new Error("Symbol recycling detected (fix your scanner).");
         }

         short var1 = this.get_action(((Symbol)this.stack.peek()).parse_state, this.cur_token.sym);
         if (var1 > 0) {
            this.cur_token.parse_state = var1 - 1;
            this.cur_token.used_by_parser = true;
            this.debug_shift(this.cur_token);
            this.stack.push(this.cur_token);
            ++this.tos;
            this.cur_token = this.scan();
            this.debug_message("# Current token is " + this.cur_token);
         } else if (var1 >= 0) {
            if (var1 == 0) {
               this.syntax_error(this.cur_token);
               if (!this.error_recovery(true)) {
                  this.unrecovered_syntax_error(this.cur_token);
                  this.done_parsing();
               } else {
                  var2 = (Symbol)this.stack.peek();
               }
            }
         } else {
            var2 = this.do_action(-var1 - 1, this, this.stack, this.tos);
            short var4 = this.production_tab[-var1 - 1][0];
            short var3 = this.production_tab[-var1 - 1][1];
            this.debug_reduce(-var1 - 1, var4, var3);

            for(int var5 = 0; var5 < var3; ++var5) {
               this.stack.pop();
               --this.tos;
            }

            var1 = this.get_reduce(((Symbol)this.stack.peek()).parse_state, var4);
            this.debug_message("# Reduce rule: top state " + ((Symbol)this.stack.peek()).parse_state + ", lhs sym " + var4 + " -> state " + var1);
            var2.parse_state = var1;
            var2.used_by_parser = true;
            this.stack.push(var2);
            ++this.tos;
            this.debug_message("# Goto state #" + var1);
         }
      }

      return var2;
   }

   public void debug_reduce(int var1, int var2, int var3) {
      this.debug_message("# Reduce with prod #" + var1 + " [NT=" + var2 + ", " + "SZ=" + var3 + "]");
   }

   public void debug_shift(Symbol var1) {
      this.debug_message("# Shift under term #" + var1.sym + " to state #" + var1.parse_state);
   }

   public void debug_stack() {
      StringBuffer var1 = new StringBuffer("## STACK:");

      for(int var2 = 0; var2 < this.stack.size(); ++var2) {
         Symbol var3 = (Symbol)this.stack.elementAt(var2);
         var1.append(" <state " + var3.parse_state + ", sym " + var3.sym + ">");
         if (var2 % 3 == 2 || var2 == this.stack.size() - 1) {
            this.debug_message(var1.toString());
            var1 = new StringBuffer("         ");
         }
      }

   }

   public abstract Symbol do_action(int var1, lr_parser var2, Stack var3, int var4) throws Exception;

   public void done_parsing() {
      this._done_parsing = true;
   }

   public void dump_stack() {
      if (this.stack == null) {
         this.debug_message("# Stack dump requested, but stack is null");
      } else {
         this.debug_message("============ Parse Stack Dump ============");

         for(int var1 = 0; var1 < this.stack.size(); ++var1) {
            this.debug_message("Symbol: " + ((Symbol)this.stack.elementAt(var1)).sym + " State: " + ((Symbol)this.stack.elementAt(var1)).parse_state);
         }

         this.debug_message("==========================================");
      }
   }

   protected boolean error_recovery(boolean var1) throws Exception {
      if (var1) {
         this.debug_message("# Attempting error recovery");
      }

      if (!this.find_recovery_config(var1)) {
         if (var1) {
            this.debug_message("# Error recovery fails");
         }

         return false;
      } else {
         this.read_lookahead();

         while(true) {
            if (var1) {
               this.debug_message("# Trying to parse ahead");
            }

            if (this.try_parse_ahead(var1)) {
               if (var1) {
                  this.debug_message("# Parse-ahead ok, going back to normal parse");
               }

               this.parse_lookahead(var1);
               return true;
            }

            if (this.lookahead[0].sym == this.EOF_sym()) {
               if (var1) {
                  this.debug_message("# Error recovery fails at EOF");
               }

               return false;
            }

            if (var1) {
               this.debug_message("# Consuming Symbol #" + this.lookahead[0].sym);
            }

            this.restart_lookahead();
         }
      }
   }

   public abstract int error_sym();

   protected int error_sync_size() {
      return 3;
   }

   protected boolean find_recovery_config(boolean var1) {
      if (var1) {
         this.debug_message("# Finding recovery state on stack");
      }

      int var4 = ((Symbol)this.stack.peek()).right;
      int var5 = ((Symbol)this.stack.peek()).left;

      do {
         if (this.shift_under_error()) {
            short var3 = this.get_action(((Symbol)this.stack.peek()).parse_state, this.error_sym());
            if (var1) {
               this.debug_message("# Recover state found (#" + ((Symbol)this.stack.peek()).parse_state + ")");
               this.debug_message("# Shifting on error to state #" + (var3 - 1));
            }

            Symbol var2 = new Symbol(this.error_sym(), var5, var4);
            var2.parse_state = var3 - 1;
            var2.used_by_parser = true;
            this.stack.push(var2);
            ++this.tos;
            return true;
         }

         if (var1) {
            this.debug_message("# Pop stack by one, state was # " + ((Symbol)this.stack.peek()).parse_state);
         }

         var5 = ((Symbol)this.stack.pop()).left;
         --this.tos;
      } while(!this.stack.empty());

      if (var1) {
         this.debug_message("# No recovery state found on stack");
      }

      return false;
   }

   public Scanner getScanner() {
      return this._scanner;
   }

   protected final short get_action(int var1, int var2) {
      short[] var7 = this.action_tab[var1];
      int var6;
      if (var7.length < 20) {
         for(var6 = 0; var6 < var7.length; ++var6) {
            short var3 = var7[var6++];
            if (var3 == var2 || var3 == -1) {
               return var7[var6];
            }
         }

         return 0;
      } else {
         int var4 = 0;
         int var5 = (var7.length - 1) / 2 - 1;

         while(var4 <= var5) {
            var6 = (var4 + var5) / 2;
            if (var2 == var7[var6 * 2]) {
               return var7[var6 * 2 + 1];
            }

            if (var2 > var7[var6 * 2]) {
               var4 = var6 + 1;
            } else {
               var5 = var6 - 1;
            }
         }

         return var7[var7.length - 1];
      }
   }

   protected final short get_reduce(int var1, int var2) {
      short[] var4 = this.reduce_tab[var1];
      if (var4 == null) {
         return -1;
      } else {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            short var3 = var4[var5++];
            if (var3 == var2 || var3 == -1) {
               return var4[var5];
            }
         }

         return -1;
      }
   }

   protected abstract void init_actions() throws Exception;

   public Symbol parse() throws Exception {
      Symbol var2 = null;
      this.production_tab = this.production_table();
      this.action_tab = this.action_table();
      this.reduce_tab = this.reduce_table();
      this.init_actions();
      this.user_init();
      this.cur_token = this.scan();
      this.stack.removeAllElements();
      this.stack.push(new Symbol(0, this.start_state()));
      this.tos = 0;
      this._done_parsing = false;

      while(!this._done_parsing) {
         if (this.cur_token.used_by_parser) {
            throw new Error("Symbol recycling detected (fix your scanner).");
         }

         short var1 = this.get_action(((Symbol)this.stack.peek()).parse_state, this.cur_token.sym);
         if (var1 > 0) {
            this.cur_token.parse_state = var1 - 1;
            this.cur_token.used_by_parser = true;
            this.stack.push(this.cur_token);
            ++this.tos;
            this.cur_token = this.scan();
         } else if (var1 >= 0) {
            if (var1 == 0) {
               this.syntax_error(this.cur_token);
               if (!this.error_recovery(false)) {
                  this.unrecovered_syntax_error(this.cur_token);
                  this.done_parsing();
               } else {
                  var2 = (Symbol)this.stack.peek();
               }
            }
         } else {
            var2 = this.do_action(-var1 - 1, this, this.stack, this.tos);
            short var4 = this.production_tab[-var1 - 1][0];
            short var3 = this.production_tab[-var1 - 1][1];

            for(int var5 = 0; var5 < var3; ++var5) {
               this.stack.pop();
               --this.tos;
            }

            var1 = this.get_reduce(((Symbol)this.stack.peek()).parse_state, var4);
            var2.parse_state = var1;
            var2.used_by_parser = true;
            this.stack.push(var2);
            ++this.tos;
         }
      }

      return var2;
   }

   protected void parse_lookahead(boolean var1) throws Exception {
      Symbol var3 = null;
      this.lookahead_pos = 0;
      if (var1) {
         this.debug_message("# Reparsing saved input with actions");
         this.debug_message("# Current Symbol is #" + this.cur_err_token().sym);
         this.debug_message("# Current state is #" + ((Symbol)this.stack.peek()).parse_state);
      }

      while(true) {
         while(!this._done_parsing) {
            short var2 = this.get_action(((Symbol)this.stack.peek()).parse_state, this.cur_err_token().sym);
            if (var2 > 0) {
               this.cur_err_token().parse_state = var2 - 1;
               this.cur_err_token().used_by_parser = true;
               if (var1) {
                  this.debug_shift(this.cur_err_token());
               }

               this.stack.push(this.cur_err_token());
               ++this.tos;
               if (!this.advance_lookahead()) {
                  if (var1) {
                     this.debug_message("# Completed reparse");
                  }

                  return;
               }

               if (var1) {
                  this.debug_message("# Current Symbol is #" + this.cur_err_token().sym);
               }
            } else if (var2 >= 0) {
               if (var2 == 0) {
                  this.report_fatal_error("Syntax error", var3);
                  return;
               }
            } else {
               var3 = this.do_action(-var2 - 1, this, this.stack, this.tos);
               short var5 = this.production_tab[-var2 - 1][0];
               short var4 = this.production_tab[-var2 - 1][1];
               if (var1) {
                  this.debug_reduce(-var2 - 1, var5, var4);
               }

               for(int var6 = 0; var6 < var4; ++var6) {
                  this.stack.pop();
                  --this.tos;
               }

               var2 = this.get_reduce(((Symbol)this.stack.peek()).parse_state, var5);
               var3.parse_state = var2;
               var3.used_by_parser = true;
               this.stack.push(var3);
               ++this.tos;
               if (var1) {
                  this.debug_message("# Goto state #" + var2);
               }
            }
         }

         return;
      }
   }

   public abstract short[][] production_table();

   protected void read_lookahead() throws Exception {
      this.lookahead = new Symbol[this.error_sync_size()];

      for(int var1 = 0; var1 < this.error_sync_size(); ++var1) {
         this.lookahead[var1] = this.cur_token;
         this.cur_token = this.scan();
      }

      this.lookahead_pos = 0;
   }

   public abstract short[][] reduce_table();

   public void report_error(String var1, Object var2) {
      System.err.print(var1);
      if (var2 instanceof Symbol) {
         if (((Symbol)var2).left != -1) {
            System.err.println(" at character " + ((Symbol)var2).left + " of input");
         } else {
            System.err.println("");
         }
      } else {
         System.err.println("");
      }

   }

   public void report_fatal_error(String var1, Object var2) throws Exception {
      this.done_parsing();
      this.report_error(var1, var2);
      throw new Exception("Can't recover from previous error(s)");
   }

   protected void restart_lookahead() throws Exception {
      for(int var1 = 1; var1 < this.error_sync_size(); ++var1) {
         this.lookahead[var1 - 1] = this.lookahead[var1];
      }

      this.lookahead[this.error_sync_size() - 1] = this.cur_token;
      this.cur_token = this.scan();
      this.lookahead_pos = 0;
   }

   public Symbol scan() throws Exception {
      Symbol var1 = this.getScanner().next_token();
      return var1 != null ? var1 : new Symbol(this.EOF_sym());
   }

   public void setScanner(Scanner var1) {
      this._scanner = var1;
   }

   protected boolean shift_under_error() {
      return this.get_action(((Symbol)this.stack.peek()).parse_state, this.error_sym()) > 0;
   }

   public abstract int start_production();

   public abstract int start_state();

   public void syntax_error(Symbol var1) {
      this.report_error("Syntax error", var1);
   }

   protected boolean try_parse_ahead(boolean var1) throws Exception {
      virtual_parse_stack var5 = new virtual_parse_stack(this.stack);

      do {
         while(true) {
            short var2 = this.get_action(var5.top(), this.cur_err_token().sym);
            if (var2 == 0) {
               return false;
            }

            if (var2 > 0) {
               var5.push(var2 - 1);
               if (var1) {
                  this.debug_message("# Parse-ahead shifts Symbol #" + this.cur_err_token().sym + " into state #" + (var2 - 1));
               }
               break;
            }

            if (-var2 - 1 == this.start_production()) {
               if (var1) {
                  this.debug_message("# Parse-ahead accepts");
               }

               return true;
            }

            short var3 = this.production_tab[-var2 - 1][0];
            short var4 = this.production_tab[-var2 - 1][1];

            for(int var6 = 0; var6 < var4; ++var6) {
               var5.pop();
            }

            if (var1) {
               this.debug_message("# Parse-ahead reduces: handle size = " + var4 + " lhs = #" + var3 + " from state #" + var5.top());
            }

            var5.push(this.get_reduce(var5.top(), var3));
            if (var1) {
               this.debug_message("# Goto state #" + var5.top());
            }
         }
      } while(this.advance_lookahead());

      return true;
   }

   protected static short[][] unpackFromStrings(String[] var0) {
      StringBuffer var1 = new StringBuffer(var0[0]);

      for(int var2 = 1; var2 < var0.length; ++var2) {
         var1.append(var0[var2]);
      }

      int var3 = 0;
      int var4 = var1.charAt(var3) << 16 | var1.charAt(var3 + 1);
      var3 += 2;
      short[][] var5 = new short[var4][];

      for(int var6 = 0; var6 < var4; ++var6) {
         int var7 = var1.charAt(var3) << 16 | var1.charAt(var3 + 1);
         var3 += 2;
         var5[var6] = new short[var7];

         for(int var8 = 0; var8 < var7; ++var8) {
            var5[var6][var8] = (short)(var1.charAt(var3++) - 2);
         }
      }

      return var5;
   }

   public void unrecovered_syntax_error(Symbol var1) throws Exception {
      this.report_fatal_error("Couldn't repair and continue parse", var1);
   }

   public void user_init() throws Exception {
   }
}
