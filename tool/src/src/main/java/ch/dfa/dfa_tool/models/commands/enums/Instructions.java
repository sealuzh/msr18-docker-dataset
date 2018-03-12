package ch.dfa.dfa_tool.models.commands.enums;

/**
 * Created by salizumberi-laptop on 03.12.2016.
 */
public enum Instructions {
    ADD("ADD"),
    ARG("ARG"),
    CMD("CMD"),
    COPY("COPY"),
    ENTRYPOINT("ENTRYPOINT"),
    ENV("ENV"),
    EXPOSE("EXPOSE"),
    FROM("FROM"),
    HEALTHCHECK("HEALTHCHECK"),
    LABEL("LABEL"),
    MAINAINER("MAINAINER"),
    ONBUILD("ONBUILD"),
    RUN("RUN"),
    STOPSIGNAL("STOPSIGNAL"),
    USER("USER"),
    VOLUME("VOLUME"),
    WORKDIR("WORKDIR"),
    COMMENT("COMMENT");


    private final String formatted;


    Instructions(String formatted){
        this.formatted = formatted;
    }

    @Override
    public String toString() {
        return formatted;
    }
}
