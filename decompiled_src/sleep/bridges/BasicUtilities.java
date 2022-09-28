package sleep.bridges;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Pattern;
import sleep.engine.Block;
import sleep.engine.ObjectUtilities;
import sleep.engine.ProxyInterface;
import sleep.engine.atoms.Iterate;
import sleep.engine.types.OrderedHashContainer;
import sleep.error.YourCodeSucksException;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.interfaces.Operator;
import sleep.interfaces.Predicate;
import sleep.interfaces.Variable;
import sleep.parser.ParserConfig;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarArray;
import sleep.runtime.ScriptInstance;
import sleep.runtime.ScriptLoader;
import sleep.runtime.ScriptVariables;
import sleep.runtime.SleepUtils;
import sleep.runtime.WatchScalar;
import sleep.taint.TaintUtils;

public class BasicUtilities implements Function, Loadable, Predicate {
   public void scriptUnloaded(ScriptInstance var1) {
   }

   public void scriptLoaded(ScriptInstance var1) {
      Hashtable var2 = var1.getScriptEnvironment().getEnvironment();
      array var3 = new array();
      hash var4 = new hash();
      var2.put("&array", var3);
      var2.put("&hash", var4);
      var2.put("&ohash", var4);
      var2.put("&ohasha", var4);
      var2.put("&@", var3);
      var2.put("&%", var4);
      var2.put("&concat", this);
      var2.put("&keys", this);
      var2.put("&size", this);
      var2.put("&push", this);
      var2.put("&pop", this);
      var2.put("&add", this);
      var2.put("&flatten", this);
      var2.put("&clear", this);
      var2.put("&splice", this);
      var2.put("&subarray", this);
      var2.put("&sublist", this);
      var2.put("&copy", new copy());
      var2.put("&setRemovalPolicy", this);
      var2.put("&setMissPolicy", this);
      var2.put("&untaint", TaintUtils.Sanitizer(this));
      var2.put("&taint", TaintUtils.Tainter(this));
      map var5 = new map();
      var2.put("&map", var5);
      var2.put("&filter", var5);
      f_cast var6 = new f_cast();
      var2.put("&cast", var6);
      var2.put("&casti", var6);
      var2.put("&putAll", this);
      var2.put("&addAll", this);
      var2.put("&removeAll", this);
      var2.put("&retainAll", this);
      var2.put("&pushl", this);
      var2.put("&popl", this);
      var2.put("&search", this);
      var2.put("&reduce", this);
      var2.put("&values", this);
      var2.put("&remove", this);
      var2.put("-istrue", this);
      var2.put("-isarray", this);
      var2.put("-ishash", this);
      var2.put("-isfunction", this);
      var2.put("-istainted", this);
      var2.put("isa", this);
      var2.put("in", this);
      var2.put("=~", this);
      var2.put("&setField", this);
      var2.put("&typeOf", this);
      var2.put("&newInstance", this);
      var2.put("&scalar", this);
      var2.put("&exit", this);
      SetScope var7 = new SetScope();
      var2.put("&local", var7);
      var2.put("&this", var7);
      var2.put("&global", var7);
      var2.put("&watch", this);
      var2.put("&debug", this);
      var2.put("&warn", this);
      var2.put("&profile", this);
      var2.put("&getStackTrace", this);
      var2.put("&reverse", new reverse());
      var2.put("&removeAt", new removeAt());
      var2.put("&shift", new shift());
      var2.put("&systemProperties", new systemProperties());
      var2.put("&use", TaintUtils.Sensitive(new f_use()));
      var2.put("&include", TaintUtils.Sensitive((Function)var2.get("&use")));
      var2.put("&checkError", this);
      var2.put("&lambda", new lambda());
      var2.put("&compile_closure", TaintUtils.Sensitive((Function)var2.get("&lambda")));
      var2.put("&let", var2.get("&lambda"));
      function var8 = new function();
      var2.put("&function", TaintUtils.Sensitive(var8));
      var2.put("function", var2.get("&function"));
      var2.put("&setf", var8);
      var2.put("&eval", TaintUtils.Sensitive(new eval()));
      var2.put("&expr", TaintUtils.Sensitive((Function)var2.get("&eval")));
      SyncPrimitives var9 = new SyncPrimitives();
      var2.put("&semaphore", var9);
      var2.put("&acquire", var9);
      var2.put("&release", var9);
      var2.put("&invoke", this);
      var2.put("&inline", this);
      var2.put("=>", new HashKeyValueOp());
   }

