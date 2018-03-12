package ch.dfa.dfa_tool;

import ch.dfa.dfa_tool.models.ChangedFile;
import ch.dfa.dfa_tool.models.Project;
import ch.dfa.dfa_tool.models.Snapshot;
import ch.dfa.dfa_tool.services.CommitProcessor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AppTest {
    public static App appToBeTested;
    public static List<File> rawDockerfilesToBeTested;
    public static String gitUrlToBeTested;
    public static File firstDockerFileToBeTested;
    public static List<RevCommit> revCommitsToBeTested;

    @BeforeClass
    public static void setup() throws Exception {
        gitUrlToBeTested = "https://github.com/probr/probr-core";
        appToBeTested = new App(gitUrlToBeTested);
        rawDockerfilesToBeTested  = appToBeTested.getDockerFilesFromGitRepository( appToBeTested.getRepoFolderName());
        firstDockerFileToBeTested = rawDockerfilesToBeTested.get(0);
        appToBeTested.setRelativePathToDockerfile(appToBeTested.getRelativePathToDockerFile(firstDockerFileToBeTested.getPath()));
        revCommitsToBeTested = appToBeTested.getRevCommitsOfRawDockerfile(appToBeTested.getRepoFolderNameDotGit(), appToBeTested.getRelativePathToDockerfile());

    }

    @Test
    public void getProjectMetaDataFromGITHUBAPI() throws ParseException {
        //arrange
        String project1GithubURL = gitUrlToBeTested;

        //act
        Project project1 = new Project(project1GithubURL);

        //assert
        assertEquals(project1.getRepo_id(),40481849);
        assertEquals(project1.getDotGitUrl(),"git://github.com/probr/probr-core.git");
    }

    @Test
    public void getRawDockerfilesFromProjectWithRepisitory() throws Exception {
        //arrange
        App app = appToBeTested;

        //act
        String projectFolderName = app.getProject().getRepositoryPath().replaceAll("/","_");
        app.setRepoFolderName(App.LOCAL_REPO_FOLDER + projectFolderName);
        List<File> rawDockerfiles = app.getDockerFilesFromGitRepository( app.getRepoFolderName());

        //assert
        assertEquals(rawDockerfiles.size(),3);
    }

    @Test
    public void getRevCommitsOfRawDockerfile() throws Exception {
        //arrange
        App app = appToBeTested;
        List<File> rawDockerfiles  = rawDockerfilesToBeTested;
        File rawDockerfile = rawDockerfiles.get(0);
        app.setRelativePathToDockerfile(app.getRelativePathToDockerFile(rawDockerfile.getPath()));

        //act
        List<RevCommit> revCommits = app.getRevCommitsOfRawDockerfile(app.getRepoFolderNameDotGit(), app.getRelativePathToDockerfile());

        //assert
        assertEquals(revCommits.size(),7);
    }

    @Test
    public void getSnapshotsFromRevCommits() throws Exception {
        //arrange
        App app = appToBeTested;
        List<RevCommit> revCommits = revCommitsToBeTested;

        //act
        List<Snapshot> snapshots = app.getSnapshotsFromRevCommits(revCommits);

        //assert
        assertEquals(snapshots.size(),7);

    }

    @Test
    public void getFullSnapshotFromRevCommit() throws Exception {
        //arrange
        App app = appToBeTested;
        List<RevCommit> revCommits = app.getRevCommitsOfRawDockerfile(app.getRepoFolderNameDotGit(), app.getRelativePathToDockerfile());

        //act
        Snapshot snapshots = app.getFullSnapshotFromRevCommit(revCommits.get(0),true,0,app.getRepository(),app.getGit());

        //assert
        assertEquals(snapshots.isCurrentDockerfile(),true);
        assertEquals(snapshots.getIndex(),0);

    }


    @Test
    public void getChangedFilesForCommit() throws ParseException, IOException, GitAPIException {
        //arrange
        App app = appToBeTested;
        RevCommit commit = revCommitsToBeTested.get(0);

        //act
        List<ChangedFile> changedFiles = app.getChangedFilesForCommit(commit);

        //assert
        assertEquals(changedFiles.size(),31);

    }

    @Test
    public void checkRangesOfChangedFiles() throws ParseException, IOException, GitAPIException {
        //arrange
        App app = appToBeTested;
        RevCommit commit = revCommitsToBeTested.get(0);
        CommitProcessor commitProcessor = new CommitProcessor();
        int rangeSize = 6;

        //act
        List<ChangedFile> changedFilesRangeN3 = commitProcessor.getChangedFilesWithinCommit(commit, app.getRepository(), rangeSize, -3, app.getGit(), app.getProject().getRepositoryPath());
        List<ChangedFile> changedFilesRangeN2 = commitProcessor.getChangedFilesWithinCommit(commit, app.getRepository(), rangeSize, -2, app.getGit(), app.getProject().getRepositoryPath());
        List<ChangedFile> changedFilesRangeN1 = commitProcessor.getChangedFilesWithinCommit(commit, app.getRepository(), rangeSize, -1, app.getGit(), app.getProject().getRepositoryPath());
        List<ChangedFile> changedFilesRangeN0 = commitProcessor.getChangedFilesWithinCommit(commit, app.getRepository(), rangeSize, -0, app.getGit(), app.getProject().getRepositoryPath());
        List<ChangedFile> changedFilesRangeP1 = commitProcessor.getChangedFilesWithinCommit(commit, app.getRepository(), rangeSize, 1, app.getGit(), app.getProject().getRepositoryPath());
        List<ChangedFile> changedFilesRangeP2 = commitProcessor.getChangedFilesWithinCommit(commit, app.getRepository(), rangeSize, 2, app.getGit(), app.getProject().getRepositoryPath());
        List<ChangedFile> changedFilesRangeP3 = commitProcessor.getChangedFilesWithinCommit(commit, app.getRepository(), rangeSize, 3, app.getGit(), app.getProject().getRepositoryPath());

        //assert
        assertEquals(changedFilesRangeN3.size(),16);
        assertEquals(changedFilesRangeN2.size(),1);
        assertEquals(changedFilesRangeN1.size(),4);
        assertEquals(changedFilesRangeN0.size(),4);
        assertEquals(changedFilesRangeP1.size(),1);
        assertEquals(changedFilesRangeP2.size(),3);
        assertEquals(changedFilesRangeP3.size(),2);

        assertEquals(changedFilesRangeN3.get(0).fullFileName,"Dockerfile");
        assertEquals(changedFilesRangeN2.get(0).fullFileName,"probr");
        assertEquals(changedFilesRangeN1.get(0).fullFileName,"deploy.sh");
        assertEquals(changedFilesRangeN0.get(0).fullFileName,".dockerignore");
        assertEquals(changedFilesRangeP1.get(0).fullFileName,"deploy.sh");
        assertEquals(changedFilesRangeP2.get(0).fullFileName,"handlers.py");
        assertEquals(changedFilesRangeP3.get(0).fullFileName,"handlers.py");
    }
}
