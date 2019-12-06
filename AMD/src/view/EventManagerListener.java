package view;

import java.util.EventListener;
import java.util.List;

import codigos.Requisicao;
import codigos.Tabela;

public interface EventManagerListener extends EventListener{
	void novaRequisicaoEnviada(List<Requisicao> aux);
	void novaFilaEnviada(Tabela tabela);
	void novaTabelaPopulada(Requisicao requisicao);
	void novasMetricasEnviadas(String[] metricas);
}