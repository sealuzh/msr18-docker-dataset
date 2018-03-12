package ch.dfa.dfa_tool.models;


import ch.dfa.dfa_tool.models.commands.*;
import ch.dfa.dfa_tool.services.DockerService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ImageSearchResult;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by salizumberi-laptop on 18.11.2016.
 */
@Entity
@Table(name = "snapshot")
@Getter
@Setter
public class Snapshot {

    @Id
    @GeneratedValue(generator = "SEC_GEN_SNAP", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_SNAP", sequenceName = "SEC_SNAP", allocationSize = 1)
    @Column(name = "SNAP_ID", unique = true, nullable = false)
    private long id;

    @Column(name = "REPO_ID", updatable = false, nullable = false)
    private Long repoId;

    public Dockerfile getDockerfile() {
        return dockerfile;
    }

    public void setDockerfile(Dockerfile dockerfile) {
        this.dockerfile = dockerfile;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "DOCK_ID")
    private Dockerfile dockerfile;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "snapshot", orphanRemoval = true)
    public List<ChangedFile> filesChangedWithinCommit = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<Run> runs = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<Label> labels = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<Env> envs = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<Expose> exposes = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<Add> adds = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<Copy> copies = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<Volume> volumes = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<User> users = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<WorkDir> workDirs = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<Arg> args = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<Comment> comments = new ArrayList<>();

    //TODO: repair instruction !!!
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "snapshot", orphanRemoval = true)
    public List<OnBuild> onBuilds = new ArrayList<>();

    //TODO: repair instruction !!!
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "snapshot", orphanRemoval = true)
    @PrimaryKeyJoinColumn
    public Healthcheck healthCheck;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    public EntryPoint entryPoint;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    public From from;


    public void setNewAndOldDiff(Diff oldDiff, Diff newDiff) {
        if (oldDiff != null) {
            this.diffs.add(oldDiff);
        }
        if (newDiff != null) {
            this.diffs.add(newDiff);
        }
    }

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "SNAP_DIFF",
            joinColumns = {@JoinColumn(name = "SNAP_ID")},
            inverseJoinColumns = {@JoinColumn(name = "DIFF_ID")})
    private List<Diff> diffs = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "snapshot", orphanRemoval = true)
    @PrimaryKeyJoinColumn
    public Cmd cmd;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "snapshot", orphanRemoval = true)
    @PrimaryKeyJoinColumn
    public StopSignal stopSignals;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "snapshot", orphanRemoval = true)
    @PrimaryKeyJoinColumn
    public Maintainer maintainer;

    @Column(name = "instructions", nullable = false)
    private int instructions;

    @Column(name = "commit_date", nullable = false)
    private long commitDate;

    @Column(name = "from_date", nullable = false)
    private long fromDate;

    public Long getRepoId() {
        return repoId;
    }

    public void setRepoId(Long repoId) {
        this.repoId = repoId;
    }

    @Column(name = "to_date", nullable = false)

    private long toDate;

    // TODO: assign those values
    @Column(name = "change_type")
    private String changeType;

    // TODO: assign those values
    @Column(name = "del")
    private int del;

    // TODO: assign those values
    @Column(name = "ins")
    private int ins;

    @Column(name = "image_is_automated")
    private Boolean imageIsAutomated;

    @Column(name = "image_is_offical")
    private Boolean imageIsOffical;

    @Column(name = "star_count")
    private Integer starCount;

    @Column(name = "commit_index", nullable = false)
    private int index;

    @Column(name = "current", nullable = false)
    private boolean isCurrentDockerfile;

    public void setNewDiff(Diff newDiff) {
        if (newDiff != null) {
            this.diffs.add(newDiff);
        }
    }


    public void setOldDiff(Diff oldDiff) {
        if (oldDiff != null) {
            this.diffs.add(oldDiff);
        }
    }

    public int countInstructions() {
        int counter = 1;
        if (maintainer != null) {
            counter++;
        }
        if (cmd != null) {
            counter++;
        }
        if (entryPoint != null) {
            counter++;
        }
        if (stopSignals != null) {
            counter++;
        }
        if (healthCheck != null) {
            counter++;
        }
        return runs.size() +
                labels.size() +
                envs.size() +
                exposes.size() +
                adds.size() +
                copies.size() +
                volumes.size() +
                users.size() +
                workDirs.size() +
                args.size() +
                onBuilds.size() + counter;
    }

    public void extractCommit() {
        ImageSearchResult imageSearchResult = null;
        try {
            imageSearchResult = fetchImageInfos();
        } catch (Exception e) {
        }
        if (imageSearchResult != null) {
            this.imageIsAutomated = imageSearchResult.isAutomated();
            this.imageIsOffical = imageSearchResult.isOfficial();
            this.starCount = imageSearchResult.getStarCount();
        } else {
            this.imageIsAutomated = null;
            this.imageIsOffical = null;
            this.starCount = null;
        }

    }

    @JsonIgnore
    public ImageSearchResult fetchImageInfos() {
        ImageSearchResult imagesInfos = null;
        try {
            imagesInfos = DockerService.getImageInfos(from.imagename);
        } catch (DockerCertificateException e) {
        } catch (DockerException e) {
        } catch (InterruptedException e) {
        }
        return imagesInfos;
    }

    public Snapshot() {

    }
}
