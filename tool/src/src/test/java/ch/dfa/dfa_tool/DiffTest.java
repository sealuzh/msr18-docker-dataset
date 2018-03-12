package ch.dfa.dfa_tool;

import ch.dfa.dfa_tool.models.Diff;
import ch.dfa.dfa_tool.models.Snapshot;
import ch.dfa.dfa_tool.models.commands.*;
import ch.dfa.dfa_tool.models.diff.DiffType;
import ch.dfa.dfa_tool.services.DiffProcessor;
import ch.dfa.dfa_tool.services.DockerParser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DiffTest {
    public static DockerParser dockerParserToTest;

    @BeforeClass
    public static void setup() throws ParseException, IOException {
        dockerParserToTest = new DockerParser("", "");
    }

    @Test
    public void diffOfNullAndSnapshot() throws IOException {
        //arrange
        String line1 = "FROM ubuntu:14.2";
        String line2 = "\n" +
                "RUN apt-get update && \\\n" +
                "    DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-7-jdk && \\\n" +
                "    rm -rf /var/lib/apt/lists/*";

        String line3 = "ENV HADOOP_VERSION\t2.6.0\n";
        String line4 = "CMD [\"hdfs\"]";

        File rawFile = createWriteAndGetFile(Arrays.asList(line1, line2, line3, line4), "Snapshot");
        DockerParser dockerParser1 = new DockerParser("Snapshots", "Snapshot1");
        Snapshot snapshot1 = null;
        Snapshot snapshot2 = dockerParser1.getParsedDockerfileObject(rawFile);

        //act
        Diff diffActual = DiffProcessor.getDiff(snapshot1, snapshot2);
        DiffType diffTypeActual1 = diffActual.getDiffs().get(0);
        DiffType diffTypeActual2 = diffActual.getDiffs().get(1);
        DiffType diffTypeActual3 = diffActual.getDiffs().get(2);
        DiffType diffTypeActual4 = diffActual.getDiffs().get(3);
        DiffType diffTypeActual5 = diffActual.getDiffs().get(4);

        //assert
        assertEquals(6, diffActual.getDiffs().size());
        assertEquals("NULL_COMMIT", diffActual.getDiffState());
        assertEquals(0, diffActual.getDel());
        assertEquals(6, diffActual.getIns());
        assertEquals(0, diffActual.getMod());

        assertEquals("AddType_FROM", diffTypeActual1.getChangeType());
        assertEquals("ubuntu:14.2", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getExecutable());
        assertEquals("FROM", diffTypeActual1.getInstruction().name());

        assertEquals("AddType_CMD", diffTypeActual2.getChangeType());
        assertEquals("hdfs", diffTypeActual2.getExecutable());
        assertEquals("", diffTypeActual2.getAfter());
        assertEquals("CMD", diffTypeActual2.getInstruction().name());

        assertEquals("AddType_RUN", diffTypeActual3.getChangeType());
        assertEquals("apt-get", diffTypeActual3.getExecutable());
        assertEquals("¦update", diffTypeActual3.getAfter());
        assertEquals("RUN", diffTypeActual3.getInstruction().name());

        assertEquals("AddType_RUN", diffTypeActual4.getChangeType());
        assertEquals("DEBIAN_FRONTEND=noninteractive", diffTypeActual4.getExecutable());
        assertEquals("¦apt-get¦install¦-y¦openjdk-7-jdk", diffTypeActual4.getAfter());
        assertEquals("RUN", diffTypeActual4.getInstruction().name());

        assertEquals("AddType_RUN", diffTypeActual5.getChangeType());
        assertEquals("rm", diffTypeActual5.getExecutable());
        assertEquals("¦-rf¦/var/lib/apt/lists/*", diffTypeActual5.getAfter());
        assertEquals("RUN", diffTypeActual5.getInstruction().name());

    }

    @Test
    public void diffOfSnapshotAndSnapshot() throws IOException {
        //arrange
        String lineA1 = "FROM ubuntu:14.2";
        String lineA2 = "\n" +
                "RUN apt-get update && \\\n" +
                "    DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-7-jdk && \\\n" +
                "    rm -rf /var/lib/apt/lists/*";
        String lineA3 = "ENV HADOOP_VERSION\t2.6.0\n";
        String lineA4 = "CMD [\"hdfs\"]";
        File rawFileA = createWriteAndGetFile(Arrays.asList(lineA1, lineA2, lineA3, lineA4), "SnapshotA");

        String lineB1 = "FROM ubuntu:14.3";
        String lineB2 = "\n" +
                "RUN apt-get test && \\\n" +
                "    DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-7-jdk";
        String lineB3 = "ENV HADOP_VERSION\t2.6.0\n";
        String lineB4 = "CMD [\"hdfss\"]";
        String lineB5 = "ENV TEST\t2.6.0\n";
        File rawFileB = createWriteAndGetFile(Arrays.asList(lineB1, lineB2, lineB3, lineB4, lineB5), "SnapshotB");

        DockerParser dockerParserA = new DockerParser("Snapshots", "Snapshot1");
        DockerParser dockerParserB = new DockerParser("Snapshots", "Snapshot1");
        Snapshot snapshot1 = dockerParserA.getParsedDockerfileObject(rawFileA);
        Snapshot snapshot2 = dockerParserB.getParsedDockerfileObject(rawFileB);

        //act
        Diff diffActual = DiffProcessor.getDiff(snapshot1, snapshot2);
        DiffType diffTypeActual1 = diffActual.getDiffs().get(0);
        DiffType diffTypeActual2 = diffActual.getDiffs().get(1);
        DiffType diffTypeActual3 = diffActual.getDiffs().get(2);
        DiffType diffTypeActual4 = diffActual.getDiffs().get(3);
        DiffType diffTypeActual5 = diffActual.getDiffs().get(4);
        DiffType diffTypeActual6 = diffActual.getDiffs().get(5);
        DiffType diffTypeActual7 = diffActual.getDiffs().get(6);

        //assert
        assertEquals(7, diffActual.getDiffs().size());
        assertEquals("COMMIT_COMMIT", diffActual.getDiffState());
        assertEquals(2, diffActual.getDel());
        assertEquals(2, diffActual.getIns());
        assertEquals(3, diffActual.getMod());

        assertEquals("UpdateType_IMAGE_VERSION_NUMBER", diffTypeActual1.getChangeType());
        assertEquals("ubuntu:14.3", diffTypeActual1.getAfter());
        assertEquals("ubuntu:14.2", diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getExecutable());
        assertEquals("FROM", diffTypeActual1.getInstruction().name());

        assertEquals("UpdateType_CMD", diffTypeActual2.getChangeType());
        assertEquals(null, diffTypeActual2.getExecutable());
        assertEquals("hdfs", diffTypeActual2.getBefore());
        assertEquals("hdfss", diffTypeActual2.getAfter());
        assertEquals("CMD", diffTypeActual2.getInstruction().name());

        assertEquals("UpdateType_PARAMETER", diffTypeActual3.getChangeType());
        assertEquals("apt-get", diffTypeActual3.getExecutable());
        assertEquals("test", diffTypeActual3.getAfter());
        assertEquals("RUN", diffTypeActual3.getInstruction().name());

        assertEquals("DelType_RUN", diffTypeActual4.getChangeType());
        assertEquals("rm", diffTypeActual4.getExecutable());
        assertEquals(null, diffTypeActual4.getAfter());
        assertEquals("RUN", diffTypeActual4.getInstruction().name());

        assertEquals("AddType_ENV", diffTypeActual5.getChangeType());
        assertEquals(null, diffTypeActual5.getExecutable());
        assertEquals("HADOP_VERSION:2.6.0", diffTypeActual5.getAfter());
        assertEquals("ENV", diffTypeActual5.getInstruction().name());

        assertEquals("AddType_ENV", diffTypeActual6.getChangeType());
        assertEquals(null, diffTypeActual6.getExecutable());
        assertEquals("TEST:2.6.0", diffTypeActual6.getAfter());
        assertEquals("ENV", diffTypeActual6.getInstruction().name());

        assertEquals("DelType_ENV", diffTypeActual7.getChangeType());
        assertEquals(null, diffTypeActual7.getExecutable());
        assertEquals(null, diffTypeActual7.getAfter());
        assertEquals("ENV", diffTypeActual7.getInstruction().name());
    }

    @Test
    public void diffOfFroms_AddType_FROM() {
        //arrange
        From fromA = null;
        From fromB1 = new From(null, "ubuntu", 13.2);

        //act
        List<DiffType> diffTypesActual1 = DiffProcessor.getDiffOfFrom(fromA, fromB1);

        //assert
        assertEquals("AddType_FROM", diffTypesActual1.get(0).getChangeType());
        assertEquals("FROM", diffTypesActual1.get(0).getInstruction().name());
        assertEquals(null, diffTypesActual1.get(0).getBefore());
        assertEquals("ubuntu:13.2", diffTypesActual1.get(0).getAfter());
        assertEquals(null, diffTypesActual1.get(0).getExecutable());
    }

    @Test
    public void diffOfFroms_UpdateType_IMAGE_NAME() {
        //arrange
        From fromA = new From(null, "ubuntu", 13.2);
        From fromB = new From(null, "windows", "next", "");

        //act
        List<DiffType> diffTypesActual1 = DiffProcessor.getDiffOfFrom(fromA, fromB);

        //assert
        assertEquals("UpdateType_IMAGE_NAME", diffTypesActual1.get(0).getChangeType());
        assertEquals("FROM", diffTypesActual1.get(0).getInstruction().name());
        assertEquals("ubuntu", diffTypesActual1.get(0).getBefore());
        assertEquals("windows", diffTypesActual1.get(0).getAfter());
        assertEquals(null, diffTypesActual1.get(0).getExecutable());

    }

    @Test
    public void diffOfFroms_UpdateType_IMAGE_VERSION_STRING() {
        //arrange
        From fromA = new From(null, "ubuntu", "next", "");
        From fromB = new From(null, "ubuntu", "latest", "");

        //act
        List<DiffType> diffTypesActual1 = DiffProcessor.getDiffOfFrom(fromA, fromB);


        //assert
        assertEquals("UpdateType_IMAGE_VERSION_STRING", diffTypesActual1.get(0).getChangeType());
        assertEquals("FROM", diffTypesActual1.get(0).getInstruction().name());
        assertEquals("ubuntu:next", diffTypesActual1.get(0).getBefore());
        assertEquals("ubuntu:latest", diffTypesActual1.get(0).getAfter());
        assertEquals(null, diffTypesActual1.get(0).getExecutable());

    }

    @Test
    public void diffOfFroms_UpdateType_IMAGE_VERSION_NUMBER() {
        //arrange
        From fromA = new From(null, "windows", 7);
        From fromB = new From(null, "windows", 10);

        //act
        List<DiffType> diffTypesActual1 = DiffProcessor.getDiffOfFrom(fromA, fromB);

        //assert
        assertEquals("UpdateType_IMAGE_VERSION_NUMBER", diffTypesActual1.get(0).getChangeType());
        assertEquals("FROM", diffTypesActual1.get(0).getInstruction().name());
        assertEquals("windows:7.0", diffTypesActual1.get(0).getBefore());
        assertEquals("windows:10.0", diffTypesActual1.get(0).getAfter());
        assertEquals(null, diffTypesActual1.get(0).getExecutable());

    }


    @Test
    public void diffOfMaintainer_AddType_MAINTAINER() {
        //arrange
        Maintainer maintainerA = null;
        Maintainer maintainerB = new Maintainer(null, "cito");

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfMaintainer(maintainerA, maintainerB);

        //assert
        assertEquals("AddType_MAINTAINER", diffTypeActual.getChangeType());
        assertEquals("MAINAINER", diffTypeActual.getInstruction().name());
        assertEquals(null, diffTypeActual.getBefore());
        assertEquals("cito", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());

    }

    @Test
    public void diffOfMaintainer_DelType_MAINTAINER() {
        //arrange
        Maintainer maintainerA = new Maintainer(null, "cito");
        Maintainer maintainerB = null;


        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfMaintainer(maintainerA, maintainerB);

        //assert
        assertEquals("DelType_MAINTAINER", diffTypeActual.getChangeType());
        assertEquals("MAINAINER", diffTypeActual.getInstruction().name());
        assertEquals("cito", diffTypeActual.getBefore());
        assertEquals(null, diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());

    }

    @Test
    public void diffOfMaintainer_UpdateType_MAINTAINER() {
        //arrange
        Maintainer maintainerA = new Maintainer(null, "cito");
        ;
        Maintainer maintainerB = new Maintainer(null, "sali");

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfMaintainer(maintainerA, maintainerB);

        //assert
        assertEquals("UpdateType_MAINTAINER", diffTypeActual.getChangeType());
        assertEquals("MAINAINER", diffTypeActual.getInstruction().name());
        assertEquals("cito", diffTypeActual.getBefore());
        assertEquals("sali", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());

    }

    @Test
    public void diffOfCmd_AddType_CMD() {
        //arrange
        Cmd cmdA = null;

        String param1 = "checkout";
        String param2 = "-b";
        String param3 = "1111-fix-tests";
        Cmd cmdB = new Cmd(null, "git", Arrays.asList(param1, param2, param3));

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfCmd(cmdA, cmdB);
        DiffType diffTypeActual = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("AddType_CMD", diffTypeActual.getChangeType());
        assertEquals("CMD", diffTypeActual.getInstruction().name());
        assertEquals(null, diffTypeActual.getBefore());
        assertEquals("¦checkout¦-b¦1111-fix-tests", diffTypeActual.getAfter());
        assertEquals("git", diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfCmd_DelType_CMD() {
        //arrange
        String param1 = "checkout";
        String param2 = "-b";
        String param3 = "1111-fix-tests";
        Cmd cmdA = new Cmd(null, "git", Arrays.asList(param1, param2, param3));

        Cmd cmdB = null;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfCmd(cmdA, cmdB);
        DiffType diffTypeActual = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("DelType_CMD", diffTypeActual.getChangeType());
        assertEquals("CMD", diffTypeActual.getInstruction().name());
        assertEquals("¦checkout¦-b¦1111-fix-tests", diffTypeActual.getBefore());
        assertEquals(null, diffTypeActual.getAfter());
        assertEquals("git", diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfCmd_UpdateType_CMD() {
        //arrange
        String paramA1 = "checkout";
        String paramA2 = "-b";
        String paramA3 = "1111-fix-tests";
        Cmd cmdA = new Cmd(null, "git", Arrays.asList(paramA1, paramA2, paramA3));
        ;

        String paramB1 = "checkout";
        String paramB2 = "-b";
        String paramB3 = "1111-fix-tests";
        Cmd cmdB = new Cmd(null, "gitty", Arrays.asList(paramB1, paramB2, paramB3));

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfCmd(cmdA, cmdB);
        DiffType diffTypeActual = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("UpdateType_CMD", diffTypeActual.getChangeType());
        assertEquals("CMD", diffTypeActual.getInstruction().name());
        assertEquals("git", diffTypeActual.getBefore());
        assertEquals("gitty", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfCmd_UpdateType_CMD_EXECUTABLE_PARAMETER() {
        //arrange
        String paramA1 = "co";
        String paramA2 = "-b";
        String paramA3 = "1111-fix-tests";
        Cmd cmdA = new Cmd(null, "git", Arrays.asList(paramA1, paramA2, paramA3));
        ;

        String paramB1 = "checkout";
        String paramB3 = "1111-fix-tests";
        Cmd cmdB = new Cmd(null, "gitty", Arrays.asList(paramB1, paramB3));

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfCmd(cmdA, cmdB);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);
        DiffType diffTypeActual4 = diffTypesActual.get(3);

        //assert
        assertEquals(4, diffTypesActual.size());
        assertEquals("UpdateType_EXECUTABLE_PARAMETER", diffTypeActual1.getChangeType());
        assertEquals("CMD", diffTypeActual1.getInstruction().name());
        assertEquals("git", diffTypeActual1.getBefore());
        assertEquals("gitty", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("DelType_EXECUTABLE_PARAMETER", diffTypeActual2.getChangeType());
        assertEquals("CMD", diffTypeActual2.getInstruction().name());
        assertEquals("co", diffTypeActual2.getBefore());
        assertEquals(null, diffTypeActual2.getAfter());
        assertEquals("git", diffTypeActual2.getExecutable());

        assertEquals("DelType_EXECUTABLE_PARAMETER", diffTypeActual3.getChangeType());
        assertEquals("CMD", diffTypeActual3.getInstruction().name());
        assertEquals("-b", diffTypeActual3.getBefore());
        assertEquals(null, diffTypeActual3.getAfter());
        assertEquals("git", diffTypeActual3.getExecutable());

        assertEquals("AddType_EXECUTABLE_PARAMETER", diffTypeActual4.getChangeType());
        assertEquals("CMD", diffTypeActual4.getInstruction().name());
        assertEquals(null, diffTypeActual4.getBefore());
        assertEquals("checkout", diffTypeActual4.getAfter());
        assertEquals("git", diffTypeActual4.getExecutable());

    }


    @Test
    public void diffOfEntryPoint_AddType_ENTRYPOINT() {
        //arrange
        EntryPoint entryPointA = null;

        String param1 = "checkout";
        String param2 = "-b";
        String param3 = "1111-fix-tests";
        EntryPoint entryPointB = new EntryPoint(null, "git", Arrays.asList(param1, param2, param3));

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfEntryPoint(entryPointA, entryPointB);
        DiffType diffTypeActual = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("AddType_ENTRYPOINT", diffTypeActual.getChangeType());
        assertEquals("ENTRYPOINT", diffTypeActual.getInstruction().name());
        assertEquals(null, diffTypeActual.getBefore());
        assertEquals("¦checkout¦-b¦1111-fix-tests", diffTypeActual.getAfter());
        assertEquals("git", diffTypeActual.getExecutable());

    }

    @Test
    public void diffOfEntryPoint_DelType_ENTRYPOINT() {
        //arrange
        String param1 = "checkout";
        String param2 = "-b";
        String param3 = "1111-fix-tests";
        EntryPoint entryPointA = new EntryPoint(null, "git", Arrays.asList(param1, param2, param3));

        EntryPoint entryPointB = null;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfEntryPoint(entryPointA, entryPointB);
        DiffType diffTypeActual = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("DelType_ENTRYPOINT", diffTypeActual.getChangeType());
        assertEquals("ENTRYPOINT", diffTypeActual.getInstruction().name());
        assertEquals("¦checkout¦-b¦1111-fix-tests", diffTypeActual.getBefore());
        assertEquals(null, diffTypeActual.getAfter());
        assertEquals("git", diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfEntryPoint_UpdateType_ENTRYPOINT() {
        //arrange
        String paramA1 = "checkout";
        String paramA2 = "-b";
        String paramA3 = "1111-fix-tests";
        EntryPoint entryPointA = new EntryPoint(null, "git", Arrays.asList(paramA1, paramA2, paramA3));
        ;

        String paramB1 = "checkout";
        String paramB2 = "-b";
        String paramB3 = "1111-fix-tests";
        EntryPoint entryPointB = new EntryPoint(null, "gitty", Arrays.asList(paramB1, paramB2, paramB3));

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfEntryPoint(entryPointA, entryPointB);
        DiffType diffTypeActual = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("UpdateType_EXECUTABLE", diffTypeActual.getChangeType());
        assertEquals("ENTRYPOINT", diffTypeActual.getInstruction().name());
        assertEquals("git", diffTypeActual.getBefore());
        assertEquals("gitty", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfEntryPoint_UpdateType_ENTRYPOINT_EXECUTABLE_PARAMETER() {
        //arrange
        String paramA1 = "co";
        String paramA2 = "-b";
        String paramA3 = "1111-fix-tests";
        EntryPoint entryPointA = new EntryPoint(null, "git", Arrays.asList(paramA1, paramA2, paramA3));
        ;

        String paramB1 = "checkout";
        String paramB3 = "1111-fix-tests";
        EntryPoint entryPointB = new EntryPoint(null, "gitty", Arrays.asList(paramB1, paramB3));

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfEntryPoint(entryPointA, entryPointB);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);
        DiffType diffTypeActual4 = diffTypesActual.get(3);

        //assert
        assertEquals(4, diffTypesActual.size());
        assertEquals("UpdateType_EXECUTABLE_PARAMETER", diffTypeActual1.getChangeType());
        assertEquals("ENTRYPOINT", diffTypeActual1.getInstruction().name());
        assertEquals("git", diffTypeActual1.getBefore());
        assertEquals("gitty", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("DelType_EXECUTABLE_PARAMETER", diffTypeActual2.getChangeType());
        assertEquals("ENTRYPOINT", diffTypeActual2.getInstruction().name());
        assertEquals("co", diffTypeActual2.getBefore());
        assertEquals(null, diffTypeActual2.getAfter());
        assertEquals("git", diffTypeActual2.getExecutable());

        assertEquals("DelType_EXECUTABLE_PARAMETER", diffTypeActual3.getChangeType());
        assertEquals("ENTRYPOINT", diffTypeActual3.getInstruction().name());
        assertEquals("-b", diffTypeActual3.getBefore());
        assertEquals(null, diffTypeActual3.getAfter());
        assertEquals("git", diffTypeActual3.getExecutable());

        assertEquals("AddType_EXECUTABLE_PARAMETER", diffTypeActual4.getChangeType());
        assertEquals("ENTRYPOINT", diffTypeActual4.getInstruction().name());
        assertEquals(null, diffTypeActual4.getBefore());
        assertEquals("checkout", diffTypeActual4.getAfter());
        assertEquals("git", diffTypeActual4.getExecutable());

    }


    @Test
    public void diffOfMultipleRuns() {
        //arrange
        Run runA1 = new Run(null, "python", Arrays.asList("A", "B"));
        Run runA2 = new Run(null, "git", Arrays.asList("A", "B", "C", "D"));
        Run runA3 = new Run(null, "choco", Arrays.asList("X", "Y", "Z"));
        List<Run> oldRuns = Arrays.asList(runA1, runA2, runA3);

        Run runB1 = new Run(null, "git", Arrays.asList("A", "B", "D"));
        Run runB2 = new Run(null, "choco", Arrays.asList("Y", "Z"));
        Run runB3 = new Run(null, "git", Arrays.asList("A", "B"));
        List<Run> newRuns = Arrays.asList(runB1, runB2, runB3);


        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfMultipleRuns(oldRuns, newRuns);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);
        DiffType diffTypeActual4 = diffTypesActual.get(3);

        //assert
        assertEquals(4, diffTypesActual.size());
        assertEquals("DelType_PARAMETER", diffTypeActual1.getChangeType());
        assertEquals("RUN", diffTypeActual1.getInstruction().name());
        assertEquals("X", diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getAfter());
        assertEquals("choco", diffTypeActual1.getExecutable());

        assertEquals("DelType_PARAMETER", diffTypeActual2.getChangeType());
        assertEquals("RUN", diffTypeActual2.getInstruction().name());
        assertEquals("C", diffTypeActual2.getBefore());
        assertEquals(null, diffTypeActual2.getAfter());
        assertEquals("git", diffTypeActual2.getExecutable());

        assertEquals("AddType_RUN", diffTypeActual3.getChangeType());
        assertEquals("RUN", diffTypeActual3.getInstruction().name());
        assertEquals(null, diffTypeActual3.getBefore());
        assertEquals("¦A¦B", diffTypeActual3.getAfter());
        assertEquals("git", diffTypeActual3.getExecutable());

        assertEquals("DelType_RUN", diffTypeActual4.getChangeType());
        assertEquals("RUN", diffTypeActual4.getInstruction().name());
        assertEquals("¦A¦B", diffTypeActual4.getBefore());
        assertEquals(null, diffTypeActual4.getAfter());
        assertEquals("python", diffTypeActual4.getExecutable());
    }

    @Test
    public void diffOfRun_AddType_RUN() {
        //arrange
        Run runA = null;
        Run runB = new Run(null, "python", Arrays.asList("A", "B"));

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffsOfRun(runA, runB);
        DiffType diffTypeActual = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("AddType_RUN", diffTypeActual.getChangeType());
        assertEquals("RUN", diffTypeActual.getInstruction().name());
        assertEquals(null, diffTypeActual.getBefore());
        assertEquals("¦A¦B", diffTypeActual.getAfter());
        assertEquals("python", diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfRun_DelType_RUN() {
        //arrange
        Run runA = new Run(null, "python", Arrays.asList("A", "B"));
        Run runB = null;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffsOfRun(runA, runB);
        DiffType diffTypeActual = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("DelType_RUN", diffTypeActual.getChangeType());
        assertEquals("RUN", diffTypeActual.getInstruction().name());
        assertEquals("¦A¦B", diffTypeActual.getBefore());
        assertEquals(null, diffTypeActual.getAfter());
        assertEquals("python", diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfRun_UpdateType_RUN_EXECUTABLE() {
        //arrange
        Run runA = new Run(null, "python", Arrays.asList("A", "B"));
        Run runB = new Run(null, "matlab", Arrays.asList("A", "B"));

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffsOfRun(runA, runB);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("UpdateType_EXECUTABLE", diffTypeActual1.getChangeType());
        assertEquals("RUN", diffTypeActual1.getInstruction().name());
        assertEquals("python", diffTypeActual1.getBefore());
        assertEquals("matlab", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }

    @Test
    public void diffOfRun_UpdateType_RUN_EXECUTABLE_PARAMETER() {
        //arrange
        Run runA = new Run(null, "python", Arrays.asList("A", "B"));
        Run runB = new Run(null, "matlab", Arrays.asList("C", "B"));

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffsOfRun(runA, runB);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);

        //assert
        assertEquals(2, diffTypesActual.size());
        assertEquals("UpdateType_EXECUTABLE_PARAMETER", diffTypeActual1.getChangeType());
        assertEquals("RUN", diffTypeActual1.getInstruction().name());
        assertEquals("python", diffTypeActual1.getBefore());
        assertEquals("matlab", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("UpdateType_EXECUTABLE_PARAMETER", diffTypeActual2.getChangeType());
        assertEquals("RUN", diffTypeActual2.getInstruction().name());
        assertEquals("A", diffTypeActual2.getBefore());
        assertEquals("C", diffTypeActual2.getAfter());
        assertEquals("python", diffTypeActual2.getExecutable());
    }


    @Test
    public void diffOfMultipleLabels() {
        //arrange
        Label labelA1 = new Label(null, "python", "v1");
        Label labelA2 = new Label(null, "git", "v1");
        Label labelA3 = new Label(null, "choco", "v1");
        List<Label> oldLabels = Arrays.asList(labelA1, labelA2, labelA3);

        Label labelB1 = new Label(null, "git", "v5");
        Label labelB2 = new Label(null, "R", "1111");
        Label labelB3 = new Label(null, "env", "222");
        List<Label> newLabels = Arrays.asList(labelB1, labelB2, labelB3);


        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfMultipleLabels(oldLabels, newLabels);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);
        DiffType diffTypeActual4 = diffTypesActual.get(3);
        DiffType diffTypeActual5 = diffTypesActual.get(4);

        //assert
        assertEquals(5, diffTypesActual.size());
        assertEquals("UpdateType_VALUE", diffTypeActual1.getChangeType());
        assertEquals("LABEL", diffTypeActual1.getInstruction().name());
        assertEquals("git:v1", diffTypeActual1.getBefore());
        assertEquals("git:v5", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("AddType_LABEL", diffTypeActual2.getChangeType());
        assertEquals("LABEL", diffTypeActual2.getInstruction().name());
        assertEquals(null, diffTypeActual2.getBefore());
        assertEquals("R:1111", diffTypeActual2.getAfter());
        assertEquals(null, diffTypeActual2.getExecutable());

        assertEquals("AddType_LABEL", diffTypeActual3.getChangeType());
        assertEquals("LABEL", diffTypeActual3.getInstruction().name());
        assertEquals(null, diffTypeActual3.getBefore());
        assertEquals("env:222", diffTypeActual3.getAfter());
        assertEquals(null, diffTypeActual3.getExecutable());

        assertEquals("DelType_LABEL", diffTypeActual4.getChangeType());
        assertEquals("LABEL", diffTypeActual4.getInstruction().name());
        assertEquals("python:v1", diffTypeActual4.getBefore());
        assertEquals(null, diffTypeActual4.getAfter());
        assertEquals(null, diffTypeActual4.getExecutable());

        assertEquals("DelType_LABEL", diffTypeActual5.getChangeType());
        assertEquals("LABEL", diffTypeActual5.getInstruction().name());
        assertEquals("choco:v1", diffTypeActual5.getBefore());
        assertEquals(null, diffTypeActual5.getAfter());
        assertEquals(null, diffTypeActual5.getExecutable());

    }

    @Test
    public void diffOfLabels_AddType_LABEL() {
        //arrange
        Label oldLabel = null;
        Label newLabel = new Label(null, "git", "v1");

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfLabels(oldLabel, newLabel);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("AddType_LABEL", diffTypeActual1.getChangeType());
        assertEquals("LABEL", diffTypeActual1.getInstruction().name());
        assertEquals(null, diffTypeActual1.getBefore());
        assertEquals("git:v1", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

    }

    @Test
    public void diffOfLabels_DelType_LABEL() {
        //arrange
        Label oldLabel = new Label(null, "git", "v1");
        Label newLabel = null;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfLabels(oldLabel, newLabel);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("DelType_LABEL", diffTypeActual1.getChangeType());
        assertEquals("LABEL", diffTypeActual1.getInstruction().name());
        assertEquals("git:v1", diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }

    @Test
    public void diffOfLabels_UpdateType_KEY() {
        //arrange
        Label oldLabel = new Label(null, "git", "v1");
        Label newLabel = new Label(null, "gitty", "v1");
        ;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfLabels(oldLabel, newLabel);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("UpdateType_KEY", diffTypeActual1.getChangeType());
        assertEquals("LABEL", diffTypeActual1.getInstruction().name());
        assertEquals("git:v1", diffTypeActual1.getBefore());
        assertEquals("gitty:v1", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }

    @Test
    public void diffOfLabels_UpdateType_VALUE() {
        //arrange
        Label oldLabel = new Label(null, "git", "v1");
        Label newLabel = new Label(null, "git", "v2");
        ;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfLabels(oldLabel, newLabel);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("UpdateType_VALUE", diffTypeActual1.getChangeType());
        assertEquals("LABEL", diffTypeActual1.getInstruction().name());
        assertEquals("git:v1", diffTypeActual1.getBefore());
        assertEquals("git:v2", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }


    @Test
    public void diffOfMultipleAdds() {
        //arrange
        Add addA1 = new Add(null, "A", "B");
        Add addA2 = new Add(null, "C", "D");
        Add addA3 = new Add(null, "E", "F");
        List<Add> oldadds = Arrays.asList(addA1, addA2, addA3);

        Add addB1 = new Add(null, "A", "X");
        Add addB2 = new Add(null, "D", "E");
        List<Add> newadds = Arrays.asList(addB1, addB2);


        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfMultipleAdds(oldadds, newadds);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);
        DiffType diffTypeActual4 = diffTypesActual.get(3);

        //assert
        assertEquals(4, diffTypesActual.size());
        assertEquals("UpdateType_DESTINATION", diffTypeActual1.getChangeType());
        assertEquals("ADD", diffTypeActual1.getInstruction().name());
        assertEquals("A -> B", diffTypeActual1.getBefore());
        assertEquals("A -> X", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("AddType_ADD", diffTypeActual2.getChangeType());
        assertEquals("ADD", diffTypeActual2.getInstruction().name());
        assertEquals(null, diffTypeActual2.getBefore());
        assertEquals("D -> E", diffTypeActual2.getAfter());
        assertEquals(null, diffTypeActual2.getExecutable());

        assertEquals("DelType_ADD", diffTypeActual3.getChangeType());
        assertEquals("ADD", diffTypeActual3.getInstruction().name());
        assertEquals("C -> D", diffTypeActual3.getBefore());
        assertEquals(null, diffTypeActual3.getAfter());
        assertEquals(null, diffTypeActual3.getExecutable());

        assertEquals("DelType_ADD", diffTypeActual4.getChangeType());
        assertEquals("ADD", diffTypeActual4.getInstruction().name());
        assertEquals("E -> F", diffTypeActual4.getBefore());
        assertEquals(null, diffTypeActual4.getAfter());
        assertEquals(null, diffTypeActual4.getExecutable());

    }

    @Test
    public void diffOadds_AddType_ADD() {
        //arrange
        Add oldAdd = null;
        Add newAdd = new Add(null, "A", "B");

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfAdds(oldAdd, newAdd);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("AddType_ADD", diffTypeActual1.getChangeType());
        assertEquals("ADD", diffTypeActual1.getInstruction().name());
        assertEquals(null, diffTypeActual1.getBefore());
        assertEquals("A -> B", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

    }

    @Test
    public void diffOadds_DelType_ADD() {
        //arrange
        Add oldAdd = new Add(null, "git", "v1");
        Add newAdd = null;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfAdds(oldAdd, newAdd);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("DelType_ADD", diffTypeActual1.getChangeType());
        assertEquals("ADD", diffTypeActual1.getInstruction().name());
        assertEquals("git -> v1", diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }

    @Test
    public void diffOadds_UpdateType_SOURCE() {
        //arrange
        Add oldAdd = new Add(null, "git", "v1");
        Add newAdd = new Add(null, "gitty", "v1");
        ;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfAdds(oldAdd, newAdd);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("UpdateType_SOURCE", diffTypeActual1.getChangeType());
        assertEquals("ADD", diffTypeActual1.getInstruction().name());
        assertEquals("git -> v1", diffTypeActual1.getBefore());
        assertEquals("gitty -> v1", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }

    @Test
    public void diffOadds_UpdateType_VALUE() {
        //arrange
        Add oldAdd = new Add(null, "git", "v1");
        Add newAdd = new Add(null, "git", "v2");
        ;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfAdds(oldAdd, newAdd);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("UpdateType_DESTINATION", diffTypeActual1.getChangeType());
        assertEquals("ADD", diffTypeActual1.getInstruction().name());
        assertEquals("git -> v1", diffTypeActual1.getBefore());
        assertEquals("git -> v2", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }


    @Test
    public void diffOfMultipleCopies() {
        //arrange
        Copy copyA1 = new Copy(null, "A", "B");
        Copy copyA2 = new Copy(null, "C", "D");
        Copy copyA3 = new Copy(null, "E", "F");
        List<Copy> oldcopies = Arrays.asList(copyA1, copyA2, copyA3);

        Copy copyB1 = new Copy(null, "A", "X");
        Copy copyB2 = new Copy(null, "D", "E");
        List<Copy> newcopies = Arrays.asList(copyB1, copyB2);


        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfMultipleCopies(oldcopies, newcopies);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);
        DiffType diffTypeActual4 = diffTypesActual.get(3);

        //assert
        assertEquals(4, diffTypesActual.size());
        assertEquals("UpdateType_DESTINATION", diffTypeActual1.getChangeType());
        assertEquals("COPY", diffTypeActual1.getInstruction().name());
        assertEquals("A -> B", diffTypeActual1.getBefore());
        assertEquals("A -> X", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("AddType_COPY", diffTypeActual2.getChangeType());
        assertEquals("COPY", diffTypeActual2.getInstruction().name());
        assertEquals(null, diffTypeActual2.getBefore());
        assertEquals("D -> E", diffTypeActual2.getAfter());
        assertEquals(null, diffTypeActual2.getExecutable());

        assertEquals("DelType_COPY", diffTypeActual3.getChangeType());
        assertEquals("COPY", diffTypeActual3.getInstruction().name());
        assertEquals("C -> D", diffTypeActual3.getBefore());
        assertEquals(null, diffTypeActual3.getAfter());
        assertEquals(null, diffTypeActual3.getExecutable());

        assertEquals("DelType_COPY", diffTypeActual4.getChangeType());
        assertEquals("COPY", diffTypeActual4.getInstruction().name());
        assertEquals("E -> F", diffTypeActual4.getBefore());
        assertEquals(null, diffTypeActual4.getAfter());
        assertEquals(null, diffTypeActual4.getExecutable());

    }

    @Test
    public void diffOfCopy_CopyType_ADD() {
        //arrange
        Copy oldCopy = null;
        Copy newCopy = new Copy(null, "A", "B");

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfCopies(oldCopy, newCopy);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("AddType_COPY", diffTypeActual1.getChangeType());
        assertEquals("COPY", diffTypeActual1.getInstruction().name());
        assertEquals(null, diffTypeActual1.getBefore());
        assertEquals("A -> B", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

    }

    @Test
    public void diffOfCopy_DelType_ADD() {
        //arrange
        Copy oldCopy = new Copy(null, "git", "v1");
        Copy newCopy = null;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfCopies(oldCopy, newCopy);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("DelType_COPY", diffTypeActual1.getChangeType());
        assertEquals("COPY", diffTypeActual1.getInstruction().name());
        assertEquals("git -> v1", diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }

    @Test
    public void diffOfCopy_UpdateType_SOURCE() {
        //arrange
        Copy oldCopy = new Copy(null, "git", "v1");
        Copy newCopy = new Copy(null, "gitty", "v1");
        ;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfCopies(oldCopy, newCopy);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("UpdateType_SOURCE", diffTypeActual1.getChangeType());
        assertEquals("COPY", diffTypeActual1.getInstruction().name());
        assertEquals("git -> v1", diffTypeActual1.getBefore());
        assertEquals("gitty -> v1", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }

    @Test
    public void diffOfCopy_UpdateType_DESTINATION() {
        //arrange
        Copy oldCopy = new Copy(null, "git", "v1");
        Copy newCopy = new Copy(null, "git", "v2");
        ;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfCopies(oldCopy, newCopy);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("UpdateType_DESTINATION", diffTypeActual1.getChangeType());
        assertEquals("COPY", diffTypeActual1.getInstruction().name());
        assertEquals("git -> v1", diffTypeActual1.getBefore());
        assertEquals("git -> v2", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }

    @Test
    public void diffOfMultipleEnvs() {
        //arrange
        Env labelA1 = new Env(null, "python", "v1");
        Env labelA2 = new Env(null, "git", "v1");
        Env labelA3 = new Env(null, "choco", "v1");
        List<Env> oldEnvs = Arrays.asList(labelA1, labelA2, labelA3);

        Env labelB1 = new Env(null, "git", "v5");
        Env labelB2 = new Env(null, "R", "1111");
        Env labelB3 = new Env(null, "env", "222");
        List<Env> newEnvs = Arrays.asList(labelB1, labelB2, labelB3);


        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfMultipleEnvs(oldEnvs, newEnvs);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);
        DiffType diffTypeActual4 = diffTypesActual.get(3);
        DiffType diffTypeActual5 = diffTypesActual.get(4);

        //assert
        assertEquals(5, diffTypesActual.size());
        assertEquals("UpdateType_VALUE", diffTypeActual1.getChangeType());
        assertEquals("ENV", diffTypeActual1.getInstruction().name());
        assertEquals("git:v1", diffTypeActual1.getBefore());
        assertEquals("git:v5", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("AddType_ENV", diffTypeActual2.getChangeType());
        assertEquals("ENV", diffTypeActual2.getInstruction().name());
        assertEquals(null, diffTypeActual2.getBefore());
        assertEquals("R:1111", diffTypeActual2.getAfter());
        assertEquals(null, diffTypeActual2.getExecutable());

        assertEquals("AddType_ENV", diffTypeActual3.getChangeType());
        assertEquals("ENV", diffTypeActual3.getInstruction().name());
        assertEquals(null, diffTypeActual3.getBefore());
        assertEquals("env:222", diffTypeActual3.getAfter());
        assertEquals(null, diffTypeActual3.getExecutable());

        assertEquals("DelType_ENV", diffTypeActual4.getChangeType());
        assertEquals("ENV", diffTypeActual4.getInstruction().name());
        assertEquals("python:v1", diffTypeActual4.getBefore());
        assertEquals(null, diffTypeActual4.getAfter());
        assertEquals(null, diffTypeActual4.getExecutable());

        assertEquals("DelType_ENV", diffTypeActual5.getChangeType());
        assertEquals("ENV", diffTypeActual5.getInstruction().name());
        assertEquals("choco:v1", diffTypeActual5.getBefore());
        assertEquals(null, diffTypeActual5.getAfter());
        assertEquals(null, diffTypeActual5.getExecutable());

    }

    @Test
    public void diffOfEnvs_AddType_LABEL() {
        //arrange
        Env oldEnv = null;
        Env newEnv = new Env(null, "git", "v1");

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfEnvs(oldEnv, newEnv);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("AddType_ENV", diffTypeActual1.getChangeType());
        assertEquals("ENV", diffTypeActual1.getInstruction().name());
        assertEquals(null, diffTypeActual1.getBefore());
        assertEquals("git:v1", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

    }

    @Test
    public void diffOfEnvs_DelType_LABEL() {
        //arrange
        Env oldEnv = new Env(null, "git", "v1");
        Env newEnv = null;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfEnvs(oldEnv, newEnv);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("DelType_ENV", diffTypeActual1.getChangeType());
        assertEquals("ENV", diffTypeActual1.getInstruction().name());
        assertEquals("git:v1", diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }

    @Test
    public void diffOfEnvs_UpdateType_KEY() {
        //arrange
        Env oldEnv = new Env(null, "git", "v1");
        Env newEnv = new Env(null, "gitty", "v1");
        ;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfEnvs(oldEnv, newEnv);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("UpdateType_KEY", diffTypeActual1.getChangeType());
        assertEquals("ENV", diffTypeActual1.getInstruction().name());
        assertEquals("git:v1", diffTypeActual1.getBefore());
        assertEquals("gitty:v1", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }

    @Test
    public void diffOfEnvs_UpdateType_VALUE() {
        //arrange
        Env oldEnv = new Env(null, "git", "v1");
        Env newEnv = new Env(null, "git", "v2");
        ;

        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfEnvs(oldEnv, newEnv);
        DiffType diffTypeActual1 = diffTypesActual.get(0);

        //assert
        assertEquals(1, diffTypesActual.size());
        assertEquals("UpdateType_VALUE", diffTypeActual1.getChangeType());
        assertEquals("ENV", diffTypeActual1.getInstruction().name());
        assertEquals("git:v1", diffTypeActual1.getBefore());
        assertEquals("git:v2", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());
    }

    @Test
    public void getDiffOfMultipleUsers() {
        //arrange
        User userA1 = new User(null, "sali");
        User userA2 = new User(null, "cito");
        User userA3 = new User(null, "gerald");
        List<User> oldUsers = Arrays.asList(userA1, userA2, userA3);

        User userB1 = new User(null, "szumbe");
        User userB2 = new User(null, "cito");
        List<User> newUsers = Arrays.asList(userB1, userB2);


        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfMultipleUsers(oldUsers, newUsers);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);

        //assert
        assertEquals(3, diffTypesActual.size());
        assertEquals("DelType_USER", diffTypeActual1.getChangeType());
        assertEquals("USER", diffTypeActual1.getInstruction().name());
        assertEquals("sali", diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("DelType_USER", diffTypeActual2.getChangeType());
        assertEquals("USER", diffTypeActual2.getInstruction().name());
        assertEquals("gerald", diffTypeActual2.getBefore());
        assertEquals(null, diffTypeActual2.getAfter());
        assertEquals(null, diffTypeActual2.getExecutable());

        assertEquals("AddType_USER", diffTypeActual3.getChangeType());
        assertEquals("USER", diffTypeActual3.getInstruction().name());
        assertEquals(null, diffTypeActual3.getBefore());
        assertEquals("szumbe", diffTypeActual3.getAfter());
        assertEquals(null, diffTypeActual3.getExecutable());

    }

    @Test
    public void diffOfUsers_AddType_USER() {
        //arrange
        User oldUser = null;
        User newUser = new User(null, "gerald");

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfUsers(oldUser, newUser);

        //assert
        assertEquals("AddType_USER", diffTypeActual.getChangeType());
        assertEquals("USER", diffTypeActual.getInstruction().name());
        assertEquals(null, diffTypeActual.getBefore());
        assertEquals("gerald", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());

    }

    @Test
    public void diffOfUsers_DelType_USER() {
        //arrange
        User oldUser = new User(null, "sali");
        User newUser = null;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfUsers(oldUser, newUser);

        //assert
        assertEquals("DelType_USER", diffTypeActual.getChangeType());
        assertEquals("USER", diffTypeActual.getInstruction().name());
        assertEquals("sali", diffTypeActual.getBefore());
        assertEquals(null, diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfUsers_UpdateType_USER_NAME() {
        //arrange
        User oldUser = new User(null, "gerald");
        User newUser = new User(null, "sali");
        ;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfUsers(oldUser, newUser);

        //assert
        assertEquals("UpdateType_USER_NAME", diffTypeActual.getChangeType());
        assertEquals("USER", diffTypeActual.getInstruction().name());
        assertEquals("gerald", diffTypeActual.getBefore());
        assertEquals("sali", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }


    @Test
    public void getDiffOfMultipleWorkDir() {
        //arrange
        WorkDir workDirA1 = new WorkDir(null, "a");
        WorkDir workDirA2 = new WorkDir(null, "b");
        WorkDir workDirA3 = new WorkDir(null, "c");
        List<WorkDir> oldWorkDirs = Arrays.asList(workDirA1, workDirA2, workDirA3);

        WorkDir workDirB1 = new WorkDir(null, "aa");
        WorkDir workDirB2 = new WorkDir(null, "b");
        List<WorkDir> newWorkDirs = Arrays.asList(workDirB1, workDirB2);


        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfMultipleWorkDir(oldWorkDirs, newWorkDirs);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);

        //assert
        assertEquals(3, diffTypesActual.size());
        assertEquals("DelType_WORKDIR", diffTypeActual1.getChangeType());
        assertEquals("WORKDIR", diffTypeActual1.getInstruction().name());
        assertEquals("a", diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("DelType_WORKDIR", diffTypeActual2.getChangeType());
        assertEquals("WORKDIR", diffTypeActual2.getInstruction().name());
        assertEquals("c", diffTypeActual2.getBefore());
        assertEquals(null, diffTypeActual2.getAfter());
        assertEquals(null, diffTypeActual2.getExecutable());

        assertEquals("AddType_WORKDIR", diffTypeActual3.getChangeType());
        assertEquals("WORKDIR", diffTypeActual3.getInstruction().name());
        assertEquals(null, diffTypeActual3.getBefore());
        assertEquals("aa", diffTypeActual3.getAfter());
        assertEquals(null, diffTypeActual3.getExecutable());

    }

    @Test
    public void diffOfWorkDirs_AddType_WORKDIR() {
        //arrange
        WorkDir oldWorkDir = null;
        WorkDir newWorkDir = new WorkDir(null, "a");

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfWorkDirs(oldWorkDir, newWorkDir);

        //assert
        assertEquals("AddType_WORKDIR", diffTypeActual.getChangeType());
        assertEquals("WORKDIR", diffTypeActual.getInstruction().name());
        assertEquals(null, diffTypeActual.getBefore());
        assertEquals("a", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());

    }

    @Test
    public void diffOfWorkDirs_DelType_WORKDIR() {
        //arrange
        WorkDir oldWorkDir = new WorkDir(null, "b");
        WorkDir newWorkDir = null;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfWorkDirs(oldWorkDir, newWorkDir);

        //assert
        assertEquals("DelType_WORKDIR", diffTypeActual.getChangeType());
        assertEquals("WORKDIR", diffTypeActual.getInstruction().name());
        assertEquals("b", diffTypeActual.getBefore());
        assertEquals(null, diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfWorkDirs_UpdateType_WORKDIR_PATH() {
        //arrange
        WorkDir oldWorkDir = new WorkDir(null, "a");
        WorkDir newWorkDir = new WorkDir(null, "b");
        ;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfWorkDirs(oldWorkDir, newWorkDir);

        //assert
        assertEquals("UpdateType_PATH", diffTypeActual.getChangeType());
        assertEquals("WORKDIR", diffTypeActual.getInstruction().name());
        assertEquals("a", diffTypeActual.getBefore());
        assertEquals("b", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }


    @Test
    public void getDiffOfMultipleComments() {
        //arrange
        Comment commentA1 = new Comment(null, "FROM","this is a comment for FROM");
        Comment commentA2 = new Comment(null, "RUN","this is a comment for RUN");
        Comment commentA3 = new Comment(null, "ENV","this is a comment for ENV");
        List<Comment> oldComments = Arrays.asList(commentA1, commentA2, commentA3);

        Comment commentB1 = new Comment(null, "FROM","this is a modified comment for FROM");
        Comment commentB2 = new Comment(null, "RUN","please consider the new dependency");
        List<Comment> newComments = Arrays.asList(commentB1, commentB2);


        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfMultipleComments(oldComments, newComments);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);
        DiffType diffTypeActual4 = diffTypesActual.get(3);

        //assert
        assertEquals(4, diffTypesActual.size());
        assertEquals("UpdateType_COMMENT", diffTypeActual1.getChangeType());
        assertEquals("COMMENT", diffTypeActual1.getInstruction().name());
        assertEquals("this is a comment for FROM", diffTypeActual1.getBefore());
        assertEquals("this is a modified comment for FROM", diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("DelType_COMMENT", diffTypeActual2.getChangeType());
        assertEquals("COMMENT", diffTypeActual2.getInstruction().name());
        assertEquals("this is a comment for RUN", diffTypeActual2.getBefore());
        assertEquals(null, diffTypeActual2.getAfter());
        assertEquals(null, diffTypeActual2.getExecutable());

        assertEquals("DelType_COMMENT", diffTypeActual3.getChangeType());
        assertEquals("COMMENT", diffTypeActual3.getInstruction().name());
        assertEquals("this is a comment for ENV", diffTypeActual3.getBefore());
        assertEquals(null, diffTypeActual3.getAfter());
        assertEquals(null, diffTypeActual3.getExecutable());      
        
        assertEquals("AddType_COMMENT", diffTypeActual4.getChangeType());
        assertEquals("COMMENT", diffTypeActual4.getInstruction().name());
        assertEquals(null, diffTypeActual4.getBefore());
        assertEquals("please consider the new dependency", diffTypeActual4.getAfter());
        assertEquals(null, diffTypeActual4.getExecutable());

    }

    @Test
    public void diffOfComments_AddType_COMMENT() {
        //arrange
        Comment oldComment = null;
        Comment newComment = new Comment(null, "FROM","This from has a new comment now");

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfComments(oldComment, newComment);

        //assert
        assertEquals("AddType_COMMENT", diffTypeActual.getChangeType());
        assertEquals("COMMENT", diffTypeActual.getInstruction().name());
        assertEquals(null, diffTypeActual.getBefore());
        assertEquals("This from has a new comment now", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());

    }

    @Test
    public void diffOfComments_DelType_COMMENT() {
        //arrange
        Comment oldComment = new Comment(null, "RUN","TODO:");
        Comment newComment = null;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfComments(oldComment, newComment);

        //assert
        assertEquals("DelType_COMMENT", diffTypeActual.getChangeType());
        assertEquals("COMMENT", diffTypeActual.getInstruction().name());
        assertEquals("TODO:", diffTypeActual.getBefore());
        assertEquals(null, diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfComments_UpdateType_COMMENT() {
        //arrange
        Comment oldComment = new Comment(null, "FROM","i dont know why we choose this version");
        Comment newComment = new Comment(null, "FROM","new version because of security reasons");
        ;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfComments(oldComment, newComment);

        //assert
        assertEquals("UpdateType_COMMENT", diffTypeActual.getChangeType());
        assertEquals("COMMENT", diffTypeActual.getInstruction().name());
        assertEquals("i dont know why we choose this version", diffTypeActual.getBefore());
        assertEquals("new version because of security reasons", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    @Test
    public void getDiffOfMultipleVolumes() {
        //arrange
        Volume volumeA1 = new Volume(null, "a");
        Volume volumeA2 = new Volume(null, "b");
        Volume volumeA3 = new Volume(null, "c");
        List<Volume> oldVolumes = Arrays.asList(volumeA1, volumeA2, volumeA3);

        Volume volumeB1 = new Volume(null, "aa");
        Volume volumeB2 = new Volume(null, "b");
        List<Volume> newVolumes = Arrays.asList(volumeB1, volumeB2);


        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfMultipleVolumes(oldVolumes, newVolumes);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);

        //assert
        assertEquals(3, diffTypesActual.size());
        assertEquals("DelType_VOLUME", diffTypeActual1.getChangeType());
        assertEquals("VOLUME", diffTypeActual1.getInstruction().name());
        assertEquals("a", diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("DelType_VOLUME", diffTypeActual2.getChangeType());
        assertEquals("VOLUME", diffTypeActual2.getInstruction().name());
        assertEquals("c", diffTypeActual2.getBefore());
        assertEquals(null, diffTypeActual2.getAfter());
        assertEquals(null, diffTypeActual2.getExecutable());

        assertEquals("AddType_VOLUME", diffTypeActual3.getChangeType());
        assertEquals("VOLUME", diffTypeActual3.getInstruction().name());
        assertEquals(null, diffTypeActual3.getBefore());
        assertEquals("aa", diffTypeActual3.getAfter());
        assertEquals(null, diffTypeActual3.getExecutable());

    }

    @Test
    public void diffOfVolumes_AddType_VOLUMES() {
        //arrange
        Volume oldVolume = null;
        Volume newVolume = new Volume(null, "a");

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfVolumes(oldVolume, newVolume);

        //assert
        assertEquals("AddType_VOLUME", diffTypeActual.getChangeType());
        assertEquals("VOLUME", diffTypeActual.getInstruction().name());
        assertEquals(null, diffTypeActual.getBefore());
        assertEquals("a", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());

    }

    @Test
    public void diffOfVolumes_DelType_VOLUMES() {
        //arrange
        Volume oldVolume = new Volume(null, "b");
        Volume newVolume = null;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfVolumes(oldVolume, newVolume);

        //assert
        assertEquals("DelType_VOLUME", diffTypeActual.getChangeType());
        assertEquals("VOLUME", diffTypeActual.getInstruction().name());
        assertEquals("b", diffTypeActual.getBefore());
        assertEquals(null, diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfVolumes_UpdateType_VOLUMES() {
        //arrange
        Volume oldVolume = new Volume(null, "a");
        Volume newVolume = new Volume(null, "b");
        ;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfVolumes(oldVolume, newVolume);

        //assert
        assertEquals("UpdateType_VALUE", diffTypeActual.getChangeType());
        assertEquals("VOLUME", diffTypeActual.getInstruction().name());
        assertEquals("a", diffTypeActual.getBefore());
        assertEquals("b", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    @Test
    public void getDiffOfMultipleExposes() {
        //arrange
        Expose exposeA1 = new Expose(null, "8080");
        Expose exposeA2 = new Expose(null, "9000");
        Expose exposeA3 = new Expose(null, "4200");
        List<Expose> oldExposes = Arrays.asList(exposeA1, exposeA2, exposeA3);

        Expose exposeB1 = new Expose(null, "8080");
        Expose exposeB2 = new Expose(null, "10");
        List<Expose> newExposes = Arrays.asList(exposeB1, exposeB2);


        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfMultipleExposes(oldExposes, newExposes);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);

        //assert
        assertEquals(3, diffTypesActual.size());
        assertEquals("DelType_EXPOSE", diffTypeActual1.getChangeType());
        assertEquals("EXPOSE", diffTypeActual1.getInstruction().name());
        assertEquals("9000", diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("DelType_EXPOSE", diffTypeActual2.getChangeType());
        assertEquals("EXPOSE", diffTypeActual2.getInstruction().name());
        assertEquals("4200", diffTypeActual2.getBefore());
        assertEquals(null, diffTypeActual2.getAfter());
        assertEquals(null, diffTypeActual2.getExecutable());

        assertEquals("AddType_EXPOSE", diffTypeActual3.getChangeType());
        assertEquals("EXPOSE", diffTypeActual3.getInstruction().name());
        assertEquals(null, diffTypeActual3.getBefore());
        assertEquals("10", diffTypeActual3.getAfter());
        assertEquals(null, diffTypeActual3.getExecutable());

    }

    @Test
    public void diffOfExposes_AddType_EXPOSE() {
        //arrange
        Expose oldExpose = null;
        Expose newExpose = new Expose(null, "8080");

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfExposes(oldExpose, newExpose);

        //assert
        assertEquals("AddType_EXPOSE", diffTypeActual.getChangeType());
        assertEquals("EXPOSE", diffTypeActual.getInstruction().name());
        assertEquals(null, diffTypeActual.getBefore());
        assertEquals("8080", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());

    }

    @Test
    public void diffOfExposes_DelType_EXPOSE() {
        //arrange
        Expose oldExpose = new Expose(null, "8080");
        Expose newExpose = null;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfExposes(oldExpose, newExpose);

        //assert
        assertEquals("DelType_EXPOSE", diffTypeActual.getChangeType());
        assertEquals("EXPOSE", diffTypeActual.getInstruction().name());
        assertEquals("8080", diffTypeActual.getBefore());
        assertEquals(null, diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfExposes_UpdateType_EXPOSE_PORT() {
        //arrange
        Expose oldExpose = new Expose(null, "80");
        Expose newExpose = new Expose(null, "81");
        ;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfExposes(oldExpose, newExpose);

        //assert
        assertEquals("UpdateType_PORT", diffTypeActual.getChangeType());
        assertEquals("EXPOSE", diffTypeActual.getInstruction().name());
        assertEquals("80", diffTypeActual.getBefore());
        assertEquals("81", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    @Test
    public void getDiffOfMultipleArgs() {
        //arrange
        Arg argA1 = new Arg(null, "A");
        Arg argA2 = new Arg(null, "B");
        Arg argA3 = new Arg(null, "C");
        List<Arg> oldArgs = Arrays.asList(argA1, argA2, argA3);

        Arg argB1 = new Arg(null, "A");
        Arg argB2 = new Arg(null, "X");
        List<Arg> newArgs = Arrays.asList(argB1, argB2);


        //act
        List<DiffType> diffTypesActual = DiffProcessor.getDiffOfMultipleArgs(oldArgs, newArgs);
        DiffType diffTypeActual1 = diffTypesActual.get(0);
        DiffType diffTypeActual2 = diffTypesActual.get(1);
        DiffType diffTypeActual3 = diffTypesActual.get(2);

        //assert
        assertEquals(3, diffTypesActual.size());
        assertEquals("DelType_ARG", diffTypeActual1.getChangeType());
        assertEquals("ARG", diffTypeActual1.getInstruction().name());
        assertEquals("B", diffTypeActual1.getBefore());
        assertEquals(null, diffTypeActual1.getAfter());
        assertEquals(null, diffTypeActual1.getExecutable());

        assertEquals("DelType_ARG", diffTypeActual2.getChangeType());
        assertEquals("ARG", diffTypeActual2.getInstruction().name());
        assertEquals("C", diffTypeActual2.getBefore());
        assertEquals(null, diffTypeActual2.getAfter());
        assertEquals(null, diffTypeActual2.getExecutable());

        assertEquals("AddType_ARG", diffTypeActual3.getChangeType());
        assertEquals("ARG", diffTypeActual3.getInstruction().name());
        assertEquals(null, diffTypeActual3.getBefore());
        assertEquals("X", diffTypeActual3.getAfter());
        assertEquals(null, diffTypeActual3.getExecutable());

    }

    @Test
    public void diffOfArgs_AddType_ARG() {
        //arrange
        Arg oldArg = null;
        Arg newArg = new Arg(null, "B");

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfArgs(oldArg, newArg);

        //assert
        assertEquals("AddType_ARG", diffTypeActual.getChangeType());
        assertEquals("ARG", diffTypeActual.getInstruction().name());
        assertEquals(null, diffTypeActual.getBefore());
        assertEquals("B", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());

    }

    @Test
    public void diffOfArgs_DelType_ARG() {
        //arrange
        Arg oldArg = new Arg(null, "A");
        Arg newArg = null;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfArgs(oldArg, newArg);

        //assert
        assertEquals("DelType_ARG", diffTypeActual.getChangeType());
        assertEquals("ARG", diffTypeActual.getInstruction().name());
        assertEquals("A", diffTypeActual.getBefore());
        assertEquals(null, diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    @Test
    public void diffOfArgs_UpdateType_ARG() {
        //arrange
        Arg oldArg = new Arg(null, "A");
        Arg newArg = new Arg(null, "B");
        ;

        //act
        DiffType diffTypeActual = DiffProcessor.getDiffOfArgs(oldArg, newArg);

        //assert
        assertEquals("UpdateType_ARG", diffTypeActual.getChangeType());
        assertEquals("ARG", diffTypeActual.getInstruction().name());
        assertEquals("A", diffTypeActual.getBefore());
        assertEquals("B", diffTypeActual.getAfter());
        assertEquals(null, diffTypeActual.getExecutable());
    }

    public File createWriteAndGetFile(List<String> linesToWrite, String nameOfFile) throws IOException {
        List<String> lines = linesToWrite;
        String fullPathToFile = "Snapshots/repo1/" + nameOfFile;
        Path file = Paths.get(fullPathToFile);
        Files.write(file, lines, Charset.forName("UTF-8"));

        return new File(fullPathToFile);

    }


}
