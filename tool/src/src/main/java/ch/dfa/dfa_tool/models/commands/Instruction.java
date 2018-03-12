package ch.dfa.dfa_tool.models.commands;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by salizumberi-laptop on 01.11.2016.
 */
@MappedSuperclass
@Getter
@Setter
public class Instruction {
    /*
    @Id
    @Column(name="REPO_ID", unique=true, nullable=false)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }*/
}
