package ch.dfa.dfa_tool.models.commands;

import ch.dfa.dfa_tool.models.Snapshot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by salizumberi-laptop on 01.11.2016.
 */
@Entity
@Getter
@Setter
@Table(name = "df_from")
public class From extends Instruction implements Serializable {


    @Id
    @Column(name = "SNAP_ID", unique = true, nullable = false)
    @GeneratedValue(generator = "from_gen")
    @GenericGenerator(name = "from_gen", strategy = "foreign",
            parameters = @Parameter(name = "property", value = "snapshot"))
    private long id;

    @JsonIgnore
    @OneToOne(mappedBy = "from", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    Snapshot snapshot;

    @Column(name = "imagename", nullable = false)
    public String imagename ="" ;

    @Column(name = "current", nullable = false)
    public boolean current;

    @Column(name = "imageVersionNumber")
    public double imageVersionNumber;

    @Column(name = "imageVersionString")
    public String imageVersionString  ="";

    @Column(name = "digest")
    public String digest  ="";

    @Column(name = "full_name")
    public String fullName;

    public From(Snapshot snapshot, String imagename) {
        this.snapshot = snapshot;
        this.imagename = imagename;
        this.fullName = imagename;
    }

    public From(Snapshot snapshot, String imagename, double imageVersionNumber) {
        this.snapshot = snapshot;
        this.imagename = imagename;
        this.imageVersionNumber = imageVersionNumber;
        this.fullName = imagename + ":" + imageVersionNumber;

    }

    public From(Snapshot snapshot, String imagename, String digest, String what) {
        this.snapshot = snapshot;
        this.imagename = imagename;

        if (what.equals("diggest")) {
            this.digest = digest;
            this.fullName = imagename + "@" +digest;

        } else {
            this.imageVersionString = digest;
            this.fullName = imagename + "@" +imageVersionString;
        }

    }

    public From() {
    }
}
