package beacon;

import common.CommonUtils;

public class BeaconErrors {
   public static String toString(int var0, int var1, int var2, String var3) {
      switch (var0) {
         case 0:
            return "DEBUG: " + var3;
         case 1:
            return "Failed to get token";
         case 2:
            return "BypassUAC is for Windows 7 and later";
         case 3:
            return "You're already an admin";
         case 4:
            return "could not connect to pipe";
         case 5:
            return "Maximum links reached. Disconnect one";
         case 6:
            return "I'm already in SMB mode";
         case 7:
            return "could not run command (w/ token) because of its length of " + var1 + " bytes!";
         case 8:
            return "could not upload file: " + var1;
         case 9:
            return "could not get file time: " + var1;
         case 10:
            return "could not set file time: " + var1;
         case 11:
            return "Could not create service: " + var1;
         case 12:
            return "Failed to impersonate token: " + var1;
         case 13:
            return "copy failed: " + var1;
         case 14:
            return "move failed: " + var1;
         case 15:
            return "ppid " + var1 + " is in a different desktop session (spawned jobs may fail). Use 'ppid' to reset.";
         case 16:
            return "could not write to process memory: " + var1;
         case 17:
            return "could not adjust permissions in process: " + var1;
         case 18:
            return var1 + " is an x64 process (can't inject x86 content)";
         case 19:
            return var1 + " is an x86 process (can't inject x64 content)";
         case 20:
            return "Could not connect to pipe: " + var1;
         case 21:
            return "Could not bind to " + var1;
         case 22:
            return "Command length (" + var1 + ") too long";
         case 23:
            return "could not create pipe: " + var1;
         case 24:
            return "Could not create token: " + var1;
         case 25:
            return "Failed to impersonate token: " + var1;
         case 26:
            return "Could not start service: " + var1;
         case 27:
            return "Could not set PPID to " + var1;
         case 28:
            return "kerberos ticket purge failed: " + var1;
         case 29:
            return "kerberos ticket use failed: " + var1;
         case 30:
            return "Could not open process token: " + var1 + " (" + var2 + ")";
         case 31:
            return "could not allocate " + var1 + " bytes in process: " + var2;
         case 32:
            return "could not create remote thread in " + var1 + ": " + var2;
         case 33:
            return "could not open process " + var1 + ": " + var2;
         case 34:
            return "Could not set PPID to " + var1 + ": " + var2;
         case 35:
            return "Could not kill " + var1 + ": " + var2;
         case 36:
            return "Could not open process token: " + var1 + " (" + var2 + ")";
         case 37:
            return "Failed to impersonate token from " + var1 + " (" + var2 + ")";
         case 38:
            return "Failed to duplicate primary token for " + var1 + " (" + var2 + ")";
         case 39:
            return "Failed to impersonate logged on user " + var1 + " (" + var2 + ")";
         case 40:
            return "Could not open '" + var3 + "'";
         case 41:
            return "could not spawn " + var3 + " (token): " + var1;
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         default:
            CommonUtils.print_error("Unknown error toString(" + var0 + ", " + var1 + ", " + var2 + ", '" + var3 + "') BEACON_ERROR");
            return "Unknown error: " + var0;
         case 48:
            return "could not spawn " + var3 + ": " + var1;
         case 49:
            return "could not open " + var3 + ": " + var1;
         case 50:
            return "Could not connect to pipe (" + var3 + "): " + var1;
         case 51:
            return "Could not open service control manager on " + var3 + ": " + var1;
         case 52:
            return "could not open " + var3 + ": " + var1;
         case 53:
            return "could not run " + var3;
         case 54:
            return "Could not create service " + var3;
         case 55:
            return "Could not start service " + var3;
         case 56:
            return "Could not query service " + var3;
         case 57:
            return "Could not delete service " + var3;
         case 58:
            return "Privilege '" + var3 + "' does not exist";
         case 59:
            return "Could not open process token";
         case 60:
            return "File '" + var3 + "' is either too large (>4GB) or size check failed";
         case 61:
            return "Could not determine full path of '" + var3 + "'";
         case 62:
            return "Can only LoadLibrary() in same-arch process";
         case 63:
            return "Could not open registry key: " + var1;
         case 64:
            return "x86 Beacon cannot adjust arguments in x64 process";
         case 65:
            return "Could not adjust arguments in process: " + var1;
         case 66:
            return "Real arguments are longer than fake arguments.";
         case 67:
            return "x64 Beacon cannot adjust arguments in x86 process";
         case 68:
            return "Could not connect to target";
         case 69:
            return "could not spawn " + var3 + " (token&creds): " + var1;
         case 70:
            return "Could not connect to target (stager)";
         case 71:
            return "Could not update process attribute: " + var1;
         case 72:
            return "could not create remote thread in " + var1 + ": " + var2;
         case 73:
            return "allocate section and copy data failed: " + var1;
         case 74:
            return "could not spawn " + var3 + " (token) with extended startup information. Reset ppid, disable blockdlls, or rev2self to drop your token.";
         case 75:
            return "current process will not auto-elevate COM object. Try from a program that lives in c:\\windows\\*";
      }
   }
}
