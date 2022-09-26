/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import POJOS.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import modelo.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author David Garcia Diez 71478250T
 *
 */
public class Nominas2022 {

    ExcelManager e = new ExcelManager();
    SessionFactory sf = null;
    SessionFactory sf2 = HibernateUtil.getSessionFactory();
    Session sesion = null;

    public Nominas2022() {

    }

    public void mostrarNominas() {

        sf = HibernateUtil.getSessionFactory();
        sesion = sf.openSession();
        //ESTO EJECUTA LAS QUERYS

        String CIFEmpresa = "FROM Empresas nf";
        String infoTrabaj = "FROM Trabajadorbbdd nf";
        String qnominas = "FROM Nomina nf";

        Query askCIF = sesion.createQuery(CIFEmpresa);
        Query askTrabajador = sesion.createQuery(infoTrabaj);
        Query askNominas = sesion.createQuery(qnominas);

        List<Empresas> resultado = askCIF.list();
        List<Trabajadorbbdd> trabajadores = askTrabajador.list();
        List<Nomina> listNominas = askNominas.list();
        Scanner scanCIF = new Scanner(System.in);

        System.out.println("Introduce un CIF: ");
        String cif_scaneado = scanCIF.nextLine();
        boolean empresa_existe = false;
        double media_nominas = 0.0;
        double cantidad_nominas = 0.0;
        StringBuffer aux = new StringBuffer();
        Nomina nomina_aux;

        for (Empresas nom : resultado) {
            if (nom.getCif().equals(cif_scaneado)) {
                empresa_existe = true;

                //Muestra nombre empresa, CIF, y datos de los trabajadores
                System.out.println("Nombre Empresa: " + nom.getNombre()
                        + "CIF empresa: " + nom.getCif());

                //Bucle que recorre a los trabajadores de la empresa
                for (Trabajadorbbdd t : trabajadores) {
                    if (t.getEmpresas().getCif().equals(nom.getCif())) {
                        System.out.println("Nombre: " + t.getNombre()
                                + " Apellidos: " + t.getApellido1() + " " + t.getApellido2()
                                + " NIF: " + t.getNifnie()
                                + " Categoria: " + t.getCategorias().getNombreCategoria()
                                + " Numero de Nominas: " + t.getNominas().size());
                    }

                }
                SessionFactory act_nombres = HibernateUtil.getSessionFactory();
                Session actualizar = act_nombres.openSession();

                Transaction transacion = actualizar.beginTransaction();
                String cif_elegido = cif_scaneado;
                Query query = actualizar.createQuery("update Empresas e set e.nombre = concat(e.nombre, '2022') where e.cif != :cif_elegido");
                query.setParameter("cif_elegido", cif_scaneado);
                //Query query_borrar = borrar.createQuery("delete Nomina where brutoNomina < " + media_nominas);
                query.executeUpdate();
                transacion.commit();
                actualizar.clear();
                actualizar.close();

                for (Nomina n : listNominas) {
                    for (Trabajadorbbdd t : trabajadores) {
                        if (t.getEmpresas().getCif().equals(nom.getCif())) {
                            media_nominas = media_nominas + n.getBrutoNomina();
                            cantidad_nominas = cantidad_nominas + 1;
                        }
                    }
                }

                media_nominas = media_nominas / cantidad_nominas;
                System.out.println("MEDIA NOMINAS: " + media_nominas);
                SessionFactory rem_nominas = HibernateUtil.getSessionFactory();
                Session borrar = rem_nominas.openSession();

                Transaction tran = borrar.beginTransaction();
                Query query_borrar = borrar.createQuery("delete Nomina where brutoNomina < " + media_nominas);
                query_borrar.executeUpdate();
                tran.commit();
                borrar.close();
                break;
            }
        }

        //Si no se encuentra la empresa, salta el error
        if (empresa_existe == false) {
            System.out.println("Error: Empresa no registrada en el sistema");
        }
        sf.close();
        HibernateUtil.shutdown();

    }

    public void insertarNominas(ArrayList<Trabajador> trabajadores, int mes, int año, ArrayList<Double> brutos) {

        sf = HibernateUtil.getSessionFactory();
        sesion = sf.openSession();
        for (int i = 0; i < trabajadores.size(); i++) {

            //EMPRESAS
            insertarEmpresa(trabajadores, mes, año, sesion, i);

            //CATEGORIAS
            insertarCategoria(trabajadores, mes, año, sesion, i);

            //TRABAJADORES
            insertarTrabajador(trabajadores, mes, año, sesion, i);

            //NOMINAS
            insertarNom(trabajadores, mes, año, sesion, i, brutos);

        }

        sf.close();
        HibernateUtil.shutdown();
    }

