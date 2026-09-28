create table if not exists applications (
    id text primary key,
    name text not null unique,
    display_name text not null,
    command text not null,
    arguments text,
    process_name text,
    enabled integer not null default 1
);

create table if not exists workspaces (
    id text primary key,
    name text not null unique,
    display_name text not null,
    project_path text,
    enabled integer not null default 1
);

create table if not exists workspace_applications (
    workspace_id text not null,
    application_id text not null,
    sort_order integer not null default 0,
    primary key (workspace_id, application_id),
    foreign key (workspace_id) references workspaces(id),
    foreign key (application_id) references applications(id)
);

create table if not exists workspace_urls (
    id integer primary key autoincrement,
    workspace_id text not null,
    url text not null,
    display_name text not null,
    sort_order integer not null default 0,
    foreign key (workspace_id) references workspaces(id)
);

create table if not exists settings (
    name text primary key,
    value text not null
);

create table if not exists command_history (
    id integer primary key autoincrement,
    command text not null,
    response text not null,
    created_at text not null,
    status text not null
);
