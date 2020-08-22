INSERT INTO role (name) VALUES ('ROLE_APP_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_USER_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_USER_EDIT');
INSERT INTO role (name) VALUES ('ROLE_USER_VIEW');

INSERT INTO `dms`.`user` (`account_non_expired`, `account_non_locked`, `credentials_non_expired`, `date_created`, `date_last_modified`, `display_name`, `email`, `enabled`, `last_login_date`, `mobile_number`, `password`, `user_name`) VALUES (b'1', b'1', b'1', '2018-04-30 11:47:54', '2018-04-30 11:47:55', 'Admin', 'admin@localhost.com', b'1', '2018-04-30 11:48:11', '123456789', '$2a$10$rZw/UFQllzCZTM6tWCXfue9d/c/2H7Nz9ZSYqXVxt6WvtK/p.Eeh6', 'admin');
INSERT INTO user_roles (user_id, role_id) VALUES ('1', '1');