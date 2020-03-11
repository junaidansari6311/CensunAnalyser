package censusanalyser;

import java.util.Map;

public class CensusAdapterFactory {

    public static Map<String, CensusDTO> getCensusAdapter(CensusAnalyser.Country country, String... csvFilePath) {
        if(country.equals(CensusAnalyser.Country.INDIA))
            return new IndiaCensusAdapter().loadCensusData(csvFilePath);
        else if(country.equals(CensusAnalyser.Country.US))
            return new USCensusAdapter().loadCensusData(csvFilePath);
        else
            throw new CensusAnalyserException("No Such Country",CensusAnalyserException.ExceptionType.NO_SUCH_COUNTRY);
    }
}