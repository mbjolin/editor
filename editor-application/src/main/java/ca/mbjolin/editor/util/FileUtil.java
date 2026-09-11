
package ca.mbjolin.editor.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

/* https://www.baeldung.com/java-nio2-watchservice */
public class FileUtil {

  public static void directoryNotChange(String directoryString) throws IOException, InterruptedException {

    WatchService watchService = FileSystems.getDefault().newWatchService();

    Path path = FileSystems.getDefault().getPath(directoryString);

    path.register(
        watchService,
        StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_DELETE,
        StandardWatchEventKinds.ENTRY_MODIFY);

    WatchKey key;
    while ((key = watchService.take()) != null) {
      if(key.pollEvents().size() > 1) {
        break;
      }
      for (WatchEvent<?> event : key.pollEvents()) {
        System.out.println(
            "Event kind:" + event.kind()
                + ". File affected: " + event.context() + ".");
      }
      key.reset();
    }
  }
}
