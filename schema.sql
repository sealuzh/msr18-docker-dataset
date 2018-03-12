--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: changed_files; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE changed_files (
    changedfile_id bigint NOT NULL,
    changetype character varying(1024),
    commit character varying(1024),
    deletions integer,
    file_name character varying(1024),
    path character varying(1024),
    file_type character varying(1024),
    full_file_name character varying(1024),
    insertions integer,
    mode integer,
    range_index integer,
    range_size integer,
    reponame character varying(1024),
    snap_id bigint
);


ALTER TABLE changed_files OWNER TO postgres;

--
-- Name: cmd_params; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cmd_params (
    run_id bigint NOT NULL,
    cmd_params character varying(1024)
);


ALTER TABLE cmd_params OWNER TO postgres;

--
-- Name: df_add; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_add (
    add_id bigint NOT NULL,
    current boolean NOT NULL,
    destination character varying(1024),
    source character varying(1024),
    source_destination character varying(1024),
    snap_id bigint
);


ALTER TABLE df_add OWNER TO postgres;

--
-- Name: df_arg; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_arg (
    arg_id bigint NOT NULL,
    arg character varying(255),
    current boolean NOT NULL,
    snap_id bigint
);


ALTER TABLE df_arg OWNER TO postgres;

--
-- Name: df_cmd; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_cmd (
    snap_id bigint NOT NULL,
    run_params character varying(1024),
    current boolean NOT NULL,
    executable character varying(255)
);


ALTER TABLE df_cmd OWNER TO postgres;

--
-- Name: df_comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_comment (
    comment_id bigint NOT NULL,
    comment character varying(1024) NOT NULL,
    current boolean NOT NULL,
    instruction character varying(1024),
    snap_id bigint
);


ALTER TABLE df_comment OWNER TO postgres;

--
-- Name: df_copy; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_copy (
    copy_id bigint NOT NULL,
    current boolean NOT NULL,
    destination character varying(1024),
    source character varying(1024),
    source_destination character varying(1024),
    snap_id bigint
);


ALTER TABLE df_copy OWNER TO postgres;

--
-- Name: df_entrypoint; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_entrypoint (
    snap_id bigint NOT NULL,
    run_params character varying(1024),
    current boolean NOT NULL,
    executable character varying(255)
);


ALTER TABLE df_entrypoint OWNER TO postgres;

--
-- Name: df_env; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_env (
    env_id bigint NOT NULL,
    current boolean NOT NULL,
    key character varying(255),
    key_value character varying(1024),
    value character varying(255),
    snap_id bigint
);


ALTER TABLE df_env OWNER TO postgres;

--
-- Name: df_expose; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_expose (
    expose_id bigint NOT NULL,
    current boolean NOT NULL,
    port bigint,
    snap_id bigint
);


ALTER TABLE df_expose OWNER TO postgres;

--
-- Name: df_from; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_from (
    snap_id bigint NOT NULL,
    current boolean NOT NULL,
    digest character varying(255),
    full_name character varying(255),
    imageversionnumber double precision,
    imageversionstring character varying(255),
    imagename character varying(255) NOT NULL
);


ALTER TABLE df_from OWNER TO postgres;

--
-- Name: df_healthcheck; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_healthcheck (
    snap_id bigint NOT NULL,
    instruction_params character varying(1024) NOT NULL,
    current boolean NOT NULL,
    instruction character varying(255) NOT NULL,
    options_params character varying(1024) NOT NULL
);


ALTER TABLE df_healthcheck OWNER TO postgres;

--
-- Name: df_label; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_label (
    label_id bigint NOT NULL,
    current boolean NOT NULL,
    key character varying(255),
    key_value character varying(1024),
    value character varying(255),
    snap_id bigint
);


ALTER TABLE df_label OWNER TO postgres;

--
-- Name: df_maintainer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_maintainer (
    snap_id bigint NOT NULL,
    current boolean NOT NULL,
    maintainername character varying(1024)
);


ALTER TABLE df_maintainer OWNER TO postgres;

--
-- Name: df_onbuild; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_onbuild (
    onbuild_id bigint NOT NULL,
    instruction_params character varying(1024),
    current boolean NOT NULL,
    instruction character varying(255) NOT NULL,
    snap_id bigint
);


ALTER TABLE df_onbuild OWNER TO postgres;

--
-- Name: df_run; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_run (
    run_id bigint NOT NULL,
    run_params character varying(2024),
    current boolean NOT NULL,
    executable character varying(255),
    snap_id bigint
);


