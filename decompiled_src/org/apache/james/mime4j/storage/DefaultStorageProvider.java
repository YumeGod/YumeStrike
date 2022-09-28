package org.apache.james.mime4j.storage;

public class DefaultStorageProvider {
   public static final String DEFAULT_STORAGE_PROVIDER_PROPERTY = "org.apache.james.mime4j.defaultStorageProvider";
   private static volatile StorageProvider instance = null;

   private DefaultStorageProvider() {
   }

   public static StorageProvider getInstance() {
      return instance;
   }

   public static void setInstance(StorageProvider instance) {
      if (instance == null) {
         throw new IllegalArgumentException();
      } else {
         DefaultStorageProvider.instance = instance;
      }
   }

   private static void initialize() {
      String clazz = System.getProperty("org.apache.james.mime4j.defaultStorageProvider");

      try {
         if (clazz != null) {
            instance = (StorageProvider)Class.forName(clazz).newInstance();
         }
      } catch (Exception var2) {
      }

      if (instance == null) {
         StorageProvider backend = new TempFileStorageProvider();
         instance = new ThresholdStorageProvider(backend, 1024);
      }

   }

   static void reset() {
      instance = null;
      initialize();
   }

   static {
      initialize();
   }
}
