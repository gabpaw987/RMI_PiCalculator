package pawsoc.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import pawsoc.balancer.Balancer;
/**
 * Der Server stellt eine Verbindung zum LoadBalancer her. Dieser beinhaltet eine add-Methode<br>
 * und ermoeglicht es dem Server sich bei ihm anzumelden. Wenn der Server beim<br>
 * Balancer angemeldet ist, kann der Balancer Pi-Berechnungsanfragen an die Referenz<br>
 * der Berechnungseinheit CalculatorImpl vom Server uebergeben, um diese Berechnung<br>
 * durchfuehren zu lassen. Wenn der Server geschlossen wird, meldet er sich ueber einen <br>
 * shutdownHook automatisch vom Namensdienst ab und unexportiert seine Berechnungseinheit.
 * 
 * @author Gabriel Pawlowsky
 * @version 2012-03-01 
 */

public class Server {

	//Diese Attribute muessen gespeichert werden, damit der shutdownHook darauf zugreifen kann.
	//Attribut: Calculator, die Berechnungseinheit des Servers, die Pi anfragen uebernimmt 
	static Calculator c;
	//Attribut: Balancer, der die add-Methode zur Verfuegung stellt
	static Balancer service;
	
	/**
	 * Main-Methode, welche einen Security-Manager erstellt und das Policy-File uebernimmt,<br>
	 * damit eine RMI-Verbindung richtig aufgebaut werden kann, ausserdem lookuped diese<br>
	 * Methode aus dem RMI den Load-Balancer-Service und fuehrt uber diesen die add-Methode<br>
	 * aus welche es dem Server ermoeglicht seine Berechnungseinheit an den Balancer zu uebergeben, <br>
	 * damit dieser diese in seine Serverliste eintragen kann und die Berechnungsauftraege vom Client <br>
	 * auf den Server auslagern kann.
	 * 
	 * @param args Array welches die Konsolen-Argumente wie die Anzahl der Stellen auf die Pi berechnet werden soll<br>
	 * 			   und die IP-Adresse des Balancers beinhalten.<br>
	 */
	public static void main(String args[]){
		//Es wird ueberprueft, ob wirklich ein Konsolenargument uebergeben
		//wurden, wenn ja wird die main-Methode richtig ausgefuehrt
		if(args.length == 1){
			//Hier wird ueberprueft ob schon ein Security-Manager vorhanden ist, wenn dies
			//nicht der Fall ist wird ein neuer erzeugt.
			if (System.getSecurityManager() == null) {
				//Diese Zeile setzt die java securtity Policy auf die im jar-File vorhandene Policy,
				//die erlaubt, das die gewuenschten RMI-Befehle ausgefuehrt werden koennen.
				//Das Policy im Code setzen macht das selbe wie folgender Konsolenparameter:
				//-Djava.security.policy=/C:/Users/Gabriel/Dropbox/VSDBWorkspace/PICalculatorRMI/security.policy
				System.setProperty("java.security.policy", Server.class
						.getResource("/security.policy").toString());
				//Diese Zeile ersetzt die darueber wenn man in Eclipse arbeitet und kein jar-File hat, sondern
				//das Policy-File sich im Projekt-Ordner befindet
				// System.setProperty("java.security.policy", "file:security.policy");

				//Nun wird der Security-Manager, der nach der Erlaubnis RMI zu benutzen gefragt wird
				//mit der zuvor gesetzten Policy erzeugt
				System.setSecurityManager(new RMISecurityManager());
			}
			
			//try-Catch Block, um die Exceptions abfangen und Fehlermeldungen ausgeben zu koennen
			try {
				//Es wird der Load-Balancer Service gelookuped und gespeichert, um sich spaeter
				//beim Balancer an und abmelden zu koennen.
				service = (Balancer) Naming.lookup("//"+args[0]+"/Balancer"); // Man bekommt eine Referenz
				
				//Nun wird die Berechnungseinheit, das CalculatorImpl-Objekt
				//erzeugt und im Attribut gespeichert
				c = new CalculatorImpl();
				//Die Meldung, das dieser Vorgang erfolgreich war wird ausgegeben, wenn
				//es zu keiner Exception gekommen ist
				System.out.println("Calculator exported!");
				
				/**
				 * ShutdownHook, der sobald der Server auf welche Weise auch immer geschlossen wird, <br>
				 * diesen aus dem RMI-Namensdienst einfernt und seine eigene Berechnungseinheit unexportiert.<br>
				 * Dieser wird als Thread erzeugt und an die Runtime gebunden.
				 */
				Runtime.getRuntime().addShutdownHook(new Thread()
		        {
					/**
					 * run-Methode, die von der start-Methode aufgerufen wird, sobald das Programm beendet wird<br>
					 * und die die beschriebene Funktions des shutdownHooks implementiert.
					 */
		            @Override
		            public void run()
		            {
		            	//try-Catch Block, um die Exceptions abfangen und Fehlermeldungen ausgeben zu koennen
						try {
							//Diese Zeile entfernt ueber die remove-Methode des LoadBalancer-Services
							//die eigene Berechnungseinheit aus dem Namensdienst
							service.remove(c);
							//unexportiert anschliessend die Berehnungseinheit
							UnicastRemoteObject.unexportObject(c, true);
							//Zu den Exceptions die auftreten koennen werden hier die passenden Fehlermeldungen ausgegeben
						} catch (NoSuchObjectException e) {
							System.err.println("Objekt nicht vorhanden!");
						} catch (RemoteException e) {
							System.err.println("Balancer nicht erreichbar!");
						}
		            }
		        });
				
				//Der Server wird ueber einen RMI-Zugriff auf den LoadBalancer
				//in dessen Serverliste registriert.
				service.add(c);
				//Zu den Exceptions die auftreten koennen werden hier die passenden Fehlermeldungen ausgegeben
			} catch (NumberFormatException e) {
				System.err.println("Die Zahl hat das falsche Format!");
			} catch (RemoteException e) {
				System.err.println("Verbindung konnte nicht aufgebaut werden!");
			} catch (MalformedURLException e) {
				System.err.println("Die URL zum RMI-Namensdienst ist fehlerhaft!");
			} catch (NotBoundException e) {
				System.err.println("Der gefragte Service ist nicht gebunden!");
			}
		} else {
			//Synopsis wird ausgegeben
			System.err.println("Works Like this: \njarname.jar ip-adress");
		}
	}

}
