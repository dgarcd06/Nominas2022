
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.Document;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.itextpdf.commons.actions.*;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import java.text.DateFormat;

public class ExcelManager {

    private String dniIntroducido;

    private static String dni_correcto;
    private static String ccc_correcto;
    private static ArrayList<String> correos = new ArrayList<String>();
    private static ArrayList<Trabajador> trabajadores = new ArrayList<Trabajador>();
    private static ArrayList<String> imp_trienios = new ArrayList<String>();
    private static ArrayList<Double> brutos = new ArrayList<Double>();
    private static ArrayList<Double> brutos_extra = new ArrayList<Double>();

    File outputFile = new File("C:\\Users\\davga\\OneDrive\\Escritorio\\SIST2\\Primera convocatoria\\Primera convocatoria\\a\\SistemasInformacionII.xlsx");

    public static ArrayList<Trabajador> getTrabajadores() {
        return trabajadores;
    }

    /**
     * La ubicación donde se almacenará el archivo de Excel, suponiendo que esté
     * debajo de la unidad D
     */
    public static void main(String argv[]) {
        FileInputStream file = null;
        XSSFWorkbook wb = null;
        //arraylist que guardara los dni correctos para comprobar los duplicados
        ArrayList<String> dni_mostrados = new ArrayList<String>();

        Row fila;
        Cell cell;

        //Creacion de archivo XML
        DocumentBuilderFactory docFactory_dni = DocumentBuilderFactory.newInstance();
        DocumentBuilderFactory docFactory_iban = DocumentBuilderFactory.newInstance();
        DocumentBuilderFactory docFactory_nominas = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder_dni = docFactory_dni.newDocumentBuilder();
            DocumentBuilder docBuilder_iban = docFactory_iban.newDocumentBuilder();
            DocumentBuilder docBuilder_nominas = docFactory_nominas.newDocumentBuilder();
            //archivo XML DNI
            org.w3c.dom.Document doc_dni = docBuilder_dni.newDocument();
            org.w3c.dom.Element root_dni = doc_dni.createElement("trabajadores");
            doc_dni.appendChild(root_dni);

            //archivo XML IBAN
            org.w3c.dom.Document doc_iban = docBuilder_iban.newDocument();
            org.w3c.dom.Element root_iban = doc_iban.createElement("cuentas");
            doc_iban.appendChild(root_iban);

            //archivo XML nominas
            org.w3c.dom.Document doc_nominas = docBuilder_nominas.newDocument();
            org.w3c.dom.Element root_nominas = doc_nominas.createElement("Nominas");

            //RUTA EXCEL
            file = new FileInputStream("C:\\Users\\davga\\OneDrive\\Escritorio\\Segunda convocatoria\\Segunda convocatoria\\c\\SistemasInformacionII.xlsx");

            wb = new XSSFWorkbook(file);
            file.close();

            XSSFSheet hoja1 = wb.getSheet("Hoja1");
            Iterator<Row> filas1 = hoja1.iterator();
            Iterator<Cell> celda_hoja1;
            XSSFSheet hoja2 = wb.getSheet("Hoja2");
            Iterator<Row> filas2 = hoja2.iterator();
            Iterator<Cell> celda_hoja2;
            XSSFSheet hoja3 = wb.getSheet("Hoja3");
            Iterator<Row> filas3 = hoja3.iterator();
            Iterator<Cell> celda_hoja3;
            XSSFSheet hoja4 = wb.getSheet("Hoja4");
            Iterator<Row> filas4 = hoja4.iterator();
            Iterator<Cell> celda_hoja4;
            XSSFSheet hoja5 = wb.getSheet("Hoja5");
            Iterator<Row> filas5 = hoja5.iterator();
            Iterator<Cell> celda_hoja5;
            filas5.next();
            int i = 1;
            String no_espacios;
            StringBuffer datos_trabajador = new StringBuffer();
            Cell celda_cuenta;
            Cell celda_iban;
            Cell celda_email;

            //Practica4
            Scanner scaner = new Scanner(System.in);
            String fecha;
            String mes;
            String año;
            Double descuentos;
            String aux;
            Double pagos;
            boolean checkNull = false;
            //PEDIR DATOS MES Y AÑO
            System.out.println("Introduce una fecha: ");
            fecha = scaner.nextLine();
            mes = fecha.substring(0, 2);
            año = fecha.substring(3);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

            root_nominas.setAttribute("fechaNomina", fecha);
            doc_nominas.appendChild(root_nominas);

            while (filas5.hasNext()) {

                Trabajador t = new Trabajador();
                fila = filas5.next();
                celda_hoja5 = fila.cellIterator();
                cell = celda_hoja5.next();

                //DATOS DE CADA TRABAJADOR
                if (fila.getCell(0) == null || fila.getCell(1) == null) {
                    checkNull = true;
                }
                if (checkNull == false) {
                    t.setDni(fila.getCell(0).toString());

                    t.setNombre(fila.getCell(1).toString());
                    if (fila.getCell(3) != null) {
                        t.setApellidos(fila.getCell(2).toString() + " " + fila.getCell(3).toString());
                    } else {
                        t.setApellidos(fila.getCell(2).toString());
                    }
                    t.setCif(fila.getCell(4).toString());
                    t.setEmpresa(fila.getCell(5).toString());
                    t.setFecha_alta(fila.getCell(6).getDateCellValue());
                    t.setCategoria_empleado(fila.getCell(7).toString());

                    aux = fila.getCell(8).toString();
                    if (aux.equals("SI")) {
                        t.setProrateo(true);
                        t.setExtra(false);
                    } else {
                        t.setProrateo(false);
                        t.setExtra(true);
                    }
                    celda_cuenta = fila.getCell(9);
                    celda_iban = fila.createCell(11);
                    celda_email = fila.getCell(12);

                    //Correccion del DNI/NIF/NIE
                    if (validar(cell.toString(), t)) {

                        fila.getCell(0).setCellValue(dni_correcto);
                        if (dni_mostrados.contains(cell.toString())) {

                            org.w3c.dom.Element dup = doc_dni.createElement("Trabajador");
                            dup.setAttribute("id", "" + i);
                            root_dni.appendChild(dup);

                            for (int j = 1; j < fila.toString().length(); j++) {
                                if (fila.getCell(j) != null && (j == 1 || j == 2 || j == 3 || j == 5 || j == 7)) {
                                    switch (j) {
                                        case 1:
                                            org.w3c.dom.Element name = doc_dni.createElement("Nombre");
                                            name.appendChild(doc_dni.createTextNode(fila.getCell(j).toString()));
                                            dup.appendChild(name);
                                            break;
                                        case 2:
                                            org.w3c.dom.Element apellido1 = doc_dni.createElement("Apellido1");
                                            apellido1.appendChild(doc_dni.createTextNode(fila.getCell(j).toString()));
                                            dup.appendChild(apellido1);
                                            break;
                                        case 3:
                                            org.w3c.dom.Element apellido2 = doc_dni.createElement("Apellido2");
                                            apellido2.appendChild(doc_dni.createTextNode(fila.getCell(j).toString()));
                                            dup.appendChild(apellido2);
                                            break;
                                        case 5:
                                            org.w3c.dom.Element name_empresa = doc_dni.createElement("Empresa");
                                            name_empresa.appendChild(doc_dni.createTextNode(fila.getCell(j).toString()));
                                            dup.appendChild(name_empresa);
                                            break;

                                        case 7:
                                            org.w3c.dom.Element categoria = doc_dni.createElement("Categoria");
                                            categoria.appendChild(doc_dni.createTextNode(fila.getCell(j).toString()));
                                            dup.appendChild(categoria);
                                            break;

                                    }
                                }
                            }
                        } else {
                            dni_mostrados.add(cell.toString());
                        }
                    } else if (celda_hoja5.hasNext()) {

                        org.w3c.dom.Element dup = doc_dni.createElement("Trabajador");
                        dup.setAttribute("id", "" + i);
                        root_dni.appendChild(dup);

                        for (int j = 1; j < fila.toString().length(); j++) {
                            if (fila.getCell(j) != null && (j == 1 || j == 2 || j == 3 || j == 5 || j == 7)) {
                                switch (j) {
                                    case 1:
                                        org.w3c.dom.Element name = doc_dni.createElement("Nombre");
                                        name.appendChild(doc_dni.createTextNode(fila.getCell(j).toString()));
                                        dup.appendChild(name);
                                        break;
                                    case 2:
                                        org.w3c.dom.Element apellido1 = doc_dni.createElement("Apellido1");
                                        apellido1.appendChild(doc_dni.createTextNode(fila.getCell(j).toString()));
                                        dup.appendChild(apellido1);
                                        break;
                                    case 3:
                                        org.w3c.dom.Element apellido2 = doc_dni.createElement("Apellido2");
                                        apellido2.appendChild(doc_dni.createTextNode(fila.getCell(j).toString()));
                                        dup.appendChild(apellido2);
                                        break;
                                    case 5:
                                        org.w3c.dom.Element name_empresa = doc_dni.createElement("Empresa");
                                        name_empresa.appendChild(doc_dni.createTextNode(fila.getCell(j).toString()));
                                        dup.appendChild(name_empresa);
                                        break;

                                    case 7:
                                        org.w3c.dom.Element categoria = doc_dni.createElement("Categoria");
                                        categoria.appendChild(doc_dni.createTextNode(fila.getCell(j).toString()));
                                        dup.appendChild(categoria);
                                        break;

                                }
                            }
                        }
                    }
                    if (celda_hoja5.hasNext() && corregirCuenta(celda_cuenta.toString())) {

                        celda_cuenta.setCellValue(ccc_correcto);
                        t.setCuenta(ccc_correcto);
                        celda_iban.setCellValue(generarIBAN(ccc_correcto, fila.getCell(10).toString()));
                        t.setIBAN(fila.getCell(11).toString());
                        String n = "";
                        String ap1 = "";
                        String ap2 = "";
                        String emp = "";
                        if (fila.getCell(1) != null) {
                            n = fila.getCell(1).toString();
                        }
                        if (fila.getCell(2) != null) {
                            ap1 = fila.getCell(2).toString();
                        }
                        if (fila.getCell(3) != null) {
                            ap2 = fila.getCell(3).toString();
                        }
                        if (fila.getCell(5) != null) {
                            emp = fila.getCell(5).toString();
                        }
                        fila.createCell(12);
                        fila.getCell(12).setCellValue(generarEmail(fila.getCell(1).toString(), fila.getCell(2).toString(), ap2, fila.getCell(5).toString()));
                        t.setEmail(fila.getCell(12).toString());
                    } else if (celda_hoja5.hasNext()) {

                        org.w3c.dom.Element cuenta_mal = doc_iban.createElement("cuenta");
                        cuenta_mal.setAttribute("id", "" + i);
                        root_iban.appendChild(cuenta_mal);

                        //nombre
                        org.w3c.dom.Element nameccc = doc_iban.createElement("Nombre");
                        nameccc.appendChild(doc_iban.createTextNode(fila.getCell(1).toString()));
                        cuenta_mal.appendChild(nameccc);

                        //apellido1
                        org.w3c.dom.Element ap1ccc = doc_iban.createElement("Apellido1");
                        ap1ccc.appendChild(doc_iban.createTextNode(fila.getCell(2).toString()));
                        cuenta_mal.appendChild(ap1ccc);

                        if (fila.getCell(3) != null) {
                            //apellido2
                            org.w3c.dom.Element ap2ccc = doc_iban.createElement("Apellido2");
                            ap2ccc.appendChild(doc_iban.createTextNode(fila.getCell(3).toString()));
                            cuenta_mal.appendChild(ap2ccc);
                        }

                        //empresa
                        org.w3c.dom.Element empccc = doc_iban.createElement("Empresa");
                        empccc.appendChild(doc_iban.createTextNode(fila.getCell(5).toString()));
                        cuenta_mal.appendChild(empccc);

                        //codigo cuenta erroneo
                        org.w3c.dom.Element ccc = doc_iban.createElement("CodigoCuentaErroneo");
                        ccc.appendChild(doc_iban.createTextNode(fila.getCell(9).toString()));
                        cuenta_mal.appendChild(ccc);

                        celda_cuenta.setCellValue(ccc_correcto);
                        t.setCuenta(ccc_correcto);
                        //generamos el IBAN
                        celda_iban.setCellValue(generarIBAN(ccc_correcto, fila.getCell(10).toString()));
                        t.setIBAN(celda_iban.getStringCellValue());
                        //iban
                        org.w3c.dom.Element ibanccc = doc_iban.createElement("IBAN");
                        ibanccc.appendChild(doc_iban.createTextNode(fila.getCell(11).toString()));
                        cuenta_mal.appendChild(ibanccc);

                        String n = "";
                        String ap1 = "";
                        String ap2 = "";
                        String emp = "";
                        if (fila.getCell(1) != null) {
                            n = fila.getCell(1).toString();
                        }
                        if (fila.getCell(2) != null) {
                            ap1 = fila.getCell(2).toString();
                        }
                        if (fila.getCell(3) != null) {
                            ap2 = fila.getCell(3).toString();
                        }
                        if (fila.getCell(5) != null) {
                            emp = fila.getCell(5).toString();
                        }
                        fila.createCell(12);
                        fila.getCell(12).setCellValue(generarEmail(fila.getCell(1).toString(), fila.getCell(2).toString(), ap2, fila.getCell(5).toString()));
                        t.setEmail(fila.getCell(12).toString());
                    }

                    Date fecha_alt_aux = t.getFecha_alta();
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                        String fecha_int = "01-" + mes + "-" + año;
                        Date fecha_introducida = sdf.parse(fecha_int);
                        t.setTrienio(t.getFecha_alta(), fecha_introducida);
                        if (antes(mes, año, t)) {
                            t.setIdFilaExcel(fila.getRowNum() + 1);
                            trabajadores.add(t);
                        }
                    } catch (ParseException Ex) {
                        Ex.printStackTrace();
                    }
                    i++;
                }
                checkNull = false;
            }
            //el iterador i se recicla para las demas hojas
            i = 1;
            //HOJA 3 => Salario base & complemetos segun categoria
            while (filas3.hasNext()) {
                fila = filas3.next();
                for (int j = 0; j < trabajadores.size(); j++) {
                    if (trabajadores.get(j).getCategoria_empleado().equals(fila.getCell(0).toString())) {
                        trabajadores.get(j).setComplementos(fila.getCell(1).getNumericCellValue());
                        trabajadores.get(j).setSalarioBase(fila.getCell(2).getNumericCellValue());
                    }
                }
            }

            //HOJA 2 => Complemento de trienios
            while (filas2.hasNext()) {
                fila = filas2.next();
                for (int j = 0; j < trabajadores.size(); j++) {
                    if (fila.getCell(0).toString().equals("Número de trienios")) {

                    } else {

                        if (trabajadores.get(j).getTrienio() == fila.getCell(0).getNumericCellValue()) {
                            trabajadores.get(j).setComplemento_trienios(Double.valueOf(fila.getCell(1).toString()));
                        }
                    }
                }
                if (fila.getCell(1).toString().equals("Importe bruto")) {

                } else {
                    imp_trienios.add(fila.getCell(1).toString());
                }
            }
            for (int j = 0; j < trabajadores.size(); j++) {
                trabajadores.get(j).setBruto(trabajadores.get(j).getSalarioBase(), trabajadores.get(j).getComplementos(), trabajadores.get(j).getComplemento_trienios());
            }
            //HOJA 4 => Retencion IRPF
            Double valor_anterior = 0.0;
            while (filas4.hasNext()) {

                fila = filas4.next();
                for (int j = 0; j < trabajadores.size(); j++) {
                    if (fila.getCell(0).toString().equals("Bruto anual")) {

                    } else {
                        if (fila.getCell(0).getNumericCellValue() > trabajadores.get(j).getBruto() && trabajadores.get(j).getIRPF() == null) {
                            trabajadores.get(j).setIRPF(fila.getCell(1).getNumericCellValue());
                        }
                    }

                }
                if (fila.getCell(1).toString().equals("Retención")) {

                } else {
                    valor_anterior = fila.getCell(1).getNumericCellValue();
                }
            }

