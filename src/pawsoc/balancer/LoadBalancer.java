package pawsoc.balancer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import pawsoc.server.Calculator;

/**
 * Der LoadBalancer beinhaltet eine ArrayList, die alle bereits beim Balancer <br>
 * registrierten Berechnungeinheiten von Servern speichert. Ausserdem implementiert<br>
 * er das Balancer-Interface, was es ihm mithilfe der Erbschaft von UnicastRemoteObject<br>
 * und dem daraus resultierenden super() Aufruf der ihn exportiert ermoeglicht, sich<br>
 * an einen RMI-Namensdienst zu binden und die add und die remove-Methode ueber RMI<br>
 * verfuegbar zu machen. Beim Aufruf dieser von einem Server meldet sich dieser mit<br>
 * seiner Berechnungeinheit beim Balancer an und die Einheit wird in die ArrayList<br>
 * eingetragen.
 * 
 * @author Gabriel Pawlowsky & Josef Sochovsky
 * @version 2012-03-01
 */

public class LoadBalancer extends UnicastRemoteObject implements Balancer {
	
	/**
	 * automatisch generierte serialVersionUID
	 */
	private static final long serialVersionUID = 4666375654721677202L;
	// in dieser ArrayList sind alle momentan verfuegbaren 
	private ArrayList<Calculator> calc;
	//diese Variable enthaelt die Zahl aller gespeicherten Server momentan
	private int servers;
	//diese Variable enthaelt die Zahl des zuletzt benutzten Servers, dies ist wichtig
	//um die RoundRobin-Strategy richtig implementieren zu koennen
	private int serversatm;
	/**
	 * Es wird der SuperKonstruktor ausgefuehrt und weiters werden die Zaehlervariablen auf -1 gesetzt <br>
	 * das wird deswegen gemacht weil length so null zurueckgegeben -1 ist am anfang einfacher zu ueber-<br>
	 * pruefen als length() und so weiter <br>
	 * zusaetzlich wird die ArrayList definiert
	 * @throws RemoteException
	 */
	public LoadBalancer() throws RemoteException {
		super();
		this.servers = -1;
		this.serversatm = -1;
		this.calc = new ArrayList<Calculator>();
	}

	/* 
	 * @see pawsoc.balancer.Balancer#add(pawsoc.server.Calculator)
	 */
	@Override
	public void add(Calculator neucalc) throws java.rmi.RemoteException{
		servers++;
		calc.add(neucalc);
		System.out.println("Server bound ... "+neucalc.toString());
	}

	/**
	 * getter-Methode fuer die Anzahl der Server die breits registriert sind
	 * 
	 * @return anzahl der Server
	 */
	public int getServers() {
		return servers;
	}

	/**
	 * setter-Methode fuer die Anzahl der Server die breits registriert sind
	 * 
	 * @param servers anzahl der Server
	 */
	public void setServers(int servers) {
		this.servers = servers;
	}

	/**
	 * getter-Methode fuer den Index des Servers, der als letztes eine Berechnung durchgefuert hat
	 * 
	 * @return Index des Servers
	 */
	public int getServersatm() {
		return serversatm;
	}

	/**
	 * setter-Methode fuer den Index des Servers, der als letztes eine Berechnung durchgefuert hat
	 * 
	 * @param serversatm Index des Servers
	 */
	public void setServersatm(int serversatm) {
		this.serversatm = serversatm;
	}

	/**
	 * getter-Methode fuer die ArrayList, die die Berechnungeinheiten aller bereits registrierten <br>
	 * Server beinhaltet
	 *  
	 * @return ArrayList der Berechnungseinheiten
	 */
	public ArrayList<Calculator> getCalc() {
		return calc;
	}

	/**
	 * setter-Methode fuer die ArrayList, die die Berechnungeinheiten aller bereits registrierten <br>
	 * Server beinhaltet
	 *  
	 * @param calc ArrayList der Berechnungseinheiten
	 */
	public void setCalc(ArrayList<Calculator> calc) {
		this.calc = calc;
	}
	/**
	 * @see pawsoc.balancer.Balancer#remove(pawsoc.server.Calculator)
	 */
	@Override
	public void remove(Calculator altcalc) throws RemoteException {
		if(calc.contains(altcalc)){
			servers--;
			calc.remove(altcalc);
			System.out.println("Server unbound ... "+altcalc.toString());
		} else{
			System.out.println("Dieser Server war nie vorhanden!");
		}
	}
	
}
