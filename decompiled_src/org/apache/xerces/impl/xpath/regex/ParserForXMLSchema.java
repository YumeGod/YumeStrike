package org.apache.xerces.impl.xpath.regex;

import java.util.Hashtable;
import java.util.Locale;

class ParserForXMLSchema extends RegexParser {
   private static Hashtable ranges = null;
   private static Hashtable ranges2 = null;
   private static final String SPACES = "\t\n\r\r  ";
   private static final String NAMECHARS = "-.0:AZ__az··ÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁːˑ̀͠͡ͅΆΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁ҃҆ҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆֹֻֽֿֿׁׂ֑֣֡ׄׄאתװײءغـْ٠٩ٰڷںھۀێېۓە۪ۭۨ۰۹ँःअह़्॑॔क़ॣ०९ঁঃঅঌএঐওনপরললশহ়়াৄেৈো্ৗৗড়ঢ়য়ৣ০ৱਂਂਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹ਼਼ਾੂੇੈੋ੍ਖ਼ੜਫ਼ਫ਼੦ੴઁઃઅઋઍઍએઑઓનપરલળવહ઼ૅેૉો્ૠૠ૦૯ଁଃଅଌଏଐଓନପରଲଳଶହ଼ୃେୈୋ୍ୖୗଡ଼ଢ଼ୟୡ୦୯ஂஃஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹாூெைொ்ௗௗ௧௯ఁఃఅఌఎఐఒనపళవహాౄెైొ్ౕౖౠౡ౦౯ಂಃಅಌಎಐಒನಪಳವಹಾೄೆೈೊ್ೕೖೞೞೠೡ೦೯ംഃഅഌഎഐഒനപഹാൃെൈൊ്ൗൗൠൡ൦൯กฮะฺเ๎๐๙ກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະູົຽເໄໆໆ່ໍ໐໙༘༙༠༩༹༹༵༵༷༷༾ཇཉཀྵ྄ཱ྆ྋྐྕྗྗྙྭྱྷྐྵྐྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼ⃐⃜⃡⃡ΩΩKÅ℮℮ↀↂ々々〇〇〡〯〱〵ぁゔ゙゚ゝゞァヺーヾㄅㄬ一龥가힣";
   private static final String LETTERS = "AZazÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΆΆΈΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆאתװײءغفيٱڷںھۀێېۓەەۥۦअहऽऽक़ॡঅঌএঐওনপরললশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜਫ਼ਫ਼ੲੴઅઋઍઍએઑઓનપરલળવહઽઽૠૠଅଌଏଐଓନପରଲଳଶହଽଽଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೞೞೠೡഅഌഎഐഒനപഹൠൡกฮะะาำเๅກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະະາຳຽຽເໄཀཇཉཀྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼΩΩKÅ℮℮ↀↂ〇〇〡〩ぁゔァヺㄅㄬ一龥가힣";
   private static final String DIGITS = "09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩";

   public ParserForXMLSchema() {
   }

   public ParserForXMLSchema(Locale var1) {
   }

   Token processCaret() throws ParseException {
      this.next();
      return Token.createChar(94);
   }

   Token processDollar() throws ParseException {
      this.next();
      return Token.createChar(36);
   }

   Token processLookahead() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processNegativelookahead() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processLookbehind() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processNegativelookbehind() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processBacksolidus_A() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processBacksolidus_Z() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processBacksolidus_z() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processBacksolidus_b() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processBacksolidus_B() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processBacksolidus_lt() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processBacksolidus_gt() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processStar(Token var1) throws ParseException {
      this.next();
      return Token.createClosure(var1);
   }

   Token processPlus(Token var1) throws ParseException {
      this.next();
      return Token.createConcat(var1, Token.createClosure(var1));
   }

   Token processQuestion(Token var1) throws ParseException {
      this.next();
      Token.UnionToken var2 = Token.createUnion();
      var2.addChild(var1);
      var2.addChild(Token.createEmpty());
      return var2;
   }

   boolean checkQuestion(int var1) {
      return false;
   }

   Token processParen() throws ParseException {
      this.next();
      Token.ParenToken var1 = Token.createParen(this.parseRegex(), 0);
      if (this.read() != 7) {
         throw this.ex("parser.factor.1", super.offset - 1);
      } else {
         this.next();
         return var1;
      }
   }

   Token processParen2() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processCondition() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processModifiers() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processIndependent() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token processBacksolidus_c() throws ParseException {
      this.next();
      return this.getTokenForShorthand(99);
   }