    public void insertarEmpresa(ArrayList<Trabajador> trabajadores, int mes, int año, Session sesion, int i) {
        Transaction t_emp = sesion.beginTransaction();
        String queryEmpHQL = "FROM Empresas";
        Query query_emp = sesion.createQuery(queryEmpHQL);
        List<Empresas> emp = query_emp.list();

        boolean emp_existe = false;
        for (Empresas e : emp) {
            if (e.getCif().equals(trabajadores.get(i).getCif())) {
                emp_existe = true;
                break;
            }
        }
        if (emp_existe == false) {
            Empresas e_nueva = new Empresas((int) (Math.random() * 10000000), trabajadores.get(i).getEmpresa(), trabajadores.get(i).getCif());
            sesion.save(e_nueva);
        }
        t_emp.commit();
    }

    public void insertarCategoria(ArrayList<Trabajador> trabajadores, int mes, int año, Session sesion, int i) {
        Transaction t_cat = sesion.beginTransaction();
        String queryCat = "FROM Categorias";
        Query query_cat = sesion.createQuery(queryCat);
        List<Categorias> cat = query_cat.list();

        boolean cat_existe = false;
        for (Categorias c : cat) {
            if (c.getNombreCategoria().equals(trabajadores.get(i).getCategoria_empleado())) {
                cat_existe = true;
                break;
            }
        }
        if (cat_existe == false) {
            Categorias c_nueva = new Categorias((int) (Math.random() * 10000000), trabajadores.get(i).getCategoria_empleado(), trabajadores.get(i).getSalarioBase(), trabajadores.get(i).getComplementos());
            sesion.save(c_nueva);
        }
        t_cat.commit();
    }

    public void insertarTrabajador(ArrayList<Trabajador> trabajadores, int mes, int año, Session sesion, int i) {
        String apellidos[] = trabajadores.get(i).getApellidos().split("\\s+");
        Transaction t_trab = sesion.beginTransaction();
        String querytrab = "FROM Trabajadorbbdd";
        Query query_trab = sesion.createQuery(querytrab);
        List<Trabajadorbbdd> trab = query_trab.list();
        String queryEmpHQL = "FROM Empresas";
        Query query_emp = sesion.createQuery(queryEmpHQL);
        List<Empresas> emp = query_emp.list();
        String queryCat = "FROM Categorias";
        Query query_cat = sesion.createQuery(queryCat);
        List<Categorias> cat = query_cat.list();

        boolean trab_existe = false;
        Trabajadorbbdd t_nueva = new Trabajadorbbdd();
        for (Trabajadorbbdd t : trab) {
            if ((t.getNombre().equals(trabajadores.get(i).getNombre()) && (t.getNifnie().equals(trabajadores.get(i).getDni()))) && (t.getFechaAlta().equals(trabajadores.get(i).getFecha_alta()))) {
                trab_existe = true;
                t_nueva = t;
                break;
            }
        }
        if (trab_existe == false) {
            Categorias cat_trabajador = new Categorias();
            for (Categorias c : cat) {
                if (c.getNombreCategoria().equals(trabajadores.get(i).getCategoria_empleado())) {
                    cat_trabajador = c;
                    break;
                }
            }
            Empresas emp_trabajador = new Empresas();
            for (Empresas e : emp) {
                if (e.getCif().equals(trabajadores.get(i).getCif())) {
                    emp_trabajador = e;
                    break;
                }
            }
            if (apellidos.length == 2) {
                t_nueva = new Trabajadorbbdd((int) (Math.random() * 10000000), cat_trabajador, emp_trabajador, trabajadores.get(i).getNombre(), apellidos[0], apellidos[1], trabajadores.get(i).getDni(), trabajadores.get(i).getEmail(), trabajadores.get(i).getFecha_alta(), trabajadores.get(i).getCuenta(), trabajadores.get(i).getIBAN(), null);
            } else {
                t_nueva = new Trabajadorbbdd((int) (Math.random() * 10000000), cat_trabajador, emp_trabajador, trabajadores.get(i).getNombre(), apellidos[0], null, trabajadores.get(i).getDni(), trabajadores.get(i).getEmail(), trabajadores.get(i).getFecha_alta(), trabajadores.get(i).getCuenta(), trabajadores.get(i).getIBAN(), null);
            }

        }
        sesion.save(t_nueva);
        t_trab.commit();
    }