            //HOJA 1 => Cuotas y otros descuentos
            while (filas1.hasNext()) {
                fila = filas1.next();
                for (int j = 0; j < trabajadores.size(); j++) {
                    switch (fila.getCell(0).toString()) {
                        case "Accidentes trabajo EMPRESARIO":
                            trabajadores.get(j).setAccidentes(Double.valueOf(fila.getCell(1).toString()));
                            break;
                        case "Contingencias comunes EMPRESARIO":
                            trabajadores.get(j).setContingencias_comunes(Double.valueOf(fila.getCell(1).toString()));
                            break;
                        case "Fogasa EMPRESARIO":
                            trabajadores.get(j).setFogasa(Double.valueOf(fila.getCell(1).toString()));
                            break;
                        case "Desempleo EMPRESARIO":
                            trabajadores.get(j).setDesempleo_emp(Double.valueOf(fila.getCell(1).toString()));
                            break;
                        case "Formacion EMPRESARIO":
                            trabajadores.get(j).setFormacion_emp(Double.valueOf(fila.getCell(1).toString()));
                            break;
                        case "Cuota obrera general TRABAJADOR":
                            trabajadores.get(j).setCuota_obrera(Double.valueOf(fila.getCell(1).toString()));
                            break;
                        case "Cuota desempleo TRABAJADOR":
                            trabajadores.get(j).setDesempleo_trabajador(Double.valueOf(fila.getCell(1).toString()));
                            break;
                        case "Cuota formación TRABAJADOR":
                            trabajadores.get(j).setFormacion_trabajador(Double.valueOf(fila.getCell(1).toString()));
                            break;
                    }
                }
            }

            for (int p = 0; p < trabajadores.size(); p++) {
                trabajadores.get(p).setNeto();
                trabajadores.get(p).setCoste_empresario();
            }