   Token processBacksolidus_C() throws ParseException {
      this.next();
      return this.getTokenForShorthand(67);
   }

   Token processBacksolidus_i() throws ParseException {
      this.next();
      return this.getTokenForShorthand(105);
   }

   Token processBacksolidus_I() throws ParseException {
      this.next();
      return this.getTokenForShorthand(73);
   }

   Token processBacksolidus_g() throws ParseException {
      throw this.ex("parser.process.1", super.offset - 2);
   }

   Token processBacksolidus_X() throws ParseException {
      throw this.ex("parser.process.1", super.offset - 2);
   }

   Token processBackreference() throws ParseException {
      throw this.ex("parser.process.1", super.offset - 4);
   }

   int processCIinCharacterClass(RangeToken var1, int var2) {
      var1.mergeRanges(this.getTokenForShorthand(var2));
      return -1;
   }

   protected RangeToken parseCharacterClass(boolean var1) throws ParseException {
      this.setContext(1);
      this.next();
      boolean var2 = false;
      boolean var3 = false;
      RangeToken var4 = null;
      RangeToken var5;
      if (this.read() == 0 && super.chardata == 94) {
         var2 = true;
         this.next();
         var4 = Token.createRange();
         var4.addRange(0, 1114111);
         var5 = Token.createRange();
      } else {
         var5 = Token.createRange();
      }

      boolean var7 = true;

      while(true) {
         int var6;
         int var8;
         boolean var9;
         int var12;
         label156: {
            if ((var6 = this.read()) != 1) {
               var3 = false;
               if (var6 == 0 && super.chardata == 93 && !var7) {
                  if (var2) {
                     var4.subtractRanges(var5);
                     var5 = var4;
                  }
               } else {
                  var8 = super.chardata;
                  var9 = false;
                  if (var6 == 10) {
                     switch (var8) {
                        case 45:
                           var8 = this.decodeEscaped();
                           var3 = true;
                           break label156;
                        case 67:
                        case 73:
                        case 99:
                        case 105:
                           var8 = this.processCIinCharacterClass(var5, var8);
                           if (var8 < 0) {
                              var9 = true;
                           }
                           break label156;
                        case 68:
                        case 83:
                        case 87:
                        case 100:
                        case 115:
                        case 119:
                           var5.mergeRanges(this.getTokenForShorthand(var8));
                           var9 = true;
                           break label156;
                        case 80:
                        case 112:
                           var12 = super.offset;
                           RangeToken var11 = this.processBacksolidus_pP(var8);
                           if (var11 == null) {
                              throw this.ex("parser.atom.5", var12);
                           }

                           var5.mergeRanges(var11);
                           var9 = true;
                           break label156;
                        default:
                           var8 = this.decodeEscaped();
                           break label156;
                     }
                  }

                  if (var6 != 24 || var7) {
                     break label156;
                  }

                  if (var2) {
                     var4.subtractRanges(var5);
                     var5 = var4;
                  }

                  RangeToken var10 = this.parseCharacterClass(false);
                  var5.subtractRanges(var10);
                  if (this.read() != 0 || super.chardata != 93) {
                     throw this.ex("parser.cc.5", super.offset);
                  }
               }
            }

            if (this.read() == 1) {
               throw this.ex("parser.cc.2", super.offset);
            }

            var5.sortRanges();
            var5.compactRanges();
            this.setContext(0);
            this.next();
            return var5;
         }

         this.next();
         if (!var9) {
            if (var6 == 0) {
               if (var8 == 91) {
                  throw this.ex("parser.cc.6", super.offset - 2);
               }

               if (var8 == 93) {
                  throw this.ex("parser.cc.7", super.offset - 2);
               }

               if (var8 == 45 && super.chardata == 93 && var7) {
                  throw this.ex("parser.cc.8", super.offset - 2);
               }
            }

            if (var8 == 45 && super.chardata == 45 && this.read() != 10 && !var3) {
               throw this.ex("parser.cc.8", super.offset - 2);
            }

            if (this.read() == 0 && super.chardata == 45) {
               this.next();
               if ((var6 = this.read()) == 1) {
                  throw this.ex("parser.cc.2", super.offset);
               }

               if (var6 == 0 && super.chardata == 93) {
                  var5.addRange(var8, var8);
                  var5.addRange(45, 45);
               } else {
                  if (var6 == 24) {
                     throw this.ex("parser.cc.8", super.offset - 1);
                  }

                  var12 = super.chardata;
                  if (var6 == 0) {
                     if (var12 == 91) {
                        throw this.ex("parser.cc.6", super.offset - 1);
                     }

                     if (var12 == 93) {
                        throw this.ex("parser.cc.7", super.offset - 1);
                     }

                     if (var12 == 45) {
                        throw this.ex("parser.cc.8", super.offset - 2);
                     }
                  } else if (var6 == 10) {
                     var12 = this.decodeEscaped();
                  }

                  this.next();
                  if (var8 > var12) {
                     throw this.ex("parser.ope.3", super.offset - 1);
                  }

                  var5.addRange(var8, var12);
               }
            } else {
               var5.addRange(var8, var8);
            }
         }

         var7 = false;
      }
   }

