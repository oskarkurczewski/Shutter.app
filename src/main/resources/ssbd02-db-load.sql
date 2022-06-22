--Dane inicjalizacyjne tablic słownikowych
INSERT INTO public.account_report_cause (id, version, cause)
VALUES (-1, 0, 'USER_REPORT_DIDNT_PAY_ON_TIME'),
       (-2, 0, 'USER_REPORT_DIDNT_SHOW_UP')
ON CONFLICT DO NOTHING;

INSERT INTO public.photographer_report_cause (id, version, cause)
VALUES (-1, 0, 'PHOTOGRAPHER_REPORT_DIDNT_SHOW_UP'),
       (-2, 0, 'PHOTOGRAPHER_REPORT_FALSE_DESCRIPTION'),
       (-3, 0, 'PHOTOGRAPHER_REPORT_PRIVACY_VIOLATION')
ON CONFLICT DO NOTHING;

INSERT INTO public.review_report_cause (id, version, cause)
VALUES (-1, 0, 'REVIEW_REPORT_OBSCENE_CONTENT'),
       (-2, 0, 'REVIEW_REPORT_SPAM'),
       (-3, 0, 'REVIEW_REPORT_FAKE_REVIEW')
ON CONFLICT DO NOTHING;

INSERT INTO public.specialization (id, version, name)
VALUES (-1, 0, 'SPECIALIZATION_STUDIO'),
       (-2, 0, 'SPECIALIZATION_PHOTOREPORT'),
       (-3, 0, 'SPECIALIZATION_PRODUCT'),
       (-4, 0, 'SPECIALIZATION_LANDSCAPE')
ON CONFLICT DO NOTHING;

INSERT INTO public.access_level (id, version, name)
VALUES (-1, 0, 'ADMINISTRATOR'),
       (-2, 0, 'MODERATOR'),
       (-3, 0, 'PHOTOGRAPHER'),
       (-4, 0, 'CLIENT');

--Dane inicjalizacyjne kont użytkowników
INSERT INTO public.account (id, version, login, email, name, surname, locale, two_factor_auth, active, password, registered,
                            created_at, secret) OVERRIDING SYSTEM VALUE
VALUES (-1, 0, 'aurelian', '229888@edu.p.lodz.pl', 'Lucjusz', 'Aurelian', 'pl', false, true,
        '$2a$06$YXhULrtOFarRlrSrdWc7oO4e6xtEp303QOg4KgTAK0nY51jgu8Vl6', true, current_timestamp,
        '1c9ca805-4f1b-4875-9fbc-4fa4ee45f182'),
       (-2, 1, 'majster', 'uax58284@jeoce.com', 'Oktawian', 'August', 'pl', false, true,
        '$2a$06$YXhULrtOFarRlrSrdWc7oO4e6xtEp303QOg4KgTAK0nY51jgu8Vl6', true, current_timestamp,
        '14d25d6f-f757-4559-ac5d-64aa4703fccb'),
       (-3, 1, 'majster2', 'cct97683@jiooq.com', 'Marek', 'Aureliusz', 'pl', false, true,
        '$2a$06$YXhULrtOFarRlrSrdWc7oO4e6xtEp303QOg4KgTAK0nY51jgu8Vl6', true, current_timestamp,
        '215a0e2e-b6e5-45d5-902e-9beea66a3826'),
       (-4, 1, 'majster3', 'iydvxvqhxwiuhlbvqf@nthrl.com', 'Dydiusz', 'Julian', 'pl', false, true,
        '$2a$06$YXhULrtOFarRlrSrdWc7oO4e6xtEp303QOg4KgTAK0nY51jgu8Vl6', true, current_timestamp,
        '97ee343b-9e6b-429f-8ece-d0c103c0f483'),
       (-5, 1, 'majster4', '229944@edu.p.lodz.pl', 'Septymiusz', 'Sewer', 'pl', true, true,
        '$2a$06$YXhULrtOFarRlrSrdWc7oO4e6xtEp303QOg4KgTAK0nY51jgu8Vl6', true, current_timestamp,
        '52ee343b-9e6b-429f-8ece-d0c103c0f4fr')
ON CONFLICT DO NOTHING;

INSERT INTO public.access_level_assignment (id, version, account_id, access_level_id, active, created_at)
VALUES (-1, 0, -1, -1, true, current_timestamp),
       (-2, 1, -2, -1, true, current_timestamp),
       (-3, 1, -2, -2, true, current_timestamp),
       (-4, 1, -3, -3, true, current_timestamp),
       (-5, 1, -3, -4, true, current_timestamp),
       (-6, 1, -4, -4, true, current_timestamp),
       (-7, 1, -5, -4, true, current_timestamp)
ON CONFLICT DO NOTHING;

INSERT INTO public.photographer_info (version, account_id, score, review_count, description, visible,
                                      created_at)
VALUES (1, -3, 17, 2, 'Aby poznac stawki, zapraszam do kontaktu mailowego.',
        true, current_timestamp)
ON CONFLICT DO NOTHING;

