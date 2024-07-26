-- Category data insertion
INSERT INTO Category (id, name, color, image_url, description) VALUES (1, 'category1', 'red', 'image.jpg', '');
INSERT INTO Category (id, name, color, image_url, description) VALUES (2, 'category2', 'blue', 'image.jpg', '');
INSERT INTO Category (id, name, color, image_url, description) VALUES (3, 'category3', 'yellow', 'image.jpg', '');

-- Product data insertion
INSERT INTO Product (id, name, price, image_url, category_id) VALUES (1, 'product1', 1000, 'image.jpg', 1);
INSERT INTO Product (id, name, price, image_url, category_id) VALUES (2, 'product2', 2000, 'image.jpg', 2);
INSERT INTO Product (id, name, price, image_url, category_id) VALUES (3, 'product3', 3000, 'image.jpg', 3);

-- Option data insertion
INSERT INTO Option (id, name, quantity, product_id) VALUES (1, 'option1', 10, 1);
INSERT INTO Option (id, name, quantity, product_id) VALUES (2, 'option2', 20, 2);
INSERT INTO Option (id, name, quantity, product_id) VALUES (3, 'option3', 30, 3);

