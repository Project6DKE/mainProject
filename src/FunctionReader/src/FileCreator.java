package FunctionReader.src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileCreator {
public FileCreator() throws IOException {
    File newFile = new File(getFileName());
    if (newFile.createNewFile()) {
        System.out.println("File created: " + newFile.getName());
    } else {
        System.out.println("File already exists.");
    }
    FileWriter writer = new FileWriter(getFileName());
    writer.write("m ="+ getMassFromUser()+"\n");
    writer.write("mu ="+getMuFromUser()+"\n");
    writer.write("vmax ="+getVmaxFromUser()+"\n");
    writer.write("tol ="+getTolFromUser()+"\n");
    writer.write("startX ="+getStartXFromUser()+"\n");
    writer.write("startY ="+getStartYFromUser()+"\n");
    writer.write("goalX ="+getGoalXFromUser()+"\n");
    writer.write("goalY ="+getGoalYFromUser()+"\n");
    writer.write("height ="+getHeightFuncFromUser()+"\n");
    writer.close();
}

}