package edu.hipsec.xrdtiffoperations.datamodel.mardetector.mar345.base;

import java.util.ArrayList;

public abstract class MAR345OutputSchema {

    /////////// Constants ///////////////////////////////////////////////////////////////////

    protected final int HEADER_BYTES_LENGTH = 4096;

    /////////// Fields //////////////////////////////////////////////////////////////////////

    protected double diameter;
    protected ArrayList<String> extensions;
    protected double pixelSize;

    /////////// Accessors ///////////////////////////////////////////////////////////////////

    public double getDiameter(){
        return this.diameter;
    }

    public ArrayList<String> getExtensions(){
        return this.extensions;
    }

    public double pixelSize(){
        return this.pixelSize;
    }

    /////////// Constructor /////////////////////////////////////////////////////////////////

    public MAR345OutputSchema() {
        this.extensions = new ArrayList<>();
        this.initializeExtensions();
        this.initializeDiameter();
        this.initializePixelSize();
    }

    /////////// Protected Methods ///////////////////////////////////////////////////////////

    protected abstract void initializeDiameter();

    protected abstract void initializeExtensions();

    protected abstract void initializePixelSize();

}
