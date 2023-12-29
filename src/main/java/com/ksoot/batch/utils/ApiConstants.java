package com.ksoot.batch.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiConstants {

  public static final String RECORD_CREATED_RESPONSE = """
          {
              "messages": [
                  "Record created successfully"
              ]
          }
          """;

  public static final String RECORD_UPDATED_RESPONSE = """
          {
              "messages": [
                  "Record updated successfully"
              ]
          }
          """;

  public static final String RECORD_DELETED_RESPONSE = """
          {
              "messages": [
                  "Record deleted successfully"
              ]
          }
          """;

  public static final String BAD_REQUEST_EXAMPLE_RESPONSE = """
          {
              "type": "http://localhost:8080/problems/help.html#400",
              "code": "constraint-violations",
              "title": "Bad Request",
              "status": 400,
              "detail": "Constraint violations has happened, please correct the request and try again",
              "instance": "/api/example",
              "method": "POST",
              "timestamp": "2023-09-28T22:33:05.781257+05:30",
              "violations": [
                  {
                      "code": "XYZ-100",
                      "detail": "must not be empty",
                      "propertyPath": "name"
                  },
                  {
                      "code": "XYZ-101",
                      "detail": "must not be null",
                      "propertyPath": "type"
                  }
              ]
          }
          """;
  public static final String NOT_FOUND_EXAMPLE_RESPONSE = """
          {
              "type": "http://localhost:8080/problems/help.html#404",
              "code": "404",
              "title": "Not Found",
              "status": 404,
              "detail": "Requested resource not found",
              "instance": "/vehicles/v1/fuel-types/1",
              "method": "GET",
              "timestamp": "2023-09-28T22:35:56.968474+05:30"
          }
          """;

  public static final String INTERNAL_SERVER_ERROR_EXAMPLE_RESPONSE = """
          {
              "type": "http://localhost:8080/problems/help.html#500",
              "code": "XYZ-123",
              "title": "Internal Server Error",
              "status": 500,
              "detail": "Something has gone wrong, please contact administrator",
              "instance": "/api/example",
              "method": "POST",
              "timestamp": "2023-09-28T22:24:54.886137+05:30"
          }
          """;

  public static final String JOB_SUBMITTED_SUCCESSFULLY_RESPONSE = """
          {
            "messages": [
              "Statement job submitted successfully for Month: 2023-11"
            ]
          }
          """;

  public static final String PROCESSING_EXAMPLE_RESPONSE = """
          {
            "type": "http://localhost:8080/problems/help.html#102",
            "title": "Processing",
            "status": 102,
            "detail": "Job already running for given parameters. Please retry after sometime.",
            "instance": "/v1/statements/job",
            "method": "PUT",
            "timestamp": "2023-12-29T17:15:48.322827+05:30",
            "code": "102"
          }
          """;

  public static final String CONFLICT_EXAMPLE_RESPONSE = """
          {
            "type": "http://localhost:8080/problems/help.html#409",
            "title": "Conflict",
            "status": 409,
            "detail": "Job already completed successfully for given parameters. If you still want to run it again, set `forceRestart` as true.",
            "instance": "/v1/statements/job",
            "method": "PUT",
            "timestamp": "2023-12-29T17:15:48.322827+05:30",
            "code": "409"
          }
          """;
}
