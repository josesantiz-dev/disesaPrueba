package net.giro.cargas.documentos.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;

public class Addenda implements Serializable {
	private static final long serialVersionUID = 1L;

	@ElementList(inline=true, required=false)
    protected List<Object> any;
	
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }
}
