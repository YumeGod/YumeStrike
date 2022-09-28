package org.apache.regexp;

import java.util.Vector;

public class RE {
   public static final int MATCH_NORMAL = 0;
   public static final int MATCH_CASEINDEPENDENT = 1;
   public static final int MATCH_MULTILINE = 2;
   public static final int MATCH_SINGLELINE = 4;
   static final char OP_END = 'E';
   static final char OP_BOL = '^';
   static final char OP_EOL = '$';
   static final char OP_ANY = '.';
   static final char OP_ANYOF = '[';
   static final char OP_BRANCH = '|';
   static final char OP_ATOM = 'A';
   static final char OP_STAR = '*';
   static final char OP_PLUS = '+';
   static final char OP_MAYBE = '?';
   static final char OP_ESCAPE = '\\';
   static final char OP_OPEN = '(';
   static final char OP_CLOSE = ')';
   static final char OP_BACKREF = '#';
   static final char OP_GOTO = 'G';
   static final char OP_NOTHING = 'N';
   static final char OP_RELUCTANTSTAR = '8';
   static final char OP_RELUCTANTPLUS = '=';
   static final char OP_RELUCTANTMAYBE = '/';
   static final char OP_POSIXCLASS = 'P';
   static final char E_ALNUM = 'w';
   static final char E_NALNUM = 'W';
   static final char E_BOUND = 'b';
   static final char E_NBOUND = 'B';
   static final char E_SPACE = 's';
   static final char E_NSPACE = 'S';
   static final char E_DIGIT = 'd';
   static final char E_NDIGIT = 'D';
   static final char POSIX_CLASS_ALNUM = 'w';
   static final char POSIX_CLASS_ALPHA = 'a';
   static final char POSIX_CLASS_BLANK = 'b';
   static final char POSIX_CLASS_CNTRL = 'c';
   static final char POSIX_CLASS_DIGIT = 'd';
   static final char POSIX_CLASS_GRAPH = 'g';
   static final char POSIX_CLASS_LOWER = 'l';
   static final char POSIX_CLASS_PRINT = 'p';
   static final char POSIX_CLASS_PUNCT = '!';
   static final char POSIX_CLASS_SPACE = 's';
   static final char POSIX_CLASS_UPPER = 'u';
   static final char POSIX_CLASS_XDIGIT = 'x';
   static final char POSIX_CLASS_JSTART = 'j';
   static final char POSIX_CLASS_JPART = 'k';
   static final int maxNode = 65536;
   static final int maxParen = 16;
   static final int offsetOpcode = 0;
   static final int offsetOpdata = 1;
   static final int offsetNext = 2;
   static final int nodeSize = 3;
   static final String NEWLINE = System.getProperty("line.separator");
   REProgram program;
   CharacterIterator search;
   int idx;
   int matchFlags;
   int parenCount;
   int start0;
   int end0;
   int start1;
   int end1;
   int start2;
   int end2;
   int[] startn;
   int[] endn;
   int[] startBackref;
   int[] endBackref;
   public static final int REPLACE_ALL = 0;
   public static final int REPLACE_FIRSTONLY = 1;

   public RE() {
      this((REProgram)null, 0);
   }

   public RE(String var1) throws RESyntaxException {
      this((String)var1, 0);
   }

   public RE(String var1, int var2) throws RESyntaxException {
      this((new RECompiler()).compile(var1));
      this.setMatchFlags(var2);
   }

   public RE(REProgram var1) {
      this((REProgram)var1, 0);
   }

   public RE(REProgram var1, int var2) {
      this.setProgram(var1);
      this.setMatchFlags(var2);
   }

   private final void allocParens() {
      this.startn = new int[16];
      this.endn = new int[16];

      for(int var1 = 0; var1 < 16; ++var1) {
         this.startn[var1] = -1;
         this.endn[var1] = -1;
      }

   }

   public int getMatchFlags() {
      return this.matchFlags;
   }

   public String getParen(int var1) {
      int var2;
      return var1 < this.parenCount && (var2 = this.getParenStart(var1)) >= 0 ? this.search.substring(var2, this.getParenEnd(var1)) : null;
   }

   public int getParenCount() {
      return this.parenCount;
   }

   public final int getParenEnd(int var1) {
      if (var1 < this.parenCount) {
         switch (var1) {
            case 0:
               return this.end0;
            case 1:
               return this.end1;
            case 2:
               return this.end2;
            default:
               if (this.endn == null) {
                  this.allocParens();
               }

               return this.endn[var1];
         }
      } else {
         return -1;
      }
   }

   public final int getParenLength(int var1) {
      return var1 < this.parenCount ? this.getParenEnd(var1) - this.getParenStart(var1) : -1;
   }

   public final int getParenStart(int var1) {
      if (var1 < this.parenCount) {
         switch (var1) {
            case 0:
               return this.start0;
            case 1:
               return this.start1;
            case 2:
               return this.start2;
            default:
               if (this.startn == null) {
                  this.allocParens();
               }

               return this.startn[var1];
         }
      } else {
         return -1;
      }
   }

   public REProgram getProgram() {
      return this.program;
   }

