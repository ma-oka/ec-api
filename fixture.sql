insert into users(user_id, user_password, user_name, user_gender, user_phone_number, user_zip, user_address, created_at, updated_at)
  values(1, 'aiueo', 'yamada', 'male', '08011223344', '1001000', 'okafyama', null, null),
        (2, 'kakikukeko', 'suzuki', 'female', '09012345678', '2003000', 'tokyo', null, null),
        (3, 'sashisuseso', 'yamada', 'male', '07098765432', '7002000', 'osaka', null, null);

insert into size_data(size_id, size_name, min_height, max_height, min_chest, max_chest, min_waist, max_waist)
  values(1, 'S', 1550, 1650, 800, 880, 680, 760),
        (2, 'M', 1650, 1750, 880, 960, 760, 840),
        (3, 'L', 1750, 1850, 960, 1040, 840, 940);

insert into body_data(body_data_id, user_id, body_height, body_chest, body_waist)
  values(1, 2, 1605, 820, 700),
        (2, 3, 1720, 890, 780),
        (3, 1, 1805, 980, 850);

insert into items(item_id, item_name, size_id, item_price, item_quantity, created_at, updated_at, deleted_at)
  values(1, 'シャツ', 1, 1000, 100, null, null, null),
        (2, 'シャツ', 2, 900, 150, null, null, null);
