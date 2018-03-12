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
@Table(name = "df_copy")
public class Copy extends Instruction{

    @Id
    @GeneratedValue(generator = "SEC_GEN_COPY", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_COPY", sequenceName = "SEC_COPY",allocationSize=1)
    @Column(name="COPY_ID", unique=true, nullable=false)
    public long id;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="SNAP_ID")
    Snapshot snapshot;

    @Column(name = "source", length = 1024)
    public String source;

    @Column(name = "destination", length = 1024)
    public String destination;

    @Column(name = "source_destination", length = 1024)
    public String sourceDestination;

    @Column(name = "current", nullable = false)
    public boolean current;

    public Copy(Snapshot snapshot, String source, String destinatation) {
        super();
        this.source = source;
        this.snapshot = snapshot;
        this.destination=destinatation;

        this.sourceDestination = source +" -> " + destinatation;
        if (sourceDestination.length() > 240){
            this.sourceDestination = sourceDestination.substring(0, 240) + "...";
        }
    }

    public Copy() {
    }
}
