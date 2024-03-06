package mx.org.banxico.sie.api;

import java.util.List;

public class SeriesResponse {
	private List<Serie> series;

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series = series;
	}
}
