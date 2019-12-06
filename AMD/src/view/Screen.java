package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import codigos.Cliente;
import codigos.Requisicao;
import codigos.SGBD;
import codigos.Tabela;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Screen extends JFrame implements EventManagerUIListener {

	private static final long serialVersionUID = 1L;

	public static EventManager em = EventManager.getInstance();
	public static Font font = new Font("Comic Sans MS", Font.PLAIN, 10);
	public static JLabel lblFila1 = new JLabel("[ ]");
	public static JLabel lblFila2 = new JLabel("[ ]");
	public static JLabel lblFila3 = new JLabel("[ ]");
	public static JLabel lblFila4 = new JLabel("[ ]");
	public static JLabel lblFila5 = new JLabel("[ ]");
	public static JLabel lblTabela1 = new JLabel("- - - -");
	public static JLabel lblTabela2 = new JLabel("- - - -");
	public static JLabel lblTabela3 = new JLabel("- - - -");
	public static JLabel lblTabela4 = new JLabel("- - - -");
	public static JLabel lblTabela5 = new JLabel("- - - -");
	public static JLabel lblTempo1 = new JLabel("0,00");
	public static JLabel lblTempo2 = new JLabel("0,00");
	public static JLabel lblTempo3 = new JLabel("0,00");
	public static JButton btnPause = new JButton("Pausar");
	public static SpinnerModel valor = new SpinnerNumberModel(1, 1, 5, 1);
	public static SpinnerModel valor2 = new SpinnerNumberModel(1, 1, 5, 1);
	public static JSpinner jSpinnerTabela = new JSpinner(valor);
	public static JSpinner jSpinnerCliente = new JSpinner(valor2);
	public static JTextArea txtArea = new JTextArea();

	static boolean isRunInBackground = false;
	static boolean isPaused = false;
	static Thread mainThread;
	static Queue<Cliente> clientes;
	// private static JFrame view;

	public Screen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);
		setResizable(false);
		this.setSize(1024, 768);

		em.addListener(this);

		JButton btnStart = new JButton("Iniciar");
		btnStart.setFont(new Font("Calibri Light", Font.PLAIN, 32));
		btnStart.setForeground(new Color(255, 255, 255));
		btnStart.setBackground(new Color(152, 251, 152));
		btnStart.setContentAreaFilled(false);
		btnStart.setOpaque(true);
		btnStart.setFocusable(false);
		btnStart.setBounds(44, 605, 161, 66);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (!isRunInBackground) {
						SGBD sgbd = new SGBD();
						mainThread = new Thread(sgbd);
						mainThread.start();
						sgbd.criaTabela((Integer) jSpinnerTabela.getValue());
						clientes = createPoolClientes((Integer) jSpinnerCliente.getValue(), sgbd);
						EventManager.getInstance().startRun();
						btnStart.setBackground(new Color(230, 87, 87));
						btnStart.setText("Parar");
						btnPause.setEnabled(true);
					} else {
						btnStart.setBackground(new Color(152, 251, 152));
						btnStart.setText("Reiniciar");
						btnPause.setBackground(new Color(222, 222, 222));
						btnPause.setEnabled(false);
						EventManager.getInstance().stopRun();
						Thread.sleep(1000);
						txtArea.setText("");
						lblTabela1.setText("");
						lblTabela2.setText("");
						lblTabela3.setText("");
						lblTabela4.setText("");
						lblTabela5.setText("");
						lblFila1.setText("");
						lblFila2.setText("");
						lblFila3.setText("");
						lblFila4.setText("");
						lblFila5.setText("");
						lblTempo1.setText("0,00");
						lblTempo1.setText("0,00");
						lblTempo1.setText("0,00");
					}
					isRunInBackground = !isRunInBackground;
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		getContentPane().setLayout(null);
		getContentPane().add(btnStart);

		btnPause.setEnabled(false);
		btnPause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!isPaused && btnPause.isEnabled()) {
					btnPause.setText("Retomar");
					EventManager.getInstance().pauseRun();
				} else {
					btnPause.setText("Pausar");
					btnPause.setBackground(new Color(232, 213, 93));
					EventManager.getInstance().pauseRun();
				}
				isPaused = !isPaused;
			}
		});
		btnPause.setOpaque(true);
		btnPause.setFocusable(false);
		btnPause.setForeground(new Color(255, 255, 255));
		btnPause.setFont(new Font("Calibri Light", Font.PLAIN, 32));
		btnPause.setContentAreaFilled(false);
		btnPause.setBackground(new Color(232, 213, 93));
		btnPause.setContentAreaFilled(false);
		btnPause.setOpaque(true);
		btnPause.setBounds(280, 605, 161, 66);
		getContentPane().add(btnPause);

		JLabel lblSistemaDeGerenciamento = new JLabel("Fila de Processamento - SGBD");
		lblSistemaDeGerenciamento.setBounds(24, 24, 417, 48);
		lblSistemaDeGerenciamento.setFont(new Font("Calibri Light", Font.PLAIN, 28));
		getContentPane().add(lblSistemaDeGerenciamento);

		JPanel panelReq = new JPanel();
		panelReq.setBackground(Color.WHITE);
		panelReq.setBounds(24, 85, 950, 166);
		panelReq.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Requisições geradas pelos clientes", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Calibri Light", Font.PLAIN, 18), Color.BLACK));
		getContentPane().add(panelReq);
		panelReq.setLayout(null);
		txtArea.setLineWrap(true);
		txtArea.setFont(new Font("Calibri Light", Font.PLAIN, 22));
		txtArea.setEditable(false);
		txtArea.setOpaque(false);
		txtArea.setBounds(10, 19, 928, 134);
		panelReq.add(txtArea);

		JPanel panelFila = new JPanel();
		panelFila.setBackground(Color.WHITE);
		panelFila.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Filas",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Calibri Light", Font.PLAIN, 18), Color.BLACK));
		panelFila.setBounds(175, 264, 799, 293);
		getContentPane().add(panelFila);
		panelFila.setLayout(null);

		JPanel fila5 = new JPanel();
		fila5.setBackground(Color.WHITE);
		fila5.setBorder(new LineBorder(new Color(0, 0, 0)));
		fila5.setBounds(10, 240, 777, 40);
		panelFila.add(fila5);
		fila5.setLayout(null);
		lblFila5.setBackground(Color.WHITE);
		lblFila5.setFont(new Font("Calibri Light", Font.PLAIN, 24));

		lblFila5.setBounds(12, 0, 753, 40);
		fila5.add(lblFila5);

		JPanel fila4 = new JPanel();
		fila4.setLayout(null);
		fila4.setBorder(new LineBorder(new Color(0, 0, 0)));
		fila4.setBackground(Color.WHITE);
		fila4.setBounds(10, 187, 777, 40);
		panelFila.add(fila4);

		lblFila4.setFont(new Font("Calibri Light", Font.PLAIN, 24));
		lblFila4.setBackground(Color.WHITE);
		lblFila4.setBounds(12, 0, 753, 40);
		fila4.add(lblFila4);

		JPanel fila3 = new JPanel();
		fila3.setLayout(null);
		fila3.setBorder(new LineBorder(new Color(0, 0, 0)));
		fila3.setBackground(Color.WHITE);
		fila3.setBounds(10, 134, 777, 40);
		panelFila.add(fila3);

		lblFila3.setFont(new Font("Calibri Light", Font.PLAIN, 24));
		lblFila3.setBackground(Color.WHITE);
		lblFila3.setBounds(12, 0, 753, 40);
		fila3.add(lblFila3);

		JPanel fila2 = new JPanel();
		fila2.setLayout(null);
		fila2.setBorder(new LineBorder(new Color(0, 0, 0)));
		fila2.setBackground(Color.WHITE);
		fila2.setBounds(10, 81, 777, 40);
		panelFila.add(fila2);

		lblFila2.setFont(new Font("Calibri Light", Font.PLAIN, 24));
		lblFila2.setBackground(Color.WHITE);
		lblFila2.setBounds(12, 0, 753, 40);
		fila2.add(lblFila2);

		JPanel fila1 = new JPanel();
		fila1.setLayout(null);
		fila1.setBorder(new LineBorder(new Color(0, 0, 0)));
		fila1.setBackground(Color.WHITE);
		fila1.setBounds(10, 24, 777, 40);
		panelFila.add(fila1);

		lblFila1.setFont(new Font("Calibri Light", Font.PLAIN, 24));
		lblFila1.setBackground(Color.WHITE);
		lblFila1.setBounds(12, 0, 753, 40);
		fila1.add(lblFila1);

		JPanel panelTabela = new JPanel();
		panelTabela.setBackground(Color.WHITE);
		panelTabela.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Tabelas",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Calibri Light", Font.PLAIN, 18), Color.BLACK));
		panelTabela.setBounds(24, 264, 145, 293);
		getContentPane().add(panelTabela);
		panelTabela.setLayout(null);

		JPanel tab5 = new JPanel();
		tab5.setForeground(Color.WHITE);
		tab5.setBackground(new Color(201, 201, 201));
		tab5.setBorder(new LineBorder(new Color(0, 0, 0)));
		tab5.setBounds(10, 240, 124, 40);
		panelTabela.add(tab5);
		tab5.setLayout(null);
		lblTabela5.setBounds(15, 0, 92, 40);
		tab5.add(lblTabela5);
		lblTabela5.setForeground(new Color(0, 0, 0));
		lblTabela5.setBackground(Color.WHITE);
		lblTabela5.setFont(new Font("Calibri Light", Font.BOLD, 24));

		JPanel tab4 = new JPanel();
		tab4.setLayout(null);
		tab4.setForeground(Color.WHITE);
		tab4.setBorder(new LineBorder(new Color(0, 0, 0)));
		tab4.setBackground(new Color(252, 255, 150));
		tab4.setBounds(10, 187, 124, 40);
		panelTabela.add(tab4);

		lblTabela4.setForeground(new Color(0, 0, 0));
		lblTabela4.setFont(new Font("Calibri Light", Font.BOLD, 24));
		lblTabela4.setBackground(Color.WHITE);
		lblTabela4.setBounds(15, 0, 92, 40);
		tab4.add(lblTabela4);

		JPanel tab3 = new JPanel();
		tab3.setLayout(null);
		tab3.setForeground(Color.WHITE);
		tab3.setBorder(new LineBorder(new Color(0, 0, 0)));
		tab3.setBackground(new Color(177, 255, 171));
		tab3.setBounds(10, 134, 124, 40);
		panelTabela.add(tab3);

		lblTabela3.setForeground(new Color(0, 0, 0));
		lblTabela3.setFont(new Font("Calibri Light", Font.BOLD, 24));
		lblTabela3.setBackground(Color.WHITE);
		lblTabela3.setBounds(15, 0, 92, 40);
		tab3.add(lblTabela3);

		JPanel tab2 = new JPanel();
		tab2.setLayout(null);
		tab2.setForeground(Color.WHITE);
		tab2.setBorder(new LineBorder(new Color(0, 0, 0)));
		tab2.setBackground(new Color(255, 171, 192));
		tab2.setBounds(10, 81, 124, 40);
		panelTabela.add(tab2);

		lblTabela2.setForeground(new Color(0, 0, 0));
		lblTabela2.setFont(new Font("Calibri Light", Font.BOLD, 24));
		lblTabela2.setBackground(Color.WHITE);
		lblTabela2.setBounds(15, 0, 92, 40);
		tab2.add(lblTabela2);

		JPanel tab1 = new JPanel();
		tab1.setLayout(null);
		tab1.setForeground(Color.WHITE);
		tab1.setBorder(new LineBorder(new Color(0, 0, 0)));
		tab1.setBackground(new Color(176, 231, 255));
		tab1.setBounds(10, 24, 124, 40);
		panelTabela.add(tab1);

		lblTabela1.setForeground(new Color(0, 0, 0));
		lblTabela1.setFont(new Font("Calibri Light", Font.BOLD, 24));
		lblTabela1.setBackground(new Color(255, 255, 255));
		lblTabela1.setBounds(15, 0, 92, 40);
		tab1.add(lblTabela1);

		JLabel lblTempoEspera = new JLabel("Tempo médio de espera na fila:");
		lblTempoEspera.setFont(new Font("Calibri Light", Font.PLAIN, 28));
		lblTempoEspera.setBounds(507, 663, 366, 46);
		getContentPane().add(lblTempoEspera);

		lblTempo1.setBounds(885, 663, 99, 46);
		lblTempo1.setFont(new Font("Calibri Light", Font.PLAIN, 28));
		getContentPane().add(lblTempo1);

		JLabel lblTempoDeAtend = new JLabel("Tempo médio de atendimento:");
		lblTempoDeAtend.setFont(new Font("Calibri Light", Font.PLAIN, 28));
		lblTempoDeAtend.setBounds(507, 616, 366, 46);
		getContentPane().add(lblTempoDeAtend);

		lblTempo2.setBounds(885, 616, 99, 46);
		lblTempo2.setFont(new Font("Calibri Light", Font.PLAIN, 28));
		getContentPane().add(lblTempo2);

		JLabel lbltempoTotal = new JLabel("Tempo médio total:");
		lbltempoTotal.setFont(new Font("Calibri Light", Font.PLAIN, 28));
		lbltempoTotal.setBounds(507, 570, 366, 46);
		getContentPane().add(lbltempoTotal);

		lblTempo3.setBounds(885, 570, 99, 46);
		lblTempo3.setFont(new Font("Calibri Light", Font.PLAIN, 28));
		getContentPane().add(lblTempo3);
		jSpinnerTabela.setFont(new Font("Calibri Light", Font.PLAIN, 22));

		jSpinnerTabela.setBounds(755, 50, 46, 32);
		getContentPane().add(jSpinnerTabela);
		jSpinnerCliente.setFont(new Font("Calibri Light", Font.PLAIN, 22));

		jSpinnerCliente.setBounds(755, 13, 46, 32);
		getContentPane().add(jSpinnerCliente);

		JLabel lblFilas = new JLabel("Threads (1-5)");
		lblFilas.setFont(new Font("Calibri Light", Font.PLAIN, 22));
		lblFilas.setBounds(813, 13, 161, 32);
		getContentPane().add(lblFilas);

		JLabel lblTabelas = new JLabel("Tabelas (1-5)");
		lblTabelas.setFont(new Font("Calibri Light", Font.PLAIN, 22));
		lblTabelas.setBounds(813, 50, 130, 32);
		getContentPane().add(lblTabelas);

	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					Screen frame = new Screen();
					frame.setLocation(dim.width / 2 - frame.getSize().width / 2,
							dim.height / 2 - frame.getSize().height / 2);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static Queue<Cliente> createPoolClientes(int quantidade, SGBD sgbd) {
		Queue<Cliente> clientes = new LinkedList<>();

		for (int i = 0; i < quantidade; i++) {
			Cliente c = new Cliente(sgbd, i + 1);
			new Thread(c).start();
			clientes.add(c);
		}
		return clientes;
	}

	@Override
	public synchronized void novaRequisicaoEnviada(List<Requisicao> aux) {

		StringBuilder sb = new StringBuilder(); 
		for (Requisicao i : aux) {
			sb.append(i == null ? "" : "|" + i.toString() + "| "); 
		}

		txtArea.setText(sb.toString());
		/*for (Requisicao i : aux) {
			switch (i.getId()) {
			case 1:
				txtArea.append(i == null ? "" : "|" + i.toString() + "| ");
				break;
			case 2:
				txtArea.append(i == null ? "" : "|" + i.toString() + "| ");
				break;
			case 3:
				txtArea.append(i == null ? "" : "|" + i.toString() + "| ");
				break;
			case 4:
				txtArea.append(i == null ? "" : "|" + i.toString() + "| ");
				break;
			case 5:
				txtArea.append(i == null ? "" : "|" + i.toString() + "| ");
				break;
			}
		}*/
	}

	@Override
	public synchronized void novaFilaEnviada(Tabela tabela) {
		switch (tabela.getId()) {
		case 1:
			lblFila1.setText(tabela.getFilaTabela().toString());
			break;
		case 2:
			lblFila2.setText(tabela.getFilaTabela().toString());
			break;
		case 3:
			lblFila3.setText(tabela.getFilaTabela().toString());
			break;
		case 4:
			lblFila4.setText(tabela.getFilaTabela().toString());
			break;
		case 5:
			lblFila5.setText(tabela.getFilaTabela().toString());
			break;
		}
	}

	@Override
	public void novaTabelaPopulada(Requisicao requisicao) {
		switch (requisicao.getDestino()) {
		case 1:
			lblTabela1.setText(requisicao.toString());
			break;
		case 2:
			lblTabela2.setText(requisicao.toString());
			break;
		case 3:
			lblTabela3.setText(requisicao.toString());
			break;
		case 4:
			lblTabela4.setText(requisicao.toString());
			break;
		case 5:
			lblTabela5.setText(requisicao.toString());
			break;
		}
	}

	@Override
	public void novasMetricasEnviadas(String[] metrica) {
		// lblTempo1,lblTempo2,lblTempo3
		lblTempo1.setText(metrica[0]);
		lblTempo2.setText(metrica[1]);
		lblTempo3.setText(metrica[2]);
	}

}
