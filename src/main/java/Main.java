import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        String fileName = "aaa.csv";
        List<Pacote> captura = read(fileName);
        Atraso atraso = new Atraso(captura);
        Enderecos enderecos = new Enderecos(captura);
        Protocolos protocolos = new Protocolos(captura);
        for (String p: protocolos.getProtocolos().keySet()) {
            System.out.println(p+": "+protocolos.getProtocolos().get(p));
        }

    }


    public static List<Pacote> read(String fileName) throws IOException, CsvValidationException {
        List<Pacote> captura = new ArrayList<>();
        try (var fr = new FileReader(fileName, StandardCharsets.UTF_8);
             var reader = new CSVReader(fr)) {
            reader.readNext();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                Pacote pacote = new Pacote();
                pacote.setNumber(Integer.parseInt(nextLine[0]));
                pacote.setTime(Float.parseFloat(nextLine[1]));
                pacote.setSource(nextLine[2]);
                pacote.setDestination(nextLine[3]);
                pacote.setProtocol(nextLine[4]);
                pacote.setSize(Integer.parseInt(nextLine[5]));
                pacote.setInfo(nextLine[6]);
                captura.add(pacote);
            }
        }
        return captura;
    }

}
