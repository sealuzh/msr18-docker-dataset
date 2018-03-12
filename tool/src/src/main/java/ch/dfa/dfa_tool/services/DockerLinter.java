package ch.dfa.dfa_tool.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by salizumberi-laptop on 26.10.2016.
 */
public class DockerLinter {

    public final static String HADOLINT_EXEC = "docker run --rm -i lukasmartinelli/hadolint < ";
    public static String getReportOfLinting(File file) throws IOException, InterruptedException{
        String exec  = HADOLINT_EXEC +file.getAbsolutePath();
        ProcessBuilder processBuilder = null;

        processBuilder = new ProcessBuilder("cmd","/c",exec);

        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        StringBuilder processOutput = new StringBuilder();

        try (BufferedReader processOutputReader = new BufferedReader(
                new InputStreamReader(process.getInputStream())))
        {
            String readLine;
            while ((readLine = processOutputReader.readLine()) != null){
                processOutput.append(readLine + System.lineSeparator());
            }
            process.waitFor();
        }

        return processOutput.toString().trim();
    }
}
