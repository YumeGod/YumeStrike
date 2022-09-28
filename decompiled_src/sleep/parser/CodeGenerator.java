package sleep.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import sleep.engine.Block;
import sleep.engine.GeneratedSteps;
import sleep.engine.Step;
import sleep.engine.atoms.Check;
import sleep.engine.atoms.PLiteral;
import sleep.runtime.Scalar;
import sleep.runtime.SleepUtils;

public class CodeGenerator implements ParserConstants {
   protected Block CURRENT_BLOCK;
   protected Stack BACKUP_BLOCKS;
   protected GeneratedSteps factory;
   protected Parser parser;
   protected static HashMap escape_constants = new HashMap();

   public static void installEscapeConstant(char var0, String var1) {
      escape_constants.put(var0 + "", var1);
   }

   public Block getRunnableBlock() {
      return this.CURRENT_BLOCK;
   }

   public void add(Step var1, Token var2) {
      this.CURRENT_BLOCK.add(var1);
      var1.setInfo(var2.getHint());
   }

   public void backup() {
      this.BACKUP_BLOCKS.push(this.CURRENT_BLOCK);
      this.CURRENT_BLOCK = new Block(this.parser.getName());
   }

   public Block restore() {
      Block var1 = this.CURRENT_BLOCK;
      this.CURRENT_BLOCK = (Block)((Block)this.BACKUP_BLOCKS.pop());
      return var1;
   }

   public CodeGenerator(Parser var1, GeneratedSteps var2) {
      this.parser = var1;
      this.factory = var2 != null ? var2 : new GeneratedSteps();
      this.CURRENT_BLOCK = new Block(this.parser.getName());
      this.BACKUP_BLOCKS = new Stack();
   }

   public CodeGenerator(Parser var1) {
      this(var1, (GeneratedSteps)null);
   }

   public Check parsePredicate(Token var1) {
      Statement var2 = TokenParser.ParsePredicate(this.parser, LexicalAnalyzer.GroupBlockTokens(this.parser, new StringIterator(var1.toString(), var1.getHint())));
      return this.parsePredicate(var2);
   }

   public Check parsePredicate(Statement var1) {
      Token[] var2 = var1.getTokens();
      String[] var3 = var1.getStrings();
      Check var5;
      Check var6;
      Check var7;
      switch (var1.getType()) {
         case 801:
            this.backup();
            this.parseIdea(var2[0]);
            this.parseIdea(var2[2]);
            var5 = this.factory.Check(var3[1], this.restore());
            var5.setInfo(var2[1].getHint());
            return var5;
         case 802:
            this.backup();
            this.parseIdea(var2[1]);
            var5 = this.factory.Check(var3[0], this.restore());
            var5.setInfo(var2[0].getHint());
            return var5;
         case 803:
            var6 = this.parsePredicate(var2[0]);
            var7 = this.parsePredicate(var2[1]);
            return this.factory.CheckOr(var6, var7);
         case 804:
            var6 = this.parsePredicate(var2[0]);
            var7 = this.parsePredicate(var2[1]);
            return this.factory.CheckAnd(var6, var7);
         case 805:
            return this.parsePredicate(ParserUtilities.extract(var2[0]));
         case 806:
            if (var3[0].charAt(0) == '!' && var3[0].length() > 1) {
               return this.parsePredicate(var2[0].copy("!-istrue (" + var3[0].substring(1, var3[0].length()) + ")"));
            }

            return this.parsePredicate(var2[0].copy("-istrue (" + var3[0] + ")"));
         default:
            this.parser.reportError("Unknown predicate.", var2[0].copy(var1.toString()));
            return null;
      }
   }

   public void parseObject(Token var1) {
      Statement var2 = TokenParser.ParseObject(this.parser, LexicalAnalyzer.GroupExpressionIndexTokens(this.parser, new StringIterator(var1.toString(), var1.getHint())));
      if (!this.parser.hasErrors()) {
         this.parseObject(var2);
      }
   }

