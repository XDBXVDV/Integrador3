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
direccion varchar(100),
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

CREATE TABLE proveedores(
	id_proveedor int auto_increment primary key,
    razon_social varchar(100) not null,
    ruc varchar(100) not null unique,
    telefono varchar(15) ,
    direccion varchar(100),
    email varchar(100),
    estado enum('Activo','Inactivo') default 'Activo'
);

CREATE TABLE pedidos_compra(
id_pedido_compra int auto_increment primary key,
id_proveedor int not null,
id_empleado int not null,
estado enum ('Registrado','Aprobado','Recibido','Anulado'),
total Decimal(10,2),
fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
foreign key(id_proveedor) references proveedores(id_proveedor),
foreign key(id_empleado) references empleados(id_empleado)
);

create table detalle_pedidos_compra(
idDetalle int auto_increment primary key,
id_pedido_compra int not null,
id_producto int not null,
cantidad int not null,
precioUnitario Decimal(10,2) not null,
subtotal Decimal(10,2) not null,
foreign key(id_pedido_compra) references pedidos_compra(id_pedido_compra),
foreign key(id_producto) references productos(id_producto)
);


insert into roles (nombre) values ('Cliente'),('Administrador'),('Area de ventas'),('Area de compras'),('Area de almacén');


select * from roles;
select * from clientes;
select * from usuarios;
select * from empleados;
select * from categorias;
select * from marcas;
select * from productos;
select * from ventas;
select * from detalle_ventas;
select * from devoluciones;
select * from proveedores;
select * from pedidos_compra;
select * from detalle_pedidos_compra