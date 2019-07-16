package org.nmdp.fhirsubmission.fhir;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.nmdp.fhirsubmission.util.StringEncoding;

/**
 * hml-fhir-app
 */

public class SequenceTransform {

    private static final PropertyNames PROPERTY_NAMES = new PropertyNames();

    public JsonObject getSequence(String specimenId, String centerCode, String sampleId, String dna, String glString) {
        JsonObject sequence = new JsonObject();

        sequence.addProperty(PROPERTY_NAMES.RESOURCE_TYPE_KEY, "Sequence");
//        sequence.add(PROPERTY_NAMES.FHIR_COMMENTS_KEY, getFhirComments());
        sequence.add(PROPERTY_NAMES.TEXT_KEY, getText(glString));
        sequence.addProperty(PROPERTY_NAMES.TYPE_KEY, "dna");
        sequence.addProperty(PROPERTY_NAMES.COORDINATE_SYSTEM, 0);


        sequence.add(PROPERTY_NAMES.SPECIMEN_KEY, getSpecimen(specimenId, centerCode, sampleId));
        sequence.add(PROPERTY_NAMES.REFERENCE_SEQ_KEY, getReferenceSeq(glString));
        sequence.addProperty(PROPERTY_NAMES.OBSERVED_SEQ_KEY, dna);
        sequence.addProperty(PROPERTY_NAMES.ID_KEY, FhirGuid.genereateUrn());

        return sequence;
    }

    private JsonObject getReferenceSeq(String glString){
        JsonObject referenceSeq = new JsonObject();
        JsonObject referenceSeqId = new JsonObject();
        JsonObject _windowStart = new JsonObject();

        _windowStart.addProperty(PROPERTY_NAMES._FHIR_COMMENTS_KEY, "Using 0 for windowStart and windowEnd, not provided in HML");

        referenceSeqId.add(PROPERTY_NAMES.CODING_KEY, getCodings(glString));
        referenceSeqId.addProperty(PROPERTY_NAMES.TEXT_KEY, glString);

        referenceSeq.add(PROPERTY_NAMES.REFERENCE_SEQ_ID_KEY, referenceSeqId);
        referenceSeq.addProperty(PROPERTY_NAMES.WINDOW_START_KEY, 0);
//        referenceSeq.add(PROPERTY_NAMES._WINDOW_START_KEY, _windowStart);
        referenceSeq.addProperty(PROPERTY_NAMES.WINDOW_END_KEY, 0);

        return referenceSeq;
    }

    private JsonArray getCodings(String glString) {
        JsonArray codings = new JsonArray();
        JsonObject obj = new JsonObject();

        obj.addProperty(PROPERTY_NAMES.SYSTEM_KEY, "http://www.ebi.ac.uk/ipd/imgt/hla");
        obj.addProperty(PROPERTY_NAMES.VERSION_KEY, "3.31.0");
        obj.addProperty(PROPERTY_NAMES.CODE_KEY, glString);
        codings.add(obj);

        return codings;
    }

    private JsonObject getSpecimen(String specimenId, String centerCode, String sampleId) {
        JsonObject specimen = new JsonObject();

        specimen.addProperty(PROPERTY_NAMES.REFERENCE_KEY, specimenId);
        specimen.addProperty(PROPERTY_NAMES.DISPLAY_KEY, String.format("buccal swab: %s-%s", centerCode, sampleId));

        return specimen;
    }

    private JsonObject getText(String glString) {
        JsonObject text = new JsonObject();
        JsonObject _status = new JsonObject();
        JsonArray _fhirComments = new JsonArray();

        _fhirComments.add("from reference-sequence.name");
        _status.add(PROPERTY_NAMES._FHIR_COMMENTS_KEY, _fhirComments);
        text.addProperty(PROPERTY_NAMES.STATUS_KEY, "generated");
//        text.add(PROPERTY_NAMES._STATUS_KEY, _status);
        text.addProperty(PROPERTY_NAMES.DIV_KEY, StringEncoding.encodeHtml(
                String.format("<div xmlns=\"http://www.w3.org/1999/xhtml\">%s</div>", glString)));

        return text;
    }

    private JsonArray getFhirComments() {
        JsonArray fhirComments = new JsonArray();
        JsonObject obj = new JsonObject();
        JsonObject request = new JsonObject();
        JsonObject method = new JsonObject();
        JsonObject url = new JsonObject();

        url.addProperty(PROPERTY_NAMES.VALUE_KEY, "Sequence");
        method.addProperty(PROPERTY_NAMES.VALUE_KEY, "POST");
        request.add(PROPERTY_NAMES.METHOD_KEY, method);
        request.add(PROPERTY_NAMES.URL_KEY, url);
        obj.add(PROPERTY_NAMES.REQUEST_KEY, request);
        fhirComments.add(obj);

        return fhirComments;
    }
}