            String mes_texto;
            switch (mes) {
                case "01":
                    mes_texto = "Enero";
                    break;
                case "02":
                    mes_texto = "Febrero";
                    break;
                case "03":
                    mes_texto = "Marzo";
                    break;
                case "04":
                    mes_texto = "Abril";
                    break;
                case "05":
                    mes_texto = "Mayo";
                    break;
                case "06":
                    mes_texto = "Junio";
                    break;
                case "07":
                    mes_texto = "Julio";
                    break;
                case "08":
                    mes_texto = "Agosto";
                    break;
                case "09":
                    mes_texto = "Septiembre";
                    break;
                case "10":
                    mes_texto = "Octubre";
                    break;
                case "11":
                    mes_texto = "Noviembre";
                    break;
                case "12":
                    mes_texto = "Diciembre";
                    break;
                default:
                    mes_texto = "ENERO";
                    break;
            }
            //Muestra de nominas por pantalla
            //
            System.out.println("**NOMINAS DE TRABAJADORES PARA LA FECHA " + fecha + " **");
            for (int k = 0; k < trabajadores.size(); k++) {
                try {
                    //RUTA NOMINAS
                    File nomina = new File("C:\\Users\\davga\\OneDrive\\Escritorio\\aaa\\" + trabajadores.get(k).getDni() + trabajadores.get(k).getNombre() + trabajadores.get(k).getApellidos() + mes_texto + año + ".pdf");
                    PdfWriter pdfwriter = new PdfWriter(nomina);
                    PdfDocument pdfdocument = new PdfDocument(pdfwriter);
                    Document document_nomina = new com.itextpdf.layout.Document(pdfdocument);
                    PdfPage page = pdfdocument.addNewPage();

                    Double bruto_verdad;
                    Double trienio_plus = 0.0;
                    if ((trabajadores.get(k).getAño() + 1) % 3 == 0) {
                        for (int a = 0; a < imp_trienios.size(); a++) {
                            if (Double.valueOf(imp_trienios.get(a)) == trabajadores.get(k).getComplemento_trienios() / 14) {
                                trienio_plus = Double.valueOf(imp_trienios.get(a + 1));
                                break;
                            }
                        }
                    }

                    Double pr;
                    if ((trabajadores.get(k).getAño() + 1) % 3 == 0) {
                        pr = (trabajadores.get(k).getSalarioBase() + trabajadores.get(k).getComplementos() + (trienio_plus * 14));
                    } else {
                        pr = (trabajadores.get(k).getSalarioBase() + trabajadores.get(k).getComplementos() + trabajadores.get(k).getComplemento_trienios());
                    }

                    pr = pr / 84;

                    if (trabajadores.get(k).isProrateo()) {
                        bruto_verdad = redondear(trabajadores.get(k).getSalarioBase() / 14 + pr + trabajadores.get(k).getComplementos() / 14 + trabajadores.get(k).getComplemento_trienios() / 14);
                    } else {
                        bruto_verdad = redondear(trabajadores.get(k).getSalarioBase() / 12 + trabajadores.get(k).getComplementos() / 12 + trabajadores.get(k).getComplemento_trienios() / 12);
                    }
                    Paragraph nombre_normal = new Paragraph(trabajadores.get(k).getNombre() + " " + trabajadores.get(k).getApellidos());
                    document_nomina.add(nombre_normal);
                    document_nomina.add(new Paragraph("DNI: " + trabajadores.get(k).getDni()));
                    document_nomina.add(new Paragraph("Categoria: " + trabajadores.get(k).getCategoria_empleado()));
                    document_nomina.add(new Paragraph("IBAN: " + trabajadores.get(k).getIBAN()));
                    //COMPROBAR BRUTO SEGUN SI TIENE CAMBIO DE TRIENIO
                    if (trabajadores.get(k).getAño() == 0) {
                        document_nomina.add(new Paragraph("Bruto Anual: " + bruto_verdad * (12 - trabajadores.get(k).getMes() + 1)));
                    } else {
                        document_nomina.add(new Paragraph("Bruto Anual: " + bruto_verdad * 12));
                    }
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String today = formatter.format(trabajadores.get(k).getFecha_alta());

                    document_nomina.add(new Paragraph("Fecha de alta: " + today));
                    document_nomina.add(new Paragraph("Empresa: " + trabajadores.get(k).getEmpresa() + " (CIF: " + trabajadores.get(k).getCif() + ")"));

                    //titulo de nomina
                    Style estilo = new Style().setBold();
                    estilo.setFontSize(12);
                    ArrayList<TabStop> centrarTexto = new ArrayList<>();
                    centrarTexto.add(new TabStop((pdfdocument.getDefaultPageSize().getWidth() - document_nomina.getLeftMargin() - document_nomina.getRightMargin()) / 2, TabAlignment.CENTER));
                    Paragraph p = new Paragraph().addTabStops(centrarTexto);
                    p.add(new Tab());
                    p.add("Nomina: " + mes_texto + " " + año).addStyle(estilo);
                    p.add(new Tab());
                    p.setMarginTop(10);
                    document_nomina.add(p);

                    /**
                     *
                     * CONCEPTOS DEL TRABAJADOR
                     *
                     */
                    Table tabla_trabajador = new Table(5);

                    tabla_trabajador.setMarginTop(5);
                    tabla_trabajador.setWidth(520);
                    tabla_trabajador.setBorderTop(new SolidBorder(1));
                    tabla_trabajador.setBorderBottom(new SolidBorder(1));
                    //CELDA CONCEPTOS
                    com.itextpdf.layout.element.Cell celda_conceptos = new com.itextpdf.layout.element.Cell();
                    celda_conceptos.setTextAlignment(TextAlignment.LEFT);
                    celda_conceptos.setWidth(250);
                    celda_conceptos.setBorder(Border.NO_BORDER);
                    celda_conceptos.add(new Paragraph("Conceptos"));
                    tabla_trabajador.addCell(celda_conceptos);
                    //CELDA CANTIDAD
                    com.itextpdf.layout.element.Cell celda_cantidad = new com.itextpdf.layout.element.Cell();
                    celda_cantidad.setTextAlignment(TextAlignment.CENTER);
                    celda_cantidad.setWidth(130);
                    celda_cantidad.setBorder(Border.NO_BORDER);
                    celda_cantidad.add(new Paragraph("Cantidad"));
                    tabla_trabajador.addCell(celda_cantidad);

                    //CELDA IMP.UNITARIO
                    com.itextpdf.layout.element.Cell celda_impunitario = new com.itextpdf.layout.element.Cell();
                    celda_impunitario.setTextAlignment(TextAlignment.CENTER);
                    celda_impunitario.setWidth(130);
                    celda_impunitario.setBorder(Border.NO_BORDER);
                    celda_impunitario.add(new Paragraph("Imp.Unitario"));
                    tabla_trabajador.addCell(celda_impunitario);

                    //CELDA DEVENGO
                    com.itextpdf.layout.element.Cell celda_devengos = new com.itextpdf.layout.element.Cell();
                    celda_devengos.setTextAlignment(TextAlignment.CENTER);
                    celda_devengos.setWidth(130);
                    celda_devengos.setBorder(Border.NO_BORDER);
                    celda_devengos.add(new Paragraph("Devengo"));
                    tabla_trabajador.addCell(celda_devengos);

                    //CELDA DEDUCCION
                    com.itextpdf.layout.element.Cell celda_deduccion = new com.itextpdf.layout.element.Cell();
                    celda_deduccion.setTextAlignment(TextAlignment.CENTER);
                    celda_deduccion.setWidth(130);
                    celda_deduccion.setBorder(Border.NO_BORDER);
                    celda_deduccion.add(new Paragraph("Deduccion"));
                    tabla_trabajador.addCell(celda_deduccion);

                    document_nomina.add(tabla_trabajador);

                    //SALARIO BASE
                    Table salario_base = new Table(5);
                    com.itextpdf.layout.element.Cell celda_titulo_salariobase = new com.itextpdf.layout.element.Cell();
                    celda_titulo_salariobase.setTextAlignment(TextAlignment.LEFT);
                    celda_titulo_salariobase.setWidth(130);
                    celda_titulo_salariobase.setBorder(Border.NO_BORDER);
                    celda_titulo_salariobase.add(new Paragraph("Salario Base"));
                    salario_base.addCell(celda_titulo_salariobase);

                    com.itextpdf.layout.element.Cell celda_cantidad_salariobase = new com.itextpdf.layout.element.Cell();
                    celda_cantidad_salariobase.setTextAlignment(TextAlignment.CENTER);
                    celda_cantidad_salariobase.setWidth(100);
                    celda_cantidad_salariobase.setBorder(Border.NO_BORDER);
                    celda_cantidad_salariobase.add(new Paragraph("30 dias"));
                    salario_base.addCell(celda_cantidad_salariobase);

                    com.itextpdf.layout.element.Cell celda_unitario_salariobase = new com.itextpdf.layout.element.Cell();
                    celda_unitario_salariobase.setTextAlignment(TextAlignment.CENTER);
                    celda_unitario_salariobase.setWidth(100);
                    celda_unitario_salariobase.setBorder(Border.NO_BORDER);
                    celda_unitario_salariobase.add(new Paragraph("" + redondear(trabajadores.get(k).getSalarioBase() / 14 / 30)));
                    salario_base.addCell(celda_unitario_salariobase);

                    com.itextpdf.layout.element.Cell celda_salario_salariobase = new com.itextpdf.layout.element.Cell();
                    celda_salario_salariobase.setTextAlignment(TextAlignment.CENTER);
                    celda_salario_salariobase.setWidth(100);
                    celda_salario_salariobase.setBorder(Border.NO_BORDER);
                    celda_salario_salariobase.add(new Paragraph("" + redondear(trabajadores.get(k).getSalarioBase() / 14)));
                    salario_base.addCell(celda_salario_salariobase);

                    salario_base.addCell(celda_salario_salariobase);
                    document_nomina.add(salario_base);

                    //Prorrateo
                    Table prorrateo = new Table(5);
                    com.itextpdf.layout.element.Cell celda_titulo_prorrateo = new com.itextpdf.layout.element.Cell();
                    celda_titulo_prorrateo.setTextAlignment(TextAlignment.LEFT);
                    celda_titulo_prorrateo.setWidth(130);
                    celda_titulo_prorrateo.setBorder(Border.NO_BORDER);
                    celda_titulo_prorrateo.add(new Paragraph("Prorrateo"));
                    prorrateo.addCell(celda_titulo_prorrateo);

                    com.itextpdf.layout.element.Cell celda_cantidad_prorrateo = new com.itextpdf.layout.element.Cell();
                    celda_cantidad_prorrateo.setTextAlignment(TextAlignment.CENTER);
                    celda_cantidad_prorrateo.setWidth(100);
                    celda_cantidad_prorrateo.setBorder(Border.NO_BORDER);
                    celda_cantidad_prorrateo.add(new Paragraph("30 dias"));
                    prorrateo.addCell(celda_cantidad_prorrateo);

                    com.itextpdf.layout.element.Cell celda_unitario_prorrateo = new com.itextpdf.layout.element.Cell();
                    celda_unitario_prorrateo.setTextAlignment(TextAlignment.CENTER);
                    celda_unitario_prorrateo.setWidth(100);
                    celda_unitario_prorrateo.setBorder(Border.NO_BORDER);
                    if (trabajadores.get(k).isProrateo()) {
                        celda_unitario_prorrateo.add(new Paragraph("" + redondear(pr / 30)));
                    } else {
                        celda_unitario_prorrateo.add(new Paragraph("0.00"));

                    }
                    prorrateo.addCell(celda_unitario_prorrateo);

                    com.itextpdf.layout.element.Cell celda_salario_prorrateo = new com.itextpdf.layout.element.Cell();
                    celda_salario_prorrateo.setTextAlignment(TextAlignment.CENTER);
                    celda_salario_prorrateo.setWidth(100);
                    celda_salario_prorrateo.setBorder(Border.NO_BORDER);
                    if (trabajadores.get(k).isProrateo()) {
                        celda_salario_prorrateo.add(new Paragraph("" + redondear(pr)));
                    } else {
                        celda_salario_prorrateo.add(new Paragraph("0.00"));

                    }
                    prorrateo.addCell(celda_salario_prorrateo);

                    prorrateo.addCell(celda_salario_prorrateo);
                    document_nomina.add(prorrateo);

                    //Complementos
                    Table complementos = new Table(5);
                    com.itextpdf.layout.element.Cell celda_titulo_complementos = new com.itextpdf.layout.element.Cell();
                    celda_titulo_complementos.setTextAlignment(TextAlignment.LEFT);
                    celda_titulo_complementos.setWidth(130);
                    celda_titulo_complementos.setBorder(Border.NO_BORDER);
                    celda_titulo_complementos.add(new Paragraph("Complementos"));
                    complementos.addCell(celda_titulo_complementos);

                    com.itextpdf.layout.element.Cell celda_cantidad_complementos = new com.itextpdf.layout.element.Cell();
                    celda_cantidad_complementos.setTextAlignment(TextAlignment.CENTER);
                    celda_cantidad_complementos.setWidth(100);
                    celda_cantidad_complementos.setBorder(Border.NO_BORDER);
                    celda_cantidad_complementos.add(new Paragraph("30 dias"));
                    complementos.addCell(celda_cantidad_complementos);

                    com.itextpdf.layout.element.Cell celda_unitario_complementos = new com.itextpdf.layout.element.Cell();
                    celda_unitario_complementos.setTextAlignment(TextAlignment.CENTER);
                    celda_unitario_complementos.setWidth(100);
                    celda_unitario_complementos.setBorder(Border.NO_BORDER);
                    celda_unitario_complementos.add(new Paragraph("" + redondear(trabajadores.get(k).getComplementos() / 14 / 30)));
                    complementos.addCell(celda_unitario_complementos);

                    com.itextpdf.layout.element.Cell celda_salario_complementos = new com.itextpdf.layout.element.Cell();
                    celda_salario_complementos.setTextAlignment(TextAlignment.CENTER);
                    celda_salario_complementos.setWidth(100);
                    celda_salario_complementos.setBorder(Border.NO_BORDER);
                    celda_salario_complementos.add(new Paragraph("" + redondear(trabajadores.get(k).getComplementos() / 14)));
                    complementos.addCell(celda_salario_complementos);

                    complementos.addCell(celda_salario_complementos);
                    document_nomina.add(complementos);

                    //Trienios
                    Table trienios = new Table(5);
                    com.itextpdf.layout.element.Cell celda_titulo_trienios = new com.itextpdf.layout.element.Cell();
                    celda_titulo_trienios.setTextAlignment(TextAlignment.LEFT);
                    celda_titulo_trienios.setWidth(130);
                    celda_titulo_trienios.setBorder(Border.NO_BORDER);
                    celda_titulo_trienios.add(new Paragraph("Antigüedad"));
                    trienios.addCell(celda_titulo_trienios);

                    com.itextpdf.layout.element.Cell celda_cantidad_trienios = new com.itextpdf.layout.element.Cell();
                    celda_cantidad_trienios.setTextAlignment(TextAlignment.CENTER);
                    celda_cantidad_trienios.setWidth(100);
                    celda_cantidad_trienios.setBorder(Border.NO_BORDER);
                    celda_cantidad_trienios.add(new Paragraph(trabajadores.get(k).getTrienio() + " trienio/s"));
                    trienios.addCell(celda_cantidad_trienios);

                    com.itextpdf.layout.element.Cell celda_unitario_trienios = new com.itextpdf.layout.element.Cell();
                    celda_unitario_trienios.setTextAlignment(TextAlignment.CENTER);
                    celda_unitario_trienios.setWidth(100);
                    celda_unitario_trienios.setBorder(Border.NO_BORDER);
                    if (trabajadores.get(k).getTrienio() == 0) {
                        celda_unitario_trienios.add(new Paragraph());
                    } else {
                        celda_unitario_trienios.add(new Paragraph("" + redondear(trabajadores.get(k).getComplemento_trienios() / 14 / trabajadores.get(k).getTrienio())));
                    }
                    trienios.addCell(celda_unitario_trienios);

                    com.itextpdf.layout.element.Cell celda_salario_trienios = new com.itextpdf.layout.element.Cell();
                    celda_salario_trienios.setTextAlignment(TextAlignment.CENTER);
                    celda_salario_trienios.setWidth(100);
                    celda_salario_trienios.setBorder(Border.NO_BORDER);
                    celda_salario_trienios.add(new Paragraph("" + redondear(trabajadores.get(k).getComplemento_trienios() / 14)));
                    trienios.addCell(celda_salario_trienios);

                    trienios.addCell(celda_salario_trienios);
                    document_nomina.add(trienios);

                    //Contingencias generales
                    Table contingencias_generales = new Table(5);
                    com.itextpdf.layout.element.Cell celda_titulo_contingenciasgenerales = new com.itextpdf.layout.element.Cell();
                    celda_titulo_contingenciasgenerales.setTextAlignment(TextAlignment.LEFT);
                    celda_titulo_contingenciasgenerales.setWidth(175);
                    celda_titulo_contingenciasgenerales.setBorder(Border.NO_BORDER);
                    celda_titulo_contingenciasgenerales.add(new Paragraph("Cont. Generales"));
                    contingencias_generales.addCell(celda_titulo_contingenciasgenerales);

                    com.itextpdf.layout.element.Cell celda_cantidad_contingenciasgenerales = new com.itextpdf.layout.element.Cell();
                    celda_cantidad_contingenciasgenerales.setTextAlignment(TextAlignment.CENTER);
                    celda_cantidad_contingenciasgenerales.setWidth(130);
                    celda_cantidad_contingenciasgenerales.setBorder(Border.NO_BORDER);
                    Double p_cont_tr = (double) Math.round((trabajadores.get(k).getCuota_obrera() * 100) / trabajadores.get(k).getBruto() * 100) / 100;
                    celda_cantidad_contingenciasgenerales.add(new Paragraph("" + p_cont_tr + " % de " + bruto_verdad));
                    contingencias_generales.addCell(celda_cantidad_contingenciasgenerales);

                    com.itextpdf.layout.element.Cell celda_v_contingenciasgenerales = new com.itextpdf.layout.element.Cell();
                    celda_v_contingenciasgenerales.setBorder(Border.NO_BORDER);
                    celda_v_contingenciasgenerales.setWidth(50);
                    celda_v_contingenciasgenerales.add(new Paragraph());
                    contingencias_generales.addCell(celda_v_contingenciasgenerales);

                    com.itextpdf.layout.element.Cell celda_v_contingenciasgenerales2 = new com.itextpdf.layout.element.Cell();
                    celda_v_contingenciasgenerales2.setBorder(Border.NO_BORDER);
                    celda_v_contingenciasgenerales2.setWidth(150);
                    celda_v_contingenciasgenerales2.add(new Paragraph());
                    contingencias_generales.addCell(celda_v_contingenciasgenerales2);

                    com.itextpdf.layout.element.Cell celda_precio_contingenciasgenerales = new com.itextpdf.layout.element.Cell();
                    celda_precio_contingenciasgenerales.setTextAlignment(TextAlignment.CENTER);
                    celda_precio_contingenciasgenerales.setWidth(130);
                    celda_precio_contingenciasgenerales.setBorder(Border.NO_BORDER);
                    celda_precio_contingenciasgenerales.add(new Paragraph("" + redondear(p_cont_tr * bruto_verdad / 100)));
                    contingencias_generales.addCell(celda_precio_contingenciasgenerales);
                    document_nomina.add(contingencias_generales);

                    //Desempleo
                    Table desempleo = new Table(5);
                    com.itextpdf.layout.element.Cell celda_titulo_desempleo = new com.itextpdf.layout.element.Cell();
                    celda_titulo_desempleo.setTextAlignment(TextAlignment.LEFT);
                    celda_titulo_desempleo.setWidth(175);
                    celda_titulo_desempleo.setBorder(Border.NO_BORDER);
                    celda_titulo_desempleo.add(new Paragraph("Desempleo"));
                    desempleo.addCell(celda_titulo_desempleo);

                    com.itextpdf.layout.element.Cell celda_cantidad_desempleo = new com.itextpdf.layout.element.Cell();
                    celda_cantidad_desempleo.setTextAlignment(TextAlignment.CENTER);
                    celda_cantidad_desempleo.setWidth(130);
                    celda_cantidad_desempleo.setBorder(Border.NO_BORDER);
                    Double p_des_tr = (double) Math.round((trabajadores.get(k).getDesempleo_trabajador() * 100) / trabajadores.get(k).getBruto() * 100) / 100;
                    celda_cantidad_desempleo.add(new Paragraph("" + p_des_tr + " % de " + bruto_verdad));
                    desempleo.addCell(celda_cantidad_desempleo);

                    com.itextpdf.layout.element.Cell celda_v_desempleo = new com.itextpdf.layout.element.Cell();
                    celda_v_desempleo.setBorder(Border.NO_BORDER);
                    celda_v_desempleo.setWidth(50);
                    celda_v_desempleo.add(new Paragraph());
                    desempleo.addCell(celda_v_desempleo);

                    com.itextpdf.layout.element.Cell celda_v_desempleo2 = new com.itextpdf.layout.element.Cell();
                    celda_v_desempleo2.setBorder(Border.NO_BORDER);
                    celda_v_desempleo2.setWidth(150);
                    celda_v_desempleo2.add(new Paragraph());
                    desempleo.addCell(celda_v_desempleo2);

                    com.itextpdf.layout.element.Cell celda_precio_desempleo = new com.itextpdf.layout.element.Cell();
                    celda_precio_desempleo.setTextAlignment(TextAlignment.CENTER);
                    celda_precio_desempleo.setWidth(130);
                    celda_precio_desempleo.setBorder(Border.NO_BORDER);
                    celda_precio_desempleo.add(new Paragraph("" + redondear(p_des_tr * bruto_verdad / 100)));
                    desempleo.addCell(celda_precio_desempleo);
                    document_nomina.add(desempleo);

                    //Formacion
                    Table formacion = new Table(5);
                    com.itextpdf.layout.element.Cell celda_titulo_formacion = new com.itextpdf.layout.element.Cell();
                    celda_titulo_formacion.setTextAlignment(TextAlignment.LEFT);
                    celda_titulo_formacion.setWidth(175);
                    celda_titulo_formacion.setBorder(Border.NO_BORDER);
                    celda_titulo_formacion.add(new Paragraph("Cuota Formacion"));
                    formacion.addCell(celda_titulo_formacion);

                    com.itextpdf.layout.element.Cell celda_cantidad_formacion = new com.itextpdf.layout.element.Cell();
                    celda_cantidad_formacion.setTextAlignment(TextAlignment.CENTER);
                    celda_cantidad_formacion.setWidth(130);
                    celda_cantidad_formacion.setBorder(Border.NO_BORDER);
                    Double p_for_tr = (double) Math.round((trabajadores.get(k).getFormacion_trabajador() * 100) / trabajadores.get(k).getBruto() * 100) / 100;
                    celda_cantidad_formacion.add(new Paragraph("" + p_for_tr + " % de " + bruto_verdad));
                    formacion.addCell(celda_cantidad_formacion);

                    com.itextpdf.layout.element.Cell celda_v_formacion = new com.itextpdf.layout.element.Cell();
                    celda_v_formacion.setBorder(Border.NO_BORDER);
                    celda_v_formacion.setWidth(50);
                    celda_v_formacion.add(new Paragraph());
                    formacion.addCell(celda_v_formacion);

                    com.itextpdf.layout.element.Cell celda_v_formacion2 = new com.itextpdf.layout.element.Cell();
                    celda_v_formacion2.setBorder(Border.NO_BORDER);
                    celda_v_formacion2.setWidth(150);
                    celda_v_formacion2.add(new Paragraph());
                    formacion.addCell(celda_v_formacion2);

                    com.itextpdf.layout.element.Cell celda_precio_formacion = new com.itextpdf.layout.element.Cell();
                    celda_precio_formacion.setTextAlignment(TextAlignment.CENTER);
                    celda_precio_formacion.setWidth(130);
                    celda_precio_formacion.setBorder(Border.NO_BORDER);
                    celda_precio_formacion.add(new Paragraph("" + redondear(p_for_tr * bruto_verdad / 100)));
                    formacion.addCell(celda_precio_formacion);
                    document_nomina.add(formacion);

                    //IRPF
                    Table IRPF = new Table(5);
                    com.itextpdf.layout.element.Cell celda_titulo_irpf = new com.itextpdf.layout.element.Cell();
                    celda_titulo_irpf.setTextAlignment(TextAlignment.LEFT);
                    celda_titulo_irpf.setWidth(170);
                    celda_titulo_irpf.setBorder(Border.NO_BORDER);
                    celda_titulo_irpf.add(new Paragraph("IRPF"));
                    IRPF.addCell(celda_titulo_irpf);

                    com.itextpdf.layout.element.Cell celda_cantidad_irpf = new com.itextpdf.layout.element.Cell();
                    celda_cantidad_irpf.setTextAlignment(TextAlignment.CENTER);
                    celda_cantidad_irpf.setWidth(130);
                    celda_cantidad_irpf.setBorder(Border.NO_BORDER);
                    Double calculo_IRPF;
                    if (trabajadores.get(k).getAño() == 0) {
                        if (trabajadores.get(k).isProrateo()) {
                            calculo_IRPF = (bruto_verdad) * trabajadores.get(k).getPorcentaje_IRPF() / 100;
                            celda_cantidad_irpf.add(new Paragraph("0.00 % de " + bruto_verdad));

                        } else {
                            calculo_IRPF = (bruto_verdad * 12 / 14) * trabajadores.get(k).getPorcentaje_IRPF() / 100;
                            celda_cantidad_irpf.add(new Paragraph("0.00 % de " + redondear(bruto_verdad * 12 / 14)));

                        }
                    } else {
                        if (trabajadores.get(k).isProrateo()) {
                            calculo_IRPF = (bruto_verdad) * trabajadores.get(k).getPorcentaje_IRPF() / 100;
                            celda_cantidad_irpf.add(new Paragraph("" + trabajadores.get(k).getPorcentaje_IRPF() + " % de " + bruto_verdad));

                        } else {
                            calculo_IRPF = (bruto_verdad * 12 / 14) * trabajadores.get(k).getPorcentaje_IRPF() / 100;
                            celda_cantidad_irpf.add(new Paragraph("" + trabajadores.get(k).getPorcentaje_IRPF() + " % de " + redondear(bruto_verdad * 12 / 14)));

                        }
                    }

                    IRPF.addCell(celda_cantidad_irpf);

                    com.itextpdf.layout.element.Cell celda_v_irpf = new com.itextpdf.layout.element.Cell();
                    celda_v_irpf.setBorder(Border.NO_BORDER);
                    celda_v_irpf.setWidth(50);
                    celda_v_irpf.add(new Paragraph());
                    IRPF.addCell(celda_v_irpf);

                    com.itextpdf.layout.element.Cell celda_v_irpf2 = new com.itextpdf.layout.element.Cell();
                    celda_v_irpf2.setBorder(Border.NO_BORDER);
                    celda_v_irpf2.setWidth(150);
                    celda_v_irpf2.add(new Paragraph());
                    IRPF.addCell(celda_v_irpf2);

                    com.itextpdf.layout.element.Cell celda_precio_irpf = new com.itextpdf.layout.element.Cell();
                    celda_precio_irpf.setTextAlignment(TextAlignment.CENTER);
                    celda_precio_irpf.setWidth(130);
                    celda_precio_irpf.setBorder(Border.NO_BORDER);
                    if (trabajadores.get(k).getAño() == 0) {
                        celda_precio_irpf.add(new Paragraph("0.00"));
                    } else {
                        celda_precio_irpf.add(new Paragraph("" + redondear(calculo_IRPF)));
                    }

                    IRPF.addCell(celda_precio_irpf);
                    document_nomina.add(IRPF);

                    //Total deducciones
                    Table total_deducciones = new Table(5);
                    total_deducciones.setBorderTop(new SolidBorder(1));
                    com.itextpdf.layout.element.Cell celda_titulo_totaldeducciones = new com.itextpdf.layout.element.Cell();
                    celda_titulo_totaldeducciones.setTextAlignment(TextAlignment.LEFT);
                    celda_titulo_totaldeducciones.setWidth(190);
                    celda_titulo_totaldeducciones.setBorder(Border.NO_BORDER);
                    celda_titulo_totaldeducciones.add(new Paragraph("Total Deducciones"));
                    total_deducciones.addCell(celda_titulo_totaldeducciones);

                    com.itextpdf.layout.element.Cell celda_v_totaldeducciones = new com.itextpdf.layout.element.Cell();
                    celda_v_totaldeducciones.setBorder(Border.NO_BORDER);
                    celda_v_totaldeducciones.setWidth(150);
                    celda_v_totaldeducciones.add(new Paragraph());
                    total_deducciones.addCell(celda_v_totaldeducciones);

                    com.itextpdf.layout.element.Cell celda_v_totaldeducciones2 = new com.itextpdf.layout.element.Cell();
                    celda_v_totaldeducciones2.setBorder(Border.NO_BORDER);
                    celda_v_totaldeducciones2.setWidth(150);
                    celda_v_totaldeducciones2.add(new Paragraph());
                    total_deducciones.addCell(celda_v_totaldeducciones2);

                    com.itextpdf.layout.element.Cell celda_v_totaldeducciones3 = new com.itextpdf.layout.element.Cell();
                    celda_v_totaldeducciones3.setBorder(Border.NO_BORDER);
                    celda_v_totaldeducciones3.setWidth(250);
                    celda_v_totaldeducciones3.add(new Paragraph());
                    total_deducciones.addCell(celda_v_totaldeducciones3);

                    com.itextpdf.layout.element.Cell celda_total_deducciones = new com.itextpdf.layout.element.Cell();
                    celda_total_deducciones.setBorder(Border.NO_BORDER);
                    celda_total_deducciones.setWidth(90);
                    Double deducciones;
                    if (trabajadores.get(k).getAño() == 0) {
                        deducciones = redondear((p_for_tr * bruto_verdad / 100) + (p_des_tr * bruto_verdad / 100) + (p_cont_tr * bruto_verdad / 100));

                    } else {
                        deducciones = redondear(calculo_IRPF + (p_for_tr * bruto_verdad / 100) + (p_des_tr * bruto_verdad / 100) + (p_cont_tr * bruto_verdad / 100));
                    }
                    celda_total_deducciones.add(new Paragraph("" + deducciones));
                    total_deducciones.addCell(celda_total_deducciones);
                    document_nomina.add(total_deducciones);

                    //Total devengos
                    Table total_devengos = new Table(5);
                    total_devengos.setBorderBottom(new SolidBorder(1));
                    com.itextpdf.layout.element.Cell celda_titulo_totaldevengos = new com.itextpdf.layout.element.Cell();
                    celda_titulo_totaldevengos.setTextAlignment(TextAlignment.LEFT);
                    celda_titulo_totaldevengos.setWidth(190);
                    celda_titulo_totaldevengos.setBorder(Border.NO_BORDER);
                    celda_titulo_totaldevengos.add(new Paragraph("Total Devengos"));
                    total_devengos.addCell(celda_titulo_totaldevengos);

                    com.itextpdf.layout.element.Cell celda_v_totaldevengos = new com.itextpdf.layout.element.Cell();
                    celda_v_totaldevengos.setBorder(Border.NO_BORDER);
                    celda_v_totaldevengos.setWidth(150);
                    celda_v_totaldevengos.add(new Paragraph());
                    total_devengos.addCell(celda_v_totaldevengos);

                    com.itextpdf.layout.element.Cell celda_v_totaldevengos2 = new com.itextpdf.layout.element.Cell();
                    celda_v_totaldevengos2.setBorder(Border.NO_BORDER);
                    celda_v_totaldevengos2.setWidth(150);
                    celda_v_totaldevengos2.add(new Paragraph());
                    total_devengos.addCell(celda_v_totaldevengos2);

                    com.itextpdf.layout.element.Cell celda_v_totaldevengos3 = new com.itextpdf.layout.element.Cell();
                    celda_v_totaldevengos3.setBorder(Border.NO_BORDER);
                    celda_v_totaldevengos3.setWidth(250);
                    celda_v_totaldevengos3.add(new Paragraph());
                    total_devengos.addCell(celda_v_totaldevengos3);

                    com.itextpdf.layout.element.Cell celda_total_devengos = new com.itextpdf.layout.element.Cell();
                    celda_total_devengos.setBorder(Border.NO_BORDER);
                    celda_total_devengos.setWidth(100);
                    Double devengos;
                    if (trabajadores.get(k).isProrateo()) {
                        devengos = redondear(trabajadores.get(k).getSalarioBase() / 14 + pr + trabajadores.get(k).getComplementos() / 14 + trabajadores.get(k).getComplemento_trienios() / 14);
                        celda_total_devengos.add(new Paragraph("" + devengos));
                    } else {
                        devengos = redondear(trabajadores.get(k).getSalarioBase() / 14 + trabajadores.get(k).getComplementos() / 14 + trabajadores.get(k).getComplemento_trienios() / 14);
                        celda_total_devengos.add(new Paragraph("" + devengos));
                    }
                    brutos.add(devengos);
                    total_devengos.addCell(celda_total_devengos);
                    document_nomina.add(total_devengos);

                    //Liquido a percibir
                    Table neto = new Table(5);
                    com.itextpdf.layout.element.Cell celda_titulo_liquido = new com.itextpdf.layout.element.Cell();
                    celda_titulo_liquido.setTextAlignment(TextAlignment.LEFT);
                    celda_titulo_liquido.setWidth(190);
                    celda_titulo_liquido.setBorder(Border.NO_BORDER);
                    celda_titulo_liquido.add(new Paragraph("Liquido a percibir"));
                    neto.addCell(celda_titulo_liquido);

                    com.itextpdf.layout.element.Cell celda_v_liquido = new com.itextpdf.layout.element.Cell();
                    celda_v_liquido.setBorder(Border.NO_BORDER);
                    celda_v_liquido.setWidth(150);
                    celda_v_liquido.add(new Paragraph());
                    neto.addCell(celda_v_liquido);

                    com.itextpdf.layout.element.Cell celda_v_liquido2 = new com.itextpdf.layout.element.Cell();
                    celda_v_liquido2.setBorder(Border.NO_BORDER);
                    celda_v_liquido2.setWidth(150);
                    celda_v_liquido2.add(new Paragraph());
                    neto.addCell(celda_v_liquido2);

                    com.itextpdf.layout.element.Cell celda_v_liquido3 = new com.itextpdf.layout.element.Cell();
                    celda_v_liquido3.setBorder(Border.NO_BORDER);
                    celda_v_liquido3.setWidth(250);
                    celda_v_liquido3.add(new Paragraph());
                    neto.addCell(celda_v_liquido3);

                    com.itextpdf.layout.element.Cell celda_total_liquido = new com.itextpdf.layout.element.Cell();
                    celda_total_liquido.setBorder(Border.NO_BORDER);
                    celda_total_liquido.setWidth(100);

                    Double liquido_total;
                    liquido_total = devengos - deducciones;
                    celda_total_liquido.add(new Paragraph("" + redondear(liquido_total)));

                    neto.addCell(celda_total_liquido);
                    document_nomina.add(neto);

                    Table vacio = new Table(1);
                    com.itextpdf.layout.element.Cell celda_aux = new com.itextpdf.layout.element.Cell();
                    celda_aux.setBorder(Border.NO_BORDER);
                    celda_aux.add(new Paragraph());
                    vacio.addCell(celda_aux);
                    document_nomina.add(vacio);

                    //Empresario
                    estilo.setFontSize(12);
                    centrarTexto.add(new TabStop((pdfdocument.getDefaultPageSize().getWidth() - document_nomina.getLeftMargin() - document_nomina.getRightMargin()) / 2, TabAlignment.CENTER));
                    Paragraph costes_emp = new Paragraph().addTabStops(centrarTexto);
                    costes_emp.add(new Tab());
                    costes_emp.add("Costes asociados al empresario").addStyle(estilo);
                    costes_emp.add(new Tab());
                    document_nomina.add(costes_emp);

                    Table base_emp = new Table(2);
                    SolidBorder borde = new SolidBorder(1);
                    borde.setColor(ColorConstants.GRAY);
                    base_emp.setBorderTop(borde);

                    com.itextpdf.layout.element.Cell base_emp_titulo = new com.itextpdf.layout.element.Cell();
                    base_emp_titulo.setBorder(Border.NO_BORDER);
                    base_emp_titulo.setTextAlignment(TextAlignment.LEFT);
                    base_emp_titulo.setWidth(700);
                    base_emp_titulo.add(new Paragraph("Calculo Empresario: Base"));
                    base_emp.addCell(base_emp_titulo);

                    com.itextpdf.layout.element.Cell base_emp_costebase = new com.itextpdf.layout.element.Cell();
                    base_emp_costebase.setBorder(Border.NO_BORDER);
                    base_emp_costebase.add(new Paragraph("" + bruto_verdad));
                    base_emp.addCell(base_emp_costebase);

                    base_emp.setBorderBottom(borde);
                    document_nomina.add(base_emp);

                    //Contingencias comunes empresario
                    Table cont_comunes_emp = new Table(2);

                    com.itextpdf.layout.element.Cell cont_comunes_titulo = new com.itextpdf.layout.element.Cell();
                    cont_comunes_titulo.setBorder(Border.NO_BORDER);
                    cont_comunes_titulo.setTextAlignment(TextAlignment.LEFT);
                    cont_comunes_titulo.setWidth(700);
                    Double p_cont_emp = redondear((trabajadores.get(k).getContingencias_comunes() * 100) / trabajadores.get(k).getBruto());
                    cont_comunes_titulo.add(new Paragraph("Contingencias comunes " + p_cont_emp + "%"));
                    cont_comunes_emp.addCell(cont_comunes_titulo);

                    com.itextpdf.layout.element.Cell cont_comunes_coste = new com.itextpdf.layout.element.Cell();
                    cont_comunes_coste.setBorder(Border.NO_BORDER);

                    cont_comunes_coste.add(new Paragraph("" + redondear(p_cont_emp * bruto_verdad / 100)));

                    cont_comunes_emp.addCell(cont_comunes_coste);

                    document_nomina.add(cont_comunes_emp);

                    //Desempleo
                    Table desempleo_emp = new Table(2);

                    com.itextpdf.layout.element.Cell desempleo_titulo = new com.itextpdf.layout.element.Cell();
                    desempleo_titulo.setBorder(Border.NO_BORDER);
                    desempleo_titulo.setTextAlignment(TextAlignment.LEFT);
                    desempleo_titulo.setWidth(700);
                    Double p_des_emp = (double) Math.round((trabajadores.get(k).getDesempleo_emp() * 100) / trabajadores.get(k).getBruto() * 100) / 100;
                    desempleo_titulo.add(new Paragraph("Desempleo " + p_des_emp + "%"));
                    desempleo_emp.addCell(desempleo_titulo);

                    com.itextpdf.layout.element.Cell desempleo_coste = new com.itextpdf.layout.element.Cell();
                    desempleo_coste.setBorder(Border.NO_BORDER);
                    desempleo_coste.add(new Paragraph("" + redondear(p_des_emp * bruto_verdad / 100)));

                    desempleo_emp.addCell(desempleo_coste);

                    document_nomina.add(desempleo_emp);

                    //Formacion
                    Table formacion_emp = new Table(2);

                    com.itextpdf.layout.element.Cell formacion_titulo = new com.itextpdf.layout.element.Cell();
                    formacion_titulo.setBorder(Border.NO_BORDER);
                    formacion_titulo.setTextAlignment(TextAlignment.LEFT);
                    formacion_titulo.setWidth(700);
                    Double p_for_emp = (double) Math.round((trabajadores.get(k).getFormacion_emp() * 100) / trabajadores.get(k).getBruto() * 100) / 100;
                    formacion_titulo.add(new Paragraph("Formacion " + p_for_emp + "%"));
                    formacion_emp.addCell(formacion_titulo);

                    com.itextpdf.layout.element.Cell formacion_coste = new com.itextpdf.layout.element.Cell();
                    formacion_coste.setBorder(Border.NO_BORDER);
                    formacion_coste.add(new Paragraph("" + redondear(p_for_emp * bruto_verdad / 100)));

                    formacion_emp.addCell(formacion_coste);

                    document_nomina.add(formacion_emp);

                    //Accidentes
                    Table accidentes_emp = new Table(2);

                    com.itextpdf.layout.element.Cell accidentes_titulo = new com.itextpdf.layout.element.Cell();
                    accidentes_titulo.setBorder(Border.NO_BORDER);
                    accidentes_titulo.setTextAlignment(TextAlignment.LEFT);
                    accidentes_titulo.setWidth(700);
                    Double p_acc = (double) Math.round((trabajadores.get(k).getAccidentes() * 100) / trabajadores.get(k).getBruto() * 100) / 100;
                    accidentes_titulo.add(new Paragraph("Accidentes " + p_acc + "%"));
                    accidentes_emp.addCell(accidentes_titulo);

                    com.itextpdf.layout.element.Cell accidentes_coste = new com.itextpdf.layout.element.Cell();
                    accidentes_coste.setBorder(Border.NO_BORDER);
                    accidentes_coste.add(new Paragraph("" + redondear(p_acc * bruto_verdad / 100)));

                    accidentes_emp.addCell(accidentes_coste);

                    document_nomina.add(accidentes_emp);

                    //FOGASA
                    Table fogasa = new Table(2);

                    com.itextpdf.layout.element.Cell fogasa_titulo = new com.itextpdf.layout.element.Cell();
                    fogasa_titulo.setBorder(Border.NO_BORDER);
                    fogasa_titulo.setTextAlignment(TextAlignment.LEFT);
                    fogasa_titulo.setWidth(700);
                    Double p_fog = (double) Math.round((trabajadores.get(k).getFogasa() * 100) / trabajadores.get(k).getBruto() * 100) / 100;
                    fogasa_titulo.add(new Paragraph("FOGASA " + p_fog + "%"));
                    fogasa.addCell(fogasa_titulo);

                    com.itextpdf.layout.element.Cell fogasa_coste = new com.itextpdf.layout.element.Cell();
                    fogasa_coste.setBorder(Border.NO_BORDER);
                    fogasa_coste.add(new Paragraph("" + redondear(p_fog * bruto_verdad / 100)));

                    fogasa.addCell(fogasa_coste);

                    document_nomina.add(fogasa);

                    //TOTAL EMPRESARIO
                    Table total_emp = new Table(2);
                    total_emp.setBorderTop(borde);
                    com.itextpdf.layout.element.Cell total_emp_titulo = new com.itextpdf.layout.element.Cell();
                    total_emp_titulo.setBorder(Border.NO_BORDER);
                    total_emp_titulo.setTextAlignment(TextAlignment.LEFT);
                    total_emp_titulo.setWidth(700);
                    total_emp_titulo.add(new Paragraph("Total Empresario"));
                    total_emp.addCell(total_emp_titulo);

                    com.itextpdf.layout.element.Cell total_emp_coste = new com.itextpdf.layout.element.Cell();
                    total_emp_coste.setBorder(Border.NO_BORDER);
                    Double t_empresario;
                    t_empresario = redondear((p_fog * bruto_verdad / 100) + (p_cont_emp * bruto_verdad / 100) + (p_des_emp * bruto_verdad / 100) + (p_for_emp * bruto_verdad / 100) + (p_acc * bruto_verdad / 100));

                    total_emp_coste.add(new Paragraph("" + t_empresario));
                    total_emp.addCell(total_emp_coste);
                    total_emp.setMarginBottom(50);
                    document_nomina.add(total_emp);
                    //Coste total trabajador
                    Table coste_total = new Table(2);
                    Style estilo2 = new Style();
                    estilo2.setFontColor(ColorConstants.RED);
                    coste_total.addStyle(estilo2);
                    coste_total.setBorderTop(new SolidBorder(2));

                    com.itextpdf.layout.element.Cell coste_t_titulo = new com.itextpdf.layout.element.Cell();
                    coste_t_titulo.setBorder(Border.NO_BORDER);
                    coste_t_titulo.setTextAlignment(TextAlignment.LEFT);
                    coste_t_titulo.setWidth(700);
                    coste_t_titulo.add(new Paragraph("Coste Total Trabajador"));
                    coste_total.addCell(coste_t_titulo);

                    com.itextpdf.layout.element.Cell coste_t_ = new com.itextpdf.layout.element.Cell();
                    coste_t_.setBorder(Border.NO_BORDER);
                    coste_t_.setTextAlignment(TextAlignment.CENTER);
                    if (trabajadores.get(k).isProrateo()) {

                        coste_t_.add(new Paragraph("" + redondear(bruto_verdad + t_empresario)));
                    } else {

                        coste_t_.add(new Paragraph("" + redondear(trabajadores.get(k).getBruto() / 14 + t_empresario)));
                    }
                    coste_total.addCell(coste_t_);

                    document_nomina.add(coste_total);
                    document_nomina.close();

                    pdfwriter.close();
                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                System.out.println("Tipo de nómina: Normal.");
                System.out.println("Nombre: " + trabajadores.get(k).getNombre() + " " + trabajadores.get(k).getApellidos());
                System.out.println("DNI: " + trabajadores.get(k).getDni());
                System.out.println("Categoria: " + trabajadores.get(k).getCategoria_empleado());
                System.out.println("IBAN: " + trabajadores.get(k).getIBAN());
                System.out.println("Fecha de alta: " + trabajadores.get(k).getFecha_alta());
                System.out.println("Empresa: " + trabajadores.get(k).getEmpresa() + " (CIF: " + trabajadores.get(k).getCif() + ")");
                System.out.println("Salario base: " + (trabajadores.get(k).getSalarioBase() / 14));
                System.out.println("Complementos: " + (trabajadores.get(k).getComplementos() / 14));
                System.out.println("Complementos Trienio: " + (trabajadores.get(k).getComplemento_trienios() / 14));
                if (trabajadores.get(k).isProrateo()) {
                    System.out.println("Salario bruto (Prorrateado): " + (trabajadores.get(k).getBruto() / 12) + (trabajadores.get(k).getBruto() / 72));
                } else {
                    System.out.println("Salario bruto: " + (trabajadores.get(k).getBruto() / 12));
                }

                System.out.println("IRPF: " + (trabajadores.get(k).getIRPF() / 14) + " (" + (double) Math.round((trabajadores.get(k).getIRPF() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "% del salario bruto)");

                System.out.println("Seguridad Social: " + (trabajadores.get(k).getCuota_obrera() / 12) + " (" + (double) Math.round((trabajadores.get(k).getCuota_obrera() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "% del salario bruto)");
                System.out.println("Formación: " + (trabajadores.get(k).getFormacion_trabajador() / 12) + " (" + (double) Math.round((trabajadores.get(k).getFormacion_trabajador() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "% del salario bruto)");
                System.out.println("Desempleo: " + (trabajadores.get(k).getDesempleo_trabajador() / 12) + " (" + (double) Math.round((trabajadores.get(k).getDesempleo_trabajador() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "% del salario bruto)");
                if (trabajadores.get(k).isProrateo()) {
                    System.out.println("Salario Neto(Prorrateado): " + ((trabajadores.get(k).getNeto() / 12) + (trabajadores.get(k).getNeto() / 72)));
                } else {
                    System.out.println("Salario Neto: " + ((trabajadores.get(k).getNeto() / 12)));
                }
                System.out.println("_______________________");

                if (trabajadores.get(k).isExtra() && (mes.equals("06") || mes.equals("12"))) {

                    try {
                        //RUTA NOMINA EXTRA
                        File nomina_extra = new File("C:\\Users\\davga\\OneDrive\\Escritorio\\aaa\\" + trabajadores.get(k).getDni() + trabajadores.get(k).getNombre() + trabajadores.get(k).getApellidos() + mes_texto + año + "EXTRA.pdf");
                        PdfWriter pdfwriter_extra = new PdfWriter(nomina_extra);
                        PdfDocument pdfdocument_extra = new PdfDocument(pdfwriter_extra);
                        Document document_extra = new com.itextpdf.layout.Document(pdfdocument_extra);

                        Paragraph nombre_normal = new Paragraph(trabajadores.get(k).getNombre() + " " + trabajadores.get(k).getApellidos());
                        document_extra.add(nombre_normal);
                        document_extra.add(new Paragraph("DNI: " + trabajadores.get(k).getDni()));
                        document_extra.add(new Paragraph("Categoria: " + trabajadores.get(k).getCategoria_empleado()));
                        document_extra.add(new Paragraph("IBAN: " + trabajadores.get(k).getIBAN()));
                        if (trabajadores.get(k).getAño() == 0) {
                            document_extra.add(new Paragraph("Bruto Anual: " + redondear(trabajadores.get(k).getBruto() / 12 * (12 - trabajadores.get(k).getMes() + 1))));
                            brutos_extra.add(trabajadores.get(k).getBruto() / 12 * (12 - trabajadores.get(k).getMes() + 1));
                        } else {
                            document_extra.add(new Paragraph("Bruto Anual: " + redondear(trabajadores.get(k).getBruto())));
                            brutos_extra.add(trabajadores.get(k).getBruto());
                        }
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String today = formatter.format(trabajadores.get(k).getFecha_alta());

                        document_extra.add(new Paragraph("Fecha de alta: " + today));
                        document_extra.add(new Paragraph("Empresa: " + trabajadores.get(k).getEmpresa() + " (CIF: " + trabajadores.get(k).getCif() + ")"));

                        //titulo de nomina
                        Style estilo = new Style().setBold();
                        estilo.setFontSize(12);
                        ArrayList<TabStop> centrarTexto = new ArrayList<>();
                        centrarTexto.add(new TabStop((pdfdocument_extra.getDefaultPageSize().getWidth() - document_extra.getLeftMargin() - document_extra.getRightMargin()) / 2, TabAlignment.CENTER));
                        Paragraph p = new Paragraph().addTabStops(centrarTexto);
                        p.add(new Tab());
                        p.add("Nomina EXTRA: " + mes_texto + " " + año).addStyle(estilo);
                        p.add(new Tab());
                        p.setMarginTop(10);
                        document_extra.add(p);

                        /**
                         *
                         * CONCEPTOS DEL TRABAJADOR
                         *
                         */
                        Table tabla_trabajador = new Table(5);

                        tabla_trabajador.setMarginTop(5);
                        tabla_trabajador.setWidth(520);
                        tabla_trabajador.setBorderTop(new SolidBorder(1));
                        tabla_trabajador.setBorderBottom(new SolidBorder(1));
                        //CELDA CONCEPTOS
                        com.itextpdf.layout.element.Cell celda_conceptos = new com.itextpdf.layout.element.Cell();
                        celda_conceptos.setTextAlignment(TextAlignment.LEFT);
                        celda_conceptos.setWidth(250);
                        celda_conceptos.setBorder(Border.NO_BORDER);
                        celda_conceptos.add(new Paragraph("Conceptos"));
                        tabla_trabajador.addCell(celda_conceptos);
                        //CELDA CANTIDAD
                        com.itextpdf.layout.element.Cell celda_cantidad = new com.itextpdf.layout.element.Cell();
                        celda_cantidad.setTextAlignment(TextAlignment.CENTER);
                        celda_cantidad.setWidth(130);
                        celda_cantidad.setBorder(Border.NO_BORDER);
                        celda_cantidad.add(new Paragraph("Cantidad"));
                        tabla_trabajador.addCell(celda_cantidad);

                        //CELDA IMP.UNITARIO
                        com.itextpdf.layout.element.Cell celda_impunitario = new com.itextpdf.layout.element.Cell();
                        celda_impunitario.setTextAlignment(TextAlignment.CENTER);
                        celda_impunitario.setWidth(130);
                        celda_impunitario.setBorder(Border.NO_BORDER);
                        celda_impunitario.add(new Paragraph("Imp.Unitario"));
                        tabla_trabajador.addCell(celda_impunitario);

                        //CELDA DEVENGO
                        com.itextpdf.layout.element.Cell celda_devengos = new com.itextpdf.layout.element.Cell();
                        celda_devengos.setTextAlignment(TextAlignment.CENTER);
                        celda_devengos.setWidth(130);
                        celda_devengos.setBorder(Border.NO_BORDER);
                        celda_devengos.add(new Paragraph("Devengo"));
                        tabla_trabajador.addCell(celda_devengos);

                        //CELDA DEDUCCION
                        com.itextpdf.layout.element.Cell celda_deduccion = new com.itextpdf.layout.element.Cell();
                        celda_deduccion.setTextAlignment(TextAlignment.CENTER);
                        celda_deduccion.setWidth(130);
                        celda_deduccion.setBorder(Border.NO_BORDER);
                        celda_deduccion.add(new Paragraph("Deduccion"));
                        tabla_trabajador.addCell(celda_deduccion);

                        document_extra.add(tabla_trabajador);

                        //SALARIO BASE
                        Table salario_base = new Table(5);
                        com.itextpdf.layout.element.Cell celda_titulo_salariobase = new com.itextpdf.layout.element.Cell();
                        celda_titulo_salariobase.setTextAlignment(TextAlignment.LEFT);
                        celda_titulo_salariobase.setWidth(130);
                        celda_titulo_salariobase.setBorder(Border.NO_BORDER);
                        celda_titulo_salariobase.add(new Paragraph("Salario Base"));
                        salario_base.addCell(celda_titulo_salariobase);

                        com.itextpdf.layout.element.Cell celda_cantidad_salariobase = new com.itextpdf.layout.element.Cell();
                        celda_cantidad_salariobase.setTextAlignment(TextAlignment.CENTER);
                        celda_cantidad_salariobase.setWidth(100);
                        celda_cantidad_salariobase.setBorder(Border.NO_BORDER);
                        celda_cantidad_salariobase.add(new Paragraph("30 dias"));
                        salario_base.addCell(celda_cantidad_salariobase);

                        com.itextpdf.layout.element.Cell celda_unitario_salariobase = new com.itextpdf.layout.element.Cell();
                        celda_unitario_salariobase.setTextAlignment(TextAlignment.CENTER);
                        celda_unitario_salariobase.setWidth(100);
                        celda_unitario_salariobase.setBorder(Border.NO_BORDER);
                        Double base = trabajadores.get(k).getSalarioBase();
                        if (trabajadores.get(k).getAño() == 0) {
                            if (mes.equals("12") && (trabajadores.get(k).getMes()) > 6) {
                                base = trabajadores.get(k).getSalarioBase() * (12 - trabajadores.get(k).getMes()) / 6;
                            } else if (mes.equals("6") && (12 - trabajadores.get(k).getMes()) > 0) {
                                base = trabajadores.get(k).getSalarioBase() * trabajadores.get(k).getMes() / 6;
                            }
                        }
                        celda_unitario_salariobase.add(new Paragraph("" + redondear(base / 14 / 30)));
                        salario_base.addCell(celda_unitario_salariobase);

                        com.itextpdf.layout.element.Cell celda_salario_salariobase = new com.itextpdf.layout.element.Cell();
                        celda_salario_salariobase.setTextAlignment(TextAlignment.CENTER);
                        celda_salario_salariobase.setWidth(100);
                        celda_salario_salariobase.setBorder(Border.NO_BORDER);
                        celda_salario_salariobase.add(new Paragraph("" + redondear(base / 14)));
                        salario_base.addCell(celda_salario_salariobase);

                        salario_base.addCell(celda_salario_salariobase);
                        document_extra.add(salario_base);

                        //Prorrateo
                        Table prorrateo = new Table(5);
                        com.itextpdf.layout.element.Cell celda_titulo_prorrateo = new com.itextpdf.layout.element.Cell();
                        celda_titulo_prorrateo.setTextAlignment(TextAlignment.LEFT);
                        celda_titulo_prorrateo.setWidth(130);
                        celda_titulo_prorrateo.setBorder(Border.NO_BORDER);
                        celda_titulo_prorrateo.add(new Paragraph("Prorrateo"));
                        prorrateo.addCell(celda_titulo_prorrateo);

                        com.itextpdf.layout.element.Cell celda_cantidad_prorrateo = new com.itextpdf.layout.element.Cell();
                        celda_cantidad_prorrateo.setTextAlignment(TextAlignment.CENTER);
                        celda_cantidad_prorrateo.setWidth(100);
                        celda_cantidad_prorrateo.setBorder(Border.NO_BORDER);
                        celda_cantidad_prorrateo.add(new Paragraph("30 dias"));
                        prorrateo.addCell(celda_cantidad_prorrateo);

                        com.itextpdf.layout.element.Cell celda_unitario_prorrateo = new com.itextpdf.layout.element.Cell();
                        celda_unitario_prorrateo.setTextAlignment(TextAlignment.CENTER);
                        celda_unitario_prorrateo.setWidth(100);
                        celda_unitario_prorrateo.setBorder(Border.NO_BORDER);
                        celda_unitario_prorrateo.add(new Paragraph("0.00"));

                        prorrateo.addCell(celda_unitario_prorrateo);

                        com.itextpdf.layout.element.Cell celda_salario_prorrateo = new com.itextpdf.layout.element.Cell();
                        celda_salario_prorrateo.setTextAlignment(TextAlignment.CENTER);
                        celda_salario_prorrateo.setWidth(100);
                        celda_salario_prorrateo.setBorder(Border.NO_BORDER);
                        celda_salario_prorrateo.add(new Paragraph("0.00"));

                        prorrateo.addCell(celda_salario_prorrateo);

                        prorrateo.addCell(celda_salario_prorrateo);
                        document_extra.add(prorrateo);

                        //Complementos
                        Table complementos = new Table(5);
                        com.itextpdf.layout.element.Cell celda_titulo_complementos = new com.itextpdf.layout.element.Cell();
                        celda_titulo_complementos.setTextAlignment(TextAlignment.LEFT);
                        celda_titulo_complementos.setWidth(130);
                        celda_titulo_complementos.setBorder(Border.NO_BORDER);
                        celda_titulo_complementos.add(new Paragraph("Complementos"));
                        complementos.addCell(celda_titulo_complementos);

                        com.itextpdf.layout.element.Cell celda_cantidad_complementos = new com.itextpdf.layout.element.Cell();
                        celda_cantidad_complementos.setTextAlignment(TextAlignment.CENTER);
                        celda_cantidad_complementos.setWidth(100);
                        celda_cantidad_complementos.setBorder(Border.NO_BORDER);
                        celda_cantidad_complementos.add(new Paragraph("30 dias"));
                        complementos.addCell(celda_cantidad_complementos);

                        com.itextpdf.layout.element.Cell celda_unitario_complementos = new com.itextpdf.layout.element.Cell();
                        celda_unitario_complementos.setTextAlignment(TextAlignment.CENTER);
                        celda_unitario_complementos.setWidth(100);
                        celda_unitario_complementos.setBorder(Border.NO_BORDER);
                        Double comp = trabajadores.get(k).getComplementos();
                        if (trabajadores.get(k).getAño() == 0) {
                            if (mes.equals("12") && (trabajadores.get(k).getMes()) > 6) {
                                comp = trabajadores.get(k).getComplementos() * (12 - trabajadores.get(k).getMes()) / 6;
                            } else if (mes.equals("6") && (12 - trabajadores.get(k).getMes()) > 0) {
                                comp = trabajadores.get(k).getComplementos() * trabajadores.get(k).getMes() / 6;
                            }
                        }
                        celda_unitario_complementos.add(new Paragraph("" + redondear(comp / 14 / 30)));
                        complementos.addCell(celda_unitario_complementos);

                        com.itextpdf.layout.element.Cell celda_salario_complementos = new com.itextpdf.layout.element.Cell();
                        celda_salario_complementos.setTextAlignment(TextAlignment.CENTER);
                        celda_salario_complementos.setWidth(100);
                        celda_salario_complementos.setBorder(Border.NO_BORDER);
                        celda_salario_complementos.add(new Paragraph("" + redondear(comp / 14)));
                        complementos.addCell(celda_salario_complementos);

                        complementos.addCell(celda_salario_complementos);
                        document_extra.add(complementos);

                        //Trienios
                        Table trienios = new Table(5);
                        com.itextpdf.layout.element.Cell celda_titulo_trienios = new com.itextpdf.layout.element.Cell();
                        celda_titulo_trienios.setTextAlignment(TextAlignment.LEFT);
                        celda_titulo_trienios.setWidth(130);
                        celda_titulo_trienios.setBorder(Border.NO_BORDER);
                        celda_titulo_trienios.add(new Paragraph("Antigüedad"));
                        trienios.addCell(celda_titulo_trienios);

                        com.itextpdf.layout.element.Cell celda_cantidad_trienios = new com.itextpdf.layout.element.Cell();
                        celda_cantidad_trienios.setTextAlignment(TextAlignment.CENTER);
                        celda_cantidad_trienios.setWidth(100);
                        celda_cantidad_trienios.setBorder(Border.NO_BORDER);
                        celda_cantidad_trienios.add(new Paragraph(trabajadores.get(k).getTrienio() + " trienio/s"));
                        trienios.addCell(celda_cantidad_trienios);

                        com.itextpdf.layout.element.Cell celda_unitario_trienios = new com.itextpdf.layout.element.Cell();
                        celda_unitario_trienios.setTextAlignment(TextAlignment.CENTER);
                        celda_unitario_trienios.setWidth(100);
                        celda_unitario_trienios.setBorder(Border.NO_BORDER);
                        if (trabajadores.get(k).getTrienio() == 0) {
                            celda_unitario_trienios.add(new Paragraph(""));
                        } else {
                            celda_unitario_trienios.add(new Paragraph("" + redondear(trabajadores.get(k).getComplemento_trienios() / 14 / trabajadores.get(k).getTrienio())));
                        }

                        trienios.addCell(celda_unitario_trienios);

                        com.itextpdf.layout.element.Cell celda_salario_trienios = new com.itextpdf.layout.element.Cell();
                        celda_salario_trienios.setTextAlignment(TextAlignment.CENTER);
                        celda_salario_trienios.setWidth(100);
                        celda_salario_trienios.setBorder(Border.NO_BORDER);
                        if (trabajadores.get(k).getTrienio() == 0) {
                            celda_salario_trienios.add(new Paragraph(""));
                        } else {
                            celda_salario_trienios.add(new Paragraph("" + redondear(trabajadores.get(k).getComplemento_trienios() / 14)));
                        }

                        trienios.addCell(celda_salario_trienios);

                        trienios.addCell(celda_salario_trienios);
                        document_extra.add(trienios);

                        //Contingencias generales
                        Table contingencias_generales = new Table(5);
                        com.itextpdf.layout.element.Cell celda_titulo_contingenciasgenerales = new com.itextpdf.layout.element.Cell();
                        celda_titulo_contingenciasgenerales.setTextAlignment(TextAlignment.LEFT);
                        celda_titulo_contingenciasgenerales.setWidth(175);
                        celda_titulo_contingenciasgenerales.setBorder(Border.NO_BORDER);
                        celda_titulo_contingenciasgenerales.add(new Paragraph("Cont. Generales"));
                        contingencias_generales.addCell(celda_titulo_contingenciasgenerales);

                        com.itextpdf.layout.element.Cell celda_cantidad_contingenciasgenerales = new com.itextpdf.layout.element.Cell();
                        celda_cantidad_contingenciasgenerales.setTextAlignment(TextAlignment.CENTER);
                        celda_cantidad_contingenciasgenerales.setWidth(130);
                        celda_cantidad_contingenciasgenerales.setBorder(Border.NO_BORDER);
                        celda_cantidad_contingenciasgenerales.add(new Paragraph("0.00 % de " + redondear(trabajadores.get(k).getBruto() / 12)));
                        contingencias_generales.addCell(celda_cantidad_contingenciasgenerales);

                        com.itextpdf.layout.element.Cell celda_v_contingenciasgenerales = new com.itextpdf.layout.element.Cell();
                        celda_v_contingenciasgenerales.setBorder(Border.NO_BORDER);
                        celda_v_contingenciasgenerales.setWidth(50);
                        celda_v_contingenciasgenerales.add(new Paragraph());
                        contingencias_generales.addCell(celda_v_contingenciasgenerales);

                        com.itextpdf.layout.element.Cell celda_v_contingenciasgenerales2 = new com.itextpdf.layout.element.Cell();
                        celda_v_contingenciasgenerales2.setBorder(Border.NO_BORDER);
                        celda_v_contingenciasgenerales2.setWidth(150);
                        celda_v_contingenciasgenerales2.add(new Paragraph());
                        contingencias_generales.addCell(celda_v_contingenciasgenerales2);

                        com.itextpdf.layout.element.Cell celda_precio_contingenciasgenerales = new com.itextpdf.layout.element.Cell();
                        celda_precio_contingenciasgenerales.setTextAlignment(TextAlignment.CENTER);
                        celda_precio_contingenciasgenerales.setWidth(130);
                        celda_precio_contingenciasgenerales.setBorder(Border.NO_BORDER);
                        celda_precio_contingenciasgenerales.add(new Paragraph("0.00"));
                        contingencias_generales.addCell(celda_precio_contingenciasgenerales);
                        document_extra.add(contingencias_generales);

                        //Desempleo
                        Table desempleo = new Table(5);
                        com.itextpdf.layout.element.Cell celda_titulo_desempleo = new com.itextpdf.layout.element.Cell();
                        celda_titulo_desempleo.setTextAlignment(TextAlignment.LEFT);
                        celda_titulo_desempleo.setWidth(175);
                        celda_titulo_desempleo.setBorder(Border.NO_BORDER);
                        celda_titulo_desempleo.add(new Paragraph("Desempleo"));
                        desempleo.addCell(celda_titulo_desempleo);

                        com.itextpdf.layout.element.Cell celda_cantidad_desempleo = new com.itextpdf.layout.element.Cell();
                        celda_cantidad_desempleo.setTextAlignment(TextAlignment.CENTER);
                        celda_cantidad_desempleo.setWidth(130);
                        celda_cantidad_desempleo.setBorder(Border.NO_BORDER);
                        celda_cantidad_desempleo.add(new Paragraph("0.00 % de " + redondear(trabajadores.get(k).getBruto() / 12)));
                        desempleo.addCell(celda_cantidad_desempleo);

                        com.itextpdf.layout.element.Cell celda_v_desempleo = new com.itextpdf.layout.element.Cell();
                        celda_v_desempleo.setBorder(Border.NO_BORDER);
                        celda_v_desempleo.setWidth(50);
                        celda_v_desempleo.add(new Paragraph());
                        desempleo.addCell(celda_v_desempleo);

                        com.itextpdf.layout.element.Cell celda_v_desempleo2 = new com.itextpdf.layout.element.Cell();
                        celda_v_desempleo2.setBorder(Border.NO_BORDER);
                        celda_v_desempleo2.setWidth(150);
                        celda_v_desempleo2.add(new Paragraph());
                        desempleo.addCell(celda_v_desempleo2);

                        com.itextpdf.layout.element.Cell celda_precio_desempleo = new com.itextpdf.layout.element.Cell();
                        celda_precio_desempleo.setTextAlignment(TextAlignment.CENTER);
                        celda_precio_desempleo.setWidth(130);
                        celda_precio_desempleo.setBorder(Border.NO_BORDER);
                        celda_precio_desempleo.add(new Paragraph("0.00"));
                        desempleo.addCell(celda_precio_desempleo);
                        document_extra.add(desempleo);

                        //Formacion
                        Table formacion = new Table(5);
                        com.itextpdf.layout.element.Cell celda_titulo_formacion = new com.itextpdf.layout.element.Cell();
                        celda_titulo_formacion.setTextAlignment(TextAlignment.LEFT);
                        celda_titulo_formacion.setWidth(175);
                        celda_titulo_formacion.setBorder(Border.NO_BORDER);
                        celda_titulo_formacion.add(new Paragraph("Cuota Formacion"));
                        formacion.addCell(celda_titulo_formacion);

                        com.itextpdf.layout.element.Cell celda_cantidad_formacion = new com.itextpdf.layout.element.Cell();
                        celda_cantidad_formacion.setTextAlignment(TextAlignment.CENTER);
                        celda_cantidad_formacion.setWidth(130);
                        celda_cantidad_formacion.setBorder(Border.NO_BORDER);
                        celda_cantidad_formacion.add(new Paragraph("0.00 % de " + redondear(trabajadores.get(k).getBruto() / 12)));
                        formacion.addCell(celda_cantidad_formacion);

                        com.itextpdf.layout.element.Cell celda_v_formacion = new com.itextpdf.layout.element.Cell();
                        celda_v_formacion.setBorder(Border.NO_BORDER);
                        celda_v_formacion.setWidth(50);
                        celda_v_formacion.add(new Paragraph());
                        formacion.addCell(celda_v_formacion);

                        com.itextpdf.layout.element.Cell celda_v_formacion2 = new com.itextpdf.layout.element.Cell();
                        celda_v_formacion2.setBorder(Border.NO_BORDER);
                        celda_v_formacion2.setWidth(150);
                        celda_v_formacion2.add(new Paragraph());
                        formacion.addCell(celda_v_formacion2);

                        com.itextpdf.layout.element.Cell celda_precio_formacion = new com.itextpdf.layout.element.Cell();
                        celda_precio_formacion.setTextAlignment(TextAlignment.CENTER);
                        celda_precio_formacion.setWidth(130);
                        celda_precio_formacion.setBorder(Border.NO_BORDER);
                        celda_precio_formacion.add(new Paragraph("0.00"));
                        formacion.addCell(celda_precio_formacion);
                        document_extra.add(formacion);

                        //IRPF
                        Table IRPF = new Table(5);
                        com.itextpdf.layout.element.Cell celda_titulo_irpf = new com.itextpdf.layout.element.Cell();
                        celda_titulo_irpf.setTextAlignment(TextAlignment.LEFT);
                        celda_titulo_irpf.setWidth(170);
                        celda_titulo_irpf.setBorder(Border.NO_BORDER);
                        celda_titulo_irpf.add(new Paragraph("IRPF"));
                        IRPF.addCell(celda_titulo_irpf);

                        com.itextpdf.layout.element.Cell celda_cantidad_irpf = new com.itextpdf.layout.element.Cell();
                        celda_cantidad_irpf.setTextAlignment(TextAlignment.CENTER);
                        celda_cantidad_irpf.setWidth(130);
                        celda_cantidad_irpf.setBorder(Border.NO_BORDER);
                        if (trabajadores.get(k).getAño() == 0) {
                            celda_cantidad_irpf.add(new Paragraph("0.00 % de " + redondear(trabajadores.get(k).getBruto() / 14)));
                        } else {
                            celda_cantidad_irpf.add(new Paragraph("" + (double) Math.round((trabajadores.get(k).getIRPF() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + " % de " + redondear(trabajadores.get(k).getBruto() / 14)));
                        }

                        IRPF.addCell(celda_cantidad_irpf);

                        com.itextpdf.layout.element.Cell celda_v_irpf = new com.itextpdf.layout.element.Cell();
                        celda_v_irpf.setBorder(Border.NO_BORDER);
                        celda_v_irpf.setWidth(50);
                        celda_v_irpf.add(new Paragraph());
                        IRPF.addCell(celda_v_irpf);

                        com.itextpdf.layout.element.Cell celda_v_irpf2 = new com.itextpdf.layout.element.Cell();
                        celda_v_irpf2.setBorder(Border.NO_BORDER);
                        celda_v_irpf2.setWidth(150);
                        celda_v_irpf2.add(new Paragraph());
                        IRPF.addCell(celda_v_irpf2);

                        com.itextpdf.layout.element.Cell celda_precio_irpf = new com.itextpdf.layout.element.Cell();
                        celda_precio_irpf.setTextAlignment(TextAlignment.CENTER);
                        celda_precio_irpf.setWidth(130);
                        celda_precio_irpf.setBorder(Border.NO_BORDER);
                        celda_precio_irpf.add(new Paragraph("0.00"));
                        IRPF.addCell(celda_precio_irpf);
                        document_extra.add(IRPF);

                        //Total deducciones
                        Table total_deducciones = new Table(5);
                        total_deducciones.setBorderTop(new SolidBorder(1));
                        com.itextpdf.layout.element.Cell celda_titulo_totaldeducciones = new com.itextpdf.layout.element.Cell();
                        celda_titulo_totaldeducciones.setTextAlignment(TextAlignment.LEFT);
                        celda_titulo_totaldeducciones.setWidth(190);
                        celda_titulo_totaldeducciones.setBorder(Border.NO_BORDER);
                        celda_titulo_totaldeducciones.add(new Paragraph("Total Deducciones"));
                        total_deducciones.addCell(celda_titulo_totaldeducciones);

                        com.itextpdf.layout.element.Cell celda_v_totaldeducciones = new com.itextpdf.layout.element.Cell();
                        celda_v_totaldeducciones.setBorder(Border.NO_BORDER);
                        celda_v_totaldeducciones.setWidth(150);
                        celda_v_totaldeducciones.add(new Paragraph());
                        total_deducciones.addCell(celda_v_totaldeducciones);

                        com.itextpdf.layout.element.Cell celda_v_totaldeducciones2 = new com.itextpdf.layout.element.Cell();
                        celda_v_totaldeducciones2.setBorder(Border.NO_BORDER);
                        celda_v_totaldeducciones2.setWidth(150);
                        celda_v_totaldeducciones2.add(new Paragraph());
                        total_deducciones.addCell(celda_v_totaldeducciones2);

                        com.itextpdf.layout.element.Cell celda_v_totaldeducciones3 = new com.itextpdf.layout.element.Cell();
                        celda_v_totaldeducciones3.setBorder(Border.NO_BORDER);
                        celda_v_totaldeducciones3.setWidth(250);
                        celda_v_totaldeducciones3.add(new Paragraph());
                        total_deducciones.addCell(celda_v_totaldeducciones3);

                        com.itextpdf.layout.element.Cell celda_total_deducciones = new com.itextpdf.layout.element.Cell();
                        celda_v_totaldeducciones.setBorder(Border.NO_BORDER);
                        celda_v_totaldeducciones.setWidth(90);
                        Double extra_deducciones;
                        if (trabajadores.get(k).getAño() == 0) {
                            extra_deducciones = 0.0;
                        } else {
                            extra_deducciones = redondear(trabajadores.get(k).getIRPF() / 14);
                        }

                        celda_v_totaldeducciones.add(new Paragraph("" + extra_deducciones));
                        total_deducciones.addCell(celda_v_totaldeducciones);
                        document_extra.add(total_deducciones);

                        //Total devengos
                        Table total_devengos = new Table(5);
                        total_devengos.setBorderBottom(new SolidBorder(1));
                        com.itextpdf.layout.element.Cell celda_titulo_totaldevengos = new com.itextpdf.layout.element.Cell();
                        celda_titulo_totaldevengos.setTextAlignment(TextAlignment.LEFT);
                        celda_titulo_totaldevengos.setWidth(190);
                        celda_titulo_totaldevengos.setBorder(Border.NO_BORDER);
                        celda_titulo_totaldevengos.add(new Paragraph("Total Devengos"));
                        total_devengos.addCell(celda_titulo_totaldevengos);

                        com.itextpdf.layout.element.Cell celda_v_totaldevengos = new com.itextpdf.layout.element.Cell();
                        celda_v_totaldevengos.setBorder(Border.NO_BORDER);
                        celda_v_totaldevengos.setWidth(150);
                        celda_v_totaldevengos.add(new Paragraph());
                        total_devengos.addCell(celda_v_totaldevengos);

                        com.itextpdf.layout.element.Cell celda_v_totaldevengos2 = new com.itextpdf.layout.element.Cell();
                        celda_v_totaldevengos2.setBorder(Border.NO_BORDER);
                        celda_v_totaldevengos2.setWidth(150);
                        celda_v_totaldevengos2.add(new Paragraph());
                        total_devengos.addCell(celda_v_totaldevengos2);

                        com.itextpdf.layout.element.Cell celda_v_totaldevengos3 = new com.itextpdf.layout.element.Cell();
                        celda_v_totaldevengos3.setBorder(Border.NO_BORDER);
                        celda_v_totaldevengos3.setWidth(250);
                        celda_v_totaldevengos3.add(new Paragraph());
                        total_devengos.addCell(celda_v_totaldevengos3);

                        com.itextpdf.layout.element.Cell celda_total_devengos = new com.itextpdf.layout.element.Cell();
                        celda_total_devengos.setBorder(Border.NO_BORDER);
                        celda_total_devengos.setWidth(100);
                        Double devengos = base / 14 + comp / 14 + trabajadores.get(k).getComplemento_trienios() / 14;
                        brutos_extra.add(devengos);
                        celda_total_devengos.add(new Paragraph("" + redondear(devengos)));

                        total_devengos.addCell(celda_total_devengos);
                        document_extra.add(total_devengos);

                        //Liquido a percibir
                        Table neto = new Table(5);
                        com.itextpdf.layout.element.Cell celda_titulo_liquido = new com.itextpdf.layout.element.Cell();
                        celda_titulo_liquido.setTextAlignment(TextAlignment.LEFT);
                        celda_titulo_liquido.setWidth(190);
                        celda_titulo_liquido.setBorder(Border.NO_BORDER);
                        celda_titulo_liquido.add(new Paragraph("Liquido a percibir"));
                        neto.addCell(celda_titulo_liquido);

                        com.itextpdf.layout.element.Cell celda_v_liquido = new com.itextpdf.layout.element.Cell();
                        celda_v_liquido.setBorder(Border.NO_BORDER);
                        celda_v_liquido.setWidth(150);
                        celda_v_liquido.add(new Paragraph());
                        neto.addCell(celda_v_liquido);

                        com.itextpdf.layout.element.Cell celda_v_liquido2 = new com.itextpdf.layout.element.Cell();
                        celda_v_liquido2.setBorder(Border.NO_BORDER);
                        celda_v_liquido2.setWidth(150);
                        celda_v_liquido2.add(new Paragraph());
                        neto.addCell(celda_v_liquido2);

                        com.itextpdf.layout.element.Cell celda_v_liquido3 = new com.itextpdf.layout.element.Cell();
                        celda_v_liquido3.setBorder(Border.NO_BORDER);
                        celda_v_liquido3.setWidth(250);
                        celda_v_liquido3.add(new Paragraph());
                        neto.addCell(celda_v_liquido3);

                        com.itextpdf.layout.element.Cell celda_total_liquido = new com.itextpdf.layout.element.Cell();
                        celda_total_liquido.setBorder(Border.NO_BORDER);
                        celda_total_liquido.setWidth(100);
                        celda_total_liquido.add(new Paragraph("" + redondear(devengos - extra_deducciones)));

                        neto.addCell(celda_total_liquido);
                        document_extra.add(neto);

                        Table vacio = new Table(1);
                        com.itextpdf.layout.element.Cell celda_aux = new com.itextpdf.layout.element.Cell();
                        celda_aux.setBorder(Border.NO_BORDER);
                        celda_aux.add(new Paragraph());
                        vacio.addCell(celda_aux);
                        document_extra.add(vacio);

                        //Empresario
                        estilo.setFontSize(12);
                        centrarTexto.add(new TabStop((pdfdocument_extra.getDefaultPageSize().getWidth() - document_extra.getLeftMargin() - document_extra.getRightMargin()) / 2, TabAlignment.CENTER));
                        Paragraph costes_emp = new Paragraph().addTabStops(centrarTexto);
                        costes_emp.add(new Tab());
                        costes_emp.add("Costes asociados al empresario").addStyle(estilo);
                        costes_emp.add(new Tab());
                        document_extra.add(costes_emp);

                        Table base_emp = new Table(2);
                        SolidBorder borde = new SolidBorder(1);
                        borde.setColor(ColorConstants.GRAY);
                        base_emp.setBorderTop(borde);

                        com.itextpdf.layout.element.Cell base_emp_titulo = new com.itextpdf.layout.element.Cell();
                        base_emp_titulo.setBorder(Border.NO_BORDER);
                        base_emp_titulo.setTextAlignment(TextAlignment.LEFT);
                        base_emp_titulo.setWidth(700);
                        base_emp_titulo.add(new Paragraph("Calculo Empresario: Base"));
                        base_emp.addCell(base_emp_titulo);

                        com.itextpdf.layout.element.Cell base_emp_costebase = new com.itextpdf.layout.element.Cell();
                        base_emp_costebase.setBorder(Border.NO_BORDER);
                        base_emp_costebase.add(new Paragraph("" + redondear(devengos)));
                        base_emp.addCell(base_emp_costebase);

                        base_emp.setBorderBottom(borde);
                        document_extra.add(base_emp);

                        //Contingencias comunes empresario
                        Table cont_comunes_emp = new Table(2);

                        com.itextpdf.layout.element.Cell cont_comunes_titulo = new com.itextpdf.layout.element.Cell();
                        cont_comunes_titulo.setBorder(Border.NO_BORDER);
                        cont_comunes_titulo.setTextAlignment(TextAlignment.LEFT);
                        cont_comunes_titulo.setWidth(700);
                        cont_comunes_titulo.add(new Paragraph("Contingencias comunes " + (double) Math.round((trabajadores.get(k).getContingencias_comunes() * 100) / trabajadores.get(k).getBruto()) + "%"));
                        cont_comunes_emp.addCell(cont_comunes_titulo);

                        com.itextpdf.layout.element.Cell cont_comunes_coste = new com.itextpdf.layout.element.Cell();
                        cont_comunes_coste.setBorder(Border.NO_BORDER);
                        cont_comunes_coste.add(new Paragraph("0.00"));
                        cont_comunes_emp.addCell(cont_comunes_coste);

                        document_extra.add(cont_comunes_emp);

                        //Desempleo
                        Table desempleo_emp = new Table(2);

                        com.itextpdf.layout.element.Cell desempleo_titulo = new com.itextpdf.layout.element.Cell();
                        desempleo_titulo.setBorder(Border.NO_BORDER);
                        desempleo_titulo.setTextAlignment(TextAlignment.LEFT);
                        desempleo_titulo.setWidth(700);
                        desempleo_titulo.add(new Paragraph("Desempleo " + (double) Math.round((trabajadores.get(k).getDesempleo_emp() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "%"));
                        desempleo_emp.addCell(desempleo_titulo);

                        com.itextpdf.layout.element.Cell desempleo_coste = new com.itextpdf.layout.element.Cell();
                        desempleo_coste.setBorder(Border.NO_BORDER);
                        desempleo_coste.add(new Paragraph("0.00"));
                        desempleo_emp.addCell(desempleo_coste);

                        document_extra.add(desempleo_emp);

                        //Formacion
                        Table formacion_emp = new Table(2);

                        com.itextpdf.layout.element.Cell formacion_titulo = new com.itextpdf.layout.element.Cell();
                        formacion_titulo.setBorder(Border.NO_BORDER);
                        formacion_titulo.setTextAlignment(TextAlignment.LEFT);
                        formacion_titulo.setWidth(700);
                        formacion_titulo.add(new Paragraph("Formacion " + (double) Math.round((trabajadores.get(k).getFormacion_emp() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "%"));
                        formacion_emp.addCell(formacion_titulo);

                        com.itextpdf.layout.element.Cell formacion_coste = new com.itextpdf.layout.element.Cell();
                        formacion_coste.setBorder(Border.NO_BORDER);
                        formacion_coste.add(new Paragraph("0.00"));
                        formacion_emp.addCell(formacion_coste);

                        document_extra.add(formacion_emp);

                        //Accidentes
                        Table accidentes_emp = new Table(2);

                        com.itextpdf.layout.element.Cell accidentes_titulo = new com.itextpdf.layout.element.Cell();
                        accidentes_titulo.setBorder(Border.NO_BORDER);
                        accidentes_titulo.setTextAlignment(TextAlignment.LEFT);
                        accidentes_titulo.setWidth(700);
                        accidentes_titulo.add(new Paragraph("Accidentes " + (double) Math.round((trabajadores.get(k).getAccidentes() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "%"));
                        accidentes_emp.addCell(accidentes_titulo);

                        com.itextpdf.layout.element.Cell accidentes_coste = new com.itextpdf.layout.element.Cell();
                        accidentes_coste.setBorder(Border.NO_BORDER);
                        accidentes_coste.add(new Paragraph("0.00"));
                        accidentes_emp.addCell(accidentes_coste);

                        document_extra.add(accidentes_emp);

                        //FOGASA
                        Table fogasa = new Table(2);

                        com.itextpdf.layout.element.Cell fogasa_titulo = new com.itextpdf.layout.element.Cell();
                        fogasa_titulo.setBorder(Border.NO_BORDER);
                        fogasa_titulo.setTextAlignment(TextAlignment.LEFT);
                        fogasa_titulo.setWidth(700);
                        fogasa_titulo.add(new Paragraph("FOGASA " + (double) Math.round((trabajadores.get(k).getFogasa() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "%"));
                        fogasa.addCell(fogasa_titulo);

                        com.itextpdf.layout.element.Cell fogasa_coste = new com.itextpdf.layout.element.Cell();
                        fogasa_coste.setBorder(Border.NO_BORDER);
                        fogasa_coste.add(new Paragraph("0.00"));
                        fogasa.addCell(fogasa_coste);

                        document_extra.add(fogasa);

                        //TOTAL EMPRESARIO
                        Table total_emp = new Table(2);
                        total_emp.setBorderTop(borde);
                        com.itextpdf.layout.element.Cell total_emp_titulo = new com.itextpdf.layout.element.Cell();
                        total_emp_titulo.setBorder(Border.NO_BORDER);
                        total_emp_titulo.setTextAlignment(TextAlignment.LEFT);
                        total_emp_titulo.setWidth(700);
                        total_emp_titulo.add(new Paragraph("Total Empresario"));
                        total_emp.addCell(total_emp_titulo);

                        com.itextpdf.layout.element.Cell total_emp_coste = new com.itextpdf.layout.element.Cell();
                        total_emp_coste.setBorder(Border.NO_BORDER);
                        total_emp_coste.add(new Paragraph("0.00"));
                        total_emp.addCell(total_emp_coste);
                        total_emp.setMarginBottom(50);
                        document_extra.add(total_emp);
                        //Coste total trabajador
                        Table coste_total = new Table(2);
                        Style estilo2 = new Style();
                        estilo2.setFontColor(ColorConstants.RED);
                        coste_total.addStyle(estilo2);
                        coste_total.setBorderTop(new SolidBorder(2));

                        com.itextpdf.layout.element.Cell coste_t_titulo = new com.itextpdf.layout.element.Cell();
                        coste_t_titulo.setBorder(Border.NO_BORDER);
                        coste_t_titulo.setTextAlignment(TextAlignment.LEFT);
                        coste_t_titulo.setWidth(700);
                        coste_t_titulo.add(new Paragraph("Coste Total Trabajador"));
                        coste_total.addCell(coste_t_titulo);

                        com.itextpdf.layout.element.Cell coste_t_ = new com.itextpdf.layout.element.Cell();
                        coste_t_.setBorder(Border.NO_BORDER);
                        coste_t_.setTextAlignment(TextAlignment.CENTER);
                        coste_t_.add(new Paragraph("" + redondear(devengos)));
                        coste_total.addCell(coste_t_);

                        document_extra.add(coste_total);

                        document_extra.close();

                        pdfwriter_extra.close();
                    } catch (FileNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }

                    System.out.println("Tipo de nómina: Extra.");
                    System.out.println("Nombre: " + trabajadores.get(k).getNombre() + " " + trabajadores.get(k).getApellidos());
                    System.out.println("DNI: " + trabajadores.get(k).getDni());
                    System.out.println("Categoria: " + trabajadores.get(k).getCategoria_empleado());
                    System.out.println("IBAN: " + trabajadores.get(k).getIBAN());
                    System.out.println("Fecha de alta: " + trabajadores.get(k).getFecha_alta());
                    System.out.println("Empresa: " + trabajadores.get(k).getEmpresa() + " (CIF: " + trabajadores.get(k).getCif() + ")");
                    System.out.println("Salario base: " + trabajadores.get(k).getSalarioBase());
                    System.out.println("Complementos: " + (trabajadores.get(k).getComplementos() / 14));
                    System.out.println("Complementos Trienio: " + (trabajadores.get(k).getComplemento_trienios() / 14));
                    System.out.println("Salario bruto: " + (trabajadores.get(k).getBruto() / 14));
                    System.out.println("IRPF: " + (trabajadores.get(k).getIRPF() / 14) + " (" + (double) Math.round((trabajadores.get(k).getIRPF() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "% del salario bruto)");
                    System.out.println("Salario Neto: " + ((trabajadores.get(k).getBruto() / 14) - (trabajadores.get(k).getIRPF() / 14)));
                    System.out.println("_______________________");
                }
                System.out.println("Base de costes del empresario: " + (trabajadores.get(k).getBruto() / 14));
                System.out.println("Seguridad Social: " + (trabajadores.get(k).getContingencias_comunes() / 12) + " (" + (double) Math.round((trabajadores.get(k).getContingencias_comunes() * 100) / trabajadores.get(k).getBruto()) + "% del salario bruto)");
                System.out.println("Cuota por desempleo: " + (trabajadores.get(k).getDesempleo_emp() / 12) + " (" + (double) Math.round((trabajadores.get(k).getDesempleo_emp() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "% del salario bruto)");
                System.out.println("Cuota por Formación: " + (trabajadores.get(k).getFormacion_emp() / 12) + " (" + (double) Math.round((trabajadores.get(k).getFormacion_emp() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "% del salario bruto)");
                System.out.println("Cuota por Accidentes: " + (trabajadores.get(k).getAccidentes() / 12) + " (" + (double) Math.round((trabajadores.get(k).getAccidentes() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "% del salario bruto)");
                System.out.println("FOGASA: " + (trabajadores.get(k).getFogasa() / 12) + " (" + (double) Math.round((trabajadores.get(k).getFogasa() * 100) / trabajadores.get(k).getBruto() * 100) / 100 + "% del salario bruto)");
                System.out.println("Total costes empresario: " + trabajadores.get(k).getCoste_empresario());
                System.out.println("***********************");
            }
            //Fichero XML con las nominas
            int c = 0;
            for (int w = 0; w < trabajadores.size(); w++) {

                org.w3c.dom.Element elem_nominas = doc_nominas.createElement("Nomina");
                elem_nominas.setAttribute("id", "" + w);
                root_nominas.appendChild(elem_nominas);
                //Extra
                org.w3c.dom.Element ex_nomina = doc_nominas.createElement("Extra");
                ex_nomina.appendChild(doc_nominas.createTextNode("NO"));
                elem_nominas.appendChild(ex_nomina);
                //IDFilaExcel
                org.w3c.dom.Element idfilaexcel_nomina = doc_nominas.createElement("IdFilaExcel");
                String id = "" + trabajadores.get(w).getIdFilaExcel();
                idfilaexcel_nomina.appendChild(doc_nominas.createTextNode(id));
                elem_nominas.appendChild(idfilaexcel_nomina);
                //Nombre
                org.w3c.dom.Element nombre_nomina = doc_nominas.createElement("Nombre");
                nombre_nomina.appendChild(doc_nominas.createTextNode(trabajadores.get(w).getNombre() + " " + trabajadores.get(w).getApellidos()));
                elem_nominas.appendChild(nombre_nomina);
                //NIF
                org.w3c.dom.Element dni_nomina = doc_nominas.createElement("NIF");
                dni_nomina.appendChild(doc_nominas.createTextNode(trabajadores.get(w).getDni()));
                elem_nominas.appendChild(dni_nomina);
                //IBAN
                org.w3c.dom.Element iban_nomina = doc_nominas.createElement("IBAN");
                iban_nomina.appendChild(doc_nominas.createTextNode(trabajadores.get(w).getIBAN()));
                elem_nominas.appendChild(iban_nomina);
                //CATEGORIA
                org.w3c.dom.Element cat_nomina = doc_nominas.createElement("Categoria");
                cat_nomina.appendChild(doc_nominas.createTextNode(trabajadores.get(w).getCategoria_empleado()));
                elem_nominas.appendChild(cat_nomina);
                //BRUTO ANUAL

                org.w3c.dom.Element bruto_nomina = doc_nominas.createElement("BrutoAnual");
                if (trabajadores.get(w).getAño() == 0) {
                    bruto_nomina.appendChild(doc_nominas.createTextNode(String.valueOf(brutos.get(w) * (12 - trabajadores.get(w).getMes() + 1))));
                } else {
                    bruto_nomina.appendChild(doc_nominas.createTextNode(String.valueOf(brutos.get(w) * 12)));
                }

                elem_nominas.appendChild(bruto_nomina);
                //IRPF
                org.w3c.dom.Element irpf_nomina = doc_nominas.createElement("IRPF");
                Double irpf_mensual;
                if (trabajadores.get(w).getAño() == 0) {
                    irpf_mensual = 0.0;
                } else {
                    if (trabajadores.get(w).isProrateo()) {
                        irpf_mensual = (trabajadores.get(w).getIRPF() / 12);
                    } else {
                        irpf_mensual = (trabajadores.get(w).getIRPF() / 14);
                    }
                }

                irpf_nomina.appendChild(doc_nominas.createTextNode(irpf_mensual.toString()));
                elem_nominas.appendChild(irpf_nomina);
                //BRUTONOMINA
                org.w3c.dom.Element b_nomina = doc_nominas.createElement("BrutoNomina");
                b_nomina.appendChild(doc_nominas.createTextNode(brutos.get(w).toString()));
                elem_nominas.appendChild(b_nomina);
                //NETONOMINA
                org.w3c.dom.Element n_nomina = doc_nominas.createElement("LiquidoNomina");
                Double n_mensual;
                if (trabajadores.get(w).getAño() == 0) {
                    n_mensual = brutos.get(w) - (trabajadores.get(w).getFormacion_trabajador() / 12 + trabajadores.get(w).getDesempleo_trabajador() / 12 + trabajadores.get(w).getCuota_obrera() / 12);
                } else {
                    n_mensual = brutos.get(w) - (irpf_mensual + trabajadores.get(w).getFormacion_trabajador() / 12 + trabajadores.get(w).getDesempleo_trabajador() / 12 + trabajadores.get(w).getCuota_obrera() / 12);
                }

                n_nomina.appendChild(doc_nominas.createTextNode(n_mensual.toString()));
                elem_nominas.appendChild(n_nomina);
                //COSTETOTALEMPRESARIO
                org.w3c.dom.Element c_nomina = doc_nominas.createElement("CosteEmpresario");
                Double c_mensual = brutos.get(w) + trabajadores.get(w).getFogasa() / 12 + trabajadores.get(w).getContingencias_comunes() / 12 + trabajadores.get(w).getDesempleo_emp() / 12 + trabajadores.get(w).getFormacion_emp() / 12 + trabajadores.get(w).getAccidentes() / 12;
                c_nomina.appendChild(doc_nominas.createTextNode(c_mensual.toString()));
                elem_nominas.appendChild(c_nomina);

                if (trabajadores.get(w).isExtra() && (mes.equals("06") || mes.equals("12"))) {

                    org.w3c.dom.Element elem_nominas_extra = doc_nominas.createElement("Nomina");
                    elem_nominas_extra.setAttribute("id", "" + w);
                    root_nominas.appendChild(elem_nominas_extra);
                    //Extra
                    org.w3c.dom.Element ex_nomina_extra = doc_nominas.createElement("Extra");
                    ex_nomina_extra.appendChild(doc_nominas.createTextNode("SI"));
                    elem_nominas_extra.appendChild(ex_nomina_extra);
                    //IDFilaExcel
                    org.w3c.dom.Element id_fila_extra = doc_nominas.createElement("IdFilaExcel");
                    id_fila_extra.appendChild(doc_nominas.createTextNode("" + trabajadores.get(w).getIdFilaExcel()));
                    elem_nominas_extra.appendChild(id_fila_extra);
                    //Nombre
                    org.w3c.dom.Element nombre_extra = doc_nominas.createElement("Nombre");
                    nombre_extra.appendChild(doc_nominas.createTextNode(trabajadores.get(w).getNombre() + " " + trabajadores.get(w).getApellidos()));
                    elem_nominas_extra.appendChild(nombre_extra);
                    //NIF
                    org.w3c.dom.Element dni_extra = doc_nominas.createElement("NIF");
                    dni_extra.appendChild(doc_nominas.createTextNode("" + trabajadores.get(w).getDni()));
                    elem_nominas_extra.appendChild(dni_extra);
                    //IBAN
                    org.w3c.dom.Element iban_extra = doc_nominas.createElement("IBAN");
                    iban_extra.appendChild(doc_nominas.createTextNode("" + trabajadores.get(w).getIBAN()));
                    elem_nominas_extra.appendChild(iban_extra);
                    //CATEGORIA
                    org.w3c.dom.Element categoria_extra = doc_nominas.createElement("Categoria");
                    categoria_extra.appendChild(doc_nominas.createTextNode("" + trabajadores.get(w).getCategoria_empleado()));
                    elem_nominas_extra.appendChild(categoria_extra);
                    //BRUTO ANUAL
                    org.w3c.dom.Element brutoanual_extra = doc_nominas.createElement("BrutoAnual");
                    if (trabajadores.get(w).getAño() == 0) {
                        brutoanual_extra.appendChild(doc_nominas.createTextNode("" + (trabajadores.get(w).getBruto() / 12 * (12 - trabajadores.get(w).getMes()))));
                    } else {
                        brutoanual_extra.appendChild(doc_nominas.createTextNode("" + trabajadores.get(w).getBruto()));
                    }

                    elem_nominas_extra.appendChild(brutoanual_extra);
                    //IRPF
                    org.w3c.dom.Element irpf_extra = doc_nominas.createElement("IRPF");
                    if (trabajadores.get(w).getAño() == 0) {
                        irpf_extra.appendChild(doc_nominas.createTextNode("0.00"));
                    } else {
                        irpf_extra.appendChild(doc_nominas.createTextNode("" + trabajadores.get(w).getIRPF() / 14));
                    }

                    elem_nominas_extra.appendChild(irpf_extra);
                    //BRUTONOMINA
                    org.w3c.dom.Element bruto_extra = doc_nominas.createElement("BrutoNomina");
                    bruto_extra.appendChild(doc_nominas.createTextNode("" + brutos_extra.get(c)));
                    c += 1;
                    elem_nominas_extra.appendChild(bruto_extra);
                    //NETONOMINA
                    org.w3c.dom.Element n_nomina_extra = doc_nominas.createElement("LiquidoNomina");
                    Double n_mensual_extra;
                    if (trabajadores.get(w).getAño() == 0) {
                        n_mensual_extra = trabajadores.get(w).getBruto() / 14;
                    } else {
                        n_mensual_extra = trabajadores.get(w).getBruto() / 14 - (trabajadores.get(w).getIRPF() / 14);
                    }

                    n_nomina_extra.appendChild(doc_nominas.createTextNode(n_mensual_extra.toString()));
                    elem_nominas_extra.appendChild(n_nomina_extra);
                    //COSTETOTALEMPRESARIO
                    org.w3c.dom.Element c_nomina_extra = doc_nominas.createElement("CosteEmpresario");
                    c_nomina_extra.appendChild(doc_nominas.createTextNode("" + trabajadores.get(w).getBruto() / 14));
                    elem_nominas_extra.appendChild(c_nomina_extra);

                }
            }

            /**
             * LLAMADA CLASE HIBERNATE
             */
            Nominas2022 nom = new Nominas2022();
            nom.insertarNominas(trabajadores, Integer.valueOf(mes), Integer.valueOf(año), brutos);
            Trabajador menorSueldo = nom.findTrabajador();
            //RUTA ACTUALIZACION HOJA EXCEL
            FileOutputStream archivo_final = new FileOutputStream("C:\\Users\\davga\\OneDrive\\Escritorio\\Segunda convocatoria\\Segunda convocatoria\\c\\solucion\\SistemasInformacionII.xlsx");
            wb.write(archivo_final);
            archivo_final.flush();
            archivo_final.close();
            //Creacion del archivo XML trabajadores
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc_dni);
            //RUTA XML ERRORES TRABAJADORES
            StreamResult result = new StreamResult(new File("C:\\Users\\davga\\OneDrive\\Escritorio\\Segunda convocatoria\\Segunda convocatoria\\c\\solucion\\Errores.xml"));

            transformer.transform(source, result);

            //creacion archivo XML cuentas
            TransformerFactory factory_iban = TransformerFactory.newInstance();
            Transformer trans_iban = factory_iban.newTransformer();
            trans_iban.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source_iban = new DOMSource(doc_iban);
            //RUTA XML CUENTAS
            StreamResult resultado_iban = new StreamResult(new File("C:\\Users\\davga\\OneDrive\\Escritorio\\Segunda convocatoria\\Segunda convocatoria\\c\\solucion\\ErroresCCC.xml"));
            trans_iban.transform(source_iban, resultado_iban);

            //CREACION XML NOMINAS
            TransformerFactory factory_nominas = TransformerFactory.newInstance();
            Transformer trans_nominas = factory_nominas.newTransformer();
            trans_nominas.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source_nominas = new DOMSource(doc_nominas);
            //RUTA XML NOMINAS
            StreamResult resultado_nominas = new StreamResult(new File("C:\\Users\\davga\\OneDrive\\Escritorio\\Segunda convocatoria\\Segunda convocatoria\\c\\solucion\\Nominas.xml"));
            trans_nominas.transform(source_nominas, resultado_nominas);

            //**************PDF PARTE 3*******************
            File nomina3 = new File("C:\\Users\\davga\\OneDrive\\Escritorio\\Segunda convocatoria\\Segunda convocatoria\\c\\solucion\\" + menorSueldo.getDni() + menorSueldo.getNombre() + menorSueldo.getApellidos() + mes_texto + año + ".pdf");
            PdfWriter pdfwriter = new PdfWriter(nomina3);
            PdfDocument pdfdocument = new PdfDocument(pdfwriter);
            Document document_nom = new com.itextpdf.layout.Document(pdfdocument);
            PdfPage page = pdfdocument.addNewPage();
            
            Paragraph p = new Paragraph(menorSueldo.getNombre() + " " + menorSueldo.getApellidos() + " (" + menorSueldo.getCategoria_empleado() + ") : " + menorSueldo.getEmpresa() + " (Liquido Nomina: " + menorSueldo.getSalarioBase() + ")");
            document_nom.add(p);
            document_nom.close();
            
            
            //******************************************
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ExcelManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(ExcelManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ExcelManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado, cerrando...");
            System.out.println(e);
            return;
        } catch (IOException e) {
            System.out.println("Archivo no encontrado, cerrando...");
            System.out.println(e);
            return;
        }
    }

    /**
     * Metodo que comprueba la validez de un DNI o de un NIF/NIE
     *
     * @param dni El dni/nif/nie introducido
     * @return true si el DNI/NIF/NIE es correcto
     */
    public static boolean validar(String dni, Trabajador tr) {

        String letraMayuscula = ""; // Guardaremos la letra introducida en formato mayúscula

        // Aquí excluimos cadenas distintas a 9 caracteres que debe tener un dni y
        // también si el último caracter no es una letra
        if (dni.length() != 9 || Character.isLetter(dni.charAt(8)) == false || !Character.isDigit(dni.charAt(2))) {
            return false;
        }

        // Al superar la primera restricción, la letra la pasamos a mayúscula
        letraMayuscula = (dni.substring(8)).toUpperCase();

        //Comprobamos si el documento es un dni o un nif/nie segun el primer caracter
        if (Character.isLetter(dni.charAt(0)) == true) {
            //Llamamos a los metodos privados de la clase checkNIF() y letraNIF
            String letra_correcta = letraNIF(dni);
            if (checkNIF(dni) == true && letra_correcta.equals(letraMayuscula)) {
                dni_correcto = dni;
                return true;
            } else {
                dni.replace(dni.charAt(8), letra_correcta.charAt(0));
                dni_correcto = dni;
                return true;
            }
        } else {
            // Llamamos a los métodos privados de la clase soloNumeros() y letraDNI()
            String letra_correcta = letraDNI(dni);
            if (soloNumeros(dni) == true && letra_correcta.equals(letraMayuscula)) {
                dni_correcto = dni;
                return true;
            } else {
                dni = dni.replace(dni.charAt(8), letra_correcta.charAt(0));
                tr.setDni(dni);
                dni_correcto = dni;
                return true;
            }
        }
    }

    /**
     * Calcula si las 8 primeras posiciones del dni son numeros
     *
     * @param dni
     * @return
     */
    private static boolean soloNumeros(String dni) {

        int i, j = 0;
        String numero = ""; // Es el número que se comprueba uno a uno por si hay alguna letra entre los 8
        // primeros dígitos
        String miDNI = ""; // Guardamos en una cadena los números para después calcular la letra
        String[] unoNueve = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        for (i = 0; i < dni.length() - 1; i++) {
            numero = dni.substring(i, i + 1);

            for (j = 0; j < unoNueve.length; j++) {
                if (numero.equals(unoNueve[j])) {
                    miDNI += unoNueve[j];
                }
            }
        }
        if (miDNI.length() != 8) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Calcula la letra asignada a los numeros del DNI pasados por parametro
     *
     * @param dni
     * @return La letra asignada al dni
     */
    private static String letraDNI(String dni) {
        // El método es privado porque lo voy a usar internamente en esta clase, no se
        // necesita fuera de ella

        // pasar miNumero a integer
        int miDNI = Integer.parseInt(dni.substring(0, 8));
        int resto = 0;
        String miLetra = "";
        String[] asignacionLetra = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S",
            "Q", "V", "H", "L", "C", "K", "E"};

        resto = miDNI % 23;

        miLetra = asignacionLetra[resto];

        return miLetra;
    }

    /**
     * Comprueba que el NIF/NIE es correcto
     *
     * @param dni
     * @return
     */
    private static boolean checkNIF(String dni) {
        int i, j = 0;

        //Segun el algoritmo español, le damos valor a la letra inicial del NIF si es X, Y o Z
        if (dni.charAt(0) != 'X' || dni.charAt(0) != 'Y' || dni.charAt(0) != 'Z') {
            return false;
        }
        String numero = ""; // Es el número que se comprueba uno a uno por si hay alguna letra entre los 8
        // primeros dígitos
        String miDNI = ""; // Guardamos en una cadena los números para después calcular la letra
        String[] unoNueve = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        for (i = 1; i < dni.length() - 1; i++) {
            numero = dni.substring(i, i + 1);

            for (j = 0; j < unoNueve.length; j++) {
                if (numero.equals(unoNueve[j])) {
                    miDNI += unoNueve[j];
                }
            }
        }
        if (miDNI.length() != 7) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Devuelve la letra correspondiente al NIF/NIE
     *
     * @param dni
     * @return
     */
    private static String letraNIF(String dni) {
        int primeraLetra = 0;
        // El método es privado porque lo voy a usar internamente en esta clase, no se
        // necesita fuera de ella
        if (dni.charAt(0) == 'X') {
            primeraLetra = 0;
        } else if (dni.charAt(0) == 'Y') {
            primeraLetra = 1;
        } else if (dni.charAt(0) == 'Z') {
            primeraLetra = 2;
        }
        StringBuffer miDNI = new StringBuffer();
        miDNI.append(primeraLetra);
        miDNI.append(dni.substring(1, 8));
        // pasar nif a integer
        int nif_numero = Integer.parseInt(miDNI.toString());
        int resto = 0;
        String miLetra = "";
        String[] asignacionLetra = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S",
            "Q", "V", "H", "L", "C", "K", "E"};

        resto = nif_numero % 23;

        miLetra = asignacionLetra[resto];

        return miLetra;
    }

    /**
     * Corrige el codigo de cuenta bancaria
     *
     * @param cuenta la cuenta
     * @return si la cuenta estaba bien o no
     */
    public static boolean corregirCuenta(String cuenta) {
        boolean cuenta_check = true;
        StringBuffer cuenta_correcta = new StringBuffer(cuenta.substring(0, 8));
        //para el primer digito de control
        int primero = Integer.valueOf(cuenta.substring(8, 9));
        StringBuffer check_primero = new StringBuffer("00");
        check_primero.append(cuenta.substring(0, 8));

        //para segundo digito de control
        int segundo = Integer.valueOf(cuenta.substring(9, 10));
        StringBuffer check_segundo = new StringBuffer();
        check_segundo.append(cuenta.substring(10));

        //calculo de los digitos correctos
        int primer_digito = checkNumControl(check_primero.toString());
        int sec_digito = checkNumControl(check_segundo.toString());

        if (primero == primer_digito) {
            cuenta_correcta.append(primero);
        } else {
            cuenta_check = false;
            cuenta_correcta.append(primer_digito);
        }
        if (segundo == sec_digito) {
            cuenta_correcta.append(segundo);
        } else {
            cuenta_check = false;
            cuenta_correcta.append(sec_digito);
        }

        cuenta_correcta.append(cuenta.substring(10));
        ccc_correcto = cuenta_correcta.toString();

        return cuenta_check;
    }

    /**
     * Metodo que calcula el digito de control correspondiente
     *
     * @param numeros los 10 numeros corresponientes a ese digito
     * @return valor en int del digito de control
     */
    private static int checkNumControl(String numeros) {
        int result = 0;
        int producto;

        for (int i = 0; i < numeros.length(); i++) {
            int pos = Character.getNumericValue(numeros.charAt(i));;
            producto = (potencia(2, i) % 11);
            result = result + (pos * producto);
        }
        result = result % 11;
        result = 11 - result;
        if (result == 11) {
            result = 0;
        } else if (result == 10) {
            result = 1;
        }

        return result;
    }

    /**
     * Genera un IBAN a partir de una cuenta bancaria
     *
     * @param cuenta el codigo de cuenta bancaria
     * @param pais el pais al que pertenece la cuenta bancaria
     * @return un String con el IBAN calculado
     */
    public static String generarIBAN(String cuenta, String pais) {
        //iban que se genera al final
        StringBuffer iban = new StringBuffer();
        //stringbuffer para los calculos
        StringBuffer iban_calculos = new StringBuffer(cuenta);
        int letra1 = valorLetra(pais.charAt(0));
        int letra2 = valorLetra(pais.charAt(1));

        iban_calculos.append(letra1);
        iban_calculos.append(letra2);
        iban_calculos.append("00");
        BigInteger codigo = new BigInteger(iban_calculos.toString());
        String r = "97";
        String r2 = "98";
        BigInteger resto = new BigInteger(r);
        BigInteger code = codigo.remainder(resto);
        BigInteger result = new BigInteger(r2);

        iban.append(pais);
        iban.append(result.subtract(code));
        iban.append(cuenta);
        return iban.toString();
    }

    private static int potencia(int num, int pow) {
        if (pow == 0) {
            return 1;
        } else {
            return num * potencia(num, pow - 1);
        }
    }

    /**
     * convierte la letra de un pais en el valor numerico asociado
     *
     * @param letra la letra del pais
     * @return el valor numerico asociado a la letra
     */
    private static int valorLetra(char letra) {
        int valorFinal = 0;
        switch (letra) {
            case 'A':
                valorFinal = 10;
                break;
            case 'B':
                valorFinal = 11;
                break;
            case 'C':
                valorFinal = 12;
                break;
            case 'D':
                valorFinal = 13;
                break;
            case 'E':
                valorFinal = 14;
                break;
            case 'F':
                valorFinal = 15;
                break;
            case 'G':
                valorFinal = 16;
                break;
            case 'H':
                valorFinal = 17;
                break;
            case 'I':
                valorFinal = 18;
                break;
            case 'J':
                valorFinal = 19;
                break;
            case 'K':
                valorFinal = 20;
                break;
            case 'L':
                valorFinal = 21;
                break;
            case 'M':
                valorFinal = 22;
                break;
            case 'N':
                valorFinal = 23;
                break;
            case 'O':
                valorFinal = 24;
                break;
            case 'P':
                valorFinal = 25;
                break;
            case 'Q':
                valorFinal = 26;
                break;
            case 'R':
                valorFinal = 27;
                break;
            case 'S':
                valorFinal = 28;
                break;
            case 'T':
                valorFinal = 29;
                break;
            case 'U':
                valorFinal = 30;
                break;
            case 'V':
                valorFinal = 31;
                break;
            case 'W':
                valorFinal = 32;
                break;
            case 'X':
                valorFinal = 33;
                break;
            case 'Y':
                valorFinal = 34;
                break;
            case 'Z':
                valorFinal = 35;
                break;

        }
        return valorFinal;
    }

    /**
     * genera un email a partir de los datos del trabajador
     *
     * @param nombre nombre del trabajador
     * @param apellido1 apellido1 del trabajador
     * @param apellido2 apellido2 del trabajador (si tiene)
     * @param empresa nombre de la empresa a la que pertenece el trabajador
     * @return un email generado a partir de los datos
     */
    public static String generarEmail(String nombre, String apellido1, String apellido2, String empresa) {
        StringBuffer email = new StringBuffer();
        if (apellido2.length() >= 2) {
            email.append(apellido2.charAt(0));
        }
        email.append(apellido1.charAt(0));
        email.append(nombre.charAt(0));
        // ac00 acb
        int rep = 0;
        for (int i = 0; i < correos.size(); i++) {
            if (email.length() > 2 && email.substring(0, 3).equals(correos.get(i).substring(0, 3))) {
                rep++;
            } else if (email.substring(0, 2).equals(correos.get(i).substring(0, 2)) && Character.isDigit(correos.get(i).charAt(2))) {
                rep++;
            } else if (email.substring(0, 1).equals(correos.get(i).substring(0, 1)) && Character.isDigit(correos.get(i).charAt(1))) {
                rep++;
            }
        }
        if (rep < 10) {
            email.append("0");
        }
        email.append(rep);
        email.append("@" + empresa + ".es");

        correos.add(email.toString());
        return email.toString();
    }

    public static double redondear(double numero) {
        double entero, result;
        result = numero;
        entero = Math.floor(result);
        result = (result - entero) * Math.pow(10, 2);
        result = Math.round(result);
        result = (result / Math.pow(10, 2)) + entero;
        return result;
    }

    public static int getDiasMes(String mes) {
        int dias = 30;

        switch (mes) {
            case "02":
                dias = 28;
                break;
            case "01":
            case "03":
            case "05":
            case "07":
            case "08":
            case "10":
            case "12":
                dias = 31;
                break;
            default:
                break;

        }

        return dias;
    }

    /**
     * Metodo para comprobar fecha
     *
     * @param fecha fecha introducida por pantalla
     * @param t Trabajador
     * @return true si el trabajador estaba en su empresa antes de la fecha
     * introducida
     */
    public static boolean antes(String mes, String año, Trabajador t) {
        boolean antes = false;
        int mes_num = Integer.parseInt(mes);
        int año_num = Integer.parseInt(año);
        if (año_num - (2022 - t.getAño()) > 0) {
            antes = true;
        } else if (año_num - (2022 - t.getAño()) == 0) {
            if (mes_num - t.getMes() > 0) {
                antes = true;
            }
        }
        return antes;
    }
}
