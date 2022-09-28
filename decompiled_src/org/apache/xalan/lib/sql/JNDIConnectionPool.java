package org.apache.xalan.lib.sql;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JNDIConnectionPool implements ConnectionPool {
   protected Object jdbcSource = null;
   private Method getConnectionWithArgs = null;
   private Method getConnection = null;
   protected String jndiPath = null;
   protected String user = null;
   protected String pwd = null;
   // $FF: synthetic field
   static Class class$java$lang$String;

   public JNDIConnectionPool() {
   }

   public JNDIConnectionPool(String jndiDatasourcePath) {
      this.jndiPath = jndiDatasourcePath.trim();
   }

   public void setJndiPath(String jndiPath) {
      this.jndiPath = jndiPath;
   }

   public String getJndiPath() {
      return this.jndiPath;
   }

   public boolean isEnabled() {
      return true;
   }

   public void setDriver(String d) {
      throw new Error("This method is not supported. All connection information is handled by the JDBC datasource provider");
   }

   public void setURL(String url) {
      throw new Error("This method is not supported. All connection information is handled by the JDBC datasource provider");
   }

   public void freeUnused() {
   }

   public boolean hasActiveConnections() {
      return false;
   }

   public void setPassword(String p) {
      if (p != null) {
         p = p.trim();
      }

      if (p != null && p.length() == 0) {
         p = null;
      }

      this.pwd = p;
   }

   public void setUser(String u) {
      if (u != null) {
         u = u.trim();
      }

      if (u != null && u.length() == 0) {
         u = null;
      }

      this.user = u;
   }

   public Connection getConnection() throws SQLException {
      if (this.jdbcSource == null) {
         try {
            this.findDatasource();
         } catch (NamingException var3) {
            throw new SQLException("Could not create jndi context for " + this.jndiPath + " - " + var3.getLocalizedMessage());
         }
      }

      try {
         Object[] arglist;
         if (this.user == null && this.pwd == null) {
            arglist = new Object[0];
            return (Connection)this.getConnection.invoke(this.jdbcSource, arglist);
         } else {
            arglist = new Object[]{this.user, this.pwd};
            return (Connection)this.getConnectionWithArgs.invoke(this.jdbcSource, arglist);
         }
      } catch (Exception var2) {
         throw new SQLException("Could not create jndi connection for " + this.jndiPath + " - " + var2.getLocalizedMessage());
      }
   }

   protected void findDatasource() throws NamingException {
      try {
         InitialContext context = new InitialContext();
         this.jdbcSource = context.lookup(this.jndiPath);
         Class[] withArgs = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};
         this.getConnectionWithArgs = this.jdbcSource.getClass().getDeclaredMethod("getConnection", withArgs);
         Class[] noArgs = new Class[0];
         this.getConnection = this.jdbcSource.getClass().getDeclaredMethod("getConnection", noArgs);
      } catch (NamingException var4) {
         throw var4;
      } catch (NoSuchMethodException var5) {
         throw new NamingException("Unable to resolve JNDI DataSource - " + var5);
      }
   }

   public void releaseConnection(Connection con) throws SQLException {
      con.close();
   }

   public void releaseConnectionOnError(Connection con) throws SQLException {
      con.close();
   }

   public void setPoolEnabled(boolean flag) {
      if (!flag) {
         this.jdbcSource = null;
      }

   }

   public void setProtocol(Properties p) {
   }

   public void setMinConnections(int n) {
   }

   public boolean testConnection() {
      if (this.jdbcSource == null) {
         try {
            this.findDatasource();
         } catch (NamingException var2) {
            return false;
         }
      }

      return true;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
