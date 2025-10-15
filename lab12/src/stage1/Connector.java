package stage1;

import java.io.*;

public class Connector {
  private File file;

  public Connector(String filename) {
    this.file = new File(filename);
  }

  public Connector(File file) {
    this.file = file;
  }

  public void write(GardenTree[] trees) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeInt(trees.length);
      for (GardenTree tree : trees) {
        oos.writeObject(tree);
      }
      oos.flush();
    } catch (IOException e) {
      throw new IOException("Error writing to file: " + e.getMessage(), e);
    }
  }

  public GardenTree[] read() throws IOException, ClassNotFoundException {
    if (!file.exists()) {
      throw new FileNotFoundException("File not found: " + file.getPath());
    }

    try (FileInputStream fis = new FileInputStream(file);
        ObjectInputStream oin = new ObjectInputStream(fis)) {
      int length = oin.readInt();
      GardenTree[] result = new GardenTree[length];
      for (int i = 0; i < length; i++) {
        result[i] = (GardenTree) oin.readObject();
      }
      return result;
    } catch (IOException e) {
      throw new IOException("Error reading from file: " + e.getMessage(), e);
    } catch (ClassNotFoundException e) {
      throw new ClassNotFoundException("Class not found during deserialization", e);
    }
  }
}
