package net.jsign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.Proxy.Type;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.jsign.commons.cli.CommandLine;
import net.jsign.commons.cli.DefaultParser;
import net.jsign.commons.cli.HelpFormatter;
import net.jsign.commons.cli.OptionBuilder;
import net.jsign.commons.cli.Options;
import net.jsign.commons.cli.ParseException;
import net.jsign.pe.PEFile;
import net.jsign.timestamp.TimestampingMode;

public class PESignerCLI {
   private Options options = new Options();

   public static void main(String... args) {
      try {
         (new PESignerCLI()).execute(args);
      } catch (SignerException var2) {
         System.err.println("pesign: " + var2.getMessage());
         if (var2.getCause() != null) {
            var2.getCause().printStackTrace(System.err);
         }

         System.err.println("Try `pesign --help' for more information.");
         System.exit(1);
      }

   }

   PESignerCLI() {
      Options var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("keystore");
      OptionBuilder.withArgName("FILE");
      OptionBuilder.withDescription("The keystore file");
      OptionBuilder.withType(File.class);
      var10000.addOption(OptionBuilder.create('s'));
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("storepass");
      OptionBuilder.withArgName("PASSWORD");
      OptionBuilder.withDescription("The password to open the keystore");
      var10000.addOption(OptionBuilder.create());
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("storetype");
      OptionBuilder.withArgName("TYPE");
      OptionBuilder.withDescription("The type of the keystore:\n- JKS: Java keystore (.jks files)\n- PKCS12: Standard PKCS#12 keystore (.p12 or .pfx files)\n");
      var10000.addOption(OptionBuilder.create());
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("alias");
      OptionBuilder.withArgName("NAME");
      OptionBuilder.withDescription("The alias of the certificate used for signing in the keystore.");
      var10000.addOption(OptionBuilder.create('a'));
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("keypass");
      OptionBuilder.withArgName("PASSWORD");
      OptionBuilder.withDescription("The password of the private key. When using a keystore, this parameter can be omitted if the keystore shares the same password.");
      var10000.addOption(OptionBuilder.create());
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("keyfile");
      OptionBuilder.withArgName("FILE");
      OptionBuilder.withDescription("The file containing the private key. Only PVK files are supported. ");
      OptionBuilder.withType(File.class);
      var10000.addOption(OptionBuilder.create());
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("certfile");
      OptionBuilder.withArgName("FILE");
      OptionBuilder.withDescription("The file containing the PKCS#7 certificate chain\n(.p7b or .spc files).");
      OptionBuilder.withType(File.class);
      var10000.addOption(OptionBuilder.create('c'));
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("keyfile");
      OptionBuilder.withArgName("FILE");
      OptionBuilder.withDescription("The file containing the private key. Only PVK files are supported. ");
      OptionBuilder.withType(File.class);
      var10000.addOption(OptionBuilder.create());
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("alg");
      OptionBuilder.withArgName("ALGORITHM");
      OptionBuilder.withDescription("The digest algorithm (SHA-1, SHA-256, SHA-384 or SHA-512)");
      var10000.addOption(OptionBuilder.create('d'));
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("tsaurl");
      OptionBuilder.withArgName("URL");
      OptionBuilder.withDescription("The URL of the timestamping authority.");
      var10000.addOption(OptionBuilder.create('t'));
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("tsmode");
      OptionBuilder.withArgName("MODE");
      OptionBuilder.withDescription("The timestamping mode (RFC3161 or Authenticode)");
      var10000.addOption(OptionBuilder.create('m'));
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("name");
      OptionBuilder.withArgName("NAME");
      OptionBuilder.withDescription("The name of the application");
      var10000.addOption(OptionBuilder.create('n'));
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("url");
      OptionBuilder.withArgName("URL");
      OptionBuilder.withDescription("The URL of the application");
      var10000.addOption(OptionBuilder.create('u'));
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("proxyUrl");
      OptionBuilder.withArgName("URL");
      OptionBuilder.withDescription("The URL of the HTTP proxy");
      var10000.addOption(OptionBuilder.create());
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("proxyUser");
      OptionBuilder.withArgName("NAME");
      OptionBuilder.withDescription("The user for the HTTP proxy. If an user is needed.");
      var10000.addOption(OptionBuilder.create());
      var10000 = this.options;
      OptionBuilder.hasArg();
      OptionBuilder.withLongOpt("proxyPass");
      OptionBuilder.withArgName("PASSWORD");
      OptionBuilder.withDescription("The password for the HTTP proxy user. If an user is needed.");
      var10000.addOption(OptionBuilder.create());
      var10000 = this.options;
      OptionBuilder.withLongOpt("help");
      OptionBuilder.withDescription("Print the help");
      var10000.addOption(OptionBuilder.create('h'));
   }

