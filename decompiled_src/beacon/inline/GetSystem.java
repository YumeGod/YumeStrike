package beacon.inline;

import aggressor.AggressorClient;
import beacon.PostExInline;

public class GetSystem extends PostExInline {
   public GetSystem(AggressorClient var1) {
      super(var1);
   }

   public String getFunction() {
      return "GetSystem";
   }
}
