package kwee;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ThunderbirdFilterSorter {

  public static void main(String[] args) {
    // if (args.length != 1) {
    // System.out.println("Usage: java ThunderbirdFilterSorter <path_to_msgFilterRules.dat>");
    // System.exit(1);
    // }

    // String filename = args[0];
    String filename = "F:\\dev\\Tools\\Thunderbird\\src\\main\\resources\\msgFilterRules.dat";
    sortThunderbirdFilters(filename);
  }

  public static void sortThunderbirdFilters(String filename) {
    try {
      // Read the entire file
      String content = readFile(filename);

      // Pattern to match filter blocks
      Pattern pattern = Pattern.compile("(name=\"[^\"]*\".*?)(?=name=\"[^\"]*\"|\\Z)", Pattern.DOTALL);
      Matcher matcher = pattern.matcher(content);

      // Extract all filters with their names
      List<Filter> filters = new ArrayList<>();
      while (matcher.find()) {
        String filterContent = matcher.group(1);
        String name = extractFilterName(filterContent);
        if (name != null) {
          filters.add(new Filter(name, filterContent));
        }
      }

      // Sort filters alphabetically by name (case-insensitive)
      filters.sort(Comparator.comparing(f -> f.name.toLowerCase()));

      // Reconstruct the sorted content
      StringBuilder sortedContent = new StringBuilder();
      for (Filter filter : filters) {
        sortedContent.append(filter.content);
      }

      // Write the sorted content to a new file
      String outputFilename = filename + ".sorted";
      writeFile(outputFilename, sortedContent.toString());

      System.out.println("Successfully sorted " + filters.size() + " filters.");
      System.out.println("Output file: " + outputFilename);

    } catch (IOException e) {
      System.err.println("Error processing file: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static String extractFilterName(String filterContent) {
    Pattern namePattern = Pattern.compile("name=\"([^\"]*)\"");
    Matcher nameMatcher = namePattern.matcher(filterContent);
    if (nameMatcher.find()) {
      return nameMatcher.group(1);
    }
    return null;
  }

  private static String readFile(String filename) throws IOException {
    StringBuilder content = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        content.append(line).append("\n");
      }
    }
    return content.toString();
  }

  private static void writeFile(String filename, String content) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
      writer.write(content);
    }
  }

  static class Filter {
    String name;
    String content;

    Filter(String name, String content) {
      this.name = name;
      this.content = content;
    }
  }
}