package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class OpenCSVBuilder<E> implements ICSVBuilder {
    public Iterator<E> getCSVFileIterator(Reader reader, Class csvClass) {
        return getCSVToBean(reader, csvClass).iterator();
    }

    public List<E> getCSVFileList(Reader reader, Class csvClass) {
        return getCSVToBean(reader, csvClass).parse();
    }

    private CsvToBean<E> getCSVToBean(Reader reader, Class csvClass){
        try{
            CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(csvClass);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            CsvToBean<E> csvToBean = csvToBeanBuilder.build();
            return csvToBean;
        } catch (IllegalStateException e) {
            throw new CSVBuilderException(e.getMessage(), CSVBuilderException.ExceptionType.UNABLE_TO_PARSE);
        }
    }
}


