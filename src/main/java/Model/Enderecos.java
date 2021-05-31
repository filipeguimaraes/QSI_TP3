package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Enderecos {
    private List<Endereco> enderecosOrigem = new ArrayList<>();
    private final Map<String, Integer> destination = new HashMap<>();

    public Enderecos(List<Pacote> captura) {
        final Map<String, Integer> source = new HashMap<>();
        for (Pacote pacote : captura) {
            int somaS = 0;
            int somaD = 0;
            String sourceAd = pacote.getSource();
            String destAd = pacote.getDestination();
            if (source.containsKey(sourceAd)){
                somaS = source.get(sourceAd) + 1;
                source.remove(sourceAd);
                source.put(sourceAd,somaS);
            } else {
                source.put(sourceAd,1);
            }
            if (destination.containsKey(destAd)){
                somaD = destination.get(destAd) + 1;
                destination.remove(destAd);
                destination.put(destAd,somaD);
            } else {
                destination.put(destAd,1);
            }
        }

        for (String en : source.keySet()) {
            enderecosOrigem.add(new Endereco(en,source.get(en)));
        }

        enderecosOrigem = enderecosOrigem.stream()
                .sorted((Endereco e1, Endereco e2) -> e2.getOcorrencias() - e1.getOcorrencias())
                .collect(Collectors.toList());

    }

    public List<Endereco> getEnderecosOrigem() {
        //System.out.println(enderecosOrigem);
        return enderecosOrigem;
    }

    public Map<String, Integer> getDestination() {
        return destination;
    }
}