ALTER TABLE df_run OWNER TO postgres;

--
-- Name: df_stopsignal; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_stopsignal (
    snap_id bigint NOT NULL,
    current boolean NOT NULL,
    signal character varying(255)
);


ALTER TABLE df_stopsignal OWNER TO postgres;

--
-- Name: df_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_user (
    user_id bigint NOT NULL,
    current boolean NOT NULL,
    username character varying(255),
    snap_id bigint
);


ALTER TABLE df_user OWNER TO postgres;

--
-- Name: df_volume; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_volume (
    volume_id bigint NOT NULL,
    current boolean NOT NULL,
    value character varying(1024),
    snap_id bigint
);


ALTER TABLE df_volume OWNER TO postgres;

--
-- Name: df_workdir; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE df_workdir (
    workdir_id bigint NOT NULL,
    current boolean NOT NULL,
    path character varying(1024),
    snap_id bigint
);


ALTER TABLE df_workdir OWNER TO postgres;

--
-- Name: diff; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE diff (
    diff_id bigint NOT NULL,
    commit_date bigint NOT NULL,
    del integer NOT NULL,
    diff_state character varying(255) NOT NULL,
    ins integer NOT NULL,
    mod integer NOT NULL
);


ALTER TABLE diff OWNER TO postgres;

--
-- Name: diff_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE diff_type (
    diff_type_id bigint NOT NULL,
    after character varying(255),
    before character varying(255),
    change_type character varying(255) NOT NULL,
    executable character varying(255),
    instruction character varying(255),
    diff_id bigint
);


ALTER TABLE diff_type OWNER TO postgres;

--
-- Name: dockerfile; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE dockerfile (
    dock_id bigint NOT NULL,
    commits integer NOT NULL,
    docker_path character varying(255) NOT NULL,
    created_at bigint NOT NULL,
    first_docker_commit bigint NOT NULL,
    repo_id bigint NOT NULL,
    i_size integer NOT NULL,
    project_project_id bigint
);


ALTER TABLE dockerfile OWNER TO postgres;

--
-- Name: entrypoints_params; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE entrypoints_params (
    entrypoint_id bigint NOT NULL,
    entrypoints_params character varying(1024)
);


ALTER TABLE entrypoints_params OWNER TO postgres;

--
-- Name: project; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE project (
    project_id bigint NOT NULL,
    git_url character varying(1024) NOT NULL,
    created_at bigint NOT NULL,
    i_forks integer NOT NULL,
    giturl character varying(255),
    i_network_count integer NOT NULL,
    i_open_issues integer NOT NULL,
    i_owner_type character varying(255) NOT NULL,
    repo_id bigint NOT NULL,
    repo_path character varying(1024) NOT NULL,
    i_size integer NOT NULL,
    i_stargazers integer NOT NULL,
    i_subscribers integer NOT NULL,
    i_watchers integer NOT NULL
);


ALTER TABLE project OWNER TO postgres;

--
-- Name: run_params; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE run_params (
    run_id bigint NOT NULL,
    run_params character varying(2024)
);


ALTER TABLE run_params OWNER TO postgres;

--
-- Name: sec_add; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_add
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_add OWNER TO postgres;

--
-- Name: sec_arg; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_arg
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_arg OWNER TO postgres;

--
-- Name: sec_changedfile; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_changedfile
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_changedfile OWNER TO postgres;

--
-- Name: sec_comment; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_comment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_comment OWNER TO postgres;

--
-- Name: sec_copy; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_copy
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_copy OWNER TO postgres;

--
-- Name: sec_diff; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_diff
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_diff OWNER TO postgres;

--
-- Name: sec_diff_type; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_diff_type
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_diff_type OWNER TO postgres;

--
-- Name: sec_dock; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_dock
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_dock OWNER TO postgres;

--
-- Name: sec_env; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_env
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_env OWNER TO postgres;

--
-- Name: sec_expose; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_expose
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_expose OWNER TO postgres;

--
-- Name: sec_label; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_label
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_label OWNER TO postgres;

--
-- Name: sec_onbuild; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_onbuild
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_onbuild OWNER TO postgres;

--
-- Name: sec_project; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_project
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_project OWNER TO postgres;

--
-- Name: sec_run; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_run
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_run OWNER TO postgres;

--
-- Name: sec_snap; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_snap
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_snap OWNER TO postgres;

--
-- Name: sec_user; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_user
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_user OWNER TO postgres;

