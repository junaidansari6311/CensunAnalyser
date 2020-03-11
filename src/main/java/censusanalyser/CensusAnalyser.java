package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    public enum Country{
        INDIA,US;
    }
    private List<CensusDTO> censusList ;
    Map<String, CensusDTO> censusMap;
    Map<SortedField, Comparator<CensusDTO>> sortMap;

    public CensusAnalyser() {
        censusList = new ArrayList<>();
        censusMap = new HashMap<>();
        sortMap = new HashMap<>();
        sortMap.put(SortedField.STATE, Comparator.comparing(census -> census.state));
        sortMap.put(SortedField.POPULATION, Comparator.comparing(census -> census.population));
        sortMap.put(SortedField.TOTAL_AREA, Comparator.comparing(census -> census.totalArea));
        sortMap.put(SortedField.POPULATION_DENSITY, Comparator.comparing(census -> census.populationDensity));
        sortMap.put(SortedField.STATE_CODE, Comparator.comparing(census -> census.stateCode));
    }

    public int loadCensusData(Country country, String... csvFilePath) {
        censusMap = CensusAdapterFactory.getCensusAdapter(country, csvFilePath);
        return censusMap.size();
    }

    public String getStateWiseSortedCensusData(SortedField sortedField) throws CensusAnalyserException{
        censusList = censusMap.values().stream().collect(Collectors.toList());
        if (censusList == null || censusList.size() == 0){
            throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDTO> censusCSVComparator = Comparator.comparing(census -> census.state);
        sort(this.sortMap.get(sortedField).reversed());
        String sortedStateCensus = new Gson().toJson(censusList);
        return sortedStateCensus;
    }

    private void sort(Comparator<CensusDTO> censusCSVComparator) {
        for (int i=0; i<censusList.size(); i++){
            for (int j=0; j<censusList.size()-1-i; j++){
                CensusDTO censusDTO1 = censusList.get(j);
                CensusDTO censusDTO2 = censusList.get(j+1);
                if(censusCSVComparator.compare(censusDTO1, censusDTO2) > 0){
                    censusList.set(j,censusDTO2);
                    censusList.set(j+1,censusDTO1);
                }
            }
        }
    }
}
