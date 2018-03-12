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
@Table(name = "df_onbuild")
public class OnBuild extends Instruction{

    @Id
    @GeneratedValue(generator = "SEC_GEN_ONBUILD", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_ONBUILD", sequenceName = "SEC_ONBUILD",allocationSize=1)
    @Column(name="ONBUILD_ID", unique=true, nullable=false)
    public long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="SNAP_ID")
    Snapshot snapshot;

    @Column(name = "current", nullable = false)
    public boolean current;

    @Column(name = "instruction", nullable = false)
    public String instruction;

    @Column(name = "instruction_params", length = 1024)
    public String allParams;

    public OnBuild(Snapshot snapshot, String instruction, String allParams) {
        super();
        this.snapshot = snapshot;
        this.instruction =instruction;

        if (allParams.length() > 240){
            this.allParams = allParams.substring(0, 240) + "...";
        }else{
            this.allParams= allParams;
        }
    }

    public OnBuild() {

    }
}
