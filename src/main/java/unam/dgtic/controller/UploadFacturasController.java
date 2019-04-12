/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import org.jdom2.JDOMException;
import org.primefaces.json.JSONObject;
import org.primefaces.model.UploadedFile;
import unam.dgtic.cliente.FacturaJerseyClient;
import unam.dgtic.ejb.EmpresaFacadeLocal;
import unam.dgtic.model.Empresa;
import unam.dgtic.model.Factura;
import unam.dgtic.utils.CargaArchivos;
import unam.dgtic.utils.LectorXML;

/**
 *
 * @author edher
 */
@Named
@ViewScoped
public class UploadFacturasController implements Serializable {
    @Inject
    private Factura factura;
    
    private List<Factura> facturas;
    
    @EJB
    private EmpresaFacadeLocal empresaEJB;

    
    private UploadedFile filePdf;
    private UploadedFile fileXml;

    
    
    
    @PostConstruct  
    public void init(){
        factura = new Factura();
        //0
        FacturaJerseyClient pjc = new FacturaJerseyClient();
            GenericType<List<Factura>> gType = new GenericType<List<Factura>>() {};
            facturas = pjc.findAll(gType);
        //
    }
    public void deleteFactura(){
    
    }   


    public UploadedFile getFilePdf() {
        return filePdf;
    }

    public void setFilePdf(UploadedFile filePdf) {
        this.filePdf = filePdf;
    }

    public UploadedFile getFileXml() {
        return fileXml;
    }

    public void setFileXml(UploadedFile fileXml) {
        this.fileXml = fileXml;
    }
    
    public void cargar(){

        if(filePdf.getSize()!=0 && fileXml.getSize()!=0) {
            if(validarExtension(filePdf,fileXml)){
                LectorXML lector= new LectorXML();
                try {
                    Map<String, String> xmlData = new HashMap<>();
                    xmlData = lector.datosFactura(fileXml);
                    if(xmlData!=null){
                        Empresa empresaRev = new Empresa();
                        empresaRev = revisarEmpresa(xmlData.get("rfc"));
                        if(empresaRev!=null){
                            
                            //System.out.println(empresaRev.toString());
                            try{
                                Map<String, String> facturaData = new HashMap<>();
                                Integer idDolar= getWSDolar();
                                CargaArchivos cargaAr = new CargaArchivos();
                                facturaData=cargaAr.cargar(fileXml, filePdf, xmlData,empresaRev, idDolar);
                                System.out.println(facturaData);
                                sendingData(facturaData);
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso:","Se han cargado los archivos"));
                                
                            }
                            catch(IOException exe){
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error","Contacte al Administrador"));
                                exe.printStackTrace();
                            }
                        }
                        else{
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","La empresa no esta dada de alta"));
                        }
                    //clean();
                    }else{
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error","Contacte al Administrador"));
                    }
                } catch (JDOMException | IOException ex) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error","Contacteal Administrador"));
                }
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","Las extensiones no son correctas"));
            }
        }
        else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Aviso:","Seleccione los Archivos antes"));
        }
    }
    public void clean(){
        filePdf=null;
        fileXml=null;
    }
    
    public boolean validarExtension(UploadedFile filePdf,UploadedFile fileXml){
        boolean result;
        String strPdf = filePdf.getFileName();
        String extPdf = strPdf.substring(strPdf.lastIndexOf('.'), strPdf.length());
        String strXml = fileXml.getFileName();
        String extXml = strXml.substring(strXml.lastIndexOf('.'), strXml.length());
        if(extPdf.equals(".pdf") && extXml.equals(".xml")){
            result=true;
        }
        else{
            result=false;
        }
        return result;
    }
    
    public Empresa revisarEmpresa(String rfc){
        Empresa empresaFind = new Empresa();
        empresaFind=empresaEJB.findByRfc(rfc);
        return empresaFind;
    }
    
    public Integer getWSDolar() throws MalformedURLException, IOException{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date fecha = new Date();
        Integer output  = getUrlContents("http://localhost:8080/SEKREST/webresources/dolar/findFec/"+format.format(fecha));
    return output;
    }

    private Integer getUrlContents(String urlDolar) throws MalformedURLException, IOException{
    int idDolar;
    URL url = new URL(urlDolar);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    int responseCode = con.getResponseCode();
     BufferedReader in = new BufferedReader(
             new InputStreamReader(con.getInputStream()));
     String inputLine;
     StringBuffer response = new StringBuffer();
     while ((inputLine = in.readLine()) != null) {
     	response.append(inputLine);
     }
     in.close();
     //System.out.println(response.toString());
     JSONObject myResponse = new JSONObject(response.toString());
     idDolar=myResponse.getInt("id");

     return idDolar;
  }
    private boolean sendingData(Map<String, String> facturaData) throws MalformedURLException{
        System.out.println("++++++++++++++Sending Data++++++++++++++");
        boolean result=false;
        try {
            
            ///
            String urlParameters = getDataString(facturaData);
            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            String request = "http://localhost:8080/SEKREST/webresources/factura/new";
            URL url = new URL( request );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //
            PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
            printWriter.print(urlParameters); // params => api_key=abcd123&update_data_id=123&update_data_value=Test&amp;Value
            printWriter.flush();
            
            InputStream inputStream = conn.getInputStream();
            String contentType = conn.getContentType();

            result=true;

            }

        catch (IOException ex) {
            Logger.getLogger(UploadFacturasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    private String getDataString(Map<String, String> params) throws UnsupportedEncodingException{
        System.out.println("++++++++++++++getDataString++++++++++++++++");
    StringBuilder result = new StringBuilder();
    boolean first = true;
    for(Map.Entry<String, String> entry : params.entrySet()){
        if (first)
            first = false;
        else
            result.append("&");    
        result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
    }    
    return result.toString();
}

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }
}
