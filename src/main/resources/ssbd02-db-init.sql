SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

CREATE TABLE public.access_level (
    version bigint,
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    name character varying(64) NOT NULL
);

ALTER TABLE public.access_level OWNER TO ssbd02admin;

CREATE TABLE public.access_level_assignment (
    version bigint,
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    account_id bigint NOT NULL,
    access_level_id bigint NOT NULL,
    active boolean DEFAULT true NOT NULL
);

ALTER TABLE public.access_level_assignment OWNER TO ssbd02admin;

CREATE TABLE public.account (
    version bigint,
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    login character varying(64) NOT NULL,
    email character varying(64) NOT NULL,
    password character varying(60) NOT NULL,
    name character varying(64) NOT NULL,
    surname character varying(64) NOT NULL,
    registered boolean DEFAULT false NOT NULL,
    active boolean DEFAULT true NOT NULL
);

ALTER TABLE public.account OWNER TO ssbd02admin;

CREATE TABLE public.account_report (
    version bigint, id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    reportee_id bigint NOT NULL,
    reported_id bigint NOT NULL,
    cause_id integer NOT NULL,
    reviewed boolean DEFAULT false NOT NULL
);


ALTER TABLE public.account_report OWNER TO ssbd02admin;

CREATE TABLE public.account_report_cause (
    version bigint,
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    cause character varying(128) NOT NULL
);


ALTER TABLE public.account_report_cause OWNER TO ssbd02admin;

CREATE VIEW public.authorization_view AS
 SELECT account.login,
    account.password,
    access_level.name AS access_level
   FROM ((public.account
     JOIN public.access_level_assignment ON ((account.id = access_level_assignment.account_id)))
     JOIN public.access_level ON ((access_level.id = access_level_assignment.access_level_id)))
  WHERE ((account.active = true) AND (access_level_assignment.active = true) AND (account.registered = true));


ALTER TABLE public.authorization_view OWNER TO ssbd02admin;

CREATE TABLE public.availability (
    version bigint,
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    photographer_id bigint NOT NULL,
    weekday smallint NOT NULL,
    "from" time without time zone NOT NULL,
    "to" time without time zone NOT NULL,
    CONSTRAINT weekday_check CHECK (((weekday >= 0) AND (weekday <= 6)))
);


ALTER TABLE public.availability OWNER TO ssbd02admin;

