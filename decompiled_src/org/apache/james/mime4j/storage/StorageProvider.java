package org.apache.james.mime4j.storage;

import java.io.IOException;
import java.io.InputStream;

public interface StorageProvider {
   Storage store(InputStream var1) throws IOException;

   StorageOutputStream createStorageOutputStream() throws IOException;
}
