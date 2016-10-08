package pawsoc.balancer;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

/**
 * Diese Klasse ist dafuer da den Balancer an die Domain zu binden.<br>
 * Binden bedeutet, dass die Balancer-Services ueber einen RMI-Zugriff<br>
 * erreichbar sind.
 * <br> 
 * Folgende Methoden werden dadurch abrufbar:<br>
 * 		add(CalculatorImpl)<br>
 * 		remove(CalculatorImpl)<br>
 * 		pi(int digits)<br>
 * 
 * @author Gabriel Pawlowsky
 * @version 2012-03-01
 */
public class BalancerMain {
	/**
	 * Main Methode sie soll den Balancer binden, die Registry starten und den SecurityManager erstellen wenn er noch <br>
	 * nicht gesetzt ist + Exception Handling
	 * 
	 * @param args es wird in der jar beim Balancer nichts weiter uebergeben 
	 */
	public static void main(String[] args){
		//Starten der rmiregistry
		try {
			//es wird in auf dem Port 1099 die Registry gestartet
			System.out.println(java.rmi.registry.LocateRegistry.createRegistry(1099).toString());
			//Ausgabe, dass es erfolgreich war, oder einer Fehlermeldung,
			//falls es nicht erfolgreich war
			System.out.println("RMI registry ready.");
		} catch (Exception e) {
			System.out.println("Exception starting RMI registry:");
			e.printStackTrace();
		}	
		
		//Hier wird ueberprueft ob schon ein Security-Manager vorhanden ist, wenn dies
		//nicht der Fall ist wird ein neuer erzeugt.
		if (System.getSecurityManager() == null) {
			//Diese Zeile setzt die java securtity Policy auf die im jar-File vorhandene Policy,
			//die erlaubt, das die gewuenschten RMI-Befehle ausgefuehrt werden koennen.
			//Das Policy im Code setzen macht das selbe wie folgender Konsolenparameter:
			//-Djava.security.policy=/C:/Users/Gabriel/Dropbox/VSDBWorkspace/PICalculatorRMI/security.policy
			System.setProperty("java.security.policy", BalancerMain.class
					.getResource("/security.policy").toString());
			//Diese Zeile ersetzt die darueber wenn man in Eclipse arbeitet und kein jar-File hat, sondern
			//das Policy-File sich im Projekt-Ordner befindet
			// System.setProperty("java.security.policy", "file:security.policy");

			//Nun wird der Security-Manager, der nach der Erlaubnis RMI zu benutzen gefragt wird
			//mit der zuvor gesetzten Policy erzeugt
			System.setSecurityManager(new RMISecurityManager());
		}
		
		//hier werden sie kurz auf null gesetzt weil beim erstellen eine exception auftreten kann
		LoadBalancer loadbal = null;
		CalculatorBalancer calbal = null;
		//definieren der Balancer
		try {
			loadbal = new LoadBalancer();
			calbal = new CalculatorBalancer(loadbal);
		} catch (RemoteException e1) {
			System.err.println("Fehler beim erstellen des Calculator und LoadBalancers");
		}
		
		//nun wird der Balancer gebindet und zwar einmal der loadbalancer und dann der calculatorbalancer
		//zusaetzlich wird immer eine Exception ausgegeben, falls hierbei ein Fehler auftritt
		//das binden ermoeglicht dem Server und dem Client die Balancer-Services ueber einen
		//RMI-Zugriff zu nutzen
		try {
			Naming.bind ("Balancer", loadbal);
			System.out.println ("Service bound....");
		} catch (Exception e) {
			System.err.println("Fehler beim Anbieten des Balancer-Dienstes.");
			e.printStackTrace();
		}
		try {
			Naming.bind ("Calculator", calbal);
			System.out.println ("Service bound....");
		} catch (Exception e) {
			System.err.println("Fehler beim Anbieten des Balancer-Dienstes.");
			e.printStackTrace();
		}
		
	}
}