--
-- Name: sec_volume; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_volume
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_volume OWNER TO postgres;

--
-- Name: sec_workdir; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE sec_workdir
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sec_workdir OWNER TO postgres;

--
-- Name: snap_diff; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE snap_diff (
    snap_id bigint NOT NULL,
    diff_id bigint NOT NULL
);


ALTER TABLE snap_diff OWNER TO postgres;

--
-- Name: snapshot; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE snapshot (
    snap_id bigint NOT NULL,
    change_type character varying(255),
    commit_date bigint NOT NULL,
    del integer,
    from_date bigint NOT NULL,
    image_is_automated boolean,
    image_is_offical boolean,
    commit_index integer NOT NULL,
    ins integer,
    instructions integer NOT NULL,
    current boolean NOT NULL,
    repo_id bigint NOT NULL,
    star_count integer,
    to_date bigint NOT NULL,
    dock_id bigint
);


ALTER TABLE snapshot OWNER TO postgres;

--
-- Name: violated_rules; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE violated_rules (
    dock_id bigint NOT NULL,
    violated_rules character varying(255)
);


ALTER TABLE violated_rules OWNER TO postgres;

--
-- Name: changed_files_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY changed_files
    ADD CONSTRAINT changed_files_pkey PRIMARY KEY (changedfile_id);


--
-- Name: df_add_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_add
    ADD CONSTRAINT df_add_pkey PRIMARY KEY (add_id);


--
-- Name: df_arg_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_arg
    ADD CONSTRAINT df_arg_pkey PRIMARY KEY (arg_id);


--
-- Name: df_cmd_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_cmd
    ADD CONSTRAINT df_cmd_pkey PRIMARY KEY (snap_id);


--
-- Name: df_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_comment
    ADD CONSTRAINT df_comment_pkey PRIMARY KEY (comment_id);


--
-- Name: df_copy_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_copy
    ADD CONSTRAINT df_copy_pkey PRIMARY KEY (copy_id);


--
-- Name: df_entrypoint_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_entrypoint
    ADD CONSTRAINT df_entrypoint_pkey PRIMARY KEY (snap_id);


--
-- Name: df_env_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_env
    ADD CONSTRAINT df_env_pkey PRIMARY KEY (env_id);


--
-- Name: df_expose_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_expose
    ADD CONSTRAINT df_expose_pkey PRIMARY KEY (expose_id);


--
-- Name: df_from_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_from
    ADD CONSTRAINT df_from_pkey PRIMARY KEY (snap_id);


--
-- Name: df_healthcheck_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_healthcheck
    ADD CONSTRAINT df_healthcheck_pkey PRIMARY KEY (snap_id);


--
-- Name: df_label_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_label
    ADD CONSTRAINT df_label_pkey PRIMARY KEY (label_id);


--
-- Name: df_maintainer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_maintainer
    ADD CONSTRAINT df_maintainer_pkey PRIMARY KEY (snap_id);


--
-- Name: df_onbuild_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_onbuild
    ADD CONSTRAINT df_onbuild_pkey PRIMARY KEY (onbuild_id);


--
-- Name: df_run_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_run
    ADD CONSTRAINT df_run_pkey PRIMARY KEY (run_id);


--
-- Name: df_stopsignal_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_stopsignal
    ADD CONSTRAINT df_stopsignal_pkey PRIMARY KEY (snap_id);


--
-- Name: df_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_user
    ADD CONSTRAINT df_user_pkey PRIMARY KEY (user_id);


--
-- Name: df_volume_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_volume
    ADD CONSTRAINT df_volume_pkey PRIMARY KEY (volume_id);


--
-- Name: df_workdir_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_workdir
    ADD CONSTRAINT df_workdir_pkey PRIMARY KEY (workdir_id);


--
-- Name: diff_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY diff
    ADD CONSTRAINT diff_pkey PRIMARY KEY (diff_id);


--
-- Name: diff_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY diff_type
    ADD CONSTRAINT diff_type_pkey PRIMARY KEY (diff_type_id);


--
-- Name: dockerfile_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dockerfile
    ADD CONSTRAINT dockerfile_pkey PRIMARY KEY (dock_id);


--
-- Name: project_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY project
    ADD CONSTRAINT project_pkey PRIMARY KEY (project_id);


--
-- Name: snapshot_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY snapshot
    ADD CONSTRAINT snapshot_pkey PRIMARY KEY (snap_id);


