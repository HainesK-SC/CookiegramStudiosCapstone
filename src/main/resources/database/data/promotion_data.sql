-- SCRIPT TO POPULATE THE PROMOTION TABLE WITH SEED DATA
-- Recent edit: 2026-02-24 - fixed name
-- SPRING5 PROMOTION
INSERT INTO promotions (promo_code, description, promo_type, promo_value, start_date, end_date, is_active)
VALUES (
           'SPRING5',
           'Seasonal spring discount',
           'FIXED',
           5.00,
           '2026-03-01',
           '2027-05-31',
           true
       );

-- SWEET25 PROMOTION
INSERT INTO promotions (promo_code, description, promo_type, promo_value, start_date, end_date, is_active)
VALUES (
           'SWEET25',
           'Discount off entire order',
           'PERCENTAGE',
           25.00,
           '2026-04-01',
           '2026-06-30',
           true
       );

-- BDAY10 PROMOTION
INSERT INTO promotions (promo_code, description, promo_type, promo_value, start_date, end_date, is_active)
VALUES (
           'BDAY10',
           'Birthday celebration discount',
           'FIXED',
           10.00,
           '2026-01-01',
           '2026-12-31',
           false
       );

-- FREESHIP PROMOTION
INSERT INTO promotions (promo_code, description, promo_type, promo_value, start_date, end_date, is_active)
VALUES (
           'FREESHIP',
           'Free shipping on any order',
           'FIXED',
           10.00,
           '2026-02-01',
           '2027-07-01',
           true
       );

-- HOLIDAY15 PROMOTION
INSERT INTO promotions (promo_code, description, promo_type, promo_value, start_date, end_date, is_active)
VALUES (
           'HOLIDAY15',
           'Holiday season savings',
           'PERCENTAGE',
           15.00,
           '2026-11-01',
           '2026-12-31',
           false
       );