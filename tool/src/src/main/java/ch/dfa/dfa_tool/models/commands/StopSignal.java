package ch.dfa.dfa_tool.models.commands;

import ch.dfa.dfa_tool.models.Snapshot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by salizumberi-laptop on 01.11.2016.
 */
@Entity
@Getter
@Setter
@Table(name = "df_stopsignal")
public class StopSignal extends Instruction{
/*    @Id
    @Column(name="REPO_ID", unique=true, nullable=false)
    public long id;*/

    @Id
    @Column(name = "SNAP_ID", unique = true, nullable = false)
    @GeneratedValue(generator = "stopsignal_gen")
    @GenericGenerator(name = "stopsignal_gen", strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "snapshot"))
    private long id;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    Snapshot snapshot;

    @Column(name = "signal")
    public String signal;

    @Column(name = "current", nullable = false)
    public boolean current;

    public StopSignal(Snapshot snapshot, String signal) {
        super();
        this.snapshot = snapshot;
        this.signal = signal;
    }


    public StopSignal() {
    }
}
