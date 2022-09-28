package common;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Loggable {
   String getBeaconId();

   void formatEvent(DataOutputStream var1) throws IOException;

   String getLogFile();

   String getLogFolder();
}
