package censusanalyser;

import com.google.gson.Gson;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    private List<IndiaCensusCSV> censusCSVList;

    public int loadIndiaCensusData(String csvFilePath) {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createBuilder();
            censusCSVList = icsvBuilder.getCSVFileList(reader, IndiaCensusCSV.class);
            return censusCSVList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndianStateCode(String csvFilePath) {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICSVBuilder icsvBuilder = CSVBuilderFactory.createBuilder();
            Iterator<IndiaStateCodeCSV> stateCodeCSVIterator = icsvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            return getCount(stateCodeCSVIterator);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    private  <E> int getCount(Iterator<E> iterator){
        Iterable<E> csvIterable = () -> iterator;
        int numOfStateCode = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return numOfStateCode;
    }

    public String getStateWiseSortedCensusData() {
        if (censusCSVList == null || censusCSVList.size() == 0){
            throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.state);
        sort(censusCSVComparator);
        String sortedStateCensus = new Gson().toJson(censusCSVList);
        return sortedStateCensus;
    }

    private void sort(Comparator<IndiaCensusCSV> censusCSVComparator) {
        for (int i=0; i<censusCSVList.size(); i++){
            for(int j=0; j<censusCSVList.size()-1-i; j++){
                IndiaCensusCSV indiaCensusCSV = censusCSVList.get(j);
                IndiaCensusCSV indiaCensusCSV1 = censusCSVList.get(j+1);
                if(censusCSVComparator.compare(indiaCensusCSV, indiaCensusCSV1) > 0){
                    censusCSVList.set(j,indiaCensusCSV1);
                    censusCSVList.set(j+1,indiaCensusCSV);
                }
            }
        }
    }
}
