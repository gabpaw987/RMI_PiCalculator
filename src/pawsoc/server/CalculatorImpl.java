/**
 * 
 */
package pawsoc.server;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Diese Klasse stellt die Berechungseinheiten der Server dar und berechnet <br>
 * Pi mithilfe des Arcus-Tangens verfahrens auf so viele Stellen genau, wie <br>
 * der Client es dem Balancer und somit der Recheneinheit anweist. Diese <br>
 * Klasse ist nicht selbst geschrieben und kommt von der Oracle-Tutorial-Page <br>
 * daher ist das Verfahren in Englisch Dokumentiert. Veraendert wurde nur, <br>
 * dass es UnicastRemoteObject erbt und das Calculator-Interface implementiert <br>
 * damit es in unsere Klassenstruktur passt.
 * 
 * @author Oracle => http://docs.oracle.com/javase/tutorial/rmi/client.html
 * @version 2012-03-01 
 */

public class CalculatorImpl extends UnicastRemoteObject implements Calculator, Serializable {

    private static final long serialVersionUID = 227L;

    /** constants used in pi computation */
    private static final BigDecimal FOUR =
        BigDecimal.valueOf(4);

    /** rounding mode to use during pi computation */
    private static final int roundingMode = 
        BigDecimal.ROUND_HALF_EVEN;

    /** digits of precision after the decimal point */
    @SuppressWarnings("unused")
	private int digits;
    
    /**
     * Construct a task to calculate pi to the specified
     * precision.
     * @throws RemoteException 
     */
    public CalculatorImpl() throws RemoteException {
    	super();
    }

    /**
     * Calculate pi.
     */
    public BigDecimal pi(int digits) {
    	this.digits = digits;
        return computePi(digits);
    }
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
    public static BigDecimal computePi(int digits) {
        int scale = digits + 5;
        BigDecimal arctan1_5 = arctan(5, scale);
        BigDecimal arctan1_239 = arctan(239, scale);
        BigDecimal pi = arctan1_5.multiply(FOUR).subtract(
                                  arctan1_239).multiply(FOUR);
        return pi.setScale(digits, 
                           BigDecimal.ROUND_HALF_UP);
    }
    /**
     * Compute the value, in radians, of the arctangent of 
     * the inverse of the supplied integer to the specified
     * number of digits after the decimal point.  The value
     * is computed using the power series expansion for the
     * arc tangent:
     *
     * arctan(x) = x - (x^3)/3 + (x^5)/5 - (x^7)/7 + 
     *     (x^9)/9 ...
     */   
    public static BigDecimal arctan(int inverseX, 
                                    int scale) 
    {
        BigDecimal result, numer, term;
        BigDecimal invX = BigDecimal.valueOf(inverseX);
        BigDecimal invX2 = 
            BigDecimal.valueOf(inverseX * inverseX);

        numer = BigDecimal.ONE.divide(invX,
                                      scale, roundingMode);

        result = numer;
        int i = 1;
        do {
            numer = 
                numer.divide(invX2, scale, roundingMode);
            int denom = 2 * i + 1;
            term = 
                numer.divide(BigDecimal.valueOf(denom),
                             scale, roundingMode);
            if ((i % 2) != 0) {
                result = result.subtract(term);
            } else {
                result = result.add(term);
            }
            i++;
        } while (term.compareTo(BigDecimal.ZERO) != 0);
        return result;
    }
}
