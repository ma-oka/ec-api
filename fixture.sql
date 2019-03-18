insert into users(user_id, user_name, user_address)
  values(1, 'yamada', 'okafyama'),
        (2, 'suzuki', 'tokyo'),
        (3, 'yamada', 'osaka');

insert into size_data(size_id, size_name, min_height, max_height, min_chest, max_chest, min_waist, max_waist)
  values(1, 'S', 1550, 1650, 800, 880, 680, 760),
        (2, 'M', 1650, 1750, 880, 960, 760, 840),
        (3, 'L', 1750, 1850, 960, 1040, 840, 940);

insert into body_data(user_id, body_height, body_chest, body_waist)
  values(1, 1605, 820, 700),
        (2, 1720, 890, 780),
        (3, 1805, 980, 850);

insert into items(item_id, item_name, size_id, item_price, item_quantity, created_at, updated_at, deleted_at)
  values(1, 'シャツ', 1, 1000, 100, null, null, null),
        (2, 'シャツ', 2, 900, 150, null, null, null);