    public void insertarNom(ArrayList<Trabajador> trabajadores, int mes, int año, Session sesion, int i, ArrayList<Double> brutos) {
        Transaction t_nom = sesion.beginTransaction();
        String querynom = "FROM Nomina";
        Query query_nom = sesion.createQuery(querynom);
        String querytrab = "FROM Trabajadorbbdd";
        Query query_trab = sesion.createQuery(querytrab);
        List<Trabajadorbbdd> trab = query_trab.list();
        List<Nomina> nom = query_nom.list();

        boolean nom_existe = false;
        for (Nomina n : nom) {
            if ((n.getMes() == mes) && (n.getAnio() == año) && (n.getImporteSalarioMes().equals(trabajadores.get(i).getSalarioBase()) && n.getLiquidoNomina().equals(trabajadores.get(i).getNeto()))) {
                nom_existe = true;
                break;
            }
        }
        if (nom_existe == false) {
            Trabajadorbbdd trab_aux = new Trabajadorbbdd();
            for (Trabajadorbbdd t : trab) {
                if ((t.getNombre().equals(trabajadores.get(i).getNombre()) && (t.getNifnie().equals(trabajadores.get(i).getDni()))) && (t.getFechaAlta().equals(trabajadores.get(i).getFecha_alta()))) {
                    trab_aux = t;
                    break;
                }
            }
            Nomina n_nueva;
            if (trabajadores.get(i).getAño() == 0) {
                if (trabajadores.get(i).isProrateo() == true) {
                    n_nueva = new Nomina((int) (Math.random() * 100000000), trab_aux, mes, año, trabajadores.get(i).getTrienio(), trabajadores.get(i).getComplemento_trienios() / 14, trabajadores.get(i).getSalarioBase() / 14, trabajadores.get(i).getComplementos() / 14, trabajadores.get(i).getSalarioBase() / 84, brutos.get(i) * (12 - trabajadores.get(i).getMes()),0.00, 0.00, brutos.get(i), (trabajadores.get(i).getContingencias_comunes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getContingencias_comunes() / 12, (trabajadores.get(i).getDesempleo_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_emp() / 12, (trabajadores.get(i).getFormacion_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_emp() / 12, (trabajadores.get(i).getAccidentes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getAccidentes() / 12, (trabajadores.get(i).getFogasa() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFogasa() / 12, (trabajadores.get(i).getCuota_obrera() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getCuota_obrera() / 12, (trabajadores.get(i).getDesempleo_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_trabajador() / 12, (trabajadores.get(i).getFormacion_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_trabajador() / 12, brutos.get(i), (brutos.get(i) - trabajadores.get(i).getCuota_obrera() / 12 - trabajadores.get(i).getDesempleo_trabajador() / 12 - trabajadores.get(i).getFormacion_trabajador() / 12), brutos.get(i) + trabajadores.get(i).getContingencias_comunes() / 12 + trabajadores.get(i).getDesempleo_emp() / 12 + trabajadores.get(i).getFormacion_emp() / 12 + trabajadores.get(i).getAccidentes() / 12 + trabajadores.get(i).getFogasa() / 12);
                } else {
                    n_nueva = new Nomina((int) (Math.random() * 100000000), trab_aux, mes, año, trabajadores.get(i).getTrienio(), trabajadores.get(i).getComplemento_trienios() / 14, trabajadores.get(i).getSalarioBase() / 14, trabajadores.get(i).getComplementos() / 14, 0.00, brutos.get(i) * (12 - trabajadores.get(i).getMes()),0.00, 0.00, brutos.get(i) * 14 / 12, (trabajadores.get(i).getContingencias_comunes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getContingencias_comunes() / 12, (trabajadores.get(i).getDesempleo_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_emp() / 12, (trabajadores.get(i).getFormacion_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_emp() / 12, (trabajadores.get(i).getAccidentes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getAccidentes() / 12, (trabajadores.get(i).getFogasa() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFogasa() / 12, (trabajadores.get(i).getCuota_obrera() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getCuota_obrera() / 12, (trabajadores.get(i).getDesempleo_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_trabajador() / 12, (trabajadores.get(i).getFormacion_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_trabajador() / 12, brutos.get(i), (brutos.get(i) - trabajadores.get(i).getCuota_obrera() / 12 - trabajadores.get(i).getDesempleo_trabajador() / 12 - trabajadores.get(i).getFormacion_trabajador() / 12), brutos.get(i) + trabajadores.get(i).getContingencias_comunes() / 12 + trabajadores.get(i).getDesempleo_emp() / 12 + trabajadores.get(i).getFormacion_emp() / 12 + trabajadores.get(i).getAccidentes() / 12 + trabajadores.get(i).getFogasa() / 12);
                }
            } else {
                if (trabajadores.get(i).isProrateo() == true) {
                    n_nueva = new Nomina((int) (Math.random() * 100000000), trab_aux, mes, año, trabajadores.get(i).getTrienio(), trabajadores.get(i).getComplemento_trienios() / 14, trabajadores.get(i).getSalarioBase() / 14, trabajadores.get(i).getComplementos() / 14, trabajadores.get(i).getSalarioBase() / 84, brutos.get(i) * 12,(trabajadores.get(i).getIRPF() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getIRPF() / 14, brutos.get(i), (trabajadores.get(i).getContingencias_comunes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getContingencias_comunes() / 12, (trabajadores.get(i).getDesempleo_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_emp() / 12, (trabajadores.get(i).getFormacion_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_emp() / 12, (trabajadores.get(i).getAccidentes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getAccidentes() / 12, (trabajadores.get(i).getFogasa() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFogasa() / 12, (trabajadores.get(i).getCuota_obrera() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getCuota_obrera() / 12, (trabajadores.get(i).getDesempleo_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_trabajador() / 12, (trabajadores.get(i).getFormacion_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_trabajador() / 12, brutos.get(i), (brutos.get(i) - trabajadores.get(i).getCuota_obrera() / 12 - trabajadores.get(i).getDesempleo_trabajador() / 12 - trabajadores.get(i).getFormacion_trabajador() / 12 - trabajadores.get(i).getIRPF() / 14), (brutos.get(i)  + trabajadores.get(i).getContingencias_comunes() / 12 + trabajadores.get(i).getDesempleo_emp() / 12 + trabajadores.get(i).getFormacion_emp() / 12 + trabajadores.get(i).getAccidentes() / 12 + trabajadores.get(i).getFogasa() / 12));
                } else {
                    n_nueva = new Nomina((int) (Math.random() * 100000000), trab_aux, mes, año, trabajadores.get(i).getTrienio(), trabajadores.get(i).getComplemento_trienios() / 14, trabajadores.get(i).getSalarioBase() / 14, trabajadores.get(i).getComplementos() / 14, 0.00, brutos.get(i)*14,(trabajadores.get(i).getIRPF() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getIRPF() / 14, brutos.get(i) * 14 / 12, (trabajadores.get(i).getContingencias_comunes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getContingencias_comunes() / 12, (trabajadores.get(i).getDesempleo_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_emp() / 12, (trabajadores.get(i).getFormacion_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_emp() / 12, (trabajadores.get(i).getAccidentes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getAccidentes() / 12, (trabajadores.get(i).getFogasa() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFogasa() / 12, (trabajadores.get(i).getCuota_obrera() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getCuota_obrera() / 12, (trabajadores.get(i).getDesempleo_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_trabajador() / 12, (trabajadores.get(i).getFormacion_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_trabajador() / 12, brutos.get(i), (brutos.get(i) - trabajadores.get(i).getCuota_obrera() / 12 - trabajadores.get(i).getDesempleo_trabajador() / 12 - trabajadores.get(i).getFormacion_trabajador() / 12 - trabajadores.get(i).getIRPF() / 14), (brutos.get(i) + trabajadores.get(i).getContingencias_comunes() / 12 + trabajadores.get(i).getDesempleo_emp() / 12 + trabajadores.get(i).getFormacion_emp() / 12 + trabajadores.get(i).getAccidentes() / 12 + trabajadores.get(i).getFogasa() / 12));
                }
            }

            sesion.save(n_nueva);

            if (trabajadores.get(i).isExtra() && (mes == 06 || mes == 12)) {
                Nomina n_extra;
                if(trabajadores.get(i).getAño() == 0){
                    n_extra = new Nomina((int) (Math.random() * 100000000), trab_aux, mes, año, trabajadores.get(i).getTrienio(), trabajadores.get(i).getComplemento_trienios() / 14, trabajadores.get(i).getSalarioBase() / 14, trabajadores.get(i).getComplementos() / 14,0.00, trabajadores.get(i).getBruto(),0.00, 0.00, trabajadores.get(i).getBruto() / 14, (trabajadores.get(i).getContingencias_comunes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getContingencias_comunes() / 12, (trabajadores.get(i).getDesempleo_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_emp() / 12, (trabajadores.get(i).getFormacion_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_emp() / 12, (trabajadores.get(i).getAccidentes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getAccidentes() / 12, (trabajadores.get(i).getFogasa() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFogasa() / 12, (trabajadores.get(i).getCuota_obrera() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getCuota_obrera() / 12, (trabajadores.get(i).getDesempleo_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_trabajador() / 12, (trabajadores.get(i).getFormacion_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_trabajador() / 12, trabajadores.get(i).getBruto() / 14, (trabajadores.get(i).getBruto() / 14 - trabajadores.get(i).getCuota_obrera() / 12 - trabajadores.get(i).getDesempleo_trabajador() / 12 - trabajadores.get(i).getFormacion_trabajador() / 12 - trabajadores.get(i).getIRPF() / 14), (trabajadores.get(i).getBruto() / 14 + trabajadores.get(i).getContingencias_comunes() / 12 + trabajadores.get(i).getDesempleo_emp() / 12 + trabajadores.get(i).getFormacion_emp() / 12 + trabajadores.get(i).getAccidentes() / 12 + trabajadores.get(i).getFogasa() / 12));
                }else{
                    n_extra = new Nomina((int) (Math.random() * 100000000), trab_aux, mes, año, trabajadores.get(i).getTrienio(), trabajadores.get(i).getComplemento_trienios() / 14, trabajadores.get(i).getSalarioBase() / 14, trabajadores.get(i).getComplementos() / 14, 0.00, brutos.get(i)*14,(trabajadores.get(i).getIRPF() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getIRPF() / 14, trabajadores.get(i).getBruto() / 14, (trabajadores.get(i).getContingencias_comunes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getContingencias_comunes() / 12, (trabajadores.get(i).getDesempleo_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_emp() / 12, (trabajadores.get(i).getFormacion_emp() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_emp() / 12, (trabajadores.get(i).getAccidentes() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getAccidentes() / 12, (trabajadores.get(i).getFogasa() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFogasa() / 12, (trabajadores.get(i).getCuota_obrera() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getCuota_obrera() / 12, (trabajadores.get(i).getDesempleo_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getDesempleo_trabajador() / 12, (trabajadores.get(i).getFormacion_trabajador() * 100 / trabajadores.get(i).getBruto()), trabajadores.get(i).getFormacion_trabajador() / 12, trabajadores.get(i).getBruto() / 14, (trabajadores.get(i).getBruto() / 14 - trabajadores.get(i).getCuota_obrera() / 12 - trabajadores.get(i).getDesempleo_trabajador() / 12 - trabajadores.get(i).getFormacion_trabajador() / 12 - trabajadores.get(i).getIRPF() / 14), (trabajadores.get(i).getBruto() /14 + trabajadores.get(i).getContingencias_comunes() / 12 + trabajadores.get(i).getDesempleo_emp() / 12 + trabajadores.get(i).getFormacion_emp() / 12 + trabajadores.get(i).getAccidentes() / 12 + trabajadores.get(i).getFogasa() / 12));
                }
                
                sesion.save(n_extra);
            }
        }
        t_nom.commit();
    }
    
    public Trabajador findTrabajador(){
        sesion = sf2.openSession();
        Trabajador t = new Trabajador();
        Transaction t_nom = sesion.beginTransaction();
        String querynom = "FROM Nomina";
        Query query_nom = sesion.createQuery(querynom);
        String querytrab = "FROM Trabajadorbbdd";
        Query query_trab = sesion.createQuery(querytrab);
        List<Trabajadorbbdd> trab = query_trab.list();
        List<Nomina> nom = query_nom.list();
        for (Nomina n : nom) {
            for(Trabajadorbbdd tr: trab){
                if(t.getSalarioBase() == null){
                    t.setNombre(tr.getNombre());
                    if(tr.getApellido2() != null){
                        t.setApellidos(tr.getApellido1() + " " + tr.getApellido2());
                    }else{
                        t.setApellidos(tr.getApellido1());
                    }
                    t.setCategoria_empleado(tr.getCategorias().getNombreCategoria());
                    t.setEmpresa(tr.getEmpresas().getNombre());
                    t.setSalarioBase(n.getLiquidoNomina());
                }else if(n.getTrabajadorbbdd().getNifnie().equals(tr.getNifnie())){
                    if(n.getLiquidoNomina() < t.getSalarioBase()){
                        t.setNombre(tr.getNombre());
                    if(tr.getApellido2() != null){
                        t.setApellidos(tr.getApellido1() + " " + tr.getApellido2());
                    }else{
                        t.setApellidos(tr.getApellido1());
                    }
                    t.setCategoria_empleado(tr.getCategorias().getNombreCategoria());
                    t.setEmpresa(tr.getEmpresas().getNombre());
                    t.setSalarioBase(n.getLiquidoNomina());
                    }
                }
            }
        }
        return t;
    }
}
