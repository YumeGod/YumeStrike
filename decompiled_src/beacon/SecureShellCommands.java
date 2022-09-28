package beacon;

public class SecureShellCommands extends BeaconCommands {
   public String getCommandFile() {
      return "resources/ssh_help.txt";
   }

   public String getDetailFile() {
      return "resources/ssh_details.txt";
   }
}
