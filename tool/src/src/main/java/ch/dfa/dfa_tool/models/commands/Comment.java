package ch.dfa.dfa_tool.models.commands;


import ch.dfa.dfa_tool.models.Snapshot;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by salizumberi-laptop on 01.11.2016.
 */

@Entity
@Table(name = "df_comment")
public class Comment extends Instruction{
    @Id
    @GeneratedValue(generator = "SEC_GEN_COMMENT", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_COMMENT", sequenceName = "SEC_COMMENT",allocationSize=1)
    @Column(name="COMMENT_ID", unique=true, nullable=false)
    public long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "SNAP_ID")
    Snapshot snapshot;

    @Column(name = "instruction", length = 1024)
    public String instructionAfter;

    @Column(name = "comment", nullable = false, length = 1024)
    public String comment;

    @Column(name = "current", nullable = false)
    public boolean current;

    public Comment(Snapshot snapshot, String instructionAfter, String comment) {
        super();
        this.snapshot = snapshot;
        this.instructionAfter = instructionAfter;

        if (comment.length() > 240){
            this.comment = comment.substring(0, 240) + "...";
        }else{
            this.comment = comment;
        }
    }
    public Comment() {
    }
}
