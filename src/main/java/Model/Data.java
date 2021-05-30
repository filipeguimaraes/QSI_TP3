package Model;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Data {

    private static Data instance = null;
    private final Atraso atraso;
    private final Enderecos enderecos;
    private final Protocolos protocolos;


    private Data(String file) throws Exception {
        List<Pacote> captura = read(file);
        atraso = new Atraso(captura);
        enderecos = new Enderecos(captura);
        protocolos = new Protocolos(captura);

    }

    public static Data getInstance(String file) throws Exception {
        instance = new Data(file);
        return instance;
    }

    public static Data getInstance() {
        return instance;
    }

    private List<Pacote> read(String fileName) throws IOException, CsvValidationException {
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

    public Atraso getAtraso() {
        return atraso;
    }

    public Enderecos getEnderecos() {
        return enderecos;
    }

    public Protocolos getProtocolos() {
        return protocolos;
    }
}
