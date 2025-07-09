--User
INSERT INTO users(id, email, password, name, role, locked, created_at, linked_oauth_providers)
--테스트유저의 실 비밀번호는 test1234!
VALUES
    ('6e54fc4a-f8f0-478d-a54e-ebe573316061', 'test@test.com', '$2a$10$28/I0KoP1GKVXGg22B6PQeMRYqKvMUqJ7E7XWhIIDHHISpJhjnoIG', '테스트유저', 'USER', false, '2025-07-08 09:21:33.619871', null );

--Profiles
INSERT INTO profiles (id, name, gender, birth_date, latitude, longitude , x, y, location_names, temperature_sensitivity, profile_image_url, created_at, updated_at)
VALUES
    ('6e54fc4a-f8f0-478d-a54e-ebe573316061', '테스트유저', 'OTHER', '2025-07-08', 0, 0, 0, 0, null, 3, null, '2025-07-08 09:21:33.624865', '2025-07-08 09:21:33.624865');



--의상 속성
INSERT INTO attributes_defs (id, name, selectable_values, created_at, updated_at)
VALUES ('15723d92-6654-4976-8034-0b34a0a2ccf7', '촉감', ARRAY['부드러움', '뻣뻣함'],
        '2025-07-02T02:36:04.644+00:00', null),
       ('25723d92-6654-4976-8034-0b34a0a2ccf7', '사이즈', ARRAY['XS', 'S', 'M', 'L', 'XL', 'XXL'],
        '2025-07-02T00:31:58.223283Z', null),
       ('35723d92-6654-4976-8034-0b34a0a2ccf7', '색', ARRAY['화이트', '그레이', '레드', '그린', '블루', '핑크',
        '블랙'], '2025-07-02T00:34:58.223283Z', null),
       ('45723d92-6654-4976-8034-0b34a0a2ccf7', '계절', ARRAY['봄', '여름', '가을', '겨울'],
        '2025-07-01T00:34:58.223283Z', null),
       ('55723d92-6654-4976-8034-0b34a0a2ccf7', '두께감', ARRAY['두꺼움', '약간 두꺼움', '약간 얇음', '얇음'],
        '2025-07-08T00:34:58.223283Z', null),
       ('65723d92-6654-4976-8034-0b34a0a2ccf7', '따뜻한 정도', ARRAY['따뜻함', '시원함' ],
        '2025-07-08T00:34:58.223283Z', null),
       ('75723d92-6654-4976-8034-0b34a0a2ccf7', '스타일', ARRAY['캐주얼', '스트릿', '스포티', '미니멀', '시크',
        '레트로' ], '2025-07-08T00:34:58.223283Z', null),
       ('85723d92-6654-4976-8034-0b34a0a2ccf7', '신축성', ARRAY['없음', '약간 있음', '있음' ],
        '2025-07-08T00:34:58.223283Z', null);


--User
INSERT INTO users(id, email, password, name, role, locked, created_at, linked_oauth_providers)
--테스트유저의 실 비밀번호는 test1234!
VALUES ('6e54fc4a-f8f0-478d-a54e-ebe573316061', 'test@test.com',
        '$2a$10$28/I0KoP1GKVXGg22B6PQeMRYqKvMUqJ7E7XWhIIDHHISpJhjnoIG', '테스트유저', 'USER', false,
        '2025-07-08 09:21:33.619871', null),
       ('77777777-7777-7777-7777-777777777777', 'hjhj@naver.com',
        '$2a$10$28/I0KoP1GKVXGg22B6PQeMRYqKvMUqJ7E7XWhIIDHHISpJhjnoIG',
        'hjhj', 'USER', FALSE, CURRENT_TIMESTAMP, NULL);


--Profiles
INSERT INTO profiles (id, name, gender, birth_date, latitude, longitude, x, y, location_names,
                      temperature_sensitivity, profile_image_url, created_at, updated_at)
