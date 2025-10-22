package zandbak;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.crypt.CryptCodecProvider;
import java.io.File;

public class SDFRecovery {
  public static void main(String[] args) {
    File sdfFile = new File("F:/dev/Tools/Thunderbird/src/main/resources/hMailServer.sdf");

    try {
      // Jackcess should automatically handle encryption if the file is encrypted
      System.out.println("Attempting to open with automatic encryption detection...");
      Database db = DatabaseBuilder.open(sdfFile);

      System.out.println("SUCCESS! Database opened without password.");
      readDatabaseContents(db);
      db.close();

    } catch (Exception e) {
      System.out.println("Failed to open: " + e.getMessage());
      System.out.println("This suggests the file might be encrypted with a specific password.");

      // Try alternative approaches
      tryAlternativeRecoveryMethods(sdfFile);
    }
  }

  private static void readDatabaseContents(Database db) throws Exception {
    System.out.println("\n=== DATABASE CONTENTS ===");
    System.out.println("File format: " + db.getFileFormat());

    System.out.println("\nTables found:");
    for (String tableName : db.getTableNames()) {
      System.out.println(" - " + tableName);
      try {
        var table = db.getTable(tableName);
        System.out.println("   Rows: " + table.getRowCount());
      } catch (Exception e) {
        System.out.println("   Could not access table");
      }
    }
  }

  private static void tryAlternativeRecoveryMethods(File sdfFile) {
    System.out.println("\n=== TRYING ALTERNATIVE RECOVERY METHODS ===");

    // Method 2: Try with CodecProvider (for encrypted databases)
    try {
      System.out.println("Trying with CryptCodecProvider...");
      Database db = new DatabaseBuilder(sdfFile).setCodecProvider(new CryptCodecProvider()).open();
      System.out.println("SUCCESS with CryptCodecProvider!");
      readDatabaseContents(db);
      db.close();
      return;
    } catch (Exception e) {
      System.out.println("Failed with CryptCodecProvider: " + e.getMessage());
    }

    // Method 3: Try different file formats
    tryDifferentFormats(sdfFile);
  }

  private static void tryDifferentFormats(File sdfFile) {
    System.out.println("\nTrying different database formats...");

    Database.FileFormat[] formats = { Database.FileFormat.V2000, Database.FileFormat.V2003, Database.FileFormat.V2007,
        Database.FileFormat.V2010, Database.FileFormat.V2016, Database.FileFormat.V2019 };

    for (Database.FileFormat format : formats) {
      try {
        System.out.println("Trying format: " + format);
        Database db = new DatabaseBuilder(sdfFile).setFileFormat(format).open();
        System.out.println("SUCCESS with format: " + format);
        readDatabaseContents(db);
        db.close();
        return;
      } catch (Exception e) {
        System.out.println("Failed with format " + format + ": " + e.getMessage());
      }
    }
  }
}