package ch.dfa.dfa_tool.models.commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ch.dfa.dfa_tool.models.Snapshot;import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by salizumberi-laptop on 01.11.2016.
 */
@Entity
@Getter
@Setter
@Table(name = "df_cmd")
public class Cmd extends Instruction{
/*    @Id
    @Column(name="REPO_ID", unique=true, nullable=false)
    public long id;*/

    @Id
    @Column(name = "SNAP_ID", unique = true, nullable = false)
    @GeneratedValue(generator = "cmd_gen")
    @GenericGenerator(name = "cmd_gen", strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "snapshot"))
    private long id;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public Snapshot snapshot;

    @Column(name = "executable")
    public String executable;

    @ElementCollection
    @CollectionTable(name="cmd_params", joinColumns=@JoinColumn(name="RUN_ID"))
    @Column(name="cmd_params" , length = 1024)
    public List<String> params;

    @Column(name = "current", nullable = false)
    public boolean current;

    @Column(name = "run_params", length = 1024)
    public String allParams;

    public Cmd(Snapshot snapshot, String executable, List<String> params) {
        this.snapshot = snapshot;
        this.executable=executable;
        this.params=params;

        String allParams = "";
        for(String p: params){
            allParams += "¦"+ p;
        }

        if (allParams.length() > 240){
            this.allParams = allParams.substring(0, 240) + "...";
        }else{
            this.allParams = allParams;
        }
    }

    public Cmd() {
    }
}
