import java.util.ArrayList;
import java.util.List;

public class Atraso {
    private final List<Float> atrasos = new ArrayList<>();
    private final List<Float> tempo = new ArrayList<>();

    public Atraso(List<Pacote> captura){
        float anterior = 0;
        for (int i = 0; i<captura.size();i++) {
            tempo.add(captura.get(i).time);
            atrasos.add(captura.get(i).time-anterior);
            anterior = captura.get(i).time;
        }
    }

    public List<Float> getAtrasos() {
        return atrasos;
    }

    public List<Float> getTempo() {
        return tempo;
    }
}
