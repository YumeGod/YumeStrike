package proxy;

public interface HTTPProxyEventListener {
   int EVENT_INFO = 0;
   int EVENT_BAD = 1;
   int EVENT_GOOD = 2;
   int EVENT_STATUS = 3;

   void proxyEvent(int var1, String var2);
}
