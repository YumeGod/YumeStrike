package common;

import c2profile.Profile;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;
import java.security.KeyStore;
import net.jsign.DigestAlgorithm;
import net.jsign.PESigner;
import net.jsign.pe.PEFile;
import net.jsign.timestamp.TimestampingMode;

public class CodeSigner implements Serializable {
   protected byte[] keystore;
   protected String password = null;
   protected String alias = null;
   protected String digest_algorithm = null;
   protected String program_name = null;
   protected String program_url = null;
   protected boolean timestamp = false;
   protected String timestamp_url = null;
   protected String timestamp_mode = null;

   public CodeSigner() {
      this.keystore = new byte[0];
   }

   protected String get(Profile var1, String var2) {
      return var1.hasString(var2) && !"".equals(var1.getString(var2)) ? var1.getString(var2) : null;
   }

   public CodeSigner(Profile var1) {
      if (var1.isFile(".code-signer.keystore")) {
         this.keystore = CommonUtils.readFile(var1.getString(".code-signer.keystore"));
         this.password = var1.getString(".code-signer.password");
         this.alias = var1.getString(".code-signer.alias");
         this.digest_algorithm = this.get(var1, ".code-signer.digest_algorithm");
         this.program_name = this.get(var1, ".code-signer.program_name");
         this.program_url = this.get(var1, ".code-signer.program_url");
         this.timestamp_url = this.get(var1, ".code-signer.timestamp_url");
         this.timestamp_mode = this.get(var1, ".code-signer.timestamp_mode");
         this.timestamp = var1.option(".code-signer.timestamp");
      } else {
         this.keystore = new byte[0];
      }
   }

   public boolean available() {
      return this.keystore.length > 0;
   }

   public byte[] sign(byte[] var1) {
      if (!this.available()) {
         return var1;
      } else {
         String var2 = CommonUtils.writeToTemp("signme", "exe", var1);
         this.sign(new File(var2));
         byte[] var3 = CommonUtils.readFile(var2);
         (new File(var2)).delete();
         return var3;
      }
   }

   public void sign(File var1) {
      if (this.available()) {
         try {
            KeyStore var2 = KeyStore.getInstance("JKS");
            var2.load(new ByteArrayInputStream(this.keystore), this.password.toCharArray());
            PESigner var3 = new PESigner(var2, this.alias, this.password);
            var3.withTimestamping(this.timestamp);
            if (this.program_name != null) {
               var3.withProgramName(this.program_name);
            }

            if (this.program_url != null) {
               var3.withProgramURL(this.program_url);
            }

            if (this.timestamp_mode != null) {
               var3.withTimestampingMode(TimestampingMode.valueOf(this.timestamp_mode));
            }

            if (this.timestamp_url != null) {
               var3.withTimestampingAutority(this.timestamp_url);
            }

            if (this.digest_algorithm != null) {
               var3.withDigestAlgorithm(DigestAlgorithm.valueOf(this.digest_algorithm));
            }

            var3.sign(new PEFile(var1));
         } catch (Exception var4) {
            MudgeSanity.logException("Could not sign '" + var1 + "'", var4, false);
         }

      }
   }
}
