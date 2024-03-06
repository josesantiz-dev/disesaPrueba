package net.giro.comparativa;

import java.util.HashMap;

public class App {
    public static void main(String[] args) throws Exception {
		HashMap<String, String> params = new HashMap<String, String>();
		String[] splitted = null;
		/*String test = "?re=EMISOR&rr=RECEPTOR&tt=TOTAL&id=ID";
		String expresionRegular = "(^[\\w&]{3,4}(\\d{2}((?:0[1-9]|1[0-2])(?:0[1-9]|[1,2]\\d|3[0,1])))([A-Z\\d]{2})([A\\d]))";
		String valor = "IPS08060248X5";
		Pattern pattern = null;
		Matcher matcher = null;
		// ----------------------------------------
		GregorianCalendar gCalendar = null;
		XMLGregorianCalendar date = null;
		DateFormat format = null;
		String FORMATER = "yyyy-MM-dd'T'HH:mm:ss";
		// ----------------------------------------
		List<String> supplierCells = Arrays.asList("A", "AA", "AAA"
				, "A0", "A1", "A12", "A123"
				, "AA0", "AA1", "AA12", "AA123"
				, "AAA0", "AAA1", "AAA12", "AAA123"
				, "1", "12", "123", "1234"
				, "PRODUCTOS");
		boolean match = false;
		// ----------------------------------------
		String rangoItem = "";
		String[] sections = null;
		String[] parts = null;
		String seccion = "";
		String column = "";
		int colIndex = -1;*/
		
    	try {
    		/*Class.forName("java.lang.int");
    		Class.forName("int");
    		System.out.println(int.class);
    		System.out.println(boolean.class);
    		System.out.println(String.class);
    		System.out.println(Object.class);
    		System.out.println(MovimientoReferencia.DEVOLUCION_ALMACEN);
    		System.out.println(MovimientoReferencia.DEVOLUCION_ALMACEN.name());
    		System.out.println(MovimientoReferencia.DEVOLUCION_ALMACEN.toString());
    		System.out.println(MovimientoReferencia.DEVOLUCION_ALMACEN.ordinal());
    		
    		
    		Test test1 = Test.BackOfficeRequisicion;
			System.out.println(Test.BackOfficeCotizacion.val);
			System.out.println(test1.val);
    		
    		rangoItem = "A{$MATERIALES,$CLAVE}:$TOTAL_DE_MATERIALES|A:I|A,B,C,D,E,F|CODIGO,DESCRIPCION,UM,CANTIDAD,PU,IMPORTE";
    		sections = rangoItem.split("\\|");
			seccion = "A{$MATERIALES,$CLAVE}:$TOTAL_DE_MATERIALES";//sections[0]; (^[a-zA-Z]{1,2}$)|
			
    		pattern = Pattern.compile("(^[1-9]+$)|(^[a-zA-Z]{1,2}$)|(^([a-zA-Z]{1,2})([\\\\{]{1})([\\w_$,]*)([\\\\}]{1})([\\\\:]?)([\\w_$]*)$)");
			matcher = pattern.matcher(seccion);
			match = matcher.find();
			System.out.println(seccion + " --> " + match);
			if (matcher.groupCount() > 0) {
				for (int i = 0; i <= matcher.groupCount(); i++)
					System.out.println(" --> g" + i + " : " + matcher.group(i));
			}
			
			matcher = pattern.matcher("A");
			match = matcher.find();
			System.out.println(seccion + " --> " + match);
			if (matcher.groupCount() > 0) {
				for (int i = 0; i <= matcher.groupCount(); i++)
					System.out.println(" --> g" + i + " : " + matcher.group(i));
			}
			
			matcher = pattern.matcher("19");
			match = matcher.find();
			System.out.println(seccion + " --> " + match);
			if (matcher.groupCount() > 0) {
				for (int i = 0; i <= matcher.groupCount(); i++)
					System.out.println(" --> g" + i + " : " + matcher.group(i));
			}

			parts = seccion.split("\\:");
			seccion = parts[0];
			column = seccion.substring(0, seccion.indexOf("{"));
			colIndex = CellReference.convertColStringToIndex(column);
			seccion = seccion.substring(seccion.indexOf("{")).replace("{", "").replace("}", "");
			parts = seccion.split("\\,");
    		
			//pat = Pattern.compile("(^\\d+)(\\s{1}\\-{1}\\s{1}[\\d\\s\\w]+)");
    		pattern = Pattern.compile("(^[a-zA-Z]{1,2})([1-9]*)$");
    		for (String val : supplierCells) {
    			matcher = pattern.matcher(val);
    			match = matcher.find();
				System.out.println(val + " --> " + match);
				if (match && matcher.groupCount() > 0) {
					System.out.println(" --> groupCount " + matcher.groupCount());
					for (int i = 1; i <= matcher.groupCount(); i++)
						System.out.println(" --> g" + i + " : " + matcher.group(i));
				}
    		}
    		
    		double res = (7365222.94/42386214.66);
    		System.out.println(" --> groupCount " + res);
    		System.out.println(" --> groupCount " + Double.parseDouble((new DecimalFormat("0.0000")).format(res)));
    		
    		System.out.println(new Date());
			
    		//System.out.println(new String(Base64.decodeBase64("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4NCjxjZmRpOkNvbXByb2JhbnRlIHhtbG5zOmNmZGk9Imh0dHA6Ly93d3cuc2F0LmdvYi5teC9jZmQvMyIgeG1sbnM6cGFnbzEwPSJodHRwOi8vd3d3LnNhdC5nb2IubXgvUGFnb3MiIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiIENlcnRpZmljYWRvPSJNSUlHY0RDQ0JGaWdBd0lCQWdJVU1EQXdNREV3TURBd01EQTBNRFF6T0RjNE1UUXdEUVlKS29aSWh2Y05BUUVMQlFBd2dnR3lNVGd3TmdZRFZRUUREQzlCTGtNdUlHUmxiQ0JUWlhKMmFXTnBieUJrWlNCQlpHMXBibWx6ZEhKaFkybkRzMjRnVkhKcFluVjBZWEpwWVRFdk1DMEdBMVVFQ2d3bVUyVnlkbWxqYVc4Z1pHVWdRV1J0YVc1cGMzUnlZV05wdzdOdUlGUnlhV0oxZEdGeWFXRXhPREEyQmdOVkJBc01MMEZrYldsdWFYTjBjbUZqYWNPemJpQmtaU0JUWldkMWNtbGtZV1FnWkdVZ2JHRWdTVzVtYjNKdFlXTnB3N051TVI4d0hRWUpLb1pJaHZjTkFRa0JGaEJoWTI5a2MwQnpZWFF1WjI5aUxtMTRNU1l3SkFZRFZRUUpEQjFCZGk0Z1NHbGtZV3huYnlBM055d2dRMjlzTGlCSGRXVnljbVZ5YnpFT01Bd0dBMVVFRVF3Rk1EWXpNREF4Q3pBSkJnTlZCQVlUQWsxWU1Sa3dGd1lEVlFRSURCQkVhWE4wY21sMGJ5QkdaV1JsY21Gc01SUXdFZ1lEVlFRSERBdERkV0YxYUhURHFXMXZZekVWTUJNR0ExVUVMUk1NVTBGVU9UY3dOekF4VGs0ek1WMHdXd1lKS29aSWh2Y05BUWtDREU1U1pYTndiMjV6WVdKc1pUb2dRV1J0YVc1cGMzUnlZV05wdzdOdUlFTmxiblJ5WVd3Z1pHVWdVMlZ5ZG1samFXOXpJRlJ5YVdKMWRHRnlhVzl6SUdGc0lFTnZiblJ5YVdKMWVXVnVkR1V3SGhjTk1UWXhNVEk1TVRZME5EVXhXaGNOTWpBeE1USTVNVFkwTkRVeFdqQ0NBUTh4T3pBNUJnTlZCQU1VTWtSSlUwWFJUeUJKVGxOVVFVeEJRMGxQVGlCWklGTlZUVWxPU1ZOVVVrOGdSVXhGUTFSU1NVTlBJRk5CSUVSRklFTldNVHN3T1FZRFZRUXBGREpFU1ZORjBVOGdTVTVUVkVGTVFVTkpUMDRnV1NCVFZVMUpUa2xUVkZKUElFVk1SVU5VVWtsRFR5QlRRU0JFUlNCRFZqRTdNRGtHQTFVRUNoUXlSRWxUUmRGUElFbE9VMVJCVEVGRFNVOU9JRmtnVTFWTlNVNUpVMVJTVHlCRlRFVkRWRkpKUTA4Z1UwRWdSRVVnUTFZeEpUQWpCZ05WQkMwVEhFUkpVekEyTURjd05GRk1RU0F2SUVGVlZFdzNNVEExTWpaVFRETXhIakFjQmdOVkJBVVRGU0F2SUVGVlZFdzNNVEExTWpaSVUweEhVMDR3T0RFUE1BMEdBMVVFQ3hNR1RFRWdVRUZhTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FROEFNSUlCQ2dLQ0FRRUFtbDhnR2paUnI1RkZaN1Y5SG1NWU9hdHlsOUQwNmg5V01WeEgvUmJjRmZEd2NwdmZkZnhYbHBaUlVKdng0aDg4d2Y4b2orZitHWEc1RVhoWE96S0tVOVFGbWJuTjBiOSszaEpLNkdOM3lWZ3YwN0NrQmx6TjZHUHU1bERoS0JoTlJ0Q1ZrdEZiRTRYL0RsSHNPelI2QnA1MEVvdFNlWVprM21ZT3JhL3c5L0Vxc1VjWC83NkhLOWdYeWZtc3BpSGlnMVlSMXlLWm1zZGtvTjM4QWl6b3NscUczZzIrQTJjWnlKQ09qNVIzSDVuUTRmVkZOQXlsaXpFM1BKK3VvdWgreHBNQ2tIVkFqVUVEUm5aaTRUR0NqcWNDS0psQmtXVSswWE1sUVp5Um9sWDFTcFlCTFkxWFdxdmNzdEJ4UDVYcnJjdTNqOCtMSWhOUitLZFlaR3NFZndJREFRQUJveDB3R3pBTUJnTlZIUk1CQWY4RUFqQUFNQXNHQTFVZER3UUVBd0lHd0RBTkJna3Foa2lHOXcwQkFRc0ZBQU9DQWdFQWMyYzAwcHNtOXlBYTNqZ29STDAxSGxoU2tHTHB5dThGM1J2M0NFZU5HbzBmcEhrNnRJTm1LOGtNbGl0cnVXcDZLa2FNaHUvb3IxaEJJbDh2L1kwanhGb05sOGY0RjZSTmpqT2xRaTlya0hNdkZIWE9XalhKVmExQ29OeTB5VEJqTllLTWs3MUdhVndScE9lT3QyUVFZQ3dmQzhwRFUvY2FZRzZKSWVSR0syZE9qcDFRNkN2Yk9HSE1WU0JJOGttRUR1d1pNRXVHcEl1N2EvNTFxVHZGM2IzWWVSTDg2dkFCL2xEUndUb0NZYU9IYzhzS3JYQURIdGtIZVYyM09GczVSd2VYMzBacmJnYlBlVVdnV3FuSDlabWFtVnk4NFRSV0wzd0pyZWJxYitiaE9Cc3NOMU55ZnVoS3NJangzSE9rK2RTMS83R1ZZUThxcjlpSHcrdFRQOVBjUmF2bU1CM3pIODdqb3hFZjFLWW51ZXEyNGxDVi9UbmNaeUN1TWFweDVMY2lIVG9YalJiUEo3a2Rjakc2aW8yZHlYVXJnd2N5cmdaWk9UbDVpOVBZSUIvYUxaYkJlbk4rK3EzT01rKytVU0dnbC9Dd0xYMkp6VHRsUHdHcXF6SmRxSmE3eVZnMnhRL2RKQ1dLZ3FDcm9DS09yQWF2MUFGY3pMaWRKRWpEMkhRUmt1emZNdDVpYmNoeFhVQnNPWkJEcnA1YU5ac3VXZXBPMzkxcUdJTjRzQnd5UzJpTnhtNWVZa1l5dVFVZGtuOWhNOGlob0c2SlV6d1RNdkFUcjJqNndhblRlMGNreDkyR3BVb25STHlJN1daSCtYVkRJYTNOZURjeWlRU01nMndIMWVHRmtxVk9vSFQzZEd3TkZGVmRDWE13WHkreDRkVnI5SzVjQkZ3PSIgRmVjaGE9IjIwMTgtMDQtMDNUMTU6NTI6MzMiIEZvbGlvPSIxMDAwMDEiIEx1Z2FyRXhwZWRpY2lvbj0iMjM0MjgiIE1vbmVkYT0iWFhYIiBOb0NlcnRpZmljYWRvPSIwMDAwMTAwMDAwMDQwNDM4NzgxNCIgU2VsbG89IlYwcjFNNUJBbytwS0J5NFZWTWxqMEU2NzZUWjEzTk41a0NVb1J0em90bzFqREw0N0NWc2hTNDdRRGFHa1JpbEowcHNtOXI1cWpJUnVEK2VQWHU2ZHY3dnA1ZVEwNFhkL3lZd0ZkdkFRWU9Cckc5c05XcDEwb2ZzZ3JEMzArS3Z5M0V5dVVUaFpGTXNBQXFVWkpJK1JFUjhySXhadUFVTm4rdXdXN2U1YnpvcVBZcjF3NUZmMXFmOXd3UkpkeEdVYUg3SUQyU0NoVVpYWHpnemZ4a1A3Q2w0QVk2cytlc1NOc3dnNXZFMjlsYi9SU0RSTWhqK2FZUms2ZWcrV04vRGNJV1dxSGhiSHdJY1FUdmpNVkVVM1VoRWlQVnBnTFltQjFDcDNRL21zOEtDZCtxOVgxbWZKNE81ZUtYZ0MvazBVWTdYSTBDenovalpJTHIybnpFc2FYUT09IiBTZXJpZT0iQyIgU3ViVG90YWw9IjAiIFRpcG9EZUNvbXByb2JhbnRlPSJQIiBUb3RhbD0iMCIgVmVyc2lvbj0iMy4zIiB4c2k6c2NoZW1hTG9jYXRpb249Imh0dHA6Ly93d3cuc2F0LmdvYi5teC9jZmQvMyAgaHR0cDovL3d3dy5zYXQuZ29iLm14L3NpdGlvX2ludGVybmV0L2NmZC8zL2NmZHYzMy54c2QiPjxjZmRpOkVtaXNvciBOb21icmU9IkRJU0XDkU8gSU5TVEFMQUNJT04gWSBTVU1JTklTVFJPIEVMRUNUUklDTywgU0EuIERFIENWLiIgUmVnaW1lbkZpc2NhbD0iNjAxIiBSZmM9IkRJUzA2MDcwNFFMQSIgLz48Y2ZkaTpSZWNlcHRvciBOb21icmU9IlFVSU5UQSBERUwgR09MRk8gREUgQ09SVEVaIFMuQS4gREUgQy5WLiAiIFJmYz0iUUdDOTQwMTAzNzFBIiBVc29DRkRJPSJQMDEiIC8+PGNmZGk6Q29uY2VwdG9zPjxjZmRpOkNvbmNlcHRvIENhbnRpZGFkPSIxIiBDbGF2ZVByb2RTZXJ2PSI4NDExMTUwNiIgQ2xhdmVVbmlkYWQ9IkFDVCIgRGVzY3JpcGNpb249IlBhZ28iIEltcG9ydGU9IjAiIFZhbG9yVW5pdGFyaW89IjAiIC8+PC9jZmRpOkNvbmNlcHRvcz48Y2ZkaTpDb21wbGVtZW50bz48cGFnbzEwOlBhZ29zIFZlcnNpb249IjEuMCI+PHBhZ28xMDpQYWdvIEZlY2hhUGFnbz0iMjAxOC0wNC0wM1QxNTo1MjozMyIgRm9ybWFEZVBhZ29QPSIwMiIgTW9uZWRhUD0iTVhOIiBNb250bz0iMjU4ODcuNjUiIE51bU9wZXJhY2lvbj0iMTIzNDU2Nzg5MCI+PHBhZ28xMDpEb2N0b1JlbGFjaW9uYWRvIEZvbGlvPSIwMDExMjYiIElkRG9jdW1lbnRvPSJiYTJjN2ZjYi02NjZkLTRlZDktYjk4Yi0zNGVkNDA0YjI5MmYiIEltcFBhZ2Fkbz0iMjU4ODcuNjUiIEltcFNhbGRvQW50PSIyNTg4Ny42NSIgSW1wU2FsZG9JbnNvbHV0bz0iMC4wMCIgTWV0b2RvRGVQYWdvRFI9IlBQRCIgTW9uZWRhRFI9Ik1YTiIgTnVtUGFyY2lhbGlkYWQ9IjEiIFNlcmllPSJTSkMiIC8+PC9wYWdvMTA6UGFnbz48L3BhZ28xMDpQYWdvcz48dGZkOlRpbWJyZUZpc2NhbERpZ2l0YWwgeHNpOnNjaGVtYUxvY2F0aW9uPSJodHRwOi8vd3d3LnNhdC5nb2IubXgvVGltYnJlRmlzY2FsRGlnaXRhbCBodHRwOi8vd3d3LnNhdC5nb2IubXgvc2l0aW9faW50ZXJuZXQvY2ZkL1RpbWJyZUZpc2NhbERpZ2l0YWwvVGltYnJlRmlzY2FsRGlnaXRhbHYxMS54c2QiIFZlcnNpb249IjEuMSIgVVVJRD0iOTZiYmRmY2YtMWI5OC00MWJmLWE1NjItNjE0ZmE1YWUzNmJlIiBGZWNoYVRpbWJyYWRvPSIyMDE4LTA0LTAzVDE2OjUyOjUwIiBSZmNQcm92Q2VydGlmPSJBQUEwMTAxMDFBQUEiIFNlbGxvQ0ZEPSJWMHIxTTVCQW8rcEtCeTRWVk1sajBFNjc2VFoxM05ONWtDVW9SdHpvdG8xakRMNDdDVnNoUzQ3UURhR2tSaWxKMHBzbTlyNXFqSVJ1RCtlUFh1NmR2N3ZwNWVRMDRYZC95WXdGZHZBUVlPQnJHOXNOV3AxMG9mc2dyRDMwK0t2eTNFeXVVVGhaRk1zQUFxVVpKSStSRVI4ckl4WnVBVU5uK3V3VzdlNWJ6b3FQWXIxdzVGZjFxZjl3d1JKZHhHVWFIN0lEMlNDaFVaWFh6Z3pmeGtQN0NsNEFZNnMrZXNTTnN3ZzV2RTI5bGIvUlNEUk1oaithWVJrNmVnK1dOL0RjSVdXcUhoYkh3SWNRVHZqTVZFVTNVaEVpUFZwZ0xZbUIxQ3AzUS9tczhLQ2QrcTlYMW1mSjRPNWVLWGdDL2swVVk3WEkwQ3p6L2paSUxyMm56RXNhWFE9PSIgTm9DZXJ0aWZpY2Fkb1NBVD0iMjAwMDEwMDAwMDAzMDAwMjIzMjMiIFNlbGxvU0FUPSJhWVdqQWVoTlZqL1kzV1Q1N2RnNkExKzNPMDFaN1YxM0s2Q0JldGtlMVBrRFJuRUFXajdSTDB1NmxyTzk1aGx3VnNCWDBPQUFrNHRzSGhrSW1kUFp3eVlwb2tZQlpaRExoNGFJWVNOMGNQMVVwbjU2T3pCRjVsaytCWkxlOUwremFPS1p5aXBzWVp2QVZGYVBoaVBDZVZWVERYZlcxcVYrbi9DbGU2QlM5L3VFMVV1L09hUTZ2TGlacFNIcUVLWTJldXZWS1VYK3BSdzh0ei93VjB4SHhseWRvdENaNXgxNEtXbHZ1SnBNZURVMzBPZFROd2QzZWNtZ285Rjcxd0lycGYwQUdmaHVjTXpjSmRNU0J2QkFLWWxtMmluZk5KTjg0ZWd2YTNJOG9xbEk5QmlCQ25XdEZzL2pCbUg2c3B5OTMzbllrV2t6NWJIdkhPUlY1cVYraVE9PSIgeG1sbnM6dGZkPSJodHRwOi8vd3d3LnNhdC5nb2IubXgvVGltYnJlRmlzY2FsRGlnaXRhbCIgeG1sbnM6eHNpPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYS1pbnN0YW5jZSIgLz48L2NmZGk6Q29tcGxlbWVudG8+PC9jZmRpOkNvbXByb2JhbnRlPg0K")));
    		
			gCalendar = new GregorianCalendar();
			gCalendar.setTime(Calendar.getInstance().getTime());
			format = new SimpleDateFormat(FORMATER);
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(gCalendar.getTime()));
			System.out.println(date);
			FORMATER = "yyyy-MM-dd'T'HH:mm:ss";
			format = new SimpleDateFormat(FORMATER);
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(gCalendar));
			System.out.println(date);
			
    		System.out.println(MovimientoReferencia.DEVOLUCION_ALMACEN);
    		System.out.println(MovimientoReferencia.TRASPASO_BODEGA.name());
    		System.out.println(MovimientoReferencia.TRASPASO_BODEGA.toString());
    		System.out.println(MovimientoReferencia.DEVOLUCION_ALMACEN.ordinal());
    		
    		System.out.println(test.substring(test.indexOf("?re=") + 4, test.indexOf("&rr=")));
    		System.out.println(test.substring(test.indexOf("&rr=") + 4, test.indexOf("&tt=")));
    		System.out.println(test.substring(test.indexOf("&tt=") + 4, test.indexOf("&id=")));
    		System.out.println(test.substring(test.indexOf("&id=") + 4));
    		
    		BasicConfigurator.configure();

			// Validador RFC
    		//###expresionRegular = "(^[\\w&]{3,4}(\\d{2}((?:0[1-9]|1[0-2])(?:0[1-9]|[1,2]\\d|3[0,1])))[\\d\\w]{3})";
    		     expresionRegular = "(^[\\w&]{3,4}(\\d{2}((?:0[1-9]|1[0-2])(?:0[1-9]|[1,2]\\d|3[0,1])))([A-Z\\d]{2})([A\\d]))";
    		//expresionRegular = "/^([A-ZÑ&]{3,4}) ?(?:- ?)?(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[1,2]\\d|3[0,1])) ?(?:- ?)?([A-Z\\d]{2})([A\\d])$";
    		// expresionRegular = "(^[\\w&]{3}\\d{6}[\\d\\w]{3})$";
    		pattern = Pattern.compile(expresionRegular);
    		
    		valor = "DIS060704QLA"; // OK
    		matcher = pattern.matcher(valor);
    		System.out.println("RFC " + valor + " valido : " + (matcher.find()));
			
    		valor = "tirf850218422"; // OK
    		matcher = pattern.matcher(valor);
    		System.out.println("RFC " + valor + " valido : " + (matcher.find()));
			
    		valor = "IPS0806248X5"; // OK
    		matcher = pattern.matcher(valor);
    		System.out.println("RFC " + valor + " valido : " + (matcher.find()));

    		valor = "IPS08060248X5"; // 02
    		matcher = pattern.matcher(valor);
    		System.out.println("RFC " + valor + " valido : " + (matcher.find()));

    		valor = "IPS9906248X5"; // 990624
    		matcher = pattern.matcher(valor);
    		System.out.println("RFC " + valor + " valido : " + (matcher.find()));

    		valor = "IPS0899248X5"; // 089924
    		matcher = pattern.matcher(valor);
    		System.out.println("RFC " + valor + " valido : " + (matcher.find()));

    		valor = "IPS0806998X5"; // 080699
    		matcher = pattern.matcher(valor);
    		System.out.println("RFC " + valor + " valido : " + (matcher.find()));

    		valor = "IPS080624ABC"; // ABC
    		matcher = pattern.matcher(valor);
    		System.out.println("RFC " + valor + " valido : " + (matcher.find()));

    		valor = "IPS080624"; // ?
    		matcher = pattern.matcher(valor);
    		System.out.println("RFC " + valor + " valido : " + (matcher.find()));

    		valor = "XXX010101001"; // ?
    		matcher = pattern.matcher(valor);
    		System.out.println("RFC " + valor + " valido : " + (matcher.find()));
			
			// Persona Fisica
    		valor = "XXXX010101001"; // OK
    		matcher = pattern.matcher(valor);
    		System.out.println("RFC " + valor + " valido : " + (matcher.find()));
			*/
    		
    		// Generamos los parametros de acuerdo a los argumentos
    		for (String p : args) {
    			splitted = p.split("=");
    			params.put(splitted[0], splitted[1]);
    		}

    		// PARAMETROS de ejemplo 
			params.put("pgDriver","org.postgresql.Driver");
			params.put("pgUrl","jdbc:postgresql://localhost:5432/disesa");
			params.put("pgUsuario","apps");
			params.put("pgPassword","apps");
			//params.put("pgUrl","jdbc:postgresql://localhost:5555/disesa");
			params.put("ruta_img","/home/javaz/Developer/sources/air/reportes/imagenes/");
			params.put("idComparativa","10000179");
			//params.put("idObra","10000090");
			params.put("idObra","10000075");
			params.put("idFamilia","0");
			params.put("idRequisicion","0");
			params.put("ruta_salida","/home/javaz/Developer/sources/air/outputs/");
			params.put("salida","/home/javaz/Developer/sources/air/outputs/Comparativa_" + params.get("idComparativa") + "_O-" + params.get("idObra"));
			
    		if (params != null && ! params.isEmpty()) {
    	    	// creamos nuestra instancia y le pasamos los parametros generados 
    	    	Comparativa comp = new Comparativa(params);
    	    	
    	    	// Generamos reporte de comparativa
    	    	comp.generarReporte();
    		}
    	} catch (Exception e) {
    		throw e;
    	}
    }
}
