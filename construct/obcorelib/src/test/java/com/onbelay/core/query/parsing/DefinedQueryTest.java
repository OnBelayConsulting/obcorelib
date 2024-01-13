package com.onbelay.core.query.parsing;

import com.onbelay.core.query.enums.ExpressionOperator;
import com.onbelay.core.query.snapshot.DefinedQuery;
import com.onbelay.core.query.snapshot.DefinedWhereExpression;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefinedQueryTest {

    @Test
    public void testGenerateWithString() {
        DefinedQuery definedQuery = new DefinedQuery("MyAggregate");

        definedQuery.getWhereClause().addExpression(
                new DefinedWhereExpression("name", ExpressionOperator.EQUALS, "my:d:name"));

        String queryText = definedQuery.toString();

        DefinedQueryBuilder builder = new DefinedQueryBuilder("MyAggregate", queryText);
        DefinedQuery recreatedQuery = builder.build();
        assertEquals(queryText, recreatedQuery.toString());
    }

    @Test
    public void testGenerateWithDate() {
        DefinedQuery definedQuery = new DefinedQuery("MyAggregate");

        definedQuery.getWhereClause().addExpression(
                new DefinedWhereExpression("startDate", ExpressionOperator.EQUALS, LocalDate.of(2023, 1, 1)));

        String queryText = definedQuery.toString();

        DefinedQueryBuilder builder = new DefinedQueryBuilder("MyAggregate", queryText);
        DefinedQuery recreatedQuery = builder.build();
        assertEquals(queryText, recreatedQuery.toString());
    }

}
