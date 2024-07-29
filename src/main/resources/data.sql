-- Insert member
INSERT INTO member (email, password) VALUES ('user1@example.com', '111');
INSERT INTO member (email, password) VALUES ('user3@example.com', '333');
INSERT INTO member (email, password) VALUES ('user2@example.com', '222');

-- Insert category
INSERT INTO category (name, color, image_url) VALUES ('cat1', 'color1', 'cat1_image.jpg');
INSERT INTO category (name, color, image_url) VALUES ('cat2', 'color2', 'cat2_image.jpg');
INSERT INTO category (name, color, image_url) VALUES ('cat3', 'color3', 'cat3_image.jpg');

-- Insert products
INSERT INTO product (name, price, image_url, category_id)
    VALUES ('Product 1', 1000, 'http://example.com/product1.jpg', 1);
INSERT INTO product (name, price, image_url, category_id)
    VALUES ('Product 2', 2000, 'http://example.com/product2.jpg', 2);
INSERT INTO product (name, price, image_url, category_id)
    VALUES ('Product 3', 3000, 'http://example.com/product3.jpg', 3);

-- Insert Options
INSERT INTO option (name, quantity, product_id)
    VALUES ('Option 1 for Product 1', 10, 1);
INSERT INTO option (name, quantity, product_id)
    VALUES ('Option 1 for Product 2', 20, 2);
INSERT INTO option (name, quantity, product_id)
    VALUES ('Option 1 for Product 3', 30, 3);

-- Insert wishlist items
INSERT INTO wishlist (member_id, product_id) VALUES (1, 1);
INSERT INTO wishlist (member_id, product_id) VALUES (1, 2);
INSERT INTO wishlist (member_id, product_id) VALUES (2, 2);
INSERT INTO wishlist (member_id, product_id) VALUES (2, 3);
INSERT INTO wishlist (member_id, product_id) VALUES (3, 1);
INSERT INTO wishlist (member_id, product_id) VALUES (3, 3);
