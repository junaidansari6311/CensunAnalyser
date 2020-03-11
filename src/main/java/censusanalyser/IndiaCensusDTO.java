package censusanalyser;

public class IndiaCensusDTO {

    public String state;
    public int population;
    public int areaInSqKm;
    public int densityPerSqKm;
    public String stateCode;

    public IndiaCensusDTO(IndiaCensusCSV indiaCensusDTO) {
        this.state = indiaCensusDTO.state;
        this.population = indiaCensusDTO.population;
        this.areaInSqKm = indiaCensusDTO.areaInSqKm;
        this.densityPerSqKm = indiaCensusDTO.densityPerSqKm;
    }
}