CREATE TABLE public.photo (
    version bigint,
    photographer_id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    id bigint NOT NULL,
    data bytea NOT NULL,
    title character varying(64),
    description character varying(1024),
    like_count bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.photo OWNER TO ssbd02admin;

CREATE TABLE public.photo_like (
    version bigint,
    photo_id bigint NOT NULL,
    account_id bigint NOT NULL,
    test_field bigint NOT NULL
);

ALTER TABLE public.photo_like OWNER TO ssbd02admin;

CREATE TABLE public.photographer_info (
    version bigint,
    account_id bigint NOT NULL,
    score bigint DEFAULT 0 NOT NULL,
    review_count bigint DEFAULT 0 NOT NULL,
    description character varying(4096)[],
    lat real,
    long real
);

ALTER TABLE public.photographer_info OWNER TO ssbd02admin;

CREATE TABLE public.photographer_report (
    version bigint,
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    cause_id bigint NOT NULL,
    reviewed boolean DEFAULT false NOT NULL,
    account_id bigint NOT NULL,
    photographer_id bigint NOT NULL
);

ALTER TABLE public.photographer_report OWNER TO ssbd02admin;

CREATE TABLE public.photographer_report_cause (
    version bigint,
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    cause character varying(128) NOT NULL
);

ALTER TABLE public.photographer_report_cause OWNER TO ssbd02admin;

CREATE TABLE public.photographer_specialization (
    version bigint,
    photographer_id bigint NOT NULL,
    specialization_id integer NOT NULL
);


ALTER TABLE public.photographer_specialization OWNER TO ssbd02admin;

CREATE TABLE public.reservation (
    version bigint,
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    photographer_id bigint NOT NULL,
    account_id bigint NOT NULL,
    "from" timestamp without time zone NOT NULL,
    "to" timestamp without time zone NOT NULL
);

ALTER TABLE public.reservation OWNER TO ssbd02admin;

CREATE TABLE public.review (
    version bigint,
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    photographer_id bigint NOT NULL,
    account_id bigint NOT NULL,
    score bigint NOT NULL,
    like_count bigint DEFAULT 0 NOT NULL,
    content character varying(4096),
    active boolean DEFAULT true NOT NULL,
    CONSTRAINT score_check CHECK (((score >= 0) AND (score <= 10)))
);

ALTER TABLE public.review OWNER TO ssbd02admin;

CREATE TABLE public.review_like (
    version bigint,
    account_id bigint NOT NULL,
    review_id bigint NOT NULL
);

ALTER TABLE public.review_like OWNER TO ssbd02admin;

CREATE TABLE public.review_report (
    version bigint,
    id bigint NOT NULL GENERATED ALWAYS AS IDENTITY,
    review_id bigint NOT NULL,
    account_id bigint NOT NULL,
    cause_id integer NOT NULL,
    reviewed boolean DEFAULT false NOT NULL
);


ALTER TABLE public.review_report OWNER TO ssbd02admin;

CREATE TABLE public.review_report_cause (
    version bigint,
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    cause character varying(128) NOT NULL
);


ALTER TABLE public.review_report_cause OWNER TO ssbd02admin;

CREATE TABLE public.specialization (
    version bigint,
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY,
    name character varying(64) NOT NULL
);

ALTER TABLE public.specialization OWNER TO ssbd02admin;

CREATE TABLE public.token (
    version bigint,
    id character varying(64),
    "expiration" timestamp without time zone NOT NULL,
    account_id bigint NOT NULL,
    token_type character varying(32)
);

ALTER TABLE ONLY public.token OWNER TO ssbd02admin;

ALTER TABLE ONLY public.token ADD CONSTRAINT "FK_token.account_id" FOREIGN KEY (account_id) REFERENCES public.account(id);

ALTER TABLE ONLY public.access_level_assignment ADD CONSTRAINT access_level_assignment_account_id_access_level_id_key UNIQUE (account_id, access_level_id);

ALTER TABLE ONLY public.access_level_assignment ADD CONSTRAINT access_level_assignment_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.access_level ADD CONSTRAINT access_level_name_key UNIQUE (name);

ALTER TABLE ONLY public.access_level ADD CONSTRAINT access_level_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.account ADD CONSTRAINT account_email_key UNIQUE (email);

ALTER TABLE ONLY public.account ADD CONSTRAINT account_login_key UNIQUE (login);

ALTER TABLE ONLY public.account ADD CONSTRAINT account_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.availability ADD CONSTRAINT availability_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.photo_like ADD CONSTRAINT photo_like_pkey PRIMARY KEY (photo_id, account_id);

ALTER TABLE ONLY public.photo ADD CONSTRAINT photo_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.photographer_info ADD CONSTRAINT photographer_pkey PRIMARY KEY (account_id);

ALTER TABLE ONLY public.photographer_report_cause ADD CONSTRAINT photographer_report_cause_cause_key UNIQUE (cause);

ALTER TABLE ONLY public.photographer_report_cause ADD CONSTRAINT photographer_report_cause_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.photographer_report ADD CONSTRAINT photographer_report_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.photographer_specialization ADD CONSTRAINT photographer_specialization_pkey PRIMARY KEY (photographer_id, specialization_id);

ALTER TABLE ONLY public.reservation ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.review_like ADD CONSTRAINT review_like_pkey PRIMARY KEY (account_id, review_id);

ALTER TABLE ONLY public.review ADD CONSTRAINT review_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.review_report_cause ADD CONSTRAINT review_report_cause_cause_key UNIQUE (cause);

ALTER TABLE ONLY public.review_report_cause ADD CONSTRAINT review_report_cause_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.review_report ADD CONSTRAINT review_report_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.specialization ADD CONSTRAINT specialization_name_key UNIQUE (name);

ALTER TABLE ONLY public.specialization ADD CONSTRAINT specialization_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.account_report_cause ADD CONSTRAINT user_report_cause_cause_key UNIQUE (cause);

ALTER TABLE ONLY public.account_report_cause ADD CONSTRAINT user_report_cause_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.account_report ADD CONSTRAINT user_report_pkey PRIMARY KEY (id);

CREATE INDEX access_level_assignment_access_level_id_idx ON public.access_level_assignment USING btree (access_level_id);

CREATE INDEX access_level_assignment_account_id_idx ON public.access_level_assignment USING btree (account_id);

CREATE INDEX account_report_cause_id_idx ON public.account_report USING btree (cause_id);

CREATE INDEX account_report_reported_id_idx ON public.account_report USING btree (reported_id);

CREATE INDEX account_report_reportee_id_idx ON public.account_report USING btree (reportee_id);

CREATE INDEX availability_photographer_id_idx ON public.availability USING btree (photographer_id);

CREATE INDEX photo_like_photo_id_idx ON public.photo_like USING btree (photo_id);

CREATE INDEX photo_like_user_id_idx ON public.photo_like USING btree (account_id);

CREATE INDEX photo_photographer_id_idx ON public.photo USING btree (photographer_id);

CREATE INDEX photographer_account_id_idx ON public.photographer_info USING btree (account_id);

CREATE INDEX photographer_report_account_id_idx ON public.photographer_report USING btree (account_id);

CREATE INDEX photographer_report_cause_id_idx ON public.photographer_report USING btree (cause_id);

CREATE INDEX photographer_report_photographer_id_idx ON public.photographer_report USING btree (photographer_id);

CREATE INDEX photographer_specialization_photographer_id_idx ON public.photographer_specialization USING btree (photographer_id);

CREATE INDEX photographer_specialization_specialization_id_idx ON public.photographer_specialization USING btree (specialization_id);

CREATE INDEX reservation_account_id_idx ON public.reservation USING btree (account_id);

CREATE INDEX reservation_photographer_id_idx ON public.reservation USING btree (photographer_id);

CREATE INDEX review_account_id_idx ON public.review USING btree (account_id);

CREATE INDEX review_like_account_id_idx ON public.review_like USING btree (account_id);

CREATE INDEX review_like_review_id_idx ON public.review_like USING btree (review_id);

CREATE INDEX review_photographer_id_idx ON public.review USING btree (photographer_id);

CREATE INDEX review_report_account_id_idx ON public.review_report USING btree (account_id);

CREATE INDEX review_report_cause_id_idx ON public.review_report USING btree (cause_id);

CREATE INDEX review_report_review_id_idx ON public.review_report USING btree (review_id);

ALTER TABLE ONLY public.access_level_assignment ADD CONSTRAINT "FK_access_level_assignment.access_level_id" FOREIGN KEY (access_level_id) REFERENCES public.access_level(id);

ALTER TABLE ONLY public.access_level_assignment ADD CONSTRAINT "FK_access_level_assignment.account_id" FOREIGN KEY (account_id) REFERENCES public.account(id);

ALTER TABLE ONLY public.availability ADD CONSTRAINT "FK_availability.photographer_id" FOREIGN KEY (photographer_id) REFERENCES public.photographer_info(account_id);

ALTER TABLE ONLY public.photo ADD CONSTRAINT "FK_photo.photographer_id" FOREIGN KEY (photographer_id) REFERENCES public.photographer_info(account_id);

ALTER TABLE ONLY public.photo_like ADD CONSTRAINT "FK_photo_like.photo_id" FOREIGN KEY (photo_id) REFERENCES public.photo(id);

ALTER TABLE ONLY public.photo_like ADD CONSTRAINT "FK_photo_like.user_id" FOREIGN KEY (account_id) REFERENCES public.account(id);

ALTER TABLE ONLY public.photographer_report ADD CONSTRAINT "FK_photographer_report.account_id" FOREIGN KEY (account_id) REFERENCES public.account(id);

ALTER TABLE ONLY public.photographer_report ADD CONSTRAINT "FK_photographer_report.cause_id" FOREIGN KEY (cause_id) REFERENCES public.photographer_report_cause(id);

ALTER TABLE ONLY public.photographer_report ADD CONSTRAINT "FK_photographer_report.photographer_id" FOREIGN KEY (photographer_id) REFERENCES public.photographer_info(account_id);

ALTER TABLE ONLY public.photographer_specialization ADD CONSTRAINT "FK_photographer_specialization.photographer_id" FOREIGN KEY (photographer_id) REFERENCES public.photographer_info(account_id);

ALTER TABLE ONLY public.photographer_specialization ADD CONSTRAINT "FK_photographer_specialization.specialization_id" FOREIGN KEY (specialization_id) REFERENCES public.specialization(id);

ALTER TABLE ONLY public.reservation ADD CONSTRAINT "FK_reservation.account_id" FOREIGN KEY (account_id) REFERENCES public.account(id);

ALTER TABLE ONLY public.reservation ADD CONSTRAINT "FK_reservation.photographer_id" FOREIGN KEY (photographer_id) REFERENCES public.photographer_info(account_id);

ALTER TABLE ONLY public.review ADD CONSTRAINT "FK_review.account_id" FOREIGN KEY (account_id) REFERENCES public.account(id);

ALTER TABLE ONLY public.review ADD CONSTRAINT "FK_review.photographer_id" FOREIGN KEY (photographer_id) REFERENCES public.photographer_info(account_id);

ALTER TABLE ONLY public.review_like ADD CONSTRAINT "FK_review_like.account_id" FOREIGN KEY (account_id) REFERENCES public.account(id);

ALTER TABLE ONLY public.review_like ADD CONSTRAINT "FK_review_like.review_id" FOREIGN KEY (review_id) REFERENCES public.review(id);

ALTER TABLE ONLY public.review_report ADD CONSTRAINT "FK_review_report.account_id" FOREIGN KEY (account_id) REFERENCES public.account(id);

ALTER TABLE ONLY public.review_report ADD CONSTRAINT "FK_review_report.cause_id" FOREIGN KEY (cause_id) REFERENCES public.review_report_cause(id);

ALTER TABLE ONLY public.review_report ADD CONSTRAINT "FK_review_report.review_id" FOREIGN KEY (review_id) REFERENCES public.review(id);

ALTER TABLE ONLY public.account_report ADD CONSTRAINT "FK_user_report.cause_id" FOREIGN KEY (cause_id) REFERENCES public.account_report_cause(id);

ALTER TABLE ONLY public.account_report ADD CONSTRAINT "FK_user_report.reported_id" FOREIGN KEY (reported_id) REFERENCES public.account(id);

ALTER TABLE ONLY public.account_report ADD CONSTRAINT "FK_user_report.reportee_Id" FOREIGN KEY (reportee_id) REFERENCES public.account(id);

ALTER TABLE ONLY public.photographer_info ADD CONSTRAINT account_id FOREIGN KEY (account_id) REFERENCES public.account(id) NOT VALID;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.access_level TO ssbd02mok;

GRANT SELECT,INSERT,UPDATE ON TABLE public.access_level_assignment TO ssbd02mok;

GRANT SELECT,INSERT,UPDATE ON TABLE public.account TO ssbd02mok;

GRANT SELECT,INSERT,UPDATE ON TABLE public.account_report TO ssbd02mow;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.account_report_cause TO ssbd02mow;

GRANT SELECT ON TABLE public.authorization_view TO ssbd02glassfish;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.availability TO ssbd02mor;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.photo TO ssbd02mow;

GRANT SELECT,INSERT,DELETE ON TABLE public.photo_like TO ssbd02mow;

GRANT SELECT,INSERT,UPDATE ON TABLE public.photographer_info TO ssbd02mok;

GRANT SELECT,INSERT,UPDATE ON TABLE public.photographer_report TO ssbd02mow;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.photographer_report_cause TO ssbd02mow;

GRANT SELECT,INSERT,DELETE ON TABLE public.photographer_specialization TO ssbd02mow;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.reservation TO ssbd02mor;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.review TO ssbd02mow;

GRANT SELECT,INSERT,DELETE ON TABLE public.review_like TO ssbd02mow;

GRANT SELECT,INSERT,UPDATE ON TABLE public.review_report TO ssbd02mow;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.review_report_cause TO ssbd02mow;

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.specialization TO ssbd02mow;