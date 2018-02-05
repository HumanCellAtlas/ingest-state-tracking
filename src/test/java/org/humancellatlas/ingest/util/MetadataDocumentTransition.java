package org.humancellatlas.ingest.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.humancellatlas.ingest.model.MetadataDocumentReference;
import org.humancellatlas.ingest.model.SubmissionEnvelopeReference;
import org.humancellatlas.ingest.state.MetadataDocumentState;
import org.joda.time.DateTime;

/**
 * for a specified submission envelope, descibes a metadata document state transition at a moment of time
 */
@Data
@AllArgsConstructor
class MetadataDocumentTransition {
    private MetadataDocumentReference metadataDocumentReference;
    private SubmissionEnvelopeReference submissionEnvelopeReference;
    private Long eventTime;
    private DateTime dateTime;
    private MetadataDocumentState targetState;
}