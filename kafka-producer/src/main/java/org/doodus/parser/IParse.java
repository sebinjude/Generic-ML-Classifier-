package org.doodus.parser;
import java.io.InputStreamReader;


public interface IParse {
    void Parse(InputStreamReader csvFile);
    void Stop();
}
