/**
 * 
 */
package pawsoc.balancer;

import java.math.BigDecimal;
import java.rmi.RemoteException;


/**
 * Implementierung des RoundRobinverfahrens mit unserem Beispiel <br>
 * es wird darauf geachtet das der Zaehler nicht ueber die maximal Grenze kommt <br>
 * 
 * @author Josef Sochovsky
 * @version 2012-03-01 
 */
public class RoundRobinStrategy implements Strategy {
	/** 
	 * @see pawsoc.balancer.Strategy#pi(int, pawsoc.balancer.LoadBalancer)
	 * 
	 * Hier ist die Pi-Methode mit dem RoundRobinVerfahren implementiert,
	 * welches angibt, welcher Server benutzt werden soll.
	 */
	@Override
	public BigDecimal pi(int anzahl_nachkommastellen, LoadBalancer lb)
			throws RemoteException {
		//wenn kein Server vorhanden ist soll null zurueckgegeben werden
		if (lb.getServers() == -1)
			return null;
		//jetzt wird der momentane erhoeht
		lb.setServersatm(lb.getServersatm() + 1);
		//fals serversatm die maximale serverzahl uebersteigt wird es wieder auf 0 gesetzt
		if (lb.getServersatm() >= lb.getServers())
			lb.setServersatm(0);
		//jetzt kann die BalancerConsole das ausgeben
		System.out
				.println("Momentan rechnet der Server: " + lb.getServersatm());
		//die pi Methode wird jetzt von dem ausgewaehlten Server berechnet und dann zurueckgegeben
		return lb.getCalc().get(lb.getServersatm()).pi(anzahl_nachkommastellen);
	}

}
