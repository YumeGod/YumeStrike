package common;

import java.awt.Component;
import javax.swing.JOptionPane;

public class License {
   public static void checkLicenseGUI(Authorization var0) {
      if (!var0.isValid()) {
         CommonUtils.print_error("Your authorization file is not valid: " + var0.getError());
         JOptionPane.showMessageDialog((Component)null, "Your authorization file is not valid.\n" + var0.getError(), (String)null, 0);
         System.exit(0);
      }

      if (!var0.isPerpetual()) {
         if (var0.isExpired()) {
            CommonUtils.print_error("Your Cobalt Strike license is expired. Please contact sales@strategiccyber.com to renew. If you did renew, run the update program to refresh your authorization file.");
            JOptionPane.showMessageDialog((Component)null, "Your Cobalt Strike license is expired.\nPlease contact sales@strategiccyber.com to renew\n\nIf you did renew, run the update program to refresh your\nauthorization file.", (String)null, 0);
            System.exit(0);
         }

         if (var0.isAlmostExpired()) {
            CommonUtils.print_warn("Your Cobalt Strike license expires in " + var0.whenExpires() + ". Email sales@strategiccyber.com to renew. If you did renew, run the update program to refresh your authorization file.");
            JOptionPane.showMessageDialog((Component)null, "Your Cobalt Strike license expires in " + var0.whenExpires() + "\nEmail sales@strategiccyber.com to renew\n\nIf you did renew, run the update program to refresh your\nauthorization file.", (String)null, 1);
         }

      }
   }

   public static boolean isTrial() {
      return false;
   }

   public static void checkLicenseConsole(Authorization var0) {
      if (!var0.isValid()) {
         CommonUtils.print_error("Your authorization file is not valid: " + var0.getError());
         System.exit(0);
      }

      if (!var0.isPerpetual()) {
         if (var0.isExpired()) {
            CommonUtils.print_error("Your Cobalt Strike license is expired. Please contact sales@strategiccyber.com to renew. If you did renew, run the update program to refresh your authorization file.");
            System.exit(0);
         }

         if (var0.isAlmostExpired()) {
            CommonUtils.print_warn("Your Cobalt Strike license expires in " + var0.whenExpires() + ". Email sales@strategiccyber.com to renew. If you did renew, run the update program to refresh your authorization file.");
         }

      }
   }
}
