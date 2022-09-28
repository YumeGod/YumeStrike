package org.apache.xalan.lib.sql;

import java.sql.Connection;
import java.sql.SQLException;

public class PooledConnection {
   private Connection connection = null;
   private boolean inuse = false;

   public PooledConnection(Connection value) {
      if (value != null) {
         this.connection = value;
      }

   }

   public Connection getConnection() {
      return this.connection;
   }

   public void setInUse(boolean value) {
      this.inuse = value;
   }

   public boolean inUse() {
      return this.inuse;
   }

   public void close() {
      try {
         this.connection.close();
      } catch (SQLException var2) {
         System.err.println(var2.getMessage());
      }

   }
}
