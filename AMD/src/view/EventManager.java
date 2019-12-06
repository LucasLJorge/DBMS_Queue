package view;

import java.util.ArrayList;
import java.util.List;

import codigos.Requisicao;
import codigos.Tabela;

public class EventManager {
	private static EventManager instance;
	private ArrayList<EventManagerRunnableListener> runnableListener = new ArrayList<EventManagerRunnableListener>();
	private EventManagerUIListener emuilistener;
	
	private EventManager() {
	}

	public static synchronized EventManager getInstance() {
		if(instance == null) {
			instance = new EventManager();
		}
		return instance;
	}

 	public void addListener(EventManagerUIListener eml) {
 		emuilistener = eml;
	}
 	
 	public void addRunnableListener(EventManagerRunnableListener rl) {
 		if(!runnableListener.contains(rl))
 			runnableListener.add(rl);
	}

	public void enviouRequisicao(List<Requisicao> aux) {
		if(emuilistener != null)
			emuilistener.novaRequisicaoEnviada(aux);
	}

	public void novaFilaEnviada(Tabela tabela) {
		if(emuilistener != null)
			emuilistener.novaFilaEnviada(tabela);
	}

	public void novaTabelaPopulada(Requisicao requisicao) {
		if(emuilistener != null)
			emuilistener.novaTabelaPopulada(requisicao);
	}

	public void novasMetricasEnviadas(String[] metrica) {
		if(emuilistener != null)
			emuilistener.novasMetricasEnviadas(metrica);
	}

	public void stopRun() {
		for(EventManagerRunnableListener rn : runnableListener) {
			rn.stopRun();
		}
	}
	
	public void startRun() {
		for(EventManagerRunnableListener rn : runnableListener) {
			rn.startRun();
		}
	}
	
	public void pauseRun() {
		for(EventManagerRunnableListener rn : runnableListener) {
			rn.pauseRun();
		}
	}
	
}