   public void parseObject(Statement var1) {
      String[] var3 = var1.getStrings();
      Token[] var4 = var1.getTokens();
      Class var5 = null;
      Step var2;
      switch (var1.getType()) {
         case 441:
            var2 = this.factory.CreateFrame();
            this.add(var2, var4[0]);
            if (var4.length > 1) {
               this.parseParameters(var4[1]);
            }

            var5 = this.parser.findImportedClass(var3[0]);
            if (var5 == null) {
               this.parser.reportError("Class " + var3[0] + " was not found", var4[0]);
            }

            var2 = this.factory.ObjectNew(var5);
            this.add(var2, var4[0]);
            break;
         case 442:
            var2 = this.factory.CreateFrame();
            this.add(var2, var4[0]);
            if (var4.length > 2) {
               this.parseParameters(var4[2]);
            }

            this.parseIdea(var4[0]);
            var2 = this.factory.ObjectAccess(var3[1]);
            this.add(var2, var4[0]);
            break;
         case 443:
            var2 = this.factory.CreateFrame();
            this.add(var2, var4[0]);
            if (var4.length > 2) {
               this.parseParameters(var4[2]);
            }

            var5 = this.parser.findImportedClass(var3[0]);
            if (var5 == null) {
               this.parser.reportError("Class " + var3[0] + " was not found", var4[0]);
            }

            var2 = this.factory.ObjectAccessStatic(var5, var3[1]);
            this.add(var2, var4[0]);
         case 444:
         case 445:
         default:
            break;
         case 446:
            var2 = this.factory.CreateFrame();
            this.add(var2, var4[0]);
            if (var4.length > 1) {
               this.parseParameters(var4[1]);
            }

            this.parseIdea(var4[0]);
            var2 = this.factory.ObjectAccess((String)null);
            this.add(var2, var4[0]);
      }

   }

   public void parseBlock(Token var1) {
      LinkedList var2 = TokenParser.ParseBlocks(this.parser, LexicalAnalyzer.GroupBlockTokens(this.parser, new StringIterator(var1.toString(), var1.getHint())));
      if (!this.parser.hasErrors()) {
         if (var2.size() == 0) {
            Step var3 = new Step();
            this.add(var3, var1);
         } else {
            this.parseBlock(var2);
         }

      }
   }

