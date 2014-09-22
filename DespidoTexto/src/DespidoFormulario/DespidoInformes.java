package DespidoFormulario;

import java.util.GregorianCalendar;
import static DespidoFormulario.DespidoTrabajador.MILISEGS_POR_DIA;

public class DespidoInformes {

    public static String informeCausaObjetiva(
            String tipoDespido, String fechaAlta, String fechaBaja,
            String bCotiz, String diasCotizados) {

        float antiguedadTotal = DespidoMetodosFechas.calcularFloatEntreDosFechasString(fechaBaja, fechaAlta);
        float bCotizDiaria = Float.valueOf(bCotiz) / Float.valueOf(diasCotizados);
        float numDiasIndemnizacion = DespidoMetodosFechas.calculaDiasIndemnObjetiva(antiguedadTotal);
        float importeIndemnizacion = DespidoMetodosFechas.calculaImporteIndemnObjetiva(numDiasIndemnizacion, bCotizDiaria);
        float topeCausaObjetiva = 360f;
        String textoTopeMens = "";
        
        if (numDiasIndemnizacion > topeCausaObjetiva){
            numDiasIndemnizacion = topeCausaObjetiva;            
            textoTopeMens = " (TOPE ALCANZADO)";
        }
        
        String informe = ("\nTipo de despido: " + tipoDespido
                + "\n\nALTA: " + DespidoMetodosFechas.convertirAFechaBonita(fechaAlta)
                + "\nBAJA: " + DespidoMetodosFechas.convertirAFechaBonita(fechaBaja)
                + "\n(Total: " + DespidoMetodosFormatos.darFormatoEsp(antiguedadTotal) + " dias)"
                + "\n\nLa base de cotización diaria es: " + DespidoMetodosFormatos.darFormatoMoneda(bCotizDiaria) + "/dias"
                + "\n\nEl número de días de indemnización es: " + DespidoMetodosFormatos.darFormatoEsp(numDiasIndemnizacion) + textoTopeMens
                + "\nEl importe de la indemnización es: " + DespidoMetodosFormatos.darFormatoMoneda(importeIndemnizacion));

        return informe;
    }

    public static String informeImprocedente(
            String tipoDespido, String fechaAlta, String fechaBaja,
            String bCotiz, String diasCotizados) {

        GregorianCalendar reforma = new GregorianCalendar(2012, 1, 12, 0, 0);

        float antiguedadTotal;
        float antiguedadTotalSumada = 0;
        float reformaMilis = reforma.getTimeInMillis();
        float fAltaMilis = DespidoMetodosFechas.convertirFechaInicialStringEnMilis(fechaAlta);
        float fBajaMilis = DespidoMetodosFechas.convertirFechaFinalStringEnMilis(fechaBaja);
        float bCotizDiaria = Float.valueOf(bCotiz) / Float.valueOf(diasCotizados);
        float numDiasIndemnizacion;
        float numDiasIndemnPreReforma;
        float numDiasIndemnPostReforma;
        float importeIndemnizacion;
        float diasHastaReforma = DespidoMetodosFechas.diasHastaReforma(fechaAlta);
        float diasDesdeReforma = DespidoMetodosFechas.diasDesdeReforma(fechaBaja);
        float antiguedadPREreforma = 0;
        float antiguedadPOSTreforma = 0;
        String textoControl = "";
        String textoTopeMens = "";
        String textoTopeMensPRE = "";
        String textoTopeMensPOST = "";
        float topeImprocedente45 = 1260f;
        float topeImprocedente33 = 720f;

        //El siguiente IF es cuando todo se produce DESPUÉS de la reforma.
        if (fAltaMilis > reformaMilis) {

            antiguedadTotal = DespidoMetodosFechas.calcularFloatEntreDosFechasString(fechaBaja, fechaAlta);
            diasHastaReforma = 0;
            diasDesdeReforma = antiguedadTotal;
            numDiasIndemnizacion = antiguedadTotal * (33f / 365f);
            if (numDiasIndemnizacion > topeImprocedente33) {
                textoTopeMens = " (TOPE ALCANZADO)";
                numDiasIndemnizacion = 720f;
            }
            

            //El siguiente IF es cuando todo se produce ANTES de la reforma.
        } else if (fBajaMilis <= reformaMilis) {

            antiguedadTotal = DespidoMetodosFechas.calcularFloatEntreDosFechasString(fechaBaja, fechaAlta);
            diasHastaReforma = antiguedadTotal;
            diasDesdeReforma = 0;
            numDiasIndemnizacion = antiguedadTotal * (45f / 365f);
            if (numDiasIndemnizacion > topeImprocedente45) {
                textoTopeMens = " (TOPE ALCANZADO)";
                numDiasIndemnizacion = 1260f;
            }

            //El siguiente IF es cuando la reforma afecta al periodo.    
        } else {

            antiguedadPREreforma = (reformaMilis - fAltaMilis) / MILISEGS_POR_DIA;
            antiguedadPOSTreforma = (fBajaMilis - reformaMilis) / MILISEGS_POR_DIA;
            antiguedadTotalSumada = antiguedadPREreforma + antiguedadPOSTreforma;
            antiguedadTotal = (fBajaMilis - fAltaMilis) / MILISEGS_POR_DIA;
            numDiasIndemnPreReforma = antiguedadPREreforma * (45f / 365f);
            numDiasIndemnPostReforma = antiguedadPOSTreforma * (33f / 365f);
            numDiasIndemnizacion = numDiasIndemnPreReforma + numDiasIndemnPostReforma;
            
            if (numDiasIndemnPreReforma > 1260f) {
                numDiasIndemnPreReforma = 1260f;
                textoTopeMensPRE = " (TOPE ALCANZADO)";
                numDiasIndemnizacion = 1260f;
                numDiasIndemnPostReforma = 0;
            }
            
            if (numDiasIndemnPostReforma > 720f) {
                numDiasIndemnPostReforma = 720f;
                textoTopeMensPOST = " (TOPE ALCANZADO)";
            }
            
            textoControl = 
                    "( " + DespidoMetodosFormatos.darFormatoEsp(diasHastaReforma) + textoTopeMensPRE + " antes de la reforma" 
                    + " y " + DespidoMetodosFormatos.darFormatoEsp(diasDesdeReforma) + textoTopeMensPOST + " despúes de la reforma)";
        };

        importeIndemnizacion = numDiasIndemnizacion * bCotizDiaria;

        String informe = ("\nTipo de despido: " + tipoDespido
                + "\n\nALTA: " + DespidoMetodosFechas.convertirAFechaBonita(fechaAlta)
                + "\nBAJA: " + DespidoMetodosFechas.convertirAFechaBonita(fechaBaja)
                + "\n\n Antigüedad Total: " + DespidoMetodosFormatos.darFormatoEsp(antiguedadTotal) + " dias. \n"
                + textoControl
                + "\n\nLa base de cotización diaria es: " + DespidoMetodosFormatos.darFormatoMoneda(bCotizDiaria) + "/dias"
                + "\n\nEl número de días de indemnización es: " + DespidoMetodosFormatos.darFormatoEsp(numDiasIndemnizacion) + textoTopeMens
                + "\nEl importe de la indemnización es: " + DespidoMetodosFormatos.darFormatoMoneda(importeIndemnizacion));

        return informe;
    }

}
