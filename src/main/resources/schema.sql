create table if not exists member (
    id          BIGINT          not null AUTO_INCREMENT,
    email       VARCHAR(255)    not null,
    password    VARCHAR(255)    not null,
    PRIMARY KEY (id)
);

create table if not exists category (
    id          BIGINT          not null AUTO_INCREMENT,
    name        VARCHAR(255)    not null,
    color       VARCHAR(7)      not null,
    image_url   VARCHAR(255)    not null,
    description VARCHAR(255),
    PRIMARY KEY (id)
);

create table if not exists product (
    id          BIGINT          not null AUTO_INCREMENT,
    name        VARCHAR(15)     not null,
    price       INTEGER         not null,
    image_url   VARCHAR(255)    not null,
    category_id BIGINT          not null,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id)   REFERENCES category(id)
);

create table if not exists option (
    id          BIGINT          not null AUTO_INCREMENT,
    name        VARCHAR(50)     not null,
    quantity    INTEGER         not null,
    product_id  BIGINT          not null,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id)    REFERENCES product(id)
);

create table if not exists wish (
    id          BIGINT          not null AUTO_INCREMENT,
    member_id   BIGINT          not null,
    product_id  BIGINT          not null,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id)     REFERENCES member(id),
    FOREIGN KEY (product_id)    REFERENCES product(id)
);

create table if not exists orders (
    id              BIGINT          not null AUTO_INCREMENT,
    option_id       BIGINT          not null,
    quantity        INTEGER         not null,
    message         VARCHAR(255),
    order_date_time TIMESTAMP       not null,
    PRIMARY KEY (id),
    FOREIGN KEY (option_id)         REFERENCES option(id)
);


alter table member
    add constraint uk_member unique (email);

alter table category
    add constraint uk_category unique (name);