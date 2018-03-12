package ch.dfa.dfa_tool.models.commands;

import ch.dfa.dfa_tool.models.Snapshot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

;

/**
 * Created by salizumberi-laptop on 01.11.2016.
 */
@Entity
@Getter
@Setter
@Table(name = "df_entrypoint")
public class EntryPoint extends Instruction {

    @Id
    @Column(name = "SNAP_ID", unique = true, nullable = false)
    @GeneratedValue(generator = "entrypoint_gen")
    @GenericGenerator(name = "entrypoint_gen", strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "snapshot"))
    private long id;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    Snapshot snapshot;

    @Column(name = "executable")
    public String executable;

    @ElementCollection
    @CollectionTable(name = "entrypoints_params", joinColumns = @JoinColumn(name = "ENTRYPOINT_ID"))
    @Column(name = "entrypoints_params", length = 1024)
    public List<String> params;

    @Column(name = "current", nullable = false)
    public boolean current;

    @Column(name = "run_params", length = 1024)
    public String allParams;

    public EntryPoint(Snapshot snapshot, String executable, List<String> params) {
        super();
        this.snapshot = snapshot;
        this.executable = executable;
        this.params = params;

        String allParams = "";
        for (String p : params) {
            allParams += "¦" + p;
        }
        if (allParams.length() > 240) {
            this.allParams = allParams.substring(0, 240) + "...";
        } else {
            this.allParams = allParams;
        }
    }

    public EntryPoint() {
    }
}
