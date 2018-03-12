package ch.dfa.dfa_tool.models.commands;


import ch.dfa.dfa_tool.models.Snapshot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by salizumberi-laptop on 01.11.2016.
 */

@Entity
@Setter
@Getter
@Table(name = "df_volume")
public class Volume extends Instruction{
    @Id
    @GeneratedValue(generator = "SEC_GEN_VOLUME", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_VOLUME", sequenceName = "SEC_VOLUME",allocationSize=1)
    @Column(name="VOLUME_ID", unique=true, nullable=false)
    public long id;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="SNAP_ID")
    Snapshot snapshot;

    @Column(name = "value", length = 1024)
    public String value;

    @Column(name = "current", nullable = false)
    public boolean current;

    public Volume(Snapshot snapshot, String match) {
        super();
        this.snapshot = snapshot;
        this.value = match;
    }

    public Volume() {
    }
}
