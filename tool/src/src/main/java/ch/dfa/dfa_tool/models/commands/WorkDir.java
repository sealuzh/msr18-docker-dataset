package ch.dfa.dfa_tool.models.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ch.dfa.dfa_tool.models.Snapshot;

import javax.persistence.*;

/**
 * Created by salizumberi-laptop on 01.11.2016.
 */
@Entity
@Getter
@Setter
@Table(name = "df_workdir")
public class WorkDir extends Instruction{
    @Id
    @GeneratedValue(generator = "SEC_GEN_WORKDIR", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_WORKDIR", sequenceName = "SEC_WORKDIR",allocationSize=1)
    @Column(name="WORKDIR_ID", unique=true, nullable=false)
    public long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="SNAP_ID")
    Snapshot snapshot;

    @Column(name = "path", length = 1024)
    public String path;

    @Column(name = "current", nullable = false)
    public boolean current;

    public WorkDir(Snapshot snapshot, String path) {
        super();
        this.snapshot = snapshot;
        this.path= path;
    }

    public WorkDir() {
    }
}
