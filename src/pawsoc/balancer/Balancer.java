package
pawsoc.balancer;

import pawsoc.server.Calculator;
/**
 * Dieses Interface gibt die Eigenschaften fuer die LoadBalancer <br>
 * 
 * @author Gabriel Pawlowsky & Josef Sochovsky
 * @version 2012-03-01
 */
public interface Balancer extends java.rmi.Remote {
	/**
	 * Fuegt einen Calculator zur ArrayList hinzu
	 * @param c der neue Calculator
	 * @throws java.rmi.RemoteException
	 */
	public void add(Calculator c) throws java.rmi.RemoteException;
	/**
	 * Loescht einen Calculator von der ArrayList<br>
	 * dies funktioniert nur dann, wenn dieser vorher bereits hinzugefuegt wurde<br>
	 * @param c der alte Calculator
	 * @throws java.rmi.RemoteException
	 */
	public void remove(Calculator c) throws java.rmi.RemoteException;
}
