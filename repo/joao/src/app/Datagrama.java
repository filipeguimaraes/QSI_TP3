package app;

import java.io.Serializable;

public class Datagrama implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6750341841814535400L;
    int id;
    Long saida;
    Long intermedio;
    Long chegada;

    public Long getSaida() {
        return saida;
    }

    public void setSaida(Long timestamp) {
        this.saida = timestamp;
    }

    public Long getChegada() {
        return chegada;
    }

    public void setChegada(Long timestamp) {
        this.chegada = timestamp;
    }

    @Override
    public String toString() {
        return "Datagrama [chegada=" + chegada + ", id=" + id + ", intermedio=" + intermedio + ", saida=" + saida + "]";
    }

    public Datagrama(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((chegada == null) ? 0 : chegada.hashCode());
        result = prime * result + id;
        result = prime * result + ((intermedio == null) ? 0 : intermedio.hashCode());
        result = prime * result + ((saida == null) ? 0 : saida.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Datagrama other = (Datagrama) obj;
        if (chegada == null) {
            if (other.chegada != null)
                return false;
        } else if (!chegada.equals(other.chegada))
            return false;
        if (id != other.id)
            return false;
        if (intermedio == null) {
            if (other.intermedio != null)
                return false;
        } else if (!intermedio.equals(other.intermedio))
            return false;
        if (saida == null) {
            if (other.saida != null)
                return false;
        } else if (!saida.equals(other.saida))
            return false;
        return true;
    }

    public Long getIntermedio() {
        return intermedio;
    }

    public void setIntermedio(Long intermedio) {
        this.intermedio = intermedio;
    }
}