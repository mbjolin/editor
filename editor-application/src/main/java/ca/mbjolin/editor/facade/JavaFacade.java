package ca.mbjolin.editor.facade;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import ca.mbjolin.editor.config.AppConfig;
import ca.mbjolin.editor.util.CustomClassLoader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class JavaFacade {

  private String javaDirectory;

  private String classDirectory;

  private CustomClassLoader customClassLoader;

  @Inject
  public JavaFacade(AppConfig appConfig, CustomClassLoader customClassLoader) {
    this.javaDirectory = appConfig.getJavaDirectory();
    this.classDirectory = appConfig.getClassDirectory();
    this.customClassLoader = customClassLoader;
  }

  /* String... */
  public void compileJava(String grammarName, String packageName) throws FileNotFoundException {

    String origPath = javaDirectory + packageName.replaceAll("\\.", "/") + "/";

    File[] files = new File(origPath)
        .listFiles((FileFilter) new SuffixFileFilter(".java", IOCase.INSENSITIVE));

    if (files == null || files.length < 1) {
      throw new FileNotFoundException(
          "Il n'y a rien à compiler pour cette grammaire : " + grammarName);
    }

    // https://www.baeldung.com/java-compile-multiple-files
    OutputStream outputStream = new FileOutputStream(classDirectory + "compile.txt");
    OutputStream errorOutputStream = new FileOutputStream(classDirectory + "compile-error.txt");

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    String[] namesString = new String[files.length + 3];
    namesString[0] = "-verbose";
    namesString[1] = "-d";
    namesString[2] = classDirectory;

    for (int i = 3; i < files.length + 3; i++) {
      namesString[i] = origPath + files[i - 3].getName();
    }

    compiler.run(null, outputStream, errorOutputStream, namesString);

  }

  public void verifyIncludeClass(String packageName)
      throws ClassNotFoundException, FileNotFoundException {

    String origPath = classDirectory + packageName.replaceAll("\\.", "/") + "/";

    File[] files = new File(origPath)
        .listFiles((FileFilter) new SuffixFileFilter(".class", IOCase.INSENSITIVE));

    if (files == null || files.length < 1) {
      throw new FileNotFoundException(
          "Il n'y a rien à inclure dans la jvm pour ce package: " + packageName);
    }

    List<String> innerClassName = new ArrayList<>();
    List<String> className = new ArrayList<>();

    for (File file : files) {
      String fileName = file.getName().substring(0, file.getName().length() - 6);
      if (fileName.contains("$")) {
        innerClassName.add(fileName);
      } else if (fileName.endsWith("ParserListener")) {
        /* C'est une interface simplement. Et cela évite les conflits avec la classe de base. */
      } else {
        className.add(fileName);
      }
    }

    for (String name : className) {
      customClassLoader.findClass(packageName + "." + name);
    }

  }
}
