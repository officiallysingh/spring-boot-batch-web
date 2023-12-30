package com.ksoot.batch.controller;

import static com.ksoot.batch.utils.CommonConstants.DEFAULT_PAGE_SIZE;

import com.ksoot.batch.domain.model.Statement;
import com.ksoot.batch.job.StatementJobExecutor;
import com.ksoot.batch.repository.StatementRepository;
import com.ksoot.batch.utils.APIResponse;
import com.ksoot.batch.utils.DateTimeUtils;
import com.ksoot.batch.utils.PaginatedResource;
import com.ksoot.batch.utils.PaginatedResourceAssembler;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatementController implements StatementApi {

  private final StatementJobExecutor statementJobExecutor;

  private final StatementRepository statementRepository;

  @Override
  public ResponseEntity<APIResponse<?>> submitStatementJob(
      final YearMonth month, final List<String> cardNumbers, final boolean forceRestart)
      throws JobInstanceAlreadyCompleteException,
          JobExecutionAlreadyRunningException,
          JobParametersInvalidException,
          JobRestartException {
    final YearMonth statementMonth =
        Objects.nonNull(month) ? month : DateTimeUtils.previousMonthIST();

    log.info(
        "Starting Statement job task with parameters >> month: "
            + statementMonth
            + ", cardNumbers: "
            + (CollectionUtils.isNotEmpty(cardNumbers) ? String.join(",", cardNumbers) : "All")
            + ", forceRestart: "
            + forceRestart);
    this.statementJobExecutor.executeStatementJob(statementMonth, forceRestart, cardNumbers);

    return ResponseEntity.accepted()
        .body(
            APIResponse.newInstance()
                .addMessage("Statement job submitted successfully for Month: " + statementMonth));
  }

  @Override
  public PaginatedResource<Statement> getStatements(
      final YearMonth month, final List<String> cardNumbers, final Pageable pageRequest) {
    return PaginatedResourceAssembler.assemble(
        this.statementRepository.getStatements(month, cardNumbers, pageRequest));
  }
}
