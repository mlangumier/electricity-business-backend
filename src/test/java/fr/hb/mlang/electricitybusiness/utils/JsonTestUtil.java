package fr.hb.mlang.electricitybusiness.utils;

import java.io.IOException;
import java.nio.file.Files;
import org.springframework.core.io.ClassPathResource;

/**
 * Util class that contains methods to read and assert JSON files in the testing environment.
 */
public class JsonTestUtil {

  private JsonTestUtil() {
    // Prevents instanciation
  }

  /**
   * Finds and reads a JSON file.
   *
   * @param folder Folder containing the file (inside "test/resources/json")
   * @param file   Name of the file with extension.
   * @return A string containing the JSON data, ready for assertion.
   * @throws IOException if the file cannot be found or read.
   */
  public static String readJsonFile(String folder, String file) throws IOException {
    ClassPathResource resource = new ClassPathResource("json/" + folder + "/" + file);
    return Files.readString(resource.getFile().toPath());
  }
}
