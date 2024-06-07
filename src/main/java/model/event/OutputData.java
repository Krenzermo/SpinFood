package model.event;

import model.event.collection.Group;
import model.event.list.GroupList;
import model.event.collection.Pair;
import model.event.list.PairList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/** This class contains the methods for creating an output File for the Groups and pairs created in {@link PairList}
 * and {@link GroupList}
 *
 * @author Daniel Hinkelmann
 *
 */
public class OutputData {

    private String outputFilePath;
    private GroupList groupList;


    public OutputData(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public OutputData(String outputFilePath, GroupList groupList) {
        this.outputFilePath = outputFilePath;
        this.groupList = groupList;
    }


    /** This method writes the GroupList to a csv File sorted by Courses.
     *
     * See {@link Pair#asOutputString()} for the format of each entry
     *
     */
    public void makeOutputFile() {
        List<Group> groups = groupList.getGroups().stream().sorted(Comparator.comparing(Group::getCourse)).toList();
        List<Pair> pairs = groups.stream().flatMap(g -> Arrays.stream(g.getPairs())).toList();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath + "/pairs.csv"))) {
            for (Pair pair : pairs) {
                writer.write(pair.asOutputString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
