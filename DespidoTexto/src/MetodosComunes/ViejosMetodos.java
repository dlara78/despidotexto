package MetodosComunes;

import ExtincionContrato.Despido;
import static MetodosComunes.Trabajador.MILISEGS_POR_DIA;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author david
 */
public class ViejosMetodos {
    public static float diasHastaReforma(String fecha) {
        float dias;
        GregorianCalendar reforma = new GregorianCalendar(2012, 1, 12, 0, 0, 0);
        float temp1 = reforma.getTimeInMillis() - ViejosMetodos.convertirFechaInicialStringEnMilis(fecha);
        dias = temp1 / MILISEGS_POR_DIA;
        return dias;
    }

    public static float diasDesdeReforma(String fecha) {
        float dias;
        GregorianCalendar reforma = new GregorianCalendar(2012, 1, 12, 0, 0, 0);
        float temp1 = ViejosMetodos.convertirFechaFinalStringEnMilis(fecha) - reforma.getTimeInMillis();
        dias = temp1 / MILISEGS_POR_DIA;
        return dias;
    }

    public static float convertirFechaInicialStringEnMilis(String fechaString) {

        Date dateFecha = null;
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dateFecha = df.parse(fechaString);
        } catch (ParseException ex) {
            Logger.getLogger(Despido.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar calFechaIntroducida = Calendar.getInstance();
        calFechaIntroducida.setTime(dateFecha);
        float fechaEnMilis = calFechaIntroducida.getTimeInMillis();

        return fechaEnMilis;
    }

    public static float convertirFechaFinalStringEnMilis(String fechaString) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date dateFecha = null;
        try {
            dateFecha = df.parse(fechaString);
        } catch (ParseException ex) {
            Logger.getLogger(Despido.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar calFechaIntroducida = Calendar.getInstance();
        calFechaIntroducida.setTime(dateFecha);
        calFechaIntroducida.add(Calendar.HOUR, 24);  //Esta linea es para que cuente el día completo.
        float fechaEnMilis = calFechaIntroducida.getTimeInMillis();

        return fechaEnMilis;
    }

    public static GregorianCalendar convertirFechaStringAGregorian(String fechaImportada) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        GregorianCalendar fechaGregorian = (GregorianCalendar) GregorianCalendar.getInstance();

        try {
            fechaGregorian.setTime(df.parse(fechaImportada));
        } catch (ParseException ex) {
            Logger.getLogger(Despido.class.getName()).log(Level.SEVERE, null, ex);
        }

        fechaGregorian.roll(Calendar.HOUR, 24);

        return fechaGregorian;
    }

    public static String convertirAFechaBonita(String fechaString) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat dfCompleta = DateFormat.getDateInstance(DateFormat.FULL);

        Date dateFecha = null;
        try {
            dateFecha = df.parse(fechaString);
        } catch (ParseException ex) {
            Logger.getLogger(Despido.class.getName()).log(Level.SEVERE, null, ex);
        }
        Calendar calFechaIntroducida = Calendar.getInstance();
        calFechaIntroducida.setTime(dateFecha);

        String fechaBonita = dfCompleta.format(dateFecha);

        return fechaBonita;
    }

    public static float calcularFloatEntreDosFechasString(String fechaBaja, String fechaAlta) {
        float diferenciaEnMilis = convertirFechaFinalStringEnMilis(fechaBaja) - convertirFechaInicialStringEnMilis(fechaAlta);
        float diferenciaEnDias = diferenciaEnMilis / MILISEGS_POR_DIA;
        return diferenciaEnDias;
    }
    
    public static float calculaDiasImprocedente(String fechaFin, String fechaInicio) {
        //Metodo "en construccion"       
        float diasIndemnizacion = 0;
        Calendar fechaAlta = Calendar.getInstance();
        Calendar fechaBaja = Calendar.getInstance();
        Calendar reforma = Calendar.getInstance();

        return diasIndemnizacion;

    }
}
