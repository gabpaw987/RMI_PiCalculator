package pawsoc.balancer;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import pawsoc.server.Calculator;

/**
 * Der Calculator-Balancer wird spaeter fuer den Client per RMI verfuegbar gemacht<br>
 * und implementiert das Calculator-Interface, das ihn fuer den Client transparent,<br>
 * also wie einen normalen Rechner aussehen laesst. In Wirklichkeit aber benutzt diese Klasse <br>
 * die Strategy(im Moment standardmaessig die RoundRobinstrategy, da keine andere vorhanden ist) <br>
 * um Load-Balancing mit den Servern, die im Load-Balancer einegtragen ist auszufuehren, und laesst <br>
 * alles von diesen Servern berechnen.
 * 
 * @author Gabriel Pawlowsky & Josef Sochovsky
 * @version 2012-03-01 
 */
public class CalculatorBalancer extends UnicastRemoteObject implements
		Calculator {
	/**
	 * automatisch generierte serialVersionUID
	 */
	private static final long serialVersionUID = 3866469092207492403L;
	// wird hier benoetigt um den momentanen Server und insgesamt Server zu
	// erhalten
	private LoadBalancer lb;
	// die Strategy kann von ausserhalb mittels der getter und setter veraendert
	// werden
	private Strategy rbs;

	/**
	 * Dieser Konstruktor soll den super Konstruktor ausfuehren und dann noch <br>
	 * eine Referenz von einem Loadbalancer bekommen und speichern, ausserdem
	 * wird <br>
	 * die Strategie standartmaessig auf RoundRobinStrategy gesetzt<br>
	 * 
	 * @param lb
	 *            ein Loadbalancer wird hier uebergeben damit man die pi methode
	 *            aufrufen kann
	 * @throws RemoteException
	 *             wird in BalancerMain behandelt
	 */
	public CalculatorBalancer(LoadBalancer lb) throws RemoteException {
		super();
		this.lb = lb;
		rbs = new RoundRobinStrategy();
	}

	@Override
	/**
	 * die Methode pi uebergibt der Strategy die Digits und den LoadBalancer damit er alles getten kann
	 * @param anzahl_nachkommastellen beinhaltet die Anzahl der Nachkommastellen
	 * @return die berechnete Zahl
	 */
	public BigDecimal pi(int anzahl_nachkommastellen) throws RemoteException {
		return rbs.pi(anzahl_nachkommastellen, lb);
	}

	/**
	 * Setzt die Strategy
	 * 
	 * @param s
	 *            neue Strategy
	 */
	public void setStrategy(Strategy s) {
		this.rbs = s;
	}

	/**
	 * Gibt die Strategy zurueck
	 * 
	 * @return rbs die momentane Strategy
	 */
	public Strategy getStrategy() {
		return rbs;
	}

}
