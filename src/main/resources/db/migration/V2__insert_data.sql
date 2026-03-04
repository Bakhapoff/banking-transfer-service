INSERT INTO users (first_name, last_name, phone_number, passport_number, mail, created_at)
VALUES (
        'Mirbek',
        'Atabekov',
        '+996555909090',
        '21212211111296',
        'matabekov@gmail.com',
        NOW()
       );

INSERT INTO users (first_name, last_name, phone_number, passport_number, mail, created_at)
VALUES (
           'Temir',
           'Nazarov',
           '+996773808080',
           '24242244444296',
           'tnazarov@gmail.com',
           NOW()
       );

INSERT INTO accounts (account_number, balance, status, created_at, user_id)
VALUES (
        '1000000001',
        1000,
        'ACTIVE',
        NOW(),
        1
       );

INSERT INTO accounts (account_number, balance, status, created_at, user_id)
VALUES (
           '1000000002',
           1000,
           'ACTIVE',
           NOW(),
           2
       );