--
-- Name: fk_99hxtsq8a8nyqbhklapsmr2hk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_env
    ADD CONSTRAINT fk_99hxtsq8a8nyqbhklapsmr2hk FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_9x85wmy92i0nxyp8m50j0neo6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_comment
    ADD CONSTRAINT fk_9x85wmy92i0nxyp8m50j0neo6 FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_aixhmxvmk7f2x1oui5f39jqbv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dockerfile
    ADD CONSTRAINT fk_aixhmxvmk7f2x1oui5f39jqbv FOREIGN KEY (project_project_id) REFERENCES project(project_id);


--
-- Name: fk_c93w807v568ko6rymyt6c305s; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_user
    ADD CONSTRAINT fk_c93w807v568ko6rymyt6c305s FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_d3nxo5ilgrrenenxsd7h3x7l; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY changed_files
    ADD CONSTRAINT fk_d3nxo5ilgrrenenxsd7h3x7l FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_e1aohuvxylrai5jrd8ioa7yh4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_run
    ADD CONSTRAINT fk_e1aohuvxylrai5jrd8ioa7yh4 FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_e68ai4ye6gok22iwxdjeknujn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY snapshot
    ADD CONSTRAINT fk_e68ai4ye6gok22iwxdjeknujn FOREIGN KEY (dock_id) REFERENCES dockerfile(dock_id);


--
-- Name: fk_imwmlbx9q2sohu6bm3nprg0e3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_expose
    ADD CONSTRAINT fk_imwmlbx9q2sohu6bm3nprg0e3 FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_imyorafv390dk6vb5cpwn84tw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_copy
    ADD CONSTRAINT fk_imyorafv390dk6vb5cpwn84tw FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_iv7p0pca2ic6g7tb968avp8b4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_label
    ADD CONSTRAINT fk_iv7p0pca2ic6g7tb968avp8b4 FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_j9k0lofaph27yn3v9ms8rycpe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_onbuild
    ADD CONSTRAINT fk_j9k0lofaph27yn3v9ms8rycpe FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_jtj9sa23utyko8btdkfr49omm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY diff_type
    ADD CONSTRAINT fk_jtj9sa23utyko8btdkfr49omm FOREIGN KEY (diff_id) REFERENCES diff(diff_id);


--
-- Name: fk_kardaha6aesqadwib0p6dt51q; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY run_params
    ADD CONSTRAINT fk_kardaha6aesqadwib0p6dt51q FOREIGN KEY (run_id) REFERENCES df_run(run_id);


--
-- Name: fk_keddgxuuo3c933tiuvbsgmk3i; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_workdir
    ADD CONSTRAINT fk_keddgxuuo3c933tiuvbsgmk3i FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_l8hd6ihlkfn2l7e1sww17prrj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cmd_params
    ADD CONSTRAINT fk_l8hd6ihlkfn2l7e1sww17prrj FOREIGN KEY (run_id) REFERENCES df_cmd(snap_id);


--
-- Name: fk_lv62obmxaucl16p15oa7xftvh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY snap_diff
    ADD CONSTRAINT fk_lv62obmxaucl16p15oa7xftvh FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_nuqilek2p0y97y6rg3dw7fro1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY entrypoints_params
    ADD CONSTRAINT fk_nuqilek2p0y97y6rg3dw7fro1 FOREIGN KEY (entrypoint_id) REFERENCES df_entrypoint(snap_id);


--
-- Name: fk_r5afv125tbmyxxy31ave0joeq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_add
    ADD CONSTRAINT fk_r5afv125tbmyxxy31ave0joeq FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_rwwejse1ufbkqbxg8b4pj9f3c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY violated_rules
    ADD CONSTRAINT fk_rwwejse1ufbkqbxg8b4pj9f3c FOREIGN KEY (dock_id) REFERENCES dockerfile(dock_id);


--
-- Name: fk_si2wryf9fls2y8fs2himb2lyu; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY snap_diff
    ADD CONSTRAINT fk_si2wryf9fls2y8fs2himb2lyu FOREIGN KEY (diff_id) REFERENCES diff(diff_id);


--
-- Name: fk_tadokjw7t1pj9s04dglksf4ot; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_arg
    ADD CONSTRAINT fk_tadokjw7t1pj9s04dglksf4ot FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: fk_tnet594603b55lakbf0o3p577; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY df_volume
    ADD CONSTRAINT fk_tnet594603b55lakbf0o3p577 FOREIGN KEY (snap_id) REFERENCES snapshot(snap_id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

