import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;

public class Enderecos {
    private final Map<String, Integer> source = new HashMap<>();
    private final Map<String, Integer> destination = new HashMap<>();

    public Enderecos(List<Pacote> captura) {
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
    }

    public Map<String, Integer> getSource() {
        return source;
    }

    public Map<String, Integer> getDestination() {
        return destination;
    }
}
