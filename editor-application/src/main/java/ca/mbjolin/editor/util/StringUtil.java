package ca.mbjolin.editor.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

  public static String compare(String first, String second) {
    String result = "line:first=>second \n";
    List<String> firstLines = first.lines().toList();
    List<String> secondLines = second.lines().toList();
    Integer minSize = firstLines.size();
    if (firstLines.size() > secondLines.size()) {
      minSize = secondLines.size();
    }

    for (Integer lineNumber = 0; lineNumber < minSize; lineNumber++) {
      String diff = StringUtils.difference(firstLines.get(lineNumber), secondLines.get(lineNumber));
      //if (!diff.isEmpty()) {
        result = result
            .concat(String.valueOf(lineNumber + 1))
            .concat(" :")
            .concat(firstLines.get(lineNumber))
            .concat("=>")
            .concat(secondLines.get(lineNumber))
            .concat("\n");
      //}
    }

    result = result
        .concat("number of line : ")
        .concat(String.valueOf(firstLines.size()))
        .concat(" ")
        .concat(String.valueOf(secondLines.size()));

    return result;
  }
}
