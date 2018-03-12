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
@Table(name = "df_arg")
public class Arg extends Instruction{
    @Id
    @GeneratedValue(generator = "SEC_GEN_ARG", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_ARG", sequenceName = "SEC_ARG",allocationSize=1)
    @Column(name="ARG_ID", unique=true, nullable=false)
    public long id;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="SNAP_ID")
    Snapshot snapshot;

    @Column(name = "arg")
    public String arg;

    @Column(name = "current", nullable = false)
    public boolean current;

    public Arg(Snapshot snapshot, String arg) {
        super();
        this.snapshot =snapshot;
        this.arg = arg;
    }

    public Arg() {
    }
}