   protected RangeToken parseSetOperations() throws ParseException {
      throw this.ex("parser.process.1", super.offset);
   }

   Token getTokenForShorthand(int var1) {
      switch (var1) {
         case 67:
            return getRange("xml:isNameChar", false);
         case 68:
            return getRange("xml:isDigit", false);
         case 73:
            return getRange("xml:isInitialNameChar", false);
         case 83:
            return getRange("xml:isSpace", false);
         case 87:
            return getRange("xml:isWord", false);
         case 99:
            return getRange("xml:isNameChar", true);
         case 100:
            return getRange("xml:isDigit", true);
         case 105:
            return getRange("xml:isInitialNameChar", true);
         case 115:
            return getRange("xml:isSpace", true);
         case 119:
            return getRange("xml:isWord", true);
         default:
            throw new RuntimeException("Internal Error: shorthands: \\u" + Integer.toString(var1, 16));
      }
   }

   int decodeEscaped() throws ParseException {
      if (this.read() != 10) {
         throw this.ex("parser.next.1", super.offset - 1);
      } else {
         int var1 = super.chardata;
         switch (var1) {
            case 40:
            case 41:
            case 42:
            case 43:
            case 45:
            case 46:
            case 63:
            case 91:
            case 92:
            case 93:
            case 94:
            case 123:
            case 124:
            case 125:
               break;
            case 110:
               var1 = 10;
               break;
            case 114:
               var1 = 13;
               break;
            case 116:
               var1 = 9;
               break;
            default:
               throw this.ex("parser.process.1", super.offset - 2);
         }

         return var1;
      }
   }

