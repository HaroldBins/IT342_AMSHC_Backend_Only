import java.util.jar.*;
import java.io.*;

public class ListJarContents {
    public static void main(String[] args) throws IOException {
        JarFile jarFile = new JarFile("target/appointment-system-0.0.1-SNAPSHOT.jar");
        jarFile.stream()
               .map(JarEntry::getName)
               .filter(name -> name.contains("AuthController"))
               .forEach(System.out::println);
    }
}
