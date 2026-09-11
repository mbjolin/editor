package ca.mbjolin.editor.facade;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;

import org.antlr.v4.Tool;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.FileUtils;

import ca.mbjolin.editor.config.AppConfig;
import ca.mbjolin.editor.preprocess._2_macro.grammar.MacroSymbolTable;
import ca.mbjolin.editor.process._1_translation.grammar.SymbolTable;
import ca.mbjolin.editor.util.LoggingErrorListener;
import ca.mbjolin.editor.util.TreeUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/*
 *
   https://github.com/antlr/antlr4/blob/master/doc/tool-options.md
   https://www.antlr.org/api/JavaTool/org/antlr/v4/tool/package-summary.html
   Tool tool = new Tool();
   Grammar grammar = tool.loadGrammar(rootPath + "/src/main/resources/antlr/grammar/JSON.G4");
   tool.process(grammar, true);
 */
@ApplicationScoped
public class AntlrFacade {

  private String javaDirectory;

  private String grammarDirectory =
      Paths.get("").toAbsolutePath().toString() + "/src/main/resources/antlr/grammar/";

  //private String abstractGrammarDirectory;

  private ClassLoader customClassLoader;

  @Inject
  public AntlrFacade(AppConfig appConfig, ClassLoader customClassLoader) {
    this.javaDirectory = appConfig.getJavaDirectory();
    //abstractGrammarDirectory = javaDirectory + "ca/mbjolin/antlr/editor";
    
    //this.customClassLoader = customClassLoader;
    // pour le debug quarkus
    this.customClassLoader = this.getClass().getClassLoader();
  }

  public void generateJava(String grammarName, String packageName)
      throws IOException, InterruptedException {

    String destPath = javaDirectory + packageName.replaceAll("\\.", "/") + "/";

    /*
     * Une erreur lors de la lecture de .token
     * error(114): editor-application/src/main/resources/antlr/grammar/EditorLexer.tokens
     * Alors on copie tous dans le src/gen/ et puis on raoule de là.
     */
    new File(destPath).mkdirs();
    File parserFile = new File(grammarDirectory + grammarName + "Parser.g4");
    File parserDestFile = new File(destPath + grammarName + "Parser.g4");
    FileUtils.copyFile(parserFile, parserDestFile);

    File lexerFile = new File(grammarDirectory + grammarName + "Lexer.g4");
    File lexerDestFile = new File(destPath + grammarName + "Lexer.g4");
    FileUtils.copyFile(lexerFile, lexerDestFile);

    String[] args = new String[6];
    args[0] = destPath + grammarName + "Lexer.g4";
    args[1] = destPath + grammarName + "Parser.g4";
    args[2] = "-package";
    args[3] = packageName;
    args[4] = "-o";
    args[5] = destPath;
    //args[6] = "-lib";
    //args[7] = abstractGrammarDirectory;

    Tool tool = new Tool(args);
    tool.processGrammarsOnCommandLine();
  }

