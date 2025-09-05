-- Categories
INSERT INTO categories (id, name) VALUES
(1, 'Electronics'), (2, 'Books'), (3, 'Clothing'), (4, 'Toys'), (5, 'Home Appliances'),
(6, 'Sports'), (7, 'Beauty'), (8, 'Automotive'), (9, 'Garden'), (10, 'Music'),
(11, 'Office'), (12, 'Grocery'), (13, 'Jewelry'), (14, 'Shoes'), (15, 'Movies'),
(16, 'Computers'), (17, 'Phones'), (18, 'Pets'), (19, 'Tools'), (20, 'Stationery');

-- Customers
INSERT INTO customers (id, name, email, address, password) VALUES
(1, 'alice', 'alice@example.com', '123 Park St', 'pass123'),
(2, 'bob', 'bob@example.com', '234 Maple Ave', 'pass234'),
(3, 'charlie', 'charlie@example.com', '345 Oak Dr', 'charlie456'),
(4, 'diana', 'diana@example.com', '456 Birch Ln', 'diana789'),
(5, 'emily', 'emily@example.com', '567 Elm Blvd', 'emily890'),
(6, 'frank', 'frank@example.com', '678 Willow Rd', 'frank100'),
(7, 'grace', 'grace@example.com', '789 Cedar Ct', 'grace200'),
(8, 'henry', 'henry@example.com', '890 Spruce Sq', 'henry300'),
(9, 'isabel', 'isabel@example.com', '901 Aspen Pl', 'isabel400'),
(10, 'jack', 'jack@example.com', '234 Pine Circle', 'jack123'),
(11, 'kate', 'kate@example.com', '121 King Dr', 'kate432'),
(12, 'luke', 'luke@example.com', '232 Queen St', 'luke453'),
(13, 'maya', 'maya@example.com', '343 Prince Ln', 'maya765'),
(14, 'nina', 'nina@example.com', '454 Duke Ave', 'nina678'),
(15, 'oliver', 'oliver@example.com', '565 Earl Dr', 'oliver876'),
(16, 'pat', 'pat@example.com', '676 Lady St', 'pat345'),
(17, 'quinn', 'quinn@example.com', '787 Lord Rd', 'quinn100'),
(18, 'rose', 'rose@example.com', '898 Regent Blvd', 'rose200'),
(19, 'sam', 'sam@example.com', '909 Monarch Pl', 'sam300'),
(20, 'tina', 'tina@example.com', '122 Emperor Ct', 'tina400');

-- Products
INSERT INTO products (id, name, description, price, stock_quantity, category_id) VALUES
(1, 'Laptop', 'High performance laptop', 800.00, 20, 1),
(2, 'Headphones', 'Wireless Bluetooth headphones', 120.00, 40, 1),
(3, 'Java Book', 'Java programming for beginners', 25.00, 100, 2),
(4, 'T-Shirt', '100% Cotton, M size', 10.99, 150, 3),
(5, 'Toy Car', 'Remote-controlled toy car', 30.50, 60, 4),
(6, 'Blender', '1.5L kitchen blender', 65.00, 25, 5),
(7, 'Football', 'Synthetic leather football', 19.99, 80, 6),
(8, 'Lipstick', 'Matte finish lipstick', 15.00, 70, 7),
(9, 'Car Cover', 'Universal size', 49.99, 35, 8),
(10, 'Plant Pot', 'Ceramic garden pot', 18.75, 40, 9),
(11, 'Guitar', 'Acoustic six-string guitar', 189.99, 12, 10),
(12, 'Notebook', 'A4 ruled notebook', 2.99, 200, 11),
(13, 'Organic Honey', 'Raw organic honey, 500g', 7.50, 85, 12),
(14, 'Gold Ring', '18k Gold ring', 199.99, 10, 13),
(15, 'Sneakers', 'Men, white, size 9', 58.99, 45, 14),
(16, 'DVD Movie', 'Comedy movie DVD', 8.50, 30, 15),
(17, 'Dell PC', 'Desktop computer', 500.00, 17, 16),
(18, 'iPhone', 'Latest iPhone model', 999.99, 35, 17),
(19, 'Dog Food', 'Premium dry dog food', 45.00, 90, 18),
(20, 'Wrench Set', 'Steel wrench set', 25.50, 55, 19);