INSERT INTO public.photographer_specialization (version, photographer_id, specialization_id)
VALUES (1, -3, -1),
       (1, -3, -2),
       (1, -3, -3),
       (1, -3, -4)
ON CONFLICT DO NOTHING;

INSERT INTO public.photographer_specialization (version, photographer_id, specialization_id, created_by, created_at)
VALUES (1, -3, -1, -3, current_timestamp)
ON CONFLICT DO NOTHING;

-- Dane przeznaczone do testów aplikacji
INSERT INTO public.review (id, version, photographer_id, account_id, score, like_count, content, active, created_at,
                           created_by)
VALUES (-1, 0, -3, -4, 7, 0, 'Doskonaly fotograf. Polecam!', true, current_timestamp, -4);

INSERT INTO public.review (id, version, photographer_id, account_id, score, like_count, content, active, created_at,
                           created_by)
VALUES (-2, 1, -3, -5, 10, 0,
        'Majster to wspanialy czlowiek i prawdziwy profesjonalista. W jego towarzystwie rodzina chetnie pozowala, a zdjecia wyszly olsniewajaco. Nie moge sie nacieszyc jego pracami!',
        true, current_timestamp, -5);

INSERT INTO public.review_report (id, version, review_id, account_id, cause_id, reviewed, created_by, created_at)
VALUES (-1, 0, -1, -5, -2, false, -5, current_timestamp);

INSERT INTO public.photographer_report (id, version, photographer_id, account_id, cause_id, reviewed, created_by,
                                        created_at)
VALUES (-1, 0, -3, -4, -1, false, -5, current_timestamp);

INSERT INTO public.photo
    (id, version, photographer_id, s3_url, object_key, title, description, like_count, created_by, created_at)
values
(-1, 0, -3, 'https://ssbd02.s3.eu-central-1.amazonaws.com/majster2/df31007ef0e611eca21264bc582d3ac5.png',
        'sfa', 'Nocne niebo', 'Tak zwane startrailsy widoczne ze Skrzypek - wakacje 2021', default, -3,
        current_timestamp),
(-2, 0, -3, 'https://ssbd02.s3.eu-central-1.amazonaws.com/majster2/df35e4fef0e611ecb51064bc582d3ac5.png',
        'sfa', 'Pogoria', 'Morze widoczne z Pogorii podczas zachodu Slonca', default, -3, current_timestamp),
(-3, 0, -3, 'https://ssbd02.s3.eu-central-1.amazonaws.com/majster2/df3b2766f0e611ecb43f64bc582d3ac5.png',
        'sfa', 'Wyspa przed burza', 'Chmury nadchodzace nad wyspe tworzac ciekawy dwupodzial zdjecia', default, -3,
        current_timestamp),
(-4, 0, -3, 'https://ssbd02.s3.eu-central-1.amazonaws.com/majster2/df405420f0e611ec9ebd64bc582d3ac5.png',
        'sfa', 'Pasikonik', 'Zblizenie na tego cudownego zielonego owada', default, -3, current_timestamp),
(-5, 0, -3, 'https://ssbd02.s3.eu-central-1.amazonaws.com/majster2/df45d878f0e611ecb81564bc582d3ac5.png',
        'sfa', 'Grecki klimat', 'Rzezba na skalach na greckiej wyspie Rodos w 2018 roku', default, -3,
        current_timestamp),
(-6, 0, -3, 'https://ssbd02.s3.eu-central-1.amazonaws.com/majster2/df4b972cf0e611ecb23f64bc582d3ac5.png',
        'sfa', 'Mia', '<3 <3 <3 <3 <3', default, -3, current_timestamp),
(-7, 0, -3, 'https://ssbd02.s3.eu-central-1.amazonaws.com/majster2/df50c27ef0e611ec998364bc582d3ac5.png',
        'sfa', 'Deltonsz', 'Ten chlopak co zrobil kalendarz w tej aplikacji', default, -3, current_timestamp),
(-8, 0, -3, 'https://ssbd02.s3.eu-central-1.amazonaws.com/majster2/df568fc4f0e611ecb87764bc582d3ac5.png',
        'sfa', 'Kuba Karas', 'Koncert zespolu Karasa/Rogucki w grudniu 2021', default, -3, current_timestamp);

INSERT INTO public.reservation (id, version, photographer_id, account_id, time_from, time_to, created_by, created_at)
VALUES (-1, 0, -3, -4, '2022-06-22 15:00:00.000000', '2022-06-22 16:00:00.000000', -4, current_timestamp),
       (-2, 0, -3, -4, '2022-06-23 16:00:00.000000', '2022-06-23 17:00:00.000000', -4, current_timestamp),
       (-3, 0, -3, -4, '2022-06-16 12:00:00.000000', '2022-06-16 18:00:00.000000', -4, current_timestamp)
    ON CONFLICT DO NOTHING;

INSERT INTO public.availability (id, version, photographer_id, weekday, time_from, time_to)
VALUES (-1, 0, -3, 2, '06:00:00', '23:00:00'),
       (-2, 0, -3, 3, '06:00:00', '23:00:00')
    ON CONFLICT DO NOTHING;
