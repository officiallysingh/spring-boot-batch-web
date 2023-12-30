package com.ksoot.batch.repository;

import com.ksoot.batch.domain.model.Statement;
import com.ksoot.batch.utils.DateTimeUtils;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class StatementRepository {

    private final MongoTemplate mongoTemplate;

    public Page<Statement> getStatements(
            final YearMonth month,
            final List<String> cardNumbers,
            final Pageable pageRequest) {
        final Query query = new Query();
        if (Objects.nonNull(month)) {
            OffsetDateTime fromDateTime =
                    month.atDay(1).atStartOfDay().atOffset(DateTimeUtils.ZONE_OFFSET_IST);
            OffsetDateTime tillDateTime =
                    month
                            .atEndOfMonth()
                            .plusDays(1)
                            .atStartOfDay()
                            .atOffset(DateTimeUtils.ZONE_OFFSET_IST);
            query.addCriteria(Criteria.where("transaction_date").gte(fromDateTime).lt(tillDateTime));
        }
        if (CollectionUtils.isNotEmpty(cardNumbers)) {
            query.addCriteria(Criteria.where("card_number")
                    .in(cardNumbers));
        }
        final long totalRecords = this.mongoTemplate.count(query, Statement.class);
        if (totalRecords == 0) {
            return Page.empty();
        } else {
            final Pageable pageable =
                    totalRecords <= pageRequest.getPageSize()
                            ? PageRequest.of(0, pageRequest.getPageSize(), pageRequest.getSort())
                            : pageRequest;
            final List<Statement> feeMovementRecords =
                    this.mongoTemplate.find(query.with(pageable), Statement.class);
            return new PageImpl<>(feeMovementRecords, pageable, totalRecords);
        }
    }
}
