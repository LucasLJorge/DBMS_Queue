package codigos;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import view.EventManager;
import view.EventManagerRunnableListener;

public class Cliente implements Runnable, EventManagerRunnableListener {
	private boolean pause = false;
	private boolean stop = false;
	private int id;
	private int count;
	private SGBD sgbd;

	public Cliente(SGBD sgbd, int id) {
		this.id = id;
		this.sgbd = sgbd;
		this.count = 0;
		EventManager.getInstance().addRunnableListener(this);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void run() {
		try {
			while (!stop) {
				while (!pause) {
					Random r = new Random();
					Thread.sleep(800);
					String alphabet = "RW";
					sgbd.enviaRequisicao(new Requisicao(this.id, ++count,
							ThreadLocalRandom.current().nextInt(1, sgbd.getTabelas().size() + 1),
							alphabet.charAt(r.nextInt(alphabet.length()))));

				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
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
