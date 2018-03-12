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
@Table(name = "df_healthcheck")
public class Healthcheck extends Instruction {

    @Id
    @Column(name = "SNAP_ID", unique = true, nullable = false)
    @GeneratedValue(generator = "healthcheck_gen")
    @GenericGenerator(name = "healthcheck_gen", strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "snapshot"))
    private long id;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    Snapshot snapshot;

    @Column(name = "options_params", nullable = false, length = 1024)
    String optionsBeforeInstructions;

    @Column(name = "current", nullable = false)
    public boolean current;

    @Column(name = "instruction", nullable = false)
    public String instruction;

    @Column(name = "instruction_params", nullable = false, length = 1024)
    public String allParams;

    public Healthcheck(Snapshot snapshot, String instruction, String allParams) {
        super();
        this.snapshot = snapshot;
        this.instruction = instruction;
        this.allParams = allParams;
    }

    public Healthcheck(String optionsBeforeInstructions) {
        super();
        this.optionsBeforeInstructions = optionsBeforeInstructions;
    }

    public Healthcheck(Snapshot snapshot, String instruction, String optionsBeforeInstructions, String allParams) {
        super();
        this.snapshot = snapshot;
        this.instruction = instruction;
        this.optionsBeforeInstructions = optionsBeforeInstructions;

        if (allParams.length() > 240) {
            this.allParams = allParams.substring(0, 240) + "...";
        } else {
            this.allParams = allParams;
        }
    }


    public Healthcheck() {
    }
}
