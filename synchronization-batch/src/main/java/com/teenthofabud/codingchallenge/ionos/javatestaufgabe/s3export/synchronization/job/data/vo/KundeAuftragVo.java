package com.teenthofabud.codingchallenge.ionos.javatestaufgabe.s3export.synchronization.job.data.vo;

import com.opencsv.bean.CsvBindByName;
import lombok.*;
import org.apache.logging.log4j.util.Strings;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KundeAuftragVo {

    private static final String CSV_SEPARATOR = ",";

    @CsvBindByName(column = "Firma")
    private String firma;
    @CsvBindByName(column = "Strasse")
    private String strasse;
    @CsvBindByName(column = "Strassenzusatz")
    private String strassenZuSatz;
    @CsvBindByName(column = "Ort")
    private String ort;
    @CsvBindByName(column = "Land")
    private String land;
    @CsvBindByName(column = "PLZ")
    private String plz;
    @CsvBindByName(column = "Vorname")
    private String vorName;
    @CsvBindByName(column = "Nachname")
    private String nachName;
    @CsvBindByName(column = "Kunde-ID")
    private String kundeId;
    @CsvBindByName(column = "Auftrag-ID")
    private String auftragId;
    @CsvBindByName(column = "Artikel-Nummer")
    private String artikelNummer;

    @Override
    public String toString() {
        List<String> lineItem = new LinkedList<>();
        lineItem.add(firma);
        lineItem.add(strasse);
        lineItem.add(strassenZuSatz);
        lineItem.add(ort);
        lineItem.add(land);
        lineItem.add(plz);
        lineItem.add(vorName);
        lineItem.add(nachName);
        lineItem.add(kundeId);
        lineItem.add(auftragId);
        lineItem.add(artikelNummer);
        String lineEntry = Strings.join(lineItem, ',');
        return lineEntry;
    }
}
