package org.humancellatlas.ingest.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.util.UUID;

/**
 * Javadocs go here!
 *
 * @author tburdett
 * @date 26/11/2017
 */
@Getter
@RequiredArgsConstructor
public class SubmissionEnvelopeReference {
    private final String id;
    private final String uuid;
    private final URI callbackLocation;
}
