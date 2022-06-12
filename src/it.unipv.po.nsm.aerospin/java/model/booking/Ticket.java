package model.booking;

import com.google.zxing.WriterException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

/**
 * Classe che si occupa della creazione e della gestione di un biglietto.
 *
 * @author GruppoNoSuchMethod
 */
public class Ticket {
    private String name;
    private String surname;
    private String departure;
    private String arrival;
    private String flightNumber;
    private String date;
    private String depTime;
    private String arrTime;
    private String path = "src/it.unipv.po.nsm.aerospin/resources/GeneratedPDF/Boarding Pass.pdf";

    /**
     * Costruttore dell'oggetto biglietto.
     *
     * @param name Nome
     * @param surname Cognome
     * @param departure Partenza
     * @param arrival Arrivo
     * @param flightNumber Numero del Volo
     * @param date Data
     * @param depTime Ora Partenza
     * @param arrTime Ora Arrivo
     */
    public Ticket(String name, String surname, String departure, String arrival, String flightNumber, String date, String depTime, String arrTime) {
        this.name = name;
        this.surname = surname;
        this.departure = departure;
        this.arrival = arrival;
        this.flightNumber = flightNumber;
        this.date = date;
        this.depTime = depTime;
        this.arrTime = arrTime;
    }

    public void generateTicket() throws IOException, WriterException {
        File file = new File("src/it.unipv.po.nsm.aerospin/resources/GeneratedPDF/BoardingPassTemplate.pdf");

        PDDocument document =  PDDocument.load(file);
        PDPage page = document.getPage(0);
        PDFont font = PDType0Font.load(document, new File("src/it.unipv.po.nsm.aerospin/resources/fonts/Roboto-Thin.ttf"));
        PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
        contentStream.beginText();
        contentStream.setFont(font,25);
        contentStream.setNonStrokingColor(255,255,255);
        contentStream.newLineAtOffset(113, 215);
        contentStream.drawString(name);
        contentStream.newLineAtOffset(250,0);
        contentStream.drawString(surname);
        contentStream.setFont(font,70);
        contentStream.newLineAtOffset(-275,275);
        contentStream.drawString(departure);
        contentStream.newLineAtOffset(300,0);
        contentStream.drawString(arrival);
        contentStream.setFont(font,25);
        contentStream.newLineAtOffset(-275,-190);
        contentStream.drawString(flightNumber);
        contentStream.newLineAtOffset(125,0);
        contentStream.drawString(date);
        contentStream.newLineAtOffset(175,0);
        contentStream.drawString(depTime);
        contentStream.newLineAtOffset(200,0);
        contentStream.drawString(arrTime);
        contentStream.endText();


        GenerateQRCode qr = new GenerateQRCode(flightNumber+ "" + name + "" + surname);
        qr.generate();

        PDImageXObject pdImage = PDImageXObject.createFromFile("src/it.unipv.po.nsm.aerospin/resources/GeneratedQr/qr.png", document);
        contentStream.drawImage(pdImage, 50, 50);
        System.out.println("Image inserted");
        contentStream.close();
        document.save(path);
        document.close();

    }


    public String getPath(){
        return path;
    }



}
