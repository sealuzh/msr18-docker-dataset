package ch.dfa.dfa_tool.models.diff;

import ch.dfa.dfa_tool.models.Diff;
import ch.dfa.dfa_tool.models.commands.enums.Instructions;
import ch.dfa.dfa_tool.models.diff.enums.ChangeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by salizumberi-laptop on 02.12.2016.
 */
@Entity
@Getter
@Setter
@Table(name = "diff_type")
public class DiffType {
    @Id
    @GeneratedValue(generator = "SEC_GEN_DIFF_TYPE", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_DIFF_TYPE", sequenceName = "SEC_DIFF_TYPE", allocationSize = 1)
    @Column(name = "DIFF_TYPE_ID", unique = true, nullable = false)
    private long id;

    @Enumerated(EnumType.STRING)
    private Instructions instruction;

    @Column(name = "change_type", nullable = false)
    private String changeType;

    @Column(name = "before")
    private String before;

    @Column(name = "after")
    private String after;

    @Column(name = "executable")
    private String executable;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="DIFF_ID")
    private Diff diff;


    public DiffType(Diff diff) {
        this.diff = diff;
    }

    public DiffType() {
    }

    public void setBeforeAndAfter(String before,String after) {
        this.before = before;
        this.after = after;
    }

    public void setBeforeAndAfter(String before,String after, String executable) {
        this.before = before;
        this.after = after;
        this.executable = executable;
    }
    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getChangeType() {
        return changeType;
    }


   public <T extends ChangeType> void addChangeType(T obj){
        Class enumy = obj.getClass();
        String enumyClassName = enumy.getSimpleName();
        String changetype = obj.toString();
       this.setChangeType(enumyClassName+"_"+changetype);
    }

    public void setChangeType(String string){
        this.changeType = string;
    }

    public Instructions getInstruction() {
        return instruction;
    }

    public void setInstruction(Instructions instruction) {
        this.instruction = instruction;
    }



}
