package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class CensusLoader {

    public Map<String, CensusDTO> censusMap = new HashMap<>();
    public <E> Map<String, CensusDTO> loadCensusData(CensusAnalyser.Country country,String... csvFilePath) {
        if (country.equals(CensusAnalyser.Country.INDIA))
            return loadCensusData(IndiaCensusCSV.class,csvFilePath);
        else if (country.equals(CensusAnalyser.Country.US))
            return loadCensusData(USCensusCSV.class,csvFilePath);
        else
            throw new CensusAnalyserException("No such Country",CensusAnalyserException.ExceptionType.NO_SUCH_COUNTRY);
    }

    public <E> Map<String, CensusDTO> loadCensusData(Class<E> censusCSVClass,String... csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));) {
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createBuilder();
            Iterator<E> iterator = icsvBuilder.getCSVFileIterator(reader, censusCSVClass);
            Iterable<E> iterable = () -> iterator;
            if (censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(iterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDTO(censusCSV)));
            } else if (censusCSVClass.getName().equals("censusanalyser.USCensusCSV")) {
                StreamSupport.stream(iterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDTO(censusCSV)));
            }
            if(csvFilePath.length==1) return censusMap;
            this.loadIndianStateCode(censusMap,csvFilePath[1]);
            return censusMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndianStateCode(Map<String, CensusDTO> censusMap, String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createBuilder();
            Iterator<IndiaStateCodeCSV> stateCodeCSVIterator = icsvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable = () -> stateCodeCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(), false)
                    .filter(csvState -> this.censusMap.get(csvState.stateName) != null)
                    .forEach(csvState -> this.censusMap.get(csvState.stateName).stateCode = csvState.stateCode);
            return this.censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }
}