package net.jsign.commons.cli;

public interface CommandLineParser {
   CommandLine parse(Options var1, String[] var2) throws ParseException;

   CommandLine parse(Options var1, String[] var2, boolean var3) throws ParseException;
}
