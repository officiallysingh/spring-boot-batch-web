package com.ksoot.batch.controller;

import com.ksoot.batch.job.StatementJobExecutor;
import com.ksoot.batch.utils.APIResponse;
import com.ksoot.batch.utils.DateTimeUtils;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatementController implements StatementApi {

  private final StatementJobExecutor statementJobExecutor;

  @Override
  public ResponseEntity<APIResponse<?>> submitStatementJob(
          final YearMonth month, final List<String> cardNumbers, final boolean forceRestart)
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
          JobParametersInvalidException, JobRestartException {
    final YearMonth statementMonth = Objects.nonNull(month) ? month : DateTimeUtils.previousMonthIST();

    log.info(
            "Starting Statement job task with parameters >> month: "
                    + statementMonth
                    + ", cardNumbers: "
                    + (CollectionUtils.isNotEmpty(cardNumbers) ? String.join(",", cardNumbers) : "All")
                    + ", forceRestart: "
                    + forceRestart);
    this.statementJobExecutor.executeStatementJob(
            statementMonth, forceRestart, cardNumbers);

    return ResponseEntity.accepted()
            .body(APIResponse.newInstance().addMessage("Statement job submitted successfully for Month: " + statementMonth));
  }


//  @Override
//  public ResponseEntity<AggregateInterestAccrued> getAggregateInterestAccrued(
//      final String loanAccountNumber,
//      final LocalDate dateFromInterestAccrued,
//      final LocalDate dateTillInterestAccrued) {
//    log.info(
//        "Get Aggregate Interest request received for loan {} from date {}, to date {}",
//        loanAccountNumber,
//        dateFromInterestAccrued,
//        dateTillInterestAccrued);
//    return ResponseEntity.ok(
//        AggregateInterestAccrued.builder()
//            .interestAccrued(
//                accrualService.getAggregateInterestAccrued(
//                    loanAccountNumber, dateFromInterestAccrued, dateTillInterestAccrued))
//            .build());
//  }
}
