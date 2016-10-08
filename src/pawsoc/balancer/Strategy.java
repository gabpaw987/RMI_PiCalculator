package pawsoc.balancer;

import java.math.BigDecimal;
import java.rmi.RemoteException;


/**
 * Dieses Interface gibt die Art der berechnung von pi an <br>
 * in diesem Fall braucht die Strategie die Methode pi <br>
 * diese Methode entscheidet welcher Calculator benutzt wird und gibt,<br>
 * dann das Ergebnis zurueck.
 * 
 * @author Josef Sochovsky
 * @version 2012-03-01 
 */
public interface Strategy {
	/**
	 * Genau so muss eine Strategy implementiert werden
	 * 
	 * @param anzahl_nachkommastellen die Anzahl der Nachkommastellen, auf die das
	 * 								  Pi berechnet werden soll
	 * @param lb der LoadBalancer zum getten und setten der Server
	 * @return das Ergebnis der Pi-Berechnung
	 * @throws RemoteException Falls es Probleme bei der Verbindung gibt
	 */
	public BigDecimal pi(int anzahl_nachkommastellen,LoadBalancer lb) throws RemoteException;
}
