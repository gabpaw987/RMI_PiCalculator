package pawsoc.client;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import pawsoc.server.Calculator;

/**
 * Der Client stellt eine Verbindung zum CalculatorBalancer her. Dieser ist <br>
 * auch ein Calculator und kann wenn dies aufgerufen wird ueber seine Methoden <br>
 * Pi berechnen.
 * 
 * @author Gabriel Pawlowsky
 * @version 2012-03-01 
 */
public class Client {
	/**
	 * Main-Methode, welche einen Security-Manager erstellt und das Policy-File uebernimmt,<br>
	 * damit eine RMI-Verbindung richtig aufgebaut werden kann, ausserdem lookuped diese<br>
	 * Methode aus dem RMI den Calculator-Balancer-Service und fuehrt uber diesen die Pi-Methode<br>
	 * aus welche Pi zurueckgibt. Ausserdem gibt sie das berechnete Pi aus.
	 * 
	 * @param args Array welches die Konsolen-Argumente wie die Anzahl der Stellen auf die Pi berechnet werden soll<br>
	 * 			   und die IP-Adresse des Balancers beinhalten.<br>
	 */
	public static void main(String args[]) {
		//Es wird ueberprueft, ob wirklich zwei Konsolenargumente uebergeben
		//wurden, wenn ja wird die main-Methode richtig ausgefuehrt
		if (args.length == 2) {
			//Hier wird ueberprueft ob schon ein Security-Manager vorhanden ist, wenn dies
			//nicht der Fall ist wird ein neuer erzeugt.
			if (System.getSecurityManager() == null) {
				//Diese Zeile setzt die java securtity Policy auf die im jar-File vorhandene Policy,
				//die erlaubt, das die gewuenschten RMI-Befehle ausgefuehrt werden koennen.
				//Das Policy im Code setzen macht das selbe wie folgender Konsolenparameter:
				//-Djava.security.policy=/C:/Users/Gabriel/Dropbox/VSDBWorkspace/PICalculatorRMI/security.policy
				System.setProperty("java.security.policy", Client.class
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
				//Hier wird der Calculator-Balancer-Service aus dem RMI ueber Angabe der Balancer-IP
				//gespeichert, um spaeter die Pi-Berechnung durchfuehren zu koennen
				Calculator service = (Calculator) Naming.lookup("//" + args[0]
						+ "/Calculator"); // Man bekommt eine Referenz
				// Nun wird die Pi-Methode ausgefuehrt und zwischengespeichert, um die folgende Abfrage durchfuehren zu koennen
				BigDecimal bd = service.pi(Integer.parseInt(args[1]));
				// Wenn es null ist wird eine Meldung an der User zurueckgegeben,
				// da null nur auftritt, wenn noch kein Server beim Balancer
				//angemeldet ist.
				if (bd == null)
					System.out
							.println("Leider steht noch kein Server zur Verfuegung!! :(");
				
				//Das berechnete Pi wird ausgegeben
				System.out.println(bd);
				
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
			System.err
					.println("Works Like this: \njarname.jar ip-adress PiDigitCount");
		}
	}
}
