package ch.dfa.dfa_tool.models;


import ch.dfa.dfa_tool.services.DateExtractor;
import ch.dfa.dfa_tool.services.GitHubMinerService;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static java.lang.Math.toIntExact;

@Entity
@Getter
@Setter
public class Project {
    private final static String GITAPI = "https://api.github.com/";
    private final static String REPOS = "repos/";
    public final String gitUrl;

    @Id
    @GeneratedValue(generator = "SEC_GEN_Project", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_Project", sequenceName = "SEC_Project", allocationSize = 1)
    @Column(name = "PROJECT_ID", unique = true, nullable = false)
    private long id;

    @Transient
    private String localRepoPath;

    @Column(name = "repo_path", nullable = false, length = 1024)
    private String repositoryPath;

    @Column(name = "i_owner_type", nullable = false)
    private String ownerType;

    @Column(name = "i_network_count", nullable = false)
    private int networkCount;

    @Column(name = "i_open_issues", nullable = false)
    private int opneIssues;

    @Column(name = "i_forks", nullable = false)
    private int forks;

    @Column(name = "i_watchers", nullable = false)
    private int watchers;

    @Column(name = "i_stargazers", nullable = false)
    private int stargazers;

    @Column(name = "i_subscribers", nullable = false)
    private int subscribers;

    @Column(name = "i_size", nullable = false)
    private int size;

    @Column(name = "created_at", nullable = false)
    private long firstCommitDate;

    @Column(name = "REPO_ID", nullable = false, length = 1024)
    private long repo_id;

    @Column(name = "git_url", nullable = false, length = 1024)
    private String dotGitUrl;

    @Transient
    private boolean isForked;


    //@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "project", orphanRemoval = true)
    //private List<Dockerfile> dockerfiles = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Dockerfile> dockerfiles;


    public Project(){
        this.gitUrl = "http://github.com/repo.git";
        this.repositoryPath = extractRepositoryName("repo/repo");

    }

    public Project(String gitUrl) throws ParseException {
        this.gitUrl = gitUrl;
        this.repositoryPath = extractRepositoryName(gitUrl);
        GitHubAPIMetaData gitHubAPIMetaData = this.fetchGitHubAPIMetaData();
        mapGitHubAPIMetaDataToProject(gitHubAPIMetaData, this);
    }


    private GitHubAPIMetaData fetchGitHubAPIMetaData() throws ParseException {
        return GitHubMinerService.getGitHubRepository(GITAPI + REPOS + this.repositoryPath);
    }

    private String extractRepositoryName(String gitUrl) {
        String gitUrl1 = gitUrl.replaceAll("https://github.com/", "");
        String gitUrl2 = gitUrl1.replaceAll(".git", "");
        return gitUrl2;
    }

    private void mapGitHubAPIMetaDataToProject(GitHubAPIMetaData gitHubAPIMetaData, Project project) throws ParseException {
        Date created_at = DateExtractor.getDateFromJsonString(gitHubAPIMetaData.created_at);
        project.setLocalRepoPath(gitHubAPIMetaData.name);
        project.setRepo_id(toIntExact(gitHubAPIMetaData.id));
        project.setFirstCommitDate(Long.valueOf(firstCommitDate));
        project.setForks(toIntExact(gitHubAPIMetaData.forks_count));
        project.setSize(toIntExact(gitHubAPIMetaData.size));
        project.setNetworkCount(toIntExact(gitHubAPIMetaData.network_count));
        project.setStargazers(toIntExact(gitHubAPIMetaData.stargazers_count));
        project.setOpneIssues(toIntExact(gitHubAPIMetaData.open_issues));
        project.setOwnerType(gitHubAPIMetaData.owner.type);
        project.setWatchers(toIntExact(gitHubAPIMetaData.watchers_count));
        project.setSubscribers(toIntExact(gitHubAPIMetaData.subscribers_count));
        project.setDotGitUrl(gitHubAPIMetaData.git_url);
        project.setForked(gitHubAPIMetaData.fork);
    }
}
