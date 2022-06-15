INSERT INTO public.account_report_cause (version, cause)
VALUES (0, 'USER_REPORT_DIDNT_PAY_ON_TIME'),
       (0, 'USER_REPORT_DIDNT_SHOW_UP')
ON CONFLICT DO NOTHING;

INSERT INTO public.photographer_report_cause (version, cause)
VALUES (0, 'PHOTOGRAPHER_REPORT_DIDNT_SHOW_UP'),
       (0, 'PHOTOGRAPHER_REPORT_FALSE_DESCRIPTION'),
       (0, 'PHOTOGRAPHER_REPORT_PRIVACY_VIOLATION')
ON CONFLICT DO NOTHING;

INSERT INTO public.review_report_cause (version, cause)
VALUES (0, 'REVIEW_REPORT_OBSCENE_CONTENT'),
       (0, 'REVIEW_REPORT_SPAM'),
       (0, 'REVIEW_REPORT_FAKE_REVIEW')
ON CONFLICT DO NOTHING;

INSERT INTO public.specialization (version, name)
VALUES (0, 'SPECIALIZATION_STUDIO'),
       (0, 'SPECIALIZATION_PHOTOREPORT'),
       (0, 'SPECIALIZATION_PRODUCT'),
       (0, 'SPECIALIZATION_LANDSCAPE')
ON CONFLICT DO NOTHING;

INSERT INTO public.access_level (version, name)
VALUES (0, 'ADMINISTRATOR'),
       (0, 'MODERATOR'),
       (0, 'PHOTOGRAPHER'),
       (0, 'CLIENT');

INSERT INTO public.account (version, login, email, name, surname, locale, two_factor_auth, active, password, registered,
                            created_at, secret) OVERRIDING SYSTEM VALUE
VALUES (0, 'aurelian', 'aurelian@rzym.it', 'Lucjusz', 'Aurelian', 'pl', false, true,
        '$2a$06$YXhULrtOFarRlrSrdWc7oO4e6xtEp303QOg4KgTAK0nY51jgu8Vl6', true, current_timestamp,
        '1c9ca805-4f1b-4875-9fbc-4fa4ee45f182'),
       (1, 'majster', 'majster@nadachu.pl', 'Majster', 'Nadachu', 'pl', false, true,
        '$2a$06$YXhULrtOFarRlrSrdWc7oO4e6xtEp303QOg4KgTAK0nY51jgu8Vl6', true, current_timestamp,
        '14d25d6f-f757-4559-ac5d-64aa4703fccb'),
       (1, 'majster2', 'majster2@nadachu.pl', 'Majster', 'Nadachu', 'pl', true, true,
        '$2a$06$YXhULrtOFarRlrSrdWc7oO4e6xtEp303QOg4KgTAK0nY51jgu8Vl6', true, current_timestamp,
        '215a0e2e-b6e5-45d5-902e-9beea66a3826'),
       (1, 'majster3', 'majster3@nadachu.pl', 'Majster', 'Nadachu', 'pl', true, true,
        '$2a$06$YXhULrtOFarRlrSrdWc7oO4e6xtEp303QOg4KgTAK0nY51jgu8Vl6', true, current_timestamp,
        '97ee343b-9e6b-429f-8ece-d0c103c0f483')
ON CONFLICT DO NOTHING;



INSERT INTO public.access_level_assignment (version, account_id, access_level_id, active, created_at)
VALUES (0, 1, 1, true, current_timestamp),
       (1, 2, 1, true, current_timestamp),
       (1, 2, 2, true, current_timestamp),
       (1, 3, 3, true, current_timestamp),
       (1, 3, 4, true, current_timestamp),
       (1, 4, 4, true, current_timestamp)
ON CONFLICT DO NOTHING;

INSERT INTO public.photographer_info (version, account_id, score, review_count, description, lat, long, visible, created_at)
VALUES (1, 3, 5, 2137, 'Zucchini can be seasoned with shredded strawberries, also try soaking the stew with tea.', 10,
        20, true)
ON CONFLICT DO NOTHING;

INSERT INTO public.photographer_specialization (version, photographer_id, specialization_id)
VALUES (1, 3, 1),
       (1, 3, 2),
       (1, 3, 3),
       (1, 3, 4) ON CONFLICT DO NOTHING;

-- Dane Przeznaczone Do Testów Aplikacji

INSERT INTO public.photographer_specialization (version, photographer_id, specialization_id, created_by, created_at)
VALUES (0, 3, 1, 3, current_timestamp)
ON CONFLICT DO NOTHING;

INSERT INTO public.reservation (version, photographer_id, account_id, created_by, created_at)
VALUES (0, 3, 4, '2022-06-14 16:00:00.000000', '2022-06-14 16:00:00.000000'),
       (0, 3, 4, '2022-06-13 16:00:00.000000', '2022-06-13 17:00:00.000000')
ON CONFLICT DO NOTHING;

INSERT INTO public.availability (version, photographer_id, weekday, "from", "to")
VALUES (0, 3, 0, '06:00:00', '23:00:00'),
       (0, 3, 1, '06:00:00', '23:00:00')
ON CONFLICT DO NOTHING;

INSERT INTO public.review (version, photographer_id, account_id, score, like_count, content, active, created_at, created_by)
VALUES (0, 3, 4, 3, 0, 'I really appreciate this photographer', true, current_timestamp, 4);