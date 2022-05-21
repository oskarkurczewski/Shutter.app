INSERT INTO public.account_report_cause (version, cause) VALUES
(0, 'USER_REPORT_DIDNT_PAY_ON_TIME'),
(0, 'USER_REPORT_DIDNT_SHOW_UP')
ON CONFLICT DO NOTHING;

INSERT INTO public.photographer_report_cause (version, cause) VALUES
(0, 'PHOTOGRAPHER_REPORT_DIDNT_SHOW_UP'),
(0, 'PHOTOGRAPHER_REPORT_FALSE_DESCRIPTION'),
(0, 'PHOTOGRAPHER_REPORT_PRIVACY_VIOLATION')
ON CONFLICT DO NOTHING;

INSERT INTO public.review_report_cause (version, cause) VALUES
(0, 'REVIEW_REPORT_OBSCENE_CONTENT'),
(0, 'REVIEW_REPORT_SPAM'),
(0, 'REVIEW_REPORT_FAKE_REVIEW')
ON CONFLICT DO NOTHING;

INSERT INTO public.specialization (version, name) VALUES
(0, 'SPECIALIZATION_STUDIO'),
(0, 'SPECIALIZATION_PHOTOREPORT'),
(0, 'SPECIALIZATION_PRODUCT'),
(0, 'SPECIALIZATION_LANDSCAPE')
ON CONFLICT DO NOTHING;

INSERT INTO public.access_level (version, name) VALUES
(0, 'ADMINISTRATOR'),
(0, 'MODERATOR'),
(0, 'PHOTOGRAPHER'),
(0, 'CLIENT');

INSERT INTO public.account (version, login, email, name, surname, active, password, registered, created_at) OVERRIDING SYSTEM VALUE VALUES
(0, 'aurelian', 'aurelian@rzym.it' ,'Lucjusz', 'Aurelian', true, '$2a$06$MxBtT02QBhHL9G5OuWwau.wG.2XpcXDiXmYyjQ/.H/xrTpv5sb7Pi', true, current_timestamp),
(1, 'majster', 'majster@nadachu.pl', 'majster', 'nadachu', true, '$2a$06$00QDelhrdoVVYAw5UGCvheH/6dYrlBF5wU96vyV.zPBzhwkkeTjKO', true, current_timestamp),
(1, 'majster2', 'majster2@nadachu.pl', 'majster', 'nadachu', true, '$2a$06$00QDelhrdoVVYAw5UGCvheH/6dYrlBF5wU96vyV.zPBzhwkkeTjKO', true, current_timestamp),
(1, 'majster3', 'majster3@nadachu.pl', 'majster', 'nadachu', true, '$2a$06$00QDelhrdoVVYAw5UGCvheH/6dYrlBF5wU96vyV.zPBzhwkkeTjKO', true, current_timestamp)
ON CONFLICT DO NOTHING;



INSERT INTO public.access_level_assignment (version, account_id, access_level_id, active, created_at) VALUES
(0, 1, 1, true, current_timestamp),
(1, 2, 1, true, current_timestamp),
(1, 2, 2, true, current_timestamp),
(1, 3, 3, true, current_timestamp),
(1, 3, 4, true, current_timestamp),
(1, 4, 4, true, current_timestamp)
ON CONFLICT DO NOTHING;

INSERT INTO public.photographer_info (version, account_id, score, review_count, description, lat, long, visible)
VALUES (1, 2, 5, 2137, 'Zucchini can be seasoned with shredded strawberries, also try soaking the stew with tea.', 10,
        20, true)
ON CONFLICT DO NOTHING;
