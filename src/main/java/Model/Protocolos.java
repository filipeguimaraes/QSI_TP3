package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Protocolos {
    private final Map<String, List<Integer>> protocolos = new HashMap<>();

    public Protocolos(List<Pacote> captura) {
        int time = 0;
        for (int i = 0; i < captura.size(); i++) {
            if (((int) captura.get(i).time) <= time) {
                if (protocolos.containsKey(captura.get(i).protocol)) {
                    List<Integer> protocolo = protocolos.get(captura.get(i).protocol);
                    int count = 1;
                    if (protocolo.size() >= time + 1) {
                        count = protocolo.get(time) + 1;
                        protocolo.remove(time);
                    }else {
                        for (int j = protocolo.size()-1; j < time; j++) {
                            protocolo.add(0);
                        }
                    }
                    protocolo.add(count);
                } else {
                    protocolos.put(captura.get(i).protocol, new ArrayList<>());
                }
            } else {
                time++;
                i--;
            }
        }
        for (String p:protocolos.keySet()) {
            for (int i = protocolos.get(p).size(); i < time; i++) {
                protocolos.get(p).add(0);
            }
        }

    }

    public Map<String, List<Integer>> getProtocolos() {
        return protocolos;
    }
}
