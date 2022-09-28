package org.apache.batik.bridge;

public interface UpdateManagerListener {
   void managerStarted(UpdateManagerEvent var1);

   void managerSuspended(UpdateManagerEvent var1);

   void managerResumed(UpdateManagerEvent var1);

   void managerStopped(UpdateManagerEvent var1);

   void updateStarted(UpdateManagerEvent var1);

   void updateCompleted(UpdateManagerEvent var1);

   void updateFailed(UpdateManagerEvent var1);
}