VALUES ('6e54fc4a-f8f0-478d-a54e-ebe573316061', '테스트유저', 'OTHER', '2025-07-08', 37.37471572625859,
        127.14516640412474, 63, 123, null, 3, null, '2025-07-08 09:21:33.624865',
        '2025-07-08 09:21:33.624865'),
       ('77777777-7777-7777-7777-777777777777', 'hjhj', 'OTHER', '1990-01-01',
        37.4276096, 126.9006336, 59, 124,
        NULL, 3, NULL,
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

/* ---------- profile_location_names 더미 ---------- */
INSERT INTO profile_location_names (profile_id, location_name)
VALUES ('6e54fc4a-f8f0-478d-a54e-ebe573316061', '경기도'),
       ('6e54fc4a-f8f0-478d-a54e-ebe573316061', '성남시 분당구'),
       ('6e54fc4a-f8f0-478d-a54e-ebe573316061', '분당동'),
       ('6e54fc4a-f8f0-478d-a54e-ebe573316061', ''),
       ('77777777-7777-7777-7777-777777777777', '경기도'),
       ('77777777-7777-7777-7777-777777777777', '안양시 만안구'),
       ('77777777-7777-7777-7777-777777777777', '석수동'),
       ('77777777-7777-7777-7777-777777777777', '');


/* ---------- weathers 더미 ---------- */
INSERT INTO weathers (id, forecasted_at, forecast_at, lat, lon, grid_x, grid_y, sky_status,
                      precipitation_type, temperature_current, temperature_min, temperature_max,
                      precipitation_amount, precipitation_amount_text, precipitation_probability,
                      humidity, snow_amount, lightning, wind_speed, wind_level, wind_direction,
                      wind_u, wind_v)
VALUES
    ('15723d92-6654-4976-8034-0b34a0a2ccf7', '촉감', ARRAY['부드러움', '뻣뻣함'], '2025-07-02T02:36:04.644+00:00', null),
    ('25723d92-6654-4976-8034-0b34a0a2ccf7', '사이즈', ARRAY['XS','S', 'M', 'L', 'XL', 'XXL'], '2025-07-02T00:31:58.223283Z', null),
    ('35723d92-6654-4976-8034-0b34a0a2ccf7', '색', ARRAY['화이트', '그레이', '레드', '그린', '블루', '핑크', '블랙'], '2025-07-02T00:34:58.223283Z', null),
    ('45723d92-6654-4976-8034-0b34a0a2ccf7', '계절', ARRAY['봄', '여름','가을', '겨울'], '2025-07-01T00:34:58.223283Z', null),
    ('55723d92-6654-4976-8034-0b34a0a2ccf7', '두께감', ARRAY['두꺼움','약간 두꺼움', '약간 얇음', '얇음'], '2025-07-08T00:34:58.223283Z', null),
    ('65723d92-6654-4976-8034-0b34a0a2ccf7', '따뜻한 정도', ARRAY['따뜻함', '시원함' ], '2025-07-08T00:34:58.223283Z', null),
    ('75723d92-6654-4976-8034-0b34a0a2ccf7', '스타일', ARRAY['캐주얼', '스트릿', '스포티', '미니멀', '시크', '레트로' ], '2025-07-08T00:34:58.223283Z', null),
    ('85723d92-6654-4976-8034-0b34a0a2ccf7', '신축성', ARRAY['없음', '약간 있음', '있음' ], '2025-07-08T00:34:58.223283Z', null);

-- 의상
INSERT INTO clothes (id, owner_id, name, type, image_url, created_at, updated_at)
VALUES
    --테스트 유저 의상
    ('e68bd02c-347b-4c9b-8243-353e8e7ae959', '6e54fc4a-f8f0-478d-a54e-ebe573316061', '상의', 'TOP', null,  '2025-07-09 07:43:36.588879', '2025-07-09 07:43:36.588879'
       );
    /* ── 63 / 123 ─────────────────────────────────────────────────────────── */
    ('9af5b946-d51b-4369-aa8a-cefcc7c799aa', '2025-07-08 20:00:00', '2025-07-12 15:00:00',
        37.37471572625859, 127.14516640412474, 63, 123, 'CLEAR', 'NONE', 25, 25, 25, 0, NULL, 0, 75,
        NULL, NULL, 1, NULL, NULL, NULL, NULL),
       ('931284c4-f041-417e-b460-e9aa17aa41e4', '2025-07-08 20:00:00', '2025-07-11 15:00:00',
        37.37471572625859, 127.14516640412474, 63, 123, 'CLEAR', 'NONE', 28, 23, 34, 0, NULL, 0,
        55.6, NULL, NULL, 1.1, NULL, NULL, NULL, NULL),
       ('d1f8b458-c1e9-400b-8fe8-5866469bbd3a', '2025-07-08 20:00:00', '2025-07-10 15:00:00',
        37.37471572625859, 127.14516640412474, 63, 123, 'CLEAR', 'NONE', 27.2, 22, 34, 0, NULL, 0,
        52.9, NULL, NULL, 2.4, NULL, NULL, NULL, NULL),
       ('d4bc5c07-6de7-4c5e-bfb5-9f2dd242474b', '2025-07-08 20:00:00', '2025-07-09 15:00:00',
        37.37471572625859, 127.14516640412474, 63, 123, 'CLEAR', 'NONE', 29, 24, 34, 0, NULL, 0,
        53.8, NULL, NULL, 3.4, NULL, NULL, NULL, NULL),
       ('1bad4311-d010-4be4-b7e1-76c6dea994a1', '2025-07-08 20:00:00', '2025-07-08 15:00:00',
        37.37471572625859, 127.14516640412474, 63, 123, 'MOSTLY_CLOUDY', 'NONE', 31.7, 31.7, 36, 0,
        NULL, 20, 59.2, NULL, NULL, 2.7, NULL, NULL, NULL, NULL),
    /* ── 59 / 124 ─────────────────────────────────────────────────────────── */
       ('08f1532e-b8af-4a01-9aa2-4a3f4387e4b0','2025-07-09 05:00:00','2025-07-13 15:00:00',
        37.4276096,126.9006336,59,124,
        'CLEAR','NONE',26,24,31,0,NULL,10,68,NULL,NULL,0.9,NULL,NULL,NULL,
        '2025-07-09 08:29:03','2025-07-09 08:29:03'),
       ('195f29cd-05ad-4e7f-9bc0-57f68a56ab56','2025-07-09 05:00:00','2025-07-12 15:00:00',
        37.4276096,126.9006336,59,124,
        'CLEAR','NONE',27,23,32,0,NULL,5,63,NULL,NULL,1.3,NULL,NULL,NULL,
        '2025-07-09 08:29:03','2025-07-09 08:29:03'),
       ('2c2710a6-7f61-4a0e-a5c1-6045e1d7d3c9','2025-07-09 05:00:00','2025-07-11 15:00:00',
        37.4276096,126.9006336,59,124,
        'CLEAR','NONE',28,22,33,0,NULL,0,60,NULL,NULL,1.8,NULL,NULL,NULL,
        '2025-07-09 08:29:03','2025-07-09 08:29:03'),
       ('3ca5a9f3-4207-4e17-8780-9f057e2e5d33','2025-07-09 05:00:00','2025-07-10 15:00:00',
        37.4276096,126.9006336,59,124,
        'MOSTLY_CLOUDY','NONE',29.5,25,34,0,NULL,0,58,NULL,NULL,2.2,NULL,NULL,NULL,
        '2025-07-09 08:29:03','2025-07-09 08:29:03'),
       ('4d84b62b-c483-4e08-8615-807a4b0d62e8','2025-07-09 05:00:00','2025-07-09 15:00:00',
        37.4276096,126.9006336,59,124,
        'CLOUDY','NONE',31.1,27,35,0,NULL,20,55,NULL,NULL,3.0,NULL,NULL,NULL,
        '2025-07-09 08:29:03','2025-07-09 08:29:03');


-- 의상 속성 중간테이블
INSERT INTO clothes_attributes (id, clothes_id, attribute_id, value, created_at, updated_at)
/* ---------- weather_location_names 더미 ---------- */
INSERT INTO weather_location_names (id, weather_id, location_name)
VALUES
    --테스트 유저의 '상의' 의상 속성 중간 정보
    ('855c08b1-af6d-488e-aff0-d0a624fa0b66', 'e68bd02c-347b-4c9b-8243-353e8e7ae959', '45723d92-6654-4976-8034-0b34a0a2ccf7', '여름','2025-07-09 07:43:36.588879', '2025-07-09 07:43:36.588879'
    ),
    ('4e6c22a7-4bcb-4d63-b5cd-fe067fce2344', 'e68bd02c-347b-4c9b-8243-353e8e7ae959', '55723d92-6654-4976-8034-0b34a0a2ccf7', '얇음','2025-07-09 07:43:36.588879', '2025-07-09 07:43:36.588879'
    );




-- ①
('62b3554e-9091-42f7-ab61-b184fc4e1df4', '9af5b946-d51b-4369-aa8a-cefcc7c799aa', '경기도'),
('31025efa-94fa-43fc-b594-f299b5d9977f', '9af5b946-d51b-4369-aa8a-cefcc7c799aa', '성남시 분당구'),
('e9d3bbdc-734e-4792-8203-ca13e8adc0d7', '9af5b946-d51b-4369-aa8a-cefcc7c799aa', '분당동'),
('6fbb8d0b-ae07-466f-be6e-2777ab017feb', '9af5b946-d51b-4369-aa8a-cefcc7c799aa', ''),

('7e8b39d7-4631-4473-8de2-de5db463526f', '931284c4-f041-417e-b460-e9aa17aa41e4', '경기도'),
('e8cae376-d5e9-466a-8e2f-c89931bcbe79', '931284c4-f041-417e-b460-e9aa17aa41e4', '성남시 분당구'),
('9849fb8c-c5af-424e-a761-7a45bd6a8f8d', '931284c4-f041-417e-b460-e9aa17aa41e4', '분당동'),
('3ca6939c-f006-4123-b12c-8cd64372a987', '931284c4-f041-417e-b460-e9aa17aa41e4', ''),

('3555f40a-33fe-41be-91b2-048d8c01525f', 'd1f8b458-c1e9-400b-8fe8-5866469bbd3a', '경기도'),
('a5c296a7-4963-4c6d-8980-92afb0c11084', 'd1f8b458-c1e9-400b-8fe8-5866469bbd3a', '성남시 분당구'),
('a5fd628c-c515-4f6f-aaeb-927decefc85e', 'd1f8b458-c1e9-400b-8fe8-5866469bbd3a', '분당동'),
('d7b10b76-756b-466b-9f46-d7294eb34efc', 'd1f8b458-c1e9-400b-8fe8-5866469bbd3a', ''),

('850c1b95-8bb1-4ed1-adf9-8a0e805f639f', 'd4bc5c07-6de7-4c5e-bfb5-9f2dd242474b', '경기도'),
('7f8ca29f-db89-460e-b3b1-5bc73625a678', 'd4bc5c07-6de7-4c5e-bfb5-9f2dd242474b', '성남시 분당구'),
('385e3bc4-164e-413f-b7b0-9520cface181', 'd4bc5c07-6de7-4c5e-bfb5-9f2dd242474b', '분당동'),
('d0b6d1ff-ff0c-49b8-b402-41b1f493ca42', 'd4bc5c07-6de7-4c5e-bfb5-9f2dd242474b', ''),

('0b8adec0-d927-4e49-a236-e692ac1431e9', '1bad4311-d010-4be4-b7e1-76c6dea994a1', '경기도'),
('0ab5dd7c-b2fd-4831-bd70-cdd54c7f5170', '1bad4311-d010-4be4-b7e1-76c6dea994a1', '성남시 분당구'),
('bdc65873-9c15-4e59-acad-3603d2ccd4cc', '1bad4311-d010-4be4-b7e1-76c6dea994a1', '분당동'),
('259603de-49f5-465a-9ee2-0b1324b701ea', '1bad4311-d010-4be4-b7e1-76c6dea994a1', ''),

('74d5d832-31e6-4ffd-9aa4-b46a5065da3f', '08f1532e-b8af-4a01-9aa2-4a3f4387e4b0', '경기도'),
('895f1d8e-d437-4b75-846b-bd0726d62999', '08f1532e-b8af-4a01-9aa2-4a3f4387e4b0', '안양시 만안구'),
('8c00ce7a-ea19-4874-9d2b-f7015d875147', '08f1532e-b8af-4a01-9aa2-4a3f4387e4b0', '석수동'),
('9b6f1151-8cee-4a50-8bb4-0d1eac2ea5e2', '08f1532e-b8af-4a01-9aa2-4a3f4387e4b0', ''),
('0c37f199-324b-4943-b2b8-93d17bc3b636', '195f29cd-05ad-4e7f-9bc0-57f68a56ab56', '경기도'),
('1f5f672c-7dd5-449c-a415-f7b6af23b9dd', '195f29cd-05ad-4e7f-9bc0-57f68a56ab56', '안양시 만안구'),
('2a6bc6bc-ab1e-4194-8cf7-7c2e29af5a28', '195f29cd-05ad-4e7f-9bc0-57f68a56ab56', '석수동'),
('3be9299f-77b6-4aac-86d2-5a233f9dfac8', '195f29cd-05ad-4e7f-9bc0-57f68a56ab56', ''),
('446fe4c8-46a3-4896-9bb0-aec2c869d54c', '2c2710a6-7f61-4a0e-a5c1-6045e1d7d3c9', '경기도'),
('54ecdf09-608c-41ed-be48-7b2f59dc32a5', '2c2710a6-7f61-4a0e-a5c1-6045e1d7d3c9', '안양시 만안구'),
('5f21a0ac-b6e4-4ef6-9650-6d09586588f3', '2c2710a6-7f61-4a0e-a5c1-6045e1d7d3c9', '석수동'),
('6dc71e73-befd-4c5e-b5d5-baf97a1f1604', '2c2710a6-7f61-4a0e-a5c1-6045e1d7d3c9', ''),
('7abae3ad-2639-4a97-bc5d-dbcab64cb616', '3ca5a9f3-4207-4e17-8780-9f057e2e5d33', '경기도'),
('8f47c91f-20ea-4d3e-bcbd-7b1f5a7e088f', '3ca5a9f3-4207-4e17-8780-9f057e2e5d33', '안양시 만안구'),
('9c27c7a9-cc2c-4e13-81be-82e0ff862b7e', '3ca5a9f3-4207-4e17-8780-9f057e2e5d33', '석수동'),
('aeaf1b74-56de-4e40-9a39-537a3a460dde', '3ca5a9f3-4207-4e17-8780-9f057e2e5d33', ''),
('bf31e9b0-43ea-41b3-8f70-3de6a157dfce', '4d84b62b-c483-4e08-8615-807a4b0d62e8', '경기도'),
('c141e305-4ca2-4b59-b513-9dfbc498b34d', '4d84b62b-c483-4e08-8615-807a4b0d62e8', '안양시 만안구'),
('cf8c3104-48b8-4d7e-b39f-4f5c267acacd', '4d84b62b-c483-4e08-8615-807a4b0d62e8', '석수동'),
('d5c749b8-8b1f-4805-8d15-4a7374310f44', '4d84b62b-c483-4e08-8615-807a4b0d62e8', '');