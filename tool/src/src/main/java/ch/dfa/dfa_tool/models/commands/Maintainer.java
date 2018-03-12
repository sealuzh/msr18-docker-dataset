package ch.dfa.dfa_tool.models.commands;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ch.dfa.dfa_tool.models.Snapshot;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by salizumberi-laptop on 01.11.2016.
 */
@Entity
@Getter
@Setter
@Table(name = "df_maintainer")
public class Maintainer extends Instruction{
/*    @Id
    @Column(name="REPO_ID", unique=true, nullable=false)
    public long id;*/

    @Id
    @Column(name = "SNAP_ID", unique = true, nullable = false)
    @GeneratedValue(generator = "maintainer_gen")
    @GenericGenerator(name = "maintainer_gen", strategy = "foreign",
            parameters = @org.hibernate.annotations.Parameter(name = "property", value = "snapshot"))
    private long id;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    Snapshot snapshot;

    @Column(name = "maintainername", length = 1024)
    public String maintainername;

    @Column(name = "current", nullable = false)
    public boolean current;

    public Maintainer(Snapshot snapshot, String maintainername) {
        super();
        this.snapshot = snapshot;
        this.maintainername  =maintainername;
    }

    public Maintainer() {
    }
}
