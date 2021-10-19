package com.clickhouse.client.data;

import java.util.List;

import com.clickhouse.client.ClickHouseColumn;
import com.clickhouse.client.ClickHouseRecord;
import com.clickhouse.client.ClickHouseUtils;
import com.clickhouse.client.ClickHouseValue;

/**
 * Default implementation of {@link com.clickhouse.client.ClickHouseRecord},
 * which is simply a combination of list of columns and array of values.
 */
public class ClickHouseSimpleRecord implements ClickHouseRecord {
    protected final List<ClickHouseColumn> columns;

    private ClickHouseValue[] values;

    /**
     * Creates a record object to wrap given values.
     *
     * @param columns non-null list of columns
     * @param values  non-null array of values
     * @return record
     */
    public static ClickHouseRecord of(List<ClickHouseColumn> columns, ClickHouseValue[] values) {
        if (columns == null || values == null) {
            throw new IllegalArgumentException("Non-null columns and values are required");
        } else if (columns.size() != values.length) {
            throw new IllegalArgumentException(ClickHouseUtils.format(
                    "Mismatched count: we have %d columns but we got %d values", columns.size(), values.length));
        }

        return new ClickHouseSimpleRecord(columns, values);
    }

    protected ClickHouseSimpleRecord(List<ClickHouseColumn> columns, ClickHouseValue[] values) {
        this.columns = columns;
        this.values = values;
    }

    protected ClickHouseValue[] getValues() {
        return values;
    }

    protected void update(ClickHouseValue[] values) {
        this.values = values;
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public ClickHouseValue getValue(int index) {
        return values[index];
    }

    @Override
    public ClickHouseValue getValue(String name) {
        int index = 0;
        for (ClickHouseColumn c : columns) {
            if (c.getColumnName().equalsIgnoreCase(name)) {
                return getValue(index);
            }
            index++;
        }

        throw new IllegalArgumentException(ClickHouseUtils.format("Unable to find column [%s]", name));
    }
}
