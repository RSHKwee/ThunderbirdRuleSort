package zandbak;
// String url = "jdbc:ucanaccess://F:/dev/Tools/Thunderbird/src/main/resources/hMailServer.sdf";

import com.healthmarketscience.jackcess.*;
import java.io.File;

public class JackcessSDFReader {
  public static void main(String[] args) {
    File sdfFile = new File("F:/dev/Tools/Thunderbird/src/main/resources/hMailServer.sdf");

    try {
      // Try different database types and encodings
      Database db = DatabaseBuilder.open(sdfFile);

      System.out.println("Database opened successfully!");
      System.out.println("Tables in database:");

      for (String tableName : db.getTableNames()) {
        System.out.println(" - " + tableName);
        Table table = db.getTable(tableName);
        System.out.println("   Columns: " + table.getColumnCount());
      }

      db.close();

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}