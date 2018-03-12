package ch.dfa.dfa_tool;

import ch.dfa.dfa_tool.models.Snapshot;
import ch.dfa.dfa_tool.models.commands.*;
import ch.dfa.dfa_tool.services.DockerParser;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DockerParserTest {
    public static DockerParser dockerParserToTest;

    @BeforeClass
    public static void setup() throws ParseException, IOException {
        //arrange
        dockerParserToTest = new DockerParser("", "");
    }

    @Test
    public void getSnapshotObjectFromRawSnapshot() throws ParseException, IOException {
        //arrange
        File rawDockerfile1 = new File("Dockerfiles/Dockerfile1");
        DockerParser dockerParser1 = new DockerParser("Dockerfiles", "Dockerfile1");
        File rawDockerfile2 = new File("Dockerfiles/Dockerfile2");
        DockerParser dockerParser2 = new DockerParser("Dockerfiles", "Dockerfile2");


        //act
        Snapshot dockerfileSnapshot1 = dockerParser1.getParsedDockerfileObject(rawDockerfile1);
        Snapshot dockerfileSnapshot2 = dockerParser2.getParsedDockerfileObject(rawDockerfile2);


        //assert dockerfileSnapshot1
        assertTrue(rawDockerfile1.exists());
        assertEquals(dockerfileSnapshot1.getFrom().getImagename(), "ubuntu");
        assertEquals(dockerfileSnapshot1.getFrom().getImageVersionNumber(), 14.04, 0);
        assertEquals(dockerfileSnapshot1.getFrom().getFullName(), "ubuntu:14.04");
        assertEquals(dockerfileSnapshot1.getEnvs().size(), 5);
        assertEquals(dockerfileSnapshot1.getEnvs().get(0).getKey(), "JAVA_HOME");
        assertEquals(dockerfileSnapshot1.getEnvs().get(0).getValue(), "/usr/lib/jvm/java-7-openjdk-amd64");
        assertEquals(dockerfileSnapshot1.getEnvs().get(1).getKey(), "HADOOP_VERSION");
        assertEquals(dockerfileSnapshot1.getEnvs().get(1).getValue(), "2.6.0");
        assertEquals(dockerfileSnapshot1.getEnvs().get(2).getKey(), "HADOOP_HOME");
        assertEquals(dockerfileSnapshot1.getEnvs().get(2).getValue(), "/usr/local/hadoop");
        assertEquals(dockerfileSnapshot1.getEnvs().get(3).getKey(), "HADOOP_OPTS");
        assertEquals(dockerfileSnapshot1.getEnvs().get(3).getValue(), "-Djava.library.path=/usr/local/hadoop/lib/native");
        assertEquals(dockerfileSnapshot1.getEnvs().get(4).getKey(), "PATH");
        assertEquals(dockerfileSnapshot1.getEnvs().get(4).getValue(), "$PATH:$HADOOP_HOME/bin:$HADOOP_HOME/sbin");
        assertEquals(dockerfileSnapshot1.getRuns().size(), 3);
        assertEquals(dockerfileSnapshot1.getRuns().get(0).getExecutable(), "apt-get");
        assertEquals(dockerfileSnapshot1.getRuns().get(0).getParams().get(0), "update");
        assertEquals(dockerfileSnapshot1.getRuns().get(1).getExecutable(), "DEBIAN_FRONTEND=noninteractive");
        assertEquals(dockerfileSnapshot1.getRuns().get(1).getParams().get(0), "apt-get");
        assertEquals(dockerfileSnapshot1.getRuns().get(2).getExecutable(), "rm");
        assertEquals(dockerfileSnapshot1.getRuns().get(2).getParams().get(0), "-rf");
        assertEquals(dockerfileSnapshot1.getCopies().get(0).getSource(), "conf");
        assertEquals(dockerfileSnapshot1.getCopies().get(0).getDestination(), "$HADOOP_HOME/etc/hadoop/");
        assertEquals(dockerfileSnapshot1.getVolumes().get(0).getValue(), "/data");
        assertEquals(dockerfileSnapshot1.getExposes().get(0).getPort(), 9000);
        assertEquals(dockerfileSnapshot1.getExposes().get(1).getPort(), 50070);
        assertEquals(dockerfileSnapshot1.getExposes().get(2).getPort(), 50010);
        assertEquals(dockerfileSnapshot1.getExposes().get(3).getPort(), 50020);
        assertEquals(dockerfileSnapshot1.getExposes().get(4).getPort(), 50075);
        assertEquals(dockerfileSnapshot1.getExposes().get(5).getPort(), 50090);

        //assert dockerfileSnapshot2
        assertTrue(rawDockerfile2.exists());
        assertEquals(dockerfileSnapshot2.getFrom().getImagename(), "python");
        assertEquals(dockerfileSnapshot2.getFrom().getImageVersionNumber(), 2.7, 0);
        assertEquals(dockerfileSnapshot2.getFrom().getFullName(), "python:2.7");
        assertEquals(dockerfileSnapshot2.getEnvs().size(), 1);
        assertEquals(dockerfileSnapshot2.getEnvs().get(0).getKey(), "PYTHONUNBUFFERED");
        assertEquals(dockerfileSnapshot2.getEnvs().get(0).getValue(), "1");
        assertEquals(dockerfileSnapshot2.getRuns().size(), 10);
        assertEquals(dockerfileSnapshot2.getRuns().get(0).getExecutable(), "mkdir");
        assertEquals(dockerfileSnapshot2.getRuns().get(0).getParams().get(0), "/app");
        assertEquals(dockerfileSnapshot2.getRuns().get(1).getExecutable(), "apt-get");
        assertEquals(dockerfileSnapshot2.getRuns().get(1).getParams().get(0), "clean");
        assertEquals(dockerfileSnapshot2.getRuns().get(2).getExecutable(), "apt-get");
        assertEquals(dockerfileSnapshot2.getRuns().get(2).getParams().get(0), "update");
        assertEquals(dockerfileSnapshot2.getRuns().get(2).getParams().get(1), "-y");
        assertEquals(dockerfileSnapshot2.getRuns().get(3).getExecutable(), "apt-get");
        assertEquals(dockerfileSnapshot2.getRuns().get(3).getParams().get(0), "install");
        assertEquals(dockerfileSnapshot2.getRuns().get(3).getParams().get(1), "npm");
        assertEquals(dockerfileSnapshot2.getRuns().get(3).getParams().get(2), "-y");
        assertEquals(dockerfileSnapshot2.getRuns().get(4).getExecutable(), "npm");
        assertEquals(dockerfileSnapshot2.getRuns().get(4).getParams().get(0), "install");
        assertEquals(dockerfileSnapshot2.getRuns().get(4).getParams().get(1), "-g");
        assertEquals(dockerfileSnapshot2.getRuns().get(4).getParams().get(2), "bower");
        assertEquals(dockerfileSnapshot2.getRuns().get(5).getExecutable(), "npm");
        assertEquals(dockerfileSnapshot2.getRuns().get(5).getParams().get(0), "install");
        assertEquals(dockerfileSnapshot2.getRuns().get(5).getParams().get(1), "-g");
        assertEquals(dockerfileSnapshot2.getRuns().get(5).getParams().get(2), "less");
        assertEquals(dockerfileSnapshot2.getRuns().get(6).getExecutable(), "ln");
        assertEquals(dockerfileSnapshot2.getRuns().get(6).getParams().get(0), "-s");
        assertEquals(dockerfileSnapshot2.getRuns().get(6).getParams().get(1), "/usr/bin/nodejs");
        assertEquals(dockerfileSnapshot2.getRuns().get(6).getParams().get(2), "/usr/bin/node");
        assertEquals(dockerfileSnapshot2.getRuns().get(7).getExecutable(), "pip");
        assertEquals(dockerfileSnapshot2.getRuns().get(7).getParams().get(0), "install");
        assertEquals(dockerfileSnapshot2.getRuns().get(7).getParams().get(1), "-r");
        assertEquals(dockerfileSnapshot2.getRuns().get(7).getParams().get(2), "requirements.txt");
        assertEquals(dockerfileSnapshot2.getRuns().get(8).getExecutable(), "bower");
        assertEquals(dockerfileSnapshot2.getRuns().get(8).getParams().get(0), "install");
        assertEquals(dockerfileSnapshot2.getRuns().get(8).getParams().get(1), "--allow-root");
        assertEquals(dockerfileSnapshot2.getRuns().get(9).getExecutable(), "python");
        assertEquals(dockerfileSnapshot2.getRuns().get(9).getParams().get(0), "manage.py");
        assertEquals(dockerfileSnapshot2.getRuns().get(9).getParams().get(1), "collectstatic");
        assertEquals(dockerfileSnapshot2.getRuns().get(9).getParams().get(2), "--noinput");
        assertEquals(dockerfileSnapshot2.getWorkDirs().get(0).getPath(), "/app");
        assertEquals(dockerfileSnapshot2.getAdds().get(0).getSource(), "requirements.txt");
        assertEquals(dockerfileSnapshot2.getAdds().get(0).getDestination(), "/app/");
        assertEquals(dockerfileSnapshot2.getAdds().get(1).getSource(), "bower.json");
        assertEquals(dockerfileSnapshot2.getAdds().get(1).getDestination(), "/app/");
        assertEquals(dockerfileSnapshot2.getAdds().get(2).getSource(), ".bowerrc");
        assertEquals(dockerfileSnapshot2.getAdds().get(2).getDestination(), "/app/");
        assertEquals(dockerfileSnapshot2.getAdds().get(3).getSource(), ".");
        assertEquals(dockerfileSnapshot2.getAdds().get(3).getDestination(), "/app/");
    }

    @Test
    public void parseFromInstruction() {

        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String fromLineExample1 = "python";
        String fromLineExample2 = "python:1.0";
        String fromLineExample3 = "python@latest";
        From from1 = new From(null, "python");
        From from2 = new From(null, "python", 1.0);
        From from3 = new From(null, "python", "latest", "imageVersion");

        //act
        From fromExample1 = (From) dockerParser.parseFromInstruction(fromLineExample1);
        From fromExample2 = (From) dockerParser.parseFromInstruction(fromLineExample2);
        From fromExample3 = (From) dockerParser.parseFromInstruction(fromLineExample3);

        //assert
        assertEquals(fromExample1.getImagename(), from1.getImagename());
        assertEquals(fromExample1.getFullName(), from1.getFullName());
        assertEquals(fromExample2.getFullName(), from2.getFullName());
        assertEquals(fromExample2.getImageVersionNumber(), from2.getImageVersionNumber(), 0);
        assertEquals(fromExample3.getFullName(), from3.getFullName());
        assertEquals(fromExample3.getImageVersionString(), from3.getImageVersionString());
    }

    @Test
    public void parseEnvInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String envLineExample1 = "test 211";
        String envLineExample2 = "test";
        Env env1 = new Env(null, "test", "211");
        Env env2 = new Env(null, "test", "");

        //act
        Env envExample1 = (Env) dockerParser.parseAndGetEnvInstruction(envLineExample1);
        Env envExample2 = (Env) dockerParser.parseAndGetEnvInstruction(envLineExample2);

        //assert
        assertEquals(envExample1.getKey(), env1.getKey());
        assertEquals(envExample1.getValue(), env1.getValue());
        assertEquals(envExample2.getKey(), env2.getKey());
        assertEquals(envExample2.getValue(), env2.getValue());
    }

    @Test
    public void parseMaintainerInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String envLineExample1 = "Sali";
        Maintainer maintainer1 = new Maintainer(null, "Sali");

        //act
        Maintainer maintainerExample1 = (Maintainer) dockerParser.parseAndGetMaintainerInstruction(envLineExample1);

        //assert
        assertEquals(maintainerExample1.getMaintainername(), maintainer1.getMaintainername());
    }

    @Test
    public void parseRunInstructions() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String runLineExample1 = "git clone dockolution";
        String runLineExample2 = "git clone dockolution && python run script.py";
        String runLineExample3 = "[\"git\",\"clone\",\"dockolution\"]";

        String runList1Exec = "git";
        List<String> runList1Params = Arrays.asList("clone", "dockolution");
        List<Run> runList1 = Arrays.asList(new Run(null, runList1Exec, runList1Params));

        String runList2Exec1 = "git";
        String runList2Exec2 = "python";
        List<String> runList2Params1 = Arrays.asList("clone", "dockolution");
        List<String> runList2Params2 = Arrays.asList("run", "script.py");
        List<Run> runList2 = Arrays.asList(
                new Run(null, runList2Exec1, runList2Params1),
                new Run(null, runList2Exec2, runList2Params2));

        String runList3Exec = "git";
        List<String> runList3Params = Arrays.asList("clone", "dockolution");
        List<Run> runList3 = Arrays.asList(new Run(null, runList3Exec, runList3Params));

        //act
        List<Run> runListActual1 = dockerParser.parseAndGetRunInstruction(runLineExample1);
        List<Run> runListActual2 = dockerParser.parseAndGetRunInstruction(runLineExample2);
        List<Run> runListActual3 = dockerParser.parseAndGetRunInstruction(runLineExample3);

        //assert
        assertEquals(runList1.size(), runListActual1.size());

        int index1 = 0;
        for (Run run : runList1) {
            assertEquals(run.getExecutable(), runListActual1.get(index1).getExecutable());
            Assert.assertArrayEquals(run.getParams().toArray(), runListActual1.get(index1).getParams().toArray());
            assertEquals(run.getAllParams(), runListActual1.get(index1++).getAllParams());
        }

        int index2 = 0;
        for (Run run : runList2) {
            assertEquals(run.getExecutable(), runListActual2.get(index2).getExecutable());
            Assert.assertArrayEquals(run.getParams().toArray(), runListActual2.get(index2).getParams().toArray());
            assertEquals(run.getAllParams(), runListActual2.get(index2++).getAllParams());
        }

        int index3 = 0;
        for (Run run : runList3) {
            assertEquals(run.getExecutable(), runListActual3.get(index3).getExecutable());
            Assert.assertArrayEquals(run.getParams().toArray(), runListActual3.get(index3).getParams().toArray());
            assertEquals(run.getAllParams(), runListActual3.get(index3++).getAllParams());
        }
    }

    @Test
    public void parseCMDInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String cmdLineExample1 = "[\"executable\",\"param1\",\"param2\"]";
        String cmdLineExample2 = "command param1 param2";
        List<String> paramsExample = Arrays.asList("param1", "param2");
        Cmd cmd1 = new Cmd(null, "executable", paramsExample);
        Cmd cmd2 = new Cmd(null, "command", paramsExample);

        //act
        Cmd cmdActual1 = (Cmd) dockerParser.parseAndGetCMDInstruction(cmdLineExample1);
        Cmd cmdActual2 = (Cmd) dockerParser.parseAndGetCMDInstruction(cmdLineExample2);

        //assert
        assertEquals(cmd1.getExecutable(), cmdActual1.getExecutable());
        Assert.assertArrayEquals(cmd1.getParams().toArray(), cmdActual1.getParams().toArray());

        assertEquals(cmd2.getExecutable(), cmdActual2.getExecutable());
        Assert.assertArrayEquals(cmd2.getParams().toArray(), cmd2.getParams().toArray());

    }

    @Test
    public void parseAddInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String addLineExample1 = "newyork tokio";
        String addLineExample2 = "[\"berlin\",\"zurich\"]";


        Add add1 = new Add(null, "newyork", "tokio");
        Add add2 = new Add(null, "berlin", "zurich");

        //act
        Add addActual1 = (Add) dockerParser.parseAndGetAddInstruction(addLineExample1);
        Add addActual2 = (Add) dockerParser.parseAndGetAddInstruction(addLineExample2);

        //assert
        assertEquals(add1.getSource(), addActual1.getSource());
        assertEquals(add1.getDestination(), addActual1.getDestination());


        assertEquals(add2.getSource(), addActual2.getSource());
        assertEquals(add2.getDestination(), addActual2.getDestination());
    }

    @Test
    public void parseCopyInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String copyLineExample1 = "newyork tokio";
        String copyLineExample2 = "[\"berlin\",\"zurich\"]";


        Copy copy1 = new Copy(null, "newyork", "tokio");
        Copy copy2 = new Copy(null, "berlin", "zurich");

        //act
        Copy copyActual1 = (Copy) dockerParser.parseAndGetCopyInstruction(copyLineExample1);
        Copy copyActual2 = (Copy) dockerParser.parseAndGetCopyInstruction(copyLineExample2);

        //assert
        assertEquals(copy1.getSource(), copyActual1.getSource());
        assertEquals(copy1.getDestination(), copyActual1.getDestination());


        assertEquals(copy2.getSource(), copyActual2.getSource());
        assertEquals(copy2.getDestination(), copyActual2.getDestination());
    }

    @Test
    public void parseExposeInstructions() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String exposeLineExample1 = "8888";
        String exposeLineExample2 = "4200 8080 121";


        List<Expose> exposeList1 = Arrays.asList(new Expose(null, "8888"));

        List<Expose> exposeList2 = Arrays.asList(new Expose(null, "4200"), new Expose(null, "8080"), new Expose(null, "121"));

        //act
        List<Expose> exposeListActual1 = dockerParser.parseAndGetExposeInstruction(exposeLineExample1);
        List<Expose> exposeListActual2 = dockerParser.parseAndGetExposeInstruction(exposeLineExample2);

        //assert
        assertEquals(exposeList1.size(), exposeListActual1.size());
        assertEquals(exposeList1.get(0).getPort(), exposeListActual1.get(0).getPort());

        assertEquals(exposeList2.size(), exposeListActual2.size());
        int index = 0;
        for (Expose expose : exposeList2) {
            assertEquals(expose.getPort(), exposeListActual2.get(index++).getPort());
        }
    }

    @Test
    public void parseLabelInstructions() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String labelLineExample1 = "in=der";
        String labelLineExample2 = "hey=ho ba=bo hi=hi";


        List<Label> labelList1 = Arrays.asList(new Label(null, "in", "der"));

        List<Label> labelList2 = Arrays.asList(
                new Label(null, "hey", "ho"),
                new Label(null, "ba", "bo"),
                new Label(null, "hi", "hi"));

        //act
        List<Label> labelListActual1 = dockerParser.parseAndGetLabelInstruction(labelLineExample1);
        List<Label> labelListActual2 = dockerParser.parseAndGetLabelInstruction(labelLineExample2);

        //assert
        assertEquals(labelList1.size(), labelListActual1.size());
        assertEquals(labelList1.get(0).getKey(), labelListActual1.get(0).getKey());
        assertEquals(labelList1.get(0).getValue(), labelListActual1.get(0).getValue());
        assertEquals(labelList1.get(0).getKeyValue(), labelListActual1.get(0).getKeyValue());

        assertEquals(labelList2.size(), labelListActual2.size());
        int index = 0;
        for (Label label : labelList2) {
            assertEquals(label.getKey(), labelListActual2.get(index).getKey());
            assertEquals(label.getValue(), labelListActual2.get(index).getValue());
            assertEquals(label.getKeyValue(), labelListActual2.get(index++).getKeyValue());
        }
    }

    @Test
    public void parseEntryPointInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String entrypointLineExample1 = "[\"executable\",\"param1\",\"param2\"]";
        String entrypointLineExample2 = "command param1 param2";
        List<String> paramsExample = Arrays.asList("param1", "param2");
        EntryPoint entrypoint1 = new EntryPoint(null, "executable", paramsExample);
        EntryPoint entrypoint2 = new EntryPoint(null, "command", paramsExample);

        //act
        EntryPoint entrypointActual1 = (EntryPoint) dockerParser.parseAndGetEntryPointInstruction(entrypointLineExample1);
        EntryPoint entrypointActual2 = (EntryPoint) dockerParser.parseAndGetEntryPointInstruction(entrypointLineExample2);

        //assert
        assertEquals(entrypoint1.getExecutable(), entrypointActual1.getExecutable());
        Assert.assertArrayEquals(entrypoint1.getParams().toArray(), entrypointActual1.getParams().toArray());
        assertEquals(entrypoint2.getExecutable(), entrypointActual2.getExecutable());
        Assert.assertArrayEquals(entrypoint2.getParams().toArray(), entrypoint2.getParams().toArray());
    }

    @Test
    public void parseVolumeInstructions() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String volumeLineExample1 = "/data";
        String volumeLineExample2 = "[\"/data\",\"/tests\"]";


        List<Volume> volumeList1 = Arrays.asList(new Volume(null, "/data"));

        List<Volume> volumeList2 = Arrays.asList(
                new Volume(null, "/data"),
                new Volume(null, "/tests"));

        //act
        List<Volume> volumeListActual1 = dockerParser.parseAndGetVolumeInstruction(volumeLineExample1);
        List<Volume> volumeListActual2 = dockerParser.parseAndGetVolumeInstruction(volumeLineExample2);

        //assert
        assertEquals(volumeList1.size(), volumeListActual1.size());
        assertEquals(volumeList1.get(0).getValue(), volumeListActual1.get(0).getValue());

        assertEquals(volumeList2.size(), volumeListActual2.size());
        int index = 0;
        for (Volume volume : volumeList2) {
            assertEquals(volume.getValue(), volumeListActual2.get(index++).getValue());
        }
    }

    @Test
    public void parseWorkDirInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String workDirLineExample1 = "aha";


        WorkDir workDir1 = new WorkDir(null, workDirLineExample1);

        //act
        WorkDir workDirActual1 = (WorkDir) dockerParser.parseAndGetWorkDirInstruction(workDirLineExample1);

        //assert
        assertEquals(workDir1.getPath(), workDirActual1.getPath());
    }

    @Test
    public void parseArgInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String argLineExample1 = "aha=okay";


        Arg arg1 = new Arg(null, argLineExample1);

        //act
        Arg argActual1 = (Arg) dockerParser.parseAndGetArgInstruction(argLineExample1);

        //assert
        assertEquals(arg1.getArg(), argActual1.getArg());
    }

    @Test
    public void parseOnBuildInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String onBuildLineExample1 = "CMD exec param param";

        OnBuild onBuild = new OnBuild(null, "CMD", "exec param param");

        //act
        OnBuild onBuildActual = (OnBuild) dockerParser.parseAndGetOnBuildInstruction(onBuildLineExample1);

        //assert
        assertEquals(onBuild.getInstruction(), onBuildActual.getInstruction());
        assertEquals(onBuild.getAllParams(), onBuildActual.getAllParams());
    }

    @Test
    public void parseStopSignalInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String stopSignalLineExample = "hehe";
        StopSignal stopSignal = new StopSignal(null, "hehe");

        //act
        StopSignal stopSignalActual = (StopSignal) dockerParser.parseAndGetStopSignalInstruction(stopSignalLineExample);

        //assert
        assertEquals(stopSignal.getSignal(), stopSignalActual.getSignal());
    }

    @Test
    public void parseHealthCheckInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String healthcheckLineExample = "CMD curl -f http://localhost/";
        String healthcheckLineExampleWithOptions = "--interval=5m --timeout=3s CMD curl -f http://localhost/";

        Healthcheck healthcheck = new Healthcheck(null, "CMD", "curl -f http://localhost/");
        Healthcheck healthcheckWithOptions = new Healthcheck(null, "CMD", "--interval=5m --timeout=3s", "curl -f http://localhost/");

        //act
        Healthcheck healthcheckActual = (Healthcheck) dockerParser.parseAndGetHealthCheckInstruction(healthcheckLineExample);
        Healthcheck healthcheckWithOptionsActual = (Healthcheck) dockerParser.parseAndGetHealthCheckInstruction(healthcheckLineExampleWithOptions);

        //assert
        assertEquals(healthcheck.getInstruction(), healthcheckActual.getInstruction());
        assertEquals(healthcheck.getAllParams(), healthcheckActual.getAllParams());
        assertTrue(healthcheck.getOptionsBeforeInstructions() == null);

        assertEquals(healthcheckWithOptions.getInstruction(), healthcheckWithOptionsActual.getInstruction());
        assertEquals(healthcheckWithOptions.getAllParams(), healthcheckWithOptionsActual.getAllParams());
        assertEquals(healthcheckWithOptions.getOptionsBeforeInstructions(), healthcheckWithOptionsActual.getOptionsBeforeInstructions());
    }

    @Test
    public void parseShellInstruction() {
        //TODO: write shell parser and corresponding test
    }

    @Test
    public void parseUserInstruction() {
        //arrange
        DockerParser dockerParser = dockerParserToTest;
        String userLineExample = "szumbe";


        User user = new User(null, userLineExample);

        //act
        User userActual1 = (User) dockerParser.parseAndGetUserInstruction(userLineExample);

        //assert
        assertEquals(user.getUsername(), userActual1.getUsername());
    }

}