   public boolean decide(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("isa")) {
         Class var8 = BridgeUtilities.getClass(var3, (Class)null);
         Object var11 = BridgeUtilities.getObject(var3);
         return var8 != null && var8.isInstance(var11);
      } else {
         Scalar var4;
         if (var1.equals("=~")) {
            var4 = BridgeUtilities.getScalar(var3);
            Scalar var10 = BridgeUtilities.getScalar(var3);
            return var10.sameAs(var4);
         } else if (var1.equals("in")) {
            var4 = BridgeUtilities.getScalar(var3);
            if (var4.getHash() == null) {
               Iterator var9 = SleepUtils.getIterator(var4, var2);
               Scalar var6 = BridgeUtilities.getScalar(var3);

               Scalar var7;
               do {
                  if (!var9.hasNext()) {
                     return false;
                  }

                  var7 = (Scalar)var9.next();
               } while(!var6.sameAs(var7));

               return true;
            } else {
               String var5 = BridgeUtilities.getString(var3, "");
               return var4.getHash().getData().containsKey(var5) && !SleepUtils.isEmptyScalar((Scalar)((Scalar)var4.getHash().getData().get(var5)));
            }
         } else {
            var4 = (Scalar)var3.pop();
            if (var1.equals("-istrue")) {
               return SleepUtils.isTrueScalar(var4);
            } else if (var1.equals("-isfunction")) {
               return SleepUtils.isFunctionScalar(var4);
            } else if (var1.equals("-istainted")) {
               return TaintUtils.isTainted(var4);
            } else if (var1.equals("-isarray")) {
               return var4.getArray() != null;
            } else if (var1.equals("-ishash")) {
               return var4.getHash() != null;
            } else {
               return false;
            }
         }
      }
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var3.isEmpty() && var1.equals("&remove")) {
         Stack var28 = (Stack)((Stack)var2.getScriptEnvironment().getContextMetadata("iterators"));
         if (var28 != null && !var28.isEmpty()) {
            Iterate.IteratorData var59 = (Iterate.IteratorData)var28.peek();
            var59.iterator.remove();
            --var59.count;
            return var59.source;
         } else {
            throw new RuntimeException("&remove: no active foreach loop to remove element from");
         }
      } else {
         Scalar var4;
         Scalar var5;
         Iterator var6;
         Iterator var7;
         Scalar var9;
         Iterator var10;
         Scalar var11;
         SleepClosure var20;
         Stack var25;
         int var30;
         Scalar var43;
         if (var1.equals("&watch")) {
            String var24 = BridgeUtilities.getString(var3, "");
            String[] var29 = var24.split(" ");

            for(var30 = 0; var30 < var29.length; ++var30) {
               Variable var26 = var2.getScriptVariables().getScalarLevel(var29[var30], var2);
               if (var26 == null) {
                  throw new IllegalArgumentException(var29[var30] + " must already exist in a scope prior to watching");
               }

               WatchScalar var35 = new WatchScalar(var29[var30], var2.getScriptEnvironment());
               var35.setValue((Scalar)var26.getScalar(var29[var30]));
               var2.getScriptVariables().setScalarLevel(var29[var30], var35, var26);
            }
         } else {
            if (var1.equals("&scalar")) {
               return ObjectUtilities.BuildScalar(true, BridgeUtilities.getObject(var3));
            }

            if (var1.equals("&untaint") || var1.equals("&taint")) {
               return !var3.isEmpty() ? (Scalar)var3.pop() : SleepUtils.getEmptyScalar();
            }

            if (var1.equals("&newInstance")) {
               var4 = BridgeUtilities.getScalar(var3);
               SleepClosure var27;
               if (var4.getArray() != null) {
                  Class[] var23 = (Class[])((Class[])ObjectUtilities.buildArgument(Class[].class, var4, var2));
                  var27 = (SleepClosure)BridgeUtilities.getObject(var3);
                  return SleepUtils.getScalar(ProxyInterface.BuildInterface((Class[])var23, (Function)var27, var2));
               }

               Class var21 = (Class)var4.objectValue();
               var27 = (SleepClosure)BridgeUtilities.getObject(var3);
               return SleepUtils.getScalar(SleepUtils.newInstance(var21, var27, var2));
            }

            if (var1.equals("&typeOf")) {
               var4 = BridgeUtilities.getScalar(var3);
               if (var4.getArray() != null) {
                  return SleepUtils.getScalar((Object)var4.getArray().getClass());
               }

               if (var4.getHash() != null) {
                  return SleepUtils.getScalar((Object)var4.getHash().getClass());
               }

               return SleepUtils.getScalar((Object)var4.getActualValue().getType());
            }

            if (var1.equals("&inline")) {
               SleepClosure var22 = BridgeUtilities.getFunction(var3, var2);
               var22.getRunnableCode().evaluate(var2.getScriptEnvironment());
               return SleepUtils.getEmptyScalar();
            }

            if (var1.equals("&invoke")) {
               Map var19 = BridgeUtilities.extractNamedParameters(var3);
               var20 = BridgeUtilities.getFunction(var3, var2);
               var25 = new Stack();
               var7 = BridgeUtilities.getIterator(var3, var2);

               while(var7.hasNext()) {
                  var25.add(0, var7.next());
               }

               String var8 = BridgeUtilities.getString(var3, (String)null);
               if (var19.containsKey("parameters")) {
                  var9 = (Scalar)var19.get("parameters");
                  var10 = var9.getHash().keys().scalarIterator();

                  while(var10.hasNext()) {
                     var11 = (Scalar)var10.next();
                     KeyValuePair var12 = new KeyValuePair(var11, var9.getHash().getAt(var11));
                     var25.add(0, SleepUtils.getScalar((Object)var12));
                  }
               }

               if (var19.containsKey("message")) {
                  var8 = var19.get("message").toString();
               }

               Variable var38 = var20.getVariables();
               if (var19.containsKey("$this")) {
                  SleepClosure var42 = (SleepClosure)((Scalar)var19.get("$this")).objectValue();
                  var20.setVariables(var42.getVariables());
               }

               var43 = var20.callClosure(var8, var2, var25);
               var20.setVariables(var38);
               return var43;
            }

            if (var1.equals("&checkError")) {
               var4 = BridgeUtilities.getScalar(var3);
               var4.setValue(var2.getScriptEnvironment().checkError());
               return var4;
            }

            if (var1.equals("&profile")) {
               return SleepUtils.getArrayWrapper(var2.getProfilerStatistics());
            }

            if (var1.equals("&getStackTrace")) {
               return SleepUtils.getArrayWrapper(var2.getStackTrace());
            }

            if (var1.equals("&warn")) {
               var2.fireWarning(BridgeUtilities.getString(var3, "warning requested"), BridgeUtilities.getInt(var3, -1));
               return SleepUtils.getEmptyScalar();
            }

            if (var1.equals("&debug")) {
               if (!var3.isEmpty()) {
                  int var18 = BridgeUtilities.getInt(var3, 0);
                  var2.setDebugFlags(var18);
               }

               return SleepUtils.getScalar(var2.getDebugFlags());
            }

            if (var1.equals("&flatten")) {
               return BridgeUtilities.flattenIterator(BridgeUtilities.getIterator(var3, var2), (Scalar)null);
            }

            if (var1.equals("&pushl") || var1.equals("&popl")) {
               ScriptVariables var17 = var2.getScriptVariables();
               if (var1.equals("&pushl")) {
                  var17.pushLocalLevel();
               } else if (var1.equals("&popl")) {
                  if (!var17.haveMoreLocals()) {
                     throw new RuntimeException("&popl: no more local frames exist");
                  }

                  var17.popLocalLevel();
               }

               if (!var3.isEmpty()) {
                  BridgeUtilities.initLocalScope(var17, var17.getLocalVariables(), var3);
               }

               return SleepUtils.getEmptyScalar();
            }

            if (var1.equals("&concat")) {
               var4 = SleepUtils.getArrayScalar();

               while(true) {
                  while(!var3.isEmpty()) {
                     var5 = (Scalar)var3.pop();
                     if (var5.getArray() != null) {
                        var6 = var5.getArray().scalarIterator();

                        while(var6.hasNext()) {
                           var4.getArray().push(SleepUtils.getScalar((Scalar)var6.next()));
                        }
                     } else {
                        var4.getArray().push(SleepUtils.getScalar(var5));
                     }
                  }

                  return var4;
               }
            }
         }

         var4 = BridgeUtilities.getScalar(var3);
         if (var1.equals("&push") && BridgeUtilities.expectArray(var1, var4)) {
            var5 = null;

            while(!var3.isEmpty()) {
               var5 = (Scalar)var3.pop();
               var4.getArray().push(SleepUtils.getScalar(var5));
            }

            return var5 == null ? SleepUtils.getEmptyScalar() : var5;
         } else {
            Scalar var33;
            Iterator var45;
            ScalarArray var46;
            ScalarArray var60;
            if ((var1.equals("&retainAll") || var1.equals("&removeAll")) && BridgeUtilities.expectArray(var1, var4)) {
               var46 = var4.getArray();
               var60 = BridgeUtilities.getArray(var3);
               HashSet var55 = new HashSet();
               var45 = var60.scalarIterator();

               while(var45.hasNext()) {
                  var33 = (Scalar)var45.next();
                  var55.add(var33.identity());
               }

               var45 = var46.scalarIterator();

               while(var45.hasNext()) {
                  var33 = (Scalar)var45.next();
                  if (!var55.contains(var33.identity())) {
                     if (var1.equals("&retainAll")) {
                        var45.remove();
                     }
                  } else if (var1.equals("&removeAll")) {
                     var45.remove();
                  }
               }

               return SleepUtils.getArrayScalar(var46);
            } else if (var1.equals("&addAll") && BridgeUtilities.expectArray(var1, var4)) {
               var46 = var4.getArray();
               var60 = BridgeUtilities.getArray(var3);
               HashSet var52 = new HashSet();
               Iterator var50 = var46.scalarIterator();

               while(var50.hasNext()) {
                  var9 = (Scalar)var50.next();
                  var52.add(var9.identity());
               }

               var50 = var60.scalarIterator();

               while(var50.hasNext()) {
                  var9 = (Scalar)var50.next();
                  if (!var52.contains(var9.identity())) {
                     var46.push(SleepUtils.getScalar(var9));
                  }
               }

               return SleepUtils.getArrayScalar(var46);
            } else {
               int var54;
               if (var1.equals("&add") && var4.getArray() != null) {
                  var5 = BridgeUtilities.getScalar(var3);
                  var54 = BridgeUtilities.normalize(BridgeUtilities.getInt(var3, 0), var4.getArray().size() + 1);
                  var4.getArray().add(SleepUtils.getScalar(var5), var54);
                  return var4;
               } else {
                  Scalar var49;
                  if (var1.equals("&add") && var4.getHash() != null) {
                     while(!var3.isEmpty()) {
                        KeyValuePair var48 = BridgeUtilities.getKeyValuePair(var3);
                        var49 = var4.getHash().getAt(var48.getKey());
                        var49.setValue(var48.getValue());
                     }

                     return var4;
                  } else if (var1.equals("&splice") && BridgeUtilities.expectArray(var1, var4)) {
                     var46 = BridgeUtilities.getArray(var3);
                     var54 = BridgeUtilities.normalize(BridgeUtilities.getInt(var3, 0), var4.getArray().size());
                     var30 = BridgeUtilities.getInt(var3, var46.size()) + var54;
                     int var47 = var54;
                     var45 = var4.getArray().scalarIterator();

                     for(int var53 = 0; var53 < var54 && var45.hasNext(); ++var53) {
                        var45.next();
                     }

                     for(; var47 < var30; ++var47) {
                        if (var45.hasNext()) {
                           var45.next();
                           var45.remove();
                        }
                     }

                     ListIterator var58 = (ListIterator)var4.getArray().scalarIterator();

                     for(int var51 = 0; var51 < var54 && var58.hasNext(); ++var51) {
                        var58.next();
                     }

                     Iterator var56 = var46.scalarIterator();

                     while(var56.hasNext()) {
                        Scalar var57 = (Scalar)var56.next();
                        var58.add(var57);
                     }

                     return var4;
                  } else if (var1.equals("&pop") && BridgeUtilities.expectArray(var1, var4)) {
                     return var4.getArray().pop();
                  } else if (var1.equals("&size") && var4.getArray() != null) {
                     return SleepUtils.getScalar(var4.getArray().size());
                  } else if (var1.equals("&size") && var4.getHash() != null) {
                     return SleepUtils.getScalar(var4.getHash().keys().size());
                  } else {
                     Iterator var34;
                     if (var1.equals("&clear")) {
                        if (var4.getArray() != null) {
                           var34 = var4.getArray().scalarIterator();

                           while(var34.hasNext()) {
                              var34.next();
                              var34.remove();
                           }
                        } else if (var4.getHash() != null) {
                           var4.setValue(SleepUtils.getHashScalar());
                        } else {
                           var4.setValue(SleepUtils.getEmptyScalar());
                        }
                     } else if (var1.equals("&search") && BridgeUtilities.expectArray(var1, var4)) {
                        var20 = BridgeUtilities.getFunction(var3, var2);
                        var54 = BridgeUtilities.normalize(BridgeUtilities.getInt(var3, 0), var4.getArray().size());
                        var30 = 0;
                        Stack var44 = new Stack();
                        var45 = var4.getArray().scalarIterator();

                        while(var45.hasNext()) {
                           var43 = (Scalar)var45.next();
                           if (var54 > 0) {
                              --var54;
                              ++var30;
                           } else {
                              var44.push(SleepUtils.getScalar(var30));
                              var44.push(var43);
                              var11 = var20.callClosure("eval", var2, var44);
                              if (!SleepUtils.isEmptyScalar(var11)) {
                                 return var11;
                              }

                              var44.clear();
                              ++var30;
                           }
                        }
                     } else {
                        if (var1.equals("&reduce") && SleepUtils.isFunctionScalar(var4)) {
                           var20 = SleepUtils.getFunctionFromScalar(var4, var2);
                           var25 = new Stack();
                           var7 = BridgeUtilities.getIterator(var3, var2);
                           Scalar var40 = var7.hasNext() ? (Scalar)var7.next() : SleepUtils.getEmptyScalar();
                           var9 = var7.hasNext() ? (Scalar)var7.next() : SleepUtils.getEmptyScalar();
                           var10 = null;
                           var25.push(var40);
                           var25.push(var9);
                           var40 = var20.callClosure("eval", var2, var25);
                           var25.clear();

                           while(var7.hasNext()) {
                              var9 = (Scalar)var7.next();
                              var25.push(var9);
                              var25.push(var40);
                              var40 = var20.callClosure("eval", var2, var25);
                              var25.clear();
                           }

                           return var40;
                        }

                        if ((var1.equals("&subarray") || var1.equals("&sublist")) && BridgeUtilities.expectArray(var1, var4)) {
                           return sublist(var4, BridgeUtilities.getInt(var3, 0), BridgeUtilities.getInt(var3, var4.getArray().size()));
                        }

                        if (var1.equals("&remove")) {
                           while(!var3.isEmpty()) {
                              var5 = (Scalar)var3.pop();
                              if (var4.getArray() != null) {
                                 var4.getArray().remove(var5);
                              } else if (var4.getHash() != null) {
                                 var4.getHash().remove(var5);
                              }
                           }

                           return var4;
                        }

                        if (var1.equals("&keys")) {
                           if (var4.getHash() != null) {
                              var5 = SleepUtils.getEmptyScalar();
                              var5.setValue(var4.getHash().keys());
                              return var5;
                           }
                        } else if (!var1.equals("&setRemovalPolicy") && !var1.equals("&setMissPolicy")) {
                           if (var1.equals("&putAll")) {
                              if (var4.getHash() != null) {
                                 var34 = BridgeUtilities.getIterator(var3, var2);
                                 var6 = var3.isEmpty() ? var34 : BridgeUtilities.getIterator(var3, var2);

                                 while(var34.hasNext()) {
                                    var33 = var4.getHash().getAt((Scalar)var34.next());
                                    if (var6.hasNext()) {
                                       var33.setValue((Scalar)var6.next());
                                    } else {
                                       var33.setValue(SleepUtils.getEmptyScalar());
                                    }
                                 }
                              } else if (var4.getArray() != null) {
                                 var34 = BridgeUtilities.getIterator(var3, var2);

                                 while(var34.hasNext()) {
                                    var49 = (Scalar)var34.next();
                                    var4.getArray().push(SleepUtils.getScalar(var49));
                                 }
                              }

                              return var4;
                           }

                           if (var1.equals("&values")) {
                              if (var4.getHash() != null) {
                                 var5 = SleepUtils.getArrayScalar();
                                 if (var3.isEmpty()) {
                                    var6 = var4.getHash().getData().values().iterator();

                                    while(var6.hasNext()) {
                                       var33 = (Scalar)var6.next();
                                       if (!SleepUtils.isEmptyScalar(var33)) {
                                          var5.getArray().push(SleepUtils.getScalar(var33));
                                       }
                                    }
                                 } else {
                                    var6 = BridgeUtilities.getIterator(var3, var2);

                                    while(var6.hasNext()) {
                                       var33 = (Scalar)var6.next();
                                       var5.getArray().push(SleepUtils.getScalar(var4.getHash().getAt(var33)));
                                    }
                                 }

                                 return var5;
                              }
                           } else if (var1.equals("&exit")) {
                              var2.getScriptEnvironment().flagReturn((Scalar)null, 16);
                              if (!SleepUtils.isEmptyScalar(var4)) {
                                 throw new RuntimeException(var4.toString());
                              }
                           } else if (var1.equals("&setField")) {
                              var5 = null;
                              var6 = null;
                              var7 = null;
                              if (var4.objectValue() == null) {
                                 throw new IllegalArgumentException("&setField: can not set field on a null object");
                              }

                              Object var36;
                              Class var37;
                              if (var4.objectValue() instanceof Class) {
                                 var37 = (Class)var4.objectValue();
                                 var36 = null;
                              } else {
                                 var36 = var4.objectValue();
                                 var37 = var36.getClass();
                              }

                              while(!var3.isEmpty()) {
                                 KeyValuePair var39 = BridgeUtilities.getKeyValuePair(var3);
                                 String var41 = var39.getKey().toString();
                                 var43 = var39.getValue();

                                 try {
                                    Field var32;
                                    try {
                                       var32 = var37.getDeclaredField(var41);
                                    } catch (NoSuchFieldException var13) {
                                       var32 = var37.getField(var41);
                                    }

                                    if (ObjectUtilities.isArgMatch(var32.getType(), var43) == 0) {
                                       throw new RuntimeException("unable to convert " + SleepUtils.describe(var43) + " to a " + var32.getType());
                                    }

                                    var32.setAccessible(true);
                                    var32.set(var36, ObjectUtilities.buildArgument(var32.getType(), var43, var2));
                                 } catch (NoSuchFieldException var14) {
                                    throw new RuntimeException("no field named " + var41 + " in " + var37);
                                 } catch (RuntimeException var15) {
                                    throw var15;
                                 } catch (Exception var16) {
                                    throw new RuntimeException("cannot set " + var41 + " in " + var37 + ": " + var16.getMessage());
                                 }
                              }
                           }
                        } else {
                           if (var4.getHash() == null || !(var4.getHash() instanceof OrderedHashContainer)) {
                              throw new IllegalArgumentException(var1 + ": expected an ordered hash, received: " + SleepUtils.describe(var4));
                           }

                           var20 = BridgeUtilities.getFunction(var3, var2);
                           OrderedHashContainer var31 = (OrderedHashContainer)((OrderedHashContainer)var4.getHash());
                           if (var1.equals("&setMissPolicy")) {
                              var31.setMissPolicy(var20);
                           } else {
                              var31.setRemovalPolicy(var20);
                           }
                        }
                     }

                     return SleepUtils.getEmptyScalar();
                  }
               }
            }
         }
      }
   }

   private static Scalar sublist(Scalar var0, int var1, int var2) {
      int var3 = var0.getArray().size();
      int var4 = BridgeUtilities.normalize(var1, var3);
      int var5 = var2 < 0 ? var2 + var3 : var2;
      var5 = var5 <= var3 ? var5 : var3;
      if (var4 > var5) {
         throw new IllegalArgumentException("illegal subarray(" + SleepUtils.describe(var0) + ", " + var1 + " -> " + var4 + ", " + var2 + " -> " + var5 + ")");
      } else {
         return SleepUtils.getArrayScalar(var0.getArray().sublist(var4, var5));
      }
   }

   static {
      ParserConfig.addKeyword("isa");
      ParserConfig.addKeyword("in");
      ParserConfig.addKeyword("=~");
   }

   private static class eval implements Function {
      private eval() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4 = var3.pop().toString();

         try {
            Scalar var5;
            if (var1.equals("&eval")) {
               var5 = SleepUtils.getScalar(var2.getScriptEnvironment().evaluateStatement(var4));
               return var5;
            } else {
               var5 = SleepUtils.getScalar(var2.getScriptEnvironment().evaluateExpression(var4));
               return var5;
            }
         } catch (YourCodeSucksException var6) {
            var2.getScriptEnvironment().flagError(var6);
            return SleepUtils.getEmptyScalar();
         }
      }

      // $FF: synthetic method
      eval(Object var1) {
         this();
      }
   }

   private static class systemProperties implements Function {
      private systemProperties() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         return SleepUtils.getHashWrapper(System.getProperties());
      }

      // $FF: synthetic method
      systemProperties(Object var1) {
         this();
      }
   }

   private static class SetScope implements Function {
      private Pattern splitter;

      private SetScope() {
         this.splitter = Pattern.compile("\\s+");
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         Variable var4 = null;
         if (var1.equals("&local")) {
            var4 = var2.getScriptVariables().getLocalVariables();
         } else if (var1.equals("&this")) {
            var4 = var2.getScriptVariables().getClosureVariables();
         } else if (var1.equals("&global")) {
            var4 = var2.getScriptVariables().getGlobalVariables();
         }

         String var5 = var3.pop().toString();
         if (var4 == null) {
            return SleepUtils.getEmptyScalar();
         } else {
            String[] var6 = this.splitter.split(var5);

            for(int var7 = 0; var7 < var6.length; ++var7) {
               if (!var4.scalarExists(var6[var7])) {
                  if (var6[var7].charAt(0) == '$') {
                     var2.getScriptVariables().setScalarLevel(var6[var7], SleepUtils.getEmptyScalar(), var4);
                  } else if (var6[var7].charAt(0) == '@') {
                     var2.getScriptVariables().setScalarLevel(var6[var7], SleepUtils.getArrayScalar(), var4);
                  } else {
                     if (var6[var7].charAt(0) != '%') {
                        throw new IllegalArgumentException(var1 + ": malformed variable name '" + var6[var7] + "' from '" + var5 + "'");
                     }

                     var2.getScriptVariables().setScalarLevel(var6[var7], SleepUtils.getHashScalar(), var4);
                  }
               }
            }

            return SleepUtils.getEmptyScalar();
         }
      }

      // $FF: synthetic method
      SetScope(Object var1) {
         this();
      }
   }

   private static class reverse implements Function {
      private reverse() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = SleepUtils.getArrayScalar();
         Iterator var5 = BridgeUtilities.getIterator(var3, var2);

         while(var5.hasNext()) {
            var4.getArray().add(SleepUtils.getScalar((Scalar)var5.next()), 0);
         }

         return var4;
      }

      // $FF: synthetic method
      reverse(Object var1) {
         this();
      }
   }

   private static class shift implements Function {
      private shift() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         ScalarArray var4 = BridgeUtilities.getArray(var3);
         return var4.remove(0);
      }

      // $FF: synthetic method
      shift(Object var1) {
         this();
      }
   }

   private static class removeAt implements Function {
      private removeAt() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = (Scalar)var3.pop();
         if (var4.getArray() != null) {
            while(!var3.isEmpty()) {
               var4.getArray().remove(BridgeUtilities.normalize(BridgeUtilities.getInt(var3, 0), var4.getArray().size()));
            }
         } else if (var4.getHash() != null) {
            while(!var3.isEmpty()) {
               Scalar var5 = var4.getHash().getAt((Scalar)var3.pop());
               var5.setValue(SleepUtils.getEmptyScalar());
            }
         }

         return SleepUtils.getEmptyScalar();
      }

      // $FF: synthetic method
      removeAt(Object var1) {
         this();
      }
   }

   private static class copy implements Function {
      private copy() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = BridgeUtilities.getScalar(var3);
         Scalar var5;
         Iterator var6;
         if (var4.getArray() == null && !SleepUtils.isFunctionScalar(var4)) {
            if (var4.getHash() == null) {
               return SleepUtils.getScalar(var4);
            } else {
               var5 = SleepUtils.getHashScalar();
               var6 = var4.getHash().keys().scalarIterator();

               while(var6.hasNext()) {
                  Scalar var7 = (Scalar)var6.next();
                  Scalar var8 = var5.getHash().getAt(var7);
                  var8.setValue(var4.getHash().getAt(var7));
               }

               return var5;
            }
         } else {
            var5 = SleepUtils.getArrayScalar();
            var6 = var4.getArray() == null ? SleepUtils.getFunctionFromScalar(var4, var2).scalarIterator() : var4.getArray().scalarIterator();

            while(var6.hasNext()) {
               var5.getArray().push(SleepUtils.getScalar((Scalar)var6.next()));
            }

            return var5;
         }
      }

      // $FF: synthetic method
      copy(Object var1) {
         this();
      }
   }

   private static class map implements Function {
      private map() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         SleepClosure var4 = BridgeUtilities.getFunction(var3, var2);
         Iterator var5 = BridgeUtilities.getIterator(var3, var2);
         Scalar var6 = SleepUtils.getArrayScalar();

         for(Stack var7 = new Stack(); var5.hasNext(); var7.clear()) {
            var7.push(var5.next());
            Scalar var8 = var4.callClosure("eval", var2, var7);
            if (!SleepUtils.isEmptyScalar(var8) || var1.equals("&map")) {
               var6.getArray().push(SleepUtils.getScalar(var8));
            }
         }

         return var6;
      }

      // $FF: synthetic method
      map(Object var1) {
         this();
      }
   }

   private static class lambda implements Function {
      private lambda() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         SleepClosure var4;
         SleepClosure var5;
         if (var1.equals("&lambda")) {
            var5 = BridgeUtilities.getFunction(var3, var2);
            var4 = new SleepClosure(var2, var5.getRunnableCode());
         } else if (var1.equals("&compile_closure")) {
            String var6 = var3.pop().toString();

            try {
               var5 = new SleepClosure(var2, SleepUtils.ParseCode(var6));
               var4 = var5;
            } catch (YourCodeSucksException var9) {
               var2.getScriptEnvironment().flagError(var9);
               return SleepUtils.getEmptyScalar();
            }
         } else {
            var5 = BridgeUtilities.getFunction(var3, var2);
            var4 = var5;
         }

         Variable var10 = var4.getVariables();

         while(!var3.isEmpty()) {
            KeyValuePair var7 = BridgeUtilities.getKeyValuePair(var3);
            if (var7.getKey().toString().equals("$this")) {
               SleepClosure var8 = (SleepClosure)var7.getValue().objectValue();
               var4.setVariables(var8.getVariables());
               var10 = var8.getVariables();
            } else {
               var10.putScalar(var7.getKey().toString(), SleepUtils.getScalar(var7.getValue()));
            }
         }

         return SleepUtils.getScalar((Object)var4);
      }

      // $FF: synthetic method
      lambda(Object var1) {
         this();
      }
   }

   private static class hash implements Function {
      private hash() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = null;
         if (var1.equals("&ohash")) {
            var4 = SleepUtils.getOrderedHashScalar();
         } else if (var1.equals("&ohasha")) {
            var4 = SleepUtils.getAccessOrderedHashScalar();
         } else {
            var4 = SleepUtils.getHashScalar();
         }

         while(!var3.isEmpty()) {
            KeyValuePair var5 = BridgeUtilities.getKeyValuePair(var3);
            Scalar var6 = var4.getHash().getAt(var5.getKey());
            var6.setValue(var5.getValue());
         }

         return var4;
      }

      // $FF: synthetic method
      hash(Object var1) {
         this();
      }
   }

   private static class function implements Function {
      private function() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         String var4;
         if (!var1.equals("&function") && !var1.equals("function")) {
            if (var1.equals("&setf")) {
               var4 = BridgeUtilities.getString(var3, "&eh");
               Object var5 = BridgeUtilities.getObject(var3);
               if (var4.charAt(0) == '&' && (var5 == null || var5 instanceof Function)) {
                  if (var5 == null) {
                     var2.getScriptEnvironment().getEnvironment().remove(var4);
                  } else {
                     var2.getScriptEnvironment().getEnvironment().put(var4, var5);
                  }
               } else {
                  if (var4.charAt(0) != '&') {
                     throw new IllegalArgumentException("&setf: invalid function name '" + var4 + "'");
                  }

                  if (var5 != null) {
                     throw new IllegalArgumentException("&setf: can not set function " + var4 + " to a " + var5.getClass());
                  }
               }
            }

            return SleepUtils.getEmptyScalar();
         } else {
            var4 = BridgeUtilities.getString(var3, "");
            if (var4.length() != 0 && var4.charAt(0) == '&') {
               return SleepUtils.getScalar((Object)var2.getScriptEnvironment().getFunction(var4));
            } else {
               throw new IllegalArgumentException(var1 + ": requested function name must begin with '&'");
            }
         }
      }

      // $FF: synthetic method
      function(Object var1) {
         this();
      }
   }

   private static class f_cast implements Function {
      private f_cast() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = BridgeUtilities.getScalar(var3);
         Scalar var5 = BridgeUtilities.getScalar(var3);
         if (var1.equals("&casti")) {
            Class var16 = ObjectUtilities.convertScalarDescriptionToClass(var5);
            if (var16 != null) {
               Object var17 = ObjectUtilities.buildArgument(var16, var4, var2);
               return SleepUtils.getScalar(var17);
            } else {
               throw new RuntimeException("&casti: '" + var5 + "' is an invalid primitive cast identifier");
            }
         } else if (var4.getArray() == null) {
            if (var5.toString().charAt(0) == 'c') {
               return SleepUtils.getScalar((Object)var4.toString().toCharArray());
            } else {
               return var5.toString().charAt(0) == 'b' ? SleepUtils.getScalar((Object)BridgeUtilities.toByteArrayNoConversion(var4.toString())) : SleepUtils.getEmptyScalar();
            }
         } else {
            if (var3.size() == 0) {
               var3.push(SleepUtils.getScalar(var4.getArray().size()));
            }

            int[] var6 = new int[var3.size()];
            int var7 = 1;

            for(int var8 = 0; !var3.isEmpty(); ++var8) {
               var6[var8] = BridgeUtilities.getInt(var3, 0);
               var7 *= var6[var8];
            }

            Class var9 = ObjectUtilities.convertScalarDescriptionToClass(var5);
            if (var9 == null) {
               var9 = ObjectUtilities.getArrayType(var4, Object.class);
            }

            Scalar var10 = BridgeUtilities.flattenArray(var4, (Scalar)null);
            if (var7 != var10.getArray().size()) {
               throw new RuntimeException("&cast: specified dimensions " + var7 + " is not equal to total array elements " + var10.getArray().size());
            } else {
               Object var18 = Array.newInstance(var9, var6);
               int[] var11 = new int[var6.length];
               if (var10.getArray().size() == 0) {
                  return SleepUtils.getScalar(var18);
               } else {
                  int var12 = 0;

                  while(true) {
                     Object var13 = var18;

                     for(int var14 = 0; var14 < var11.length - 1; ++var14) {
                        var13 = Array.get(var13, var11[var14]);
                     }

                     Object var19 = ObjectUtilities.buildArgument(var9, var10.getArray().getAt(var12), var2);
                     Array.set(var13, var11[var11.length - 1], var19);
                     int var10002 = var11[var11.length - 1]++;

                     for(int var15 = var11.length - 1; var11[var15] >= var6[var15]; --var15) {
                        if (var15 == 0) {
                           return SleepUtils.getScalar(var18);
                        }

                        var11[var15] = 0;
                        ++var11[var15 - 1];
                     }

                     ++var12;
                  }
               }
            }
         }
      }

      // $FF: synthetic method
      f_cast(Object var1) {
         this();
      }
   }

   private static class array implements Function {
      private array() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = SleepUtils.getArrayScalar();

         while(!var3.isEmpty()) {
            var4.getArray().push(SleepUtils.getScalar(BridgeUtilities.getScalar(var3)));
         }

         return var4;
      }

      // $FF: synthetic method
      array(Object var1) {
         this();
      }
   }

   private static class f_use implements Function {
      private HashMap bridges;

      private f_use() {
         this.bridges = new HashMap();
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         File var4 = null;
         String var5 = "";
         Class var6 = null;
         if (var3.size() == 2) {
            var4 = ParserConfig.findJarFile(var3.pop().toString());
            var5 = BridgeUtilities.getString(var3, "");
         } else {
            Scalar var7 = (Scalar)var3.pop();
            if (var7.objectValue() instanceof Class && var1.equals("&use")) {
               var6 = (Class)var7.objectValue();
            } else {
               File var8 = ParserConfig.findJarFile(var7.toString());
               var4 = var8.getParentFile();
               var5 = var8.getName();
            }
         }

         if (var4 != null && !var4.exists()) {
            throw new IllegalArgumentException(var1 + ": could not locate source '" + var4 + "'");
         } else {
            try {
               if (var1.equals("&use")) {
                  if (var6 == null) {
                     if (var4 != null) {
                        URLClassLoader var15 = new URLClassLoader(new URL[]{var4.toURL()});
                        var6 = Class.forName(var5, true, var15);
                     } else {
                        var6 = Class.forName(var5);
                     }
                  }

                  Loadable var16;
                  if (this.bridges.get(var6) == null) {
                     var16 = (Loadable)var6.newInstance();
                     this.bridges.put(var6, var16);
                  } else {
                     var16 = (Loadable)this.bridges.get(var6);
                  }

                  var16.scriptLoaded(var2);
               } else {
                  ScriptLoader var17 = (ScriptLoader)var2.getScriptEnvironment().getEnvironment().get("(isloaded)");
                  Scalar var10 = var2.getScriptVariables().getScalar("$__INCLUDE__");
                  if (var10 == null) {
                     var10 = SleepUtils.getEmptyScalar();
                     var2.getScriptVariables().getGlobalVariables().putScalar("$__INCLUDE__", var10);
                  }

                  Object var9;
                  File var11;
                  if (var4 != null) {
                     var11 = var4.isDirectory() ? new File(var4, var5) : var4;
                     URLClassLoader var12 = new URLClassLoader(new URL[]{var4.toURL()});
                     var17.touch(var5, var11.lastModified());
                     var2.associateFile(var11);
                     var9 = var12.getResourceAsStream(var5);
                     var10.setValue(SleepUtils.getScalar((Object)var11));
                  } else {
                     var11 = BridgeUtilities.toSleepFile(var5, var2);
                     var17.touch(var5, var11.lastModified());
                     var2.associateFile(var11);
                     var9 = new FileInputStream(var11);
                     var10.setValue(SleepUtils.getScalar((Object)var11));
                  }

                  if (var9 == null) {
                     throw new IOException("unable to locate " + var5 + " from: " + var4);
                  }

                  Block var18 = var17.compileScript(var5, (InputStream)var9);
                  SleepUtils.runCode(var18, var2.getScriptEnvironment());
               }
            } catch (YourCodeSucksException var13) {
               var2.getScriptEnvironment().flagError(var13);
            } catch (Exception var14) {
               var2.getScriptEnvironment().flagError(var14);
            }

            return SleepUtils.getEmptyScalar();
         }
      }

      // $FF: synthetic method
      f_use(Object var1) {
         this();
      }
   }

   private static class HashKeyValueOp implements Operator {
      private HashKeyValueOp() {
      }

      public Scalar operate(String var1, ScriptInstance var2, Stack var3) {
         Scalar var4 = (Scalar)var3.pop();
         Scalar var5 = (Scalar)var3.pop();
         return SleepUtils.getScalar((Object)(new KeyValuePair(var4, var5)));
      }

      // $FF: synthetic method
      HashKeyValueOp(Object var1) {
         this();
      }
   }

   private static class SyncPrimitives implements Function {
      private SyncPrimitives() {
      }

      public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
         if (var1.equals("&semaphore")) {
            int var5 = BridgeUtilities.getInt(var3, 1);
            return SleepUtils.getScalar((Object)(new Semaphore((long)var5)));
         } else {
            Semaphore var4;
            if (var1.equals("&acquire")) {
               var4 = (Semaphore)BridgeUtilities.getObject(var3);
               var4.P();
            } else if (var1.equals("&release")) {
               var4 = (Semaphore)BridgeUtilities.getObject(var3);
               var4.V();
            }

            return SleepUtils.getEmptyScalar();
         }
      }

      // $FF: synthetic method
      SyncPrimitives(Object var1) {
         this();
      }
   }
}
