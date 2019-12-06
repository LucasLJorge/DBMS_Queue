package codigos;

import view.EventManager;
import view.EventManagerRunnableListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SGBD implements Runnable, EventManagerRunnableListener {
	private boolean pause = false;
	private boolean stop = false;
	private ArrayList<Tabela> tabelas;
	private Queue<Requisicao> filaChegada;// Função de centralizar as requisições para uma manipulação mais fácil

	public SGBD() {
		this.tabelas = new ArrayList<>();
		this.filaChegada = new LinkedList<>();
		EventManager.getInstance().addRunnableListener(this);
	}

	public void criaTabela(int quantidade) {
		for (int i = 0; i < quantidade; i++)
			tabelas.add(new Tabela(i + 1));
	}

	public ArrayList<Tabela> getTabelas() {
		return tabelas;
	}

	public void setTabelas(ArrayList<Tabela> tabelas) {
		this.tabelas = tabelas;
	}

	public Queue<Requisicao> getFilaChegada() {
		return filaChegada;
	}

	public void setFilaChegada(Queue<Requisicao> filaChegada) {
		this.filaChegada = filaChegada;
	}

	public void enviaRequisicao(Requisicao requisicao) {
		try {
			List<Requisicao> aux = new ArrayList<>();
			filaChegada.add(requisicao);
			aux.addAll(filaChegada);

			EventManager.getInstance().enviouRequisicao(aux);
			EventManager.getInstance().novaFilaEnviada(getTabelas().get(requisicao.getDestino() - 1));

			//if (!aux.isEmpty())
				//System.out.println("REQUISIÇÕES AGUARDANDO: " + aux);

		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	public void run() {
		try {
			while (!stop) {
				while (!pause) {
					Thread.sleep(500);
					if (filaChegada.peek() != null) {
						Requisicao requisicao = filaChegada.peek();
						for (Tabela t : tabelas) {
							if (requisicao.getDestino() == t.getId()) {
								requisicao.setTempoFila(System.nanoTime());
								t.getFilaTabela().add(requisicao);
								filaChegada.poll();
							}
						}
					}

				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startRun() {
		stop = false;
		pause = false;
	}

	@Override
	public void stopRun() {
		stop = true;
		pause = true;
	}

	@Override
	public void pauseRun() {
		pause = !pause;
	}
}
