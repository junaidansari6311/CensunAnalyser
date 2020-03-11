package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class IndiaCensusAdapter extends CensusAdapter {

    public Map<String, CensusDTO> censusMap;
    List<CensusDTO> censusList;

    @Override
    public Map<String, CensusDTO> loadCensusData(String... csvFilePath) {
        censusMap = super.loadCensusData(IndiaCensusCSV.class, csvFilePath[0]);
        this.loadIndiaStateCode(censusMap, csvFilePath[1]);
        return censusMap;
    }

    public int loadIndiaStateCode(Map<String, CensusDTO> censusMap, String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createBuilder();
            Iterator<IndiaStateCodeCSV> stateCodeCSVIterator = icsvBuilder.
                    getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable = () -> stateCodeCSVIterator;
            StreamSupport.stream(csvIterable.spliterator(), false)
                    .filter(csvState -> this.censusMap.get(csvState.stateName) != null)
                    .forEach(csvState -> this.censusMap.get(csvState.stateName).stateCode = csvState.stateCode);
            censusList = this.censusMap.values().stream().collect(Collectors.toList());
            return this.censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }
}