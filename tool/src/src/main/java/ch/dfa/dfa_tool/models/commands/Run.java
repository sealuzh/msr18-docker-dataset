package ch.dfa.dfa_tool.models.commands;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ch.dfa.dfa_tool.models.Snapshot;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by salizumberi-laptop on 01.11.2016.
 */
@Entity
@Setter
@Getter
@Table(name = "df_run")
public class Run extends Instruction{

    @Id
    @GeneratedValue(generator = "SEC_GEN_RUN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEC_GEN_RUN", sequenceName = "SEC_RUN",allocationSize=1)
    @Column(name="RUN_ID", unique=true, nullable=false)
    public long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name ="SNAP_ID")
    Snapshot snapshot;

    @Transient
    public int score;

    @Column(name = "executable")
    public String executable;

    @Column(name = "run_params", length = 2024)
    public String allParams;

    @ElementCollection
    @CollectionTable(name="run_params", joinColumns=@JoinColumn(name="RUN_ID"))
    @Column(name="run_params", length = 2024)
    public List<String> params;

    @Column(name = "current", nullable = false)
    public boolean current;

    public Run(Snapshot snapshot,String executable, List<String> params) {
        this.snapshot = snapshot;
        this.executable=executable;

        List<String> paramsShortened = new ArrayList<>();
        for (String param: params){
            if (param.length() > 240){
                paramsShortened.add(param.substring(0, 240) + "...");
            }else{
                paramsShortened.add(param);

            }
        }
        this.params= paramsShortened;

        String allParams = "";
        for(String p: params){
            allParams += "¦"+ p;
        }

        if (allParams.length() > 240){
            this.allParams = allParams.substring(0, 240) + "...";
        }else{
            this.allParams = allParams;
        }

        if (allParams.length() > 240){
            this.allParams = allParams.substring(0, 240) + "...";
        }else{
            this.allParams = allParams;
        }
    }

    public Run() {
    }
}