   protected static synchronized RangeToken getRange(String var0, boolean var1) {
      RangeToken var2;
      if (ranges == null) {
         ranges = new Hashtable();
         ranges2 = new Hashtable();
         var2 = Token.createRange();
         setupRange(var2, "\t\n\r\r  ");
         ranges.put("xml:isSpace", var2);
         ranges2.put("xml:isSpace", Token.complementRanges(var2));
         var2 = Token.createRange();
         setupRange(var2, "09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩");
         ranges.put("xml:isDigit", var2);
         ranges2.put("xml:isDigit", Token.complementRanges(var2));
         var2 = Token.createRange();
         setupRange(var2, "09٠٩۰۹०९০৯੦੯૦૯୦୯௧௯౦౯೦೯൦൯๐๙໐໙༠༩");
         ranges.put("xml:isDigit", var2);
         ranges2.put("xml:isDigit", Token.complementRanges(var2));
         var2 = Token.createRange();
         setupRange(var2, "AZazÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΆΆΈΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆאתװײءغفيٱڷںھۀێېۓەەۥۦअहऽऽक़ॡঅঌএঐওনপরললশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜਫ਼ਫ਼ੲੴઅઋઍઍએઑઓનપરલળવહઽઽૠૠଅଌଏଐଓନପରଲଳଶହଽଽଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೞೞೠೡഅഌഎഐഒനപഹൠൡกฮะะาำเๅກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະະາຳຽຽເໄཀཇཉཀྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼΩΩKÅ℮℮ↀↂ〇〇〡〩ぁゔァヺㄅㄬ一龥가힣");
         var2.mergeRanges((Token)ranges.get("xml:isDigit"));
         ranges.put("xml:isWord", var2);
         ranges2.put("xml:isWord", Token.complementRanges(var2));
         var2 = Token.createRange();
         setupRange(var2, "-.0:AZ__az··ÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁːˑ̀͠͡ͅΆΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁ҃҆ҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆֹֻֽֿֿׁׂ֑֣֡ׄׄאתװײءغـْ٠٩ٰڷںھۀێېۓە۪ۭۨ۰۹ँःअह़्॑॔क़ॣ०९ঁঃঅঌএঐওনপরললশহ়়াৄেৈো্ৗৗড়ঢ়য়ৣ০ৱਂਂਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹ਼਼ਾੂੇੈੋ੍ਖ਼ੜਫ਼ਫ਼੦ੴઁઃઅઋઍઍએઑઓનપરલળવહ઼ૅેૉો્ૠૠ૦૯ଁଃଅଌଏଐଓନପରଲଳଶହ଼ୃେୈୋ୍ୖୗଡ଼ଢ଼ୟୡ୦୯ஂஃஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹாூெைொ்ௗௗ௧௯ఁఃఅఌఎఐఒనపళవహాౄెైొ్ౕౖౠౡ౦౯ಂಃಅಌಎಐಒನಪಳವಹಾೄೆೈೊ್ೕೖೞೞೠೡ೦೯ംഃഅഌഎഐഒനപഹാൃെൈൊ്ൗൗൠൡ൦൯กฮะฺเ๎๐๙ກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະູົຽເໄໆໆ່ໍ໐໙༘༙༠༩༹༹༵༵༷༷༾ཇཉཀྵ྄ཱ྆ྋྐྕྗྗྙྭྱྷྐྵྐྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼ⃐⃜⃡⃡ΩΩKÅ℮℮ↀↂ々々〇〇〡〯〱〵ぁゔ゙゚ゝゞァヺーヾㄅㄬ一龥가힣");
         ranges.put("xml:isNameChar", var2);
         ranges2.put("xml:isNameChar", Token.complementRanges(var2));
         var2 = Token.createRange();
         setupRange(var2, "AZazÀÖØöøıĴľŁňŊžƀǃǍǰǴǵǺȗɐʨʻˁΆΆΈΊΌΌΎΡΣώϐϖϚϚϜϜϞϞϠϠϢϳЁЌЎяёќўҁҐӄӇӈӋӌӐӫӮӵӸӹԱՖՙՙաֆאתװײءغفيٱڷںھۀێېۓەەۥۦअहऽऽक़ॡঅঌএঐওনপরললশহড়ঢ়য়ৡৰৱਅਊਏਐਓਨਪਰਲਲ਼ਵਸ਼ਸਹਖ਼ੜਫ਼ਫ਼ੲੴઅઋઍઍએઑઓનપરલળવહઽઽૠૠଅଌଏଐଓନପରଲଳଶହଽଽଡ଼ଢ଼ୟୡஅஊஎஐஒகஙசஜஜஞடணதநபமவஷஹఅఌఎఐఒనపళవహౠౡಅಌಎಐಒನಪಳವಹೞೞೠೡഅഌഎഐഒനപഹൠൡกฮะะาำเๅກຂຄຄງຈຊຊຍຍດທນຟມຣລລວວສຫອຮະະາຳຽຽເໄཀཇཉཀྵႠჅაჶᄀᄀᄂᄃᄅᄇᄉᄉᄋᄌᄎᄒᄼᄼᄾᄾᅀᅀᅌᅌᅎᅎᅐᅐᅔᅕᅙᅙᅟᅡᅣᅣᅥᅥᅧᅧᅩᅩᅭᅮᅲᅳᅵᅵᆞᆞᆨᆨᆫᆫᆮᆯᆷᆸᆺᆺᆼᇂᇫᇫᇰᇰᇹᇹḀẛẠỹἀἕἘἝἠὅὈὍὐὗὙὙὛὛὝὝὟώᾀᾴᾶᾼιιῂῄῆῌῐΐῖΊῠῬῲῴῶῼΩΩKÅ℮℮ↀↂ〇〇〡〩ぁゔァヺㄅㄬ一龥가힣");
         var2.addRange(95, 95);
         var2.addRange(58, 58);
         ranges.put("xml:isInitialNameChar", var2);
         ranges2.put("xml:isInitialNameChar", Token.complementRanges(var2));
      }

      var2 = var1 ? (RangeToken)ranges.get(var0) : (RangeToken)ranges2.get(var0);
      return var2;
   }

   static void setupRange(Token var0, String var1) {
      int var2 = var1.length();

      for(int var3 = 0; var3 < var2; var3 += 2) {
         var0.addRange(var1.charAt(var3), var1.charAt(var3 + 1));
      }

   }
}
