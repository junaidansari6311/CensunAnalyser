package censusanalyser;

public class CensusDTO {

    public String state;
    public int population;
    public double totalArea;
    public double populationDensity;
    public String stateCode;

    public CensusDTO(IndiaCensusCSV censusDTO) {
        this.state = censusDTO.state;
        this.population = censusDTO.population;
        this.totalArea = censusDTO.areaInSqKm;
        this.populationDensity = censusDTO.densityPerSqKm;
    }
    public CensusDTO(USCensusCSV censusCSV) {
        state = censusCSV.state;
        stateCode = censusCSV.stateId;
        population = censusCSV.population;
        totalArea = censusCSV.totalArea;
        populationDensity = censusCSV.populationDensity;
    }
}
