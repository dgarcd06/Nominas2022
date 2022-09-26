
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Trabajador {

    private Double salario_base;
    private String IBAN;
    private boolean prorateo;
    private Date fecha_alta;
    private String categoria_empleado;
    private Double bruto;
    private Double neto;
    private int trienio;
    private Double IRPF;
    private Double extra_junio;
    private Double extra_diciembre;
    private Double s_social;
    private Double accidentes;
    private Double fogasa;
    private Double contingencias_comunes;
    private Double desempleo_emp;
    private Double formacion_emp;
    private Double cuota_obrera;
    private Double desempleo_trabajador;
    private Double formacion_trabajador;
    private String empresa;
    private String cif;
    private String dni;
    private String nombre;
    private String apellidos;
    private Double complementos;
    private Double complemento_trienios;
    private Double coste_empresario;
    private boolean extra;
    private String email;
    private String cuenta;
    private int idFilaExcel;
    private Double porcentaje_IRPF;
    private int año;
    private int mes;

    //constructor => de momento no lo usamos para nada
    public Trabajador() {
    }

    /**
     * GETTERS/SETTERS
     */
    public Double getSalarioBase() {
        return salario_base;
    }

    public void setSalarioBase(Double salario) {
        this.salario_base = salario;
    }

    public boolean isProrateo() {
        return prorateo;
    }

    public void setProrateo(boolean prorateo) {
        this.prorateo = prorateo;
    }

    public Double getBruto() {
        return bruto;
    }

    //bruto anual
    public void setBruto(Double salarioBase, Double complementos, Double comp_trienios) {
        Double bruto = 0.0;
        if (this.getComplemento_trienios() == null) {
            this.complemento_trienios = 0.0;
            comp_trienios = 0.0;
        }
        if (this.prorateo == false) {
            bruto = bruto + salarioBase + complementos + comp_trienios;
        } else {
            bruto = bruto + salarioBase / 14 + complementos / 14 + comp_trienios / 14 + (salario_base / 84 + complementos / 84 + comp_trienios / 84);
            bruto = bruto * 12;
        }

        this.bruto = bruto;
    }

    public Double getNeto() {
        return neto;
    }

    public void setNeto() {
        Double neto = this.getBruto();
        if (this.getIRPF() == null) {
            this.IRPF = 0.0;
        }
        if (this.getFormacion_trabajador() == null) {
            this.formacion_trabajador = 0.0;
        }
        if (this.getDesempleo_trabajador() == null) {
            this.desempleo_trabajador = 0.0;
        }
        if (this.getCuota_obrera() == null) {
            this.cuota_obrera = 0.0;
        }
        if (this.getContingencias_comunes() == null) {
            this.contingencias_comunes = 0.0;
        }
        neto = neto - (this.getIRPF()) - (this.getFormacion_trabajador()) - (this.getDesempleo_trabajador()) - (this.getCuota_obrera());
        this.neto = neto;
    }

    public int getTrienio() {
        return trienio;
    }

    public void setTrienio(Date fecha_alta, Date fecha_nomina) {
        int trienios = 0;
        int diferencia = fecha_nomina.getYear() - fecha_alta.getYear();
        this.setAño(diferencia);
        this.setMes(fecha_alta.getMonth() + 1);
        if (diferencia != 0) {
            if (fecha_nomina.getMonth() <= (fecha_alta.getMonth() + 1)) {
                diferencia--;
            }
        }
        trienios = diferencia / 3;
        if (trienios < 1) {
            trienios = 0;
        }

        this.trienio = trienios;
    }

    public Double getIRPF() {
        return IRPF;
    }

    public void setIRPF(Double IRPF) {

        this.setPorcentaje_IRPF(IRPF);
        Double descuento_irpf = 0.0;
        if (IRPF > 0.0) {
            descuento_irpf = (this.bruto * IRPF) / 100;
        }
        this.IRPF = descuento_irpf;
    }

    public Double getExtraJunio() {
        return extra_junio;
    }

    public void setExtraJunio(Double extra) {
        this.extra_junio = extra;
    }

    public Double getExtraDiciembre() {
        return extra_diciembre;
    }

    public void setExtraDiciembre(Double extra) {
        this.extra_diciembre = extra;
    }

    public Double getS_social() {
        return s_social;
    }

    public void setS_social(Double s_social) {
        this.s_social = s_social;
    }

    public Double getDesempleo_emp() {
        return desempleo_emp;
    }

    public void setDesempleo_emp(Double desempleo_emp) {
        desempleo_emp = ((this.getBruto() * desempleo_emp) / 100);
        this.desempleo_emp = desempleo_emp;
    }

    public Double getFormacion_emp() {
        return formacion_emp;
    }

    public void setFormacion_emp(Double formacion_emp) {
        formacion_emp = ((this.getBruto() * formacion_emp) / 100);
        this.formacion_emp = formacion_emp;
    }

    public Date getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(Date fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    public String getCategoria_empleado() {
        return categoria_empleado;
    }

    public void setCategoria_empleado(String categoria_empleado) {
        this.categoria_empleado = categoria_empleado;
    }

    public Double getExtra_junio() {
        return extra_junio;
    }

    public void setExtra_junio(Double extra_junio) {
        this.extra_junio = extra_junio;
    }

    public Double getExtra_diciembre() {
        return extra_diciembre;
    }

    public void setExtra_diciembre(Double extra_diciembre) {
        this.extra_diciembre = extra_diciembre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Double getComplementos() {
        return complementos;
    }

    public void setComplementos(Double complementos) {
        this.complementos = complementos;
    }

    public Double getComplemento_trienios() {
        return complemento_trienios;
    }

    public void setComplemento_trienios(Double complemento_trienios) {
        this.complemento_trienios = complemento_trienios * 14;
    }

    public Double getAccidentes() {
        return accidentes;
    }

    public void setAccidentes(Double accidentes) {
        accidentes = ((this.getBruto() * accidentes) / 100);
        this.accidentes = accidentes;
    }

    public Double getFogasa() {
        return fogasa;
    }

    public void setFogasa(Double fogasa) {
        fogasa = ((this.getBruto() * fogasa) / 100);
        this.fogasa = fogasa;
    }

    public Double getContingencias_comunes() {
        return contingencias_comunes;
    }

    public void setContingencias_comunes(Double contingencias_comunes) {
        contingencias_comunes = ((this.getBruto() * contingencias_comunes) / 100);
        this.contingencias_comunes = contingencias_comunes;
    }

    public Double getCuota_obrera() {
        return cuota_obrera;
    }

    public void setCuota_obrera(Double cuota_obrera) {
        cuota_obrera = ((this.getBruto() * cuota_obrera) / 100);
        this.cuota_obrera = cuota_obrera;
    }

    public Double getDesempleo_trabajador() {
        return desempleo_trabajador;
    }

    public void setDesempleo_trabajador(Double desempleo_trabajador) {
        desempleo_trabajador = ((this.getBruto() * desempleo_trabajador) / 100);
        this.desempleo_trabajador = desempleo_trabajador;
    }

    public Double getFormacion_trabajador() {
        return formacion_trabajador;
    }

    public void setFormacion_trabajador(Double formacion_trabajador) {
        formacion_trabajador = ((this.getBruto() * formacion_trabajador) / 100);
        this.formacion_trabajador = formacion_trabajador;
    }

    public Double getCoste_empresario() {
        return coste_empresario;
    }

    public void setCoste_empresario() {
        Double coste_empresario = (this.getBruto() / 12);
        if (this.getDesempleo_emp() == null) {
            this.desempleo_emp = 0.0;
        }
        if (this.getFormacion_emp() == null) {
            this.formacion_emp = 0.0;
        }
        if (this.getFogasa() == null) {
            this.fogasa = 0.0;
        }
        if (this.getAccidentes() == null) {
            this.accidentes = 0.0;
        }
        if (this.getContingencias_comunes() == null) {
            this.contingencias_comunes = 0.0;
        }
        coste_empresario = coste_empresario + (this.getDesempleo_emp() / 12) + (this.getFormacion_emp() / 12) + (this.getFogasa() / 12) + (this.getAccidentes() / 12) + (this.getContingencias_comunes() / 12);
        this.coste_empresario = coste_empresario;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public boolean isExtra() {
        return extra;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public int getIdFilaExcel() {
        return idFilaExcel;
    }

    public void setIdFilaExcel(int idFilaExcel) {
        this.idFilaExcel = idFilaExcel;
    }

    public Double getPorcentaje_IRPF() {
        return porcentaje_IRPF;
    }

    public void setPorcentaje_IRPF(Double porcentaje_IRPF) {
        this.porcentaje_IRPF = porcentaje_IRPF;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }
}