   void execute(String... args) throws SignerException {
      DefaultParser parser = new DefaultParser();

      try {
         CommandLine cmd = parser.parse(this.options, args);
         if (cmd.hasOption("help") || args.length == 0) {
            this.printHelp();
            return;
         }

         File keystore = cmd.hasOption("keystore") ? new File(cmd.getOptionValue("keystore")) : null;
         String storepass = cmd.getOptionValue("storepass");
         String storetype = cmd.getOptionValue("storetype");
         String alias = cmd.getOptionValue("alias");
         String keypass = cmd.getOptionValue("keypass");
         File keyfile = cmd.hasOption("keyfile") ? new File(cmd.getOptionValue("keyfile")) : null;
         File certfile = cmd.hasOption("certfile") ? new File(cmd.getOptionValue("certfile")) : null;
         String tsaurl = cmd.getOptionValue("tsaurl");
         String tsmode = cmd.getOptionValue("tsmode");
         String algorithm = cmd.getOptionValue("alg");
         String name = cmd.getOptionValue("name");
         String url = cmd.getOptionValue("url");
         String proxyUrl = cmd.getOptionValue("proxyUrl");
         String proxyUser = cmd.getOptionValue("proxyUser");
         String proxyPassword = cmd.getOptionValue("proxyPass");
         File file = cmd.getArgList().isEmpty() ? null : new File((String)cmd.getArgList().get(0));
         if (keystore != null && storetype == null) {
            String filename = keystore.getName().toLowerCase();
            if (!filename.endsWith(".p12") && !filename.endsWith(".pfx")) {
               storetype = "JKS";
            } else {
               storetype = "PKCS12";
            }
         }

         if (keystore == null && keyfile == null && certfile == null) {
            throw new SignerException("keystore option, or keyfile and certfile options must be set");
         }

         if (keystore != null && (keyfile != null || certfile != null)) {
            throw new SignerException("keystore option can't be mixed with keyfile or certfile");
         }

         PrivateKey privateKey;
         Certificate[] chain;
         if (keystore != null) {
            KeyStore ks;
            try {
               ks = KeyStore.getInstance(storetype);
            } catch (KeyStoreException var69) {
               throw new SignerException("keystore type '" + storetype + "' is not supported", var69);
            }

            if (!keystore.exists()) {
               throw new SignerException("The keystore " + keystore + " couldn't be found");
            }

            FileInputStream in = null;

            try {
               in = new FileInputStream(keystore);
               ks.load(in, storepass != null ? storepass.toCharArray() : null);
            } catch (Exception var67) {
               throw new SignerException("Unable to load the keystore " + keystore, var67);
            } finally {
               try {
                  if (in != null) {
                     in.close();
                  }
               } catch (IOException var59) {
               }

            }

            if (alias == null) {
               throw new SignerException("alias option must be set");
            }

            try {
               chain = ks.getCertificateChain(alias);
            } catch (KeyStoreException var66) {
               throw new SignerException(var66.getMessage(), var66);
            }

            if (chain == null) {
               throw new SignerException("No certificate found under the alias '" + alias + "' in the keystore " + keystore);
            }

            char[] password = keypass != null ? keypass.toCharArray() : storepass.toCharArray();

            try {
               privateKey = (PrivateKey)ks.getKey(alias, password);
            } catch (Exception var65) {
               throw new SignerException("Failed to retrieve the private key from the keystore", var65);
            }
         } else {
            if (keyfile == null) {
               throw new SignerException("keyfile option must be set");
            }

            if (!keyfile.exists()) {
               throw new SignerException("The keyfile " + keyfile + " couldn't be found");
            }

            if (certfile == null) {
               throw new SignerException("certfile option must be set");
            }

            if (!certfile.exists()) {
               throw new SignerException("The certfile " + certfile + " couldn't be found");
            }

            try {
               chain = this.loadCertificateChain(certfile);
            } catch (Exception var64) {
               throw new SignerException("Failed to load the certificate from " + certfile, var64);
            }

            try {
               privateKey = PVK.parse(keyfile, keypass);
            } catch (Exception var63) {
               throw new SignerException("Failed to load the private key from " + keyfile, var63);
            }
         }

         if (algorithm != null && DigestAlgorithm.of(algorithm) == null) {
            throw new SignerException("The digest algorithm " + algorithm + " is not supported");
         }

         if (file == null) {
            throw new SignerException("missing file argument");
         }

         if (!file.exists()) {
            throw new SignerException("The file " + file + " couldn't be found");
         }

         PEFile peFile;
         try {
            peFile = new PEFile(file);
         } catch (IOException var62) {
            throw new SignerException("Couldn't open the executable file " + file, var62);
         }

         PESigner signer = (new PESigner(chain, privateKey)).withProgramName(name).withProgramURL(url).withDigestAlgorithm(DigestAlgorithm.of(algorithm)).withTimestamping(tsaurl != null || tsmode != null).withTimestampingMode(tsmode != null ? TimestampingMode.of(tsmode) : TimestampingMode.AUTHENTICODE).withTimestampingAutority(tsaurl);

         try {
            this.initializeProxy(proxyUrl, proxyUser, proxyPassword);
            System.out.println("Adding Authenticode signature to " + file);
            signer.sign(peFile);
         } catch (Exception var60) {
            throw new SignerException("Couldn't sign " + file, var60);
         } finally {
            try {
               peFile.close();
            } catch (IOException var58) {
               System.err.println("Couldn't close " + file);
               var58.printStackTrace(System.err);
            }

         }
      } catch (ParseException var70) {
         var70.printStackTrace();
      }

   }

