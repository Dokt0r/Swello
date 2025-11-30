-- ==========================================
-- Base de datos: swello
-- ==========================================

CREATE DATABASE IF NOT EXISTS swello_db;
USE swello_db;

-- ==========================================
-- Tablas principales
-- ==========================================
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS valoraciones;
DROP TABLE IF EXISTS imagenes_playa;
DROP TABLE IF EXISTS playas;

CREATE TABLE playas (
    id INT AUTO_INCREMENT PRIMARY KEY,        -- Identificador único de la playa
    nombre VARCHAR(255) NOT NULL,             -- Nombre de la playa
    descripcion TEXT,                         -- Descripción de la playa
    valoracion FLOAT DEFAULT 0,               -- Media geométrica de las valoraciones
    latitud DOUBLE NOT NULL,                  -- Latitud de la playa
    longitud DOUBLE NOT NULL,                 -- Longitud de la playa
    altura_ola DOUBLE DEFAULT 0,              -- Altura media de la ola (m)
    periodo_ola DOUBLE DEFAULT 0,             -- Periodo medio de la ola (s)
    direccion_ola VARCHAR(10),               -- Dirección de la ola (N, NE, E, etc.)
    distancia DOUBLE DEFAULT 0,               -- Distancia estimada desde el usuario (km)
    temp_agua DOUBLE DEFAULT 0,               -- Temperatura del agua (°C)
    ocean_current_velocity DOUBLE DEFAULT 0,  -- Velocidad de la corriente oceánica (m/s)
    ocean_current_direction VARCHAR(10)      -- Dirección de la corriente oceánica (N, NE, E, etc.)
);

CREATE TABLE imagenes_playa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_playa INT NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_playa) REFERENCES playas(id) ON DELETE CASCADE
);

CREATE TABLE valoraciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_playa INT NOT NULL,
    id_usuario INT NOT NULL,
    valoracion FLOAT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_playa) REFERENCES playas(id) ON DELETE CASCADE
);

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    foto_perfil_url VARCHAR(255),
    fecha_nacimiento DATE
);

-- ===========================================
-- DATOS DE PRUEBA
-- ===========================================
INSERT INTO playas 
(nombre, descripcion, valoracion, latitud, longitud, altura_ola, periodo_ola, direccion_ola, distancia, temp_agua, ocean_current_velocity, ocean_current_direction) 
VALUES
('Playa de Pantín', 'Playa mítica para el surf en Valdoviño, A Coruña.', 4.8, 43.5630, -8.0985, 1.8, 10.0, 'NW', 0.0, 16.0, 0.5, 'NW'),
('Playa de Razo', 'Gran arenal de Carballo, muy popular entre surfistas.', 4.5, 43.2935, -8.7725, 1.5, 9.0, 'W', 0.0, 17.0, 0.4, 'W'),
('Playa de La Lanzada', 'Clásico spot de surf en las Rías Baixas, al mar abierto.', 4.6, 42.5067, -8.7799, 1.4, 8.5, 'W', 0.0, 18.0, 0.3, 'W'),
('Playa de Doniños', 'Playa de Ferrol con olas potentes y arena amplia.', 4.7, 43.5403, -8.2332, 1.7, 9.5, 'NW', 0.0, 15.5, 0.6, 'NW'),
('Playa de Lariño', 'Playa salvaje de Carnota, fondo de arena y roca.', 4.4, 42.8151, -9.0610, 1.6, 8.8, 'W', 0.0, 16.5, 0.5, 'W'),
('Playa de Patos', 'Playa de Nigrán, spot clásico en las Rías Baixas.', 4.5, 42.1156, -8.8173, 1.3, 8.2, 'S', 0.0, 17.5, 0.4, 'S'),
('Playa de Areoura (Machacona)', 'Derecha rápida en la Costa de Lugo, para surf avanzado.', 4.2, 43.7635, -7.4730, 2.0, 11.0, 'N', 0.0, 14.0, 0.7, 'N'),
('Playa de Santa Comba', 'Playa virgen de Ferrol con mucho viento y buen oleaje.', 4.3, 43.4850, -8.3890, 1.4, 8.0, 'W', 0.0, 14.5, 0.5, 'W'),
('Playa de Caión', 'Spot de A Laracha, popular para surf intermedio y avanzado.', 4.4, 43.3311, -8.5703, 1.6, 9.3, 'NW', 0.0, 16.0, 0.6, 'NW'),
('Playa de Nemiña', 'Playa salvaje en Muxía, ideal para surf de fondo atlántico.', 4.1, 43.0408, -9.1423, 1.9, 10.8, 'W', 0.0, 15.0, 0.6, 'W');

INSERT INTO imagenes_playa (id_playa, nombre_archivo) VALUES
(1, '1.jpg'),
(1, '4.jpg'),
(2, '2.jpg'),
(2, '4.jpg'),
(3, '3.jpg'),
(3, '5.jpg'),
(3, '1.jpg'),
(4, '1.jpg'),
(4, '2.jpg'),
(5, '4.jpg'),
(6, '5.jpg'),
(7, '5.jpg'),
(7, '2.jpg'),
(7, '1.jpg'),
(8, '1.jpg'),
(8, '2.jpg'),
(9, '2.jpg'),
(10, '4.jpg');

INSERT INTO  usuarios (nombre, email, contraseña, fecha_nacimiento) VALUES
('admin', 'admin@swello.app' , '1234' , '1111-11-11'),
('paco', 'pacoPruebas@swello.app' , '1234' , '1111-01-22');