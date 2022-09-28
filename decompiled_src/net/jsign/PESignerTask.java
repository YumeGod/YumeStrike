package net.jsign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import net.jsign.pe.PEFile;
import net.jsign.timestamp.TimestampingMode;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.FileUtils;

public class PESignerTask extends Task {
   private File file;
   private String name;
   private String url;
   private String algorithm;
   private File keystore;
   private String storepass;
   private String storetype = "JKS";
   private String alias;
   private File certfile;
   private File keyfile;
   private String keypass;
   private String tsaurl;
   private String tsmode;

   public void setFile(File file) {
      this.file = file;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setAlg(String alg) {
      this.algorithm = alg;
   }

   public void setTsmode(String tsmode) {
      this.tsmode = tsmode;
   }

   public void setKeystore(File keystore) {
      this.keystore = keystore;
      String name = keystore.getName().toLowerCase();
      if (name.endsWith(".p12") || name.endsWith(".pfx")) {
         this.storetype = "PKCS12";
      }

   }

   public void setStorepass(String storepass) {
      this.storepass = storepass;
   }

   public void setStoretype(String storetype) {
      this.storetype = storetype;
   }

   public void setAlias(String alias) {
      this.alias = alias;
   }

   public void setCertfile(File certfile) {
      this.certfile = certfile;
   }

   public void setKeyfile(File keyfile) {
      this.keyfile = keyfile;
   }

   public void setKeypass(String keypass) {
      this.keypass = keypass;
   }

   public void setTsaurl(String tsaurl) {
      this.tsaurl = tsaurl;
   }

   public void execute() throws BuildException {
      if (this.keystore == null && this.keyfile == null && this.certfile == null) {
         throw new BuildException("keystore attribute, or keyfile and certfile attributes must be set");
      } else if (this.keystore == null || this.keyfile == null && this.certfile == null) {
         Certificate[] chain;
         PrivateKey privateKey;
         if (this.keystore != null) {
            KeyStore ks;
            try {
               ks = KeyStore.getInstance(this.storetype);
            } catch (KeyStoreException var48) {
               throw new BuildException("keystore type '" + this.storetype + "' is not supported", var48);
            }

            if (!this.keystore.exists()) {
               throw new BuildException("The keystore " + this.keystore + " couldn't be found");
            }

            FileInputStream in = null;

            try {
               in = new FileInputStream(this.keystore);
               ks.load(in, this.storepass != null ? this.storepass.toCharArray() : null);
            } catch (Exception var46) {
               throw new BuildException("Unable to load the keystore " + this.keystore, var46);
            } finally {
               try {
                  if (in != null) {
                     in.close();
                  }
               } catch (IOException var37) {
               }

            }

            if (this.alias == null) {
               throw new BuildException("alias attribute must be set");
            }

            try {
               chain = ks.getCertificateChain(this.alias);
            } catch (KeyStoreException var45) {
               throw new BuildException(var45);
            }

            if (chain == null) {
               throw new BuildException("No certificate found under the alias '" + this.alias + "' in the keystore " + this.keystore);
            }

            char[] password = this.keypass != null ? this.keypass.toCharArray() : this.storepass.toCharArray();

            try {
               privateKey = (PrivateKey)ks.getKey(this.alias, password);
            } catch (Exception var44) {
               throw new BuildException("Failed to retrieve the private key from the keystore", var44);
            }
         } else {
            if (this.keyfile == null) {
               throw new BuildException("keyfile attribute must be set");
            }

            if (!this.keyfile.exists()) {
               throw new BuildException("The keyfile " + this.keyfile + " couldn't be found");
            }

            if (this.certfile == null) {
               throw new BuildException("certfile attribute must be set");
            }

            if (!this.certfile.exists()) {
               throw new BuildException("The certfile " + this.certfile + " couldn't be found");
            }

            try {
               chain = this.loadCertificateChain(this.certfile);
            } catch (Exception var43) {
               throw new BuildException("Failed to load the certificate from " + this.certfile, var43);
            }

            try {
               privateKey = PVK.parse(this.keyfile, this.keypass);
            } catch (Exception var42) {
               throw new BuildException("Failed to load the private key from " + this.keyfile, var42);
            }
         }

         if (this.algorithm != null && DigestAlgorithm.of(this.algorithm) == null) {
            throw new BuildException("The digest algorithm " + this.algorithm + " is not supported");
         } else if (this.file == null) {
            throw new BuildException("file attribute must be set");
         } else if (!this.file.exists()) {
            throw new BuildException("The file " + this.file + " couldn't be found");
         } else {
            PEFile peFile;
            try {
               peFile = new PEFile(this.file);
            } catch (IOException var41) {
               throw new BuildException("Couldn't open the executable file " + this.file, var41);
            }

            PESigner signer = (new PESigner(chain, privateKey)).withProgramName(this.name).withProgramURL(this.url).withDigestAlgorithm(DigestAlgorithm.of(this.algorithm)).withTimestamping(this.tsaurl != null).withTimestampingMode(this.tsmode != null ? TimestampingMode.of(this.tsmode) : TimestampingMode.AUTHENTICODE).withTimestampingAutority(this.tsaurl);

            try {
               this.log("Adding Authenticode signature to " + FileUtils.getRelativePath(this.getProject().getBaseDir(), this.file));
               signer.sign(peFile);
            } catch (Exception var39) {
               throw new BuildException("Couldn't sign " + this.file, var39);
            } finally {
               try {
                  peFile.close();
               } catch (IOException var38) {
                  this.log("Couldn't close " + this.file, var38, 1);
               }

            }

         }
      } else {
         throw new BuildException("keystore attribute can't be mixed with keyfile or certfile");
      }
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
         if (in != null) {
            in.close();
         }

      }

      return var5;
   }
}
