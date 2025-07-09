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


-- 의상 속성 중간테이블
INSERT INTO clothes_attributes (id, clothes_id, attribute_id, value, created_at, updated_at)
VALUES
    --테스트 유저의 '상의' 의상 속성 중간 정보
    ('855c08b1-af6d-488e-aff0-d0a624fa0b66', 'e68bd02c-347b-4c9b-8243-353e8e7ae959', '45723d92-6654-4976-8034-0b34a0a2ccf7', '여름','2025-07-09 07:43:36.588879', '2025-07-09 07:43:36.588879'
    ),
    ('4e6c22a7-4bcb-4d63-b5cd-fe067fce2344', 'e68bd02c-347b-4c9b-8243-353e8e7ae959', '55723d92-6654-4976-8034-0b34a0a2ccf7', '얇음','2025-07-09 07:43:36.588879', '2025-07-09 07:43:36.588879'
    );



