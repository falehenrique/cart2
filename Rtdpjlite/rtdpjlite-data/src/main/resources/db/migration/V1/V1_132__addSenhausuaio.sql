UPDATE usuario SET senha =  '$2a$10$GNg4TK8fE.Jk/ksF2IBaH.1LGpWer7qmuDUiru8rec44QvSu7ypj.';
ALTER TABLE usuario ADD COLUMN role VARCHAR(250) NOT NULL DEFAULT 'admin';