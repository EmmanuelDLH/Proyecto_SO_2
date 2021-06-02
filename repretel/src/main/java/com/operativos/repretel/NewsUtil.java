package com.operativos.repretel;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class NewsUtil {
    
    /**
	 * Con este método devuelvo un objeto de la clase Document con el contenido del
	 * HTML de la web que me permitirá parsearlo con los métodos de la librelia JSoup
	 * @param url
	 * @return Documento con el HTML
	 */

	public static Document getHtmlDocument(String url) {

	    Document doc = null;
		try {
		    doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36").timeout(100000).get();
		    } catch (IOException ex) {
			System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
		    }
	    return doc;
	}

    public static String getPropertyUrl(int index) {
		String contain = null;
        if (index == 0) {
            contain = "";
        }
        else if (index == 1){
            contain = "";
        }
        return contain;
    }
}
