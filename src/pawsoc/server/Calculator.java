package pawsoc.server;

import java.math.BigDecimal;
import java.rmi.RemoteException;

/**
 * Calculator-Interface, welches einm Remote-Interface ist und daher angibt, welche<br>
 * Methoden kein UnicastRemoteObject, das dieses Interface implementiert und ueber<br>
 * RMI zugaenglich ist, auch ueber RMI aufrufbar sind. Jede Klasse die dieses Interface<br>
 * implementiert muss eine Pi-Methode besitzen und erscheint als Rechner der Pi auf eine<br>
 * uebergeben Anzahl an Stellen genau berechnet. Ausserdem muss jede Methode dieses Interfaces<br>
 * eine RemoteException werfen, da man per RMI auf sie zugreifen kann und das Remote<br>
 * Interface dies vorschreibt.
 * 
 * @author Gabriel Pawlowsky & Josef Sochovsky
 * @version 2012-03-01 
 */
public interface Calculator extends java.rmi.Remote{
	/**
     * Compute the value of pi to the specified number of 
     * digits after the decimal point.  The value is 
     * computed using Machin's formula:
     *
     *          pi/4 = 4*arctan(1/5) - arctan(1/239)
     *
     * and a power series expansion of arctan(x) to 
     * sufficient precision.
     */
	public BigDecimal pi (int anzahl_nachkommastellen) throws RemoteException;
}