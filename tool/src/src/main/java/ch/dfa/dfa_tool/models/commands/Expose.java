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
@Getter
@Setter
@Table(name = "df_expose")
public class Expose extends Instruction{
    @Id
    @GeneratedValue(generator = "SEC_GEN_EXPOSE", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_EXPOSE", sequenceName = "SEC_EXPOSE",allocationSize=1)
    @Column(name="EXPOSE_ID", unique=true, nullable=false)
    public long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="SNAP_ID")
    Snapshot snapshot;

    @Column(name = "port")
    public long port;

    @Column(name = "current", nullable = false)
    public boolean current;

    public Expose(Snapshot snapshot, String port) {
        super();
        this.snapshot = snapshot;
        String p = port.replaceAll("[^0-9]+", " ");
        p = p.replaceAll(" ", "");
        this.port = Integer.parseInt(p);
}

    public Expose() {
    }
}
