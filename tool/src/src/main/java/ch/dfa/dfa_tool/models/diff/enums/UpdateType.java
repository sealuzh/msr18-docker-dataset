package ch.dfa.dfa_tool.models.diff.enums;

/**
 * Created by salizumberi-laptop on 03.12.2016.
 */
public enum UpdateType implements ChangeType<UpdateType>{
    ADD("ADD"),
    CMD("CMD"),
    COPY("COPY"),
    ENTRYPOINT("ENTRYPOINT"),
    ENV("ENV"),
    EXPOSE("EXPOSE"),
    COMMENT("COMMENT"),
    FROM("FROM"),
    LABEL("LABEL"),
    MAINAINER("MAINAINER"),
    ONBUILD("ONBUILD"),
    RUN("RUN"),
    STOPSIGNAL("STOPSIGNAL"),
    USER("USER"),
    VOLUME("VOLUME"),
    WORKDIR("WORKDIR"),
    SOURCE("SOURCE"),
    DESTINATION("DESTINATION"),
    ARG("ARG"),
    EXECUTABLE("EXECUTABLE"),
    PARAMETER("PARAMETER"),
    EXECUTABLE_PARAMETER("EXECUTABLE_PARAMETER"),
    KEY("KEY"),
    VALUE("VALUE"),
    PORT("PORT"),
    IMAGE("IMAGE"),
    IMAGE_NAME("IMAGE_NAME"),
    IMAGE_VERSION_STRING("IMAGE_VERSION_STRING"),
    IMAGE_VERSION_NUMBER("IMAGE_VERSION_NUMBER"),
    IMAGE_VERSION_DIGEST("IMAGE_VERSION_DIGEST"),
    OPTION_PARAMETER("OPTION_PARAMETER"),
    MAINTAINER("MAINTAINER"),
    SIGNAL("SIGNAL"),
    USER_NAME("USER_NAME"),
    PATH("PATH"),
    HEALTHCHECK("HEALTHCHECK");

    UpdateType valueOf(){
        return valueOf(name());
    }

    private final String formatted;


    UpdateType(String formatted){
        this.formatted = formatted;
    }

    @Override
    public String toString() {
        return formatted;
    }
}
