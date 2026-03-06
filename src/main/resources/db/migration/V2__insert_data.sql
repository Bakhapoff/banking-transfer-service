INSERT INTO users (first_name, last_name, phone_number, passport_number, mail, created_at)
VALUES
    ('Mirbek',  'Atabekov',  '+996555909090', '21212211111296', 'matabekov@gmail.com',       NOW()),
    ('Temir',   'Nazarov',   '+996773808080', '24242244444296', 'tnazarov@gmail.com',         NOW()),
    ('Ivan',    'Ivanov',    '+996555100003', '21000000000003', 'ivan.ivanov@gmail.com',      NOW()),
    ('Sveta',   'Svetikova', '+996555100004', '21000000000004', 'sveta.svetikova@gmail.com',  NOW()),
    ('Petr',    'Petrov',    '+996555100005', '21000000000005', 'petr.petrov@gmail.com',      NOW()),
    ('Michael', 'Jackson',   '+996555100006', '21000000000006', 'michael.jackson@gmail.com',  NOW()),
    ('Naruto',  'Uzumaki',   '+996555100007', '21000000000007', 'naruto.uzumaki@gmail.com',   NOW()),
    ('Optimus', 'Prime',     '+996555100008', '21000000000008', 'optimus.prime@gmail.com',    NOW());

INSERT INTO accounts (account_number, balance, status, created_at, user_id)
VALUES
    ('1000000001', 1000, 'ACTIVE',  NOW(), 1),
    ('1000000002', 1000, 'ACTIVE',  NOW(), 2),
    ('1000000003', 1000, 'BLOCKED', NOW(), 3),
    ('1000000004', 1000, 'ACTIVE',  NOW(), 4),
    ('1000000005', 1000, 'ACTIVE',  NOW(), 5),
    ('1000000006', 1000, 'BLOCKED', NOW(), 6),
    ('1000000007', 1000, 'ACTIVE',  NOW(), 7),
    ('1000000008', 1000, 'ACTIVE',  NOW(), 8);