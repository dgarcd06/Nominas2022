-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.28 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.0.0.6468
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para nominas
CREATE DATABASE IF NOT EXISTS `nominas` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `nominas`;

-- Volcando estructura para tabla nominas.categorias
CREATE TABLE IF NOT EXISTS `categorias` (
  `IdCategoria` int NOT NULL DEFAULT '10',
  `NombreCategoria` varchar(75) NOT NULL,
  `SalarioBaseCategoria` double NOT NULL,
  `ComplementoCategoria` double NOT NULL,
  PRIMARY KEY (`IdCategoria`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla nominas.categorias: ~4 rows (aproximadamente)
INSERT INTO `categorias` (`IdCategoria`, `NombreCategoria`, `SalarioBaseCategoria`, `ComplementoCategoria`) VALUES
	(1022, 'Jefe de sección', 19500, 3750),
	(5478, 'Programador', 17500, 2750),
	(8648, 'Administrativo', 14500, 1900),
	(9737, 'Coordinador', 32000, 5800);

-- Volcando estructura para tabla nominas.empresas
CREATE TABLE IF NOT EXISTS `empresas` (
  `IdEmpresa` int NOT NULL DEFAULT '10',
  `Nombre` varchar(100) NOT NULL,
  `CIF` varchar(10) NOT NULL,
  PRIMARY KEY (`IdEmpresa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla nominas.empresas: ~2 rows (aproximadamente)
INSERT INTO `empresas` (`IdEmpresa`, `Nombre`, `CIF`) VALUES
	(1014, 'TecnoLeonSL', 'P1254785I'),
	(2380, 'TecnoProyectSL', 'P2472621I');

-- Volcando estructura para tabla nominas.nomina
CREATE TABLE IF NOT EXISTS `nomina` (
  `IdNomina` int NOT NULL DEFAULT '10',
  `Mes` int NOT NULL DEFAULT '10',
  `Anio` int NOT NULL DEFAULT '10',
  `NumeroTrienios` int NOT NULL DEFAULT '10',
  `ImporteTrienios` double DEFAULT NULL,
  `importeSalarioMes` double DEFAULT NULL,
  `importeComplementoMes` double DEFAULT NULL,
  `ValorProrrateo` double DEFAULT NULL,
  `brutoAnual` double DEFAULT NULL,
  `IRPF` double DEFAULT NULL,
  `ImporteIRPF` double DEFAULT NULL,
  `BaseEmpresario` double DEFAULT NULL,
  `SeguridadSocialEmpresario` double DEFAULT NULL,
  `ImporteSeguridadSocialEmpresario` double DEFAULT NULL,
  `DesempleoEmpresario` double DEFAULT NULL,
  `ImporteDesempleoEmpresario` double DEFAULT NULL,
  `FormacionEmpresario` double DEFAULT NULL,
  `ImporteFormacionEmpresario` double DEFAULT NULL,
  `AccidentesTrabajoEmpresario` double DEFAULT NULL,
  `ImporteAccidentesTrabajoEmpresario` double DEFAULT NULL,
  `FOGASAEmpresario` double DEFAULT NULL,
  `ImporteFOGASAEmpresario` double DEFAULT NULL,
  `SeguridadSocialTrabajador` double DEFAULT NULL,
  `ImporteSeguridadSocialTrabajador` double DEFAULT NULL,
  `DesempleoTrabajador` double DEFAULT NULL,
  `ImporteDesempleoTrabajador` double DEFAULT NULL,
  `FormacionTrabajador` double DEFAULT NULL,
  `ImporteFormacionTrabajador` double DEFAULT NULL,
  `BrutoNomina` double DEFAULT NULL,
  `LiquidoNomina` double DEFAULT NULL,
  `CosteTotalEmpresario` double DEFAULT NULL,
  `idTrabajador` int NOT NULL DEFAULT '10',
  PRIMARY KEY (`IdNomina`),
  KEY `idTrabajador _idx` (`idTrabajador`),
  CONSTRAINT `idTrabajador ` FOREIGN KEY (`idTrabajador`) REFERENCES `trabajadorbbdd` (`idTrabajador`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla nominas.nomina: ~10 rows (aproximadamente)
INSERT INTO `nomina` (`IdNomina`, `Mes`, `Anio`, `NumeroTrienios`, `ImporteTrienios`, `importeSalarioMes`, `importeComplementoMes`, `ValorProrrateo`, `brutoAnual`, `IRPF`, `ImporteIRPF`, `BaseEmpresario`, `SeguridadSocialEmpresario`, `ImporteSeguridadSocialEmpresario`, `DesempleoEmpresario`, `ImporteDesempleoEmpresario`, `FormacionEmpresario`, `ImporteFormacionEmpresario`, `AccidentesTrabajoEmpresario`, `ImporteAccidentesTrabajoEmpresario`, `FOGASAEmpresario`, `ImporteFOGASAEmpresario`, `SeguridadSocialTrabajador`, `ImporteSeguridadSocialTrabajador`, `DesempleoTrabajador`, `ImporteDesempleoTrabajador`, `FormacionTrabajador`, `ImporteFormacionTrabajador`, `BrutoNomina`, `LiquidoNomina`, `CosteTotalEmpresario`, `idTrabajador`) VALUES
	(985, 6, 2022, 0, 0, 1250, 196.42857142857142, 0, 20250.02, 13, 188.03571428571428, 1446.4285714285713, 23.6, 398.25, 6.7, 113.0625, 0.6, 10.125, 1, 16.875, 0.2, 3.375, 4.7, 79.3125, 1.6, 27, 0.1, 1.6875, 1446.4285714285713, 1150.392857142857, 1988.1160714285713, 1276),
	(1545, 5, 2021, 1, 20, 1392.857142857143, 267.85714285714283, 232.14285714285714, 23529.96, 14.25, 239.50178571428572, 1960.83, 23.6, 462.75666666666666, 6.7, 131.37583333333333, 0.6, 11.765, 1, 19.608333333333334, 0.2, 3.921666666666667, 4.7, 92.15916666666668, 1.6, 31.373333333333335, 0.1, 1.9608333333333334, 1960.83, 1595.8348809523811, 2590.2574999999997, 1932),
	(1732, 6, 2022, 1, 20, 2285.714285714286, 414.2857142857143, 380.95238095238096, 38100, 20.1, 546.72, 3175, 23.599999999999998, 748.9066666666666, 6.7, 212.61333333333334, 0.6, 19.04, 1, 31.733333333333334, 0.2, 6.346666666666667, 4.7, 149.14666666666668, 1.6, 50.77333333333333, 0.1, 3.1733333333333333, 3175, 2425.1866666666665, 4193.64, 797),
	(3220, 5, 2021, 0, 0, 1250, 196.42857142857142, 0, 20250.02, 13, 188.03571428571428, 1687.5016666666668, 23.6, 398.25, 6.7, 113.0625, 0.6, 10.125, 1, 16.875, 0.2, 3.375, 4.7, 79.3125, 1.6, 27, 0.1, 1.6875, 1446.43, 1150.3942857142858, 1988.1175, 1276),
	(4168, 5, 2021, 2, 30, 1035.7142857142858, 135.71428571428572, 0, 16820.02, 10.55, 126.75071428571428, 1401.6683333333333, 23.6, 330.79333333333335, 6.7, 93.91166666666668, 0.6, 8.41, 1, 14.016666666666666, 0.2, 2.8033333333333332, 4.7, 65.87833333333333, 1.6, 22.426666666666666, 0.1, 1.4016666666666666, 1201.43, 984.9726190476191, 1651.365, 4938),
	(4591, 6, 2022, 0, 0, 1250, 196.42857142857142, 0, 20250.02, 13, 188.03571428571428, 1687.5016666666668, 23.6, 398.25, 6.7, 113.0625, 0.6, 10.125, 1, 16.875, 0.2, 3.375, 4.7, 79.3125, 1.6, 27, 0.1, 1.6875, 1446.43, 1150.3942857142858, 1988.1175, 1276),
	(5563, 6, 2022, 2, 30, 1035.7142857142858, 135.71428571428572, 0, 16820.02, 10.55, 126.75071428571428, 1401.6683333333333, 23.6, 330.79333333333335, 6.7, 93.91166666666668, 0.6, 8.41, 1, 14.016666666666666, 0.2, 2.8033333333333332, 4.7, 65.87833333333333, 1.6, 22.426666666666666, 0.1, 1.4016666666666666, 1201.43, 984.9726190476191, 1651.365, 4938),
	(6879, 5, 2021, 1, 20, 2285.714285714286, 414.2857142857143, 380.95238095238096, 38079.96, 20.1, 546.72, 3173.33, 23.599999999999998, 748.9066666666666, 6.7, 212.61333333333334, 0.6, 19.04, 1, 31.733333333333334, 0.2, 6.346666666666667, 4.7, 149.14666666666668, 1.6, 50.77333333333333, 0.1, 3.1733333333333333, 3173.33, 2423.5166666666664, 4191.97, 797),
	(9002, 6, 2022, 2, 30, 1035.7142857142858, 135.71428571428572, 0, 16820.02, 10.55, 126.75071428571428, 1201.4285714285713, 23.6, 330.79333333333335, 6.7, 93.91166666666668, 0.6, 8.41, 1, 14.016666666666666, 0.2, 2.8033333333333332, 4.7, 65.87833333333333, 1.6, 22.426666666666666, 0.1, 1.4016666666666666, 1201.4285714285713, 984.9711904761904, 1651.3635714285713, 4938),
	(9307, 6, 2022, 1, 20, 1392.857142857143, 267.85714285714283, 232.14285714285714, 23550, 14.25, 239.50178571428572, 1962.5, 23.6, 462.75666666666666, 6.7, 131.37583333333333, 0.6, 11.765, 1, 19.608333333333334, 0.2, 3.921666666666667, 4.7, 92.15916666666668, 1.6, 31.373333333333335, 0.1, 1.9608333333333334, 1962.5, 1597.5048809523812, 2591.9275, 1932);

-- Volcando estructura para tabla nominas.trabajadorbbdd
CREATE TABLE IF NOT EXISTS `trabajadorbbdd` (
  `idTrabajador` int NOT NULL DEFAULT '10',
  `Nombre` varchar(50) NOT NULL,
  `Apellido1` varchar(75) NOT NULL,
  `Apellido2` varchar(75) DEFAULT NULL,
  `NIFNIE` varchar(10) NOT NULL,
  `email` varchar(75) DEFAULT NULL,
  `FechaAlta` date DEFAULT NULL,
  `CodigoCuenta` varchar(20) DEFAULT NULL,
  `IBAN` varchar(24) DEFAULT NULL,
  `IdEmpresa` int NOT NULL DEFAULT '10',
  `IdCategoria` int NOT NULL DEFAULT '10',
  PRIMARY KEY (`idTrabajador`),
  KEY `IdEmpresa _idx` (`IdEmpresa`),
  KEY `IdCategoria _idx` (`IdCategoria`),
  CONSTRAINT `IdEmpresa ` FOREIGN KEY (`IdEmpresa`) REFERENCES `empresas` (`IdEmpresa`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla nominas.trabajadorbbdd: ~4 rows (aproximadamente)
INSERT INTO `trabajadorbbdd` (`idTrabajador`, `Nombre`, `Apellido1`, `Apellido2`, `NIFNIE`, `email`, `FechaAlta`, `CodigoCuenta`, `IBAN`, `IdEmpresa`, `IdCategoria`) VALUES
	(797, 'Consuelo', 'Francisco', 'López', '09652873A', 'LFC00@TecnoProyectSL.es', '2017-07-01', '20960043042158800000', 'ES8220960043042158800000', 2380, 9737),
	(1276, 'Martin', 'Gonzalez', 'Fernandez', '12345678Z', 'FGM00@TecnoLeonSL.es', '2020-01-01', '20960043062158805837', 'ES8120960043062158805837', 1014, 5478),
	(1932, 'Clementina', 'Montiel', 'Martínes', '09714235R', 'MMC00@TecnoLeonSL.es', '2017-04-01', '01826530120201560000', 'ES9001826530120201560000', 1014, 1022),
	(4938, 'Carolina', 'Mielgo', 'Gutierrez', '09341138X', 'GMC00@TecnoProyectSL.es', '2013-05-01', '20960583831234500000', 'ES3220960583831234500000', 2380, 8648);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
