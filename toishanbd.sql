drop database toishan;
create database toishan;
use toishan; 

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);
create table usuarios(
id_usuario int auto_increment primary key,
usuario varchar(50) not null unique,
email varchar(100) not null unique,
contrasena varchar(255) not null,
id_rol int not null default 1,
estado enum('Activo','Inactivo') default 'Activo',
fecha_creacion timestamp default current_timestamp,
foreign key (id_rol) references roles(id_rol)
);

create table clientes(
id_cliente int auto_increment primary key,
id_usuario int unique not null,
nombre varchar(50) not null,
apellido varchar(70) not null,
dni char(8) unique,
telefono varchar(20),
dirección varchar(100),
foreign key(id_usuario) references usuarios(id_usuario));

create table empleados(
id_empleado int auto_increment primary key,
id_usuario int unique not null,
nombre varchar(50) not null,
apellido varchar(70) not null,
dni char(8) unique,
FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

create table categorias(
id_categoria int auto_increment primary key,
nombre varchar(50) not null,
estado enum('Activo','Inactivo') default 'Activo'
);

create table marcas(
id_marca int auto_increment primary key,
nombre varchar(50) unique not null,
estado enum('Activo','Inactivo') default 'Activo'
);

create table productos(
id_producto int auto_increment primary key,
id_categoria int not null,
id_marca int not null,
nombre varchar(100) not null,
precio DECIMAL(10,2) not null,
stock_minimo int not null,
stock int not null,
estado enum('Activo','Inactivo') default 'Activo',
condicion enum('En_stock','Stock_bajo','Agotado'),
foreign key (id_categoria) references categorias (id_categoria),
foreign key (id_marca) references marcas(id_marca),
check (stock>=0)
);

create table ventas(
id_venta int auto_increment primary key,
id_cliente int not null,
id_empleado int not null,
fechaventa timestamp default current_timestamp,
total decimal (10,2) not null,
estado ENUM('Registrada','Anulada') DEFAULT 'Registrada',
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado)
);

CREATE TABLE detalle_ventas (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    CHECK (cantidad > 0)
);

CREATE TABLE devoluciones (
    id_devolucion INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    fecha_devolucion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    motivo VARCHAR(150),
    total_devuelto DECIMAL(10,2) NOT NULL,
    estado ENUM('Registrada','Anulada') DEFAULT 'Registrada',
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta)
);

CREATE TABLE detalle_devoluciones (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_devolucion INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_devolucion) REFERENCES devoluciones(id_devolucion),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    CHECK (cantidad > 0)
);

DELIMITER $$

CREATE TRIGGER trg_detalle_ventas_after_insert
AFTER INSERT ON detalle_ventas
FOR EACH ROW
BEGIN
    UPDATE productos
    SET stock = stock - NEW.cantidad
    WHERE id_producto = NEW.id_producto;
END$$

DELIMITER ;


DELIMITER $$

CREATE TRIGGER trg_productos_before_insert
BEFORE INSERT ON productos
FOR EACH ROW
BEGIN
    IF NEW.stock = 0 THEN
        SET NEW.condicion = 'Agotado';
    ELSEIF NEW.stock <= NEW.stock_minimo THEN
        SET NEW.condicion = 'Stock_bajo';
    ELSE
        SET NEW.condicion = 'En_stock';
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_productos_before_update
BEFORE UPDATE ON productos
FOR EACH ROW
BEGIN
    IF NEW.stock = 0 THEN
        SET NEW.condicion = 'Agotado';
    ELSEIF NEW.stock <= NEW.stock_minimo THEN
        SET NEW.condicion = 'Stock_bajo';
    ELSE
        SET NEW.condicion = 'En_stock';
    END IF;
END$$

DELIMITER ;
DELIMITER $$

CREATE TRIGGER trg_ventas_after_update
AFTER UPDATE ON ventas
FOR EACH ROW
BEGIN
  
    IF OLD.estado = 'Registrada' AND NEW.estado = 'Anulada' THEN

        UPDATE productos p
        JOIN detalle_ventas dv ON dv.id_producto = p.id_producto
        SET p.stock = p.stock + dv.cantidad
        WHERE dv.id_venta = NEW.id_venta;

    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_detalle_devoluciones_after_insert
AFTER INSERT ON detalle_devoluciones
FOR EACH ROW
BEGIN
    UPDATE productos
    SET stock = stock + NEW.cantidad
    WHERE id_producto = NEW.id_producto;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_devoluciones_after_update
AFTER UPDATE ON devoluciones
FOR EACH ROW
BEGIN

    IF OLD.estado = 'Registrada' AND NEW.estado = 'Anulada' THEN

        UPDATE productos p
        JOIN detalle_devoluciones dd
            ON dd.id_producto = p.id_producto
        SET p.stock = p.stock - dd.cantidad
        WHERE dd.id_devolucion = NEW.id_devolucion;

    END IF;
END$$

DELIMITER ;



insert into roles (nombre) values ('Cliente'),('Administrador'),('Area de ventas'),('Area de compras'),('Area de almacén');
insert into categorias(nombre) values('hola');
insert into productos(id_categoria,nombre, marca, precio, stock_minimo, stock) values (1,'a','b',12.5,20,0);
update productos set stock_minimo =22 where id_producto=1;
insert into usuarios (usuario,email, contrasena,id_rol) values ('asdasd','asdasda','asdasda',1);
insert into usuarios (usuario,email, contrasena,id_rol) values ('qweqweq','dfgdfgdfg','xvcxcvxcb',2);
insert into clientes (id_usuario,nombre,apellido,dni,telefono,dirección) values (1,'amogus','sus','12345678','+52 66666666','Polus');
insert into empleados (id_usuario,nombre,apellido,dni) values(3,'tula','tula2','87654321');
insert into ventas(id_cliente,id_empleado,total)values (1,2,69.69);
insert into detalle_ventas(id_venta,id_producto,cantidad,precio_unitario, subtotal) values (1,1,1,69.69,69.69);
insert into devoluciones (id_venta,motivo, total_devuelto) values(1,'volgoverg',69.69);
insert into detalle_devoluciones(id_devolucion, id_producto, cantidad,precio_unitario, subtotal)values (1,1,1,69.69,69.69);
update devoluciones set estado= 'Anulada' where id_devolucion=1;
select * from ventas;
select * from usuarios;
select * from roles;
select * from categorias;
select * from productos;
select * from clientes;
select * from empleados;
select * from detalle_ventas;
select * from devoluciones;
select * from detalle_devoluciones;

