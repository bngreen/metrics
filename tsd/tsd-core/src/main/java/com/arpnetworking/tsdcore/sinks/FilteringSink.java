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
package com.arpnetworking.tsdcore.sinks;

import com.arpnetworking.tsdcore.model.AggregatedData;
import com.google.common.base.Objects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

import net.sf.oval.constraint.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A publisher that wraps another, filters the metrics with regular expressions,
 * and forwards included metrics to the wrapped sink. This  class is thread 
 * safe.
 *
 * @author Ville Koskela (vkoskela at groupon dot com)
 */
public final class FilteringSink extends BaseSink {

    /**
     * {@inheritDoc}
     */
    @Override
    public void recordAggregateData(final List<AggregatedData> data) {
        // This is optimistic that most/all of the data will pass the filters.
        final List<AggregatedData> filteredData = Lists.newArrayListWithCapacity(data.size());
        for (final AggregatedData datum : data) {
            final String metric = datum.getMetric();
            final Boolean cachedResult = _cachedFilterResult.getUnchecked(metric);
            if (cachedResult.booleanValue()) {
                filteredData.add(datum);
            }
        }
        if (!filteredData.isEmpty()) {
            _sink.recordAggregateData(filteredData);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        _sink.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("super", super.toString())
                .add("ExcludeFilters", _excludeFilters)
                .add("IncludeFilters", _includeFilters)
                .add("Sink", _sink)
                .toString();
    }

    private boolean includeMetric(final String metric) {
        for (final Pattern includeFilter : _includeFilters) {
            if (includeFilter.matcher(metric).matches()) {
                return true;
            }
        }
        for (final Pattern excludeFilter : _excludeFilters) {
            if (excludeFilter.matcher(metric).matches()) {
                return false;
            }
        }
        return true;
    }

    private static List<Pattern> compileExpressions(final List<String> expressions) {
        final List<Pattern> patterns = Lists.newArrayListWithExpectedSize(expressions.size());
        for (final String expression : expressions) {
            patterns.add(Pattern.compile(expression));
        }
        return patterns;
    }

    /**
     * Protected constructor.
     * 
     * @param builder Instance of <code>Builder</code>.
     */
    protected FilteringSink(final Builder builder) {
        super(builder);
        _cachedFilterResult = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(new CacheLoader<String, Boolean>() {
                    @Override
                    public Boolean load(final String key) throws Exception {
                        return Boolean.valueOf(includeMetric(key));
                    }
                });
        _excludeFilters = compileExpressions(builder._excludeFilters);
        _includeFilters = compileExpressions(builder._includeFilters);
        _sink = builder._sink;
    }

    private final LoadingCache<String, Boolean> _cachedFilterResult;
    private final List<Pattern> _excludeFilters;
    private final List<Pattern> _includeFilters;
    private final Sink _sink;

    /**
     * Base <code>Builder</code> implementation.
     *
     * @author Ville Koskela (vkoskela at groupon dot com)
     */
    public static final class Builder extends BaseSink.Builder<Builder> {

        /**
         * Public constructor.
         */
        public Builder() {
            super(FilteringSink.class);
        }

        /**
         * Sets exclude filters. Exclude filters are regular expressions matched
         * against metric names. Include filters take precedence over exclude 
         * filters and the default is to include if neither applies. Cannot be 
         * null.
         * 
         * @param value The exclude filters.
         * @return This instance of <code>Builder</code>.
         */
        public Builder setExcludeFilters(final List<String> value) {
            _excludeFilters = value;
            return self();
        }

        /**
         * Sets include filters. Include filters are regular expressions matched
         * against metric names. Include filters take precedence over exclude 
         * filters and the default is to include if neither applies. Cannot be 
         * null.
         * 
         * @param value The include filters.
         * @return This instance of <code>Builder</code>.
         */
        public Builder setIncludeFilters(final List<String> value) {
            _includeFilters = value;
            return self();
        }

        /**
         * The aggregated data sink to buffer. Cannot be null.
         * 
         * @param value The aggregated data sink to buffer.
         * @return This instance of <code>Builder</code>.
         */
        public Builder setSink(final Sink value) {
            _sink = value;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Builder self() {
            return this;
        }

        @NotNull
        private List<String> _excludeFilters = Collections.emptyList();
        @NotNull
        private List<String> _includeFilters = Collections.emptyList();
        @NotNull
        private Sink _sink;
    }
}
