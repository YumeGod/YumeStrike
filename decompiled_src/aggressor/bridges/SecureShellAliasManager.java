package aggressor.bridges;

import sleep.interfaces.Loadable;

public class SecureShellAliasManager extends AliasManager {
   public Loadable getBridge() {
      return new SecureShellAliases(this);
   }
}
