package common;

import c2profile.Profile;
import cloudstrike.Response;
import cloudstrike.ResponseFilter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WebTransforms implements ResponseFilter {
   protected List order;
   protected Map headers;
   protected Profile c2profile;

   public WebTransforms(Profile var1) {
      this.c2profile = var1;
      if (var1.hasString(".http-config.headers")) {
         this.order = CommonUtils.toList(var1.getString(".http-config.headers"));
      } else {
         this.order = null;
      }

      this.headers = var1.getHeadersAsMap(".http-config");
      if (this.headers.size() == 0) {
         this.headers = null;
      }

   }

   public void filterResponse(Response var1) {
      try {
         if (this.headers != null) {
            Iterator var2 = this.headers.entrySet().iterator();

            while(var2.hasNext()) {
               Map.Entry var3 = (Map.Entry)var2.next();
               if (!var1.header.containsKey(var3.getKey())) {
                  var1.addHeader(var3.getKey().toString(), var3.getValue().toString());
               }
            }
         }

         if (this.order != null) {
            var1.orderHeaders(this.order);
         }
      } catch (Throwable var4) {
         MudgeSanity.logException("filterResponse failed", var4, false);
      }

   }
}
