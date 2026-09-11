
package ca.mbjolin.editor.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ca.mbjolin.editor.config.AppConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/*https://www.baeldung.com/java-classloaders*/
@ApplicationScoped
public class CustomClassLoader extends ClassLoader {

  private final String classDirectory;

  @Inject
  public CustomClassLoader(AppConfig appConfig) {
    this.classDirectory = appConfig.getClassDirectory();
  }

  @Override
  public Class findClass(String name) throws ClassNotFoundException {
    byte[] b = loadClassFromFile(name);
    return defineClass(name, b, 0, b.length);
  }

  private byte[] loadClassFromFile(String fileName) {
    fileName = classDirectory + fileName;
    String input = fileName.replace('.', File.separatorChar) + ".class";

    InputStream inputStream;
    byte[] buffer = null;
    try {
      inputStream = new FileInputStream(input);

      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      int nextValue = 0;
      try {
        while ((nextValue = inputStream.read()) != -1) {
          byteStream.write(nextValue);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      buffer = byteStream.toByteArray();
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return buffer;
  }
}
