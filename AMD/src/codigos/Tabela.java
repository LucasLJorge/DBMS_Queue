package codigos;
import java.util.LinkedList;
import java.util.Queue;

import view.Screen;

public class Tabela {
	private int id;
    private Queue<Requisicao> requisicoes;
    private boolean flagTrava;
    private Executer e;

    public Tabela(int id) {
    	this.id = id;
        this.requisicoes = new LinkedList<>();
        this.flagTrava = false;
        this.e = new Executer(this);
        new Thread(e).start();
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Queue<Requisicao> getRequisicoes() {
		return requisicoes;
	}

	public void setRequisicoes(Queue<Requisicao> requisicoes) {
		this.requisicoes = requisicoes;
	}

	public Queue<Requisicao> getFilaTabela() {
        return requisicoes;
    }

    public void setFilaTabela(Queue<Requisicao> filaTabela) {
        this.requisicoes = filaTabela;
    }

    public boolean isFlagTrava() {
        return flagTrava;
    }

    public void setFlagTrava(boolean flagTrava) {
        this.flagTrava = flagTrava;
    }

    public void mudaTrava(){
        if(!this.flagTrava){
            this.flagTrava = true;
        } else {
            this.flagTrava = false;
        }
    }

    public void add(Requisicao requisicao){
        requisicoes.add(requisicao);
    }

    public Requisicao remove(){
        return this.requisicoes.poll();
    }

    public Requisicao elementoInicial(){
        return this.requisicoes.peek();
    }

    public boolean isVazio(){
        return this.requisicoes.isEmpty();
    }

    public String toString(Requisicao requisicao) {
        String msg = "[" +requisicao.getTipo() +requisicao.getId() +"-" +requisicao.getDestino() +"]";
        return msg;
    }

	public Executer getE() {
		return e;
	}

	public void setE(Executer e) {
		this.e = e;
	}
	
}
