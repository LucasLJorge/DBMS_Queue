package codigos;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import view.EventManager;
import view.EventManagerRunnableListener;

public class Executer implements Runnable, EventManagerRunnableListener {
	private boolean pause = false;
	private boolean stop = false;
	private Tabela tabela;
	private static ArrayList<Double> tempoRequisicaoAtendimento = new ArrayList<Double>();
	private static ArrayList<Double> tempoRequisicaoFila = new ArrayList<Double>();

	private static double tempoTotalAtendimento = 0.0;
	private static double tempoTotalRequisicoesFilas = 0.0;

	private static double tempoMedioFilaRequisicoes = 0.0;
	private static double tempoMedioAtendimento = 0.0;

	public Executer(Tabela tabela) {
		this.tabela = tabela;
		EventManager.getInstance().addRunnableListener(this);
	}

	public void calculaTempos() throws InterruptedException {
		Random r = new Random();
		String formato = "0.00";
		DecimalFormat d = new DecimalFormat(formato);

		tabela.getFilaTabela().peek().setTempoSistema(System.nanoTime());
		Thread.sleep((long) r.nextInt(1500));
		tabela.getFilaTabela().peek().setTempoTotal(System.nanoTime());

		tempoRequisicaoAtendimento
				.add((tabela.getFilaTabela().peek().getTempoTotal() - tabela.getFilaTabela().peek().getTempoSistema())
						/ 1_000_000_000.0);

		tempoRequisicaoFila
				.add((tabela.getFilaTabela().peek().getTempoSistema() - tabela.getFilaTabela().peek().getTempoCriacao())
						/ 1_000_000_000.0);

		tempoTotalAtendimento += tempoRequisicaoAtendimento.get(tempoRequisicaoAtendimento.size() - 1);
		tempoMedioAtendimento = tempoTotalAtendimento / tempoRequisicaoAtendimento.size();

		tempoTotalRequisicoesFilas += tempoRequisicaoFila.get(tempoRequisicaoFila.size() - 1);
		tempoMedioFilaRequisicoes = tempoTotalRequisicoesFilas / tempoRequisicaoFila.size();

		EventManager.getInstance().novasMetricasEnviadas(new String[] { d.format(tempoMedioFilaRequisicoes),
				d.format(tempoMedioAtendimento), d.format(tempoMedioFilaRequisicoes + tempoMedioAtendimento) });

	}

	public void run() {
		try {
			while (!stop) {
				while (!pause) {
					Thread.sleep(500);
					if (!tabela.isFlagTrava() && !tabela.isVazio()) {
						tabela.mudaTrava();
						EventManager.getInstance().novaTabelaPopulada(tabela.getFilaTabela().peek());
						if (tabela.getFilaTabela().peek().getTipo() == 'W') {
							calculaTempos();
							tabela.getFilaTabela().poll();
						} else if (tabela.getFilaTabela().peek().getTipo() == 'R') {
							calculaTempos();
							tabela.getFilaTabela().poll();
						}
						tabela.mudaTrava();
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
