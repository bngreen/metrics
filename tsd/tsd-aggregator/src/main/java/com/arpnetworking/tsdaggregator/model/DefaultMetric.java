/**
 * Copyright 2014 Groupon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arpnetworking.tsdaggregator.model;

import com.arpnetworking.tsdcore.model.Quantity;
import com.arpnetworking.utility.OvalBuilder;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import net.sf.oval.constraint.NotNull;

import java.util.List;

/**
 * A variable and data to describe the input to a statistic calculator.
 *
 * @author Brandon Arp (barp at groupon dot com)
 */
public final class DefaultMetric implements Metric {

    /**
     * {@inheritDoc}
     */
    @Override
    public MetricType getType() {
        return _type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Quantity> getValues() {
        return _values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Metric)) {
            return false;
        }

        final Metric otherMetric = (Metric) other;
        return Objects.equal(getType(), otherMetric.getType())
                && Objects.equal(getValues(), otherMetric.getValues());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(getType(), getValues());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Type", _type)
                .add("Values", _values)
                .toString();
    }

    private DefaultMetric(final Builder builder) {
        _type = builder._type;
        _values = ImmutableList.copyOf(builder._values);
    }

    private final MetricType _type;
    private final ImmutableList<Quantity> _values;

    /**
     * Implementation of builder pattern for <code>DefaultMetric</code>.
     *
     * @author Ville Koskela (vkoskela at groupon dot com)
     */
    public static final class Builder extends OvalBuilder<Metric> {

        /**
         * Public constructor.
         */
        public Builder() {
            super(DefaultMetric.class);
        }

        /**
         * Public constructor. This constructor can be used for cloning.
         * 
         * @param metric The <code>Metric</code> instance to set all the builder
         * fields from.
         */
        public Builder(final Metric metric) {
            super(DefaultMetric.class);
            setType(metric.getType());
            setValues(metric.getValues());
        }

        /**
         * The values <code>List</code>. Cannot be null.
         *
         * @param value The values <code>List</code>.
         * @return This instance of <code>Builder</code>.
         */
        public Builder setValues(final List<Quantity> value) {
            _values = value;
            return this;
        }

        /**
         * The metric type. Cannot be null.
         *
         * @param value The metric type.
         * @return This instance of <code>Builder</code>.
         */
        public Builder setType(final MetricType value) {
            _type = value;
            return this;
        }

        @NotNull
        private List<Quantity> _values;
        @NotNull
        private MetricType _type;
    }
}
