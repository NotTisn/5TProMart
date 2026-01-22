-- =============================================================================
-- 5TPROMART - Seed Data: Promotions
-- =============================================================================
-- Part 6 of modular seed system
-- =============================================================================

\echo '→ Seeding Promotions...'

-- Insert promotions
INSERT INTO promotions (
    promotion_id,
    promotion_name,
    promotion_description,
    promotion_type,
    discount_percent,
    buy_quantity,
    get_quantity,
    start_date,
    end_date,
    status,
    is_active
) VALUES
    (
        'promo-001',
        'New Year Sale - Electronics',
        'Get 15% off on all electronic items',
        'PERCENTAGE_DISCOUNT',
        15,
        NULL,
        NULL,
        CURRENT_DATE - INTERVAL '2 days',
        CURRENT_DATE + INTERVAL '1 day',
        'ACTIVE',
        true
    ),
    (
        'promo-002',
        'Buy 2 Get 1 - Beverages',
        'Buy 2 beverages, get 1 free',
        'BUY_X_GET_Y',
        NULL,
        2,
        1,
        CURRENT_DATE - INTERVAL '5 days',
        CURRENT_DATE + INTERVAL '16 days',
        'ACTIVE',
        true
    ),
    (
        'promo-003',
        'Summer Discount - Personal Care',
        '20% discount on personal care products',
        'PERCENTAGE_DISCOUNT',
        20,
        NULL,
        NULL,
        CURRENT_DATE - INTERVAL '5 days',
        CURRENT_DATE + INTERVAL '11 days',
        'ACTIVE',
        true
    )
ON CONFLICT (promotion_id) DO NOTHING;

-- Link products to promotions
-- Note: id column is auto-generated (IDENTITY), product_name is optional
INSERT INTO promotion_products (promotion_id, product_id, product_name) VALUES
    -- Electronics promotion
    ('promo-001', 'prod-001', 'USB Flash Drive 32GB'),
    ('promo-001', 'prod-002', 'AA Batteries (4-pack)'),
    ('promo-001', 'prod-003', 'Phone Charger Cable'),
    
    -- Beverages promotion
    ('promo-002', 'prod-009', 'Coca-Cola 330ml'),
    ('promo-002', 'prod-010', 'Bottled Water 500ml'),
    ('promo-002', 'prod-011', 'Orange Juice 1L'),
    
    -- Personal care promotion
    ('promo-003', 'prod-016', 'Shampoo 400ml'),
    ('promo-003', 'prod-017', 'Body Soap Bar'),
    ('promo-003', 'prod-018', 'Toothpaste 150g'),
    ('promo-003', 'prod-019', 'Facial Tissues (3-pack)')
ON CONFLICT DO NOTHING;

\echo '  ✓ Promotions seeded (3 active promotions)'
