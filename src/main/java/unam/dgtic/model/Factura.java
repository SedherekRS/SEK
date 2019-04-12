/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author edher
 */
public class Factura implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String numeroFactura;
    private double monto;
    private String moneda;
    private Date fechaFactura;
    private String rutaFacturaXml;
    private String rutaFacturaPdf;
    private String status;
    private Empresa empresa;
    private Dolar Dolar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public String getRutaFacturaXml() {
        return rutaFacturaXml;
    }

    public void setRutaFacturaXml(String rutaFacturaXml) {
        this.rutaFacturaXml = rutaFacturaXml;
    }

    public String getRutaFacturaPdf() {
        return rutaFacturaPdf;
    }

    public void setRutaFacturaPdf(String rutaFacturaPdf) {
        this.rutaFacturaPdf = rutaFacturaPdf;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Dolar getDolar() {
        return Dolar;
    }

    public void setDolar(Dolar Dolar) {
        this.Dolar = Dolar;
    }
    
    
    
    
    
    
}
