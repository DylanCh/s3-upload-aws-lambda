
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args){
        try {
            System.out.println(Base64.encodeBase64String(Files.readAllBytes(Paths.get(new File("test.txt").getAbsolutePath()))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
