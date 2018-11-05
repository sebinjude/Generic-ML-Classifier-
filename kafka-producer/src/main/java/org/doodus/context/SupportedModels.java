package org.doodus.context;

public enum  SupportedModels {
    MODEL1("model1");

    private final String modelName;
    SupportedModels(final String modelName){
        this.modelName = modelName;
    }
    public SupportedModels getModelName(){
        return this.MODEL1;
    }
}
