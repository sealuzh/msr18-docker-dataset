package ch.dfa.dfa_tool.models;


import ch.dfa.dfa_tool.models.diff.DiffType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by salizumberi-laptop on 18.11.2016.
 */
@Entity
@Getter
@Setter
@Table(name = "diff")
public class Diff implements Serializable {
    @Id
    @GeneratedValue(generator = "SEC_GEN_DIFF", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_DIFF", sequenceName = "SEC_DIFF",allocationSize=1)
    @Column(name="DIFF_ID", unique=true, nullable=false)
    public long id;

    @JsonIgnore
    @ManyToMany(cascade=CascadeType.ALL, mappedBy="diffs")
    private Set<Snapshot> snapshots = new HashSet<Snapshot>();

    @Column(name = "diff_state", nullable = false)
    private String diffState;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "diff", orphanRemoval = true)
    @Fetch(value = FetchMode.SUBSELECT)
    public List<DiffType> diffs = new ArrayList<>();

    @Column(name = "commit_date", nullable = false)
    private long commitDate;

    @Column(name = "del", nullable = false)
    private int del;

    @Column(name = "ins", nullable = false)
    private int ins;

    public int getMod() {
        return mod;
    }

    public void setMod(int mod) {
        this.mod = mod;
    }

    @Column(name = "mod", nullable = false)
    private int mod;


    public long getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(long commitDate) {
        this.commitDate = commitDate;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    public int getIns() {
        return ins;
    }

    public void setIns(int ins) {
        this.ins = ins;
    }

    public String getDiffState() {
        return diffState;
    }

    public void setDiffState(String diffState) {
        this.diffState = diffState;
    }


    public void setSnapshots(Snapshot oldSnapShot, Snapshot newSnapshot) {
        if(oldSnapShot != null){
            snapshots.add(oldSnapShot);
        }
        if(newSnapshot != null){
            snapshots.add(newSnapshot);
        }
    }

    public Diff() {

    }
}
