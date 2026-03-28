-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: toishan
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `estado` enum('Activo','Inactivo') DEFAULT 'Activo',
  PRIMARY KEY (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias`
--

LOCK TABLES `categorias` WRITE;
/*!40000 ALTER TABLE `categorias` DISABLE KEYS */;
INSERT INTO `categorias` VALUES (1,'Cereal','Activo'),(2,'Bebidas','Activo');
/*!40000 ALTER TABLE `categorias` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(70) NOT NULL,
  `dni` char(8) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `id_usuario` (`id_usuario`),
  UNIQUE KEY `dni` (`dni`),
  CONSTRAINT `clientes_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientes`
--

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,2,'CLIENTE','CLIENTE','97654238','967843512','asfsdgfsdf');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cotizaciones`
--

DROP TABLE IF EXISTS `cotizaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cotizaciones` (
  `id_cotizacion` int NOT NULL AUTO_INCREMENT,
  `id_pedido_compra` int NOT NULL,
  `id_proveedor` int NOT NULL,
  `fecha_emision` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `validez_dias` int DEFAULT '5',
  `total_cotizado` decimal(10,2) NOT NULL,
  `estado` enum('PENDIENTE','ACEPTADA','RECHAZADA') DEFAULT 'PENDIENTE',
  PRIMARY KEY (`id_cotizacion`),
  KEY `id_pedido_compra` (`id_pedido_compra`),
  KEY `id_proveedor` (`id_proveedor`),
  CONSTRAINT `cotizaciones_ibfk_1` FOREIGN KEY (`id_pedido_compra`) REFERENCES `pedidos_compra` (`id_pedido_compra`),
  CONSTRAINT `cotizaciones_ibfk_2` FOREIGN KEY (`id_proveedor`) REFERENCES `proveedores` (`id_proveedor`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cotizaciones`
--

LOCK TABLES `cotizaciones` WRITE;
/*!40000 ALTER TABLE `cotizaciones` DISABLE KEYS */;
INSERT INTO `cotizaciones` VALUES (1,1,1,'2026-03-19 01:45:43',5,888.00,'ACEPTADA'),(2,2,1,'2026-03-23 22:50:23',5,5457.00,'ACEPTADA'),(3,3,2,'2026-03-23 22:54:31',5,100.00,'ACEPTADA');
/*!40000 ALTER TABLE `cotizaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_cotizaciones`
--

DROP TABLE IF EXISTS `detalle_cotizaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_cotizaciones` (
  `id_detalle_cot` int NOT NULL AUTO_INCREMENT,
  `id_cotizacion` int NOT NULL,
  `id_producto` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario_ofertado` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_detalle_cot`),
  KEY `id_cotizacion` (`id_cotizacion`),
  KEY `id_producto` (`id_producto`),
  CONSTRAINT `detalle_cotizaciones_ibfk_1` FOREIGN KEY (`id_cotizacion`) REFERENCES `cotizaciones` (`id_cotizacion`),
  CONSTRAINT `detalle_cotizaciones_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_cotizaciones`
--

LOCK TABLES `detalle_cotizaciones` WRITE;
/*!40000 ALTER TABLE `detalle_cotizaciones` DISABLE KEYS */;
INSERT INTO `detalle_cotizaciones` VALUES (1,1,2,13,21.00,273.00),(2,1,1,15,41.00,615.00),(3,2,3,321,12.00,3852.00),(4,2,1,321,5.00,1605.00),(5,3,3,1000,0.10,100.00);
/*!40000 ALTER TABLE `detalle_cotizaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_guia`
--

DROP TABLE IF EXISTS `detalle_guia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_guia` (
  `id_detalle_guia` int NOT NULL AUTO_INCREMENT,
  `id_guia` int NOT NULL,
  `id_producto` int NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`id_detalle_guia`),
  KEY `id_guia` (`id_guia`),
  KEY `id_producto` (`id_producto`),
  CONSTRAINT `detalle_guia_ibfk_1` FOREIGN KEY (`id_guia`) REFERENCES `guias_almacen` (`id_guia`) ON DELETE CASCADE,
  CONSTRAINT `detalle_guia_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_guia`
--

LOCK TABLES `detalle_guia` WRITE;
/*!40000 ALTER TABLE `detalle_guia` DISABLE KEYS */;
INSERT INTO `detalle_guia` VALUES (1,1,2,1),(2,2,1,1),(3,3,1,16);
/*!40000 ALTER TABLE `detalle_guia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_pedidos_compra`
--

DROP TABLE IF EXISTS `detalle_pedidos_compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_pedidos_compra` (
  `id_detalle` int NOT NULL AUTO_INCREMENT,
  `id_pedido_compra` int NOT NULL,
  `id_producto` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_detalle`),
  KEY `id_pedido_compra` (`id_pedido_compra`),
  KEY `id_producto` (`id_producto`),
  CONSTRAINT `detalle_pedidos_compra_ibfk_1` FOREIGN KEY (`id_pedido_compra`) REFERENCES `pedidos_compra` (`id_pedido_compra`),
  CONSTRAINT `detalle_pedidos_compra_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_pedidos_compra`
--

LOCK TABLES `detalle_pedidos_compra` WRITE;
/*!40000 ALTER TABLE `detalle_pedidos_compra` DISABLE KEYS */;
INSERT INTO `detalle_pedidos_compra` VALUES (1,1,2,13,0.00,0.00),(2,1,1,15,0.00,0.00),(3,2,3,321,0.00,0.00),(4,2,1,321,0.00,0.00),(5,3,3,1000,0.00,0.00),(6,4,2,543,5.00,2715.00);
/*!40000 ALTER TABLE `detalle_pedidos_compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_ventas`
--

DROP TABLE IF EXISTS `detalle_ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_ventas` (
  `id_detalle` int NOT NULL AUTO_INCREMENT,
  `id_venta` int NOT NULL,
  `id_producto` int NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_detalle`),
  KEY `id_venta` (`id_venta`),
  KEY `id_producto` (`id_producto`),
  CONSTRAINT `detalle_ventas_ibfk_1` FOREIGN KEY (`id_venta`) REFERENCES `ventas` (`id_venta`) ON DELETE CASCADE,
  CONSTRAINT `detalle_ventas_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`),
  CONSTRAINT `detalle_ventas_chk_1` CHECK ((`cantidad` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_ventas`
--

LOCK TABLES `detalle_ventas` WRITE;
/*!40000 ALTER TABLE `detalle_ventas` DISABLE KEYS */;
INSERT INTO `detalle_ventas` VALUES (1,1,1,8,2.50,20.00),(2,2,1,4,2.50,10.00),(3,3,1,9,2.50,22.50),(4,4,2,3,5.00,15.00),(5,4,3,17,0.15,2.55),(6,4,4,5,0.06,0.30),(7,5,3,2,0.15,0.30);
/*!40000 ALTER TABLE `detalle_ventas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleados`
--

DROP TABLE IF EXISTS `empleados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleados` (
  `id_empleado` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(70) NOT NULL,
  `dni` char(8) DEFAULT NULL,
  PRIMARY KEY (`id_empleado`),
  UNIQUE KEY `id_usuario` (`id_usuario`),
  UNIQUE KEY `dni` (`dni`),
  CONSTRAINT `empleados_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleados`
--

LOCK TABLES `empleados` WRITE;
/*!40000 ALTER TABLE `empleados` DISABLE KEYS */;
INSERT INTO `empleados` VALUES (1,1,'ADMIN','ADMIN','15984267');
/*!40000 ALTER TABLE `empleados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facturas_compra`
--

DROP TABLE IF EXISTS `facturas_compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `facturas_compra` (
  `id_factura_compra` int NOT NULL AUTO_INCREMENT,
  `id_orden_compra` int NOT NULL,
  `serie_factura` varchar(10) NOT NULL,
  `correlativo_factura` varchar(15) NOT NULL,
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total_pagado` decimal(10,2) NOT NULL,
  `pdf_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_factura_compra`),
  KEY `id_orden_compra` (`id_orden_compra`),
  CONSTRAINT `facturas_compra_ibfk_1` FOREIGN KEY (`id_orden_compra`) REFERENCES `ordenes_compra` (`id_orden_compra`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facturas_compra`
--

LOCK TABLES `facturas_compra` WRITE;
/*!40000 ALTER TABLE `facturas_compra` DISABLE KEYS */;
INSERT INTO `facturas_compra` VALUES (1,1,'F001','000456','2026-03-19 01:46:08',888.00,'/docs/facturas/3edac6ef-2386-4777-b47d-2685e0d69fdc.pdf'),(2,2,'F002','000666','2026-03-23 22:51:04',5457.00,'/docs/facturas/17974937-a115-4def-8ace-8cc8205d8f89.pdf'),(3,3,'F004','000667','2026-03-23 22:55:10',100.00,'/docs/facturas/4eb61d0e-843a-4565-af51-f81d9df93171.pdf');
/*!40000 ALTER TABLE `facturas_compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guias_almacen`
--

DROP TABLE IF EXISTS `guias_almacen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guias_almacen` (
  `id_guia` int NOT NULL AUTO_INCREMENT,
  `numero_guia` varchar(20) NOT NULL,
  `tipo_movimiento` enum('ENTRADA','SALIDA') NOT NULL,
  `fecha_movimiento` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `motivo` varchar(100) DEFAULT NULL,
  `id_empleado_almacen` int NOT NULL,
  `id_documento_referencia` int DEFAULT NULL,
  PRIMARY KEY (`id_guia`),
  KEY `id_empleado_almacen` (`id_empleado_almacen`),
  CONSTRAINT `guias_almacen_ibfk_1` FOREIGN KEY (`id_empleado_almacen`) REFERENCES `empleados` (`id_empleado`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guias_almacen`
--

LOCK TABLES `guias_almacen` WRITE;
/*!40000 ALTER TABLE `guias_almacen` DISABLE KEYS */;
INSERT INTO `guias_almacen` VALUES (1,'G001-000','ENTRADA','2026-03-25 22:26:52','SD',1,NULL),(2,'G002-000','ENTRADA','2026-03-26 00:03:18','tefusite',1,NULL),(3,'G003-000','SALIDA','2026-03-26 00:04:09','tefusite2',1,NULL);
/*!40000 ALTER TABLE `guias_almacen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `marcas`
--

DROP TABLE IF EXISTS `marcas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `marcas` (
  `id_marca` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `estado` enum('Activo','Inactivo') DEFAULT 'Activo',
  PRIMARY KEY (`id_marca`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `marcas`
--

LOCK TABLES `marcas` WRITE;
/*!40000 ALTER TABLE `marcas` DISABLE KEYS */;
INSERT INTO `marcas` VALUES (1,'Zucaritas','Activo'),(2,'Coca Cola','Activo');
/*!40000 ALTER TABLE `marcas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordenes_compra`
--

DROP TABLE IF EXISTS `ordenes_compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordenes_compra` (
  `id_orden_compra` int NOT NULL AUTO_INCREMENT,
  `id_cotizacion` int NOT NULL,
  `nro_orden` varchar(20) NOT NULL,
  `fecha_emision` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` enum('EMITIDA','FACTURADA','ANULADA') DEFAULT 'EMITIDA',
  `total` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_orden_compra`),
  UNIQUE KEY `nro_orden` (`nro_orden`),
  KEY `id_cotizacion` (`id_cotizacion`),
  CONSTRAINT `ordenes_compra_ibfk_1` FOREIGN KEY (`id_cotizacion`) REFERENCES `cotizaciones` (`id_cotizacion`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordenes_compra`
--

LOCK TABLES `ordenes_compra` WRITE;
/*!40000 ALTER TABLE `ordenes_compra` DISABLE KEYS */;
INSERT INTO `ordenes_compra` VALUES (1,1,'OC-1773884755874','2026-03-19 01:45:55','FACTURADA',888.00),(2,2,'OC-1774306233311','2026-03-23 22:50:33','FACTURADA',5457.00),(3,3,'OC-1774306485479','2026-03-23 22:54:45','FACTURADA',100.00);
/*!40000 ALTER TABLE `ordenes_compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pedidos_compra`
--

DROP TABLE IF EXISTS `pedidos_compra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pedidos_compra` (
  `id_pedido_compra` int NOT NULL AUTO_INCREMENT,
  `id_proveedor` int DEFAULT NULL,
  `id_empleado` int NOT NULL,
  `estado` enum('PENDIENTE','COTIZADO','ANULADO') DEFAULT NULL,
  `total` decimal(10,2) DEFAULT '0.00',
  `fecha` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_pedido_compra`),
  KEY `id_proveedor` (`id_proveedor`),
  KEY `id_empleado` (`id_empleado`),
  CONSTRAINT `pedidos_compra_ibfk_1` FOREIGN KEY (`id_proveedor`) REFERENCES `proveedores` (`id_proveedor`),
  CONSTRAINT `pedidos_compra_ibfk_2` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pedidos_compra`
--

LOCK TABLES `pedidos_compra` WRITE;
/*!40000 ALTER TABLE `pedidos_compra` DISABLE KEYS */;
INSERT INTO `pedidos_compra` VALUES (1,NULL,1,'COTIZADO',0.00,'2026-03-19 01:45:07'),(2,NULL,1,'COTIZADO',0.00,'2026-03-23 22:50:02'),(3,NULL,1,'COTIZADO',0.00,'2026-03-23 22:54:19'),(4,2,1,'PENDIENTE',NULL,'2026-03-24 23:43:52');
/*!40000 ALTER TABLE `pedidos_compra` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `id_categoria` int NOT NULL,
  `id_marca` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text,
  `precio` decimal(10,2) NOT NULL,
  `stock_minimo` int NOT NULL,
  `stock` int NOT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `estado` enum('Activo','Inactivo') DEFAULT 'Activo',
  `condicion` enum('En_stock','Stock_bajo','Agotado') DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  KEY `id_categoria` (`id_categoria`),
  KEY `id_marca` (`id_marca`),
  CONSTRAINT `productos_ibfk_1` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id_categoria`),
  CONSTRAINT `productos_ibfk_2` FOREIGN KEY (`id_marca`) REFERENCES `marcas` (`id_marca`),
  CONSTRAINT `productos_chk_1` CHECK ((`stock` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (1,2,2,'Coca cola 500ml','bcv',2.50,10,400,'/img/productos/64ec5c73-4ea1-4161-9dda-9fd428ca7604.png','Activo','En_stock'),(2,1,1,'Zucaritassss','asdffdhgk',5.00,666,131,'/img/productos/c45511f7-0a77-4aee-8f12-81fe7de81a8a.png','Activo','Stock_bajo'),(3,1,2,'lancergod','gthyhyhyhyhyhyhyhyhyhyhyhyhyhyhyhyhyf',0.15,3,1313,'/img/productos/c60b5190-b6a6-43a5-972e-00aafb02787d.jpg','Activo','En_stock'),(4,2,1,'gfhfhgfgh','6ytdr',0.06,4,10,'/img/productos/5538cf7f-3c68-4454-b93c-caf4fe9922eb.jpeg','Activo','En_stock'),(5,1,2,'CUM','DFG',0.07,9,8,'/img/productos/5bfc99d5-a712-4f90-9ecf-b6766c2e6ef7.jpeg','Activo','Stock_bajo'),(6,1,1,'nuevo','fehsdrz',0.22,30,22,'/img/productos/b48546ff-3175-40af-bdf5-5221c71a7080.png','Activo','Stock_bajo');
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proveedores`
--

DROP TABLE IF EXISTS `proveedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedores` (
  `id_proveedor` int NOT NULL AUTO_INCREMENT,
  `razon_social` varchar(100) NOT NULL,
  `ruc` varchar(100) NOT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `estado` enum('Activo','Inactivo') DEFAULT 'Activo',
  PRIMARY KEY (`id_proveedor`),
  UNIQUE KEY `ruc` (`ruc`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proveedores`
--

LOCK TABLES `proveedores` WRITE;
/*!40000 ALTER TABLE `proveedores` DISABLE KEYS */;
INSERT INTO `proveedores` VALUES (1,'dfcvcvbcvb','27482424svxvxcv','123541687','sczcxzxc','uihxviuhxcv@szkjhcz.c','Activo'),(2,'inutil','27482424svxvxcB','321654987','sdbvksdbkvsd','lvhxkljcvkxjcbv@sdjihs.com','Activo');
/*!40000 ALTER TABLE `proveedores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requerimientos_reposicion`
--

DROP TABLE IF EXISTS `requerimientos_reposicion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requerimientos_reposicion` (
  `id_requerimiento` int NOT NULL AUTO_INCREMENT,
  `id_producto` int NOT NULL,
  `id_empleado_almacen` int NOT NULL,
  `cantidad_sugerida` int NOT NULL,
  `fecha_solicitud` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `prioridad` enum('BAJA','MEDIA','ALTA','CRITICA') DEFAULT 'MEDIA',
  `estado` enum('PENDIENTE','EN_PROCESO','ATENDIDO','RECHAZADO') DEFAULT 'PENDIENTE',
  PRIMARY KEY (`id_requerimiento`),
  KEY `id_producto` (`id_producto`),
  KEY `id_empleado_almacen` (`id_empleado_almacen`),
  CONSTRAINT `requerimientos_reposicion_ibfk_1` FOREIGN KEY (`id_producto`) REFERENCES `productos` (`id_producto`),
  CONSTRAINT `requerimientos_reposicion_ibfk_2` FOREIGN KEY (`id_empleado_almacen`) REFERENCES `empleados` (`id_empleado`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requerimientos_reposicion`
--

LOCK TABLES `requerimientos_reposicion` WRITE;
/*!40000 ALTER TABLE `requerimientos_reposicion` DISABLE KEYS */;
INSERT INTO `requerimientos_reposicion` VALUES (1,2,1,543,'2026-03-24 22:46:31','ALTA','ATENDIDO');
/*!40000 ALTER TABLE `requerimientos_reposicion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (2,'Administrador'),(5,'Area de almacén'),(4,'Area de compras'),(3,'Area de ventas'),(1,'Cliente');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `usuario` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `id_rol` int NOT NULL DEFAULT '1',
  `estado` enum('Activo','Inactivo') DEFAULT 'Activo',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `usuario` (`usuario`),
  UNIQUE KEY `email` (`email`),
  KEY `id_rol` (`id_rol`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'ADMIN','ADMIN@ADMIN.ADMIN','$2a$10$V7KQnrDxYWXyir/G7m96Ue4ThrLoH6DyLOrCZQW6S2smdopCCSHu2',2,'Activo','2026-03-17 11:51:15'),(2,'CLIENTE','CLIENTE@GMAIL.COM','$2a$10$XKOT3FKdje.lsD.Eieo6QOUX.sX5u2XrtQNVDL9xbECiUHdVPDwJO',1,'Activo','2026-03-17 12:04:56');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ventas`
--

DROP TABLE IF EXISTS `ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ventas` (
  `id_venta` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NOT NULL,
  `fechaventa` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `total` decimal(10,2) NOT NULL,
  `igv` decimal(10,2) NOT NULL DEFAULT '0.00',
  `tipo_comprobante` enum('BOLETA','FACTURA') NOT NULL DEFAULT 'BOLETA',
  `nro_documento` varchar(15) NOT NULL,
  `metodo_pago` enum('TARJETA','YAPE','PLIN','PAYPAL','TRANSFERENCIA') DEFAULT 'TARJETA',
  `estado` enum('Registrada','Anulada','Completada') DEFAULT 'Registrada',
  PRIMARY KEY (`id_venta`),
  KEY `id_cliente` (`id_cliente`),
  CONSTRAINT `ventas_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ventas`
--

LOCK TABLES `ventas` WRITE;
/*!40000 ALTER TABLE `ventas` DISABLE KEYS */;
INSERT INTO `ventas` VALUES (1,1,'2026-03-17 12:06:03',20.00,0.00,'BOLETA','98457684','TARJETA','Registrada'),(2,1,'2026-03-17 13:37:11',10.00,0.00,'BOLETA','98457684','TARJETA','Registrada'),(3,1,'2026-03-19 01:43:38',26.55,4.05,'FACTURA','98457684458','YAPE','Registrada'),(4,1,'2026-03-26 22:22:51',17.85,0.00,'BOLETA','78459654','TARJETA','Registrada'),(5,1,'2026-03-27 00:43:48',0.35,0.05,'FACTURA','98457768455','YAPE','Registrada');
/*!40000 ALTER TABLE `ventas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-27 19:47:52
