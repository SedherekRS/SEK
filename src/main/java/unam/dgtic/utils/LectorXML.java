/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unam.dgtic.utils;

import org.jdom2.input.SAXBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import org.jdom2.Document;
import org.jdom2.Element;
import java.util.List;
import java.util.Map;
import org.jdom2.Attribute;
import org.jdom2.JDOMException;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author edher
 */
public class LectorXML implements Serializable {
    
    public Map<String, String> datosFactura (UploadedFile fileXml) throws JDOMException, IOException{
        int verificador = 0;
        Map<String, String> xmlData = new HashMap<>();
        SAXBuilder builder = new SAXBuilder();
        String outputFile = fileXml.getFileName();
        File file = new File(outputFile);
        Document document = builder.build(file);
        Element rootNode = document.getRootElement();
        List<Attribute> listRoot = rootNode.getAttributes();
        
        for(Attribute atriR : listRoot){
                 switch (atriR.getName()){
                     case "Serie":
                         xmlData.put("serie", atriR.getValue());
                         verificador++;
                         break;
                     case "Folio":
                         xmlData.put("folio", atriR.getValue());
                         verificador++;
                         break;
                     case "Moneda":
                         xmlData.put("moneda", atriR.getValue());
                         verificador++;
                         break;
                     case "Fecha":
                         xmlData.put("fecha", atriR.getValue());
                         verificador++;
                         break;
                     case "Total":
                         xmlData.put("monto", atriR.getValue());
                         verificador++;
                         break;
                     default:                     
                 }
             }      
        for (Element element : rootNode.getChildren()) {
             List<Attribute> list=element.getAttributes();
             for(Attribute atri : list){
                 switch (element.getName()+":"+atri.getName()){
                     case "Emisor:Rfc":
                         xmlData.put("rfc", atri.getValue());
                         verificador++;
                         break;
                 }
             }     
        }
        if(verificador==6){
            System.out.println(xmlData);
        }
        else{
            xmlData=null;
        }      
    return xmlData;
    }
    
}