   public String[] grep(Object[] var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3].toString();
         if (this.match(var4)) {
            var2.addElement(var4);
         }
      }

      String[] var5 = new String[var2.size()];
      var2.copyInto(var5);
      return var5;
   }

   protected void internalError(String var1) throws Error {
      throw new Error("RE internal error: " + var1);
   }

   private boolean isNewline(int var1) {
      if (var1 < NEWLINE.length() - 1) {
         return false;
      } else if (this.search.charAt(var1) == '\n') {
         return true;
      } else {
         for(int var2 = NEWLINE.length() - 1; var2 >= 0; --var1) {
            if (NEWLINE.charAt(var2) != this.search.charAt(var1)) {
               return false;
            }

            --var2;
         }

         return true;
      }
   }

   public boolean match(String var1) {
      return this.match((String)var1, 0);
   }

   public boolean match(String var1, int var2) {
      return this.match((CharacterIterator)(new StringCharacterIterator(var1)), var2);
   }

   public boolean match(CharacterIterator var1, int var2) {
      if (this.program == null) {
         this.internalError("No RE program to run!");
      }

      this.search = var1;
      if (this.program.prefix == null) {
         while(!var1.isEnd(var2 - 1)) {
            if (this.matchAt(var2)) {
               return true;
            }

            ++var2;
         }

         return false;
      } else {
         boolean var3 = (this.matchFlags & 1) != 0;

         for(char[] var4 = this.program.prefix; !var1.isEnd(var2 + var4.length - 1); ++var2) {
            boolean var5 = false;
            if (var3) {
               var5 = Character.toLowerCase(var1.charAt(var2)) == Character.toLowerCase(var4[0]);
            } else {
               var5 = var1.charAt(var2) == var4[0];
            }

            if (var5) {
               int var6 = var2++;
               int var7 = 1;

               while(var7 < var4.length) {
                  if (var3) {
                     var5 = Character.toLowerCase(var1.charAt(var2++)) == Character.toLowerCase(var4[var7++]);
                  } else {
                     var5 = var1.charAt(var2++) == var4[var7++];
                  }

                  if (!var5) {
                     break;
                  }
               }

               if (var7 == var4.length && this.matchAt(var6)) {
                  return true;
               }

               var2 = var6;
            }
         }

         return false;
      }
   }

   protected boolean matchAt(int var1) {
      this.start0 = -1;
      this.end0 = -1;
      this.start1 = -1;
      this.end1 = -1;
      this.start2 = -1;
      this.end2 = -1;
      this.startn = null;
      this.endn = null;
      this.parenCount = 1;
      this.setParenStart(0, var1);
      if ((this.program.flags & 1) != 0) {
         this.startBackref = new int[16];
         this.endBackref = new int[16];
      }

      int var2;
      if ((var2 = this.matchNodes(0, 65536, var1)) != -1) {
         this.setParenEnd(0, var2);
         return true;
      } else {
         this.parenCount = 0;
         return false;
      }
   }

   protected int matchNodes(int var1, int var2, int var3) {
      // $FF: Couldn't be decompiled
   }

   public void setMatchFlags(int var1) {
      this.matchFlags = var1;
   }

   protected final void setParenEnd(int var1, int var2) {
      if (var1 < this.parenCount) {
         switch (var1) {
            case 0:
               this.end0 = var2;
               break;
            case 1:
               this.end1 = var2;
               break;
            case 2:
               this.end2 = var2;
               break;
            default:
               if (this.endn == null) {
                  this.allocParens();
               }

               this.endn[var1] = var2;
         }
      }

   }

   protected final void setParenStart(int var1, int var2) {
      if (var1 < this.parenCount) {
         switch (var1) {
            case 0:
               this.start0 = var2;
               break;
            case 1:
               this.start1 = var2;
               break;
            case 2:
               this.start2 = var2;
               break;
            default:
               if (this.startn == null) {
                  this.allocParens();
               }

               this.startn[var1] = var2;
         }
      }

   }

   public void setProgram(REProgram var1) {
      this.program = var1;
   }

   public static String simplePatternToFullRegularExpression(String var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         switch (var3) {
            case '$':
            case '(':
            case ')':
            case '+':
            case '.':
            case '?':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '{':
            case '|':
            case '}':
               var1.append('\\');
            default:
               var1.append(var3);
               break;
            case '*':
               var1.append(".*");
         }
      }

      return var1.toString();
   }

   public String[] split(String var1) {
      Vector var2 = new Vector();
      int var3 = 0;

      int var6;
      for(int var4 = var1.length(); var3 < var4 && this.match(var1, var3); var3 = var6) {
         int var5 = this.getParenStart(0);
         var6 = this.getParenEnd(0);
         if (var6 == var3) {
            var2.addElement(var1.substring(var3, var5 + 1));
            ++var6;
         } else {
            var2.addElement(var1.substring(var3, var5));
         }
      }

      String var7 = var1.substring(var3);
      if (var7.length() != 0) {
         var2.addElement(var7);
      }

      String[] var8 = new String[var2.size()];
      var2.copyInto(var8);
      return var8;
   }

   public String subst(String var1, String var2) {
      return this.subst(var1, var2, 0);
   }

   public String subst(String var1, String var2, int var3) {
      StringBuffer var4 = new StringBuffer();
      int var5 = 0;
      int var6 = var1.length();

      while(var5 < var6 && this.match(var1, var5)) {
         var4.append(var1.substring(var5, this.getParenStart(0)));
         var4.append(var2);
         int var7 = this.getParenEnd(0);
         if (var7 == var5) {
            ++var7;
         }

         var5 = var7;
         if ((var3 & 1) != 0) {
            break;
         }
      }

      if (var5 < var6) {
         var4.append(var1.substring(var5));
      }

      return var4.toString();
   }
}
