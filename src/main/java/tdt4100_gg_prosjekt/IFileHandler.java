package tdt4100_gg_prosjekt;

import java.io.FileNotFoundException;
import java.util.List;

public interface IFileHandler {
    
    public List<List<String>> readFile(String filename, String splitOn) throws FileNotFoundException;
    public void writeToFile(String filename, String write, boolean append) throws FileNotFoundException;
}
