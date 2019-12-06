package codigos;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Requisicao {
	int id;
    int clientid;
    char tipo;
    int destino;
    long tempoCriacao, tempoFila, tempoSistema, tempoTotal;
    public Requisicao() {
    }

    public Requisicao(int clientid, int id, int destino, char tipo) {
    	this.clientid = clientid;
        this.id = id;
        this.tipo = tipo;
        this.destino = destino;
        //this.tempoEspera1 = System.nanoTime();
        this.tempoCriacao = System.nanoTime();
        this.tempoFila = 0; 
        this.tempoSistema = 0;
        this.tempoTotal = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public int getDestino() {
        return destino;
    }

    public void setDestino(int destino) {
        this.destino = destino;
    }

    public long getTempoCriacao() {
        return tempoCriacao;
    }

    public void setTempoCriacao(long tempoCriacao) {
        this.tempoCriacao = tempoCriacao;
    }

    public long getTempoSistema() {
        return tempoSistema;
    }

    public void setTempoSistema(long tempoSistema) {
        this.tempoSistema = tempoSistema;
    }

    public long getTempoTotal() {
        return tempoTotal;
    }

    public void setTempoTotal(long tempoTotal) {
        this.tempoTotal = tempoTotal;
    }
    

    public int getClientid() {
		return clientid;
	}

	public void setClientid(int clientid) {
		this.clientid = clientid;
	}

	public long getTempoFila() {
		return tempoFila;
	}

	public void setTempoFila(long tempoFila) {
		this.tempoFila = tempoFila;
	}

    @Override
    public String toString() {
        String msg = "" + getClientid() + "." + getId() + "-T" +getDestino();
        return msg;
    }

}
