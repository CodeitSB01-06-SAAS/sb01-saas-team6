--의상 속성
INSERT INTO attributes_defs (id, name, selectable_values, created_at, updated_at)
VALUES
    ('15723d92-6654-4976-8034-0b34a0a2ccf7', '촉감', ARRAY['부드러움', '뻣뻣함'], '2025-07-02T02:36:04.644+00:00', null),
    ('25723d92-6654-4976-8034-0b34a0a2ccf7', '사이즈', ARRAY['S', 'M', 'L', 'XL'], '2025-07-02T00:31:58.223283Z', null),
    ('35723d92-6654-4976-8034-0b34a0a2ccf7', '색', ARRAY['레드', '그린'], '2025-07-02T00:34:58.223283Z', null),
    ('45723d92-6654-4976-8034-0b34a0a2ccf7', '계절', ARRAY['봄', '여름','가을', '겨울'], '2025-07-01T00:34:58.223283Z', null);


--User
INSERT INTO users(id, email, password, name, role, locked, created_at, linked_oauth_providers)
--테스트유저의 실 비밀번호는 test1234!
VALUES
    ('6e54fc4a-f8f0-478d-a54e-ebe573316061', 'test@test.com', '$2a$10$28/I0KoP1GKVXGg22B6PQeMRYqKvMUqJ7E7XWhIIDHHISpJhjnoIG', '테스트유저', 'USER', false, '2025-07-08 09:21:33.619871', null );

--Profiles
INSERT INTO profiles (id, name, gender, birth_date, latitude, longitude , x, y, location_names, temperature_sensitivity, profile_image_url, created_at, updated_at)
VALUES
    ('6e54fc4a-f8f0-478d-a54e-ebe573316061', '테스트유저', 'OTHER', '2025-07-08', 0, 0, 0, 0, null, 3, null, '2025-07-08 09:21:33.624865', '2025-07-08 09:21:33.624865');