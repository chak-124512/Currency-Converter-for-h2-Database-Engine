/*
 * ExchangeRate2.java
 *
 * Version:
 *    $id: 1.8.0_144
 *
 * Revision:
 *      11/05/2018
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import java.net.URL;

/**
 * This program is used to get the exchange rate for a given currency with respect to USD.
 * 
 * @author      Rahul Chakraborty
 */
public class ExchangeRate {


    /**
     * Return the conversion rate for the specified currency wrt to USD
     *
     * @param    isoCode      of type String
     * @returns  exchangeRate of type float
     */
    public static float getConversionRate(String isoCode) {
        float exRate = 0;
        try {
            if (isoCode.equals("USD")) {
                return 1;
            } else {
                float euroToUsd = 0; // Euro to USD conversion rate since URL contains exchange rates wrt to Euro
                boolean usdExRateFound = false; // To check if USD tag is encountered while parsing XML
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                // this URL contains latest exchange rates of 32 currencies wrt to Euro whereas this program takes default currency as USD
                Document doc = dBuilder.parse(new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml").openStream());
                //optional, but recommended
                //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
                //doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("Cube");
                while (!usdExRateFound) {
                    for (int temp = 0; temp < nList.getLength(); temp++) {
                        Node nNode = nList.item(temp);
                        NamedNodeMap nnm = nNode.getAttributes();
                        if (nnm.getLength() > 0) {
                            if (nnm.item(0).getNodeName().equals("currency")) {
                                if (!usdExRateFound) {
                                    if (nnm.item(0).getNodeValue().equals("USD")) {
                                        euroToUsd = Float.parseFloat(nnm.item(1).getNodeValue());
                                        usdExRateFound = true;
                                    }
                                } else if (nnm.item(0).getNodeValue().equals(isoCode)) {
                                    exRate = Float.parseFloat(nnm.item(1).getNodeValue()) / euroToUsd;
                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exRate;

    }

    /**
     * Main method
     *
     * @param    args[]      of type String
     */
    public static void main(String argv[]) {
        System.out.println(getConversionRate("CAD"));
    }

}