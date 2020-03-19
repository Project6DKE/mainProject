package FunctionReader.src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileCreator {
    public FileCreator(String fileName, String mass, String friction, String holeDist, String startX, String startY, String ballSpeed, String goalX, String goalY, String height, String gravity) throws IOException {

    File newFile = new File(fileName);
    if (newFile.createNewFile()) {
        System.out.println("File created: " + newFile.getName());
    } else {
        System.out.println("File already exists.");
    }
    FileWriter writer = new FileWriter(fileName);
    writer.write("m ="+ mass+"\n");
    writer.write("g ="+ gravity+"\n");
    writer.write("mu ="+friction+"\n");
    writer.write("vmax ="+ballSpeed+"\n");
    writer.write("tol ="+holeDist+"\n");
    writer.write("startX ="+startX+"\n");
    writer.write("startY ="+startY+"\n");
    writer.write("goalX ="+goalX+"\n");
    writer.write("goalY ="+goalY+"\n");
    writer.write("height ="+height+"\n");
    writer.close();
    FunctionReader newRead = new FunctionReader(fileName);
}



}