package ch.dfa.dfa_tool.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.eclipse.jgit.diff.DiffEntry;

import javax.persistence.*;

/**
 * Created by salizumberi-laptop on 21.11.2016.
 */
@Entity
@Table(name = "changed_files")
public class ChangedFile {

    @Id
    @GeneratedValue(generator = "SEC_GEN_CHANGEDFILE", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_CHANGEDFILE", sequenceName = "SEC_CHANGEDFILE",allocationSize=1)
    @Column(name="CHANGEDFILE_ID", unique=true, nullable=false)
    public long id;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="SNAP_ID")
    Snapshot snapshot;

    @Column(name = "path", length = 1024)
    public String filePath;

    @Column(name = "range_size")
    public int rangeSize;

    @Column(name = "range_index")
    public int rangeIndex;

    @Transient
    public String dockerPath;

    @Column(name = "file_name", length = 1024)
    public String fileName;

    @Column(name = "full_file_name", length = 1024)
    public String fullFileName;

    @Column(name = "file_type", length = 1024)
    public String fileType;

    @Column(name = "repoName", length = 1024)
    public String repoName;

    @Column(name = "commit", length = 1024)
    public String commitId;

    @Column(name = "changeType", length = 1024)
    public String changeType;

    @Column(name = "mode")
    public int mode;

    @Column(name = "deletions")
    public int deletions;

    @Column(name = "insertions")
    public int insertions;


    public ChangedFile(String path, String fullFileName, String fileName, String filePath, String fileType,
                       int mode, DiffEntry.ChangeType changeType, int deletions, int insertions, int rangeIndex, int rangeSize,
                       String commitId, String repo_name) {
        this.dockerPath = dockerPath;
        this.fullFileName = fullFileName;
        this.fileName = fileName;
        this.fileType = fileType;
        this.mode = mode;
        this.changeType = changeType.toString();
        this.deletions = deletions;
        this.insertions = insertions;
        this.rangeIndex = rangeIndex;
        this.rangeSize  =rangeSize;
        this.commitId = commitId;
        this.repoName = repo_name;


        if (filePath.length() > 240){
            this.filePath = filePath.substring(0, 240) + "...";
        }else{
            this.filePath = filePath;
        }

        if (fullFileName.length() > 240){
            this.fullFileName = fullFileName.substring(0, 240) + "...";
        }else{
            this.fullFileName = fullFileName;
        }
    }

    public ChangedFile() {
    }
}
