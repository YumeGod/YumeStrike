package sleep.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import sleep.engine.Block;
import sleep.engine.GeneratedSteps;
import sleep.error.SyntaxError;
import sleep.error.YourCodeSucksException;

public class Parser {
   private static final boolean DEBUG_ITER = false;
   private static final boolean DEBUG_LEX = false;
   private static final boolean DEBUG_COMMENTS = false;
   private static final boolean DEBUG_TPARSER = false;
   protected String code;
   protected String name;
   protected LinkedList comments;
   protected LinkedList errors;
   protected LinkedList warnings;
   protected TokenList tokens;
   protected LinkedList statements;
   protected Block executeMe;
   public char EndOfTerm;
   protected ImportManager imports;
   protected GeneratedSteps factory;

   public ImportManager getImportManager() {
      return this.imports;
   }

   public void setCodeFactory(GeneratedSteps var1) {
      this.factory = var1;
   }

   public void importPackage(String var1, String var2) {
      this.imports.importPackage(var1, var2);
   }

   public Class findImportedClass(String var1) {
      return this.imports.findImportedClass(var1);
   }

   public void setEndOfTerm(char var1) {
      this.EndOfTerm = var1;
   }

   public Parser(String var1) {
      this("unknown", var1);
   }

   public Parser(String var1, String var2) {
      this(var1, var2, (ImportManager)null);
   }

   public Parser(String var1, String var2, ImportManager var3) {
      this.comments = new LinkedList();
      this.errors = new LinkedList();
      this.warnings = new LinkedList();
      this.tokens = new TokenList();
      this.statements = new LinkedList();
      this.EndOfTerm = ';';
      this.factory = null;
      if (var3 == null) {
         var3 = new ImportManager();
      }

      this.imports = var3;
      this.importPackage("java.lang.*", (String)null);
      this.importPackage("java.util.*", (String)null);
      this.importPackage("sleep.runtime.*", (String)null);
      this.code = var2;
      this.name = var1;
   }

   public void addStatement(Statement var1) {
      this.statements.add(var1);
   }

   public LinkedList getStatements() {
      return this.statements;
   }

   public String getName() {
      return this.name;
   }

   public void parse() throws YourCodeSucksException {
      this.parse(new StringIterator(this.code));
   }

   public void parse(StringIterator var1) throws YourCodeSucksException {
      TokenList var2 = LexicalAnalyzer.GroupBlockTokens(this, var1);
      if (this.hasErrors()) {
         this.errors.addAll(this.warnings);
         throw new YourCodeSucksException(this.errors);
      } else {
         LinkedList var3 = TokenParser.ParseBlocks(this, var2);
         if (this.hasErrors()) {
            this.errors.addAll(this.warnings);
            throw new YourCodeSucksException(this.errors);
         } else {
            CodeGenerator var4 = new CodeGenerator(this, this.factory);
            var4.parseBlock(var3);
            if (this.hasErrors()) {
               this.errors.addAll(this.warnings);
               throw new YourCodeSucksException(this.errors);
            } else {
               this.executeMe = var4.getRunnableBlock();
            }
         }
      }
   }

   public void reportError(String var1, Token var2) {
      this.errors.add(new SyntaxError(var1, var2.toString(), var2.getHint()));
   }

   public void reportErrorWithMarker(String var1, Token var2) {
      this.errors.add(new SyntaxError(var1, var2.toString(), var2.getHint(), var2.getMarker()));
   }

   public void reportError(SyntaxError var1) {
      this.errors.add(var1);
   }

   public Block getRunnableBlock() {
      return this.executeMe;
   }

   public void reportWarning(String var1, Token var2) {
      this.warnings.add(new SyntaxError(var1, var2.toString(), var2.getHint()));
   }

   public boolean hasErrors() {
      return this.errors.size() > 0;
   }

   public boolean hasWarnings() {
      return this.warnings.size() > 0;
   }

   public void addComment(String var1) {
      this.comments.add(var1);
   }

   public static void main(String[] var0) {
      try {
         File var1 = new File(var0[0]);
         BufferedReader var8 = new BufferedReader(new InputStreamReader(new FileInputStream(var1)));
         StringBuffer var9 = new StringBuffer();

         String var10;
         while((var10 = var8.readLine()) != null) {
            var9.append(var10);
            var9.append('\n');
         }

         Parser var5 = new Parser(var9.toString());
         var5.parse();
         System.out.println(var5.getRunnableBlock());
      } catch (YourCodeSucksException var6) {
         LinkedList var2 = var6.getErrors();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            SyntaxError var4 = (SyntaxError)var3.next();
            System.out.println("Error: " + var4.getDescription() + " at line " + var4.getLineNumber());
            System.out.println("       " + var4.getCodeSnippet());
            if (var4.getMarker() != null) {
               System.out.println("       " + var4.getMarker());
            }
         }
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
