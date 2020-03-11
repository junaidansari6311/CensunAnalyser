package censusanalyser;

public class CensusAnalyserException extends RuntimeException {

    enum ExceptionType {
        CENSUS_FILE_PROBLEM, NO_CENSUS_DATA, NO_SUCH_COUNTRY, UNABLE_TO_PARSE
    }

    ExceptionType type;

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

    public CensusAnalyserException(String message, ExceptionType type, Throwable cause) {
        super(message, cause);
        this.type = type;
    }
}
