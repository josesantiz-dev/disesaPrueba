package net.giro.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class DateIterator implements Serializable, Iterator<Date>, Iterable<Date> {
	private static final long serialVersionUID = 6628190665853444926L;
	
	private Calendar start = Calendar.getInstance();
	private Calendar limit = Calendar.getInstance();
	private Calendar current = Calendar.getInstance();

	
	public DateIterator(Date start, Date end) {
		this.start.setTime(start);
		this.limit.setTime(end);
		this.current.setTime(start);
	}
	

	// ----------------------------------------------------------
	// METODOS
	// ----------------------------------------------------------
	
	public void restart() {
		this.current.setTime(this.start.getTime());
	}

	@Override
	public Iterator<Date> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return ! this.current.after(this.limit);
	}

	@Override
	public Date next() {
		this.current.add(Calendar.DATE, 1);
		return this.current.getTime();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Cannot remove");
	}

	// ----------------------------------------------------------
	// PROPIEDADES
	// ----------------------------------------------------------
	
	public Date getStart() {
		return this.start.getTime();
	}

	public Date getLimit() {
		return this.limit.getTime();
	}

	public Date getCurrent() {
		return this.current.getTime();
	}
}
