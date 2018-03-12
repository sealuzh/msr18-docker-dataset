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
@Table(name = "df_add")
public class Add extends Instruction{
    @Id
    @GeneratedValue(generator = "SEC_GEN_ADD", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_ADD", sequenceName = "SEC_ADD",allocationSize=1)
    @Column(name="ADD_ID", unique=true, nullable=false)
    public long id;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="SNAP_ID")
    Snapshot snapshot;

    @Column(name = "source", length = 1024)
    public String source;

    @Column(name = "source_destination", length = 1024)
    public String sourceDestination;

    @Column(name = "destination", length = 1024)
    public String destination;

    @Column(name = "current", nullable = false)
    public boolean current;

    public Add(Snapshot snapshot, String source, String destinatation) {
        super();
        this.snapshot = snapshot;
        this.source = source;
        this.destination=destinatation;

        this.sourceDestination = source +" -> " + destinatation;
        if (sourceDestination.length() > 240){
            this.sourceDestination = this.sourceDestination.substring(0, 240) + "...";
        }

    }

    public Add() {
    }
}
