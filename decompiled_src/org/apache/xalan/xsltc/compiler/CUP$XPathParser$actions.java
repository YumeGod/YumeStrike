package org.apache.xalan.xsltc.compiler;

import java.util.Stack;
import java.util.Vector;
import java_cup.runtime.Symbol;
import java_cup.runtime.lr_parser;

class CUP$XPathParser$actions {
   private final XPathParser parser;

   CUP$XPathParser$actions(XPathParser parser) {
      this.parser = parser;
   }

   public final Symbol CUP$XPathParser$do_action(int CUP$XPathParser$act_num, lr_parser CUP$XPathParser$parser, Stack CUP$XPathParser$stack, int CUP$XPathParser$top) throws Exception {
      Symbol CUP$XPathParser$result;
      QName RESULT;
      int qnameleft;
      int qnameright;
      String string;
      Vector argl;
      int ppleft;
      int axis;
      Vector pp;
      QName fname;
      Object ntest;
      int arglleft;
      Expression rlp;
      Vector temp;
      int index;
      String prefix;
      Step right;
      Object ntest;
      Expression step;
      Integer an;
      Integer RESULT;
      Step RESULT;
      StepPattern pip;
      RelativePathPattern rpp;
      Object RESULT;
      StepPattern pip;
      RelativePathPattern rpp;
      IdKeyPattern ikp;
      Pattern lpp;
      AbsoluteLocationPath RESULT;
      BinOpExpr RESULT;
      RelationalExpr RESULT;
      EqualityExpr RESULT;
      LogicalExpr RESULT;
      ProcessingInstructionPattern RESULT;
      StepPattern RESULT;
      AncestorPattern RESULT;
      ParentPattern RESULT;
      AbsolutePathPattern RESULT;
      switch (CUP$XPathParser$act_num) {
         case 0:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            SyntaxTreeNode start_val = (SyntaxTreeNode)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            CUP$XPathParser$result = new Symbol(0, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, start_val);
            CUP$XPathParser$parser.done_parsing();
            return CUP$XPathParser$result;
         case 1:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            lpp = (Pattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(1, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, lpp);
            return CUP$XPathParser$result;
         case 2:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(1, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 3:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            lpp = (Pattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(28, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, lpp);
            return CUP$XPathParser$result;
         case 4:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            lpp = (Pattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            Pattern p = (Pattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            Pattern RESULT = new AlternativePattern(lpp, p);
            CUP$XPathParser$result = new Symbol(28, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 5:
            RESULT = null;
            RESULT = new AbsolutePathPattern((RelativePathPattern)null);
            CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 6:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new AbsolutePathPattern(rpp);
            CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 7:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            ikp = (IdKeyPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, ikp);
            return CUP$XPathParser$result;
         case 8:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            ikp = (IdKeyPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new ParentPattern(ikp, rpp);
            CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 9:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            ikp = (IdKeyPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new AncestorPattern(ikp, rpp);
            CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 10:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new AncestorPattern(rpp);
            CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 11:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(29, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rpp);
            return CUP$XPathParser$result;
         case 12:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            string = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            IdKeyPattern RESULT = new IdPattern(string);
            this.parser.setHasIdCall(true);
            CUP$XPathParser$result = new Symbol(27, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 13:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).right;
            string = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            prefix = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            IdKeyPattern RESULT = new KeyPattern(string, prefix);
            CUP$XPathParser$result = new Symbol(27, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 5)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 14:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            string = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            RESULT = new ProcessingInstructionPattern(string);
            CUP$XPathParser$result = new Symbol(30, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 15:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            pip = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(31, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, pip);
            return CUP$XPathParser$result;
         case 16:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            pip = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new ParentPattern(pip, rpp);
            CUP$XPathParser$result = new Symbol(31, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 17:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            pip = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rpp = (RelativePathPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new AncestorPattern(pip, rpp);
            CUP$XPathParser$result = new Symbol(31, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 18:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = this.parser.createStepPattern(3, ntest, (Vector)null);
            CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 19:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            argl = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = this.parser.createStepPattern(3, ntest, argl);
            CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 20:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            pip = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, pip);
            return CUP$XPathParser$result;
         case 21:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            pip = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            argl = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = (ProcessingInstructionPattern)pip.setPredicates(argl);
            CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 22:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            an = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = this.parser.createStepPattern(an, ntest, (Vector)null);
            CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 23:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            an = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            ppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            axis = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            pp = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = this.parser.createStepPattern(an, ntest, pp);
            CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 24:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            an = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            pip = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, pip);
            return CUP$XPathParser$result;
         case 25:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            an = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            pip = (StepPattern)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            ppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            axis = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            pp = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = (ProcessingInstructionPattern)pip.setPredicates(pp);
            CUP$XPathParser$result = new Symbol(32, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 26:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(33, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, ntest);
            return CUP$XPathParser$result;
         case 27:
            RESULT = null;
            RESULT = new Integer(-1);
            CUP$XPathParser$result = new Symbol(33, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 28:
            RESULT = null;
            RESULT = new Integer(3);
            CUP$XPathParser$result = new Symbol(33, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 29:
            RESULT = null;
            RESULT = new Integer(8);
            CUP$XPathParser$result = new Symbol(33, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 30:
            RESULT = null;
            RESULT = new Integer(7);
            CUP$XPathParser$result = new Symbol(33, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 31:
            RESULT = null;
            RESULT = null;
            CUP$XPathParser$result = new Symbol(34, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 32:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            fname = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(34, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, fname);
            return CUP$XPathParser$result;
         case 33:
            RESULT = null;
            RESULT = new Integer(2);
            CUP$XPathParser$result = new Symbol(42, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 34:
            RESULT = null;
            RESULT = new Integer(3);
            CUP$XPathParser$result = new Symbol(42, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 35:
            RESULT = null;
            RESULT = new Integer(2);
            CUP$XPathParser$result = new Symbol(42, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 36:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            temp = new Vector();
            temp.addElement(rlp);
            CUP$XPathParser$result = new Symbol(35, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, temp);
            return CUP$XPathParser$result;
         case 37:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            argl = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            argl.insertElementAt(rlp, 0);
            CUP$XPathParser$result = new Symbol(35, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, argl);
            return CUP$XPathParser$result;
         case 38:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            Expression RESULT = new Predicate(rlp);
            CUP$XPathParser$result = new Symbol(5, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 39:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(2, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 40:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(8, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 41:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new LogicalExpr(0, rlp, step);
            CUP$XPathParser$result = new Symbol(8, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 42:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(9, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 43:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new LogicalExpr(1, rlp, step);
            CUP$XPathParser$result = new Symbol(9, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 44:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(10, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 45:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new EqualityExpr(0, rlp, step);
            CUP$XPathParser$result = new Symbol(10, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 46:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new EqualityExpr(1, rlp, step);
            CUP$XPathParser$result = new Symbol(10, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 47:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(11, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 48:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new RelationalExpr(3, rlp, step);
            CUP$XPathParser$result = new Symbol(11, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 49:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new RelationalExpr(2, rlp, step);
            CUP$XPathParser$result = new Symbol(11, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 50:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new RelationalExpr(5, rlp, step);
            CUP$XPathParser$result = new Symbol(11, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 51:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new RelationalExpr(4, rlp, step);
            CUP$XPathParser$result = new Symbol(11, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 52:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(12, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 53:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new BinOpExpr(0, rlp, step);
            CUP$XPathParser$result = new Symbol(12, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 54:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new BinOpExpr(1, rlp, step);
            CUP$XPathParser$result = new Symbol(12, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 55:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(13, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 56:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new BinOpExpr(2, rlp, step);
            CUP$XPathParser$result = new Symbol(13, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 57:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new BinOpExpr(3, rlp, step);
            CUP$XPathParser$result = new Symbol(13, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 58:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new BinOpExpr(4, rlp, step);
            CUP$XPathParser$result = new Symbol(13, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 59:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(14, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 60:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            Expression RESULT = new UnaryOpExpr(rlp);
            CUP$XPathParser$result = new Symbol(14, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 61:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(18, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 62:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            Expression RESULT = new UnionPathExpr(rlp, step);
            CUP$XPathParser$result = new Symbol(18, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 63:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(19, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 64:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(19, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 65:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            Expression RESULT = new FilterParentPath(rlp, step);
            CUP$XPathParser$result = new Symbol(19, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 66:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            int nodeType = -1;
            if (step instanceof Step && this.parser.isElementAxis(((Step)step).getAxis())) {
               nodeType = 1;
            }

            Step step = new Step(5, nodeType, (Vector)null);
            FilterParentPath fpp = new FilterParentPath(rlp, step);
            fpp = new FilterParentPath(fpp, step);
            if (!(rlp instanceof KeyCall)) {
               fpp.setDescendantAxis();
            }

            CUP$XPathParser$result = new Symbol(19, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, fpp);
            return CUP$XPathParser$result;
         case 67:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(4, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 68:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(4, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 69:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(21, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 70:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            if (rlp instanceof Step && ((Step)rlp).isAbbreviatedDot()) {
               RESULT = step;
            } else if (((Step)step).isAbbreviatedDot()) {
               RESULT = rlp;
            } else {
               RESULT = new ParentLocationPath((RelativeLocationPath)rlp, step);
            }

            CUP$XPathParser$result = new Symbol(21, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 71:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(21, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 72:
            RESULT = null;
            RESULT = new AbsoluteLocationPath();
            CUP$XPathParser$result = new Symbol(23, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 73:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new AbsoluteLocationPath(rlp);
            CUP$XPathParser$result = new Symbol(23, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 74:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(23, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 75:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            step = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            right = (Step)step;
            axis = right.getAxis();
            int type = right.getNodeType();
            Vector predicates = right.getPredicates();
            RelativeLocationPath left;
            Step mid;
            ParentLocationPath ppl;
            if (axis == 3 && type != 2) {
               if (predicates == null) {
                  right.setAxis(4);
                  if (rlp instanceof Step && ((Step)rlp).isAbbreviatedDot()) {
                     RESULT = right;
                  } else {
                     left = (RelativeLocationPath)rlp;
                     RESULT = new ParentLocationPath(left, right);
                  }
               } else if (rlp instanceof Step && ((Step)rlp).isAbbreviatedDot()) {
                  Step left = new Step(5, 1, (Vector)null);
                  RESULT = new ParentLocationPath(left, right);
               } else {
                  left = (RelativeLocationPath)rlp;
                  mid = new Step(5, 1, (Vector)null);
                  ppl = new ParentLocationPath(mid, right);
                  RESULT = new ParentLocationPath(left, ppl);
               }
            } else if (axis != 2 && type != 2) {
               left = (RelativeLocationPath)rlp;
               mid = new Step(5, -1, (Vector)null);
               ppl = new ParentLocationPath(mid, right);
               RESULT = new ParentLocationPath(left, ppl);
            } else {
               left = (RelativeLocationPath)rlp;
               mid = new Step(5, 1, (Vector)null);
               ppl = new ParentLocationPath(mid, right);
               RESULT = new ParentLocationPath(left, ppl);
            }

            CUP$XPathParser$result = new Symbol(22, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 76:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            int nodeType = -1;
            if (rlp instanceof Step && this.parser.isElementAxis(((Step)rlp).getAxis())) {
               nodeType = 1;
            }

            Step step = new Step(5, nodeType, (Vector)null);
            RESULT = new AbsoluteLocationPath(this.parser.insertStep(step, (RelativeLocationPath)rlp));
            CUP$XPathParser$result = new Symbol(24, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 77:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            if (ntest instanceof Step) {
               RESULT = (Step)ntest;
            } else {
               RESULT = new Step(3, this.parser.findNodeType(3, ntest), (Vector)null);
            }

            CUP$XPathParser$result = new Symbol(7, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 78:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            argl = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            if (ntest instanceof Step) {
               right = (Step)ntest;
               right.addPredicates(argl);
               RESULT = (Step)ntest;
            } else {
               RESULT = new Step(3, this.parser.findNodeType(3, ntest), argl);
            }

            CUP$XPathParser$result = new Symbol(7, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 79:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            an = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            ppleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            axis = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            pp = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new Step(an, this.parser.findNodeType(an, ntest), pp);
            CUP$XPathParser$result = new Symbol(7, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 80:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            an = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = new Step(an, this.parser.findNodeType(an, ntest), (Vector)null);
            CUP$XPathParser$result = new Symbol(7, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 81:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(7, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 82:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            an = (Integer)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            CUP$XPathParser$result = new Symbol(41, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, an);
            return CUP$XPathParser$result;
         case 83:
            RESULT = null;
            RESULT = new Integer(2);
            CUP$XPathParser$result = new Symbol(41, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 84:
            RESULT = null;
            RESULT = new Integer(0);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 85:
            RESULT = null;
            RESULT = new Integer(1);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 86:
            RESULT = null;
            RESULT = new Integer(2);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 87:
            RESULT = null;
            RESULT = new Integer(3);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 88:
            RESULT = null;
            RESULT = new Integer(4);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 89:
            RESULT = null;
            RESULT = new Integer(5);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 90:
            RESULT = null;
            RESULT = new Integer(6);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 91:
            RESULT = null;
            RESULT = new Integer(7);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 92:
            RESULT = null;
            RESULT = new Integer(9);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 93:
            RESULT = null;
            RESULT = new Integer(10);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 94:
            RESULT = null;
            RESULT = new Integer(11);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 95:
            RESULT = null;
            RESULT = new Integer(12);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 96:
            RESULT = null;
            RESULT = new Integer(13);
            CUP$XPathParser$result = new Symbol(40, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 97:
            RESULT = null;
            RESULT = new Step(13, -1, (Vector)null);
            CUP$XPathParser$result = new Symbol(20, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 98:
            RESULT = null;
            RESULT = new Step(10, -1, (Vector)null);
            CUP$XPathParser$result = new Symbol(20, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 99:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(6, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 100:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            argl = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            Expression RESULT = new FilterExpr(rlp, argl);
            CUP$XPathParser$result = new Symbol(6, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 101:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 102:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 103:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            string = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            String namespace = null;
            index = string.lastIndexOf(58);
            if (index > 0) {
               prefix = string.substring(0, index);
               namespace = this.parser._symbolTable.lookupNamespace(prefix);
            }

            Expression RESULT = namespace == null ? new LiteralExpr(string) : new LiteralExpr(string, namespace);
            CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 104:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            Long num = (Long)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            long value = num;
            if (value >= -2147483648L && value <= 2147483647L) {
               if (num.doubleValue() == 0.0) {
                  RESULT = new RealExpr(num.doubleValue());
               } else if (num.intValue() == 0) {
                  RESULT = new IntExpr(num.intValue());
               } else if (num.doubleValue() == 0.0) {
                  RESULT = new RealExpr(num.doubleValue());
               } else {
                  RESULT = new IntExpr(num.intValue());
               }
            } else {
               RESULT = new RealExpr((double)value);
            }

            CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 105:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            Double num = (Double)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            Expression RESULT = new RealExpr(num);
            CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 106:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(17, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 107:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            fname = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            SyntaxTreeNode node = this.parser.lookupName(fname);
            if (node != null) {
               if (node instanceof Variable) {
                  RESULT = new VariableRef((Variable)node);
               } else if (node instanceof Param) {
                  RESULT = new ParameterRef((Param)node);
               } else {
                  RESULT = new UnresolvedRef(fname);
               }
            }

            if (node == null) {
               RESULT = new UnresolvedRef(fname);
            }

            CUP$XPathParser$result = new Symbol(15, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 108:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            fname = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            if (fname == this.parser.getQNameIgnoreDefaultNs("current")) {
               RESULT = new CurrentCall(fname);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("number")) {
               RESULT = new NumberCall(fname, XPathParser.EmptyArgs);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("string")) {
               RESULT = new StringCall(fname, XPathParser.EmptyArgs);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("concat")) {
               RESULT = new ConcatCall(fname, XPathParser.EmptyArgs);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("true")) {
               RESULT = new BooleanExpr(true);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("false")) {
               RESULT = new BooleanExpr(false);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("name")) {
               RESULT = new NameCall(fname);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("generate-id")) {
               RESULT = new GenerateIdCall(fname, XPathParser.EmptyArgs);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("string-length")) {
               RESULT = new StringLengthCall(fname, XPathParser.EmptyArgs);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("position")) {
               RESULT = new PositionCall(fname);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("last")) {
               RESULT = new LastCall(fname);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("local-name")) {
               RESULT = new LocalNameCall(fname);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("namespace-uri")) {
               RESULT = new NamespaceUriCall(fname);
            } else {
               RESULT = new FunctionCall(fname, XPathParser.EmptyArgs);
            }

            CUP$XPathParser$result = new Symbol(16, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 109:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).right;
            fname = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            argl = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            if (fname == this.parser.getQNameIgnoreDefaultNs("concat")) {
               RESULT = new ConcatCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("number")) {
               RESULT = new NumberCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("document")) {
               this.parser.setMultiDocument(true);
               RESULT = new DocumentCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("string")) {
               RESULT = new StringCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("boolean")) {
               RESULT = new BooleanCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("name")) {
               RESULT = new NameCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("generate-id")) {
               RESULT = new GenerateIdCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("not")) {
               RESULT = new NotCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("format-number")) {
               RESULT = new FormatNumberCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("unparsed-entity-uri")) {
               RESULT = new UnparsedEntityUriCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("key")) {
               RESULT = new KeyCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("id")) {
               RESULT = new KeyCall(fname, argl);
               this.parser.setHasIdCall(true);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("ceiling")) {
               RESULT = new CeilingCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("round")) {
               RESULT = new RoundCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("floor")) {
               RESULT = new FloorCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("contains")) {
               RESULT = new ContainsCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("string-length")) {
               RESULT = new StringLengthCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("starts-with")) {
               RESULT = new StartsWithCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("function-available")) {
               RESULT = new FunctionAvailableCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("element-available")) {
               RESULT = new ElementAvailableCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("local-name")) {
               RESULT = new LocalNameCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("lang")) {
               RESULT = new LangCall(fname, argl);
            } else if (fname == this.parser.getQNameIgnoreDefaultNs("namespace-uri")) {
               RESULT = new NamespaceUriCall(fname, argl);
            } else if (fname == this.parser.getQName("http://xml.apache.org/xalan/xsltc", "xsltc", "cast")) {
               RESULT = new CastCall(fname, argl);
            } else if (!fname.getLocalPart().equals("nodeset") && !fname.getLocalPart().equals("node-set")) {
               RESULT = new FunctionCall(fname, argl);
            } else {
               this.parser.setCallsNodeset(true);
               RESULT = new FunctionCall(fname, argl);
            }

            CUP$XPathParser$result = new Symbol(16, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 110:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            temp = new Vector();
            temp.addElement(rlp);
            CUP$XPathParser$result = new Symbol(36, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, temp);
            return CUP$XPathParser$result;
         case 111:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).value;
            arglleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            index = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            argl = (Vector)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            argl.insertElementAt(rlp, 0);
            CUP$XPathParser$result = new Symbol(36, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 2)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, argl);
            return CUP$XPathParser$result;
         case 112:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            fname = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(38, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, fname);
            return CUP$XPathParser$result;
         case 113:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            fname = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(39, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, fname);
            return CUP$XPathParser$result;
         case 114:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            rlp = (Expression)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(3, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, rlp);
            return CUP$XPathParser$result;
         case 115:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            ntest = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, ntest);
            return CUP$XPathParser$result;
         case 116:
            RESULT = null;
            RESULT = new Integer(-1);
            CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 117:
            RESULT = null;
            RESULT = new Integer(3);
            CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 118:
            RESULT = null;
            RESULT = new Integer(8);
            CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 119:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).right;
            string = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 1)).value;
            QName name = this.parser.getQNameIgnoreDefaultNs("name");
            Expression exp = new EqualityExpr(0, new NameCall(name), new LiteralExpr(string));
            argl = new Vector();
            argl.addElement(new Predicate(exp));
            RESULT = new Step(3, 7, argl);
            CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 3)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 120:
            RESULT = null;
            RESULT = new Integer(7);
            CUP$XPathParser$result = new Symbol(25, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 121:
            RESULT = null;
            RESULT = null;
            CUP$XPathParser$result = new Symbol(26, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 122:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            fname = (QName)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            CUP$XPathParser$result = new Symbol(26, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, fname);
            return CUP$XPathParser$result;
         case 123:
            RESULT = null;
            qnameleft = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left;
            qnameright = ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right;
            string = (String)((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).value;
            RESULT = this.parser.getQNameIgnoreDefaultNs(string);
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 124:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("div");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 125:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("mod");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 126:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("key");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 127:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("child");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 128:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("ancestor-or-self");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 129:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("attribute");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 130:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("child");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 131:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("decendant");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 132:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("decendant-or-self");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 133:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("following");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 134:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("following-sibling");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 135:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("namespace");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 136:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("parent");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 137:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("preceding");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 138:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("preceding-sibling");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 139:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("self");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         case 140:
            RESULT = null;
            RESULT = this.parser.getQNameIgnoreDefaultNs("id");
            CUP$XPathParser$result = new Symbol(37, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).left, ((Symbol)CUP$XPathParser$stack.elementAt(CUP$XPathParser$top - 0)).right, RESULT);
            return CUP$XPathParser$result;
         default:
            throw new Exception("Invalid action number found in internal parse table");
      }
   }
}
