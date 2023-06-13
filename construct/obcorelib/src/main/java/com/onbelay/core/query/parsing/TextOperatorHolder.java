package com.onbelay.core.query.parsing;

import com.onbelay.core.query.enums.TextExpressionOperator;

public class TextOperatorHolder extends ExpressionToken {

    private TextExpressionOperator operator;

    public TextOperatorHolder(TextExpressionOperator operator) {
        this.operator = operator;
    }

    @Override
    public Object getValue() {
        return null;
    }

    public TextExpressionOperator getOperator() {
        return operator;
    }
}
