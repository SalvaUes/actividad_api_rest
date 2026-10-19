-- Opcional: Hibernate ya crea estas tablas con ddl-auto=update.
-- Si las creas a mano, cambia spring.jpa.hibernate.ddl-auto=none
CREATE TABLE usuarios (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(150) NOT NULL,
  password VARCHAR(255) NOT NULL,
  rol VARCHAR(20) NOT NULL DEFAULT 'USER',
  fecha_creacion DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uk_usuarios_correo (correo)
);

CREATE TABLE productos (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  descripcion VARCHAR(500),
  precio DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL,
  activo BIT(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (id)
);

CREATE TABLE sesiones (
  id BIGINT NOT NULL AUTO_INCREMENT,
  usuario_id BIGINT NOT NULL,
  token VARCHAR(512) NOT NULL,
  fecha_inicio DATETIME(6) NOT NULL,
  fecha_expiracion DATETIME(6) NOT NULL,
  activa BIT(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (id),
  CONSTRAINT fk_sesiones_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
