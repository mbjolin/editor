package ca.mbjolin.editor.test;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import io.quarkus.logging.Log;

/* Number try        | Reflect   | Direct | Reflect Keep
 * 100 * 100              | 30   | 1 | 8
 * 100 * 100 * 100        | 113  | 4 | 35
 * 100 * 100 * 100 * 100  | 8527 | 5 | 1687
 *
 * En pratique, je ne suis pas certain qu'on dépasse 10000 noeuds dans un document.
 *
 * */
public class ReflectiveTest {

  class Call {
    public Long method() {
      Long result = 0L;
      return result;
    }
  }

  Call object = new Call();

  long tryInt = 100 * 100;
  Method m;

  @Test
  public void performanceTest() throws Exception {

    m = object.getClass().getMethod("method");

    long beforeReflective = System.currentTimeMillis();
    for(long i=0 ; i<tryInt; i++) {
      reflectiveMethod();
    }

    long beforeMethod = System.currentTimeMillis();

    for(long i=0 ; i<tryInt; i++) {
      directMethod();
    }

    long afterMethod= System.currentTimeMillis();

    for(long i=0 ; i<tryInt; i++) {
      reflectiveMethodKeep();
    }

    long afterReflectKeep = System.currentTimeMillis();

    Log.info("Reflective : " + (beforeMethod - beforeReflective));
    Log.info("Method : " + (afterMethod - beforeMethod));
    Log.info("Reflect Keep : " + (afterReflectKeep - afterMethod));
  }

  private Long reflectiveMethod() {
    Method m;
    Long result = 0L;
    try {
      m = object.getClass().getMethod("method");
      result = (Long) m.invoke(object);
    }catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  private Long reflectiveMethodKeep() {
    Long result = 0L;
    try {
      result = (Long) m.invoke(object);
    }catch (Exception e) {
      e.printStackTrace();
    }
    return result;
  }

  private Long directMethod() {
    return object.method();
  }


}
