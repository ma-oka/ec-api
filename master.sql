create table if not exists size_data(
  size_id serial primary key,
  size_name text not null,
  min_height integer not null,
  max_height integer not null,
  min_chest integer not null,
  max_chest integer not null,
  min_waist integer not null,
  max_waist integer not null);

create table if not exists items(
  item_id serial primary key,
  item_name text not null,
  item_price integer,
  item_quantity integer,
  size_id integer not null references size_data(size_id),
  created_at timestamptz,
  updated_at timestamptz,
  deleted_at timestamptz);

create table if not exists users(
  user_id serial primary key,
  user_name text not null,
  user_address text not null);

create table if not exists orders(
  order_id serial primary key,
  item_id integer references items(item_id),
  order_price integer not null,
  order_quantity integer not null,
  user_id integer references users(user_id),
  order_status integer not null,
  created_at timestamptz,
  updated_at timestamptz);

create table if not exists body_data(
  body_data_id serial,
  user_id integer references users(user_id),
  body_height integer,
  body_chest integer,
  body_waist integer);