import java.net.InetAddress;

public class Pacote {
    int number;
    float time;
    String source;
    String destination;
    String protocol;
    int size;
    String info;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Pacote{" +
                "number=" + number +
                ", time=" + time +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", protocol='" + protocol + '\'' +
                ", size=" + size +
                ", info='" + info + '\'' +
                "}\n";
    }
}
