/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.primefaces.model.UploadedFile;
import static unam.dgtic.googleD.CreateGoogleFile.createGoogleFile;
import unam.dgtic.model.Empresa;
/**
 *
 * @author edher
 */
public class CargaArchivos {
    
    public HashMap<String, String> cargar(UploadedFile fileXML, UploadedFile filePDF,Map<String, String> xmlData,Empresa empresa,Integer dolar) throws IOException{
        HashMap<String,String> facturaData = new HashMap<>();
        /////Armado de Archivos y nombres
        File fileSaveXML = new File(fileXML.getFileName());
        File fileSavePDF = new File(filePDF.getFileName());
        String[] fecha = xmlData.get("fecha").split("T");
        String fileNamePdf = fecha[0]+"."+xmlData.get("rfc")+"."+xmlData.get("serie")+xmlData.get("folio")+".pdf";
        String fileNameXml = fecha[0]+"."+xmlData.get("rfc")+"."+xmlData.get("serie")+xmlData.get("folio")+".xml";
        
        //Create google Files
        com.google.api.services.drive.model.File googleFileXml = createGoogleFile("1ofWflOWzxz9cJ6Ccd05jbRuS5E7aX8CS", "text/plain", fileNameXml, fileSaveXML);
        com.google.api.services.drive.model.File googleFilePdf = createGoogleFile("1ofWflOWzxz9cJ6Ccd05jbRuS5E7aX8CS", "", fileNamePdf, fileSavePDF);
        //////Armado de Mapa de datos para persistir
        facturaData.put("numeroFactura", xmlData.get("serie")+xmlData.get("folio"));
        facturaData.put("monto", xmlData.get("monto")); ///////Checar persistencia en REST
        facturaData.put("moneda", xmlData.get("moneda"));
        facturaData.put("fechaFactura", xmlData.get("fecha"));
        facturaData.put("rutaFacturaXml",googleFileXml.getId());
        facturaData.put("rutaFacturaPdf",googleFilePdf.getId());
        facturaData.put("idEmpresa",Integer.toString(empresa.getId()));
        facturaData.put("idDolar",Integer.toString(dolar));
 
        //System.out.println("-->"+googleFileXml.getId());
        //System.out.println("-->"+googleFilePdf.getId());      
        //System.out.println(fileNamePdf);
        //System.out.println(fileNameXml);
        return facturaData;
    }   
}
