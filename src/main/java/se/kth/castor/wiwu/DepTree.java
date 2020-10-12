package se.kth.castor.wiwu;

import fr.dutra.tools.maven.deptree.core.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class DepTree {

    public static void main(String[] args) throws ParseException, IOException, VisitException {
        parseTree("src/test/resources/cloned/jHiccup/dependency-tree.txt");
    }

    public static void parseTree(String treeTextFilePath)
            throws ParseException, IOException, VisitException {
        InputType type = InputType.TEXT;
        Reader r = new BufferedReader(new InputStreamReader(new FileInputStream(treeTextFilePath), "UTF-8"));
        Parser parser = type.newParser();
        Node tree = parser.parse(r);

        //      VelocityRendererMain.main(new String[]{
        //                     "/Users/cesarsv/IdeaProjects/wiwu/src/test/resources/cloned/jHiccup/dependency-tree.txt",
        //                     String.valueOf(InputType.TEXT),
        //                     "/Users/cesarsv/IdeaProjects/wiwu/src/test/resources/html",
        //                     String.valueOf(VelocityRenderType.JQUERY_JSTREE)
        //             }
        //                              );
    }

}
