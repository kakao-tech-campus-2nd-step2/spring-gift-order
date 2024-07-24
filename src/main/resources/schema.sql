DROP TABLE IF EXISTS wish_lists;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS category;

CREATE TABLE users (
                       id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY COMMENT '사용자 ID',
                       email VARCHAR(255) NOT NULL UNIQUE COMMENT '사용자 이메일',
                       password VARCHAR(255) NOT NULL COMMENT '사용자 비밀번호'
);

CREATE TABLE products (
                          id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY COMMENT '상품 ID',
                          name VARCHAR(15) NOT NULL COMMENT '상품 이름',
                          price INTEGER NOT NULL COMMENT '상품 가격',
                          image_url VARCHAR(255) NOT NULL COMMENT '상품 이미지 URL',
                          category_id BIGINT NOT NULL COMMENT '카테고리 ID'
);


CREATE TABLE wish_lists (
                            id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY COMMENT '위시리스트 ID',
                            member_id BIGINT NOT NULL COMMENT '사용자 ID',
                            product_id BIGINT NOT NULL COMMENT '상품 ID',
                            FOREIGN KEY (member_id) REFERENCES users(id),
                            FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE category (
                          id BIGINT GENERATED BY DEFAULT AS IDENTITY,
                          name VARCHAR(255) NOT NULL COMMENT '카테고리 항목 이름',
                          PRIMARY KEY (id)
);

CREATE TABLE options (
                         id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY COMMENT '옵션 ID',
                         name VARCHAR(50) NOT NULL COMMENT '옵션 이름',
                         quantity INTEGER NOT NULL COMMENT '옵션 수량',
                         product_id BIGINT NOT NULL COMMENT '상품 ID',
                         CONSTRAINT unique_option_name_per_product UNIQUE (name, product_id),
                         FOREIGN KEY (product_id) REFERENCES products(id)
);