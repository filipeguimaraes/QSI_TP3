package Model;

public class Endereco {
    private String endereco;
    private int ocorrencias;

    public Endereco(String endereco, int ocorrencias) {
        this.endereco = endereco;
        this.ocorrencias = ocorrencias;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getOcorrencias() {
        return ocorrencias;
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "endereco='" + endereco + '\'' +
                ", ocorrencias=" + ocorrencias +
                '}';
    }
}
