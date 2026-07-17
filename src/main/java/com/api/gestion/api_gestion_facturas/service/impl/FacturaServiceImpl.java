package com.api.gestion.api_gestion_facturas.service.impl;

import java.io.FileOutputStream;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.gestion.api_gestion_facturas.constantes.FacturaConstantes;
import com.api.gestion.api_gestion_facturas.dao.FacturaRepository;
import com.api.gestion.api_gestion_facturas.pojo.Factura;
import com.api.gestion.api_gestion_facturas.security.jwt.JwtFilter;
import com.api.gestion.api_gestion_facturas.service.FacturaService;
import com.api.gestion.api_gestion_facturas.util.FacturaUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService{
    
    private final JwtFilter jwtFilter;

    private final FacturaRepository facturaDAO;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Dentro de Repository DTO");
        try {
            String filename;
            if(validateRequestMap(requestMap)){
                if(requestMap.containsKey("isGenerate") && !(Boolean)requestMap.get("isGenerate")){
                    filename = (String) requestMap.get("uuid");
                }
                else {
                    filename = FacturaUtils.getUUID();
                    requestMap.put("uuid", filename);
                    insertarFactura(requestMap);
                }
                
                String data = "Nombre: " + requestMap.get("nombre")+"\n Numero de contacto: "+requestMap.get("numeroContacto")+
                         "\n"+" Email: " + requestMap.get("email")+"\n"+" Metodo de pago"+requestMap.get("metodoPago");
                
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(FacturaConstantes.STORE_LOCATION+"\\"+filename+".pdf"));
                
                document.open();
                setRectangleInPDF(document);

                Paragraph paragraphHeader = new Paragraph("Gestion de categoria y productos");
                paragraphHeader.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraphHeader);

                PdfPTable pdfPTable = new PdfPTable(5);
                pdfPTable.setWidthPercentage((100));
                addTableHeader(pdfPTable);

                JSONArray jsonArray = FacturaUtils.getJsonArrayFromString((String)requestMap.get("productoDetalles"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    addRows(pdfPTable, FacturaUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(pdfPTable);

                Paragraph footer = new Paragraph("Total: "+requestMap.get("total")+"\n"
                    +"Gracias por visitarnos, vuelva pronto!!", getFont("Data"));
                document.add(footer);

                document.close();

                return new ResponseEntity<>("{\"uuid\":\""+filename+"\"}",HttpStatus.OK);
            }
            return FacturaUtils.getResponseEntity("Datos no encontrados", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity("Algo salio mal en implementacion", HttpStatus.INTERNAL_SERVER_ERROR);
        
    }

    private void setRectangleInPDF(Document document) throws DocumentException {
        log.info("Dentro de setRectanglePDF");
        Rectangle rectangle = new Rectangle(577,825,18,15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorder(1);
        document.add(rectangle);
    }

    private Font getFont(String type){
        log.info("Dentro de GetFont");
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void addRows(PdfPTable pdfPTable, Map<String,Object> data) {
        log.info("Dentro de addRows");
        pdfPTable.addCell((String)data.get("nombre"));
        pdfPTable.addCell((String)data.get("categoria"));
        pdfPTable.addCell((String)data.get("cantidad"));
        pdfPTable.addCell(Double.toString((Double)data.get("precio")));
        pdfPTable.addCell(Double.toString((Double)data.get("total")));
    }


    private void addTableHeader(PdfPTable pdfTable) {
        log.info("Dentro de addTableHeader");
        Stream.of("Nombre","Categoria","Cantidad","Precio","Subtotal")
                    .forEach(columnTitle -> {
                        PdfPCell pdfCell = new PdfPCell();
                        pdfCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        pdfCell.setBorderWidth(2);
                        pdfCell.setPhrase(new Phrase(columnTitle));
                        pdfCell.setBackgroundColor(BaseColor.YELLOW);
                        pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfTable.addCell(pdfCell);
                    });
    }

    private void insertarFactura(Map<String,Object> requestMap) {
        try {
            Factura factura = new Factura();
            factura.setUuid(String.valueOf(requestMap.get("uuid")));
            factura.setNombre(String.valueOf(requestMap.get("nombre")));
            factura.setEmail(String.valueOf(requestMap.get("email")));
            factura.setNumeroContacto(String.valueOf(requestMap.get("numeroContacto")));
            factura.setMetodoPago(String.valueOf(requestMap.get("metodoPago")));
            factura.setTotal(Integer.parseInt(String.valueOf(requestMap.get("total"))));
            factura.setProductoDetalles(String.valueOf(requestMap.get("productoDetalles")));
            factura.setCreatedBy(jwtFilter.getCurrentUser());
            facturaDAO.save(factura);
        } catch (Exception e) {
            e.printStackTrace();        
        }
    }

    private Boolean validateRequestMap(Map<String,Object> requestMap){
        return requestMap.containsKey("nombre") &&
                requestMap.containsKey("numeroContacto") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("metodoPago") &&
                requestMap.containsKey("productoDetalles") &&
                requestMap.containsKey("total");
    }
}
