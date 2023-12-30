package com.ksoot.batch.controller;

import static com.ksoot.batch.utils.ApiConstants.BAD_REQUEST_EXAMPLE_RESPONSE;
import static com.ksoot.batch.utils.ApiConstants.CONFLICT_EXAMPLE_RESPONSE;
import static com.ksoot.batch.utils.ApiConstants.INTERNAL_SERVER_ERROR_EXAMPLE_RESPONSE;
import static com.ksoot.batch.utils.ApiConstants.JOB_SUBMITTED_SUCCESSFULLY_RESPONSE;
import static com.ksoot.batch.utils.ApiConstants.PROCESSING_EXAMPLE_RESPONSE;
import static com.ksoot.batch.utils.CommonConstants.DEFAULT_PAGE_SIZE;

import com.ksoot.batch.domain.model.Statement;
import com.ksoot.batch.utils.APIResponse;
import com.ksoot.batch.utils.Api;
import com.ksoot.batch.utils.PaginatedResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Past;
import java.time.YearMonth;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/v1/statements")
@Tag(name = "Statement", description = "APIs")
public interface StatementApi extends Api {

  @Operation(
      operationId = "submit-statement-job",
      summary = "Submit Statement job execution asynchronously",
      description = "Accepts the Statement job execution request and executes in background",
      tags = {"Statement"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "202",
            description = "Statement job execution request submitted successfully",
            content = @Content(examples = @ExampleObject(JOB_SUBMITTED_SUCCESSFULLY_RESPONSE))),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(examples = @ExampleObject(BAD_REQUEST_EXAMPLE_RESPONSE))),
        @ApiResponse(
            responseCode = "102",
            description = "Job already running",
            content = @Content(examples = @ExampleObject(PROCESSING_EXAMPLE_RESPONSE))),
        @ApiResponse(
            responseCode = "409",
            description = "Job already completed successfully for given parameters",
            content = @Content(examples = @ExampleObject(CONFLICT_EXAMPLE_RESPONSE))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server error",
            content = @Content(examples = @ExampleObject(INTERNAL_SERVER_ERROR_EXAMPLE_RESPONSE)))
      })
  @PutMapping("/job")
  ResponseEntity<APIResponse<?>> submitStatementJob(
      @Parameter(
              description =
                  "Statement Month (IST) in ISO format yyyy-MM. Last Month is taken as default, if not specified.",
              example = "2023-11")
          @RequestParam(value = "month", required = false /*,
              defaultValue = "#{T(com.ksoot.batch.utils.DateTimeUtils).previousMonthIST()}"*/)
          @Past
          final YearMonth month,
      @Parameter(
              description =
                  "List of Credit Card numbers, if Statement job is to be executed only for specific accounts.")
          @RequestParam(required = false)
          final List<String> cardNumbers,
      @Parameter(
              description =
                  "Force restart job in case job was already completed successfully in last run. "
                      + "false is taken as default if not specified")
          @RequestParam(value = "forceRestart", required = false, defaultValue = "false")
          final boolean forceRestart)
      throws JobInstanceAlreadyCompleteException,
          JobExecutionAlreadyRunningException,
          JobParametersInvalidException,
          JobRestartException;

  @Operation(
      operationId = "get-statements",
      summary = "Gets the Statements for given Loan account number in given date range",
      tags = {"Statement"})
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Statements returned successfully"),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(examples = @ExampleObject(BAD_REQUEST_EXAMPLE_RESPONSE))),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server error",
            content = @Content(examples = @ExampleObject(INTERNAL_SERVER_ERROR_EXAMPLE_RESPONSE)))
      })
  @GetMapping
  PaginatedResource<Statement> getStatements(
      @Parameter(description = "Year Month in ISO format yyyy-MM.", example = "2023-11")
          @RequestParam(required = false)
          @Past
          final YearMonth month,
      @Parameter(
              description =
                  "List of Credit Card numbers, if Statement job is to be executed only for specific accounts.")
          @RequestParam(required = false)
          final List<String> cardNumbers,
      @ParameterObject @PageableDefault(size = DEFAULT_PAGE_SIZE) final Pageable pageRequest);
}
