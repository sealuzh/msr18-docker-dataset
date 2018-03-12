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
@Table(name = "df_user")
public class User extends Instruction{
    @Id
    @GeneratedValue(generator = "SEC_GEN_USER", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_USER", sequenceName = "SEC_USER",allocationSize=1)
    @Column(name="USER_ID", unique=true, nullable=false)
    public long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="SNAP_ID")
    Snapshot snapshot;

    @Column(name = "username")
    public String username;

    @Column(name = "current", nullable = false)
    public boolean current;

    public User(Snapshot snapshot, String username) {
        super();
        this.snapshot = snapshot;
        this.username = username;
    }

    public User() {
    }
}
