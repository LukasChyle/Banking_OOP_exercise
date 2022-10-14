import java.io.*;
import java.util.List;

public class ObjectFileStore {

    public static void storeObjectList(List<?> objectList, String fileName){
        try (ObjectOutputStream objOPS = new ObjectOutputStream(new FileOutputStream(fileName.trim() + ".ser"))) {
            objOPS.writeObject(objectList);
            objOPS.flush();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static List<?> retrieveObjectList(String fileName){
        try (ObjectInputStream objIPS = new ObjectInputStream(new FileInputStream(fileName.trim() + ".ser"))) {
            return (List<?>) objIPS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e);
        }
        return null;
    }
}
