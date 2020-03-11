package censusanalyser;

import java.util.Map;

public class USCensusAdapter extends CensusAdapter{

    public Map<String, CensusDTO> censusMap;

    @Override
    public Map<String, CensusDTO> loadCensusData(String... csvFilePath) {
        censusMap = super.loadCensusData(USCensusCSV.class, csvFilePath[0]);
        return censusMap;
    }
}
