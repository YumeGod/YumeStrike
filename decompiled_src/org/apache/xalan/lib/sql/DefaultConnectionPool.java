package org.apache.xalan.lib.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import org.apache.xalan.res.XSLMessages;

public class DefaultConnectionPool implements ConnectionPool {
   private Driver m_Driver = null;
   private static final boolean DEBUG = false;
   private String m_driver = new String("");
   private String m_url = new String("");
   private int m_PoolMinSize = 1;
   private Properties m_ConnectionProtocol = new Properties();
   private Vector m_pool = new Vector();
   private boolean m_IsActive = false;

   public boolean isEnabled() {
      return this.m_IsActive;
   }

   public void setDriver(String d) {
      this.m_driver = d;
   }

   public void setURL(String url) {
      this.m_url = url;
   }

   public void freeUnused() {
      Iterator i = this.m_pool.iterator();

      while(i.hasNext()) {
         PooledConnection pcon = (PooledConnection)i.next();
         if (!pcon.inUse()) {
            pcon.close();
            i.remove();
         }
      }

   }

   public boolean hasActiveConnections() {
      return this.m_pool.size() > 0;
   }

   public void setPassword(String p) {
      this.m_ConnectionProtocol.put("password", p);
   }

   public void setUser(String u) {
      this.m_ConnectionProtocol.put("user", u);
   }

   public void setProtocol(Properties p) {
      Enumeration e = p.keys();

      while(e.hasMoreElements()) {
         String key = (String)e.nextElement();
         this.m_ConnectionProtocol.put(key, p.getProperty(key));
      }

   }

   public void setMinConnections(int n) {
      this.m_PoolMinSize = n;
   }

   public boolean testConnection() {
      try {
         Connection conn = this.getConnection();
         if (conn == null) {
            return false;
         } else {
            this.releaseConnection(conn);
            return true;
         }
      } catch (Exception var2) {
         return false;
      }
   }

   public synchronized Connection getConnection() throws IllegalArgumentException, SQLException {
      PooledConnection pcon = null;
      if (this.m_pool.size() < this.m_PoolMinSize) {
         this.initializePool();
      }

      for(int x = 0; x < this.m_pool.size(); ++x) {
         pcon = (PooledConnection)this.m_pool.elementAt(x);
         if (!pcon.inUse()) {
            pcon.setInUse(true);
            return pcon.getConnection();
         }
      }

      Connection con = this.createConnection();
      pcon = new PooledConnection(con);
      pcon.setInUse(true);
      this.m_pool.addElement(pcon);
      return pcon.getConnection();
   }

   public synchronized void releaseConnection(Connection con) throws SQLException {
      for(int x = 0; x < this.m_pool.size(); ++x) {
         PooledConnection pcon = (PooledConnection)this.m_pool.elementAt(x);
         if (pcon.getConnection() == con) {
            if (!this.isEnabled()) {
               con.close();
               this.m_pool.removeElementAt(x);
            } else {
               pcon.setInUse(false);
            }
            break;
         }
      }

   }

   public synchronized void releaseConnectionOnError(Connection con) throws SQLException {
      for(int x = 0; x < this.m_pool.size(); ++x) {
         PooledConnection pcon = (PooledConnection)this.m_pool.elementAt(x);
         if (pcon.getConnection() == con) {
            con.close();
            this.m_pool.removeElementAt(x);
            break;
         }
      }

   }

   private Connection createConnection() throws SQLException {
      Connection con = null;
      con = this.m_Driver.connect(this.m_url, this.m_ConnectionProtocol);
      return con;
   }

   public synchronized void initializePool() throws IllegalArgumentException, SQLException {
      if (this.m_driver == null) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_NO_DRIVER_NAME_SPECIFIED", (Object[])null));
      } else if (this.m_url == null) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_NO_URL_SPECIFIED", (Object[])null));
      } else if (this.m_PoolMinSize < 1) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_POOLSIZE_LESS_THAN_ONE", (Object[])null));
      } else {
         try {
            this.m_Driver = (Driver)ObjectFactory.newInstance(this.m_driver, ObjectFactory.findClassLoader(), true);
            DriverManager.registerDriver(this.m_Driver);
         } catch (ObjectFactory.ConfigurationError var3) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_INVALID_DRIVER_NAME", (Object[])null));
         } catch (Exception var4) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_INVALID_DRIVER_NAME", (Object[])null));
         }

         if (this.m_IsActive) {
            do {
               Connection con = this.createConnection();
               if (con != null) {
                  PooledConnection pcon = new PooledConnection(con);
                  this.addConnection(pcon);
               }
            } while(this.m_pool.size() < this.m_PoolMinSize);

         }
      }
   }

   private void addConnection(PooledConnection value) {
      this.m_pool.addElement(value);
   }

   protected void finalize() throws Throwable {
      for(int x = 0; x < this.m_pool.size(); ++x) {
         PooledConnection pcon = (PooledConnection)this.m_pool.elementAt(x);
         if (!pcon.inUse()) {
            pcon.close();
         } else {
            try {
               Thread.sleep(30000L);
               pcon.close();
            } catch (InterruptedException var4) {
            }
         }
      }

      super.finalize();
   }

   public void setPoolEnabled(boolean flag) {
      this.m_IsActive = flag;
      if (!flag) {
         this.freeUnused();
      }

   }
}