-- Orders
INSERT INTO orders (id, order_date, total_amount, order_status, customer_id) VALUES
(1,  '2024-08-23 14:34:56', 800.00, 'PENDING', 1),
(2,  '2024-08-24 10:02:17', 120.00, 'SHIPPED', 2),
(3,  '2024-08-25 09:51:03', 25.00, 'DELIVERED', 3),
(4,  '2024-08-26 16:42:14', 21.98, 'PENDING', 4),
(5,  '2024-08-26 17:23:16', 61.00, 'SHIPPED', 5),
(6,  '2024-08-27 12:48:56', 19.99, 'DELIVERED', 6),
(7,  '2024-08-28 18:39:45', 15.00, 'CANCELLED', 7),
(8,  '2024-08-28 20:15:24', 49.99, 'DELIVERED', 8),
(9,  '2024-08-29 11:15:33', 18.75, 'PENDING', 9),
(10, '2024-08-29 12:20:43', 189.99, 'SHIPPED', 10),
(11, '2024-08-29 13:55:25', 2.99, 'DELIVERED', 11),
(12, '2024-08-29 14:24:51', 7.50, 'CANCELLED', 12),
(13, '2024-08-29 15:10:11', 199.99, 'DELIVERED', 13),
(14, '2024-08-29 17:33:23', 58.99, 'DELIVERED', 14),
(15, '2024-08-29 18:12:29', 8.50, 'PENDING', 15),
(16, '2024-08-29 19:16:39', 500.00, 'SHIPPED', 16),
(17, '2024-08-29 20:17:53', 999.99, 'DELIVERED', 17),
(18, '2024-08-30 07:56:06', 45.00, 'SHIPPED', 18),
(19, '2024-08-30 09:11:44', 25.50, 'DELIVERED', 19),
(20, '2024-08-30 12:34:01', 10.99, 'PENDING', 20);

-- Order Items
INSERT INTO order_items (id, product_name, price, quantity, order_id, product_id) VALUES
(1, 'Laptop', 800.00, 1, 1, 1),
(2, 'Headphones', 120.00, 1, 2, 2),
(3, 'Java Book', 25.00, 1, 3, 3),
(4, 'T-Shirt', 10.99, 2, 4, 4),
(5, 'Toy Car', 30.50, 2, 5, 5),
(6, 'Blender', 61.00, 1, 5, 6),
(7, 'Football', 19.99, 1, 6, 7),
(8, 'Lipstick', 15.00, 1, 7, 8),
(9, 'Car Cover', 49.99, 1, 8, 9),
(10, 'Plant Pot', 18.75, 1, 9, 10),
(11, 'Guitar', 189.99, 1, 10, 11),
(12, 'Notebook', 2.99, 1, 11, 12),
(13, 'Organic Honey', 7.50, 1, 12, 13),
(14, 'Gold Ring', 199.99, 1, 13, 14),
(15, 'Sneakers', 58.99, 1, 14, 15),
(16, 'DVD Movie', 8.50, 1, 15, 16),
(17, 'Dell PC', 500.00, 1, 16, 17),
(18, 'iPhone', 999.99, 1, 17, 18),
(19, 'Dog Food', 45.00, 1, 18, 19),
(20, 'Wrench Set', 25.50, 1, 19, 20);

-- Cart Items
INSERT INTO cart_items (id, quantity, product_id, customer_id) VALUES
(1, 2, 1, 2), (2, 1, 3, 5), (3, 1, 5, 1), (4, 3, 8, 7), (5, 1, 9, 12),
(6, 2, 10, 15), (7, 2, 4, 20), (8, 1, 14, 11), (9, 1, 7, 8), (10, 2, 2, 3),
(11, 1, 6, 18), (12, 4, 13, 4), (13, 2, 18, 17), (14, 5, 16, 13), (15, 1, 11, 16),
(16, 3, 20, 14), (17, 1, 12, 6), (18, 2, 17, 9), (19, 2, 15, 10), (20, 1, 19, 19);

-- All IDs are unique and references (foreign keys) relate correctly.
