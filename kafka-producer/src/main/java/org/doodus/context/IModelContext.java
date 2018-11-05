package org.doodus.context;

import org.supercsv.cellprocessor.ift.CellProcessor;

public interface IModelContext {
    CellProcessor[] getProcessors();
}