  public void showTree(String grammarName, String exampleName) throws Exception {

    try {
      InputStream inputStream =
          AntlrFacade.class.getResourceAsStream("/antlr/example/" + exampleName);

      CharStream charStream = CharStreams.fromStream(inputStream);

      Lexer lexer = findLexer(grammarName, charStream);

      Object parser = findParser(grammarName, lexer);
      Parser parserInstance = Parser.class.cast(parser);
      //parserInstance.removeErrorListeners();

      ParseTree tree = findTree(grammarName, parser);

//      Class cls = parser.getClass();
//      SymbolTable st = (SymbolTable) cls.getField("symbolTable").get(parser);
//      st.verification();
//      st.convert();

      System.out.println("----- exemple : " + exampleName + " --------");

      System.out.println("tree " + grammarName + " : " + tree.toStringTree(parserInstance));

      System.out.println("\n");


    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void listenerResult(String grammarName, String exampleName) throws Exception {
    try {

      InputStream inputStream =
          AntlrFacade.class.getResourceAsStream("/antlr/example/" + exampleName);

      CharStream charStream = CharStreams.fromStream(inputStream);

      Lexer lexer = findLexer(grammarName, charStream);

      Object parser = findParser(grammarName, lexer);
      Parser parserInstance = Parser.class.cast(parser);
      parserInstance.removeErrorListeners();

      ParseTree tree = findTree(grammarName, parser);

      ParseTreeWalker walker = new ParseTreeWalker();

      System.out.println("----- listener : " + exampleName + " --------");

      ParseTreeListener execute = (ParseTreeListener) findListener(grammarName);
      walker.walk(execute, tree);

      String result =
          System.lineSeparator() + String.join(System.lineSeparator());// , execute.getResult());
      System.out.println("result " + grammarName + " : " + result);

      System.out.println("\n");

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String compileContentWithGrammar(String content, String grammarName) throws Exception {
    try {

      InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

      CharStream charStream = CharStreams.fromStream(inputStream);

      Lexer lexer = findLexer(grammarName, charStream);

      Object parser = findParser(grammarName, lexer);
      Parser parserInstance = Parser.class.cast(parser);
      parserInstance.removeErrorListeners();

      ParseTree tree = findTree(grammarName, parser);

      ParseTreeWalker walker = new ParseTreeWalker();

      ParseTreeListener execute = (ParseTreeListener) findListener(grammarName);
      walker.walk(execute, tree);

      String result =
          System.lineSeparator() + String.join(System.lineSeparator());// , execute.getResult());
      System.out.println("result " + grammarName + " : " + result);

      System.out.println("\n");



    } catch (IOException e) {
      e.printStackTrace();
    }

    return "Le retour";
  }

  public SymbolTable compileContentWithGrammarST(String content, String grammarName, LoggingErrorListener lexicalAnalysisError,
      LoggingErrorListener syntaxAnalysisError) throws Exception {
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

    CharStream charStream = CharStreams.fromStream(inputStream);

    Lexer lexer = findLexer(grammarName, charStream);
    lexer.addErrorListener(lexicalAnalysisError);
    Object parser = findParser(grammarName, lexer);
    Parser parserInstance = Parser.class.cast(parser);
    parserInstance.addErrorListener(syntaxAnalysisError);

    ParseTree tree = findTree(grammarName, parser);

    ParseTreeWalker walker = new ParseTreeWalker();

    ParseTreeListener execute = (ParseTreeListener) findListener(grammarName);
    walker.walk(execute, tree);

    String result = new TreeUtil().convertToStringTree(tree, Arrays.asList(parserInstance.getRuleNames()));
    System.out.println(result);

    Class<? extends Object> cls = parser.getClass();
    SymbolTable symbolTable = (SymbolTable) cls.getField("symbolTable").get(parser);

    return symbolTable;
  }

  public MacroSymbolTable compileContentWithGrammarMacroST(String content, String grammarName, LoggingErrorListener lexicalAnalysisError,
      LoggingErrorListener syntaxAnalysisError) throws Exception {
    InputStream inputStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

    CharStream charStream = CharStreams.fromStream(inputStream);

    Lexer lexer = findLexer(grammarName, charStream);
    lexer.addErrorListener(lexicalAnalysisError);
    Object parser = findParser(grammarName, lexer);
    Parser parserInstance = Parser.class.cast(parser);
    parserInstance.addErrorListener(syntaxAnalysisError);

    ParseTree tree = findTree(grammarName, parser);

    ParseTreeWalker walker = new ParseTreeWalker();

    ParseTreeListener execute = (ParseTreeListener) findListener(grammarName);
    walker.walk(execute, tree);

    String result = new TreeUtil().convertToStringTree(tree, Arrays.asList(parserInstance.getRuleNames()));
    System.out.println(result);

    Class<? extends Object> cls = parser.getClass();
    MacroSymbolTable macroSymbolTable = (MacroSymbolTable) cls.getField("macroSymbolTable").get(parser);

    return macroSymbolTable;
  }

  private Lexer findLexer(String grammarName, CharStream charStream) throws ClassNotFoundException,
      InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {
    Class<?> lexerClass = Class
        .forName("ca.mbjolin.antlr." + grammarName.toLowerCase() + "." + grammarName + "Lexer", true,
            customClassLoader);
    Constructor<?> lexerConstructor = lexerClass.getConstructor(CharStream.class);

    Lexer lexer = (Lexer) lexerConstructor.newInstance(charStream);
    // lexer.removeErrorListeners();
    return lexer;
  }

  private Object findListener(String grammarName)
      throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    Class<?> listenerClass = Class
        .forName(
            "ca.mbjolin.antlr." + grammarName.toLowerCase() + "." + grammarName
                + "ParserBaseListener",
            true, customClassLoader);

    Constructor<?> listenerConstructor = listenerClass.getConstructor();

    return listenerConstructor.newInstance();
  }

  private Object findParser(String grammarName, Lexer lexer) throws ClassNotFoundException,
      NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    TokenStream tokenStream = new CommonTokenStream(lexer);

    Class<?> parserClass = Class
        .forName("ca.mbjolin.antlr." + grammarName.toLowerCase() + "." + grammarName + "Parser", true,
            customClassLoader);
    Constructor<?> parserConstructor = parserClass.getConstructor(TokenStream.class);

    return parserConstructor.newInstance(tokenStream);
  }

  private ParseTree findTree(String grammarName, Object parser)
      throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchFieldException, SecurityException, NoSuchMethodException {

    Class<?> parserClass = Class
        .forName("ca.mbjolin.antlr." + grammarName.toLowerCase() + "." + grammarName + "Parser", true,
            customClassLoader);
    Field ruleNamesField = parserClass.getDeclaredField("ruleNames");
    String[] rulesNames = (String[]) ruleNamesField.get(parser);

    Method contextMethod = parserClass.getMethod(rulesNames[0]);
    ParseTree tree = (ParseTree) contextMethod.invoke(parser);

    return tree;
  }



}
