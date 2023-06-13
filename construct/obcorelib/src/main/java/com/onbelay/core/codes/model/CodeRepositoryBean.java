package com.onbelay.core.codes.model;

import com.onbelay.core.codes.repository.CodeRepository;
import com.onbelay.core.codes.snapshot.CodeLabel;
import com.onbelay.core.entity.repository.BaseRepository;
import com.onbelay.core.query.enums.ExpressionOperator;
import com.onbelay.core.query.snapshot.DefinedOrderExpression;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.DefinedWhereExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class CodeRepositoryBean extends BaseRepository<AbstractCodeEntity> implements CodeRepository {

    @Autowired
    private CodeColumnDefinitions codeColumnDefinitions;

    @Override
    public CodeLabel findCode(String entityName, String code) {
        DefinedQuery query = new DefinedQuery(entityName);
        query.getWhereClause().addExpression(
                new DefinedWhereExpression(
                        "code",
                        ExpressionOperator.EQUALS,
                        code));

        List<AbstractCodeEntity> entities = executeDefinedQuery(codeColumnDefinitions, query);
        if (entities.isEmpty())
            return null;
        AbstractCodeEntity codeEntity = entities.get(0);
        return new CodeLabel(
                codeEntity.getCode(),
                codeEntity.getLabel());
    }

    public List<CodeLabel> findCodes(String entityName, String filter) {
        DefinedQuery query = new DefinedQuery(entityName);
        if (filter != null) {
            query.getWhereClause().addExpression(
                    new DefinedWhereExpression("label", ExpressionOperator.LIKE, filter + "%")
            );
        }
        query.getOrderByClause().addOrderExpression(
                new DefinedOrderExpression("label"));

        List<AbstractCodeEntity> codes = executeDefinedQuery(
                codeColumnDefinitions,
                query);

        return codes
                .stream()
                .map(c-> new CodeLabel(c.getCode(), c.getLabel()))
                .collect(Collectors.toList());

    }

}