   private void printHelp() {
      String header = "Sign and timestamp a Windows executable file.\n\n";
      String footer = "\nPlease report suggestions and issues on the GitHub project at https://github.com/ebourg/jsign/issues";
      HelpFormatter formatter = new HelpFormatter();
      formatter.setOptionComparator((Comparator)null);
      formatter.setWidth(85);
      formatter.setDescPadding(1);
      formatter.printHelp("java -jar jsign.jar [OPTIONS] FILE", header, this.options, footer);
   }

   private Certificate[] loadCertificateChain(File file) throws IOException, CertificateException {
      FileInputStream in = null;

      Certificate[] var5;
      try {
         in = new FileInputStream(file);
         CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
         Collection certificates = certificateFactory.generateCertificates(in);
         var5 = (Certificate[])certificates.toArray(new Certificate[certificates.size()]);
      } finally {
         try {
            if (in != null) {
               in.close();
            }
         } catch (IOException var12) {
         }

      }

      return var5;
   }

   private void initializeProxy(String proxyUrl, final String proxyUser, final String proxyPassword) throws MalformedURLException {
      if (proxyUrl != null && proxyUrl.trim().length() > 0) {
         if (!proxyUrl.trim().startsWith("http")) {
            proxyUrl = "http://" + proxyUrl.trim();
         }

         final URL url = new URL(proxyUrl);
         final int port = url.getPort() < 0 ? 80 : url.getPort();
         ProxySelector.setDefault(new ProxySelector() {
            private List proxies;

            {
               this.proxies = Collections.singletonList(new Proxy(Type.HTTP, new InetSocketAddress(url.getHost(), port)));
            }

            public List select(URI uri) {
               return this.proxies;
            }

            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
            }
         });
         if (proxyUser != null && proxyUser.length() > 0 && proxyPassword != null) {
            Authenticator.setDefault(new Authenticator() {
               protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
               }
            });
         }
      }

   }
}
