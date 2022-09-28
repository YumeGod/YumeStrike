package net.jsign.commons.cli;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class DefaultParser implements CommandLineParser {
   protected CommandLine cmd;
   protected Options options;
   protected boolean stopAtNonOption;
   protected String currentToken;
   protected Option currentOption;
   protected boolean skipParsing;
   protected List expectedOpts;

   public CommandLine parse(Options options, String[] arguments) throws ParseException {
      return this.parse(options, arguments, (Properties)null);
   }

   public CommandLine parse(Options options, String[] arguments, Properties properties) throws ParseException {
      return this.parse(options, arguments, properties, false);
   }

   public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException {
      return this.parse(options, arguments, (Properties)null, stopAtNonOption);
   }

   public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption) throws ParseException {
      this.options = options;
      this.stopAtNonOption = stopAtNonOption;
      this.skipParsing = false;
      this.currentOption = null;
      this.expectedOpts = new ArrayList(options.getRequiredOptions());
      Iterator var5 = options.getOptionGroups().iterator();

      while(var5.hasNext()) {
         OptionGroup group = (OptionGroup)var5.next();
         group.setSelected((Option)null);
      }

      this.cmd = new CommandLine();
      if (arguments != null) {
         String[] var9 = arguments;
         int var10 = arguments.length;

         for(int var7 = 0; var7 < var10; ++var7) {
            String argument = var9[var7];
            this.handleToken(argument);
         }
      }

      this.checkRequiredArgs();
      this.handleProperties(properties);
      this.checkRequiredOptions();
      return this.cmd;
   }

   private void handleProperties(Properties properties) throws ParseException {
      if (properties != null) {
         Enumeration e = properties.propertyNames();

         while(true) {
            Option opt;
            String value;
            do {
               String option;
               boolean selected;
               do {
                  do {
                     if (!e.hasMoreElements()) {
                        return;
                     }

                     option = e.nextElement().toString();
                     opt = this.options.getOption(option);
                     if (opt == null) {
                        throw new UnrecognizedOptionException("Default option wasn't defined", option);
                     }

                     OptionGroup group = this.options.getOptionGroup(opt);
                     selected = group != null && group.getSelected() != null;
                  } while(this.cmd.hasOption(option));
               } while(selected);

               value = properties.getProperty(option);
               if (opt.hasArg()) {
                  if (opt.getValues() == null || opt.getValues().length == 0) {
                     opt.addValueForProcessing(value);
                  }
                  break;
               }
            } while(!"yes".equalsIgnoreCase(value) && !"true".equalsIgnoreCase(value) && !"1".equalsIgnoreCase(value));

            this.handleOption(opt);
            this.currentOption = null;
         }
      }
   }

   private void checkRequiredOptions() throws MissingOptionException {
      if (!this.expectedOpts.isEmpty()) {
         throw new MissingOptionException(this.expectedOpts);
      }
   }

   private void checkRequiredArgs() throws ParseException {
      if (this.currentOption != null && this.currentOption.requiresArg()) {
         throw new MissingArgumentException(this.currentOption);
      }
   }

   private void handleToken(String token) throws ParseException {
      this.currentToken = token;
      if (this.skipParsing) {
         this.cmd.addArg(token);
      } else if ("--".equals(token)) {
         this.skipParsing = true;
      } else if (this.currentOption != null && this.currentOption.acceptsArg() && this.isArgument(token)) {
         this.currentOption.addValueForProcessing(Util.stripLeadingAndTrailingQuotes(token));
      } else if (token.startsWith("--")) {
         this.handleLongOption(token);
      } else if (token.startsWith("-") && !"-".equals(token)) {
         this.handleShortAndLongOption(token);
      } else {
         this.handleUnknownToken(token);
      }

      if (this.currentOption != null && !this.currentOption.acceptsArg()) {
         this.currentOption = null;
      }

   }

   private boolean isArgument(String token) {
      return !this.isOption(token) || this.isNegativeNumber(token);
   }

   private boolean isNegativeNumber(String token) {
      try {
         Double.parseDouble(token);
         return true;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   private boolean isOption(String token) {
      return this.isLongOption(token) || this.isShortOption(token);
   }

   private boolean isShortOption(String token) {
      return token.startsWith("-") && token.length() >= 2 && this.options.hasShortOption(token.substring(1, 2));
   }

   private boolean isLongOption(String token) {
      if (token.startsWith("-") && token.length() != 1) {
         int pos = token.indexOf("=");
         String t = pos == -1 ? token : token.substring(0, pos);
         if (!this.options.getMatchingOptions(t).isEmpty()) {
            return true;
         } else {
            return this.getLongPrefix(token) != null && !token.startsWith("--");
         }
      } else {
         return false;
      }
   }

   private void handleUnknownToken(String token) throws ParseException {
      if (token.startsWith("-") && token.length() > 1 && !this.stopAtNonOption) {
         throw new UnrecognizedOptionException("Unrecognized option: " + token, token);
      } else {
         this.cmd.addArg(token);
         if (this.stopAtNonOption) {
            this.skipParsing = true;
         }

      }
   }

   private void handleLongOption(String token) throws ParseException {
      if (token.indexOf(61) == -1) {
         this.handleLongOptionWithoutEqual(token);
      } else {
         this.handleLongOptionWithEqual(token);
      }

   }

   private void handleLongOptionWithoutEqual(String token) throws ParseException {
      List matchingOpts = this.options.getMatchingOptions(token);
      if (matchingOpts.isEmpty()) {
         this.handleUnknownToken(this.currentToken);
      } else {
         if (matchingOpts.size() > 1) {
            throw new AmbiguousOptionException(token, matchingOpts);
         }

         this.handleOption(this.options.getOption((String)matchingOpts.get(0)));
      }

   }

   private void handleLongOptionWithEqual(String token) throws ParseException {
      int pos = token.indexOf(61);
      String value = token.substring(pos + 1);
      String opt = token.substring(0, pos);
      List matchingOpts = this.options.getMatchingOptions(opt);
      if (matchingOpts.isEmpty()) {
         this.handleUnknownToken(this.currentToken);
      } else {
         if (matchingOpts.size() > 1) {
            throw new AmbiguousOptionException(opt, matchingOpts);
         }

         Option option = this.options.getOption((String)matchingOpts.get(0));
         if (option.acceptsArg()) {
            this.handleOption(option);
            this.currentOption.addValueForProcessing(value);
            this.currentOption = null;
         } else {
            this.handleUnknownToken(this.currentToken);
         }
      }

   }

   private void handleShortAndLongOption(String token) throws ParseException {
      String t = Util.stripLeadingHyphens(token);
      int pos = t.indexOf(61);
      if (t.length() == 1) {
         if (this.options.hasShortOption(t)) {
            this.handleOption(this.options.getOption(t));
         } else {
            this.handleUnknownToken(token);
         }
      } else {
         String opt;
         if (pos == -1) {
            if (this.options.hasShortOption(t)) {
               this.handleOption(this.options.getOption(t));
            } else if (!this.options.getMatchingOptions(t).isEmpty()) {
               this.handleLongOptionWithoutEqual(token);
            } else {
               opt = this.getLongPrefix(t);
               if (opt != null && this.options.getOption(opt).acceptsArg()) {
                  this.handleOption(this.options.getOption(opt));
                  this.currentOption.addValueForProcessing(t.substring(opt.length()));
                  this.currentOption = null;
               } else if (this.isJavaProperty(t)) {
                  this.handleOption(this.options.getOption(t.substring(0, 1)));
                  this.currentOption.addValueForProcessing(t.substring(1));
                  this.currentOption = null;
               } else {
                  this.handleConcatenatedOptions(token);
               }
            }
         } else {
            opt = t.substring(0, pos);
            String value = t.substring(pos + 1);
            if (opt.length() == 1) {
               Option option = this.options.getOption(opt);
               if (option != null && option.acceptsArg()) {
                  this.handleOption(option);
                  this.currentOption.addValueForProcessing(value);
                  this.currentOption = null;
               } else {
                  this.handleUnknownToken(token);
               }
            } else if (this.isJavaProperty(opt)) {
               this.handleOption(this.options.getOption(opt.substring(0, 1)));
               this.currentOption.addValueForProcessing(opt.substring(1));
               this.currentOption.addValueForProcessing(value);
               this.currentOption = null;
            } else {
               this.handleLongOptionWithEqual(token);
            }
         }
      }

   }

   private String getLongPrefix(String token) {
      String t = Util.stripLeadingHyphens(token);
      String opt = null;

      for(int i = t.length() - 2; i > 1; --i) {
         String prefix = t.substring(0, i);
         if (this.options.hasLongOption(prefix)) {
            opt = prefix;
            break;
         }
      }

      return opt;
   }

   private boolean isJavaProperty(String token) {
      String opt = token.substring(0, 1);
      Option option = this.options.getOption(opt);
      return option != null && (option.getArgs() >= 2 || option.getArgs() == -2);
   }

   private void handleOption(Option option) throws ParseException {
      this.checkRequiredArgs();
      option = (Option)option.clone();
      this.updateRequiredOptions(option);
      this.cmd.addOption(option);
      if (option.hasArg()) {
         this.currentOption = option;
      } else {
         this.currentOption = null;
      }

   }

   private void updateRequiredOptions(Option option) throws AlreadySelectedException {
      if (option.isRequired()) {
         this.expectedOpts.remove(option.getKey());
      }

      if (this.options.getOptionGroup(option) != null) {
         OptionGroup group = this.options.getOptionGroup(option);
         if (group.isRequired()) {
            this.expectedOpts.remove(group);
         }

         group.setSelected(option);
      }

   }

   protected void handleConcatenatedOptions(String token) throws ParseException {
      for(int i = 1; i < token.length(); ++i) {
         String ch = String.valueOf(token.charAt(i));
         if (!this.options.hasOption(ch)) {
            this.handleUnknownToken(this.stopAtNonOption && i > 1 ? token.substring(i) : token);
            break;
         }

         this.handleOption(this.options.getOption(ch));
         if (this.currentOption != null && token.length() != i + 1) {
            this.currentOption.addValueForProcessing(token.substring(i + 1));
            break;
         }
      }

   }
}
