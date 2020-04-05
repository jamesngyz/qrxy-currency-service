--liquibase formatted sql
--changeset jamesngyz:202003291900-1
--comment: create_currency_table

CREATE TABLE currency (
    id uuid PRIMARY KEY,
    code varchar(3) NOT NULL,
    name text NOT NULL,
    status text NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    created_by text NOT NULL,
    updated_at timestamptz NOT NULL DEFAULT now(),
    updated_by text NOT NULL,
    version integer NOT NULL,

    CONSTRAINT currency_code_uq  UNIQUE(code),
    CONSTRAINT currency_name_uq  UNIQUE(name)
);
