package logic;

import com.sun.xml.ws.rx.policy.assertion.metro.rm200702.AckRequestIntervalAssertion;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public abstract class FilesManager {

    private static final String RESOURCES_FOLDER_NAME = "Resources";
    private static final String SCHEMA_NAME = "Full6ColorsBoard.txt";

    public static ArrayList<String> readBoardFromFile() { //returns file lines by array of strings.
//        Scanner s;
//        try {
//            s = new Scanner(new File("C:\\Users\\oroze\\Desktop\\ChineseCheckersWeb12345\\src\\java\\resources\\Full6ColorsBoard.txt"));
//        } catch (FileNotFoundException ex) {
//            System.out.println("The file " + SCHEMA_NAME + " could not be found.");
//            return null;
//        }
//        ArrayList<String> list = new ArrayList<>();
//        while (s.hasNextLine()) {
//            list.add(s.nextLine());
//        }
//        s.close();
        ArrayList<String> list = readBoardHardCoded();
        return list;
    }
    
    private static ArrayList<String> readBoardHardCoded() {
        ArrayList<String> list = new ArrayList<>();
        
        list.add("            G            ");
        list.add("           G G           ");
        list.add("          G G G          ");
        list.add("         G G G G         ");
        list.add("R R R R E E E E E W W W W");
        list.add(" R R R E E E E E E W W W ");
        list.add("  R R E E E E E E E W W  ");
        list.add("   R E E E E E E E E W   ");
        list.add("    E E E E E E E E E    ");
        list.add("   B E E E E E E E E N   ");
        list.add("  B B E E E E E E E N N  ");
        list.add(" B B B E E E E E E N N N ");
        list.add("B B B B E E E E E N N N N");
        list.add("         Y Y Y Y         ");
        list.add("          Y Y Y          ");
        list.add("           Y Y           ");
        list.add("            Y            ");
        
        return list;
    }
}


