package se.kth.castor.wiwu;

import org.junit.jupiter.api.Test;

import java.io.File;

class CmdTest {

    @Test
    void name() {
        Cmd cmd = new Cmd(new File("tmp"));
        String[] str = new String[]{
                "cd",
                "/Users/cesarsv/IdeaProjects/wiwu/src/main/resources/cloned-repository/spoon",
                "mvn",
                "se.kth.castor:depclean-maven-plugin:1.1.0:depclean"
        };
        cmd.execProcess(str);
    }
}