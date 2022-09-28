package org.apache.xalan.lib.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public interface ConnectionPool {
   boolean isEnabled();

   void setDriver(String var1);

   void setURL(String var1);

   void freeUnused();

   boolean hasActiveConnections();

   void setPassword(String var1);

   void setUser(String var1);

   void setMinConnections(int var1);

   boolean testConnection();

   Connection getConnection() throws SQLException;

   void releaseConnection(Connection var1) throws SQLException;

   void releaseConnectionOnError(Connection var1) throws SQLException;

   void setPoolEnabled(boolean var1);

   void setProtocol(Properties var1);
}