   public void parseBlock(LinkedList var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         this.parse((Statement)var2.next());
      }

   }

   public List parseIdea(Token var1) {
      LinkedList var2 = TokenParser.ParseIdea(this.parser, LexicalAnalyzer.GroupBlockTokens(this.parser, new StringIterator(var1.toString(), var1.getHint())));
      if (this.parser.hasErrors()) {
         return null;
      } else {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            this.parse((Statement)var3.next());
         }

         return var2;
      }
   }

   public void parse(Statement var1) {
      String[] var12 = var1.getStrings();
      Token[] var13 = var1.getTokens();
      Block var2;
      Block var3;
      Step var5;
      Scalar var6;
      Check var7;
      String var10;
      TokenList var24;
      Token[] var25;
      switch (var1.getType()) {
         case 100:
            this.backup();
            this.parseBlock(var13[2]);
            var5 = this.factory.Goto(this.parsePredicate(ParserUtilities.extract(var13[1])), this.restore(), (Block)null);
            this.add(var5, var13[1]);
            break;
         case 101:
            this.backup();
            this.parseBlock(var13[3]);
            var3 = this.restore();
            this.backup();
            var5 = this.factory.CreateFrame();
            this.add(var5, var13[2]);
            this.parseIdea(var13[2]);
            this.backup();
            this.parseIdea(var13[1]);
            var5 = this.factory.Assign(this.restore());
            this.add(var5, var13[2]);
            this.add(this.factory.SValue(SleepUtils.getEmptyScalar()), var13[2]);
            var2 = this.restore();
            var7 = this.factory.Check("!is", var2);
            var7.setInfo(var13[1].getHint());
            var5 = this.factory.Goto(var7, var3, (Block)null);
            this.add(var5, var13[1]);
            break;
         case 150:
            this.parseBlock(ParserUtilities.extract(var13[0]));
            break;
         case 200:
         case 203:
            var5 = this.factory.CreateFrame();
            this.add(var5, var13[2]);
            this.parseIdea(var13[2]);
            this.backup();
            this.parseIdea(var13[0]);
            if (var1.getType() == 203) {
               var5 = this.factory.AssignAndOperate(this.restore(), var12[1].substring(0, var12[1].length() - 1));
            } else {
               var5 = this.factory.Assign(this.restore());
            }

            this.add(var5, var13[2]);
            break;
         case 202:
         case 204:
            var5 = this.factory.CreateFrame();
            this.add(var5, var13[0]);
            var24 = ParserUtilities.groupByParameterTerm(this.parser, ParserUtilities.extract(var13[0]));
            var25 = var24.getTokens();

            for(int var40 = 0; var40 < var25.length; ++var40) {
               this.parseIdea(var25[var40]);
            }

            this.parseIdea(var13[2]);
            if (var1.getType() == 204) {
               var5 = this.factory.AssignTupleAndOperate(var12[1].substring(0, var12[1].length() - 1));
            } else {
               var5 = this.factory.AssignT();
            }

            this.add(var5, var13[0]);
            break;
         case 301:
            this.backup();
            this.parseBlock(var13[2]);
            var2 = this.restore();
            this.backup();
            if (var13.length >= 4) {
               if (var12[4].equals("if")) {
                  this.parseBlock(ParserUtilities.join(ParserUtilities.get(var13, 4, var13.length)));
               } else {
                  this.parseBlock(var13[4]);
               }
            }

            var3 = this.restore();
            var5 = this.factory.Decide(this.parsePredicate(ParserUtilities.extract(var13[1])), var2, var3);
            this.add(var5, var13[1]);
            break;
         case 400:
         case 402:
            var5 = this.factory.CreateFrame();
            this.add(var5, var13[0]);
            if (var1.getType() == 400) {
               this.parseIdea(ParserUtilities.extract(var13[2]));
               var5 = this.factory.IteratorCreate((String)null, var12[1]);
            } else {
               this.parseIdea(ParserUtilities.extract(var13[4]));
               var5 = this.factory.IteratorCreate(var12[1], var12[3]);
            }

            this.add(var5, var13[0]);
            this.backup();
            if (var1.getType() == 400) {
               this.parseBlock(ParserUtilities.extract(var13[3]));
            } else {
               this.parseBlock(ParserUtilities.extract(var13[5]));
            }

            var2 = this.restore();
            this.backup();
            var5 = this.factory.IteratorNext();
            this.add(var5, var13[0]);
            var7 = this.factory.Check("-istrue", this.restore());
            var7.setInfo(var13[0].getHint());
            var5 = this.factory.Goto(var7, var2, (Block)null);
            this.add(var5, var13[1]);
            var5 = this.factory.IteratorDestroy();
            this.add(var5, var13[1]);
            break;
         case 401:
            Token[] var26 = ParserUtilities.groupByBlockTerm(this.parser, ParserUtilities.extract(var13[1])).getTokens();
            StringBuffer var27 = new StringBuffer();
            TokenList var28 = ParserUtilities.groupByParameterTerm(this.parser, var26[0]);
            Iterator var8 = var28.getList().iterator();

            while(var8.hasNext()) {
               var27.append(var8.next().toString());
               var27.append("; ");
            }

            this.parseBlock(var13[0].copy(var27.toString()));
            if (var26.length != 3) {
               var2 = null;
               var27 = new StringBuffer();
            } else {
               this.backup();
               var27 = new StringBuffer();
               TokenList var41 = ParserUtilities.groupByParameterTerm(this.parser, var26[2]);
               var8 = var41.getList().iterator();

               while(var8.hasNext()) {
                  var27.append(var8.next().toString());
                  var27.append("; ");
               }

               this.parseBlock(var13[0].copy(var27.toString()));
               var2 = this.restore();
            }

            this.backup();
            this.parseBlock(var13[2]);
            this.parseBlock(var13[0].copy(var27.toString()));
            var3 = this.restore();
            var5 = this.factory.Goto(this.parsePredicate(var26[1]), var3, var2);
            this.add(var5, var13[1]);
            break;
         case 403:
            this.backup();
            this.parseBlock(ParserUtilities.extract(var13[1]));
            var5 = this.factory.PopTry();
            this.add(var5, var13[4]);
            var2 = this.restore();
            this.backup();
            var5 = this.factory.PopTry();
            this.add(var5, var13[4]);
            this.parseBlock(ParserUtilities.extract(var13[4]));
            var3 = this.restore();
            var5 = this.factory.Try(var2, var3, var12[3]);
            this.add(var5, var13[0]);
            break;
         case 444:
            try {
               if (var12.length == 1) {
                  this.parser.importPackage(var12[0], (String)null);
               } else {
                  if (Checkers.isString(var12[1]) || Checkers.isLiteral(var12[1])) {
                     var12[1] = ParserUtilities.extract(var12[1]);
                  }

                  this.parser.importPackage(var12[0], var12[1]);
               }
            } catch (Exception var32) {
               if (var13.length == 2) {
                  this.parser.reportError(var32.getMessage(), ParserUtilities.makeToken("import " + var12[0] + " from: " + var12[1], var13[1]));
               } else {
                  this.parser.reportError(var32.getMessage(), ParserUtilities.makeToken("import " + var12[0], var13[0]));
               }
            }
            break;
         case 500:
            var5 = this.factory.CreateFrame();
            this.add(var5, var13[0]);
            if (var12[0].equals("done")) {
               this.parseIdea(var13[0].copy("1"));
            } else if (var12[0].equals("halt")) {
               this.parseIdea(var13[0].copy("2"));
            } else if (var13.length >= 2) {
               this.parseIdea(var13[1]);
            } else {
               this.parseIdea(var13[0].copy("$null"));
            }

            if (var12[0].equals("break")) {
               var5 = this.factory.Return(2);
               this.add(var5, var13[0]);
            } else if (var12[0].equals("continue")) {
               var5 = this.factory.Return(4);
               this.add(var5, var13[0]);
            } else if (var12[0].equals("throw")) {
               var5 = this.factory.Return(16);
               this.add(var5, var13[0]);
            } else if (var12[0].equals("yield")) {
               var5 = this.factory.Return(8);
               this.add(var5, var13[0]);
            } else if (var12[0].equals("callcc")) {
               var5 = this.factory.Return(72);
               this.add(var5, var13[0]);
            } else {
               var5 = this.factory.Return(1);
               this.add(var5, var13[0]);
            }
            break;
         case 502:
            this.backup();
            if (!Checkers.isString(var12[1]) && !Checkers.isLiteral(var12[1])) {
               this.parseIdea(new Token("'" + var12[1] + "'", var13[1].getHint()));
            } else {
               this.parseIdea(var13[1]);
            }

            Block var37 = this.restore();
            this.backup();
            this.parseBlock(var13[2]);
            var5 = this.factory.Bind(var12[0], var37, this.restore());
            this.add(var5, var13[0]);
            break;
         case 504:
            this.backup();
            this.parseBlock(var13[2]);
            var5 = this.factory.BindPredicate(var12[0], this.parsePredicate(ParserUtilities.extract(var13[1])), this.restore());
            this.add(var5, var13[0]);
            break;
         case 505:
            this.backup();
            this.parseBlock(var13[3]);
            var3 = this.restore();
            var5 = this.factory.BindFilter(var12[0], var12[1], var3, var12[2]);
            this.add(var5, var13[0]);
            break;
         case 506:
            var5 = this.factory.CreateFrame();
            this.add(var5, var13[0]);
            var1.setType(605);
            this.parse(var1);
            var5 = this.factory.Call("__EXEC__");
            this.add(var5, var13[0]);
            break;
         case 507:
            if (var13.length == 1) {
               this.parser.reportError("Assertion can't be empty!", var13[0]);
               return;
            }

            if (Boolean.valueOf(System.getProperty("sleep.assert", "true")) == Boolean.FALSE) {
               return;
            }

            Token[] var29 = ParserUtilities.groupByMessageTerm(this.parser, var13[1]).getTokens();
            this.backup();
            var5 = this.factory.CreateFrame();
            this.add(var5, var13[0]);
            if (var29.length == 1) {
               var6 = SleepUtils.getScalar("assertion failed");
               var5 = this.factory.SValue(var6);
               this.add(var5, var13[0]);
            } else {
               this.parseIdea(var29[1]);
            }

            var5 = this.factory.Call("&exit");
            this.add(var5, var13[0]);
            var3 = this.restore();
            var5 = this.factory.Decide(this.parsePredicate(var29[0]), (Block)null, var3);
            this.add(var5, var13[1]);
            break;
         case 601:
            this.parseIdea(ParserUtilities.extract(var13[0]));
            break;
         case 603:
            var5 = this.factory.CreateFrame();
            this.add(var5, var13[2]);
            this.parseIdea(var13[2]);
            this.parseIdea(var13[0]);
            var5 = this.factory.Operate(var12[1]);
            this.add(var5, var13[1]);
            break;
         case 604:
            TokenList var39 = LexicalAnalyzer.CreateTerms(this.parser, new StringIterator(var12[0], var13[0].getHint()));
            var12 = var39.getStrings();
            var13 = var39.getTokens();
            if (var12[0].charAt(0) != '&') {
               var12[0] = '&' + var12[0];
            }

            if ((var12[0].equals("&iff") || var12[0].equals("&?")) && var13.length > 1) {
               var24 = ParserUtilities.groupByParameterTerm(this.parser, ParserUtilities.extract(var13[1]));
               var25 = var24.getTokens();
               this.backup();
               if (var25.length >= 2) {
                  this.parseIdea(var25[1]);
               } else {
                  this.parseIdea(var25[0].copy("true"));
               }

               var2 = this.restore();
               this.backup();
               if (var25.length == 3) {
                  this.parseIdea(var25[2]);
               } else {
                  this.parseIdea(var25[0].copy("false"));
               }

               var3 = this.restore();
               var5 = this.factory.Decide(this.parsePredicate(var25[0]), var2, var3);
               this.add(var5, var13[0]);
            } else if (var13.length > 1) {
               var5 = this.factory.CreateFrame();
               this.add(var5, var13[0]);
               if (var12[0].equals("&warn")) {
                  var5 = this.factory.SValue(SleepUtils.getScalar(var13[0].getHint()));
                  this.add(var5, var13[0]);
               }

               this.parseParameters(ParserUtilities.extract(var13[1]));
               var5 = this.factory.Call(var12[0]);
               this.add(var5, var13[0]);
            } else {
               var5 = this.factory.CreateFrame();
               this.add(var5, var13[0]);
               var5 = this.factory.SValue(SleepUtils.getScalar(var12[0]));
               this.add(var5, var13[0]);
               var5 = this.factory.Call("function");
               this.add(var5, var13[0]);
            }
            break;
         case 605:
            var5 = this.factory.CreateFrame();
            this.add(var5, var13[0]);
            boolean var36 = false;
            StringBuffer var20 = new StringBuffer();
            LinkedList var9 = new LinkedList();
            StringIterator var21 = new StringIterator(ParserUtilities.extract(var12[0]), var13[0].getHint());

            while(var21.hasNext()) {
               char var22 = var21.next();
               int var23;
               if (var22 == '\\' && var21.hasNext()) {
                  var22 = var21.next();
                  var10 = var22 + "";
                  if (escape_constants.containsKey(var10)) {
                     var20.append(escape_constants.get(var10));
                  } else if (var22 == 'u') {
                     if (!var21.hasNext(4)) {
                        this.parser.reportErrorWithMarker("not enough remaining characters for \\uXXXX", var21.getErrorToken());
                     } else {
                        var10 = var21.next(4);

                        try {
                           var23 = Integer.parseInt(var10, 16);
                           var20.append((char)var23);
                        } catch (NumberFormatException var31) {
                           this.parser.reportErrorWithMarker("invalid unicode escape \\u" + var10 + " - must be hex digits", var21.getErrorToken());
                        }
                     }
                  } else if (var22 == 'x') {
                     if (!var21.hasNext(2)) {
                        this.parser.reportErrorWithMarker("not enough remaining characters for \\xXX", var21.getErrorToken());
                     } else {
                        var10 = var21.next(2);

                        try {
                           var23 = Integer.parseInt(var10, 16);
                           var20.append((char)var23);
                        } catch (NumberFormatException var30) {
                           this.parser.reportErrorWithMarker("invalid unicode escape \\x" + var10 + " - must be hex digits", var21.getErrorToken());
                        }
                     }
                  } else {
                     var20.append(var22);
                  }
               } else if (var22 == ' ' && var21.isNextString("$+ ")) {
                  var21.skip(3);
               } else if (var22 == '$' && var21.isNextChar('+')) {
                  this.parser.reportErrorWithMarker("operator $+ must be surrounded with whitespace", var21.getErrorToken());
               } else if (var36 && (Checkers.isEndOfVar(var21.peek()) || !var21.hasNext())) {
                  var20.append(var22);
                  String[] var38 = LexicalAnalyzer.CreateTerms(this.parser, new StringIterator(var20.toString(), var21.getLineNumber())).getStrings();
                  String var18;
                  if (var38.length == 3) {
                     var18 = var38[0] + var38[2];
                     String var19 = ParserUtilities.extract(var38[1]);
                     if (var19.length() > 0) {
                        this.parseIdea(new Token(var19, var21.getLineNumber()));
                        var9.add(PLiteral.fragment(2, (Object)null));
                     } else {
                        this.parser.reportErrorWithMarker("Empty alignment specification for " + var18, var21.getErrorToken());
                     }
                  } else {
                     var18 = var20.toString();
                  }

                  this.parseIdea(new Token(var18, var21.getLineNumber()));
                  var9.add(PLiteral.fragment(3, (Object)null));
                  var36 = false;
                  var20 = new StringBuffer();
               } else if (var22 == '$' && !Checkers.isEndOfVar(var21.peek()) && var21.hasNext()) {
                  var9.add(PLiteral.fragment(1, var20.toString()));
                  var20 = new StringBuffer();
                  var20.append('$');
                  var36 = true;
                  if (var21.isNextChar('[')) {
                     var23 = 0;

                     do {
                        var22 = var21.next();
                        if (var22 == '[') {
                           ++var23;
                        }

                        if (var22 == ']') {
                           --var23;
                        }

                        var20.append(var22);
                     } while(var21.hasNext() && var23 > 0);

                     if (var23 != 0) {
                        this.parser.reportError("missing close brace for variable alignment", new Token(var20.toString(), var21.getLineNumber()));
                        var36 = false;
                     } else if (!var21.hasNext() || Checkers.isEndOfVar(var21.peek())) {
                        this.parser.reportErrorWithMarker("can not align an empty variable", var21.getErrorToken());
                        var36 = false;
                     }
                  }
               } else {
                  var20.append(var22);
               }

               if (!var21.hasNext() && var20.length() > 0) {
                  var9.add(PLiteral.fragment(1, var20.toString()));
               }
            }

            var5 = this.factory.PLiteral(var9);
            this.add(var5, var13[0]);
            break;
         case 606:
            StringBuffer var11 = new StringBuffer(ParserUtilities.extract(var12[0]));

            for(int var34 = 0; var34 < var11.length(); ++var34) {
               if (var11.charAt(var34) == '\\' && var34 + 1 < var11.length()) {
                  char var35 = var11.charAt(var34 + 1);
                  if (var35 == '\'' || var35 == '\\') {
                     var11.deleteCharAt(var34);
                  }
               }
            }

            var6 = SleepUtils.getScalar(var11.toString());
            var5 = this.factory.SValue(var6);
            this.add(var5, var13[0]);
            break;
         case 607:
            if (var12[0].endsWith("L")) {
               var6 = SleepUtils.getScalar(Long.decode(var12[0].substring(0, var12[0].length() - 1)));
            } else {
               var6 = SleepUtils.getScalar(Integer.decode(var12[0]));
            }

            var5 = this.factory.SValue(var6);
            this.add(var5, var13[0]);
            break;
         case 608:
            var6 = SleepUtils.getScalar(Double.parseDouble(var12[0]));
            var5 = this.factory.SValue(var6);
            this.add(var5, var13[0]);
            break;
         case 609:
            var6 = SleepUtils.getScalar(Boolean.valueOf(var12[0]));
            var5 = this.factory.SValue(var6);
            this.add(var5, var13[0]);
            break;
         case 611:
            this.parseObject(ParserUtilities.extract(var13[0]));
            break;
         case 612:
            var5 = this.factory.CreateFrame();
            this.add(var5, var13[2]);
            List var14 = this.parseIdea(var13[2]);
            Iterator var15 = var14.iterator();

            while(var15.hasNext()) {
               Statement var33 = (Statement)var15.next();
               if (var33.getType() == 612) {
                  this.parser.reportError("key/value pair specified for '" + var13[0] + "', did you forget a comma?", var13[2]);
               }
            }

            var6 = SleepUtils.getScalar(var12[0]);
            var5 = this.factory.SValue(var6);
            this.add(var5, var13[0]);
            var5 = this.factory.Operate(var12[1]);
            this.add(var5, var13[1]);
            break;
         case 613:
            this.backup();
            this.parseBlock(ParserUtilities.extract(var13[0]));
            var5 = this.factory.CreateClosure(this.restore());
            this.add(var5, var13[0]);
            break;
         case 614:
            Class var16 = this.parser.findImportedClass(var12[0].substring(1));
            if (var16 == null) {
               this.parser.reportError("unable to resolve class: " + var12[0].substring(1), var13[0]);
            } else {
               var6 = SleepUtils.getScalar((Object)this.parser.findImportedClass(var12[0].substring(1)));
               var5 = this.factory.SValue(var6);
               this.add(var5, var13[0]);
            }
            break;
         case 701:
            if (var12[0].equals("$null")) {
               var6 = SleepUtils.getEmptyScalar();
               var5 = this.factory.SValue(var6);
               this.add(var5, var13[0]);
            } else {
               var5 = this.factory.Get(var12[0]);
               this.add(var5, var13[0]);
            }
            break;
         case 705:
            var5 = this.factory.CreateFrame();
            this.add(var5, var13[0]);
            var5 = this.factory.Get(var12[0].substring(1));
            this.add(var5, var13[0]);
            var6 = SleepUtils.getScalar(var12[0].substring(1));
            var5 = this.factory.SValue(var6);
            this.add(var5, var13[0]);
            var5 = this.factory.Operate("=>");
            this.add(var5, var13[0]);
            break;
         case 710:
            this.parseIdea(var13[0]);

            for(int var17 = 1; var17 < var13.length; ++var17) {
               this.backup();
               var5 = this.factory.CreateFrame();
               this.add(var5, var13[0]);
               this.parseIdea(ParserUtilities.extract(var13[var17]));
               var5 = this.factory.Index(var12[0], this.restore());
               this.add(var5, var13[0]);
            }

            return;
         case 901:
            var10 = var12[0].substring(0, var12[0].length() - 2);
            this.parseBlock(new Token(var10 + " = " + var10 + " + 1;", var13[0].getHint()));
            break;
         case 902:
            var10 = var12[0].substring(0, var12[0].length() - 2);
            this.parseBlock(new Token(var10 + " = " + var10 + " - 1;", var13[0].getHint()));
      }

   }

   public void parseParameters(Token var1) {
      TokenList var2 = ParserUtilities.groupByParameterTerm(this.parser, var1);
      Token[] var3 = var2.getTokens();

      for(int var4 = var3.length - 1; var4 >= 0; --var4) {
         this.parseIdea(var3[var4]);
      }

   }

   static {
      installEscapeConstant('t', "\t");
      installEscapeConstant('n', "\n");
      installEscapeConstant('r', "\r");
   }
}
